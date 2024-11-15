# Sample Android TV App
The project is a test project that implements and test Jetpack Compose for Android TV OS app with API request (dynamic list), Hilt and Room Database.

<a title="simulator_image"><img src="Screenshot_20231119_124940.png" height="300" width="500"></a>

# This Project contains:
For Android TV OS support <br />
Compose <br />
Hilt <br />
Room Database <br />
Coroutines <br />
Kotlin ktx <br />
MVVM <br />
Support kapt and ksp (ksp only setup for Room Database) <br />
minifyEnabled, shrinkResources, R8 are enabled <br />
Gradle Kotlin DSL <br />
Repository <br />
Offline <br />

## Similar project with (Dart Language)
https://github.com/NicosNicolaou16/SampleFlutterTVApp <br />

# References/Tutorials Follow/For Manifest Setup
https://developer.android.com/training/tv/playback/compose <br />
https://developer.android.com/jetpack/compose/touch-input/focus/react-to-focus <br />
https://stackoverflow.com/questions/76281554/android-jetpack-compose-tv-focus-restoring <br />

# Setup in Manifest
```xml

<manifest>

    <uses-feature android:name="android.software.leanback" android:required="false" />

    <uses-feature android:name="android.hardware.touchscreen" android:required="false" />

    <application android:banner="@mipmap/ic_launcher">
        <!--other code here-->
        <activity>
            <!--other code here-->
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
                <category android:name="android.intent.category.LEANBACK_LAUNCHER" />
            </intent-filter>
        </activity>
        <!--other code here-->
    </application>
</manifest>
```

## Check my article
https://medium.com/@nicosnicolaou/android-tv-application-jetpack-compose-and-flutter-f4decfa765c6

# Feeds/Urls/End Point (parsing some data from response)
## (Links References for Ends Points)
https://github.com/r-spacex/SpaceX-API (GitHub) <br />
https://docs.spacexdata.com/?version=latest (Postman) <br />

Target SDK version: 35 <br />
Minimum SDK version: 28 <br />
Kotlin version: 2.0.21 <br />
Gradle version: 8.7.2 <br />