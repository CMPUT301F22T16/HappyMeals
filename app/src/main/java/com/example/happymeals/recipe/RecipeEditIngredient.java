package com.example.happymeals.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.happymeals.R;

import java.util.Date;

/**
 * This class opens an Activity to allow the user to edit an ingredient in their recipe
 * @author John Yu
 */
public class RecipeEditIngredient extends AppCompatActivity {

    /**
     * This EditText lets the user edit the ingredient description
     */
    EditText desc_edit_text;

    /**
     * This EditText lets the user edit the ingredient location
     */
    EditText loc_edit_text;

    /**
     * This EditText lets the user edit the ingredient date
     */
    EditText date_edit_text;

    /**
     * This EditText lets the user update the ingredient category
     */
    EditText category_edit_text;

    /**
     * This EditText lets the user update the ingredient amount
     */
    EditText amount_edit_text;

    /**
     * This EditText lets the user update the ingredient cost
     */
    EditText cost_edit_text;

    /**
     * This Button will clear all the data in the ingredient
     */
    Button clear_btn;

    /**
     * This button will push all data of the ingredient to the {@link EditRecipe} class.
     */
    Button save_btn;

    /**
     * This is a String that stores the ingredient description
     */
    String desc = null;

    /**
     * This is a String that stores the ingredient location
     */
    String loc = null;

    /**
     * This is a string that stores the ingredient date
     */
    Date date;

    /**
     * This is a string that stores the ingredient category
     */
    String category = null;

    /**
     * This is an integer that stores the ingredient amount
     */
    Integer amount = null;

    /**
     * This is a double that stores the ingredient cost
     */
    Double cost = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_edit_ingredient);

        desc_edit_text = findViewById(R.id.recipe_edit_ingredient_description);
        loc_edit_text = findViewById(R.id.recipe_edit_ingredient_location);
        date_edit_text = findViewById(R.id.recipe_edit_ingredient_date);
        category_edit_text = findViewById(R.id.recipe_edit_ingredient_category);
        amount_edit_text = findViewById(R.id.recipe_edit_ingredient_amount);
        cost_edit_text = findViewById(R.id.recipe_edit_ingredient_cost);
        clear_btn = findViewById(R.id.recipe_edit_ingredient_clear_btn);
        save_btn = findViewById(R.id.recipe_edit_ingredient_save_btn);

        Intent intent = getIntent();
        desc = intent.getStringExtra("desc");
        amount = intent.getIntExtra("amount", 0);

        desc_edit_text.setText(desc);
        amount_edit_text.setText(getString(R.string.integer_to_string, amount));

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("desc", desc_edit_text.getText().toString());
                intent.putExtra("amount", Integer.parseInt(amount_edit_text.getText().toString()));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}