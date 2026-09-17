package com.rama.mako_zero.managers;

import android.os.Handler;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.text.DateFormatSymbols;

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
        String timePattern = "HH:mm";
        timeView.setText(new SimpleDateFormat(timePattern, locale).format(calendar.getTime()));
        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        String weekday = symbols.getWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)];
        String date = new SimpleDateFormat("yyyy-MM-dd", locale).format(calendar.getTime());
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int totalDays = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
        String weekOfYear = "w-" + calendar.get(Calendar.WEEK_OF_YEAR);
        String yearDay = dayOfYear + "/" + totalDays;
        String line = weekday + " :: " + date + " :: " + weekOfYear + " :: " + yearDay;
        dateView.setText(line.toUpperCase(locale));
    }
}
