package com.example.happymeals;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.databinding.ActivityMpmealRecipeListBinding;
import com.example.happymeals.databinding.MealRecipeListContentBinding;

import java.util.ArrayList;


public class MPMealRecipeListAdapter extends RecyclerView.Adapter<MPMealRecipeListAdapter.MRLViewHolder>{
    private ArrayList<Recipe> recipes;
    private ActivityMpmealRecipeListBinding activityMpmealRecipeListBinding;
    private Context mContext;
    private String m_id ="";
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int itemPosition = activityMpmealRecipeListBinding.mpRecipeListRecyclerview.getChildLayoutPosition(view);
            // TODO: should start activity: View Recipe

            Intent intent = new Intent(mContext,MPPickRecipeActivity.class);
            intent.putExtra("Meal_ID",getMid());
            mContext.startActivity(intent);
        }
    };

    // indices needs to be added into the arguments.
    public MPMealRecipeListAdapter(Context context, ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        this.mContext = context;
        activityMpmealRecipeListBinding = ActivityMpmealRecipeListBinding.inflate(LayoutInflater.from(context));
    }
    @NonNull
    @Override
    public MPMealRecipeListAdapter.MRLViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MealRecipeListContentBinding binding = MealRecipeListContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.getRoot().setOnClickListener(mOnClickListener);
        return new MPMealRecipeListAdapter.MRLViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MPMealRecipeListAdapter.MRLViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.binding.mpMealRecipeListContentTitle.setText(recipe.getTitle());

    }
    public void delete(int index){
        recipes.remove(index);
    }


    public String getMid(){
        return this.m_id;
    }

    public void setMid(String m_id){
        this.m_id = m_id;

    }

    public void clear(){
        recipes.clear();
    }

    public void add(Recipe recipe){
        recipes.add(recipe);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class MRLViewHolder extends RecyclerView.ViewHolder {

        MealRecipeListContentBinding binding;

        public MRLViewHolder(MealRecipeListContentBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
