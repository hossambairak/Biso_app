package com.bisostore.biso;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bisostore.main.Post;

import org.json.JSONException;
import org.json.JSONObject;

public class PostActivity extends AppCompatActivity {

    final String WEBSITE_URL="http://bisostore.com/mobile/";
    final String GET_POST = "get_post/";
    Post p=new Post();
    private TextView title_ar;
    private TextView country;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private ViewFlipper mViewFlipper;
    private Context mContext;
   // private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        title_ar = (TextView)findViewById(R.id.title_ar);
        country = (TextView)findViewById(R.id.country);
        String url = WEBSITE_URL+GET_POST+"32000";
        JsonObjectRequest object = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject post_object = response.getJSONObject("post").getJSONObject("0");
                    Log.e("object is",post_object.toString());
                    p.setPost_title_ar(post_object.getString("post_title_ar"));
                    p.setCountry(post_object.getString("country"));
                }
                catch (JSONException e){
                    Log.e("error:",e.getLocalizedMessage());
                }
                update_ui();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("error:",error.getLocalizedMessage());
            }
        });
        Volley.newRequestQueue(this).add(object);

    }

    public void update_ui(){
        country.setText(p.getCountry());
        title_ar.setText(p.getPost_title_ar());
    }

   /* class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));
                    mViewFlipper.showNext();
                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,R.anim.right_out));
                    mViewFlipper.showPrevious();
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }*/
}
