package com.example.happymeals.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.happymeals.R;

public class RecipeAddComment extends AppCompatActivity {

    /**
     * This is the EditText View where the user can type in their comment.
     */
    EditText commentEditText;

    /**
     * This is the submit button where the user can submit their comment.
     */
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add_comment);

        commentEditText = findViewById(R.id.recipe_add_comment_editText);
        submitBtn = findViewById(R.id.recipe_add_comment_submit_button);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allEntered;
                allEntered = !commentEditText.getText().toString().isEmpty();

                if (commentEditText.getText().toString().isEmpty()) {
                    commentEditText.requestFocus();
                    commentEditText.setError("Please provide the recipe comment.");
                }

                if (allEntered) {
                    String comment = commentEditText.getText().toString();

                    Intent intent = new Intent();
                    intent.putExtra("comment", comment);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}