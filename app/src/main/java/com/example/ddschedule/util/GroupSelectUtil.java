package com.example.ddschedule.util;

import android.content.Context;

import com.example.ddschedule.model.GroupModel;

import org.json.JSONException;

import java.security.acl.Group;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupSelectUtil {
//
//    private List<String> mGroups;
//    private List<String> ytbGroups;
//    private List<String> biliGroups;
//
    public static GroupSelectClass GroupSelect(List<String> groups) {
        List<String> ytbGroups = new ArrayList<>();
        List<String> biliGroups = new ArrayList<>();
        for (String str : groups){
            if(str.equals("Hololive_Bilibili") ||
                    str.equals("Nijisanji_Bilibili") ||
                    str.equals("Other_Bilibili")){
                biliGroups.add(str);
            } else {
                ytbGroups.add(str);
            }
        }
        return new GroupSelectClass(ytbGroups, biliGroups);
    }

    public static class GroupSelectClass {
        public List<String> ytbGroups;
        public List<String> biliGroups;

        public GroupSelectClass(List<String> ytbGroups, List<String> biliGroups) {
            this.ytbGroups = ytbGroups;
            this.biliGroups = biliGroups;
        }
    }
}
