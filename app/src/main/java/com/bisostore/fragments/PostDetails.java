package com.bisostore.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.provider.Telephony;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bisostore.adapters.ImageAdapter;
import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.MainActivity;
import com.bisostore.biso.R;
import com.bisostore.main.Comment;
import com.bisostore.main.Post;
import com.bisostore.services.DataService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**posts
 */
public class PostDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String POST_ID = "post_id";

    // TODO: Rename and change types of parameters
    private int post_id;
    private Post post=new Post();
    ArrayList<Comment> comments = new ArrayList<>();
    private TextView postTitleView;
    private TextView postPriceView;
    private TextView postDescrptionView;
    private TextView postPhoneView;
    private TextView postRemainView;
    private TextView postDate;
    private TextView postViews;
    private TextView postId;
    private ImageButton fav;
    private Button add_comment;
    private ImageButton share;
    private ImageButton share_tw;
    private ImageButton share_go;
    private ImageButton share_wh;
    private ImageButton share_me;
    private EditText comment_text;

    private static ViewPager mPager;
    private static int currentPage = 0;
    ArrayList<String> uri=new ArrayList<>();
    private ArrayList<Bitmap> arr=new ArrayList<>();

    ImageAdapter im;
    public PostDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PostDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static PostDetails newInstance(int post_id) {
        PostDetails fragment = new PostDetails();
        Bundle args = new Bundle();
        args.putInt(POST_ID, post_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            post_id = getArguments().getInt(POST_ID);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        im=new ImageAdapter(HomeActivity.context,arr);
        final View v=inflater.inflate(R.layout.fragment_post_details, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.getMenu().getItem(1).setVisible(true);

        toolbar.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Log.d("report","click");
                show_dialog();
                return false;
            }
        });
        setHasOptionsMenu(true);
        fav = (ImageButton) v.findViewById(R.id.fav);

        //check if user is logged in
        Log.d("email is",HomeActivity.getHomeActivity().loadPreferences("email"));
        if(HomeActivity.getHomeActivity().loadPreferences("email").isEmpty())
        {
            fav.setVisibility(View.INVISIBLE);
        }
        else{ //check if added to fav
            check_fav();
        }
        add_comment = (Button) v.findViewById(R.id.add_comment);
        comment_text = (EditText) v.findViewById(R.id.comment_text);
        share = (ImageButton) v.findViewById(R.id.share_fb);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setPackage("com.facebook.katana");
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, post.getPost_title_ar());
                i.putExtra(Intent.EXTRA_TEXT, "http://www.bisostore.com/"+post.getPost_id());
                startActivity(Intent.createChooser(i, "Share URL"));
            }
        });
        share_tw = (ImageButton) v.findViewById(R.id.share_tw);
        share_tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setPackage("com.twitter.android");
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, post.getPost_title_ar());
                i.putExtra(Intent.EXTRA_TEXT, "http://www.bisostore.com/"+post.getPost_id());
                startActivity(Intent.createChooser(i, "Share URL"));
            }
        });
        share_go = (ImageButton) v.findViewById(R.id.share_go);
        share_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setPackage("com.google.android.apps.plus");
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, post.getPost_title_ar());
                i.putExtra(Intent.EXTRA_TEXT, "http://www.bisostore.com/"+post.getPost_id());
                startActivity(Intent.createChooser(i, "Share URL"));
            }
        });
        share_wh = (ImageButton) v.findViewById(R.id.share_wh);
        share_wh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setPackage("com.whatsapp");
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, post.getPost_title_ar());
                i.putExtra(Intent.EXTRA_TEXT, "http://www.bisostore.com/"+post.getPost_id());
                startActivity(Intent.createChooser(i, "Share URL"));
            }
        });
        share_me = (ImageButton) v.findViewById(R.id.share_me);
        share_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                String p = Telephony.Sms.getDefaultSmsPackage(getContext());
                Log.d("package name",p);
                i.setPackage(p);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, post.getPost_title_ar());
                i.putExtra(Intent.EXTRA_TEXT, "http://www.bisostore.com/"+post.getPost_id());
                startActivity(Intent.createChooser(i, "Share URL"));
            }
        });
        get_comments(v);

        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("add comment",comment_text.getText().toString());
                add_comment();

            }
        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("click button","fav");
                fav();

            }
        });

        String url=Post.WEBSITE_URL+Post.POST_PATH+post_id;
        Log.d("post url",url);
        JsonObjectRequest object = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject post_object = response.getJSONObject("post").getJSONObject("0");
                    Log.e("object is",post_object.toString());
                    post.setPost_id(post_object.getInt("post_id"));
                    Log.d("visit",String.valueOf(post_object.getInt("post_visits")));
                    post.setPost_visits(post_object.getInt("post_visits"));
                    Log.d("post date",post_object.getString("post_date"));
                    post.setPost_date(post_object.getString("post_date"));
                    post.setAuthor_id(post_object.getInt("author_id"));
                    post.setPost_title_ar(post_object.getString("post_title_ar"));
                    post.setCountry(post_object.getString("country"));
                    post.setImages(post_object.getString("images"));
                    post.setPrice(post_object.getInt("price"));
                    post.setPhone(post_object.getString("phone"));
                    post.setDescrption(post_object.getString("post_body_ar"));
                    post.setRemain(post_object.getInt("remain"));
                    post.setPeriod(post_object.getInt("period"));
                    Log.d("period",String.valueOf(post.getPeriod()));
                    ArrayList<String> inputs_name=new ArrayList<>();
                    ArrayList<String> input_value = new ArrayList<>();
                    Log.d("input is",response.getJSONObject("extraInput").toString()) ;
                    for (int i=0;i<response.getJSONObject("extraInput").length();i++){
                        JSONObject temp = response.getJSONObject("extraInput").getJSONObject(String.valueOf(i));

                        if(HomeActivity.getHomeActivity().loadPreferences("language").equals("ar")) {
                            input_value.add(i, temp.getString("text_ar"));
                            inputs_name.add(i,temp.getString("inputname_ar"));
                        }
                        else {
                            input_value.add(i, temp.getString("text_en"));
                            inputs_name.add(i,temp.getString("inputname_en"));
                        }
                    }
                    post.setInputs_name(inputs_name);
                    post.setInputs_value(input_value);

                }
                catch (JSONException e){
                    Log.e("error:",e.getLocalizedMessage());
                }
                update_ui();
                init(getView());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("error:",error.getLocalizedMessage());
            }
        });
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(object);

        android.app.FragmentManager fm=getActivity().getFragmentManager();

       PostsFragment postsFragment;
        postsFragment=PostsFragment.newInstance(PostsFragment.POST_TYPE_RELATED,"eg","ar",post_id);

        fm.beginTransaction().add(R.id.container_new_row,postsFragment).commit();
        //DataService.getInstance().clear();
        return v;

    }

    public void show_dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("report");

        LinearLayout lm= new LinearLayout(getContext());

        lm.setOrientation(LinearLayout.VERTICAL);

        // Set up the input
        final EditText reason_edit = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        reason_edit.setInputType(InputType.TYPE_CLASS_TEXT );

        // Set up the input
        final EditText email_edit = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        email_edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);

        final TextView reason_tv = new TextView(getContext());
        reason_tv.setText("ادخل سبب التبليغ هنا");

        final TextView email_tv = new TextView(getContext());
        email_tv.setText("ادخل البريد الإلكتروني للتواصل");

        lm.addView(reason_tv);
        lm.addView(reason_edit);
        lm.addView(email_tv);
        lm.addView(email_edit);

        builder.setView(lm);


        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reason = reason_edit.getText().toString();
                String email = email_edit.getText().toString();
                Log.d("text is",reason);
                Log.d("email is",email);
                String url = "http://bisostore.com/mobile/report";
                int post_id = post.getPost_id();
                String subject = post.getPost_title_ar();
                Uri.Builder builder = Uri.parse(url).buildUpon();
                builder.appendQueryParameter("reportemail", email);
                builder.appendQueryParameter("report",reason);
                if(!HomeActivity.getHomeActivity().loadPreferences("name").isEmpty())
                    builder.appendQueryParameter("user_name", HomeActivity.getHomeActivity().loadPreferences("name"));
                builder.appendQueryParameter("subject", post.getPost_title_ar());
                builder.appendQueryParameter("post_id",String.valueOf(post_id));
                String reportUrl=builder.build().toString();
                Log.d("report url",reportUrl);
                StringRequest req=new StringRequest(Request.Method.GET, reportUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ddd",response.toString());
                        Log.d("ddd1","22");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                Volley.newRequestQueue(getContext()).add(req);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    //add or remove add to favorites
    public void fav(){
        String url = "http://bisostore.com/mobile/fav/3/" + HomeActivity.getHomeActivity().loadPreferences("id") + "/" + String.valueOf(post_id);
        Log.d("fav url is:",url);
        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("work",response);
                PostDetails postDetails=new PostDetails().newInstance(post_id);
                getFragmentManager().
                        beginTransaction().
                        replace(R.id.container_main,postDetails).
                        addToBackStack(null).
                        commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getContext()).add(req);

    }

    // check if post added to user favorites
    public void check_fav(){

        Log.d("echk fav","c");
        String user_id = HomeActivity.getHomeActivity().loadPreferences("id");//should replace
        String url = "http://bisostore.com/mobile/check_fav/"+user_id+"/"+post_id;
        Log.d("check fav url",url);
        StringRequest req= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("yes")){
                   //post added
                    fav.setBackgroundResource(R.drawable.account);
                }
                else{
                    //post not added do nothing
                    Log.d("no", "for check");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getContext()).add(req);

    }

    public void add_comment(){
        Log.d("start com","android and server");
        String url = "http://bisostore.com/mobile/add_comment";
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // refresh fragment
                Log.d("work",response);
                PostDetails postDetails=new PostDetails().newInstance(post_id);
                getFragmentManager().
                        beginTransaction().
                        replace(R.id.container_main,postDetails).
                        addToBackStack(null).
                        commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("comment", comment_text.getText().toString());
                map.put("user_id",HomeActivity.getHomeActivity().loadPreferences("id"));
                map.put("post_id",String.valueOf(post_id));

                return map;
            }
        };
        Volley.newRequestQueue(getContext()).add(req);
    }

    public void get_comments(final View v){
        String url ="http://bisostore.com/mobile/get_comments/"+String.valueOf(post_id);
        JsonObjectRequest req=  new JsonObjectRequest(JsonObjectRequest.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("get comments","work");
                try {
                    JSONArray comment_array = response.getJSONArray("comments");
                    for (int i=0;i<comment_array.length();i++){
                        String comment_content = comment_array.getJSONObject(i).getString("comment_content");
                        String comment_date = comment_array.getJSONObject(i).getString("comment_date");
                        String u_name = comment_array.getJSONObject(i).getString("u_name");
                        Comment comment = new Comment(u_name,comment_content,comment_date);
                        comments.add(comment);
                    }
                }
                catch (JSONException j){Log.e("error2",j.getLocalizedMessage());}
                update_comments(v);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getContext()).add(req);
    }

    public void update_comments(View v){

        LinearLayout lm = new LinearLayout(getContext());
        lm.setOrientation(LinearLayout.VERTICAL);
        if(HomeActivity.getHomeActivity().loadPreferences("language").equals("ar"))
            lm.setTextDirection(View.TEXT_DIRECTION_RTL);
        else
            lm.setTextDirection(View.TEXT_DIRECTION_LTR);

        LinearLayout parent = (LinearLayout) getView().findViewById(R.id.comments);
        for(int i=0;i<comments.size();i++){

            TextView tv = new TextView(getContext());
            tv.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT,1f));

            tv.setPadding(20,10,20,10);
            tv.setText(comments.get(i).getComment_content());

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.leftMargin = 20; params.rightMargin = 20;
            lm.setLayoutParams(params);
            lm.addView(tv);


        }
        parent.addView(lm);
    }

    public void delete_post(){
        String url = "http://bisostore.com/mobile/delete_ads";

        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equals("fail")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("فشلت عملية الحذف!");
                    AlertDialog alert = builder.create();
                    alert.show();
                    HomeActivity.getHomeActivity().loadHome();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("تمت عملية الحذف بنجاح");
                    AlertDialog alert = builder.create();
                    alert.show();
                    HomeActivity.getHomeActivity().loadHome();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
        @Override
        protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();
            params.put("post_id",String.valueOf(post.getPost_id()));
            params.put("user_id",HomeActivity.getHomeActivity().loadPreferences("id"));

            return params;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String,String> params = new HashMap<String, String>();
            params.put("Content-Type","application/x-www-form-urlencoded");
            return params;
        }
    };
        Volley.newRequestQueue(getContext()).add(req);

    }

    public void edit_post(){
        EditAdFragment fragment = new EditAdFragment().newInstance(post);
        getFragmentManager().beginTransaction().replace(R.id.container_main,fragment).commit();
    }
    public void update_ui(){

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if(HomeActivity.getHomeActivity().loadPreferences("id")!="") {
            if (post.getAuthor_id() == Integer.valueOf(HomeActivity.getHomeActivity().loadPreferences("id"))) {
                toolbar.getMenu().getItem(2).setVisible(true);

                toolbar.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("هل أنت متأكد من أنك تود حذف الإعلان!")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                        delete_post();
                                        Log.d("delete", "click");
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        return false;
                    }
                });
                toolbar.getMenu().getItem(3).setVisible(true);

                toolbar.getMenu().getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("هل أنت متأكد من أنك تود تعديل الإعلان!")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                        //delete_post();
                                        edit_post();
                                        Log.d("edit", "click");
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        return false;
                    }
                });


                toolbar.getMenu().getItem(4).setVisible(true);

                toolbar.getMenu().getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        //move to new fragment
                        return false;
                    }
                });
            }
        }

        postId = (TextView) getView().findViewById(R.id.ad_id);
        postDate = (TextView) getView().findViewById(R.id.ad_date);
        postViews = (TextView) getView().findViewById(R.id.ad_views);
        postTitleView=(TextView)getView().findViewById(R.id.ad_title);
        postDescrptionView = (TextView) getView().findViewById(R.id.ad_description);
        postPriceView = (TextView) getView().findViewById(R.id.ad_price);
        postPhoneView=(TextView) getView().findViewById(R.id.ad_phone);
        postRemainView = (TextView) getView().findViewById(R.id.ad_remain);

        if(HomeActivity.getHomeActivity().loadPreferences("language").equals("ar")) {
            postTitleView.setText(post.getPost_title_ar());
            toolbar.setTitle(post.getPost_title_ar());
        }
        else{
            postTitleView.setText(post.getPost_title_en());
            toolbar.setTitle(post.getPost_title_en());
        }


        postPhoneView.setText(post.getPhone());
        postViews.setText(String.valueOf(post.getPost_visits()));
        postDate.setText(String.valueOf(post.getPost_date()));
        postId.setText(String.valueOf(post.getPost_id()));
        Log.e("remain",String.valueOf(post.getRemain()));
        if(post.getPrice()!=0)
            postPriceView.setText(String.valueOf(post.getPrice()));


        postRemainView.setText(String.valueOf(post.getRemain()));
        postDescrptionView.setText(post.getDescrption());

        //add extra inputs
        LinearLayout linearLayout = new LinearLayout(getContext());

        linearLayout.setOrientation(linearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView tv = new TextView(getContext());
        tv.setText(getResources().getText(R.string.special_details));
        tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        tv.setPadding(55,55,55,55);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT
        );
        params.setMargins(35,35,35,35);
        tv.setLayoutParams(params);
        tv.setTextSize(18);
        tv.setBackgroundColor(getResources().getColor(R.color.purple));
        tv.setTextColor(getResources().getColor(R.color.white));
        linearLayout.addView(tv);

        LinearLayout l= new LinearLayout(getContext());
        ViewGroup.MarginLayoutParams params2 = new ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT
        );
        params2.setMargins(35,35,35,35);
        l.setPadding(35,35,35,35);
        l.setLayoutParams(params2);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setBackgroundColor(getResources().getColor(R.color.white));
        if(post.getInputs_name()!=null)
        for (int i=0;i<post.getInputs_name().size();i++){

            //create dynamic linear layout for extra inputs
            LinearLayout lm = new LinearLayout(getContext());
            lm.setOrientation(LinearLayout.HORIZONTAL);

            TextView tv2= new TextView(getContext());
            TextView tv1=new TextView(getContext());

            tv1.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT,1f));
            tv2.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT,1f));

            tv1.setPadding(10,10,10,10);
            tv2.setPadding(10,10,10,10);
            tv1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            tv2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            tv1.setTextSize(15);
            tv2.setTextSize(15);

            if(HomeActivity.getHomeActivity().loadPreferences("language").equals("ar")){
                lm.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            tv1.setText(post.getInputs_name().get(i));
            tv2.setText(post.getInputs_value().get(i));

            lm.addView(tv1);
            lm.addView(tv2);

            l.addView(lm);

        }
        linearLayout.addView(l);
        LinearLayout parent = (LinearLayout) getView().findViewById(R.id.post_layout);
        parent.addView(linearLayout,5);
        //parent.addView(linearLayout);


        final String images=post.getImages();
        for (int i=0;i<images.split(",").length;i++){
            final int j=i;
            Log.e("image uri is",String.valueOf(i));
            if(images.isEmpty()) {
                uri.add("http://bisostore.com/files/images/1455985567.png");
            }
            else if(images.split(",").length==1)
                uri.add("http://bisostore.com/files/uploads/"+images);
            else
                uri.add("http://bisostore.com/files/uploads/"+images.split(",")[i]);

            ImageRequest imageRequest=new ImageRequest(uri.get(i), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //thumbnail.setImageBitmap(response);
                    arr.add(response);
                    im.notifyDataSetChanged();
                    if(j==images.split(",").length-1){

                        Log.e("notify","change");
                    }
                }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            Volley.newRequestQueue(HomeActivity.getHomeActivity().getApplicationContext()).add(imageRequest);
            init(getView());
        }

    }

    private void init(View v) {

        mPager = (ViewPager) v.findViewById(R.id.pager);
        mPager.setAdapter(im);

        CircleIndicator indicator = (CircleIndicator) v.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == arr.size()) {
                    currentPage = 0;
                }
                //im.notifyDataSetChanged();
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }

}
