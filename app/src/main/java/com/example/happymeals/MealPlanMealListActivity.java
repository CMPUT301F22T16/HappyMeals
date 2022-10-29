package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.happymeals.databinding.ActivityMealPlanMealListBinding;

public class MealPlanMealListActivity extends AppCompatActivity {

    ActivityMealPlanMealListBinding activityMealPlanMealListBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_meal_list);


    }
}