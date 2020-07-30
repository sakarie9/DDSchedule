package com.example.ddschedule.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ListDataUtil {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    String PREFERENCE_NAME = "GroupsList";
    String KEY_MAME = "GroupsListJson";

    public ListDataUtil(Context mContext) {
        preferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * 保存List
     * @param datalist
     */
    public <T> void setDataList(List<T> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(KEY_MAME, strJson);
        editor.commit();

    }

    /**
     * 获取List
     * @return
     */
    public <T> List<T> getDataList() {
        List<T> datalist=new ArrayList<T>();
        String strJson = preferences.getString(KEY_MAME, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return datalist;

    }

}
