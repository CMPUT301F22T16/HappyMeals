package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

        recipe_list_view = findViewById(R.id.recipe_list);
        recipes = new ArrayList<>();
        RecipeListAdapter recipeAdapter = new RecipeListAdapter(this, recipes);
        recipe_list_view.setAdapter(recipeAdapter);
        Recipe recipe = new Recipe();
        recipeAdapter.add(recipe);
        recipeAdapter.add(new Recipe());
        recipeAdapter.add(new Recipe());


        recipe_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                
            }
        });
    }
}