package com.example.happymeals;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.happymeals.recipe.EditRecipe;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class RecipeListActivity extends AppCompatActivity implements RecipeListInterface, AdapterView.OnItemSelectedListener {

    private ExtendedFloatingActionButton add_recipe_button;
    private Spinner sort_recipe_spinner;
    private ListView recipe_list_view;
    private List<Recipe> recipes;
    private RecipeListAdapter recipeAdapter;
    private FloatingActionButton sortButton;
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
        recipe_list_view = findViewById(R.id.recipe_list);
        recipes = new ArrayList<>();
        recipeAdapter = new RecipeListAdapter(this, recipes, db, this);
        recipe_list_view.setAdapter(recipeAdapter);
        LoadingDialog dialog = new LoadingDialog(this);
        db.getUserRecipes(recipeAdapter,dialog);

        // Populate the recipe list
        db.getUserRecipes(recipeAdapter, new LoadingDialog(this));


        // Setup add recipe button
        add_recipe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecipeAction(view);
            }
        });



    }

    public void addRecipeAction(View view) {
        // TODO start the AddRecipeActivity here

        Intent intent = new Intent(RecipeListActivity.this, EditRecipe.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER", db.getUsername());
        intent.putExtras(bundle);
        // The operation extra tells the EditRecipe Activity whether it is adding or editing a recipe
        intent.putExtra("operation", "add");
        add_recipe_for_result.launch(intent);
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

            String uriStr = result.getData().getStringExtra("photo");
            Uri uri = null;
            if (!Objects.equals(uriStr, "")) {
                uri = Uri.parse(uriStr);
            }
            rec.setDownloadUri(uriStr);
            db.updateRecipe(rec);

            db.uploadImage(uri, rec);
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

            String uriStr = result.getData().getStringExtra("photo");
            Uri uri = null;
            if (!Objects.equals(uriStr, "")) {
                uri = Uri.parse(uriStr);
            }
            Recipe newRecipe = new Recipe(title, prepTime, numServ, category, comments, ing);
            newRecipe.setDownloadUri(uriStr);
            db.addRecipe(newRecipe);
            // TODO pass file extension
//            ContentResolver cR = this.getContentResolver();
//            String type = cR.getType(uri);
            db.uploadImage(uri, newRecipe);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        switch (adapterView.getItemAtPosition(i).toString()) {
//            case "Title (A - Z)":
//                Collections.sort(recipes, (o1, o2) -> (o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase())));
//                break;
//            case "Title (Z - A)":
//                Collections.sort(recipes, (o1, o2) -> (o2.getTitle().toLowerCase().compareTo(o1.getTitle().toLowerCase())));
//                break;
//            case "Preparation Time (High to Low)":
//                Collections.sort(recipes, (o1, o2) -> (o2.getPreparation_time() - o1.getPreparation_time()));
//                break;
//            case "Preparation Time (Low to High)":
//                Collections.sort(recipes, (o1, o2) -> (o1.getPreparation_time() - o2.getPreparation_time()));
//                break;
//            case "Number of Servings (High to Low)":
//                Collections.sort(recipes, (o1, o2) -> (o2.getNum_servings() - o1.getNum_servings()));
//                break;
//            case "Number of Servings (Low to High)":
//                Collections.sort(recipes, (o1, o2) -> (o1.getNum_servings() - o2.getNum_servings()));
//                break;
//            case "Recipe Category (A - Z)":
//                Collections.sort(recipes, (o1, o2) -> (o1.getCategory().toLowerCase().compareTo(o2.getCategory().toLowerCase())));
//                break;
//            case "Recipe Category (Z - A)":
//                Collections.sort(recipes, (o1, o2) -> (o2.getCategory().toLowerCase().compareTo(o1.getCategory().toLowerCase())));
//                break;
//            default:
//                Toast.makeText(RecipeListActivity.this, "Error when selecting sort method.", Toast.LENGTH_SHORT).show();
//                break;
//        }
        recipe_list_view.setAdapter(recipeAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        ;
    }
}