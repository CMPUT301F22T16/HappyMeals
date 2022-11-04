package com.example.happymeals;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.databinding.ActivityMpmealPlanBinding;
import com.example.happymeals.databinding.MealPlanListContentBinding;

import java.util.ArrayList;

public class MPListAdapter extends RecyclerView.Adapter<MPListAdapter.MPViewHolder> {

    private ArrayList<MealPlan> mealPlans;
    private Context mContext;
    private ActivityMpmealPlanBinding activityMpmealPlanBinding;
    private Intent intent;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = activityMpmealPlanBinding.mpRecyclerview.getChildLayoutPosition(v);
            intent = new Intent(mContext, MPMealListActivity.class);
            intent.putExtra("Meal-Plan-Index", index);
            mContext.startActivity(intent);
        }
    };


    public MPListAdapter(Context context, ArrayList<MealPlan> mealPlans) {
        this.mealPlans = mealPlans;
        mContext = context;
        activityMpmealPlanBinding = ActivityMpmealPlanBinding.inflate(LayoutInflater.from(context));
    }

    @NonNull
    @Override
    public MPViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MealPlanListContentBinding binding = MealPlanListContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.getRoot().setOnClickListener(mOnClickListener);
        return new MPViewHolder(binding);
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

    public void clear() {
        mealPlans.clear();
    }

    public void add(MealPlan mealPlan) {
        mealPlans.add(mealPlan);
    }

    public static class MPViewHolder extends RecyclerView.ViewHolder {

        MealPlanListContentBinding binding;

        public MPViewHolder(MealPlanListContentBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
