package com.example.ddschedule.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ddschedule.db.AppDataBase;
import com.example.ddschedule.model.ScheduleModel;
import com.example.ddschedule.util.NotificationUtil;

import java.util.List;

public class NotificationWorker extends Worker {

    public static final String SCHEDULE_SYNC_WORK_NAME = "notification_work";
    public static final Long INTERVAL_TIME = 900000L; // 15 minutes

    private Context appContext;

    public NotificationWorker(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        this.appContext = appContext;
    }

    @NonNull
    @Override
    public Result doWork() {
        List<ScheduleModel> schedules = getSchedules(getGroupIDs());
        NotificationUtil notificationUtil = new NotificationUtil(appContext);
        notificationUtil.setupChannel();
        notificationUtil.showNotification(schedules);
        return null;
    }

    private List<String> getGroupIDs() {
        List<String> tmpGroups = AppDataBase.getDatabase(appContext).groupDao().getSelectedGroupIDsNow();
        Log.d("TAG", "getGroupIDs: "+tmpGroups.toString());
        return tmpGroups;
    }

    private List<ScheduleModel> getSchedules(List<String> groups) {
        List<ScheduleModel> schedules = AppDataBase.getDatabase(appContext).scheduleDao().
                getNotificationSchedules(groups, System.currentTimeMillis(), INTERVAL_TIME);
        return schedules;
    }


}
