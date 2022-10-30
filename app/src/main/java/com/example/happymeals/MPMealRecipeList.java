package com.example.happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.happymeals.databinding.ActivityMealRecipeListBinding;

import java.util.ArrayList;

public class MPMealRecipeList extends AppCompatActivity {

    ActivityMealRecipeListBinding activityMealRecipeListBinding;
    RecyclerView.Adapter mpMealRecipeListAdapter;
    ArrayList<Recipe> recipes;
    Button addRecipButton;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_recipe_list);

        addRecipButton = findViewById(R.id.mp_recipe_add_button);
        intent = new Intent(this,MPPickRecipeActivity.class);

        mpMealRecipeListAdapter = new MPMealRecipeListAdapter(this, recipes);
        activityMealRecipeListBinding.mpRecipeListRecyclerview.setLayoutManager(new GridLayoutManager(this, 1));
        activityMealRecipeListBinding.mpRecipeListRecyclerview.setAdapter(mpMealRecipeListAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final Recipe recipe = recipes.get(viewHolder.getAdapterPosition());
                // delete recipe
            }
        });
        itemTouchHelper.attachToRecyclerView(activityMealRecipeListBinding.mpRecipeListRecyclerview);
    }

    private void setOnAddButtonListener() {
        addRecipButton.setOnClickListener(v -> {
            // pass user id, mealplan index(or which day it is)
//            intent.putExtra();
            startActivity(intent);

        });
    }
}