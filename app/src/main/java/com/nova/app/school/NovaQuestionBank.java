package com.nova.app.school;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class NovaQuestionBank {

    private static final String PREFS = "nova_question_bank";
    private static final String ATTEMPTED_PREFIX = "attempted_";
    private static final String LAST_ASSESSMENT_PREFIX = "last_assessment_";

    private NovaQuestionBank() {}

    public enum Difficulty {
        FOUNDATION,
        INTERMEDIATE,
        ADVANCED
    }

    public static final class Question {

        private final String id;
        private final String topic;
        private final String question;
        private final String answer;
        private final Difficulty difficulty;

        public Question(
                String id,
                String topic,
                String question,
                String answer,
                Difficulty difficulty
        ) {
            this.id = id;
            this.topic = topic;
            this.question = question;
            this.answer = answer;
            this.difficulty = difficulty;
        }

        public String getId() {
            return id;
        }

        public String getTopic() {
            return topic;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }

        public Difficulty getDifficulty() {
            return difficulty;
        }
    }

    /*
     * Return questions for a course/topic.
     *
     * The first version provides a reusable foundation.
     * More course-specific banks will be added without changing
     * the assessment engine.
     */
    public static List<Question> questions(String course, String topic) {

        List<Question> result = new ArrayList<>();

        if (course == null || topic == null) {
            return result;
        }

        String c = course.toLowerCase();
        String t = topic.toLowerCase();

        /*
         * TERMUX
         */
        if (c.contains("termux") && t.contains("fundamental")) {

            add(result,
                    "termux-fund-01",
                    topic,
                    "What is Termux used for on Android?",
                    "Termux provides a Linux-like command-line environment on Android.",
                    Difficulty.FOUNDATION);

            add(result,
                    "termux-fund-02",
                    topic,
                    "What command displays the current directory?",
                    "pwd",
                    Difficulty.FOUNDATION);

            add(result,
                    "termux-fund-03",
                    topic,
                    "What command lists files and directories?",
                    "ls",
                    Difficulty.FOUNDATION);

            add(result,
                    "termux-fund-04",
                    topic,
                    "What command changes the current directory?",
                    "cd",
                    Difficulty.INTERMEDIATE);

            add(result,
                    "termux-fund-05",
                    topic,
                    "Why should commands be tested before using them in an important project?",
                    "To reduce errors and understand what the command will do.",
                    Difficulty.INTERMEDIATE);
            add(result,
                    "termux-fund-06",
                    topic,
                    "Which command shows files including hidden files?",
                    "ls -a",
                    Difficulty.FOUNDATION);

            add(result,
                    "termux-fund-07",
                    topic,
                    "Which command creates a new empty file?",
                    "touch",
                    Difficulty.FOUNDATION);

            add(result,
                    "termux-fund-08",
                    topic,
                    "Which command displays the contents of a text file?",
                    "cat",
                    Difficulty.FOUNDATION);

            add(result,
                    "termux-fund-09",
                    topic,
                    "Which command copies a file?",
                    "cp",
                    Difficulty.FOUNDATION);

            add(result,
                    "termux-fund-10",
                    topic,
                    "Which command moves or renames a file?",
                    "mv",
                    Difficulty.INTERMEDIATE);

            add(result,
                    "termux-fund-11",
                    topic,
                    "What does the chmod command control?",
                    "It changes file or directory permissions.",
                    Difficulty.INTERMEDIATE);

            add(result,
                    "termux-fund-12",
                    topic,
                    "What is the purpose of the HOME directory?",
                    "It is the user's home directory where personal files and configuration are stored.",
                    Difficulty.INTERMEDIATE);

            add(result,
                    "termux-fund-13",
                    topic,
                    "Why is package management important in Termux?",
                    "It installs, updates, and manages software packages and their dependencies.",
                    Difficulty.INTERMEDIATE);

            add(result,
                    "termux-fund-14",
                    topic,
                    "What is a shell script?",
                    "A text file containing commands that can be executed by a shell.",
                    Difficulty.ADVANCED);

            add(result,
                    "termux-fund-15",
                    topic,
                    "How can Termux be useful for Android development?",
                    "It can provide command-line tools for coding, Git, scripting, servers, and development workflows.",
                    Difficulty.ADVANCED);

        }

        /*
         * LINUX COMMANDS
         */
        else if (c.contains("linux") && t.contains("command")) {

            add(result,
                    "linux-cmd-01",
                    topic,
                    "Which command prints the current working directory?",
                    "pwd",
                    Difficulty.FOUNDATION);

            add(result,
                    "linux-cmd-02",
                    topic,
                    "Which command lists directory contents?",
                    "ls",
                    Difficulty.FOUNDATION);

            add(result,
                    "linux-cmd-03",
                    topic,
                    "Which command creates a directory?",
                    "mkdir",
                    Difficulty.FOUNDATION);

            add(result,
                    "linux-cmd-04",
                    topic,
                    "Which command removes a file?",
                    "rm",
                    Difficulty.INTERMEDIATE);

            add(result,
                    "linux-cmd-05",
                    topic,
                    "What is the purpose of the cd command?",
                    "It changes the current working directory.",
                    Difficulty.INTERMEDIATE);
        }

        /*
         * PROGRAMMING FUNDAMENTALS
         */
        else if (c.contains("programming fundamentals")) {

            add(result,
                    "prog-fund-01",
                    topic,
                    "What is a variable?",
                    "A named storage location used to hold a value.",
                    Difficulty.FOUNDATION);

            add(result,
                    "prog-fund-02",
                    topic,
                    "What is a conditional statement?",
                    "A statement that chooses behavior based on a condition.",
                    Difficulty.FOUNDATION);

            add(result,
                    "prog-fund-03",
                    topic,
                    "What is a loop?",
                    "A programming structure that repeats instructions.",
                    Difficulty.FOUNDATION);

            add(result,
                    "prog-fund-04",
                    topic,
                    "Why are functions useful?",
                    "They organize reusable instructions into named blocks.",
                    Difficulty.INTERMEDIATE);

            add(result,
                    "prog-fund-05",
                    topic,
                    "What is an algorithm?",
                    "A step-by-step procedure for solving a problem.",
                    Difficulty.INTERMEDIATE);
        }

        /*
         * OBJECT-ORIENTED PROGRAMMING
         */
        else if (c.contains("object-oriented")) {

            add(result,
                    "oop-01",
                    topic,
                    "What is a class?",
                    "A blueprint for creating objects.",
                    Difficulty.FOUNDATION);

            add(result,
                    "oop-02",
                    topic,
                    "What is an object?",
                    "An instance of a class.",
                    Difficulty.FOUNDATION);

            add(result,
                    "oop-03",
                    topic,
                    "What is encapsulation?",
                    "Bundling data and behavior while controlling access to internal state.",
                    Difficulty.INTERMEDIATE);

            add(result,
                    "oop-04",
                    topic,
                    "What is inheritance?",
                    "A mechanism where one class derives behavior or properties from another.",
                    Difficulty.INTERMEDIATE);

            add(result,
                    "oop-05",
                    topic,
                    "What is polymorphism?",
                    "The ability for the same interface to support different implementations.",
                    Difficulty.ADVANCED);
        }

        /*
         * DATABASES
         */
        else if (c.contains("database") || c.contains("sql")) {

            add(result,
                    "db-01",
                    topic,
                    "What is a database?",
                    "An organized collection of data.",
                    Difficulty.FOUNDATION);

            add(result,
                    "db-02",
                    topic,
                    "What does SQL stand for?",
                    "Structured Query Language.",
                    Difficulty.FOUNDATION);

            add(result,
                    "db-03",
                    topic,
                    "What is a table?",
                    "A structure that stores related data in rows and columns.",
                    Difficulty.FOUNDATION);

            add(result,
                    "db-04",
                    topic,
                    "What is a primary key?",
                    "A field or combination of fields that uniquely identifies a record.",
                    Difficulty.INTERMEDIATE);

            add(result,
                    "db-05",
                    topic,
                    "Why are database indexes used?",
                    "To speed up data retrieval operations.",
                    Difficulty.ADVANCED);
        }

        /*
         * GENERIC FALLBACK
         *
         * Every course/topic receives a bank instead of having
         * an empty assessment.
         */
        if (result.isEmpty()) {

            add(result,
                    "generic-01-" + safe(topic),
                    topic,
                    "Explain the main purpose of this topic.",
                    "Explain the main concept, its purpose, and where it is used.",
                    Difficulty.FOUNDATION);

            add(result,
                    "generic-02-" + safe(topic),
                    topic,
                    "Give one practical example related to this topic.",
                    "Provide a correct real-world or technical example.",
                    Difficulty.FOUNDATION);

            add(result,
                    "generic-03-" + safe(topic),
                    topic,
                    "Describe one important concept you learned.",
                    "Give a clear explanation using the terminology of the topic.",
                    Difficulty.INTERMEDIATE);

            add(result,
                    "generic-04-" + safe(topic),
                    topic,
                    "How could this topic be used in a practical project?",
                    "Describe a realistic project application.",
                    Difficulty.INTERMEDIATE);

            add(result,
                    "generic-05-" + safe(topic),
                    topic,
                    "Solve or explain a challenging problem related to this topic.",
                    "Give a logical, technically correct solution.",
                    Difficulty.ADVANCED);
        }

        return result;
    }

    /*
     * Select a question that has not been attempted recently.
     */
    public static Question nextQuestion(
            Context context,
            String course,
            String topic,
            Difficulty difficulty
    ) {

        List<Question> all = questions(course, topic);

        List<Question> available = new ArrayList<>();

        for (Question question : all) {

            if (question.getDifficulty() != difficulty) {
                continue;
            }

            if (!hasAttempted(
                    context,
                    course,
                    topic,
                    question.getId()
            )) {
                available.add(question);
            }
        }

        /*
         * If the current difficulty has been exhausted,
         * allow another difficulty rather than repeating immediately.
         */
        if (available.isEmpty()) {

            for (Question question : all) {

                if (!hasAttempted(
                        context,
                        course,
                        topic,
                        question.getId()
                )) {
                    available.add(question);
                }
            }
        }

        /*
         * If everything has been used, rotate the bank.
         * The question IDs remain in history, but the next attempt
         * can still select a different question from the bank.
         */
        if (available.isEmpty() && !all.isEmpty()) {

            int index = attemptedCount(
                    context,
                    course,
                    topic
            ) % all.size();

            Question selected = all.get(index);

            recordAttempt(
                    context,
                    course,
                    topic,
                    selected.getId()
            );

            return selected;
        }

        if (available.isEmpty()) {
            return null;
        }

        Collections.shuffle(available);

        Question selected = available.get(0);

        recordAttempt(
                context,
                course,
                topic,
                selected.getId()
        );

        return selected;
    }

    public static void recordAttempt(
            Context context,
            String course,
            String topic,
            String questionId
    ) {

        if (questionId == null || questionId.trim().isEmpty()) {
            return;
        }

        SharedPreferences preferences =
                context.getSharedPreferences(
                        PREFS,
                        Context.MODE_PRIVATE
                );

        String key = attemptedKey(course, topic);

        String stored =
                preferences.getString(key, "");

        List<String> history = new ArrayList<>();

        if (!stored.isEmpty()) {
            String[] values = stored.split("\\|");

            for (String value : values) {
                if (!value.trim().isEmpty()) {
                    history.add(value);
                }
            }
        }

        if (!history.contains(questionId)) {
            history.add(questionId);
        }

        StringBuilder builder = new StringBuilder();

        for (String value : history) {

            if (builder.length() > 0) {
                builder.append("|");
            }

            builder.append(value.replace("|", ""));
        }

        preferences.edit()
                .putString(key, builder.toString())
                .apply();
    }

    /**
     * CIA AI SMART ANSWER MARKING
     *
     * Accepts answers that are clearly equivalent to the expected answer.
     * This is intentionally conservative so short answers are not
     * incorrectly marked as correct.
     */
    /**
     * Creates a varied assessment question set.
     *
     * Questions are selected from the course/topic bank, shuffled,
     * and adapted to the requested difficulty. Previously attempted
     * questions are avoided whenever unused questions are available.
     */
    public static java.util.List<Question> createAssessment(
            Context context,
            String course,
            String topic,
            Difficulty difficulty,
            int questionCount
    ) {
        // Safety limit: assessments never request an excessive
        // number of questions.
        questionCount = Math.max(1, Math.min(questionCount, 10));

        java.util.List<Question> pool =
                new java.util.ArrayList<>(
                        questions(course, topic)
                );

        java.util.Collections.shuffle(
                pool,
                new java.util.Random(
                        System.currentTimeMillis()
                )
        );

        java.util.List<Question> selected =
                new java.util.ArrayList<>();

        String sessionKey =
                LAST_ASSESSMENT_PREFIX
                        + safe(course)
                        + "_"
                        + safe(topic);

        android.content.SharedPreferences prefs =
                context.getSharedPreferences(
                        PREFS,
                        Context.MODE_PRIVATE
                );

        java.util.Set<String> previous =
                prefs.getStringSet(
                        sessionKey,
                        new java.util.HashSet<String>()
                );

        java.util.Set<String> previousCopy =
                new java.util.HashSet<>(previous);

        // First pass:
        // preferred difficulty + unseen + not used in
        // the previous assessment.
        for (Question question : pool) {

            if (selected.size() >= questionCount) {
                break;
            }

            if (question.getDifficulty() == difficulty
                    && !previousCopy.contains(question.getId())
                    && !hasAttempted(
                            context,
                            course,
                            topic,
                            question.getId()
                    )) {

                selected.add(question);
            }
        }

        // Second pass:
        // any difficulty + unseen + not used previously.
        for (Question question : pool) {

            if (selected.size() >= questionCount) {
                break;
            }

            if (!selected.contains(question)
                    && !previousCopy.contains(question.getId())
                    && !hasAttempted(
                            context,
                            course,
                            topic,
                            question.getId()
                    )) {

                selected.add(question);
            }
        }

        // Third pass:
        // if unused questions remain but were in the previous
        // assessment, prefer other unused questions first.
        for (Question question : pool) {

            if (selected.size() >= questionCount) {
                break;
            }

            if (!selected.contains(question)
                    && !hasAttempted(
                            context,
                            course,
                            topic,
                            question.getId()
                    )) {

                selected.add(question);
            }
        }

        // Final fallback:
        // when the entire bank has been attempted, rotate
        // through the bank while still avoiding duplicates
        // inside this assessment.
        for (Question question : pool) {

            if (selected.size() >= questionCount) {
                break;
            }

            if (!selected.contains(question)) {
                selected.add(question);
            }
        }

        // Store exactly the questions used for this session.
        java.util.HashSet<String> current =
                new java.util.HashSet<>();

        for (Question question : selected) {
            current.add(question.getId());
        }

        prefs.edit()
                .putStringSet(
                        sessionKey,
                        current
                )
                .apply();

        return selected;
    }

    public static boolean isAnswerCorrect(
            String studentAnswer,
            String expectedAnswer
    ) {
        if (studentAnswer == null || expectedAnswer == null) {
            return false;
        }

        String student = normalizeAnswer(studentAnswer);
        String expected = normalizeAnswer(expectedAnswer);

        if (student.isEmpty() || expected.isEmpty()) {
            return false;
        }

        // Exact match.
        if (student.equals(expected)) {
            return true;
        }

        // Very short answers require an exact match.
        if (expected.length() < 5 || student.length() < 5) {
            return false;
        }

        // If the expected answer is a clear phrase,
        // accept a longer student explanation containing it.
        if (student.length() >= expected.length()
                && student.contains(expected)) {
            return true;
        }

        // Accept the expected answer when the student's answer
        // contains all meaningful words from it.
        String[] expectedWords = expected.split(" ");
        int meaningful = 0;
        int matched = 0;

        for (String word : expectedWords) {
            if (word.length() >= 4) {
                meaningful++;

                if (student.contains(word)) {
                    matched++;
                }
            }
        }

        if (meaningful >= 2) {
            return matched == meaningful;
        }

        return false;
    }

    private static String normalizeAnswer(String value) {
        return value
                .toLowerCase(java.util.Locale.ROOT)
                .replaceAll("[^a-z0-9 ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    public static boolean hasAttempted(
            Context context,
            String course,
            String topic,
            String questionId
    ) {

        if (questionId == null) {
            return false;
        }

        String stored =
                context.getSharedPreferences(
                        PREFS,
                        Context.MODE_PRIVATE
                )
                .getString(
                        attemptedKey(course, topic),
                        ""
                );

        if (stored.isEmpty()) {
            return false;
        }

        for (String value : stored.split("\\|")) {

            if (questionId.equals(value)) {
                return true;
            }
        }

        return false;
    }

    public static int attemptedCount(
            Context context,
            String course,
            String topic
    ) {

        String stored =
                context.getSharedPreferences(
                        PREFS,
                        Context.MODE_PRIVATE
                )
                .getString(
                        attemptedKey(course, topic),
                        ""
                );

        if (stored.isEmpty()) {
            return 0;
        }

        int count = 0;

        for (String value : stored.split("\\|")) {

            if (!value.trim().isEmpty()) {
                count++;
            }
        }

        return count;
    }

    public static void resetTopic(
            Context context,
            String course,
            String topic
    ) {

        context.getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
        )
        .edit()
        .remove(attemptedKey(course, topic))
        .apply();
    }

    private static void add(
            List<Question> result,
            String id,
            String topic,
            String question,
            String answer,
            Difficulty difficulty
    ) {

        result.add(
                new Question(
                        id,
                        topic,
                        question,
                        answer,
                        difficulty
                )
        );
    }

    private static String attemptedKey(
            String course,
            String topic
    ) {

        return ATTEMPTED_PREFIX
                + safe(course)
                + "_"
                + safe(topic);
    }

    private static String safe(String value) {

        if (value == null) {
            return "unknown";
        }

        return value
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "_");
    }
}
