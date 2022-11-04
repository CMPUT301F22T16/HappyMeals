package com.example.happymeals;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.happymeals.recipe.EditRecipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity implements RecipeListInterface{

    private ListView recipe_list_view;
    private List<Recipe> recipes;

    ActivityResultLauncher<Intent> edit_recipe_for_result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            handleEditRecipeForResultLauncher(result);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        Context context = this;
        LoadingDialog ld = new LoadingDialog(RecipeListActivity.this);
        // Get the current user
        Bundle bundle = getIntent().getExtras();
        String username = (String) bundle.getSerializable("USER");
        User curUser = new User(username);

        // Add back button to action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recipe_list_view = findViewById(R.id.recipe_list);
        recipes = new ArrayList<>();
        RecipeListAdapter recipeAdapter = new RecipeListAdapter(this, recipes, curUser, this);
        recipe_list_view.setAdapter(recipeAdapter);

        // Populate the recipe list
        curUser.getUserRecipes(recipeAdapter, ld, context);

    }

    public void handleEditRecipeForResultLauncher(ActivityResult result) {

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

    @Override
    public void onItemClick(int position, String op) {
//        if (op.equals("edit")) {
//            Intent intent = new Intent(RecipeListActivity.this, EditRecipe.class);
//            Recipe recipe = recipes.get(position);
//            intent.putExtra("title", recipe.getTitle());
//            intent.putExtra("preparation_time", recipe.getPreparation_time());
//            intent.putExtra("num_servings", recipe.getNum_servings());
//            intent.putExtra("category", recipe.getCategory());
//            edit_recipe_for_result.launch(intent);
//            // TODO create an intent to put comments
////            intent.putExtra("ingredients", recipe.getIngredients());
//        }
    }
}