package com.rama.mako_forever.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rama.mako_forever.R;
import com.rama.mako_forever.managers.FontManager;

public class About extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        View root = findViewById(R.id.root);
        FontManager.apply(root, FontManager.getJersey25(this));

//        val claimsLayout = findViewById<LinearLayout>(R.id.claims)
//        if (appClaimsArrayRes != 0) {
//            val claimsData = resources.getStringArray(appClaimsArrayRes)
//            claimsData.forEach { claim ->
//                    val tag = WdLabel(this)
//                tag.setText(claim)
//                tag.setIcon(R.drawable.px_octagon_check)
//                claimsLayout.addView(tag)
//            }
//        }

        Button btnBack = (Button) findViewById(R.id.go_back);
        btnBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { startActivity(new Intent(About.this, Settings.class)); } });
    }
}
