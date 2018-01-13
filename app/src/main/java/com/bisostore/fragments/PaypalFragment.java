package com.bisostore.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.R;
import com.bisostore.main.Part;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ShippingAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PaypalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CONFIG_ENVIRONMENT =PayPalConfiguration.ENVIRONMENT_SANDBOX ;//Enter the correct environment here;
    private static final String CONFIG_CLIENT_ID = "AbRXvAzg7eOjPIA0FRqf2PeYzUGwbXgyPUybBlCo22We5CJGLChFtAWXrmLtFgv9X_kjleryyn7j6mzb";//you need to register with PayPal and enter your client_ID here;

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            .acceptCreditCards(true)
            .languageOrLocale("EN")
            .rememberUser(true)
            .merchantName("Company name");

    EditText amount;
    Button pay;
    final String access_token="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_paypal, container, false);

        amount = (EditText) rootView.findViewById(R.id.amount);
        pay = (Button) rootView.findViewById(R.id.button1);

        pay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onPayPressed(v);
            }

        });

        initPaymentService();

        return rootView;
    }

    public void initPaymentService() {
        try {
            Intent intent = new Intent(getActivity(), PayPalService.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

            getActivity().startService(intent);
        } catch (Exception e) {
            Log.i("PayPal Exception", e.getMessage());
        }
    }

    public void onPayPressed(View pressed) {

        try{
                PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(amount.getText().toString()), "USD", "Bisostore Payment", PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                startActivityForResult(intent, REQUEST_CODE_PAYMENT);
        }catch (NumberFormatException e){
            Toast.makeText(getActivity(), "amount value cannot be empty. \n Please enter a amount .", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {

                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);


                if (confirm != null) {
                    try {

                        Log.i("paymentExample", confirm.toJSONObject().toString());
                        Log.e("pay id",confirm.toJSONObject().getJSONObject("response").getString("id"));
                        final String id = confirm.toJSONObject().getJSONObject("response").getString("id");

                        JsonObjectRequest req2 = new JsonObjectRequest(Request.Method.GET, "http://bisostore.com/mobile/get_access_token", null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String access_token = response.getString("access_token");
                                    Log.d("access token",access_token);
                                    JsonObjectRequest req3 = new JsonObjectRequest(Request.Method.GET, "http://bisostore.com/mobile/get_paypal_info/"+access_token+"/"+id, null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                //String access_token = response.getJSONObject("").getString("access_token");
                                                JSONObject payer_info = response.getJSONObject("payer").getJSONObject("payer_info");
                                                String first_name = payer_info.getString("first_name");
                                                String last_name = payer_info.getString("last_name");
                                                String email = payer_info.getString("email");
                                                Log.d("email",email);
                                                Log.d("first name",first_name);
                                                Log.d("last name",last_name);
                                                String amount = response.getJSONArray("transactions").getJSONObject(0).getJSONObject("amount").getString("total");
                                                Log.d("amount",amount);
                                                String date = response.getJSONArray("transactions").getJSONObject(0).getJSONArray("related_resources").getJSONObject(0).getJSONObject("sale").getString("create_time");

                                                date = date.replace("T"," ");
                                                date = date.replace("Z"," ");

                                                Log.d("date",date);
                                                Log.e("info:",response.toString());
                                                add_payment(amount,email,first_name,last_name,date);
                                            }
                                            catch (Exception j){Log.e("error2",j.getLocalizedMessage());}

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("error1121",error.toString());
                                        }
                                    });
                                    Volley.newRequestQueue(getContext()).add(req3);
                                    req3.setRetryPolicy(new RetryPolicy() {
                                        @Override
                                        public int getCurrentTimeout() {
                                            return 50000;
                                        }

                                        @Override
                                        public int getCurrentRetryCount() {
                                            return 50000;
                                        }

                                        @Override
                                        public void retry(VolleyError error) throws VolleyError {

                                        }
                                    });
                                }
                                catch (JSONException j){Log.e("error21212",j.getLocalizedMessage());}

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("error12112",error.toString());
                            }
                        });


                        Volley.newRequestQueue(getContext()).add(req2);
                        req2.setRetryPolicy(new RetryPolicy() {
                            @Override
                            public int getCurrentTimeout() {
                                return 50000;
                            }

                            @Override
                            public int getCurrentRetryCount() {
                                return 50000;
                            }

                            @Override
                            public void retry(VolleyError error) throws VolleyError {

                            }
                        });

                        Toast.makeText(getActivity().getApplicationContext(), "PaymentConfirmation info received from PayPal",
                                Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment was submitted. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

    }

    @Override
    public void onDestroy() {
        // Stop service when done
        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }

    public void add_payment(final String amount,final String email,final String first_name,final String last_name,final String date){

        String url = "http://bisostore.com/mobile/add_funds";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response is",response);
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
                map.put("amount",amount);
                map.put("first_name",first_name);
                map.put("last_name",last_name);
                map.put("date",date);
                map.put("user_id", HomeActivity.getHomeActivity().loadPreferences("id"));

                Log.d("email",email);
                Log.d("date",date);
                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        Volley.newRequestQueue(getContext()).add(request);
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

    }
}
