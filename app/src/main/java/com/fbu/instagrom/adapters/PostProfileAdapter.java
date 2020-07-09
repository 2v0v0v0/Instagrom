package com.fbu.instagrom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fbu.instagrom.R;
import com.fbu.instagrom.models.Post;
import com.parse.ParseFile;

import java.util.List;

public class PostProfileAdapter extends RecyclerView.Adapter<PostProfileAdapter.ViewHolder> {
    private Context context;
    private List<Post> listPosts;

    public PostProfileAdapter (Context context, List<Post> listPosts) {
        this.context = context;
        this.listPosts = listPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_profile_post, parent, false);
        return new PostProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post aPost = listPosts.get(position);
        holder.bind(aPost);
    }

    @Override
    public int getItemCount() {
            return listPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIV = itemView.findViewById(R.id.imageIV);
            //TODO
//            itemView.setOnClickListener();
        }

        public void bind(Post aPost) {
            ParseFile image = aPost.getImage();
            if (image != null) {
                Glide.with(context).load(aPost.getImage().getUrl()).centerCrop().into(imageIV);
            }else {
                Glide.with(context).load(R.drawable.placeholder).centerCrop().into(imageIV);
            }
        }
    }
}
