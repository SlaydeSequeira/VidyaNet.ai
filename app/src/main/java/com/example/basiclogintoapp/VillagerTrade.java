package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class VillagerTrade extends AppCompatActivity {
CardView c1,c2,c3,c4,c5;
Button b1,b2,b3,b4,b5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_villager_trade);
        Objects.requireNonNull(getSupportActionBar()).hide();
        c1=findViewById(R.id.cardView1);
        c2=findViewById(R.id.cardView2);
        c3=findViewById(R.id.cardView3);
        c4=findViewById(R.id.cardView4);
        c5=findViewById(R.id.cardView5);
        b1=findViewById(R.id.c1);
        b2=findViewById(R.id.c2);
        b3=findViewById(R.id.c3);
        b4=findViewById(R.id.c4);
        b5=findViewById(R.id.c5);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mess();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mess();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mess();
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mess();
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mess();
            }
        });
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAct(200);
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAct(800);
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAct(400);
            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAct(1200);
            }
        });
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAct(600);
            }
        });
    }

    private void mess() {
        Intent i = new Intent(VillagerTrade.this, MessageActivity.class);
        i.putExtra("userid","vrszPY65eDYgOMgVFkhMrc2Xkvu1");
        startActivity(i);

    }

    private void startAct(int x) {
        Intent i = new Intent(VillagerTrade.this,Payment.class);
        i.putExtra("Cost",x);
        startActivity(i);
    }
}