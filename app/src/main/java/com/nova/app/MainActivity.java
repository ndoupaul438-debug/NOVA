package com.nova.app;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.nova.app.core.NovaFeatures;
import com.nova.app.ai.MeganEngine;
import com.nova.app.projects.ProjectManager;
import com.nova.app.settings.NovaTheme;
import com.nova.app.settings.ThemeRegistry;
import com.nova.app.ui.NovaCommandCenter;

import java.io.File;
import java.util.List;

public class MainActivity extends Activity {

    private LinearLayout root;
    private LinearLayout content;
    private String currentTheme;

    private int DP;
    private TextToSpeech novaTts;
    private static final int VOICE_REQUEST = 7101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DP = (int) getResources().getDisplayMetrics().density;
        currentTheme = NovaTheme.get(this);
        buildUI();
    }

    private int dp(int value) {
        return value * DP;
    }

    private void buildUI() {
        NovaTheme.applyNavigationBar(this, currentTheme);

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(NovaTheme.background(currentTheme));

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16), dp(18), dp(16), dp(18));

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.addView(content);

        root.addView(scroll, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1
        ));

        root.addView(bottomNavigation());

        setContentView(root);

        showHome();
    }

    private TextView text(String value, float size, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(NovaTheme.text(currentTheme));
        t.setTypeface(bold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        t.setGravity(Gravity.CENTER_VERTICAL);
        return t;
    }

    private TextView title(String value) {
        TextView t = text(value, 28, true);
        t.setPadding(0, 0, 0, dp(8));
        return t;
    }

    private TextView subtitle(String value) {
        TextView t = text(value, 14, false);
        t.setTextColor(NovaTheme.muted(currentTheme));
        t.setPadding(0, 0, 0, dp(18));
        return t;
    }

    private GradientDrawable background(int color, int radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp(radius));
        return g;
    }

    private Button button(String label, View.OnClickListener listener) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextColor(Color.WHITE);
        b.setTextSize(14);
        b.setAllCaps(false);
        b.setTypeface(Typeface.DEFAULT_BOLD);
        b.setGravity(Gravity.CENTER);
        b.setBackground(background(NovaTheme.accent(currentTheme), 14));
        b.setPadding(dp(12), dp(4), dp(12), dp(4));
        b.setOnClickListener(listener);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(50)
        );
        p.setMargins(0, 0, 0, dp(10));
        b.setLayoutParams(p);

        return b;
    }

    private LinearLayout card(String heading, String description,
                              View.OnClickListener listener) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(14), dp(16), dp(14));
        card.setBackground(background(NovaTheme.card(currentTheme), 18));

        TextView h = text(heading, 17, true);

        TextView d = text(description, 13, false);
        d.setTextColor(NovaTheme.muted(currentTheme));
        d.setPadding(0, dp(5), 0, dp(10));

        card.addView(h);
        card.addView(d);

        if (listener != null) {
            card.setOnClickListener(listener);
        }

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        p.setMargins(0, 0, 0, dp(12));
        card.setLayoutParams(p);

        return card;
    }

    private void clear(String heading, String description) {
        content.removeAllViews();
        content.addView(title(heading));
        content.addView(subtitle(description));
    }

    private void showHome() {
        content.removeAllViews();

        LinearLayout commandCenter = NovaCommandCenter.create(
                this,
                destination -> {
                    if (destination.contains("App Studio")) {
                        showCreate();
                    } else if (destination.contains("Game Studio")) {
                        showFeatureGroup("GAME STUDIO", NovaFeatures.GAME_STUDIO);
                    } else if (destination.contains("Website Studio")) {
                        showFeatureGroup("WEBSITE STUDIO", NovaFeatures.WEBSITE_STUDIO);
                    } else if (destination.contains("AI Studio")) {
                        showFeatureGroup("AI STUDIO", NovaFeatures.AI_STUDIO);
                    } else if (destination.contains("Projects")) {
                        showProjects();
                    } else if (destination.contains("M3GAN")) {
                        showMegan();
                    } else if (destination.contains("Settings")) {
                        showSettings();
                    }
                }
        );

        content.addView(
                commandCenter,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );
    }

    private void showCreate() {
        clear("Create Anything", "Choose what you want NOVA to create.");

        for (String item : NovaFeatures.CREATE_ANYTHING) {
            content.addView(card(
                    item,
                    "Create a new " + item + " project.",
                    v -> createProject(item)
            ));
        }
    }

    private void createProject(String type) {
        try {
            ProjectManager manager = new ProjectManager(this);

            String name = type.replace(" ", "_") + "_Project";

            File project = manager.createProject(name, type);

            Toast.makeText(
                    this,
                    "Created " + project.getName(),
                    Toast.LENGTH_SHORT
            ).show();

            showProjects();

        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "Project creation failed: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void showMegan() {
        clear("M3GAN", "NOVA's AI workspace");

        content.addView(card(
                "🧠 M3GAN Chat",
                "Talk to the local NOVA intelligence engine.",
                v -> showMeganChat()
        ));

        for (String feature : NovaFeatures.M3GAN) {
            if (!feature.equalsIgnoreCase("Chat")) {
                content.addView(card(
                        feature,
                        "Open " + feature + " inside M3GAN.",
                        v -> showFeature(feature)
                ));
            }
        }
    }

    private void showMeganChat() {
        clear("M3GAN", "AI Creation Engine");

        // Header
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setPadding(dp(18), dp(16), dp(18), dp(16));
        header.setBackground(background(
                NovaTheme.card(currentTheme), 20
        ));

        TextView name = text("🧠  M3GAN", 24, true);
        name.setTextColor(NovaTheme.accent(currentTheme));

        TextView description = text(
                "Your NOVA AI creation assistant",
                14,
                false
        );
        description.setTextColor(NovaTheme.muted(currentTheme));

        TextView provider = text(
                "● LOCAL AI  •  READY",
                12,
                true
        );
        provider.setTextColor(Color.rgb(5, 150, 105));

        header.addView(name);
        header.addView(description);
        header.addView(provider);

        content.addView(header);

        // Quick prompts
        TextView quickTitle = text("Quick prompts", 15, true);
        quickTitle.setPadding(0, dp(16), 0, dp(8));
        content.addView(quickTitle);

        LinearLayout prompts = new LinearLayout(this);
        prompts.setOrientation(LinearLayout.HORIZONTAL);

        String[] quick = {
                "Build an app",
                "Write code",
                "Teach me",
                "Plan a project"
        };

        final EditText[] inputHolder = new EditText[1];

        for (String prompt : quick) {
            Button q = new Button(this);
            q.setText(prompt);
            q.setTextSize(11);
            q.setAllCaps(false);
            q.setTextColor(NovaTheme.accent(currentTheme));
            q.setBackground(background(
                    NovaTheme.card(currentTheme), 14
            ));

            LinearLayout.LayoutParams qp =
                    new LinearLayout.LayoutParams(
                            0,
                            dp(46),
                            1
                    );

            qp.setMargins(dp(3), 0, dp(3), 0);
            prompts.addView(q, qp);

            q.setOnClickListener(v -> {
                if (inputHolder[0] != null) {
                    inputHolder[0].setText(prompt + " ");
                    inputHolder[0].setSelection(
                            inputHolder[0].length()
                    );
                    inputHolder[0].requestFocus();
                }
            });
        }

        content.addView(prompts);

        // Conversation
        LinearLayout conversation = new LinearLayout(this);
        conversation.setOrientation(LinearLayout.VERTICAL);
        conversation.setPadding(0, dp(14), 0, dp(10));

        ScrollView chatScroll = new ScrollView(this);
        chatScroll.setFillViewport(false);
        chatScroll.addView(conversation);

        content.addView(chatScroll, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(300)
        ));

        MeganEngine engine = new MeganEngine(this);

        // Restore history
        for (int i = 0;
             i < engine.getConversationHistory().length();
             i++) {

            try {
                org.json.JSONObject item =
                        engine.getConversationHistory()
                                .getJSONObject(i);

                addMeganMessage(
                        conversation,
                        "You",
                        item.optString("request"),
                        false
                );

                addMeganMessage(
                        conversation,
                        "M3GAN",
                        item.optString("response"),
                        true
                );

            } catch (Exception ignored) {
            }
        }

        // Input row
        LinearLayout inputRow = new LinearLayout(this);
        inputRow.setOrientation(LinearLayout.HORIZONTAL);
        inputRow.setGravity(Gravity.CENTER_VERTICAL);

        EditText input = new EditText(this);
        inputHolder[0] = input;

        input.setHint("Message M3GAN...");
        input.setTextSize(15);
        input.setTextColor(NovaTheme.text(currentTheme));
        input.setHintTextColor(NovaTheme.muted(currentTheme));
        input.setSingleLine(false);
        input.setMaxLines(4);
        input.setPadding(
                dp(14),
                dp(8),
                dp(14),
                dp(8)
        );
        input.setBackground(background(
                NovaTheme.card(currentTheme), 16
        ));

        Button send = new Button(this);
        send.setText("➤");
        send.setTextSize(20);
        send.setTextColor(Color.WHITE);
        send.setAllCaps(false);
        send.setTypeface(Typeface.DEFAULT_BOLD);
        send.setBackground(background(
                NovaTheme.accent(currentTheme), 16
        ));

        LinearLayout.LayoutParams inputParams =
                new LinearLayout.LayoutParams(
                        0,
                        dp(58),
                        1
                );

        LinearLayout.LayoutParams sendParams =
                new LinearLayout.LayoutParams(
                        dp(64),
                        dp(58)
                );

        sendParams.setMargins(dp(8), 0, 0, 0);

        inputRow.addView(input, inputParams);
        inputRow.addView(send, sendParams);

        content.addView(inputRow);

        // Controls
        LinearLayout controls = new LinearLayout(this);
        controls.setOrientation(LinearLayout.HORIZONTAL);

        Button clearChat = new Button(this);
        clearChat.setText("🧹 Clear");
        clearChat.setAllCaps(false);
        clearChat.setTextColor(NovaTheme.text(currentTheme));
        clearChat.setBackgroundColor(Color.TRANSPARENT);

        Button agentTask = new Button(this);
        agentTask.setText("🤖 Agent Task");
        agentTask.setAllCaps(false);
        agentTask.setTextColor(NovaTheme.accent(currentTheme));
        agentTask.setBackgroundColor(Color.TRANSPARENT);

        controls.addView(
                clearChat,
                new LinearLayout.LayoutParams(0, dp(48), 1)
        );

        controls.addView(
                agentTask,
                new LinearLayout.LayoutParams(0, dp(48), 1)
        );

        content.addView(controls);

        // Send
        send.setOnClickListener(v -> {
            String request = input.getText().toString().trim();

            if (request.isEmpty()) {
                return;
            }

            String response = engine.respond(request);
            String intent = engine.detectIntent(request);

            addMeganMessage(
                    conversation,
                    "You",
                    request,
                    false
            );

            addMeganMessage(
                    conversation,
                    "M3GAN  •  " + intent,
                    response,
                    true
            );

            input.setText("");

            chatScroll.post(() ->
                    chatScroll.fullScroll(View.FOCUS_DOWN)
            );
        });

        // Enter key sends when appropriate
        input.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null &&
                    event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER &&
                    event.getAction() == android.view.KeyEvent.ACTION_DOWN) {

                send.performClick();
                return true;
            }

            return false;
        });

        clearChat.setOnClickListener(v -> {
            engine.clearConversationHistory();
            conversation.removeAllViews();

            Toast.makeText(
                    this,
                    "Conversation cleared",
                    Toast.LENGTH_SHORT
            ).show();
        });

        agentTask.setOnClickListener(v -> {
            String request = input.getText().toString().trim();

            if (request.isEmpty()) {
                Toast.makeText(
                        this,
                        "Enter a task first",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            org.json.JSONObject task =
                    engine.createAgentTask(request);

            Toast.makeText(
                    this,
                    "Agent task queued • " +
                            task.optString(
                                    "intent",
                                    "conversation"
                            ),
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    private void addMeganMessage(
            LinearLayout conversation,
            String sender,
            String message,
            boolean assistant
    ) {
        LinearLayout bubble = new LinearLayout(this);
        bubble.setOrientation(LinearLayout.VERTICAL);
        bubble.setPadding(
                dp(14),
                dp(10),
                dp(14),
                dp(10)
        );

        int backgroundColor = assistant
                ? NovaTheme.card(currentTheme)
                : NovaTheme.accent(currentTheme);

        bubble.setBackground(
                background(backgroundColor, 16)
        );

        TextView name = text(
                sender,
                12,
                true
        );

        name.setTextColor(
                assistant
                        ? NovaTheme.accent(currentTheme)
                        : Color.WHITE
        );

        TextView body = text(
                message,
                14,
                false
        );

        body.setTextColor(
                assistant
                        ? NovaTheme.text(currentTheme)
                        : Color.WHITE
        );

        body.setPadding(0, dp(4), 0, 0);

        bubble.addView(name);
        bubble.addView(body);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                0,
                0,
                dp(10)
        );

        conversation.addView(bubble, params);
    }

    private void showProjects() {
        clear("Projects", "Your NOVA workspace");

        ProjectManager manager = new ProjectManager(this);
        List<File> projects = manager.getProjects();

        if (projects.isEmpty()) {
            content.addView(card(
                    "No projects yet",
                    "Create your first project using Quick Create.",
                    v -> showCreate()
            ));
            return;
        }

        for (File project : projects) {
            String name = project.getName();

            content.addView(card(
                    "📦 " + name,
                    "Open project workspace.",
                    v -> showProject(project)
            ));
        }
    }

    private void showProject(File project) {
        clear(project.getName(), "Project workspace");

        content.addView(card(
                "🧠 Project Brain",
                "View the project's planning and intelligence foundation.",
                v -> showFeature("Project Brain")
        ));

        content.addView(card(
                "📂 File Explorer",
                "Browse project files and folders.",
                v -> showFeature("File Explorer")
        ));

        content.addView(card(
                "⌨️ Code Editor",
                "Open the project's code workspace.",
                v -> showFeature("Code Editor")
        ));

        content.addView(card(
                "🧪 Test",
                "Run project tests.",
                v -> showFeature("Test")
        ));

        content.addView(card(
                "🔨 Build & Export",
                "Build project outputs.",
                v -> showFeature("Build & Export")
        ));

        content.addView(card(
                "🕘 Versions",
                "Manage project versions.",
                v -> showFeature("Versions")
        ));
    }

    private void showSettings() {
        clear("Settings", "Configure your NOVA workspace");

        content.addView(card(
                "🎨 Themes",
                "Choose the visual theme used throughout NOVA.",
                v -> showThemes()
        ));

        content.addView(card(
                "🌗 Appearance",
                "Current theme: " + currentTheme,
                v -> showThemes()
        ));

        content.addView(card(
                "🤖 M3GAN",
                "Configure AI behaviour and preferences.",
                v -> showFeature("M3GAN Settings")
        ));

        content.addView(card(
                "🔒 Privacy",
                "Manage privacy and local application data.",
                v -> showFeature("Privacy")
        ));

        content.addView(card(
                "🛡 Security",
                "Review NOVA security controls.",
                v -> showFeature("Security")
        ));

        content.addView(card(
                "💾 Storage",
                "View NOVA workspace storage.",
                v -> showFeature("Storage")
        ));

        content.addView(card(
                "ℹ️ About NOVA",
                "NOVA native Android workspace.",
                v -> showFeature("About NOVA")
        ));
    }

    private void showThemes() {
        clear("Themes", "Choose a NOVA visual theme");

        for (String theme : ThemeRegistry.all()) {

            boolean selected = theme.equals(currentTheme);

            content.addView(card(
                    (selected ? "✓ " : "") + theme,
                    selected
                            ? "Currently active"
                            : "Tap to apply this theme.",
                    v -> {
                        NovaTheme.set(this, theme);
                        currentTheme = theme;
                        buildUI();
                        Toast.makeText(
                                this,
                                theme + " applied",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
            ));
        }
    }

    private void showFeatureGroup(String name, java.util.List<String> features) {
        clear(name, "NOVA workspace");

        for (String feature : features) {
            content.addView(card(
                    feature,
                    "Open " + feature + ".",
                    v -> showFeature(feature)
            ));
        }
    }

    private void showFeature(String feature) {
        if ("Voice".equalsIgnoreCase(feature)) {
            showMeganVoice();
            return;
        }

        clear(feature, "NOVA feature workspace");

        content.addView(card(
                "NOVA " + feature,
                "This workspace is registered and ready for its implementation layer.",
                null
        ));

        content.addView(button(
                "← Back to Home",
                v -> showHome()
        ));
    }

    private void showMeganVoice() {
        clear("M3GAN Voice", "Speak naturally with NOVA");

        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setGravity(Gravity.CENTER_HORIZONTAL);
        panel.setPadding(
                dp(20),
                dp(24),
                dp(20),
                dp(20)
        );
        panel.setBackground(
                background(NovaTheme.card(currentTheme), 22)
        );

        TextView icon = text("🎙️", 52, false);
        icon.setGravity(Gravity.CENTER);

        TextView heading = text(
                "Talk to M3GAN",
                24,
                true
        );
        heading.setGravity(Gravity.CENTER);

        TextView status = text(
                "Tap the microphone and speak",
                14,
                false
        );
        status.setTextColor(NovaTheme.muted(currentTheme));
        status.setGravity(Gravity.CENTER);

        TextView transcript = text(
                "Your speech will appear here.",
                16,
                false
        );
        transcript.setTextColor(NovaTheme.text(currentTheme));
        transcript.setGravity(Gravity.CENTER);
        transcript.setPadding(
                dp(10),
                dp(20),
                dp(10),
                dp(20)
        );

        Button microphone = button(
                "🎙️  Start Listening",
                v -> startMeganVoice(status, transcript)
        );

        Button speak = button(
                "🔊  Speak Last Response",
                v -> {
                    String value = transcript.getText().toString();

                    if (!value.isEmpty() &&
                            !value.equals("Your speech will appear here.")) {
                        speakMegan(value);
                    }
                }
        );

        panel.addView(
                icon,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(80)
                )
        );

        panel.addView(
                heading,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(45)
                )
        );

        panel.addView(
                status,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(35)
                )
        );

        panel.addView(
                transcript,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(110)
                )
        );

        panel.addView(microphone);
        panel.addView(speak);

        content.addView(panel);

        content.addView(button(
                "← Back to M3GAN",
                v -> showMegan()
        ));

        if (novaTts == null) {
            novaTts = new TextToSpeech(
                    this,
                    result -> {
                        if (result != TextToSpeech.ERROR) {
                            novaTts.setLanguage(
                                    java.util.Locale.getDefault()
                            );
                        }
                    }
            );
        }
    }

    private void startMeganVoice(
            TextView status,
            TextView transcript
    ) {
        Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                java.util.Locale.getDefault()
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "Speak to M3GAN"
        );

        status.setText("● Listening...");

        try {
            startActivityForResult(
                    intent,
                    VOICE_REQUEST
            );
        } catch (Exception e) {
            status.setText("Voice recognition unavailable");
            Toast.makeText(
                    this,
                    "No speech recognition service is available.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void speakMegan(String text) {
        if (novaTts == null) {
            novaTts = new TextToSpeech(
                    this,
                    result -> {
                        if (result != TextToSpeech.ERROR) {
                            novaTts.setLanguage(
                                    java.util.Locale.getDefault()
                            );
                            novaTts.speak(
                                    text,
                                    TextToSpeech.QUEUE_FLUSH,
                                    null,
                                    "NOVA_M3GAN"
                            );
                        }
                    }
            );
            return;
        }

        novaTts.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "NOVA_M3GAN"
        );
    }

    @Override
    protected void onDestroy() {
        if (novaTts != null) {
            novaTts.stop();
            novaTts.shutdown();
            novaTts = null;
        }

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {
        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode != VOICE_REQUEST ||
                resultCode != RESULT_OK ||
                data == null) {
            return;
        }

        java.util.ArrayList<String> results =
                data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS
                );

        if (results == null || results.isEmpty()) {
            return;
        }

        String request = results.get(0);

        MeganEngine engine = new MeganEngine(this);
        String response = engine.respond(request);

        showMeganVoice();

        Toast.makeText(
                this,
                "M3GAN: " + response,
                Toast.LENGTH_LONG
        ).show();

        speakMegan(response);
    }

    private LinearLayout bottomNavigation() {
        LinearLayout nav = new LinearLayout(this);
        nav.setOrientation(LinearLayout.HORIZONTAL);
        nav.setGravity(Gravity.CENTER);
        nav.setPadding(dp(6), dp(6), dp(6), dp(6));
        nav.setBackgroundColor(NovaTheme.card(currentTheme));

        String[] items = {"HOME", "M3GAN", "CODE", "PROJECTS", "MORE"};

        for (String item : items) {
            Button b = new Button(this);
            b.setText(item);
            b.setTextSize(11);
            b.setAllCaps(false);
            b.setTextColor(NovaTheme.text(currentTheme));
            b.setBackgroundColor(Color.TRANSPARENT);

            b.setOnClickListener(v -> {
                if (item.equals("HOME")) showHome();
                else if (item.equals("M3GAN")) showMegan();
                else if (item.equals("CODE"))
                    showFeatureGroup("CODING HUB", NovaFeatures.CODING_HUB);
                else if (item.equals("PROJECTS")) showProjects();
                else showSettings();
            });

            nav.addView(b, new LinearLayout.LayoutParams(
                    0, dp(52), 1
            ));
        }

        return nav;
    }
}
