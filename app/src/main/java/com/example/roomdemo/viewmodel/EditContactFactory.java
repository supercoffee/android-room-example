package com.example.roomdemo.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.roomdemo.db.ContactDao;
import com.example.roomdemo.db.PhoneDao;


public class EditContactFactory implements ViewModelProvider.Factory {

    private final ContactDao contactDao;
    private final PhoneDao phoneDao;

    public EditContactFactory(ContactDao contactDao, PhoneDao phoneDao) {
        this.contactDao = contactDao;
        this.phoneDao = phoneDao;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EditContact.class)) {
            return (T) new EditContact(contactDao, phoneDao);
        }
        return null;
    }
}
