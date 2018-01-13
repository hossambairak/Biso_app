package com.bisostore.fragments;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
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
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEYWORD = "keyword";

    // TODO: Rename and change types of parameters
    private String Keyword;
    PostListAdapter postListAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(KEYWORD, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Keyword = getArguments().getString(KEYWORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.search_result));
        Log.d("search fragment","new");
        Log.d("keyword:", Keyword);
        DataService.getInstance().clear();
        search_post(v);
        RecyclerView recyclerView=(RecyclerView)v.findViewById(R.id.content_last_posts_recycler);
        recyclerView.addItemDecoration(new com.bisostore.Decoration.DividerItemDecoration(getContext()));


        Log.e("size",String.valueOf(DataService.getInstance().getMostPosts().size()));
        postListAdapter = new PostListAdapter(DataService.getInstance().get_posts_list());
        recyclerView.setAdapter(postListAdapter);
        LinearLayoutManager lm= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(lm);
        return v;
    }


    public void search_post(View v){
        String url = "http://bisostore.com/mobile/search_post";
        final DataService d=DataService.getInstance();
        Uri.Builder builder = Uri.parse(url).buildUpon();
        builder.appendQueryParameter("keyword", Keyword);
        String searchUrl=builder.build().toString();


        Log.e("url new is",url);
        JsonObjectRequest req2 = new JsonObjectRequest(Request.Method.GET, searchUrl, null, new Response.Listener<JSONObject>() {
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
