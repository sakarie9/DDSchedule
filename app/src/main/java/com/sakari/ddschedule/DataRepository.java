package com.sakari.ddschedule;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.sakari.ddschedule.db.AppDataBase;
import com.sakari.ddschedule.db.GroupDao;
import com.sakari.ddschedule.db.ScheduleDao;
import com.sakari.ddschedule.model.GroupModel;
import com.sakari.ddschedule.model.ScheduleModel;
import com.sakari.ddschedule.util.DateUtil;

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
    public DataRepository(Context application) {
        AppDataBase db = AppDataBase.getDatabase(application);

        mScheduleDao = db.scheduleDao();
        //mAllSchedules = mScheduleDao.getSchedules();

        mGroupDao = db.groupDao();
        mAllGroups = mGroupDao.getGroups();
        mSelectedGroupIDs = mGroupDao.getSelectedGroupIDs();
    }

    public LiveData<List<ScheduleModel>> getSchedules(List<String> groups) {
        DateUtil dateUtil = new DateUtil();
        if (groups == null || groups.size() == 0)
            mSchedules = mScheduleDao.getAllSchedules(dateUtil.getOffsetTimestampBefore(), dateUtil.getOffsetTimestampAfter());
        else
            mSchedules = mScheduleDao.getSchedules(groups, dateUtil.getOffsetTimestampBefore(), dateUtil.getOffsetTimestampAfter());
        return mSchedules;
    }


    public LiveData<List<GroupModel>> getAllGroups() {
        return mAllGroups;
    }

    public LiveData<List<String>> getSelectedGroupIDs() {
        return mSelectedGroupIDs;
    }

    public void insertSchedules(List<ScheduleModel> schedules) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            mScheduleDao.insertAll(schedules);
        });
    }

    public void insertGroup(GroupModel group) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            mGroupDao.insert(group);
        });
    }

    public void insertAllGroups(List<GroupModel> groups) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            mGroupDao.insertAll(groups);
        });
    }
}
