package com.appnouncements.sdk.support;

public class AppnouncementsException extends Exception {
    public AppnouncementsException(String message) {
        super(message);
    }

    public AppnouncementsException(String message, Throwable cause) {
        super(message, cause);
    }
}
