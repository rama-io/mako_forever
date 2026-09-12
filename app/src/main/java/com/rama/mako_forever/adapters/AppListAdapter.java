package com.rama.mako_forever.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rama.mako_forever.R;
import com.rama.mako_forever.managers.AppsProvider;
import com.rama.mako_forever.managers.FontManager;
import com.rama.mako_forever.managers.GroupManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AppListAdapter extends BaseAdapter {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_APP = 1;

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

    public AppListAdapter(Context context, AppsProvider appsProvider, GroupManager groupManager) {
        this.context = context;
        this.appsProvider = appsProvider;
        this.groupManager = groupManager;
    }

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

        TextView label = (TextView) view.findViewById(R.id.header_text);
        label.setTypeface(FontManager.getJersey25(context));
        boolean expanded = groupManager.isGroupExpanded(header.groupId);
        String indicator = expanded ? "\u2212 " : "+ "; // minus / plus
        label.setText(indicator + header.label.toUpperCase(Locale.getDefault()));

        return view;
    }

    private View getAppView(final AppsProvider.AppEntry app, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_app, parent, false);
        }

        TextView label = (TextView) view.findViewById(R.id.app_label);
        label.setTypeface(FontManager.getJersey25(context));
        label.setText(app.label);

        return view;
    }
}
