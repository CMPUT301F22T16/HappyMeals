package com.example.happymeals;

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
        for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {

            // decimal adjusting for nice formatting
            Double decimal = recipeIngredient.getAmount() - Math.floor(recipeIngredient.getAmount());
            String amount = recipeIngredient.getAmount().toString();
            if (decimal == 0.0f) {
                amount = Long.toString(Math.round(recipeIngredient.getAmount()));
            }

            ingredientListAdapter.add(String.format("%s %s", amount + " " + recipeIngredient.getUnit(), recipeIngredient.getDescription()));
        }

        for (String comment: recipe.getComments()) {
            commentListAdapter.add(comment);
        }

        category.setText(recipe.getCategory());
        servings.setText("Servings: " + recipe.getNum_servings());
        prep_time.setText(recipe.getPreparation_time() + " min/s");

        // Setting photo
        String uri = recipe.getDownloadUri();
        System.out.println("IIIIIIIIIII" + uri);
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