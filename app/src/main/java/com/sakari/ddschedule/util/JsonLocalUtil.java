package com.sakari.ddschedule.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.sakari.ddschedule.model.GroupModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class JsonLocalUtil {
    public static String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static List<GroupModel> getGroups(Context context) throws JSONException {
        String JsonData = getJson(context, "groups.json");//获取assets目录下的json文件数据
        JSONObject jsonObject = new JSONObject(JsonData);
        JSONArray jsonArray = jsonObject.getJSONArray("groups");
        Gson gson = new Gson();
        List<GroupModel> list = gson.fromJson(jsonArray.toString(), new TypeToken<List<GroupModel>>(){}.getType());
        return list;
    }
}
