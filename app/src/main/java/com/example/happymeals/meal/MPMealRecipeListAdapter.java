package com.example.happymeals.meal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.R;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.ViewRecipeActivity;
import com.example.happymeals.databinding.ActivityMpmealRecipeListBinding;
import com.example.happymeals.databinding.MealRecipeListContentBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This custom adapter keeps meal recipe list up to date
 * It is also responsible for reacting to item click
 * within the list
 */
public class MPMealRecipeListAdapter extends RecyclerView.Adapter<MPMealRecipeListAdapter.MRLViewHolder>{
    private Meal meal;
    private ActivityMpmealRecipeListBinding activityMpmealRecipeListBinding;
    private Context mContext;
    private Intent intent;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int itemPosition = activityMpmealRecipeListBinding.mpRecipeListRecyclerview.getChildLayoutPosition(view);
            showBottomSheetDialogOnRecipe(itemPosition);
        }
    };

    /**
     * Constructor of MPMEalRecipeListAdapter
     * @param context the context. In this case it would be the {@link MPMealRecipeList} activity.
     * @param meal
     */
    public MPMealRecipeListAdapter(Context context, Meal meal) {
        this.meal = meal;
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
        Recipe recipe =this.meal.getRecipes().get(position);
        holder.binding.mpMealRecipeListContentTitle.setText(recipe.getTitle());
        Double scale = 1.0;
        try {
            scale = this.meal.getScalingForRecipe(recipe);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.binding.mpMealRecipeListContentScale.setText("Scale: "+scale.toString());
    }

    /**
     * Update the recipes list and corresponding scales list
     * @param recipes a list of {@link Recipe}
     */
    public void updateRecipesList(ArrayList<Recipe> recipes){
        Map<String, Double> scalings_buffer = new HashMap<>();
        ArrayList<Recipe> new_recipes = new ArrayList<>();
        Map<String, Double> scalings = this.meal.getScalings();
        for(Recipe r : recipes){
            double scale = (scalings.get(r.get_r_id())!=null)?scalings.get(r.get_r_id()): -1.0;
            if (scale>=0){
                scalings_buffer.put(r.get_r_id(),scale);
            } else {
                new_recipes.add(r); // add all new recipes
            }
        }

        for (Recipe new_r: new_recipes){
            try {
                meal.addRecipe(new_r);
                meal.setScalingForRecipe(new_r,1.0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        notifyDataSetChanged();
    }

    /**
     * delete the recipe and its scaling from the list
     * @param index the index of the recipe to be deleted
     */
    public void delete(int index){
        Recipe recipe = meal.getRecipes().get(index);
        this.meal.removeScalingForRecipe(recipe);
        this.meal.removeRecipe(recipe);
        notifyDataSetChanged();
    }


    /**
     * Add a new recipe to list
     * andd also add a default scaling for it
     * @param recipe new {@link Recipe} to be added to the list
     */
    public void add(Recipe recipe) {
        this.meal.addRecipe(recipe);
        try {
            this.meal.setScalingForRecipe(recipe,1.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the total counts
     * @return return a {@link int} which is the size
     * of the recipes list
     */
    @Override
    public int getItemCount() {
        return meal.getRecipes().size();
    }

    /**
     * get the updated meal
     * @return a updated {@link Meal}
     */
    public Meal getMeal() {
        return this.meal;
    }

    /**
     * Creates a popup tha allows user to update scaling for a recipe
     * @param recipe the new recipe of type {@link Recipe} we are displaying
     * @param meal the meal of type {@link Meal} we will be updating
     * Knowledge from Sumit Saxena's anwser(Apr 22, 2016) to
     * https://stackoverflow.com/questions/22655599/alertdialog-builder-with-custom-layout-and-edittext-cannot-access-view
     */
    private EditText createPopup(Recipe recipe,Meal meal) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        final View popupView = LayoutInflater.from(mContext).inflate(R.layout.adjust_scaling_popup, null);
        dialogBuilder.setView((popupView));
        dialogBuilder.setTitle(recipe.getTitle());
        EditText editText = popupView.findViewById(R.id.editTextScalingNumber);
        dialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double new_scaling = Double.parseDouble(editText.getText().toString());
                        try {
                            meal.setScalingForRecipe(recipe,new_scaling);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        notifyDataSetChanged();
                    }
                }
        );
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogBuilder.create();
        dialogBuilder.show();
        notifyDataSetChanged();
        return editText;
    }

    /**
     * Create a new bottom sheet dialog and show it.
     * @param itemPosition the position where the recipe is clicked
     */
    private void showBottomSheetDialogOnRecipe(int itemPosition) {
        final BottomSheetDialog bottomSheet = new BottomSheetDialog(mContext);
        bottomSheet.setContentView(R.layout.meal_recipe_list_bottom_sheet);

        TextView view_recipe = bottomSheet.findViewById(R.id.bottom_sheet_textview1);
        view_recipe.setText("View Details of Recipe");
        TextView scale_recipe = bottomSheet.findViewById(R.id.bottom_sheet_textview2);
        scale_recipe.setText("Adjust Scaling for Recipe");
        TextView cancel = bottomSheet.findViewById(R.id.bottom_sheet_cancel);
        Recipe recipe = meal.getRecipes().get(itemPosition);
        view_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(mContext, ViewRecipeActivity.class);
                Double s = 1.0;
                try {
                    s = meal.getScalingForRecipe(recipe);
                } catch (Exception e) {
                }
                Bundle bundle = new Bundle();
                bundle.putDouble("SCALE",s);
                bundle.putSerializable("RECIPE", recipe);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                bottomSheet.dismiss();
            }
        });

        scale_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // build another dialog here
                EditText editText =createPopup(recipe,meal);
                notifyDataSetChanged();
                Double s = 1.0;
                try {
                    s = meal.getScalingForRecipe(recipe);
                } catch (Exception e) {
                }
                editText.setText(s.toString());
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
        notifyDataSetChanged();
    }

    public class MRLViewHolder extends RecyclerView.ViewHolder {

        MealRecipeListContentBinding binding;

        public MRLViewHolder(MealRecipeListContentBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
