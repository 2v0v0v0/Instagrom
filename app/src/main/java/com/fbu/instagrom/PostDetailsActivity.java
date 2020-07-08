package com.fbu.instagrom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.fbu.instagrom.databinding.ActivityPostDetailsBinding;
import com.fbu.instagrom.models.Post;
import com.fbu.instagrom.models.RelativeTime;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class PostDetailsActivity extends AppCompatActivity {
    private static final String TAG = "PostDetailsActivity";
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityPostDetailsBinding binding = ActivityPostDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.d(TAG, String.format("Showing details for '%s'", post.getDescription()));

        binding.usernameTextView.setText(post.getUser().getUsername());
        binding.descriptionTextView.setText(post.getDescription());

        RelativeTime relativeTime = new RelativeTime();
        binding.timeStampTextView.setText(relativeTime.getRelativeTimeAgo(post.getTime()));

        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(post.getImage().getUrl()).centerCrop().into(binding.imageIV);
        }
    }



}