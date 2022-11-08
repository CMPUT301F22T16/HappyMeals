package com.example.happymeals;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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
    Ingredient thisIngredient;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_ingredient_fragment_layout, null);

        Context context = getContext();
        DBHandler db = new DBHandler();

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

        final Bundle ingredient = this.getArguments();

        if (ingredient != null){
            thisIngredient = (Ingredient) ingredient.getSerializable("ingredient");
            thisCategory.setSelection(categories.indexOf(thisIngredient.getCategory()));
            thisDescription.setText(thisIngredient.getDescription());
            thisLocation.setText(thisIngredient.getLoc());
            thisAmount.setText(Integer.toString(thisIngredient.getAmount()));
            thisUnitCost.setText(Double.toString(thisIngredient.getCost()));
            thisBestBefore.updateDate(thisIngredient.getYear(), thisIngredient.getMonth(), thisIngredient.getDay());
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
                            thisIngredient.setCategory(category);
                            thisIngredient.setDescription(description);
                            thisIngredient.setAmount(count);
                            thisIngredient.setCost(unitCost);
                            thisIngredient.setDate(year, month, day);
                            thisIngredient.setLoc(location);
                            db.updateIngredient(thisIngredient);

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
                                db.deleteIngredient(thisIngredient);
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
    static ViewIngredientFragment newInstance(Ingredient ingredient) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient", ingredient);

        ViewIngredientFragment fragment = new ViewIngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
