package com.sakari.ddschedule.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sakari.ddschedule.model.LiverModel;
import com.sakari.ddschedule.model.ScheduleModel;
import com.sakari.ddschedule.util.DateUtil;

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
import okhttp3.RequestBody;
import okhttp3.Response;

public class LiverRequest {
    private String group;
    private int count;
    private List<LiverModel> liverModelList;

    public LiverRequest(String group, int count) {
        this.group = group;
        this.count = count;
    }

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public void postData(int page, final LiverRequest.NetDataCallback netDataCallback){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        String str = BodyBuilder(page);
        RequestBody body = RequestBody.create(str, JSON);
        Request request = new Request.Builder()//创建Request 对象。
                .url("https://hiyoko.sonoj.net/f-slow/avtapi/search/streamer/fetchv3")
                .post(body)//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String str = Objects.requireNonNull(response.body()).string();
                Log.d("LiverRequest", "onResponse: "+str);
                JSONObject jsonObject;
                JSONArray liverArray = null;
                boolean isValidNext = false;
                try {
                    jsonObject = new JSONObject(str);
                    if (count == jsonObject.getInt("totalCount")) {
                        netDataCallback.LiverLatest();
                        return;
                    } else {
                        isValidNext = jsonObject.getBoolean("isValidNext");
                        liverArray = jsonObject.getJSONArray("result");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                if (liverArray != null){
                    liverModelList = gson.fromJson(liverArray.toString(), new TypeToken<List<LiverModel>>(){}.getType());
                }
                if (isValidNext) {
                    netDataCallback.LiverNextPage(liverModelList);
                } else {
                    netDataCallback.LiverCallback(liverModelList);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                netDataCallback.LiverErr(500, e.getMessage());
            }
        });
    }

    private String BodyBuilder(int page){
        String str = String.format("{\"filter_state\": \"{\\\"selectedGroups\\\":\\\"%s\\\",\\\"text\\\":\\\"\\\",\\\"inc_old_group\\\":false,\\\"retired\\\":\\\"off\\\",\\\"following\\\":false,\\\"notifications\\\":false}\",\"page\": %s,\"sort_by\": \"subscriber_count\"}",
                group, page);
        Log.d("BodyBuilder", str);
        return str;
    }

    public interface NetDataCallback {
        void LiverCallback(List<LiverModel> livers);
        void LiverErr(int code,String s);
        void LiverLatest();
        void LiverNextPage(List<LiverModel> livers);
    }

}
