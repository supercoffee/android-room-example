package com.example.roomdemo;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.roomdemo.db.Phone;

import java.util.List;

class ContactPhoneAdapater extends RecyclerView.Adapter<ContactPhoneVH> {

    private final List<Phone> phones;

    public ContactPhoneAdapater(List<Phone> phones) {
        this.phones = phones;
    }

    @Override
    public ContactPhoneVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item_contact_phone, parent, false);
        return new ContactPhoneVH(v);
    }

    @Override
    public void onBindViewHolder(ContactPhoneVH holder, int position) {
        Resources res = holder.itemView.getResources();
        String[] types = res.getStringArray(R.array.phone_types);
        Phone phone = phones.get(position);
        holder.phoneNumberText.setText(phone.phoneNumber);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(holder.itemView.getContext(), R.array.phone_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.selectedType.setAdapter(adapter);

        for (int i = 0; i < types.length; i++) {
            String s = types[i];
            if (s.equals(phone.type)) {
                holder.selectedType.setSelection(i);
                break;
            }
        }
        holder.bind(phone);
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }
}
