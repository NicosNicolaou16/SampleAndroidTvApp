plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.nicos.sampleandroidtvapp"
    compileSdk = 34
    buildToolsVersion = "34.0.0"

    defaultConfig {
        applicationId = "com.nicos.sampleandroidtvapp"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

val coreKtxVersion by extra("1.12.0")
val lifeCycleAndLiveDataCompilerAndViewModelKTXVersion by extra("2.6.2")
val activityVersion by extra("1.8.2")
val retrofitVersion by extra("2.9.0")
val okHttpVersion by extra("4.12.0")
val roomVersion by extra("2.6.1")
val daggerVersion by extra("2.15")
val coroutineVersion by extra("1.7.3")
val hiltVersion by extra("2.50")
val hiltCompilerVersion by extra("1.1.0")
val materialDesignVersion by extra("1.11.0")
val coilVersion by extra("2.5.0")
val swipeRefreshLayoutVersion by extra("1.1.0")
val tvComposeVersion by extra("1.0.0-alpha10")
val composeNavigationVersion by extra("2.7.6")
val composeHiltNavigationVersion by extra("1.1.0")
val composeVersion by extra("1.5.4")
val composeMaterialVersion by extra("1.5.4")

dependencies {

    implementation("androidx.core:core-ktx:$coreKtxVersion")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:$swipeRefreshLayoutVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifeCycleAndLiveDataCompilerAndViewModelKTXVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifeCycleAndLiveDataCompilerAndViewModelKTXVersion")
    //noinspection LifecycleAnnotationProcessorWithJava8
    kapt("androidx.lifecycle:lifecycle-compiler:$lifeCycleAndLiveDataCompilerAndViewModelKTXVersion")
    implementation("androidx.activity:activity-compose:$activityVersion")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:$composeNavigationVersion")
    implementation("androidx.hilt:hilt-navigation-compose:$composeHiltNavigationVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation("androidx.compose.material:material:$composeMaterialVersion")
    implementation("androidx.compose.runtime:runtime:$composeVersion")
    //Android Tv
    implementation("androidx.tv:tv-foundation:$tvComposeVersion")
    implementation("androidx.tv:tv-material:$tvComposeVersion")
    //Room Database
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    //Retrofit request
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")
    //OKHttp client
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    //Coil load Image
    implementation("io.coil-kt:coil-compose:$coilVersion")
    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")
    //Materials
    implementation("com.google.android.material:material:$materialDesignVersion")
    //Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    ksp("com.google.dagger:hilt-compiler:$hiltVersion")
    ksp("androidx.hilt:hilt-compiler:$hiltCompilerVersion")
    //Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}