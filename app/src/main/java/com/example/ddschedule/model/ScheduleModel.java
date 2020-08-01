package com.example.ddschedule.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "schedule_table", primaryKeys = {"streamer_id", "scheduled_start_time"})
public class ScheduleModel {
    private String ch_id;
    private int ch_type;
    private String groups;
    private String groups_name;
    @ColumnInfo(index = true)
    @NonNull
    private long scheduled_start_time;
    @NonNull
    private String streamer_id;
    private String streamer_name;
    private String streamer_name_en;
    private String thumbnail_url;
    private String title;
    private String video_id;

    public ScheduleModel(String ch_id, int ch_type, String groups, String groups_name, long scheduled_start_time, String streamer_id, String streamer_name, String streamer_name_en, String thumbnail_url, String title, String video_id) {
        this.ch_id = ch_id;
        this.ch_type = ch_type;
        this.groups = groups;
        this.groups_name = groups_name;
        this.scheduled_start_time = scheduled_start_time;
        this.streamer_id = streamer_id;
        this.streamer_name = streamer_name;
        this.streamer_name_en = streamer_name_en;
        this.thumbnail_url = thumbnail_url;
        this.title = title;
        this.video_id = video_id;
    }

    public String getCh_id() {
        return ch_id;
    }

    public void setCh_id(String ch_id) {
        this.ch_id = ch_id;
    }

    public int getCh_type() {
        return ch_type;
    }

    public void setCh_type(int ch_type) {
        this.ch_type = ch_type;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getGroups_name() {
        return groups_name;
    }

    public void setGroups_name(String groups_name) {
        this.groups_name = groups_name;
    }

    public long getScheduled_start_time() {
        return scheduled_start_time;
    }

    public void setScheduled_start_time(long scheduled_start_time) {
        this.scheduled_start_time = scheduled_start_time;
    }

    public String getStreamer_id() {
        return streamer_id;
    }

    public void setStreamer_id(String streamer_id) {
        this.streamer_id = streamer_id;
    }

    public String getStreamer_name() {
        return streamer_name;
    }

    public void setStreamer_name(String streamer_name) {
        this.streamer_name = streamer_name;
    }

    public String getStreamer_name_en() {
        return streamer_name_en;
    }

    public void setStreamer_name_en(String streamer_name_en) {
        this.streamer_name_en = streamer_name_en;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}
