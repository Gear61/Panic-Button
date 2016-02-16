package com.randomappsinc.panicbutton.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.randomappsinc.panicbutton.Contact;
import com.randomappsinc.panicbutton.R;
import com.randomappsinc.panicbutton.Utils.ContactServer;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 2/15/16.
 */
public class ContactsAdapter extends BaseAdapter {
    private Context context;
    private List<Contact> contacts;

    public ContactsAdapter(Context context) {
        this.context = context;
        this.contacts = ContactServer.getInstance().getMatches("");
    }

    public void updateWithPrefix(String prefix) {
        this.contacts = ContactServer.getInstance().getMatches(prefix);
        notifyDataSetChanged();
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

    public class FriendViewHolder {
        @Bind(R.id.contact_name) TextView name;
        @Bind(R.id.contact_phone_number) TextView phoneNumber;

        public FriendViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        FriendViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.contact_cell, parent, false);
            holder = new FriendViewHolder(view);
            view.setTag(holder);
        }
        else {
            holder = (FriendViewHolder) view.getTag();
        }
        holder.name.setText(getItem(position).getName());
        holder.phoneNumber.setText(getItem(position).getFormattedPhoneNumber());
        return view;
    }
}
