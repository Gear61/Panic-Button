package com.randomappsinc.panicbutton;

import android.support.annotation.NonNull;

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

    public String getName()
    {
        return name;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public int compareTo(@NonNull Contact other) {
        return this.name.toLowerCase().compareTo(other.getName().toLowerCase());
    }
}
