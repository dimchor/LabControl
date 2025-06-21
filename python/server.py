import socket
import platform
from utilities import (
    shutdown,
    reboot,
    default_interface_mac
)

# constants
HOST = ""
PORT = 47001
BUFSIZE = 1024
# globals
hostname = socket.gethostname()
system = platform.system()
mac = None

def choose(option: str) -> str:
    match option:
        case "echo":   
            return f"{hostname}%{system}%{mac}"
        case "shutdown":
            err = shutdown()
            if err != None:
                return err
            return "Shutting down..."
        case "reboot":
            err = reboot()
            if err != None:
                return err
            return "Rebooting..."
        case "restore":
            return "error: restore is an unsupported operation"
        case _:
            pass
    return "error: unknown option"

def main():
    print("Welcome to LabControl!")

    print("Determining default interface mac...")
    global mac
    mac = default_interface_mac()
    if mac != None:
        print(f"Using MAC {mac}")
    else:
        print(f"Couldn't determine MAC address")
    
    with socket.socket() as soc:
        soc.bind((HOST, PORT))
        while True:
            print("Listening...")
            soc.listen()
            conn, addr = soc.accept()
            with conn:
                print(f"Connected by {addr}")
                try:
                    if data := conn.recv(BUFSIZE):
                        option = data.decode("utf-8")
                        ret = choose(option)
                        print(ret)
                        conn.sendall(bytes(ret, encoding="utf-8"))
                except Exception as e:
                    print(f"[Exception] {e}")
                print(f"Disconnected...")
                    
if __name__ == "__main__":
    main()
