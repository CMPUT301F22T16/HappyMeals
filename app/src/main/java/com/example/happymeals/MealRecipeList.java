package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.happymeals.databinding.ActivityMealRecipeListBinding;

import java.util.ArrayList;

public class MealRecipeList extends AppCompatActivity {
    ActivityMealRecipeListBinding activityMealRecipeListBinding;
    RecyclerView.Adapter mpMealRecipeListAdapter;
    ArrayList<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_recipe_list);

        mpMealRecipeListAdapter = new MPMealRecipeListAdapter(this, recipes);
    }
}