package com.example.ddschedule.network;

import android.util.Log;

import com.example.ddschedule.model.ScheduleModel;
import com.example.ddschedule.model.Schedules;
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

    public NetworkRequest() { }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public void postData(final NetDataCallback netDataCallback){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        String str = "{\"filter_state\":\"{\\\"open\\\":true,\\\"selectedGroups\\\":\\\"cover,hololivechina,kaguragumi,noripro\\\",\\\"following\\\":false,\\\"text\\\":\\\"\\\"}\",\"start\":\"2020-07-27 18:43:15\",\"end\":\"2020-08-03 06:00:00\"}";
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
                Log.d("NetworkRequest", ss);
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
//    //数据的获取接口
//    public interface onLoadData{
//        void success(List<ScheduleModel> data);
//        void failure();
//    }
//    private onLoadData mOnLoadData;
//
//    public void OKHTTP(onLoadData onLoadData) {
//        mOnLoadData = onLoadData;
//    }
    public interface NetDataCallback {
        void callback(List<ScheduleModel> data);
        void err(int code,String s);
    }
}
