package com.example.ddschedule.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.ddschedule.model.GroupModel;

import java.util.List;

@Dao
public interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GroupModel group);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<GroupModel> groups);

    @Query("DELETE FROM group_table")
    void deleteAll();

    @Query("SELECT * FROM group_table ORDER BY isSelected DESC ,name ASC")
    LiveData<List<GroupModel>> getGroups();

    @Query("SELECT group_id FROM group_table WHERE isSelected = 1")
    LiveData<List<String>> getSelectedGroupIDs();
}
