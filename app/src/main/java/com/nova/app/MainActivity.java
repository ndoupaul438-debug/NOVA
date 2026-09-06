package com.nova.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import com.nova.app.core.NovaFeatures;

import java.util.List;

public class MainActivity extends Activity {

    private LinearLayout root;
    private LinearLayout content;
    private TextView title;
    private int blue = Color.rgb(37, 99, 235);
    private int dark = Color.rgb(15, 23, 42);
    private int light = Color.rgb(248, 250, 252);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showHome();
    }

    private TextView text(String value, float size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        t.setTypeface(Typeface.DEFAULT, bold ? Typeface.BOLD : Typeface.NORMAL);
        t.setPadding(22, 14, 22, 14);
        return t;
    }

    private GradientDrawable background(int color, float radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(radius);
        return g;
    }

    private Button button(String name) {
        Button b = new Button(this);
        b.setText(name);
        b.setTextSize(15);
        b.setTextColor(dark);
        b.setAllCaps(false);
        b.setGravity(Gravity.CENTER_VERTICAL);
        b.setPadding(18, 8, 18, 8);
        b.setBackground(background(Color.WHITE, 28));
        b.setElevation(3);
        return b;
    }

    private void base(String screenTitle) {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(light);

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(12, 10, 12, 10);
        header.setBackgroundColor(Color.WHITE);

        Button back = new Button(this);
        back.setText("‹");
        back.setTextSize(30);
        back.setTextColor(dark);
        back.setBackgroundColor(Color.TRANSPARENT);
        back.setOnClickListener(v -> showHome());

        title = text(screenTitle, 21, dark, true);

        header.addView(back, new LinearLayout.LayoutParams(55, 60));
        header.addView(title, new LinearLayout.LayoutParams(
                0, 60, 1
        ));

        root.addView(header);

        ScrollView scroll = new ScrollView(this);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(16, 18, 16, 90);
        scroll.addView(content);

        root.addView(scroll, new LinearLayout.LayoutParams(
                -1, 0, 1
        ));

        bottomNavigation();
        setContentView(root);
    }

    private void bottomNavigation() {
        LinearLayout nav = new LinearLayout(this);
        nav.setGravity(Gravity.CENTER);
        nav.setPadding(4, 5, 4, 5);
        nav.setBackgroundColor(Color.WHITE);

        String[] names = {"HOME", "M3GAN", "CODE", "PROJECTS", "MORE"};

        for (String name : names) {
            Button b = button(name);
            b.setTextSize(11);
            b.setPadding(3, 2, 3, 2);

            if (name.equals("HOME")) b.setOnClickListener(v -> showHome());
            if (name.equals("M3GAN")) b.setOnClickListener(v -> showSection("M3GAN", NovaFeatures.M3GAN));
            if (name.equals("CODE")) b.setOnClickListener(v -> showSection("CODING HUB", NovaFeatures.CODING_HUB));
            if (name.equals("PROJECTS")) b.setOnClickListener(v -> showSection("PROJECTS", NovaFeatures.PROJECTS));
            if (name.equals("MORE")) b.setOnClickListener(v -> showMore());

            nav.addView(b, new LinearLayout.LayoutParams(0, 58, 1));
        }

        root.addView(nav);
    }

    private void showHome() {
        base("NOVA");

        TextView welcome = text(
                "NOVA\nYour creation command center",
                25, dark, true
        );
        welcome.setPadding(8, 10, 8, 22);
        content.addView(welcome);

        addCard("⚡ Quick Create",
                "Create an App, Game, Website, Web App, AI, API or Script.",
                v -> showSection("CREATE ANYTHING", NovaFeatures.CREATE_ANYTHING));

        addCard("🤖 M3GAN",
                "AI chat, coding, tutor, research, documents and Project Brain.",
                v -> showSection("M3GAN", NovaFeatures.M3GAN));

        addCard("💻 Coding Hub",
                "Build and test apps, games, websites and AI projects.",
                v -> showSection("CODING HUB", NovaFeatures.CODING_HUB));

        addCard("🎮 Game Studio",
                "Create 2D/3D games, characters, maps, physics and more.",
                v -> showSection("GAME STUDIO", NovaFeatures.GAME_STUDIO));

        addCard("🌐 Website Studio",
                "Build websites and web apps with HTML, CSS, JavaScript or React.",
                v -> showSection("WEBSITE STUDIO", NovaFeatures.WEBSITE_STUDIO));

        addCard("🎨 Create Studio",
                "Design interfaces, logos, icons, images and documents.",
                v -> showSection("CREATE STUDIO", NovaFeatures.CREATE_STUDIO));

        addCard("📚 Learn",
                "Tutor, courses, notes, PDFs, quizzes and study plans.",
                v -> showSection("LEARN", NovaFeatures.LEARN));

        addCard("📁 Projects",
                "Manage apps, games, websites, AI projects, builds and backups.",
                v -> showSection("PROJECTS", NovaFeatures.PROJECTS));

        addCard("🛠 Tools",
                "JSON, CSV, PDF, images, data analysis and converters.",
                v -> showSection("TOOLS", NovaFeatures.TOOLS));
    }

    private void addCard(String heading, String description, View.OnClickListener action) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(16, 15, 16, 15);
        card.setBackground(background(Color.WHITE, 30));
        card.setElevation(4);

        TextView h = text(heading, 18, dark, true);
        TextView d = text(description, 14, Color.DKGRAY, false);

        card.addView(h);
        card.addView(d);

        card.setOnClickListener(action);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.setMargins(0, 0, 0, 14);
        content.addView(card, p);
    }

    private void showSection(String section, List<String> features) {
        base(section);

        TextView info = text(
                "NOVA • " + section + "\nSelect a feature to open its workspace.",
                17, dark, true
        );
        info.setPadding(8, 5, 8, 20);
        content.addView(info);

        for (String feature : features) {
            Button b = button(feature);

            b.setOnClickListener(v -> openFeature(section, feature));

            LinearLayout.LayoutParams p =
                    new LinearLayout.LayoutParams(-1, 58);
            p.setMargins(0, 0, 0, 9);
            content.addView(b, p);
        }
    }

    private void showMore() {
        base("MORE");

        addCard("🎨 Create Studio", "Design and creative tools.",
                v -> showSection("CREATE STUDIO", NovaFeatures.CREATE_STUDIO));

        addCard("🛠 Tools", "Developer and file utilities.",
                v -> showSection("TOOLS", NovaFeatures.TOOLS));

        addCard("🛍 Marketplace", "Templates, themes, plugins and assets.",
                v -> showSection("MARKETPLACE", NovaFeatures.MARKETPLACE));

        addCard("🛡 Security", "Permissions, privacy and security tools.",
                v -> showSection("SECURITY", NovaFeatures.SECURITY));

        addCard("☁ Cloud", "GitHub, backup, sync and collaboration.",
                v -> showSection("CLOUD", NovaFeatures.CLOUD));

        addCard("⚙ Settings", "NOVA preferences and configuration.",
                v -> showSection("SETTINGS", NovaFeatures.SETTINGS));
    }

    private void openFeature(String section, String feature) {
        base(feature);

        TextView heading = text(
                feature,
                26, dark, true
        );
        content.addView(heading);

        TextView description = text(
                "NOVA " + section + "\n\n"
                + "This workspace is now connected to NOVA navigation.\n"
                + "The next implementation layer will connect its real "
                + "local functionality, project storage and external services.",
                16, Color.DKGRAY, false
        );
        content.addView(description);

        if (feature.equals("App") ||
            feature.equals("Game") ||
            feature.equals("Website") ||
            feature.equals("Web App") ||
            feature.equals("AI") ||
            feature.equals("API") ||
            feature.equals("Script")) {

            Button create = button("＋ CREATE " + feature.toUpperCase());
            create.setTextColor(blue);
            create.setOnClickListener(v ->
                    Toast.makeText(this,
                            "Creation workspace opened for " + feature,
                            Toast.LENGTH_SHORT).show());

            content.addView(create);
        }

        if (feature.equals("Project Brain")) {
            Button brain = button("🧠 OPEN PROJECT BRAIN");
            brain.setOnClickListener(v ->
                    Toast.makeText(this,
                            "Project Brain foundation ready",
                            Toast.LENGTH_SHORT).show());
            content.addView(brain);
        }
    }
}
