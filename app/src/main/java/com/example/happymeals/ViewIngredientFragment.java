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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private Spinner thisLocation;
    private EditText thisAmount;
    private EditText thisUnitCost;
    private DatePicker thisBestBefore;
    private Spinner thisUnit;
    private UserIngredient thisUserIngredient;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_ingredient_fragment_layout, null);

        Context context = getContext();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DBHandler db = new DBHandler(user.getUid());

        thisCategory = view.findViewById(R.id.category);
        thisDescription = view.findViewById(R.id.description_frag);
        thisLocation = view.findViewById(R.id.location_frag);
        thisAmount = view.findViewById(R.id.count_frag);
        thisUnitCost = view.findViewById(R.id.unitcost_frag);
        thisUnit = view.findViewById(R.id.unit_frag);
        thisBestBefore = view.findViewById(R.id.ingredientBestbefore);


        ArrayList<String> locations = new ArrayList<>();
        ArrayAdapter<String> locationAdapt = new ArrayAdapter<String>(this.getContext(), R.layout.ingredient_content, R.id.myTextview, locations);
        this.thisLocation.setAdapter(locationAdapt);

        ArrayList<String> categories = new ArrayList<>(Arrays.asList("Vegetable", "Fruit", "Meat", "Drink", "Dry food", "Others"));
        ArrayAdapter<String> categoryAdapt = new ArrayAdapter<String>(context, R.layout.ingredient_content, R.id.myTextview, categories);
        thisCategory.setAdapter(categoryAdapt);
        thisCategory.setPrompt("Ingredient category");

        ArrayList<String> fluidUnit = new ArrayList<>(Arrays.asList("ml", "L"));
        ArrayList<String> solidUnit = new ArrayList<>(Arrays.asList("g", "kg"));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        final Bundle ingredient = this.getArguments();
        boolean isSL = (Boolean) ingredient.getSerializable("SL");
        if (ingredient != null){
            thisUserIngredient = (UserIngredient) ingredient.getSerializable("ingredient");
            thisCategory.setSelection(categories.indexOf(thisUserIngredient.getCategory()));
            thisDescription.setText(thisUserIngredient.getDescription());

            locations = (ArrayList<String>) ingredient.getSerializable("locations");
            locationAdapt = new ArrayAdapter<String>(this.getContext(), R.layout.ingredient_content, R.id.myTextview, locations);
            this.thisLocation.setAdapter(locationAdapt);

            thisAmount.setText(Double.toString(thisUserIngredient.getAmount()));

            if(!isSL) {
                thisUnitCost.setText(Double.toString(thisUserIngredient.getCost()));
                thisLocation.setSelection(locations.indexOf(thisUserIngredient.getLoc()));
                thisBestBefore.updateDate(thisUserIngredient.getYear(), thisUserIngredient.getMonth(), thisUserIngredient.getDay());
            }

            if (thisUserIngredient.getCategory().equals("Drink")){
                ArrayAdapter<String> unitAdapt = new ArrayAdapter<String>(context, R.layout.ingredient_content, R.id.myTextview, fluidUnit);
                thisUnit.setAdapter(unitAdapt);
                // Toast.makeText(context, thisUserIngredient.getUnit(), Toast.LENGTH_SHORT).show();
                thisUnit.setSelection(fluidUnit.indexOf(thisUserIngredient.getUnit()));

            }else{
                ArrayAdapter<String> unitAdapt = new ArrayAdapter<String>(context, R.layout.ingredient_content, R.id.myTextview, solidUnit);
                thisUnit.setAdapter(unitAdapt);
                thisUnit.setSelection(solidUnit.indexOf(thisUserIngredient.getUnit()));
            }
        }

        thisCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (categories.get(position) == "Drink") {
                    if (thisUserIngredient.getCategory().equals("Drink")){
                        ArrayAdapter<String> unitAdapt = new ArrayAdapter<String>(context, R.layout.ingredient_content, R.id.myTextview, fluidUnit);
                        thisUnit.setAdapter(unitAdapt);
                        // Toast.makeText(context, thisUserIngredient.getUnit(), Toast.LENGTH_SHORT).show();
                        thisUnit.setSelection(fluidUnit.indexOf(thisUserIngredient.getUnit()));
                    }else{
                        ArrayAdapter<String> unitAdapt = new ArrayAdapter<String>(context, R.layout.ingredient_content, R.id.myTextview, fluidUnit);
                        thisUnit.setAdapter(unitAdapt);
                    }
                    // Toast.makeText(getApplicationContext(), "Selected Drink", Toast.LENGTH_SHORT).show();

                } else {
                    if (!thisUserIngredient.getCategory().equals("Drink")){
                        ArrayAdapter<String> unitAdapt = new ArrayAdapter<String>(context, R.layout.ingredient_content, R.id.myTextview, solidUnit);
                        thisUnit.setAdapter(unitAdapt);
                        // Toast.makeText(context, thisUserIngredient.getUnit(), Toast.LENGTH_SHORT).show();
                        thisUnit.setSelection(solidUnit.indexOf(thisUserIngredient.getUnit()));
                    }else{
                        ArrayAdapter<String> unitAdapt = new ArrayAdapter<String>(context, R.layout.ingredient_content, R.id.myTextview, solidUnit);
                        thisUnit.setAdapter(unitAdapt);
                    }
                    // Toast.makeText(getApplicationContext(), "Selected non-Drink", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (isSL) {
            return builder
                    .setView(view)
                    .setTitle("Add to Ingredient")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String category = thisCategory.getSelectedItem().toString();
                            String description = thisDescription.getText().toString();
                            String countString = thisAmount.getText().toString();
                            String unitCostString = thisUnitCost.getText().toString();

                            double count = -1;
                            double unitCost = -1;

                            int year = thisBestBefore.getYear();
                            int month = thisBestBefore.getMonth();
                            int day = thisBestBefore.getDayOfMonth();
                            String location = thisLocation.getSelectedItem().toString();
                            String unit = thisUnit.getSelectedItem().toString();

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
                                count = Double.parseDouble(thisAmount.getText().toString());
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

                            if (location.equals("Select location")){
                                toast = TRUE;
                            }

                            if (toast == TRUE){
                                Toast.makeText(context, "Incomplete/Invalid input", Toast.LENGTH_SHORT).show();
                            }
                            if (location.isEmpty() == FALSE && countString.isEmpty() == FALSE && unitCostString.isEmpty() == FALSE && description.isEmpty() == FALSE && count != 0 && unitCost != 0 && toast == FALSE) {
                                thisUserIngredient.setCategory(category);
                                thisUserIngredient.setDescription(description);
                                thisUserIngredient.setAmount(count);
                                thisUserIngredient.setCost(unitCost);
                                thisUserIngredient.setDate(year, month, day);
                                thisUserIngredient.setLoc(location);
                                thisUserIngredient.setUnit(unit);
                                db.newIngredient(thisUserIngredient);

                            }

                            //startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .create();
        }

        return builder
                .setView(view)
                .setTitle("Ingredient detail")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String category = thisCategory.getSelectedItem().toString();
                        String description = thisDescription.getText().toString();
                        String countString = thisAmount.getText().toString();
                        String unitCostString = thisUnitCost.getText().toString();

                        double count = -1;
                        double unitCost = -1;

                        int year = thisBestBefore.getYear();
                        int month = thisBestBefore.getMonth();
                        int day = thisBestBefore.getDayOfMonth();
                        String location = thisLocation.getSelectedItem().toString();
                        String unit = thisUnit.getSelectedItem().toString();

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
                            count = Double.parseDouble(thisAmount.getText().toString());
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

                        if (location.equals("Select location")){
                            toast = TRUE;
                        }

                        if (toast == TRUE){
                            Toast.makeText(context, "Incomplete/Invalid input", Toast.LENGTH_SHORT).show();
                        }
                        if (location.isEmpty() == FALSE && countString.isEmpty() == FALSE && unitCostString.isEmpty() == FALSE && description.isEmpty() == FALSE && count != 0 && unitCost != 0 && toast == FALSE) {
                            thisUserIngredient.setCategory(category);
                            thisUserIngredient.setDescription(description);
                            thisUserIngredient.setAmount(count);
                            thisUserIngredient.setCost(unitCost);
                            thisUserIngredient.setDate(year, month, day);
                            thisUserIngredient.setLoc(location);
                            thisUserIngredient.setUnit(unit);
                            thisUserIngredient.setIncomplete(false);
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

        // This is used to serialize the ingredient object and so it can be passed between activities.
    public static ViewIngredientFragment newInstance(UserIngredient userIngredient, ArrayList<String> locations, boolean isSL) {
        Bundle args = new Bundle();
        args.putSerializable("SL", isSL);
        args.putSerializable("ingredient", userIngredient);
        args.putSerializable("locations", locations);
        ViewIngredientFragment fragment = new ViewIngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
