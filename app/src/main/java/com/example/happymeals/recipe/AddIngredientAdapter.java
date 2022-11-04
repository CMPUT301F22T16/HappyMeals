package com.example.happymeals.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.happymeals.Ingredient;
import com.example.happymeals.R;

import java.util.ArrayList;

/**
 * The IngredientAdaptor class defines how the an object of the "Ingredient" class will be displayed in a ListView
 */
// Coding of this adaptor comes from lab example.
public class AddIngredientAdapter extends ArrayAdapter<Ingredient> {

    public AddIngredientAdapter(Context context, ArrayList<Ingredient> ingredients) {
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

        int month = ingredient.getMonth()+1;
        int day = ingredient.getDay();
        // Set the values for display.
        description.setText(ingredient.getDescription());
        if (ingredient.getMonth() < 9){
            if (ingredient.getDay() < 9){
                bestbefore.setText("Bestbefore: " + ingredient.getYear() + "-0" + month + "-0" + day);
            } else {
                bestbefore.setText("Bestbefore: " + ingredient.getYear() + "-0" + month + "-" + day);
            }
        } else {
            bestbefore.setText("Bestbefore: " + ingredient.getYear() + "-" + month + "-" + day);
        }
        count.setText("Amount: " + String.valueOf(ingredient.getAmount()));

        System.out.println("Here");
        System.out.println(ingredient.getCost());
        unitcost.setText("Unit cost: $" + Double.toString(ingredient.getCost()));

        return convertView;
    }
}
