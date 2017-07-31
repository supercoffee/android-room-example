package com.example.roomdemo.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.roomdemo.db.ContactDao;


public class EditContactFactory implements ViewModelProvider.Factory {

    private final ContactDao contactDao;
    private final long contactId;

    public EditContactFactory(ContactDao contactDao, long contactId) {
        this.contactDao = contactDao;
        this.contactId = contactId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EditContactViewModel.class)) {
            return (T) new EditContactViewModel(contactDao, contactId);
        }
        return null;
    }
}
