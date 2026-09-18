package com.rama.mako_zero.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rama.mako_zero.R;
import com.rama.mako_zero.helpers.DialogHelper;
import com.rama.mako_zero.managers.FontManager;
import com.rama.mako_zero.managers.GroupManager;
import com.rama.mako_zero.managers.PrefsManager;
import com.rama.mako_zero.managers.ThemeManager;
import com.rama.mako_zero.objects.Themes;
import com.rama.mako_zero.widgets.WdCheckbox;
import com.rama.mako_zero.widgets.WdRadio;
import com.rama.mako_zero.widgets.WdRadioGroup;

import java.util.ArrayList;
import java.util.List;

public class Settings extends Activity {
    private GroupManager groupManager;
    private LinearLayout groupsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        groupManager = new GroupManager(this);
        groupsContainer = findViewById(R.id.groups_container);
        View root = findViewById(R.id.root);
        FontManager.apply(root, FontManager.getJersey25(this));
        setupSystemSection();
        setupGroupsSection();
        setupAppearanceSection();
        ThemeManager.applyTheme(this, root);
        if (Build.VERSION.SDK_INT < 11) {
            findViewById(R.id.themes_section).setVisibility(View.GONE);
            findViewById(R.id.themes_separator).setVisibility(View.GONE);
        }
        Button btnAbout = findViewById(R.id.go_about);
        btnAbout.setOnClickListener(v -> startActivity(new Intent(Settings.this, About.class)));
        Button btnBack = findViewById(R.id.go_back);
        btnBack.setOnClickListener(v -> startActivity(new Intent(Settings.this, Main.class)));
    }

    private void setupSystemSection() {
        Button activateButton = findViewById(R.id.activate_button);
        activateButton.setOnClickListener(v -> setLauncherAsDefault());
        Button resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, Main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        Button changeAppsButton = findViewById(R.id.change_apps_button);
        changeAppsButton.setOnClickListener(v -> {
            try {
                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_SETTINGS));
            } catch (Exception e) {
                Toast.makeText(Settings.this, R.string.toast_unable_open_settings, Toast.LENGTH_SHORT).show();
            }
        });
        final WdCheckbox preventRotation = findViewById(R.id.prevent_home_screen_rotation);
        preventRotation.setChecked(PrefsManager.getInstance(this).getBoolean(PrefsManager.PREVENT_ROTATION, false));
        preventRotation.setOnCheckedChangeListener(isChecked -> PrefsManager.getInstance(Settings.this).setBoolean(PrefsManager.PREVENT_ROTATION, isChecked));
        final WdCheckbox showApiIndicators = findViewById(R.id.show_api_indicators);
        showApiIndicators.setChecked(PrefsManager.getInstance(this).hasApiIndicatorsVisible());
        showApiIndicators.setOnCheckedChangeListener(isChecked -> PrefsManager.getInstance(Settings.this).setApiIndicatorsVisible(isChecked));
        final WdCheckbox showAppSize = findViewById(R.id.show_app_size);
        showAppSize.setChecked(PrefsManager.getInstance(this).hasAppSizeVisible());
        showAppSize.setOnCheckedChangeListener(isChecked -> PrefsManager.getInstance(Settings.this).setAppSizeVisible(isChecked));
    }

    private void setLauncherAsDefault() {
        try {
            startActivity(new Intent(android.provider.Settings.ACTION_HOME_SETTINGS));
            return;
        } catch (Exception ignored) {
            // No direct default-home-app screen before Android 10, fall through.
        }
        try {
            getPackageManager().clearPackagePreferredActivities(getPackageName());
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, R.string.toast_unable_open_settings, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupGroupsSection() {
        renderGroups();
        final PrefsManager prefs = PrefsManager.getInstance(this);
        final WdCheckbox collapseOnHome = findViewById(R.id.collapse_groups_on_home);
        collapseOnHome.setChecked(prefs.shouldCollapseGroupsOnHome());
        collapseOnHome.setOnCheckedChangeListener(isChecked -> prefs.setCollapseGroupsOnHome(isChecked));
        final WdCheckbox onlyOneOpen = findViewById(R.id.only_one_group_open);
        onlyOneOpen.setChecked(prefs.isOnlyOneGroupOpenEnabled());
        onlyOneOpen.setOnCheckedChangeListener(isChecked -> prefs.setOnlyOneGroupOpenEnabled(isChecked));
        Button addGroupButton = findViewById(R.id.add_group_button);
        addGroupButton.setOnClickListener(v -> {
            groupManager.createGroup(getString(R.string.new_group_header));
            renderGroups();
        });
    }

    private void setupAppearanceSection() {
        final PrefsManager prefs = PrefsManager.getInstance(this);
        WdRadioGroup themeGroup = findViewById(R.id.theme_group);
        String currentTheme = ThemeManager.currentPalette(this).id;
        List<Themes.Palette> palettes = Themes.all();
        for (int i = 0; i < palettes.size(); i++) {
            final Themes.Palette palette = palettes.get(i);
            WdRadio radio = new WdRadio(this);
            radio.setId(1000 + i);
            radio.setText(palette.label);
            radio.setTextColor(getResources().getColor(R.color.text));
            radio.setChecked(palette.id.equals(currentTheme));
            themeGroup.addView(radio);
            radio.setOnClickListener(v -> {
                prefs.setTheme(palette.id);
                ThemeManager.applyTheme(Settings.this, findViewById(R.id.root));
            });
        }
    }

    private void renderGroups() {
        groupsContainer.removeAllViews();
        List<String> groupIds = groupManager.getGroupIds();
        for (int i = 0; i < groupIds.size(); i++) {
            addGroupRow(groupIds.get(i));
        }
    }

    private void addGroupRow(final String groupId) {
        View row = getLayoutInflater().inflate(R.layout.list_item_group, groupsContainer, false);
        FontManager.apply(row, FontManager.getJersey25(this));
        final EditText name = row.findViewById(R.id.group_name);
        View delete = row.findViewById(R.id.delete_group);
        View toggleVisibility = row.findViewById(R.id.toggle_visibility);
        final ImageView toggleVisibilityIcon = row.findViewById(R.id.toggle_visibility_img);
        View toggleKeepExpanded = row.findViewById(R.id.toggle_keep_expanded);
        final ImageView toggleKeepExpandedIcon = row.findViewById(R.id.toggle_keep_expanded_img);
        final View saveButton = row.findViewById(R.id.save_changes_button);
        View ascend = row.findViewById(R.id.ascend_group);
        View descend = row.findViewById(R.id.descend_group);
        name.setText(groupManager.getGroupLabel(groupId));
        updateVisibilityIcon(toggleVisibilityIcon, groupId);
        updateKeepExpandedIcon(toggleKeepExpandedIcon, groupId);
        toggleVisibility.setOnClickListener(v -> {
            groupManager.toggleGroupVisible(groupId);
            updateVisibilityIcon(toggleVisibilityIcon, groupId);
        });
        toggleKeepExpanded.setOnClickListener(v -> {
            groupManager.toggleGroupKeepExpanded(groupId);
            updateKeepExpandedIcon(toggleKeepExpandedIcon, groupId);
            renderGroups();
        });
        final String originalText = name.getText().toString();
        name.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String current = s.toString();
                boolean changed = !current.equals(originalText) && current.trim().length() > 0;
                saveButton.setVisibility(changed ? View.VISIBLE : View.GONE);
            }

            public void afterTextChanged(Editable s) {
            }
        });

        saveButton.setOnClickListener(v -> {
            String newLabel = name.getText().toString().trim();
            if (newLabel.length() > 0) {
                groupManager.renameGroup(groupId, newLabel);
                Toast.makeText(Settings.this, R.string.toast_group_label_updated, Toast.LENGTH_SHORT).show();
                renderGroups();
            }
        });
        ascend.setOnClickListener(v -> {
            groupManager.moveGroup(groupId, -1);
            renderGroups();
        });
        descend.setOnClickListener(v -> {
            groupManager.moveGroup(groupId, 1);
            renderGroups();
        });
        if (PrefsManager.DEFAULT_GROUP_ID.equals(groupId)) {
            delete.setVisibility(View.GONE);
        } else {
            delete.setOnClickListener(v -> showDeleteGroupDialog(groupId, name.getText().toString()));
        }
        groupsContainer.addView(row);
    }

    private void updateVisibilityIcon(ImageView icon, String groupId) {
        icon.setImageResource(groupManager.isGroupVisible(groupId) ? R.drawable.px_eye : R.drawable.px_eye_cross);
    }

    private void updateKeepExpandedIcon(ImageView icon, String groupId) {
        icon.setImageResource(groupManager.isGroupKeepExpanded(groupId) ? R.drawable.px_pin : R.drawable.px_pin_outline);
    }

    private void showDeleteGroupDialog(final String groupId, String groupLabel) {
        DialogHelper.show(this, R.layout.dialog_groups_delete, (view, dialog) -> {
            TextView groupNameView = view.findViewById(R.id.group_name);
            WdRadioGroup radioGroup = view.findViewById(R.id.groups);
            View yesButton = view.findViewById(R.id.yes_button);
            View noButton = view.findViewById(R.id.no_button);
            groupNameView.setText(groupLabel);
            final List<String> targetGroups = new ArrayList<>();
            List<String> allGroups = groupManager.getGroupIds();
            for (int i = 0; i < allGroups.size(); i++) {
                String targetId = allGroups.get(i);
                if (!targetId.equals(groupId)) {
                    targetGroups.add(targetId);
                }
            }
            final String[] selectedGroupId = new String[1];
            for (int i = 0; i < targetGroups.size(); i++) {
                String targetId = targetGroups.get(i);
                WdRadio radio = new WdRadio(this);
                radio.setId(2000 + i);
                radio.setText(groupManager.getGroupLabel(targetId));
                radio.setTextColor(getResources().getColor(R.color.text));
                radioGroup.addView(radio);
                if (i == 0) {
                    radio.setChecked(true);
                    selectedGroupId[0] = targetId;
                }
            }
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                WdRadio checked = group.findViewById(checkedId);
                if (checked == null) {
                    return;
                }
                int index = group.indexOfChild(checked);
                if (index >= 0 && index < targetGroups.size()) {
                    selectedGroupId[0] = targetGroups.get(index);
                }
            });
            yesButton.setOnClickListener(v -> {
                if (selectedGroupId[0] == null) {
                    Toast.makeText(Settings.this, R.string.toast_select_target_group, Toast.LENGTH_SHORT).show();
                    return;
                }
                groupManager.deleteGroup(groupId, selectedGroupId[0]);
                renderGroups();
                dialog.dismiss();
            });
            noButton.setOnClickListener(v -> dialog.dismiss());
        });
    }

}
