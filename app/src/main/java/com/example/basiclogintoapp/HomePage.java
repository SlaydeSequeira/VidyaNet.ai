package com.example.basiclogintoapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.basiclogintoapp.Fragments.BlankFragment;
import com.example.basiclogintoapp.Fragments.ChatFragment;
import com.example.basiclogintoapp.Fragments.ProfileFragment;
import com.example.basiclogintoapp.Fragments.SearchFragment;
import com.example.basiclogintoapp.Model.Users;
import com.example.basiclogintoapp.adapter.MyPagerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class HomePage extends AppCompatActivity {
    private TextToSpeech textToSpeech;
    private TextView textView;
    private FirebaseUser firebaseUser;
    private DatabaseReference myRef;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private LinearLayout r1, r2, r3, r4, r5;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MyPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initializeUI();
        setupFirebase();
        setupNavigationDrawer();
        setupTabs();
        setupNavigationView();
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.US);  // Set language to English (US)
                }
            }
        });
    }

    private void initializeUI() {
        Objects.requireNonNull(getSupportActionBar()).hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#212825"));
        }

        drawerLayout = findViewById(R.id.my_drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpager);
    }

    private void setupFirebase() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseUser.getUid());

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String token = task.getResult();
                    Log.d(TAG, "FCM Token: " + token);
                });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setupTabs() {
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragment(new BlankFragment(), "Home");
        pagerAdapter.addFragment(new SearchFragment(), "Search");
        pagerAdapter.addFragment(new ProfileFragment(), "Profile");

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // Set icons for tabs
        tabLayout.getTabAt(0).setIcon(R.drawable.baseline_home_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.baseline_search_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.baseline_person_24);

        // Add swipe listener
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // Handle tab selection if needed
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setupNavigationDrawer() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView openDrawerButton = findViewById(R.id.open);
        openDrawerButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void setupNavigationView() {
        View headerView = navigationView.getHeaderView(0);
        ImageView imageView = headerView.findViewById(R.id.image);
        TextView pts = headerView.findViewById(R.id.text);
        TextView username = headerView.findViewById(R.id.username);

        r1 = headerView.findViewById(R.id.rel1);
        r2 = headerView.findViewById(R.id.rel2);
        r3 = headerView.findViewById(R.id.rel3);
        r4 = headerView.findViewById(R.id.rel6);
        r5 = headerView.findViewById(R.id.rel5);

        setupUserProfile(imageView, username);
        setupPoints(pts);
        setupNavigationClickListeners();
    }

    private void setupUserProfile(ImageView imageView, TextView username) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MyUsers")
                .child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                if (user != null) {
                    username.setText(user.getUsername());
                    if (user.getImageURL().equals("default")) {
                        imageView.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        try {
                            Glide.with(HomePage.this).load(user.getImageURL()).into(imageView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setupPoints(TextView pts) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("MyUsers")
                .child(firebaseUser.getUid())
                .child("points");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pointsValue = dataSnapshot.getValue(String.class);
                pts.setText(pointsValue + " years");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
    }

    private void setupNavigationClickListeners() {
        r1.setOnClickListener(v -> {
            viewPager.setCurrentItem(2);
            drawerLayout.closeDrawer(GravityCompat.START);
            String text = "You Have Clicked Profile Page";
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        });

        r2.setOnClickListener(v -> {
            startActivity(new Intent(HomePage.this, Hospital.class));
            String text = "You Have Clicked Maps Page";
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        });

        r3.setOnClickListener(v -> {
            startActivity(new Intent(HomePage.this, GetImageInfo.class));
            String text = "You Have Clicked Click and Learn Page";
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        });

        r4.setOnClickListener(v -> {
            PackageManager pm = getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(" com.androiddevs.furnituretryout"); // Replace with the actual package name
            if (intent != null) {
                startActivity(intent); // Launch the other app
            } else {
                Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
            }

        });

        r5.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomePage.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.logout:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(HomePage.this, MainActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    break;
                case R.id.message:
                    replace(new ChatFragment());
                    break;
                case R.id.maps:
                    Intent i = new Intent(HomePage.this, MainActivity2.class);
                    i.putExtra("X", "72");
                    i.putExtra("Y", "19");
                    i.putExtra("Z", "hotel0");
                    startActivity(i);
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    // Navigation methods
    public void moveToNextTab() {
        int currentItem = viewPager.getCurrentItem();
        int totalTabs = pagerAdapter.getCount();

        if (currentItem < totalTabs - 1) {
            viewPager.setCurrentItem(currentItem + 1, true);
        }
    }

    public void moveToPreviousTab() {
        int currentItem = viewPager.getCurrentItem();

        if (currentItem > 0) {
            viewPager.setCurrentItem(currentItem - 1, true);
        }
    }

    private void replace(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.test, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void CheckStatus(String status) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        myRef.updateChildren(hashMap);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        CheckStatus("Offline");
    }
}