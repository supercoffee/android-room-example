package com.example.roomdemo.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.example.roomdemo.db.Contact;
import com.example.roomdemo.db.ContactDao;
import com.example.roomdemo.db.Phone;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.internal.operators.completable.CompletableFromAction;
import io.reactivex.schedulers.Schedulers;


public class EditContactViewModel extends ViewModel {

    private final ContactDao contactDao;
    private final long contactId;

    public EditContactViewModel(ContactDao contactDao, long contactId) {
        this.contactDao = contactDao;
        this.contactId = contactId;
    }

    public Flowable<Contact> getContact() {
        return contactDao.getContactById(contactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<Phone>> getPhones() {
        return contactDao.getContactPhones(contactId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

    public Disposable saveContact(final Contact contact) {

        return new CompletableFromAction(new Action() {
            @Override
            public void run() throws Exception {
                contactDao.insertContact(contact);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void savePhones(final List<Phone> mPhones) {
        new CompletableFromAction(new Action() {
            @Override
            public void run() throws Exception {
                Phone[] phones = new Phone[mPhones.size()];
                contactDao.insertPhone(mPhones.toArray(phones));
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Disposable addPhone() {
        final Phone phone = new Phone();
        phone.contactId = contactId;

        return new CompletableFromAction(new Action() {
            @Override
            public void run() throws Exception {
                contactDao.insertPhone(phone);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deletePhone(final Phone phone) {
        new CompletableFromAction(new Action() {
            @Override
            public void run() throws Exception {
                contactDao.deletePhone(phone);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteContact() {
        new CompletableFromAction(new Action() {
            @Override
            public void run() throws Exception {
                Contact toDelete = new Contact();
                toDelete.id = contactId;
                contactDao.deleteContact(toDelete);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

}
