package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

public class Courses extends AppCompatActivity {
Button b1,b2,b3,b4,b5,r1,r2,r3,r4,r5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        Objects.requireNonNull(getSupportActionBar()).hide();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.parseColor("#F6F6F6"));
        }
        b1 = findViewById(R.id.buy_button_1);
        b2 = findViewById(R.id.buy_button_2);
        b3 = findViewById(R.id.buy_button_3);
        b4 = findViewById(R.id.buy_button_4);
        b5 = findViewById(R.id.buy_button_5);
        r1 = findViewById(R.id.redeem_button_1);
        r2 = findViewById(R.id.redeem_button_2);
        r3 = findViewById(R.id.redeem_button_3);
        r4 = findViewById(R.id.redeem_button_4);
        r5 = findViewById(R.id.redeem_button_5);
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Courses.this, "Insufficient Points", Toast.LENGTH_SHORT).show();
            }
        });
// For r2 button
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Courses.this, "Insufficient Points", Toast.LENGTH_SHORT).show();
            }
        });

// For r3 button
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Courses.this, "Insufficient Points", Toast.LENGTH_SHORT).show();
            }
        });

// For r4 button
        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Courses.this, "Insufficient Points", Toast.LENGTH_SHORT).show();
            }
        });

// For r5 button
        r5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Courses.this, "Insufficient Points", Toast.LENGTH_SHORT).show();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Courses.this,Payment.class);
                i.putExtra("Cost",999);
                startActivity(i);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Courses.this,Payment.class);
                i.putExtra("Cost",499);
                startActivity(i);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Courses.this,Payment.class);
                i.putExtra("Cost",1499);
                startActivity(i);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Courses.this,Payment.class);
                i.putExtra("Cost",699);
                startActivity(i);
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Courses.this,Payment.class);
                i.putExtra("Cost",799);
                startActivity(i);
            }
        });
    }
}