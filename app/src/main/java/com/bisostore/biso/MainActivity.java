package com.bisostore.biso;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bisostore.main.Part;
import com.bisostore.main.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final String WEBSITE_URL="http://bisostore.com/mobile/";
    final String GET_LAST_POST = "get_last_ads/sy";
    final String GET_CATEGORIES = "get_categories/sy";
    ArrayList<Post> last_posts = new ArrayList<Post>();
    ArrayList<Part> Parts = new ArrayList<Part>();
    private TextView parts_view;
    private TextView posts_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parts_view=(TextView)findViewById(R.id.parts);
        posts_view=(TextView)findViewById(R.id.posts);
        String url1 = WEBSITE_URL+GET_CATEGORIES;
        String url2 = WEBSITE_URL+GET_LAST_POST;
        JsonObjectRequest req1 = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray part_array = response.getJSONArray("allParts");
                    Log.e("array is",part_array.toString());
                    for (int i=0;i<part_array.length();i++){
                        Part p=new Part();
                        p.setName_ar(part_array.getJSONObject(i).getString("name_ar"));
                        p.setName_en(part_array.getJSONObject(i).getString("name_en"));
                        p.setPart_level(part_array.getJSONObject(i).getInt("part_level"));
                        p.setBelong(part_array.getJSONObject(i).getInt("belong"));
                        Log.e("part",p.getName_ar());
                        Parts.add(p);
                    }
                }
                catch (JSONException j){Log.e("error2",j.getLocalizedMessage());}
                update_ui1();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error1",error.toString());
            }
        });

        JsonObjectRequest req2 = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray part_array = response.getJSONArray("lastPosts");
                    Log.e("array is",part_array.toString());
                    for (int i=0;i<part_array.length();i++){
                        Post p=new Post();
                        //p.setCountry(part_array.getJSONObject(i).getString("country"));
                        p.setPost_title(part_array.getJSONObject(i).getString("post_title"));
                        p.setPost_title_ar(part_array.getJSONObject(i).getString("post_title_ar"));
                        //p.setPrice(part_array.getJSONObject(i).getInt("price"));
                        last_posts.add(p);
                    }
                }
                catch (JSONException j){Log.e("error2",j.getLocalizedMessage());}
                update_ui2();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error1",error.toString());
            }
        });
        Volley.newRequestQueue(this).add(req1);
        Volley.newRequestQueue(this).add(req2);
        Log.e("request",req1.getBodyContentType());
    }

    public void update_ui1(){
        String names="";
        for (Part p:Parts
             ) {
            names+= p.getName_ar()+"\n";
        }
        parts_view.setText(names);
    }

    public void update_ui2(){
        String names="";
        for (Post p:last_posts
                ) {
            names+= p.getPost_title_ar()+"\n";
        }
        posts_view.setText(names);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2,menu);
        Log.e("inflate","inflate");
        return super.onCreateOptionsMenu(menu);
    }
}
