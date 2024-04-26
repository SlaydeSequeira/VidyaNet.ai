package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.basiclogintoapp.adapter.RecyclerAdapter3;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JobListing extends AppCompatActivity {
    private DatabaseReference mJobsReference;
    private RecyclerView recyclerView;
    private RecyclerAdapter3 recyclerAdapter;

    // Initialize arrays with a set size
    private static final int MAX_SIZE = 100;
    private String[] jobTitles = new String[MAX_SIZE];
    private String[] jobDescriptions = new String[MAX_SIZE];
    private String[] jobLocations = new String[MAX_SIZE];
    private int currentSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_listing);

        recyclerView = findViewById(R.id.recyclerView); // Get RecyclerView instance
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set layout manager

        mJobsReference = FirebaseDatabase.getInstance().getReference("Jobs");

        // Retrieve data from Firebase
        mJobsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentSize = 0; // Reset the counter

                // Extract job data from Firebase
                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    if (currentSize >= MAX_SIZE) {
                        break; // Avoid array index out of bounds
                    }

                    String jobTitle = jobSnapshot.child("job_title").getValue(String.class);
                    String jobDescription = jobSnapshot.child("job_description").getValue(String.class);
                    String jobLocation = jobSnapshot.child("location").getValue(String.class);

                    jobTitles[currentSize] = jobTitle;
                    jobDescriptions[currentSize] = jobDescription;
                    jobLocations[currentSize] = jobLocation;

                    currentSize++; // Increment the counter
                }

                // Initialize and set the adapter
                recyclerAdapter = new RecyclerAdapter3(JobListing.this, jobTitles, jobDescriptions, jobLocations, currentSize);
                recyclerView.setAdapter(recyclerAdapter); // Set adapter

                // Debug logs to confirm the data
                Log.d("JobListing", "Job Titles: " + java.util.Arrays.toString(jobTitles));
                Log.d("JobListing", "Job Descriptions: " + java.util.Arrays.toString(jobDescriptions));
                Log.d("JobListing", "Job Locations: " + java.util.Arrays.toString(jobLocations));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("JobListing", "Error fetching data", databaseError.toException());
            }
        });
    }
}
