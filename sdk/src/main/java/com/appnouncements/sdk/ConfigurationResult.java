package com.appnouncements.sdk;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfigurationResult {
    public static ConfigurationResult fromJsonObject(JSONObject jsonObject) throws JSONException {
        return new ConfigurationResult(jsonObject.getInt("unseenReleasesCount"));
    }

    private int unseenReleasesCount;

    private ConfigurationResult(int unseenReleasesCount) {
        this.unseenReleasesCount = unseenReleasesCount;
    }

    public int getUnseenReleasesCount() {
        return unseenReleasesCount;
    }
}
