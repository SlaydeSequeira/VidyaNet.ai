package com.example.basiclogintoapp.adapter;

import static com.example.basiclogintoapp.R.drawable.baseline_work_outline_24;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basiclogintoapp.ItemDetailActivity;
import com.example.basiclogintoapp.R;

public class RecyclerAdapter3 extends RecyclerView.Adapter<RecyclerAdapter3.ViewHolder> {
    private final Context context;
    private final String[] titles;
    private final String[] descriptions;
    private final String[] locations;
    private final int count;

    public RecyclerAdapter3(Context context, String[] titles, String[] descriptions, String[] locations, int count) {
        this.context = context;
        this.titles = titles;
        this.descriptions = descriptions;
        this.locations = locations;
        this.count = count;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.add, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set the text and image data for each item
        holder.title.setText(titles[position]);
        holder.description.setText(descriptions[position]);
        holder.location.setText(locations[position]);
        holder.image.setImageResource(baseline_work_outline_24);

        // Set up a click listener to open a new activity with additional item data
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra("title", titles[position]);
            intent.putExtra("description", descriptions[position]);
            intent.putExtra("location", locations[position]);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView location;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text);
            description = itemView.findViewById(R.id.text2);
            location = itemView.findViewById(R.id.text3);
            image = itemView.findViewById(R.id.imageinrecycler);
        }
    }
}

