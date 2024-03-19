package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ShopInfo extends AppCompatActivity {
    TextView t0,t1,t2;
    Button b1,b2;
    EditText e1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);

        // Receive data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String markerTitle = intent.getStringExtra("markerTitle");
            double markerLat = intent.getDoubleExtra("markerLat", 0.0);
            double markerLng = intent.getDoubleExtra("markerLng", 0.0);
            t0= findViewById(R.id.textView0);
            t1= findViewById(R.id.textView2);
            t2= findViewById(R.id.textView4);
            b1 = findViewById(R.id.submitButton);
            b2 = findViewById(R.id.payButton);
            b2= findViewById(R.id.payButton);
            e1 = findViewById(R.id.edittext1);
            t0.setText(markerTitle);
            String distanceString = String.valueOf((int) (Math.sqrt(Math.pow(markerLat - 19.04444, 2) + Math.pow(markerLng - 72.820563, 2)) * 120000));
            t1.setText(distanceString);
            t2.setText(distanceString);
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ShopInfo.this,Payment.class);
                    int b = Integer.parseInt(distanceString);
                    i.putExtra("Cost",b);
                    startActivity(i);
                }
            });
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String editTextValue = e1.getText().toString();

                    // Parse the editTextValue to an integer
                    int a = Integer.parseInt(editTextValue);

                    // Calculate distanceString/a and set the result to t2
                    t2.setText(String.valueOf((int) ((double) Integer.parseInt(distanceString) / a)));
                }
            });
        }
    }
}
