package com.rama.mako_forever.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rama.mako_forever.R;
import com.rama.mako_forever.managers.FontManager;
import com.rama.mako_forever.managers.ThemeManager;

public class About extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        View root = findViewById(R.id.root);
        FontManager.apply(root, FontManager.getJersey25(this));
        ThemeManager.applyTheme(this, root);

        TextView appName = findViewById(R.id.name_version);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);

            appName.setText(getString(R.string.app_name) + " " + info.versionCode);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Button btnBack = findViewById(R.id.go_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(About.this, Settings.class));
            }
        });
    }
}
