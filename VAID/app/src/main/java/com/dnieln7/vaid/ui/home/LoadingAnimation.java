package com.dnieln7.vaid.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

public class LoadingAnimation extends Animation {
    private Context context;
    private ProgressBar loadingBar;
    private TextView loadingPercentage;
    private static final int MAX = 100;

    public LoadingAnimation(Context context, ProgressBar loadingBar, TextView loadingPercentage, long duration) {
        this.context = context;
        this.loadingBar = loadingBar;
        this.loadingPercentage = loadingPercentage;
        setDuration(duration);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        float percentage = MAX * interpolatedTime;

        loadingBar.setProgress((int) percentage);
        loadingPercentage.setText(String.format(Locale.forLanguageTag("es"), "%d %%", (int) percentage));

        if (percentage == MAX) {
            context.startActivity(new Intent(context, HomeActivity.class));
        }
    }
}
