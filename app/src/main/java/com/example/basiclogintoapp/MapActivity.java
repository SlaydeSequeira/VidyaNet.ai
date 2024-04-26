package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

// Example MapActivity Class
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        EditText searchBar = findViewById(R.id.edit);
        Button searchButton = findViewById(R.id.button);
        iv = findViewById(R.id.image);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchBar.getText().toString().toLowerCase();

                switch (query) {
                    case "python":
                        showPythonLocations();
                        break;
                    case "java":
                        showJavaLocations();
                        break;
                    case "c":
                        showCLocations();
                        break;
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng pointA = new LatLng(19.966306, 73.67050);  // Start location
        mMap.addMarker(new MarkerOptions().position(pointA).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointA, 10));
    }

    private void showPythonLocations() {
        LatLng[] pythonLocations = {
                new LatLng(19.755056, 73.76057),
                new LatLng(19.645056, 73.56057),
                new LatLng(19.835056, 73.73057)
        };

        LatLng start = new LatLng(19.965056, 73.67057); // Point A

        for (LatLng location : pythonLocations) {
            mMap.addMarker(new MarkerOptions().position(location).title("Python Location"));
            mMap.addPolyline(new PolylineOptions().add(location, start).width(4).color(Color.BLUE));
        }
    }

    private void showJavaLocations() {
        LatLng[] javaLocations = {
                new LatLng(19.645056, 73.65057),
                new LatLng(19.785056, 73.63057),
                new LatLng(19.865056, 73.68057)
        };

        LatLng start = new LatLng(19.965056, 73.67057); // Point A

        for (LatLng location : javaLocations) {
            mMap.addMarker(new MarkerOptions().position(location).title("Java Location"));
            mMap.addPolyline(new PolylineOptions().add(location, start).width(4).color(Color.BLUE));
        }
    }

    private void showCLocations() {
        LatLng[] cLocations = {
                new LatLng(19.895056, 73.60157),
                new LatLng(19.895156, 73.59057),
                new LatLng(19.875556, 73.59087)
        };

        LatLng start = new LatLng(19.965056, 73.67057); // Point A

        for (LatLng location : cLocations) {
            mMap.addMarker(new MarkerOptions().position(location).title("C Location"));
            mMap.addPolyline(new PolylineOptions().add(location, start).width(4).color(Color.BLUE));
        }
    }
    private void openFragment(String data) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Create an instance of the fragment and pass the data as a bundle
        marker_data fragment = marker_data.newInstance(data);

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
