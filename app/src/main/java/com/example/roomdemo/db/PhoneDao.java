package com.example.roomdemo.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

@Dao
public interface PhoneDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertPhone(Phone... phones);

    @Delete
    void delete(Phone ... phone);
}
