package com.fbu.instagrom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.fbu.instagrom.R;
import com.fbu.instagrom.adapters.CommentAdapter;
import com.fbu.instagrom.databinding.ActivityPostDetailsBinding;
import com.fbu.instagrom.fragments.CommentDialogFragment;
import com.fbu.instagrom.models.Comment;
import com.fbu.instagrom.models.Post;
import com.fbu.instagrom.models.RelativeTime;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class PostDetailsActivity extends AppCompatActivity implements CommentDialogFragment.CommentDialogListener {
    private static final String TAG = "PostDetailsActivity";
    private ActivityPostDetailsBinding binding;
    private Post post;
    private CommentAdapter adapter;
    private RecyclerView commentsRV;
    private List<Comment> listComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.d(TAG, String.format("Showing details for '%s'", post.getDescription()));
        binding.outer.setBackgroundColor(getResources().getColor(R.color.gray_tint));
        //Setup Post
        ParseUser user = post.getUser();
        try {
            user.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        binding.usernameTextView.setText(user.getUsername());
        binding.descriptionTextView.setText(post.getDescription());

        RelativeTime relativeTime = new RelativeTime();
        binding.timeStampTextView.setText(relativeTime.getRelativeTimeAgo(post.getTime()));

        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(post.getImage().getUrl()).centerCrop().into(binding.imageIV);
        } else {
            Glide.with(this).load(R.drawable.placeholder).centerCrop().into(binding.imageIV);
        }


        ParseFile profileImageSource = post.getUser().getParseFile("profilePic");
        if (profileImageSource != null) {
            Glide.with(this).load(profileImageSource.getUrl()).centerCrop().circleCrop().into(binding.profileImage);
        } else {
            Glide.with(this).load(R.drawable.placeholder).centerCrop().circleCrop().into(binding.profileImage);
        }

        setOnClickListener();

        //Setting up the recycler view for comments
        listComments = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new CommentAdapter(PostDetailsActivity.this, listComments);
        commentsRV = findViewById(R.id.commentsRV);
        commentsRV.setLayoutManager(linearLayoutManager);
        commentsRV.setAdapter(adapter);
        listComments.addAll(post.getCommentList());
        adapter.notifyDataSetChanged();
    }

    private void setOnClickListener() {
        binding.commentAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCommentDialog();
                Log.i("PostAdapter", "comment clicked");
            }
        });

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

    private void goToCommentDialog() {
        CommentDialogFragment commentDialogFragment = new CommentDialogFragment();
        commentDialogFragment.show(getSupportFragmentManager(), "comment dialog");
    }

    @Override
    public void applyComment(String text) {
        //commentText = text;
        final Post commentedPost = post;
        final Comment comment = new Comment();
        comment.setPost(commentedPost);
        comment.setText(text);
        comment.setUser(ParseUser.getCurrentUser());
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("comment button", "Error while saving comment", e);
                }
                commentedPost.setComment(comment);
                commentedPost.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e("comment button", "Error while saving post", e);
                        }
                    }
                });
            }
        });

        listComments.clear();
        listComments.addAll(post.getCommentList());
        adapter.notifyDataSetChanged();
    }
}