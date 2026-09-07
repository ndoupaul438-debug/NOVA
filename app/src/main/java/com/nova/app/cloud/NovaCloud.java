package com.nova.app.cloud;

import android.content.Context;
import org.json.JSONObject;

public class NovaCloud {

    private final Context context;

    public NovaCloud(Context context) {
        this.context = context.getApplicationContext();
    }

    public JSONObject status() {
        JSONObject result = new JSONObject();

        try {
            result.put("github", "ready_for_connection");
            result.put("backup", "local_ready");
            result.put("sync", "provider_required");
            result.put("sharing", "local_ready");
            result.put("collaboration", "backend_required");
        } catch (Exception ignored) {}

        return result;
    }
}
