package com.nova.app.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class NovaCommandCenter {

    private NovaCommandCenter() {}

    private static final int BLUE = Color.rgb(37, 99, 235);
    private static final int PURPLE = Color.rgb(124, 58, 237);
    private static final int TEXT = Color.rgb(15, 23, 42);
    private static final int MUTED = Color.rgb(100, 116, 139);
    private static final int BG = Color.rgb(245, 247, 251);

    public interface Navigator {
        void open(String destination);
    }

    public static LinearLayout create(Context context, Navigator navigator) {

        LinearLayout root = NovaUi.vertical(context);
        root.setBackgroundColor(BG);
        NovaUi.padding(root, context, 18, 18, 18, 28);

        TextView brand = NovaUi.text(
                context,
                "NOVA",
                30,
                BLUE
        );
        brand.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        root.addView(
                brand,
                new LinearLayout.LayoutParams(
                        -1,
                        NovaUi.dp(context, 42)
                )
        );

        TextView subtitle = NovaUi.text(
                context,
                "Your AI creation command center",
                15,
                MUTED
        );

        root.addView(
                subtitle,
                new LinearLayout.LayoutParams(
                        -1,
                        NovaUi.dp(context, 30)
                )
        );

        root.addView(space(context, 14));

        root.addView(
                heroCard(
                        context,
                        "M3GAN",
                        "AI CREATION ENGINE",
                        "Ask, plan, code, design and build with M3GAN.",
                        PURPLE
                )
        );

        root.addView(space(context, 18));

        sectionTitle(context, root, "Quick Create");

        root.addView(
                actionRow(
                        context,
                        "📱  App Studio",
                        "Build Android applications",
                        BLUE,
                        navigator
                )
        );

        root.addView(
                actionRow(
                        context,
                        "🎮  Game Studio",
                        "Create 2D and 3D games",
                        PURPLE,
                        navigator
                )
        );

        root.addView(
                actionRow(
                        context,
                        "🌐  Website Studio",
                        "Design modern websites and web apps",
                        Color.rgb(5, 150, 105),
                        navigator
                )
        );

        root.addView(
                actionRow(
                        context,
                        "🤖  AI Studio",
                        "Create AI-powered projects",
                        Color.rgb(220, 38, 38),
                        navigator
                )
        );

        root.addView(space(context, 18));

        sectionTitle(context, root, "Workspace");

        LinearLayout workspace = NovaUi.horizontal(context);

        workspace.addView(
                miniCard(context, "📁", "Projects", "Manage projects", BLUE),
                weight()
        );

        workspace.addView(spaceHorizontal(context, 10));

        workspace.addView(
                miniCard(context, "🔨", "Builds", "APK & exports", PURPLE),
                weight()
        );

        root.addView(workspace);

        root.addView(space(context, 10));

        LinearLayout secondRow = NovaUi.horizontal(context);

        secondRow.addView(
                miniCard(context, "🧠", "M3GAN", "AI assistant", Color.rgb(14, 116, 144)),
                weight()
        );

        secondRow.addView(spaceHorizontal(context, 10));

        secondRow.addView(
                miniCard(context, "⚙️", "Settings", "Configure NOVA", MUTED),
                weight()
        );

        root.addView(secondRow);

        root.addView(space(context, 20));

        TextView status = NovaUi.text(
                context,
                "●  NOVA CORE     READY",
                13,
                Color.rgb(5, 150, 105)
        );
        status.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        root.addView(
                status,
                new LinearLayout.LayoutParams(
                        -1,
                        NovaUi.dp(context, 35)
                )
        );

        return root;
    }

    private static void sectionTitle(
            Context context,
            LinearLayout root,
            String title
    ) {
        TextView view = NovaUi.text(
                context,
                title,
                18,
                TEXT
        );

        view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        root.addView(
                view,
                new LinearLayout.LayoutParams(
                        -1,
                        NovaUi.dp(context, 40)
                )
        );
    }

    private static TextView heroCard(
            Context context,
            String title,
            String label,
            String description,
            int accent
    ) {
        TextView card = NovaUi.text(
                context,
                title + "\n" + label + "\n\n" + description,
                16,
                Color.WHITE
        );

        card.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        NovaUi.padding(contextView(card), context, 20, 18, 20, 18);

        GradientDrawable background = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[] {
                        accent,
                        Color.rgb(59, 130, 246)
                }
        );

        background.setCornerRadius(
                NovaUi.dp(context, 22)
        );

        card.setBackground(background);

        return card;
    }

    private static TextView actionRow(
            Context context,
            String title,
            String description,
            int accent,
            Navigator navigator
    ) {
        TextView card = NovaUi.text(
                context,
                title + "\n" + description,
                16,
                TEXT
        );

        NovaUi.padding(card, context, 18, 14, 18, 14);

        GradientDrawable background =
                NovaUi.roundedBackground(
                        Color.WHITE,
                        NovaUi.dp(context, 18)
                );

        background.setStroke(
                NovaUi.dp(context, 1),
                Color.rgb(226, 232, 240)
        );

        card.setBackground(background);

        card.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        card.setText(
                                title + "\n\nNOVA is preparing this workspace..."
                        );
                    }
                }
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        NovaUi.dp(context, 72)
                );

        params.setMargins(
                0,
                0,
                0,
                NovaUi.dp(context, 10)
        );

        return card;
    }

    private static TextView miniCard(
            Context context,
            String icon,
            String title,
            String description,
            int accent
    ) {
        TextView card = NovaUi.text(
                context,
                icon + "  " + title + "\n" + description,
                14,
                TEXT
        );

        NovaUi.padding(card, context, 14, 14, 14, 14);

        GradientDrawable background =
                NovaUi.roundedBackground(
                        Color.WHITE,
                        NovaUi.dp(context, 18)
                );

        background.setStroke(
                NovaUi.dp(context, 1),
                Color.rgb(226, 232, 240)
        );

        card.setBackground(background);

        card.setOnClickListener(
                view -> card.setText(
                        icon + "  " + title +
                        "\n\nWorkspace selected"
                )
        );

        return card;
    }

    private static LinearLayout.LayoutParams weight() {
        return new LinearLayout.LayoutParams(
                0,
                92,
                1
        );
    }

    private static View space(Context context, int dp) {
        View view = new View(context);
        view.setLayoutParams(
                new LinearLayout.LayoutParams(
                        1,
                        NovaUi.dp(context, dp)
                )
        );
        return view;
    }

    private static View spaceHorizontal(Context context, int dp) {
        View view = new View(context);
        view.setLayoutParams(
                new LinearLayout.LayoutParams(
                        NovaUi.dp(context, dp),
                        1
                )
        );
        return view;
    }

    private static View contextView(View view) {
        return view;
    }
}
