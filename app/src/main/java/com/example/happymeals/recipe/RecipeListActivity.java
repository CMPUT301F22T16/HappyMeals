package com.example.happymeals.recipe;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.ListView;
import android.widget.Toast;

import com.example.happymeals.DBHandler;
import com.example.happymeals.LoadingDialog;
import com.example.happymeals.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeListActivity extends AppCompatActivity implements RecipeListInterface {

    private ExtendedFloatingActionButton addRecipeButton;
    private ListView recipeListView;
    private List<Recipe> recipes;
    private RecipeListAdapter recipeAdapter;
    private FloatingActionButton sortButton;
    private DBHandler db;
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
        addRecipeButton = findViewById(R.id.add_recipe_button);


        // Sort button
        sortButton = findViewById(R.id.sort_recipes);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeSortFragment.newInstance(recipes, recipeAdapter).show(getSupportFragmentManager(), "SORTING RECIPES");
            }
        });

        // Get the current user
        Bundle bundle = getIntent().getExtras();
        String username = (String) bundle.getSerializable("USER");
        db = new DBHandler(username);

        // Add back button to action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recipes");

        // Setup recipe list
        recipeListView = findViewById(R.id.recipe_list);
        recipes = new ArrayList<>();
        LoadingDialog dialog = new LoadingDialog(this);
        recipeAdapter = new RecipeListAdapter(this, recipes, db, this);
        recipeListView.setAdapter(recipeAdapter);
        dialog.startLoadingDialog();
        db.getUserRecipes(recipeAdapter,dialog);



        // Setup add recipe button
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
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
            String filetype = result.getData().getStringExtra("filetype");
            rec.setTitle(title);
            rec.setPreparation_time(prepTime);
            rec.setNum_servings(numServ);
            rec.setCategory(category);
            rec.setComments(comments);
            rec.setIngredients(ing);


            String uriStr = result.getData().getStringExtra("photo");
            Uri uri = null;
            if (!Objects.equals(uriStr, null) && !uriStr.equals("")) {
                uri = Uri.parse(uriStr);
            }
            db.updateRecipe(rec);

            db.uploadImage(uri, rec, filetype);
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
            String filetype = result.getData().getStringExtra("filetype");

            String uriStr = result.getData().getStringExtra("photo");
            Uri uri = null;
            if (!Objects.equals(uriStr, null) && !uriStr.equals("")) {
                uri = Uri.parse(uriStr);
            }
            Recipe newRecipe = new Recipe(title, prepTime, numServ, category, comments, ing);
            db.addRecipe(newRecipe);

            db.uploadImage(uri, newRecipe, filetype);
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
            bundle.putDouble("SCALE",1.0);
            bundle.putSerializable("RECIPE", recipes.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }

        if (op.equals("delete")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm deletion");
            builder.setMessage("Are you sure you want to delete this recipe?");
            Context context = this;
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context, "Recipe deleted", Toast.LENGTH_SHORT).show();
                    db.removeRecipe(recipeAdapter.getItem(position));
                    recipeAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();

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
            intent.putExtra("photo", recipe.getDownloadUri());
            edit_recipe_for_result.launch(intent);

        }
    }

}