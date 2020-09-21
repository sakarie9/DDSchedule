package com.sakari.ddschedule;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.sakari.ddschedule.db.AppDataBase;
import com.sakari.ddschedule.db.GroupDao;
import com.sakari.ddschedule.db.LiverDao;
import com.sakari.ddschedule.db.ScheduleDao;
import com.sakari.ddschedule.model.GroupModel;
import com.sakari.ddschedule.model.LiverModel;
import com.sakari.ddschedule.model.ScheduleModel;
import com.sakari.ddschedule.network.LiverRequestMain;
import com.sakari.ddschedule.util.DateUtil;
import com.sakari.ddschedule.util.SettingsUtil;

import java.util.List;

public class DataRepository {

    private ScheduleDao mScheduleDao;
    private LiveData<List<ScheduleModel>> mSchedules;

    private GroupDao mGroupDao;
    private LiveData<List<GroupModel>> mAllGroups;
    private LiveData<List<String>> mSelectedGroupIDs;

    private LiverDao mLiverDao;
    private LiveData<List<LiverModel>> mLivers;

    Context context;

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
        mLiverDao = db.liverDao();
        context = application;
    }

    // Schedules
    public LiveData<List<ScheduleModel>> getSchedules(List<String> groups) {
        DateUtil dateUtil = new DateUtil();
        boolean isFilterEnabled = SettingsUtil.getBoolean(context, "switch_filter", false);
        boolean isFilterEnabledSchedule = SettingsUtil.getBoolean(context, "switch_filter_schedule", false);
        if (groups == null || groups.size() == 0)
            mSchedules = mScheduleDao.getAllSchedules(dateUtil.getOffsetTimestampBefore(), dateUtil.getOffsetTimestampAfter());
        else if (isFilterEnabled && isFilterEnabledSchedule) // Filter Enabled
            mSchedules = mScheduleDao.getSchedules_filter(dateUtil.getOffsetTimestampBefore(), dateUtil.getOffsetTimestampAfter());
        else
            mSchedules = mScheduleDao.getSchedules(dateUtil.getOffsetTimestampBefore(), dateUtil.getOffsetTimestampAfter());
        return mSchedules;
    }
    public void insertSchedules(List<ScheduleModel> schedules) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            mScheduleDao.insertAll(schedules);
        });
    }

    // Groups
    public LiveData<List<GroupModel>> getAllGroups() {
        return mAllGroups;
    }
    public LiveData<List<String>> getSelectedGroupIDs() {
        return mSelectedGroupIDs;
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

    // Livers
    public void insertLivers(List<LiverModel> livers){
        AppDataBase.databaseWriteExecutor.execute(() -> {
            mLiverDao.insertAll(livers);
        });
    }
    public LiveData<List<LiverModel>> getLiversByGroup(String group){
        int count;
        try {
            count = mLiverDao.getLiverCountByGroup(group);
        } catch (NullPointerException e) {
            count = 0;
        }
        LiverRequestMain liverRequestMain = new LiverRequestMain();
        liverRequestMain.run(group, count, context);
        mLivers = mLiverDao.getLiversByGroup(group);
        return mLivers;
    }
}
