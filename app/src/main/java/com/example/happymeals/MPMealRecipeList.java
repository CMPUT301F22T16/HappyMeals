package com.example.happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.happymeals.databinding.ActivityMpmealRecipeListBinding;

import java.util.ArrayList;
import java.util.List;

public class MPMealRecipeList extends AppCompatActivity {

    ActivityMpmealRecipeListBinding activityMpmealRecipeListBinding;
    RecyclerView.Adapter mpMealRecipeListAdapter;

    ArrayList<Recipe> recipes;
    Button addRecipButton;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMpmealRecipeListBinding = ActivityMpmealRecipeListBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_mpmeal_recipe_list);

        // for testing
        Ingredient ind = new Ingredient(3,"carrot");
        List<String> comments = new ArrayList<>();
        comments.add("LGTM!");
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ind);
        Recipe r1 = new Recipe("Greedy recipe",1,1,"vst", comments, ingredients);
        recipes = new ArrayList<>();
        recipes.add(r1);

        addRecipButton = findViewById(R.id.mp_recipe_add_button);
        intent = new Intent(this,MPPickRecipeActivity.class);

        mpMealRecipeListAdapter = new MPMealRecipeListAdapter(this, recipes);
        activityMpmealRecipeListBinding.mpRecipeListRecyclerview.setAdapter(mpMealRecipeListAdapter);

        activityMpmealRecipeListBinding.mpRecipeListRecyclerview.setLayoutManager(new GridLayoutManager(this, 1));

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
        setOnAddButtonListener();
        mpMealRecipeListAdapter.notifyDataSetChanged();
        itemTouchHelper.attachToRecyclerView(activityMpmealRecipeListBinding.mpRecipeListRecyclerview);
    }

    private void setOnAddButtonListener() {
        addRecipButton.setOnClickListener(v -> {
            // pass user id, mealplan index(or which day it is)
//            intent.putExtra();
            startActivity(intent);

        });
    }
}