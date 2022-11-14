package com.example.happymeals.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.happymeals.R;

public class RecipeEditComment extends AppCompatActivity {

    /**
     * This is the EditText view for the user to edit their comment.
     */
    EditText commentEditText;

    /**
     * This is the Button that the user can submit their newly edited comment.
     */
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_edit_comment);

        commentEditText = findViewById(R.id.recipe_edit_comment_editText);
        submitBtn = findViewById(R.id.recipe_edit_comment_submit_button);

        Intent intent = getIntent();
        commentEditText.setText(intent.getStringExtra("comment").toString());

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = commentEditText.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("comment", comment);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}