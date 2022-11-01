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
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int itemPosition = activityMpmealRecipeListBinding.mpRecipeListRecyclerview.getChildLayoutPosition(view);
            Intent intent = new Intent(mContext,MPPickRecipeActivity.class);
            intent.putExtra("recipe_index",itemPosition);
            mContext.startActivity(intent);

        }
    };

    // indices needs to be added into the arguments.
    public MPMealRecipeListAdapter(Context context, ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        this.mContext = context;
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
