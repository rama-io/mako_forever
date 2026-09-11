package com.rama.mako_forever.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Reads the group structure that apps are bucketed into.
 *
 * This minimal build only *displays* groups (label, order, expand/collapse) -
 * there's no UI yet for creating/renaming groups or moving apps between them.
 * Everything not in a group falls back to a single "Apps" group, so the
 * feature degrades gracefully to a plain app list until a management screen
 * is added on top of this.
 */
public class GroupManager {

    private final PrefsManager prefs;

    public GroupManager(Context context) {
        prefs = PrefsManager.getInstance(context);
    }

    /** Group ids sorted by their display order. */
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
        // Self-heal: if the app points at a group that no longer exists,
        // fall back to the default group instead of hiding the app.
        if (!getGroupIds().contains(groupId)) {
            return PrefsManager.DEFAULT_GROUP_ID;
        }
        return groupId;
    }
}
