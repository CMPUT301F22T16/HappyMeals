package com.example.happymeals.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.Ingredient;
import com.example.happymeals.R;

import java.util.ArrayList;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private ArrayList<Ingredient> ingredients;

    public RecipeIngredientAdapter(Context context, ArrayList<Ingredient> ingredients, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.ingredients = ingredients;
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
        holder.desc.setText(ingredients.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView desc;
        ImageButton deleteBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.recipe_ingredient_desc);
            deleteBtn = itemView.findViewById(R.id.delete_recipe_ingredient_btn);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

                        recyclerViewInterface.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}

