package com.fbu.instagrom.fragments;

import android.app.Dialog;
import android.os.Bundle;
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
import com.fbu.instagrom.models.Comment;

public class CommentDialogFragment extends DialogFragment {
    private String TAG = "CommentDialog";
    private EditText commentEditText;
    private Button postButton;
    private ImageView cancelButton;

    public CommentDialogFragment() {
        // Empty constructor is required for DialogFragment
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

        return builder.create();

    }
}
