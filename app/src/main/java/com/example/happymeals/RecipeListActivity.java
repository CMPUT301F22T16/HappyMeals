package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
        Bundle bundle = getIntent().getExtras();
        String username = (String) bundle.getSerializable("USER");
        DBHandler db = new DBHandler(username);

        // Add back button to action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recipes");


        recipe_list_view = findViewById(R.id.recipe_list);
        recipes = new ArrayList<>();
        RecipeListAdapter recipeAdapter = new RecipeListAdapter(this, recipes, db);
        recipe_list_view.setAdapter(recipeAdapter);
        LoadingDialog dialog = new LoadingDialog(this);
        db.getUserRecipes(recipeAdapter,dialog,this);


        // Populate the recipe list
        db.getUserRecipes(recipeAdapter, new LoadingDialog(this), this);

        recipe_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("SELECTED");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}