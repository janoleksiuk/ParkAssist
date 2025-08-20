import cv2
import numpy as np
import requests
import time
from ultralytics import YOLO

# ThingSpeak config 
API_KEY = "XXXXX"
FIELD_NUMBER = 2

# function for counting cars within frame using YOLO
def count_cars(img):
    # YOLOv8n
    model = YOLO("yolov8n_ncnn_model")  
    
    # Wykonanie detekcji
    results = model(img)
    
    car_count = 0
    for result in results:
        for box in result.boxes:
            cls_id = int(box.cls[0])  
            class_name = model.names[cls_id]  
            if class_name == "car":  
                car_count += 1
    
    return car_count

# function for sending data to ThingSpeak
def send_to_ts(api_key, field, value):
    base_url = "https://api.thingspeak.com/update"

    payload = {
        "api_key": api_key,
        f"field{field}": value
    }

    response = requests.get(base_url, params=payload)

    return response

def main():   
    cap = cv2.VideoCapture(0)

    if not cap.isOpened():
        print("Error: Could not access the camera")
    exit()

    frame_count = 0

    while True:
        _, frame = cap.read()

        # taking time to establish hardware
        frame_count += 1
        if frame_count < 30:
            continue
        
        cars_in_frame = int(count_cars(frame))

        response = send_to_ts(API_KEY, FIELD_NUMBER, int(cars_in_frame))

        if not response.status_code == 200:
            print("failed to send data", response.status_code)
        else:
            print("data sent")

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()

if __name__ == "__main__":
    main()
