package com.example.basiclogintoapp.Fragments;

import static android.app.Activity.RESULT_OK;
import static android.service.controls.ControlsProviderService.TAG;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.basiclogintoapp.Activity_Shelf_2;
import com.example.basiclogintoapp.AddInventory;
import com.example.basiclogintoapp.AisleSelector;
import com.example.basiclogintoapp.CaseManagementSystem;
import com.example.basiclogintoapp.Courses;
import com.example.basiclogintoapp.Craft;
import com.example.basiclogintoapp.FacialRecog;
import com.example.basiclogintoapp.MainActivity;
import com.example.basiclogintoapp.MainActivity2;
import com.example.basiclogintoapp.MainActivity3;
import com.example.basiclogintoapp.MessageActivity;
import com.example.basiclogintoapp.Model.OrderItem;
import com.example.basiclogintoapp.Model.Users;
import com.example.basiclogintoapp.NewItemPrediction;
import com.example.basiclogintoapp.OfflineClass;
import com.example.basiclogintoapp.OrderPage;
import com.example.basiclogintoapp.Payment;
import com.example.basiclogintoapp.PendingCases;
import com.example.basiclogintoapp.PendingLectures;
import com.example.basiclogintoapp.PieChart1;
import com.example.basiclogintoapp.R;
import com.example.basiclogintoapp.ReportForm;
import com.example.basiclogintoapp.SendAlert;
import com.example.basiclogintoapp.Shelf3;
import com.example.basiclogintoapp.ShelfAssistant;
import com.example.basiclogintoapp.Travel;
import com.example.basiclogintoapp.UpdateItems;
import com.example.basiclogintoapp.UpdateReel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    TextView username;
    ImageView imageView;

    DatabaseReference reference;
    TextView t1;
    FirebaseUser fuser;

    RecyclerView recyclerView;
    RelativeLayout r1,r2,r3,r4,r5;
    // Profile Image
    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;

    String[] data4= new String[100];
    String[] data5= new String[100];
    String[] data6= new String[100];
    int count1=0;
    String[] Image2= new String[100];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imageView = view.findViewById(R.id.profile_image2);
        username  = view.findViewById(R.id.username);
        r1= view.findViewById(R.id.rel1);
        r2= view.findViewById(R.id.rel2);
        r3=view.findViewById(R.id.rel3);
        r4= view.findViewById(R.id.rel4);
        r5= view.findViewById(R.id.rel5);
// Assuming t1 is a TextView
        TextView t1 = view.findViewById(R.id.points);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers")
                .child(fuser.getUid());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("MyUsers").child(fuser.getUid()).child("points");

// Add a ValueEventListener to fetch the data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve the value from the dataSnapshot
                String pointsValue = dataSnapshot.getValue(String.class);

                // Set the retrieved value to t1
                t1.setText(pointsValue+" points");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(getActivity(), MainActivity3.class);
               startActivity(i);
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Courses.class);
                startActivity(i);
            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PieChart1.class);
                startActivity(i);
                Log.d(TAG, "onClick: " );
            }
        });
        r5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UpdateReel.class);
                startActivity(i);
            }
        });
        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish(); // Finish the current activity to prevent going back to it
            }
        });

        // Profile Image reference in storage
        storageReference = FirebaseStorage.getInstance().getReference("uploads");







        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Users user = dataSnapshot.getValue(Users.class);
                username.setText(user.getUsername());

                if (user.getImageURL().equals("default")){
                    imageView.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(getContext()).load(user.getImageURL()).into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });





        return view;
    }

    private void SelectImage() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, IMAGE_REQUEST);

    }


    private String getFileExtention(Uri uri){


        ContentResolver contentResolver =getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


    private void UploadMyImage(){


        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if(imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtention(imageUri));


            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if (!task.isSuccessful()){

                        throw  task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()){

                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(fuser.getUid());


                        HashMap<String, Object> map = new HashMap<>();

                        map.put("imageURL", mUri);
                        reference.updateChildren(map);
                        progressDialog.dismiss();
                    }else{


                        Toast.makeText(getContext(), "Failed!!", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });


        }else
        {
            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMAGE_REQUEST &&  resultCode == RESULT_OK
                && data != null && data.getData() != null){


            imageUri = data.getData();


            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Upload in progress..", Toast.LENGTH_SHORT).show();
            }else {

                UploadMyImage();
            }


        }
    }


}