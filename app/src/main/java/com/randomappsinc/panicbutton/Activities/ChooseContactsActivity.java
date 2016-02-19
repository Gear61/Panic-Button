package com.randomappsinc.panicbutton.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.randomappsinc.panicbutton.Adapters.ContactsAdapter;
import com.randomappsinc.panicbutton.R;
import com.randomappsinc.panicbutton.Utils.Contacts.ContactServer;
import com.randomappsinc.panicbutton.Utils.PermissionUtils;
import com.randomappsinc.panicbutton.Utils.PreferencesManager;
import com.randomappsinc.panicbutton.Utils.UIUtils;

import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by alexanderchiou on 2/15/16.
 */
public class ChooseContactsActivity extends SlidingActivity {
    @Bind(R.id.parent) View parent;
    @Bind(R.id.loading_contacts) View loadingContacts;
    @Bind(R.id.content) View content;
    @Bind(R.id.friends_list) ListView contactsList;
    @Bind(R.id.friend_input) EditText searchInput;

    private ContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_contacts);
        ButterKnife.bind(this);

        // If the user is opening the app for the first time, show them the instructions
        if (PreferencesManager.get().getEmergencyContacts().isEmpty()) {
            showInstructions(true);
        }
        else {
            setUpContactsList();
        }
    }

    public void setUpContactsList() {
        if (!PermissionUtils.isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
            PermissionUtils.requestPermission(this, Manifest.permission.READ_CONTACTS, 0);
        }
        else {
            fetchContactsList();
        }
    }

    public void fetchContactsList() {
        new FriendsListInitializer().execute();
    }

    private class FriendsListInitializer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            ContactServer.getInstance().initialize();
            renderContactsList();
            return null;
        }
    }

    private void renderContactsList() {
        final Context context = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingContacts.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
                contactsAdapter = new ContactsAdapter(context);
                contactsList.setAdapter(contactsAdapter);
            }
        });
    }

    private void showInstructions(final boolean firstTime) {
        new MaterialDialog.Builder(this)
                .title(R.string.instructions)
                .content(R.string.choose_contacts_instructions)
                .positiveText(android.R.string.yes)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (firstTime) {
                            setUpContactsList();
                        }
                    }
                })
                .show();
    }

    private void getReadContacts() {
        final Activity activity = this;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
            new MaterialDialog.Builder(this)
                    .content(R.string.read_contacts_explanation)
                    .positiveText(android.R.string.yes)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            PermissionUtils.requestPermission(activity, Manifest.permission.READ_CONTACTS, 0);
                        }
                    })
                    .show();
        }
        else {
            PermissionUtils.requestPermission(activity, Manifest.permission.READ_CONTACTS, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchContactsList();
        }
        else {
            getReadContacts();
        }
    }

    @OnTextChanged(value = R.id.friend_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable searchInput) {
        contactsAdapter.updateWithPrefix(searchInput.toString());
    }

    @OnClick(R.id.clear_input)
    public void clearSearch() {
        searchInput.setText("");
    }

    @OnClick(R.id.submit)
    public void chooseContacts() {
        Set<String> chosenContacts = contactsAdapter.getChosenPhoneNumbers();
        if (chosenContacts.isEmpty()) {
            UIUtils.showSnackbar(parent, getString(R.string.need_a_contact));
        }
        else {
            PreferencesManager.get().setEmergencyContacts(chosenContacts);
            finish();
        }
    }

    @Override
    public void onBackPressed() {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.choose_contacts_menu, menu);
        UIUtils.loadMenuIcon(menu, R.id.show_instructions, FontAwesomeIcons.fa_info_circle, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_instructions:
                showInstructions(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
