package com.aqualink.iot.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Device.class}, version = 1,exportSchema = false)
public abstract class DeviceDatabase extends RoomDatabase {
    private static DeviceDatabase instance;
    public abstract DeviceDao deviceDao();
    public static synchronized DeviceDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    DeviceDatabase.class, "device_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private DeviceDao deviceDao;
        private PopulateDbAsyncTask(DeviceDatabase db) {
            deviceDao = db.deviceDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }
}
