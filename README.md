Based on the additional files you provided, here is an updated and comprehensive README for "AccommoDate" following the "Roomie" style:

---

# AccommoDate

AccommoDate is a distributed accommodation booking system developed in Java with an Android frontend. It utilizes a distributed architecture to efficiently process and manage booking data. AccommoDate consists of two main components:

1. **Backend**: Handles data processing and system operations.
2. **Frontend**: An Android application for managing and booking accommodations.

AccommoDate supports both manager and customer functionality, accessible through the console for managers and an Android app for customers.

## Features

### Manager
- Add new accommodations for hosting.
- Set availability for managed accommodations.
- View and manage reservations for owned properties.
- Generate reservation reports by area over specified time periods.

### Customer
- Search accommodations using filters (location, date range, price range, rating, capacity).
- View detailed information (photos, amenities, rooms, bathrooms, etc.).
- Make and manage bookings.
- Rate properties (1-5 star ratings).
- Access booking history and account details.

## Compile and Run

1. Go to `backend/src/main/java/com/example/accommodate/config` and update the host configurations.
2. Compile and run the backend files:
   ```bash
   javac Master.java
   javac Worker.java
   javac Reducer.java
   java Master
   java Worker
   java Reducer
   ```

### For the Manager Console
```bash
javac ConsoleApp.java
java ConsoleApp
```

### For the Android App

1. Locate `BackendCommunicator` and update the host for the Master node.
2. Download and run the Android app.

**Note:** You can run multiple workers, but update each configuration correctly for independent operation.

---

This README captures the backend and frontend aspects of the project, emphasizing distributed architecture and key functionalities for managers and customers. Let me know if any further adjustments are needed!
