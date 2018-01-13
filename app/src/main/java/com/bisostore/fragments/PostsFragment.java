package com.bisostore.fragments;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.bisostore.adapters.PostsAdapter;
import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.R;
import com.bisostore.main.Post;
import com.bisostore.services.DataService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsFragment extends Fragment {

    private static final String ARG_POST_TYPE = "post_type";
    private static final String ARG_POST_ID = "post_id";
    private static final String COUNTRY="country";
    private static final String LANGUAGE="language";

    private int postType;
    private int post_id;

    public static final int POST_TYPE_MOST=1;
    public static final int POST_TYPE_RECENT=0;
    public static final int POST_TYPE_RELATED=2;
    public String country;
    public String language;
    ArrayList<Post> most_posts = new ArrayList<>();
    ArrayList<Post> last_posts = new ArrayList<>();

    public PostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param postType The Post Type
     * @return A new instance of fragment PostsFragment.
     */
    public static PostsFragment newInstance(int postType,String country,String language,int post_id) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POST_TYPE,postType);
        args.putInt(ARG_POST_ID,post_id);
        args.putString(COUNTRY,country);
        args.putString(LANGUAGE,language);
        fragment.setArguments(args);
        return fragment;
    }
    public static PostsFragment newInstance(int postType,String country,String language) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POST_TYPE,postType);
        args.putString(COUNTRY,country);
        args.putString(LANGUAGE,language);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postType = getArguments().getInt(ARG_POST_TYPE);
            post_id = getArguments().getInt(ARG_POST_ID);
            country = getArguments().getString(COUNTRY);
            language = getArguments().getString(LANGUAGE);
        }
    }
    PostsAdapter postsAdapter;
    PostsAdapter postsAdapter1;
    PostsAdapter postsAdapter2;
    PostsAdapter postsAdapter3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_posts, container, false);

        if(postType==POST_TYPE_RECENT) {
            get_posts(v, Post.WEBSITE_URL + "/get_last_ads/", 1);
            Log.d("last","ads");
        }
        else if(postType == POST_TYPE_MOST) {
            get_posts(v, Post.WEBSITE_URL + "/get_most_ads/", 0);
            Log.d("most","ads");
        }
       else
            get_posts(v,Post.WEBSITE_URL+"/get_related/"+post_id,2);

        RecyclerView recyclerView=(RecyclerView) v.findViewById(R.id.recycler_post);
        recyclerView.setHasFixedSize(true);
        if (postType == POST_TYPE_MOST) {
            //most_posts = DataService.getInstance().getMostPosts();
            postsAdapter = new PostsAdapter(DataService.getInstance().getMostPosts());
            // postsAdapter1 = new PostsAdapter(DataService.getInstance().getMostPosts());
            Log.d("most2",String .valueOf(DataService.getInstance().getMostPosts().size()));
            recyclerView.setAdapter(postsAdapter);
        }
        else if(postType == POST_TYPE_RECENT) {
            postsAdapter =new PostsAdapter(DataService.getInstance().getRecentPosts());
            //postsAdapter3 = new PostsAdapter(DataService.getInstance().getRelatedPosts());
            Log.d("last2",String.valueOf(DataService.getInstance().getRecentPosts().size()));
            recyclerView.setAdapter(postsAdapter);
        }
        else
        {
            Log.d("last2","ads");
            postsAdapter = new PostsAdapter(DataService.getInstance().getRelatedPosts());
            recyclerView.setAdapter(postsAdapter);
        }

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        return v;
    }

    public void get_posts(final View v,String url, final int type){
        Log.d("url is",url);
if(type!=POST_TYPE_RELATED)
        url=url+ HomeActivity.getHomeActivity().loadPreferences("country").toLowerCase();
        else
    Log.d("related","ok");
        final ProgressDialog pd = ProgressDialog.show(getContext(),null,"Please wait");
        JsonObjectRequest req2 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("res",response.toString());
                try {
                    //DataService.getInstance().clear();
                    if(pd!=null && pd.isShowing())
                        pd.dismiss();
                    JSONArray post_array = response.getJSONArray("Posts");
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
                        Log.d("post images",p.getImages());
                        if(type==2)
                            Log.d("get related","posts");
                        p.setPost_id(post_array.getJSONObject(i).getInt("post_id"));
                        //d.add_post(p,type);
                        DataService.getInstance().add_post(p,postType);

                    }

                }
                catch (JSONException j){if(pd!=null && pd.isShowing())
                    pd.dismiss();
                    Log.e("error222",j.getLocalizedMessage());}
                postsAdapter.notifyDataSetChanged();

                Log.d("notify","n");
                //DataService.getInstance().clear();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error1",error.toString());
            }
        });


        int TimeOut=30000;
        RetryPolicy retryPolicy=new DefaultRetryPolicy(TimeOut,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req2.setRetryPolicy(retryPolicy);
        Volley.newRequestQueue(v.getContext()).add(req2);

    }
}
