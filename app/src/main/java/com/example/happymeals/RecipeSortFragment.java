package com.example.happymeals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeSortFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeSortFragment extends DialogFragment {

    private List<Recipe> recipes;
    private RadioGroup sortingOptions;
    private RadioGroup orderOptions;

    public static RecipeSortFragment newInstance(List<Recipe> recipeList, RecipeListAdapter adapter) {
        RecipeSortFragment fragment = new RecipeSortFragment();
        Bundle args = new Bundle();
        args.putSerializable("COLLECTION", (Serializable) recipeList);
        args.putSerializable("ADAPTER", (Serializable) adapter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_fragment_sort, null);


        Bundle bundle = this.getArguments();
        recipes = (List<Recipe>) bundle.getSerializable("COLLECTION");
        RecipeListAdapter adapter = (RecipeListAdapter) bundle.getSerializable("ADAPTER");
        setupView(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        return builder
                .setView(view)
                .setTitle("Sort Recipes")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean isLowtoHigh = orderOptions.getCheckedRadioButtonId() == R.id.ingredient_low_to_high;

                        int option = sortingOptions.getCheckedRadioButtonId();

                        switch(option) {

                            case R.id.price_sort_ingredient:

                                if (isLowtoHigh) {
                                    Collections.sort(recipes, (o1, o2) -> (o1.getPreparation_time() - o2.getPreparation_time()));
                                }

                                else {
                                    Collections.sort(recipes, (o1, o2) -> (o2.getPreparation_time() - o1.getPreparation_time()));
                                }

                                break;

                            case R.id.recipe_servings_radio:

                                if (isLowtoHigh) {
                                    Collections.sort(recipes, (o1, o2) -> (o1.getNum_servings() - o2.getNum_servings()));
                                }

                                else {
                                    Collections.sort(recipes, (o1, o2) -> (o2.getNum_servings() - o1.getNum_servings()));
                                }

                                break;

                            case R.id.recipe_category_radio:

                                if (isLowtoHigh) {
                                    Collections.sort(recipes, (o1, o2) -> (o1.getCategory().toLowerCase().compareTo(o2.getCategory().toLowerCase())));
                                }

                                else {
                                    Collections.sort(recipes, (o1, o2) -> (o2.getCategory().toLowerCase().compareTo(o1.getCategory().toLowerCase())));
                                }

                                break;

                            default:

                                if (isLowtoHigh) {
                                    Collections.sort(recipes, (o1, o2) -> (o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase())));
                                }

                                else {
                                    Collections.sort(recipes, (o1, o2) -> (o2.getTitle().toLowerCase().compareTo(o1.getTitle().toLowerCase())));
                                }

                                break;
                        }

                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();

    }

    private void setupView(View view) {
        sortingOptions = view.findViewById(R.id.sort_recipe_group);
        orderOptions = view.findViewById(R.id.sort_order_group);
    }


}