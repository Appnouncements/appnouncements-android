package com.appnouncements.sdk;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConfigurationFetcherAsyncTask extends AsyncTask<Void, Void, ConfigurationResult> {
    private final ApiConfiguration apiConfiguration;
    private Throwable error;

    public ConfigurationFetcherAsyncTask(ApiConfiguration apiConfiguration) {
        this.apiConfiguration = apiConfiguration;
    }

    @Override
    protected ConfigurationResult doInBackground(Void... voids) {
        StringBuilder jsonBuffer = new StringBuilder();
        HttpURLConnection connection = null;

        // Network Connection
        try {
            connection = (HttpURLConnection) new URL(apiConfiguration.buildUrl("configuration")).openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoInput(true);
            connection.setConnectTimeout(20 * 1000);
            connection.setReadTimeout(20 * 1000);

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new Exception("Not OK Status Code!");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(connection.getInputStream())));

            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
            reader.close();
        } catch (Exception e) {
            error = e;
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) connection.disconnect();
        }

        // Parse JSON
        try {
            return ConfigurationResult.fromJsonObject(new JSONObject(jsonBuffer.toString()));
        } catch (Exception e) {
            error = e;
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ConfigurationResult configurationResult) {
        if (configurationResult != null) {
            Appnouncements.onConfigurationReceived(configurationResult);
        }
        else {
            Appnouncements.onConfigurationError(error);
        }
    }
}
