package com.example.happymeals;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RecipeListAdapter extends ArrayAdapter<Recipe> {
    Context context;
    List<Recipe> recipes;
    DBHandler db;
    RecipeListInterface recipeListInterface;

    RecipeListAdapter(Context context, List<Recipe> recipes, DBHandler user, RecipeListInterface recipeListInterface) {
        super(context, 0, recipes);
        this.context = context;
        this.recipes = recipes;
        this.db = db;
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

        title_text.setText(title);
        preparation_text.setText("Preparation time: " + prep_time);
        category_text.setText("Category: " + category);

        // Adding on click listeners for delete, edit and view
        FloatingActionButton delete_recipe = (FloatingActionButton) convertView.findViewById(R.id.recipe_card_delete);
        FloatingActionButton edit_recipe = (FloatingActionButton) convertView.findViewById(R.id.recipe_card_edit);
        Button view_recipe = (Button) convertView.findViewById(R.id.recipe_card_view);

        delete_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.removeRecipe(recipes.get(position), context);
                recipes.remove(position);
                notifyDataSetChanged();

            }
        });

        edit_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO add john's code here
                if (recipeListInterface == null) return;
                recipeListInterface.onItemClick(position, "edit");
            }
        });

        view_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO start ViewRecipeActivity
            }
        });

        return convertView;
    }
}
