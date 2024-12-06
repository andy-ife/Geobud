<p align="center">
  <a href="" rel="noopener">
 <img width=200px height=200px src="/images/geobud_logo.svg" alt="Project logo"></a>
</p>

<h3 align="center">Geobud</h3>

<div align="center">

[![Status](https://img.shields.io/badge/status-active-success.svg)]()
[![GitHub Issues](https://img.shields.io/github/issues/andy-ife/Geobud.svg)](https://github.com/andy-ife/Geobud/issues)
[![GitHub Pull Requests](https://img.shields.io/github/issues-pr/andy-ife/Geobud.svg)](https://github.com/andy-ife/Geobud/pulls)
[![License](https://img.shields.io/badge/license-GPLv3-blue.svg)](/LICENSE)

</div>

---

<p align="center"> 
Geobud is a simple fun geography quiz app that lets you guess countries from their photos. It is built using the latest technologies and best Modern Android Development practices.
<br> <br>
</p>

<div>
<img src="/images/app_screen1.jpg" width="300px" alt="App Screen 1" style="margin-right: 20px;"></img>
<img src="/images/app_screen2.jpg" width="300px" alt="App Screen 2" style="margin-right: 20px;"></img>
<img src="/images/app_screen3.jpg" width="300px" alt="App Screen 3"></img>
<br><br>
</div>

<p align="center">
Contributors are highly welcome. Please go through the contributing guidelines <a href="./CONTRIBUTING.md">here</a> before you start contributing. 
</p>

<!-- ## 📝 Table of Contents
- [Features](#features)
- [Installation](#installation)
- [Built Using](#built_using)
- [Contributing](./CONTRIBUTING.md)
- [Authors](#authors)
- [Thanks](#thanks) -->

## 💡 Features <a name = "features"></a>
Geobud combines education and entertainment, providing the following unique features:

- Guess countries based on photos of places and famous landmarks. ✈️
- Save your favorite photos to your phone storage. ⬇️
- Limited offline capability with photo caching.
- Browse the history of all your past photos and locations. 🖼️
- Dark and light mode. 🌓
- Single activity design.
- Clean and simple Jetpack Compose UI.
- Reset your progress so you can enjoy Geobud once more!

## 🏁 Installation <a name = "installation"></a>

### Release apk
If you simply want to install the app, get the release apk <a href="https://github.com/andy-ife/Geobud/releases">here.</a>

### Building from source

If you'd like to tinker with the source code, all you'll need is an API Key from [Pexels](https://www.pexels.com/api/), the API that provides photos.

In your **local** gradle.properties file, add the line:

```
PEXELS_API_KEY=<Your_API_Key>
```
## ⛏️ Built Using <a name = "built_using"></a>

- [Kotlin](https://kotlinlang.org/) - Google's preferred language for Android development.
- [Jetpack Compose](https://developer.android.com/compose) - Android's recommended modern UI toolkit.
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Lifecycle-aware stateholder class that encapsulates business logic and survives configuration changes.
- [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - Kotlin's solution for asynchronous/non-blocking code.
- [Flows](https://kotlinlang.org/docs/coroutines-overview.html) - For reactive programming. In addition to Kotlin's flows, Android's [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow) and [SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow) APIs were used.
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - For lifecycle-aware observable data.
- [Retrofit](https://github.com/square/retrofit) - Aids REST API communication.
- [Room](https://developer.android.com/training/data-storage/room) - Provides an abstraction layer over SQLite for persistent local data storage.
- [WorkManager](https://developer.android.com/reference/androidx/work/WorkManager) - For guaranteed execution of persistent background tasks.
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Dependency Injection library built on Dagger.
- [Preferences Datastore](https://developer.android.com/topic/libraries/architecture/datastore) - To store small simple datasets persistently in key-value pairs.
- [Coil](https://github.com/coil-kt/coil) - Image loading and caching library.
- [Gson](https://github.com/google/gson) - Java library for JSON conversion.
- [Material UI](https://developer.android.com/design/ui/mobile/guides/components/material-overview) - A set of guidelines and components for building modern user-centric interfaces.
- [Lottie](https://lottiefiles.com/) - Lightweight JSON animations.
- [MediaPlayer](https://developer.android.com/reference/android/media/MediaPlayer.html) - Android library for audio/video playback control.

## 🤝 Contributing
Please read through [CONTRIBUTING.md](./CONTRIBUTING.md) if you would like to contribute to the project.

## ✍️ Authors <a name = "authors"></a>

- [@andy-ife](https://github.com/kylelobo) - Idea & Initial work

See also the list of [contributors](https://github.com/andy-ife/Geobud/contributors) who participated in this project.

## 🙏 Thanks <a name = "thanks"></a>
- [Pexels](https://www.pexels.com/api/) for their REST API, where all photos used in this application come from.
- [Reshot](https://www.reshot.com/) for the app icon.
- [SVG Repo](https://www.svgrepo.com/) for icons.
