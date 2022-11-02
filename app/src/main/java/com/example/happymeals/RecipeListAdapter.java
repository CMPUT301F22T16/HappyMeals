package com.example.happymeals;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;
import android.view.LayoutInflater;
import android.widget.TextView;

public class RecipeListAdapter extends ArrayAdapter<Recipe> {
    Context context;
    List<Recipe> recipes;

    RecipeListAdapter(Context context, List<Recipe> recipes) {
        super(context, 0, recipes);
        this.context = context;
        this.recipes = recipes;
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

        return convertView;
    }
}
