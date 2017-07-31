package com.example.roomdemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;

class ContactPhoneVH extends RecyclerView.ViewHolder {

    @BindView(R.id.sp_phone_type)
    Spinner selectedTypeSpinner;

    @BindView(R.id.et_phone)
    EditText phoneNumberText;

    @BindView(R.id.btn_delete)
    ImageButton deleteButton;

    ContactPhoneVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        assert selectedTypeSpinner != null;
        assert phoneNumberText != null;
        assert deleteButton != null;
    }

}
