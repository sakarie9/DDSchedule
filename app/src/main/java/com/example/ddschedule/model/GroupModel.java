package com.example.ddschedule.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

@Entity(tableName = "group_table")
public class GroupModel implements Comparator<GroupModel> {
    @PrimaryKey
    @NonNull
    private String group_id;
    @ColumnInfo(index = true)
    private String name;
    private String name_jpn;
    private String twitter_id;
    private String twitter_thumbnail_url;
    private String count;

    public Boolean isSelected;

    @Ignore
    public GroupModel(String group_id, String name, String name_jpn, String twitter_id, String twitter_thumbnail_url, String count) {
        this.group_id = group_id;
        this.name = name;
        this.name_jpn = name_jpn;
        this.twitter_id = twitter_id;
        this.twitter_thumbnail_url = twitter_thumbnail_url;
        this.count = count;
        isSelected = false;
    }
    public GroupModel(){
        isSelected = false;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_jpn() {
        return name_jpn;
    }

    public void setName_jpn(String name_jpn) {
        this.name_jpn = name_jpn;
    }

    public String getTwitter_id() {
        return twitter_id;
    }

    public void setTwitter_id(String twitter_id) {
        this.twitter_id = twitter_id;
    }

    public String getTwitter_thumbnail_url() {
        return twitter_thumbnail_url;
    }

    public void setTwitter_thumbnail_url(String twitter_thumbnail_url) {
        this.twitter_thumbnail_url = twitter_thumbnail_url;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

//    @Override
//    public int compareTo(GroupModel gm) {
//        int num = this.isSelected.compareTo(gm.isSelected);
//        if (num == 0){
//            return this.getName().compareTo(gm.getName());
//        }
//        return num;
//    }

    @Override
    public int compare(GroupModel o1, GroupModel o2) {
        //int num = Collator.getInstance().compare(o1.isSelected(),o2.isSelected());
        int num = o2.isSelected.compareTo(o1.isSelected);
        if (num == 0){
            return Collator.getInstance(Locale.JAPANESE).compare(o1.getName(),o2.getName());
        }
        return num;
    }
}
