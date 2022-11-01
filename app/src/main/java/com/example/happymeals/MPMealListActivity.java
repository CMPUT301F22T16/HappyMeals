package com.example.happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.happymeals.databinding.ActivityMpmealListBinding;

import java.util.ArrayList;
import java.util.List;

public class MPMealListActivity extends AppCompatActivity {

    ActivityMpmealListBinding activityMpmealListBinding;
    RecyclerView.Adapter mpMealListAdapter;
    ArrayList<Meal> meals;
    Button addMealsButton;
    Button nextDayButton;
    Button finishButton;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMpmealListBinding = ActivityMpmealListBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_mpmeal_list);

        addMealsButton = findViewById(R.id.meal_plan_add_button);
        nextDayButton = findViewById(R.id.mp_fab_next_day);
        finishButton = findViewById(R.id.mp_fab_finish);
        intent = new Intent(this, MPMyMealsActivity.class);

        //Testing
        Ingredient ind = new Ingredient(3,"carrot");
        List<String> comments = new ArrayList<>();
        comments.add("LGTM!");
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ind);
        Recipe r1 = new Recipe("Greedy recipe",1,1,"vst", comments, ingredients);
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(r1);
        List<Double> scalings = new ArrayList<>();
        scalings.add(1.11);
        meals = new ArrayList<>();
        meals.add(new Meal(recipes,scalings,3.4));

        mpMealListAdapter = new MPMealListAdapter(this, meals);
        activityMpmealListBinding.mpMealListRecyclerview.setLayoutManager(new GridLayoutManager(this, 1));
        activityMpmealListBinding.mpMealListRecyclerview.setAdapter(mpMealListAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final Meal meal = meals.get(viewHolder.getAdapterPosition());
                // delete mealPlan
            }
        });
        itemTouchHelper.attachToRecyclerView(activityMpmealListBinding.mpMealListRecyclerview);

        setOnAddButtonListener();

    }

    private void setOnAddButtonListener() {
        addMealsButton.setOnClickListener(v -> {
            // pass user id, mealplan index(or which day it is)
//            intent.putExtra();
            startActivity(intent);

        });
    }
}