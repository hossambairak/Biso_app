package com.bisostore.holders;

import android.graphics.Bitmap;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.R;
import com.bisostore.main.Post;

import java.util.ArrayList;

/**
 * Created by USER on 13/08/2017.
 */

public class PostListViewHolder extends RecyclerView.ViewHolder{

    ImageView thumbnail;
    TextView title;
    final String WEBSITE_URL="http://bisostore.com";
    final String PATH_URL="/files/uploads/";

    public PostListViewHolder(View ItemView){
        super(ItemView);
        this.thumbnail=(ImageView)ItemView.findViewById(R.id.post_list_thumbnail);
        this.title = (TextView)ItemView.findViewById(R.id.post_list_title);

    }

    public void update_ui(Post p){

        if(HomeActivity.getHomeActivity().loadPreferences("language").contentEquals("ar"))
            title.setText(p.getPost_title_ar());
        else
            title.setText(p.getPost_title_en());
        String images=p.getImages();
        String uri="";
        if(!images.equals("1455985567.png"))
            uri=WEBSITE_URL+PATH_URL+"thumb_"+images.split(",")[0];
        else
            uri = WEBSITE_URL+"/files/images/1455985567.png";
        ImageRequest imageRequest=new ImageRequest(uri, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                thumbnail.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(HomeActivity.getHomeActivity().getApplicationContext()).add(imageRequest);


    }
}
