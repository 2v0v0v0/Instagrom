package com.fbu.instagrom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.fbu.instagrom.R;
import com.fbu.instagrom.databinding.ItemPostBinding;
import com.fbu.instagrom.models.Post;
import com.fbu.instagrom.models.RelativeTime;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class PostDetailsActivity extends AppCompatActivity {
    private static final String TAG = "PostDetailsActivity";
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ItemPostBinding binding = ItemPostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.d(TAG, String.format("Showing details for '%s'", post.getDescription()));

        binding.outer.setBackgroundColor(getResources().getColor(R.color.gray_tint));

        binding.usernameTextView.setText(post.getUser().getUsername());
        binding.descriptionTextView.setText(post.getDescription());

        RelativeTime relativeTime = new RelativeTime();
        binding.timeStampTextView.setText(relativeTime.getRelativeTimeAgo(post.getTime()));

        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(post.getImage().getUrl()).centerCrop().into(binding.imageIV);
        }else {
            Glide.with(this).load(R.drawable.placeholder).centerCrop().into(binding.imageIV);
        }


        ParseFile profileImageSource = ParseUser.getCurrentUser().getParseFile("profilePic");
        if (image != null) {
            Glide.with(this).load(profileImageSource.getUrl()).centerCrop().circleCrop().into(binding.profileImage);
        }else {
            Glide.with(this).load(R.drawable.placeholder).centerCrop().circleCrop().into(binding.profileImage);
        }

        binding.usernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToClickedProfile();
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToClickedProfile();
            }
        });
    }

    private void goToClickedProfile() {
        ParseUser user = post.getUser();
        Intent intent = new Intent(this, OtherUserProfileActivity.class);
        intent.putExtra("clickedOnProfile", Parcels.wrap(user));
        startActivity(intent);
    }
}