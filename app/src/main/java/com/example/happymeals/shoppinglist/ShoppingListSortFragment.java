package com.example.happymeals.shoppinglist;

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
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import com.example.happymeals.DBHandler;
import com.example.happymeals.R;

import com.example.happymeals.ingredient.UserIngredient;
import com.example.happymeals.recipe.RecipeIngredient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListSortFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListSortFragment extends DialogFragment {

    private List<UserIngredient> userIngredients;
    private RadioGroup sortingOptions;
    private RadioGroup orderOptions;
    private DBHandler db;

    public static ShoppingListSortFragment newInstance(ArrayList<RecipeIngredient> ingredientList, ArrayAdapter adapter) {
        ShoppingListSortFragment fragment = new ShoppingListSortFragment();
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
        View view = LayoutInflater.from(context).inflate(R.layout.shopping_ingredient_sort_fragment, null);


        Bundle bundle = this.getArguments();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = new DBHandler(user.getUid());
        userIngredients = (List<UserIngredient>) bundle.getSerializable("COLLECTION");
        SLShoppingListAdapter adapter = (SLShoppingListAdapter) bundle.getSerializable("ADAPTER");
        setupView(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        return builder
                .setView(view)
                .setTitle("Sort Recipes")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean isLowtoHigh = orderOptions.getCheckedRadioButtonId() == R.id.shopping_ingredient_low_to_high;

                        int option = sortingOptions.getCheckedRadioButtonId();

                        switch(option) {

                            default:

                                if (isLowtoHigh) {
//                                    db.setSort(adapter, userIngredients, null, "Z-A");
                                }

                                else {
//                                    db.setSort(adapter, userIngredients, null, "A-Z");
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
        sortingOptions = view.findViewById(R.id.shopping_sort_recipe_group);
        orderOptions = view.findViewById(R.id.shopping_sort_order_group);
    }
}
