# debug_server.py

import os
import socket
import struct
import signal
import sys
from dotenv import load_dotenv

# Load .env file
load_dotenv()

# IP, port 4950
UDP_IP = os.environ["UDP_IP"]
UDP_PORT = 4950

# Create a UDP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.settimeout(1.0)  # Add timeout so Ctrl+C works
sock.bind((UDP_IP, UDP_PORT))


def signal_handler(sig, frame):
    print("\nShutting down server...")
    sock.close()
    sys.exit(0)


# Register signal handler for Ctrl+C
signal.signal(signal.SIGINT, signal_handler)

print(f"Listening for packets on {UDP_IP}:{UDP_PORT}...")
print("Press Ctrl+C to stop")

while True:
    try:
        data, addr = sock.recvfrom(1024)  # buffer size is enough for our packet
        print(f"\nReceived {len(data)} bytes from {addr[0]}:{addr[1]}")
        print(f"Raw hex: {data.hex(' ')}")
        if len(data) == 16:
            hid_pad, circle_pad, cpp_state, interface_buttons = struct.unpack(
                "<IIII", data
            )
            print("Decoded values:")
            print(f"HID Pad:        0x{hid_pad:08X} ({hid_pad})")
            print(f"Circle Pad:     0x{circle_pad:08X}")
            print(f"CPP State:      0x{cpp_state:08X}")
            print(f"Interface Btns: 0x{interface_buttons:08X}")
            # Extract circle pad coordinates
            circle_x = circle_pad & 0xFFF
            circle_y = (circle_pad >> 12) & 0xFFF
            print(f"Circle Pad X: {circle_x} (0x{circle_x:03X})")
            print(f"Circle Pad Y: {circle_y} (0x{circle_y:03X})")
        else:
            print(f"Warning: Expected 16 bytes, got {len(data)} bytes")
    except socket.timeout:
        # Timeout allows Ctrl+C to be processed
        continue
    except Exception as e:
        print(f"Error: {e}")
        break
