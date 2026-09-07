package com.nova.app.school;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class NovaSchoolLearning {

    private static final String PREFS = "nova_school_learning";
    private static final String COMPLETED = "completed_lessons";
    private static final String SCORES = "quiz_scores";

    private final SharedPreferences prefs;

    public NovaSchoolLearning(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public List<Lesson> lessons(String course) {
        List<Lesson> list = new ArrayList<>();

        if ("Termux & Android Terminal School".equalsIgnoreCase(course)) {
            list.add(new Lesson(
                    "termux-01",
                    "Termux Fundamentals",
                    "Learn what Termux is, how packages work and how to start a terminal session.",
                    new String[]{
                            "Termux provides a Linux-like terminal environment on Android.",
                            "Commands can be used to manage files, run programs and automate tasks.",
                            "Packages should be installed from trusted repositories.",
                            "Never use security tools against systems without authorization."
                    }
            ));

            list.add(new Lesson(
                    "termux-02",
                    "Linux File Management",
                    "Learn the core commands used to navigate and manage files.",
                    new String[]{
                            "pwd shows the current working directory.",
                            "ls lists files and directories.",
                            "cd changes directories.",
                            "mkdir creates directories.",
                            "cp copies files and directories.",
                            "mv moves or renames files.",
                            "rm removes files, so use it carefully."
                    }
            ));

            list.add(new Lesson(
                    "termux-03",
                    "Bash Scripting",
                    "Learn how to automate repeated terminal tasks.",
                    new String[]{
                            "Bash scripts are text files containing shell commands.",
                            "Variables can store values for later use.",
                            "Conditions allow scripts to make decisions.",
                            "Loops allow repeated operations.",
                            "Always test scripts in a safe workspace."
                    }
            ));

            list.add(new Lesson(
                    "termux-04",
                    "Python in Termux",
                    "Build command-line programs using Python.",
                    new String[]{
                            "Python can be installed and executed from Termux.",
                            "Python scripts can read files, process data and create utilities.",
                            "Small command-line projects are excellent practice.",
                            "Keep projects organized inside your NOVA workspace."
                    }
            ));

            list.add(new Lesson(
                    "termux-05",
                    "Git and GitHub",
                    "Learn version control and collaborative development.",
                    new String[]{
                            "Git records changes to a project.",
                            "A repository contains the project's tracked history.",
                            "Commits provide checkpoints.",
                            "Branches allow isolated development.",
                            "GitHub can host repositories and support collaboration."
                    }
            ));

        } else if ("Coding School".equalsIgnoreCase(course)) {
            list.add(new Lesson(
                    "code-01",
                    "Programming Fundamentals",
                    "Understand variables, conditions, loops and functions.",
                    new String[]{
                            "Programs process input and produce useful output.",
                            "Variables store data.",
                            "Conditions control decisions.",
                            "Loops repeat operations.",
                            "Functions organize reusable logic."
                    }
            ));

            list.add(new Lesson(
                    "code-02",
                    "Object-Oriented Programming",
                    "Learn classes, objects, methods and encapsulation.",
                    new String[]{
                            "A class defines a structure and behavior.",
                            "An object is an instance of a class.",
                            "Methods represent behavior.",
                            "Encapsulation keeps implementation details controlled."
                    }
            ));

            list.add(new Lesson(
                    "code-03",
                    "Debugging",
                    "Learn how to find and fix programming problems.",
                    new String[]{
                            "Read the error message first.",
                            "Reproduce the problem consistently.",
                            "Inspect the smallest section that could cause the issue.",
                            "Make one change at a time.",
                            "Test again after every meaningful fix."
                    }
            ));

        } else if ("ICT Academy".equalsIgnoreCase(course)) {
            list.add(new Lesson(
                    "ict-01",
                    "Computer Fundamentals",
                    "Understand hardware, software, storage and operating systems.",
                    new String[]{
                            "Hardware is the physical part of a computer.",
                            "Software provides instructions for hardware.",
                            "RAM is temporary working memory.",
                            "Storage keeps data when power is removed.",
                            "An operating system manages hardware and applications."
                    }
            ));

            list.add(new Lesson(
                    "ict-02",
                    "Networking Fundamentals",
                    "Learn the basic concepts behind computer networks.",
                    new String[]{
                            "A network connects devices so they can communicate.",
                            "IP addresses identify network interfaces.",
                            "Routers connect different networks.",
                            "Switches connect devices within a local network.",
                            "Network security protects systems and data."
                    }
            ));

        } else {
            list.add(new Lesson(
                    "general-01",
                    "Welcome to NOVA School",
                    "Learn how NOVA School works.",
                    new String[]{
                            "Choose a subject or academy.",
                            "Complete lessons and practical activities.",
                            "Take assessments to measure your progress.",
                            "Participate in competitions.",
                            "Earn NOVA credentials for completed achievements."
                    }
            ));
        }

        return list;
    }

    public void markCompleted(String lessonId) {
        JSONArray array = completed();

        for (int i = 0; i < array.length(); i++) {
            if (lessonId.equals(array.optString(i))) {
                return;
            }
        }

        array.put(lessonId);

        prefs.edit()
                .putString(COMPLETED, array.toString())
                .apply();
    }

    public boolean isCompleted(String lessonId) {
        JSONArray array = completed();

        for (int i = 0; i < array.length(); i++) {
            if (lessonId.equals(array.optString(i))) {
                return true;
            }
        }

        return false;
    }

    public int completedCount() {
        return completed().length();
    }

    public void saveQuizScore(String quizId, int score, int total) {
        try {
            JSONObject scores = new JSONObject(
                    prefs.getString(SCORES, "{}")
            );

            JSONObject result = new JSONObject();
            result.put("score", score);
            result.put("total", total);
            result.put("percentage",
                    total == 0 ? 0 : (score * 100) / total);

            scores.put(quizId, result);

            prefs.edit()
                    .putString(SCORES, scores.toString())
                    .apply();

        } catch (Exception ignored) {
        }
    }

    public int getQuizPercentage(String quizId) {
        try {
            JSONObject scores = new JSONObject(
                    prefs.getString(SCORES, "{}")
            );

            JSONObject result = scores.optJSONObject(quizId);

            if (result == null) {
                return -1;
            }

            return result.optInt("percentage", -1);

        } catch (Exception e) {
            return -1;
        }
    }

    private JSONArray completed() {
        try {
            return new JSONArray(
                    prefs.getString(COMPLETED, "[]")
            );
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    public static final class Lesson {

        private final String id;
        private final String title;
        private final String description;
        private final String[] content;

        public Lesson(
                String id,
                String title,
                String description,
                String[] content
        ) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.content = content;
        }

        public String id() {
            return id;
        }

        public String title() {
            return title;
        }

        public String description() {
            return description;
        }

        public String[] content() {
            return content;
        }
    }
}
