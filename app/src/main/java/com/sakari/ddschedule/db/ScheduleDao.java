package com.sakari.ddschedule.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sakari.ddschedule.model.ScheduleModel;

import java.util.List;

@Dao
public interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ScheduleModel> schedules);

    @Query("DELETE FROM schedule_table")
    void deleteAll();

    @Query("SELECT * FROM schedule_table " +
            "WHERE groups IN (:groups) AND scheduled_start_time BETWEEN :start_timestamp AND :end_timestamp " +
            "ORDER BY scheduled_start_time ASC")
    LiveData<List<ScheduleModel>> getSchedules(List<String> groups, long start_timestamp, long end_timestamp);

    @Query("SELECT * FROM schedule_table " +
            "WHERE scheduled_start_time BETWEEN :start_timestamp AND :end_timestamp " +
            "ORDER BY scheduled_start_time ASC")
    LiveData<List<ScheduleModel>> getAllSchedules(long start_timestamp, long end_timestamp);

    @Query("SELECT * FROM schedule_table " +
            "WHERE groups IN (:groups) " +
            "AND ((scheduled_start_time - :now_time) >= 0) " +
            "AND ((scheduled_start_time - :now_time) <= :interval_time) " +
            "ORDER BY scheduled_start_time ASC")
    List<ScheduleModel> getNotificationSchedules(List<String> groups, long now_time, long interval_time);
}
