package com.example.ddschedule.worker;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ddschedule.db.AppDataBase;
import com.example.ddschedule.model.GroupModel;
import com.example.ddschedule.model.ScheduleModel;
import com.example.ddschedule.network.NetworkRequest;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ScheduleSyncWorker extends Worker implements NetworkRequest.NetDataCallback {

    public static final String SCHEDULE_SYNC_WORK_NAME = "schedule_sync_work";

    private Context appContext;

    public ScheduleSyncWorker(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        this.appContext = appContext;
    }

    @NonNull
    @Override
    public Result doWork() {
        requestData(getGroupIDs());
        return Result.success();
    }

    private List<String> getGroupIDs() {
        List<String> tmpGroups = AppDataBase.getDatabase(appContext).groupDao().getSelectedGroupIDsNow();
        //Log.d("TAG", "getGroupIDs: "+tmpGroups.toString());
        return tmpGroups;
    }

    private void requestData(List<String> groups) {
        NetworkRequest http=new NetworkRequest(groups);
        http.postData(this);
    }

    //创建 Handler对象，并关联主线程消息队列
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                List<ScheduleModel> tmpSchedules = (List<ScheduleModel>)msg.obj;
                //Log.d("TAG", "handleMessage: "+(tmpSchedules).toString());
                AppDataBase.databaseWriteExecutor.execute(() -> {
                    AppDataBase.getDatabase(appContext).scheduleDao().insertAll(tmpSchedules);
                    Log.d("TAG", "ScheduleWorker: Sync Complete");
                });
            }
        }
    };

    @Override
    public void callback(List<ScheduleModel> data) {
        Message msg = Message.obtain();
        msg.what=1;
        msg.obj=data;
        mHandler.sendMessage(msg);
    }

    @Override
    public void err(int code, String s) {

    }
}
