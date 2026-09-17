package com.rama.mako_zero.managers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;

import java.util.Locale;

public class BatteryStatusManager {

    private static final String FAHRENHEIT_COUNTRIES = ",US,BS,BZ,KY,PW,";

    private final Context context;
    private final TextView view;
    private boolean registered = false;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctx, Intent intent) {
            update(intent);
        }
    };

    public BatteryStatusManager(Context context, TextView view) {
        this.context = context.getApplicationContext();
        this.view = view;
    }

    private void update(Intent intent) {
        if (intent == null) return;

        int level = intent.getIntExtra(android.os.BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(android.os.BatteryManager.EXTRA_SCALE, -1);
        if (level < 0 || scale <= 0) return;
        int levelPct = Math.round(level * 100f / scale);

        int tempC = intent.getIntExtra(android.os.BatteryManager.EXTRA_TEMPERATURE, -1) / 10;

        boolean useFahrenheit = FAHRENHEIT_COUNTRIES.contains("," + Locale.getDefault().getCountry() + ",");
        int temperature = useFahrenheit ? (tempC * 9 / 5 + 32) : tempC;
        String unit = useFahrenheit ? "\u00B0F" : "\u00B0C";

        view.setText(levelPct + "% :: " + temperature + unit);
    }

    public void register() {
        if (registered) return;
        Intent sticky = context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        registered = true;
        update(sticky);
    }

    public void unregister() {
        if (!registered) return;
        context.unregisterReceiver(receiver);
        registered = false;
    }
}
