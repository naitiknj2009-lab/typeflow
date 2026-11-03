# TypeFlow implementation status

## Implemented
- Native Kotlin + Jetpack Compose Android app (minimum Android 10 / API 29).
- Offline-only design with no Internet permission and no account flow.
- 126 distinct English lesson prompts in progressive curriculum stages.
- Physical USB OTG and Bluetooth keyboard input.
- Live typing feedback, backspace, pause/restart, timer, WPM, accuracy, and error tracking.
- Finger-position explanations and a colour-coded keyboard guide.
- Four arcade modes, including timed Word Sprint and Number & Symbol Mix.
- Device-local progress, dark appearance, and reset controls.
- GitHub Actions build plus unit tests.

## Verification still required on a physical device
- Test USB OTG and Bluetooth keyboards on Android 10 and newer.
- Check portrait/landscape and smaller phone layouts.
- Verify long-session performance and accessibility with real users.
- Prepare a release-signed APK only after a signing key is securely configured.

## Future improvements
- Add a full lesson-by-lesson finger animation and optional audio feedback.
- Add richer per-lesson history, charts, daily goals, and local backup/restore.
- Expand arcade modes with additional scoring rules and replayable levels.
