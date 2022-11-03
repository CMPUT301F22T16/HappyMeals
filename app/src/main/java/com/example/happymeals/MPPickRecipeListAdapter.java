package com.example.happymeals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Custom adapter for recipe lists
 */
public class MPPickRecipeListAdapter extends BaseAdapter {
    private ArrayList<Recipe> recipes;
    private ArrayList<Recipe> arraylist;
    private ArrayList<Recipe> recipes_buffer;
    private Context context;
    LayoutInflater inflater;

    public MPPickRecipeListAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes_buffer = new ArrayList<Recipe>(recipes);
        this.recipes = new ArrayList<Recipe>(recipes);
        this.arraylist = new ArrayList<Recipe>(recipes);
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    public void clear(){recipes.clear(); arraylist.clear();}

    public void add(Recipe recipe){recipes.add(recipe); arraylist.add(recipe);}

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
        if (recipes_buffer.contains(recipes.get(position))){
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
