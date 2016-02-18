package com.randomappsinc.panicbutton.Utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexanderchiou on 2/15/16.
 */
public class PreferencesManager {
    private static final String EMERGENCY_CONTACTS_KEY = "emergencyContacts";
    private static PreferencesManager instance;
    private SharedPreferences prefs;

    public static PreferencesManager get() {
        if (instance == null) {
            instance = getSync();
        }
        return instance;
    }

    private static synchronized PreferencesManager getSync() {
        if (instance == null) {
            instance = new PreferencesManager();
        }
        return instance;
    }

    private PreferencesManager() {
        prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
    }

    public Set<String> getEmergencyContacts() {
        return prefs.getStringSet(EMERGENCY_CONTACTS_KEY, new HashSet<String>());
    }

    public void setEmergencyContacts(Set<String> emergencyContacts) {
        prefs.edit().remove(EMERGENCY_CONTACTS_KEY).apply();
        prefs.edit().putStringSet(EMERGENCY_CONTACTS_KEY, emergencyContacts).apply();
    }
}
