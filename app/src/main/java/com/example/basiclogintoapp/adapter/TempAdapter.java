package com.example.basiclogintoapp.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basiclogintoapp.R;


public class TempAdapter extends RecyclerView.Adapter<TempAdapter.ViewHolder> {

    private final int count;
    String[] Author;
    String[] data2;
    String[] data;
    Context context;
    String[] picture;


    public TempAdapter(Context context, String[] data, int count, String[] picture, String[] data2, String[] Author) {
        this.data = data;
        this.context = context;
        this.count = count;
        this.data2 = data2;
        this.picture = picture;
        this.Author = Author;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.swiped_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {

    }

    @Override
    public int getItemCount() {
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


        }
    }


}

