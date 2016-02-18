package com.randomappsinc.panicbutton.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.randomappsinc.panicbutton.R;
import com.randomappsinc.panicbutton.Utils.PreferencesManager;
import com.randomappsinc.panicbutton.Utils.UIUtils;

public class MainActivity extends SlidingActivity {
    private static final int SEND_SMS_REQUEST = 1;
    private static final int ACCESS_LOCATION_REQUEST = 2;

    private boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PreferencesManager.get().getEmergencyContacts().isEmpty()) {
            firstTime = true;
            startActivity(new Intent(this, ChooseContactsActivity.class));
        }

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firstTime && !PreferencesManager.get().getEmergencyContacts().isEmpty()) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        UIUtils.loadMenuIcon(menu, R.id.choose_emergency_contacts, FontAwesomeIcons.fa_users, this);
        UIUtils.loadMenuIcon(menu, R.id.customize_help_message, FontAwesomeIcons.fa_exclamation_circle, this);
        UIUtils.loadMenuIcon(menu, R.id.settings, FontAwesomeIcons.fa_gear, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.choose_emergency_contacts:
                startActivity(new Intent(this, ChooseContactsActivity.class));
                return true;
            case R.id.customize_help_message:
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
