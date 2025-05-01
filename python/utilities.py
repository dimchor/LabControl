from sys import platform
from os import system

def shutdown():
    ret = 0
    if platform == "linux":
        ret = system("sleep 10; poweroff")
    elif platform == "win32":
        ret = system("shutdown /s /t 10")
    else
        raise Exception(f"unable to poweroff: unsupported platform {platform}")
    if ret:
        raise Exception(f"unable to poweroff: return value {ret}")

def reboot():
    ret = 0
    if platform == "linux":
        ret = system("sleep 10; reboot")
    elif platform == "win32":
        ret = system("shutdown /r /t 10")
    else
        raise Exception(f"unable to reboot: unsupported platform {platform}")
    if ret:
        raise Exception(f"unable to reboot: return value {ret}")
