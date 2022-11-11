package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewRecipeActivity extends AppCompatActivity {

    ImageView photo;
    TextView category;
    TextView servings;
    TextView prep_time;
    ListView ingredients_list;
    ListView comments_list;

    List<String> ingredient_str_list;
    List<String> comment_str_list;

    ArrayAdapter<String> ingredientListAdapter;
    ArrayAdapter<String> commentListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        // Fetching the recipe
        Bundle bundle = getIntent().getExtras();
        Recipe recipe = (Recipe) bundle.getSerializable("RECIPE");

        // Setting the title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(recipe.getTitle());

        // Setting up Views
        photo = findViewById(R.id.recipe_detail_image);
        category = findViewById(R.id.recipe_detail_category);
        servings = findViewById(R.id.recipe_detail_servings);
        prep_time = findViewById(R.id.recipe_detail_preptime);
        ingredients_list = findViewById(R.id.ingredients_list);
        comments_list = findViewById(R.id.comments_list);
        ingredient_str_list = new ArrayList<>();
        comment_str_list = new ArrayList<>();
        ingredientListAdapter = new ArrayAdapter<String>(this, R.layout.recipe_details_content, ingredient_str_list);
        commentListAdapter = new ArrayAdapter<String>(this, R.layout.recipe_details_content, comment_str_list);
        ingredients_list.setAdapter(ingredientListAdapter);
        comments_list.setAdapter(commentListAdapter);

        // Setting the page details
        for (UserIngredient userIngredient : recipe.getIngredients()) {
            ingredientListAdapter.add(String.format("%-25s %50s", userIngredient.getDescription(), userIngredient.getAmount() + " uts"));
        }

        for (String comment: recipe.getComments()) {
            commentListAdapter.add(comment);
        }

        category.setText(recipe.getCategory());
        servings.setText("Servings: " + recipe.getNum_servings());
        prep_time.setText(recipe.getPreparation_time() + " min/s");
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