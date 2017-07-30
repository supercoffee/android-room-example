package com.example.roomdemo.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao
public interface PhoneDao {

    @Insert
    void insertPhone(Phone... phones);
}
