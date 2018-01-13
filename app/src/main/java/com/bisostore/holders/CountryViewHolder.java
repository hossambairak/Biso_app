package com.bisostore.holders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bisostore.biso.R;
import com.bisostore.main.Country;

/**
 * Created by HOSSAM on 03/08/2017.
 */

public class CountryViewHolder extends RecyclerView.ViewHolder{
    private ImageView mainImage;
    private TextView mainText;
    public CountryViewHolder(View itemview){
        super(itemview);
        mainImage=(ImageView)itemview.findViewById(R.id.countryImage);
        mainText=(TextView)itemview.findViewById(R.id.countryName);
    }
    public void update_ui(Country country){
        mainText.setText(country.getName_ar());
        //mainImage.setImageResource();
        //Country c=
      //  int id = getResources().getIdentifier("yourpackagename:drawable/" + StringGenerated, null, null);
       Log.e("image",mainImage.getContext().getPackageName()+":drawable/flags/flag_" + country.getId().toLowerCase()+".png");
       int recource=mainImage.getResources().getIdentifier("flag_" + country.getId().toLowerCase(), "drawable", mainImage.getContext().getPackageName());
        Log.e("rec value",String.valueOf(recource));
       mainImage.setImageResource(recource);

    }
}
