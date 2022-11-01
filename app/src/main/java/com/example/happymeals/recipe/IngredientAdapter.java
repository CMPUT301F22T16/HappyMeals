package com.example.happymeals.recipe;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.Ingredient;
import com.example.happymeals.R;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.MyViewHolder>{

    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private ArrayList<Ingredient> ingredients;
    private int selection = -1;

    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredients, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_add_ingredient_custom_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.MyViewHolder holder, int position) {
        holder.desc.setText(ingredients.get(position).getDescription());
        holder.category.setText(ingredients.get(position).getCategory());
        if (selection == position) {
            holder.cardview.setBackgroundColor(context.getResources().getColor(R.color.highlight));
        } else {
            holder.cardview.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView desc;
        TextView category;
        CardView cardview;
        ConstraintLayout rootLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.recipe_add_ingredient_desc);
            category = itemView.findViewById(R.id.recipe_add_ingredient_category);
            cardview = itemView.findViewById(R.id.recipe_add_ingredient_cardview);
            rootLayout = itemView.findViewById(R.id.recipe_add_ingredient_custom_layout_root_constraintlayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

                        notifyItemChanged(selection);
                        selection = getAdapterPosition();
                        notifyItemChanged(selection);

                        recyclerViewInterface.onItemClick(selection, "");
                    }
                }
            });
        }
    }
}
