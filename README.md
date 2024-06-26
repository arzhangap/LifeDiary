<div align="center">

<h1 align = "center">
<b><i>DiaryApp</i></b>
</h1>

  <p align="center">
    Complete Multi-Modular Android App 
    <br />

  
[Screenshots](#camera_flash-screenshots-camera_flash) ~
[Deployment](#arrow_lower_right-deployment-arrow_lower_right) ~
[Architecture](#hammer_and_wrench-architecture-hammer_and_wrench) ~
[Tech Stack](#building_construction-tech-stack-building_construction) ~
[Authors](#memo-authors-memo) ~
[Contributing](#handshake-contributing-handshake)  
[License](#scroll-license-scroll)  
 
</div>

a variant of [diary app](https://github.com/stevdza-san/DiaryApp/).

DiaryApp allows users to write and save diary entries with the option to include photos and expressive emojis to capture the essence of each moment. 

written in Kotlin and Jetpack Compose


# :camera_flash: **Screenshots** :camera_flash:

DiaryApp follows the latest Material 3 guidelines for a visually appealing and a consistent UI.!

<p align="center">
<img img width="200" height="400" src="https://github.com/arzhangap/LifeDiary/assets/17007550/30204ded-979b-434a-a74e-7c8538e55f84"> &nbsp;&nbsp;&nbsp;&nbsp;   
<img img width="200" height="400" src="https://github.com/arzhangap/LifeDiary/assets/17007550/0a513c83-e185-4b4e-888b-a6aaeba80f2f"> &nbsp;&nbsp;&nbsp;&nbsp;   
<img img width="200" height="400" src="https://github.com/arzhangap/LifeDiary/assets/17007550/f7293475-05df-4a15-94fc-0e94490556d1"> &nbsp;&nbsp;&nbsp;&nbsp;   

</p>



# :arrow_lower_right: Deployment :arrow_lower_right:
These are the key parameters for DiaryApp.

| Parameters     | Value |
|----------------|-------|
| compileSdk     | 34    |
| targetSdk      | 34    |
| minSdk         | 24    |
| composeVersion | 1.5.1 |
| kotlinVersion  | 1.9.0 |


To build and run the app, you will need the latest version of Android Studio Flamingo (or [newer](https://developer.android.com/studio/)) installed on your system.
# :hammer_and_wrench: Architecture :hammer_and_wrench:
### Modules

DiaryApp is initially built using Android Clean Architecture that follows the more familiar   **Model-View-ViewModel** (MVVM) pattern.

On the [first branch](https://github.com/stevdza-san/LifeDiary/tree/modualr) the project is restructured into a Multi-Module Architecture using layered features.
and on the [second branch](https://github.com/stevdza-san/LifeDiary/tree/master) there is Android Clean Architecture version.

Here's an overview of the app's architectural modular components:
- **App Module**: This is the main module of the DiaryApp, whhich acts as the orchestrator of the different features and modules. It handles the navigation flow between the Authentication, Home, and the Write features, ensuring a cohesive and seamless user experience. The App Module integrates the dependencies from the feature modules and manages the overall lifecycle of the app.

- **buildSrc**: The buildSrc module serves as a central location for managing project configuration and dependencies. This module allows for a streamlined and standardized setup of project configurations, build scripts, and dependencies, simplifying the build process and ensuring consistency across the app.

- **Data Module**:The Data module in the Android Diary App is responsible for managing data storage and retrieval using both MongoDB and Room. It handles the setup and integration of Mongo Realm, allowing seamless connectivity to the MongoDB backend. The Data module provides functionalities for inserting, fetching, updating, and deleting diary entries in the MongoDB database. Additionally, DiaryApp leverages Room Librayto provide offline access and local caching of diary entries, enhancing the app's responsiveness and offline capabilities.

- **Common/Core Modules**: The app includes two core modules: *UI* and *Utils*. The UI module contains common Compose functions, components, and UI-related code that are shared across different features. This module promotes code reuse and consistency in the app's user interface. The Utils module provides essential utilities such as model classes, connectivity observers, constants, strings and drawable resources. It ensures a centralized and efficient management of commonly used resources and functionalities.

- **Feature Modules**: This module includes three destinations: Auth, Home and Write. 'Auth' handles the authentication with the users. 'Home' displays all the data/diaries in our application. And 'Write' module allows you to create a new diary note in your app.

### Navigation
The app has :three: screen destinations which use Compose Navigation to manage navigation.

https://github.com/arzhangap/LifeDiary/assets/17007550/504a0a9f-68f4-40c2-aee6-61839500f66c

- **Authentication Feature**: This feature focuses on user authentication and validation. It utilizes Google Sign-In to ensure that users can securely access their diary entries. By authenticating users, the app guarantees that only authorized individuals can interact with their personal diaries.

- **Home Feature**: The Home feature is responsible for displaying and filtering diary entries based on the date. It provides a user-friendly interface to navigate through diary entries and quickly filter diaries by specific dates. Additional selections can be accessed through the Navigation Drawer.

- **Write Feature**: The Write feature enables users to create new diary entries or modify existing ones. It offers a seamless and intuitive interface for users to capture and document their thoughts, moments, and memories. DiaryApp empowers the users to personalize content by adding emojis and accompanying images


Overroll by adopting a multi-modular architecture with layered features, the DiaryApp achieves a separation of concerns, enabling independent development and testing of specific functionalities. This architecture promotes code reusability, scalability and easy maintainability

# :building_construction: Tech Stack :building_construction:

The DiaryApp project uses many popular libraries and tools in the Android Ecosystem:

* [Jetpack Compose](https://developer.android.com/jetpack/compose) - modern toolkit for building native Android UI.
* [Android KTX](https://developer.android.com/kotlin/ktx) - helps to write more concise, idiomatic Kotlin code.

* [Coroutines and Kotlin Flow](https://kotlinlang.org/docs/reference/coroutines-overview.html) - used to manage the local storage i.e. `writing to and reading from the database`. Coroutines help in managing background threads and reduces the need for callbacks.
* [Material Design 3](https://m3.material.io/) - an adaptable system of guidelines, components, and tools that support the best practices of user interface design.
* [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) - navigate between composables while taking advantage of the stateful NavController which keeps track of the back stack of composables that make up the screens in your app. 
* [Google Accompanist Libraries](https://github.com/google/accompanist) - these are a collection of extension libraries for Jetpack Compose. DiaryApp specifically uses Accompanist's Pager Library
* [Dagger Hilt](https://dagger.dev/hilt/) - used for Dependency Injection.
* [Coil](https://coil-kt.github.io/coil/) - an image loading library for Android backed by Kotlin Coroutines
* [SplashScreen API](https://developer.android.com/develop/ui/views/launch/splash-screen) - SplashScreen API lets apps launch with animation, including an into-app motion at launch, a splash screen showing your app icon, and a transition to your app itself.

* [Room](https://developer.android.com/topic/libraries/architecture/room) persistence library which provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
* [Mongo](https://www.mongodb.com/) - MongoDB is a popular NoSQL database, used in this app for storing and managing data related to diary entries.
* [Firebase Storage](https://firebase.google.com/docs/storage/android/start) -  Firebase Cloud Storage is a scalable and reliable cloud storage solution used in the app for storing and retrieving photos associated with diary entries.
* [Firebase Auth](https://firebase.google.com/docs/auth/android/start) - Firebase Authentication provides a secure and easy-to-use authentication system, allowing users to sign in and access their diary entries securely.
* [Max Keppeler's Sheet Compose Dialog](https://github.com/maxkeppeler/sheets-compose-dialogs) - Firebase Authentication provides a secure and easy-to-use authentication system, allowing users to sign in and access their diary entries securely.

* [Stevdza-San's MessageBarCompose](https://github.com/stevdza-san/MessageBarCompose) - Animated Message Bar UI that can be wrapped around your screen content in order to display Error/Success messages in your app. It is adapted and optimized for use with Compose and Material 3 projects.

* [Stevdza-San's OneTapCompose](https://github.com/stevdza-san/OneTapCompose) - Animated Message Bar UI that can be wrapped around your screen content in order to display Error/Success messages in your app. It is adapted and optimized for use with Compose and Material 3 projects.

# :scroll: License :scroll:

MIT License

Copyright (c) [2023] [Stefan Jovanovic]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
