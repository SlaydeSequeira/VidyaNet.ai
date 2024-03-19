package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Craftable extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craftable);

        // Retrieve the message from the intent
        String message = getIntent().getStringExtra("message");

        // Display the message in a TextView
        TextView messageTextView = findViewById(R.id.messageTextView);
        messageTextView.setText(message);
    }
}
