package com.example.basiclogintoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddInventory extends AppCompatActivity {
    CardView c1,c2;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference storageReference;

    String imageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);
        FirebaseApp.initializeApp(this);
        storageReference = FirebaseStorage.getInstance().getReference();


        // Initialize c1 with the CardView using findViewById
        c1 = findViewById(R.id.submit);
        c2 = findViewById(R.id.photo);
        // Assuming you have a submit button with an ID 'submit'
        c1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
              updateDatabaseWithImage();
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage(view);
            }
        });
    }

    private String[] getAllEditTextValues() {
        // Retrieve the values from the EditText fields
        EditText itemNameEditText = findViewById(R.id.edittext1);
        EditText costPriceEditText = findViewById(R.id.edittext2);
        EditText sellingPriceEditText = findViewById(R.id.edittext3);
        EditText aisleEditText = findViewById(R.id.edittext4);
        EditText categoryEditText = findViewById(R.id.edittext5);
        EditText quantityEditText = findViewById(R.id.edittext6);
        EditText reviewEditText = findViewById(R.id.edittext7);
        EditText seasonEditText = findViewById(R.id.edittext8);

        // Extract values and store in an array
        String[] allEditTextValues = new String[9]; // Update the array size to accommodate the new EditText
        allEditTextValues[0] = itemNameEditText.getText().toString().trim();
        allEditTextValues[1] = costPriceEditText.getText().toString().trim();
        allEditTextValues[2] = sellingPriceEditText.getText().toString().trim();
        allEditTextValues[3] = aisleEditText.getText().toString().trim();
        allEditTextValues[4] = categoryEditText.getText().toString().trim();
        allEditTextValues[5] = quantityEditText.getText().toString().trim();
        allEditTextValues[6] = reviewEditText.getText().toString().trim();
        allEditTextValues[7] = seasonEditText.getText().toString().trim();
        allEditTextValues[8] = imageUrl;

        return allEditTextValues;
    }
    public void pickImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadImage();
        }
    }

    private void uploadImage() {
        if (imageUri != null) {
            // Create a reference to "images/[filename]"
            String filename = "images/" + System.currentTimeMillis() + ".jpg";
            StorageReference imageRef = storageReference.child(filename);

            // Upload the image
            UploadTask uploadTask = imageRef.putFile(imageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Image uploaded successfully, get download URL
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUrl = uri.toString();
                    // Use imageUrl as needed (e.g., display it in TextView)
                    TextView textView = findViewById(R.id.textView);
                    textView.setText("Image URL: " + imageUrl);
                });
            }).addOnFailureListener(e -> {
                // Handle failed image upload
                Toast.makeText(AddInventory.this, "Image upload failed", Toast.LENGTH_SHORT).show();
            });
        }
    }
    private void updateDatabaseWithImage() {
        // Call a method to retrieve and store the values
        String[] allEditTextValues = getAllEditTextValues();

        // Get a reference to your Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference inventoryRef = database.getReference("items");

        // Use itemName as the key for DatabaseReference
        DatabaseReference currentItemRef = inventoryRef.child(allEditTextValues[0]);
        currentItemRef.child("costPrice").setValue(allEditTextValues[1]);
        currentItemRef.child("sellingPrice").setValue(allEditTextValues[2]);
        currentItemRef.child("aisle").setValue(allEditTextValues[3]);
        currentItemRef.child("category").setValue(allEditTextValues[4]);
        currentItemRef.child("quantity").setValue(allEditTextValues[5]);
        currentItemRef.child("review").setValue(allEditTextValues[6]);
        currentItemRef.child("season").setValue(allEditTextValues[7]);
        currentItemRef.child("img").setValue(imageUrl); // Save the image URL directly
    }

}
