package com.example.ddschedule;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkManager;

import com.example.ddschedule.model.GroupModel;
import com.example.ddschedule.model.ScheduleModel;
import com.example.ddschedule.worker.NotificationWorker;
import com.example.ddschedule.worker.ScheduleSyncWorker;

import java.util.List;

import static com.example.ddschedule.worker.NotificationWorker.SCHEDULE_NOTIFICATION_WORK_NAME;
import static com.example.ddschedule.worker.ScheduleSyncWorker.SCHEDULE_SYNC_WORK_NAME;
import static java.util.concurrent.TimeUnit.MINUTES;

public class ScheduleViewModel extends AndroidViewModel {
    private DataRepository mRepository;

    private LiveData<List<GroupModel>> mGroups;
    private LiveData<List<ScheduleModel>> mSchedules;
    private LiveData<List<String>> mSelectedGroupIDs;

    private MutableLiveData<List<String>> filterGroups = new MutableLiveData<List<String>>();

    private WorkManager mWorkManager;

    public ScheduleViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mGroups = mRepository.getAllGroups();
        //mSchedules = mRepository.getSchedules();
        mSelectedGroupIDs = mRepository.getSelectedGroupIDs();

        mSchedules = Transformations.switchMap(filterGroups,
                c -> mRepository.getSchedules(c));

        mWorkManager = WorkManager.getInstance(application);

    }

    LiveData<List<GroupModel>> getGroups() {
        return mGroups;
    }

    LiveData<List<ScheduleModel>> getSchedules() {
        return mSchedules;
    }

    void setGroups(List<String> groups) {
        filterGroups.setValue(groups);
    }

    LiveData<List<String>> getSelectedGroupIDs() {
        return mSelectedGroupIDs;
    }

    public void insertGroup(GroupModel group) {
        mRepository.insertGroup(group);
    }

    public void insertAllGroups(List<GroupModel> groups) {
        mRepository.insertAllGroups(groups);
    }

    public void insertSchedules(List<ScheduleModel> schedules) {
        mRepository.insertSchedules(schedules);
    }


    public void startSyncWork() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest build = new PeriodicWorkRequest.Builder(
                ScheduleSyncWorker.class, 30, MINUTES, 5, MINUTES) //每三十分钟同步一次
                //ScheduleSyncWorker.class, 30, MINUTES)
                .setConstraints(constraints)
                .build();
        mWorkManager.enqueueUniquePeriodicWork(SCHEDULE_SYNC_WORK_NAME, ExistingPeriodicWorkPolicy.REPLACE, build);
    }

    public void startWorkNow() {
        mWorkManager.enqueue(OneTimeWorkRequest.from(ScheduleSyncWorker.class));
    }

    public void startNotificationWork() {
        PeriodicWorkRequest build = new PeriodicWorkRequest.Builder(
                NotificationWorker.class, 15, MINUTES, 5, MINUTES) //每十五分钟同步一次
                //NotificationWorker.class, 15, MINUTES)
                .build();
        mWorkManager.enqueueUniquePeriodicWork(SCHEDULE_NOTIFICATION_WORK_NAME, ExistingPeriodicWorkPolicy.REPLACE, build);
    }
}
