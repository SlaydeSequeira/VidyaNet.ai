package com.example.basiclogintoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Augmented extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_augmented);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Button 1: Redirect to example1.com
        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(v -> openWebsite("https://sketchfab.com/models/633e387c5c644c1a8e0d0f90d1d87b17/embed?autostart=1&cardboard=1&internal=1&tracking=0&ui_infos=0&ui_snapshots=1&ui_stop=0&ui_watermark=0"));

        // Button 2: Redirect to example2.com
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> openWebsite("https://sketchfab.com/models/9537575aa7be4bd79302aef025c2abb2/embed?autostart=1&cardboard=1&internal=1&tracking=0&ui_infos=0&ui_snapshots=1&ui_stop=0&ui_watermark=0"));

        // Button 3: Redirect to example3.com
        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(v -> openWebsite("https://sketchfab.com/models/2d0328579da04b1d8bd14c2eebd52ca9/embed?autostart=1&cardboard=1&internal=1&tracking=0&ui_infos=0&ui_snapshots=1&ui_stop=0&ui_watermark=0"));

    }

    private void openWebsite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
