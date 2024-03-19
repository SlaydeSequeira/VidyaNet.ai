package com.example.basiclogintoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShelfAssistant extends AppCompatActivity {

    private EditText editText;
    private ImageView searchButton;

    // Firebase
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf_assistant);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("items");

        // Reference to UI elements
        editText = findViewById(R.id.edittext);
        searchButton = findViewById(R.id.image);
        FloatingActionButton fab = findViewById(R.id.centerButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddItemDialog();
            }
        });
        markAllAisles();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInFirebase();
            }
        });
    }

    private void searchInFirebase() {
        final String userEnteredText = editText.getText().toString().trim();

        // Check if the user entered a search term
        if (!userEnteredText.isEmpty()) {
            // Look for the entered item in the Firebase database
            databaseReference.child(userEnteredText).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        Long aisleNumberLong = dataSnapshot.child("aisle").getValue(Long.class);
                        if (aisleNumberLong != null) {
                            String aisleNumber = String.valueOf(aisleNumberLong);
                            String buttonId = getButtonId(aisleNumber);
                            if (buttonId != null) {
                                Button button = findViewById(getResources().getIdentifier(buttonId, "id", getPackageName()));
                                if (button != null) {
                                    highlightButton(button);
                                } else {
                                    showToast("Button not found for aisle " + aisleNumber);
                                }
                            } else {
                                showToast("Aisle not found");
                            }
                        }
                    } else {
                        showToast("Item not found");
                    }
                }

                private String getButtonId(String aisleNumber) {
                    // Generate the button ID based on the aisle number
                    switch (aisleNumber) {
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                        case "5":
                        case "6":
                        case "7":
                        case "8":
                            return "A" + aisleNumber;
                        case "11":
                        case "12":
                        case "13":
                        case "14":
                        case "15":
                        case "16":
                        case "17":
                        case "18":
                            return "B" + (Integer.parseInt(aisleNumber) - 10);
                        case "21":
                        case "22":
                        case "23":
                        case "24":
                        case "25":
                        case "26":
                        case "27":
                        case "28":
                            return "C" + (Integer.parseInt(aisleNumber) - 20);
                        case "31":
                        case "32":
                        case "33":
                        case "34":
                        case "35":
                        case "36":
                        case "37":
                        case "38":
                            return "D" + (Integer.parseInt(aisleNumber) - 30);
                        default:
                            return null; // Invalid aisle number
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showToast("Database error");
                }
            });
        } else {
            showToast("Please enter a search term");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void highlightButton(Button button) {
        // Set the background color of the button when highlighted
        button.setBackgroundColor(getResources().getColor(R.color.material_green)); // Replace R.color.material_green with your actual color resource ID

        // You can also set other visual properties if needed, such as text color, etc.
    }
    private void markAllAisles() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Reset the background color of all buttons before marking

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Long aisleNumberLong = itemSnapshot.child("aisle").getValue(Long.class);

                    if (aisleNumberLong != null) {
                        String aisleNumber = String.valueOf(aisleNumberLong);
                        String buttonId = getButtonId(aisleNumber);

                        if (buttonId != null) {
                            Button button = findViewById(getResources().getIdentifier(buttonId, "id", getPackageName()));
                            if (button != null) {
                                if (!itemSnapshot.getKey().equals(editText.getText().toString().trim())) {
                                    // Highlight buttons for aisles with items different from the searched item
                                    button.setBackgroundColor(getResources().getColor(R.color.custom_circle_color)); // Replace R.color.material_red with your actual color resource ID
                                }
                            } else {
                                showToast("Button not found for aisle " + aisleNumber);
                            }
                        } else {
                            showToast("Aisle not found");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Database error");
            }
        });

    }
    private String getButtonId(String aisleNumber) {
        // Generate the button ID based on the aisle number
        switch (aisleNumber) {
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
                return "A" + aisleNumber;
            case "11":
            case "12":
            case "13":
            case "14":
            case "15":
            case "16":
            case "17":
            case "18":
                return "B" + (Integer.parseInt(aisleNumber) - 10);
            case "21":
            case "22":
            case "23":
            case "24":
            case "25":
            case "26":
            case "27":
            case "28":
                return "C" + (Integer.parseInt(aisleNumber) - 20);
            case "31":
            case "32":
            case "33":
            case "34":
            case "35":
            case "36":
            case "37":
            case "38":
                return "D" + (Integer.parseInt(aisleNumber) - 30);
            default:
                return null; // Invalid aisle number
        }
    }
    private void showAddItemDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_item, null);
        dialogBuilder.setView(dialogView);

        final EditText itemNameEditText = dialogView.findViewById(R.id.editTextItemName);
        final EditText aisleNumberEditText = dialogView.findViewById(R.id.editTextAisleNumber);

        dialogBuilder.setTitle("Add Item");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Add your logic to handle the entered item name and aisle number
                String itemName = itemNameEditText.getText().toString();
                String aisleNumber = aisleNumberEditText.getText().toString();

                if (!itemName.isEmpty() && !aisleNumber.isEmpty()) {
                    updateAisleNumberInFirebase(itemName, aisleNumber);
                } else {
                    showToast("Please enter both item name and aisle number");
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled
            }
        });

        AlertDialog addItemDialog = dialogBuilder.create();
        addItemDialog.show();
    }

    private void updateAisleNumberInFirebase(final String itemName, final String newAisleNumber) {
        // Check if the item exists in the Firebase database
        databaseReference.child(itemName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Item found, update the aisle number
                    dataSnapshot.getRef().child("aisle").setValue(Long.parseLong(newAisleNumber));
                    showToast("Aisle number updated successfully");

                    // Optionally, you can update the UI or perform other actions here
                } else {
                    showToast("Item not found in the database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Database error");
            }
        });
    }
}
