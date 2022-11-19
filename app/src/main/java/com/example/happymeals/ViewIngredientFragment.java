package com.example.happymeals;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The AddCityFragment is used to add a new city, it defines the actions to take once the users
 * decides to add/edit a city after pressing the button. Depending on the mode, this activity will
 * retrieve the input from the text box provided by the user.
 */
public class ViewIngredientFragment extends DialogFragment {

    private Spinner thisCategory;
    private EditText thisDescription;
    private EditText thisLocation;
    private EditText thisAmount;
    private EditText thisUnitCost;
    private DatePicker thisBestBefore;
    UserIngredient thisUserIngredient;
    String userId;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_ingredient_fragment_layout, null);

        final Bundle ingredientAndId = this.getArguments();

        Context context = getContext();
        userId = (String) ingredientAndId.getSerializable("USERID");
        DBHandler db = new DBHandler(userId);

        thisCategory = view.findViewById(R.id.category);
        thisDescription = view.findViewById(R.id.description_frag);
        thisLocation = view.findViewById(R.id.location_frag);
        thisAmount = view.findViewById(R.id.count_frag);
        thisUnitCost = view.findViewById(R.id.unitcost_frag);
        thisBestBefore = view.findViewById(R.id.ingredientBestbefore);

        ArrayList<String> categories = new ArrayList<>(Arrays.asList("Vegetable", "Fruit", "Meat", "Drink", "Dry food", "Others"));
        ArrayAdapter<String> categoryAdapt = new ArrayAdapter<String>(context, R.layout.ingredient_content, R.id.myTextview, categories);


        thisCategory.setAdapter(categoryAdapt);
        thisCategory.setPrompt("Ingredient category");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (ingredientAndId != null){
            thisUserIngredient = (UserIngredient) ingredientAndId.getSerializable("ingredient");
            thisCategory.setSelection(categories.indexOf(thisUserIngredient.getCategory()));
            thisDescription.setText(thisUserIngredient.getDescription());
            thisLocation.setText(thisUserIngredient.getLoc());
            thisAmount.setText(Integer.toString(thisUserIngredient.getAmount()));
            thisUnitCost.setText(Double.toString(thisUserIngredient.getCost()));
            thisBestBefore.updateDate(thisUserIngredient.getYear(), thisUserIngredient.getMonth(), thisUserIngredient.getDay());
        }


        return builder
                .setView(view)
                .setTitle("Food detail")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String category = thisCategory.getSelectedItem().toString();
                        String description = thisDescription.getText().toString();
                        String countString = thisAmount.getText().toString();
                        String unitCostString = thisUnitCost.getText().toString();

                        int count = -1;
                        double unitCost = -1;

                        int year = thisBestBefore.getYear();
                        int month = thisBestBefore.getMonth();
                        int day = thisBestBefore.getDayOfMonth();
                        String location = thisLocation.getText().toString();

                        boolean toast = FALSE;

                        if (description.isEmpty()) {
//                            thisDescription.requestFocus();
//                            thisDescription.setError("Please provide the ingredient description.");
                            toast = TRUE;

                        }

                        if (location.isEmpty()) {
//                            thisDescription.requestFocus();
//                            thisDescription.setError("Please provide the ingredient description.");
                            toast = TRUE;

                        }

                        // Set corresponding error messages if a text box is empty.
                        if (countString.isEmpty()) {
//                            thisAmount.requestFocus();
//                            thisAmount.setError("Please provide the amount of the ingredient.");
                            toast = TRUE;
                        } else {
                            count = Integer.parseInt(thisAmount.getText().toString());
                            if (count <= 0) {
//                                thisAmount.requestFocus();
//                                thisAmount.setError("Please provide a valid count.");
                                toast = TRUE;
                            }
                        }

                        if (unitCostString.isEmpty()) {
//                            thisUnitCost.requestFocus();
//                            thisUnitCost.setError("Please provide the unit cost of the ingredient.");
                            toast = TRUE;
                        } else {
                            unitCost = Double.parseDouble(thisUnitCost.getText().toString());
                            if (unitCost <= 0) {
//                                thisUnitCost.requestFocus();
//                                thisUnitCost.setError("Please provide a valid unit cost.");
                                toast = TRUE;
                            }
                        }

                        if (toast == TRUE){
                            Toast.makeText(context, "Incomplete/Invalid input.", Toast.LENGTH_SHORT).show();
                        }
                        if (location.isEmpty() == FALSE && countString.isEmpty() == FALSE && unitCostString.isEmpty() == FALSE && description.isEmpty() == FALSE && count != 0 && unitCost != 0) {
                            thisUserIngredient.setCategory(category);
                            thisUserIngredient.setDescription(description);
                            thisUserIngredient.setAmount(count);
                            thisUserIngredient.setCost(unitCost);
                            thisUserIngredient.setDate(year, month, day);
                            thisUserIngredient.setLoc(location);
                            db.updateIngredient(thisUserIngredient);

                        }

                        //startActivity(intent);
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Confirm deletion");
                        builder.setMessage("Are you sure you want to delete this ingredient?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Ingredient deleted", Toast.LENGTH_SHORT).show();
                                db.deleteIngredient(thisUserIngredient);
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();


                    }
                })
                .create();
        }

    // This is used to serialize the city object and so it can be passed between activities.
    static ViewIngredientFragment newInstance(UserIngredient userIngredient, String userId) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient", userIngredient);
        args.putSerializable("USERID", userId);

        ViewIngredientFragment fragment = new ViewIngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
