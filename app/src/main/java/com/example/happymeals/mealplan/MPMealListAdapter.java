package com.example.happymeals.mealplan;

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
import com.example.happymeals.meal.MPMealRecipeList;
import com.example.happymeals.meal.MPMyMealsActivity;
import com.example.happymeals.meal.Meal;

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
            mealPlan.clearMealsWithDay(dayIndex);
            mealPlan.loadMealsWithDay(dayIndex, meals);
            intent = new Intent(mContext, MPMyMealsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("MEALPLAN", mealPlan);
            bundle.putSerializable("Is-From-MealPlan",true);
            bundle.putSerializable("MEAL", itemPosition);
            bundle.putSerializable("DAY", dayIndex);
            bundle.putSerializable("USER", userName);
            intent.putExtras(bundle);
            activityLauncher.launch(intent);
        }
    };

    private final View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            intent = new Intent(mContext, MPMealRecipeList.class);
            Bundle bundle = new Bundle();
            int itemPosition = activityMpmealListBinding.mpMealListRecyclerview.getChildLayoutPosition(v);
            bundle.putSerializable("IsNewMeal", false);
            Meal meal = mealPlan.getMeals().get(dayIndex).get(itemPosition);
            bundle.putSerializable("MEAL", meal);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
            return false;
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
        binding.getRoot().setOnLongClickListener(onLongClickListener);
        return new MPMealListAdapter.MealViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MPMealListAdapter.MealViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.binding.mpMealListTextView1.setText(meal.getTitle());
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
