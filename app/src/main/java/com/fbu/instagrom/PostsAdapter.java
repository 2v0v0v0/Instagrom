package com.fbu.instagrom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fbu.instagrom.models.RelativeTime;
import com.parse.ParseFile;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> listPosts;

    public PostsAdapter(Context context, List<Post> listPosts) {
        this.context = context;
        this.listPosts = listPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
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

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView usernameTextView;
        private ImageView imageIV;
        private TextView descriptionTextView;
        private TextView timeStampTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            imageIV = itemView.findViewById(R.id.imageIV);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            timeStampTextView = itemView.findViewById(R.id.timeStampTextView);
        }

        public void bind(Post aPost) {
            descriptionTextView.setText(aPost.getDescription());
            usernameTextView.setText(aPost.getUser().getUsername());
            RelativeTime relativeTime = new RelativeTime();
            timeStampTextView.setText(relativeTime.getRelativeTimeAgo(aPost.getTime()));
            ParseFile image = aPost.getImage();
            if(image != null){
                Glide.with(context).load(aPost.getImage().getUrl()).centerCrop().into(imageIV);
            }

        }
    }

    public void clear() {
        listPosts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        listPosts.addAll(list);
        notifyDataSetChanged();
    }
}
