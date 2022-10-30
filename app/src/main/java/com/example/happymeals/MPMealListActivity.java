package com.example.happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.happymeals.databinding.ActivityMealPlanMealListBinding;

public class MPMealListActivity extends AppCompatActivity {

    ActivityMealPlanMealListBinding activityMealPlanMealListBinding;
    RecyclerView.Adapter mpMealListAdapter;
    ArrayList<Meal> meals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_meal_list);

        mpMealListAdapter = new MPMealListAdapter(this, meals);
        activityMealPlanMealListBinding.mpMealListRecyclerview.setLayoutManager(new GridLayoutManager(this, 1));
        activityMealPlanMealListBinding.mpMealListRecyclerview.setAdapter(mpMealListAdapter);

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
        itemTouchHelper.attachToRecyclerView(activityMealPlanMealListBinding.mpMealListRecyclerview);

    }
}