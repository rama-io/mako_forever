package com.rama.mako_forever.managers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Looks up launchable apps and launches them.
 *
 * This intentionally only uses {@link PackageManager}, which has existed since
 * API 1. The original Mako uses {@code LauncherApps} (API 21+) to also support
 * work-profile apps and pinned shortcuts, but that API isn't available on our
 * API 9 floor, so this minimal build only ever shows the apps of the current
 * user/profile.
 */
public class AppsProvider {

    /** A single launchable app. */
    public static class AppEntry {
        public final String packageName;
        public final String activityName;
        public final String label;
        private final ResolveInfo resolveInfo;

        AppEntry(String packageName, String activityName, String label, ResolveInfo resolveInfo) {
            this.packageName = packageName;
            this.activityName = activityName;
            this.label = label;
            this.resolveInfo = resolveInfo;
        }
    }

    private final Context context;

    public AppsProvider(Context context) {
        this.context = context.getApplicationContext();
    }

    /** Returns every app that shows up on the system launcher, sorted by label. */
    public List<AppEntry> getAll() {
        PackageManager pm = context.getPackageManager();

        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolved = pm.queryIntentActivities(launcherIntent, 0);
        List<AppEntry> apps = new ArrayList<AppEntry>(resolved.size());

        for (int i = 0; i < resolved.size(); i++) {
            ResolveInfo info = resolved.get(i);
            String packageName = info.activityInfo.packageName;
            String activityName = info.activityInfo.name;
            String label = info.loadLabel(pm).toString();
            apps.add(new AppEntry(packageName, activityName, label, info));
        }

        java.util.Collections.sort(apps, new java.util.Comparator<AppEntry>() {
            public int compare(AppEntry a, AppEntry b) {
                return a.label.compareToIgnoreCase(b.label);
            }
        });

        return apps;
    }

    public Drawable getIcon(AppEntry app) {
        return app.resolveInfo.loadIcon(context.getPackageManager());
    }

    /** Launches the app. Returns false if it could no longer be started. */
    public boolean launch(AppEntry app) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(new ComponentName(app.packageName, app.activityName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
