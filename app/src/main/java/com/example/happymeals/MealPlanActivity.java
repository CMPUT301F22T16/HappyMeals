package com.example.happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.example.happymeals.databinding.ActivityMealPlanBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MealPlanActivity extends AppCompatActivity {

    ActivityMealPlanBinding activityMealPlanBinding;

    RecyclerView.Adapter mpAdapter;
    ArrayList<MealPlan> mealPlans;
    Button new_mp_button;
    ListView meal_plan_list;
    Intent intent_mpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        new_mp_button = findViewById(R.id.meal_plan_add_button);
        meal_plan_list = findViewById(R.id.mp_recyclerview);

        mpAdapter = new MPListAdapter(this, mealPlans);
        activityMealPlanBinding.mpRecyclerview.setLayoutManager(new GridLayoutManager(this, 1));
        activityMealPlanBinding.mpRecyclerview.setAdapter(mpAdapter);


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
        itemTouchHelper.attachToRecyclerView(activityMealPlanBinding.);
    }

    public void setOnAddButtonListener() {
        new_mp_button.setOnClickListener(v -> {
            intent_mpl = new Intent(this, MealPlanMealListActivity.class);
            startActivity(intent_mpl);
        });
    }
}