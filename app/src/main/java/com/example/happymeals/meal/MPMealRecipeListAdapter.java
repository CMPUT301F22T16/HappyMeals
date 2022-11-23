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
import com.example.happymeals.Recipe;
import com.example.happymeals.ViewRecipeActivity;
import com.example.happymeals.databinding.ActivityMpmealRecipeListBinding;
import com.example.happymeals.databinding.MealRecipeListContentBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * This custom adapter keeps meal recipe list up to date
 * It is also responsible for reacting to item click
 * within the list
 */
public class MPMealRecipeListAdapter extends RecyclerView.Adapter<MPMealRecipeListAdapter.MRLViewHolder>{
    private List<Recipe> recipes;
    private List<Double> scalings;
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
        this.recipes = meal.getRecipes();
        this.scalings = meal.getScalings();

        if(recipes.size() > scalings.size()){
            for (int i = scalings.size();i<recipes.size();i++){
                scalings.add(1.0); // default scale is 1
            }
        }
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
        Recipe recipe = recipes.get(position);
        holder.binding.mpMealRecipeListContentTitle.setText(recipe.getTitle());

    }

    /**
     * Update the recipes list and corresponding scales list
     * @param recipes a list of {@link Recipe}
     */
    public void updateRecipesList(ArrayList<Recipe> recipes){
        int old_recipe_size = this.recipes.size();
        int new_recipe_size = recipes.size();
        int max_size = new_recipe_size;
        int min_size = old_recipe_size;

        // update scalings
        if (new_recipe_size < old_recipe_size){max_size = old_recipe_size; min_size = new_recipe_size;}
        int i = 0;
        for (; i < min_size;i++){
            Recipe new_recipe = recipes.get(i);
            Recipe old_recipe = this.recipes.get(i);
            if (!(new_recipe.get_r_id().equals(old_recipe.get_r_id()))){
                scalings.set(i,1.0);
            }
        }
        if (new_recipe_size < old_recipe_size){
            for (int j = max_size-1;j>min_size;i--){
                scalings.remove(j);
            }
        }else {
            for (;i<max_size;i++){
                scalings.add(1.0);
            }
        }
        // update recipe
        this.recipes = recipes;
    }

    public void delete(int index){
        recipes.remove(index);
        scalings.remove(index);
        notifyDataSetChanged();
    }

    /**
     * clear recipes and scalings list
     */
    public void clear(){
        recipes.clear();
        scalings.clear();
    }

    /**
     * Add a new recipe to list
     * andd also add a default scaling for it
     * @param recipe new {@link Recipe} to be added to the list
     */
    public void add(Recipe recipe){
        recipes.add(recipe);
        scalings.add(1.0);
    }

    /**
     * Get the total counts
     * @return return a {@link int} which is the size
     * of the recipes list
     */
    @Override
    public int getItemCount() {
        assert recipes.size() == scalings.size();
        return recipes.size();
    }

    /**
     * get the updated meal
     * @return a updated {@link Meal}
     */
    public Meal getMeal() {
        this.meal.setRecipes(this.recipes);
        this.meal.setScalings(this.scalings);
        return this.meal;
    }

    /**
     * Creates a popup tha allows user to update scaling for a recipe
     * @param title the title of the recipe
     * Knowledge from Sumit Saxena's anwser(Apr 22, 2016) to
     * https://stackoverflow.com/questions/22655599/alertdialog-builder-with-custom-layout-and-edittext-cannot-access-view
     */
    private EditText createPopup(String title,int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        final View popupView = LayoutInflater.from(mContext).inflate(R.layout.adjust_scaling_popup, null);
        dialogBuilder.setView((popupView));
        dialogBuilder.setTitle(title);
        EditText editText = popupView.findViewById(R.id.editTextScalingNumber);
        dialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double new_scaling = Double.parseDouble(editText.getText().toString());
                        scalings.set(position,new_scaling);
//                        Toast.makeText(mContext, (String)editText.getText().toString(),
//                                Toast.LENGTH_LONG).show();
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
                EditText editText =createPopup("Title",itemPosition);
                Double s = scalings.get(itemPosition);
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

    }

    public class MRLViewHolder extends RecyclerView.ViewHolder {

        MealRecipeListContentBinding binding;

        public MRLViewHolder(MealRecipeListContentBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
