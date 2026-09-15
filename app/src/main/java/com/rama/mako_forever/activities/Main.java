package com.rama.mako_forever.activities;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rama.mako_forever.R;
import com.rama.mako_forever.adapters.AppListAdapter;
import com.rama.mako_forever.managers.AppsProvider;
import com.rama.mako_forever.managers.BatteryStatusManager;
import com.rama.mako_forever.managers.ClockManager;
import com.rama.mako_forever.managers.FontManager;
import com.rama.mako_forever.managers.GroupManager;
import com.rama.mako_forever.managers.PrefsManager;
import com.rama.mako_forever.managers.ThemeManager;

import java.util.List;

public class Main extends Activity implements AppListAdapter.Listener {

    public static final String PREF_PREVENT_ROTATION = "settings:prevent_rotation";

    private ClockManager clockManager;
    private BatteryStatusManager batteryStatusManager;
    private AppsProvider appsProvider;
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

        View root = findViewById(R.id.root);
        FontManager.apply(root, FontManager.getJersey25(this));

        TextView timeView = (TextView) findViewById(R.id.time);
        TextView dateView = (TextView) findViewById(R.id.date);
        TextView batteryView = (TextView) findViewById(R.id.battery);
        ListView appList = (ListView) findViewById(R.id.app_list);

        clockManager = new ClockManager(timeView, dateView);
        batteryStatusManager = new BatteryStatusManager(this, batteryView);

        appsProvider = new AppsProvider(this);
        groupManager = new GroupManager(this);
        adapter = new AppListAdapter(this, appsProvider, groupManager);
        adapter.setListener(this);

        appList.setAdapter(adapter);

        // The blank area below the last row isn't a child view, so no row
        // listener can fire there. A GestureDetector on the ListView covers it:
        // ViewGroup only consults its own touch listener when no child consumed
        // the event, so this fires for empty space and never for a real row.
        final ListView appListRef = appList;
        final GestureDetector emptySpaceDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public void onLongPress(MotionEvent event) {
                        if (adapter.isMultiSelectMode()) return;
                        int position = appListRef.pointToPosition(
                                (int) event.getX(), (int) event.getY());
                        if (position == AdapterView.INVALID_POSITION) {
                            appListRef.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                            onOpenSettingsRequested();
                        }
                    }
                });
        appList.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                emptySpaceDetector.onTouchEvent(event);
                return false; // let the ListView keep scrolling normally
            }
        });

        menuBar = findViewById(R.id.menu_bar);
        selectedCountView = (TextView) findViewById(R.id.selected_count);
        renameButton = findViewById(R.id.rename_btn);
        appSettingsButton = findViewById(R.id.app_settings);
        View moveToGroupButton = findViewById(R.id.move_to_group_button);
        View cancelButton = findViewById(R.id.multi_select_cancel_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                adapter.exitMultiSelectMode();
            }
        });

        moveToGroupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showGroupPickerDialog();
            }
        });

        renameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AppsProvider.AppEntry app = adapter.getSingleSelectedApp();
                if (app != null) {
                    adapter.exitMultiSelectMode();
                    showRenameDialog(app);
                }
            }
        });

        appSettingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AppsProvider.AppEntry app = adapter.getSingleSelectedApp();
                if (app != null) {
                    adapter.exitMultiSelectMode();
                    openAppDetails(app);
                }
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

    /**
     * Pressing HOME while already on the launcher re-delivers the intent
     * rather than recreating the activity, so the collapse has to happen here
     * too - not just in onResume.
     */
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

    // ---------------- AppListAdapter.Listener ----------------

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

    // ---------------- app actions ----------------

    private void openAppDetails(AppsProvider.AppEntry app) {
        try {
            Intent intent = new Intent(
                    android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", app.packageName, null)
            );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, R.string.toast_unable_open_settings, Toast.LENGTH_SHORT).show();
        }
    }

    private void showRenameDialog(final AppsProvider.AppEntry app) {
        View view = getLayoutInflater().inflate(R.layout.dialog_rename_app, null);
        FontManager.apply(view, FontManager.getJersey25(this));

        final android.widget.EditText input = (android.widget.EditText) view.findViewById(R.id.edit_text);
        View yesButton = view.findViewById(R.id.yes_button);
        View resetButton = view.findViewById(R.id.reset_button);
        View noButton = view.findViewById(R.id.no_button);

        input.setText(groupManager.getAppLabel(app));
        input.setSelection(input.getText().length());

        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                .setView(view)
                .create();

        yesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String label = input.getText().toString().trim();
                if (label.length() > 0) {
                    groupManager.renameApp(app.packageName, label);
                    adapter.refresh();
                }
                dialog.dismiss();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                groupManager.resetAppLabel(app.packageName);
                adapter.refresh();
                dialog.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showGroupPickerDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_groups_pick, null);
        FontManager.apply(view, FontManager.getJersey25(this));

        final android.widget.RadioGroup radioGroup =
                (android.widget.RadioGroup) view.findViewById(R.id.groups);
        View closeButton = view.findViewById(R.id.close_button);

        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(true)
                .create();

        List<String> groupIds = groupManager.getGroupIds();
        for (int i = 0; i < groupIds.size(); i++) {
            final String groupId = groupIds.get(i);
            android.widget.RadioButton radio = new android.widget.RadioButton(this);
            radio.setText(groupManager.getGroupLabel(groupId));
            radio.setTextColor(getResources().getColor(R.color.text));
            radioGroup.addView(radio);

            radio.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    adapter.moveSelectedAppsToGroup(groupId);
                    dialog.dismiss();
                }
            });
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // ---------------- screen rotation lock ----------------

    private void applyRotationLock() {
        boolean prevent = PrefsManager.getInstance(this).getBoolean(PREF_PREVENT_ROTATION, false);
        if (!prevent) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            return;
        }

        // SCREEN_ORIENTATION_LOCKED needs API 18+; locking to the device's
        // *current* orientation instead works the same way back to API 1.
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
