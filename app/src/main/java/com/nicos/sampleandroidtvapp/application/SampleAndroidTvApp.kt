package com.nicos.sampleandroidtvapp.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SampleAndroidTvApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}