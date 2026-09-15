package com.rama.mako_forever.widgets;

import android.content.Context;
import android.content.res.TypedArray;
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
        // getAttributeResourceValue() only works for "@string/..." references;
        // obtainStyledAttributes()+getText() resolves literal inline strings too.
        TypedArray ta = context.obtainStyledAttributes(attrs, new int[] { android.R.attr.text });
        CharSequence text = ta.getText(0);
        if (text != null) {
            checkBox.setText(text);
        }
        ta.recycle();
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