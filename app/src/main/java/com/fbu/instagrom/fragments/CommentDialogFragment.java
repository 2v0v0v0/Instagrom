package com.fbu.instagrom.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.fbu.instagrom.R;

public class CommentDialogFragment extends DialogFragment {
    private static final String TAG = "CommentDialog";
    private EditText commentEditText;
    private Button postButton;
    private ImageView cancelButton;
    protected CommentDialogListener listener;
    private String text = "";

    public CommentDialogFragment() {
        // Empty constructor is required for DialogFragment
    }

    public static CommentDialogFragment newInstance(CommentDialogListener lis) {
        CommentDialogFragment fragment = new CommentDialogFragment();
        fragment.listener = lis;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_comment, null);

        builder.setView(view);

        commentEditText = view.findViewById(R.id.commentEditText);
        postButton = view.findViewById(R.id.postButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        cancelComment();
        postComment();

        return builder.create();

    }

    /*@Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (CommentDialogListener) context;
        } catch (Exception e) {
            Log.e(TAG, "comment dialog error : ", e);
        }
    }*/

    private void cancelComment(){
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void postComment(){
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = commentEditText.getText().toString();
                Log.i(TAG, text);
                listener.applyComment(text);
                Log.i(TAG, "post button clicked");
                dismiss();
            }
        });
    }

    public interface CommentDialogListener{
        void applyComment(String text);
    }
}
