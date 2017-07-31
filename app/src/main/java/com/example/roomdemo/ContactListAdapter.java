package com.example.roomdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roomdemo.db.Contact;

import java.util.List;

class ContactListAdapter extends RecyclerView.Adapter<ContactListItemHolder> {

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
