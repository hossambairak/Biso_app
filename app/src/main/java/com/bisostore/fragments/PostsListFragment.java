package com.bisostore.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bisostore.adapters.ImageAdapter;
import com.bisostore.adapters.PostListAdapter;
import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.R;
import com.bisostore.holders.PostListViewHolder;
import com.bisostore.main.Post;
import com.bisostore.services.DataService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CATEGORY = "category";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int category;
    private String mParam2;
    PostListAdapter postListAdapter;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN= {R.drawable.s,R.drawable.mb};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    ArrayList<Bitmap> arr = new ArrayList<>();

    public PostsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @param param2 Parameter 2.
     * @return A new instance of fragment PostsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostsListFragment newInstance(int category, String param2) {
        PostsListFragment fragment = new PostsListFragment();
        Bundle args = new Bundle();
        args.putInt(CATEGORY, category);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getInt(CATEGORY);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_posts_list, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        DataService.getInstance().clear();
        get_posts(v, Post.WEBSITE_URL+"/get_posts_list/"+category);
        RecyclerView recyclerView=(RecyclerView)v.findViewById(R.id.content_last_posts_recycler);
        recyclerView.addItemDecoration(new com.bisostore.Decoration.DividerItemDecoration(getContext()));


        Log.e("size",String.valueOf(DataService.getInstance().getMostPosts().size()));
        postListAdapter = new PostListAdapter(DataService.getInstance().get_posts_list());
        recyclerView.setAdapter(postListAdapter);
        LinearLayoutManager lm= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(lm);
        return v;
    }

    public void get_posts(View v,String url){
        final DataService d=DataService.getInstance();

        url = url +"/" + HomeActivity.getHomeActivity().loadPreferences("country").toLowerCase();

        JsonObjectRequest req2 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray post_array = response.getJSONArray("posts");
                    Log.e("len",String.valueOf(post_array.length()));
                    for (int i=0;i<post_array.length();i++){
                        Log.e("add","post");
                        Post p=new Post();

                        p.setPost_title(post_array.getJSONObject(i).getString("post_title"));
                        p.setPost_title_ar(post_array.getJSONObject(i).getString("post_title_ar"));
                        p.setPost_title_en(post_array.getJSONObject(i).getString("post_title_en"));
                        if(post_array.getJSONObject(i).getString("images").isEmpty())
                            p.setImages("1455985567.png");
                        else
                            p.setImages(post_array.getJSONObject(i).getString("images"));
                        p.setPost_id(post_array.getJSONObject(i).getInt("post_id"));
                        d.add_post(p,3);
                    }
                }
                catch (JSONException j){Log.e("error2",j.getLocalizedMessage());}
                Log.e("tag",String.valueOf(DataService.getInstance().getMostPosts().size()));
                postListAdapter.notifyDataSetChanged();
                //DataService.getInstance().clear();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error1",error.toString());
            }
        });
        Volley.newRequestQueue(v.getContext()).add(req2);

    }


}
