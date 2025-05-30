package com.daclink.citypulse;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.daclink.citypulse.database.Activities;
import com.daclink.citypulse.database.ActivitiesDAO;
import com.daclink.citypulse.model.CachedEvent;
import com.daclink.citypulse.model.CachedEventDao;

@Database(entities = {User.class, CachedEvent.class, Activities.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CachedEventDao cachedEventDao();
    public abstract ActivitiesDAO activitiesDAO();
    public abstract UserDao userDao();

    private static final String DATABASE_NAME = "citypulse_database";
    private static volatile AppDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                    databaseWriteExecutor.execute(() -> {
                        UserDao dao = instance.userDao();
                        User existingAdmin = dao.getUserByUsername("admin2");
                        if (existingAdmin == null) {
                            User admin2 = new User("admin2", "admin2", true);
                            dao.insert(admin2);
                            Log.d("AppDatabase", "Inserted admin2 into DB.");
                        } else {
                            Log.d("AppDatabase", "admin2 already exists.");
                        }
                    });
                }
            }
        }
        return instance;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                UserDao dao = instance.userDao();
                try {
                    User testUserCheck = dao.getUserByUsername("testuser1");
                    if (testUserCheck == null) {
                        User testUser1 = new User("testuser1", "testuser1", false);
                        dao.insert(testUser1);
                        Log.d("AppDatabaseCallback", "Inserted testuser1");
                    } else {
                        Log.d("AppDatabaseCallback", "testuser1 already exists.");
                    }
                    User adminCheck = dao.getUserByUsername("admin2");
                    if (adminCheck == null) {
                        User admin2 = new User("admin2", "admin2", true);
                        dao.insert(admin2);
                        Log.d("AppDatabaseCallback", "Inserted admin2");
                    } else {
                        Log.d("AppDatabaseCallback", "admin2 already exists.");
                    }
                } catch (Exception e) {
                    Log.e("AppDatabaseCallback", "Error during pre-population", e);
                }
            });
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
}
