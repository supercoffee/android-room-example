package com.example.roomdemo.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Contact.class, Phone.class}, version = 1)
public abstract class AppDb extends RoomDatabase {

    private static AppDb instance;

    public abstract ContactDao contactDao();

    public abstract PhoneDao phoneDao();

    public static AppDb instance(Context c)
    {
        if (instance == null) {
            instance = Room.databaseBuilder(c.getApplicationContext(), AppDb.class, "app-db")
                            .build();
        }
        return instance;
    }
}
