package com.example.happymeals;


/**
 * This interface lets me invoke an item click listener in the {@link RecipeListAdapter}
 * class and define the item click implementation in the {@link RecipeListActivity} class.
 * @author John Yu
 */
public interface RecipeListInterface {
    void onItemClick(int position, String op);
}
