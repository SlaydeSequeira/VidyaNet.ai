package com.example.basiclogintoapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewMedicine extends AppCompatActivity {
    private CalendarView calendarView;
    private FirebaseDatabase database;
    private DatabaseReference medicinesRef;
    private Map<String, List<Medicine>> medicinesByDay;
    private TextView medicinesTextView;  // TextView to display medicines

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medicine);

        calendarView = findViewById(R.id.calendarView);
        medicinesTextView = findViewById(R.id.medicinesTextView);  // Initialize TextView
        database = FirebaseDatabase.getInstance();
        medicinesRef = database.getReference("medicines");
        medicinesByDay = new HashMap<>();

        fetchMedicinesAndDisplay();
    }

    private void fetchMedicinesAndDisplay() {
        medicinesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                medicinesByDay.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Medicine medicine = snapshot.getValue(Medicine.class);
                    if (medicine != null) {
                        // Add medicine to appropriate days based on frequency
                        addMedicineToSchedule(medicine);
                    }
                }

                // Set up calendar listener after data is loaded
                setupCalendarListener();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewMedicine.this, "Failed to load medicines: " +
                        databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMedicineToSchedule(Medicine medicine) {
        switch (medicine.frequency) {
            case "Everyday":
                addToDay("MON", medicine);
                addToDay("TUE", medicine);
                addToDay("WED", medicine);
                addToDay("THU", medicine);
                addToDay("FRI", medicine);
                addToDay("SAT", medicine);
                addToDay("SUN", medicine);
                break;
            case "3 times a week":
                addToDay("MON", medicine);
                addToDay("WED", medicine);
                addToDay("FRI", medicine);
                break;
            case "2 times a week":
                addToDay("TUE", medicine);
                addToDay("THU", medicine);
                break;
            case "Once a week":
                addToDay("SUN", medicine);
                break;
        }
    }

    private void addToDay(String day, Medicine medicine) {
        if (!medicinesByDay.containsKey(day)) {
            medicinesByDay.put(day, new ArrayList<>());
        }
        medicinesByDay.get(day).add(medicine);
    }

    private void setupCalendarListener() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String dayOfWeek = getDayOfWeek(year, month, dayOfMonth);
            List<Medicine> medicines = medicinesByDay.get(dayOfWeek);

            if (medicines != null && !medicines.isEmpty()) {
                StringBuilder medicineText = new StringBuilder("Medicines for today:\n");
                for (Medicine medicine : medicines) {
                    medicineText.append("- ").append(medicine.name)
                            .append(" (").append(medicine.timeOfDay).append(")\n");
                    if (medicine.note != null && !medicine.note.isEmpty()) {
                        medicineText.append("  Note: ").append(medicine.note).append("\n");
                    }
                }
                medicinesTextView.setText(medicineText.toString());  // Update the TextView

                // Send the notification with the list of medicines for the selected day
                sendNotification(medicineText.toString());
            } else {
                medicinesTextView.setText("No medicines scheduled for this day.");  // Update if no medicines

                // Send a notification that no medicines are scheduled for the selected day
                sendNotification("No medicines scheduled for today.");
            }
        });
    }


    private String getDayOfWeek(int year, int month, int dayOfMonth) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        int dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case java.util.Calendar.MONDAY: return "MON";
            case java.util.Calendar.TUESDAY: return "TUE";
            case java.util.Calendar.WEDNESDAY: return "WED";
            case java.util.Calendar.THURSDAY: return "THU";
            case java.util.Calendar.FRIDAY: return "FRI";
            case java.util.Calendar.SATURDAY: return "SAT";
            case java.util.Calendar.SUNDAY: return "SUN";
            default: return "";
        }
    }
    private void sendNotification(String a) {
        // Create a NotificationManager system service
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Build the notification
        Notification notification = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle("You Need to Take Medicine")
                .setContentText(a)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        // Show the notification
        if (notificationManager != null) {
            notificationManager.notify(1, notification);  // 1 is the notification ID
        }
    }
    // Medicine model class
    public static class Medicine {
        public String name;
        public String frequency;
        public String timeOfDay;
        public String note;
        public String quantity;

        // Required empty constructor for Firebase
        public Medicine() {
        }

        public Medicine(String name, String frequency, String timeOfDay, String note, String quantity) {
            this.name = name;
            this.frequency = frequency;
            this.timeOfDay = timeOfDay;
            this.note = note;
            this.quantity = quantity;
        }
    }
}
