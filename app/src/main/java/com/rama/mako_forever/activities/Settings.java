package com.rama.mako_forever.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.rama.mako_forever.R;
import com.rama.mako_forever.managers.FontManager;

public class Settings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        View root = findViewById(R.id.root);
        FontManager.apply(root, FontManager.getJersey25(this));

        Button btnAbout = (Button) findViewById(R.id.go_about);
        btnAbout.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { startActivity(new Intent(Settings.this, About.class)); } });

        Button btnBack = (Button) findViewById(R.id.go_back);
        btnBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { startActivity(new Intent(Settings.this, Main.class)); } });
    }
}
