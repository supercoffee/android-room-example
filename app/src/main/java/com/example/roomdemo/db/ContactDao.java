package com.example.roomdemo.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertContact(Contact c);

    @Query("SELECT * FROM contacts WHERE id = :id")
    Flowable<Contact> getContactById(long id);

    @Query("SELECT * FROM phones WHERE contact_id = :contactId")
    Flowable<List<Phone>> getContactPhones(long contactId);


    @Query("SELECT * FROM contacts")
    Flowable<List<Contact>> selectAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertPhone(Phone ... phones);

    @Delete
    void deletePhone(Phone phone);

    @Delete
    void deleteContact(Contact ... toDelete);
}
