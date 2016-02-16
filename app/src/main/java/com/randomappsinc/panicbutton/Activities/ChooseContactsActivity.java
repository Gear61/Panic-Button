package com.randomappsinc.panicbutton.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.panicbutton.R;
import com.randomappsinc.panicbutton.Utils.PreferencesManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 2/15/16.
 */
public class ChooseContactsActivity extends SlidingActivity {
    public static final int READ_CONTACTS_REQUEST = 1;
    public static final int SEND_SMS_REQUEST = 2;

    @Bind(R.id.loading_contacts) View loadingContacts;
    @Bind(R.id.content) View content;
    @Bind(R.id.friends_list) ListView friendsList;
    @Bind(R.id.friend_input) EditText friendInput;

    private MaterialDialog instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_contacts);
        ButterKnife.bind(this);

        if (PreferencesManager.get().getEmergencyContacts().isEmpty()) {

        }
        else {

        }
    }

    private void showInstructions() {

    }

    private void setUpContactsList() {

    }
}
