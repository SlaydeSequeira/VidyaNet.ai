package com.example.basiclogintoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basiclogintoapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    private Map<String, Integer> titleColors;

    public ColorAdapter(Map<String, Integer> titleColors) {
        this.titleColors = titleColors;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false);
        return new ColorViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        List<String> titles = new ArrayList<>(titleColors.keySet());
        String title = titles.get(position);
        int color = titleColors.get(title);

        holder.colorView.setBackgroundColor(color);
        holder.titleTextView.setText(title);
    }

    @Override
    public int getItemCount() {
        return titleColors.size();
    }

    static class ColorViewHolder extends RecyclerView.ViewHolder {
        View colorView;
        TextView titleTextView;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            colorView = itemView.findViewById(R.id.colorView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }
    }
}
