package com.appnouncements.sdk;

import android.app.Application;
import android.os.AsyncTask;

import java.util.ArrayList;

public final class Appnouncements {
    private static ConfigurationFetcherAsyncTask configurationTask;
    private static Client client;
    private static ArrayList<Client.Listener> callbacks = new ArrayList<>();
    private static ApiConfiguration apiConfiguration;

    private Appnouncements() {}

    public static void initialize(Application applicationContext, String apiKey) {
        apiConfiguration = new ApiConfiguration(new VersionManager(applicationContext), apiKey);
        startConfigurationTaskIfNeeded();
    }

    public static synchronized void getClientAsync(Client.Listener callback) {
        if (client != null) {
            callback.onAppnouncementsClientReady(Appnouncements.client);
            return;
        }

        callbacks.add(callback);
        startConfigurationTaskIfNeeded();
    }

    static synchronized void onConfigurationReceived(ConfigurationResult configurationResult) {
        Appnouncements.client = new Client(apiConfiguration, configurationResult);
        for (Client.Listener cb : callbacks) {
            cb.onAppnouncementsClientReady(Appnouncements.client);
        }
        callbacks.clear();
    }

    static synchronized void onConfigurationError(Throwable error) {
//        for (Client.Listener cb : callbacks) {
//            cb.onAppnouncementsClientFailed(error);
//        }
        callbacks.clear();
    }

    private static void startConfigurationTaskIfNeeded() {
        if (configurationTask == null || configurationTask.getStatus() == AsyncTask.Status.FINISHED) {
            configurationTask = (ConfigurationFetcherAsyncTask) new ConfigurationFetcherAsyncTask(apiConfiguration).execute();
        }
    }
}