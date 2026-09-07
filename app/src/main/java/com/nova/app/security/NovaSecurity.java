package com.nova.app.security;

import android.content.Context;
import android.content.SharedPreferences;

public final class NovaSecurity {

    private static final String PREFS = "nova_security";

    private NovaSecurity() {}

    public static boolean sandboxEnabled(Context context) {
        return prefs(context).getBoolean("sandbox", true);
    }

    public static void setSandbox(Context context, boolean enabled) {
        prefs(context).edit().putBoolean("sandbox", enabled).apply();
    }

    public static boolean allowExternalActions(Context context) {
        return prefs(context).getBoolean("external_actions", false);
    }

    public static void setExternalActions(Context context, boolean enabled) {
        prefs(context).edit().putBoolean("external_actions", enabled).apply();
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
}
