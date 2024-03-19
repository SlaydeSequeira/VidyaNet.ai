package com.example.basiclogintoapp.Fragments;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.basiclogintoapp.R;

import java.util.ArrayList;
import java.util.Map;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basiclogintoapp.adapter.ItemAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// Home2Fragment.java
public class Home2Fragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home2, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("items");

        FirebaseRecyclerOptions<Map<String, Object>> options =
                new FirebaseRecyclerOptions.Builder<Map<String, Object>>()
                        .setQuery(databaseReference, new SnapshotParser<Map<String, Object>>() {
                            @NonNull
                            @Override
                            public Map<String, Object> parseSnapshot(@NonNull DataSnapshot snapshot) {
                                // Assuming snapshot.getKey() gives the item name
                                String itemName = snapshot.getKey();
                                Map<String, Object> itemMap = (Map<String, Object>) snapshot.getValue();
                                itemMap.put("itemName", itemName);

                                // Toast the values
                                toastValues(itemMap);

                                return itemMap;
                            }
                        })
                        .build();

      //  adapter = new ItemAdapter(options);
        recyclerView.setAdapter(adapter);

        return rootView;
    }


    private void toastValues(Map<String, Object> itemMap) {
        String itemName = String.valueOf(itemMap.get("itemName"));
        String img = String.valueOf(itemMap.get("img"));

        // Use Long instead of Integer for CP and SP
        Long CP = (Long) itemMap.get("CP");
        Long SP = (Long) itemMap.get("SP");
        String cp= String.valueOf(CP);
        String sp = String.valueOf(SP);

        String aisle = String.valueOf(itemMap.get("aisle"));
        String category = String.valueOf(itemMap.get("category"));

        // Use Long instead of Integer for quantity
        Long quantity = (Long) itemMap.get("quantity");
        String review = String.valueOf(itemMap.get("review"));

        // Toast the values
        String toastMessage = "Item: " + itemName + "\n" +
                "Image: " + img + "\n" +
                "CP: " + CP + "\n" +
                "SP: " + SP + "\n" +
                "Aisle: " + aisle + "\n" +
                "Category: " + category + "\n" +
                "Quantity: " + quantity + "\n" +
                "Review: " + review;

        Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        //adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //adapter.stopListening();
    }
}

