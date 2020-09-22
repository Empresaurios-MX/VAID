package com.dnieln7.vaid.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dnieln7.vaid.R;
import com.dnieln7.vaid.ui.about.AboutActivity;
import com.dnieln7.vaid.ui.aire.AireActivity;
import com.dnieln7.vaid.ui.cerraduras.CerradurasActivity;
import com.dnieln7.vaid.ui.luces.LucesActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.main_appbar).findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
    }

    public void luces(View view) {
        startActivity(new Intent(this, LucesActivity.class));
    }

    public void cerradura(View view) {
        startActivity(new Intent(this, CerradurasActivity.class));
    }

    public void aire(View view) {
        startActivity(new Intent(this, AireActivity.class));
    }

    public void about(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }
}
