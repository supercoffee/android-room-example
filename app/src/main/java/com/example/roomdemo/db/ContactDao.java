package com.example.roomdemo.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ContactDao {

    @Insert
    long insertContact(Contact c);

    @Query("SELECT * FROM contacts WHERE id = :id")
    Contact getContactById(long id);

    @Query("SELECT * FROM phones WHERE contact_id = :contactId")
    List<Phone> getContactPhones(long contactId);


    @Query("SELECT * FROM contacts")
    List<Contact> selectAll();
}
