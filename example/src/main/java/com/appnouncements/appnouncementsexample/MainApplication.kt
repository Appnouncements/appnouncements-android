package com.appnouncements.appnouncementsexample

import android.app.Application
import com.appnouncements.sdk.Appnouncements

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Appnouncements.initialize(this, "c0748bcd-fc09-4a79-b7cb-5de7caf2e233")
    }
}