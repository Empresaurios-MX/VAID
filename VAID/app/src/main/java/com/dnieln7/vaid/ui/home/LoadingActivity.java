package com.dnieln7.vaid.ui.home;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dnieln7.vaid.R;

public class LoadingActivity extends AppCompatActivity {

    private ProgressBar loadingBar;
    private TextView loadingPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        loadingBar = findViewById(R.id.loading_bar);
        loadingPercentage = findViewById(R.id.loading_percentage);

        loadingBar.setScaleY(2);

        load();
    }

    public void load() {
        LoadingAnimation animation = new LoadingAnimation(
                this,
                loadingBar,
                loadingPercentage,
                2500
        );

        loadingBar.setAnimation(animation);
    }
}
