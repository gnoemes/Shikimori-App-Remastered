# Shikimori App Remastered — Agent Guide

## Tech stack

- Single `:app` module (`settings.gradle` only includes `:app`).
- **Kotlin 2.3.21** + Java (Dagger modules are Java). KSP (not kapt) for Dagger, Room, Glide, Moxy processors.
- **MVP** via Moxy 2.2.2 (`MvpPresenter`, `@InjectViewState`, `AddToEndSingleStrategy`).
- **DI** via Dagger 2.59.2 Android (KSP: `dagger-compiler`, `dagger-android-processor`).
- **Navigation** via Cicerone 5.1.1 (`Router`, local `NavigatorHolder`).
- **Async** via RxJava 2 (Single, Completable, Observable).
- **Network** via Retrofit 2.11.0 + OkHttp 4.12.0 + Gson.
- **DB** via Room 2.7.1 + RxJava 2 adapter (migrated from StorIO — `storio-*` dirs are dead code, not in settings.gradle).
- **Images** via Glide 4.16.0 + OkHttp integration.
- **Video** via ExoPlayer 2.19.1 + mediasession extension.
- **Firebase**: analytics, crashlytics, messaging, firestore, storage.
- **ViewBinding** enabled. Joda-Time Android for date/time.

## Build

```bash
./gradlew assembleDebug          # debug APK (app-debug.apk, -SNAPSHOT suffix)
./gradlew assembleRelease        # release (minify+shrink+zipalign)
```

- Gradle 8.10.2 / AGP 9.2.1 / Kotlin 2.3.21 / Java 17 (source & target).
- MinSdk 21, TargetSdk 35, CompileSdk 35.
- Versions in `dependencies.gradle` via `ext.versions` (no version catalog).
- `multiDexEnabled true`, `useAndroidX=true`, `enableJetifier=true`.

## Secrets

All keys/tokens are in **`gradle.properties`** (committed to repo):
`ShikimoriClientId`, `ShikimoriClientSecret`, `ShikimoriBaseUrl`, `VideoBaseUrl`, `VkRandomToken`.

Injected as `BuildConfig` fields via `app/build.gradle:41-47` (`buildTypes.configureEach { buildConfigField 'String', 'Name', Name }`). These reference Groovy project-level variables directly — **not** env vars. CI workflows pass them by appending to `gradle.properties` conditionally.

GitHub Actions secrets required (for automated builds):
`SHIKIMORI_CLIENT_ID`, `SHIKIMORI_CLIENT_SECRET`, `SHIKIMORI_BASE_URL`, `VIDEO_BASE_URL`, `VK_RANDOM_TOKEN`, plus for releases: `KEYSTORE_BASE64`, `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`.

## Versioning

- `gradle.properties`: `AI_VERSION_CODE=141` (manual).
- `app/build.gradle`: `versionName "0.8.7"`, `versionCode 1` (overridden by `advanced-build-version` plugin via git commit count).
- Debug builds get `.debug` appIdSuffix and `-SNAPSHOT` versionNameSuffix.
- `google-services.json` is committed.

## Architecture

```
app/src/main/java/com/gnoemes/shikimori/
  presentation/view/       — Activities & Fragments (Moxy views)
  presentation/presenter/  — MvpPresenter subclasses (RxJava orchestration)
  di/                      — Dagger modules in Java (@Module, @Binds, @ContributesAndroidInjector)
  entity/                  — By-feature domain: anime/, auth/, calendar/, etc. (each has data/ sub-packages)
  data/                    — Retrofit APIs, Room DAOs, SharedPrefs, repositories
  domain/                  — Interactors (business logic layer)
```

- `BasePresenter` extends `MvpPresenter` → `onFirstViewAttach()` calls `initData()`.
- Presenters injected via `@Provide` in per-feature Dagger modules.
- Activities/Fragments scoped with `@ActivityScope`, injected via `AndroidInjection.inject(this)`.
- **KSP args** in `app/build.gradle:80-92`: `defaultMoxyStrategy=AddToEndSingleStrategy`, `disableEmptyStrategyCheck=true`, `room.schemaLocation=$projectDir/schemas`.

## CI / GitHub Actions

Two workflows exist in `.github/workflows/`:

| File | Trigger | Purpose |
|------|---------|---------|
| `ci.yml` | `workflow_dispatch` only | Manual debug build |
| `release.yml` | Tag push (`v*.*.*`) + `workflow_dispatch` | Sign & release APK |

Both inject secrets conditionally (only if GitHub secret is set) via `[ -n "$VAR" ] && echo "Key=$VAR" >> gradle.properties`. The committed `gradle.properties` provides fallback defaults.

**CI quirks:**
- `chmod +x gradlew` is required before running (checkout doesn't preserve +x).
- `android-actions/setup-android@v3` is required — `setup-java` alone doesn't install the Android SDK.
- The `release.yml` `if` condition accounts for `workflow_dispatch`: `${{ github.event_name == 'workflow_dispatch' || (github.ref_type == 'tag' && ! contains(github.ref_name, '-')) }}`.

## ProGuard

Rules in `app/proguard-rules.pro` (not root). `-optimizationpasses 5`. Key keeps: Glide, Firebase, GMS, Protobuf, jsoup, ExoPlayer, WebView JS interfaces, kotlinx.coroutines internals, embedded player `@JavascriptInterface`.

## Test deps

Test dependencies exist in `app/build.gradle:165-177` — JUnit 5 (Jupiter), MockK, Turbine, Room testing, OkHttp MockWebServer, Espresso. However, **no test source directories or test files exist yet**. The `testOptions { useJUnitPlatform() }` is configured.

## Dead code

`storio-*`, `storio-common-annotations-processor/`, `storio-content-resolver*`, `storio-sqlite*` directories are leftovers from Room migration. Not in `settings.gradle`. Do not touch.

## Known quirks

- `.idea/`, `.gradle/`, `build/` are gitignored but `.project`, `.settings/`, `.classpath` (Eclipse files) are committed.
- `libs/` contains pre-built `.aar`/`.jar` files (drawer-behavior, kotlin-permissions, cicerone, flexbox, ExoPlayer 2.9.6) — some duplicate Gradle deps. `drawer-behavior` is used as source (`com.infideap.drawerbehavior.AdvanceDrawerLayout`) despite the AAR being commented out.
- Gradle daemon uses JetBrains JDK 21 toolchain (`gradle/gradle-daemon-jvm.properties`).
- No ktlint, detekt, spotless, `.editorconfig`, or `lint.xml` configured. Follow existing code — 4-space indent.
