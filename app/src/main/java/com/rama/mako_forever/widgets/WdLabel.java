package com.rama.mako_forever.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rama.mako_forever.R;

/**
 * A small icon + text row (e.g. "No Ads", "No Trackers"), ported from
 * bohio's Kotlin {@code WdLabel} for the About screen's claims list.
 *
 * Simplified from the original: since every instance here is built in code
 * (see About.java) rather than inflated from a layout, this drops the
 * custom-XML-attribute ("text"/"icon" attrs) shortcut - {@link #setText}
 * and {@link #setIcon} cover the same job.
 */
public class WdLabel extends LinearLayout {

    private final ImageView iconImage;
    private final TextView iconText;

    public WdLabel(Context context) {
        this(context, null);
    }

    public WdLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.wd_label, this, true);
        iconImage = (ImageView) findViewById(R.id.icon_image);
        iconText = (TextView) findViewById(R.id.icon_text);
    }

    public void setText(String text) {
        iconText.setText(text);
    }

    public void setIcon(int drawableResId) {
        iconImage.setImageResource(drawableResId);
    }

    public TextView getTextView() {
        return iconText;
    }
}
