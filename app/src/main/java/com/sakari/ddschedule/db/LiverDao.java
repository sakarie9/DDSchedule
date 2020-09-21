package com.sakari.ddschedule.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sakari.ddschedule.model.LiverModel;

import java.util.List;

@Dao
public interface LiverDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LiverModel> livers);

    @Query("SELECT * FROM liver_table ORDER BY isBlocked DESC ,name ASC")
    LiveData<List<LiverModel>> getLivers();

    @Query("SELECT * FROM liver_table WHERE groups=:group ORDER BY name ASC")
    LiveData<List<LiverModel>> getLiversByGroup(String group);

    @Query("SELECT COUNT(*) FROM liver_table WHERE groups=:group")
    int getLiverCountByGroup(String group);
}
