package com.example.happymeals.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.happymeals.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class creates an Activity that allows the user to add an ingredient to their recipe
 * @author John Yu
 */
public class RecipeAddIngredient extends AppCompatActivity {

    /**
     * This is the EditText view where the user can add the recipe description.
     */
    EditText description_edit_text;

    /**
     * This is the spinner where the user can select a category.
     */
    Spinner category_spinner;

    /**
     * This is the EditText View where the user can add the recipe amount.
     */
    EditText amount_edit_text;

    /**
     * This is the spinner where the user can select a unit.
     */
    Spinner amount_unit_spinner;

    /**
     * This button lets the user submit their new ingredient
     */
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add_ingredient);

        category_spinner = findViewById(R.id.recipe_add_ingredient_category_spinner);
        ArrayList<String> categories = new ArrayList<>(Arrays.asList("Vegetable", "Fruit", "Meat", "Drink", "Dry food", "Others"));
        ArrayList<String> fluidUnit = new ArrayList<>(Arrays.asList("ml", "L"));
        ArrayList<String> solidUnit = new ArrayList<>(Arrays.asList("g", "kg"));
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.ingredient_content, R.id.myTextview, categories);
        category_spinner.setAdapter(categoryAdapter);
        category_spinner.setPrompt("CATEGORY");

        amount_unit_spinner = findViewById(R.id.recipe_add_ingredient_amount_units);
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<String> unitAdapt;
                if (categories.get(i).equals("Drink")) {
                    unitAdapt = new ArrayAdapter<>(RecipeAddIngredient.this, R.layout.ingredient_content, R.id.myTextview, fluidUnit);
                } else {
                    unitAdapt = new ArrayAdapter<>(RecipeAddIngredient.this, R.layout.ingredient_content, R.id.myTextview, solidUnit);
                }
                amount_unit_spinner.setAdapter(unitAdapt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        description_edit_text = findViewById(R.id.recipe_add_ingredient_description);
//        category_edit_text = findViewById(R.id.recipe_add_ingredient_category);
        amount_edit_text = findViewById(R.id.recipe_add_ingredient_amount);

        submit_btn = findViewById(R.id.recipe_add_ingredient_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allEntered;
                allEntered = !description_edit_text.getText().toString().isEmpty()
                        && !amount_edit_text.getText().toString().isEmpty();

                if (description_edit_text.getText().toString().isEmpty()) {
                    description_edit_text.requestFocus();
                    description_edit_text.setError("Please provide the ingredient description.");
                }

                if (amount_edit_text.getText().toString().isEmpty()) {
                    amount_edit_text.requestFocus();
                    amount_edit_text.setError("Please provide the ingredient amount");
                }

                if (allEntered) {
                    Intent intent = new Intent();

                    String description = description_edit_text.getText().toString();
                    String category = category_spinner.getSelectedItem().toString();
                    Double amount = amount_edit_text.getText().toString().equals("") ? 0 : Double.parseDouble(amount_edit_text.getText().toString());
                    String amount_unit = amount_unit_spinner.getSelectedItem().toString();

                    intent.putExtra("description", description);
                    intent.putExtra("category", category);
                    intent.putExtra("amount", amount);
                    intent.putExtra("amount_unit", amount_unit);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}