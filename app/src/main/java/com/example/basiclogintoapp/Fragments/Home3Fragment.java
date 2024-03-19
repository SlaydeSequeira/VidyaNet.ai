package com.example.basiclogintoapp.Fragments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.basiclogintoapp.HomePage;
import com.example.basiclogintoapp.MainActivity;
import com.example.basiclogintoapp.R;
import com.example.basiclogintoapp.adapter.ItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Home3Fragment extends Fragment {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home3, container, false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        itemsReference = database.getReference("item");

        itemsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = String.valueOf(snapshot.child("count").getValue());
                itemCount = Integer.parseInt(count);
                int i = 0;
                //Toast.makeText(getActivity(),count,Toast.LENGTH_SHORT).show();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                    // Check if the child has the required fields
                    if (childSnapshot.hasChild("name")) {
                        // Assuming "name", "description", and "qty" are children under each item
                        String name = String.valueOf(childSnapshot.child("name").getValue());
                        String quantity = String.valueOf(childSnapshot.child("qty").getValue());
                        String url = String.valueOf(childSnapshot.child("url").getValue());
                        // Assuming "ttl" is the variable name for the "des" value
                        String des = String.valueOf(childSnapshot.child("ttl").getValue());

                        if (des != null) {
                            try {
                                int ttl = Integer.parseInt(des);

                                // Check if the item is expiring within 5 days
                                if (ttl < 5) {
                                    // Generate a message for the toast
                                    String message = name + " is expiring in " + ttl + " day" + (ttl > 1 ? "s" : "");
                                    showNotification(message);

                                    // Show a toast with the message
                                    //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                // Handle the case where "ttl" is not a valid integer
                            }
                        }

                        categoryValues[i] = des;
                        title[i] = name;
                        imgUrls[i] = url;
                        aisleValues[i] = quantity;
                        cpValues[i] = String.valueOf(101 + i);

                        // You can add additional checks for other fields if needed

                        // Toast.makeText(getActivity(), name + " " + des + " " + quantity, Toast.LENGTH_SHORT).show();
                    }
                    i++;

                    RecyclerView recyclerView;
                    recyclerView = view.findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    // Initialize the adapter with empty data arrays
                    ItemAdapter adapter = new ItemAdapter(getContext(), itemCount, title, imgUrls, cpValues, aisleValues, categoryValues, title, title, title);

                    // Set the adapter to the recyclerView
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });


        return view;
    }

    private void clearArrays() {
        // Clear arrays before populating them with new data
        Arrays.fill(title, null);
        Arrays.fill(imgUrls, null);
        Arrays.fill(cpValues, null);
        Arrays.fill(spValues, null);
        Arrays.fill(aisleValues, null);
        Arrays.fill(categoryValues, null);
        Arrays.fill(quantityValues, null);
        Arrays.fill(reviewValues, null);

        // Reset the itemCount counter
        itemCount = 0;
    }

    private void updateRecyclerView() {
        // Update the RecyclerView adapter with the new data
        ItemAdapter adapter = new ItemAdapter(getContext(), itemCount, title, imgUrls, cpValues, aisleValues, aisleValues, title, title, title);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
    }

    private void showNotification(String message) {
        // Create a notification channel
        String channelId = "channel_id";
        CharSequence channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }

        // Create an intent to open the app when the notification is clicked
        Intent intent = new Intent(getContext(), HomePage.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getContext(),
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Create the notification
        Notification notification = new Notification.Builder(getContext(), channelId)
                .setContentTitle("Expiry Alert")
                .setContentText(message)
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        // Show the notification
        if (notificationManager != null) {
            notificationManager.notify(0, notification);
        }
    }
}
