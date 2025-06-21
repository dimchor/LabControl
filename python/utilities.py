from sys import platform
from os import system
import socket
import psutil

def shutdown():
    ret = 0
    if platform == "linux":
        ret = system("(sleep 10; poweroff)&")
    elif platform == "win32":
        ret = system("shutdown /s /t 10")
    else:
        return f"error: unable to poweroff: unsupported platform {platform}"
    if ret:
        return f"error: unable to poweroff: return value {ret}"

def reboot():
    ret = 0
    if platform == "linux":
        ret = system("(sleep 10; reboot)&")
    elif platform == "win32":
        ret = system("shutdown /r /t 10")
    else:
        return f"error: unable to reboot: unsupported platform {platform}"
    if ret:
        return f"error: unable to reboot: return value {ret}"

    import socket
import psutil

def default_interface_mac():
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as soc:
        # Test hostname
        soc.connect(("uniwa.gr", 80))
        local_ip = soc.getsockname()[0]

    # Iterate over IPs
    for interface, addrs in psutil.net_if_addrs().items():
        for addr in addrs:
            if addr.address == local_ip:
                # Get the MAC address for the same interface
                for addr in addrs:
                    if addr.family == psutil.AF_LINK:
                        return addr.address
    return None



