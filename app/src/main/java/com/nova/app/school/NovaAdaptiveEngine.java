package com.nova.app.school;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class NovaAdaptiveEngine {

    private static final String PREFS = "nova_adaptive_learning";
    private static final String HISTORY_PREFIX = "history_";
    private static final String SCORE_PREFIX = "score_";
    private static final String WEAK_PREFIX = "weak_";
    private static final String RETEACH_PREFIX = "reteach_";

    private NovaAdaptiveEngine() {}

    public enum ActivityType {
        LESSON,
        ASSIGNMENT,
        PRACTICAL,
        CHALLENGE,
        QUIZ,
        EXAM,
        REASSESSMENT
    }

    /*
     * Select an activity that the student has not recently completed.
     *
     * The activity list should contain unique activity IDs.
     */
    public static String nextActivity(
            Context context,
            String course,
            ActivityType type,
            List<String> activities
    ) {
        if (activities == null || activities.isEmpty()) {
            return null;
        }

        List<String> history = getHistory(context, course, type);
        List<String> available = new ArrayList<>();

        for (String activity : activities) {
            if (activity != null
                    && !activity.trim().isEmpty()
                    && !history.contains(activity)) {
                available.add(activity);
            }
        }

        /*
         * If every known variant has been used, choose a variant
         * using a deterministic rotation instead of pretending
         * the student has never seen the activities before.
         */
        if (available.isEmpty()) {
            int index = history.size() % activities.size();
            String selected = activities.get(index);
            recordActivity(context, course, type, selected);
            return selected;
        }

        Collections.shuffle(available);

        String selected = available.get(0);
        recordActivity(context, course, type, selected);

        return selected;
    }

    public static List<String> getHistory(
            Context context,
            String course,
            ActivityType type
    ) {
        SharedPreferences preferences =
                context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        String stored =
                preferences.getString(
                        historyKey(course, type),
                        ""
                );

        List<String> result = new ArrayList<>();

        if (stored.isEmpty()) {
            return result;
        }

        String[] values = stored.split("\\|");

        for (String value : values) {
            if (!value.trim().isEmpty()) {
                result.add(value);
            }
        }

        return result;
    }

    public static void recordActivity(
            Context context,
            String course,
            ActivityType type,
            String activity
    ) {
        if (activity == null || activity.trim().isEmpty()) {
            return;
        }

        List<String> history =
                getHistory(context, course, type);

        if (!history.contains(activity)) {
            history.add(activity);
        }

        saveHistory(
                context,
                course,
                type,
                history
        );
    }

    public static boolean hasCompleted(
            Context context,
            String course,
            ActivityType type,
            String activity
    ) {
        return getHistory(
                context,
                course,
                type
        ).contains(activity);
    }

    public static int completedCount(
            Context context,
            String course,
            ActivityType type
    ) {
        return getHistory(
                context,
                course,
                type
        ).size();
    }

    /*
     * Record a score as a percentage from 0 to 100.
     */
    public static void recordScore(
            Context context,
            String course,
            ActivityType type,
            int score
    ) {
        score = Math.max(0, Math.min(100, score));

        SharedPreferences preferences =
                context.getSharedPreferences(
                        PREFS,
                        Context.MODE_PRIVATE
                );

        String key = scoreKey(course, type);

        int attempts =
                preferences.getInt(
                        key + "_attempts",
                        0
                );

        int total =
                preferences.getInt(
                        key + "_total",
                        0
                );

        preferences.edit()
                .putInt(
                        key + "_attempts",
                        attempts + 1
                )
                .putInt(
                        key + "_total",
                        total + score
                )
                .apply();
    }

    public static int averageScore(
            Context context,
            String course,
            ActivityType type
    ) {
        SharedPreferences preferences =
                context.getSharedPreferences(
                        PREFS,
                        Context.MODE_PRIVATE
                );

        String key = scoreKey(course, type);

        int attempts =
                preferences.getInt(
                        key + "_attempts",
                        0
                );

        int total =
                preferences.getInt(
                        key + "_total",
                        0
                );

        if (attempts == 0) {
            return 0;
        }

        return total / attempts;
    }

    /*
     * Difficulty adapts to the student's average performance.
     */
    public static String recommendedDifficulty(
            Context context,
            String course,
            ActivityType type
    ) {
        int score =
                averageScore(
                        context,
                        course,
                        type
                );

        if (score >= 85) {
            return "Advanced";
        }

        if (score >= 65) {
            return "Intermediate";
        }

        return "Foundation";
    }

    /*
     * Record the topic that caused a failed assessment.
     */
    public static void recordWeakTopic(
            Context context,
            String course,
            String topic
    ) {
        if (topic == null || topic.trim().isEmpty()) {
            return;
        }

        SharedPreferences preferences =
                context.getSharedPreferences(
                        PREFS,
                        Context.MODE_PRIVATE
                );

        preferences.edit()
                .putString(
                        weakKey(course),
                        topic
                )
                .apply();
    }

    public static String getWeakTopic(
            Context context,
            String course
    ) {
        return context.getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
        ).getString(
                weakKey(course),
                ""
        );
    }

    /*
     * Tell CIA AI that this topic needs reteaching.
     */
    public static void requestReteach(
            Context context,
            String course,
            String topic
    ) {
        if (topic == null || topic.trim().isEmpty()) {
            return;
        }

        context.getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
        )
                .edit()
                .putString(
                        reteachKey(course),
                        topic
                )
                .apply();

        recordWeakTopic(
                context,
                course,
                topic
        );
    }

    public static boolean needsReteach(
            Context context,
            String course
    ) {
        return !getReteachTopic(
                context,
                course
        ).isEmpty();
    }

    public static String getReteachTopic(
            Context context,
            String course
    ) {
        return context.getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
        ).getString(
                reteachKey(course),
                ""
        );
    }

    /*
     * Clear the reteaching requirement only after
     * CIA AI has actually reteached the student.
     */
    public static void clearReteach(
            Context context,
            String course
    ) {
        context.getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
        )
                .edit()
                .remove(reteachKey(course))
                .apply();
    }

    /*
     * Exam result helper.
     *
     * A failed exam automatically creates a reteaching
     * requirement for the weak topic.
     */
    public static boolean recordExamResult(
            Context context,
            String course,
            String topic,
            int percentage
    ) {
        percentage =
                Math.max(
                        0,
                        Math.min(
                                100,
                                percentage
                        )
                );

        recordScore(
                context,
                course,
                ActivityType.EXAM,
                percentage
        );

        if (percentage < 50) {
            requestReteach(
                    context,
                    course,
                    topic
            );

            return false;
        }

        clearReteach(
                context,
                course
        );

        return true;
    }

    /*
     * Reassessment must be different from the failed exam.
     */
    public static String nextReassessment(
            Context context,
            String course,
            List<String> reassessments
    ) {
        return nextActivity(
                context,
                course,
                ActivityType.REASSESSMENT,
                reassessments
        );
    }

    /*
     * Reset all adaptive data for a course.
     */
    public static void resetCourse(
            Context context,
            String course
    ) {
        SharedPreferences.Editor editor =
                context.getSharedPreferences(
                        PREFS,
                        Context.MODE_PRIVATE
                )
                        .edit();

        for (ActivityType type : ActivityType.values()) {
            editor.remove(
                    historyKey(
                            course,
                            type
                    )
            );

            String scoreKey =
                    scoreKey(
                            course,
                            type
                    );

            editor.remove(
                    scoreKey + "_attempts"
            );

            editor.remove(
                    scoreKey + "_total"
            );
        }

        editor.remove(
                weakKey(course)
        );

        editor.remove(
                reteachKey(course)
        );

        editor.apply();
    }

    private static void saveHistory(
            Context context,
            String course,
            ActivityType type,
            List<String> history
    ) {
        StringBuilder builder =
                new StringBuilder();

        for (String item : history) {
            if (builder.length() > 0) {
                builder.append("|");
            }

            builder.append(
                    item.replace("|", "")
            );
        }

        context.getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
        )
                .edit()
                .putString(
                        historyKey(
                                course,
                                type
                        ),
                        builder.toString()
                )
                .apply();
    }

    private static String historyKey(
            String course,
            ActivityType type
    ) {
        return HISTORY_PREFIX
                + normalize(course)
                + "_"
                + type.name().toLowerCase();
    }

    private static String scoreKey(
            String course,
            ActivityType type
    ) {
        return SCORE_PREFIX
                + normalize(course)
                + "_"
                + type.name().toLowerCase();
    }

    private static String weakKey(
            String course
    ) {
        return WEAK_PREFIX
                + normalize(course);
    }

    private static String reteachKey(
            String course
    ) {
        return RETEACH_PREFIX
                + normalize(course);
    }

    private static String normalize(
            String value
    ) {
        if (value == null) {
            return "unknown";
        }

        return value
                .trim()
                .toLowerCase()
                .replaceAll(
                        "[^a-z0-9]+",
                        "_"
                );
    }
}
