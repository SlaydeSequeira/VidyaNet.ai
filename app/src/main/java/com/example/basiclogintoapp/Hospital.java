package com.example.basiclogintoapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Hospital extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    // Surat city center coordinates
    private static final LatLng SURAT_CENTER = new LatLng(21.1702, 72.8311);
    Button btn;
    // Sample hospitals in Surat (you should replace these with actual data)
    private final List<HospitalInfo> hospitals = new ArrayList<HospitalInfo>() {{
        add(new HospitalInfo("SMIMER Hospital", new LatLng(21.1827, 72.8511)));
        add(new HospitalInfo("New Civil Hospital", new LatLng(21.1923, 72.8154)));
        add(new HospitalInfo("Kiran Hospital", new LatLng(21.1766, 72.8077)));
        add(new HospitalInfo("Apple Hospital", new LatLng(21.1856, 72.8398)));
        add(new HospitalInfo("Metas Hospital", new LatLng(21.1702, 72.8451)));
        add(new HospitalInfo("Unity Hospital", new LatLng(21.1789, 72.8234)));
        add(new HospitalInfo("Unique Hospital", new LatLng(21.1956, 72.8301)));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        btn= findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Hospital.this,ReportOutbreak2.class);
                startActivity(i);
            }
        });
        // Get the SupportMapFragment and request notification when the map is ready
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        btn= findViewById(R.id.button);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Enable map settings
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        // Check for location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        // Move camera to Surat with appropriate zoom level
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SURAT_CENTER, 13f));

        // Add outbreak circles
        addOutbreakCircles();

        // Add 5 random hospitals from the list
        Random random = new Random();
        List<Integer> selectedIndices = new ArrayList<>();

        while (selectedIndices.size() < 5) {
            int index = random.nextInt(hospitals.size());
            if (!selectedIndices.contains(index)) {
                selectedIndices.add(index);
                HospitalInfo hospital = hospitals.get(index);

                // Add hospital marker
                mMap.addMarker(new MarkerOptions()
                        .position(hospital.location)
                        .title(hospital.name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }
    }

    private void addOutbreakCircles() {
        // Outbreak 1 - Red Circle
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(21.1827, 72.8311))
                .radius(500) // 500 meters radius
                .strokeWidth(2)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(70, 255, 0, 0))); // Semi-transparent red

        // Add marker for Outbreak 1
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(21.1827, 72.8311))
                .title("Malaria"));

        // Outbreak 2 - Blue Circle
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(21.1702, 72.8211))
                .radius(700) // 700 meters radius
                .strokeWidth(2)
                .strokeColor(Color.BLUE)
                .fillColor(Color.argb(70, 0, 0, 255))); // Semi-transparent blue

        // Add marker for Outbreak 2
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(21.1702, 72.8211))
                .title("Dengue"));

        // Outbreak 3 - Green Circle
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(21.1602, 72.8411))
                .radius(600) // 600 meters radius
                .strokeWidth(2)
                .strokeColor(Color.GREEN)
                .fillColor(Color.argb(70, 0, 255, 0))); // Semi-transparent green

        // Add marker for Outbreak 3
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(21.1602, 72.8411))
                .title("Swine Flu"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mMap != null) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                }
            }
        }
    }

    // Inner class to store hospital data
    private static class HospitalInfo {
        String name;
        LatLng location;

        HospitalInfo(String name, LatLng location) {
            this.name = name;
            this.location = location;
        }
    }
}