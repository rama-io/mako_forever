package com.rama.mako_forever.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.rama.mako_forever.R;

import android.content.Intent;

import com.rama.mako_forever.adapters.AppListAdapter;
import com.rama.mako_forever.managers.AppsProvider;
import com.rama.mako_forever.managers.BatteryStatusManager;
import com.rama.mako_forever.managers.ClockManager;
import com.rama.mako_forever.managers.FontManager;
import com.rama.mako_forever.managers.GroupManager;

public class Main extends Activity {

    private ClockManager clockManager;
    private BatteryStatusManager batteryStatusManager;
    private AppsProvider appsProvider;
    private GroupManager groupManager;
    private AppListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        appList.setAdapter(adapter);
        appList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = adapter.getItem(position);

                if (item instanceof AppListAdapter.HeaderRow) {
                    groupManager.toggleGroupExpanded(((AppListAdapter.HeaderRow) item).groupId);
                    adapter.refresh();
                } else {
                    AppsProvider.AppEntry app = (AppsProvider.AppEntry) item;
                    if (!appsProvider.launch(app)) {
                        Toast.makeText(
                                Main.this,
                                R.string.toast_unable_to_launch_app,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
        });

        final GestureDetector emptySpaceDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent event) {
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent event) {
                        startActivity(new Intent(Main.this, Settings.class));
                    }
                });
        appList.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                emptySpaceDetector.onTouchEvent(event);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        clockManager.start();
        batteryStatusManager.register();
        adapter.refresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        clockManager.stop();
        batteryStatusManager.unregister();
    }
}
