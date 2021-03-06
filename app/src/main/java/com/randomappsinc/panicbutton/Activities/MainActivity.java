package com.randomappsinc.panicbutton.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.randomappsinc.panicbutton.R;
import com.randomappsinc.panicbutton.Utils.Contacts.ContactsUtils;
import com.randomappsinc.panicbutton.Utils.MyApplication;
import com.randomappsinc.panicbutton.Utils.PermissionUtils;
import com.randomappsinc.panicbutton.Utils.PreferencesManager;
import com.randomappsinc.panicbutton.Utils.UIUtils;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends SlidingActivity {
    private static final int SEND_SMS_CODE = 1;
    private static final int ACCESS_LOCATION_CODE = 2;

    @Bind(R.id.panic_button) FloatingActionButton panicButton;
    @Bind(R.id.panic_instructions) TextView instructions;
    @BindString(R.string.help_message_hint) String messageHint;

    private boolean panicking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PreferencesManager.get().getEmergencyContacts().isEmpty()) {
            startActivity(new Intent(this, ChooseContactsActivity.class));
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        IconDrawable exclamationIcon = new IconDrawable(this, FontAwesomeIcons.fa_exclamation)
                .colorRes(R.color.white);
        panicButton.setImageDrawable(exclamationIcon);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!PreferencesManager.get().getEmergencyContacts().isEmpty()) {
            if (!PermissionUtils.isPermissionGranted(Manifest.permission.SEND_SMS)) {
                getPermission(Manifest.permission.SEND_SMS, R.string.send_sms_explanation, SEND_SMS_CODE);
            }
            else if (!PermissionUtils.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                getPermission(Manifest.permission.ACCESS_FINE_LOCATION, R.string.location_access_explanation, ACCESS_LOCATION_CODE);
            }
            else {
                final Context context = this;
                if (!PermissionUtils.isLocationEnabled()) {
                    new MaterialDialog.Builder(this)
                            .title(R.string.enable_location_services)
                            .content(R.string.location_services_explanation)
                            .positiveText(android.R.string.yes)
                            .negativeText(android.R.string.no)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                            .show();
                }
            }
        }
    }

    private void getPermission(final String permission, int explanationId, final int requestCode) {
        final Activity activity = this;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            new MaterialDialog.Builder(this)
                    .content(explanationId)
                    .positiveText(android.R.string.yes)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            PermissionUtils.requestPermission(activity, permission, requestCode);
                        }
                    })
                    .show();
        }
        else {
            PermissionUtils.requestPermission(this, permission, requestCode);
        }
    }

    public void customizeHelpMessage() {
        String currentHelpMessage = PreferencesManager.get().getHelpMessage();
        new MaterialDialog.Builder(this)
                .title(R.string.help_message)
                .input(messageHint, currentHelpMessage, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        boolean submitEnabled = !(input.toString().trim().isEmpty());
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(submitEnabled);
                    }
                })
                .alwaysCallInputCallback()
                .positiveText(R.string.submit)
                .negativeText(android.R.string.no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String newHelpMessage = dialog.getInputEditText().getText().toString();
                        PreferencesManager.get().setHelpMessage(newHelpMessage);
                        Toast.makeText(MyApplication.getAppContext(), R.string.help_message_set, Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            ContactsUtils.sendHelpMessages();
            timerHandler.postDelayed(this, 60000);
        }
    };

    @OnClick(R.id.panic_button)
    public void panic() {
        panicking = !panicking;
        if (panicking) {
            instructions.setText(R.string.panicking);
            instructions.startAnimation(UIUtils.getFlashingAnimation());
            timerHandler.postDelayed(timerRunnable, 0);
        }
        else {
            timerHandler.removeCallbacks(timerRunnable);
            instructions.setText(R.string.panic_instructions);
            instructions.clearAnimation();
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
                customizeHelpMessage();
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
