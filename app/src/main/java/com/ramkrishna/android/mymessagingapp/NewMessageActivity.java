package com.ramkrishna.android.mymessagingapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ramkr on 23-Apr-16.
 * <p/>
 * This activity show the send new sms screen.
 */
public class NewMessageActivity extends AppCompatActivity {

    //References to the views in the layout
    private EditText phone_number, message;
    private TextView message_length_text_view;
    private Toast toast;

    //Activity Lifecycle Method OnCreate. Used to initialize activity parameters and inflate layout.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get the views in the associated references
        phone_number = (EditText) findViewById(R.id.phone_number_edit_text);
        message = (EditText) findViewById(R.id.message_edit_text);
        message_length_text_view = (TextView) findViewById(R.id.message_length_text_view);

        //Register listener to listen for text change in the message TextView
        message.addTextChangedListener(new TextWatcher() {

            //Action to take just before the text began to change
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            //Action to take on text change. Here we are updating the Textview that shows the length of the message
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                message_length_text_view.setText(Integer.toString(message.getText().length()) + getString(R.string.new_message_max_limit));
            }

            //Action to take after the text changed
            @Override
            public void afterTextChanged(Editable s) { }
        });
        //Toast to warn the user that the phone number and message field are required
        toast = Toast.makeText(this, R.string.new_message_error_toast, Toast.LENGTH_SHORT);
    }

    /**
     * Method to send sms
     *
     * @param v
     */
    public void sendSMS(View v)
    {
        String phoneNumber = phone_number.getText().toString();
        String textMessage = message.getText().toString();
        if (!phoneNumber.isEmpty() && !textMessage.isEmpty())
        {
            //Get the SMS Manager
            SmsManager smsManager = SmsManager.getDefault();
            //Send the SMS
            smsManager.sendTextMessage(phoneNumber, null, textMessage, null, null);
            //close this activity
            finish();
        } else
        {
            //show the warning toast
            toast.show();
        }
    }
}
