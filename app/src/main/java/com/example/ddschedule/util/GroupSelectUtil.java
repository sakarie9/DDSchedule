package com.example.ddschedule.util;

import android.content.Context;

import com.example.ddschedule.model.GroupModel;

import org.json.JSONException;

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

    private List<GroupModel> mGroups;
    private Map<String, GroupModel> mGroupsMap;

    public GroupSelectUtil(Context context, List<GroupModel> mGroups) {
        if (mGroups.size() == 0) {
            //获取社团数据
            try {
                mGroups = JsonLocalUtil.getGroups(context);
//                Collator collator = Collator.getInstance(Locale.JAPANESE);
//                Collections.sort(mGroups);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.mGroups = mGroups;
    }

    public List<String> getSelectedGroupsIDs() {
        List<Boolean> boolList = new ArrayList<>();
        boolList.add(true);
        // JDK1.8提供了lambda表达式， 可以从stuList中过滤出符合条件的结果。
        // 定义结果集
        List<GroupModel> result = null;
        result = mGroups.stream()
                .filter((GroupModel s) -> boolList.contains(s.isSelected()))
                .collect(Collectors.toList());

        return result.stream().map(GroupModel::getGroup_id).collect(Collectors.toList());
    }

    public List<GroupModel> setSelectedGroupIDs(List<String> idList) {

        mGroupsMap = mGroups.stream().collect(
                Collectors.toMap(GroupModel::getGroup_id, GroupModel->GroupModel)
        );

        for (String id:idList) {
            GroupModel gm = mGroupsMap.get(id);
            assert gm != null;
            gm.setSelected(true);
            mGroupsMap.put(id, gm);
        }

        List<GroupModel> list =new ArrayList<>(mGroupsMap.values());

        Collections.sort(list, new GroupModel());

        return list;
    }
}
