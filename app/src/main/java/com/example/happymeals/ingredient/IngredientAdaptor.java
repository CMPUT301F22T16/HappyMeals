package com.example.happymeals.ingredient;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.happymeals.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * The IngredientAdaptor class defines how the an object of the "Ingredient" class will be displayed in a ListView
 */
// Coding of this adaptor comes from lab example.
public class IngredientAdaptor extends ArrayAdapter<UserIngredient> implements Serializable {

    List<UserIngredient> userIngredientList;
    List<UserIngredient> ingredientBuffer;

    public IngredientAdaptor(Context context, ArrayList<UserIngredient> userIngredients) {

        super(context, 0, userIngredients);

        this.ingredientBuffer = new ArrayList<>();
        this.userIngredientList = userIngredients;

        System.out.println(this.userIngredientList);
    }

    /**
     * Filter recipe list based on user inputs to search bar
     * got inspiration from https://abhiandroid.com/ui/searchview
     */
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        userIngredientList.clear();
        if (charText.length() == 0) {
            userIngredientList.addAll(ingredientBuffer);
        } else {
            for (UserIngredient r : ingredientBuffer) {
                if (r.getDescription().toLowerCase(Locale.getDefault()).contains(charText)) {
                    userIngredientList.add(r);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void add(@Nullable UserIngredient object) {
        super.add(object);
        ingredientBuffer.add(object);
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

        Date now = new Date();
        String desc = userIngredient.getDescription();

        if (now.after(userIngredient.getDate())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                description.setTextColor(getContext().getColor(R.color.red));
            }
            desc = desc + " (Expired)";
            description.setText(desc);

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                description.setTextColor(getContext().getColor(R.color.black));
            }
            description.setText(desc);
        }


        if (userIngredient.getMonth() < 9){
            if (userIngredient.getDay() < 9){
                bestbefore.setText("Bestbefore: " + userIngredient.getYear() + "-0" + month + "-0" + day);
            } else {
                bestbefore.setText("Bestbefore: " + userIngredient.getYear() + "-0" + month + "-" + day);
            }
        } else {
            bestbefore.setText("Bestbefore: " + userIngredient.getYear() + "-" + month + "-" + day);
        }
        System.out.println(userIngredient.getDescription() + " " + userIngredient.getUnit());
        count.setText( String.valueOf(userIngredient.getAmount()) + " " + userIngredient.getUnit());

        if (userIngredient.getLoc().equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                description.setTextColor(getContext().getColor(R.color.red));
            }
            desc = desc + " (Picked-up)";
            description.setText(desc);
        }
        unitcost.setText("$" + Double.toString(userIngredient.getCost()));
        return convertView;
    }

    @Nullable
    @Override
    public UserIngredient getItem(int position) {
        return this.userIngredientList.get(position);
    }
}
