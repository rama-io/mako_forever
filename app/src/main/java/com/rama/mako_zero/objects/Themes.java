package com.rama.mako_zero.objects;

import java.util.ArrayList;
import java.util.List;

public final class Themes {
    public static final class Palette {
        public final String id;
        public final String label;
        public final int text;
        public final int base;
        public final int surface_0;
        public final int surface_1;
        public final int subtle;
        public final int disabled;
        public final int border;
        public final int accent;
        public final int success;
        public final int warning;
        public final int error;
        public final int info;
        public final int link;

        Palette(String id, String label, int text, int base, int surface_0, int surface_1, int subtle, int disabled, int border, int accent, int success, int warning, int error, int info, int link) {
            this.id = id;
            this.label = label;
            this.text = text;
            this.base = base;
            this.surface_0 = surface_0;
            this.surface_1 = surface_1;
            this.subtle = subtle;
            this.disabled = disabled;
            this.border = border;
            this.accent = accent;
            this.success = success;
            this.warning = warning;
            this.error = error;
            this.info = info;
            this.link = link;
        }
    }

    private Themes() {
    }

    public static final Palette CATPPUCCIN_MOCHA_BLUE = new Palette(PrefTheme.CATPPUCCIN_MOCHA_BLUE, "Blue", 0xFFCDD6F4, // text
            0xFF1E1E2E, // base
            0xFF45475A, // surface_0
            0xFF585B70, // surface_1
            0xFF587D52, // subtle
            0xFF585B70, // disabled
            0xFF585B70, // border
            0xFF89b4fa, // accent
            0xFFA6E3A1, // success
            0xFFFFD700, // warning
            0xFFF38BA8, // error
            0xFF89DCEB, // info
            0xFF89DCEB // link
    );

    public static final Palette CATPPUCCIN_MOCHA_MAUVE = new Palette(PrefTheme.CATPPUCCIN_MOCHA_MAUVE, "Mauve", 0xFFCDD6F4, // text
            0xFF1E1E2E, // base
            0xFF45475A, // surface_0
            0xFF585B70, // surface_1
            0xFF587D52, // subtle
            0xFF585B70, // disabled
            0xFF585B70, // border
            0xFFcba6f7, // accent
            0xFFA6E3A1, // success
            0xFFFFD700, // warning
            0xFFF38BA8, // error
            0xFF89DCEB, // info
            0xFF89DCEB // link
    );

    public static final Palette CATPPUCCIN_MOCHA_PEACH = new Palette(PrefTheme.CATPPUCCIN_MOCHA_PEACH, "Peach", 0xFFCDD6F4, // text
            0xFF1E1E2E, // base
            0xFF45475A, // surface_0
            0xFF585B70, // surface_1
            0xFF587D52, // subtle
            0xFF585B70, // disabled
            0xFF585B70, // border
            0xFFfab387, // accent
            0xFFA6E3A1, // success
            0xFFFFD700, // warning
            0xFFF38BA8, // error
            0xFF89DCEB, // info
            0xFF89DCEB // link
    );

    public static final Palette CATPPUCCIN_MOCHA_RED = new Palette(PrefTheme.CATPPUCCIN_MOCHA_RED, "Red", 0xFFCDD6F4, // text
            0xFF1E1E2E, // base
            0xFF45475A, // surface_0
            0xFF585B70, // surface_1
            0xFF587D52, // subtle
            0xFF585B70, // disabled
            0xFF585B70, // border
            0xFFf38ba8, // accent
            0xFFA6E3A1, // success
            0xFFFFD700, // warning
            0xFFF38BA8, // error
            0xFF89DCEB, // info
            0xFF89DCEB // link
    );

    public static final Palette CATPPUCCIN_MOCHA_YELLOW = new Palette(PrefTheme.CATPPUCCIN_MOCHA_YELLOW, "Yellow", 0xFFCDD6F4, // text
            0xFF1E1E2E, // base
            0xFF45475A, // surface_0
            0xFF585B70, // surface_1
            0xFF587D52, // subtle
            0xFF585B70, // disabled
            0xFF585B70, // border
            0xFFf9e2af, // accent
            0xFFA6E3A1, // success
            0xFFFFD700, // warning
            0xFFF38BA8, // error
            0xFF89DCEB, // info
            0xFF89DCEB // link
    );

    public static final Palette CATPPUCCIN_MOCHA_GREEN = new Palette(PrefTheme.CATPPUCCIN_MOCHA_GREEN, "Green", 0xFFCDD6F4, // text
            0xFF1E1E2E, // base
            0xFF45475A, // surface_0
            0xFF585B70, // surface_1
            0xFF587D52, // subtle
            0xFF585B70, // disabled
            0xFF585B70, // border
            0xFFa6e3a1, // accent
            0xFFA6E3A1, // success
            0xFFFFD700, // warning
            0xFFF38BA8, // error
            0xFF89DCEB, // info
            0xFF89DCEB // link
    );

    public static final Palette CATPPUCCIN_LATTE = new Palette(PrefTheme.CATPPUCCIN_LATTE, "Latte", 0xFF4C4F69, // text
            0xFFEFF1F5, // base
            0xFFBCC0CC, // surface_0
            0xFFBCC0CD, // surface_1
            0xFF8CCB9A, // subtle
            0xFFBCC0CD, // disabled
            0xFFBCC0CD, // border
            0xFF1E66F5, // accent
            0xFF40A02B, // success
            0xFFFE640B, // warning
            0xFFD20F39, // error
            0xFF04A5E5, // info
            0xFF04A5E5 // link
    );

    public static final Palette MONO_DARK = new Palette(PrefTheme.MONO_DARK, "Mono Dark", 0xFFFFFFF9, // text
            0xFF040100, // base
            0xFF040101, // surface_0
            0xFF040200, // surface_1
            0xFFF9FFFF, // subtle
            0xFF040200, // disabled
            0xFF040200, // border
            0xFFFFF9F9, // accent
            0xFFFBFFFF, // success
            0xFFFBFEFF, // warning
            0xFFFBFFFB, // error
            0xFFFFFAFA, // info
            0xFFFFFAFA // link
    );

    public static final Palette MONO_LIGHT = new Palette(PrefTheme.MONO_LIGHT, "Mono Light", 0xFF000040, // text
            0xFFFFFFFF, // base
            0xFFFFFEFF, // surface_0
            0xFFFEFFFF, // surface_1
            0xFF040000, // subtle
            0xFFFEFFFF, // disabled
            0xFFFEFFFF, // border
            0xFF020000, // accent
            0xFF000001, // success
            0xFF010000, // warning
            0xFF020002, // error
            0xFF000303, // info
            0xFF000303 // link
    );

    public static List<Palette> all() {
        List<Palette> list = new ArrayList<Palette>();
        list.add(CATPPUCCIN_MOCHA_MAUVE);
        list.add(CATPPUCCIN_MOCHA_BLUE);
        list.add(CATPPUCCIN_MOCHA_YELLOW);
        list.add(CATPPUCCIN_MOCHA_PEACH);
        list.add(CATPPUCCIN_MOCHA_GREEN);
        list.add(CATPPUCCIN_MOCHA_RED);
        list.add(CATPPUCCIN_LATTE);
        list.add(MONO_DARK);
        list.add(MONO_LIGHT);
        return list;
    }

    public static Palette byId(String id) {
        List<Palette> list = all();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id.equals(id)) return list.get(i);
        }
        return CATPPUCCIN_MOCHA_YELLOW;
    }
}
