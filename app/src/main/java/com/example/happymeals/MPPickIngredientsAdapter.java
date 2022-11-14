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
 * It keeps the pick recipe list up to date
 */
public class MPPickIngredientsAdapter extends MPPickAdapter {
    private ArrayList<Ingredient> displayed_ingredients;
    private ArrayList<Ingredient> all_ingredients;
    private ArrayList<Ingredient> ingredients_buffer;
    private ArrayList<Recipe> all_recipes;
    private ArrayList<Recipe> recipes_buffer;
    private Context context;
    LayoutInflater inflater;


    /**
     * This is the constructor for MPPickPrecipeListAdapter
     * @param context
     * @param ingredients existing recipes for the meal
     */
    public MPPickIngredientsAdapter(Context context, ArrayList<Ingredient> ingredients) {
        this.context = context;
        this.ingredients_buffer = new ArrayList<Ingredient>(ingredients);
        this.displayed_ingredients = new ArrayList<>();
        this.all_ingredients = new ArrayList<>();
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return displayed_ingredients.size();
    }


    public void clear(){displayed_ingredients.clear(); all_ingredients.clear();}

    public void add(Ingredient ingredient){displayed_ingredients.add(ingredient); all_ingredients.add(ingredient);}

    public List<Ingredient> getIngredientsSelected() { return this.ingredients_buffer;}


    public void addToBuffer(int position){
        ingredients_buffer.add(displayed_ingredients.get(position));
    }

    public ArrayList<Ingredient> getAllIngredients(){
        ArrayList<Ingredient> all_ingredietns = new ArrayList<>();
        // TODO: what do we return here?
        return ingredients_buffer;
    }

    public void removeFromBuffer(int position){
        Ingredient r = displayed_ingredients.get(position);
        int i = 0;
        for (Ingredient ingredient : ingredients_buffer){
            if(ingredient.getId().equals(r.getId())){
                break;
            }
            i++;
        }
        ingredients_buffer.remove(i);
    }

    @Override
    public Object getItem(int position) {
        return displayed_ingredients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Ingredient ingredient = displayed_ingredients.get(position);
        if (v == null) {
            v = inflater.inflate(R.layout.pick_ingredient_list_content,null);
        }
        // Lookup view for data population
        TextView recipeText = v.findViewById(R.id.ml_recipe_list_textView);
        CheckBox checkBox = v.findViewById(R.id.checkBox_pick_ingredients);
        if (isAlreadyInBuffer(ingredient)){
            if (!checkBox.isChecked()){
                checkBox.toggle();
            }
        } else{
            if(checkBox.isChecked()){
                checkBox.toggle();
            }
        }

        // Populate the data into the template view using the data object
        recipeText.setText(ingredient.getDescription());
        return v;
    }

    /**
     * Determines if recipe already exist in the meal's
     * recipe list
     * @param i
     * @return ture if this recipe is already added to the meal
     * false otherwise
     */
    public boolean isAlreadyInBuffer(Ingredient i){
        for (Ingredient ingredient :ingredients_buffer) {
            String a = ingredient.getId();
            String b = i.getId();
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
        displayed_ingredients.clear();
        if (charText.length() == 0) {
            displayed_ingredients.addAll(all_ingredients);
        } else {
            for (Ingredient i : all_ingredients) {
                if (i.getDescription().toLowerCase(Locale.getDefault()).contains(charText)) {
                    displayed_ingredients.add(i);
                }
            }
        }
        notifyDataSetChanged();
    }

}

