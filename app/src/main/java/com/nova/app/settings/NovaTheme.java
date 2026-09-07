package com.nova.app.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;

public final class NovaTheme {

    public static final String NOVA_LIGHT = "NOVA Light";
    public static final String NOVA_DARK = "NOVA Dark";
    public static final String MIDNIGHT = "Midnight";
    public static final String OCEAN = "Ocean";
    public static final String AURORA = "Aurora";
    public static final String EMERALD = "Emerald";
    public static final String CRIMSON = "Crimson";
    public static final String AMOLED = "AMOLED";

    private static final String PREFS = "nova_settings";
    private static final String THEME = "theme";

    private NovaTheme() {}

    public static String get(Context context) {
        return context
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getString(THEME, NOVA_LIGHT);
    }

    public static void set(Context context, String theme) {
        context
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .putString(THEME, theme)
                .apply();
    }

    public static int background(String theme) {
        if (isDark(theme)) {
            if (AMOLED.equals(theme)) {
                return Color.BLACK;
            }
            return Color.rgb(10, 15, 28);
        }

        if (OCEAN.equals(theme)) {
            return Color.rgb(239, 248, 255);
        }

        if (EMERALD.equals(theme)) {
            return Color.rgb(241, 250, 246);
        }

        if (AURORA.equals(theme)) {
            return Color.rgb(247, 243, 255);
        }

        if (CRIMSON.equals(theme)) {
            return Color.rgb(255, 246, 247);
        }

        return Color.rgb(245, 247, 251);
    }

    public static int card(String theme) {
        if (isDark(theme)) {
            if (AMOLED.equals(theme)) {
                return Color.rgb(12, 12, 12);
            }
            return Color.rgb(22, 30, 46);
        }

        return Color.WHITE;
    }

    public static int text(String theme) {
        return isDark(theme)
                ? Color.rgb(241, 245, 249)
                : Color.rgb(15, 23, 42);
    }

    public static int muted(String theme) {
        return isDark(theme)
                ? Color.rgb(148, 163, 184)
                : Color.rgb(100, 116, 139);
    }

    public static int border(String theme) {
        return isDark(theme)
                ? Color.rgb(51, 65, 85)
                : Color.rgb(226, 232, 240);
    }

    public static int accent(String theme) {

        switch (theme) {
            case OCEAN:
                return Color.rgb(14, 116, 200);

            case AURORA:
                return Color.rgb(124, 58, 237);

            case EMERALD:
                return Color.rgb(5, 150, 105);

            case CRIMSON:
                return Color.rgb(220, 38, 38);

            case MIDNIGHT:
                return Color.rgb(59, 130, 246);

            case AMOLED:
                return Color.rgb(96, 165, 250);

            default:
                return Color.rgb(37, 99, 235);
        }
    }

    public static boolean isDark(String theme) {
        return NOVA_DARK.equals(theme)
                || MIDNIGHT.equals(theme)
                || AMOLED.equals(theme);
    }

    public static void applyNavigationBar(
            android.app.Activity activity,
            String theme) {

        activity.getWindow().setStatusBarColor(
                background(theme)
        );

        activity.getWindow().setNavigationBarColor(
                background(theme)
        );

        int flags = 0;

        if (!isDark(theme)) {
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        }

        activity.getWindow()
                .getDecorView()
                .setSystemUiVisibility(flags);
    }
}
