package com.example.happymeals;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int itemPosition = activityMpmealListBinding.mpMealListRecyclerview.getChildLayoutPosition(v);
            // TODO: start new activity
        }
    };

    public MPMealListAdapter(Context context, ArrayList<Meal> meals) {
        this.meals = meals;
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
        holder.binding.mpMealListTextView1.setText("meal name here");
        holder.binding.mpMealListTextView2.setText("#of recipes here");
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
