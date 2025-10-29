# Kemana Merchant App

This is the dedicated application for merchants (restaurants, shops, etc.) to manage incoming orders from the Kemana passenger app.

## Core Functionality

This app will replicate the functionality of the web-based Kitchen Display, allowing staff to:

- View new incoming orders in real-time.
- Move orders from "New" to "Preparing".
- Mark orders as "Ready for Pickup", which will trigger the backend to find a nearby driver.

## Tech Stack (Planned)

- Kotlin
- Coroutines & Flow for asynchronous operations.
- Jetpack Compose for the UI, inspired by the Kanban-style layout from the web version.
- Retrofit for API communication.
- A WebSocket or MQTT client to receive real-time order notifications.

*(This project is currently under active development as part of the Kemana modernization initiative.)*