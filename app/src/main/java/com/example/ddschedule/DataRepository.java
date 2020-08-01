package com.example.ddschedule;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.ddschedule.db.AppDataBase;
import com.example.ddschedule.db.GroupDao;
import com.example.ddschedule.db.ScheduleDao;
import com.example.ddschedule.model.GroupModel;
import com.example.ddschedule.model.ScheduleModel;
import com.example.ddschedule.util.DateUtil;

import java.util.List;

public class DataRepository {

    private ScheduleDao mScheduleDao;
    private LiveData<List<ScheduleModel>> mSchedules;

    private GroupDao mGroupDao;
    private LiveData<List<GroupModel>> mAllGroups;
    private LiveData<List<String>> mSelectedGroupIDs;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    DataRepository(Application application) {
        AppDataBase db = AppDataBase.getDatabase(application);

        mScheduleDao = db.scheduleDao();
        //mAllSchedules = mScheduleDao.getSchedules();

        mGroupDao = db.groupDao();
        mAllGroups = mGroupDao.getGroups();
        mSelectedGroupIDs = mGroupDao.getSelectedGroupIDs();
    }

    LiveData<List<ScheduleModel>> getSchedules(List<String> groups) {
        DateUtil dateUtil = new DateUtil();
        if (groups == null || groups.size() == 0)
            mSchedules = mScheduleDao.getAllSchedules(dateUtil.getOffsetTimestampBefore(), dateUtil.getOffsetTimestampAfter());
        else
            mSchedules = mScheduleDao.getSchedules(groups, dateUtil.getOffsetTimestampBefore(), dateUtil.getOffsetTimestampAfter());
        return mSchedules;
    }


    LiveData<List<GroupModel>> getAllGroups() {
        return mAllGroups;
    }

    LiveData<List<String>> getSelectedGroupIDs() {
        return mSelectedGroupIDs;
    }

    void insertSchedules(List<ScheduleModel> schedules) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            mScheduleDao.insertAll(schedules);
        });
    }

    void insertGroup(GroupModel group) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            mGroupDao.insert(group);
        });
    }

    void insertAllGroups(List<GroupModel> groups) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            mGroupDao.insertAll(groups);
        });
    }
}
