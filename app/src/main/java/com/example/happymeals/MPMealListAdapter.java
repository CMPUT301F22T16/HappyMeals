package com.example.happymeals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.databinding.MealPlanListContentBinding;

import java.util.ArrayList;

public class MPMealListAdapter extends RecyclerView.Adapter<MPMealListAdapter.MealViewHolder>{

    private ArrayList<Meal> meals;

    public MPMealListAdapter(Context context, ArrayList<Meal> meals) { this.meals = meals;}

    @NonNull
    @Override
    public MPMealListAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MealViewHolder(MealPlanListContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
