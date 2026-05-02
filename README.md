# trAIn

Personal Android training-plan app for a Pixel 7a using Kotlin, Jetpack Compose, Room, Strava, and Google AI JSON output.

## How To Use

1. Open the project in Android Studio.
2. Connect your phone by usb, and run the app from Android Studio.
3. Open `Settings` in the app.
4. Add Strava client ID and client secret, see Strava setup for further details.
6. Tap `Authorize Strava`.
7. Tap `Sync Strava activities`.
8. Add a Google AI API key, see Google AI Setup for further details.
9. Tap `Load available models`.
10. Select a model. Gemma models are recommended if Gemini models are unavailable for your key.
11. Tap `Test selected model`.
12. Open `Coach`.
13. Enter race distance, target time, race date, and training days.
14. Generate the plan.
15. Open `Plan` to view the generated week-by-week workouts.

## Strava Setup

Be logged in to Strava in your browser first.

1. Go to the Strava API application page: `https://www.strava.com/settings/api`.
2. Create an API application if you do not already have one.
3. Fill out the form with personal/local values.
4. Set `Website` to:

```text
http://localhost
```

5. Set `Authorization Callback Domain` to:

```text
localhost
```

6. Save the Strava API application.
7. Copy `Client ID` from Strava into app `Settings`.
8. Copy `Client Secret` from Strava into app `Settings`.
9. In the app, tap `Authorize Strava`.
10. Approve the requested permissions.
11. Return to the app and tap `Sync Strava activities`.

The app uses this redirect URI:

```text
train://localhost/strava-auth
```

The current authorization scope is:

```text
read,activity:read_all
```

## Google AI Setup

1. Go to Google AI Studio: `https://aistudio.google.com/`.
2. Sign in with your Google account.
3. Create an API key.
4. Copy the API key into app `Settings` under `Google AI API key`.
5. Tap `Load available models`.
6. Select one of the returned models.
7. Prefer a Gemma model if Gemini models are unavailable or return access errors for your key.
8. Tap `Test selected model` before generating a training plan.

## Goal Input

Target time accepts:

```text
95
1:35
1:35:30
```

Race date uses ISO format:

```text
2026-09-13
```


## Build Commands

```bash
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

## Architecture

- `data/local`: Room database, entities, DAOs.
- `data/remote/strava`: Strava OAuth/activity API DTOs and Retrofit services.
- `data/remote/ai`: Google AI provider and DTOs.
- `domain/analysis`: Local training summary and threshold pace estimation.
- `domain/prompt`: JSON prompt construction.
- `domain/usecase`: Sync, summary, and plan generation use cases.
- `ui`: Compose navigation, screens, and components.
