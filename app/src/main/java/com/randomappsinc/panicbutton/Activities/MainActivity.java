package com.randomappsinc.panicbutton.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.randomappsinc.panicbutton.Utils.PreferencesManager;
import com.randomappsinc.panicbutton.R;

public class MainActivity extends SlidingActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PreferencesManager.get().getEmergencyContacts().isEmpty()) {
            startActivity(new Intent(this, ChooseContactsActivity.class));
        }

        setContentView(R.layout.activity_main);
    }
}
