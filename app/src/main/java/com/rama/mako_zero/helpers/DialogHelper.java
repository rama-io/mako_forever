package com.rama.mako_zero.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;

import com.rama.mako_zero.R;
import com.rama.mako_zero.managers.FontManager;
import com.rama.mako_zero.managers.ThemeManager;

public final class DialogHelper {

    private DialogHelper() {
        // Utility class
    }

    public static Dialog show(Activity activity, int layoutResId, DialogContent content) {
        View view = LayoutInflater.from(activity).inflate(layoutResId, null);

        FontManager.apply(view, FontManager.getJersey25(activity));
        ThemeManager.applyTheme(activity, view);

        Dialog dialog = new Dialog(activity, R.style.AppDialog);
        dialog.setContentView(view);

        if (content != null) {
            content.setup(view, dialog);
        }

        dialog.show();
        return dialog;
    }

    public interface DialogContent {
        void setup(View view, Dialog dialog);
    }
}