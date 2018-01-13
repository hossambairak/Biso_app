package com.bisostore.holders;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.R;
import com.bisostore.main.Part;

import java.util.ArrayList;

/**
 * Created by USER on 07/08/2017.
 */

public class SubpartViewHolder extends RecyclerView.ViewHolder {

    TextView main_text;
    TextView icon;

    public SubpartViewHolder(View itemView) {
        super(itemView);
        this.main_text=(TextView)itemView.findViewById(R.id.subpart_title);
    }

    public void update_ui(Part part){

        Log.e("part123",part.getName_ar());

        if(HomeActivity.getHomeActivity().loadPreferences("language").contentEquals("ar"))
            main_text.setText(part.getName_ar());
        else
            main_text.setText(part.getName_en());
       // main_text.setTextColor(Color.WHITE);

    }

}
