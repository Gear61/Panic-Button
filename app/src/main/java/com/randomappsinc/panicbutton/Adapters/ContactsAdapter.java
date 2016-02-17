package com.randomappsinc.panicbutton.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.randomappsinc.panicbutton.Contact;
import com.randomappsinc.panicbutton.R;
import com.randomappsinc.panicbutton.Utils.Contacts.ContactServer;
import com.rey.material.widget.CheckBox;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 2/15/16.
 */
public class ContactsAdapter extends BaseAdapter {
    private Context context;
    private List<Contact> contacts;
    private Set<String> chosenPhoneNumbers;

    public ContactsAdapter(Context context) {
        this.context = context;
        this.contacts = ContactServer.getInstance().getMatches("");
        this.chosenPhoneNumbers = new HashSet<>();
    }

    public void updateWithPrefix(String prefix) {
        this.contacts = ContactServer.getInstance().getMatches(prefix);
        notifyDataSetChanged();
    }

    public Set<String> getChosenPhoneNumbers() {
        return chosenPhoneNumbers;
    }

    public int getCount() {
        return contacts.size();
    }

    public Contact getItem(int position) {
        return contacts.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public class ContactViewHolder {
        @Bind(R.id.contact_name) TextView name;
        @Bind(R.id.contact_phone_number) TextView phoneNumber;
        @Bind(R.id.contact_selected) CheckBox contactSelected;

        private Contact contact;

        public ContactViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadContactInfo(Contact contact) {
            this.contact = contact;
            name.setText(contact.getName());
            phoneNumber.setText(contact.getFormattedPhoneNumber());
            contactSelected.setCheckedImmediately(chosenPhoneNumbers.contains(contact.getRawPhoneNumber()));
        }

        @OnCheckedChanged(R.id.contact_selected)
        public void contactSelected(boolean isSelected) {
            if (isSelected) {
                chosenPhoneNumbers.add(contact.getRawPhoneNumber());
            }
            else {
                chosenPhoneNumbers.remove(contact.getRawPhoneNumber());
            }
        }

        @OnClick(R.id.parent)
        public void onParentClicked() {
            contactSelected.setChecked(!contactSelected.isChecked());
        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        ContactViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.contact_cell, parent, false);
            holder = new ContactViewHolder(view);
            view.setTag(holder);
        }
        else {
            holder = (ContactViewHolder) view.getTag();
        }
        holder.loadContactInfo(getItem(position));
        return view;
    }
}
