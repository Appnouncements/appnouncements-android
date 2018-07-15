package com.appnouncements.sdk;

import com.appnouncements.sdk.VersionManager;

public class ApiConfiguration {
    private final String API_BASE_URL = "https://appnouncements.serveo.net/api";
    private final String API_KEY;
    private final VersionManager versionManager;

    public ApiConfiguration(VersionManager versionManager, String apiKey) {
        this.versionManager = versionManager;
        API_KEY = apiKey;
    }

    public String buildUrl(String action) {
        return API_BASE_URL + "/v1/" + action + "/" + API_KEY + "/" + versionManager.getLastSeenVersionCode() + "..." + versionManager.getVersionCode();
    }
}
