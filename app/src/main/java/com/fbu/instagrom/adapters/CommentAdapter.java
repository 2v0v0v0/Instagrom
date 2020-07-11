package com.fbu.instagrom.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fbu.instagrom.databinding.ItemCommentsBinding;
import com.fbu.instagrom.models.Comment;
import com.fbu.instagrom.models.RelativeTime;
import com.parse.ParseException;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private static final String TAG = "Comment Adapter";
    private Context context;
    private List<Comment> listComments;

    public CommentAdapter(Context context, List<Comment> listComments) {
        this.context = context;
        this.listComments = listComments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCommentsBinding itemCommentsBinding = ItemCommentsBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemCommentsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment aComment = listComments.get(position);
        try {
            holder.bind(aComment);
        } catch (ParseException e) {
            Log.d(TAG, "onBindViewHolder: ",e);
        }
    }

    @Override
    public int getItemCount() {
        return listComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCommentsBinding binding;

        public ViewHolder(ItemCommentsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Comment comment) throws ParseException {
            binding.commentBodyTextView.setText(comment.getText());
            binding.usernametextView.setText(comment.getUser().getUsername());
            RelativeTime relativeTime = new RelativeTime();
            binding.timeTextView.setText(relativeTime.getRelativeTimeAgo(comment.getTime()));
        }
    }
}
