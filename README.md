# Village Health Worker Digital Assistant (ASHA Saathi)

## Overview
ASHA Saathi is a comprehensive digital platform designed to empower Accredited Social Health Activists (ASHAs) by streamlining their daily tasks, patient management, and communication with supervising medical officers. 

The system provides an offline-first mobile application for health workers to use in the field and a web-based dashboard for doctors to monitor patient data and alerts in real-time.

## Project Structure
The repository is divided into three main components:

1. **AshaSaathiAndroid**
   - **Type:** Native Android Application (Kotlin)
   - **Purpose:** Used by ASHA workers in the field. Provides offline-first capabilities for patient data entry, viewing health records, and syncing data when internet connectivity is available.
   
2. **Backend Server (`/backend`)**
   - **Type:** Node.js / Express
   - **Purpose:** The central API server managing data synchronization between the mobile app and the doctor dashboard. It handles patient records, authentication, and database operations.

3. **Web Dashboard (`/dashboard`)**
   - **Type:** HTML / CSS / Vanilla JavaScript
   - **Purpose:** A responsive web interface for doctors and supervisors to monitor real-time health data, view patient alerts, and track the progress of ASHA workers.

## Key Features
- **Offline-First Sync:** Health workers can input data without internet connectivity, which syncs seamlessly with the backend once online.
- **Real-Time Dashboard:** Doctors can view patient vitals, alerts, and general health statistics on a centralized dashboard.
- **Role-Based Access:** Secure login flows tailored for ASHA workers (app) and doctors (web).

## Setup & Run
- **Backend:** Navigate to `/backend`, run `npm install`, then start the server.
- **Dashboard:** Served statically by the backend or can be run locally via a live server.
- **Android App:** Open the `AshaSaathiAndroid` folder in Android Studio and build the project.
