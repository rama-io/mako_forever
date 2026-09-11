package com.rama.mako_forever.managers;

import android.os.Handler;
import android.text.format.DateFormat;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Keeps the clock and date TextViews up to date.
 *
 * Follows the system's 12h/24h setting (via {@link DateFormat#is24HourFormat})
 * and shows "Weekday :: yyyy-MM-dd", mirroring the original app's default
 * clock/date look without exposing any of the format options as settings.
 */
public class ClockManager {

    private static final long TICK_MS = 1000;

    private final TextView timeView;
    private final TextView dateView;
    private final Handler handler = new Handler();

    private final Runnable tick = new Runnable() {
        public void run() {
            update();
            handler.postDelayed(this, TICK_MS);
        }
    };

    public ClockManager(TextView timeView, TextView dateView) {
        this.timeView = timeView;
        this.dateView = dateView;
    }

    public void start() {
        handler.post(tick);
    }

    public void stop() {
        handler.removeCallbacks(tick);
    }

    private void update() {
        Calendar calendar = Calendar.getInstance();
        Locale locale = timeView.getResources().getConfiguration().locale;

        boolean use24h = DateFormat.is24HourFormat(timeView.getContext());
        String timePattern = use24h ? "HH:mm" : "hh:mm a";
        timeView.setText(new SimpleDateFormat(timePattern, locale).format(calendar.getTime()));

        String weekday = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale);
        String date = new SimpleDateFormat("yyyy-MM-dd", locale).format(calendar.getTime());
        dateView.setText((weekday + " :: " + date).toUpperCase(locale));
    }
}
