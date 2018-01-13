package com.bisostore.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bisostore.adapters.PostListAdapter;
import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.R;
import com.bisostore.main.Post;
import com.bisostore.services.DataService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    PostListAdapter postListAdapter;

    public FavFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavFragment newInstance(String param1, String param2) {
        FavFragment fragment = new FavFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fav, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.fav));
        DataService.getInstance().clear();
        get_posts(v);
        RecyclerView recyclerView=(RecyclerView)v.findViewById(R.id.content_last_posts_recycler);
        recyclerView.addItemDecoration(new com.bisostore.Decoration.DividerItemDecoration(getContext()));
        postListAdapter = new PostListAdapter(DataService.getInstance().get_posts_list());
        recyclerView.setAdapter(postListAdapter);
        LinearLayoutManager lm= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(lm);
        return v;
    }

    public void get_posts(View v){
        final DataService d=DataService.getInstance();
        String  id = HomeActivity.getHomeActivity().loadPreferences("id");
        String url = "http://bisostore.com/mobile/get_fav/"+id;
        JsonObjectRequest req2 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray post_array = response.getJSONArray("fav");
                    for (int i=0;i<post_array.length();i++){
                        Log.e("fav","post");
                        Post p=new Post();

                        p.setPost_title(post_array.getJSONObject(i).getString("post_title"));
                        p.setPost_title_ar(post_array.getJSONObject(i).getString("post_title_ar"));
                        p.setPost_title_en(post_array.getJSONObject(i).getString("post_title_en"));
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
