package com.rama.mako_zero.managers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppsProvider {
    public static class AppEntry {
        public final String packageName;
        public final String activityName;
        public final String label;
        private final ApplicationInfo applicationInfo;

        AppEntry(String packageName, String activityName, String label, ResolveInfo resolveInfo) {
            this.packageName = packageName;
            this.activityName = activityName;
            this.label = label;
            this.applicationInfo = resolveInfo.activityInfo.applicationInfo;
        }

        public int getMinSdkVersion() {
            if (Build.VERSION.SDK_INT >= 24) {
                return applicationInfo.minSdkVersion;
            }
            return 0;
        }

        public int getTargetSdkVersion() {
            return applicationInfo.targetSdkVersion;
        }
    }

    private final Context context;
    private final Map<String, Long> appSizeCache = new HashMap<String, Long>();

    public AppsProvider(Context context) {
        this.context = context.getApplicationContext();
    }

    public long getAppSizeBytes(AppEntry app) {
        String key = appCacheKey(app);
        Long cached = appSizeCache.get(key);
        if (cached != null) {
            return cached.longValue();
        }
        long size = 0;
        try {
            ApplicationInfo info = app.applicationInfo;
            if (info.sourceDir != null) {
                size += new File(info.sourceDir).length();
            }
            if (Build.VERSION.SDK_INT >= 21 && info.splitSourceDirs != null) {
                for (int i = 0; i < info.splitSourceDirs.length; i++) {
                    String split = info.splitSourceDirs[i];
                    if (split != null) {
                        size += new File(split).length();
                    }
                }
            }
        } catch (Exception e) {
            size = 0;
        }
        appSizeCache.put(key, Long.valueOf(size));
        return size;
    }

    private String appCacheKey(AppEntry app) {
        return app.packageName + ":" + app.activityName;
    }

    public void clearAppSizeCache() {
        appSizeCache.clear();
    }

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
        Collections.sort(apps, new Comparator<AppEntry>() {
            public int compare(AppEntry a, AppEntry b) {
                return a.label.compareToIgnoreCase(b.label);
            }
        });
        return apps;
    }

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