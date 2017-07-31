package com.example.roomdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.example.roomdemo.db.AppDb;
import com.example.roomdemo.db.Contact;
import com.example.roomdemo.db.ContactDao;
import com.example.roomdemo.db.Phone;
import com.example.roomdemo.db.PhoneDao;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.completable.CompletableFromAction;
import io.reactivex.schedulers.Schedulers;


public class EditContactActivity extends Activity {

    private static final String EXTRA_CONTACT = Contact.class.getName();
    EditText firstNameText;
    EditText lastNameText;
    RecyclerView recyclerView;
    FloatingActionButton addPhoneFab;
    private Contact contact;
    private ContactDao contactDao;
    private PhoneDao phoneDao;

    public List<Phone> mPhones;


    public static Intent intentFrom(Context context, Contact contact) {
        Intent i = new Intent(context, EditContactActivity.class);
        i.putExtra(EXTRA_CONTACT, contact);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        contactDao = AppDb.instance(this).contactDao();
        phoneDao = AppDb.instance(this).phoneDao();

        firstNameText = findViewById(R.id.et_first_name);
        lastNameText = findViewById(R.id.et_last_name);
        recyclerView = findViewById(R.id.recycler);
        addPhoneFab = findViewById(R.id.fab_add_phone);

        contact = (Contact) getIntent().getSerializableExtra(EXTRA_CONTACT);
        firstNameText.setText(contact.firstName);
        lastNameText.setText(contact.lastName);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addPhoneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Phone phone = new Phone();
                phone.contactId = contact.id;

                new CompletableFromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        phoneDao.insertPhone(phone);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
        });
    }

    @Override
    protected void onResume() {
        contactDao.getContactPhones(contact.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Phone>>() {

                    @Override
                    public void accept(@NonNull List<Phone> phones) throws Exception {
                        mPhones = phones;
                        recyclerView.setAdapter(new ContactPhoneAdapater(phones));
                    }
                });
        super.onResume();
    }

    @Override
    protected void onPause() {
        saveContact();
        savePhones();
        super.onPause();
    }

    private void savePhones() {
        new CompletableFromAction(new Action() {
            @Override
            public void run() throws Exception {
                Phone[] phones = new Phone[mPhones.size()];
                phoneDao.insertPhone(mPhones.toArray(phones));
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    private void saveContact() {
        contact.firstName = firstNameText.getText().toString();
        contact.lastName = lastNameText.getText().toString();

        new CompletableFromAction(new Action() {
            @Override
            public void run() throws Exception {
                AppDb.instance(EditContactActivity.this).contactDao().insertContact(contact);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

}
