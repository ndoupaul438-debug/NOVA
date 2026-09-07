package com.nova.app.school;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class NovaSchoolChallenges {

    private static final String PREFS = "nova_school_challenges";
    private static final String COMPLETED = "completed_challenges";
    private static final String XP = "xp";

    private NovaSchoolChallenges() {}

    public static List<Challenge> challenges() {
        return new ArrayList<>(Arrays.asList(

            new Challenge(
                    "termux-001",
                    "Terminal Master",
                    "Termux & Android Terminal School",
                    "Complete a series of safe terminal navigation tasks.",
                    100,
                    "Beginner"
            ),

            new Challenge(
                    "linux-001",
                    "Linux Explorer",
                    "Termux & Android Terminal School",
                    "Solve Linux file and directory navigation tasks.",
                    150,
                    "Beginner"
            ),

            new Challenge(
                    "python-001",
                    "Python Builder",
                    "Programming School",
                    "Build a small Python program that solves a practical problem.",
                    250,
                    "Intermediate"
            ),

            new Challenge(
                    "java-001",
                    "Java Engineer",
                    "Programming School",
                    "Create a Java application using classes, methods and input.",
                    300,
                    "Intermediate"
            ),

            new Challenge(
                    "web-001",
                    "Web Creator",
                    "Web Development School",
                    "Build a responsive webpage using HTML, CSS and JavaScript.",
                    300,
                    "Intermediate"
            ),

            new Challenge(
                    "database-001",
                    "Database Architect",
                    "Database School",
                    "Design a relational database and write useful SQL queries.",
                    350,
                    "Intermediate"
            ),

            new Challenge(
                    "ai-001",
                    "AI Explorer",
                    "AI School",
                    "Analyze a dataset and design a simple AI workflow.",
                    400,
                    "Advanced"
            ),

            new Challenge(
                    "network-001",
                    "Network Engineer",
                    "ICT Academy",
                    "Design a small authorized network and document its architecture.",
                    400,
                    "Advanced"
            ),

            new Challenge(
                    "security-001",
                    "CTF Defender",
                    "Ethical Hacking School",
                    "Complete security challenges inside an isolated NOVA CTF laboratory.",
                    500,
                    "Advanced"
            ),

            new Challenge(
                    "robotics-001",
                    "Robotics Engineer",
                    "Robotics & IoT",
                    "Design a basic sensor-controlled robotic system.",
                    500,
                    "Advanced"
            ),

            new Challenge(
                    "innovation-001",
                    "Build Anything",
                    "ICT Innovation Lab",
                    "Create an original technology solution to a real-world problem.",
                    1000,
                    "Expert"
            )

        ));
    }

    public static void complete(
            Context context,
            String challengeId,
            int xp
    ) {
        SharedPreferences preferences =
                context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        String completed =
                preferences.getString(COMPLETED, "");

        if (!completed.contains(challengeId)) {
            completed = completed.isEmpty()
                    ? challengeId
                    : completed + "," + challengeId;

            int currentXp = preferences.getInt(XP, 0);

            preferences.edit()
                    .putString(COMPLETED, completed)
                    .putInt(XP, currentXp + xp)
                    .apply();
        }
    }

    public static boolean isCompleted(
            Context context,
            String challengeId
    ) {
        String completed =
                context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                        .getString(COMPLETED, "");

        return completed.contains(challengeId);
    }

    public static int getXp(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getInt(XP, 0);
    }

    public static final class Challenge {

        private final String id;
        private final String title;
        private final String category;
        private final String description;
        private final int xp;
        private final String difficulty;

        public Challenge(
                String id,
                String title,
                String category,
                String description,
                int xp,
                String difficulty
        ) {
            this.id = id;
            this.title = title;
            this.category = category;
            this.description = description;
            this.xp = xp;
            this.difficulty = difficulty;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getCategory() {
            return category;
        }

        public String getDescription() {
            return description;
        }

        public int getXp() {
            return xp;
        }

        public String getDifficulty() {
            return difficulty;
        }
    }
}
