package com.fbu.instagrom.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fbu.instagrom.EndlessRecyclerViewScrollListener;
import com.fbu.instagrom.R;
import com.fbu.instagrom.models.Comment;
import com.fbu.instagrom.models.Post;
import com.fbu.instagrom.adapters.PostsAdapter;
import com.fbu.instagrom.databinding.FragmentPostsBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment {
    private static final String TAG = "PostsFragment";
    private SwipeRefreshLayout swipeContainer;
    FragmentPostsBinding binding;
    protected PostsAdapter postsAdapter;
    protected List<Post> allPosts;
    private LinearLayoutManager layoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;

    public PostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostsBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        swipeContainer = binding.swipeContainer;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allPosts = new ArrayList<>();
        postsAdapter = new PostsAdapter(getContext(), allPosts);
        binding.postsRV.setAdapter(postsAdapter);
        layoutManager = new LinearLayoutManager(getContext());
        binding.postsRV.setLayoutManager(layoutManager);

        queryPosts();
        infiniteScroll();
        pullRefresh();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    protected void pullRefresh() {
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

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.include(Post.KEY_COMMENTS);
        query.setLimit(10);
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
                postsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadMoreData() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereLessThan(Post.KEY_CREATEDAT,allPosts.get(allPosts.size()-1).getCreatedAt());
        query.include(Post.KEY_USER);
        query.setLimit(10);
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
                postsAdapter.notifyDataSetChanged();
            }
        });
    }

    public void infiniteScroll(){
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore: " + page);
                loadMoreData();
            }
        };
        binding.postsRV.addOnScrollListener(scrollListener);
    }

}