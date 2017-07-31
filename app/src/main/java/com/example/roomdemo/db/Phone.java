package com.example.roomdemo.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.*;

@Entity(foreignKeys = @ForeignKey(entity = Contact.class, parentColumns = "id", childColumns = "contact_id", onDelete = CASCADE),
        tableName = "phones"
)
public class Phone {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "contact_id")
    public long contactId;

    @ColumnInfo(name = "phone_number")
    public String phoneNumber;

    public String type;
}
