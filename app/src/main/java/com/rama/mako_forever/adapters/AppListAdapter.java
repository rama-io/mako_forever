package com.rama.mako_forever.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rama.mako_forever.R;
import com.rama.mako_forever.managers.AppsProvider;
import com.rama.mako_forever.managers.FontManager;
import com.rama.mako_forever.managers.GroupManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * A flat, plain {@link BaseAdapter} backing the home ListView: each row is
 * either a group header or an app. Also owns multi-select state, since (as
 * in mako) selection lives per-row and needs to survive list rebuilds.
 */
public class AppListAdapter extends BaseAdapter {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_APP = 1;

    /** Callbacks for things that need Activity-level context (dialogs, toasts, the menu bar). */
    public interface Listener {
        void onAppLaunchFailed();
        void onOpenSettingsRequested();
        void onSelectionChanged(boolean active, int count);
    }

    /** A group header row. */
    public static class HeaderRow {
        public final String groupId;
        public final String label;

        HeaderRow(String groupId, String label) {
            this.groupId = groupId;
            this.label = label;
        }
    }

    private final Context context;
    private final AppsProvider appsProvider;
    private final GroupManager groupManager;
    private final List<Object> items = new ArrayList<Object>();
    private final Set<String> selectedPackages = new HashSet<String>();

    private boolean multiSelectMode = false;
    private Listener listener;

    public AppListAdapter(Context context, AppsProvider appsProvider, GroupManager groupManager) {
        this.context = context;
        this.appsProvider = appsProvider;
        this.groupManager = groupManager;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    // ---------------- multi-select ----------------

    public boolean isMultiSelectMode() {
        return multiSelectMode;
    }

    public int getSelectedCount() {
        return selectedPackages.size();
    }

    public Set<String> getSelectedPackages() {
        return new HashSet<String>(selectedPackages);
    }

    /** Returns the selected app if exactly one is selected, else null. */
    public AppsProvider.AppEntry getSingleSelectedApp() {
        if (selectedPackages.size() != 1) return null;
        String pkg = selectedPackages.iterator().next();
        List<AppsProvider.AppEntry> all = appsProvider.getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).packageName.equals(pkg)) return all.get(i);
        }
        return null;
    }

    public void exitMultiSelectMode() {
        multiSelectMode = false;
        selectedPackages.clear();
        notifySelectionChanged();
        refresh();
    }

    public void moveSelectedAppsToGroup(String groupId) {
        for (String pkg : selectedPackages) {
            groupManager.moveAppToGroup(pkg, groupId);
        }
        exitMultiSelectMode();
    }

    private void enterMultiSelectMode(String packageName) {
        multiSelectMode = true;
        selectedPackages.clear();
        selectedPackages.add(packageName);
        notifySelectionChanged();
        refresh();
    }

    private void toggleSelection(String packageName) {
        if (selectedPackages.contains(packageName)) {
            selectedPackages.remove(packageName);
            if (selectedPackages.isEmpty()) {
                exitMultiSelectMode();
                return;
            }
        } else {
            selectedPackages.add(packageName);
        }
        notifySelectionChanged();
        refresh();
    }

    private void notifySelectionChanged() {
        if (listener != null) listener.onSelectionChanged(multiSelectMode, selectedPackages.size());
    }

    // ---------------- data ----------------

    /** Re-reads installed apps and group state, rebuilding the row list. */
    public void refresh() {
        List<AppsProvider.AppEntry> allApps = appsProvider.getAll();

        Map<String, List<AppsProvider.AppEntry>> byGroup =
                new HashMap<String, List<AppsProvider.AppEntry>>();

        for (int i = 0; i < allApps.size(); i++) {
            AppsProvider.AppEntry app = allApps.get(i);
            String groupId = groupManager.getAppGroupId(app.packageName);
            List<AppsProvider.AppEntry> bucket = byGroup.get(groupId);
            if (bucket == null) {
                bucket = new ArrayList<AppsProvider.AppEntry>();
                byGroup.put(groupId, bucket);
            }
            bucket.add(app);
        }

        items.clear();
        List<String> groupIds = groupManager.getGroupIds();

        for (int i = 0; i < groupIds.size(); i++) {
            String groupId = groupIds.get(i);
            if (!groupManager.isGroupVisible(groupId)) continue;

            List<AppsProvider.AppEntry> apps = byGroup.get(groupId);
            if (apps == null || apps.isEmpty()) continue;

            items.add(new HeaderRow(groupId, groupManager.getGroupLabel(groupId)));

            if (groupManager.isGroupExpanded(groupId)) {
                items.addAll(apps);
            }
        }

        notifyDataSetChanged();
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof HeaderRow ? TYPE_HEADER : TYPE_APP;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Object item = items.get(position);
        if (item instanceof HeaderRow) {
            return getHeaderView((HeaderRow) item, convertView, parent);
        }
        return getAppView((AppsProvider.AppEntry) item, convertView, parent);
    }

    private View getHeaderView(HeaderRow header, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_header, parent, false);
        }

        final String groupId = header.groupId;

        TextView label = (TextView) view.findViewById(R.id.header_text);
        label.setTypeface(FontManager.getJersey25(context));

        boolean pinned = groupManager.isGroupKeepExpanded(groupId);
        boolean expanded = groupManager.isGroupExpanded(groupId);
        // A pinned group can't be collapsed, so it shows no +/- affordance.
        String indicator = pinned ? "" : (expanded ? "\u2212 " : "+ ");
        label.setText(indicator + header.label.toUpperCase(Locale.getDefault()));

        if (pinned) {
            view.setOnClickListener(null);
            view.setClickable(false);
        } else {
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    groupManager.toggleGroupExpanded(groupId);
                    refresh();
                }
            });
        }

        return view;
    }

    private View getAppView(final AppsProvider.AppEntry app, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_app, parent, false);
        }

        final TextView label = (TextView) view.findViewById(R.id.app_label);
        label.setTypeface(FontManager.getJersey25(context));
        label.setText(groupManager.getAppLabel(app));

        View emptySpace = view.findViewById(R.id.empty_space);
        ImageView selectionCheck = (ImageView) view.findViewById(R.id.selection_check);

        if (!multiSelectMode) {
            selectionCheck.setVisibility(View.GONE);
        } else {
            boolean isSelected = selectedPackages.contains(app.packageName);
            // No View.setAlpha() here (API 11+) - visibility toggling is the
            // API-9-safe way to show which rows are selected.
            selectionCheck.setVisibility(isSelected ? View.VISIBLE : View.INVISIBLE);
        }

        View.OnClickListener launchOrToggle = new View.OnClickListener() {
            public void onClick(View v) {
                if (multiSelectMode) {
                    toggleSelection(app.packageName);
                } else if (!appsProvider.launch(app)) {
                    if (listener != null) listener.onAppLaunchFailed();
                }
            }
        };
        view.setOnClickListener(launchOrToggle);
        label.setOnClickListener(launchOrToggle);
        emptySpace.setOnClickListener(launchOrToggle);

        View.OnLongClickListener selectOnLongPress = new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (multiSelectMode) {
                    toggleSelection(app.packageName);
                } else {
                    enterMultiSelectMode(app.packageName);
                }
                return true;
            }
        };
        view.setOnLongClickListener(selectOnLongPress);
        label.setOnLongClickListener(selectOnLongPress);

        emptySpace.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (multiSelectMode) {
                    toggleSelection(app.packageName);
                } else if (listener != null) {
                    listener.onOpenSettingsRequested();
                }
                return true;
            }
        });

        return view;
    }
}
