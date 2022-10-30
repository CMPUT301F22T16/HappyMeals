package com.example.happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.happymeals.databinding.ActivityMyMealsBinding;

import java.util.ArrayList;

public class MyMealsActivity extends AppCompatActivity {

    ActivityMyMealsBinding activityMyMealsBinding;
    RecyclerView.Adapter myMealsAdapter;
    ArrayList<Meal> meals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meals);

        myMealsAdapter = new MyMealsAdapter(this, meals);
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
                // delete mealPlan
            }
        });
        itemTouchHelper.attachToRecyclerView(activityMyMealsBinding.myMealsRecyclerview);
    }
}