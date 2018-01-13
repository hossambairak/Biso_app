package com.bisostore.fragments;


import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.R;
import com.bisostore.main.Post;
import com.bisostore.services.DataService;

import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends android.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String COUNTRY = "country";
    private static final String LANGUAGE = "language";

    // TODO: Rename and change types of parameters
    private String country;
    private String language;


    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String country, String language) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(COUNTRY, country);
        args.putString(LANGUAGE, language);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            country = getArguments().getString(COUNTRY);
            language = getArguments().getString(LANGUAGE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main2, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("menu click","search");
        //noinspection SimplifiableIfStatement
        if (id == R.id.search_menu) {
            Log.e("search","click");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v=inflater.inflate(R.layout.fragment_main, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));


        //set fav invisible
        if(HomeActivity.getHomeActivity().loadPreferences("name").isEmpty()) {
            HomeActivity.getHomeActivity().navigationView.getMenu().findItem(R.id.nav_fav).setVisible(false);
            HomeActivity.getHomeActivity().navigationView.getMenu().setGroupVisible(R.id.nav_control,false);

        }
        else {
            HomeActivity.getHomeActivity().navigationView.getMenu().findItem(R.id.nav_fav).setVisible(true);
            HomeActivity.getHomeActivity().navigationView.getMenu().setGroupVisible(R.id.nav_control,true);
        }
        android.app.FragmentManager fm=getActivity().getFragmentManager();

        PostsFragment postsFragment1;
        PostsFragment postsFragment2;
        PartsFragment partsFragment;

        postsFragment1=PostsFragment.newInstance(PostsFragment.POST_TYPE_RECENT,country,language);
        fm.beginTransaction().add(R.id.container_top_row,postsFragment1).commit();
        DataService.getInstance().clear();
        postsFragment2=PostsFragment.newInstance(PostsFragment.POST_TYPE_MOST,country,language);

        fm.beginTransaction().add(R.id.container_middle_row,postsFragment2).commit();
        partsFragment=PartsFragment.newInstance("","");
        fm.beginTransaction().add(R.id.container_last_row,partsFragment).commit();
        return v;
    }
}
