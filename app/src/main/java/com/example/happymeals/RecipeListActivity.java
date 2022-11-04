package com.example.happymeals;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.happymeals.recipe.EditRecipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RecipeListActivity extends AppCompatActivity implements RecipeListInterface{

    private ListView recipe_list_view;
    private List<Recipe> recipes;
    private RecipeListAdapter recipeAdapter;
    DBHandler db;
    private int position;


    /**
     * This is an ActivityResultLauncher that launches an activity that opens the EditRecipe Activity
     * @author John Yu
     */
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

        // Get the current user
        Bundle bundle = getIntent().getExtras();
        String username = (String) bundle.getSerializable("USER");
        db = new DBHandler(username);

        // Add back button to action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recipes");
        recipe_list_view = findViewById(R.id.recipe_list);
        recipes = new ArrayList<>();
        recipeAdapter = new RecipeListAdapter(this, recipes, db, this);

        recipe_list_view.setAdapter(recipeAdapter);
        LoadingDialog dialog = new LoadingDialog(this);
        db.getUserRecipes(recipeAdapter,dialog);

        // Populate the recipe list
        db.getUserRecipes(recipeAdapter, new LoadingDialog(this));

    }

    /**
     * This method handles the result after the user returns from the EditRecipe Activity
     * @author John Yu
     * @param result contains the data that gets returned from the EditRecipe Activity
     */
    public void handleEditRecipeForResultLauncher(ActivityResult result) {
        if (result != null && result.getResultCode() == RESULT_OK) {
            if (result.getData() == null) return;
            String title = result.getData().getStringExtra("title");
            int prepTime = result.getData().getIntExtra("prep_time", 0);
            int numServ = result.getData().getIntExtra("num_serv", 0);
            String category = result.getData().getStringExtra("category");
            List<Ingredient> ing = (List<Ingredient>) result.getData().getSerializableExtra("ingredients");
            Log.d("Random", ing.toString());
            Recipe rec = recipes.get(position);
            rec.setTitle(title);
            rec.setPreparation_time(prepTime);
            rec.setNum_servings(numServ);
            rec.setCategory(category);
            // TODO Get the updateRecipe method to work when a Recipe containing ingredients is passed.
            //  Currently the method crashes when this happens.
//            List<Ingredient> test = new ArrayList<>();
//            test.add(new Ingredient(3, "Test"));
//            test.add(new Ingredient(4, "Test 2"));
//            rec.setIngredients(test);
            db.updateRecipe(rec);
        } else {
            Toast.makeText(RecipeListActivity.this, "Failed to add recipe", Toast.LENGTH_SHORT).show();
        }
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

    /**
     * This method is invoked in the RecipeListAdapter class when the user presses on the Edit Recipe button
     * This method defines the functionality that happens after the Edit Recipe button is pressed
     * @author John Yu
     * @param position This is an integer that stores the index position of the Recipe that was clicked. The
     *                 index position can be used to retrive the recipe from the recipes list
     * @param op This is a string that defines the type of operation to perform. When the user clicks on the Edit
     *           Recipe button the operation to perform is edit and certain lines of code will be executed. When
     *           the user clicks on the View Recipe button the operation to perform is view and again certain lines of
     *           code will be executed.
     */
    @Override
    public void onItemClick(int position, String op) {
        if (op == "view") {
            Intent intent = new Intent(RecipeListActivity.this, ViewRecipeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("RECIPE", recipes.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }

        if (op == "delete") {
            db.removeRecipe(recipeAdapter.getItem(position));
            recipeAdapter.notifyDataSetChanged();
        }


        if (op.equals("edit")) {
            this.position = position;
            Intent intent = new Intent(RecipeListActivity.this, EditRecipe.class);
            Recipe recipe = recipes.get(this.position);
            intent.putExtra("title", recipe.getTitle());
            intent.putExtra("preparation_time", recipe.getPreparation_time());
            intent.putExtra("num_servings", recipe.getNum_servings());
            intent.putExtra("category", recipe.getCategory());
            intent.putStringArrayListExtra("comments", (ArrayList<String>) recipe.getComments());
            intent.putExtra("ingredients", (ArrayList<Ingredient>) recipe.getIngredients());
            Bundle bundle = new Bundle();
            bundle.putSerializable("USER", db.getUsername());
            intent.putExtras(bundle);
            edit_recipe_for_result.launch(intent);

        }
    }
}