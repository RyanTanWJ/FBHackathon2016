package com.hackathon.walrus.pigeon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class Messenger extends AppCompatActivity {

    ArrayList<Message> chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chat = new ArrayList<Message>();
        setContentView(R.layout.activity_messenger);

        final TextView chatDisplay = (TextView) (findViewById(R.id.Chat));

        Button send = (Button)(findViewById(R.id.send));
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText entry = (EditText) (findViewById(R.id.Entry));
                Message msg = new Message("user", entry.getText().toString());
                chat.add(msg);
                chatDisplay.append("\n" + msg.getSender() + ": " + msg.getMessage());
                entry.setText("");
            }
        });
    }

}
