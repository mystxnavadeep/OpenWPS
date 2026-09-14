# OpenWPS

OpenWPS is a modern, AI-native, local-first office productivity application designed to become a serious alternative to WPS Office and Microsoft Office.

## Architecture Overview

- **Android / Kotlin Layer:** Jetpack Compose UI, MVVM, Clean Architecture
- **Native C++ Layer:** Core office engines (Document, Spreadsheet, Presentation, PDF)
- **AI Integration:** Provider-agnostic AI tools and planner architecture

## Build Instructions

1. Open in Android Studio or use Gradle wrapper.
2. Ensure you have NDK installed.
3. Run `./gradlew assembleDebug` to build the app.
4. Run `./gradlew test` to execute unit tests.

See `AGENTS.md` for complete architectural rules and guidelines.
