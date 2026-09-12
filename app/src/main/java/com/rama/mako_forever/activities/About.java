package com.rama.mako_forever.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rama.mako_forever.R;
import com.rama.mako_forever.managers.FontManager;
import com.rama.mako_forever.widgets.WdLabel;

public class About extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        View root = findViewById(R.id.root);
        FontManager.apply(root, FontManager.getJersey25(this));

        LinearLayout claimsLayout = (LinearLayout) findViewById(R.id.claims);
        String[] claims = getResources().getStringArray(R.array.app_claims);
        for (int i = 0; i < claims.length; i++) {
            WdLabel tag = new WdLabel(this);
//            View separator = new View()
            tag.setText(claims[i]);
            tag.setIcon(R.drawable.px_octagon_check);
            FontManager.apply(tag, FontManager.getJersey25(this));
            claimsLayout.addView(tag);
        }

        Button btnBack = (Button) findViewById(R.id.go_back);
        btnBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { startActivity(new Intent(About.this, Settings.class)); } });
    }
}
