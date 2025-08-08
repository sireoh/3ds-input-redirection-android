# Folder Information

`client/` - The main android app source code, written in Kotlin. Handles all the communication to the server

`server/` - The debug server for testing purposes, to ensure the input packets are being sent correctly. Written in Python

# Debug Server Initialization

```shell
python -m venv .venv
.venv/Scripts/activate.ps1 # source .venv/Scripts/activate <- for bash
```

# Client App Source Code Folder Structure

```text
app/
├── src/
│   ├── main/
│   │   ├── java/com/client/
│   │   │   ├── data/
│   │   │   │   ├── local/          # Room DB, DataStore (replaces SharedPreferences)
│   │   │   │   ├── remote/         # Network services, UDP client
│   │   │   │   └── repository/     # Repository implementations
│   │   │   ├── di/                 # Hilt modules
│   │   │   ├── domain/
│   │   │   │   ├── model/          # Data classes
│   │   │   │   ├── repository/     # Repository interfaces
│   │   │   │   └── usecase/        # Business logic
│   │   │   ├── ui/                 # (renamed from presentation)
│   │   │   │   ├── components/     # Reusable composables
│   │   │   │   ├── navigation/     # Navigation graphs
│   │   │   │   ├── screen/         # Screen composables
│   │   │   │   ├── theme/          # App theming (colors, typography)
│   │   │   │   └── viewmodel/      # ViewModels
│   │   │   ├── App.kt              # Application class
│   │   │   └── MainActivity.kt     # Single Activity entry point
│   │   ├── res/
│   │   │   ├── drawable/           # Vector assets, icons
│   │   │   ├── font/               # Font files
│   │   │   └── values/             # Colors, strings (minimal with Compose)
│   │   └── AndroidManifest.xml
│   └── test/                       # Unit tests
└── build.gradle
```
