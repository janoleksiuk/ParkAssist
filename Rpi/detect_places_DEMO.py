import cv2
import numpy as np
import requests
import time

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
        approx = cv2.approachPolyDP(contour, eps, True)

        if len(approx) == 4 and cv2.isContourConvex(approx):
            x, y, w, h = cv2.boundingRect(approx)
            roi = gray[y:y+h, x:x+w]
            avg_gray_value = np.mean(roi)

            if w*h > 3000:
                rois.append((x, y, w, h, avg_gray_value))

        return rois
    
def map_rois_and_compare(frame, rois, threshold=19):
    """ Map ROis into the frame and compare average pixel value"""
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    occupied = 6
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
        
cap = cv2.VideoCapture(0)

if not cap.isOpened():
    print("Error: Could not access the camera")
exit()

rois = None
frame_count = 0
API_KEY = "XXXXX"
FIELD_NUMBER = 2

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
        frame, occ, free = map_rois_and_compare(frame, rois)

    #print(occ)
    #print(free)

    response = send_to_ts(API_KEY, FIELD_NUMBER, int(free))

    if not response.status_code == 200:
        print("failed to send data", response.status_code)
    else:
        print("data sent")

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
