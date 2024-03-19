package com.example.basiclogintoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateItems extends AppCompatActivity {

    private DatabaseReference itemsReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_items);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        itemsReference = database.getReference("item");

        final EditText editTextName = findViewById(R.id.edittext1);
        final EditText editTextDescription = findViewById(R.id.edittext2);
        final EditText editTextQty = findViewById(R.id.edittext3);
        Button btnUpdateItem = findViewById(R.id.button);
        Button addImg = findViewById(R.id.add);

        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the SelectImage method to choose an image from the device
                SelectImage();
            }
        });

        btnUpdateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                String qtyString = editTextQty.getText().toString().trim();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(qtyString)) {
                    int qty = Integer.parseInt(qtyString);

                    DatabaseReference newItemReference = itemsReference.push();
                    newItemReference.child("name").setValue(name);
                    newItemReference.child("description").setValue(description);
                    newItemReference.child("qty").setValue(qtyString);

                    // Call the method to upload the image to Cloud Storage and associate its URL
                    if (imageUri != null) {
                        uploadImageToStorage(imageUri, newItemReference);
                    }

                    incrementItemCount();

                    editTextName.getText().clear();
                    editTextDescription.getText().clear();
                    editTextQty.getText().clear();
                } else {
                    Toast.makeText(UpdateItems.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void incrementItemCount() {
        itemsReference.child("count").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long count = (long) snapshot.getValue();
                    itemsReference.child("count").setValue(count + 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });
    }

    private void SelectImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // Display a message or update UI to show that an image is selected if needed
        }
    }

    private void uploadImageToStorage(Uri imageUri, final DatabaseReference newItemReference) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("item_images");
        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

        UploadTask uploadTask = fileReference.putFile(imageUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    if (downloadUri != null) {
                        newItemReference.child("url").setValue(downloadUri.toString());
                    }
                } else {
                    Toast.makeText(UpdateItems.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
