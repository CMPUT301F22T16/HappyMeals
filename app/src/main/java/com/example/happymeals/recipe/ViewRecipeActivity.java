package com.example.happymeals.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.happymeals.R;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

public class ViewRecipeActivity extends AppCompatActivity {

    private ImageView photo;
    private TextView category;
    private TextView servings;
    private TextView prepTime;
    private ListView ingredientsList;
    private ListView commentsList;

    private List<String> ingredientStrList;
    private List<String> commentStrList;

    private ArrayAdapter<String> ingredientListAdapter;
    private ArrayAdapter<String> commentListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        // Fetching the recipe
        Bundle bundle = getIntent().getExtras();
        Double scaling_factor = (Double) bundle.getDouble("SCALE");
        Recipe recipe = (Recipe) bundle.getSerializable("RECIPE");

        // Setting the title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(recipe.getTitle());

        // Setting up Views
        photo = findViewById(R.id.recipe_detail_image);
        category = findViewById(R.id.recipe_detail_category);
        servings = findViewById(R.id.recipe_detail_servings);
        prepTime = findViewById(R.id.recipe_detail_preptime);
        ingredientsList = findViewById(R.id.ingredients_list);
        commentsList = findViewById(R.id.comments_list);
        ingredientStrList = new ArrayList<>();
        commentStrList = new ArrayList<>();
        ingredientListAdapter = new ArrayAdapter<String>(this, R.layout.recipe_details_content, ingredientStrList);
        commentListAdapter = new ArrayAdapter<String>(this, R.layout.recipe_details_content, commentStrList);
        ingredientsList.setAdapter(ingredientListAdapter);
        commentsList.setAdapter(commentListAdapter);

        // Setting the page details
        for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {

            // decimal adjusting for nice formatting
            Double decimal = recipeIngredient.getAmount()*scaling_factor - Math.floor(recipeIngredient.getAmount())*scaling_factor;
            Double a = recipeIngredient.getAmount()*scaling_factor;
            String amount = a.toString();
            if (decimal == 0.0f) {
                amount = Long.toString(Math.round(recipeIngredient.getAmount()));
            }

            ingredientListAdapter.add(String.format("%s %s", amount + " " + recipeIngredient.getUnit(), recipeIngredient.getDescription()));
        }

        for (String comment: recipe.getComments()) {
            commentListAdapter.add(comment);
        }

        category.setText(recipe.getCategory());
        servings.setText("Servings: " + recipe.getNum_servings()*scaling_factor);
        prepTime.setText(recipe.getPreparation_time()*scaling_factor + " min/s");

        // Setting photo
        String uri = recipe.getDownloadUri();

        if (uri != null && !uri.equals("")) {
            Glide.with(this).asBitmap()
                    .load(uri)
                    .centerCrop()
                    .placeholder(R.color.white)
                    .into(new BitmapImageViewTarget(photo) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(this.getView().getResources(), resource);
                            circularBitmapDrawable.setCornerRadius(32.0f); // radius for corners
                            view.setImageDrawable(circularBitmapDrawable);
                        }
                    });;
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
}