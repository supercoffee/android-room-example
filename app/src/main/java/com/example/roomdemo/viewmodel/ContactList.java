package com.example.roomdemo.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.example.roomdemo.db.Contact;
import com.example.roomdemo.db.ContactDao;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ContactList extends ViewModel {

    private final ContactDao contactDao;

    public ContactList(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    public Flowable<List<Contact>> getAllContacts() {
        return contactDao.selectAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}