package com.example.roomdemo;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.example.roomdemo.db.AppDb;
import com.example.roomdemo.db.Contact;
import com.example.roomdemo.viewmodel.EditContact;
import com.example.roomdemo.viewmodel.EditContactFactory;


public class EditContactActivity extends AppCompatActivity {

    private static final String EXTRA_CONTACT = Contact.class.getName();
    EditText firstNameText;
    EditText lastNameText;
    RecyclerView recyclerView;
    FloatingActionButton addPhoneFab;

    // TODO: get rid of this cached value
    private Contact contact;

    private EditContact editContactModel;

    private ContactPhoneAdapater adapter;

    public static Intent intentFrom(Context context, Contact contact) {
        Intent i = new Intent(context, EditContactActivity.class);
        i.putExtra(EXTRA_CONTACT, contact);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        firstNameText = findViewById(R.id.et_first_name);
        lastNameText = findViewById(R.id.et_last_name);
        recyclerView = findViewById(R.id.recycler);
        addPhoneFab = findViewById(R.id.fab_add_phone);

        contact = (Contact) getIntent().getSerializableExtra(EXTRA_CONTACT);
        if (contact == null) {
            throw new RuntimeException("Activity must be instantiated with non-null instance of Contact.");
        }
        firstNameText.setText(contact.firstName);
        lastNameText.setText(contact.lastName);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addPhoneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editContactModel.addPhone();
            }
        });

        AppDb db = AppDb.instance(this);
        EditContactFactory factory = new EditContactFactory(db.contactDao(), contact.id);
        editContactModel = ViewModelProviders.of(this, factory).get(EditContact.class);
        adapter = new ContactPhoneAdapater(editContactModel);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        saveContact();
        savePhones();
        super.onPause();
    }

    private void savePhones() {
        adapter.saveItems();
    }

    private void saveContact() {
        contact.firstName = firstNameText.getText().toString();
        contact.lastName = lastNameText.getText().toString();

        editContactModel.saveContact(contact);
    }

}
