package com.example.ddschedule.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.ddschedule.model.ScheduleModel;

import java.util.List;

@Dao
public interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ScheduleModel> groups);

    @Query("DELETE FROM schedule_table")
    void deleteAll();

    @Query("SELECT * FROM schedule_table ORDER BY scheduled_start_time ASC")
    LiveData<List<ScheduleModel>> getSchedules();
}
