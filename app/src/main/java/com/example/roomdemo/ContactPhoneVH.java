package com.example.roomdemo;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.roomdemo.db.Phone;

class ContactPhoneVH extends RecyclerView.ViewHolder {

    final Spinner selectedType;
    final EditText phoneNumberText;

    private Phone phone;

    final TextWatcher onTextChanged = new TextWatcher() {
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

    ContactPhoneVH(View itemView) {
        super(itemView);
        selectedType = itemView.findViewById(R.id.sp_phone_type);
        phoneNumberText = itemView.findViewById(R.id.et_phone);

    }

    void bind(Phone p) {
        phoneNumberText.addTextChangedListener(onTextChanged);
        this.phone = p;
    }


}
