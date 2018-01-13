package com.bisostore.holders;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

/**
 * Created by HOSSAM on 31/07/2017.
 */

public class PostViewHolder extends RecyclerView.ViewHolder {

    private ImageView mainImage;
    private TextView mainText;
    final String WEBSITE_URL="http://bisostore.com";
    final String PATH_URL="/files/uploads/";
    public PostViewHolder(View itemView){
        super(itemView);
        this.mainImage=(ImageView)itemView.findViewById(R.id.main_image);
        this.mainText=(TextView)itemView.findViewById(R.id.main_text);
    }

    public void update_ui(Post post){
Log.d("momo",post.getPost_title_ar());
        if(HomeActivity.getHomeActivity().loadPreferences("language").contentEquals("ar"))
            mainText.setText(post.getPost_title_ar());
        else
            mainText.setText(post.getPost_title_en());
        String images = post.getImages();
        String image = images.split(",")[0];
        String uri = "";
        if(!images.equals("1455985567.png"))
            uri=WEBSITE_URL+PATH_URL+"thumb_"+images.split(",")[0];
        else
            uri = WEBSITE_URL+"/files/images/1455985567.png";
        ImageRequest request=new ImageRequest(uri, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                mainImage.setImageBitmap(response);
                Log.e("work",response.toString());
            }
        }, 0, 0,ImageView.ScaleType.CENTER_CROP,null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.toString());
            }
        });
        Volley.newRequestQueue(HomeActivity.context).add(request);
    }
}
