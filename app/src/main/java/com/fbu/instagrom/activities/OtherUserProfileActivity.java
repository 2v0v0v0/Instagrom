package com.fbu.instagrom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.fbu.instagrom.R;
import com.fbu.instagrom.adapters.PostProfileAdapter;
import com.fbu.instagrom.databinding.ActivityOtherUserProfileBinding;
import com.fbu.instagrom.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class OtherUserProfileActivity extends AppCompatActivity {
    private static final String TAG = "OtherUserProfile";
    private PostProfileAdapter postProfileAdapter;
    private List<Post> userPosts;
    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityOtherUserProfileBinding binding = ActivityOtherUserProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        user = (ParseUser) Parcels.unwrap(getIntent().getParcelableExtra("clickedOnProfile"));
        Log.i(TAG, String.format("Showing details for '%s'", user.getUsername() + " " + user.getObjectId()));

        if(!ParseUser.getCurrentUser().getUsername().equals(user.getUsername())){
            binding.setProfilePicButton.setVisibility(View.GONE);
        }
        Log.i(TAG, "current user" + ParseUser.getCurrentUser().getUsername());
        userPosts = new ArrayList<>();
        postProfileAdapter = new PostProfileAdapter(this, userPosts);
        binding.postsRV.setAdapter(postProfileAdapter);
        binding.postsRV.setLayoutManager(new GridLayoutManager(this, 3));
        try {
            if (user.getString("screenName") == null || user.getString("screenName").trim().equals("")) {
                binding.textViewScreenName.setText(user.getUsername());
            } else {
                binding.textViewScreenName.setText(user.getString("screenName"));
                binding.textViewUsername.setText(user.getUsername());
            }

            ParseFile image = user.getParseFile("profilePic");
            if (image != null) {
                Glide.with(this).load(image.getUrl()).centerCrop().circleCrop().into(binding.profileImage);
            } else {
                Glide.with(this).load(R.drawable.placeholder).centerCrop().into(binding.profileImage);
            }
            queryPosts();
        } catch (Exception e) {
            Log.d(TAG, "Error: " + e);
        }
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATEDAT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts");
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                userPosts.addAll(posts);
                postProfileAdapter.notifyDataSetChanged();
            }
        });
    }
}