package com.example.roomdemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.roomdemo.db.ContactDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycle_bin);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        new QueryContactsTask().execute();

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

    private class QueryContactsTask extends AsyncTask<Void, Void, List<Contact>> {

        @Override
        protected List<Contact> doInBackground(Void... voids) {
            ContactDao contactDao = AppDb.instance(MainActivity.this).contactDao();

            return contactDao.selectAll();
        }

        @Override
        protected void onPostExecute(List<Contact> contacts) {
            recyclerView.setAdapter(new ContactListAdapter(contacts));
        }
    }

    private class ContactListAdapter extends RecyclerView.Adapter<ContactListItemHolder> {

        final List<Contact> contacts;

        ContactListAdapter(List<Contact> contacts) {
            this.contacts = contacts;
        }

        @Override
        public ContactListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.list_item_contact, parent, false);
            return new ContactListItemHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ContactListItemHolder holder, int position) {
            Contact c = contacts.get(position);
            holder.lastNameView.setText(c.lastName);
            holder.firstNameView.setText(c.firstName);
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
