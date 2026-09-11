package com.rama.mako_forever.managers;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Loads the bundled Jersey 25 font (assets/fonts/jersey25_regular.otf) and
 * applies it to every TextView under a given view.
 *
 * {@code Typeface.createFromAsset} has existed since API 1, so this needs no
 * special handling for the API 9 floor.
 */
public class FontManager {

    private static Typeface jersey25;

    /** Loads (and caches) the Jersey 25 typeface. Falls back to the system default on failure. */
    public static Typeface getJersey25(Context context) {
        if (jersey25 == null) {
            try {
                jersey25 = Typeface.createFromAsset(
                        context.getApplicationContext().getAssets(),
                        "fonts/jersey25_regular.otf"
                );
            } catch (Exception e) {
                jersey25 = Typeface.DEFAULT;
            }
        }
        return jersey25;
    }

    /** Applies the typeface to {@code view} and, if it's a ViewGroup, all of its descendants. */
    public static void apply(View view, Typeface typeface) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                apply(group.getChildAt(i), typeface);
            }
        }
    }
}
