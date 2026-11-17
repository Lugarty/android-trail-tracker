# Android Trail Tracker

> An Android app for recording and reviewing GPS trails with Google Maps and local storage.

## Overview

Android Trail Tracker is a Java application that records location points during a trail, stores them locally, and displays saved routes on a map. A trail and its points are stored as related records in SQLite.

## Features

- Record location updates with the Fused Location Provider
- Display recorded routes with the Google Maps SDK
- Store trails and their location points in SQLite
- Review, rename, and delete saved trails
- Show distance, duration, and speed information
- Switch between map display modes

## Tech Stack

- Java and Android
- Google Maps SDK
- Fused Location Provider
- SQLite
- Android Views, Material Components, and Gradle

## Getting Started

1. Install Android Studio and open this repository.
2. Allow Gradle to sync the project.
3. Create a Google Maps API key with the Maps SDK for Android enabled.
4. Replace the `YOUR_GOOGLE_MAPS_API_KEY` placeholder in `app/src/main/AndroidManifest.xml` with your local key. Do not commit a real key.
5. Run the app on an emulator with Google Play services or on a compatible Android device.

The project includes the Gradle wrapper (`gradlew` and `gradlew.bat`).

## Permissions

The app requests fine and coarse location permissions. Trail recording depends on the user granting location access and enabling device location services.

## Data Model

- `Trilha` stores trail metadata such as name, start and end times, speed, and distance.
- `PontoTrilha` stores latitude, longitude, and timestamp values associated with a trail.
- `TrilhaDBHelper` manages the SQLite database and the one-to-many relationship between trails and points.

## Tests

The repository contains Android Studio starter test files under `app/src/test/` and `app/src/androidTest/`. Project-specific automated test coverage is not yet documented.

## Current Limitations

- The current database upgrade path recreates tables instead of applying incremental migrations.
- There is no cloud synchronization or user authentication.
- The Maps API key must be configured locally.
- The package namespace is still the original project namespace, `com.example.atividaden2`.

## Project Status and License

This is a personal academic project, not a commercial product. No license has been specified.

## Author

[Anísio Oliveira (@Lugarty)](https://github.com/Lugarty)