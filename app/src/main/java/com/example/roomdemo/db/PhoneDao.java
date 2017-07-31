package com.example.roomdemo.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import io.reactivex.Flowable;

@Dao
public interface PhoneDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertPhone(Phone... phones);
}
