package com.example.basiclogintoapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class AddMedicine extends AppCompatActivity {
    private TextInputEditText medicineNameInput, quantityInput;
    private Spinner frequencySpinner;
    private RadioGroup timeRadioGroup;
    private TextInputEditText noteInput;
    private Button saveButton;
    private FirebaseDatabase database;
    private DatabaseReference medicinesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        // Initialize views
        medicineNameInput = findViewById(R.id.medicineNameInput);
        quantityInput = findViewById(R.id.quantityInput); // Added
        frequencySpinner = findViewById(R.id.frequencySpinner);
        timeRadioGroup = findViewById(R.id.timeRadioGroup);
        noteInput = findViewById(R.id.noteInput);
        saveButton = findViewById(R.id.saveButton);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        medicinesRef = database.getReference("medicines");

        // Setup frequency spinner
        String[] frequencies = new String[] {
                "Everyday",
                "3 times a week",
                "2 times a week",
                "Once a week"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                frequencies
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(adapter);

        // Handle save button click
        saveButton.setOnClickListener(v -> saveMedicine());
    }

    private void saveMedicine() {
        // Get values from form
        String medicineName = medicineNameInput.getText().toString().trim();
        String frequency = frequencySpinner.getSelectedItem().toString();
        String note = noteInput.getText().toString().trim();
        String quantity = quantityInput.getText().toString().trim(); // Get quantity

        // Get selected time of day
        String timeOfDay;
        int selectedTimeId = timeRadioGroup.getCheckedRadioButtonId();
        if (selectedTimeId == R.id.morningRadio) {
            timeOfDay = "Morning";
        } else if (selectedTimeId == R.id.afternoonRadio) {
            timeOfDay = "Afternoon";
        } else if (selectedTimeId == R.id.eveningRadio) {
            timeOfDay = "Evening";
        } else if (selectedTimeId == R.id.nightRadio) {
            timeOfDay = "Night";
        } else {
            timeOfDay = "";
        }

        // Validate inputs
        if (medicineName.isEmpty()) {
            medicineNameInput.setError("Please enter medicine name");
            return;
        }

        if (quantity.isEmpty()) {
            quantityInput.setError("Please enter quantity");
            return;
        }

        if (selectedTimeId == -1) {
            Toast.makeText(this, "Please select time of day", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a Medicine object
        Medicine medicine = new Medicine(medicineName, frequency, timeOfDay, note, quantity);

        // Push data to Firebase Realtime Database
        String medicineId = medicinesRef.push().getKey();
        if (medicineId != null) {
            medicinesRef.child(medicineId).setValue(medicine)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Medicine added successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to add medicine", Toast.LENGTH_SHORT).show());
        }
    }

    // Medicine model class with quantity
    public static class Medicine {
        public String name;
        public String frequency;
        public String timeOfDay;
        public String note;
        public String quantity;

        public Medicine(String name, String frequency, String timeOfDay, String note, String quantity) {
            this.name = name;
            this.frequency = frequency;
            this.timeOfDay = timeOfDay;
            this.note = note;
            this.quantity = quantity;
        }
    }
}
