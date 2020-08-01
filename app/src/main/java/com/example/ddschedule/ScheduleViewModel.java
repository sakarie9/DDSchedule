package com.example.ddschedule;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ddschedule.model.GroupModel;
import com.example.ddschedule.model.ScheduleModel;

import java.util.List;

public class ScheduleViewModel extends AndroidViewModel {
    private DataRepository mRepository;

    private LiveData<List<GroupModel>> mGroups;
    private LiveData<List<ScheduleModel>> mSchedules;
    private LiveData<List<String>> mSelectedGroupIDs;

    public ScheduleViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mGroups = mRepository.getAllGroups();
        //mSchedules = mRepository.getSchedules();
        mSelectedGroupIDs = mRepository.getSelectedGroupIDs();
    }

    LiveData<List<GroupModel>> getGroups() {
        return mGroups;
    }

    LiveData<List<ScheduleModel>> getSchedules(List<String> groups) {
        mSchedules = mRepository.getSchedules(groups);
        return mSchedules;
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
