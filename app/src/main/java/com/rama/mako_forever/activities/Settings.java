package com.rama.mako_forever.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.rama.mako_forever.R;
import com.rama.mako_forever.managers.FontManager;
import android.content.Intent;

public class Settings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        View root = findViewById(R.id.root);
        FontManager.apply(root, FontManager.getJersey25(this));

//        Button btnAbout = (Button) findViewById(R.id.go_about);
//        btnAbout.setOnClickListener(
//            Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
//            startActivity(intent);
//        );
    }
}
