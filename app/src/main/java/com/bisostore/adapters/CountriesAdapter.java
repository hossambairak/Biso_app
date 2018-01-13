package com.bisostore.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bisostore.biso.FirstActivity;
import com.bisostore.biso.R;
import com.bisostore.holders.CountryViewHolder;
import com.bisostore.main.Country;

import java.util.ArrayList;

/**
 * Created by USER on 03/08/2017.
 */

public class CountriesAdapter extends RecyclerView.Adapter<CountryViewHolder> {
    public CountriesAdapter(ArrayList<Country> countries) {
        this.countries = countries;
    }

    ArrayList<Country> countries;

    @Override
    public void onBindViewHolder(CountryViewHolder holder,final int position) {
        final Country country=countries.get(position);
        Log.e("country name",country.getName_ar());
        holder.update_ui(country);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirstActivity.getFirstActivity().save_prefrences(country);
            }
        });
    }

    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View countryCard= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_country,parent,false);
        return new CountryViewHolder(countryCard);
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }
}
