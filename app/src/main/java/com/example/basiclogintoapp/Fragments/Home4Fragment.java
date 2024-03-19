package com.example.basiclogintoapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.basiclogintoapp.R;
import com.example.basiclogintoapp.adapter.DataAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class Home4Fragment extends Fragment {
    private String[] data1 = new String[100];
    private String[] data2 = new String[100];
    private String[] data3 = new String[100];
    private String[] data4 = new String[100];

    private int count = 0; // Counter variable

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home4, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Clear previous items
        clearData();

        // Fetch data from Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("MyUsers");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey(); // Get uid
                    String username = snapshot.child("username").getValue(String.class); // Get username
                    String url = snapshot.child("imageURL").getValue(String.class); // Get imageURL
                    String pts = snapshot.child("points").getValue(String.class);
                    if (uid != null && username != null) {
                        data1[count] = uid;
                        data2[count] = username;
                        data3[count]= url;
                        data4[count]= pts;
                        count++;
                    }
                }
                // Create an adapter for the dataset and set it to RecyclerView
                String[] uids = new String[count];

                String[] usernames = new String[count];
                String[] imageURLs = new String[count];
                String[] points = new String[count];
                System.arraycopy(data1, 0, uids, 0, count);
                System.arraycopy(data2, 0, usernames, 0, count);
                System.arraycopy(data3, 0, imageURLs, 0, count);
                System.arraycopy(data4, 0, points, 0, count);
                recyclerView.setAdapter(new DataAdapter(getActivity(),usernames, imageURLs, points,uids));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        return view;
    }

    // Method to clear previous data
    private void clearData() {
        Arrays.fill(data1, null);
        Arrays.fill(data2, null);
        Arrays.fill(data3, null);
        Arrays.fill(data4, null);
        count = 0;
    }
}
