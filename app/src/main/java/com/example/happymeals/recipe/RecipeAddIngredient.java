package com.example.happymeals.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.happymeals.DBHandler;
import com.example.happymeals.Ingredient;
import com.example.happymeals.IngredientAdaptor;
import com.example.happymeals.LoadingDialog;
import com.example.happymeals.R;
import com.example.happymeals.RecipeListAdapter;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class creates an Activity that allows the user to add an ingredient to their recipe
 * @author John Yu
 */
public class RecipeAddIngredient extends AppCompatActivity {

    /**
     * This EditText lets the user specify the ingredient description
     */
    EditText recipe_add_ingredient_desc;

    /**
     * This EditText lets the user specify the ingredient amount
     */
    EditText recipe_add_ingredient_amount;

    /**
     * This button lets the user submit their new ingredient
     */
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add_ingredient);

        recipe_add_ingredient_desc = findViewById(R.id.recipe_add_ingredient_description);
        recipe_add_ingredient_amount = findViewById(R.id.recipe_add_ingredient_amount);

        submit_btn = findViewById(R.id.recipe_add_ingredient_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("description", recipe_add_ingredient_desc.getText().toString());
                intent.putExtra("amount", recipe_add_ingredient_amount.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}