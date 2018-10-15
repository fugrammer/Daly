package com.example.thamt.daly.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.thamt.daly.R;

@Database(entities = {Task.class}, version = 5)
@TypeConverters({DateTimeTypeConverter.class})
public abstract class TasksDatabase extends RoomDatabase {
    private static final Object sLock = new Object();

    private static TasksDatabase INSTANCE;

    public static TasksDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        TasksDatabase.class, context.getString(R.string.TASKS_DB_NAME))
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract TaskDao taskDao();
}
