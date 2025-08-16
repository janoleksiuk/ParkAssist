import cv2
import numpy as np
import requests
import time
from sklearn.ensemble import RandomForestClassifier
from sklearn.preprocessing import StandardScaler
import pickle
import os

def send_to_ts(api_key, field, value):
    base_url = "https://api.thingspeak.com/update"
    
    payload = {
        "api_key": api_key,
        f"field{field}": value
    }
    
    response = requests.get(base_url, params=payload)
    
    return response

def find_rois_and_avg(frame):
    """ Find rectangular regions of ineterst within the frame
        and calculate their avergae pixel value"""
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    blurred = cv2.GaussianBlur(gray, (5,5), 0)
    _, thresholded = cv2.threshold(blurred, 0, 255, cv2.THRESH_BINARY | cv2.THRESH_OTSU)
    edges = cv2.Canny(thresholded, 50, 150)
    #cv2.imshow("Test", edges)
    
    contours, _ = cv2.findContours(edges, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    
    rois = []
    
    for contour in contours:
        eps = 0.02 * cv2.arcLength(contour, True)
        approx = cv2.approxPolyDP(contour, eps, True)
        
        if len(approx) == 4 and cv2.isContourConvex(approx):
            x, y, w, h = cv2.boundingRect(approx)
            roi = gray[y:y+h, x:x+w]
            avg_gray_value = np.mean(roi)
            
            if w*h > 3000:
                rois.append((x, y, w, h, avg_gray_value))
        
    return rois

def extract_features(roi_image):
    """Extract features from ROI for ML classification"""
    # Resize ROI to standard size for consistency
    roi_resized = cv2.resize(roi_image, (64, 64))
    
    # Feature 1: Mean intensity
    mean_intensity = np.mean(roi_resized)
    
    # Feature 2: Standard deviation of intensity
    std_intensity = np.std(roi_resized)
    
    # Feature 3: Edge density
    edges = cv2.Canny(roi_resized, 50, 150)
    edge_density = np.sum(edges > 0) / (64 * 64)
    
    # Feature 4: Histogram features (first 4 bins)
    hist = cv2.calcHist([roi_resized], [0], None, [256], [0, 256])
    hist_normalized = hist.flatten() / np.sum(hist)
    hist_features = hist_normalized[::64][:4]  # Sample every 64th bin, take first 4
    
    # Feature 5: Local Binary Pattern approximation
    # Simple texture measure using neighboring pixel differences
    kernel = np.array([[-1, -1, -1], [-1, 8, -1], [-1, -1, -1]])
    texture = cv2.filter2D(roi_resized, -1, kernel)
    texture_variance = np.var(texture)
    
    # Feature 6: Gradient magnitude
    grad_x = cv2.Sobel(roi_resized, cv2.CV_64F, 1, 0, ksize=3)
    grad_y = cv2.Sobel(roi_resized, cv2.CV_64F, 0, 1, ksize=3)
    gradient_magnitude = np.mean(np.sqrt(grad_x**2 + grad_y**2))
    
    # Combine all features
    features = np.array([mean_intensity, std_intensity, edge_density, 
                        texture_variance, gradient_magnitude] + list(hist_features))
    
    return features

class MLParkingDetector:
    def __init__(self):
        self.model = RandomForestClassifier(n_estimators=100, random_state=42)
        self.scaler = StandardScaler()
        self.is_trained = False
        self.training_data = []
        self.training_labels = []
        
    def collect_training_data(self, roi_image, label):
        """Collect training data. Label: 1 for occupied, 0 for free"""
        features = extract_features(roi_image)
        self.training_data.append(features)
        self.training_labels.append(label)
        
    def train_model(self):
        """Train the ML model with collected data"""
        if len(self.training_data) < 10:
            print("Not enough training data. Need at least 10 samples.")
            return False
            
        X = np.array(self.training_data)
        y = np.array(self.training_labels)
        
        # Scale features
        X_scaled = self.scaler.fit_transform(X)
        
        # Train model
        self.model.fit(X_scaled, y)
        self.is_trained = True
        
        print(f"Model trained with {len(self.training_data)} samples")
        return True
    
    def predict(self, roi_image):
        """Predict if parking spot is occupied"""
        if not self.is_trained:
            return None
            
        features = extract_features(roi_image)
        features_scaled = self.scaler.transform(features.reshape(1, -1))
        
        prediction = self.model.predict(features_scaled)[0]
        confidence = np.max(self.model.predict_proba(features_scaled))
        
        return prediction, confidence
    
    def save_model(self, filepath):
        """Save trained model to file"""
        if self.is_trained:
            model_data = {
                'model': self.model,
                'scaler': self.scaler,
                'training_data': self.training_data,
                'training_labels': self.training_labels
            }
            with open(filepath, 'wb') as f:
                pickle.dump(model_data, f)
            print(f"Model saved to {filepath}")
    
    def load_model(self, filepath):
        """Load trained model from file"""
        if os.path.exists(filepath):
            with open(filepath, 'rb') as f:
                model_data = pickle.load(f)
            
            self.model = model_data['model']
            self.scaler = model_data['scaler']
            self.training_data = model_data['training_data']
            self.training_labels = model_data['training_labels']
            self.is_trained = True
            print(f"Model loaded from {filepath}")
            return True
        return False

def detect_parking_ml(frame, rois, ml_detector):
    """ML-based parking detection function"""
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    occupied = 0
    free = 0
    
    for i, roi in enumerate(rois):
        x, y, w, h, _ = roi
        roi_image = gray[y:y+h, x:x+w]
        
        if ml_detector.is_trained:
            prediction, confidence = ml_detector.predict(roi_image)
            
            if prediction == 1:  # Occupied
                occupied += 1
                label = f"Occupied ({confidence:.2f})"
                color = (0, 0, 255)  # Red
            else:  # Free
                free += 1
                label = f"Free ({confidence:.2f})"
                color = (0, 255, 0)  # Green
            
            # Draw rectangle and label
            cv2.rectangle(frame, (x, y), (x+w, y+h), color, 2)
            cv2.putText(frame, label, (x, y-10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, color, 2)
        else:
            # Fallback to original method if model not trained
            cv2.rectangle(frame, (x, y), (x+w, y+h), (255, 255, 0), 2)
            cv2.putText(frame, "Training Mode", (x, y-10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 0), 2)
    
    return frame, occupied, free

def map_rois_and_compare(frame, rois, threshold=19):
    """ Map ROis into the frame and compare average pixel value"""
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    occupied = 0
    free = 0
    
    for roi in rois:
        x, y, w, h, avg_gray_value_first = roi
        roi_new = gray[y:y+h, x:x+w]
        taken_flag = 0
        nottaken_flag = 0
        avg_gray_value_second = np.mean(roi_new)
        
        #Compare average values
        difference = abs(avg_gray_value_first - avg_gray_value_second)
        label = "Taken" if difference > threshold else "Not taken"
        
        if label == "Taken":
            taken_flag = 1
        else:
            nottaken_flag = 1
        
        #cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 255, 0), 2)
                
        occupied += taken_flag
        free += nottaken_flag
        
    return frame, occupied, free

# Initialize ML detector
ml_detector = MLParkingDetector()
model_file = "parking_model.pkl"

# Try to load existing model
if not ml_detector.load_model(model_file):
    print("No existing model found. Starting in training mode.")
    print("Press 'o' to mark current parking spots as occupied")
    print("Press 'f' to mark current parking spots as free")
    print("Press 't' to train the model after collecting samples")

cap = cv2.VideoCapture(0)

if not cap.isOpened():
    print("Error: Could not access the camera")
    exit()

rois = None
frame_count = 0
API_KEY = "XXXXX"
FIELD_NUMBER = 2
USE_ML = True  # Flag to switch between ML and original method

while True:
    _, frame = cap.read()
    occ = 0
    free = 0
    
    # taking time to establish hardware
    frame_count += 1
    if frame_count < 20:
        continue
        
    if frame_count == 20:
        rois = find_rois_and_avg(frame)
    
    if rois:
        if USE_ML:
            frame, occ, free = detect_parking_ml(frame, rois, ml_detector)
        else:
            frame, occ, free = map_rois_and_compare(frame, rois)
    
    # Display frame
    cv2.imshow('Parking Detection', frame)
    
    # Handle key presses for training
    key = cv2.waitKey(1) & 0xFF
    
    if key == ord('q'):
        break
    elif key == ord('o') and rois and not ml_detector.is_trained:
        # Mark current ROIs as occupied
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        for roi in rois:
            x, y, w, h, _ = roi
            roi_image = gray[y:y+h, x:x+w]
            ml_detector.collect_training_data(roi_image, 1)
        print(f"Marked {len(rois)} spots as occupied. Total samples: {len(ml_detector.training_data)}")
    elif key == ord('f') and rois and not ml_detector.is_trained:
        # Mark current ROIs as free
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        for roi in rois:
            x, y, w, h, _ = roi
            roi_image = gray[y:y+h, x:x+w]
            ml_detector.collect_training_data(roi_image, 0)
        print(f"Marked {len(rois)} spots as free. Total samples: {len(ml_detector.training_data)}")
    elif key == ord('t') and not ml_detector.is_trained:
        # Train the model
        if ml_detector.train_model():
            ml_detector.save_model(model_file)
            USE_ML = True
    elif key == ord('m'):
        # Toggle between ML and original method
        USE_ML = not USE_ML
        print(f"Switched to {'ML' if USE_ML else 'Original'} method")
    
    #print(occ)
    #print(free)
    
    response = send_to_ts(API_KEY, FIELD_NUMBER, int(free))
    
    if not response.status_code == 200:
        print("failed to send data", response.status_code)
    else:
        print("data sent")

cap.release()
cv2.destroyAllWindows()