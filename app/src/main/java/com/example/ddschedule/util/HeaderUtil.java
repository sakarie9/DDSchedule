package com.example.ddschedule.util;

import android.util.Log;

import com.example.ddschedule.model.ScheduleHeader;
import com.example.ddschedule.model.ScheduleModel;

import java.util.ArrayList;
import java.util.List;

public class HeaderUtil {
    public static List<ScheduleHeader> addHeader(List<ScheduleModel> ss) {
        List<ScheduleHeader> lsh = new ArrayList<>();
        for (int i = 0; i < ss.size(); i++) {
            ScheduleModel s = ss.get(i);
            if (i == 0) {
                lsh.add(new ScheduleHeader(true, getDate(s.getScheduled_start_time())));
                lsh.add(new ScheduleHeader(false, s));
                continue;
            }
            ScheduleHeader headerTmp = lsh.get(lsh.size() - 1);
            if (((ScheduleModel) headerTmp.getObject()).getScheduled_start_time() != s.getScheduled_start_time()) {
                lsh.add(new ScheduleHeader(true, getDate(s.getScheduled_start_time())));
            }
            lsh.add(new ScheduleHeader(false, s));
        }
        return lsh;
    }

    private static String getDate(long stamp){
        return DateUtil.getDateToString(stamp, "MM-dd HH:mm");
    }
}
