package com.rama.mako_forever.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import com.rama.mako_forever.R;

public class WdCheckbox extends FrameLayout {

    private final CheckBox checkBox;

    public WdCheckbox(Context context) {
        this(context, null);
    }

    public WdCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(
                R.layout.wd_checkbox,
                this,
                true
        );

        checkBox = (CheckBox) findViewById(R.id.checkbox);

        checkBox.setSaveEnabled(false);

        if (attrs != null) {
            setAttrs(context, attrs);
        }
    }

    private void setAttrs(Context context, AttributeSet attrs) {
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String name = attrs.getAttributeName(i);

            if ("text".equals(name)) {
                int resId = attrs.getAttributeResourceValue(i, 0);

                if (resId != 0) {
                    checkBox.setText(context.getString(resId));
                }
            }
        }
    }

    public void setText(String text) {
        checkBox.setText(text);
    }

    public void setChecked(boolean checked) {
        checkBox.setChecked(checked);
    }

    public void setOnCheckedChangeListener(
            final OnCheckedChangeListener listener) {

        checkBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(
                            CompoundButton buttonView,
                            boolean isChecked) {

                        if (listener != null) {
                            listener.onCheckedChanged(isChecked);
                        }
                    }
                }
        );
    }

    public boolean isChecked() {
        return checkBox.isChecked();
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(boolean isChecked);
    }
}