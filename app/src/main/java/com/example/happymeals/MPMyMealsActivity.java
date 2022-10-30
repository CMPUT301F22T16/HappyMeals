package com.example.happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.happymeals.databinding.ActivityMyMealsBinding;

import java.util.ArrayList;

public class MPMyMealsActivity extends AppCompatActivity {

    ActivityMyMealsBinding activityMyMealsBinding;
    RecyclerView.Adapter myMealsAdapter;
    ArrayList<Meal> meals;
    Button cancel_button;
    Button finish_button;
    Button add_button;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meals);

        cancel_button = findViewById(R.id.my_meals_cancel);
        finish_button = findViewById(R.id.my_meals_finish);
        add_button = findViewById(R.id.my_meals_add_button);
        intent = new Intent(this,MPMealRecipeList.class);

        myMealsAdapter = new MPMyMealsAdapter(this, meals);
        activityMyMealsBinding.myMealsRecyclerview.setLayoutManager(new GridLayoutManager(this, 1));
        activityMyMealsBinding.myMealsRecyclerview.setAdapter(myMealsAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final Meal meal = meals.get(viewHolder.getAdapterPosition());
                // TODO: delete mealPlan
            }
        });
        itemTouchHelper.attachToRecyclerView(activityMyMealsBinding.myMealsRecyclerview);

        setOnCancelButtonListener();
        setOnFinishButtonListener();
        setOnAddButtonListener();
    }
    private void setOnCancelButtonListener() {
        cancel_button.setOnClickListener(v -> {
            // maybe pop up that all changes will not be saved. sure to exist? pop up alert
            finish();
        });
    }
    private void setOnAddButtonListener() {
        add_button.setOnClickListener(v -> {
            // TODO: pass user id, indexed meal
            startActivity(intent);

        });
    }
    private void setOnFinishButtonListener() {
        finish_button.setOnClickListener(v -> {
            // TODO:update the meal plan at the passed in index day
            finish();

        });
    }
}