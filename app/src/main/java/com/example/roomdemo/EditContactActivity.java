package com.example.roomdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.roomdemo.db.AppDb;
import com.example.roomdemo.db.Contact;
import com.example.roomdemo.db.ContactDao;
import com.example.roomdemo.db.Phone;
import com.example.roomdemo.db.PhoneDao;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.completable.CompletableFromAction;
import io.reactivex.schedulers.Schedulers;


public class EditContactActivity extends Activity {

    private static final String EXTRA_CONTACT = Contact.class.getName();
    EditText firstNameText;
    EditText lastNameText;
    RecyclerView recyclerView;
    FloatingActionButton addPhoneFab;
    private Contact contact;
    private ContactDao contactDao;
    private PhoneDao phoneDao;

    public List<Phone> mPhones;


    public static Intent intentFrom(Context context, Contact contact) {
        Intent i = new Intent(context, EditContactActivity.class);
        i.putExtra(EXTRA_CONTACT, contact);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        contactDao = AppDb.instance(this).contactDao();
        phoneDao = AppDb.instance(this).phoneDao();

        firstNameText = findViewById(R.id.et_first_name);
        lastNameText = findViewById(R.id.et_last_name);
        recyclerView = findViewById(R.id.recycler);
        addPhoneFab = findViewById(R.id.fab_add_phone);

        contact = (Contact) getIntent().getSerializableExtra(EXTRA_CONTACT);
        firstNameText.setText(contact.firstName);
        lastNameText.setText(contact.lastName);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addPhoneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Phone phone = new Phone();
                phone.contactId = contact.id;

                new CompletableFromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        phoneDao.insertPhone(phone);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
        });
    }

    @Override
    protected void onResume() {
        contactDao.getContactPhones(contact.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Phone>>() {

                    @Override
                    public void accept(@NonNull List<Phone> phones) throws Exception {
                        mPhones = phones;
                        recyclerView.setAdapter(new ContactPhoneAdapater(phones));
                    }
                });
        super.onResume();
    }

    @Override
    protected void onPause() {
        saveContact();
        savePhones();
        super.onPause();
    }

    private void savePhones() {
        new CompletableFromAction(new Action() {
            @Override
            public void run() throws Exception {
                Phone[] phones = new Phone[mPhones.size()];
                phoneDao.insertPhone( mPhones.toArray(phones));
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    private void saveContact() {
        contact.firstName = firstNameText.getText().toString();
        contact.lastName = lastNameText.getText().toString();

        new CompletableFromAction(new Action() {
            @Override
            public void run() throws Exception {
                AppDb.instance(EditContactActivity.this).contactDao().insertContact(contact);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private static class ContactPhoneAdapater extends RecyclerView.Adapter<ContactPhoneVH> {

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

    private static class ContactPhoneVH extends RecyclerView.ViewHolder {

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
}
