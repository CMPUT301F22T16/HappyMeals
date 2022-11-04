package com.example.happymeals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Custom adapter for recipe lists
 */
public class MPPickRecipeListAdapter extends BaseAdapter {
    private ArrayList<Recipe> recipes;
    private ArrayList<Recipe> arraylist;
    private ArrayList<Recipe> recipes_buffer;
    private ArrayList<Recipe> existing_recipes;
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
        this.existing_recipes = new ArrayList<Recipe>(recipes);
        this.arraylist = new ArrayList<Recipe>(recipes);
        inflater = LayoutInflater.from(this.context);
    }

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
        ArrayList<Recipe> all_recipes = new ArrayList<>();
        for (Recipe r : recipes_buffer) {
            all_recipes.add(r);
        }
        return all_recipes;
    }



    public void clear(){recipes.clear(); arraylist.clear();}

    public void add(Recipe recipe){recipes.add(recipe); arraylist.add(recipe);}

    public List<Recipe> getRecipesSelected() { return this.recipes_buffer;}

    public void addToBuffer(int position){
        recipes_buffer.add(recipes.get(position));
    }

    public void removeFromBuffer(int position){
        Recipe r = recipes.get(position);
        int i = 0;
        for (Recipe recipe : recipes_buffer){
            if(recipe.get_r_id().equals(r.get_r_id())){
                break;
            }
            i++;
        }
        recipes_buffer.remove(i);
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

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
            String a = recipe.get_r_id();
            String b = r.get_r_id();
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
