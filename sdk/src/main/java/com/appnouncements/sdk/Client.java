package com.appnouncements.sdk;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.StringRes;

import com.appnouncements.sdk.ui.ReleaseNotesActivity;

public class Client {
    private final ApiConfiguration apiConfiguration;
    private final ConfigurationResult configurationResult;

    Client(ApiConfiguration apiConfiguration, ConfigurationResult configurationResult) {
        this.apiConfiguration = apiConfiguration;
        this.configurationResult = configurationResult;
    }

    public void showReleaseNotes(Activity activity) {
        showReleaseNotes(activity, R.string.toolbarTitle);
    }

    public void showReleaseNotes(Activity activity, @StringRes int actionbarTitle) {
        Intent intent = new Intent(activity, ReleaseNotesActivity.class);
        intent.putExtra(ReleaseNotesActivity.EXTRAS_RELEASE_NOTES_URL, apiConfiguration.buildUrl("release_notes"));
        intent.putExtra(ReleaseNotesActivity.EXTRAS_ACTION_BAR_TITLE, activity.getString(actionbarTitle));
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.noop);
    }

    public int getUnseenReleaseNotesCount() {
        return configurationResult.getUnseenReleasesCount();
    }

    public interface Listener {
        void onAppnouncementsClientReady(Client client);
//        void onAppnouncementsClientFailed(Throwable t);
    }
}
