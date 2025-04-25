import socket
import platform

HOST = ""
PORT = 47001
BUFSIZE = 1024

def choose(option: str) -> str:
    match option:
        case "echo":
            hostname = socket.gethostname()
            system = platform.system()
            return f"{hostname} - {system}"
        case _:
            pass
    return "unknown"

def main():
    print("Welcome to LabControl!")
    
    with socket.socket() as soc:
        soc.bind((HOST, PORT))
        while True:
            print("Listening...")
            soc.listen()
            conn, addr = soc.accept()
            with conn:
                print(f"Connected by {addr}")
                while data := conn.recv(BUFSIZE):
                    option = data.decode("utf-8")
                    conn.sendall(bytes(choose(option), encoding="utf-8"))

if __name__ == "__main__":
    main()
