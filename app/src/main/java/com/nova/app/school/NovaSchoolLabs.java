package com.nova.app.school;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class NovaSchoolLabs {

    private static final String PREFS = "nova_school_labs";
    private static final String COMPLETED = "completed_labs";

    private NovaSchoolLabs() {}

    public static List<Lab> labs() {
        return new ArrayList<>(Arrays.asList(

                new Lab(
                        "linux-lab",
                        "Linux Laboratory",
                        "Termux & Android Terminal School",
                        "Practice Linux commands, files, permissions and shell operations.",
                        "Beginner"
                ),

                new Lab(
                        "programming-lab",
                        "Programming Laboratory",
                        "Programming School",
                        "Write, execute, test and debug programming exercises.",
                        "Beginner"
                ),

                new Lab(
                        "web-lab",
                        "Web Development Laboratory",
                        "Web Development School",
                        "Build and test HTML, CSS and JavaScript projects.",
                        "Beginner"
                ),

                new Lab(
                        "database-lab",
                        "Database Laboratory",
                        "Database School",
                        "Design databases and practice SQL queries.",
                        "Intermediate"
                ),

                new Lab(
                        "ai-lab",
                        "Artificial Intelligence Laboratory",
                        "AI School",
                        "Explore datasets, machine-learning concepts and model evaluation.",
                        "Advanced"
                ),

                new Lab(
                        "network-lab",
                        "Networking Laboratory",
                        "ICT Academy",
                        "Build and troubleshoot simulated networks.",
                        "Intermediate"
                ),

                new Lab(
                        "cybersecurity-lab",
                        "Cybersecurity Laboratory",
                        "Ethical Hacking School",
                        "Practice defensive security in authorized isolated laboratory environments.",
                        "Advanced"
                ),

                new Lab(
                        "ctf-lab",
                        "NOVA CTF Laboratory",
                        "Ethical Hacking School",
                        "Solve intentionally vulnerable capture-the-flag challenges inside NOVA's sandbox.",
                        "Advanced"
                ),

                new Lab(
                        "forensics-lab",
                        "Digital Forensics Laboratory",
                        "Digital Forensics",
                        "Analyze simulated evidence, logs and incident timelines.",
                        "Advanced"
                ),

                new Lab(
                        "iot-lab",
                        "IoT Laboratory",
                        "Robotics & IoT",
                        "Experiment with sensors, devices, communication and automation concepts.",
                        "Intermediate"
                ),

                new Lab(
                        "robotics-lab",
                        "Robotics Laboratory",
                        "Robotics & IoT",
                        "Design and program simulated robotic systems.",
                        "Advanced"
                ),

                new Lab(
                        "electronics-lab",
                        "Digital Electronics Laboratory",
                        "ICT Academy",
                        "Experiment with binary logic, gates and digital circuits.",
                        "Intermediate"
                ),

                new Lab(
                        "hardware-lab",
                        "Hardware Doctor Laboratory",
                        "Hardware Doctor",
                        "Diagnose simulated computer hardware problems.",
                        "Intermediate"
                ),

                new Lab(
                        "mobile-lab",
                        "Mobile Development Laboratory",
                        "Coding School",
                        "Build and test mobile application components.",
                        "Intermediate"
                ),

                new Lab(
                        "server-lab",
                        "Server Laboratory",
                        "ICT Academy",
                        "Configure and troubleshoot simulated local server environments.",
                        "Advanced"
                ),

                new Lab(
                        "cloud-lab",
                        "Cloud Computing Laboratory",
                        "ICT Academy",
                        "Practice cloud architecture and infrastructure concepts using simulations.",
                        "Advanced"
                )

        ));
    }

    public static void markCompleted(
            Context context,
            String labId
    ) {
        SharedPreferences preferences =
                context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        String completed =
                preferences.getString(COMPLETED, "");

        if (!completed.contains(labId)) {
            completed = completed.isEmpty()
                    ? labId
                    : completed + "," + labId;

            preferences.edit()
                    .putString(COMPLETED, completed)
                    .apply();
        }
    }

    public static boolean isCompleted(
            Context context,
            String labId
    ) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getString(COMPLETED, "")
                .contains(labId);
    }

    public static int completedCount(Context context) {
        String completed =
                context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                        .getString(COMPLETED, "");

        if (completed.isEmpty()) {
            return 0;
        }

        return completed.split(",").length;
    }

    public static final class Lab {

        private final String id;
        private final String title;
        private final String category;
        private final String description;
        private final String difficulty;

        public Lab(
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
