package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.basiclogintoapp.R;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // Retrieve data passed from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString("title");
            String description = extras.getString("description");
            String location = extras.getString("location"); // Retrieve location data

            // Initialize views with correct IDs
            TextView titleTextView = findViewById(R.id.title);
            TextView descriptionTextView = findViewById(R.id.description);
            TextView locationTextView = findViewById(R.id.location);
            ImageView imageView = findViewById(R.id.itemImageView);

            // Set data to views
            titleTextView.setText(title);
            descriptionTextView.setText(description);
            locationTextView.setText(location); // Set location text
             imageView.setImageResource(R.drawable.baseline_work_outline_24); // Assuming you still need to set the image
        }
    }
}

