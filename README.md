# OpenWPS

OpenWPS is a modern, AI-native, local-first office productivity application designed to become a serious alternative to WPS Office and Microsoft Office.

## Architecture Overview (Phase 1)

- **Android / Kotlin Layer:** Jetpack Compose UI, MVVM, Clean Architecture
- **Native C++ Layer:** Core office engines (Document, Spreadsheet, Presentation, PDF)
- **AI Integration:** Provider-agnostic AI tools and planner architecture

### Module Responsibilities
- `app`: Application shell, Navigation, DI (Hilt), UI flow.
- `core/common`: Shared abstractions (Result, AppError).
- `core/database`: Room/SQLite database entities and DAOs.
- `core/filesystem`: Filesystem access and abstraction layer (OpenWpsFileSystem).
- `core/ui`: Shared compose components and themes.
- `engines/common`: Native engine abstraction boundaries for Kotlin.
- `native/jni`: Thin C++/JNI boundary connecting native engines to Kotlin.

### Kotlin/C++ Boundary
The C++ core is kept platform-independent. The `native/jni` module provides a minimal JNI wrapper that exposes an interface which `engines/common` provides a pure Kotlin abstraction for (`NativeEngine`), allowing UI and domain layers to avoid JNI specifics.

### Database Architecture
Built on `androidx.room`, utilizing a single source of truth (`OpenWpsDatabase`). The initial phase handles application metadata such as `RecentFile` and `FavoriteFile`.

### Filesystem Architecture
The `OpenWpsFileSystem` interface provides an asynchronous API over basic file operations. It acts as an abstraction to shield domain logic from Android's `Context` or `Storage Access Framework`.

### Navigation Architecture
Jetpack Compose Navigation (`androidx.navigation.compose`) manages a robust backstack for destinations like Home, Files, and Settings, easily scalable for future Editor destinations.

## Build Instructions

1. Open in Android Studio or use Gradle wrapper.
2. Ensure you have NDK installed.
3. CI automatically builds the APK on push to main via GitHub Actions.

### Phase 2: Production File Manager + Storage Integration (Complete)
- **Filesystem Abstraction:** `OpenWpsFileSystem` extended with local and SAF providers.
- **Android SAF Integration:** `AndroidFileSystem` implements local storage access.
- **Domain Modeling:** Centralized `FileTypeDetector` and `FileRouter` mapping to open targets.
- **UI:** Compose-based `FileManagerScreen` with Grid/List views, search, and sort functionality.
- **DataStore:** `FileManagerPreferences` integration for view preferences.
- **Database:** Room database updated with recent/favorites file types and locations.

### Phase 3: AI-Ready Document Engine Foundation
- **C++ Engine:** Native engine with cross-platform core and granular model support (`DocumentObjectId`, `DocumentRange`).
- **JNI Boundary:** Exposed capability registry, command execution, and session management.
- **Office API:** Extensible Kotlin API for documents (`DocumentSession`, `DocumentCommand`, `DocumentCapability`, `OperationResult`).
- **Import/Export:** Standardized adapter boundary for third-party libraries (`DocumentImporter`, `DocumentExporter`).
- **UI:** Minimal Compose-based Document Editor to interact with the engine.
