package com.hackathon.walrus.pigeon;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by riot94 on 3/9/2016.
 */
public class Messenger extends Activity {
    Button sendSMS;
    EditText msgTxt;
    EditText numTxt;
    IntentFilter intentFilter;

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //display the message in the textview
            TextView inTxt = (TextView) findViewById(R.id.textMsg);
            inTxt.setText(intent.getExtras().getString("sms"));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_app);

        //intent to filter for SMS messages received
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEVEIVED_ACTION");

        sendSMS = (Button) findViewById(R.id.sendBtn);
        msgTxt = (EditText) findViewById(R.id.message);
        numTxt = (EditText) findViewById(R.id.numberTxt);
        sendSMS.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                String myMsg = msgTxt.getText().toString();
                String theNumber = numTxt.getText().toString();
                sendMsg(theNumber, myMsg);
            }
        });
    }

    protected void sendMsg(String theNumber, String myMsg){
        String SENT = "Message Sent";
        String DELIVERED = "Message Delivered";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        registerReceiver(new BroadcastReceiver(){
            public void onReceive(Context arg0, Intent arg1){
                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(Messenger.this, "SMS sent", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic Failure", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No Service", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        registerReceiver(new BroadcastReceiver(){
            public void onReceive(Context arg0, Intent arg1){
                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(Messenger.this, "SMS sent", Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_LONG).show();
                }
            }
        }, new IntentFilter(SENT));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(theNumber,null,myMsg,sentPI,deliveredPI);
    }

    @Override
    protected void onResume(){
        //register the receiver
        registerReceiver(intentReceiver,intentFilter);
        super.onResume();
    }
    @Override
    protected void onPause(){
        //unregister the receiver
        unregisterReceiver(intentReceiver);
        super.onPause();
    }
}
