package com.example.happymeals.shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
            intent = new Intent(mContext, SLShoppingListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("MEALPLAN", mealPlan);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    };

    public SLMealPlanAdapter(Context context, ArrayList<MealPlan> mealPlans) {
        this.mealPlans = mealPlans;
        mContext = context;
        activitySlselectMealPlanBinding = ActivitySlselectMealPlanBinding.inflate(LayoutInflater.from(context));
    }

    @NonNull
    @Override
    public SLMealPlanAdapter.SLMealPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MealPlanListContentBinding binding = MealPlanListContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.getRoot().setOnClickListener(mOnClickListener);
        return new SLMealPlanViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SLMealPlanAdapter.SLMealPlanViewHolder holder, int position) {
        MealPlan mealPlan = mealPlans.get(position);
        holder.binding.mpMealListTextView1.setText(mealPlan.getTitle());
        holder.binding.mpMealListTextView2.setText(Integer.toString(mealPlan.getNum_days()));
    }

    @Override
    public int getItemCount() {
        return mealPlans.size();
    }

    /**
     * clear mealPlans
     */
    public void clear() {
        mealPlans.clear();
    }

    /**
     * adds mealPlan to mealPlans
     * @param mealPlan
     */
    public void add(MealPlan mealPlan) {
        mealPlans.add(mealPlan);
    }

    public class SLMealPlanViewHolder extends RecyclerView.ViewHolder {

        MealPlanListContentBinding binding;

        public SLMealPlanViewHolder(MealPlanListContentBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
