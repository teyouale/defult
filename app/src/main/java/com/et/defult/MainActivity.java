package com.et.defult;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

public class MainActivity extends FragmentActivity {
    private RelativeLayout mSetDefaultSmsLayout;
    private Button mSendSmsButton;
    private EditText mSendSmsEditText;
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find some views
        mSetDefaultSmsLayout = findViewById(R.id.set_default_sms_layout);
        mSendSmsEditText = findViewById(R.id.send_sms_edittext);
        ListView listView = findViewById(android.R.id.list);
        listView.setEmptyView(findViewById(android.R.id.empty));
        mSendSmsButton = findViewById(R.id.send_sms_button);
        mSendSmsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSms(mSendSmsEditText.getText().toString());
            }
        });

        // Create adapter and set it to our ListView


        // Placeholder to process incoming SEND/SENDTO intents
        String intentAction = getIntent() == null ? null : getIntent().getAction();
        if (!TextUtils.isEmpty(intentAction) && (Intent.ACTION_SENDTO.equals(intentAction)
                || Intent.ACTION_SEND.equals(intentAction))) {
            // TODO: Handle incoming SEND and SENDTO intents by pre-populating UI components
            Toast.makeText(this, "Handle SEND and SENDTO intents: " + getIntent().getDataString(),
                    Toast.LENGTH_SHORT).show();
        }

        // Simple query to show the most recent SMS messages in the inbox
        // getSupportLoaderManager().initLoader(SmsQuery.TOKEN, null, this);
    }

    /**
     * Dummy sendSms method, would need the "to" address to actually send a message :)
     */
    private void sendSms(String smsText) {
        if (!TextUtils.isEmpty(smsText)) {
            if (Utils.isDefaultSmsApp(this)) {
                // TODO: Use SmsManager to send SMS and then record the message in the system SMS
                // ContentProvider
                Toast.makeText(this, "Sending text message: " + smsText, Toast.LENGTH_SHORT).show();
            } else {
                // TODO: Notify the user the app is not default and provide a way to trigger
                // Utils.setDefaultSmsApp() so they can set it.
                Toast.makeText(this, "Not default", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Only do these checks/changes on KitKat+, the "mSetDefaultSmsLayout" has its visibility
        // set to "gone" in the xml layout so it won't show at all on earlier Android versions.
        if (Utils.hasKitKat()) {
            if (Utils.isDefaultSmsApp(this)) {
                // This app is the default, remove the "make this app the default" layout and
                // enable message sending components.
                mSetDefaultSmsLayout.setVisibility(View.GONE);
                mSendSmsEditText.setHint(R.string.sms_send_new_hint);
                mSendSmsEditText.setEnabled(true);
                mSendSmsButton.setEnabled(true);
            } else {
                // Not the default, show the "make this app the default" layout and disable
                // message sending components.
                mSetDefaultSmsLayout.setVisibility(View.VISIBLE);
                mSendSmsEditText.setText("");
                mSendSmsEditText.setHint(R.string.sms_send_disabled);
                mSendSmsEditText.setEnabled(false);
                mSendSmsButton.setEnabled(false);

                Button button = findViewById(R.id.set_default_sms_button);
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.setDefaultSmsApp(MainActivity.this);
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}