package com.example.happymeals.recipe;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import java.io.Serializable;
import java.util.List;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.happymeals.DBHandler;
import com.example.happymeals.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RecipeListAdapter extends ArrayAdapter<Recipe> implements Serializable {
    private Context context;
    private List<Recipe> recipes;
    private DBHandler db;
    private RecipeListInterface recipeListInterface;


    RecipeListAdapter(Context context, List<Recipe> recipes, DBHandler user, RecipeListInterface recipeListInterface) {
        super(context, 0, recipes);
        this.context = context;
        this.recipes = recipes;
        this.db = user;
        this.recipeListInterface = recipeListInterface;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Recipe recipe = recipes.get(position);
        String title = recipe.getTitle();
        String prep_time = Integer.toString(recipe.getPreparation_time());
        String category = recipe.getCategory();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recipe_card_content, parent, false);
        }

        TextView title_text = convertView.findViewById(R.id.recipe_card_title);
        TextView preparation_text = convertView.findViewById(R.id.recipe_card_prep_time);
        TextView category_text = convertView.findViewById(R.id.recipe_card_category);
        ImageView image = convertView.findViewById(R.id.recipe_card_image);

        title_text.setText(title);
        String timeut = "mins";
        if (recipe.getPreparation_time() == 1) {
            timeut = "min";
        }
        preparation_text.setText("Preparation time: " + prep_time + " " + timeut);
        category_text.setText("Category: " + category);
        // Adding on click listeners
        FloatingActionButton editButton = convertView.findViewById(R.id.recipe_card_edit);
        FloatingActionButton deleteButton = convertView.findViewById(R.id.recipe_card_delete);
        Button viewButton = convertView.findViewById(R.id.recipe_card_view);

        // Downloading and setting the image
        // https://stackoverflow.com/questions/35305875/progress-bar-while-loading-image-using-glide
        String uri = recipe.getDownloadUri();

        if (uri != null && !uri.equals("")) {

            CircularProgressDrawable progress = new CircularProgressDrawable(this.context);
            progress.setStrokeWidth(5f);
            progress.setCenterRadius(30f);
            progress.start();

            Glide.with(this.context).asBitmap()
                    .load(uri)
                    .centerCrop()
                    .placeholder(progress)
                    .into(new BitmapImageViewTarget(image) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCornerRadius(32.0f); // radius for corners
                            view.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }

        else {

            CircularProgressDrawable progress = new CircularProgressDrawable(this.context);
            progress.setStrokeWidth(5f);
            progress.setCenterRadius(30f);
            progress.start();
            Uri uridefault = Uri.parse("android.resource://com.example.happymeals/drawable/recipe_default");
            Glide.with(this.context).asBitmap()
                    .load(uridefault)
                    .centerCrop()
                    .placeholder(progress)
                    .into(new BitmapImageViewTarget(image) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCornerRadius(32.0f); // radius for corners
                            view.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeListInterface.onItemClick(position, "edit");
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeListInterface.onItemClick(position, "view");
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeListInterface.onItemClick(position, "delete");
            }
        });

        return convertView;
    }
}
