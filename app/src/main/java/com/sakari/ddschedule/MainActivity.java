package com.sakari.ddschedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;

import com.sakari.ddschedule.worker.NotificationWorker;
import com.sakari.ddschedule.worker.ScheduleSyncWorker;

import static com.sakari.ddschedule.worker.NotificationWorker.SCHEDULE_NOTIFICATION_WORK_NAME;
import static com.sakari.ddschedule.worker.ScheduleSyncWorker.SCHEDULE_SYNC_WORK_NAME;
import static java.util.concurrent.TimeUnit.MINUTES;

public class MainActivity extends AppCompatActivity {

    private WorkManager mWorkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FrameLayout frameLayout = findViewById(R.id.content_fragment);
        frameLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        mWorkManager = WorkManager.getInstance(getApplicationContext());

        // Start Works
        starkWorks();

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ScheduleViewFragment fragment = new ScheduleViewFragment();
            transaction.replace(R.id.content_fragment, fragment);
            transaction.commit();
        }
    }

    private void starkWorks() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (sharedPreferences.getBoolean("switch_notifications", true)) {
            startNotificationWork();
        } else {
            cancelNotificationWork();
        }

        if (sharedPreferences.getBoolean("switch_sync", true)) {
            startSyncWork();
        } else {
            cancelSyncWork();
        }

    }

    private void startSyncWork() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest build = new PeriodicWorkRequest.Builder(
                ScheduleSyncWorker.class, 30, MINUTES, 5, MINUTES) //每三十分钟同步一次
                //ScheduleSyncWorker.class, 30, MINUTES)
                .setConstraints(constraints)
                .build();
        mWorkManager.enqueueUniquePeriodicWork(SCHEDULE_SYNC_WORK_NAME, ExistingPeriodicWorkPolicy.REPLACE, build);
        Log.d("TAG", "startSyncWork");
    }

    private void cancelSyncWork() {
        mWorkManager.cancelUniqueWork(SCHEDULE_SYNC_WORK_NAME);
        Log.d("TAG", "cancelSyncWork");
    }

    private void startNotificationWork() {
        PeriodicWorkRequest build = new PeriodicWorkRequest.Builder(
                NotificationWorker.class, 15, MINUTES, 5, MINUTES) //每十五分钟同步一次
                //NotificationWorker.class, 15, MINUTES)
                .build();
        mWorkManager.enqueueUniquePeriodicWork(SCHEDULE_NOTIFICATION_WORK_NAME, ExistingPeriodicWorkPolicy.REPLACE, build);
        Log.d("TAG", "startNotificationWork");
    }

    private void cancelNotificationWork() {
        mWorkManager.cancelUniqueWork(SCHEDULE_NOTIFICATION_WORK_NAME);
        Log.d("TAG", "cancelNotificationWork");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}