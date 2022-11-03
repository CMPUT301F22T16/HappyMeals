package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    private ListView recipe_list_view;
    private List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        // Get the current user
//        Bundle bundle = getIntent().getExtras();
//        String username = (String) bundle.getSerializable("USER");
//        User curUser = new User(username);
        recipes = new ArrayList<>();
        User curUser = new User();
        recipe_list_view = findViewById(R.id.recipe_list);
        RecipeListAdapter recipeAdapter = new RecipeListAdapter(this, recipes, curUser);
        recipe_list_view.setAdapter(recipeAdapter);
        LoadingDialog dialog = new LoadingDialog(this);
        curUser.getUserRecipes(recipeAdapter,dialog,this);


        // Populate the recipe list
        //TODO
    }
}