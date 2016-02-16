package com.randomappsinc.panicbutton.Utils.Contacts;

import com.randomappsinc.panicbutton.Contact;
import com.randomappsinc.panicbutton.Utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 2/15/16.
 */
public class ContactServer {
    private static ContactServer instance;
    private List<Contact> contacts;

    public static ContactServer getInstance() {
        if (instance == null) {
            instance = new ContactServer();
        }
        return instance;
    }

    private ContactServer() {}

    public void initialize() {
        if (contacts == null) {
            contacts = ContactsUtils.getPhoneFriends(MyApplication.getAppContext().getContentResolver());
        }
    }

    public List<Contact> getMatches(String prefix) {
        int indexOfMatch = binarySearch(prefix);
        if (prefix.isEmpty()) {
            return new ArrayList<>(contacts);
        }
        else if (indexOfMatch == -1) {
            return new ArrayList<>();
        }
        else {
            List<Contact> matchingFriends = new ArrayList<>();
            matchingFriends.add(contacts.get(indexOfMatch));

            String cleanPrefix = prefix.toLowerCase();
            for (int i = indexOfMatch - 1; i >= 0; i--) {
                String friendSubstring = getSubstring(contacts.get(i).getName(), prefix);
                if (friendSubstring.equals(cleanPrefix)) {matchingFriends.add(0, contacts.get(i));}
                else {break;}
            }
            for (int i = indexOfMatch + 1; i < contacts.size(); i++) {
                String friendSubstring = getSubstring(contacts.get(i).getName(), prefix).toLowerCase();
                if (friendSubstring.equals(cleanPrefix)) {matchingFriends.add(contacts.get(i));}
                else {break;}
            }
            return matchingFriends;
        }
    }

    // Returns index of first word with given prefix (-1 if it's not found)
    private int binarySearch(String prefix) {
        String cleanPrefix = prefix.toLowerCase();

        int lo = 0;
        int hi = contacts.size() - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int compareToValue = cleanPrefix.compareTo(getSubstring(contacts.get(mid).getName(), cleanPrefix));
            if (compareToValue < 0) {
                hi = mid - 1;
            }
            else if (compareToValue > 0) {
                lo = mid + 1;
            }
            else {
                return mid;
            }
        }
        return -1;
    }

    private String getSubstring(String main, String prefix) {
        if (prefix.length() > main.length()) {
            return main.toLowerCase();
        }
        return main.substring(0, prefix.length()).toLowerCase();
    }
}

