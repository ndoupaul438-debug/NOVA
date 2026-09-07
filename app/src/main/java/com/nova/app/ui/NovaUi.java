package com.nova.app.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class NovaUi {

    private NovaUi() {}

    public static int dp(Context context, int value) {
        return (int) (value * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static TextView text(
            Context context,
            String value,
            float size,
            int color
    ) {
        TextView view = new TextView(context);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setGravity(Gravity.CENTER_VERTICAL);
        return view;
    }

    public static LinearLayout vertical(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        return layout;
    }

    public static LinearLayout horizontal(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        return layout;
    }

    public static GradientDrawable roundedBackground(
            int color,
            int radius
    ) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    public static void padding(
            View view,
            Context context,
            int left,
            int top,
            int right,
            int bottom
    ) {
        view.setPadding(
                dp(context, left),
                dp(context, top),
                dp(context, right),
                dp(context, bottom)
        );
    }

    public static TextView title(
            Context context,
            String value
    ) {
        TextView view = text(
                context,
                value,
                24,
                Color.rgb(15, 23, 42)
        );

        view.setTypeface(
                android.graphics.Typeface.DEFAULT,
                android.graphics.Typeface.BOLD
        );

        return view;
    }
}
