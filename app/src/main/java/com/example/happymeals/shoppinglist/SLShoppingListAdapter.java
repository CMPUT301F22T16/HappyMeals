package com.example.happymeals.shoppinglist;

import static java.lang.Boolean.TRUE;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import com.example.happymeals.DBHandler;
import com.example.happymeals.R;
import com.example.happymeals.ViewIngredientFragment;
import com.example.happymeals.databinding.ActivitySlshoppingListBinding;
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
    private ActivitySlshoppingListBinding binding;
    public SLShoppingListAdapter(@NonNull Context context, ArrayList<RecipeIngredient> recipeIngredients, ActivitySlshoppingListBinding binding) {
        super(context, 0, recipeIngredients);
        mContext = context;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = new DBHandler(user.getUid());
        this.binding = binding;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RecipeIngredient recipeIngredient = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sl_ingredient_row, parent, false);
        }

        TextView description = (TextView) convertView.findViewById(R.id.sl_description);
        TextView category = (TextView) convertView.findViewById(R.id.sl_category);
        TextView amount = (TextView) convertView.findViewById(R.id.sl_count);
        TextView unit = (TextView) convertView.findViewById(R.id.sl_unit);
        Button pickup = (Button) convertView.findViewById(R.id.pickup);
        pickup.setVisibility(View.VISIBLE);

        ArrayList<String> locations = new ArrayList<>();
        db.getStorageTypes(locations, TRUE);

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

        pickup.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                UserIngredient userIngredient = new UserIngredient(recipeIngredient.getAmount(), recipeIngredient.getDescription());
                userIngredient.setCategory(recipeIngredient.getCategory());
                userIngredient.setUnit(recipeIngredient.getUnit());

                ViewIngredientFragment.newInstance(userIngredient, locations, true).show(((FragmentActivity)mContext).getSupportFragmentManager(), "VIEW_INGREDIENT");
                return true;
            }
        });

        return convertView;
    }
}
