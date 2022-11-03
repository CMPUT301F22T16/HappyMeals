package com.example.happymeals;

import static java.sql.DriverManager.println;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.databinding.ActivityMpmyMealsBinding;
import com.example.happymeals.databinding.MealPlanListContentBinding;

import java.util.ArrayList;

public class MPMyMealsAdapter extends RecyclerView.Adapter<MPMyMealsAdapter.MyMealViewHolder> {

    private ArrayList<Meal> meals;
    private Intent intent;
    private Context mContext;
    private ActivityMpmyMealsBinding activityMpmyMealsBinding;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = activityMpmyMealsBinding.myMealsRecyclerview.getChildLayoutPosition(v);
            intent = new Intent(mContext,MPMealRecipeList.class);
            intent.putExtra("Meal-Index",index);
            mContext.startActivity(intent);
        }
    };

    public MPMyMealsAdapter(Context context, ArrayList<Meal> meals) {
        this.meals = meals;
        activityMpmyMealsBinding = ActivityMpmyMealsBinding.inflate(LayoutInflater.from(context));
        mContext = context;
    }

    @NonNull
    @Override
    public MyMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MealPlanListContentBinding binding = MealPlanListContentBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        binding.getRoot().setOnClickListener(mOnClickListener);
        return new MyMealViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMealViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.binding.mpMealListTextView1.setText("meal name here");
        holder.binding.mpMealListTextView2.setText("#of recipes here");
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    /**
     * clear meals
     */
    public void clear() {
        meals.clear();
    }

    /**
     * adds a meal to meals
     * @param meal
     */
    public void add(Meal meal) {
        meals.add(meal);
    }

    public class MyMealViewHolder extends RecyclerView.ViewHolder {

        MealPlanListContentBinding binding;

        public MyMealViewHolder(MealPlanListContentBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
