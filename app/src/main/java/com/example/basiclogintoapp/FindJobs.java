package com.example.basiclogintoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindJobs extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Marker> currentMarkers1 = new ArrayList<>();
    private Map<String, List<JobMarker>> salaryMarkersMap = new HashMap<>();
    private int job = 0; // Default to Sales

    private RelativeLayout hiddenRel;
    private TextView textView1;
    private TextView textView2;


    // Firebase
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_jobs);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().getReference();
        //Button btn =findViewById(R.id.button);
        CardView c1 = findViewById(R.id.card1);
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddJobDialog();
            }
        });
        // Initialize salary markers map
        initializeSalaryMarkers();

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

    private void initializeSalaryMarkers() {
        // Initialize salary markers map with empty lists
        salaryMarkersMap.put("100,000-150,000", new ArrayList<>());
        salaryMarkersMap.put("150,000-200,000", new ArrayList<>());
        salaryMarkersMap.put("200,000-250,000", new ArrayList<>());
        salaryMarkersMap.put("250,000-300,000", new ArrayList<>());
        salaryMarkersMap.put("300,000-350,000", new ArrayList<>());
        salaryMarkersMap.put("350,000-400,000", new ArrayList<>());

        // Retrieve data from Firebase and populate the map
        retrieveMarkersFromFirebase();
    }

    private void retrieveMarkersFromFirebase() {
        database.child("jobs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot salarySnapshot : dataSnapshot.getChildren()) {
                    String salaryRange = salarySnapshot.getKey();
                    for (DataSnapshot jobSnapshot : salarySnapshot.getChildren()) {
                        int jobType = Integer.parseInt(jobSnapshot.getKey());
                        for (DataSnapshot jobMarkerSnapshot : jobSnapshot.getChildren()) {
                            String jobId = jobMarkerSnapshot.getKey();
                            Double xCodDouble = jobMarkerSnapshot.child("x_cod").getValue(Double.class);
                            Double yCodDouble = jobMarkerSnapshot.child("y_cod").getValue(Double.class);
                            String title = jobMarkerSnapshot.child("title").getValue(String.class);
                            String description = jobMarkerSnapshot.child("description").getValue(String.class);

                            // Check for null values
                            double xCod = (xCodDouble != null) ? xCodDouble : 0.0; // Default value if xCodDouble is null
                            double yCod = (yCodDouble != null) ? yCodDouble : 0.0; // Default value if yCodDouble is null

                            LatLng position = new LatLng(xCod, yCod);
                            JobMarker jobMarker = new JobMarker(jobId, position, title, description);
                            salaryMarkersMap.get(salaryRange).add(jobMarker);
                        }
                    }
                }

                // After retrieving data, update markers based on initial selection
                String selectedSalaryRange = ((Spinner) findViewById(R.id.salary_spinner)).getSelectedItem().toString();
                updateMarkers(selectedSalaryRange);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }


    private void updateMarkers(String salaryRange) {
        // Clear existing markers from the map
        for (Marker marker : currentMarkers1) {
            marker.remove();
            mMap.clear();
        }
        currentMarkers1.clear();

        // Add user marker (example)
        LatLng userPosition = new LatLng(19.044313690905664, 73.02557184952288);
        mMap.addMarker(new MarkerOptions()
                .position(userPosition)
                .title("You")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userPosition, 15f));

        List<JobMarker> jobMarkers = salaryMarkersMap.get(salaryRange);
        if (jobMarkers != null) {
            for (JobMarker jobMarker : jobMarkers) {
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(jobMarker.getPosition())
                        .title(jobMarker.getTitle())
                        .snippet(jobMarker.getDescription()));
                currentMarkers1.add(marker);
            }
        }
    }

    private void clearMap() {
        if (mMap != null) {
            mMap.clear();
            currentMarkers1.clear();
            Toast.makeText(this, "Map cleared", Toast.LENGTH_SHORT).show(); // Optional: to verify the method is called
        }
    }

    // Helper class to represent a job marker
    private static class JobMarker {
        private String id;
        private LatLng position;
        private String title;
        private String description;

        public JobMarker(String id, LatLng position, String title, String description) {
            this.id = id;
            this.position = position;
            this.title = title;
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public LatLng getPosition() {
            return position;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }
    private void showAddJobDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Job Listing");

        // Set up the input layout
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(40, 0, 40, 0);

        // Job Type Spinner
        Spinner jobSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> jobAdapter = ArrayAdapter.createFromResource(this,
                R.array.jobs, android.R.layout.simple_spinner_item);
        jobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobSpinner.setAdapter(jobAdapter);
        layout.addView(jobSpinner, layoutParams);

        // Salary Range Spinner
        Spinner salarySpinner = new Spinner(this);
        ArrayAdapter<CharSequence> salaryAdapter = ArrayAdapter.createFromResource(this,
                R.array.salary_ranges, android.R.layout.simple_spinner_item);
        salaryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        salarySpinner.setAdapter(salaryAdapter);
        layout.addView(salarySpinner, layoutParams);

        // X Coordinate EditText
        EditText xCodEditText = new EditText(this);
        xCodEditText.setHint("x_cod");
        layout.addView(xCodEditText, layoutParams);

        // Y Coordinate EditText
        EditText yCodEditText = new EditText(this);
        yCodEditText.setHint("y_cod");
        layout.addView(yCodEditText, layoutParams);

        // Title EditText
        EditText titleEditText = new EditText(this);
        titleEditText.setHint("Title");
        layout.addView(titleEditText, layoutParams);

        // Description EditText
        EditText descriptionEditText = new EditText(this);
        descriptionEditText.setHint("Description");
        layout.addView(descriptionEditText, layoutParams);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve input values
                String selectedJob = (String) jobSpinner.getSelectedItem();
                int jobType = selectedJob.equals("Sales") ? 0 : 1;
                String salaryRange = (String) salarySpinner.getSelectedItem();
                double xCod = Double.parseDouble(xCodEditText.getText().toString());
                double yCod = Double.parseDouble(yCodEditText.getText().toString());
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                // Validate input fields
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
                    Toast.makeText(FindJobs.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a unique key for the new job listing
                DatabaseReference jobsRef = database.child("jobs").child(salaryRange).child(String.valueOf(jobType));
                String jobId = jobsRef.push().getKey();

                // Create a map for the job data
                Map<String, Object> jobData = new HashMap<>();
                jobData.put("x_cod", xCod);
                jobData.put("y_cod", yCod);
                jobData.put("title", title);
                jobData.put("description", description);

                // Push job data to Firebase
                jobsRef.child(jobId).setValue(jobData);

                // Update markers on the map
                updateMarkers(salaryRange);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
