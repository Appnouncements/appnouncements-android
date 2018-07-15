package com.appnouncements.sdk;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import com.appnouncements.sdk.ui.ReleaseNotesActivity;

public class VersionManager {
    private final Application application;
    private final String SHARED_PREFERENCES_KEY = "Appnouncements_Prefs";
    private final String SHARED_PREF_LAST_SEEN_VERSION_CODE = "LAST_SEEN_VERSION_CODE";

    public VersionManager(Application application) {
        this.application = application;
    }

    public String getVersionCode() {
        try {
            return String.valueOf(application.getPackageManager().getPackageInfo(application.getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    public String getLastSeenVersionCode() {
        return application.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE).getString(SHARED_PREF_LAST_SEEN_VERSION_CODE, "");
    }

    public void updateLastSeenVersionCode() {
        application.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE).edit()
                .putString(SHARED_PREF_LAST_SEEN_VERSION_CODE, getVersionCode()).apply();
    }
}
