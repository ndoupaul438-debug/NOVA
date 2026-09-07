package com.nova.app.learning;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NovaLearning {

    private final Context context;

    public NovaLearning(Context context) {
        this.context = context.getApplicationContext();
    }

    public List<String> defaultTools() {
        List<String> list = new ArrayList<>();

        list.add("AI Tutor");
        list.add("Courses");
        list.add("Notes");
        list.add("PDF Reader");
        list.add("Homework");
        list.add("Quizzes");
        list.add("Flashcards");
        list.add("Study Plans");
        list.add("Progress");

        return list;
    }

    public JSONObject createQuiz(String subject, int questions) {
        JSONObject quiz = new JSONObject();

        try {
            quiz.put("subject", subject);
            quiz.put("questions", Math.max(1, questions));
            quiz.put("status", "ready");
            quiz.put("offline", true);
        } catch (Exception ignored) {}

        return quiz;
    }
}
