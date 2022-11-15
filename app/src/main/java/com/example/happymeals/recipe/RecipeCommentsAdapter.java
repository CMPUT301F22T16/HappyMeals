package com.example.happymeals.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.R;

import java.util.ArrayList;

public class RecipeCommentsAdapter extends RecyclerView.Adapter<RecipeCommentsAdapter.MyViewHolder> {
    /**
     * This interface is here because the click listeners for the user to edit and delete a comment are
     * invoked in this class while their implementation is defined in the {@link EditRecipe} class.
     */
    private RecyclerViewInterface recyclerViewInterface;

    /**
     * This stores the context.
     */
    private Context context;

    /**
     * This is ArrayList of strings which are the comments for this recipe.
     */
    private ArrayList<String> comments;

    /**
     * Constructor
     * @param context the context. In this case it would be the {@link EditRecipe} activity.
     * @param comments an arraylist of string which represent the comments for this recipe.
     * @param recyclerViewInterface the {@link RecyclerViewInterface} interface.
     */
    public RecipeCommentsAdapter(Context context, ArrayList<String> comments, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public RecipeCommentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        /*
            Yes, I am reusing the custom layout that displays the recipe ingredients.
         */
        View view = inflater.inflate(R.layout.add_recipe_custom_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeCommentsAdapter.MyViewHolder holder, int position) {
        holder.desc.setText(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return this.comments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        /**
         * This TextView displays the comment
         */
        TextView desc;

        /**
         * This ImageButton allows the user to delete the comment
         */
        ImageButton deleteBtn;

        /**
         * This ImageButton allows the user to edit the comment
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

                        recyclerViewInterface.onItemClick(getAdapterPosition(), "delete_comment");
                    }
                }
            });

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

                        recyclerViewInterface.onItemClick(getAdapterPosition(), "edit_comment");
                    }
                }
            });
        }
    }
}
