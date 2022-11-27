package com.example.happymeals.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.happymeals.R;
import com.example.happymeals.RecipeIngredient;
import com.example.happymeals.UserIngredient;
import com.example.happymeals.databinding.IngredientRowBinding;

import java.util.ArrayList;

public class SLShoppingListAdapter extends ArrayAdapter<RecipeIngredient> {
    private Context mContext;
    public SLShoppingListAdapter(@NonNull Context context, ArrayList<RecipeIngredient> recipeIngredients) {
        super(context, 0, recipeIngredients);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RecipeIngredient recipeIngredient = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ingredient_row, parent, false);
        }

        TextView description = (TextView) convertView.findViewById(R.id.description);
        TextView category = (TextView) convertView.findViewById(R.id.bestbefore);
        TextView amount = (TextView) convertView.findViewById(R.id.count);
        TextView unit = (TextView) convertView.findViewById(R.id.unitcost);

        // Set the values for display.
        description.setText(recipeIngredient.getDescription());
        category.setText(recipeIngredient.getCategory());
        amount.setText("Amount: " + String.valueOf(recipeIngredient.getAmount()));
        unit.setText("Unit: " + recipeIngredient.getUnit());

        return convertView;
    }
}
