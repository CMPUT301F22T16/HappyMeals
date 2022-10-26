package com.example.happymeals.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.happymeals.R;

import java.util.ArrayList;

public class IngredientAdapter extends ArrayAdapter<Ingredient> {
    private ArrayList<Ingredient> ingredients;
    private Context context;

    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredients) {
        super(context, 0, ingredients);
        this.ingredients = ingredients;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.add_recipe_custom_layout, parent, false);
        }

        Ingredient ingredient = ingredients.get(position);

        TextView ingredient_desc = view.findViewById(R.id.add_recipe_view_ingredient_desc);

        ingredient_desc.setText(ingredient.getDesc());

        return view;
    }
}
