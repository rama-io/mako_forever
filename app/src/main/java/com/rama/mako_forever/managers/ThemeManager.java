package com.rama.mako_forever.managers;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.rama.mako_forever.R;
import com.rama.mako_forever.objects.PrefTheme;
import com.rama.mako_forever.objects.Themes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Recolours a view hierarchy at runtime to the selected built-in theme.
 *
 * Ported from bohio's ThemeManager with two changes for this app: only the
 * built-in palettes are supported (no custom themes), and no API 21+ tint
 * APIs are used - {@code setColorFilter} replaces {@code imageTintList} so
 * this still works on the API 9 floor.
 *
 * How it works: it builds a lookup of "every colour any known palette uses"
 * to "the equivalent colour in the target palette", then walks the tree
 * remapping text colours, image tints and solid backgrounds. Colours that
 * aren't part of any palette (e.g. transparent) are left untouched.
 */
public final class ThemeManager {

    private ThemeManager() {}

    public static Themes.Palette paletteFor(String themeId) {
        return Themes.byId(themeId);
    }

    public static Themes.Palette currentPalette(Context context) {
        return paletteFor(PrefsManager.getInstance(context).getTheme());
    }

    /** Applies the saved theme (and the app font) to {@code root} and all of its children. */
    public static void applyTheme(Context context, View root) {
        Themes.Palette palette = currentPalette(context);
        Map<Integer, Integer> colorMap = buildColorMap(context, palette);
        applyRecursively(root, palette, colorMap);
    }

    private static void applyRecursively(View view, Themes.Palette palette, Map<Integer, Integer> map) {
        applyToView(view, palette, map);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                applyRecursively(group.getChildAt(i), palette, map);
            }
        }
    }

    private static void applyToView(View view, Themes.Palette palette, Map<Integer, Integer> map) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            if (view instanceof RadioButton || view instanceof CheckBox) {
                // buttonTintList is API 21+, so the box/dot itself keeps the
                // platform colour here; only the label is themed.
                textView.setTextColor(palette.text);
            } else {
                Integer mapped = map.get(textView.getCurrentTextColor());
                if (mapped != null) textView.setTextColor(mapped);
            }
        }

        if (view instanceof ImageView) {
            // No imageTintList on API 9 - a SRC_IN colour filter is equivalent
            // for the flat single-colour vector icons this app uses.
            ((ImageView) view).setColorFilter(palette.text, PorterDuff.Mode.SRC_IN);
        }

        Drawable background = view.getBackground();
        if (background instanceof ColorDrawable) {
            Integer mapped = map.get(((ColorDrawable) background).getColor());
            if (mapped != null) view.setBackgroundColor(mapped);
        }
    }

    /**
     * Maps every colour used by any built-in palette (plus the compiled-in
     * colors.xml defaults) onto the corresponding slot of {@code target}.
     */
    private static Map<Integer, Integer> buildColorMap(Context context, Themes.Palette target) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        List<Themes.Palette> all = Themes.all();

        for (int i = 0; i < all.size(); i++) {
            Themes.Palette p = all.get(i);
            map.put(p.text, target.text);
            map.put(p.base, target.base);
            map.put(p.surface_0, target.surface_0);
            map.put(p.surface_1, target.surface_1);
            map.put(p.subtle, target.subtle);
            map.put(p.disabled, target.disabled);
            map.put(p.border, target.border);
            map.put(p.accent, target.accent);
            map.put(p.accent_2, target.accent_2);
            map.put(p.accent_3, target.accent_3);
            map.put(p.accent_4, target.accent_4);
            map.put(p.accent_5, target.accent_5);
            map.put(p.success, target.success);
            map.put(p.warning, target.warning);
            map.put(p.error, target.error);
            map.put(p.info, target.info);
            map.put(p.link, target.link);
        }

        // The XML defaults resolve to the RAMA palette, but map them explicitly
        // so a colours.xml edit can't silently break theming.
        android.content.res.Resources res = context.getResources();
        map.put(res.getColor(R.color.text), target.text);
        map.put(res.getColor(R.color.base), target.base);
        map.put(res.getColor(R.color.surface_0), target.surface_0);
        map.put(res.getColor(R.color.surface_1), target.surface_1);
        map.put(res.getColor(R.color.subtle), target.subtle);
        map.put(res.getColor(R.color.disabled), target.disabled);
        map.put(res.getColor(R.color.border), target.border);
        map.put(res.getColor(R.color.accent), target.accent);
        map.put(res.getColor(R.color.accent_2), target.accent_2);
        map.put(res.getColor(R.color.accent_3), target.accent_3);
        map.put(res.getColor(R.color.accent_4), target.accent_4);
        map.put(res.getColor(R.color.accent_5), target.accent_5);
        map.put(res.getColor(R.color.success), target.success);
        map.put(res.getColor(R.color.warning), target.warning);
        map.put(res.getColor(R.color.error), target.error);
        map.put(res.getColor(R.color.info), target.info);
        map.put(res.getColor(R.color.link), target.link);

        return map;
    }
}
