package com.example.happymeals.shoppinglist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.happymeals.DBHandler;
import com.example.happymeals.R;
import com.example.happymeals.mealplan.MealPlan;
import com.example.happymeals.recipe.RecipeIngredient;
import com.example.happymeals.UserIngredient;
import com.example.happymeals.databinding.IngredientRowBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SLShoppingListAdapter extends ArrayAdapter<RecipeIngredient> {
    private Context viewContext = getContext();
    private Context mContext;
    private DBHandler db;
    public SLShoppingListAdapter(@NonNull Context context, ArrayList<RecipeIngredient> recipeIngredients) {
        super(context, 0, recipeIngredients);
        mContext = context;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = new DBHandler(user.getUid());
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
        Button pickup = (Button) convertView.findViewById(R.id.pickup);
        pickup.setVisibility(View.VISIBLE);

        // Set the values for display.
        description.setText(recipeIngredient.getDescription());
        category.setText(recipeIngredient.getCategory());
        amount.setText(String.format("Amount: %.2f",recipeIngredient.getAmount()));
        unit.setText("Unit: " + recipeIngredient.getUnit());

        pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.pickUpIngredient(SLShoppingListAdapter.this, recipeIngredient.getDescription(), recipeIngredient.getCategory(), recipeIngredient.getAmount(), recipeIngredient.getUnit(), position);

            }
        });

        return convertView;
    }
}
