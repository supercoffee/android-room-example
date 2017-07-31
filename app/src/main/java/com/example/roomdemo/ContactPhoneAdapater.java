package com.example.roomdemo;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        holder.selectedTypeSpinner.setAdapter(adapter);

        for (int i = 0; i < types.length; i++) {
            String s = types[i];
            if (s.equals(phone.type)) {
                holder.selectedTypeSpinner.setSelection(i);
                break;
            }
        }
        holder.phoneNumberText.addTextChangedListener(onTextChanged(phone));
        holder.selectedTypeSpinner.setOnItemSelectedListener(onItemSelected(phone));
    }

    private AdapterView.OnItemSelectedListener onItemSelected(final Phone phone) {

        return new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object selection = adapterView.getAdapter().getItem(i);
                if (selection != null && selection.getClass().isAssignableFrom(String.class)) {
                    phone.type = (String) selection;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };
    }

    private TextWatcher onTextChanged(final Phone phone) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phone.phoneNumber = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }
}
