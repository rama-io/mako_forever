package com.rama.mako_forever.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GroupManager {

    private final PrefsManager prefs;

    public GroupManager(Context context) {
        prefs = PrefsManager.getInstance(context);
    }

    public List<String> getGroupIds() {
        List<String> ids = new ArrayList<String>(prefs.getGroupIds());
        Collections.sort(ids, new Comparator<String>() {
            public int compare(String a, String b) {
                return prefs.getGroupOrder(a) - prefs.getGroupOrder(b);
            }
        });
        return ids;
    }

    public String getGroupLabel(String groupId) {
        return prefs.getGroupLabel(groupId);
    }

    public boolean isGroupExpanded(String groupId) {
        return prefs.isGroupExpanded(groupId);
    }

    public void toggleGroupExpanded(String groupId) {
        prefs.setGroupExpanded(groupId, !prefs.isGroupExpanded(groupId));
    }

    public String getAppGroupId(String packageName) {
        String groupId = prefs.getAppGroupId(packageName);
        if (!getGroupIds().contains(groupId)) {
            return PrefsManager.DEFAULT_GROUP_ID;
        }
        return groupId;
    }
}
