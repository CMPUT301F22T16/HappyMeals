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
public class IngredientAdaptor extends ArrayAdapter<UserIngredient> {

    public IngredientAdaptor(Context context, ArrayList<UserIngredient> userIngredients) {
        super(context, 0, userIngredients);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserIngredient userIngredient = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ingredient_row, parent, false);
        }

        TextView description = (TextView) convertView.findViewById(R.id.description);
        TextView bestbefore = (TextView) convertView.findViewById(R.id.bestbefore);
        TextView count = (TextView) convertView.findViewById(R.id.count);
        TextView unitcost = (TextView) convertView.findViewById(R.id.unitcost);

        int month = userIngredient.getMonth()+1;
        int day = userIngredient.getDay();
        // Set the values for display.
        description.setText(userIngredient.getDescription());
        if (userIngredient.getMonth() < 9){
            if (userIngredient.getDay() < 9){
                bestbefore.setText("Bestbefore: " + userIngredient.getYear() + "-0" + month + "-0" + day);
            } else {
                bestbefore.setText("Bestbefore: " + userIngredient.getYear() + "-0" + month + "-" + day);
            }
        } else {
            bestbefore.setText("Bestbefore: " + userIngredient.getYear() + "-" + month + "-" + day);
        }
        count.setText("Amount: " + String.valueOf(userIngredient.getAmount()));

        System.out.println("Here");
        System.out.println(userIngredient.getCost());
        unitcost.setText("Unit cost: $" + Double.toString(userIngredient.getCost()));

        return convertView;
    }
}
