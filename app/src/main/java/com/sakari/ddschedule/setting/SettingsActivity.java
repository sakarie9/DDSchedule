package com.sakari.ddschedule.setting;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.sakari.ddschedule.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SwitchPreference filter = findPreference("switch_filter");
            SwitchPreference filter_schedule = findPreference("switch_filter_schedule");
            SwitchPreference filter_notifications = findPreference("switch_filter_notifications");
            Preference filter_settings = findPreference("switch_filter_settings");
            assert filter != null;
            assert filter_settings != null;
            assert filter_schedule != null;
            assert filter_notifications != null;
            filter.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if ((boolean) newValue) {
                        filter_settings.setEnabled(true);
                        filter_schedule.setVisible(true);
                        filter_notifications.setVisible(true);
                    } else {
                        filter_settings.setEnabled(false);
                        filter_schedule.setVisible(false);
                        filter_notifications.setVisible(false);
                    }
                    return true;
                }
            });
            if (filter.isChecked()) {
                filter_settings.setEnabled(true);
                filter_schedule.setVisible(true);
                filter_notifications.setVisible(true);
            } else {
                filter_settings.setEnabled(false);
                filter_schedule.setVisible(false);
                filter_notifications.setVisible(false);
            }
        }
    }
}