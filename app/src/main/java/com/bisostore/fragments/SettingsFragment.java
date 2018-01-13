package com.bisostore.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.R;
import com.bisostore.main.Country;
import com.bisostore.services.DataService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    final String COUNTRY_TAG="country";
    final String LANGUAGE_TAG="language";

    private RadioGroup radioGroup;
    private RadioButton radioButton;


    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        //check if we select country
        final View v = inflater.inflate(R.layout.fragment_settings, container, false);
        String country=HomeActivity.getHomeActivity().loadPreferences(COUNTRY_TAG);
        String language=HomeActivity.getHomeActivity().loadPreferences(LANGUAGE_TAG);

        Log.d("get cou","ss");
        final DataService d = DataService.getInstance();
        String url = Country.WEBSITE_URL + Country.countryies_PATH;
        JsonObjectRequest req2 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String u1 = Country.WEBSITE_URL + Country.countryies_PATH;

                    JSONArray country_array = response.getJSONArray("countries");
                    Log.d("get cou1",String.valueOf(country_array.length()));
                    Log.d("get cou1",u1);
                    for (int i = 0; i < country_array.length(); i++) {
                        JSONObject jsonObject = country_array.getJSONObject(i);
                        String name_ar = jsonObject.getString("name_ar");
                        String name_en = jsonObject.getString("name_en");
                        String id = jsonObject.getString("id");
                        int countryid = jsonObject.getInt("countryid");
                        int shown = jsonObject.getInt("shown");
                        Country country = new Country(countryid, id, name_ar, name_en, shown);
                        d.add_country(country);
                    }
                } catch (JSONException j) {
                    Log.e("error2", j.getLocalizedMessage());
                }
                //create fragment
                FragmentManager fm = HomeActivity.getHomeActivity().getSupportFragmentManager();
                CountriesFragment countriesFragment = (CountriesFragment) fm.findFragmentById(R.id.container_first1);
                //if (countriesFragment == null) {
                    countriesFragment = CountriesFragment.newInstance("blah", "kah");
                    fm.beginTransaction().add(R.id.container_first1, countriesFragment).commit();
                //}

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error1", error.toString());
            }
        });
        Volley.newRequestQueue(getContext()).add(req2);


        return v;
    }


}
