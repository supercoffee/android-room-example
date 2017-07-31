package com.example.roomdemo.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.example.roomdemo.db.Contact;
import com.example.roomdemo.db.ContactDao;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ContactListViewModel extends ViewModel {

    private final ContactDao contactDao;

    public ContactListViewModel(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    public Flowable<List<Contact>> getAllContacts() {
        return contactDao.selectAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Contact> createContact() {
        return Observable.fromCallable(new Callable<Contact>() {
            @Override
            public Contact call() throws Exception {
                Contact c = new Contact();
                c.id = contactDao.insertContact(c);
                return c;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
