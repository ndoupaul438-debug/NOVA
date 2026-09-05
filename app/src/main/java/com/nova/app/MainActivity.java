package com.nova.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends Activity {

    LinearLayout root;
    LinearLayout content;
    boolean darkMode = false;

    int primary = Color.rgb(37, 99, 235);

    String currentTheme = "Ocean";

    Map<String, int[]> themes = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupThemes();
        buildApp();
    }

    private void setupThemes() {

        themes.put("Ocean", new int[]{
                Color.rgb(37,99,235),
                Color.rgb(6,182,212)
        });

        themes.put("Royal Purple", new int[]{
                Color.rgb(124,58,237),
                Color.rgb(236,72,153)
        });

        themes.put("Emerald", new int[]{
                Color.rgb(5,150,105),
                Color.rgb(52,211,153)
        });

        themes.put("Sunset", new int[]{
                Color.rgb(249,115,22),
                Color.rgb(239,68,68)
        });

        themes.put("Crimson", new int[]{
                Color.rgb(220,38,38),
                Color.rgb(244,63,94)
        });

        themes.put("Cyber", new int[]{
                Color.rgb(8,145,178),
                Color.rgb(34,211,238)
        });

        themes.put("Midnight", new int[]{
                Color.rgb(79,70,229),
                Color.rgb(139,92,246)
        });

        themes.put("Aurora", new int[]{
                Color.rgb(20,184,166),
                Color.rgb(139,92,246)
        });
    }

    private void buildApp() {

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        applyBackground();

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(20,24,20,15);

        LinearLayout brand = new LinearLayout(this);
        brand.setOrientation(LinearLayout.VERTICAL);

        TextView logo = text("NOVA ✦", 28, true);
        TextView tagline = text("AI • CREATE • LEARN • BUILD", 11, false);

        brand.addView(logo);
        brand.addView(tagline);

        header.addView(
                brand,
                new LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1)
        );

        Button settings = button("⚙");
        settings.setOnClickListener(v -> showSettings());

        header.addView(settings);

        root.addView(header);

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(18,10,18,10);

        ScrollView scroll = new ScrollView(this);
        scroll.addView(content);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,1)
        );

        LinearLayout nav = new LinearLayout(this);
        nav.setGravity(Gravity.CENTER);

        addNav(nav,"AI",v -> showAI());
        addNav(nav,"Build",v -> showCodingHub());
        addNav(nav,"Learn",v -> showPage(
                "NOVA Learn",
                "AI tutoring, quizzes, study plans and knowledge tools."
        ));
        addNav(nav,"Create",v -> showCreate());
        addNav(nav,"Profile",v -> showProfile());

        root.addView(nav);

        setContentView(root);

        showHome();
    }

    private void showHome() {

        content.removeAllViews();

        TextView welcome = text(
                "Welcome to NOVA",
                27,
                true
        );

        content.addView(welcome);

        content.addView(text(
                "Your AI-powered creation and learning workspace.",
                16,false
        ));

        addSpace(15);

        EditText prompt = new EditText(this);
        prompt.setHint("Tell M3GAN what you want to create...");
        prompt.setPadding(20,18,20,18);

        content.addView(prompt);

        Button create = button("✦  Create with M3GAN");

        create.setOnClickListener(v -> {

            String idea = prompt.getText().toString().trim();

            if(idea.isEmpty()) {
                showAI();
            } else {
                showPage(
                        "M3GAN",
                        "Project request received:\n\n" +
                        idea +
                        "\n\nM3GAN will turn this into a project blueprint."
                );
            }
        });

        content.addView(create);

        addSpace(15);

        addCard("🤖 M3GAN",
                "Your conversational AI for questions, coding, learning and creation.",
                v -> showAI());

        addCard("💻 Coding Hub",
                "Build apps, games, websites, AI projects and developer tools.",
                v -> showCodingHub());

        addCard("🎨 Create Studio",
                "Create designs, documents, presentations and digital content.",
                v -> showCreate());

        addCard("📚 Learn",
                "Study with an AI tutor, quizzes and personalized learning.",
                v -> showPage(
                        "NOVA Learn",
                        "Your personalized AI learning center."
                ));

        addCard("🚀 Projects",
                "Manage everything you create inside NOVA.",
                v -> showProjects());
    }

    private void showAI() {

        content.removeAllViews();

        content.addView(text("M3GAN",30,true));
        content.addView(text(
                "Your NOVA conversational AI",
                16,false
        ));

        addSpace(15);

        TextView status = text(
                "● M3GAN ready",
                15,true
        );

        status.setTextColor(Color.rgb(16,185,129));

        content.addView(status);

        EditText message = new EditText(this);
        message.setHint("Message M3GAN...");
        message.setMinLines(3);

        content.addView(message);

        Button send = button("Send");

        send.setOnClickListener(v -> {

            String q = message.getText().toString().trim();

            if(!q.isEmpty()) {

                showPage(
                        "M3GAN",
                        "You:\n" + q +
                        "\n\nM3GAN:\n" +
                        "I'm ready to help. Connect the NOVA AI backend to enable full conversational intelligence."
                );
            }
        });

        content.addView(send);

        addSpace(15);

        addCard("💻 Coding Assistant",
                "Ask M3GAN to create or debug code.",
                v -> showCodingHub());

        addCard("🧠 Study Assistant",
                "Ask questions and learn step by step.",
                v -> showPage(
                        "M3GAN Study Mode",
                        "Your AI tutor workspace."
                ));

        addCard("🔎 Research",
                "Research and organize information.",
                v -> showPage(
                        "M3GAN Research",
                        "Research workspace ready."
                ));
    }

    private void showCodingHub() {

        content.removeAllViews();

        content.addView(text("Coding Hub",30,true));
        content.addView(text(
                "Create anything with M3GAN",
                16,false
        ));

        addSpace(15);

        addCard("📱 App Studio",
                "Create Android applications.",
                v -> createProject("Android App"));

        addCard("🎮 Game Studio",
                "Create 2D and 3D games.",
                v -> createProject("Game"));

        addCard("🌐 Website Studio",
                "Create websites with HTML, CSS and JavaScript.",
                v -> createProject("Website"));

        addCard("⚛ Web App Studio",
                "Build modern web applications.",
                v -> createProject("Web App"));

        addCard("🤖 AI Studio",
                "Create AI-powered projects and agents.",
                v -> createProject("AI Project"));

        addCard("🔌 API Studio",
                "Create backend services and APIs.",
                v -> createProject("API"));

        addCard("🐍 Python Studio",
                "Create Python tools and automation.",
                v -> createProject("Python Project"));

        addCard("📂 Project Explorer",
                "Open and manage your NOVA projects.",
                v -> showProjects());
    }

    private void createProject(String type) {

        content.removeAllViews();

        content.addView(text(
                "Create " + type,
                28,true
        ));

        content.addView(text(
                "Describe what you want M3GAN to build.",
                16,false
        ));

        addSpace(12);

        EditText idea = new EditText(this);
        idea.setHint(
                "Example: Create a football game with career mode..."
        );
        idea.setMinLines(5);

        content.addView(idea);

        Button generate = button("🚀 Generate Project");

        generate.setOnClickListener(v -> {

            String description =
                    idea.getText().toString().trim();

            if(description.isEmpty()) {
                description = "New " + type;
            }

            showPage(
                    "Project Blueprint",
                    "Type: " + type +
                    "\n\nIdea:\n" + description +
                    "\n\nProject workspace created.\n\n" +
                    "Next: M3GAN generates the project files."
            );
        });

        content.addView(generate);

        Button back = button("← Coding Hub");
        back.setOnClickListener(v -> showCodingHub());

        content.addView(back);
    }

    private void showCreate() {

        content.removeAllViews();

        content.addView(text("Create Studio",30,true));

        addCard("🎨 Design",
                "Posters, graphics, branding and UI concepts.",
                v -> showPage("Design Studio",
                        "Creative design workspace."));

        addCard("📄 Documents",
                "Create reports, presentations and documents.",
                v -> showPage("Document Studio",
                        "Document creation workspace."));

        addCard("🖼 Image Studio",
                "Create and organize visual assets.",
                v -> showPage("Image Studio",
                        "Image creation workspace."));
    }

    private void showProjects() {

        content.removeAllViews();

        content.addView(text("My Projects",30,true));

        addCard("⚽ Football Game",
                "Game project workspace.",
                v -> showPage("Football Game",
                        "Project files and development tools."));

        addCard("🌐 My Website",
                "Website project workspace.",
                v -> showPage("My Website",
                        "HTML, CSS and JavaScript workspace."));

        addCard("📱 Android App",
                "Android application workspace.",
                v -> showPage("Android App",
                        "Android project workspace."));
    }

    private void showProfile() {

        content.removeAllViews();

        content.addView(text("NOVA Profile",30,true));

        addCard("🏆 Achievements",
                "Track projects, skills and milestones.",
                v -> showPage(
                        "Achievements",
                        "Your NOVA achievements will appear here."
                ));

        addCard("📊 Progress",
                "Track learning and creation progress.",
                v -> showPage(
                        "Progress",
                        "Your progress dashboard."
                ));
    }

    private void showSettings() {

        content.removeAllViews();

        content.addView(text("Settings",30,true));

        Button theme = button("🎨 Theme Center");

        theme.setOnClickListener(v -> showThemes());

        content.addView(theme);

        Button dark = button(
                darkMode ? "☀ Light Mode" : "🌙 Dark Mode"
        );

        dark.setOnClickListener(v -> {

            darkMode = !darkMode;
            applyBackground();
            showSettings();
        });

        content.addView(dark);

        addCard("🤖 M3GAN Settings",
                "AI personality, voice and behavior.",
                v -> showPage(
                        "M3GAN Settings",
                        "AI configuration center."
                ));

        addCard("🔐 Privacy & Security",
                "Permissions, privacy and security controls.",
                v -> showPage(
                        "Security Center",
                        "NOVA security controls."
                ));
    }

    private void showThemes() {

        content.removeAllViews();

        content.addView(text(
                "Theme Center",
                30,true
        ));

        content.addView(text(
                "Choose your NOVA experience",
                16,false
        ));

        for(String name : themes.keySet()) {

            Button b = button(
                    name.equals(currentTheme)
                            ? "✓ " + name
                            : name
            );

            b.setOnClickListener(v -> {

                currentTheme = name;

                primary = themes.get(name)[0];

                showThemes();
            });

            content.addView(b);
        }

        Button back = button("← Settings");
        back.setOnClickListener(v -> showSettings());

        content.addView(back);
    }

    private void showPage(
            String title,
            String description) {

        content.removeAllViews();

        content.addView(text(title,30,true));

        addSpace(10);

        content.addView(text(
                description,
                17,false
        ));

        addSpace(20);

        Button back = button("← Home");

        back.setOnClickListener(v -> showHome());

        content.addView(back);
    }

    private void addCard(
            String title,
            String description,
            View.OnClickListener listener) {

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(20,18,20,18);

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(
                darkMode
                        ? Color.rgb(30,35,45)
                        : Color.WHITE
        );

        bg.setCornerRadius(22);

        card.setBackground(bg);

        TextView t = text(title,19,true);
        TextView d = text(description,14,false);

        card.addView(t);
        card.addView(d);

        card.setOnClickListener(listener);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        p.setMargins(0,7,0,7);

        content.addView(card,p);
    }

    private void addNav(
            LinearLayout nav,
            String label,
            View.OnClickListener listener) {

        Button b = button(label);
        b.setTextSize(11);
        b.setOnClickListener(listener);

        nav.addView(
                b,
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                )
        );
    }

    private Button button(String value) {

        Button b = new Button(this);

        b.setText(value);
        b.setAllCaps(false);

        return b;
    }

    private TextView text(
            String value,
            float size,
            boolean bold) {

        TextView t = new TextView(this);

        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(
                darkMode
                        ? Color.WHITE
                        : Color.rgb(25,30,40)
        );

        if(bold)
            t.setTypeface(null, Typeface.BOLD);

        t.setPadding(0,5,0,5);

        return t;
    }

    private void addSpace(int size) {

        Space s = new Space(this);

        content.addView(
                s,
                new LinearLayout.LayoutParams(
                        1,size
                )
        );
    }

    private void applyBackground() {

        if(root == null) return;

        root.setBackgroundColor(
                darkMode
                        ? Color.rgb(12,15,22)
                        : Color.rgb(246,248,252)
        );
    }
}
