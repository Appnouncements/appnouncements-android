package com.appnouncements.sdk;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfigurationResult {
    public static ConfigurationResult fromJsonObject(JSONObject jsonObject) throws JSONException {
        return new ConfigurationResult(jsonObject.getInt("unseenReleasesCount"), jsonObject.getBoolean("disabled"));
    }

    private final int unseenReleasesCount;
    private final boolean disabled;

    private ConfigurationResult(int unseenReleasesCount, boolean disabled) {
        this.unseenReleasesCount = unseenReleasesCount;
        this.disabled = disabled;
    }

    public int getUnseenReleasesCount() {
        return unseenReleasesCount;
    }

    public boolean isDisabled() {
        return disabled;
    }
}
