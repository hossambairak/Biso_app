package com.bisostore.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText name_sign;
    EditText email_sign;
    EditText password_sign;
    RadioGroup radioGroup;


    public SignFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignFragment newInstance(String param1, String param2) {
        SignFragment fragment = new SignFragment();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_sign, container, false);
        Button sign_button = (Button) v.findViewById(R.id.sign_button);

        radioGroup=(RadioGroup)v.findViewById(R.id.gender);
        name_sign= (EditText) v.findViewById(R.id.name_sign);
        email_sign = (EditText) v.findViewById(R.id.email_sign);
        password_sign = (EditText) v.findViewById(R.id.password_sign);



        sign_button.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {

        int id=radioGroup.getCheckedRadioButtonId();
        String gender;
        if(id==R.id.male)
            gender="male";
        else
            gender="female";
        add_member(gender);
    }

    private void add_member(final String gender){
        String url = "http://bisostore.com/mobile/sign";
        final String email = email_sign.getText().toString();
        final String name = name_sign.getText().toString();
        final String password = password_sign.getText().toString();

        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.e("res is",response);
                                            try{
                                                JSONObject obj = new JSONObject(response);
                                                if(obj.has("error")){
                                                    if(obj.getString("error").contentEquals("already Reg")){
                                                        Toast.makeText(getContext(),"هذا المستخدم له حساب بالفعل",Toast.LENGTH_LONG).show();
                                                    }
                                                    else{
                                                        JSONArray arr = obj.getJSONArray("error");
                                                        String msg = "يجب عليك كتابة";
                                                        for (int i=0;i<arr.length();i++){
                                                            Log.e("err",arr.getString(i));

                                                            if(arr.getString(i).contentEquals("name"))
                                                                msg = msg + " الاسم ";
                                                            else if(arr.getString(i).contentEquals("email"))
                                                                msg = msg + "- البريد الالكتروني ";
                                                            else if(arr.getString(i).contentEquals("password"))
                                                                msg = msg + "- كلمة المرور ";

                            }
                            Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
                        }
                    }
                    else{

                        Toast.makeText(getContext(),"تم تسجيل الحساب بنجاح",Toast.LENGTH_LONG).show();

                        HomeActivity.getHomeActivity().savePreferences("email",email);
                        HomeActivity.getHomeActivity().savePreferences("password",password);
                        HomeActivity.getHomeActivity().savePreferences("name",name);

                        HomeActivity.getHomeActivity().navigationView.getMenu().findItem(R.id.nav_sign).setVisible(false);
                        HomeActivity.getHomeActivity().navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
                        HomeActivity.getHomeActivity().navigationView.getMenu().findItem(R.id.nav_fav).setVisible(true);
                        HomeActivity.getHomeActivity().navigationView.getMenu().setGroupVisible(R.id.nav_control,true);
                        HomeActivity.getHomeActivity().loadHome();
                    }
                }
                catch (JSONException e){
                    Log.e("JSONException",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<String, String>();
                map.put("email",email);
                map.put("password",password);
                map.put("name",name);
                map.put("sex",gender);
                map.put("country",HomeActivity.getHomeActivity().loadPreferences("country"));

                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };


        Volley.newRequestQueue(getContext()).add(req);
    }
}
