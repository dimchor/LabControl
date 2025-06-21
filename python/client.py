import socket

HOST = "192.168.1.110"
PORT = 47001
BUFSIZE = 1024

with socket.socket() as soc:
    soc.connect((HOST, PORT))
    soc.sendall(b"echo")
    data = soc.recv(BUFSIZE)

print("Received", repr(data))
