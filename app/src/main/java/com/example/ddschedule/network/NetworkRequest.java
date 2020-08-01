package com.example.ddschedule.network;

import android.util.Log;

import com.example.ddschedule.model.ScheduleModel;
import com.example.ddschedule.util.DateUtil;
import com.example.ddschedule.util.ListDataUtil;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkRequest {

    private List<String> groups;

    public NetworkRequest(List<String> groups) {
        this.groups = groups;
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public void postData(final NetDataCallback netDataCallback){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        String str = BodyBuilder();
        RequestBody body = RequestBody.create(str, JSON);
        Request request = new Request.Builder()//创建Request 对象。
                .url("https://hiyoko.sonoj.net/f/avtapi/schedule/fetch_curr")
                .post(body)//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                Log.d("kwwl","获取数据成功了");
//                Log.d("kwwl","response.code()=="+response.code());
//                Log.d("kwwl","response.body().string()=="+response.body().string());
                //成功解析成对象，调用接口把集合返回
                String ss = response.body().string();
                //Log.d("NetworkRequest", ss);
//                ScheduleModel schedule = new Gson().fromJson(ss,Bean.class);
//                List<ScheduleModel> data = new Gson().fromJson(ss,ScheduleModel.class);
                Schedules schedules = new Gson().fromJson(ss, Schedules.class);
                //mOnLoadData.success(schedules.getSchedules());
                netDataCallback.callback(schedules.getSchedules());
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                netDataCallback.err(500,e.getMessage());
            }
        });
    }

    private String BodyBuilder(){
        DateUtil dateUtil = new DateUtil();
        long start = dateUtil.getCurTimeLong();
        //long end = start + 172800000L; //172800000ms = 48h
        long end = start + 345600000L;
        String startStr = DateUtil.getDateToString(start, "yyyy-MM-dd HH:mm:ss");
        String endStr = DateUtil.getDateToString(end, "yyyy-MM-dd HH:mm:ss");
        //String groups = "cover,hololivechina,kaguragumi,noripro";
        String groupsStr = String.join(",", groups);
        String str = String.format("{\"filter_state\":\"{\\\"open\\\":true,\\\"selectedGroups\\\":\\\"%s\\\",\\\"following\\\":false,\\\"text\\\":\\\"\\\"}\",\"start\":\"%s\",\"end\":\"%s\"}",
                groupsStr, startStr, endStr);
        //Log.d("BodyBuilder", str);
        return str;
    }

    public interface NetDataCallback {
        void callback(List<ScheduleModel> data);
        void err(int code,String s);
    }

    public static class Schedules {
        private List<ScheduleModel> schedules;

        public Schedules(List<ScheduleModel> schedules) {
            this.schedules = schedules;
        }

        public List<ScheduleModel> getSchedules() {
            return schedules;
        }
    }
}
