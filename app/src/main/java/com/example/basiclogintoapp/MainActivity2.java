package com.example.basiclogintoapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

public class MainActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marker1, marker2, marker3;
    private Marker selectedMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        // Set click listeners for the card views
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng me = new LatLng(19.044444, 72.820596); // Replace with actual coordinates
        marker1 = mMap.addMarker(new MarkerOptions().position(me).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(me));
        // Set a default zoom level
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(me, 15));

        findViewById(R.id.card1).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("PotentialBehaviorOverride")
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity2.this, "Electronics Clicked", Toast.LENGTH_SHORT).show();
                // Add markers for shop locations
                mMap.clear();
                LatLng me = new LatLng(19.044444, 72.820596); // Replace with actual coordinates
                marker1 = mMap.addMarker(new MarkerOptions().position(me).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                LatLng shop1 = new LatLng(19.0765, 72.8777); // Replace with actual coordinates
                LatLng shop2 = new LatLng(19.0621, 72.8311); // Replace with actual coordinates
                LatLng shop3 = new LatLng(19.1096, 72.8273); // Replace with actual coordinates
                marker1 = mMap.addMarker(new MarkerOptions().position(shop1).title("Tool Shop1"));
                marker2 = mMap.addMarker(new MarkerOptions().position(shop2).title("Tool Shop2"));
                marker3 = mMap.addMarker(new MarkerOptions().position(shop3).title("Tool Shop3"));
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        selectedMarker = marker;
                        showSelectButton();
                        return true;
                    }
                });
                // Draw lines from each electronics shop to "me"
                PolylineOptions polylineOptions1 = new PolylineOptions()
                        .add(shop1, me)
                        .color(Color.BLUE);

                PolylineOptions polylineOptions2 = new PolylineOptions()
                        .add(shop2, me)
                        .color(Color.GREEN);

                PolylineOptions polylineOptions3 = new PolylineOptions()
                        .add(shop3, me)
                        .color(Color.RED);

                mMap.addPolyline(polylineOptions1);
                mMap.addPolyline(polylineOptions2);
                mMap.addPolyline(polylineOptions3);

                // Calculate distances and display them on the map
                double distance1 = calculateDistance(shop1, me);
                double distance2 = calculateDistance(shop2, me);
                double distance3 = calculateDistance(shop3, me);

                BitmapDescriptor customIcon = BitmapDescriptorFactory.fromResource(R.drawable.baseline_circle_24); // Replace with your actual image resource
                String distanceText1 = String.format("Distance to Shop 1: %.2f km", distance1 * 120);
                mMap.addMarker(new MarkerOptions()
                        .position(midPoint(shop1, me))
                        .title(distanceText1)
                        .icon(BitmapFromVector(
                                getApplicationContext(),
                                R.drawable.baseline_circle_24)));

                String distanceText2 = String.format("Distance to Shop 2: %.2f km", distance2 * 120);
                mMap.addMarker(new MarkerOptions()
                        .position(midPoint(shop2, me))
                        .title(distanceText2)
                        .icon(BitmapFromVector(
                                getApplicationContext(),
                                R.drawable.baseline_circle_24)));

                String distanceText3 = String.format("Distance to Shop 3: %.2f km", distance3 * 120);
                mMap.addMarker(new MarkerOptions()
                        .position(midPoint(shop3, me))
                        .title(distanceText3)
                        .icon(BitmapFromVector(
                                getApplicationContext(),
                                R.drawable.baseline_circle_24)));

                // Focus the camera on the midpoint of the lines
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(shop1);
                builder.include(shop2);
                builder.include(shop3);
                builder.include(me);

                LatLngBounds bounds = builder.build();
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        });


        findViewById(R.id.card2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity2.this, "Groceries Clicked", Toast.LENGTH_SHORT).show();
                mMap.clear();
                LatLng me = new LatLng(19.044444, 72.820596); // Replace with actual coordinates
                marker1 = mMap.addMarker(new MarkerOptions().position(me).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                // Implement any action you want when Groceries card is clicked
                LatLng groceryShop1 = new LatLng(19.0350, 72.8405); // Replace with actual coordinates
                LatLng groceryShop2 = new LatLng(19.0553, 72.8258); // Replace with actual coordinates
                LatLng groceryShop3 = new LatLng(19.1232, 72.8310); // Replace with actual coordinates

                marker1 = mMap.addMarker(new MarkerOptions().position(groceryShop1).title("Seed Shop 1"));
                marker2 = mMap.addMarker(new MarkerOptions().position(groceryShop2).title("Seed Shop 2"));
                marker3 = mMap.addMarker(new MarkerOptions().position(groceryShop3).title("Seed Shop 3"));
                selectedMarker = marker1;
                showSelectButton();
                // Draw lines from each grocery shop to "me"
                PolylineOptions polylineOptions1 = new PolylineOptions()
                        .add(groceryShop1, me)
                        .color(Color.BLUE);

                PolylineOptions polylineOptions2 = new PolylineOptions()
                        .add(groceryShop2, me)
                        .color(Color.GREEN);

                PolylineOptions polylineOptions3 = new PolylineOptions()
                        .add(groceryShop3, me)
                        .color(Color.RED);

                mMap.addPolyline(polylineOptions1);
                mMap.addPolyline(polylineOptions2);
                mMap.addPolyline(polylineOptions3);

                // Calculate distances and display them on the map
                double distance1 = calculateDistance(groceryShop1, me);
                double distance2 = calculateDistance(groceryShop2, me);
                double distance3 = calculateDistance(groceryShop3, me);

                BitmapDescriptor customIcon = BitmapDescriptorFactory.fromResource(R.drawable.baseline_circle_24); // Replace with your actual image resource
                String distanceText1 = String.format("Distance to Shop 1: %.2f km", distance1 * 120);
                mMap.addMarker(new MarkerOptions()
                        .position(midPoint(groceryShop1, me))
                        .title(distanceText1)
                        .icon(BitmapFromVector(
                                getApplicationContext(),
                                R.drawable.baseline_circle_24)));

                String distanceText2 = String.format("Distance to Shop 2: %.2f km", distance2 * 120);
                mMap.addMarker(new MarkerOptions()
                        .position(midPoint(groceryShop2, me))
                        .title(distanceText2)
                        .icon(BitmapFromVector(
                                getApplicationContext(),
                                R.drawable.baseline_circle_24)));

                String distanceText3 = String.format("Distance to Shop 3: %.2f km", distance3 * 120);
                mMap.addMarker(new MarkerOptions()
                        .position(midPoint(groceryShop3, me))
                        .title(distanceText3)
                        .icon(BitmapFromVector(
                                getApplicationContext(),
                                R.drawable.baseline_circle_24)));

                // Focus the camera on the midpoint of the lines
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(groceryShop1);
                builder.include(groceryShop2);
                builder.include(groceryShop3);
                builder.include(me);

                LatLngBounds bounds = builder.build();
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        });


        findViewById(R.id.card3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity2.this, "Clothing Clicked", Toast.LENGTH_SHORT).show();
                mMap.clear();
                // Inside your onClick method
                LatLng me = new LatLng(19.044444, 72.820596); // Replace with actual coordinates
                marker1 = mMap.addMarker(new MarkerOptions().position(me).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                LatLng clothesShop1 = new LatLng(19.0467, 72.8631);
                LatLng clothesShop2 = new LatLng(19.0200, 72.8585);
                LatLng clothesShop3 = new LatLng(19.1350, 72.8442);
                marker1 = mMap.addMarker(new MarkerOptions().position(clothesShop1).title("Food Shop 1"));
                marker2 = mMap.addMarker(new MarkerOptions().position(clothesShop2).title("Food Shop 2"));
                marker3 = mMap.addMarker(new MarkerOptions().position(clothesShop3).title("Food Shop 3"));
                selectedMarker = marker1;
                showSelectButton();
// Draw lines from each clothes shop to "me"
                PolylineOptions polylineOptions1 = new PolylineOptions()
                        .add(clothesShop1, me)
                        .color(Color.BLUE);

                PolylineOptions polylineOptions2 = new PolylineOptions()
                        .add(clothesShop2, me)
                        .color(Color.GREEN);

                PolylineOptions polylineOptions3 = new PolylineOptions()
                        .add(clothesShop3, me)
                        .color(Color.RED);

                mMap.addPolyline(polylineOptions1);
                mMap.addPolyline(polylineOptions2);
                mMap.addPolyline(polylineOptions3);

// Calculate distances and display them on the map
                double distance1 = calculateDistance(clothesShop1, me);
                double distance2 = calculateDistance(clothesShop2, me);
                double distance3 = calculateDistance(clothesShop3, me);

                BitmapDescriptor customIcon = BitmapDescriptorFactory.fromResource(R.drawable.baseline_circle_24); // Replace with your actual image resource
                String distanceText1 = String.format("Distance to Shop 1: %.2f km", distance1 * 120);
                mMap.addMarker(new MarkerOptions()
                        .position(midPoint(clothesShop1, me))
                        .title(distanceText1)
                        // below line is use to add
                        // custom marker on our map.
                        .icon(BitmapFromVector(
                                getApplicationContext(),
                                R.drawable.baseline_circle_24)));
                String distanceText2 = String.format("Distance to Shop 2: %.2f km", distance2 * 120);
                mMap.addMarker(new MarkerOptions()
                        .position(midPoint(clothesShop2, me))
                        .title(distanceText2)
                        // below line is use to add
                        // custom marker on our map.
                        .icon(BitmapFromVector(
                                getApplicationContext(),
                                R.drawable.baseline_circle_24)));
                String distanceText3 = String.format("Distance to Shop 3: %.2f km", distance3 * 120);
                mMap.addMarker(new MarkerOptions()
                        .position(midPoint(clothesShop3, me))
                        .title(distanceText3)
                        // below line is use to add
                        // custom marker on our map.
                        .icon(BitmapFromVector(
                                getApplicationContext(),
                                R.drawable.baseline_circle_24)));

// Focus the camera on the midpoint of the lines
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(clothesShop1);
                builder.include(clothesShop2);
                builder.include(clothesShop3);
                builder.include(me);

                LatLngBounds bounds = builder.build();
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

            }
        });
    }

    private void drawRoute(LatLng origin, LatLng destination) {
        // Use Google Directions API to get route information
        new GetDirectionsTask().execute(origin, destination);
    }

    private class GetDirectionsTask extends AsyncTask<LatLng, Void, List<LatLng>> {
        @Override
        protected List<LatLng> doInBackground(LatLng... params) {
            LatLng origin = params[0];
            LatLng destination = params[1];

            try {
                // Make a request to Google Directions API
                DirectionsApiRequest request = DirectionsApi.getDirections(
                        new GeoApiContext.Builder()
                                .apiKey("AIzaSyD3q0XgGvb9T-xvHaq6U-xVr3WGoWWdqbE")
                                .build(),
                        origin.latitude + "," + origin.longitude,
                        destination.latitude + "," + destination.longitude
                );
                DirectionsResult result = request.await();

                // Parse the result and get polyline points
                if (result.routes != null && result.routes.length > 0) {
                    DirectionsRoute route = result.routes[0];
                    List<LatLng> polylinePoints = new ArrayList<>();

                    for (DirectionsLeg leg : route.legs) {
                        for (com.google.maps.model.LatLng point : leg.steps[0].polyline.decodePath()) {
                            polylinePoints.add(new LatLng(point.lat, point.lng));
                        }
                    }

                    return polylinePoints;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<LatLng> polylinePoints) {
            // Draw polyline on the map
            if (polylinePoints != null) {
                mMap.addPolyline(new PolylineOptions().addAll(polylinePoints).color(Color.BLUE));
            }
        }
    }

    private double calculateDistance(LatLng point1, LatLng point2) {
        double x1 = point1.latitude;
        double y1 = point1.longitude;
        double x2 = point2.latitude;
        double y2 = point2.longitude;

        // Using the distance formula
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private LatLng midPoint(LatLng point1, LatLng point2) {
        double lat1 = point1.latitude;
        double lon1 = point1.longitude;
        double lat2 = point2.latitude;
        double lon2 = point2.longitude;

        double midLat = (lat1 + lat2) / 2;
        double midLon = (lon1 + lon2) / 2;

        return new LatLng(midLat, midLon);
    }

    private BitmapDescriptor
    BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(
                context, vectorResId);

        // below line is use to set bounds to our vector
        // drawable.
        vectorDrawable.setBounds(
                0, 0, vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our
        // bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void showSelectButton() {
        // Here you can show a button in your UI or perform any other action
        // For example, you can show a button and handle its click to navigate to a new page
        CardView selectButton = findViewById(R.id.button); // Replace with your actual button
        selectButton.setVisibility(View.VISIBLE);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click, navigate to a new page, and pass marker information
                if (selectedMarker != null) {
                    String markerTitle = selectedMarker.getTitle();
                    LatLng markerPosition = selectedMarker.getPosition();

                    // Pass marker information to a new page
                    Intent intent = new Intent(MainActivity2.this, ShopInfo.class);
                    intent.putExtra("markerTitle", markerTitle);
                    intent.putExtra("markerLat", markerPosition.latitude);
                    intent.putExtra("markerLng", markerPosition.longitude);
                    // Add more data as needed
                    startActivity(intent);
                }
            }
        });
    }
}
