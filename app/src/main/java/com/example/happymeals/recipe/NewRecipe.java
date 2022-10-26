package com.example.happymeals.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import com.example.happymeals.R;

public class NewRecipe extends AppCompatActivity {

//    ListView recipe_ingredient_list;
    RecyclerView recipe_ingredient_list;
    RecipeIngredientAdapter ingredient_adapter;
    ArrayList<Ingredient> ingredient_data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        recipe_ingredient_list = findViewById(R.id.recipe_ingredient_recyclerview);

        ingredient_data_list = new ArrayList<>();
        ingredient_data_list.add(new Ingredient("Carrot"));
        ingredient_data_list.add(new Ingredient("Broccoli"));
        ingredient_data_list.add(new Ingredient("Chicken"));
        ingredient_data_list.add(new Ingredient("Milk"));
        ingredient_data_list.add(new Ingredient("Eggs"));

        ingredient_adapter = new RecipeIngredientAdapter(this, ingredient_data_list);
        recipe_ingredient_list.setAdapter(ingredient_adapter);
        recipe_ingredient_list.setLayoutManager(new LinearLayoutManager(this));
    }
}