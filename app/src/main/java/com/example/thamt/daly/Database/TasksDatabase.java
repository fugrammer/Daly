package com.example.thamt.daly.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.thamt.daly.R;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 1)
public abstract class TasksDatabase extends RoomDatabase {
    private static final Object sLock = new Object();
    private final static List<Task> TASKS = Arrays.asList(
            new Task("101 Apples"), new Task("6 Oranges")
    );
    private static TasksDatabase INSTANCE;

    public static TasksDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        TasksDatabase.class, context.getString(R.string.TASKS_DB_NAME))
                        .allowMainThreadQueries()
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                Executors.newSingleThreadExecutor().execute(
                                        () -> getInstance(context).taskDao().saveAll(TASKS));
                            }
                        })
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract TaskDao taskDao();
}