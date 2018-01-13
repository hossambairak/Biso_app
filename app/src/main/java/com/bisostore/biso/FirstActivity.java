package com.bisostore.biso;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bisostore.fragments.CountriesFragment;
import com.bisostore.fragments.MainFragment;
import com.bisostore.main.Country;
import com.bisostore.main.Post;
import com.bisostore.services.DataService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class FirstActivity extends AppCompatActivity {

    final String COUNTRY_TAG="country";
    final String LANGUAGE_TAG="language";

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    static public FirstActivity getFirstActivity(){
        return firstActivity;
    }
    private void setFirstActivity(FirstActivity firstActivity) {
        this.firstActivity = firstActivity;
    }

    static private FirstActivity firstActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if we select country
        String country=loadPreferences(COUNTRY_TAG);
        String language=loadPreferences(LANGUAGE_TAG);

        //delete temp
        //SharedPreferences sharedPreferences = getSharedPreferences("weightSetting", MODE_PRIVATE);
        //sharedPreferences.edit().clear().commit();

        if(country!="")
        {


            String languageToLoad;
            //Change language
            if(loadPreferences("language").contentEquals("ar"))
                languageToLoad  = "ar"; // your language
            else
                languageToLoad  = "en"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());


            Intent intent = new Intent(this,HomeActivity.class);
            intent.putExtra(COUNTRY_TAG,country);
            intent.putExtra(LANGUAGE_TAG,language);
            startActivity(intent);
            finish();
        }
        else {
            setContentView(R.layout.activity_first);
            setFirstActivity(this);

            Log.d("get country","start");

            final DataService d = DataService.getInstance();
            String url = Country.WEBSITE_URL + Country.countryies_PATH;
            JsonObjectRequest req2 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray country_array = response.getJSONArray("countries");
                        for (int i = 0; i < country_array.length(); i++) {
                            JSONObject jsonObject = country_array.getJSONObject(i);
                            String name_ar = jsonObject.getString("name_ar");
                            String name_en = jsonObject.getString("name_en");
                            String id = jsonObject.getString("id");
                            int countryid = jsonObject.getInt("countryid");
                            int shown = jsonObject.getInt("shown");
                            Log.d("country",name_ar);
                            Country country = new Country(countryid, id, name_ar, name_en, shown);
                            d.add_country(country);
                        }
                    } catch (JSONException j) {
                        Log.e("error2", j.getLocalizedMessage());
                    }
                    //create fragment
                    FragmentManager fm = getSupportFragmentManager();
                    CountriesFragment countriesFragment = (CountriesFragment) fm.findFragmentById(R.id.container_first);
                    if (countriesFragment == null) {
                        countriesFragment = CountriesFragment.newInstance("blah", "kah");
                        fm.beginTransaction().add(R.id.container_first, countriesFragment).commit();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error1", error.toString());
                }
            });
            Volley.newRequestQueue(this).add(req2);
        }
    }

    //save
    public void save_prefrences(Country country){

        radioGroup=(RadioGroup)findViewById(R.id.language_group);
        int id=radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton=(RadioButton)findViewById(id);
        radioButton.getText();
        savePreferences(COUNTRY_TAG,country.getId());

                //Change language
        String languageToLoad;
        if(radioButton.getText().toString().contentEquals("العربية"))
            languageToLoad  = "ar"; // your language
        else
            languageToLoad  = "en"; // your language
        savePreferences(LANGUAGE_TAG,languageToLoad);
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        //Start Home activity
        Intent intent=new Intent(this,HomeActivity.class);
        intent.putExtra(COUNTRY_TAG,country.getId());
        intent.putExtra(LANGUAGE_TAG,radioButton.getText().toString());
        startActivity(intent);
    }

    //save prefrences like name,email,etc..
    private void savePreferences(String key, String value){
        SharedPreferences sharedPreferences = getSharedPreferences("weightSetting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private String loadPreferences(String key){
        SharedPreferences sharedPreferences = getSharedPreferences("weightSetting", MODE_PRIVATE);
        String load = sharedPreferences.getString(key, "");
        return load;

    }

}
