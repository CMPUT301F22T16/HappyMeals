package com.example.happymeals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The IngredientAdaptor class defines how the an object of the "Ingredient" class will be displayed in a ListView
 */
// Coding of this adaptor comes from lab example.
public class IngredientAdaptor extends ArrayAdapter<Ingredient> {

    public IngredientAdaptor(Context context, ArrayList<Ingredient> ingredients) {
        super(context, 0, ingredients);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ingredient ingredient = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ingredient_row, parent, false);
        }

        TextView description = (TextView) convertView.findViewById(R.id.description);
        TextView bestbefore = (TextView) convertView.findViewById(R.id.bestbefore);
        TextView count = (TextView) convertView.findViewById(R.id.count);
        TextView unitcost = (TextView) convertView.findViewById(R.id.unitcost);

        // Set the values for display.
        description.setText("Name: " + ingredient.getDescription());
        if (ingredient.getMonth() < 10){
            if (ingredient.getDay() < 10){
                bestbefore.setText("Bestbefore date: " + ingredient.getYear() + "-0" + ingredient.getMonth() + "-0" + ingredient.getDay());
            } else {
                bestbefore.setText("Bestbefore date: " + ingredient.getYear() + "-0" + ingredient.getMonth() + "-" + ingredient.getDay());
            }
        } else {
            bestbefore.setText("Bestbefore date: " + ingredient.getYear() + "-" + ingredient.getMonth() + "-" + ingredient.getDay());
        }
        count.setText("Count: " + String.valueOf(ingredient.getAmount()));
        unitcost.setText("Unit cost: $" + String.valueOf(ingredient.getCost()));

        return convertView;
    }
}
