package com.nova.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildInterface();
    }

    private void buildInterface() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(247, 249, 252));

        TextView header = new TextView(this);
        header.setText("NOVA ✦");
        header.setTextSize(30);
        header.setTypeface(null, Typeface.BOLD);
        header.setPadding(24, 30, 24, 5);
        root.addView(header);

        TextView subtitle = new TextView(this);
        subtitle.setText("Learn • Build • Create");
        subtitle.setTextSize(16);
        subtitle.setTextColor(Color.GRAY);
        subtitle.setPadding(24, 0, 24, 25);
        root.addView(subtitle);

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(20, 10, 20, 10);

        root.addView(
            content,
            new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1
            )
        );

        LinearLayout navigation = new LinearLayout(this);
        navigation.setOrientation(LinearLayout.HORIZONTAL);
        navigation.setGravity(Gravity.CENTER);

        addNavButton(navigation, "AI", v -> showHome());
        addNavButton(navigation, "Learn", v ->
            showPage("NOVA Learn", "Your AI-powered learning space.")
        );
        addNavButton(navigation, "Build", v ->
            showPage("NOVA Build", "Learn coding and build projects.")
        );
        addNavButton(navigation, "Create", v ->
            showPage("NOVA Create", "Turn your ideas into creative projects.")
        );
        addNavButton(navigation, "Profile", v ->
            showPage("Your NOVA Profile", "Track your skills and achievements.")
        );

        root.addView(navigation);

        setContentView(root);

        showHome();
    }

    private void showHome() {

        content.removeAllViews();

        TextView title = new TextView(this);
        title.setText("What do you want to accomplish?");
        title.setTextSize(22);
        title.setTypeface(null, Typeface.BOLD);
        content.addView(title);

        EditText input = new EditText(this);
        input.setHint("Ask NOVA anything...");
        input.setPadding(20, 20, 20, 20);
        content.addView(input);

        Button ask = new Button(this);
        ask.setText("Ask NOVA");

        ask.setOnClickListener(v -> {
            String question = input.getText().toString();

            if (!question.trim().isEmpty()) {
                showPage(
                    "NOVA AI",
                    "You asked:\n\n" + question +
                    "\n\nAI engine coming next."
                );
            }
        });

        content.addView(ask);

        addFeature("Learn", "Study smarter with your AI tutor.");
        addFeature("Build", "Learn coding and create projects.");
        addFeature("Create", "Create posters, presentations and more.");
        addFeature("Challenges", "Compete, learn and showcase your skills.");
    }

    private void showPage(String titleText, String subtitleText) {

        content.removeAllViews();

        TextView title = new TextView(this);
        title.setText(titleText);
        title.setTextSize(28);
        title.setTypeface(null, Typeface.BOLD);
        content.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText(subtitleText);
        subtitle.setTextSize(17);
        subtitle.setTextColor(Color.GRAY);
        subtitle.setPadding(0, 12, 0, 30);
        content.addView(subtitle);

        Button back = new Button(this);
        back.setText("← Back to NOVA");
        back.setOnClickListener(v -> showHome());

        content.addView(back);
    }

    private void addFeature(String titleText, String description) {

        TextView card = new TextView(this);

        card.setText(titleText + "\n" + description);
        card.setTextSize(17);
        card.setPadding(18, 18, 18, 18);
        card.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );

        params.setMargins(0, 8, 0, 8);

        content.addView(card, params);
    }

    private void addNavButton(
        LinearLayout navigation,
        String text,
        android.view.View.OnClickListener listener
    ) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(11);
        button.setOnClickListener(listener);

        navigation.addView(
            button,
            new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
            )
        );
    }
}
