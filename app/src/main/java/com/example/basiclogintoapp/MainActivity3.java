package com.example.basiclogintoapp;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basiclogintoapp.adapter.DumpTruckAdapter;
import com.example.basiclogintoapp.adapter.TempAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
public class MainActivity3 extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText editText;
    ImageView imageView;

    DumpTruckAdapter dumpTruckAdapter;

    // Define your static locations
    String[] address = {"Slayde", "Chhand", "Aryan","Sharvin"};
    String[] location = {"Location 1", "Location 2", "Location 3","Location 4"};

    String[] images ={"https://firebasestorage.googleapis.com/v0/b/basiclogintoapp.appspot.com/o/IMG_20240316_214436.jpg?alt=media&token=2a3411a3-1cff-44be-b418-b04a2b0667a1","https://firebasestorage.googleapis.com/v0/b/basiclogintoapp.appspot.com/o/IMG_20240316_214428.jpg?alt=media&token=7f4cb9ce-0cf1-4ef5-b924-47990561d4d9","https://firebasestorage.googleapis.com/v0/b/basiclogintoapp.appspot.com/o/IMG_20240316_214442.jpg?alt=media&token=a647a6d0-a7f7-40d8-ba54-9708973053b4","https://firebasestorage.googleapis.com/v0/b/basiclogintoapp.appspot.com/o/IMG_20240316_214430.jpg?alt=media&token=47d55cf8-447b-482e-87b5-a982e8f689ce"};
    String[] dis = {"1.2 kms", "3.5 kms", "5.7 kms","3.3km"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Objects.requireNonNull(getSupportActionBar()).hide();

        recyclerView = findViewById(R.id.recyclerView1);
        RecyclerView recyclerView2 = findViewById(R.id.recyclerView2);

        dumpTruckAdapter = new DumpTruckAdapter(MainActivity3.this, address, address.length, images, location, dis);
        // Set up ItemTouchHelper with dumpTruckAdapter
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeController(dumpTruckAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        editText = findViewById(R.id.edittext);
        imageView = findViewById(R.id.image);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(MainActivity3.this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setAdapter(dumpTruckAdapter);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(MainActivity3.this);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView2.setLayoutManager(layoutManager2);

        // Initially set adapter with all data
        TempAdapter tempAdapter = new TempAdapter(MainActivity3.this, address, address.length, location, location, dis);
        recyclerView2.setAdapter(tempAdapter);

        // Implement filtering on edit text change
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String searchQuery = editText.getText().toString().toLowerCase();
                ArrayList<String> filteredAddresses = new ArrayList<>();
                ArrayList<String> filteredLocations = new ArrayList<>();
                ArrayList<String> filteredDistances = new ArrayList<>();

                // Filter data based on search query
                for (int i = 0; i < address.length; i++) {
                    if (location[i].toLowerCase().contains(searchQuery) || address[i].toLowerCase().contains(searchQuery)) {
                        filteredAddresses.add(address[i]);
                        filteredLocations.add(location[i]);
                        filteredDistances.add(dis[i]);
                    }
                }

                // Update RecyclerView2 with filtered data
                TempAdapter tempAdapter = new TempAdapter(MainActivity3.this, filteredAddresses.toArray(new String[0]),
                        filteredAddresses.size(), filteredLocations.toArray(new String[0]),
                        filteredLocations.toArray(new String[0]), filteredDistances.toArray(new String[0]));
                recyclerView2.setAdapter(tempAdapter);

                return false;
            }
        });

        // Implement filtering on image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = editText.getText().toString().toLowerCase();
                ArrayList<String> filteredAddresses = new ArrayList<>();
                ArrayList<String> filteredLocations = new ArrayList<>();
                ArrayList<String> filteredDistances = new ArrayList<>();

                // Filter data based on search query
                for (int i = 0; i < address.length; i++) {
                    if (location[i].toLowerCase().contains(searchQuery) || address[i].toLowerCase().contains(searchQuery)) {
                        filteredAddresses.add(address[i]);
                        filteredLocations.add(location[i]);
                        filteredDistances.add(dis[i]);
                    }
                }

                // Update RecyclerView2 with filtered data
                TempAdapter tempAdapter = new TempAdapter(MainActivity3.this, filteredAddresses.toArray(new String[0]),
                        filteredAddresses.size(), filteredLocations.toArray(new String[0]),
                        filteredLocations.toArray(new String[0]), filteredDistances.toArray(new String[0]));
                recyclerView2.setAdapter(tempAdapter);
            }
        });
    }

    public class SwipeController extends ItemTouchHelper.Callback {
        private final DumpTruckAdapter adapter;

        public SwipeController(DumpTruckAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeMovementFlags(0, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            // Do nothing since we don't support moving items
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT) {
                Intent intent = new Intent(MainActivity3.this, MessageActivity.class);
                if(position==3) {
                    intent.putExtra("userid", "iOssDujOa2Q2FSSp4hOO2R179zf1");
                    startActivity(intent);
                    finish();
                }
                if(position==2) {
                    intent.putExtra("userid", "fr3zRVuGL2T79ihHlcpH4imPzDj1");
                    startActivity(intent);
                    finish();
                }
                if(position==1) {
                    intent.putExtra("userid", "vrszPY65eDYgOMgVFkhMrc2Xkvu1");
                    startActivity(intent);
                    finish();
                }
                if(position==0) {
                    intent.putExtra("userid", "kGKXF4bxwmP8LYtlLv8r2JCfbel2");
                    startActivity(intent);
                    finish();
                }
            } else if (direction == ItemTouchHelper.RIGHT) {
                Intent intent = new Intent(MainActivity3.this, Map.class);
                intent.putExtra("pos",position);
                startActivity(intent);
                finish();
            }
        }
    }
}
