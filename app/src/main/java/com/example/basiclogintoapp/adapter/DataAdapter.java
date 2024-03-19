package com.example.basiclogintoapp.adapter;

import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.basiclogintoapp.MessageActivity;
import com.example.basiclogintoapp.R;

import java.util.Arrays;
import java.util.Comparator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    private String[] uid;
    private String[] usernames;
    private String[] imageUrls;
    private String[] points;
    private Context context;

    public DataAdapter(Context context, String[] usernames, String[] imageUrls, String[] points, String[] uid) {
        this.context = context;
        this.usernames = usernames;
        this.imageUrls = imageUrls;
        this.points = points;
        this.uid = uid;
        // Sort the data based on points
        sortDataByPoints();
    }

    // Sort data based on points
    private void sortDataByPoints() {
        Integer[] pointsArray = new Integer[points.length];
        for (int i = 0; i < points.length; i++) {
            pointsArray[i] = Integer.parseInt(points[i]);
        }

        // Sort the points array and rearrange other arrays accordingly
        Arrays.sort(pointsArray, Comparator.reverseOrder());

        String[] sortedUsernames = new String[usernames.length];
        String[] sortedImageUrls = new String[imageUrls.length];
        String[] sortedPoints = new String[points.length];
        String[] sortedUid = new String[uid.length];

        for (int i = 0; i < points.length; i++) {
            int index = Arrays.asList(points).indexOf(pointsArray[i].toString());
            sortedUsernames[i] = usernames[index];
            sortedImageUrls[i] = imageUrls[index];
            sortedPoints[i] = points[index];
            sortedUid[i] = uid[index];
            points[index] = null; // Mark as visited
        }

        this.usernames = sortedUsernames;
        this.imageUrls = sortedImageUrls;
        this.points = sortedPoints;
        this.uid = sortedUid;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        holder.bindData(usernames[position], points[position], imageUrls[position], uid[position]);
    }

    @Override
    public int getItemCount() {
        return usernames.length;
    }

    class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView, pts;
        ImageView pfp;
        String uid;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.username);
            pts = itemView.findViewById(R.id.points);
            pfp = itemView.findViewById(R.id.profilePic);
            itemView.setOnClickListener(this);
        }

        public void bindData(String username, String points, String imageUrl, String uid) {
            textView.setText(username);
            pts.setText("Points: " + points);
            this.uid = uid;

            // Load image into ImageView using Glide
            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.baseline_person_24) // Placeholder image while loading
                    .error(R.drawable.baseline_perm_identity_24) // Error image if loading fails
                    .into(pfp);
        }

        @Override
        public void onClick(View v) {
            // Start the Message activity and pass the UID
            Intent i = new Intent(itemView.getContext(), MessageActivity.class);
            i.putExtra("userid", uid);
            itemView.getContext().startActivity(i);
        }

    }
}
