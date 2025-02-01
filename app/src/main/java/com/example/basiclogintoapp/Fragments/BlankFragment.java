package com.example.basiclogintoapp.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.basiclogintoapp.R;

public class BlankFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        // Initialize buttons and image views
        Button btnMeet1 = view.findViewById(R.id.btnMeet1);
        Button btnMeet2 = view.findViewById(R.id.btnMeet2);
        Button btnMeet3 = view.findViewById(R.id.btnMeet3);
        Button btnMeet4 = view.findViewById(R.id.btnMeet4);
        ImageView iv1 = view.findViewById(R.id.image1); // Assuming you have set an id for your ImageView in XML
        ImageView iv2 = view.findViewById(R.id.image2);
        ImageView iv3 = view.findViewById(R.id.image3);
        ImageView iv4 = view.findViewById(R.id.image4);

        // Load images using Glide
        Glide.with(this)
                .load("https://th.bing.com/th/id/OIP.WZuFeD-_Btx-rRmknaI_9AHaHa?rs=1&pid=ImgDetMain")
                .into(iv1); // Image for Doctor 1

        Glide.with(this)
                .load("https://th.bing.com/th/id/R.b379902c62bb9c7333c2bbf704d8104c?rik=EALI63%2bLUEhkeA&riu=http%3a%2f%2fwww.texila.us%2fblog%2fwp-content%2fuploads%2f2015%2f09%2fDoctor-Background.jpg&ehk=xT7BPf004Jh0P1KDbK%2f2xsvItxYhv%2bFqwSrNT6Qamvg%3d&risl=&pid=ImgRaw&r=0")
                .into(iv2); // Image for Doctor 2

        Glide.with(this)
                .load("https://th.bing.com/th/id/OIP.rxLGgz9A_dB49_49VnMnPAHaE8?w=681&h=454&rs=1&pid=ImgDetMain")
                .into(iv3); // Image for Doctor 3

        Glide.with(this)
                .load("https://th.bing.com/th/id/OIP.wbZBGL1zXorjfgj5GLWnmwHaLH?rs=1&pid=ImgDetMain")
                .into(iv4); // Image for Doctor 4

        // Set click listeners for each button
        View.OnClickListener meetingListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://aryan.digitalsamba.com/demo-room";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        };

        btnMeet1.setOnClickListener(meetingListener);
        btnMeet2.setOnClickListener(meetingListener);
        btnMeet3.setOnClickListener(meetingListener);
        btnMeet4.setOnClickListener(meetingListener);

        return view;
    }
}
