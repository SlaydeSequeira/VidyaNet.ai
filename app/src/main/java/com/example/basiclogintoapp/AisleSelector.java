package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AisleSelector extends AppCompatActivity {
ImageView i1,i2,i3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aisle_selector);
        i1= findViewById(R.id.img1);
        i2 = findViewById(R.id.img2);
        i3 = findViewById(R.id.img3);
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AisleSelector.this, Shelf3.class);
                startActivity(i);
            }
        });
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AisleSelector.this, Activity_Shelf_2.class);
                startActivity(i);
            }
        });
        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AisleSelector.this, ShelfAssistant.class);
                startActivity(i);
            }
        });
    }
}