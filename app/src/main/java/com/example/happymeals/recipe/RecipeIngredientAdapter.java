package com.example.happymeals.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.Recipe;
import com.example.happymeals.RecipeIngredient;
import com.example.happymeals.UserIngredient;
import com.example.happymeals.R;

import java.util.ArrayList;

/**
 * This class is the RecyclerViewAdapter that displays Ingredients in the {@link EditRecipe} class.
 * @author John Yu
 */
public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.MyViewHolder> {

    /**
     * This interface is here so that I can invoke the OnItemClick method in the {@link RecipeIngredientAdapter} class
     * and define the implementation in the {@link EditRecipe} class.
     */
    private final RecyclerViewInterface recyclerViewInterface;

    /**
     * This stores the context
     */
    private Context context;

    /**
     * This stores an arraylist of ingredients
     */
    private ArrayList<RecipeIngredient> userIngredients;

    public RecipeIngredientAdapter(Context context, ArrayList<RecipeIngredient> userIngredients, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.userIngredients = userIngredients;
    }

    @NonNull
    @Override
    public RecipeIngredientAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.add_recipe_custom_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientAdapter.MyViewHolder holder, int position) {
        holder.desc.setText(userIngredients.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return userIngredients.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        /**
         * This TextView displays the ingredient description
         */
        TextView desc;

        /**
         * This ImageButton allows the user to delete the ingredient
         */
        ImageButton deleteBtn;

        /**
         * This ImageButton allows the user to edit the ingredient
         */
        ImageButton editBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.recipe_ingredient_desc);
            deleteBtn = itemView.findViewById(R.id.delete_recipe_ingredient_btn);
            editBtn = itemView.findViewById(R.id.edit_recipe_ingredient_btn);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

                        recyclerViewInterface.onItemClick(getAdapterPosition(), "delete");
                    }
                }
            });

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

                        recyclerViewInterface.onItemClick(getAdapterPosition(), "edit");
                    }
                }
            });
        }
    }
}

