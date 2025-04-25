import socket

HOST = "127.0.0.1"
PORT = 47001
BUFSIZE = 1024

with socket.socket() as soc:
    soc.connect((HOST, PORT))
    soc.sendall(b"echo")
    data = soc.recv(BUFSIZE)

print("Received", repr(data))
