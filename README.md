# Marvel Comics App 🦸‍♂️💥

An Android Native application built with modern Android development practices, demonstrating **Clean Architecture**, **MVVM**, **Jetpack Compose**, **Hilt**, **Retrofit2 + Kotlinx Serialization**, **Firebase Cloud Messaging (FCM)**, and automated **CI/CD with GitHub Actions & Firebase App Distribution**.

---

## 📱 Features

- **Marvel Character Explorer**: Grid layout displaying Marvel characters with responsive cards and official Marvel design system.
- **Real-Time Character Search**: Search characters by name with debounced input processing.
- **Spoke Character Detail View**: Detailed overlay displaying hero biography, statistics, and a horizontal carousel of featured comic books.
- **Dynamic App Variants & Icons**:
  - **Prod Build**: Official full-color Marvel Red logo (`gemini-svg (1)`).
  - **Debug Build**: Blueprint dark wireframe logo (`gemini-svg (2)`) with `.debug` applicationId suffix.
- **Push Notifications**: Integrated Firebase Cloud Messaging (FCM) service for news and content alerts.
- **Offline / Developer Fallback**: Includes built-in fallback dataset when Marvel API credentials are not provided or during server downtime.

---

## 🏗️ Architecture & Tech Stack

The application follows **Clean Architecture** principles separated into three distinct layers:

```
├── domain          # Pure Kotlin: Models, Repository Interfaces, Use Cases
├── data            # DTOs, Mappers, Retrofit API, OkHttp MD5 Auth Interceptor, Repository Implementations
├── presentation    # MVVM: ViewModels, UI State, Jetpack Compose Screens & Components
├── di              # Hilt Modules (NetworkModule, RepositoryModule)
└── notification    # Firebase Messaging Service
```

### Key Libraries & Tools

| Component | Library / Tool |
| :--- | :--- |
| **Language** | Kotlin 2.1.10 |
| **UI** | Jetpack Compose + Material3 |
| **Architecture** | MVVM + Clean Architecture |
| **Dependency Injection** | Hilt (Dagger) |
| **Networking** | Retrofit2 + OkHttp3 + Kotlinx Serialization |
| **Authentication** | MD5 Hash Request Interceptor (`ts + privateKey + publicKey`) |
| **Image Loading** | Coil 3 Compose |
| **Push Notifications** | Firebase Cloud Messaging (FCM) |
| **Testing** | MockK + Kotlinx Coroutines Test + JUnit4 |
| **CI/CD** | GitHub Actions + Firebase App Distribution |

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio** Ladybug (2024.2.1) or newer
- **JDK 17**
- **Android SDK** API 37 (minSdk 29, targetSdk 37)

### Build & Run Locally

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/marvel.git
   cd marvel
   ```

2. **Open in Android Studio**:
   - Open the project directory in Android Studio.
   - Sync Gradle project files (`./gradlew sync`).

3. **Configure Marvel API Credentials (Optional)**:
   - Request a free API key at [developer.marvel.com](https://developer.marvel.com/).
   - Update `PUBLIC_KEY` and `PRIVATE_KEY` in `MarvelAuthInterceptor.kt` or pass them via build environment variables.

4. **Run the App**:
   ```bash
   ./gradlew assembleDebug
   ```

---

## 🧪 Running Tests

Execute the unit test suite across Domain, Data, and Presentation layers:

```bash
./gradlew testDebugUnitTest
```

All tests execute headlessly with MockK and Kotlinx Coroutines Test dispatchers.

---

## 🔄 CI/CD Pipeline (GitHub Actions)

The project includes a complete GitHub Actions pipeline defined in `.github/workflows/android_ci_cd.yml`:

1. **Build & Test**:
   - Compiles code with Java 17.
   - Runs unit tests (`./gradlew testDebugUnitTest`).
   - Builds Debug APK (`./gradlew assembleDebug`).
   - Saves APK artifacts (`marvel-debug-apk`).

2. **Firebase Distribution**:
   - Automatically deploys the debug APK to **Firebase App Distribution** on pushes to `main`/`master`.

### GitHub Secrets Required for CI/CD

Configure these repository secrets in GitHub (**Settings > Secrets and variables > Actions**):

- **`GOOGLE_SERVICES_JSON`**: The contents of your `google-services.json` file.
- **`FIREBASE_SERVICE_ACCOUNT_KEY`**: Service account private key JSON from Firebase Console.
- **`FIREBASE_APP_ID`**: App ID from Firebase Console (`1:xxxxxx:android:xxxxxx`).

---

## 🔒 Security & Privacy

- `google-services.json` is strictly excluded from version control via `.gitignore` and generated dynamically in CI/CD pipeline runs from encrypted GitHub secrets.
- All API network traffic uses HTTPS endpoints (`https://gateway.marvel.com/v1/public/`).

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE). Data and images provided by © Marvel.
