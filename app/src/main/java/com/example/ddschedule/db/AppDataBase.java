package com.example.ddschedule.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.ddschedule.model.GroupModel;
import com.example.ddschedule.model.ScheduleModel;
import com.example.ddschedule.util.JsonLocalUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {GroupModel.class, ScheduleModel.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract GroupDao groupDao();
    public abstract ScheduleDao scheduleDao();

    public static final String DATABASE_NAME = "basic-schedule_database-db";

    private static volatile AppDataBase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class, DATABASE_NAME)
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    databaseWriteExecutor.execute(()->{
                                        AppDataBase database = AppDataBase.getDatabase(context);
                                        List<GroupModel> groups = new ArrayList<>();
                                        try {
                                            groups = JsonLocalUtil.getGroups(context);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        database.groupDao().insertAll(groups);

                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
