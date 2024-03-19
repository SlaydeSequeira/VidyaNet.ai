package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        // Get the intent that started this activity
        Intent intent = getIntent();

        // Get the "pos" extra from the intent
        int position = intent.getIntExtra("pos", -1); // -1 is the default value if "pos" is not found

        // Display the "pos" value using a toast message
        if (position != -1) {
            Toast.makeText(this, "Position: " + position, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Position not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker at a location with blue color
        LatLng location1 = new LatLng(19.197715, 72.8227426); // Replace with actual coordinates
        mMap.addMarker(new MarkerOptions().position(location1).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        // Add a marker at another location with red color, but set different values based on the position
        LatLng location2 = new LatLng(0, 0); // Default initialization
        int position = getIntent().getIntExtra("pos", -1);
        if (position >= 0 && position <= 4) {
            // Set different values based on the position
            switch (position) {
                case 0:
                    location2 = new LatLng(19.195639, 72.833572); // Replace with actual coordinates for position 0
                    break;
                case 1:
                    location2 = new LatLng(19.194623, 72.80487); // Replace with actual coordinates for position 1
                    break;
                case 2:
                    location2 = new LatLng(19.211003, 72.828770); // Replace with actual coordinates for position 2
                    break;
                case 3:
                    location2 = new LatLng(19.194005, 72.827386); // Replace with actual coordinates for position 3
                    break;
                case 4:
                    location2 = new LatLng(19.4, 72.6); // Replace with actual coordinates for position 4
                    break;
                default:
                    // Handle other cases if needed
            }
            mMap.addMarker(new MarkerOptions().position(location2).title("Users Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                .add(location1, location2)
                .width(5) // Width of the line in pixels
                .color(Color.GREEN)); // Color of the line

        // Move camera to the first location and zoom in
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location1, 15));
    }

}
