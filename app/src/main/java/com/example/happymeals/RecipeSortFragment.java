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
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeSortFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeSortFragment extends DialogFragment {

    private List<Recipe> listToSort;
    private RadioGroup sortingOptions;
    private RadioGroup orderOptions;
    private String orderDefault = "lowToHigh";
    private String sortingDefault = "Title";

    public static RecipeSortFragment newInstance(List<Recipe> recipeList) {
        RecipeSortFragment fragment = new RecipeSortFragment();
        Bundle args = new Bundle();
        args.putSerializable("COLLECTION", (Serializable) recipeList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_sort, null);


        Bundle bundle = this.getArguments();
        listToSort = (List<Recipe>) bundle.getSerializable("COLLECTION");
        setupView(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        return builder
                .setView(view)
                .setTitle("Sort Recipes By")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int order = orderOptions.getCheckedRadioButtonId();
                        int option = sortingOptions.getCheckedRadioButtonId();
                        System.out.println(option);
                        switch(option) {
                            case 0:
                                System.out.println("High to Low");
                                break;
                            default:
                                break;
                        }
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

    public void orderGroupOnClick(View view) {

    }

    public void sortGroupOnClick(View view) {

    }

}