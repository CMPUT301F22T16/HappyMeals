package com.example.happymeals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.databinding.MealRecipeListContentBinding;

import java.util.ArrayList;


public class MPMealRecipeListAdapter extends RecyclerView.Adapter<MPMealRecipeListAdapter.MRLViewHolder>{
    private ArrayList<Recipe> recipes;

    public MPMealRecipeListAdapter(Context context, ArrayList<Recipe> recipes) {this.recipes = recipes;}
    @NonNull
    @Override
    public MPMealRecipeListAdapter.MRLViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MPMealRecipeListAdapter.MRLViewHolder(MealRecipeListContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MPMealRecipeListAdapter.MRLViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.binding.mpMealRecipeListContentTitle.setText(recipe.getname());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MRLViewHolder extends RecyclerView.ViewHolder {

        MealRecipeListContentBinding binding;

        public MRLViewHolder(MealRecipeListContentBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
