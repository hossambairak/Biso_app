package com.bisostore.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    TextView email_view;
    TextView password_view;
    private LoginButton loginButton;
    GoogleApiClient mGoogleApiClient;
    int RC_SIGN_IN;
    CallbackManager callbackManager;
    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //for facebook init
        callbackManager = CallbackManager.Factory.create();
        //for google init
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage((FragmentActivity) getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.e("connection","failed");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            String email = result.getSignInAccount().getEmail();
            String name = result.getSignInAccount().getDisplayName();
            Log.d("email google is",email);
            Log.d("name google is",result.getSignInAccount().getDisplayName());
            check_user_google(email,name);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getContext());

        View v=inflater.inflate(R.layout.fragment_login, container, false);
        // Inflate the layout for this fragment
        Button login =(Button)v.findViewById(R.id.login);
        //login via google
        SignInButton sign_google = (SignInButton) v.findViewById(R.id.sign_in_button);
        sign_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click","sign google");
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }

        });

        //login facebook button init
        loginButton = (LoginButton) v.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
        loginButton.setFragment(this);
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("FacebookFragment", "Success!");
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    String email = object.getString("email");
                                    String name = object.getString("name"); // 01/31/1980 format
                                    String id = object.getString("id");
                                    String gender = object.getString("gender");
                                    Log.d("fb id:",id);
                                    Log.d("email is",email);
                                    Log.d("name is",name);
                                    Log.d("gender is ",gender);
                                    check_user_fb(email,name,id,gender);

                                }
                                catch (Exception e){

                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        email_view = (TextView) v.findViewById(R.id.ad_title);
        password_view = (TextView) v.findViewById(R.id.ad_time);

        login.setOnClickListener(this);

        return v;
    }

    public void create_fb_user(final String email,final String name,final String id,final String gender){
        String url = "http://bisostore.com/mobile/add_facebook_user";
        Uri.Builder builder = Uri.parse(url).buildUpon();
        builder.appendQueryParameter("email", email);
        builder.appendQueryParameter("name", name);
        builder.appendQueryParameter("id", id);
        builder.appendQueryParameter("gender", gender);
        String loginUrl=builder.build().toString();
        Log.d("url create fb",url);
        StringRequest req = new StringRequest(Request.Method.GET, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("work",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error","fb");
            }
        });
        Volley.newRequestQueue(getContext()).add(req);
    }

    public void check_user_fb(final String email,final String name,final String id,final String gender){
        String url = "http://bisostore.com/mobile/check_user";
        Uri.Builder builder = Uri.parse(url).buildUpon();
        builder.appendQueryParameter("email", email);
        builder.appendQueryParameter("name", name);
        String loginUrl=builder.build().toString();
        StringRequest req = new StringRequest(Request.Method.GET, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res is:",response);
                if(response.equals("false")) {
                    //create new account
                    Toast.makeText(getContext(), "لا يوجد حساب بنفس الإيميل", Toast.LENGTH_LONG).show();
                    create_fb_user(email,name,id,gender);

                }
                else {
                    //save current email and name if possible and show message
                    Toast.makeText(getContext(),"تم تسجيل الدخول بنجاح",Toast.LENGTH_LONG).show();

                    HomeActivity.getHomeActivity().savePreferences("email",email);
                    HomeActivity.getHomeActivity().savePreferences("name",name);
                    HomeActivity.getHomeActivity().savePreferences("id",response);
                    Log.d("u_id: ",response);


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue r = Volley.newRequestQueue(getContext());
        r.getCache().clear();
        r.add(req);
    }

    //create google user
    public void create_google_user(final String email,final String name){
        String url = "http://bisostore.com/mobile/add_google_user";
        Uri.Builder builder = Uri.parse(url).buildUpon();
        builder.appendQueryParameter("email", email);
        builder.appendQueryParameter("name", name);
        String loginUrl=builder.build().toString();
        Log.d("url create fb",url);
        StringRequest req = new StringRequest(Request.Method.GET, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("work",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error","fb");
            }
        });
        Volley.newRequestQueue(getContext()).add(req);
    }

    public void check_user_google(final String email,final String name){
        String url = "http://bisostore.com/mobile/check_user";
        Uri.Builder builder = Uri.parse(url).buildUpon();
        builder.appendQueryParameter("email", email);
        builder.appendQueryParameter("name", name);
        String loginUrl=builder.build().toString();
        StringRequest req = new StringRequest(Request.Method.GET, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("false")) {
                    //create new account
                    create_google_user(email,name);

                    Toast.makeText(getContext(), "لا يوجد حساب بنفس الإيميل", Toast.LENGTH_LONG).show();
                }
                else {
                    //save current email and name if possible and show message
                    Toast.makeText(getContext(),"تم تسجيل الدخول بنجاح",Toast.LENGTH_LONG).show();

                    HomeActivity.getHomeActivity().savePreferences("email",email);
                    HomeActivity.getHomeActivity().savePreferences("name",name);
                    HomeActivity.getHomeActivity().savePreferences("id",response);
                    show_items();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(getContext()).add(req);

    }

    public void check_details(){
        String url = "http://bisostore.com/mobile/login";
        final String email = email_view.getText().toString();
        final String password = password_view.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response is",response);
                if(response.equals("false")) {
                    Toast.makeText(getContext(),"فشل تسجيل الدخول يرجى التحقق من البريد الإلكتروني وكلمة المرور",Toast.LENGTH_LONG).show();
                }
                else{

                    Toast.makeText(getContext(),"تم تسجيل الدخول بنجاح",Toast.LENGTH_LONG).show();

                    HomeActivity.getHomeActivity().savePreferences("email",email);
                    String id = response.split("-")[0];
                    String name = response.split("-")[1];
                    HomeActivity.getHomeActivity().savePreferences("id",id);
                    HomeActivity.getHomeActivity().savePreferences("name",name);
                    show_items();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("email post",email);
                Log.d("password post",password);
                Map<String,String> map = new HashMap<String, String>();
                map.put("email",email);
                map.put("password",password);

                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        Volley.newRequestQueue(getContext()).add(request);
    }

    @Override
    public void onClick(View view) {
       check_details();

    }

    public void show_items(){
        HomeActivity.getHomeActivity().navigationView.getMenu().findItem(R.id.nav_sign).setVisible(false);
        HomeActivity.getHomeActivity().navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
        HomeActivity.getHomeActivity().navigationView.getMenu().findItem(R.id.nav_fav).setVisible(true);
        HomeActivity.getHomeActivity().navigationView.getMenu().setGroupVisible(R.id.nav_control,true);
        HomeActivity.getHomeActivity().loadHome();
    }


}

