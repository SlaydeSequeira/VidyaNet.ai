package com.example.basiclogintoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.basiclogintoapp.adapter.ColorAdapter;
import com.example.basiclogintoapp.adapter.ItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;


public class PieChart1 extends AppCompatActivity {
    // Create the object of TextView and PieChart class
    private Map<String, Integer> titleColors = new HashMap<>();
    private ColorAdapter colorAdapter;
    PieChart pieChart;
    RecyclerView recyclerView;
    DatabaseReference itemsReference;
    String title[] = new String[100];
    private String[] imgUrls = new String[100];
    private String[] cpValues = new String[100];
    private String[] spValues = new String[100];
    private String[] aisleValues = new String[100];
    private String[] categoryValues = new String[100];
    private String[] quantityValues = new String[100];
    private String[] reviewValues = new String[100];

    String count;
    int itemCount = 0; // Counter for the number of items
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        Objects.requireNonNull(getSupportActionBar()).hide();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.parseColor("#F6F6F6"));
        }
        recyclerView = findViewById(R.id.recyclerView);
        pieChart = findViewById(R.id.piechart);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        itemsReference = database.getReference("item");

        itemsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = String.valueOf(snapshot.child("count").getValue());
                itemCount = Integer.parseInt(count);
                int i=0;
                //Toast.makeText(getActivity(),count,Toast.LENGTH_SHORT).show();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                    // Check if the child has the required fields
                    if (childSnapshot.hasChild("name")) {
                        // Assuming "name", "description", and "qty" are children under each item
                        String name = String.valueOf(childSnapshot.child("name").getValue());
                        String des = String.valueOf(childSnapshot.child("description").getValue());
                        String quantity = String.valueOf(childSnapshot.child("qty").getValue());
                        String url = String.valueOf(childSnapshot.child("url").getValue());
                        title[i]=name;
                        imgUrls[i]=url;
                        aisleValues[i]=quantity;
                        // You can add additional checks for other fields if needed

                        // Toast.makeText(getActivity(), name + " " + des + " " + quantity, Toast.LENGTH_SHORT).show();
                    }
                    i++;


                }
                setData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }

        });
        RecyclerView colorRecyclerView = findViewById(R.id.recyclerView);
        colorAdapter = new ColorAdapter(titleColors);
        colorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        colorRecyclerView.setAdapter(colorAdapter);
    }

    private void setData() {
        // ... (existing code)
pieChart.clearChart();
        for (int i = 0; i < itemCount; i++) {
            if (title[i] != null && aisleValues[i] != null) {
                String currentTitle = title[i];
                int currentAisleValue = Integer.parseInt(aisleValues[i]);

                if (!titleColors.containsKey(currentTitle)) {
                    int currentColor = Color.parseColor(getRandomColor());
                    titleColors.put(currentTitle, currentColor);
                }

                int currentColor = titleColors.get(currentTitle);

                pieChart.addPieSlice(
                        new PieModel(
                                currentTitle,
                                currentAisleValue,
                                currentColor));
            }
        }

        // Notify the adapter that the data has changed
        colorAdapter.notifyDataSetChanged();

        pieChart.startAnimation();
    }

    // Helper method to generate a random color
    private String getRandomColor() {
        Random random = new Random();
        return String.format("#%06x", random.nextInt(0xFFFFFF + 1));
    }

}