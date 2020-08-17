package com.example.ddschedule.network;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.example.ddschedule.network.BiliRequest.bili_urls;

public class MainRequest implements YTBRequest.NetDataCallback, BiliRequest.NetDataCallback {

    Boolean YTB_OK = false;
    List<Boolean> Bili_OK = new ArrayList<>();
    int YTB_code;
    int Bili_code;
    String YTB_Str;
    String Bili_Str;

    MainRequest.NetDataCallback netDataCallback;

    public MainRequest(List<String> groups, Context context, final MainRequest.NetDataCallback netDataCallback) {
        this.netDataCallback = netDataCallback;
        YTBRequest req_ytb = new YTBRequest(groups, context);
        req_ytb.postData(this);
        BiliRequest req_bili = new BiliRequest(context);
        bili_urls.forEach((k,v)->{
            req_bili.getData(k, v, this);
        });
//        while(true){
//            if (Bili_OK.size() == 3 && YTB_OK) {
//                netDataCallback.NetCallback();
//                break;
//            }
//        }
    }

    public interface NetDataCallback {
        void NetCallback();
        void NetErr(int code1, String s1);
    }

    @Override
    public void BiliCallback() {
        Bili_OK.add(true);
        if (Bili_OK.size() == 3 && YTB_OK){
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
    public void YTBCallback() {
        YTB_OK = true;
        if (Bili_OK.size() == 3){
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
