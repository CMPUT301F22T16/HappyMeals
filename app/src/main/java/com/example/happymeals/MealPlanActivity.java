package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MealPlanActivity extends AppCompatActivity {
    ArrayList<MealPlan> mealPlans;
    Button new_mp_button;
    ListView meal_plan_list;
    Intent intent_mpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        new_mp_button = findViewById(R.id.meal_plan_add_button);
        meal_plan_list = findViewById(R.id.meal_plan_listView);
    }

    public void setOnAddButtonListener() {
        new_mp_button.setOnClickListener(v -> {
            intent_mpl = new Intent(this, MealPlanListActivity.class);
            startActivity(intent_mpl);
        });
    }
}