package com.example.roomdemo.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ContactDao {

    @Insert
    void insertContact(Contact... c);

    @Query("SELECT * FROM contacts WHERE id = :id")
    Contact getContactById(int id);

    @Query("SELECT * FROM phones WHERE contact_id = :contactId")
    List<Phone> getContactPhones(int contactId);


}
