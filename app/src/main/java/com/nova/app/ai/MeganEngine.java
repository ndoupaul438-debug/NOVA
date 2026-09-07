package com.nova.app.ai;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MeganEngine {

    private static final String PREFS = "nova_megan";
    private static final String HISTORY = "conversation_history";
    private static final String TASKS = "agent_tasks";

    private final Context context;
    private final SharedPreferences prefs;

    public MeganEngine(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = this.context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    /*
     * Provider abstraction.
     *
     * The local provider works immediately.
     * Online/LLMPI providers can be connected without changing
     * the NOVA UI or command engine.
     */
    public interface AiProvider {
        String getName();
        boolean isAvailable();
        String generate(String request, String context);
    }

    public static class LocalProvider implements AiProvider {

        @Override
        public String getName() {
            return "Local NOVA AI";
        }

        @Override
        public boolean isAvailable() {
            return true;
        }

        @Override
        public String generate(String request, String context) {
            if (request == null || request.trim().isEmpty()) {
                return "I'm M3GAN. Tell me what you want to create, build, analyze, or learn.";
            }

            String r = request.toLowerCase();

            if (r.contains("create") || r.contains("build") ||
                    r.contains("make") || r.contains("develop")) {

                return "I can turn that request into a NOVA project plan.\n\n"
                        + "Next steps:\n"
                        + "1. Define the project.\n"
                        + "2. Select the technology.\n"
                        + "3. Generate the project structure.\n"
                        + "4. Implement the required modules.\n"
                        + "5. Test and debug.\n"
                        + "6. Build and export.";
            }

            if (r.contains("learn") || r.contains("explain") ||
                    r.contains("teach") || r.contains("study")) {

                return "M3GAN learning mode is ready.\n\n"
                        + "I can organize this into an explanation, notes, "
                        + "examples, questions, and a study plan.";
            }

            if (r.contains("code") || r.contains("program") ||
                    r.contains("java") || r.contains("python") ||
                    r.contains("javascript")) {

                return "M3GAN coding mode is ready.\n\n"
                        + "I can plan the implementation, identify files, "
                        + "generate code, test it, and prepare a build.";
            }

            return "M3GAN received your request.\n\n"
                    + "Local intelligence is active. "
                    + "External AI providers can be connected through the "
                    + "provider layer when configured.";
        }
    }

    private AiProvider provider = new LocalProvider();

    public void setProvider(AiProvider provider) {
        if (provider != null) {
            this.provider = provider;
        }
    }

    public AiProvider getProvider() {
        return provider;
    }

    public JSONObject plan(String request) {
        JSONObject result = new JSONObject();

        try {
            String clean = request == null ? "" : request.trim();

            result.put("assistant", "M3GAN");
            result.put("request", clean);
            result.put("status", "planned");
            result.put("provider", provider.getName());
            result.put("provider_available", provider.isAvailable());
            result.put("mode", "local");
            result.put("safe_actions_only", true);
            result.put("requires_external_ai", !provider.isAvailable());

            result.put("intent", detectIntent(clean));

            JSONArray steps = new JSONArray();

            steps.put("Understand request");
            steps.put("Create execution plan");
            steps.put("Check project context");
            steps.put("Select NOVA tools");
            steps.put("Execute permitted actions");
            steps.put("Validate result");

            result.put("steps", steps);

        } catch (Exception ignored) {
        }

        return result;
    }

    public String respond(String request) {
        String context = buildContext();

        String response = provider.generate(request, context);

        saveConversation(request, response);

        return response;
    }

    public String localResponse(String request) {
        return respond(request);
    }

    public String detectIntent(String request) {

        if (request == null || request.trim().isEmpty()) {
            return "conversation";
        }

        String r = request.toLowerCase();

        if (containsAny(r, "create", "build", "make", "develop")) {
            return "create";
        }

        if (containsAny(r, "code", "program", "debug", "compile")) {
            return "coding";
        }

        if (containsAny(r, "learn", "teach", "study", "explain")) {
            return "learning";
        }

        if (containsAny(r, "image", "picture", "logo", "design")) {
            return "image_generation";
        }

        if (containsAny(r, "search", "research", "find")) {
            return "research";
        }

        if (containsAny(r, "file", "document", "pdf")) {
            return "documents";
        }

        if (containsAny(r, "project", "workspace")) {
            return "project";
        }

        if (containsAny(r, "test", "testing", "debug")) {
            return "testing";
        }

        return "conversation";
    }

    private boolean containsAny(String value, String... terms) {
        for (String term : terms) {
            if (value.contains(term)) {
                return true;
            }
        }

        return false;
    }

    private String buildContext() {
        try {
            JSONObject context = new JSONObject();

            context.put("assistant", "M3GAN");
            context.put("platform", "NOVA");
            context.put("provider", provider.getName());

            JSONArray history = getConversationHistory();
            JSONArray tasks = getAgentTasks();

            context.put("history", history);
            context.put("tasks", tasks);

            return context.toString();

        } catch (Exception e) {
            return "{}";
        }
    }

    private void saveConversation(String request, String response) {

        try {
            JSONArray history = getConversationHistory();

            JSONObject item = new JSONObject();

            item.put("request", request == null ? "" : request);
            item.put("response", response == null ? "" : response);
            item.put("timestamp", System.currentTimeMillis());

            history.put(item);

            /*
             * Keep local storage bounded.
             */
            while (history.length() > 100) {
                JSONArray trimmed = new JSONArray();

                for (int i = 1; i < history.length(); i++) {
                    trimmed.put(history.get(i));
                }

                history = trimmed;
            }

            prefs.edit()
                    .putString(HISTORY, history.toString())
                    .apply();

        } catch (Exception ignored) {
        }
    }

    public JSONArray getConversationHistory() {

        try {
            String raw = prefs.getString(HISTORY, "[]");
            return new JSONArray(raw);
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    public void clearConversationHistory() {
        prefs.edit()
                .remove(HISTORY)
                .apply();
    }

    public JSONObject createAgentTask(String request) {

        JSONObject task = new JSONObject();

        try {
            task.put("id", "task_" + System.currentTimeMillis());
            task.put("request", request == null ? "" : request);
            task.put("intent", detectIntent(request));
            task.put("status", "queued");
            task.put("created", System.currentTimeMillis());

            JSONArray tasks = getAgentTasks();
            tasks.put(task);

            prefs.edit()
                    .putString(TASKS, tasks.toString())
                    .apply();

        } catch (Exception ignored) {
        }

        return task;
    }

    public JSONArray getAgentTasks() {

        try {
            return new JSONArray(
                    prefs.getString(TASKS, "[]")
            );
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    public void clearAgentTasks() {
        prefs.edit()
                .remove(TASKS)
                .apply();
    }

    public JSONObject status() {

        JSONObject result = new JSONObject();

        try {
            result.put("name", "M3GAN");
            result.put("platform", "NOVA");
            result.put("provider", provider.getName());
            result.put("provider_available", provider.isAvailable());
            result.put("offline_ready", true);
            result.put("conversation_history",
                    getConversationHistory().length());
            result.put("agent_tasks",
                    getAgentTasks().length());
            result.put("safe_actions_only", true);
        } catch (Exception ignored) {
        }

        return result;
    }
}
