package com.rama.mako_forever.managers;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class PrefsManager {

    private static final String PREFS_NAME = "mako_forever";

    public static final String DEFAULT_GROUP_ID = "ungrouped";
    public static final String DEFAULT_GROUP_LABEL = "Apps";

    private static PrefsManager instance;

    private final SharedPreferences prefs;

    private PrefsManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized PrefsManager getInstance(Context context) {
        if (instance == null) {
            instance = new PrefsManager(context);
        }
        return instance;
    }

    private String key(String... parts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) sb.append(':');
            sb.append(parts[i]);
        }
        return sb.toString();
    }

    private List<String> splitCsv(String value) {
        List<String> result = new ArrayList<String>();
        if (value == null || value.length() == 0) return result;
        String[] parts = value.split(",");
        for (String part : parts) {
            if (part.length() > 0) result.add(part);
        }
        return result;
    }

    private String joinCsv(java.util.Collection<String> values) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String value : values) {
            if (!first) sb.append(',');
            sb.append(value);
            first = false;
        }
        return sb.toString();
    }

    public List<String> getGroupIds() {
        LinkedHashSet<String> ids = new LinkedHashSet<String>();
        ids.add(DEFAULT_GROUP_ID);
        ids.addAll(splitCsv(prefs.getString(key("groups", "ids"), "")));
        return new ArrayList<String>(ids);
    }

    public void addGroupId(String groupId) {
        List<String> ids = getGroupIds();
        if (!ids.contains(groupId)) {
            ids.add(groupId);
            prefs.edit().putString(key("groups", "ids"), joinCsv(ids)).commit();
        }
    }

    public String getGroupLabel(String groupId) {
        if (DEFAULT_GROUP_ID.equals(groupId)) {
            return prefs.getString(key("group", groupId, "label"), DEFAULT_GROUP_LABEL);
        }
        return prefs.getString(key("group", groupId, "label"), groupId);
    }

    public void setGroupLabel(String groupId, String label) {
        prefs.edit().putString(key("group", groupId, "label"), label).commit();
    }

    public int getGroupOrder(String groupId) {
        int fallback = DEFAULT_GROUP_ID.equals(groupId) ? 0 : Integer.MAX_VALUE;
        return prefs.getInt(key("group", groupId, "order"), fallback);
    }

    public void setGroupOrder(String groupId, int order) {
        prefs.edit().putInt(key("group", groupId, "order"), order).commit();
    }

    public boolean isGroupExpanded(String groupId) {
        return prefs.getBoolean(key("group", groupId, "expanded"), true);
    }

    public void setGroupExpanded(String groupId, boolean expanded) {
        prefs.edit().putBoolean(key("group", groupId, "expanded"), expanded).commit();
    }

    /** Which group an app belongs to. Falls back to the default group. */
    public String getAppGroupId(String packageName) {
        return prefs.getString(key("app", packageName, "group"), DEFAULT_GROUP_ID);
    }

    public void setAppGroupId(String packageName, String groupId) {
        prefs.edit().putString(key("app", packageName, "group"), groupId).commit();
    }
}
