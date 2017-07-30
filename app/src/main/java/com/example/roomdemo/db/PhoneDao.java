package com.example.roomdemo.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import java.util.List;

@Dao
public interface PhoneDao {

    @Insert
    long insertPhone(Phone phones);
}
