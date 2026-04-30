<div align="center">
  <img src="public/icons.svg" alt="ASHA Saathi Logo" width="120" />
  <h1>ASHA Saathi (Village Health Worker Digital Assistant)</h1>
  <p>Empowering Accredited Social Health Activists (ASHAs) with offline-first digital tools.</p>

  [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
  [![Platform: Android](https://img.shields.io/badge/Platform-Android-green.svg)]()
  [![Backend: Node.js](https://img.shields.io/badge/Backend-Node.js-blue.svg)]()
</div>

## 📖 Overview

**ASHA Saathi** is a comprehensive digital platform designed to empower Accredited Social Health Activists (ASHAs) in rural areas. By streamlining daily tasks, patient management, and communication with supervising medical officers, the platform ensures efficient and error-free healthcare delivery.

The system features an **offline-first mobile application** for health workers in the field and a **web-based dashboard** for doctors to monitor real-time data and alerts.

## ✨ Key Features

- **📶 Offline-First Architecture:** ASHA workers can input patient data and fill out health forms without an active internet connection. Data securely syncs to the central database once connectivity is restored.
- **📊 Doctor's Dashboard:** A centralized, responsive web interface for medical officers to monitor real-time health data, patient vitals, and view high-priority alerts.
- **🔐 Role-Based Access:** Secure and tailored login flows for ASHA workers (Mobile App) and Doctors/Supervisors (Web Dashboard).
- **🚨 Automated Alerts:** Immediate notification to supervisors for critical patient vitals or missed follow-ups.

## 🏗️ System Architecture

1. **AshaSaathiAndroid (`/AshaSaathiAndroid`)**
   - **Type:** Native Android Application (Kotlin, Jetpack Compose)
   - **Local DB:** Room / SQLite for offline caching
   - **Networking:** Retrofit & WorkManager for background syncing

2. **Backend Server (`/backend`)**
   - **Type:** Node.js / Express API
   - **Database:** SQLite (`ashasaathi.db`) for lightweight and fast data persistence.
   - **Role:** Central synchronization hub connecting the mobile app and dashboard.

3. **Web Dashboard (`/dashboard`)**
   - **Type:** Vanilla HTML / CSS / JS
   - **Role:** Web interface served dynamically or statically via the backend.

## 🚀 Getting Started

### Prerequisites
- [Node.js](https://nodejs.org/en/) (v16 or higher)
- [Android Studio](https://developer.android.com/studio) (for mobile app)
- Java JDK 17

### 1. Backend Setup
```bash
cd backend
npm install
npm start
```
The API server will start on `http://localhost:3000`.

### 2. Web Dashboard
The dashboard is served automatically by the backend. Once the backend is running, navigate to:
```
http://localhost:3000/dashboard
```

### 3. Android Application
1. Open Android Studio.
2. Select **Open an existing project** and choose the `AshaSaathiAndroid` folder.
3. Allow Gradle to sync dependencies.
4. Build and run the app on an emulator or physical device.

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) and [Code of Conduct](CODE_OF_CONDUCT.md) for more details.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
