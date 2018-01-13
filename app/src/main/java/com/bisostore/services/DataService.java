package com.bisostore.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.CookieManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bisostore.adapters.CountriesAdapter;
import com.bisostore.main.Comment;
import com.bisostore.main.Country;
import com.bisostore.main.Part;
import com.bisostore.main.Post;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.StreamHandler;

/**
 * Created by HOSSAM on 01/08/2017.
 */
public class DataService {


    ArrayList<Post> most_list=new ArrayList<>();
    ArrayList<Post> related_list=new ArrayList<>();
    ArrayList<Post> recent_list=new ArrayList<>();
    ArrayList<Post> posts_list = new ArrayList<>();
    ArrayList<Part> parts=new ArrayList<>();
    ArrayList<Part> subPart=new ArrayList<>();
    ArrayList<Country> countries=new ArrayList<>();
    ArrayList<Comment> comments = new ArrayList<>();


    private static DataService ourInstance = new DataService();

    public static DataService getInstance() {
        return ourInstance;
    }

    private DataService() {
    }

    public void clear(){
        parts.clear();
        most_list.clear();
        recent_list.clear();
        posts_list.clear();
        related_list.clear();
        subPart.clear();
        comments.clear();
    }
    public void add_post(Post p,int type){
        Log.e("type", String.valueOf(type));
        if(type==0)
            recent_list.add(p);
        else if(type==1)
            most_list.add(p);
        else if(type==2)
            related_list.add(p);
        else
            posts_list.add(p);
    }

    public void add_comment(Comment comment){comments.add(comment);}
    public void add_part(Part part){parts.add(part);}
    public void add_subpart(Part subpart){subPart.add(subpart);}
    public void add_country(Country country){
        countries.add(country);
    }
    public ArrayList<Post> getRecentPosts(){
        //Download from internet
        return recent_list;
    }
    public ArrayList<Post> getMostPosts(){
        //Download from internet
        return most_list;
    }
    public ArrayList<Post> getRelatedPosts(){
        //Download from internet
        return related_list;
    }

    public ArrayList<Part> getParts()
    {
        return parts;
    }

    public ArrayList<Part> getsubParts()
    {
        return subPart;
    }

    public ArrayList<Country> getCountries(Context context) {
        return  countries;

    }

    public ArrayList<Post> get_posts_list(){
        return posts_list;
    }
}
