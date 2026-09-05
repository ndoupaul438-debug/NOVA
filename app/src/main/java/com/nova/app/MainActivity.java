package com.nova.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    LinearLayout root;
    LinearLayout content;
    int primary = Color.rgb(37, 99, 235);
    int background = Color.rgb(246, 248, 252);
    int card = Color.WHITE;
    int text = Color.rgb(20, 25, 35);
    int muted = Color.rgb(100, 110, 125);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showHome();
    }

    TextView label(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setTypeface(Typeface.DEFAULT, bold ? Typeface.BOLD : Typeface.NORMAL);
        t.setPadding(4, 4, 4, 4);
        return t;
    }

    GradientDrawable bg(int color, float radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(radius);
        return g;
    }

    Button button(String title, View.OnClickListener listener) {
        Button b = new Button(this);
        b.setText(title);
        b.setTextSize(14);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setGravity(Gravity.CENTER);
        b.setBackground(bg(primary, 24));
        b.setOnClickListener(listener);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 52);
        p.setMargins(0, 6, 0, 6);
        b.setLayoutParams(p);

        return b;
    }

    LinearLayout page() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(background);

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(18, 14, 18, 14);
        header.setBackgroundColor(Color.WHITE);

        TextView logo = label("NOVA", 25, primary, true);
        header.addView(logo, new LinearLayout.LayoutParams(0, 60, 1));

        TextView menu = label("☰", 27, text, true);
        header.addView(menu, new LinearLayout.LayoutParams(55, 60));

        root.addView(header);

        ScrollView scroll = new ScrollView(this);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(18, 18, 18, 90);
        scroll.addView(content);

        root.addView(scroll,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        LinearLayout nav = new LinearLayout(this);
        nav.setGravity(Gravity.CENTER);
        nav.setPadding(4, 5, 4, 5);
        nav.setBackgroundColor(Color.WHITE);

        addNav(nav, "⌂", "Home", v -> showHome());
        addNav(nav, "✦", "M3GAN", v -> showM3GAN());
        addNav(nav, "⌘", "Code", v -> showCoding());
        addNav(nav, "▣", "Projects", v -> showProjects());
        addNav(nav, "☷", "More", v -> showMore());

        root.addView(nav,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 68));

        setContentView(root);
        return root;
    }

    void addNav(LinearLayout nav, String icon, String name,
                View.OnClickListener click) {
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);

        TextView i = label(icon, 21, primary, true);
        TextView n = label(name, 11, muted, false);

        item.addView(i);
        item.addView(n);

        item.setOnClickListener(click);

        nav.addView(item,
                new LinearLayout.LayoutParams(0, 60, 1));
    }

    void title(String heading, String subtitle) {
        content.addView(label(heading, 29, text, true));
        content.addView(label(subtitle, 15, muted, false));

        Space s = new Space(this);
        content.addView(s, new LinearLayout.LayoutParams(1, 16));
    }

    void card(String heading, String description, String action,
              View.OnClickListener listener) {

        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(18, 18, 18, 18);
        c.setBackground(bg(card, 28));

        TextView h = label(heading, 19, text, true);
        TextView d = label(description, 13, muted, false);

        c.addView(h);
        c.addView(d);

        if (action != null) {
            Button b = button(action, listener);
            c.addView(b);
        }

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        p.setMargins(0, 0, 0, 12);

        content.addView(c, p);
    }

    void showHome() {
        page();
        title("Good to see you.", "Your creative command center.");

        card(
                "✨ CREATE ANYTHING",
                "Turn an idea into an app, game, website, AI project or more.",
                "Start Creating",
                v -> showCreateAnything()
        );

        card(
                "🤖 M3GAN",
                "Your AI workspace for conversation, coding, learning and creation.",
                "Open M3GAN",
                v -> showM3GAN()
        );

        card(
                "💻 CODING HUB",
                "Build apps, games, websites, APIs, AI systems and scripts.",
                "Open Coding Hub",
                v -> showCoding()
        );

        card(
                "📁 PROJECTS",
                "Manage projects, files, builds, backups and versions.",
                "Open Projects",
                v -> showProjects()
        );

        title("Explore NOVA", "");

        card("📚 Learn",
                "AI tutoring, notes, quizzes, study plans and learning tools.",
                "Open Learn",
                v -> showLearn());

        card("🎨 Create Studio",
                "Design logos, interfaces, documents and creative assets.",
                "Open Create Studio",
                v -> showCreate());

        card("🛠 Tools",
                "Data, files, converters and developer utilities.",
                "Open Tools",
                v -> showTools());
    }

    void showCreateAnything() {
        page();
        title("✨ Create Anything", "Describe what you want to build.");

        EditText idea = new EditText(this);
        idea.setHint("Example: Build a football game...");
        idea.setTextSize(16);
        idea.setGravity(Gravity.TOP);
        idea.setPadding(18, 18, 18, 18);
        idea.setBackground(bg(Color.WHITE, 25));

        content.addView(idea,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 150));

        content.addView(button("✨ Ask M3GAN to Create", v -> {
            Toast.makeText(this,
                    "Creation blueprint started.",
                    Toast.LENGTH_SHORT).show();
        }));

        title("Creation pipeline", "");

        card("1. Idea", "Understand your goal and requirements.", null, null);
        card("2. Blueprint", "Plan architecture, files and features.", null, null);
        card("3. Build", "Generate the project and source files.", null, null);
        card("4. Test & Fix", "Check the project and identify problems.", null, null);
        card("5. Export", "Prepare APK, AAB, web or ZIP output.", null, null);
    }

    void showM3GAN() {
        page();
        title("🤖 M3GAN", "Your AI command center.");

        card("New conversation",
                "Chat with M3GAN about coding, learning, ideas and projects.",
                "Start Chat",
                v -> Toast.makeText(this,
                        "M3GAN chat workspace ready.",
                        Toast.LENGTH_SHORT).show());

        card("🎤 Voice",
                "Voice input and spoken responses.",
                "Voice Mode",
                v -> Toast.makeText(this,
                        "Voice mode foundation ready.",
                        Toast.LENGTH_SHORT).show());

        card("🧠 Project Brain",
                "Keep project goals, files, architecture and tasks connected.",
                "Project Context",
                v -> showProjects());

        card("📄 Documents",
                "Work with project documents and attachments.",
                "Open Documents",
                v -> showTools());

        card("🔎 Research",
                "Research and analysis workspace.",
                "Research",
                v -> Toast.makeText(this,
                        "Research workspace ready.",
                        Toast.LENGTH_SHORT).show());
    }

    void showCoding() {
        page();
        title("💻 Coding Hub", "Build almost anything.");

        String[][] tools = {
                {"📱 App Studio", "Create Android applications."},
                {"🎮 Game Studio", "Build 2D and 3D games."},
                {"🌐 Website Studio", "Create responsive websites."},
                {"🖥 Web App Studio", "Build full web applications."},
                {"🧠 AI Studio", "Create AI-powered projects."},
                {"🔌 API Studio", "Design APIs and services."},
                {"🐍 Python Studio", "Build scripts and data tools."},
                {"⌨ Code Editor", "Edit project source code."},
                {"📂 File Explorer", "Manage project files."},
                {"▣ Terminal", "Developer command workspace."},
                {"▶ Preview", "Preview projects."},
                {"🔨 Build & Export", "Build APK, AAB, web and ZIP."}
        };

        for (String[] x : tools) {
            card(x[0], x[1], "Open", v ->
                    Toast.makeText(this,
                            x[0] + " selected.",
                            Toast.LENGTH_SHORT).show());
        }
    }

    void showProjects() {
        page();
        title("📁 Projects", "Everything you create in NOVA.");

        card("All Projects",
                "Apps, games, websites, AI projects, APIs and scripts.",
                "Browse Projects",
                v -> Toast.makeText(this,
                        "Project browser ready.",
                        Toast.LENGTH_SHORT).show());

        card("📦 Builds",
                "APK, AAB, web and ZIP outputs.",
                "Open Builds",
                v -> Toast.makeText(this,
                        "Build manager ready.",
                        Toast.LENGTH_SHORT).show());

        card("☁️ Backup",
                "Back up and restore project data.",
                "Backup",
                v -> Toast.makeText(this,
                        "Backup manager ready.",
                        Toast.LENGTH_SHORT).show());

        card("⏱ Version History",
                "Track project changes and restore versions.",
                "History",
                v -> Toast.makeText(this,
                        "Version history ready.",
                        Toast.LENGTH_SHORT).show());

        card("🔗 GitHub",
                "Connect projects to Git repositories.",
                "GitHub",
                v -> Toast.makeText(this,
                        "GitHub workspace ready.",
                        Toast.LENGTH_SHORT).show());
    }

    void showLearn() {
        page();
        title("📚 Learn", "Learn with M3GAN.");

        card("AI Tutor", "Personal tutoring and explanations.",
                "Start Learning", v -> toast("AI Tutor"));

        card("📝 Notes", "Create and organize study notes.",
                "Open Notes", v -> toast("Notes"));

        card("🧠 Quizzes", "Practice and track your scores.",
                "Take Quiz", v -> toast("Quizzes"));

        card("🗂 Flashcards", "Study with active recall.",
                "Open Flashcards", v -> toast("Flashcards"));

        card("🎯 Study Plans", "Create structured study schedules.",
                "Create Plan", v -> toast("Study Plans"));

        card("📈 Progress", "Track your learning progress.",
                "View Progress", v -> toast("Progress"));
    }

    void showCreate() {
        page();
        title("🎨 Create Studio", "Turn ideas into creative work.");

        card("🎨 Design", "UI, layouts, colors and design systems.",
                "Open Design", v -> toast("Design Studio"));

        card("🖼 Image Studio", "Create and manage visual assets.",
                "Open Images", v -> toast("Image Studio"));

        card("🏷 Logo & Icons", "Create branding assets.",
                "Open Branding", v -> toast("Branding Studio"));

        card("📄 Documents", "Create professional documents.",
                "Open Documents", v -> toast("Documents"));

        card("📊 Presentations", "Build presentation projects.",
                "Open Presentations", v -> toast("Presentations"));
    }

    void showTools() {
        page();
        title("🛠 Tools", "Useful utilities inside NOVA.");

        card("📊 Data Analysis", "Analyze CSV and structured data.",
                "Open Data Tools", v -> toast("Data Analysis"));

        card("📄 PDF Tools", "Work with PDF documents.",
                "Open PDF Tools", v -> toast("PDF Tools"));

        card("🔄 Converters", "Convert supported project formats.",
                "Open Converters", v -> toast("Converters"));

        card("🗃 File Tools", "Inspect and organize files.",
                "Open File Tools", v -> toast("File Tools"));
    }

    void showMore() {
        page();
        title("NOVA", "Everything else.");

        card("🛍 Marketplace",
                "Templates, plugins, themes, agents and assets.",
                "Open Marketplace", v -> toast("Marketplace"));

        card("🛡 Security",
                "Permissions, privacy, API keys and security checks.",
                "Security Center", v -> toast("Security Center"));

        card("☁️ Cloud & GitHub",
                "Sync, backup, sharing and repositories.",
                "Cloud", v -> toast("Cloud"));

        card("🎨 Themes",
                "Customize the NOVA interface.",
                "Theme Center", v -> toast("Themes"));

        card("⚙️ Settings",
                "Configure NOVA and M3GAN.",
                "Settings", v -> toast("Settings"));

        card("ℹ️ About NOVA",
                "NOVA creative and development platform.",
                "About", v -> toast("NOVA"));
    }

    void toast(String s) {
        Toast.makeText(this, s + " selected.", Toast.LENGTH_SHORT).show();
    }
}
