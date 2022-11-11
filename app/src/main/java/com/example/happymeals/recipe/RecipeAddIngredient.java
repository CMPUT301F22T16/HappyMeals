package com.example.happymeals.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.happymeals.DBHandler;
import com.example.happymeals.Recipe;
import com.example.happymeals.RecipeIngredient;
import com.example.happymeals.UserIngredient;
import com.example.happymeals.IngredientAdaptor;
import com.example.happymeals.R;

import java.util.ArrayList;

/**
 * This class creates an Activity that allows the user to add an ingredient to their recipe
 * @author John Yu
 */
public class RecipeAddIngredient extends AppCompatActivity {

    EditText description_edit_text;
    EditText category_edit_text;
    EditText amount_edit_text;

    /**
     * This button lets the user submit their new ingredient
     */
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add_ingredient);

        Intent intent = getIntent();

        description_edit_text = findViewById(R.id.recipe_add_ingredient_description);
        category_edit_text = findViewById(R.id.recipe_add_ingredient_category);
        amount_edit_text = findViewById(R.id.recipe_add_ingredient_amount);

        submit_btn = findViewById(R.id.recipe_add_ingredient_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                String description = description_edit_text.getText().toString();
                String category = category_edit_text.getText().toString();
                Integer amount = amount_edit_text.getText().toString().equals("") ? 0 : Integer.parseInt(amount_edit_text.getText().toString());

                intent.putExtra("description", description);
                intent.putExtra("category", category);
                intent.putExtra("amount", amount);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}