package com.example.basiclogintoapp;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FindJobs extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Marker> currentMarkers = new ArrayList<>();
    private Map<String, List<LatLng>> salaryMarkersMap = new HashMap<>();
    private int job = 0; // Default to Sales

    private RelativeLayout hiddenRel;
    private TextView textView1;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_jobs);

        // Initialize salary markers map
        salaryMarkersMap.put("100,000-150,000", getMarkersForRange(2));
        salaryMarkersMap.put("150,000-200,000", getMarkersForRange(3));
        salaryMarkersMap.put("200,000-250,000", getMarkersForRange(4));
        salaryMarkersMap.put("250,000-300,000", getMarkersForRange(5));
        salaryMarkersMap.put("300,000-350,000", getMarkersForRange(6));
        salaryMarkersMap.put("350,000-400,000", getMarkersForRange(7));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set up the Salary Spinner
        Spinner salarySpinner = findViewById(R.id.salary_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.salary_ranges, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        salarySpinner.setAdapter(adapter);

        salarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSalaryRange = (String) parent.getItemAtPosition(position);
                updateMarkers(selectedSalaryRange);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no selection was made
            }
        });

        // Set up the Job Spinner
        Spinner jobSpinner = findViewById(R.id.job_spinner);
        ArrayAdapter<CharSequence> jobAdapter = ArrayAdapter.createFromResource(this,
                R.array.jobs, android.R.layout.simple_spinner_item);
        jobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobSpinner.setAdapter(jobAdapter);

        jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedJob = (String) parent.getItemAtPosition(position);
                if (selectedJob.equals("Sales")) {
                    job = 0;
                } else if (selectedJob.equals("Service")) {
                    job = 1;
                }
                updateMarkers((String) salarySpinner.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no selection was made
            }
        });

        // Initialize hidden layout and text views
        hiddenRel = findViewById(R.id.hiddenrel);
        textView1 = findViewById(R.id.textview1);
        textView2 = findViewById(R.id.textview2);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng markerPosition = new LatLng(19.044313690905664, 73.02557184952288);

        // Add a marker at the specified position with a green color
        mMap.addMarker(new MarkerOptions()
                .position(markerPosition)
                .title("You")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String jobType = (job == 0) ? "Sales" : "Service";
                String jobDescription = "Description for " + jobType + " job at " + marker.getPosition().toString();

                textView1.setText(jobType + " Job");
                textView2.setText(jobDescription);

                hiddenRel.setVisibility(View.VISIBLE);

                return false; // Return false to indicate that we have not consumed the event and that we wish for the default behavior to occur (which is for the camera to move such that the marker is centered and for the marker's info window to open, if it has one).
            }
        });
    }

    private List<LatLng> getMarkersForRange(int rangeIndex) {
        List<LatLng> markers = new ArrayList<>();
        // Define marker positions for each salary range and job type
        // Initialize a random number generator
        Random random = new Random();

        switch (rangeIndex) {
            case 2:
                if (job == 0) {
                    markers.add(new LatLng(19.0500 + random.nextDouble() * 0.15, 72.8500 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1000 + random.nextDouble() * 0.15, 72.9000 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1500 + random.nextDouble() * 0.15, 72.9500 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.2000 + random.nextDouble() * 0.15, 73.0000 + random.nextDouble() * 0.15));
                } else if (job == 1) {
                    markers.add(new LatLng(19.0600 + random.nextDouble() * 0.15, 72.8600 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1100 + random.nextDouble() * 0.15, 72.9100 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1600 + random.nextDouble() * 0.15, 72.9600 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.2100 + random.nextDouble() * 0.15, 73.0100 + random.nextDouble() * 0.15));
                }
                break;
            case 3:
                if (job == 0) {
                    markers.add(new LatLng(19.0400 + random.nextDouble() * 0.15, 72.8400 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.0900 + random.nextDouble() * 0.15, 72.8900 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1400 + random.nextDouble() * 0.15, 72.9400 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1900 + random.nextDouble() * 0.15, 72.9900 + random.nextDouble() * 0.15));
                } else if (job == 1) {
                    markers.add(new LatLng(19.0500 + random.nextDouble() * 0.15, 72.8500 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1000 + random.nextDouble() * 0.15, 72.9000 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1500 + random.nextDouble() * 0.15, 72.9500 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.2000 + random.nextDouble() * 0.15, 73.0000 + random.nextDouble() * 0.15));
                }
                break;
            case 4:
                if (job == 0) {
                    markers.add(new LatLng(19.0300 + random.nextDouble() * 0.15, 72.8300 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.0800 + random.nextDouble() * 0.15, 72.8800 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1300 + random.nextDouble() * 0.15, 72.9300 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1800 + random.nextDouble() * 0.15, 72.9800 + random.nextDouble() * 0.15));
                } else if (job == 1) {
                    markers.add(new LatLng(19.0400 + random.nextDouble() * 0.15, 72.8400 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.0900 + random.nextDouble() * 0.15, 72.8900 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1400 + random.nextDouble() * 0.15, 72.9400 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1900 + random.nextDouble() * 0.15, 72.9900 + random.nextDouble() * 0.15));
                }
                break;
            case 5:
                if (job == 0) {
                    markers.add(new LatLng(19.0200 + random.nextDouble() * 0.15, 72.8200 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.0700 + random.nextDouble() * 0.15, 72.8700 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1200 + random.nextDouble() * 0.15, 72.9200 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1700 + random.nextDouble() * 0.15, 72.9700 + random.nextDouble() * 0.15));
                } else if (job == 1) {
                    markers.add(new LatLng(19.0300 + random.nextDouble() * 0.15, 72.8300 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.0800 + random.nextDouble() * 0.15, 72.8800 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1300 + random.nextDouble() * 0.15, 72.9300 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1800 + random.nextDouble() * 0.15, 72.9800 + random.nextDouble() * 0.15));
                }
                break;
            case 6:
                if (job == 0) {
                    markers.add(new LatLng(19.0100 + random.nextDouble() * 0.15, 72.8100 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.0600 + random.nextDouble() * 0.15, 72.8600 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1100 + random.nextDouble() * 0.15, 72.9100 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1600 + random.nextDouble() * 0.15, 72.9600 + random.nextDouble() * 0.15));
                } else if (job == 1) {
                    markers.add(new LatLng(19.0200 + random.nextDouble() * 0.15, 72.8200 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.0700 + random.nextDouble() * 0.15, 72.8700 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1200 + random.nextDouble() * 0.15, 72.9200 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1700 + random.nextDouble() * 0.15, 72.9700 + random.nextDouble() * 0.15));
                }
                break;
            case 7:
                if (job == 0) {
                    markers.add(new LatLng(19.0000 + random.nextDouble() * 0.15, 72.8000 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.0500 + random.nextDouble() * 0.15, 72.8500 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1000 + random.nextDouble() * 0.15, 72.9000 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1500 + random.nextDouble() * 0.15, 72.9500 + random.nextDouble() * 0.15));
                } else if (job == 1) {
                    markers.add(new LatLng(19.0100 + random.nextDouble() * 0.15, 72.8100 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.0600 + random.nextDouble() * 0.15, 72.8600 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1100 + random.nextDouble() * 0.15, 72.9100 + random.nextDouble() * 0.15));
                    markers.add(new LatLng(19.1600 + random.nextDouble() * 0.15, 72.9600 + random.nextDouble() * 0.15));

                }
                break;
        }
        return markers;
    }

    private void updateMarkers(String salaryRange) {
        List<LatLng> markerPositions = salaryMarkersMap.get(salaryRange);
        mMap.clear();

        LatLng markerPosition = new LatLng(19.044313690905664, 73.02557184952288);

        // Add a marker at the specified position with a green color
        mMap.addMarker(new MarkerOptions()
                .position(markerPosition)
                .title("You")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 15f));

        for (LatLng position : markerPositions) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(position));
            currentMarkers.add(marker);
        }
    }
}
