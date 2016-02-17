package com.randomappsinc.panicbutton.Utils.Contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.randomappsinc.panicbutton.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alexanderchiou on 2/15/16.
 */
public class ContactsUtils {
    // Given a "sanitized" phone # (only digits with country code), turn it back to human-readable format
    // Example: +1 (510) 449-4353
    public static String humanizePhoneNumber(String phoneNumber) {
        if (phoneNumber.length() < 10 || (!phoneNumber.matches("[0-9]+")) || phoneNumber.length() > 11) {
            return phoneNumber;
        }
        if (phoneNumber.length() == 11) {
            String countryCode = "+" + phoneNumber.charAt(0);
            String areaCode = " (" + phoneNumber.substring(1, 4) + ") ";
            String numberPart1 = phoneNumber.substring(4, 7) + "-";
            String numberPart2 = phoneNumber.substring(7, 11);
            return countryCode + areaCode + numberPart1 + numberPart2;
        }
        else {
            String areaCode = " (" + phoneNumber.substring(0, 3) + ") ";
            String numberPart1 = phoneNumber.substring(3, 6) + "-";
            String numberPart2 = phoneNumber.substring(6, 10);
            return areaCode + numberPart1 + numberPart2;
        }
    }

    // Given a phone number as a string, removes all non-digit characters and appends country code
    // This function is called before passing phone numbers to the back-end
    public static String sanitizePhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[^0-9]", "");
    }

    // Gets data to be used in ContactsAC adapters in emergency cases
    public static List<Contact> getPhoneFriends(ContentResolver resolver) {
        List<Contact> phoneFriends = new ArrayList<>();
        Map<String, String> phoneFriendsMap = getPhoneFriendsMap(resolver);
        for (String phoneNumber : phoneFriendsMap.keySet()) {
            phoneFriends.add(new Contact(phoneFriendsMap.get(phoneNumber), phoneNumber));
        }
        Collections.sort(phoneFriends);
        return phoneFriends;
    }

    // Gets data to be used in ContactsAC adapters
    public static HashMap<String, String> getPhoneFriendsMap(ContentResolver resolver) {
        HashMap<String, String> phoneFriendsMappings = new HashMap<>();
        Cursor contactsCursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (contactsCursor != null) {
            while (contactsCursor.moveToNext()) {
                String id = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor phoneNumbersCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    // Mechanism to only have 1 number per contact
                    // Mobile is 1, home is 2, work is 3, other is 4
                    int numberPriority = 100;
                    String finalPhoneNumber = "";
                    if (phoneNumbersCursor != null) {
                        while (phoneNumbersCursor.moveToNext()) {
                            int phoneType = phoneNumbersCursor.getInt(phoneNumbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                            String phoneNumber = sanitizePhoneNumber(phoneNumbersCursor.getString(phoneNumbersCursor.getColumnIndex
                                    (ContactsContract.CommonDataKinds.Phone.NUMBER)));
                            switch (phoneType) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    if (numberPriority > 1) {
                                        finalPhoneNumber = phoneNumber;
                                        numberPriority = 1;
                                    }
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    if (numberPriority > 2) {
                                        finalPhoneNumber = phoneNumber;
                                        numberPriority = 2;
                                    }
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    if (numberPriority > 3) {
                                        finalPhoneNumber = phoneNumber;
                                        numberPriority = 3;
                                    }
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                    if (numberPriority > 4) {
                                        finalPhoneNumber = phoneNumber;
                                        numberPriority = 4;
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                        if (finalPhoneNumber.length() == 10 || finalPhoneNumber.length() == 11) {
                            phoneFriendsMappings.put(finalPhoneNumber, name);
                        }
                        phoneNumbersCursor.close();
                    }
                }
            }
            contactsCursor.close();
        }
        return phoneFriendsMappings;
    }
}
