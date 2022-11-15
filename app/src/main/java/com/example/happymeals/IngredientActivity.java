package com.example.happymeals;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * The MainActivity class defines the actions to takes at the home screen as well as initiating
 * new activities when pressing certain buttons.
 */
public class IngredientActivity extends AppCompatActivity{
    ArrayList<Ingredient> ingredientList;
    ArrayAdapter ingredientAdaptor;
    ListView ingredientListView;
    TextView totalCost;
    FloatingActionButton floatingAdd;
    Spinner sortBySelect;

    // This integer is used to store the index of the Ingredient object in the list.
    int ingredientPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        getSupportActionBar().setTitle("Ingredients");

        ingredientListView = (ListView) findViewById(R.id.ingredientList);
        totalCost = (TextView) findViewById(R.id.costDescription);
        floatingAdd = (FloatingActionButton) findViewById(R.id.floatingAdd);
        sortBySelect = (Spinner) findViewById(R.id.sortBy);

        ingredientList = new ArrayList<Ingredient>();

        ingredientAdaptor = new IngredientAdaptor(this, ingredientList);
        DBHandler db = new DBHandler();
        db.getIngredients(ingredientAdaptor, totalCost);


        ingredientListView.setAdapter(ingredientAdaptor);

        ingredientPosition = -1;

        ArrayList<String> sortBy = new ArrayList<>(Arrays.asList("Name (A-Z)", "Name (Z-A)", "Price (Low - High)", "Price (High - Low)"));
        ArrayAdapter<String> sortByAdapt = new ArrayAdapter<String>(this, R.layout.ingredient_content, R.id.myTextview, sortBy);

        sortBySelect.setAdapter(sortByAdapt);
        sortBySelect.setPrompt("Sort By:");

        // The default sorting is by name a-z
        sortBySelect.setSelection(0, true);

        sortBySelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Toast.makeText(getApplicationContext(), "Sort by Name (A-Z)", Toast.LENGTH_SHORT).show();
                    db.sortIngredients("N_AZ");
                } else if (position == 1) {
                    Toast.makeText(getApplicationContext(), "Sort by Name (Z-A)", Toast.LENGTH_SHORT).show();
                    // Do something
                } else if (position == 2) {
                    Toast.makeText(getApplicationContext(), "Sort by Price (Low - High)", Toast.LENGTH_SHORT).show();
                    // Do something
                } else if (position == 3) {
                    Toast.makeText(getApplicationContext(), "Sort by Price (High - Low)", Toast.LENGTH_SHORT).show();
                    // Do something
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        /* An ActivityResultLauncher is defined here to handle the two modes for add/edit ingredient. As
           well as handling the results returned.
        */
        // https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
        ActivityResultLauncher<Intent> addIngredientActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent addIngredient = result.getData();

                            String category = addIngredient.getStringExtra("category");
                            String description = addIngredient.getStringExtra("description");
                            int count = addIngredient.getIntExtra("count", -1);
                            double unitCost = addIngredient.getDoubleExtra("unit cost", -1);
                            int year = addIngredient.getIntExtra("year", -1);
                            int month = addIngredient.getIntExtra("month", -1);
                            int day = addIngredient.getIntExtra("day", -1);
                            String location = addIngredient.getStringExtra("location");
                            String mode = addIngredient.getStringExtra("mode");

                            if (mode.equals("Edit") && ingredientPosition != -1) {
                                Ingredient oldIngredient = ingredientList.get(ingredientPosition);

                                oldIngredient.setCategory(category);
                                oldIngredient.setDescription(description);
                                oldIngredient.setAmount(count);
                                oldIngredient.setCost(unitCost);
                                oldIngredient.setDate(year, month, day);
                                oldIngredient.setLoc(location);

                                db.updateIngredient(oldIngredient);
                                ingredientPosition = -1;
                            } else if (mode.equals("Add")) {
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, month);
                                cal.set(Calendar.DAY_OF_MONTH, day);
                                Date date = cal.getTime();
                                Ingredient newIngredient = new Ingredient(category, description, count, unitCost, date, location);
                                //ingredientList.add(newIngredient);
                                db.newIngredient(newIngredient);
                                ingredientPosition = -1;
                            }
                            ingredientListView.setAdapter(ingredientAdaptor);

                        }
                    }
                });

        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Ingredient ingredient = (Ingredient) parent.getItemAtPosition(position);
                ingredientPosition = position;


                ViewIngredientFragment.newInstance(ingredient).show(getSupportFragmentManager(), "VIEW_INGREDIENT");

            }
        });

        // https://stackoverflow.com/questions/4186021/how-to-start-new-activity-on-button-click
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IngredientActivity.this, addNewIngredient.class);
                intent.putExtra("mode", "Add");
                addIngredientActivityResultLauncher.launch(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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