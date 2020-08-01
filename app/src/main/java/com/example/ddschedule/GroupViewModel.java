package com.example.ddschedule;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ddschedule.model.GroupModel;
import com.example.ddschedule.model.ScheduleModel;

import java.util.List;

public class GroupViewModel extends AndroidViewModel {

    private DataRepository mRepository;

    private LiveData<List<GroupModel>> mGroups;
    private LiveData<List<ScheduleModel>> mSchedules;

    public GroupViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mGroups = mRepository.getAllGroups();
        mSchedules = mRepository.getAllSchedules();
    }

    LiveData<List<GroupModel>> getGroups() { return mGroups; }
    LiveData<List<ScheduleModel>> getSchedules() { return mSchedules; }

    public void insertGroups(List<GroupModel> groups) { mRepository.insertGroups(groups); }
    public void insertSchedules(List<ScheduleModel> schedules) { mRepository.insertSchedules(schedules); }
}
