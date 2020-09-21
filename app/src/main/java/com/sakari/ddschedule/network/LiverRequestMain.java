package com.sakari.ddschedule.network;

import android.content.Context;
import android.util.Log;

import com.sakari.ddschedule.db.AppDataBase;
import com.sakari.ddschedule.model.LiverModel;

import java.util.ArrayList;
import java.util.List;

public class LiverRequestMain implements LiverRequest.NetDataCallback {
    private static final String TAG = "LiverRequest";
    private int page = 0;
    private List<LiverModel> livers = new ArrayList<>();
    private LiverRequest liverRequest;
    AppDataBase appDataBase;

    public void run(String group, int count, Context context){
        liverRequest = new LiverRequest(group, count);
        liverRequest.postData(page,this);
        appDataBase = AppDataBase.getDatabase(context);
    }

    @Override
    public void LiverCallback(List<LiverModel> livers) {
        Log.d(TAG, "LiverCallback: ");
        this.livers.addAll(livers);
        AppDataBase.databaseWriteExecutor.execute(()->{
            appDataBase.liverDao().insertAll(this.livers);
        });
    }

    @Override
    public void LiverErr(int code, String s) {
        Log.d(TAG, "LiverErr: "+s);
    }

    @Override
    public void LiverLatest() {
        Log.d(TAG, "LiverLatest: ");
    }

    @Override
    public void LiverNextPage(List<LiverModel> livers) {
        Log.d(TAG, "LiverNextPage: ");
        this.livers.addAll(livers);
        page = page + 1;
        liverRequest.postData(page, this);
    }
}
