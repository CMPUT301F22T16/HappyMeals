package com.example.happymeals;

import static java.lang.Boolean.FALSE;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The addNwIngredient class handles the actions needed to either add or edit a ingredient,
 * it takes a "mode" parameter as an input to distinguish the working mode,
 * and it will return the newly added/edited parameters back to MainActivity.
 */
public class addNewIngredient extends AppCompatActivity {
    EditText ingredientLocation;
    Button confirmAdd;
    Spinner ingredientCategorySpinner;
    EditText ingredientDescription;
    EditText ingredientCount;
    EditText ingredientUnitCost;
    DatePicker ingredientBestBefore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_ingredient);

        this.setTitle("Add ingredient");

        // This line sets the return button to home screen.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ingredientCategorySpinner = findViewById(R.id.ingredientCategory);
        ingredientDescription = findViewById(R.id.ingredientDescription);
        ingredientCount = findViewById(R.id.ingredientCount);
        ingredientUnitCost = findViewById(R.id.ingredientUnitCost);
        ingredientBestBefore = findViewById(R.id.ingredientBestbefore);
        ingredientLocation = findViewById(R.id.ingredientLocation);
        confirmAdd = findViewById(R.id.confirm_button);


//        ArrayList<String> locations = new ArrayList<>(Arrays.asList("Pantry", "Freezer", "Fridge"));
//        ArrayAdapter<String> locationAdapt = new ArrayAdapter<String>(this, R.layout.ingredient_content, R.id.myTextview, locations);
        ArrayList<String> categories = new ArrayList<>(Arrays.asList("Vegetable", "Fruit", "Meat", "Drink", "Dry food", "Others"));
        ArrayAdapter<String> categoryAdapt = new ArrayAdapter<String>(this, R.layout.ingredient_content, R.id.myTextview, categories);

//        ingredientLocationSpinner.setAdapter(locationAdapt);
//        ingredientLocationSpinner.setPrompt("Ingredient location");
        ingredientCategorySpinner.setAdapter(categoryAdapt);
        ingredientCategorySpinner.setPrompt("Ingredient category");

        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");

        // If the mode is edit, population the text boxes with existing values.
        if (mode.equals("Edit")) {
            String category = intent.getStringExtra("category");
            String description = intent.getStringExtra("description");
            int count = intent.getIntExtra("count", -1);
            double unitCost = intent.getDoubleExtra("unit cost", -1);
            int year = intent.getIntExtra("year", -1);
            int month = intent.getIntExtra("month", -1);
            int day = intent.getIntExtra("day", -1);
            String location = intent.getStringExtra("location");

            ingredientCategorySpinner.setSelection(categories.lastIndexOf(category));
            ingredientDescription.setText(description);
            ingredientCount.setText(String.valueOf(count));
            ingredientUnitCost.setText(String.valueOf(unitCost));
            ingredientLocation.setText(location);
            ingredientBestBefore.updateDate(year, month, day);
        }
        confirmAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = ingredientCategorySpinner.getSelectedItem().toString();
                String description = ingredientDescription.getText().toString();
                String countString = ingredientCount.getText().toString();
                String unitCostString = ingredientUnitCost.getText().toString();

                int count = -1;
                double unitCost = -1;

                int year = ingredientBestBefore.getYear();
                int month = ingredientBestBefore.getMonth();
                int day = ingredientBestBefore.getDayOfMonth();
                String location = ingredientLocation.getText().toString();

                if (description.isEmpty()) {
                    ingredientDescription.requestFocus();
                    ingredientDescription.setError("Please provide the ingredient description.");
                }

                if (location.isEmpty()){
                    ingredientLocation.requestFocus();
                    ingredientLocation.setError("Please provide the ingredient location.");
                }

                // Set corresponding error messages if a text box is empty.
                if (countString.isEmpty()) {
                    ingredientCount.requestFocus();
                    ingredientCount.setError("Please provide the amount of the ingredient.");
                } else {
                    count = Integer.parseInt(ingredientCount.getText().toString());
                    if (count <= 0) {
                        ingredientCount.requestFocus();
                        ingredientCount.setError("Please provide a valid count.");
                    }
                }

                if (unitCostString.isEmpty()) {
                    ingredientUnitCost.requestFocus();
                    ingredientUnitCost.setError("Please provide the unit cost of the ingredient.");
                } else {
                    unitCost = Double.parseDouble(ingredientUnitCost.getText().toString());
                    if (unitCost <= 0) {
                        ingredientUnitCost.requestFocus();
                        ingredientUnitCost.setError("Please provide a valid unit cost.");
                    }
                }

                if (location.isEmpty() == FALSE && countString.isEmpty() == FALSE && unitCostString.isEmpty() == FALSE && description.isEmpty() == FALSE && count != 0 && unitCost != 0) {
                    Intent intent = new Intent(addNewIngredient.this, MainActivity.class);

                    intent.putExtra("mode", mode);
                    intent.putExtra("category", category);
                    intent.putExtra("description", description);
                    intent.putExtra("count", count);
                    intent.putExtra("unit cost", unitCost);
                    intent.putExtra("year", year);
                    intent.putExtra("month", month);
                    intent.putExtra("day", day);
                    intent.putExtra("location", location);

                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    // https://stackoverflow.com/questions/14545139/android-back-button-in-the-title-bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}