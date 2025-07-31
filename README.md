# LabControl
LabControl is an Android application that allows you to remotely turn on or off computers in a LAN.

In order for the app to work, the computers need to have a server running in the background. 

# Features
- Manually add computer
- Scan for available computers (IPv4 only)
- Ability to check if a computer is online or offline
- Shutdown or restart an online computer
- Turn on an offline computer (WoL)
- Reset a computer (not implemented)

# Screenshots
Main activity with a list of saved computers:
<img width="1440" height="2880" alt="image" src="https://github.com/user-attachments/assets/819cb95d-ca4c-42ef-8e21-cf3fa6c509d4" />

Scan activity:
<img width="1440" height="2880" alt="image" src="https://github.com/user-attachments/assets/6fcdb0dd-37d7-4370-b863-1ee2215ce14a" />

Add computer activity:
<img width="1440" height="2880" alt="image" src="https://github.com/user-attachments/assets/3a8091b9-837e-4fd8-91d3-7b40e410ef41" />

# Setup
## Android app
Download the latest version of Android Studio for the Android app.
## Server
Download the latest version of Python (along with the requirements):
```
pip install -r requirements.txt
```
