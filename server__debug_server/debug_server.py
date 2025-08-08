import socket
import struct

# Listen on all interfaces, port 4950 (same as 3DS InputRedirection)
UDP_IP = "0.0.0.0"
UDP_PORT = 4950

# Create a UDP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind((UDP_IP, UDP_PORT))

print(f"Listening for packets on {UDP_IP}:{UDP_PORT}...")

while True:
    data, addr = sock.recvfrom(1024)  # buffer size is enough for our packet
    print(f"\nReceived packet from {addr}")
    print(f"Raw bytes: {data.hex()}")

    # Optional: unpack and interpret fields from the 20-byte packet
    if len(data) == 20:
        hid_pad, touchscreen, circle_pad, cpp_state, interface_buttons = struct.unpack(
            "<IIIII", data
        )
        print(f"HID PAD         : 0x{hid_pad:08X}")
        print(f"Touchscreen     : 0x{touchscreen:08X}")
        print(f"Circle Pad      : 0x{circle_pad:08X}")
        print(f"CPP State       : 0x{cpp_state:08X}")
        print(f"Interface Btns  : 0x{interface_buttons:08X}")
    else:
        print("Unexpected packet length!")
