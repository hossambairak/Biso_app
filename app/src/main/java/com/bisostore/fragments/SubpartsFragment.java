package com.bisostore.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bisostore.adapters.SubpartAdapters;
import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.R;
import com.bisostore.main.Part;
import com.bisostore.main.Post;
import com.bisostore.services.DataService;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubpartsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubpartsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PART_ID = "part_id";

    private int partId;
    //adapter
    SubpartAdapters adapters;

    public SubpartsFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SubpartsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubpartsFragment newInstance(int partID) {
        SubpartsFragment fragment = new SubpartsFragment();
        Bundle args = new Bundle();
        args.putInt(PART_ID, partID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            partId = getArguments().getInt(PART_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_subparts, container, false);
        DataService.getInstance().clear();
        get_subparts(partId);
        RecyclerView recyclerView=(RecyclerView)v.findViewById(R.id.recycler_subpart);
        recyclerView.addItemDecoration(new com.bisostore.Decoration.DividerItemDecoration(getContext()));
        adapters =new SubpartAdapters(DataService.getInstance().getsubParts());
        recyclerView.setAdapter(adapters);
        LinearLayoutManager lm=new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        return v;
    }

    public void get_subparts(int partId){

        String country = HomeActivity.getHomeActivity().loadPreferences("country");
        String url= Post.WEBSITE_URL+ Part.SUPPART+partId + "/" + country.toLowerCase();
        Log.e("url is",url);
        //create new volley request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray part_array = response.getJSONArray("allParts");
                    Log.e("array is", part_array.toString());
                    for (int i = 0; i < part_array.length(); i++) {
                        Part p = new Part();

                        p.setName_ar(part_array.getJSONObject(i).getString("name_ar"));
                        p.setName_en(part_array.getJSONObject(i).getString("name_en"));
                        p.setBelong(part_array.getJSONObject(i).getInt("belong"));
                        p.setPart_level(part_array.getJSONObject(i).getInt("part_level"));
                        p.setIcon(part_array.getJSONObject(i).getString("icon"));
                        p.setId(part_array.getJSONObject(i).getInt("part_id"));
                        DataService.getInstance().add_subpart(p);
                    }
                }
                catch (Exception e){

                }
                adapters.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }
}
