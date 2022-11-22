package com.example.happymeals.shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.databinding.ActivitySlselectMealPlanBinding;
import com.example.happymeals.databinding.MealPlanListContentBinding;
import com.example.happymeals.mealplan.MPMealListActivity;
import com.example.happymeals.mealplan.MealPlan;

import java.util.ArrayList;

public class SLMealPlanAdapter extends RecyclerView.Adapter<SLMealPlanAdapter.SLMealPlanViewHolder> {

    private ArrayList<MealPlan> mealPlans;
    private Context mContext;
    private ActivitySlselectMealPlanBinding activitySlselectMealPlanBinding;
    private Intent intent;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = activitySlselectMealPlanBinding.slSelectMpList.getChildLayoutPosition(v);
            MealPlan mealPlan = mealPlans.get(index);
//            intent = new Intent(mContext, MPMealListActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("USER", userName);
//            bundle.putSerializable("IsNewMP", false);
//            bundle.putSerializable("MEALPLAN", mealPlan);
//            intent.putExtras(bundle);
//            mContext.startActivity(intent);
        }
    };

    @NonNull
    @Override
    public SLMealPlanAdapter.SLMealPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SLMealPlanAdapter.SLMealPlanViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SLMealPlanViewHolder extends RecyclerView.ViewHolder {

        MealPlanListContentBinding binding;

        public SLMealPlanViewHolder(MealPlanListContentBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
