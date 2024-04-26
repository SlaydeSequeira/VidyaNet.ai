package com.example.basiclogintoapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link marker_data#newInstance} factory method to
 * create an instance of this fragment.
 */
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class marker_data extends Fragment {

    private static final String ARG_DATA = "data";

    public static marker_data newInstance(String data) {
        marker_data fragment = new marker_data();
        Bundle args = new Bundle();
        args.putString(ARG_DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marker_data, container, false);
        TextView textView = view.findViewById(R.id.text_view_data);

        if (getArguments() != null) {
            String data = getArguments().getString(ARG_DATA);
            textView.setText(data); // Display the data
        }

        return view;
    }
}
