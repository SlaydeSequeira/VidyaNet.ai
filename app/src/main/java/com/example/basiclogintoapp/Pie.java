package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Pie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);
    }

    public void navigateToQuantityPieChart(View view) {
        Intent intent = new Intent(this, PieChart1.class);
        startActivity(intent);
    }

    public void navigateToExpiryPieChart(View view) {
        Intent intent = new Intent(this, PieChart3.class);
        startActivity(intent);
    }
}
