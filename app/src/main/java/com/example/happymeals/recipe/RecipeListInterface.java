package com.example.happymeals.recipe;


import com.example.happymeals.recipe.RecipeListActivity;
import com.example.happymeals.recipe.RecipeListAdapter;

/**
 * This interface lets me invoke an item click listener in the {@link RecipeListAdapter}
 * class and define the item click implementation in the {@link RecipeListActivity} class.
 * @author John Yu
 */
public interface RecipeListInterface {
    void onItemClick(int position, String op);
}
