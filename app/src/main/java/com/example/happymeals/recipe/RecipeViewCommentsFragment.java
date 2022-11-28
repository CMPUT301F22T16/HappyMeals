package com.example.happymeals.recipe;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.happymeals.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class RecipeViewCommentsFragment extends DialogFragment implements RecyclerViewInterface {

    /**
     * This is the context
     */
    Context context;

    /**
     * This stores a collection of {@link String} objects.
     */
    ArrayList<String> recipe_comments_data_list;

    /**
     * This is the recyclerview that displays each comment.
     */
    RecyclerView recipe_comments_list;

    /**
     * This is the button to add a new comment.
     */
    FloatingActionButton add_comment_btn;

    /**
     * This is the adapter for the recyclerview.
     */
    RecipeCommentsAdapter comments_adapter;

    /**
     * Handles the action to take when the user presses OK.
     */
    OnFragmentInteractionListener listener;

    /**
     * Stores the comment index position the user selecte.d
     */
    int comment_selection = -1;

    /**
     * This creates an ActivityResultLauncher where the user can send and receive data to the {@link RecipeEditComment} class
     */
    ActivityResultLauncher<Intent> edit_comment_for_result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            handleEditCommentForResultLauncher(result);
        }
    });

    /**
     * This creates an ActivityResultLauncher where the user can send and receive data to the {@link RecipeAddComment} class
     */
    ActivityResultLauncher<Intent> add_comment_for_result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            handleAddCommentForResultLauncher(result);
        }
    });

    @Override
    public void onItemClick(int position, String op) {
        if (Objects.equals(op, "delete_comment")) {
            this.recipe_comments_data_list.remove(position);
            this.recipe_comments_list.setAdapter(this.comments_adapter);
        } else if (Objects.equals(op, "edit_comment")) {
            this.comment_selection = position;
            Intent intent = new Intent(getContext(), RecipeEditComment.class);
            String comment = recipe_comments_data_list.get(position);
            intent.putExtra("comment", comment);
            edit_comment_for_result.launch(intent);
        }
    }

    public interface OnFragmentInteractionListener {
        void onOkPressed_Comment(ArrayList<String> data_list);
    }

    public RecipeViewCommentsFragment(Context context, ArrayList<String> data_list) {
        this.context = context;
        this.recipe_comments_data_list = data_list;
        this.comments_adapter = new RecipeCommentsAdapter(this.context, this.recipe_comments_data_list, this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_comments_fragment_layout, null);
        recipe_comments_list = view.findViewById(R.id.recipe_comments_recyclerview);
        recipe_comments_list.setLayoutManager(new LinearLayoutManager(getContext()));
        recipe_comments_list.setAdapter(this.comments_adapter);
        add_comment_btn = view.findViewById(R.id.recipe_add_new_comment_button);
        add_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RecipeAddComment.class);
                add_comment_for_result.launch(intent);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("View Comments")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onOkPressed_Comment(recipe_comments_data_list);
                    }
                }).create();
    }

    /**
     * This method handles the return value after the user edits a comment.
     * @param result the returned value from the {@link RecipeEditComment} Activity
     */
    public void handleEditCommentForResultLauncher(ActivityResult result) {
        if (result != null && result.getResultCode() == RESULT_OK) {
            if (result.getData() == null) return;
            String comment = result.getData().getStringExtra("comment");
            recipe_comments_data_list.set(comment_selection, comment);
            recipe_comments_list.setAdapter(comments_adapter);
        } else {
            Toast.makeText(getContext(), "Failed to edit comment", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method handles the return value after the user adds a comment.
     * @param result the returned value from the {@link RecipeAddComment} Activity
     */
    public void handleAddCommentForResultLauncher(ActivityResult result) {
        if (result != null && result.getResultCode() == RESULT_OK) {
            if (result.getData() == null) return;
            String commentExtra = result.getData().getStringExtra("comment");
            recipe_comments_data_list.add(commentExtra);
            recipe_comments_list.setAdapter(comments_adapter);
        }
    }
}