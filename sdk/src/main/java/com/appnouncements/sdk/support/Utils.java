package com.appnouncements.sdk.support;

import android.content.Context;
import android.content.pm.PackageManager;

public final class Utils {
    public static <T> T coalesce(T one, T two)
    {
        return one != null ? one : two;
    }
}
