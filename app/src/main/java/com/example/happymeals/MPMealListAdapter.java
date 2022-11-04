package com.example.happymeals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.databinding.ActivityMpmealListBinding;
import com.example.happymeals.databinding.MealPlanListContentBinding;

import java.util.ArrayList;

public class MPMealListAdapter extends RecyclerView.Adapter<MPMealListAdapter.MealViewHolder>{

    private ArrayList<Meal> meals;
    private ActivityMpmealListBinding activityMpmealListBinding;
    private Intent intent;
    private Context mContext;
    private String userName;
    private int dayIndex;
    private MealPlan mealPlan;
    private ActivityResultLauncher<Intent> activityLauncher;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int itemPosition = activityMpmealListBinding.mpMealListRecyclerview.getChildLayoutPosition(v);
            intent = new Intent(mContext, MPMyMealsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("MEALPLAN", mealPlan);
            bundle.putSerializable("MEAL", itemPosition);
            bundle.putSerializable("DAY", dayIndex);
            bundle.putSerializable("USER", userName);
            intent.putExtras(bundle);
//            startActivity(intent);
            activityLauncher.launch(intent);
        }
    };

    public MPMealListAdapter(Context context, ArrayList<Meal> meals, String userName, int dayIndex, MealPlan mealPlan, ActivityResultLauncher activityLauncher) {
        this.userName = userName;
        this.dayIndex = dayIndex;
        this.mealPlan = mealPlan;
        this.meals = meals;
        this.activityLauncher = activityLauncher;
        this.mContext = context;
        activityMpmealListBinding = ActivityMpmealListBinding.inflate(LayoutInflater.from(context));
    }

    @NonNull
    @Override
    public MPMealListAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MealPlanListContentBinding binding = MealPlanListContentBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        binding.getRoot().setOnClickListener(mOnClickListener);
        return new MPMealListAdapter.MealViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MPMealListAdapter.MealViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.binding.mpMealListTextView1.setText("meal name");
        holder.binding.mpMealListTextView2.setText(Double.toString(meal.getCost()));
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public class MealViewHolder extends RecyclerView.ViewHolder {

        MealPlanListContentBinding binding;

        public MealViewHolder(MealPlanListContentBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
