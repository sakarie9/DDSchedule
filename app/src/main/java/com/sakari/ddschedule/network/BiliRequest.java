package com.sakari.ddschedule.network;

import com.sakari.ddschedule.model.ScheduleModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BiliRequest {

    public final static Map<String, String> bili_urls = new HashMap<String, String>(){{
        put("Hololive_Bilibili", "https://api.ihateani.me/live");
        put("Nijisanji_Bilibili", "https://api.ihateani.me/nijisanji/live");
        put("Other_Bilibili", "https://api.ihateani.me/other/upcoming");
    }};

    public BiliRequest() {
    }

    public void getData(String groupName, String url, final BiliRequest.NetDataCallback netDataCallback){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        Request request = new Request.Builder()//创建Request 对象。
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String str = Objects.requireNonNull(response.body()).string();
                //Log.d("TAG", "biliResponse: "+str);
                JSONObject jsonObject;
                JSONArray liveArray = null;
                JSONArray upcomingArray = null;
                try {
                    jsonObject = new JSONObject(str);
                    liveArray = jsonObject.getJSONArray("live");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject = new JSONObject(str);
                    upcomingArray = jsonObject.getJSONArray("upcoming");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                List<BiliRequest.LiveClass> biliSchedules = new ArrayList<>();
                if (liveArray != null){
                    List<BiliRequest.LiveClass> lives = gson.fromJson(liveArray.toString(), new TypeToken<List<BiliRequest.LiveClass>>(){}.getType());
                    biliSchedules.addAll(lives);
                }
                if (upcomingArray != null){
                    List<BiliRequest.LiveClass> upcomings = gson.fromJson(upcomingArray.toString(), new TypeToken<List<BiliRequest.LiveClass>>(){}.getType());
                    biliSchedules.addAll(upcomings);
                }

                List<ScheduleModel> scheduleModels = scheduleConvert(groupName, biliSchedules);

                //Log.d(TAG, "onResponse: " + scheduleModels);

                //dataRepository.insertSchedules(scheduleModels);
                netDataCallback.BiliCallback(scheduleModels);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                netDataCallback.BiliErr(500,e.getMessage());
            }
        });
    }

    public interface NetDataCallback {
        void BiliCallback(List<ScheduleModel> scheduleModels);
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

    List<ScheduleModel> scheduleConvert(String groupName, List<BiliRequest.LiveClass> bss) {
        List<ScheduleModel> schedules = new ArrayList<>();
        for (BiliRequest.LiveClass bs : bss) {
            ScheduleModel s = new ScheduleModel(
                    String.valueOf(bs.channel),
                    2,
                    groupName,
                    groupName,
                    (bs.startTime) * 1000,
                    "",
                    bs.channel_name,
                    "",
                    bs.thumbnail,
                    bs.title,
                    String.valueOf(bs.room_id)
            );
            schedules.add(s);
        }
        return schedules;
    }
}
