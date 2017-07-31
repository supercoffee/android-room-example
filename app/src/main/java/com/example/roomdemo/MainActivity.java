package com.example.roomdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roomdemo.db.AppDb;
import com.example.roomdemo.db.Contact;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    private ContactListAdapter.OnItemClickListener onItemClickListener =
            new ContactListAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(View v, Contact c) {
                    Intent startEditContactActivity = EditContactActivity.intentFrom(MainActivity.this, c);
                    startActivity(startEditContactActivity);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        AppDb.instance(this).contactDao().selectAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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


    static private class ContactListAdapter extends RecyclerView.Adapter<ContactListItemHolder> {

        interface OnItemClickListener {
            void onItemClicked(View v, Contact c);
        }

        final List<Contact> contacts;
        final OnItemClickListener clickListener;

        ContactListAdapter(List<Contact> contacts, OnItemClickListener clickListener) {
            this.contacts = contacts;
            this.clickListener = clickListener;
        }

        @Override
        public ContactListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.list_item_contact, parent, false);
            return new ContactListItemHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ContactListItemHolder holder, int position) {
            final Contact c = contacts.get(position);
            holder.lastNameView.setText(c.lastName);
            holder.firstNameView.setText(c.firstName);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClicked(view, c);
                }
            });
        }


        @Override
        public int getItemCount() {
            return contacts.size();
        }
    }

    static private class ContactListItemHolder extends RecyclerView.ViewHolder {

        final TextView firstNameView;
        final TextView lastNameView;

        public ContactListItemHolder(View itemView) {
            super(itemView);
            firstNameView = itemView.findViewById(R.id.tv_first_name);
            lastNameView = itemView.findViewById(R.id.tv_last_name);
        }
    }
}
