package com.example.happymeals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class PickRecipeListAdapter extends BaseAdapter {
    private ArrayList<Recipe> recipes;
    private ArrayList<Recipe> arraylist;
    private Context context;
    LayoutInflater inflater;

    public PickRecipeListAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes = new ArrayList<Recipe>(recipes);
        this.arraylist = new ArrayList<Recipe>(recipes);
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return recipes.size();
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


        // Populate the data into the template view using the data object
        recipeText.setText(recipe.getname());
        return v;
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        recipes.clear();
        if (charText.length() == 0) {
            recipes.addAll(arraylist);
        } else {
            for (Recipe r : arraylist) {
                if (r.getname().toLowerCase(Locale.getDefault()).contains(charText)) {
                    recipes.add(r);
                }
            }
        }
        notifyDataSetChanged();
    }




}
