package com.sakari.ddschedule.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.sakari.ddschedule.db.AppDataBase;
import com.sakari.ddschedule.model.ScheduleModel;
import com.sakari.ddschedule.util.NotificationUtil;

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
        List<ScheduleModel> schedules = getSchedules(getGroupIDs());
        NotificationUtil notificationUtil = new NotificationUtil(appContext);
        notificationUtil.setupChannel();
        notificationUtil.showNotification(schedules);
        Log.d("TAG", "NotificationWorker: Notification Complete");
        return Result.success();
    }

    private List<String> getGroupIDs() {
        List<String> tmpGroups = AppDataBase.getDatabase(appContext).groupDao().getSelectedGroupIDsNow();
        //Log.d("TAG", "getGroupIDs: "+tmpGroups.toString());
        return tmpGroups;
    }

    private List<ScheduleModel> getSchedules(List<String> groups) {
        List<ScheduleModel> schedules = AppDataBase.getDatabase(appContext).scheduleDao().
                getNotificationSchedules(groups, System.currentTimeMillis(), INTERVAL_TIME);
//        Log.d("TAG", "getSchedules: "+ DateUtil.getDateToString(System.currentTimeMillis(), "MM-dd HH:mm")+" "+
//                DateUtil.getDateToString(System.currentTimeMillis()+INTERVAL_TIME, "MM-dd HH:mm"));
//        Log.d("TAG", "getSchedules: "+schedules.toString());
        return schedules;
    }
}
