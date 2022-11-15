package com.example.happymeals.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.happymeals.DBHandler;
import com.example.happymeals.Recipe;
import com.example.happymeals.RecipeIngredient;
import com.example.happymeals.UserIngredient;
import com.example.happymeals.IngredientAdaptor;
import com.example.happymeals.R;

import java.util.ArrayList;

/**
 * This class creates an Activity that allows the user to add an ingredient to their recipe
 * @author John Yu
 */
public class RecipeAddIngredient extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /**
     * This is the EditText view where the user can add the recipe description.
     */
    EditText description_edit_text;

    /**
     * This is the EditText View where the user can add the recipe category.
     */
    EditText category_edit_text;

    /**
     * This is the EditText View where the user can add the recipe amount.
     */
    EditText amount_edit_text;

    /**
     * This is the spinner where the user can select a unit.
     */
    Spinner amount_unit_spinner;

    /**
     * This variable stores the unit for the amounts variable.
     */
    String amount_unit;

    /**
     * This button lets the user submit their new ingredient
     */
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add_ingredient);

        amount_unit_spinner = findViewById(R.id.recipe_add_ingredient_amount_units);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.add_ingredient_amount_unit, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amount_unit_spinner.setAdapter(spinnerAdapter);
        amount_unit_spinner.setOnItemSelectedListener(this);

        description_edit_text = findViewById(R.id.recipe_add_ingredient_description);
        category_edit_text = findViewById(R.id.recipe_add_ingredient_category);
        amount_edit_text = findViewById(R.id.recipe_add_ingredient_amount);

        submit_btn = findViewById(R.id.recipe_add_ingredient_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                String description = description_edit_text.getText().toString();
                String category = category_edit_text.getText().toString();
                Double amount = amount_edit_text.getText().toString().equals("") ? 0 : Double.parseDouble(amount_edit_text.getText().toString());

                intent.putExtra("description", description);
                intent.putExtra("category", category);
                intent.putExtra("amount", amount);
                intent.putExtra("amount_unit", amount_unit);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        amount_unit = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        amount_unit = "qty";
    }
}