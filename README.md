# KEMANA - Backend Modernization & Action Plan

**NOTICE (OCTOBER 27, 2025):** This project has undergone a significant backend overhaul and modernization. This documentation serves as a comprehensive guide, merging original instructions with modern development workflows.

---

## Architecture & Technology Stack

The modern architecture separates concerns into several layers for scalability and maintainability.

*   **BACKEND (Core Service):**
    *   **Framework:** Spring Boot (Kotlin)
    *   **Purpose:** Manages core logic, user data, jobs, and driver states.
    *   **Database:** MongoDB

*   **MIDDLE-END (Business & Communication Layer):**
    *   **Platform:** Google Cloud Functions, RabbitMQ
    *   **Purpose:** Handles secondary business logic, real-time notifications, and serves as a Backend-for-Frontend (BFF) to simplify client-side responses.

*   **ANDROID (Native Client):**
    *   **Language:** Kotlin
    *   **Architecture:** Modular, MVP
    *   **Purpose:** The application for both Passengers and Drivers.

*   **PWA - HTML5 (Future):**
    *   **Framework:** (To be determined, e.g., React, Vue, or Angular)
    *   **Purpose:** To provide a lightweight, installable, web-based client alternative.

---

## Prerequisites

*   **JDK 8** or higher.
*   **MongoDB:** NoSQL database for all persistent data.
*   **RabbitMQ:** Message broker for real-time communication and task queues.
*   **Google & Firebase Account:** Required for deployment and certain backend services.
*   **Android Studio:** For building and running the Android client applications.

---

## How To Build & Run

### 1. Local Environment (MongoDB & RabbitMQ)

Before starting backend development, ensure your MongoDB and RabbitMQ servers are running.

*   **RabbitMQ:** For local users, visit `http://localhost:15672/` (user: guest, pass: guest) to manage queues. You can also use a cloud-based service like [CloudAMQP](https://www.cloudamqp.com/).

### 2. Backend Development (via Google AI Studio)

This development environment is optimized for rapid iteration using AI tools.

1.  **Open Project:** Open the `source/kemana/backend-side` directory in Google AI Studio.
2.  **Analyze & Modify:** Use Gemini to analyze the code, perform refactoring, add new endpoints, or fix bugs.
3.  **Run & Debug:** Use the integrated terminal to run the Spring Boot application. The standard command is `./mvnw spring-boot:run`.
4.  **Iterate:** Repeat the process of modifying and testing until the desired feature is complete.

### 3. Deployment (via Firebase)

Once the backend is ready, you can deploy it.

1.  **Build Application:** Ensure you have built a production version of your front-end application (e.g., PWA) into a directory like `dist` or `build`.
2.  **Use Deploy Tool:** In Google AI Studio, call the `classic_firebase_hosting_deploy` function.
    *   **Example:** `default_api.classic_firebase_hosting_deploy(path='path/to/your/dist', appType='client')`
    *   This will automatically deploy your static assets to Firebase Hosting.
3.  **Backend Service:** For the Spring Boot backend, deploy it as a service on Google Cloud Run or another server environment.

### 4. Android Client

1.  **Open Project:** Open the `source/kemana/android` directory in Android Studio.
2.  **Configure API Keys:** Update the backend and RabbitMQ URLs in `android/base/src/main/java/com/utsman/kemana/base/KEY.kt`.
3.  **Build & Run:** Run the `driver` and `passenger` applications on an emulator or a physical device.

---

## Action Plan & Next Steps

The backend is now functional for the "create job" -> "notify nearest drivers" flow. The next steps are:

*   **Implement Endpoints:** `/accept`, `/reject`, and job status updates.
*   **Real-time Location Updates:** Build a mechanism for drivers to periodically send their location.
*   **Android Client Adjustments:** Adapt the Android apps to work with the new APIs and data models.

---

## Attribution and License

This project is a modernized and refactored fork, intended for learning and architectural improvement.

*   **Original Source:** [utsmannn/Kemana](https://github.com/utsmannn/Kemana) by Muhammad Utsman.
*   **Current Remake Source:** [DhannyNara/Kemana-Remake](https://github.com/DhannyNara/Kemana-Remake)

This project is licensed under the Apache License 2.0. The original copyright is retained.

```
Copyright 2019 Muhammad Utsman

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```