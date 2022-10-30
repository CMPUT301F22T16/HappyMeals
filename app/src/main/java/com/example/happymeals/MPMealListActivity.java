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
        setContentView(R.layout.activity_mpmeal_list);

        addMealsButton = findViewById(R.id.meal_plan_add_button);
        nextDayButton = findViewById(R.id.mp_fab_next_day);
        finishButton = findViewById(R.id.mp_fab_finish);
        intent = new Intent(this, MPMyMealsActivity.class);

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