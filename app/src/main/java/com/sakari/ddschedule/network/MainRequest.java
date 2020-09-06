package com.sakari.ddschedule.network;

import android.content.Context;
import android.util.Log;

import com.sakari.ddschedule.db.AppDataBase;
import com.sakari.ddschedule.model.ScheduleModel;
import com.sakari.ddschedule.util.GroupSelectUtil;

import java.util.ArrayList;
import java.util.List;

import static com.sakari.ddschedule.network.BiliRequest.bili_urls;

public class MainRequest implements YTBRequest.NetDataCallback, BiliRequest.NetDataCallback {

    Boolean YTB_OK = false;
    List<Boolean> Bili_OK = new ArrayList<>();
    int YTB_code;
    int Bili_code;
    String YTB_Str;
    String Bili_Str;
    List<String> ytbGroups;
    List<String> biliGroups;

    MainRequest.NetDataCallback netDataCallback;
    AppDataBase appDataBase;

    public MainRequest(List<String> groups, Context context, final MainRequest.NetDataCallback netDataCallback) {
        this.netDataCallback = netDataCallback;
        appDataBase = AppDataBase.getDatabase(context);

        GroupSelectUtil.GroupSelectClass g = GroupSelectUtil.GroupSelect(groups);
        ytbGroups = g.ytbGroups;
        biliGroups = g.biliGroups;

        YTBRequest req_ytb = new YTBRequest(ytbGroups);
        req_ytb.postData(this);

        BiliRequest req_bili = new BiliRequest();
        for (String bGroup:biliGroups) {
            req_bili.getData(bGroup, bili_urls.get(bGroup), this);
        }
    }

    public interface NetDataCallback {
        void NetCallback();
        void NetErr(int code1, String s1);
    }

    // TODO: Request Callback Rewrite
    @Override
    public void BiliCallback(List<ScheduleModel> schedules) {
        AppDataBase.databaseWriteExecutor.execute(()->{
            appDataBase.scheduleDao().insertAll(schedules);
        });
        Bili_OK.add(true);
        Log.d("TAG", "BiliCallback: OK");
        if (Bili_OK.size() == biliGroups.size() && YTB_OK){
            netDataCallback.NetCallback();
        }
    }

    @Override
    public void BiliErr(int code, String s) {
        Bili_code = code;
        Bili_Str = s;
        netDataCallback.NetErr(code, s);
    }

    @Override
    public void YTBCallback(List<ScheduleModel> schedules) {
        AppDataBase.databaseWriteExecutor.execute(()->{
            appDataBase.scheduleDao().insertAll(schedules);
        });
        YTB_OK = true;
        Log.d("TAG", "YTBCallback: OK");
        if (Bili_OK.size() == biliGroups.size()){
            netDataCallback.NetCallback();
        }
    }

    @Override
    public void YTBErr(int code, String s) {
        YTB_code = code;
        YTB_Str = s;
        netDataCallback.NetErr(code, s);
    }
}
