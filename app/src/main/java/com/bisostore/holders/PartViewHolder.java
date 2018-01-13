package com.bisostore.holders;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bisostore.biso.FirstActivity;
import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.R;
import com.bisostore.main.Part;

import java.util.Random;

/**
 * Created by USER on 02/08/2017.
 */

public class PartViewHolder extends RecyclerView.ViewHolder {
    private ImageView mainImage;
    private TextView mainText;
    private TextView icon;
    final String WEBSITE_URL="http://bisostore.com";
    final String PATH_URL="/files/uploads/";
    public PartViewHolder(View itemView){
        super(itemView);
        this.mainText=(TextView)itemView.findViewById(R.id.part_title);
        this.icon=(TextView)itemView.findViewById(R.id.part_icon);
    }
    public void update_ui(Part part){
        Log.e("part123",part.getName_ar());
        Log.d("language is",HomeActivity.getHomeActivity().loadPreferences("language"));
        Log.d("empty","language");
        if(HomeActivity.getHomeActivity().loadPreferences("language").contentEquals("ar")) {
            mainText.setText(part.getName_ar());
        }
        else
            mainText.setText(part.getName_en());
        mainText.setTextColor(Color.WHITE);

        String part_icon=part.getIcon();
        part_icon=part_icon.split("-")[1];
        Log.e("icon is",part_icon);

        Typeface fontFamily = Typeface.createFromAsset(HomeActivity.getHomeActivity().getAssets(), "flaticon.ttf");
        icon.setTypeface(fontFamily);
        icon.setTextSize(25);
        icon.setTextColor(Color.WHITE);

        int id=HomeActivity.getHomeActivity().getResources().getIdentifier(part_icon,"string",HomeActivity.getHomeActivity().getPackageName());
        Log.e("icon id is",String.valueOf(id));
        icon.setText(id);


    }
}
