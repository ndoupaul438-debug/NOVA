package com.nova.app.school;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class NovaSchoolCompetitions {

    private static final String PREFS = "nova_school_competitions";
    private static final String XP = "competition_xp";
    private static final String RATING = "competition_rating";

    private NovaSchoolCompetitions() {}

    public static List<Competition> competitions() {
        return new ArrayList<>(Arrays.asList(

                new Competition(
                        "programming",
                        "Programming Championship",
                        "Programming",
                        "Solve algorithmic programming problems.",
                        "Human vs Human",
                        "Tournament",
                        1000
                ),

                new Competition(
                        "coding",
                        "NOVA Coding Arena",
                        "Coding",
                        "Build working software under time and feature constraints.",
                        "Human vs Human",
                        "Tournament",
                        1200
                ),

                new Competition(
                        "chess",
                        "NOVA Chess Championship",
                        "Chess",
                        "Compete in strategic chess matches.",
                        "Human vs Human",
                        "Championship",
                        1000
                ),

                new Competition(
                        "mind-games",
                        "Mind Games Arena",
                        "Mind Games",
                        "Test logic, memory, reasoning and problem solving.",
                        "AI vs Human",
                        "Daily",
                        500
                ),

                new Competition(
                        "ethical-hacking",
                        "NOVA Cyber CTF",
                        "Ethical Hacking",
                        "Solve cybersecurity challenges inside authorized isolated NOVA laboratories.",
                        "Human vs Human",
                        "Tournament",
                        1500
                ),

                new Competition(
                        "app-building",
                        "App Builder Championship",
                        "App Building",
                        "Build a complete application from a project brief.",
                        "Human vs Human",
                        "Monthly",
                        2000
                ),

                new Competition(
                        "game-building",
                        "Game Creator Championship",
                        "Game Building",
                        "Design and build a playable game.",
                        "Human vs Human",
                        "Monthly",
                        2000
                ),

                new Competition(
                        "website-building",
                        "Web Creator Championship",
                        "Website Building",
                        "Build a complete responsive website.",
                        "Human vs Human",
                        "Monthly",
                        1500
                ),

                new Competition(
                        "web-apps",
                        "Web App Engineering",
                        "Web Apps",
                        "Build a functional full-stack web application.",
                        "Team Battle",
                        "Tournament",
                        2000
                ),

                new Competition(
                        "database",
                        "Database Masters",
                        "Databases",
                        "Design databases and solve SQL challenges.",
                        "Human vs Human",
                        "Weekly",
                        1200
                ),

                new Competition(
                        "ai",
                        "AI Agent Battle",
                        "Artificial Intelligence",
                        "Design AI solutions and compete against other systems.",
                        "AI vs Human",
                        "Seasonal",
                        2500
                ),

                new Competition(
                        "networking",
                        "Network Engineering Challenge",
                        "Networking",
                        "Design, configure and troubleshoot authorized network simulations.",
                        "Team Battle",
                        "Tournament",
                        1800
                ),

                new Competition(
                        "robotics",
                        "NOVA Robotics Cup",
                        "Robotics",
                        "Design robotic systems and solve engineering challenges.",
                        "Team Battle",
                        "Championship",
                        2500
                ),

                new Competition(
                        "ict",
                        "NOVA ICT Championship",
                        "ICT",
                        "Compete across multiple ICT disciplines.",
                        "Team Battle",
                        "Championship",
                        3000
                ),

                new Competition(
                        "innovation",
                        "Grand Innovation War",
                        "Innovation",
                        "Build an original technology solution to a major real-world problem.",
                        "Team Battle",
                        "Global",
                        5000
                ),

                new Competition(
                        "digital-city",
                        "Digital City Challenge",
                        "Digital City",
                        "Design systems for a simulated smart city.",
                        "Team Battle",
                        "Seasonal",
                        3500
                )

        ));
    }

    public static void recordResult(
            Context context,
            int earnedXp,
            int ratingChange
    ) {
        SharedPreferences preferences =
                context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        int xp = preferences.getInt(XP, 0);
        int rating = preferences.getInt(RATING, 1000);

        preferences.edit()
                .putInt(XP, xp + Math.max(0, earnedXp))
                .putInt(RATING, Math.max(0, rating + ratingChange))
                .apply();
    }

    public static int getXp(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getInt(XP, 0);
    }

    public static int getRating(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getInt(RATING, 1000);
    }

    public static String getRank(Context context) {
        int rating = getRating(context);

        if (rating >= 2500) return "Grand Champion";
        if (rating >= 2200) return "Master";
        if (rating >= 1900) return "Diamond";
        if (rating >= 1600) return "Platinum";
        if (rating >= 1300) return "Gold";
        if (rating >= 1100) return "Silver";
        return "Bronze";
    }

    public static final class Competition {

        private final String id;
        private final String title;
        private final String category;
        private final String description;
        private final String mode;
        private final String schedule;
        private final int rewardXp;

        public Competition(
                String id,
                String title,
                String category,
                String description,
                String mode,
                String schedule,
                int rewardXp
        ) {
            this.id = id;
            this.title = title;
            this.category = category;
            this.description = description;
            this.mode = mode;
            this.schedule = schedule;
            this.rewardXp = rewardXp;
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

        public String getMode() {
            return mode;
        }

        public String getSchedule() {
            return schedule;
        }

        public int getRewardXp() {
            return rewardXp;
        }
    }
}
