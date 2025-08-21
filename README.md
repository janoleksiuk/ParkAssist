# ParkAssist

IoT device to help drivers save their time spent on searching for free parking places. 

---

## Overview

This project is a prototype portable parking spot recognition system built on a computer vision model. A camera connected to a **Raspberry Pi** microcomputer analyzes video in real-time to detect cars within the parking area of the users choice, and the results are displayed in a simple mobile application. The system demonstrates real-time integration of hardware, image processing, and a frontend interface.

**It solves the problem of wasting time and frustration caused by circling around a destination in search of free parking spaces**. The conception of the system is presented below:



<img width="1301" height="724" alt="Zrzut ekranu 2025-08-16 131446" src="https://github.com/user-attachments/assets/78710b8d-fde0-42a9-88b2-6fc9337f8ef1" />


---

**Key Features:**
- Low-cost hardware setup with **Raspberry Pi** and a webcam
- Real-time detection of cars within frame based on the **YOLOv8 nano** model suited for ARM processor architecture 
- Mobile application prototype to display live video, spot availability, and camera data - deployed on **Android Studio** emulator
- Integration between image analysis and frontend display using **ThinkSpeak** IoT system
- 3D-printed enclosure and mount for a portable prototype

---
## Usecase

The final outcome revolves around a primitive yet efficient and simple approach. Firstly, the end user places the device in a spot where the camera covers the desired parking area. The system is designed with a plug-and-play concept, so the user only needs to connect the device (here, the RPi controller) to a power supply. Then, using the mobile app prototype, they can see the number of cars detected by the device within that frame. Knowing the covered area, they can remotely determine whether the space is mostly occupied by cars and decide to change their parking destination, instead of checking in person and losing time.

**This approach has the significant advantage of working effectively in parking areas where drivers park irregularly or where typical parking spots are not clearly marked.**

---

## System Submodules


### Video processing 

Suited for deployment on **Rasberry Pi** microcomputer. It is implemented within program:
- `Rpi/main.py` - coontinously detects cars and count them within the frame received form camera data stream using light-weight **Yolov8n** model tranformed to **NNCN** format, suited for ARM processor deployment. The results are transmitted to IoT **ThingSpeak** channel.

To achieve integration with **ThingSpeak** cloud communication replace following lines with your input within both programs:
```python
API_KEY = "XXXXX"
FIELD_NUMBER = X
```

To achieve **plug-and-play** functionality of the device input those commands on the Rpi:
```bash
crontab -e
@reboot sleep 15 && /usr/bin/python3 /path/to/main.py
```


---

### Mobile application

Device GUI implemented as the **Android Studio** project, deployable in the Android machine emulator. Created with cooperation of user **Tuniekk**. 

#### How to Run on Emulator

1. **Open the Project**
   - Launch **Android Studio**.
   - Click **File > Open** and select the `Mobile-app` project folder.

2. **Sync Gradle**
   - Allow Android Studio to sync Gradle and download dependencies automatically.

3. **Set Up an Emulator**
   - Go to **Tools > Device Manager**.
   - Create a new Virtual Device (e.g., Pixel 6).
   - Select a system image (preferably the latest stable version, e.g., Android 14).
   - Finish setup.

4. **Build and Run**
   - In the top toolbar, select your emulator from the device dropdown menu.
   - Click the **Run** button (or press **Shift + F10**).
   - Android Studio will build the project and launch it on the emulator.

#### Troubleshooting
- If the emulator is slow, enable **hardware acceleration** (Intel HAXM or Hypervisor Framework on macOS).
- Ensure that your machine supports virtualization and it is enabled in BIOS/UEFI.
- If Gradle sync fails, check your internet connection or update Gradle/SDK from **File > Project Structure**.
  
To achieve integration with ThingSpeak cloud communication replace following lines with your input `Mobile-app/app/src/main/java/com/example/mobileapp/ThingSpeakReader.java`:
```java
 private static final String READ_API_KEY = "X";
 private static final String CHANNEL_ID = "X";
 private static final String FIELD_NUMBER = "X";
```

Example snapshots (deployed on **Pixel 8** emulator):


<img width="640" height="700" alt="473165910_1158533172581844_3396641954276590126_n" src="https://github.com/user-attachments/assets/e77c40c7-01d8-4707-b05f-3a4b8e78c5b1" />

<img width="640" height="700" alt="472785679_1007211271219119_9188851167837751633_n" src="https://github.com/user-attachments/assets/f94a86f6-4259-4b97-bdf2-77540da9ebde" />


---

### Casing

From `Mechanical-design` download `case.stl` and `cover.stl` files. Those are STL models which you can print (feel free to ask for **PRUSA3D** .gcode files). Then assembly accordingly to the picture below, where:
1. `case.stl` part
2. `cover.stl` part
3. M3x10 DIN 912 botls x4
4. Rasberry Pi 5
5. Creative live cam sync 1080p v2

<img width="764" height="475" alt="Zrzut ekranu 2025-08-16 213652" src="https://github.com/user-attachments/assets/1c79e738-eaeb-4feb-a487-65fb4538debe" />

Real-world assembly:

<img width="764" height="764" alt="Zrzut ekranu 2025-08-16 214433" src="https://github.com/user-attachments/assets/93a364b2-f6ae-419b-99d1-3417d2ef1feb" />

---

## Requirements

- Standard USB camera (recommended **Creative live cam sync 1080p v2** for suiting in cover part)
- **Rasberry Pi** microcomputer (recommended Rpi 5)
- **Android Studio**
- **ThingSpeak** channel
- 3D printer (optional)
