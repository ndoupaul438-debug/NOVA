package com.nova.app.core;

import android.content.Context;
import android.content.SharedPreferences;

public final class NovaAppState {

    private static final String PREFS = "nova_app_state";

    private NovaAppState() {}

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
        );
    }

    public static void setActiveModule(
            Context context,
            String module
    ) {
        prefs(context)
                .edit()
                .putString("active_module", module == null ? "" : module)
                .apply();
    }

    public static String getActiveModule(Context context) {
        return prefs(context)
                .getString("active_module", "HOME");
    }

    public static void setActiveProject(
            Context context,
            String project
    ) {
        prefs(context)
                .edit()
                .putString("active_project", project == null ? "" : project)
                .apply();
    }

    public static String getActiveProject(Context context) {
        return prefs(context)
                .getString("active_project", "");
    }

    public static void setLastAction(
            Context context,
            String action
    ) {
        prefs(context)
                .edit()
                .putString("last_action", action == null ? "" : action)
                .apply();
    }

    public static String getLastAction(Context context) {
        return prefs(context)
                .getString("last_action", "");
    }

    public static void setFirstRunComplete(Context context) {
        prefs(context)
                .edit()
                .putBoolean("first_run_complete", true)
                .apply();
    }

    public static boolean isFirstRunComplete(Context context) {
        return prefs(context)
                .getBoolean("first_run_complete", false);
    }
}
