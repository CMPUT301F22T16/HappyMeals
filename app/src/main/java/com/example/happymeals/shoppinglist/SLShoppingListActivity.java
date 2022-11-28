package com.example.happymeals.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.happymeals.DBHandler;
import com.example.happymeals.recipe.RecipeIngredient;
import com.example.happymeals.databinding.ActivitySlshoppingListBinding;
import com.example.happymeals.mealplan.MealPlan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SLShoppingListActivity extends AppCompatActivity {
    ActivitySlshoppingListBinding activity_slshopping_list;

    ArrayList<RecipeIngredient> recipeIngredients;
    ArrayAdapter shoppingListAdaptor;
    MealPlan mealPlan;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity_slshopping_list = ActivitySlshoppingListBinding.inflate(getLayoutInflater());

        setContentView(activity_slshopping_list.getRoot());

        // Add back button to action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Shopping List");

        // get userId
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = new DBHandler(user.getUid());

        Bundle bundle = getIntent().getExtras();
        mealPlan = (MealPlan) bundle.getSerializable("MEALPLAN");

        activity_slshopping_list.slShoppingListText.setText(String.format("%s SL", mealPlan.getTitle()));
        recipeIngredients = new ArrayList<>();

        shoppingListAdaptor = new SLShoppingListAdapter(this, recipeIngredients, activity_slshopping_list);
        activity_slshopping_list.slShoppingList.setAdapter(shoppingListAdaptor);
        db.getSLIngredients(shoppingListAdaptor, mealPlan);

    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        db.getSLIngredients(shoppingListAdaptor, mealPlan);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}