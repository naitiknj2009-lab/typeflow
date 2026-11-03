# TypeFlow

TypeFlow is a native Android touch-typing tutor built with Kotlin and Jetpack Compose.

## What is included
- 126 distinct English typing lessons arranged in progressive stages.
- Beginner home-row training, top-row and bottom-row reaches, full-keyboard drills, numbers, capitals, punctuation, and longer passages.
- Physical USB OTG and Bluetooth keyboard input.
- Live character highlighting, backspace correction, elapsed time, WPM, accuracy, and mistake counts.
- Finger-placement guidance and a colour-coded keyboard map.
- Four arcade modes: Word Sprint (timed), Sentence Flow, Accuracy Quest, and Number & Symbol Mix (timed).
- Local lesson progress and statistics, dark appearance, and reset controls.
- No account, no ads, and no network permission. Lessons and progress work offline.

## Requirements
- Android 10 (API 29) or newer.
- English QWERTY physical keyboard recommended (USB OTG or Bluetooth).

## Build
GitHub Actions runs unit tests and builds a debug APK on every push to main.
Open the Actions tab, select the latest Android APK run, and download the typeflow-debug-apk artifact.

## Notes
This is an offline learning app. WPM and accuracy are practice estimates, not formal certification scores. The debug APK is suitable for testing; a release-signed APK requires a separately managed signing key.
