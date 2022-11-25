package com.example.happymeals.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.happymeals.DBHandler;
import com.example.happymeals.IngredientAdaptor;
import com.example.happymeals.RecipeIngredient;
import com.example.happymeals.databinding.ActivitySlshoppingListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SLShoppingListActivity extends AppCompatActivity {
    ActivitySlshoppingListBinding activity_slshopping_list;

    ArrayList<RecipeIngredient> recipeIngredients;
    ArrayAdapter shoppingListAdaptor;

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
        DBHandler db = new DBHandler(user.getUid());

        recipeIngredients = new ArrayList<>();

        shoppingListAdaptor = new SLShoppingListAdapter(this, recipeIngredients);



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