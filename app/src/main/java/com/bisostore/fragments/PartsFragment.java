package com.bisostore.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bisostore.biso.R;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bisostore.adapters.PartsAdapter;
import com.bisostore.biso.HomeActivity;
import com.bisostore.main.Part;
import com.bisostore.services.DataService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PartsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    PartsAdapter partsAdapter;

    public PartsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PartsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PartsFragment newInstance(String param1, String param2) {
        PartsFragment fragment = new PartsFragment();
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
        Log.e("parts fragment","ok");
        View v=inflater.inflate(R.layout.fragment_parts, container, false);

        get_parts(v,"http://bisostore.com/mobile/get_categories/");
        RecyclerView recyclerView=(RecyclerView)v.findViewById(R.id.recycler_part);
        partsAdapter = new PartsAdapter(DataService.getInstance().getParts());
        recyclerView.setAdapter(partsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        return v;
    }

    public void get_parts(View v,String url){
        final DataService dataService=DataService.getInstance();

        url=url+ HomeActivity.getHomeActivity().loadPreferences("country").toLowerCase();

        JsonObjectRequest req2 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray part_array = response.getJSONArray("allParts");
                    Log.e("array is",part_array.toString());
                    for (int i=0;i<part_array.length();i++){
                        Part p=new Part();
                        Log.d("part name:",part_array.getJSONObject(i).getString("name_ar"));
                        p.setName_ar(part_array.getJSONObject(i).getString("name_ar"));
                        p.setName_en(part_array.getJSONObject(i).getString("name_en"));
                        p.setBelong(part_array.getJSONObject(i).getInt("belong"));
                        p.setPart_level(part_array.getJSONObject(i).getInt("part_level"));
                        p.setIcon(part_array.getJSONObject(i).getString("icon"));
                        p.setId(part_array.getJSONObject(i).getInt("part_id"));
                        dataService.add_part(p);
                    }
                }
                catch (JSONException j){Log.e("error2",j.getLocalizedMessage());}
                partsAdapter.notifyDataSetChanged();

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
