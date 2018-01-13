package com.bisostore.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bisostore.biso.HomeActivity;
import com.bisostore.biso.R;
import com.bisostore.holders.PostListViewHolder;
import com.bisostore.main.Post;

import java.util.ArrayList;

/**
 * Created by HOSSAM on 13/08/2017.
 */

public class PostListAdapter extends RecyclerView.Adapter<PostListViewHolder> {

    ArrayList<Post> posts;
    public PostListAdapter(ArrayList<Post> posts){
        this.posts = posts;
        for (Post p:posts
             ) {
            Log.e("posts3",p.getPost_title_ar());
        }
        Log.e("posts",String.valueOf(posts.size()));
    }

    @Override
    public void onBindViewHolder(PostListViewHolder holder,final int position) {

        final Post p=posts.get(position);
        holder.update_ui(p);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.getHomeActivity().loadPostDetails(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public PostListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_post_list,parent,false);
        return new PostListViewHolder(v);
    }
}
