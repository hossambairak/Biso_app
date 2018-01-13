package com.bisostore.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bisostore.adapters.PartsAdapter;
import com.bisostore.biso.HomeActivity;
import com.bisostore.main.Part;
import com.bisostore.services.DataService;
import com.bisostore.biso.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Part> parts = new ArrayList<>();
    PartsAdapter partsAdapter;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> arrayAdapter_sub;


    ArrayList<Part> a1;
    ArrayList<Part> a2;


    AlertDialog.Builder builderSingle;
    AlertDialog.Builder builderSingle_sub;

    public MailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MailFragment newInstance(String param1, String param2) {
        MailFragment fragment = new MailFragment();
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
        View v = inflater.inflate(R.layout.fragment_mail, container, false);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice);
        arrayAdapter_sub = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice);
        a1 = new ArrayList<>();
        a2 = new ArrayList<>();
        builderSingle = new AlertDialog.Builder(getContext());
        builderSingle_sub = new AlertDialog.Builder(getContext());
        get_parts("http://bisostore.com/mobile/get_main_parts",1);
        partsAdapter = new PartsAdapter(parts);
        init_builder(arrayAdapter,builderSingle,1);
        init_builder(arrayAdapter_sub,builderSingle_sub,2);

        return v;
    }


    public void init_builder(final ArrayAdapter<String> arrayAdapter,AlertDialog.Builder builderSingle,final int type){
        builderSingle.setTitle("Select One Name:-");

        builderSingle.setNegativeButton("cancel",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog,final int which) {
                String strName = arrayAdapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(getContext());
                builderInner.setMessage(strName);
                builderInner.setTitle("Your Selected Item is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which1) {

                        Log.d("part is:",String.valueOf(which));
                        dialog.dismiss();
                        if(type==1)
                            get_parts("http://bisostore.com/mobile/get_sub_parts/"+String.valueOf(a1.get(which).getId()),2);
                    }
                });
                builderInner.show();
            }
        });
    }

    public void get_parts(String url,final int type){

        Log.e("url is1",url);
        JsonArrayRequest req2 = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray part_array = response;
                    for (int j=0;j<part_array.length();j++){
                        Part p=new Part();
                        int i = j;
                        p.setName_ar(part_array.getJSONObject(i).getString("name_ar"));
                        p.setName_en(part_array.getJSONObject(i).getString("name_en"));
                        p.setBelong(part_array.getJSONObject(i).getInt("belong"));
                        p.setPart_level(part_array.getJSONObject(i).getInt("part_level"));
                        p.setIcon(part_array.getJSONObject(i).getString("icon"));
                        p.setId(part_array.getJSONObject(i).getInt("part_id"));
                        Log.d("part name:",p.getName_ar());
                        if(type==1) {
                            a1.add(p);
                            arrayAdapter.add(p.getName_ar());
                        }
                        else if(type==2) {
                            a2.add(p);
                            arrayAdapter_sub.add(p.getName_ar());
                        }
                        parts.add(p);
                    }
                }
                catch (JSONException j){Log.e("error2",j.getLocalizedMessage());}
                partsAdapter = new PartsAdapter(parts);
                if(type==1)
                    builderSingle.show();
                else if(type==2)
                    builderSingle_sub.show();
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
        Volley.newRequestQueue(getContext()).add(req2);

    }

}
