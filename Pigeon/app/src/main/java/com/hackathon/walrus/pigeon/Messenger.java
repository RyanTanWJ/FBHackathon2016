package com.hackathon.walrus.pigeon;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by riot94 on 3/9/2016.
 */
public class Messenger extends Activity {
    Button sendSMS;
    EditText msgTxt;
    EditText numTxt;

    @Override
    public void onCreate(Bundle sendInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
