package com.example.basiclogintoapp.adapter;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.basiclogintoapp.IncDec;
import com.example.basiclogintoapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.Map;
// ItemAdapter.java
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
// Import necessary libraries
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final Context context;
    private int count;
    private String[] itemNames;
    private String[] imgUrls;
    private String[] cpValues;
    private String[] spValues;
    private String[] aisleValues;
    private String[] categoryValues;
    private String[] quantityValues;
    private String[] reviewValues;

    public ItemAdapter(Context context, int count, String[] itemNames, String[] imgUrls, String[] cpValues, String[] spValues,
                       String[] aisleValues, String[] categoryValues, String[] quantityValues, String[] reviewValues) {
        this.count = count;
        this.itemNames = itemNames;
        this.imgUrls = imgUrls;
        this.cpValues = cpValues;
        this.spValues = spValues;
        this.aisleValues = aisleValues;
        this.categoryValues = categoryValues;
        this.quantityValues = quantityValues;
        this.reviewValues = reviewValues;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.add, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @android.annotation.SuppressLint("RecyclerView") int position) {
        holder.textView.setText(itemNames[position]);
        holder.textView2.setText("Quantity= " + spValues[position] + " items");
        Glide.with(holder.itemView.getContext())
                .load(imgUrls[position])
                .placeholder(R.drawable.back)
                .error(R.drawable.back)
                .into(holder.imageView);
        holder.TextView3.setText("ID" + cpValues[position]);

        // Add your item click handling here
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click
                Intent i = new Intent(context, IncDec.class);
                i.putExtra("title", itemNames[position]);
                context.startActivity(i);
            }
        });

        // Parse aisleValues[position] to an integer
        int aisleValue = Integer.parseInt(aisleValues[position]);

        // Set background color based on the condition
        if (aisleValue > 10) {
            ColorStateList colorStateList = ColorStateList.valueOf(context.getResources().getColor(R.color.Python));
            holder.itemView.setBackgroundTintList(colorStateList);
        } else if (aisleValue >= 5 && aisleValue <= 10) {
            ColorStateList colorStateList = ColorStateList.valueOf(context.getResources().getColor(R.color.R));
            holder.itemView.setBackgroundTintList(colorStateList);
        } else {
            ColorStateList colorStateList = ColorStateList.valueOf(context.getResources().getColor(R.color.CPP));
            holder.itemView.setBackgroundTintList(colorStateList);
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;
        ImageView imageView;
        TextView TextView3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            textView2 = itemView.findViewById(R.id.text2);
            imageView = itemView.findViewById(R.id.imageinrecycler);
            TextView3 = itemView.findViewById(R.id.text3);
        }
    }
}
