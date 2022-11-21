package com.example.happymeals.meal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.DBHandler;
import com.example.happymeals.R;
import com.example.happymeals.Recipe;
import com.example.happymeals.ViewRecipeActivity;
import com.example.happymeals.databinding.ActivityMpmealRecipeListBinding;
import com.example.happymeals.databinding.MealRecipeListContentBinding;
import com.example.happymeals.recipe.EditRecipe;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

/**
 * This custom adapter keeps meal recipe list up to date
 * It is also responsible for reacting to item click
 * within the list
 */
public class MPMealRecipeListAdapter extends RecyclerView.Adapter<MPMealRecipeListAdapter.MRLViewHolder>{
    private ArrayList<Recipe> recipes;
    private ActivityMpmealRecipeListBinding activityMpmealRecipeListBinding;
    private Context mContext;
    private DBHandler db;
    private Intent intent;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int itemPosition = activityMpmealRecipeListBinding.mpRecipeListRecyclerview.getChildLayoutPosition(view);
            showBottomSheetDialogOnRecipe(itemPosition);
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

    public ArrayList<Recipe> getRecipes(){
        return recipes;
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

    private void showBottomSheetDialogOnRecipe(int itemPosition) {
        final BottomSheetDialog bottomSheet = new BottomSheetDialog(mContext);
        bottomSheet.setContentView(R.layout.meal_recipe_list_bottom_sheet);

        TextView view_recipe = bottomSheet.findViewById(R.id.bottom_sheet_textview1);
        view_recipe.setText("View Details of Recipe");
        TextView scale_recipe = bottomSheet.findViewById(R.id.bottom_sheet_textview2);
        scale_recipe.setText("Adjust Scaling for Recipe");
        TextView cancel = bottomSheet.findViewById(R.id.bottom_sheet_cancel);
        view_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(mContext, ViewRecipeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("RECIPE", recipes.get(itemPosition));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                bottomSheet.dismiss();
            }
        });

        scale_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // build another dialog here
                bottomSheet.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismiss();
            }
        });
        bottomSheet.show();

    }

    public class MRLViewHolder extends RecyclerView.ViewHolder {

        MealRecipeListContentBinding binding;

        public MRLViewHolder(MealRecipeListContentBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
