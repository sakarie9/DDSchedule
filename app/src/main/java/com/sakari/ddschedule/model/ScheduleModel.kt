package com.sakari.ddschedule.model

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "schedule_table", primaryKeys = ["video_id", "scheduled_start_time"])
data class ScheduleModel(
        var ch_id: String,
        var ch_type: Int,
        @Nullable
        var groups: String?,
        var groups_name: String?,
        var scheduled_start_time: Long,
        var streamer_id: String,
        var streamer_name: String,
        var streamer_name_en: String?,
        var thumbnail_url: String?,
        var title: String,
        var video_id: String
)