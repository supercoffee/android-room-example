package com.example.roomdemo.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.roomdemo.db.ContactDao;

public class ContactListFactory implements ViewModelProvider.Factory {

    private final ContactDao contactDao;

    public ContactListFactory(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ContactListViewModel.class)) {
            return (T) new ContactListViewModel(contactDao);
        }
        throw new RuntimeException(String.format("Unable to instantiate viewmodel %s", modelClass.getName()));
    }
}
