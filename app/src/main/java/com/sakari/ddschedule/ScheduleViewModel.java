package com.sakari.ddschedule;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.sakari.ddschedule.model.GroupModel;
import com.sakari.ddschedule.model.ScheduleModel;
import com.sakari.ddschedule.worker.NotificationWorker;
import com.sakari.ddschedule.worker.ScheduleSyncWorker;

import java.util.List;

import static com.sakari.ddschedule.worker.NotificationWorker.SCHEDULE_NOTIFICATION_WORK_NAME;
import static com.sakari.ddschedule.worker.ScheduleSyncWorker.SCHEDULE_SYNC_WORK_NAME;
import static java.util.concurrent.TimeUnit.MINUTES;

public class ScheduleViewModel extends AndroidViewModel {
    private DataRepository mRepository;

    private LiveData<List<GroupModel>> mGroups;
    private LiveData<List<ScheduleModel>> mSchedules;
    private LiveData<List<String>> mSelectedGroupIDs;

    private MutableLiveData<List<String>> filterGroups = new MutableLiveData<>();

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
}
