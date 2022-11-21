package com.example.happymeals.recipe;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.happymeals.R;
import com.example.happymeals.RecipeIngredient;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class creates a dialog fragment that displays the ingredients for the recipe.
 * @author John Yu
 */
public class RecipeViewIngredientsFragment extends DialogFragment implements RecyclerViewInterface {

    /**
     * This is the context
     */
    Context context;

    /**
     * This stores a collection of {@link RecipeIngredient} objects.
     */
    ArrayList<RecipeIngredient> recipe_ingredients_data_list;

    /**
     * This is the recyclerview that displays each ingredient.
     */
    RecyclerView recipe_ingredients_list;

    /**
     * This is the adapter for the recyclerview.
     */
    RecipeIngredientAdapter ingredients_adapter;

    /**
     * Handles the action to take when the user presses OK.
     */
    OnFragmentInteractionListener listener;

    /**
     * Stores the ingredient index position the user selected.
     */
    int selection = -1;

    /**
     * This creates an ActivityResultLauncher where the user can send and receive data to the {@link RecipeEditIngredient} class
     */
    ActivityResultLauncher<Intent> edit_ingredient_for_result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            handleEditIngredientForResultLauncher(result);
        }
    });

    @Override
    public void onItemClick(int position, String op) {
        if (Objects.equals(op, "delete")) {
            this.recipe_ingredients_data_list.remove(position);
            this.recipe_ingredients_list.setAdapter(this.ingredients_adapter);
        } else if (Objects.equals(op, "edit")) {  // Edit Ingredient
            this.selection = position;  // This variable stores the index position of the ingredient being edited
            Intent intent = new Intent(getContext(), RecipeEditIngredient.class);
            RecipeIngredient item = this.recipe_ingredients_data_list.get(position);
            intent.putExtra("desc", item.getDescription());
            intent.putExtra("category", item.getCategory());
            intent.putExtra("amount", item.getAmount());
            edit_ingredient_for_result.launch(intent);
        }
    }

    /**
     * This interface is used to handle the action to take when the user presses ok.
     */
    public interface OnFragmentInteractionListener {
        void onOkPressed_Ingredient(ArrayList<RecipeIngredient> data_list);
    }

    public RecipeViewIngredientsFragment(Context context, ArrayList<RecipeIngredient> data_list) {
        this.context = context;
        this.recipe_ingredients_data_list = data_list;
        this.ingredients_adapter = new RecipeIngredientAdapter(this.context, this.recipe_ingredients_data_list, this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_ingredients_fragment_layout, null);
        recipe_ingredients_list = view.findViewById(R.id.recipe_ingredient_recyclerview);
        recipe_ingredients_list.setLayoutManager(new LinearLayoutManager(getContext()));
        recipe_ingredients_list.setAdapter(this.ingredients_adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("View Ingredients")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onOkPressed_Ingredient(recipe_ingredients_data_list);
                    }
                }).create();
    }

    /**
     * This method handles the return value after the user edits an ingredient
     * @param result the returned value from the {@link RecipeEditIngredient} Activity
     */
    public void handleEditIngredientForResultLauncher(ActivityResult result) {
        if (result != null && result.getResultCode() == RESULT_OK) {
            if (result.getData() == null) return;
            String desc = result.getData().getStringExtra("desc");
            String category = result.getData().getStringExtra("category");
            Double amount = result.getData().getDoubleExtra("amount", 0.00);
            RecipeIngredient item = this.recipe_ingredients_data_list.get(selection);
            item.setDescription(desc);
            item.setCategory(category);
            item.setAmount(amount);
            this.recipe_ingredients_list.setAdapter(this.ingredients_adapter);
        } else {
            Toast.makeText(getContext(), "Failed to edit ingredient", Toast.LENGTH_SHORT).show();
        }
    }
}
