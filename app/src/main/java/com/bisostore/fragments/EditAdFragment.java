package com.bisostore.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.R;
import com.bisostore.main.Post;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditAdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditAdFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private Post post;

    //ad elements
    private EditText title;
    private EditText time;
    private  EditText text;
    private EditText price;

    public EditAdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment EditAdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditAdFragment newInstance(Post p) {
        EditAdFragment fragment = new EditAdFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1,p);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = (Post) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v=inflater.inflate(R.layout.fragment_edit_ad, container, false);
        title = (EditText) v.findViewById(R.id.ad_title);
        time = (EditText) v.findViewById(R.id.ad_period);
        price = (EditText) v.findViewById(R.id.ad_price);
        text = (EditText) v.findViewById(R.id.ad_text);

        if(HomeActivity.getHomeActivity().loadPreferences("language").equals("ar")){
            title.setText(post.getPost_title_ar());
        }
        else{
            title.setText(post.getPost_title_en());
        }
        text.setText(post.getDescrption());
        Log.d("period1",String.valueOf(post.getPeriod()));
        time.setText(String.valueOf(post.getPeriod()));
        price.setText(String.valueOf(post.getPrice()));



        return v;
    }

}
