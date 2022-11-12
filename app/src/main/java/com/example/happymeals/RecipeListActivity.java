package com.example.happymeals;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.happymeals.recipe.EditRecipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity implements RecipeListInterface{

    private Button add_recipe_button;
    private Spinner sort_recipe_spinner;
    private ListView recipe_list_view;
    private List<Recipe> recipes;
    private RecipeListAdapter recipeAdapter;
    DBHandler db;
    private int position;


    ActivityResultLauncher<Intent> edit_recipe_for_result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            handleEditRecipeForResultLauncher(result);
        }
    });

    ActivityResultLauncher<Intent> add_recipe_for_result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            handleAddRecipeForResultLauncher(result);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        // Initialize the Add Recipe Button
        add_recipe_button = findViewById(R.id.add_recipe_button);

        // Initialize the sort recipe spinner
        sort_recipe_spinner = findViewById(R.id.sort_recipe_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.recipe_sort, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort_recipe_spinner.setAdapter(spinnerAdapter);

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

        // Add recipe onClickListener
        add_recipe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeListActivity.this, EditRecipe.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("USER", db.getUsername());
                intent.putExtras(bundle);
                // The operation extra tells the EditRecipe Activity whether it is adding or editing a recipe
                intent.putExtra("operation", "add");
                add_recipe_for_result.launch(intent);
            }
        });
    }

    public void handleEditRecipeForResultLauncher(ActivityResult result) {
        if (result != null && result.getResultCode() == RESULT_OK) {
            if (result.getData() == null) return;
            String title = result.getData().getStringExtra("title");
            int prepTime = result.getData().getIntExtra("prep_time", 0);
            int numServ = result.getData().getIntExtra("num_serv", 0);
            String category = result.getData().getStringExtra("category");
            List<RecipeIngredient> ing = (ArrayList<RecipeIngredient>) result.getData().getSerializableExtra("ingredients");
            List<String> comments = (ArrayList<String>) result.getData().getSerializableExtra("comments");
            Recipe rec = recipes.get(position);
            rec.setTitle(title);
            rec.setPreparation_time(prepTime);
            rec.setNum_servings(numServ);
            rec.setCategory(category);
            rec.setComments(comments);
            rec.setIngredients(ing);
            db.updateRecipe(rec);
        } else {
            Toast.makeText(RecipeListActivity.this, "Failed to edit recipe", Toast.LENGTH_SHORT).show();
        }
    }

    public void handleAddRecipeForResultLauncher(ActivityResult result) {
        if (result != null && result.getResultCode() == RESULT_OK) {
            if (result.getData() == null) return;
            String title = result.getData().getStringExtra("title");
            int prepTime = result.getData().getIntExtra("prep_time", 0);
            int numServ = result.getData().getIntExtra("num_serv", 0);
            String category = result.getData().getStringExtra("category");
            List<String> comments = (ArrayList<String>) result.getData().getSerializableExtra("comments");
            List<RecipeIngredient> ing = (ArrayList<RecipeIngredient>) result.getData().getSerializableExtra("ingredients");
            Recipe newRecipe = new Recipe(title, prepTime, numServ, category, comments, ing);
            db.addRecipe(newRecipe);
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

    @Override
    public void onItemClick(int position, String op) {
        if (op.equals("view")) {
            Intent intent = new Intent(RecipeListActivity.this, ViewRecipeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("RECIPE", recipes.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }

        if (op.equals("delete")) {
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
            intent.putExtra("comments", (ArrayList<String>) recipe.getComments());
            intent.putExtra("ingredients", (ArrayList<RecipeIngredient>) recipe.getIngredients());
            Bundle bundle = new Bundle();
            bundle.putSerializable("USER", db.getUsername());
            intent.putExtras(bundle);
            // The operation extra tells the EditRecipe Activity whether it is adding or editing a recipe
            intent.putExtra("operation", "edit");
            edit_recipe_for_result.launch(intent);

        }
    }
}