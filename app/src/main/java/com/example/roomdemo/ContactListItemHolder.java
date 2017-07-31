package com.example.roomdemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class ContactListItemHolder extends RecyclerView.ViewHolder {

    final TextView firstNameView;
    final TextView lastNameView;

    public ContactListItemHolder(View itemView) {
        super(itemView);
        firstNameView = itemView.findViewById(R.id.tv_first_name);
        lastNameView = itemView.findViewById(R.id.tv_last_name);
    }
}
