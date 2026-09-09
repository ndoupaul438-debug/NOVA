package com.nova.app;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.nova.app.core.NovaFeatures;
import com.nova.app.ai.MeganEngine;
import com.nova.app.ai.MeganProjectGenerator;
import com.nova.app.projects.ProjectManager;
import com.nova.app.school.NovaSchool;
import com.nova.app.school.NovaSchoolLabs;
import com.nova.app.school.NovaSchoolCompetitions;
import com.nova.app.school.NovaSchoolLearning;
import com.nova.app.school.NovaAdaptiveEngine;
import com.nova.app.school.CiaTeacherVoice;
import com.nova.app.school.NovaQuestionBank;
import com.nova.app.school.NovaScience;
import com.nova.app.settings.NovaTheme;
import com.nova.app.settings.ThemeRegistry;
import com.nova.app.ui.NovaCommandCenter;
import com.nova.app.workspace.NovaWorkspace;

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

        LinearLayout dashboard = new LinearLayout(this);
        dashboard.setOrientation(LinearLayout.VERTICAL);
        dashboard.setPadding(dp(16), dp(12), dp(16), dp(24));

        dashboard.addView(title("NOVA"));
        dashboard.addView(subtitle(
                "Your professional command center"
        ));

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

        dashboard.addView(commandCenter);

        // TODAY
        dashboard.addView(card(
                "📅 TODAY",
                "Start your next learning or creation activity.",
                v -> showNovaSchool()
        ));

        // CONTINUE
        dashboard.addView(card(
                "▶ CONTINUE LEARNING",
                "Return to your current NOVA School course and continue from your progress.",
                v -> showNovaSchool()
        ));

        // CIA AI
        dashboard.addView(card(
                "🧠 CIA AI RECOMMENDS",
                "CIA AI adapts lessons, practice and assessments according to your learning history.",
                v -> showNovaSchool()
        ));

        // UPCOMING
        dashboard.addView(card(
                "📝 UPCOMING ASSESSMENT",
                "NOVA rotates assessment activities so recently completed work is not immediately repeated.",
                v -> showNovaSchool()
        ));

        // PRACTICAL
        dashboard.addView(card(
                "🧪 PRACTICAL LAB",
                "Apply your knowledge through coding, programming, database, AI, web and cybersecurity labs.",
                v -> showNovaSchool()
        ));

        // PROGRESS
        dashboard.addView(card(
                "📈 YOUR PROGRESS",
                "Lessons, practice, quizzes, exams, practicals and challenges are tracked chronologically.",
                v -> showNovaSchool()
        ));

        // RECENT
        dashboard.addView(card(
                "🕘 RECENT ACTIVITY",
                "Your latest completed and in-progress activities will appear here as the learning history grows.",
                v -> showNovaSchool()
        ));

        // QUICK ACTIONS
        dashboard.addView(card(
                "🏫 NOVA SCHOOL",
                "Open CIA AI, Digital University, ICT Academy, labs, assessments and competitions.",
                v -> showNovaSchool()
        ));

        dashboard.addView(card(
                "📁 PROJECTS",
                "Open your applications, games, websites, AI projects, files and builds.",
                v -> showProjects()
        ));

        dashboard.addView(card(
                "🚀 CREATE",
                "Start creating with NOVA's studios.",
                v -> showCreate()
        ));

        dashboard.addView(card(
                "🤖 M3GAN",
                "Open NOVA's creation and engineering AI.",
                v -> showMegan()
        ));

        content.addView(dashboard);
    }

    private void showNovaSchool() {
        clear("NOVA School", "Learn, practice, compete and certify");

        // ==============================
        // GLOBAL NOVA SCHOOL SEARCH
        // ==============================

        EditText search = new EditText(this);
        search.setHint("🔎 Search schools, courses, topics, labs...");
        search.setSingleLine(true);
        search.setTextSize(16);
        search.setPadding(dp(16), dp(4), dp(16), dp(4));

        GradientDrawable searchBackground = new GradientDrawable();
        searchBackground.setCornerRadius(dp(18));
        searchBackground.setStroke(dp(1), Color.LTGRAY);
        searchBackground.setColor(Color.TRANSPARENT);
        search.setBackground(searchBackground);

        LinearLayout.LayoutParams searchParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(56)
                );
        searchParams.setMargins(0, 0, 0, dp(16));

        content.addView(search, searchParams);

        LinearLayout results = new LinearLayout(this);
        results.setOrientation(LinearLayout.VERTICAL);
        content.addView(results);

        // Search destinations
        final String[] names = {
                "CIA AI Teacher",
                "NOVA Digital University",
                "ICT Academy",
                "Termux & Android Terminal School",
                "Coding School",
                "Web Development School",
                "AI School",
                "Ethical Hacking School",
                "Database School",
                "🔬 Science Department",
                "Game Development School",
                "🔬 Science Department",
                "Practical Labs",
                "Competitions",
                "Certification Centre",
                "Skill Passport",
                "Digital City Simulator",
                "Innovation Lab"
        };

        final String[] descriptions = {
                "Your NOVA School teacher for lessons, homework, quizzes and study plans.",
                "University-style courses, practical learning and professional skills.",
                "Computer fundamentals, networking, programming, databases, cloud and AI.",
                "Learn Linux commands, Bash, Python, Git, Android development and practical terminal skills.",
                "Hands-on programming lessons and engineering challenges.",
                "HTML, CSS, JavaScript, React, APIs, databases and PWAs.",
                "Learn artificial intelligence, machine learning, data and AI engineering.",
                "Learn cybersecurity through authorized labs and isolated practice environments.",
                "SQL, database design, queries, optimization and data management.",
                "O Level Science Department: Physics, Chemistry and Biology. Physics teaches from the supplied O LEVEL PHYSICS NOTES.pdf where applicable; remaining material follows O Level content.",
                "Learn game design, programming, physics, animation and game systems.",
                "Physics, Chemistry and Biology. Physics teaching is linked to the supplied O-Level Physics PDFs; Chemistry and Biology use O-Level material.",
                "Practice networking, Linux, databases, AI, IoT, hardware and cybersecurity.",
                "Coding, AI, chess, cybersecurity, robotics, innovation and other NOVA competitions.",
                "Earn NOVA certificates, badges, achievement awards and competition credentials.",
                "Track courses, certificates, badges, projects, labs and achievements.",
                "Build and manage simulated smart-city technology systems.",
                "Solve real-world problems and turn ideas into technology projects."
        };

        final View.OnClickListener[] actions = new View.OnClickListener[] {
                v -> showCiaTeacher(),
                v -> showSchoolDigitalUniversity(),
                v -> showSchoolICTAcademy(),
                v -> showSchoolTermux(),
                v -> showSchoolCoding(),
                v -> showSchoolWeb(),
                v -> showSchoolAI(),
                v -> showSchoolCybersecurity(),
                v -> showSchoolDatabase(),
                v -> showScienceDepartment(),
                v -> showSchoolGameDevelopment(),
                v -> showScienceDepartment(),
                v -> showSchoolLabs(),
                v -> showNovaArena(),
                v -> showCertificationCentre(),
                v -> showStudentPortfolio(),
                v -> showSchoolDigitalCity(),
                v -> showSchoolInnovationLab()
        };

        Runnable renderResults = () -> {
            results.removeAllViews();

            String query = search.getText().toString()
                    .trim()
                    .toLowerCase(java.util.Locale.ROOT);

            if (query.isEmpty()) {
                return;
            }

            int matches = 0;

            // ==========================================
            // SEARCH SCHOOL DESTINATIONS
            // ==========================================

            for (int i = 0; i < names.length; i++) {
                String searchable =
                        (names[i] + " " + descriptions[i])
                                .toLowerCase(java.util.Locale.ROOT);

                if (searchable.contains(query)) {
                    results.addView(card(
                            "🔎 " + names[i],
                            descriptions[i],
                            actions[i]
                    ));
                    matches++;
                }
            }

            // ==========================================
            // SEARCH ALL NOVA SCHOOL COURSES
            // ==========================================

            java.util.List<String> allCourses =
                    new java.util.ArrayList<>();

            allCourses.addAll(NovaSchool.termuxCourses());
            allCourses.addAll(NovaSchool.codingCourses());
            allCourses.addAll(NovaSchool.programmingCourses());
            allCourses.addAll(NovaSchool.ictAcademy());

            for (String course : allCourses) {

                String courseSearch =
                        course.toLowerCase(java.util.Locale.ROOT);

                if (courseSearch.contains(query)) {

                    results.addView(card(
                            "📚 " + course,
                            "NOVA School course",
                            v -> showSchoolLesson(course)
                    ));

                    matches++;
                }

                // Search every lesson belonging to this course.
                for (NovaSchoolLearning.Lesson lesson :
                        NovaSchoolLearning.lessons(course)) {

                    String lessonSearch =
                            (
                                    course + " "
                                    + lesson.getTitle() + " "
                                    + lesson.getDescription() + " "
                                    + lesson.getContent()
                            ).toLowerCase(java.util.Locale.ROOT);

                    if (lessonSearch.contains(query)) {

                        results.addView(card(
                                "📖 " + lesson.getTitle(),
                                "Course: " + course
                                        + "\n\n"
                                        + lesson.getDescription(),
                                v -> showCiaTopic(course, lesson)
                        ));

                        matches++;
                    }
                }
            }

            // ==========================================
            // SEARCH PRACTICAL LABS
            // ==========================================

            for (NovaSchoolLabs.Lab lab :
                    NovaSchoolLabs.labs()) {

                String searchable =
                        (
                                lab.getTitle() + " "
                                + lab.getCategory() + " "
                                + lab.getDescription() + " "
                                + lab.getDifficulty()
                        ).toLowerCase(java.util.Locale.ROOT);

                if (searchable.contains(query)) {

                    results.addView(card(
                            "🧪 " + lab.getTitle(),
                            lab.getDescription(),
                            v -> showSchoolLab(lab)
                    ));

                    matches++;
                }
            }

            // ==========================================
            // SEARCH COMPETITIONS
            // ==========================================

            for (NovaSchoolCompetitions.Competition competition :
                    NovaSchoolCompetitions.competitions()) {

                String searchable =
                        (
                                competition.getTitle() + " "
                                + competition.getCategory() + " "
                                + competition.getDescription() + " "
                                + competition.getMode() + " "
                                + competition.getSchedule()
                        ).toLowerCase(java.util.Locale.ROOT);

                if (searchable.contains(query)) {

                    results.addView(card(
                            "🏆 " + competition.getTitle(),
                            competition.getDescription(),
                            v -> showCompetition(competition)
                    ));

                    matches++;
                }
            }

            // ==========================================
            // NO RESULTS
            // ==========================================

            if (matches == 0) {
                results.addView(card(
                        "No results",
                        "Nothing matched \"" + query +
                                "\". Try a school, course, lesson, topic, "
                                + "programming language, lab or competition.",
                        null
                ));
            }
        };

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                renderResults.run();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // ==============================
        // DEFAULT SCHOOL CATALOGUE
        // ==============================

        content.addView(card(
                "🤖 CIA AI Teacher",
                descriptions[0],
                actions[0]
        ));

        content.addView(card(
                "🏛️ NOVA Digital University",
                descriptions[1],
                actions[1]
        ));

        content.addView(card(
                "💻 ICT Academy",
                descriptions[2],
                actions[2]
        ));

        content.addView(card(
                "📱 Termux & Android Terminal School",
                descriptions[3],
                actions[3]
        ));

        content.addView(card(
                "🧑‍💻 Coding School",
                descriptions[4],
                actions[4]
        ));

        content.addView(card(
                "🌐 Web Development School",
                descriptions[5],
                actions[5]
        ));

        content.addView(card(
                "🤖 AI School",
                descriptions[6],
                actions[6]
        ));

        content.addView(card(
                "🛡️ Ethical Hacking School",
                descriptions[7],
                actions[7]
        ));

        content.addView(card(
                "🗄️ Database School",
                descriptions[8],
                actions[8]
        ));

        content.addView(card(
                "🎮 Game Development School",
                descriptions[9],
                actions[9]
        ));

        content.addView(card(
                "🔬 Practical Labs",
                descriptions[10],
                actions[10]
        ));

        content.addView(card(
                "🏆 Competitions",
                descriptions[11],
                actions[11]
        ));

        content.addView(card(
                "📜 Certification Centre",
                descriptions[12],
                actions[12]
        ));

        content.addView(card(
                "🪪 Skill Passport",
                descriptions[13],
                actions[13]
        ));

        content.addView(card(
                "🏙️ Digital City Simulator",
                descriptions[14],
                actions[14]
        ));

        content.addView(card(
                "🧠 Innovation Lab",
                descriptions[15],
                actions[15]
        ));

        content.addView(button(
                "← Back to Home",
                v -> showHome()
        ));
    }

    private void showCodingHub() {
        clear("Coding Hub", "Create, edit, test and build projects");

        content.addView(card(
                "⌨️ Code Editor",
                "Edit project source files directly inside NOVA.",
                v -> showCodeEditor()
        ));

        content.addView(card(
                "📂 File Explorer",
                "Browse, create, edit and manage NOVA workspace files.",
                v -> showFileExplorer(null)
        ));

        content.addView(card(
                "▶️ Preview & Run",
                "Inspect and launch the current project preview.",
                v -> showPreview()
        ));

        content.addView(card(
                "🧪 Test & Debug",
                "Run NOVA workspace diagnostics and project checks.",
                v -> showTestDebug()
        ));

        content.addView(card(
                "🔨 Build & Export",
                "Build projects and inspect APK, AAB, web and ZIP outputs.",
                v -> showBuildExport()
        ));

        content.addView(button(
                "← Back to Home",
                v -> showHome()
        ));
    }

    private void showPreview() {
        clear("Preview & Run", "NOVA project preview");

        ProjectManager manager = new ProjectManager(this);
        List<File> projects = manager.getProjects();

        if (projects.isEmpty()) {

            content.addView(card(
                    "No project",
                    "Create a project before opening the preview.",
                    v -> showCreate()
            ));

        } else {

            File project = projects.get(0);

            content.addView(card(
                    "▶️ " + project.getName(),
                    "Project workspace is available.",
                    null
            ));

            content.addView(card(
                    "📁 Project Path",
                    project.getAbsolutePath(),
                    null
            ));

            File[] files = project.listFiles();

            if (files != null) {

                for (File file : files) {

                    if (file.isFile()) {

                        content.addView(card(
                                "📄 " + file.getName(),
                                formatFileSize(file.length()),
                                v -> openFileInEditor(file)
                        ));
                    }
                }
            }

            content.addView(button(
                    "🔄 Refresh Preview",
                    v -> showPreview()
            ));

            content.addView(button(
                    "⌨️ Open Code Editor",
                    v -> showCodeEditor()
            ));
        }

        content.addView(button(
                "← Back to Coding Hub",
                v -> showCodingHub()
        ));
    }

    private void showTestDebug() {
        clear("Test & Debug", "NOVA project diagnostics");

        ProjectManager manager = new ProjectManager(this);
        List<File> projects = manager.getProjects();

        content.addView(card(
                "🧪 NOVA Diagnostics",
                "Checking the local project workspace.",
                null
        ));

        if (projects.isEmpty()) {

            content.addView(card(
                    "⚠️ No projects",
                    "Create a project to begin testing.",
                    v -> showCreate()
            ));

        } else {

            int totalFiles = 0;
            long totalBytes = 0;

            for (File project : projects) {

                File[] files = project.listFiles();

                if (files != null) {

                    for (File file : files) {

                        if (file.isFile()) {
                            totalFiles++;
                            totalBytes += file.length();
                        }
                    }
                }
            }

            content.addView(card(
                    "✅ Projects",
                    projects.size() + " project(s) detected.",
                    null
            ));

            content.addView(card(
                    "📄 Files",
                    totalFiles + " file(s) detected.",
                    null
            ));

            content.addView(card(
                    "💾 Workspace Size",
                    formatFileSize(totalBytes),
                    null
            ));

            content.addView(card(
                    "🔐 Workspace Safety",
                    "Local workspace access is active.",
                    null
            ));

            content.addView(card(
                    "⚙️ Build System",
                    "Gradle Android build pipeline available.",
                    null
            ));

            content.addView(button(
                    "🔄 Run Diagnostics Again",
                    v -> showTestDebug()
            ));

            content.addView(button(
                    "🔨 Open Build & Export",
                    v -> showBuildExport()
            ));
        }

        content.addView(button(
                "← Back to Coding Hub",
                v -> showCodingHub()
        ));
    }

    private void showBuildExport() {
        clear("Build & Export", "NOVA build pipeline");

        ProjectManager manager = new ProjectManager(this);
        List<File> projects = manager.getProjects();

        content.addView(card(
                "🔨 NOVA Build Pipeline",
                "Use the native NOVA build manager to prepare project outputs.",
                null
        ));

        if (projects.isEmpty()) {

            content.addView(card(
                    "No project available",
                    "Create a project before starting a build.",
                    v -> showCreate()
            ));

        } else {

            TextView project = text(
                    "Current project: " +
                            projects.get(0).getName(),
                    15,
                    true
            );

            project.setTextColor(
                    NovaTheme.accent(currentTheme)
            );

            content.addView(project);

            content.addView(button(
                    "📱 Build APK",
                    v -> runNovaBuild("APK")
            ));

            content.addView(button(
                    "📦 Build AAB",
                    v -> runNovaBuild("AAB")
            ));

            content.addView(button(
                    "🌐 Web Export",
                    v -> runNovaBuild("WEB")
            ));

            content.addView(button(
                    "🗜️ ZIP Export",
                    v -> runNovaBuild("ZIP")
            ));
        }

        content.addView(button(
                "📁 Open Build Folder",
                v -> showBuildOutputs()
        ));

        content.addView(button(
                "← Back to Coding Hub",
                v -> showCodingHub()
        ));
    }

    private void runNovaBuild(String type) {

        Toast.makeText(
                this,
                "Starting " + type + " build...",
                Toast.LENGTH_SHORT
        ).show();

        try {

            com.nova.app.build.NovaBuildManager buildManager =
                    new com.nova.app.build.NovaBuildManager(this);

            File output;

            if ("APK".equals(type)) {
                output = buildManager.apkOutput();
            } else if ("AAB".equals(type)) {
                output = buildManager.aabOutput();
            } else if ("WEB".equals(type)) {
                output = buildManager.webOutput();
            } else {
                output = buildManager.zipOutput();
            }

            if (!output.exists()) {
                output.getParentFile().mkdirs();
            }

            Toast.makeText(
                    this,
                    type + " output: " +
                            output.getAbsolutePath(),
                    Toast.LENGTH_LONG
            ).show();

            showBuildOutputs();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Build error: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void showBuildOutputs() {

        clear("Build Outputs", "NOVA generated files");

        com.nova.app.build.NovaBuildManager buildManager =
                new com.nova.app.build.NovaBuildManager(this);

        File[] outputs = new File[] {
                buildManager.apkOutput(),
                buildManager.aabOutput(),
                buildManager.webOutput(),
                buildManager.zipOutput()
        };

        for (File output : outputs) {

            String status = output.exists()
                    ? "Available • " + formatFileSize(output.length())
                    : "Not generated yet";

            content.addView(card(
                    "📦 " + output.getName(),
                    status,
                    output.exists()
                            ? v -> Toast.makeText(
                                    this,
                                    output.getAbsolutePath(),
                                    Toast.LENGTH_LONG
                            ).show()
                            : null
            ));
        }

        content.addView(button(
                "← Back to Build & Export",
                v -> showBuildExport()
        ));
    }

    private void showFileExplorer(File directory) {
        clear("File Explorer", "NOVA workspace");

        File rootDirectory;

        if (directory == null) {
            rootDirectory = new NovaWorkspace(this).getRoot();
        } else {
            rootDirectory = directory;
        }

        if (rootDirectory == null) {
            rootDirectory = new File(
                    getFilesDir(),
                    "nova-workspace"
            );
        }

        if (!rootDirectory.exists()) {
            rootDirectory.mkdirs();
        }

        final File currentDirectory = rootDirectory;

        TextView path = text(
                currentDirectory.getAbsolutePath(),
                12,
                false
        );

        path.setTextColor(
                NovaTheme.muted(currentTheme)
        );

        content.addView(path);

        content.addView(button(
                "🔄 Refresh",
                v -> showFileExplorer(currentDirectory)
        ));

        if (currentDirectory.getParentFile() != null) {
            content.addView(button(
                    "⬆️ Parent Folder",
                    v -> showFileExplorer(
                            currentDirectory.getParentFile()
                    )
            ));
        }

        content.addView(button(
                "➕ New File",
                v -> createWorkspaceFile(currentDirectory)
        ));

        File[] files = currentDirectory.listFiles();

        if (files == null || files.length == 0) {

            content.addView(card(
                    "📭 Empty Folder",
                    "There are no files here yet.",
                    null
            ));

        } else {

            java.util.Arrays.sort(
                    files,
                    (a, b) -> {
                        if (a.isDirectory() && !b.isDirectory()) return -1;
                        if (!a.isDirectory() && b.isDirectory()) return 1;
                        return a.getName().compareToIgnoreCase(
                                b.getName()
                        );
                    }
            );

            for (File file : files) {

                String icon = file.isDirectory()
                        ? "📁"
                        : "📄";

                String description = file.isDirectory()
                        ? "Open folder"
                        : formatFileSize(file.length());

                content.addView(card(
                        icon + "  " + file.getName(),
                        description,
                        v -> {
                            if (file.isDirectory()) {
                                showFileExplorer(file);
                            } else {
                                openFileInEditor(file);
                            }
                        }
                ));
            }
        }

        content.addView(button(
                "← Back to Coding Hub",
                v -> showCodingHub()
        ));
    }

    private String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }

        if (bytes < 1024 * 1024) {
            return (bytes / 1024) + " KB";
        }

        return (bytes / (1024 * 1024)) + " MB";
    }

    private void createWorkspaceFile(File directory) {

        final EditText input = new EditText(this);

        input.setHint("example.java");
        input.setSingleLine(true);

        new android.app.AlertDialog.Builder(this)
                .setTitle("Create File")
                .setMessage(
                        "Enter a file name for the current folder."
                )
                .setView(input)
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .setPositiveButton(
                        "Create",
                        (dialog, which) -> {

                            String name =
                                    input.getText()
                                            .toString()
                                            .trim();

                            if (name.isEmpty()) {
                                Toast.makeText(
                                        this,
                                        "File name required",
                                        Toast.LENGTH_SHORT
                                ).show();
                                return;
                            }

                            if (name.contains("/") ||
                                    name.contains("\\") ||
                                    name.contains("..")) {

                                Toast.makeText(
                                        this,
                                        "Invalid file name",
                                        Toast.LENGTH_SHORT
                                ).show();
                                return;
                            }

                            try {

                                File file =
                                        new File(
                                                directory,
                                                name
                                        );

                                if (file.exists()) {
                                    Toast.makeText(
                                            this,
                                            "File already exists",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    return;
                                }

                                java.io.FileOutputStream output =
                                        new java.io.FileOutputStream(
                                                file
                                        );

                                output.write(
                                        "".getBytes(
                                                java.nio.charset.StandardCharsets.UTF_8
                                        )
                                );

                                output.close();

                                Toast.makeText(
                                        this,
                                        "Created " + name,
                                        Toast.LENGTH_SHORT
                                ).show();

                                showFileExplorer(directory);

                            } catch (Exception e) {

                                Toast.makeText(
                                        this,
                                        "Create failed: " +
                                                e.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                )
                .show();
    }

    private void openFileInEditor(File file) {

        clear(
                "Code Editor",
                file.getName()
        );

        TextView filePath = text(
                file.getAbsolutePath(),
                12,
                false
        );

        filePath.setTextColor(
                NovaTheme.muted(currentTheme)
        );

        content.addView(filePath);

        EditText editor = new EditText(this);

        editor.setTextSize(14);
        editor.setGravity(
                Gravity.TOP | Gravity.START
        );

        editor.setTextColor(
                NovaTheme.text(currentTheme)
        );

        editor.setHintTextColor(
                NovaTheme.muted(currentTheme)
        );

        editor.setPadding(
                dp(14),
                dp(14),
                dp(14),
                dp(14)
        );

        editor.setBackground(
                background(
                        NovaTheme.card(currentTheme),
                        16
                )
        );

        editor.setSingleLine(false);

        try {

            byte[] data =
                    java.nio.file.Files.readAllBytes(
                            file.toPath()
                    );

            editor.setText(
                    new String(
                            data,
                            java.nio.charset.StandardCharsets.UTF_8
                    )
            );

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Could not read file",
                    Toast.LENGTH_SHORT
            ).show();
        }

        content.addView(
                editor,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(360)
                )
        );

        content.addView(button(
                "💾 Save",
                v -> {

                    try {

                        java.nio.file.Files.write(
                                file.toPath(),
                                editor.getText()
                                        .toString()
                                        .getBytes(
                                                java.nio.charset.StandardCharsets.UTF_8
                                        )
                        );

                        Toast.makeText(
                                this,
                                "Saved " + file.getName(),
                                Toast.LENGTH_SHORT
                        ).show();

                    } catch (Exception e) {

                        Toast.makeText(
                                this,
                                "Save failed: " +
                                        e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ));

        content.addView(button(
                "🗑️ Delete File",
                v -> {

                    new android.app.AlertDialog.Builder(this)
                            .setTitle("Delete File?")
                            .setMessage(
                                    "Delete " +
                                            file.getName() +
                                            "?"
                            )
                            .setNegativeButton(
                                    "Cancel",
                                    null
                            )
                            .setPositiveButton(
                                    "Delete",
                                    (dialog, which) -> {

                                        if (file.delete()) {

                                            Toast.makeText(
                                                    this,
                                                    "File deleted",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            showFileExplorer(
                                                    file.getParentFile()
                                            );

                                        } else {

                                            Toast.makeText(
                                                    this,
                                                    "Delete failed",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    }
                            )
                            .show();
                }
        ));

        content.addView(button(
                "← Back to Files",
                v -> showFileExplorer(
                        file.getParentFile()
                )
        ));
    }

    private void showCodeEditor() {
        clear("Code Editor", "Native NOVA project editor");

        ProjectManager manager = new ProjectManager(this);
        List<File> projects = manager.getProjects();

        if (projects.isEmpty()) {
            content.addView(card(
                    "No project available",
                    "Create a project first, then open the Code Editor.",
                    v -> showCreate()
            ));

            content.addView(button(
                    "← Back to Coding Hub",
                    v -> showCodingHub()
            ));

            return;
        }

        TextView projectLabel = text(
                "Project: " + projects.get(0).getName(),
                15,
                true
        );

        projectLabel.setTextColor(
                NovaTheme.accent(currentTheme)
        );

        content.addView(projectLabel);

        EditText editor = new EditText(this);

        editor.setText(
                "// NOVA Code Editor\n" +
                "// Project: " + projects.get(0).getName() + "\n\n" +
                "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Hello from NOVA\");\n" +
                "    }\n" +
                "}\n"
        );

        editor.setTextSize(14);
        editor.setGravity(Gravity.TOP | Gravity.START);
        editor.setTextColor(NovaTheme.text(currentTheme));
        editor.setHintTextColor(NovaTheme.muted(currentTheme));
        editor.setPadding(
                dp(14),
                dp(14),
                dp(14),
                dp(14)
        );
        editor.setBackground(
                background(NovaTheme.card(currentTheme), 16)
        );
        editor.setSingleLine(false);

        content.addView(
                editor,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(360)
                )
        );

        content.addView(button(
                "💾 Save File",
                v -> {
                    try {
                        File file = new File(
                                projects.get(0),
                                "Main.java"
                        );

                        java.io.FileOutputStream output =
                                new java.io.FileOutputStream(file);

                        output.write(
                                editor.getText()
                                        .toString()
                                        .getBytes(
                                                java.nio.charset.StandardCharsets.UTF_8
                                        )
                        );

                        output.close();

                        Toast.makeText(
                                this,
                                "Saved " + file.getName(),
                                Toast.LENGTH_SHORT
                        ).show();

                    } catch (Exception e) {
                        Toast.makeText(
                                this,
                                "Save failed: " + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ));

        content.addView(button(
                "▶️ Run / Preview",
                v -> Toast.makeText(
                        this,
                        "Project queued for preview",
                        Toast.LENGTH_SHORT
                ).show()
        ));

        content.addView(button(
                "🧪 Test",
                v -> Toast.makeText(
                        this,
                        "Project test pipeline ready",
                        Toast.LENGTH_SHORT
                ).show()
        ));

        content.addView(button(
                "← Back to Coding Hub",
                v -> showCodingHub()
        ));
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

            String intent = engine.detectIntent(request);
            String response;

            if ("create".equalsIgnoreCase(intent)) {
                MeganProjectGenerator generator =
                        new MeganProjectGenerator(this);

                org.json.JSONObject generated =
                        generator.generate(request);

                if (generated.optBoolean("success", false)) {
                    response =
                            "Project created successfully.\\n\\n"
                            + "Type: " + generated.optString("type", "Project") + "\\n"
                            + "Name: " + generated.optString("name", "NOVA Project") + "\\n"
                            + "Location: " + generated.optString("path", "NOVA Workspace") + "\\n\\n"
                            + "The project files are now available in the NOVA workspace.";
                } else {
                    response = engine.respond(request);

                    String error =
                            generated.optString("error", "");

                    if (!error.isEmpty()) {
                        response += "\\n\\nGenerator: " + error;
                    }
                }
            } else {
                response = engine.respond(request);
            }

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


    private CiaTeacherVoice ciaVoice;

    private void showCiaTeacher() {
        clear("🤖 CIA AI Teacher", "Voice • Text • Visual Learning");

        LinearLayout ciaCard = new LinearLayout(this);
        ciaCard.setOrientation(LinearLayout.HORIZONTAL);
        ciaCard.setGravity(Gravity.CENTER_VERTICAL);
        ciaCard.setPadding(dp(16), dp(14), dp(16), dp(14));
        ciaCard.setBackground(background(NovaTheme.card(currentTheme), 18));

        ImageView ciaIcon = new ImageView(this);
        ciaIcon.setImageResource(R.drawable.ic_cia_ai);
        ciaIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        LinearLayout.LayoutParams ciaIconParams =
                new LinearLayout.LayoutParams(dp(72), dp(72));
        ciaIconParams.setMargins(0, 0, dp(14), 0);
        ciaCard.addView(ciaIcon, ciaIconParams);

        LinearLayout ciaText = new LinearLayout(this);
        ciaText.setOrientation(LinearLayout.VERTICAL);

        TextView ciaHeading = text(
                "🎓 CIA AI Teacher",
                17,
                true
        );

        TextView ciaDescription = text(
                "Your NOVA School teacher for lessons, explanations, homework and practical learning.",
                13,
                false
        );
        ciaDescription.setTextColor(NovaTheme.muted(currentTheme));

        ciaText.addView(ciaHeading);
        ciaText.addView(ciaDescription);

        ciaCard.addView(
                ciaText,
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        LinearLayout.LayoutParams ciaCardParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
        ciaCardParams.setMargins(0, 0, 0, dp(12));
        ciaCard.setLayoutParams(ciaCardParams);

        content.addView(ciaCard);

        content.addView(card(
                "🎙️ Speak to CIA AI",
                "Speak your question and Android speech recognition converts it into text.",
                v -> startCiaListening()
        ));

        content.addView(card(
                "🔊 Voice Teacher",
                "CIA AI can read explanations aloud.",
                v -> speakCia(
                        "Welcome to NOVA School. I am CIA AI, your teacher."
                )
        ));

        content.addView(card(
                "🖼️ Visual Learning",
                "Learn with visual explanations and educational diagrams.",
                v -> showCiaVisualLearning()
        ));

        content.addView(card(
                "📚 Lessons",
                "Choose a subject to learn.",
                v -> showSchoolCatalogue(
                        "📚 CIA AI Lessons",
                        "Choose a NOVA School learning topic.",
                        new String[]{
                                "Computer Fundamentals",
                                "Linux Commands",
                                "Python",
                                "Java",
                                "Programming",
                                "Web Development",
                                "Databases",
                                "Networking",
                                "Cybersecurity",
                                "Artificial Intelligence",
                                "Game Development",
                                "Robotics & IoT",
                                "Digital Forensics",
                                "UI/UX"
                        }
                )
        ));

        content.addView(card(
                "📝 Homework",
                "Practise what you have learned.",
                v -> showCiaHomework()
        ));

        content.addView(card(
                "📊 Progress",
                "View your NOVA School learning progress.",
                v -> showCiaProgress()
        ));

        content.addView(button(
                "← Back to NOVA School",
                v -> showNovaSchool()
        ));
    }

    private void startCiaListening() {
        if (ciaVoice == null) {
            ciaVoice = new CiaTeacherVoice(
                    this,
                    new CiaTeacherVoice.Listener() {

                        @Override
                        public void onTranscript(String text) {
                            if (text != null && !text.trim().isEmpty()) {
                                showCiaResponse(text.trim());
                            }
                        }

                        @Override
                        public void onListening(boolean listening) {
                            if (listening) {
                                Toast.makeText(
                                        MainActivity.this,
                                        "🎙️ CIA AI is listening...",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(
                                    MainActivity.this,
                                    message,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
            );
        }

        if (!ciaVoice.hasMicrophonePermission()) {
            ciaVoice.requestMicrophonePermission(7001);
            return;
        }

        ciaVoice.startListening();
    }

    private void showCiaResponse(String question) {
        clear("🤖 CIA AI Teacher", "Teaching response");

        content.addView(card(
                "📝 You said",
                question,
                null
        ));

        String q = question.toLowerCase();
        String answer;

        if (q.contains("termux") || q.contains("linux")) {
            answer =
                    "Termux provides a Linux-like terminal environment on Android. "
                    + "Start with pwd, ls, cd and mkdir. "
                    + "Practise commands in an authorized NOVA workspace.";
        } else if (q.contains("python")) {
            answer =
                    "Python is a programming language used for applications, "
                    + "automation, data analysis and artificial intelligence. "
                    + "Start with variables, conditions, loops and functions.";
        } else if (q.contains("java")) {
            answer =
                    "Java is a general-purpose programming language. "
                    + "Learn variables, methods, classes, objects and "
                    + "object-oriented programming.";
        } else if (q.contains("network")) {
            answer =
                    "Networking allows devices to communicate. "
                    + "Important concepts include IP addresses, switches, "
                    + "routers, DNS, DHCP and network security.";
        } else if (q.contains("database") || q.contains("sql")) {
            answer =
                    "Databases organize information. SQL can create tables, "
                    + "insert records, query information and update data.";
        } else if (q.contains("ai")
                || q.contains("artificial intelligence")) {
            answer =
                    "Artificial intelligence enables computer systems to "
                    + "perform tasks involving learning, reasoning or pattern "
                    + "recognition. Machine learning, computer vision and "
                    + "natural language processing are major areas.";
        } else {
            answer =
                    "Let's learn this step by step. Identify the main concept, "
                    + "learn its key terms, study an example and then practise "
                    + "with a small activity.";
        }

        content.addView(card(
                "🎓 CIA's Explanation",
                answer,
                v -> speakCia(answer)
        ));

        content.addView(card(
                "🔊 Listen",
                "Have CIA AI read the explanation aloud.",
                v -> speakCia(answer)
        ));

        content.addView(card(
                "🖼️ Visual Explanation",
                "Open visual learning material.",
                v -> showCiaVisualLearning()
        ));

        content.addView(button(
                "🎙️ Ask Another Question",
                v -> startCiaListening()
        ));

        content.addView(button(
                "← Back to CIA AI",
                v -> showCiaTeacher()
        ));
    }

    private void speakCia(String text) {
        if (ciaVoice == null) {
            ciaVoice = new CiaTeacherVoice(
                    this,
                    new CiaTeacherVoice.Listener() {

                        @Override
                        public void onTranscript(String text) {}

                        @Override
                        public void onListening(boolean listening) {}

                        @Override
                        public void onError(String message) {
                            Toast.makeText(
                                    MainActivity.this,
                                    message,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
            );
        }

        ciaVoice.speak(text);
    }

    private void showCiaVisualLearning() {
        clear("🖼️ Visual Learning", "CIA AI • Interactive learning diagrams");

        content.addView(visualPanel(
                "💻 Computer Systems",
                new String[]{
                        "USER",
                        "↓",
                        "APPLICATIONS",
                        "↓",
                        "OPERATING SYSTEM",
                        "↓",
                        "HARDWARE"
                }
        ));

        content.addView(visualPanel(
                "🌐 Networks",
                new String[]{
                        "DEVICE",
                        "↓",
                        "SWITCH / ROUTER",
                        "↓",
                        "LOCAL NETWORK",
                        "↓",
                        "INTERNET"
                }
        ));

        content.addView(visualPanel(
                "🗄️ Databases",
                new String[]{
                        "APPLICATION",
                        "↓",
                        "SQL / QUERY",
                        "↓",
                        "DATABASE",
                        "↓",
                        "TABLES → RECORDS"
                }
        ));

        content.addView(visualPanel(
                "🤖 Artificial Intelligence",
                new String[]{
                        "DATA",
                        "↓",
                        "TRAINING",
                        "↓",
                        "AI MODEL",
                        "↓",
                        "EVALUATION",
                        "↓",
                        "APPLICATION"
                }
        ));

        content.addView(visualPanel(
                "📱 Android",
                new String[]{
                        "ANDROID APP",
                        "↓",
                        "ACTIVITY",
                        "↓",
                        "USER INTERFACE",
                        "↓",
                        "ANDROID APIs",
                        "↓",
                        "DEVICE"
                }
        ));

        content.addView(button(
                "← Back to CIA AI",
                v -> showCiaTeacher()
        ));
    }

    private LinearLayout visualPanel(
            String title,
            String[] steps
    ) {
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setGravity(Gravity.CENTER_HORIZONTAL);
        panel.setPadding(dp(16), dp(16), dp(16), dp(16));
        panel.setBackground(
                background(NovaTheme.card(currentTheme), 20)
        );

        LinearLayout.LayoutParams panelParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
        panelParams.setMargins(0, 0, 0, dp(14));
        panel.setLayoutParams(panelParams);

        TextView heading = text(title, 18, true);
        heading.setGravity(Gravity.CENTER);
        panel.addView(heading);

        for (String step : steps) {
            TextView node = text(step, 15, true);
            node.setGravity(Gravity.CENTER);
            node.setPadding(
                    dp(12),
                    dp(8),
                    dp(12),
                    dp(8)
            );

            if (step.equals("↓")) {
                node.setTextSize(20);
                node.setTextColor(NovaTheme.accent(currentTheme));
            } else {
                node.setBackground(
                        background(
                                NovaTheme.background(currentTheme),
                                12
                        )
                );
            }

            panel.addView(
                    node,
                    new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    )
            );
        }

        return panel;
    }

    private void showSchoolCatalogue(
            String title,
            String description,
            String[] topics
    ) {
        clear(title, description);

        for (String topic : topics) {
            content.addView(card(
                    topic,
                    "Open " + topic + " learning.",
                    v -> showSchoolLesson(topic)
            ));
        }

        content.addView(button(
                "← Back to NOVA School",
                v -> showNovaSchool()
        ));
    }

    private void showSchoolLesson(String topic) {
        clear("📚 " + topic, "NOVA School learning path");

        java.util.List<NovaSchoolLearning.Lesson> lessons =
                NovaSchoolLearning.lessons(topic);

        if (lessons.isEmpty()) {
            content.addView(card(
                    "⚠️ Lesson unavailable",
                    "CIA AI could not find learning content for " + topic + ".",
                    null
            ));

            content.addView(button(
                    "← Back to CIA AI",
                    v -> showCiaTeacher()
            ));
            return;
        }

        int completed = 0;
        NovaSchoolLearning.Lesson nextLesson = null;

        for (NovaSchoolLearning.Lesson lesson : lessons) {
            if (NovaSchoolLearning.isCompleted(
                    this,
                    lesson.getId()
            )) {
                completed++;
            } else if (nextLesson == null) {
                nextLesson = lesson;
            }
        }

        int total = lessons.size();
        int percentage = total > 0
                ? (completed * 100) / total
                : 0;

        // ==========================================
        // COURSE PROGRESS
        // ==========================================

        content.addView(card(
                "🎓 Course Progress",
                completed + " of " + total
                        + " lesson(s) completed"
                        + "\n\nProgress: " + percentage + "%",
                null
        ));

        // ==========================================
        // NEXT RECOMMENDED TOPIC
        // ==========================================

        if (nextLesson != null) {
            final NovaSchoolLearning.Lesson recommendedLesson = nextLesson;

            NovaAdaptiveEngine.recordActivity(
                    this,
                    topic,
                    NovaAdaptiveEngine.ActivityType.LESSON,
                    recommendedLesson.getId()
            );

            content.addView(card(
                    "🚀 Continue Learning",
                    "CIA AI recommends your next unfinished topic."
                            + "\n\n"
                            + recommendedLesson.getTitle()
                            + "\n\n"
                            + recommendedLesson.getDescription(),
                    v -> showCiaTopic(topic, recommendedLesson)
            ));

        } else {

            content.addView(card(
                    "🏆 Course Complete",
                    "You have completed every learning topic currently "
                            + "available in this course."
                            + "\n\n"
                            + "CIA AI can now help you review, practice "
                            + "and prepare for a different assessment.",
                    null
            ));
        }

        // ==========================================
        // FULL LEARNING PATH
        // ==========================================

        content.addView(text(
                "📖 Learning Path",
                20,
                true
        ));

        for (int i = 0; i < lessons.size(); i++) {

            NovaSchoolLearning.Lesson lesson = lessons.get(i);

            boolean done = NovaSchoolLearning.isCompleted(
                    this,
                    lesson.getId()
            );

            String status = done
                    ? "✅ Completed"
                    : (lesson == nextLesson
                    ? "▶️ Next"
                    : "🔒 Not completed");

            String title =
                    "Topic " + (i + 1) + " — "
                            + lesson.getTitle();

            content.addView(card(
                    title,
                    status
                            + "\n\n"
                            + lesson.getDescription()
                            + "\n\nOpen this topic to learn with CIA AI.",
                    v -> showCiaTopic(topic, lesson)
            ));
        }

        // ==========================================
        // COURSE NAVIGATION
        // ==========================================

        content.addView(button(
                "← Back to CIA AI",
                v -> showCiaTeacher()
        ));
    }

    private void showCiaTopic(
            String topic,
            NovaSchoolLearning.Lesson lesson
    ) {
        clear(
                "🤖 CIA AI Teacher",
                lesson.getTitle()
        );

        String lessonText =
                lesson.getContent();

        boolean completed =
                NovaSchoolLearning.isCompleted(
                        this,
                        lesson.getId()
                );

        content.addView(card(
                "📚 " + lesson.getTitle(),
                lesson.getDescription()
                        + "\n\n"
                        + lessonText,
                v -> speakCia(lessonText)
        ));

        content.addView(card(
                "🔊 CIA AI Voice Teacher",
                "CIA AI can read this lesson aloud.",
                v -> speakCia(lessonText)
        ));

        content.addView(card(
                "🖼️ Visual Learning",
                "Open diagrams and visual explanations related to this lesson.",
                v -> showCiaVisualLearning()
        ));

        if (!completed) {
            content.addView(button(
                    "✅ Mark Lesson Complete",
                    v -> {
                        NovaSchoolLearning.markCompleted(
                                this,
                                lesson.getId()
                        );

                        showCiaTopic(
                                topic,
                                lesson
                        );
                    }
            ));
        } else {
            content.addView(card(
                    "✅ Lesson Completed",
                    "CIA AI has recorded this lesson as completed. "
                            + "Continue to the next learning activity.",
                    null
            ));
        }

        content.addView(button(
                "🧪 Practice",
                v -> showSchoolPractice(
                        topic,
                        lesson
                )
        ));

        content.addView(button(
                "📝 Assessment",
                v -> showSchoolAssessment(
                        topic,
                        lesson
                )
        ));

        content.addView(button(
                "← Back to Lessons",
                v -> showSchoolLesson(topic)
        ));
    }


    private void showCiaReteaching(
            String topic,
            NovaSchoolLearning.Lesson lesson
    ) {
        clear(
                "🤖 CIA AI Reteaching",
                "Focused reteaching for the topic you need to improve."
        );

        String reteachTopic =
                NovaAdaptiveEngine.getReteachTopic(
                        this,
                        topic
                );

        if (reteachTopic == null
                || reteachTopic.trim().isEmpty()) {
            reteachTopic = lesson.getTitle();
        }

        content.addView(card(
                "🎯 Topic to Master",
                reteachTopic
                        + "\n\nCIA AI detected that this topic needs "
                        + "more practice. This session focuses on "
                        + "understanding the concept before you "
                        + "attempt a new assessment.",
                null
        ));

        content.addView(card(
                "📚 CIA AI Explanation",
                lesson.getContent()
                        + "\n\n\n"
                        + "Study this explanation carefully. "
                        + "You can also ask CIA AI for help.",
                null
        ));

        content.addView(button(
                "🔊 Listen to CIA AI",
                v -> speakCia(
                        lesson.getContent()
                )
        ));

        content.addView(button(
                "🧪 Start Different Practice",
                v -> showSchoolPractice(
                        topic,
                        lesson
                )
        ));

        content.addView(button(
                "← Back to Assessment Result",
                v -> showSchoolAssessment(
                        topic,
                        lesson
                )
        ));
    }

    private void showSchoolPractice(
            String topic,
            NovaSchoolLearning.Lesson lesson
    ) {
        clear(
                "🧪 CIA AI Practice",
                lesson.getTitle()
        );

        String course = topic;

        String difficultyName =
                NovaAdaptiveEngine.recommendedDifficulty(
                        this,
                        course,
                        NovaAdaptiveEngine.ActivityType.ASSIGNMENT
                );

        NovaQuestionBank.Difficulty difficulty;

        if ("Advanced".equalsIgnoreCase(difficultyName)) {
            difficulty = NovaQuestionBank.Difficulty.ADVANCED;
        } else if ("Intermediate".equalsIgnoreCase(difficultyName)) {
            difficulty = NovaQuestionBank.Difficulty.INTERMEDIATE;
        } else {
            difficulty = NovaQuestionBank.Difficulty.FOUNDATION;
        }

        java.util.List<NovaQuestionBank.Question> questions =
                NovaQuestionBank.createAssessment(
                        this,
                        course,
                        lesson.getTitle(),
                        difficulty,
                        3
                );

        content.addView(card(
                "🎯 Adaptive Practice",
                "CIA AI selected a new practice set based on "
                        + "your performance.\n\n"
                        + "Difficulty: " + difficultyName
                        + "\n\n"
                        + "Questions: " + questions.size()
                        + "\n\n"
                        + "Questions already completed are avoided "
                        + "whenever new variants are available.",
                null
        ));

        if (questions.isEmpty()) {
            content.addView(card(
                    "⚠️ No Practice Available",
                    "There are currently no questions available "
                            + "for this topic.",
                    null
            ));

            content.addView(button(
                    "← Back to Reteaching",
                    v -> showCiaReteaching(topic, lesson)
            ));

            return;
        }

        LinearLayout questionContainer =
                new LinearLayout(this);

        questionContainer.setOrientation(
                LinearLayout.VERTICAL
        );

        content.addView(questionContainer);

        java.util.List<EditText> answers =
                new java.util.ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {

            NovaQuestionBank.Question question =
                    questions.get(i);

            TextView number =
                    text(
                            "Question " + (i + 1),
                            18,
                            true
                    );

            questionContainer.addView(number);

            TextView questionText =
                    text(
                            question.getQuestion(),
                            17,
                            false
                    );

            questionContainer.addView(questionText);

            EditText answer =
                    new EditText(this);

            answer.setHint("Type your answer");
            answer.setSingleLine(false);
            answer.setMinLines(2);

            questionContainer.addView(answer);

            answers.add(answer);

            Space space = new Space(this);

            questionContainer.addView(
                    space,
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            24
                    )
            );
        }

        content.addView(button(
                "✅ Submit Practice",
                v -> {

                    int correct = 0;

                    for (int i = 0;
                         i < questions.size();
                         i++) {

                        NovaQuestionBank.Question question =
                                questions.get(i);

                        String studentAnswer =
                                answers.get(i)
                                        .getText()
                                        .toString()
                                        .trim();

                        String expected =
                                question.getAnswer()
                                        .trim();

                        NovaQuestionBank.recordAttempt(
                                this,
                                course,
                                lesson.getTitle(),
                                question.getId()
                        );

                        if (NovaQuestionBank.isAnswerCorrect(
                                studentAnswer,
                                expected
                        )) {
                            correct++;
                        }
                    }

                    int total = questions.size();

                    int percentage =
                            total > 0
                                    ? (correct * 100) / total
                                    : 0;

                    NovaAdaptiveEngine.recordScore(
                            this,
                            course,
                            NovaAdaptiveEngine.ActivityType.ASSIGNMENT,
                            percentage
                    );

                    if (percentage < 50) {

                        NovaAdaptiveEngine.requestReteach(
                                this,
                                course,
                                lesson.getTitle()
                        );

                        clear(
                                "📚 More Reteaching Needed",
                                lesson.getTitle()
                        );

                        content.addView(card(
                                "📉 Practice Result",
                                "Score: "
                                        + percentage
                                        + "%\n\n"
                                        + "Correct answers: "
                                        + correct
                                        + " / "
                                        + total
                                        + "\n\n"
                                        + "CIA AI will continue "
                                        + "reteaching this topic.",
                                null
                        ));

                        content.addView(button(
                                "🤖 Continue CIA AI Reteaching",
                                x -> showCiaReteaching(
                                        topic,
                                        lesson
                                )
                        ));

                    } else {

                        clear(
                                "🏆 Practice Complete",
                                lesson.getTitle()
                        );

                        content.addView(card(
                                "🎉 Practice Result",
                                "Score: "
                                        + percentage
                                        + "%\n\n"
                                        + "Correct answers: "
                                        + correct
                                        + " / "
                                        + total
                                        + "\n\n"
                                        + "CIA AI has recorded your "
                                        + "performance.",
                                null
                        ));

                        content.addView(button(
                                "📝 Start Different Reassessment",
                                x -> showSchoolAssessment(
                                        topic,
                                        lesson
                                )
                        ));

                        content.addView(button(
                                "🤖 Review with CIA AI",
                                x -> showCiaReteaching(
                                        topic,
                                        lesson
                                )
                        ));
                    }
                }
        ));

        content.addView(button(
                "🔊 Ask CIA AI for Help",
                v -> speakCia(
                        "I am helping you practise "
                                + lesson.getTitle()
                                + ". Read each question carefully "
                                + "and answer in your own words."
                )
        ));

        content.addView(button(
                "← Back to Reteaching",
                v -> showCiaReteaching(
                        topic,
                        lesson
                )
        ));
    }

    private void showSchoolAssessment(
            String topic,
            NovaSchoolLearning.Lesson lesson
    ) {
        clear(
                "📝 CIA AI Assessment",
                lesson.getTitle()
        );

        final String course = topic;
        final String lessonId = lesson.getId();

        final java.util.List<NovaQuestionBank.Question> questions =
                new java.util.ArrayList<>();

        NovaQuestionBank.Difficulty difficulty;

        String recommended =
                NovaAdaptiveEngine.recommendedDifficulty(
                        this,
                        course,
                        NovaAdaptiveEngine.ActivityType.QUIZ
                );

        if ("Advanced".equals(recommended)) {
            difficulty = NovaQuestionBank.Difficulty.ADVANCED;
        } else if ("Intermediate".equals(recommended)) {
            difficulty = NovaQuestionBank.Difficulty.INTERMEDIATE;
        } else {
            difficulty = NovaQuestionBank.Difficulty.FOUNDATION;
        }

        questions.addAll(
                NovaQuestionBank.createAssessment(
                        this,
                        course,
                        lesson.getTitle(),
                        difficulty,
                        5
                )
        );

        if (questions.isEmpty()) {

            content.addView(card(
                    "⚠️ No Questions Available",
                    "CIA AI could not find questions for this topic yet.",
                    null
            ));

            content.addView(button(
                    "← Back to Lesson",
                    v -> showCiaTopic(topic, lesson)
            ));

            return;
        }

        content.addView(card(
                "🎯 Adaptive Assessment",
                "CIA AI selected "
                        + questions.size()
                        + " question(s) at "
                        + recommended
                        + " difficulty."
                        + "\n\n"
                        + "Questions already attempted are avoided "
                        + "until the available question bank has been used.",
                null
        ));

        final android.widget.LinearLayout questionContainer =
                new android.widget.LinearLayout(this);

        questionContainer.setOrientation(
                android.widget.LinearLayout.VERTICAL
        );

        content.addView(questionContainer);

        for (int i = 0; i < questions.size(); i++) {

            NovaQuestionBank.Question question =
                    questions.get(i);

            android.widget.EditText answer =
                    new android.widget.EditText(this);

            answer.setHint("Type your answer");
            answer.setSingleLine(false);
            answer.setMinLines(2);
            answer.setGravity(android.view.Gravity.TOP);

            android.widget.TextView questionView =
                    text(
                            "Question " + (i + 1)
                                    + "\n\n"
                                    + question.getQuestion(),
                            17,
                            true
                    );

            questionContainer.addView(
                    questionView
            );

            questionContainer.addView(
                    answer
            );

            android.widget.Space space =
                    new android.widget.Space(this);

            questionContainer.addView(
                    space,
                    new android.widget.LinearLayout.LayoutParams(
                            1,
                            24
                    )
            );
        }

        content.addView(button(
                "✅ Submit Assessment",
                v -> {

                    int correct = 0;

                    for (int i = 0; i < questions.size(); i++) {

                        NovaQuestionBank.Question question =
                                questions.get(i);

                        android.view.View child =
                                questionContainer.getChildAt(
                                        i * 3 + 1
                                );

                        if (!(child instanceof android.widget.EditText)) {
                            continue;
                        }

                        android.widget.EditText answer =
                                (android.widget.EditText) child;

                        String studentAnswer =
                                answer.getText()
                                        .toString()
                                        .trim();

                        String expected =
                                question.getAnswer()
                                        .trim();

                        NovaQuestionBank.recordAttempt(
                                this,
                                course,
                                lesson.getTitle(),
                                question.getId()
                        );

                        if (NovaQuestionBank.isAnswerCorrect(
                                studentAnswer,
                                expected
                        )) {
                            correct++;
                        }
                    }

                    int total = questions.size();

                    int percentage =
                            total > 0
                                    ? (correct * 100) / total
                                    : 0;

                    NovaAdaptiveEngine.recordScore(
                            this,
                            course,
                            NovaAdaptiveEngine.ActivityType.EXAM,
                            percentage
                    );

                    NovaAdaptiveEngine.recordExamResult(
                            this,
                            course,
                            lesson.getTitle(),
                            percentage
                    );

                    NovaSchoolLearning.saveQuizScore(
                            this,
                            lessonId,
                            percentage,
                            100
                    );

                    if (percentage < 50) {

                        NovaAdaptiveEngine.requestReteach(
                                this,
                                course,
                                lesson.getTitle()
                        );

                        clear(
                                "📚 CIA AI Reteaching",
                                lesson.getTitle()
                        );

                        content.addView(card(
                                "📉 Assessment Result",
                                "Score: "
                                        + percentage
                                        + "%\n\n"
                                        + "CIA AI detected that this topic "
                                        + "needs more work."
                                        + "\n\n"
                                        + "The topic has been marked for "
                                        + "reteaching before reassessment.",
                                null
                        ));

                        content.addView(button(
                                "🤖 Start CIA AI Reteaching",
                                x -> showCiaReteaching(
                                        topic,
                                        lesson
                                )
                        ));

                    } else {

                        NovaAdaptiveEngine.clearReteach(
                                this,
                                course
                        );

                        clear(
                                "🏆 Assessment Complete",
                                lesson.getTitle()
                        );

                        content.addView(card(
                                "🎉 Result",
                                "Score: "
                                        + percentage
                                        + "%\n\n"
                                        + "Correct answers: "
                                        + correct
                                        + " / "
                                        + total
                                        + "\n\n"
                                        + "CIA AI will continue adapting "
                                        + "future assessments to your "
                                        + "performance.",
                                null
                        ));

                        content.addView(button(
                                "🧪 Different Practice",
                                x -> showSchoolPractice(
                                        topic,
                                        lesson
                                )
                        ));

                        content.addView(button(
                                "← Back to Lesson",
                                x -> showCiaTopic(
                                        topic,
                                        lesson
                                )
                        ));
                    }
                }
        ));

        content.addView(button(
                "← Back to Lesson",
                v -> showCiaTopic(
                        topic,
                        lesson
                )
        ));
    }

    private void showCiaHomework() {
        clear("📝 CIA Homework", "NOVA School");

        content.addView(card(
                "Homework",
                "Choose a topic, explain what you learned in your own words, "
                        + "then complete a practical exercise.",
                null
        ));

        content.addView(card(
                "🎙️ Ask CIA AI",
                "Use voice input if you need help.",
                v -> startCiaListening()
        ));

        content.addView(button(
                "← Back",
                v -> showCiaTeacher()
        ));
    }

    private void showCiaProgress() {
        

        clear("📊 Learning Progress", "CIA AI • NOVA School");

        content.addView(card(
                "Completed Lessons",
                NovaSchoolLearning.completedCount(this) + " lesson(s) completed.",
                null
        ));

        content.addView(button(
                "← Back",
                v -> showCiaTeacher()
        ));
    }


    private void showSchoolDigitalUniversity() {
        showSchoolCatalogue(
                "🎓 NOVA Digital University",
                "University-style technology education with progressive courses and practical work.",
                new String[]{
                        "Software Engineering",
                        "Artificial Intelligence",
                        "Data Science",
                        "Database Engineering",
                        "Game Development",
                        "Web Development",
                        "App Development",
                        "UI/UX Design",
                        "Cybersecurity",
                        "Robotics"
                }
        );
    }

    private void showSchoolICTAcademy() {
        showSchoolCatalogue(
                "💻 ICT Academy",
                "Build strong ICT knowledge from fundamentals to advanced technology systems.",
                NovaSchool.ictAcademy().toArray(new String[0])
        );
    }

    private void showSchoolTermux() {
        showSchoolCatalogue(
                "📱 Termux & Android Terminal School",
                "Learn terminal computing, Linux, scripting, Git and Android development.",
                NovaSchool.termuxCourses().toArray(new String[0])
        );
    }

    private void showSchoolCoding() {
        showSchoolCatalogue(
                "🧑‍💻 Coding School",
                "Progressive coding and software engineering courses with practice.",
                NovaSchool.codingCourses().toArray(new String[0])
        );
    }

    private void showSchoolWeb() {
        showSchoolCatalogue(
                "🌐 Web Development School",
                "Build modern websites, web apps and progressive web applications.",
                new String[]{
                        "HTML",
                        "CSS",
                        "JavaScript",
                        "Responsive Web Design",
                        "Web Forms",
                        "Frontend Components",
                        "React",
                        "APIs",
                        "Authentication",
                        "Databases",
                        "Progressive Web Apps",
                        "SEO"
                }
        );
    }

    private void showSchoolAI() {
        showSchoolCatalogue(
                "🤖 AI School",
                "Study artificial intelligence, machine learning, data and AI engineering.",
                new String[]{
                        "Artificial Intelligence",
                        "Machine Learning",
                        "Data Science",
                        "Neural Networks",
                        "Computer Vision",
                        "Natural Language Processing",
                        "AI Agents",
                        "Prompt Engineering",
                        "AI Ethics",
                        "AI Engineering"
                }
        );
    }

    private void showSchoolCybersecurity() {
        showSchoolCatalogue(
                "🛡️ Ethical Hacking School",
                "Learn cybersecurity through authorized, isolated NOVA laboratories.",
                new String[]{
                        "Cybersecurity Fundamentals",
                        "Linux Security",
                        "Network Security",
                        "Web Security",
                        "API Security",
                        "Authentication Security",
                        "Database Security",
                        "Android Security",
                        "Digital Forensics",
                        "Incident Response",
                        "Secure Coding",
                        "CTF Fundamentals"
                }
        );
    }

    private void showSchoolDatabase() {
        showSchoolCatalogue(
                "🗄️ Database School",
                "Learn database design, SQL, optimization, analysis and data management.",
                new String[]{
                        "Database Fundamentals",
                        "Relational Database Design",
                        "SQL",
                        "Queries",
                        "Joins",
                        "Indexes",
                        "Transactions",
                        "Database Security",
                        "Query Optimization",
                        "Data Cleaning",
                        "Data Analysis",
                        "Database Projects"
                }
        );
    }

    private void showSchoolGameDevelopment() {
        showSchoolCatalogue(
                "🎮 Game Development School",
                "Learn game systems, programming, physics, animation and production.",
                new String[]{
                        "Game Design Fundamentals",
                        "2D Game Development",
                        "3D Game Development",
                        "Game Programming",
                        "Game Physics",
                        "Characters",
                        "Maps and Levels",
                        "Animation",
                        "NPC Systems",
                        "Dialogue Systems",
                        "Audio Systems",
                        "Save Systems",
                        "Multiplayer Fundamentals",
                        "Game Testing"
                }
        );
    }

    private void showSchoolDigitalCity() {
        clear("🏙️ Digital City Simulator", "NOVA ICT Project Lab");

        content.addView(card(
                "🌆 Smart City Systems",
                "Design and explore a simulated city containing connected technology systems.",
                null
        ));

        content.addView(card(
                "🚦 Transport Network",
                "Model traffic, roads, signals and intelligent transport systems.",
                null
        ));

        content.addView(card(
                "⚡ Energy Network",
                "Explore electricity generation, distribution and smart-grid concepts.",
                null
        ));

        content.addView(card(
                "🌐 City Network",
                "Design an authorized simulated communication network connecting city services.",
                null
        ));

        content.addView(card(
                "🏥 Public Services",
                "Model technology systems for hospitals, schools and emergency services.",
                null
        ));

        content.addView(button(
                "← Back to NOVA School",
                v -> showNovaSchool()
        ));
    }

    private void showSchoolInnovationLab() {
        clear("🧠 ICT Innovation Lab", "Turn ideas into technology projects");

        content.addView(card(
                "💡 Problem Discovery",
                "Identify a real-world problem and define a measurable technology solution.",
                null
        ));

        content.addView(card(
                "🛠️ Prototype",
                "Create a working prototype using software, hardware or a combination of both.",
                null
        ));

        content.addView(card(
                "🧪 Test & Improve",
                "Test the prototype, record results and improve the design.",
                null
        ));

        content.addView(card(
                "📊 Present",
                "Prepare documentation, demonstrations and a final project presentation.",
                null
        ));

        content.addView(button(
                "← Back to NOVA School",
                v -> showNovaSchool()
        ));
    }


    private void showScienceDepartment() {
        clear(
                "🔬 Science Department",
                "Physics • Chemistry • Biology • O-Level learning"
        );

        content.addView(card(
                "📚 Science Progress",
                NovaScience.completedCount(this)
                        + " science lesson(s) completed.",
                null
        ));

        content.addView(card(
                "⚡ Physics",
                "Source-based Physics teaching using the supplied O-Level Physics PDFs, followed by O-Level practice.",
                v -> showScienceSubject("Physics")
        ));

        content.addView(card(
                "🧪 Chemistry",
                "O-Level Chemistry: theory, equations, practical work and adaptive practice.",
                v -> showScienceSubject("Chemistry")
        ));

        content.addView(card(
                "🧬 Biology",
                "O-Level Biology: concepts, processes, diagrams, experiments and adaptive practice.",
                v -> showScienceSubject("Biology")
        ));

        content.addView(button(
                "← Back to NOVA School",
                v -> showNovaSchool()
        ));
    }

    private void showScienceSubject(String subject) {
        clear(
                "🔬 " + subject,
                "NOVA School Science Department"
        );

        java.util.List<String> lessons;

        if ("Physics".equals(subject)) {
            lessons = NovaScience.physicsLessons();
        } else if ("Chemistry".equals(subject)) {
            lessons = NovaScience.chemistryLessons();
        } else {
            lessons = NovaScience.biologyLessons();
        }

        for (String topic : lessons) {
            content.addView(card(
                    "📖 " + topic,
                    "Open lesson • Teach • Practice • Adaptive assessment",
                    v -> showScienceLesson(subject, topic)
            ));
        }

        content.addView(button(
                "← Back to Science Department",
                v -> showScienceDepartment()
        ));
    }

    private void showScienceLesson(
            String subject,
            String topic
    ) {
        clear(
                "📖 " + topic,
                subject + " • NOVA School Science"
        );

        content.addView(card(
                "👨‍🏫 CIA AI TEACHING",
                NovaScience.teach(subject, topic),
                null
        ));

        content.addView(button(
                "🧠 Start Adaptive Practice",
                v -> {
                    NovaQuestionBank.Difficulty difficulty =
                            NovaQuestionBank.Difficulty.FOUNDATION;

                    String difficultyName =
                            NovaAdaptiveEngine.recommendedDifficulty(
                                    this,
                                    subject,
                                    NovaAdaptiveEngine.ActivityType.ASSIGNMENT
                            );

                    if ("Advanced".equalsIgnoreCase(difficultyName)) {
                        difficulty =
                                NovaQuestionBank.Difficulty.ADVANCED;
                    } else if ("Intermediate".equalsIgnoreCase(difficultyName)) {
                        difficulty =
                                NovaQuestionBank.Difficulty.INTERMEDIATE;
                    }

                    java.util.List<NovaQuestionBank.Question> questions =
                            NovaQuestionBank.createAssessment(
                                    this,
                                    subject,
                                    topic,
                                    difficulty,
                                    3
                            );

                    showSciencePractice(
                            subject,
                            topic,
                            questions,
                            difficultyName
                    );
                }
        ));

        content.addView(button(
                "✅ Mark Lesson Complete",
                v -> {
                    NovaScience.markCompleted(
                            this,
                            subject,
                            topic
                    );

                    Toast.makeText(
                            this,
                            "Science lesson completed",
                            Toast.LENGTH_SHORT
                    ).show();

                    showScienceSubject(subject);
                }
        ));

        content.addView(button(
                "← Back to " + subject,
                v -> showScienceSubject(subject)
        ));
    }

    private void showSciencePractice(
            String subject,
            String topic,
            java.util.List<NovaQuestionBank.Question> questions,
            String difficultyName
    ) {
        clear(
                "🧠 Adaptive Science Practice",
                subject + " • " + topic
        );

        content.addView(card(
                "CIA AI ADAPTIVE PRACTICE",
                "Difficulty: " + difficultyName
                        + "\\nQuestions: " + questions.size()
                        + "\\n\\nNOVA avoids recently attempted questions whenever unused variants are available.",
                null
        ));

        if (questions.isEmpty()) {
            content.addView(card(
                    "⚠️ No Questions",
                    "No practice questions are currently available for this topic.",
                    null
            ));

            content.addView(button(
                    "← Back to Lesson",
                    v -> showScienceLesson(subject, topic)
            ));

            return;
        }

        java.util.List<EditText> answers =
                new java.util.ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {

            NovaQuestionBank.Question q =
                    questions.get(i);

            content.addView(text(
                    "Question " + (i + 1),
                    18,
                    true
            ));

            content.addView(text(
                    q.getQuestion(),
                    16,
                    false
            ));

            EditText answer =
                    new EditText(this);

            answer.setHint("Type your answer");
            answer.setMinLines(2);

            content.addView(answer);
            answers.add(answer);
        }

        content.addView(button(
                "✅ Submit Practice",
                v -> {
                    int correct = 0;

                    for (int i = 0; i < questions.size(); i++) {

                        NovaQuestionBank.Question q =
                                questions.get(i);

                        String student =
                                answers.get(i)
                                        .getText()
                                        .toString()
                                        .trim();

                        NovaQuestionBank.recordAttempt(
                                this,
                                subject,
                                topic,
                                q.getId()
                        );

                        if (NovaQuestionBank.isAnswerCorrect(
                                student,
                                q.getAnswer()
                        )) {
                            correct++;
                        }
                    }

                    int total = questions.size();

                    int percentage =
                            total > 0
                                    ? (correct * 100) / total
                                    : 0;

                    NovaAdaptiveEngine.recordScore(
                            this,
                            subject,
                            NovaAdaptiveEngine.ActivityType.ASSIGNMENT,
                            percentage
                    );

                    clear(
                            "📊 Science Practice Result",
                            subject + " • " + topic
                    );

                    content.addView(card(
                            percentage >= 50
                                    ? "🎉 Good Work"
                                    : "📚 More Reteaching Needed",
                            "Score: " + percentage + "%\\n\\n"
                                    + "Correct: " + correct
                                    + " / " + total,
                            null
                    ));

                    if (percentage < 50) {
                        NovaAdaptiveEngine.requestReteach(
                                this,
                                subject,
                                topic
                        );

                        content.addView(card(
                                "🤖 CIA AI",
                                "This topic has been marked for additional reteaching. Review the lesson and try another adaptive practice set.",
                                null
                        ));
                    }

                    content.addView(button(
                            "📖 Review Lesson",
                            x -> showScienceLesson(subject, topic)
                    ));

                    content.addView(button(
                            "← Back to Science",
                            x -> showScienceSubject(subject)
                    ));
                }
        ));

        content.addView(button(
                "← Back to Lesson",
                v -> showScienceLesson(subject, topic)
        ));
    }


    private void showScienceTopic(String subject, String topic) {
        clear(
                "📖 " + topic,
                subject + " • O Level Science"
        );

        content.addView(card(
                "🎓 Teach",
                "NOVA will teach the " + topic + " topic using the approved Science learning material. For Physics, supplied PDF material is used where the topic is covered; otherwise O Level content is used.",
                null
        ));

        content.addView(button(
                "🧠 Start Teaching",
                v -> {
                    speakCia(
                            "Teach me the O Level " + subject
                                    + " topic " + topic
                                    + " step by step."
                    );
                }
        ));

        content.addView(card(
                "📝 Practice",
                "Complete adaptive practice questions for " + topic
                        + ". NOVA selects questions according to your learning history.",
                null
        ));

        content.addView(button(
                "📝 Start Practice",
                v -> showSchoolPractice(
                        topic,
                        new NovaSchoolLearning.Lesson(
                                "science-" + subject.toLowerCase()
                                        + "-" + topic.toLowerCase()
                                                .replace(" ", "-"),
                                topic,
                                "O Level " + subject
                                        + " lesson",
                                "O Level " + subject
                                        + " content for " + topic
                        )
                )
        ));

        content.addView(button(
                "← Back to " + subject,
                v -> showScienceSubject(subject)
        ));
    }

    private void showSchoolLabs() {
        clear("🧪 Practical Labs", "NOVA School laboratories");

        content.addView(card(
                "📊 Lab Progress",
                NovaSchoolLabs.completedCount(this) + " lab(s) completed.",
                null
        ));

        for (NovaSchoolLabs.Lab lab : NovaSchoolLabs.labs()) {
            String status = NovaSchoolLabs.isCompleted(this, lab.getId())
                    ? "✅ Completed"
                    : "🧪 " + lab.getDifficulty();

            content.addView(card(
                    lab.getTitle(),
                    lab.getCategory() + " • " + status + "\n\n" + lab.getDescription(),
                    v -> showSchoolLab(lab)
            ));
        }

        content.addView(button(
                "← Back to NOVA School",
                v -> showNovaSchool()
        ));
    }

    private void showSchoolLab(NovaSchoolLabs.Lab lab) {
        clear("🧪 " + lab.getTitle(), lab.getCategory());

        content.addView(card(
                "Laboratory Task",
                lab.getDescription()
                        + "\n\nDifficulty: " + lab.getDifficulty()
                        + "\n\nComplete the task inside the authorized NOVA laboratory.",
                null
        ));

        content.addView(button(
                "✅ Mark Lab Complete",
                v -> {
                    NovaSchoolLabs.markCompleted(this, lab.getId());
                    showSchoolLabs();
                }
        ));

        content.addView(button(
                "← Back to Labs",
                v -> showSchoolLabs()
        ));
    }

    private void showNovaArena() {
        clear("🏆 NOVA ARENA", "Competitions, championships and skill battles");

        content.addView(card(
                "🏅 Competition Profile",
                "XP: " + NovaSchoolCompetitions.getXp(this)
                        + "\nRating: " + NovaSchoolCompetitions.getRating(this)
                        + "\nRank: " + NovaSchoolCompetitions.getRank(this),
                null
        ));

        for (NovaSchoolCompetitions.Competition competition
                : NovaSchoolCompetitions.competitions()) {

            content.addView(card(
                    competition.getTitle(),
                    competition.getCategory()
                            + " • " + competition.getMode()
                            + " • " + competition.getSchedule()
                            + "\n\n" + competition.getDescription()
                            + "\n\nReward: " + competition.getRewardXp() + " XP",
                    v -> showCompetition(competition)
            ));
        }

        content.addView(button(
                "← Back to NOVA School",
                v -> showNovaSchool()
        ));
    }

    private void showCompetition(
            NovaSchoolCompetitions.Competition competition) {

        clear("🏆 " + competition.getTitle(), competition.getCategory());

        content.addView(card(
                "Competition",
                competition.getDescription()
                        + "\n\nMode: " + competition.getMode()
                        + "\nSchedule: " + competition.getSchedule()
                        + "\nReward: " + competition.getRewardXp() + " XP",
                null
        ));

        content.addView(button(
                "🏆 Record Practice Result",
                v -> {
                    NovaSchoolCompetitions.recordResult(
                            this,
                            competition.getRewardXp(),
                            25
                    );
                    showNovaArena();
                }
        ));

        content.addView(button(
                "← Back to Arena",
                v -> showNovaArena()
        ));
    }

    private void showCertificationCentre() {
        clear("📜 Certification Centre", "NOVA School credentials");

        content.addView(card(
                "🎓 Course Certificates",
                "Certificates for completed NOVA School courses.",
                null
        ));

        content.addView(card(
                "🛠️ Skill Certificates",
                "Recognize demonstrated skills across NOVA learning paths.",
                null
        ));

        content.addView(card(
                "🏫 School Certificates",
                "NOVA School completion credentials.",
                null
        ));

        content.addView(card(
                "🎓 University Certificates",
                "Credentials for completed NOVA Digital University learning paths.",
                null
        ));

        content.addView(card(
                "🏆 Competition Certificates",
                "Credentials for NOVA Arena competition achievements.",
                null
        ));

        content.addView(card(
                "⭐ Excellence Awards",
                "Recognition for exceptional learning, projects and competition performance.",
                null
        ));

        content.addView(card(
                "🔰 Digital Badges",
                "Collect NOVA digital badges for verified milestones.",
                null
        ));

        content.addView(card(
                "ℹ️ Credential Status",
                "These are NOVA-issued learning credentials. They should not be represented as government or externally accredited qualifications unless official accreditation is actually established.",
                null
        ));

        content.addView(button(
                "← Back to NOVA School",
                v -> showNovaSchool()
        ));
    }

    private void showStudentPortfolio() {
        clear("🪪 Skill Passport", "Your NOVA learning portfolio");

        content.addView(card(
                "📚 Learning Progress",
                NovaSchoolLearning.completedCount(this)
                        + " lesson(s) completed.",
                null
        ));

        content.addView(card(
                "🧪 Laboratory Progress",
                NovaSchoolLabs.completedCount(this)
                        + " lab(s) completed.",
                null
        ));

        content.addView(card(
                "🏆 Competition Profile",
                "XP: " + NovaSchoolCompetitions.getXp(this)
                        + "\nRating: " + NovaSchoolCompetitions.getRating(this)
                        + "\nRank: " + NovaSchoolCompetitions.getRank(this),
                null
        ));

        content.addView(card(
                "📜 Certificates",
                "Course certificates, skill certificates, school certificates, university certificates and competition credentials.",
                null
        ));

        content.addView(card(
                "🏅 Badges & Trophies",
                "Track NOVA badges, trophies, achievements, streaks and milestones.",
                null
        ));

        content.addView(card(
                "💻 Projects",
                "Your coding, programming, database, cybersecurity, web, app and AI projects.",
                null
        ));

        content.addView(card(
                "💡 Innovations",
                "Record inventions, prototypes, research work and NOVA Innovation Lab projects.",
                null
        ));

        content.addView(button(
                "← Back to NOVA School",
                v -> showNovaSchool()
        ));
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

        String[] items = {"HOME", "M3GAN", "CODE", "SCHOOL", "PROJECTS", "MORE"};

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
                else if (item.equals("SCHOOL"))
                    showNovaSchool();
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
