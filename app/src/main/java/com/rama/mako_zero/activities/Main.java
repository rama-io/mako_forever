package com.rama.mako_zero.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rama.mako_zero.R;
import com.rama.mako_zero.adapters.AppListAdapter;
import com.rama.mako_zero.helpers.DialogHelper;
import com.rama.mako_zero.managers.AppsProvider;
import com.rama.mako_zero.managers.BatteryStatusManager;
import com.rama.mako_zero.managers.ClockManager;
import com.rama.mako_zero.managers.FontManager;
import com.rama.mako_zero.managers.GroupManager;
import com.rama.mako_zero.managers.PrefsManager;
import com.rama.mako_zero.managers.ThemeManager;
import com.rama.mako_zero.widgets.WdRadio;
import com.rama.mako_zero.widgets.WdRadioGroup;

import java.util.List;

public class Main extends Activity implements AppListAdapter.Listener {
    private ClockManager clockManager;
    private BatteryStatusManager batteryStatusManager;
    private GroupManager groupManager;
    private AppListAdapter adapter;
    private View menuBar;
    private TextView selectedCountView;
    private View renameButton;
    private View appSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyRotationLock();
        setContentView(R.layout.activity_main);
        FontManager.apply(findViewById(R.id.root), FontManager.getJersey25(this));
        ListView appList = findViewById(R.id.app_list);
        clockManager = new ClockManager(findViewById(R.id.time), findViewById(R.id.date));
        batteryStatusManager = new BatteryStatusManager(this, findViewById(R.id.battery));
        groupManager = new GroupManager(this);
        adapter = new AppListAdapter(this, new AppsProvider(this), groupManager);
        adapter.setListener(this);
        appList.setAdapter(adapter);
        appList.setOnItemClickListener((parent, view, position, id) -> adapter.performRowAction(position));
        findViewById(R.id.home_header).setOnLongClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            onOpenSettingsRequested();
            return true;
        });
        final ListView appListRef = appList;
        final GestureDetector emptySpaceDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent event) {
                if (adapter.isMultiSelectMode()) return;
                int position = appListRef.pointToPosition((int) event.getX(), (int) event.getY());
                if (position == AdapterView.INVALID_POSITION) {
                    appListRef.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    onOpenSettingsRequested();
                }
            }
        });
        appList.setOnTouchListener((v, event) -> {
            emptySpaceDetector.onTouchEvent(event);
            return false;
        });
        menuBar = findViewById(R.id.menu_bar);
        selectedCountView = findViewById(R.id.selected_count);
        renameButton = findViewById(R.id.rename_btn);
        appSettingsButton = findViewById(R.id.app_settings);
        View moveToGroupButton = findViewById(R.id.move_to_group_button);
        View cancelButton = findViewById(R.id.multi_select_cancel_button);
        cancelButton.setOnClickListener(v -> adapter.exitMultiSelectMode());
        moveToGroupButton.setOnClickListener(v -> showGroupPickerDialog());
        renameButton.setOnClickListener(v -> {
            AppsProvider.AppEntry app = adapter.getSingleSelectedApp();
            if (app != null) {
                adapter.exitMultiSelectMode();
                showRenameDialog(app);
            }
        });
        appSettingsButton.setOnClickListener(v -> {
            AppsProvider.AppEntry app = adapter.getSingleSelectedApp();
            if (app != null) {
                adapter.exitMultiSelectMode();
                openAppDetails(app);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        clockManager.start();
        batteryStatusManager.register();
        if (groupManager.shouldCollapseGroupsOnHome()) {
            groupManager.collapseAllGroups();
        }
        adapter.refresh();
        ThemeManager.applyTheme(this, findViewById(R.id.root));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (groupManager.shouldCollapseGroupsOnHome()) {
            groupManager.collapseAllGroups();
            adapter.refresh();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        clockManager.stop();
        batteryStatusManager.unregister();
    }

    @Override
    public void onAppLaunchFailed() {
        Toast.makeText(this, R.string.toast_unable_to_launch_app, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOpenSettingsRequested() {
        startActivity(new Intent(Main.this, Settings.class));
    }

    @Override
    public void onSelectionChanged(boolean active, int count) {
        menuBar.setVisibility(active ? View.VISIBLE : View.GONE);
        selectedCountView.setText(getString(R.string.multi_select_count, count));
        int singleVisibility = count == 1 ? View.VISIBLE : View.GONE;
        renameButton.setVisibility(singleVisibility);
        appSettingsButton.setVisibility(singleVisibility);
    }

    private void openAppDetails(AppsProvider.AppEntry app) {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", app.packageName, null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, R.string.toast_unable_open_settings, Toast.LENGTH_SHORT).show();
        }
    }

    private void showRenameDialog(final AppsProvider.AppEntry app) {
        DialogHelper.show(this, R.layout.dialog_rename_app, (view, dialog) -> {
            EditText input = view.findViewById(R.id.edit_text);
            input.setText(groupManager.getAppLabel(app));
            input.setSelection(input.getText().length());
            view.findViewById(R.id.yes_button).setOnClickListener(v -> {
                String label = input.getText().toString().trim();
                groupManager.renameApp(app.packageName, label);
                adapter.refresh();
                dialog.dismiss();
            });
            view.findViewById(R.id.reset_button).setOnClickListener(v -> {
                groupManager.resetAppLabel(app.packageName);
                adapter.refresh();
                dialog.dismiss();
            });
            view.findViewById(R.id.no_button).setOnClickListener(v -> dialog.dismiss());
        });
    }

    private void showGroupPickerDialog() {
        DialogHelper.show(this, R.layout.dialog_groups_pick, (view, dialog) -> {
            List<String> groupIds = groupManager.getGroupIds();
            for (int i = 0; i < groupIds.size(); i++) {
                final String groupId = groupIds.get(i);
                WdRadio radio = new WdRadio(this);
                radio.setId(3000 + i);
                radio.setText(groupManager.getGroupLabel(groupId));
                radio.setTextColor(getResources().getColor(R.color.text));
                ((WdRadioGroup) view.findViewById(R.id.groups)).addView(radio);
                radio.setOnClickListener(v -> {
                    adapter.moveSelectedAppsToGroup(groupId);
                    dialog.dismiss();
                });
            }
            view.findViewById(R.id.close_button).setOnClickListener(v -> dialog.dismiss());
        });
    }

    private void applyRotationLock() {
        boolean prevent = PrefsManager.getInstance(this).getBoolean(PrefsManager.PREVENT_ROTATION, false);
        if (!prevent) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            return;
        }
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
