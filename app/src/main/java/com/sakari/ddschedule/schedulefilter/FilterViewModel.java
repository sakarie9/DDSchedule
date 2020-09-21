package com.sakari.ddschedule.schedulefilter;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sakari.ddschedule.DataRepository;
import com.sakari.ddschedule.model.LiverModel;

import java.util.List;

public class FilterViewModel extends AndroidViewModel {
    private DataRepository mRepository;
    private LiveData<LiverModel> mLivers;

    public FilterViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DataRepository(application);
    }

    public LiveData<List<LiverModel>> getLivers(String group) {
        return mRepository.getLiversByGroup(group);
    }
    public void insertLivers(List<LiverModel> livers) {
        mRepository.insertLivers(livers);
    }

}
