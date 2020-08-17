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

@Database(entities = {GroupModel.class, ScheduleModel.class}, version = 2)
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
                                        //插入预置的Bilibili Groups数据
                                        database.groupDao().insertAll(generateBiliGroups());
                                    });
                                }
                            })
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static List<GroupModel> generateBiliGroups(){
        GroupModel g_holo = new GroupModel(
                "Hololive_Bilibili",
                "Hololive_Bilibili",
                "",
                "",
                "https://i0.hdslb.com/bfs/face/52f316ed4b89f48f3fea7cc165585c04c32f32df.jpg",
                "");

        GroupModel g_niji = new GroupModel(
                "Nijisanji_Bilibili",
                "Nijisanji_Bilibili",
                "",
                "",
                "https://i1.hdslb.com/bfs/face/ec24bb376f1448219295eb80db2a537b0c4d87bd.jpg",
                "");

        GroupModel g_other = new GroupModel(
                "Other_Bilibili",
                "Other_Bilibili",
                "",
                "",
                "",
                "");

        List<GroupModel> groupModels = new ArrayList<>();
        groupModels.add(g_holo);
        groupModels.add(g_niji);
        groupModels.add(g_other);
        return groupModels;
    }

}
