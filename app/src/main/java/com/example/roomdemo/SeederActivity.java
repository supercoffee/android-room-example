package com.example.roomdemo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import com.example.roomdemo.db.AppDb;
import com.example.roomdemo.db.Contact;
import com.example.roomdemo.db.ContactDao;
import com.example.roomdemo.db.Phone;
import com.example.roomdemo.db.PhoneDao;
import com.github.javafaker.Faker;

import static android.view.View.VISIBLE;

public class SeederActivity extends Activity {

    AppCompatButton seedContactsButton;
    private AppDb db;

    private TextView message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeder);
        seedContactsButton = findViewById(R.id.btn_seed);
        seedContactsButton.setOnClickListener(seedButtonListener);

        message = findViewById(R.id.tv_message);

        db = AppDb.instance(this);
    }

    private View.OnClickListener seedButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SeedDBTask task = new SeedDBTask();
            task.execute();
        }
    };

    class SeedDBTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            Faker faker = new Faker();
            ContactDao contacts = db.contactDao();
            PhoneDao phones = db.phoneDao();

            for (int i = 0; i < 10; i++) {
                Contact c = new Contact(faker.name().firstName(), faker.name().lastName());
                // Room doesn't automatically assign the ID to the object when inserting.
                c.id = contacts.insertContact(c);

                Phone p = new Phone();
                p.contactId = c.id;
                p.phoneNumber = faker.phoneNumber().phoneNumber();
                p.type = "cell";

                phones.insertPhone(p);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            message.setText("Done");
            message.setVisibility(VISIBLE);
        }
    }
}
