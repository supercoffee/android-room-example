package com.example.roomdemo;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roomdemo.db.Contact;
import com.example.roomdemo.viewmodel.ContactList;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

class ContactListAdapter extends RecyclerView.Adapter<ContactListItemHolder> {

    interface OnItemClickListener {
        void onItemClicked(View v, Contact c);
    }

    private final ContactList viewModel;
    private final OnItemClickListener clickListener;

    @Nullable
    private List<Contact> contacts;

    ContactListAdapter(ContactList contacts, OnItemClickListener clickListener) {
        this.clickListener = clickListener;
        this.viewModel = contacts;
        this.viewModel.getAllContacts()
                .subscribe(new Consumer<List<Contact>>() {
                    @Override
                    public void accept(@NonNull List<Contact> contacts) throws Exception {
                        ContactListAdapter.this.contacts = contacts;
                        notifyDataSetChanged();
                    }
                });
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
        return contacts != null ? contacts.size() : 0;
    }
}
