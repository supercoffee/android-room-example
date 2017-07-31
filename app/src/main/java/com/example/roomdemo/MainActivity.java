package com.example.roomdemo;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.roomdemo.db.AppDb;
import com.example.roomdemo.db.Contact;
import com.example.roomdemo.viewmodel.ContactList;
import com.example.roomdemo.viewmodel.ContactListFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private ContactListAdapter.OnItemClickListener onItemClickListener =
            new ContactListAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(View v, Contact c) {
                    launchContactEditor(c);
                }
            };

    private void launchContactEditor(Contact c) {
        Intent startEditContactActivity = EditContactActivity.intentFrom(MainActivity.this, c);
        startActivity(startEditContactActivity);
    }

    private ContactList contactListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ContactListFactory cf = new ContactListFactory(AppDb.instance(this).contactDao());
        contactListModel = ViewModelProviders.of(this, cf).get(ContactList.class);
    }

    @Override
    protected void onResume() {
        contactListModel.getAllContacts()
                .subscribe(new Consumer<List<Contact>>() {
                    @Override
                    public void accept(@NonNull List<Contact> contacts) throws Exception {
                        recyclerView.setAdapter(new ContactListAdapter(contacts, onItemClickListener));
                    }
                });

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_seed:
                Intent openSeeder = new Intent(this, SeederActivity.class);
                startActivity(openSeeder);
                return true;
        }
        return false;
    }

    @OnClick(R.id.fab_add_contact)
    public void onFabClick(final View v) {
        v.setEnabled(false);
        contactListModel.createContact().subscribe(new Consumer<Contact>() {
            @Override
            public void accept(@NonNull Contact contact) throws Exception {
                v.setEnabled(true);
                launchContactEditor(contact);
            }
        });
    }
}
