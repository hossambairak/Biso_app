package com.bisostore.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.R;
import com.bisostore.holders.PartViewHolder;
import com.bisostore.holders.SubpartViewHolder;
import com.bisostore.main.Part;

import java.util.ArrayList;

/**
 * Created by USER on 07/08/2017.
 */

public class SubpartAdapters extends RecyclerView.Adapter<SubpartViewHolder> {

    public SubpartAdapters(ArrayList<Part> parts) {
        this.parts = parts;
    }

    ArrayList<Part> parts;

    @Override
    public void onBindViewHolder(SubpartViewHolder holder, final int position) {
        final Part p=parts.get(position);
        holder.update_ui(p);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.getHomeActivity().loadPostsList(p);
            }
        });
    }

    @Override
    public SubpartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View Subpart_card= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_subpart,parent,false);

        return new SubpartViewHolder(Subpart_card);
    }

    @Override
    public int getItemCount() {
        return parts.size();
    }

}
