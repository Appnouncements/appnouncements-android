package com.appnouncements.sdk;

import android.os.AsyncTask;
import android.util.Log;

import com.appnouncements.sdk.support.AppnouncementsException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConfigurationFetcherAsyncTask extends AsyncTask<Void, Void, ConfigurationResult> {
    private final ApiConfiguration apiConfiguration;
    private AppnouncementsException error;

    ConfigurationFetcherAsyncTask(ApiConfiguration apiConfiguration) {
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
                if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                    throw new AppnouncementsException("Invalid API Key");
                }

                throw new AppnouncementsException("Something is wrong with the Appnouncements server, returned a status " + connection.getResponseCode());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(connection.getInputStream())));

            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
            reader.close();
        } catch (IOException e) {
            error = new AppnouncementsException("Failed to connect to Appnouncements server", e);
            e.printStackTrace();
            return null;
        } catch (AppnouncementsException e) {
            error = e;
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) connection.disconnect();
        }

        // Parse JSON
        try {
            ConfigurationResult configurationResult = ConfigurationResult.fromJsonObject(new JSONObject(jsonBuffer.toString()));

            if (configurationResult.isDisabled()) {
                error = new AppnouncementsException("This app is currently disabled. See Appnouncements.com for more details.");
                error.printStackTrace();
                return null;
            }

            return configurationResult;
        } catch (JSONException e) {
            error = new AppnouncementsException("Failed parsing response from Appnouncements server", e);
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
            Log.e("appnouncements", "There was a problem creating your Appnouncements client", error);
            Appnouncements.onConfigurationError(error);
        }
    }
}
