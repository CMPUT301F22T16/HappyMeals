package com.example.happymeals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    private DBHandler db;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int itemPosition = activityMpmealRecipeListBinding.mpRecipeListRecyclerview.getChildLayoutPosition(view);
            // TODO: should start activity: View Recipe
//            intent = new Intent(mContext,MPPickRecipeActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("RECIPE", recipes.get(itemPosition));
//            intent.putExtras(bundle);
//            mContext.startActivity(intent);

        }
    };

    // indices needs to be added into the arguments.
    public MPMealRecipeListAdapter(Context context, ArrayList<Recipe> recipes,DBHandler db) {
        this.db = db;
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

    public void setRecipesList(ArrayList<Recipe> recipes){
        this.recipes = recipes;
    }

    public void delete(int index){
        recipes.remove(index);
        notifyDataSetChanged();
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
