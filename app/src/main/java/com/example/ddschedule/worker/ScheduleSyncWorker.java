package com.example.ddschedule.worker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.concurrent.callback.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;

import com.example.ddschedule.db.AppDataBase;
import com.example.ddschedule.model.ScheduleModel;
import com.example.ddschedule.network.MainRequest;
import com.google.common.util.concurrent.ListenableFuture;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.xml.transform.Result;

public class ScheduleSyncWorker extends ListenableWorker implements MainRequest.NetDataCallback {

    public static final String SCHEDULE_SYNC_WORK_NAME = "schedule_sync_work";

    private Context appContext;

    @SuppressLint("RestrictedApi")
    SettableFuture<Result> result;

    public ScheduleSyncWorker(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        this.appContext = appContext;
    }

    @SuppressLint("RestrictedApi")
    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        result = SettableFuture.create();
        requestData(getGroupIDs());
        return result;
    }

    private List<String> getGroupIDs() {
        List<String> tmpGroups = AppDataBase.getDatabase(appContext).groupDao().getSelectedGroupIDsNow();
        //Log.d("TAG", "getGroupIDs: "+tmpGroups.toString());
        return tmpGroups;
    }

    private void requestData(List<String> groups) {
        MainRequest req = new MainRequest(groups, appContext, this);
    }

    //创建 Handler对象，并关联主线程消息队列
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        @SuppressLint("RestrictedApi")
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                result.set(Result.success());
            }
        }
    };

    @Override
    public void NetCallback() {
        Message msg = Message.obtain();
        msg.what=1;
        mHandler.sendMessage(msg);
    }

    @Override
    public void NetErr(int code, String s) {

    }
}
