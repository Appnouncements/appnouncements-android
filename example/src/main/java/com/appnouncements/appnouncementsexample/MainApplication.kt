package com.appnouncements.appnouncementsexample

import android.app.Application
import com.appnouncements.sdk.Appnouncements

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Appnouncements.initialize(this, "a2f1a11b-d78b-453e-bf21-5275f88aa871")
    }
}