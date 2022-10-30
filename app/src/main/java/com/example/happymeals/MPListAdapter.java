package com.example.happymeals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.databinding.MealPlanListContentBinding;

import java.util.ArrayList;

public class MPListAdapter extends RecyclerView.Adapter<MPListAdapter.MPViewHolder>{

    private ArrayList<MealPlan> mealPlans;

    public MPListAdapter(Context context, ArrayList<MealPlan> mealPlans) { this.mealPlans = mealPlans;}

    @NonNull
    @Override
    public MPViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MPListAdapter.MPViewHolder(MealPlanListContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));;
    }

    @Override
    public void onBindViewHolder(@NonNull MPViewHolder holder, int position) {
        MealPlan mealPlan = mealPlans.get(position);
        holder.binding.mpMealListTextView1.setText("mealplan name here");
        holder.binding.mpMealListTextView2.setText("#of days here");
    }

    @Override
    public int getItemCount() {
        return mealPlans.size();
    }

    public class MPViewHolder extends RecyclerView.ViewHolder {

        MealPlanListContentBinding binding;

        public MPViewHolder(MealPlanListContentBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
