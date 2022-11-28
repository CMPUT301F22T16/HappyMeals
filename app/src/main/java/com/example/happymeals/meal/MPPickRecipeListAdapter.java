package com.example.happymeals.meal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.happymeals.R;
import com.example.happymeals.recipe.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Custom adapter for recipe lists
 * It keeps the pick recipe list up to date
 */
public class MPPickRecipeListAdapter extends BaseAdapter {
    private ArrayList<Recipe> recipes;
    private ArrayList<Recipe> arraylist;
    private ArrayList<Recipe> recipes_buffer;
    private Context context;
    LayoutInflater inflater;


    /**
     * This is the constructor for MPPickPrecipeListAdapter
     * @param context
     * @param recipes existing recipes for the meal
     */
    public MPPickRecipeListAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes_buffer = new ArrayList<Recipe>(recipes);
        this.recipes = new ArrayList<>();
        this.arraylist = new ArrayList<Recipe>(recipes);
        inflater = LayoutInflater.from(this.context);
    }

    /**
     * Return the recipe size
     * @return {@link int}
     */
    @Override
    public int getCount() {
        return recipes.size();
    }

    /**
     *
     * @return all recipes for this meal
     * include the existing ones, and also
     * the new ones user just selected
     */
    public ArrayList<Recipe> getAllRecipes(){
        return recipes_buffer;
    }


    /**
     * Clear recipes list
     */
    public void clear(){recipes.clear(); arraylist.clear();}

    /**
     * add a new recipe to list
     * @param recipe a new {@link Recipe} to be added
     */
    public void add(Recipe recipe){recipes.add(recipe); arraylist.add(recipe);}

    /**
     * Return all the selected recipes
     * @return a list of {@link Recipe} that was selected by user
     */
    public List<Recipe> getRecipesSelected() { return this.recipes_buffer;}

    /**
     * Add a recipe to buffer
     * @param position the position of the recipe in user user
     *                 recipes list
     */
    public void addToBuffer(int position){
        recipes_buffer.add(recipes.get(position));
    }

    /**
     * remove a recipe from buffer
     * @param position the index of the recipe to be removed
     *                 in the buffter
     */
    public void removeFromBuffer(int position){
        Recipe r = recipes.get(position);
        int i = 0;
        for (Recipe recipe : recipes_buffer){
            if(recipe.getRId().equals(r.getRId())){
                break;
            }
            i++;
        }
        recipes_buffer.remove(i);
    }

    /**
     * get recipe by position
     * @param position the index of the recipe in the recipes list
     * @return a recipe of type {@link Recipe}
     */
    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    /**
     * get the item id
     * @param position the index
     * @return index
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @param position The position of the item within the adapter's data set of the item whose view we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view is non-null and of an appropriate type before using. If it is not possible to convert this view to display the correct data, this method can create a new view. Heterogeneous lists can specify their number of view types, so that this View is always of the right type (see getViewTypeCount() and getItemViewType(int)).
     * @param parent The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Recipe recipe = recipes.get(position);
        if (v == null) {
            v = inflater.inflate(R.layout.pick_recipe_list_content,null);
        }
        // Lookup view for data population
        TextView recipeText = v.findViewById(R.id.ml_recipe_list_textView);
        CheckBox checkBox = v.findViewById(R.id.checkBox);
        if (isAlreadyInBuffer(recipe)){
            if (!checkBox.isChecked()){
                checkBox.toggle();
            }
        } else{
            if(checkBox.isChecked()){
                checkBox.toggle();
            }
        }

        // Populate the data into the template view using the data object
        recipeText.setText(recipe.getTitle());
        return v;
    }

    /**
     * Determines if recipe already exist in the meal's
     * recipe list
     * @param r
     * @return ture if this recipe is already added to the meal
     * false otherwise
     */
    public boolean isAlreadyInBuffer(Recipe r){
        for (Recipe recipe :recipes_buffer) {
            String a = recipe.getRId();
            String b = r.getRId();
            if (a.equals(b)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Filter recipe list based on user inputs to search bar
     * got inspiration from https://abhiandroid.com/ui/searchview
     */
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        recipes.clear();
        if (charText.length() == 0) {
            recipes.addAll(arraylist);
        } else {
            for (Recipe r : arraylist) {
                if (r.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    recipes.add(r);
                }
            }
        }
        notifyDataSetChanged();
    }

}
