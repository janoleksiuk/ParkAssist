# ParkAssist

IoT device to help drivers save their time spent on searching for free parking places. 

---

## Overview

This project is a prototype portable parking spot recognition system built on a computer vision model. A camera connected to a **Raspberry Pi** microcomputer analyzes video in real-time to detect available spaces, and the results are displayed in a simple mobile application. The system demonstrates real-time integration of hardware, image processing, and a frontend interface.

**It solves the problem of wasting time and frustration caused by circling around a destination in search of free parking spaces**. The conception of the system is presented below:



<img width="1301" height="724" alt="Zrzut ekranu 2025-08-16 131446" src="https://github.com/user-attachments/assets/78710b8d-fde0-42a9-88b2-6fc9337f8ef1" />


---




**Key Features:**
- Low-cost hardware setup with **Raspberry Pi** and a webcam
- 3D-printed enclosure and mount for a portable prototype
- Real-time detection of available parking spots 
- Mobile application to display live video, spot availability, and camera data - deployed on **Android Studio** emulator
- Integration between image analysis and frontend display using **ThinkSpeak**


---


## System Submodules


### Video processing 

Suited for deployment on **Rasberry Pi** microcomputer. It is implemented within scripts:
- `Rpi/detect_places_DEMO.py` - program utilising rectangular areas detection for parking spot classyfing - well-fitted for technology demonstartion purposes, **deployable** version.
- `Rpi/detect_places_ML.py` - program utilising **Random Forest Classifier** - model needs to be trained firstly.

To achieve integration with ThingSpeak cloud communication replace following lines with your input within both programs:
```python
API_KEY = "XXXXX"
```

To achieve **plug-and-play** functionality of the device input those commands on the Rpi:
```bash
crontab -e
@reboot sleep 15 && /usr/bin/python3 /path/to/detect_places_DEMO.py
```

---

### Mobile application


### Hardware
- Standard USB camera (webcam)
- Computer with sufficient processing power for real-time video processing

### Software
- Python 3.7+
- OpenCV 
- NumPy
- dlib
- Windows OS (for `winsound` module)
(see requirements.txt)

### Models
- dlib's 68-point facial landmark predictor model (`shape_predictor_68_face_landmarks.dat`)

## Installation

1. **Clone the repository:**
```bash
git clone https://github.com/your-repo/eye-gaze-exoskeleton
cd eye-assist
```

2. **Install required packages:**
```bash
pip install opencv-python numpy dlib
```

3. **Download the dlib model:**
   - Download `shape_predictor_68_face_landmarks.dat` from dlib's official repository
   - Place it in the `model/` directory

4. **Create necessary directories:**
```bash
mkdir -p utils model logs config
```

5. **Add calibration images:**
   - Place GUI images (`gui1.png`, `gui2.png`, `gui3.png`) in `utils/`
   - Place calibration images (`centerline.png`, `pre1.png`, `1.png`, etc.) in `utils/`

## Usage

### 1. Run the Complete System
```bash
python main.py
```
This will first run the calibration process, then start the main eye-tracking application.

### 2. Calibration Process
The system guides users through a 5-point calibration:
- **Centerline alignment**
- **Left-up gaze point**
- **Right-up gaze point** 
- **Right-down gaze point**
- **Left-down gaze point**
- **Mouth calibration** (for blink threshold)

Press `ESC` to advance through calibration stages.

### 3. Control Interface
The system provides three GUI modes:

#### GUI 1 - Angle Control
- **Top-Left**: Increase current angle position
- **Top-Right**: Decrease current angle position  
- **Bottom-Center**: Switch between angles (1, 2, 3)
- **Bottom-Left**: Switch to GUI 2
- **Bottom-Right**: Stop movement

#### GUI 2 - Velocity Control
- **Top-Left**: Increase velocity (10% increments)
- **Top-Right**: Decrease velocity (10% decrements)
- **Bottom-Left**: Switch to GUI 1
- **Bottom-Center**: Switch to GUI 3
- **Bottom-Right**: Stop movement

#### GUI 3 - Additional Functions
- **Bottom-Left**: Return to GUI 1
- **Bottom-Right**: Stop movement

### 4. Interaction Method
- **Single Blink**: Highlight button (red border)
- **Double Blink**: Execute action (green border)
- **Gaze Direction**: Navigate between buttons

## Project Structure

```
eye-gaze-exoskeleton/
├── main.py      # Main application entry point
├── eye_detection.py         # Eye detection and gaze tracking
├── calibration.py          # Calibration system
├── gui_controller.py       # GUI management and interactions
├── model/
│   └── shape_predictor_68_face_landmarks.dat
├── utils/
│   ├── gui1.png           # GUI interface images
│   ├── gui2.png
│   ├── gui3.png
|   ├── utils.py               # Utility classes and functions
│   ├── centerline.png     # Calibration images
│   ├── pre1.png - pre5.png
│   ├── 1.png - 5.png
│   └── geo_calib.txt      # Calibration data (generated)
```

**Note**: This system is intended for research and rehabilitation purposes. Always ensure proper supervision and safety protocols when used with actual exoskeleton hardware.
