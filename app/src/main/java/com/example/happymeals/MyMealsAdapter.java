package com.example.happymeals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.databinding.ActivityMyMealsBinding;
import com.example.happymeals.databinding.MealPlanListContentBinding;

import java.util.ArrayList;

public class MyMealsAdapter extends RecyclerView.Adapter<MyMealsAdapter.MyMealViewHolder> {

    private ArrayList<Meal> meals;

    public MyMealsAdapter(Context context, ArrayList<Meal> meals) { this.meals = meals;}

    @NonNull
    @Override
    public MyMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyMealsAdapter.MyMealViewHolder(MealPlanListContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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

    public class MyMealViewHolder extends RecyclerView.ViewHolder {

        MealPlanListContentBinding binding;

        public MyMealViewHolder(MealPlanListContentBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
