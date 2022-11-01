package com.example.happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.happymeals.databinding.ActivityMpmealPlanBinding;

import java.util.ArrayList;

public class MPMealPlanActivity extends AppCompatActivity {

    ActivityMpmealPlanBinding activityMpmealPlanBinding;

    RecyclerView.Adapter mpAdapter;
    ArrayList<MealPlan> mealPlans;
    Button new_mp_button;
    RecyclerView meal_plan_list;
    Intent intent_mpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMpmealPlanBinding = ActivityMpmealPlanBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_mpmeal_plan);

        new_mp_button = findViewById(R.id.my_meals_add_button);
        meal_plan_list = findViewById(R.id.mp_recyclerview);

        mpAdapter = new MPListAdapter(this, mealPlans);
        activityMpmealPlanBinding.mpRecyclerview.setLayoutManager(new GridLayoutManager(this, 1));
        activityMpmealPlanBinding.mpRecyclerview.setHasFixedSize(true);
        activityMpmealPlanBinding.mpRecyclerview.setAdapter(mpAdapter);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final MealPlan mealPlan = mealPlans.get(viewHolder.getAdapterPosition());
                // delete mealPlan
            }
        });
        itemTouchHelper.attachToRecyclerView(activityMpmealPlanBinding.mpRecyclerview);
        setOnAddButtonListener();
    }

    private void setOnAddButtonListener() {
        new_mp_button.setOnClickListener(v -> {
            intent_mpl = new Intent(this, MPMealListActivity.class);
            startActivity(intent_mpl);
        });
    }


}