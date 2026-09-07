package com.nova.app.school;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class NovaSchoolGames {

    private static final String PREFS = "nova_school_games";
    private static final String XP = "game_xp";

    private NovaSchoolGames() {}

    public static List<Game> games() {
        return new ArrayList<>(Arrays.asList(

                new Game(
                        "code-runner",
                        "Code Runner",
                        "Coding",
                        "Solve programming puzzles by writing correct code.",
                        "Beginner"
                ),

                new Game(
                        "algorithm-arena",
                        "Algorithm Arena",
                        "Programming",
                        "Race against the clock to solve algorithmic problems.",
                        "Intermediate"
                ),

                new Game(
                        "debug-master",
                        "Debug Master",
                        "Debugging",
                        "Find and fix bugs hidden inside programs.",
                        "Intermediate"
                ),

                new Game(
                        "sql-detective",
                        "SQL Detective",
                        "Database",
                        "Investigate datasets and solve mysteries using SQL.",
                        "Intermediate"
                ),

                new Game(
                        "network-builder",
                        "Network Builder",
                        "Networking",
                        "Connect simulated devices and design working networks.",
                        "Intermediate"
                ),

                new Game(
                        "cyber-defense",
                        "Cyber Defense",
                        "Cybersecurity",
                        "Defend an isolated NOVA laboratory from simulated attacks.",
                        "Advanced"
                ),

                new Game(
                        "forensics-case",
                        "Digital Forensics Case",
                        "Digital Forensics",
                        "Analyze simulated evidence and reconstruct an incident.",
                        "Advanced"
                ),

                new Game(
                        "ai-lab",
                        "AI Lab",
                        "Artificial Intelligence",
                        "Experiment with datasets, models and AI decision making.",
                        "Advanced"
                ),

                new Game(
                        "robotics-control",
                        "Robotics Control",
                        "Robotics",
                        "Program a simulated robot to complete missions.",
                        "Advanced"
                ),

                new Game(
                        "chess",
                        "NOVA Chess",
                        "Chess",
                        "Play chess and improve strategic thinking.",
                        "All Levels"
                ),

                new Game(
                        "logic-masters",
                        "Logic Masters",
                        "Mind Games",
                        "Solve logic, memory, pattern and reasoning puzzles.",
                        "All Levels"
                ),

                new Game(
                        "programming-rpg",
                        "Programming RPG",
                        "Programming",
                        "Progress through a programming adventure by solving coding missions.",
                        "Progressive"
                ),

                new Game(
                        "survival-coding",
                        "Survival Coding",
                        "Coding",
                        "Keep your program running while solving increasingly difficult problems.",
                        "Advanced"
                ),

                new Game(
                        "digital-city",
                        "Digital City Simulator",
                        "ICT",
                        "Manage networks, transport, IoT and digital infrastructure in a simulated city.",
                        "Expert"
                ),

                new Game(
                        "innovation-builder",
                        "Innovation Builder",
                        "Innovation",
                        "Turn a real-world problem into a technology solution.",
                        "Expert"
                )

        ));
    }

    public static void awardXp(Context context, int amount) {
        SharedPreferences preferences =
                context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        int current =
                preferences.getInt(XP, 0);

        preferences.edit()
                .putInt(XP, current + Math.max(0, amount))
                .apply();
    }

    public static int getXp(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getInt(XP, 0);
    }

    public static final class Game {

        private final String id;
        private final String title;
        private final String category;
        private final String description;
        private final String difficulty;

        public Game(
                String id,
                String title,
                String category,
                String description,
                String difficulty
        ) {
            this.id = id;
            this.title = title;
            this.category = category;
            this.description = description;
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

        public String getDifficulty() {
            return difficulty;
        }
    }
}
