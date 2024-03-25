package com.example.basiclogintoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class PendingLectures extends AppCompatActivity {
ProgressBar progressBar1,progressBar2,progressBar3;
TextView t1,t2,t3;
int a1,a2,a3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_lectures);
        Objects.requireNonNull(getSupportActionBar()).hide();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.parseColor("#F6F6F6"));
        }
        // Find the card views
        CardView card1 = findViewById(R.id.card1);
        CardView card2 = findViewById(R.id.card2);
        CardView card3 = findViewById(R.id.card3);
        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar3 = findViewById(R.id.progressBar3);
        t1 = findViewById(R.id.text1);
        t2 = findViewById(R.id.text2);
        t3 = findViewById(R.id.text3);


        // Set initial progress values (0%)
        progressBar1.setProgress(0);
        progressBar2.setProgress(0);
        progressBar3.setProgress(0);
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dr = firebaseDatabase.getReference("MyUsers").child(fuser.getUid()).child("course");
        dr.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String a =String.valueOf(snapshot.child("1").child("0").getValue());
                a1= (int) (25*Integer.parseInt(a));
                String b =String.valueOf(snapshot.child("1").child("1").getValue());
                a1= (int) (a1+25*Integer.parseInt(b));
                String c =String.valueOf(snapshot.child("1").child("2").getValue());
                a1= (int) (a1+25*Integer.parseInt(c));
                String d =String.valueOf(snapshot.child("1").child("3").getValue());
                a1= (int) (a1+25*Integer.parseInt(d));
                    progressBar1.setProgress(a1);
                    t1.setText(String.valueOf(a1)+"%");

                String e =String.valueOf(snapshot.child("2").child("0").getValue());
                a2= (int) (25*Integer.parseInt(e));
                String f =String.valueOf(snapshot.child("2").child("1").getValue());
                a2= (int) (a2+25*Integer.parseInt(f));
                String g =String.valueOf(snapshot.child("2").child("2").getValue());
                a2= (int) (a2+25*Integer.parseInt(g));
                String h =String.valueOf(snapshot.child("2").child("3").getValue());
                a2= (int) (a2+25*Integer.parseInt(h));
                    progressBar2.setProgress(a2);
                    t2.setText(String.valueOf(a2)+"%");

                String i =String.valueOf(snapshot.child("3").child("0").getValue());
                a3= (int) (25*Integer.parseInt(i));
                String j =String.valueOf(snapshot.child("3").child("1").getValue());
                a3= (int) (a3+25*Integer.parseInt(j));
                String k =String.valueOf(snapshot.child("3").child("2").getValue());
                a3= (int) (a3+25*Integer.parseInt(k));
                String l =String.valueOf(snapshot.child("3").child("3").getValue());
                a3= (int) (a3+25*Integer.parseInt(l));
                progressBar3.setProgress(a3);
                t3.setText(String.valueOf(a3)+"%");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Set OnClickListener for card1
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start MicroStudy activity and pass extra "cardNumber" with value 1
                startMicroStudyActivity(1);
            }
        });

        // Set OnClickListener for card2
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start MicroStudy activity and pass extra "cardNumber" with value 2
                startMicroStudyActivity(2);
            }
        });

        // Set OnClickListener for card3
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start MicroStudy activity and pass extra "cardNumber" with value 3
                startMicroStudyActivity(3);
            }
        });

    }

    // Method to start MicroStudy activity with cardNumber extra
    private void startMicroStudyActivity(int cardNumber) {
        Intent intent = new Intent(PendingLectures.this, MicroStudy.class);
        intent.putExtra("cardNumber", cardNumber);
        startActivity(intent);
    }
}
