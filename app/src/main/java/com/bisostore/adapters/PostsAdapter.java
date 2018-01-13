package com.bisostore.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.MainActivity;
import com.bisostore.biso.R;
import com.bisostore.holders.PostViewHolder;
import com.bisostore.main.Part;
import com.bisostore.main.Post;
import com.bisostore.services.DataService;

import java.util.ArrayList;

/**
 * Created by USER on 31/07/2017.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private ArrayList<Post> posts;
    public PostsAdapter(ArrayList<Post> posts){
        Log.e("constuctor","adapter1");
      Log.d("size1",String.valueOf(posts.size()));
        /*for(int i=0;i<posts.size();i++)
            Log.d("title",posts.get(i).getPost_title_ar());*/
        this.posts=posts;
    }


    @Override
    public int getItemCount() {

        Log.d("size",String.valueOf(posts.size()));
        return posts.size();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View postCard= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_post,parent,false);
Log.d("coco","bobo");
        return new PostViewHolder(postCard);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, final int position) {
        Post post=posts.get(position);
        Log.d("soso","koko");
        holder.update_ui(post);
        final int p=position;
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HomeActivity.getHomeActivity().loadPostDetails(posts.get(position));
            }
        });
    }



}
