package com.bisostore.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.R;
import com.bisostore.holders.PartViewHolder;
import com.bisostore.holders.PostViewHolder;
import com.bisostore.main.Part;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by USER on 02/08/2017.
 */

public class PartsAdapter extends RecyclerView.Adapter<PartViewHolder>  {
    ArrayList<Part> parts;

    public PartsAdapter(ArrayList<Part> parts) {
        this.parts = parts;
    }

    @Override
    public void onBindViewHolder(PartViewHolder holder,final int position) {
        final Part part=parts.get(position);
        holder.update_ui(part);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.getHomeActivity().load_subparts(part);
            }
        });
    }

    @Override
    public int getItemCount() {
        return parts.size();
    }

    @Override
    public PartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("parts adapter","ok");
        View partCard= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_part,parent,false);


        int[] array = HomeActivity.getHomeActivity().getResources().getIntArray(R.array.part_colors);
        Log.e("array length",String.valueOf(array.length));
        Log.e("array is",String.valueOf(array[1]));
        int randomStr = array[new Random().nextInt(array.length)];
        partCard.setBackgroundColor(randomStr);

        return new PartViewHolder(partCard);
    }
}
