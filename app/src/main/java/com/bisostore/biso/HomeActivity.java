package com.bisostore.biso;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bisostore.fragments.FavFragment;
import com.bisostore.fragments.LoginFragment;
import com.bisostore.fragments.MailFragment;
import com.bisostore.fragments.MainFragment;
import com.bisostore.fragments.PaypalFragment;
import com.bisostore.fragments.PostDetails;
import com.bisostore.fragments.PostsListFragment;
import com.bisostore.fragments.SearchFragment;
import com.bisostore.fragments.SettingsFragment;
import com.bisostore.fragments.SignFragment;
import com.bisostore.fragments.SubpartsFragment;
import com.bisostore.fragments.UserAdsFragment;
import com.bisostore.main.Part;
import com.bisostore.main.Post;
import com.bisostore.services.DataService;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static public HomeActivity getHomeActivity() {
        return homeActivity;
    }

    private void setHomeActivity(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    static private HomeActivity homeActivity;
    final String PATH_URL="/files/uploads/";
    private RecyclerView RecyclerView;
    public NavigationView navigationView;
    private android.support.v7.widget.RecyclerView.Adapter mAdapter;
    private android.support.v7.widget.RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Post> postList=new ArrayList<>();
    final String WEBSITE_URL="http://bisostore.com/mobile/";
    final String GET_LAST_POST = "get_last_ads/";
    final String GET_MOST_POST = "get_most_ads/";
    final String GET_CATEGORIES = "get_categories/";

    private static final String TAG = "paymentExample";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;


    private DrawerLayout drawer;

    static public Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHomeActivity(this);
        setContentView(R.layout.activity_main2);
        context=getApplicationContext();



        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.bisostore.biso",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.search_menu);



        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) toolbar.getMenu().findItem(R.id.action_search)
                .getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(true);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.
                Log.d("search text is1: ",query);
                //move to search fragment
                SearchFragment searchFragment = new SearchFragment().newInstance(query);
                getFragmentManager().beginTransaction().replace(R.id.container_main,searchFragment).commit();
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        getActionBar();

        String country=loadPreferences("country");
        String language = loadPreferences("language");
        Log.e("country selected is",country);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //DataService.getInstance().clear();
        android.app.FragmentManager fm= getFragmentManager();


        MainFragment mainFragment=(MainFragment)fm.findFragmentById(R.id.container_main);
        if (mainFragment==null){
            mainFragment=MainFragment.newInstance(country,language);
            fm.beginTransaction().add(R.id.container_main,mainFragment).commit();
        }
        //show items
        if(!HomeActivity.getHomeActivity().loadPreferences("name").isEmpty()) {
            HomeActivity.getHomeActivity().navigationView.getMenu().findItem(R.id.nav_sign).setVisible(false);
            HomeActivity.getHomeActivity().navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            HomeActivity.getHomeActivity().navigationView.getMenu().findItem(R.id.nav_fav).setVisible(true);
            //HomeActivity.getHomeActivity().navigationView.getMenu().findItem(R.id.nav_control).setVisible(true);
        }
    }
    public JsonObjectRequest get_posts(String url,final int type){
        final DataService d=DataService.getInstance();

        Bundle bundle=getIntent().getExtras();
        String country=bundle.getString("country");
        url=url+country.toLowerCase();

        JsonObjectRequest req2 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray post_array = response.getJSONArray("Posts");
                    for (int i=0;i<post_array.length();i++){
                        Post p=new Post();
                        p.setPost_title(post_array.getJSONObject(i).getString("post_title"));
                        p.setPost_title_ar(post_array.getJSONObject(i).getString("post_title_ar"));
                        p.setImages(post_array.getJSONObject(i).getString("images"));
                        p.setPost_id(post_array.getJSONObject(i).getInt("post_id"));
                        d.add_post(p,type);
                    }
                }
                catch (JSONException j){Log.e("error2",j.getLocalizedMessage());}

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error1",error.toString());
            }
        });
        return req2;
    }

    public JsonObjectRequest get_parts(String url){
        final DataService dataService=DataService.getInstance();

        Bundle bundle=getIntent().getExtras();
        String country=bundle.getString("country");
        url=url+country.toLowerCase();

        JsonObjectRequest req2 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray part_array = response.getJSONArray("allParts");
                    Log.e("array is",part_array.toString());
                    for (int i=0;i<part_array.length();i++){
                        Part p=new Part();

                        p.setName_ar(part_array.getJSONObject(i).getString("name_ar"));
                        p.setName_en(part_array.getJSONObject(i).getString("name_en"));
                        p.setBelong(part_array.getJSONObject(i).getInt("belong"));
                        p.setPart_level(part_array.getJSONObject(i).getInt("part_level"));
                        p.setIcon(part_array.getJSONObject(i).getString("icon"));
                        dataService.add_part(p);
                    }
                }
                catch (JSONException j){Log.e("error2",j.getLocalizedMessage());}

                //create fragment
                android.app.FragmentManager fm= getFragmentManager();
                MainFragment mainFragment=(MainFragment)fm.findFragmentById(R.id.container_main);
                if (mainFragment==null){
                    mainFragment=MainFragment.newInstance("blah","kah");
                    fm.beginTransaction().add(R.id.container_main,mainFragment).commit();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error1",error.toString());
            }
        });
        return req2;
    }
    public void loadPostDetails(Post p){
        PostDetails postDetails=new PostDetails().newInstance(p.getPost_id());
        getFragmentManager().
                beginTransaction().
                replace(R.id.container_main,postDetails).
                addToBackStack(null).
                commit();

    }

    public void loadHome(){
        //move to main fragmentString country = loadPreferences("country");
        String Language = loadPreferences("language");
        String country = loadPreferences("country");

        MainFragment mainFragment =new MainFragment().newInstance(country,Language);
        getFragmentManager().beginTransaction().replace(R.id.container_main,mainFragment).addToBackStack(null).commit();
    }

    public void loadPostsList(Part p){
        PostsListFragment postsListFragment = new PostsListFragment().newInstance(p.getId(),"");
        getFragmentManager().beginTransaction().replace(R.id.container_main,postsListFragment).commit();
    }
    public void load_subparts(Part p){
        SubpartsFragment subpartsFragment=new SubpartsFragment().newInstance(p.getId());
        getFragmentManager().
                beginTransaction().
                replace(R.id.container_main,subpartsFragment).
                addToBackStack(null).
                commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        //getMenuInflater().inflate(R.menu.main2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.d("menu click","search");
        //noinspection SimplifiableIfStatement
        if (id == R.id.search_menu) {
            Log.e("search","click");
            return false;
        }

        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home) {
            loadHome();

        } else if (id == R.id.nav_login) {

            LoginFragment loginFragment =new LoginFragment().newInstance("","");
            getSupportFragmentManager().beginTransaction().replace(R.id.container_main, loginFragment).addToBackStack(null).commit();

        } else if (id == R.id.nav_sign) {
            SignFragment signFragment =new SignFragment().newInstance("","");
            getFragmentManager().beginTransaction().replace(R.id.container_main,signFragment).addToBackStack(null).commit();

        } else if (id == R.id.nav_ads) {
            UserAdsFragment adsFragment = new UserAdsFragment().newInstance("","");
            getFragmentManager().beginTransaction().replace(R.id.container_main,adsFragment).addToBackStack(null).commit();
        }
        else if(id == R.id.nav_fav){
            FavFragment favFragment = new FavFragment().newInstance("","");
            getFragmentManager().beginTransaction().replace(R.id.container_main,favFragment).addToBackStack(null).commit();
        }
        else if(id == R.id.nav_settings){
            SettingsFragment favFragment = new SettingsFragment().newInstance("","");
            getFragmentManager().beginTransaction().replace(R.id.container_main,favFragment).addToBackStack(null).commit();
        }
        else if(id == R.id.nav_paypel){
            PaypalFragment paypalFragment = new PaypalFragment();
            getFragmentManager().beginTransaction().replace(R.id.container_main,paypalFragment).addToBackStack(null).commit();
        }
        else if(id == R.id.nav_mail){

            MailFragment mailFragment = new MailFragment();
            getFragmentManager().beginTransaction().replace(R.id.container_main,mailFragment).addToBackStack(null).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void savePreferences(String key, String value){
        SharedPreferences sharedPreferences = getSharedPreferences("weightSetting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public String loadPreferences(String key){
        SharedPreferences sharedPreferences = getSharedPreferences("weightSetting", MODE_PRIVATE);
        String load = sharedPreferences.getString(key, "");
        return load;

    }


    protected void displayResultText(String result) {
        //((TextView) findViewById(R.id.txtResult)).setText("Result : " + result);
        Toast.makeText(
                getApplicationContext(),
                result, Toast.LENGTH_LONG)
                .show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
                        displayResultText("PaymentConfirmation info received from PayPal");


                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
}
