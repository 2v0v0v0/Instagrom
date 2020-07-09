package com.fbu.instagrom.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.fbu.instagrom.R;
import com.fbu.instagrom.adapters.PostProfileAdapter;
import com.fbu.instagrom.databinding.FragmentProfileBinding;
import com.fbu.instagrom.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "PostsFragment";
    private SwipeRefreshLayout swipeContainer;
    private FragmentProfileBinding binding;
    private PostProfileAdapter postProfileAdapter;
    private List<Post> allPosts;
    private ParseUser user = ParseUser.getCurrentUser();

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        swipeContainer = binding.swipeContainer;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allPosts = new ArrayList<>();
        postProfileAdapter = new PostProfileAdapter(getContext(), allPosts);
        binding.postsRV.setAdapter(postProfileAdapter);
        binding.postsRV.setLayoutManager(new GridLayoutManager(getContext(), 3));

        try {
            if (user.getString("screenName") == null || user.getString("screenName").trim().equals("")) {
                binding.textViewScreenName.setText(user.getString("screenName"));
                binding.textViewUsername.setText(user.getUsername());
            } else {
                binding.textViewScreenName.setText(user.getUsername());
            }

            ParseFile image = user.getParseFile("profilePic");
            if (image != null) {
                Glide.with(this).load(image.getUrl()).centerCrop().circleCrop().into(binding.profileImage);
            } else {
                Glide.with(this).load(R.drawable.placeholder).circleCrop().into(binding.profileImage);
            }
        } catch (Exception e) {
            Log.d(TAG, "Error: " + e);
        }
        queryPosts();
        pullRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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
                allPosts.addAll(posts);
                postProfileAdapter.notifyDataSetChanged();
            }
        });
    }

    private void pullRefresh() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allPosts.clear();
                queryPosts();
                Log.i(TAG, "fetching data");
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(
                R.color.princtonOrange,
                R.color.grape,
                R.color.iris,
                R.color.jasmine,
                R.color.vividCerise);
    }

}

