package com.example.happymeals.recipe;

/**
 * This interface is used in the {@link EditRecipe} class and the {@link RecipeIngredientAdapter}.
 * Basically, this interface lets me invoke an item click listener in the {@link RecipeIngredientAdapter}
 * adapter and define the item click implementation in the {@link EditRecipe} class.
 * This interface is also used in the {@link RecipeCommentsAdapter} for the same reasons outlined above.
 * @author John Yu
 */
public interface RecyclerViewInterface {
    void onItemClick(int position, String op);
}
