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

    public GroupViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mGroups = mRepository.getAllGroups();
    }

    LiveData<List<GroupModel>> getGroups() { return mGroups; }

    public void insertGroup(GroupModel group) { mRepository.insertGroup(group); }
    public void insertAllGroups(List<GroupModel> groups) { mRepository.insertAllGroups(groups); }
}
