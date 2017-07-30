package com.example.roomdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.example.roomdemo.db.AppDb;
import com.example.roomdemo.db.Contact;
import com.example.roomdemo.db.ContactDao;
import com.example.roomdemo.db.Phone;
import com.example.roomdemo.db.PhoneDao;
import com.github.javafaker.Faker;

public class SeederActivity extends Activity {

    AppCompatButton seedContactsButton;
    private AppDb db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeder);
        seedContactsButton = findViewById(R.id.btn_seed);
        seedContactsButton.setOnClickListener(seedButtonListener);

        db = AppDb.instance(this);
    }

    private View.OnClickListener seedButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Faker faker = new Faker();
            ContactDao contacts = db.contactDao();
            PhoneDao phones = db.phoneDao();

            for (int i = 0; i < 10; i++) {
                Contact c = new Contact(faker.name().firstName(), faker.name().lastName());
                contacts.insertContact(c);

                Phone p = new Phone();
                p.contactId = c.id;
                p.phoneNumber = faker.phoneNumber().phoneNumber();
                p.type = "cell";

                phones.insertPhone(p);
            }
        }
    };
}
