package com.example.basiclogintoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.basiclogintoapp.adapter.ItemAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Craft extends AppCompatActivity {
    Button b1, b2, b3, b4, b5;
    DatabaseReference itemsReference;
    String count;
    int itemCount = 0;
    private String[] aisleValues = new String[100];

    String title[] = new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craft);
        Objects.requireNonNull(getSupportActionBar()).hide();

        b1 = findViewById(R.id.c1);
        b2 = findViewById(R.id.c2);
        b3 = findViewById(R.id.c3);
        b4 = findViewById(R.id.c4);
        b5 = findViewById(R.id.c5);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mess("Stick", 1, "Coal", 1);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mess("Stone", 1, "Stick", 2);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mess("Stone", 3, "Stick", 2);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mess("Brick", 4, "Cement", 1);
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mess("Wool", 3, "Wood", 3);
            }
        });
    }

    private void mess(String itemName1, int qty1, String itemName2, int qty2) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        itemsReference = database.getReference("item");

        itemsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean item1Found = false;
                boolean item2Found = false;
                int remainingQty1 = 0;
                int remainingQty2 = 0;

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if (childSnapshot.hasChild("name") && childSnapshot.hasChild("qty")) {
                        String name = String.valueOf(childSnapshot.child("name").getValue());
                        String quantity = String.valueOf(childSnapshot.child("qty").getValue());

                        if (name.equals(itemName1)) {
                            item1Found = true;
                            remainingQty1 = Integer.parseInt(quantity) - qty1;
                        } else if (name.equals(itemName2)) {
                            item2Found = true;
                            remainingQty2 = Integer.parseInt(quantity) - qty2;
                        }
                    }
                }

                if (item1Found && item2Found && remainingQty1 >= 0 && remainingQty2 >= 0) {
                    // Both items exist and their quantities are greater than or equal to required quantities
                    msg("Craftable");
                } else {
                    // Display the required item name and quantity that is left
                    StringBuilder message = new StringBuilder("Required items:\n");
                    if (!item1Found || remainingQty1 < 0) {
                        message.append(itemName1).append(": ").append(Math.max(0, qty1 - remainingQty1)).append(" more needed\n");
                    }
                    if (!item2Found || remainingQty2 < 0) {
                        message.append(itemName2).append(": ").append(Math.max(0, qty2 - remainingQty2)).append(" more needed\n");
                    }
                    //Toast.makeText(Craft.this, message.toString(), Toast.LENGTH_LONG).show();
                    msg(message.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });
    }

    private void msg(String craftable) {
        Intent intent = new Intent(Craft.this, Craftable.class);
        intent.putExtra("message", craftable); // Pass the message to the new Activity
        startActivity(intent);
    }

}
