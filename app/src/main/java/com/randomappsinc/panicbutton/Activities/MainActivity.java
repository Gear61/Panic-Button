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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PreferencesManager.get().getEmergencyContacts().isEmpty()) {
            startActivity(new Intent(this, ChooseContactsActivity.class));
        }

        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        UIUtils.loadMenuIcon(menu, R.id.settings, FontAwesomeIcons.fa_gear, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
