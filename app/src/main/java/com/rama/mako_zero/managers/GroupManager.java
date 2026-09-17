package com.rama.mako_zero.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class GroupManager {
    private final PrefsManager prefs;
    private final AppsProvider appsProvider;

    public GroupManager(Context context) {
        prefs = PrefsManager.getInstance(context);
        appsProvider = new AppsProvider(context);
    }

    public List<String> getGroupIds() {
        List<String> ids = new ArrayList<>(prefs.getGroupIds());
        Collections.sort(ids, (a, b) -> {
            boolean pinnedA = prefs.isGroupKeepExpanded(a);
            boolean pinnedB = prefs.isGroupKeepExpanded(b);
            if (pinnedA != pinnedB) return pinnedA ? -1 : 1;
            return prefs.getGroupOrder(a) - prefs.getGroupOrder(b);
        });
        return ids;
    }

    public String getGroupLabel(String groupId) {
        return prefs.getGroupLabel(groupId);
    }

    public void renameGroup(String groupId, String label) {
        prefs.setGroupLabel(groupId, FontManager.sanitizeForFont(label));
    }

    public boolean isGroupExpanded(String groupId) {
        return prefs.isGroupKeepExpanded(groupId) || prefs.isGroupExpanded(groupId);
    }

    public boolean isGroupKeepExpanded(String groupId) {
        return prefs.isGroupKeepExpanded(groupId);
    }

    public void toggleGroupKeepExpanded(String groupId) {
        boolean pinned = !prefs.isGroupKeepExpanded(groupId);
        prefs.setGroupKeepExpanded(groupId, pinned);
        if (pinned) prefs.setGroupExpanded(groupId, true);
    }

    public void toggleGroupExpanded(String groupId) {
        if (prefs.isGroupKeepExpanded(groupId)) return;
        boolean shouldExpand = !prefs.isGroupExpanded(groupId);
        prefs.setGroupExpanded(groupId, shouldExpand);
        if (shouldExpand && prefs.isOnlyOneGroupOpenEnabled()) {
            List<String> ids = getGroupIds();
            for (int i = 0; i < ids.size(); i++) {
                String other = ids.get(i);
                if (!other.equals(groupId) && !prefs.isGroupKeepExpanded(other)) {
                    prefs.setGroupExpanded(other, false);
                }
            }
        }
    }

    public boolean collapseAllGroups() {
        boolean changed = false;
        List<String> ids = getGroupIds();
        for (int i = 0; i < ids.size(); i++) {
            String id = ids.get(i);
            if (prefs.isGroupKeepExpanded(id)) continue;
            if (prefs.isGroupExpanded(id)) {
                prefs.setGroupExpanded(id, false);
                changed = true;
            }
        }
        return changed;
    }

    public boolean shouldCollapseGroupsOnHome() {
        return prefs.shouldCollapseGroupsOnHome();
    }

    public boolean isGroupVisible(String groupId) {
        return prefs.isGroupVisible(groupId);
    }

    public void toggleGroupVisible(String groupId) {
        prefs.setGroupVisible(groupId, !prefs.isGroupVisible(groupId));
    }

    public String getAppGroupId(String packageName) {
        String groupId = prefs.getAppGroupId(packageName);
        if (!getGroupIds().contains(groupId)) {
            return PrefsManager.DEFAULT_GROUP_ID;
        }
        return groupId;
    }

    public void moveAppToGroup(String packageName, String groupId) {
        prefs.setAppGroupId(packageName, groupId);
    }

    public String createGroup(String baseLabel) {
        String id = "group_" + System.currentTimeMillis();
        String label = generateUniqueLabel(baseLabel);
        prefs.addGroupId(id);
        prefs.setGroupLabel(id, label);
        prefs.setGroupVisible(id, true);
        prefs.setGroupExpanded(id, true);
        return id;
    }

    public void deleteGroup(String groupId, String newGroupId) {
        if (PrefsManager.DEFAULT_GROUP_ID.equals(groupId)) return;
        List<AppsProvider.AppEntry> allApps = appsProvider.getAll();
        for (int i = 0; i < allApps.size(); i++) {
            AppsProvider.AppEntry app = allApps.get(i);
            if (groupId.equals(prefs.getAppGroupId(app.packageName))) {
                prefs.setAppGroupId(app.packageName, newGroupId);
            }
        }
        prefs.removeGroupId(groupId);
        reindexOrder();
    }

    public void moveGroup(String groupId, int direction) {
        List<String> ordered = getGroupIds();
        int index = ordered.indexOf(groupId);
        int targetIndex = index + direction;
        if (index < 0 || targetIndex < 0 || targetIndex >= ordered.size()) return;
        for (int i = 0; i < ordered.size(); i++) {
            prefs.setGroupOrder(ordered.get(i), i);
        }
        String otherId = ordered.get(targetIndex);
        prefs.setGroupOrder(groupId, targetIndex);
        prefs.setGroupOrder(otherId, index);
    }

    public String getAppLabel(AppsProvider.AppEntry app) {
        String custom = prefs.getCustomName(app.packageName);
        return custom != null ? custom : app.label;
    }

    public void renameApp(String packageName, String label) {
        prefs.setCustomName(packageName, FontManager.sanitizeForFont(label));
    }

    public void resetAppLabel(String packageName) {
        prefs.clearCustomName(packageName);
    }

    private String generateUniqueLabel(String base) {
        List<String> ids = prefs.getGroupIds();
        List<String> existingLabels = new ArrayList<String>();
        for (int i = 0; i < ids.size(); i++) {
            existingLabels.add(prefs.getGroupLabel(ids.get(i)).trim().toLowerCase(Locale.getDefault()));
        }
        String label = base;
        int counter = 1;
        while (existingLabels.contains(label.trim().toLowerCase(Locale.getDefault()))) {
            counter++;
            label = base + " " + counter;
        }
        return label;
    }

    private void reindexOrder() {
        List<String> ids = getGroupIds();
        for (int i = 0; i < ids.size(); i++) {
            prefs.setGroupOrder(ids.get(i), i);
        }
    }
}
