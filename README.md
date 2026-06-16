<div align="center">
  <img src="Assets/APP_ICON.png" alt="Flow Logo" width="140" height="140">
  
  <div align="center">

# 🛡️ PromptShield
</div></div>

**PromptShield** is an Android application that scans text prompts for sensitive or risky information before users share them with AI chatbots like ChatGPT, Gemini, Claude, etc. It detects, highlights, masks, and optionally rewrites prompts using AI — all in real time.
> **Think of it as a firewall for your prompts.**
---
# Download
Latest APK: [latest v1.0.0](https://github.com/ankitsingh0913/promptshield/releases/latest)



---

## 📱 Screenshots

| Scan Screen | History Screen | Settings Screen | History Detail |
|:-----------:|:--------------:|:---------------:|:--------------:|
| ![scan](screenshots/scan.png) | ![history](screenshots/history.png) | ![settings](screenshots/settings.png) | ![detail](screenshots/detail.png) |


---

## 🎯 Problem Statement

Every day, millions of users paste sensitive data into AI chatbots without realizing it:
- API keys and tokens
- Email addresses and phone numbers
- Passwords and credentials
- Proprietary code snippets

**PromptShield** acts as a safety layer between the user and the AI — scanning, scoring, and sanitizing prompts before they leave the device.

---

## ✨ Features

### Core Features
| Feature | Description |
|---------|-------------|
| 🔍 **Real-Time Scanning** | Detects sensitive data (emails, phones, API keys, passwords, code blocks) as you type |
| 📊 **Risk Scoring** | Calculates a 0–100 risk score based on profile-specific weights |
| 🎨 **Highlighted Text** | Color-coded highlighting of detected sensitive items by severity |
| 🔒 **Auto Masking** | Replaces sensitive data with safe placeholders like `[EMAIL]`, `[API_KEY]` |
| 🤖 **AI Rewrite** | Uses Google Gemini AI to intelligently rewrite prompts while preserving meaning |
| 💡 **AI Risk Explanation** | Hybrid local + AI-powered explanation of why your prompt is risky |
| 📋 **Copy to Clipboard** | One-tap copy of AI-rewritten safe prompts |
| 📜 **Scan History** | Persistent history of all scanned prompts with risk scores |
| 📄 **History Detail** | Tap any history item to view full original, cleaned text, and risk breakdown |
| ⚙️ **Work Profiles** | Role-based detection (Developer, Student, Recruiter, Legal) |
| 🌗 **Dark Mode** | Real-time theme switching via Settings toggle |
| 💾 **Auto Save Control** | Toggle automatic saving of scan history |
| 📤 **Share Intent** | Receive shared text from other apps for instant scanning |

### Work Profiles
| Profile | Focus | Detection Priority |
|---------|-------|--------------------|
| 👨‍💻 **Developer** | Code & credentials | API keys, passwords, code blocks |
| 🎓 **Student** | Personal data | Emails, phones, passwords |
| 👔 **Recruiter** | Contact info | Emails, phone numbers |
| ⚖️ **Legal** | Confidential data | Emails, phones, identifiers |

---

## 🏗️ Architecture

### Clean Architecture + MVVM + Multi-Module

```
┌─────────────────────────────────────────────────┐
│                      app                        │
│         (MainActivity, Theme, DI Entry)         │
├──────────┬──────────────┬───────────────────────┤
│          │              │                       │
▼          ▼              ▼                       │
feature-  feature-     feature-                   │
scan      history      settings                   │
│          │              │                       │
└────┬─────┴──────┬───────┘                       │
|     ▼            ▼                              │
|   domain  ◄──  data                             │
|     ▲                                           │
|     │                                           │
detector-engine                                   │
└─────────────────────────────────────────────────┘
```

### Module Responsibilities

| Module | Purpose |
|--------|---------|
| `app` | Entry point, DI setup, theme, navigation host |
| `core-navigation` | Navigation routes and NavHost |
| `core-ui` | Shared UI components (BottomNavBar) |
| `core-common` | Shared utilities and constants |
| `core-designsystem` | Theme, colors, typography |
| `domain` | Business models, repository interfaces |
| `data` | Repository implementations, Room DB, DataStore, Retrofit/Gemini API |
| `detector-engine` | Regex scanner, risk scorer, masking engine |
| `feature-scan` | Scan screen, ViewModel, UI state |
| `feature-history` | History list + detail screen, ViewModel |
| `feature-settings` | Settings screen, profile/theme/auto-save toggles |

---

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| **Language** | Kotlin |
| **UI** | Jetpack Compose (Material 3) |
| **Architecture** | MVVM + Clean Architecture |
| **DI** | Hilt / Dagger |
| **Local DB** | Room |
| **Preferences** | DataStore |
| **Networking** | Retrofit + OkHttp |
| **AI** | Google Gemini API (v1beta) |
| **Navigation** | Jetpack Navigation Compose |
| **Async** | Kotlin Coroutines + Flow |
| **Build** | Gradle (Kotlin DSL) |

---

## 📂 Project Structure

```
PromptShield/
├── app/
│   ├── MainActivity.kt
│   ├── MainViewModel.kt
│   └── PromptShieldApp.kt
│
├── core-navigation/
│   └── navigation/
│       ├── AppNavHost.kt
│       └── Screen.kt
│
├── core-ui/
│   └── components/
│       └── BottomNavBar.kt
│
├── data/
│   ├── ai/
│   │   ├── GeminiApi.kt
│   │   ├── GeminiRequest.kt
│   │   ├── GeminiResponse.kt
│   │   └── PromptTemplates.kt
│   ├── di/
│   │   ├── AppModule.kt
│   │   ├── DatabaseModule.kt
│   │   └── NetworkModule.kt
│   ├── local/
│   │   ├── dao/PromptHistoryDao.kt
│   │   ├── database/PromptShieldDatabase.kt
│   │   └── entity/PromptHistoryEntity.kt
│   ├── preferences/
│   │   └── SettingsDataStore.kt
│   └── repository/
│       ├── AiRewriteRepositoryImpl.kt
│       ├── PromptHistoryRepositoryImpl.kt
│       └── SettingsRepositoryImpl.kt
│
├── detector-engine/
│   ├── masking/MaskingEngine.kt
│   ├── models/
│   │   ├── DetectionResult.kt
│   │   ├── SensitiveType.kt
│   │   └── Severity.kt
│   ├── regex/RegexPatterns.kt
│   ├── scanner/SensitiveDataScanner.kt
│   └── scoring/RiskScorer.kt
│
├── domain/
│   ├── model/
│   │   ├── DetectionProfile.kt
│   │   ├── DetectionProfileProvider.kt
│   │   ├── ProfileConfiguration.kt
│   │   ├── ProfileConfigurationProvider.kt
│   │   ├── PromptHistory.kt
│   │   └── WorkProfile.kt
│   └── repository/
│       ├── AiRewriteRepository.kt
│       ├── PromptHistoryRepository.kt
│       └── SettingsRepository.kt
│
├── feature-scan/
│   └── presentation/
│       ├── screen/ScanScreen.kt
│       ├── state/ScanUiState.kt
│       ├── utils/
│       │   ├── ExplanationGenerator.kt
│       │   └── HighlightTextBuilder.kt
│       └── viewmodel/ScanViewModel.kt
│
├── feature-history/
│   └── presentation/
│       ├── screen/
│       │   ├── HistoryScreen.kt
│       │   └── HistoryDetailScreen.kt
│       ├── state/HistoryDetailUiState.kt
│       └── viewmodel/
│           ├── HistoryViewModel.kt
│           └── HistoryDetailViewModel.kt
│
└── feature-settings/
    └── presentation/
        ├── screen/SettingsScreen.kt
        ├── state/SettingsUiState.kt
        └── viewmodel/SettingsViewModel.kt
```

---

## 🔧 Setup & Installation

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17+
- Min SDK 26 / Target SDK 34
- Google Gemini API Key

### Steps

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/PromptShield.git
cd PromptShield
```

2. **Add your Gemini API Key**

In your `data` module's `local.properties` or `build.gradle.kts`:
```properties
GEMINI_API_KEY=your_api_key_here
```

Make sure `BuildConfig.GEMINI_API_KEY` is generated via:
```kotlin
// data/build.gradle.kts
android {
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField(
            "String",
            "GEMINI_API_KEY",
            "\"${project.findProperty("GEMINI_API_KEY")}\""
        )
    }
}
```

3. **Sync & Build**
```bash
./gradlew build
```

4. **Run on device/emulator**

---

## 🔐 Security Considerations

| Concern | Current State | Recommendation |
|---------|--------------|----------------|
| API Key Storage | BuildConfig (compile-time) | Move to encrypted storage or backend proxy |
| Local Data | Room DB (unencrypted) | Consider SQLCipher for sensitive history |
| Network | HTTPS only | ✅ Already secured |
| Preferences | DataStore (plaintext) | Consider EncryptedSharedPreferences |

---

## 🧪 Detection Engine

### Supported Patterns

| Type | Regex Pattern | Example Match |
|------|--------------|---------------|
| Email | `[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}` | `john@company.com` |
| Phone | `(\+91)?[6-9]\d{9}` | `+919876543210` |
| API Key | `sk-[A-Za-z0-9]{20,}` | `sk-abc123def456ghi789jkl` |
| Password | `(?i)(password\|pwd\|pass)\s*[:=]\s*\S+` | `password=Secret123` |
| Code Block | ` ```[\s\S]*?``` ` | ` ```python\nprint("hi")\n``` ` |

### Risk Scoring

Each detected item contributes to the total score (0–100) based on profile-specific weights:

| Type | Developer | Student | Recruiter | Legal |
|------|-----------|---------|-----------|-------|
| Email | 10 | 20 | 30 | 15 |
| Phone | 5 | 20 | 25 | 15 |
| API Key | 50 | 20 | 10 | 10 |
| Password | 50 | 30 | 20 | 20 |
| Code Block | 40 | 15 | 5 | 5 |

---

## 🤖 AI Integration

### Gemini API
- **Model:** `gemini-2.5-flash`
- **Endpoint:** `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent`

### Prompt Engineering
The AI rewrite prompt is **profile-aware** and includes:
1. Detected sensitive types as context
2. Profile-specific rewriting rules
3. Strict output constraints (no commentary, no hallucination)
4. Replacement guidelines for each data type

### AI Risk Explanation
- **Local (instant):** Rule-based summary of findings count, types, and severity
- **AI (on-demand):** Gemini generates a 2–3 sentence contextual explanation

---



## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is open-source and available under the [MIT License](https://github.com/ankitsingh0913/PromptShield/blob/master/LICENSE).

## 👤 Author

**Ankit Singh**
- GitHub: [@ankitsingh0913](https://github.com/ankitsingh0913)
- LinkedIn: [ANKIT SINGH](https://www.linkedin.com/in/ankit-singh-22466924a/)

---

<p align="center">
  Built with ❤️ using Kotlin & Jetpack Compose
</p>
