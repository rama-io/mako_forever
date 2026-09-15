package com.rama.mako_forever.objects;

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
        public final int accent_2;
        public final int accent_3;
        public final int accent_4;
        public final int accent_5;
        public final int success;
        public final int warning;
        public final int error;
        public final int info;
        public final int link;

        Palette(String id, String label, int text, int base, int surface_0, int surface_1, int subtle, int disabled, int border, int accent, int accent_2, int accent_3, int accent_4, int accent_5, int success, int warning, int error, int info, int link) {
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
            this.accent_2 = accent_2;
            this.accent_3 = accent_3;
            this.accent_4 = accent_4;
            this.accent_5 = accent_5;
            this.success = success;
            this.warning = warning;
            this.error = error;
            this.info = info;
            this.link = link;
        }
    }

    private Themes() {}

    public static final Palette RAMA = new Palette(
            PrefTheme.RAMA, "Rama",
            0xFFCBDECD, // text
            0xFF0E190E, // base
            0xFF2D3B24, // surface_0
            0xFF3A4D2E, // surface_1
            0xFF4D7A4E, // subtle
            0xFF3A4D2E, // disabled
            0xFF3A4D2E, // border
            0xFF45995A, // accent
            0xFF7CCF8E, // accent_2
            0xFFDCD07C, // accent_3
            0xFFABD68D, // accent_4
            0xFF8DE285, // accent_5
            0xFFABD68D, // success
            0xFFDCD07C, // warning
            0xFFDC6364, // error
            0xFF7CCF8E, // info
            0xFF7CCF8E // link
    );

    public static final Palette TEYIN = new Palette(
            PrefTheme.TEYIN, "Teyin",
            0xFFEAE6DF, // text
            0xFF0E181A, // base
            0xFF1E383D, // surface_0
            0xFF28484D, // surface_1
            0xFF3F7268, // subtle
            0xFF28484D, // disabled
            0xFF28484D, // border
            0xFF3A9DA0, // accent
            0xFF6A9FCF, // accent_2
            0xFFC77D4D, // accent_3
            0xFF4DA8AC, // accent_4
            0xFF7A9495, // accent_5
            0xFF4DA8AC, // success
            0xFFC77D4D, // warning
            0xFFB83A2D, // error
            0xFF6A9FCF, // info
            0xFF6A9FCF // link
    );

    public static final Palette MAKO = new Palette(
            PrefTheme.MAKO, "Mako",
            0xFFCCCCCC, // text
            0xFF141417, // base
            0xFF24313B, // surface_0
            0xFF2E324D, // surface_1
            0xFF4A6E3A, // subtle
            0xFF2E324D, // disabled
            0xFF2E324D, // border
            0xFF459984, // accent
            0xFF71ACC7, // accent_2
            0xFFDCD07C, // accent_3
            0xFFABD68E, // accent_4
            0xFF878787, // accent_5
            0xFFABD68E, // success
            0xFFDCD07C, // warning
            0xFFDC6364, // error
            0xFF71ACC7, // info
            0xFF71ACC7 // link
    );

    public static final Palette MELANGE = new Palette(
            PrefTheme.MELANGE, "Melange Dark",
            0xFFECE1D7, // text
            0xFF161413, // base
            0xFF322B24, // surface_0
            0xFF4D463E, // surface_1
            0xFF5A6650, // subtle
            0xFF4D463E, // disabled
            0xFF4D463E, // border
            0xFFDAB148, // accent
            0xFF8CBBA3, // accent_2
            0xFFE49B5D, // accent_3
            0xFF78997A, // accent_4
            0xFFEBC06B, // accent_5
            0xFF78997A, // success
            0xFFE49B5D, // warning
            0xFFB65C60, // error
            0xFF8CBBA3, // info
            0xFF8CBBA3 // link
    );

    public static final Palette CATPPUCCIN_MOCHA = new Palette(
            PrefTheme.CATPPUCCIN_MOCHA, "Catppuccin Mocha",
            0xFFCDD6F4, // text
            0xFF1E1E2E, // base
            0xFF45475A, // surface_0
            0xFF585B70, // surface_1
            0xFF587D52, // subtle
            0xFF585B70, // disabled
            0xFF585B70, // border
            0xFF89B4FA, // accent
            0xFF89DCEB, // accent_2
            0xFFFFD700, // accent_3
            0xFFA6E3A1, // accent_4
            0xFFB4BEFE, // accent_5
            0xFFA6E3A1, // success
            0xFFFFD700, // warning
            0xFFF38BA8, // error
            0xFF89DCEB, // info
            0xFF89DCEB // link
    );

    public static final Palette CATPPUCCIN_LATTE = new Palette(
            PrefTheme.CATPPUCCIN_LATTE, "Catppuccin Latte",
            0xFF4C4F69, // text
            0xFFEFF1F5, // base
            0xFFBCC0CC, // surface_0
            0xFFBCC0CD, // surface_1
            0xFF8CCB9A, // subtle
            0xFFBCC0CD, // disabled
            0xFFBCC0CD, // border
            0xFF1E66F5, // accent
            0xFF04A5E5, // accent_2
            0xFFFE640B, // accent_3
            0xFF40A02B, // accent_4
            0xFF7287FD, // accent_5
            0xFF40A02B, // success
            0xFFFE640B, // warning
            0xFFD20F39, // error
            0xFF04A5E5, // info
            0xFF04A5E5 // link
    );

    public static final Palette DRACULA = new Palette(
            PrefTheme.DRACULA, "Dracula",
            0xFFF8F8F2, // text
            0xFF282A36, // base
            0xFF424450, // surface_0
            0xFF4F5163, // surface_1
            0xFF3E5F4A, // subtle
            0xFF4F5163, // disabled
            0xFF4F5163, // border
            0xFFBD93F8, // accent
            0xFF8BE9FB, // accent_2
            0xFFFFB86C, // accent_3
            0xFF50FA7B, // accent_4
            0xFFBD93FA, // accent_5
            0xFF50FA7B, // success
            0xFFFFB86C, // warning
            0xFFFF79C6, // error
            0xFF8BE9FB, // info
            0xFF8BE9FB // link
    );

    public static final Palette TOKYO_NIGHT = new Palette(
            PrefTheme.TOKYO_NIGHT, "Tokyo Night",
            0xFFC0CAF5, // text
            0xFF1A1B26, // base
            0xFF292E42, // surface_0
            0xFF2E324D, // surface_1
            0xFF3B5A4F, // subtle
            0xFF2E324D, // disabled
            0xFF2E324D, // border
            0xFF7AA2F7, // accent
            0xFF73DACA, // accent_2
            0xFFFF9E64, // accent_3
            0xFF9ECE6A, // accent_4
            0xFF7AA2F6, // accent_5
            0xFF9ECE6A, // success
            0xFFFF9E64, // warning
            0xFFF7768E, // error
            0xFF73DACA, // info
            0xFF73DACA // link
    );

    public static final Palette MONO_DARK = new Palette(
            PrefTheme.MONO_DARK, "Mono Dark",
            0xFFFFFFF9, // text
            0xFF040100, // base
            0xFF040101, // surface_0
            0xFF040200, // surface_1
            0xFFF9FFFF, // subtle
            0xFF040200, // disabled
            0xFF040200, // border
            0xFFFFF9F9, // accent
            0xFFFFFAFA, // accent_2
            0xFFFBFEFF, // accent_3
            0xFFFBFFFF, // accent_4
            0xFFFAFAFA, // accent_5
            0xFFFBFFFF, // success
            0xFFFBFEFF, // warning
            0xFFFBFFFB, // error
            0xFFFFFAFA, // info
            0xFFFFFAFA // link
    );

    public static final Palette MONO_LIGHT = new Palette(
            PrefTheme.MONO_LIGHT, "Mono Light",
            0xFF000040, // text
            0xFFFFFFFF, // base
            0xFFFFFEFF, // surface_0
            0xFFFEFFFF, // surface_1
            0xFF040000, // subtle
            0xFFFEFFFF, // disabled
            0xFFFEFFFF, // border
            0xFF020000, // accent
            0xFF000303, // accent_2
            0xFF010000, // accent_3
            0xFF000001, // accent_4
            0xFF030303, // accent_5
            0xFF000001, // success
            0xFF010000, // warning
            0xFF020002, // error
            0xFF000303, // info
            0xFF000303 // link
    );

    /** All built-in palettes, in the order they're offered in Settings. */
    public static List<Palette> all() {
        List<Palette> list = new ArrayList<Palette>();
        list.add(RAMA);
        list.add(TEYIN);
        list.add(MAKO);
        list.add(MELANGE);
        list.add(CATPPUCCIN_MOCHA);
        list.add(CATPPUCCIN_LATTE);
        list.add(DRACULA);
        list.add(TOKYO_NIGHT);
        list.add(MONO_DARK);
        list.add(MONO_LIGHT);
        return list;
    }

    public static Palette byId(String id) {
        List<Palette> list = all();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id.equals(id)) return list.get(i);
        }
        return RAMA;
    }
}
