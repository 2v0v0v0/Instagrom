package com.fbu.instagrom.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fbu.instagrom.R;
import com.fbu.instagrom.activities.MainActivity;
import com.fbu.instagrom.activities.OtherUserProfileActivity;
import com.fbu.instagrom.activities.PostDetailsActivity;
import com.fbu.instagrom.fragments.ProfileFragment;
import com.fbu.instagrom.models.Post;
import com.fbu.instagrom.models.RelativeTime;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> listPosts;
    public int REQUEST_CODE = 1002;

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView usernameTextView;
        private ImageView imageIV;
        private TextView descriptionTextView;
        private TextView timeStampTextView;
        private ImageView profileImageIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            imageIV = itemView.findViewById(R.id.imageIV);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            timeStampTextView = itemView.findViewById(R.id.timeStampTextView);
            profileImageIV = itemView.findViewById(R.id.profileImage);

            itemView.setOnClickListener(this);
        }

        public void bind(Post aPost) {
            descriptionTextView.setText(aPost.getDescription());
            usernameTextView.setText(aPost.getUser().getUsername());
            RelativeTime relativeTime = new RelativeTime();
            timeStampTextView.setText(relativeTime.getRelativeTimeAgo(aPost.getTime()));
            ParseFile image = aPost.getImage();
            if (image != null) {
                Glide.with(context).load(aPost.getImage().getUrl()).centerCrop().into(imageIV);
            } else {
                Glide.with(context).load(R.drawable.placeholder).centerCrop().into(imageIV);
            }

            ParseFile profileImageSource = aPost.getUser().getParseFile("profilePic");
            if (profileImageSource != null) {
                Glide.with(context).load(profileImageSource.getUrl()).centerCrop().circleCrop().into(profileImageIV);
            }else {
                Glide.with(context).load(R.drawable.placeholder).centerCrop().circleCrop().into(profileImageIV);
            }

            setOnClickProfile();
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post post = listPosts.get(position);
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                context.startActivity(intent);
            }
        }

        private void setOnClickProfile() {
            profileImageIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    goToClickedProfile(position);
                }
            });

            usernameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    goToClickedProfile(position);
                }
            });
        }

        private void goToClickedProfile(int position) {
            Post post = listPosts.get(position);
            ParseUser user = post.getUser();
            Intent intent = new Intent(context, OtherUserProfileActivity.class);
            intent.putExtra("clickedOnProfile", Parcels.wrap(user));
            context.startActivity(intent);
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
