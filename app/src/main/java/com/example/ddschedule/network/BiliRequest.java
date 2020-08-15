package com.example.ddschedule.network;

import android.content.Context;
import android.util.Log;

import com.example.ddschedule.DataRepository;
import com.example.ddschedule.model.ScheduleModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class BiliRequest {

    private Context context;
    private DataRepository dataRepository;

    public BiliRequest(Context context) {
        this.context = context;
        dataRepository = new DataRepository(context);
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public void getData(final BiliRequest.NetDataCallback netDataCallback){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        Request request = new Request.Builder()//创建Request 对象。
                .url("https://api.ihateani.me/live")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String str = Objects.requireNonNull(response.body()).string();
                JSONObject jsonObject = null;
                JSONArray liveArray = null;
                JSONArray upcomingArray = null;
                try {
                    jsonObject = new JSONObject(str);
                    liveArray = jsonObject.getJSONArray("live");
                    upcomingArray = jsonObject.getJSONArray("upcoming");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                List<BiliRequest.LiveClass> lives = gson.fromJson(liveArray.toString(), new TypeToken<List<BiliRequest.LiveClass>>(){}.getType());
                List<BiliRequest.LiveClass> upcomings = gson.fromJson(upcomingArray.toString(), new TypeToken<List<BiliRequest.LiveClass>>(){}.getType());
                List<BiliRequest.LiveClass> biliSchedules = new ArrayList<>();
                biliSchedules.addAll(lives);
                biliSchedules.addAll(upcomings);
                List<ScheduleModel> scheduleModels = scheduleConvert(biliSchedules);

                Log.d(TAG, "onResponse: " + scheduleModels);

                dataRepository.insertSchedules(scheduleModels);
                netDataCallback.BiliCallback();
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                netDataCallback.BiliErr(500,e.getMessage());
            }
        });
    }

    public interface NetDataCallback {
        void BiliCallback();
        void BiliErr(int code,String s);
    }

    static class LiveClass {
        public String id;
        public Long room_id;
        public String title;
        public Long startTime;
        public Long channel;
        public String channel_name;
        public String thumbnail;
    }

    // TODO: Hard coded live start time to UTC+8
    List<ScheduleModel> scheduleConvert(List<BiliRequest.LiveClass> bss) {
        List<ScheduleModel> schedules = new ArrayList<>();
        for (BiliRequest.LiveClass bs : bss) {
            ScheduleModel s = new ScheduleModel(
                    String.valueOf(bs.channel),
                    2,
                    "Hololive",
                    "Hololive",
                    (bs.startTime + 28800) * 1000,
                    "",
                    bs.channel_name,
                    "",
                    bs.thumbnail,
                    bs.title,
                    String.valueOf(bs.channel)
            );
            schedules.add(s);
        }
        return schedules;
    }
}
