package com.example.happymeals.meal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.DBHandler;
import com.example.happymeals.mealplan.MealPlan;
import com.example.happymeals.databinding.ActivityMpmyMealsBinding;
import com.example.happymeals.databinding.MealPlanListContentBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This is the custom adapter
 * It is responsible to keep the meal list
 * up-to-date
 */
public class MPMyMealsAdapter extends RecyclerView.Adapter<MPMyMealsAdapter.MyMealViewHolder> {

    private List<Meal> meals;
    private Context mContext;
    private Intent intent;
    private ActivityMpmyMealsBinding activityMpmyMealsBinding;
    private DBHandler db;
    private AtomicBoolean isEdit;
    private MealPlan mealPlan;
    private boolean is_from_meal_plan;
    private int dayIndex;
    private int mealIndex;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = activityMpmyMealsBinding.myMealsRecyclerview.getChildLayoutPosition(v);
            Meal meal = meals.get(index);

            if(isEdit.get() ||!is_from_meal_plan) {
                intent = new Intent(mContext, MPMealRecipeList.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("IsNewMeal", false);
                bundle.putSerializable("MEAL", meal);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            } else {
                // TODO: fix this when the data structure is fixed
                intent = new Intent();
                Bundle bundle = new Bundle();
                if(mealIndex==0) {
                    mealPlan.setBreakfastWithIndex(meal, dayIndex);
                } else if(mealIndex == 1) {
                    mealPlan.setLunchWithIndex(meal, dayIndex);
                } else if(mealIndex == 2) {
                    mealPlan.setDinnerWithIndex(meal, dayIndex);
                }
                bundle.putSerializable("M-MEALPLAN", mealPlan);
                intent.putExtras(bundle);
                // https://stackoverflow.com/questions/7951936/how-to-finish-an-activity-from-an-adapter
                ((MPMyMealsActivity)mContext).setResult(Activity.RESULT_OK, intent);
                ((MPMyMealsActivity)mContext).finish();
            }

        }
    };

    /**
     * Constructor for this activity
     * Will be used if activity is started
     * from MPMealList
     * @param context
     * @param meals this is the meal object
     * @param db this is our db controller
     * @param isEdit indicates if this is editing mode
     * @param dayIndex the index of the day in mealPlan
     * @param mealIndex the index of the meal in mealist array,
     *                  which is from MPMealList activity
     * @param mealPlan this is the mealPlan object
     */
    public MPMyMealsAdapter(Context context, ArrayList<Meal> meals, DBHandler db, AtomicBoolean isEdit, int dayIndex, int mealIndex, MealPlan mealPlan) {
        this.meals = meals;
        this.isEdit = isEdit;
        this.dayIndex = dayIndex;
        this.mealIndex = mealIndex;
        this.mealPlan = mealPlan;
        activityMpmyMealsBinding = ActivityMpmyMealsBinding.inflate(LayoutInflater.from(context));
        mContext = context;
        this.db = db;
        this.is_from_meal_plan = true;
    }

    /**
     * Constructor for this activity
     * Will be used if activity is started
     * from MPMealList
     * @param context
     * @param meals this is the meal object
     * @param db this is our db controller
     * @param isEdit inditates if this is editing mode.
     *               Will always be false in this case
     */
    public MPMyMealsAdapter(Context context, ArrayList<Meal> meals, DBHandler db,AtomicBoolean isEdit) {
        this.meals = meals;
        this.isEdit = isEdit;
        activityMpmyMealsBinding = ActivityMpmyMealsBinding.inflate(LayoutInflater.from(context));
        mContext = context;
        this.db = db;
        this.is_from_meal_plan = false;
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
        double meal_cost = meal.getCost();
        holder.binding.mpMealListTextView1.setText("meal cost: "+meal_cost);
        int number_of_recipes = meal.getRecipes().size();
        holder.binding.mpMealListTextView2.setText(Integer.toString(number_of_recipes)+" recipes");
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

    /**
     * remove the meal from meal list
     * @param index
     */
    public void remove(int index) {
        db.removeMeal(meals.get(index));
        meals.remove(index);
        notifyDataSetChanged();
    }


    public class MyMealViewHolder extends RecyclerView.ViewHolder {

        MealPlanListContentBinding binding;

        public MyMealViewHolder(MealPlanListContentBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
