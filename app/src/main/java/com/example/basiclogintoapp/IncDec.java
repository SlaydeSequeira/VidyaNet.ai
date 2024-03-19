package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IncDec extends AppCompatActivity {
    CardView c1, c2;
    private DatabaseReference databaseReference;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inc_dec);
        c1 = findViewById(R.id.Increment);
        c2 = findViewById(R.id.Decrement);
        EditText et1 = findViewById(R.id.edittext1);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("item");
        showStaticNotification();

        // Retrieve title from Intent
        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra("title");

            // Check if title is not null
            if (title != null) {
                // Call method to update quantity
                // Note: You can choose to update the quantity here as well based on some default action.
                // For example, updateQuantity(title, 1) to increment by default.
            } else {
                Toast.makeText(this, "Title is null", Toast.LENGTH_SHORT).show();
            }
        }

        // Set click listeners for the cards
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Increment button tapped
                updateQuantity(title, Integer.parseInt(et1.getText().toString()));
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Decrement button tapped
                updateQuantity(title, -Integer.parseInt(et1.getText().toString()));
            }
        });
    }

    private void updateQuantity(final String title, final int change) {
        // Query to find the item with the matching title
        databaseReference.orderByChild("name").equalTo(title).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate through the matching items
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    // Get the current quantity and apply the change
                    int currentQty = itemSnapshot.child("qty").getValue(Integer.class);
                    int newQty = currentQty + change;

                    // Update the quantity in the database
                    itemSnapshot.getRef().child("qty").setValue(newQty);

                    // Show appropriate message based on increment or decrement
                    if (change > 0) {
                        Toast.makeText(IncDec.this, "Quantity incremented for " + title, Toast.LENGTH_SHORT).show();
                    } else if (change < 0) {
                        Toast.makeText(IncDec.this, "Quantity decremented for " + title, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(IncDec.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showStaticNotification() {
        // Intent to launch when notification is clicked
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("My App")
                .setContentText("This is a static notification")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Show the notification
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(1, builder.build());
    }
}
