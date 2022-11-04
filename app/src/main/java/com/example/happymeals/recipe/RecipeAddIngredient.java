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

public class RecipeAddIngredient extends AppCompatActivity {

    EditText recipe_add_ingredient_desc;
    EditText recipe_add_ingredient_amount;
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