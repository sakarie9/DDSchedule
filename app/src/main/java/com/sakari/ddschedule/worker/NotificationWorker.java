package com.sakari.ddschedule.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.sakari.ddschedule.db.AppDataBase;
import com.sakari.ddschedule.model.ScheduleModel;
import com.sakari.ddschedule.util.NotificationUtil;
import com.sakari.ddschedule.util.SettingsUtil;

import java.util.List;

public class NotificationWorker extends Worker {

    public static final String SCHEDULE_NOTIFICATION_WORK_NAME = "notification_work";
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
        List<ScheduleModel> schedules = getSchedules();
        NotificationUtil notificationUtil = new NotificationUtil(appContext);
        notificationUtil.setupChannel();
        notificationUtil.showNotification(schedules);
        Log.d("TAG", "NotificationWorker: Notification Complete");
        return Result.success();
    }

    private List<ScheduleModel> getSchedules() {
        boolean isFilterEnabled = SettingsUtil.getBoolean(appContext, "switch_filter", false);
        boolean isFilterEnabledNotifications = SettingsUtil.getBoolean(appContext, "switch_filter_notifications", false);
        List<ScheduleModel> schedules;
        if (isFilterEnabled && isFilterEnabledNotifications) // Filter Enabled
            schedules = AppDataBase.getDatabase(appContext).scheduleDao().
                    getNotificationSchedules_filter(System.currentTimeMillis(), INTERVAL_TIME);
        else
            schedules = AppDataBase.getDatabase(appContext).scheduleDao().
                    getNotificationSchedules(System.currentTimeMillis(), INTERVAL_TIME);
//        Log.d("TAG", "getSchedules: "+ DateUtil.getDateToString(System.currentTimeMillis(), "MM-dd HH:mm")+" "+
//                DateUtil.getDateToString(System.currentTimeMillis()+INTERVAL_TIME, "MM-dd HH:mm"));
        return schedules;
    }
}
