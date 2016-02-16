package com.randomappsinc.panicbutton;

import android.support.annotation.NonNull;

import com.randomappsinc.panicbutton.Utils.Contacts.ContactsUtils;

/**
 * Created by alexanderchiou on 2/15/16.
 */
public class Contact implements Comparable<Contact> {
    private String name;
    private String phoneNumber;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getRawPhoneNumber() {
        return phoneNumber;
    }

    public String getFormattedPhoneNumber() {
        return ContactsUtils.humanizePhoneNumber(phoneNumber);
    }

    public int compareTo(@NonNull Contact other) {
        return this.name.toLowerCase().compareTo(other.getName().toLowerCase());
    }
}
