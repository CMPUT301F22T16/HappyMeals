package com.example.happymeals.ingredient;

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

import com.example.happymeals.R;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientSortFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientSortFragment extends DialogFragment {

    private List<UserIngredient> userIngredients;
    private RadioGroup sortingOptions;
    private RadioGroup orderOptions;

    public static IngredientSortFragment newInstance(List<UserIngredient> ingredientList, IngredientAdaptor adapter) {
        IngredientSortFragment fragment = new IngredientSortFragment();
        Bundle args = new Bundle();
        args.putSerializable("COLLECTION", (Serializable) ingredientList);
        args.putSerializable("ADAPTER", (Serializable) adapter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_fragment_sort, null);


        Bundle bundle = this.getArguments();
        userIngredients = (List<UserIngredient>) bundle.getSerializable("COLLECTION");
        IngredientAdaptor adapter = (IngredientAdaptor) bundle.getSerializable("ADAPTER");
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
                                    Collections.sort(userIngredients, (o1, o2) -> o1.getCost().compareTo(o2.getCost()));
                                }

                                else {
                                    Collections.sort(userIngredients, (o1, o2) -> o2.getCost().compareTo(o1.getCost()));
                                }

                                break;


                            default:

                                if (isLowtoHigh) {
                                    Collections.sort(userIngredients, (o1, o2) -> (o1.getDescription().toLowerCase().compareTo(o2.getDescription().toLowerCase())));
                                }

                                else {
                                    Collections.sort(userIngredients, (o1, o2) -> (o2.getDescription().toLowerCase().compareTo(o1.getDescription().toLowerCase())));
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
