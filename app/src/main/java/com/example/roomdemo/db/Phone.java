package com.example.roomdemo.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Contact.class, parentColumns = "id", childColumns = "contact_id"), tableName = "phones")
public class Phone {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "contact_id")
    public int contactId;

    @ColumnInfo(name = "phone_number")
    public String phoneNumber;

    public String type;
}
