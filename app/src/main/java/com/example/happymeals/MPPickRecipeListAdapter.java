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
    private double meal_cost;
    private Context context;
    private String m_id;
    LayoutInflater inflater;
    private List<Double> meal_scalings;


    public List<Double> getMeal_scalings() {
        return meal_scalings;
    }

    public void setMeal_scalings(List<Double> meal_scalings) {
        this.meal_scalings = meal_scalings;
    }


    public double getMeal_cost() {
        return meal_cost;
    }

    public void setMeal_cost(double meal_cost) {
        this.meal_cost = meal_cost;
    }



    public MPPickRecipeListAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes_buffer = new ArrayList<Recipe>(recipes);
        this.recipes = new ArrayList<Recipe>(recipes);
        this.existing_recipes = new ArrayList<>();
        this.meal_scalings = new ArrayList<Double>();
        this.m_id = "";
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
        for (Recipe r :existing_recipes){
            all_recipes.add(r);
        }
        for (Recipe r : recipes_buffer) {
            all_recipes.add(r);
        }
        return all_recipes;
    }


    public String getMid(){
        return this.m_id;
    }

    public void setMid(String m_id){
        this.m_id = m_id;

    }

    public void clear(){recipes.clear(); arraylist.clear();}

    public void add(Recipe recipe){recipes.add(recipe); arraylist.add(recipe);}

    public void clearExistingRecipes(){existing_recipes.clear();}

    public void addToExistingRecipes(Recipe recipe){existing_recipes.add(recipe);}

    public void addToBuffer(int position){
        recipes_buffer.add(recipes.get(position));
    }

    public void removeFromBuffer(int position){
        recipes_buffer.remove(recipes.get(position));
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
        if (recipes_buffer.contains(recipes.get(position)) || existing_recipes.contains(recipes.get(position))){
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
