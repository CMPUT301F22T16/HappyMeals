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
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

/**
 * The MainActivity class defines the actions to takes at the home screen as well as initiating
 * new activities when pressing certain buttons.
 */
public class IngredientActivity extends AppCompatActivity implements ViewIngredientFragment.OnFragmentInteractionListener{
    ArrayList<Ingredient> ingredientList;
    ArrayAdapter ingredientAdaptor;
    ListView ingredientListView;
//    Button deleteIngredient;
//    Button addIngredient;
//    Button editIngredient;
    TextView totalCost;
    FloatingActionButton floatingAdd;

    // This integer is used to store the index of the Ingredient object in the list.
    int ingredientPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        Context context = this;
        // this.setTitle("My ingredient list");

        ingredientList = new ArrayList<Ingredient>();


        ingredientAdaptor = new IngredientAdaptor(this, ingredientList);
        User user = new User();
        user.getIngredients(ingredientAdaptor);


        ingredientListView = (ListView) findViewById(R.id.ingredientList);
//        deleteIngredient = (Button) findViewById(R.id.deletebutton);
//        addIngredient = (Button) findViewById(R.id.addbutton);
//        editIngredient = (Button) findViewById(R.id.editbutton);
        totalCost = (TextView) findViewById(R.id.costDescription);
        floatingAdd = (FloatingActionButton) findViewById(R.id.floatingAdd);

        ingredientListView.setAdapter(ingredientAdaptor);
        updateCost();

        ingredientPosition = -1;

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

                                user.updateIngredient(oldIngredient, context);
                                ingredientPosition = -1;
                            } else if (mode.equals("Add")) {
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, month);
                                cal.set(Calendar.DAY_OF_MONTH, day);
                                Date date = cal.getTime();
                                Ingredient newIngredient = new Ingredient(category, description, count, unitCost, date, location);
                                //ingredientList.add(newIngredient);
                                user.newIngredient(newIngredient, context);
                                ingredientPosition = -1;
                            }
                            updateCost();
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

                // Two actions can be invoked after clicking a ingredient, delete or edit.
//                deleteIngredient.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (ingredientPosition != -1){
//                            user.deleteIngredient(ingredient, context);
//                            //ingredientList.remove(ingredient);
//                            updateCost();
//                            ingredientListView.setAdapter(ingredientAdaptor);
//                            ingredientPosition = -1;
//                        }
//
//                    }
//                });
//
//                editIngredient.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (ingredientPosition != -1 && ingredientList.contains(ingredient)) {
//                            Intent intent = new Intent(IngredientActivity.this, addNewIngredient.class);
//                            intent.putExtra("mode", "Edit");
//                            intent.putExtra("category", ingredient.getCategory());
//                            intent.putExtra("description", ingredient.getDescription());
//                            intent.putExtra("count", ingredient.getAmount());
//                            intent.putExtra("unit cost", ingredient.getCost());
//                            intent.putExtra("year", ingredient.getYear());
//                            intent.putExtra("month", ingredient.getMonth());
//                            intent.putExtra("day", ingredient.getDay());
//                            intent.putExtra("location", ingredient.getLoc());
//                            addIngredientActivityResultLauncher.launch(intent);
//                        }
//                    }
//                });
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

    // A function to update the total cost of the ingredient after edit, add or delete.
    private void updateCost() {
        Log.d("Mike9122001", "This is the size: "+String.valueOf(ingredientList.size()));
        double cost = 0;
        for (Ingredient i : ingredientList) {
            cost = cost + (i.getAmount() * i.getCost());
        }


        //totalCost.setText("Total cost: $" + String.valueOf(cost));
        totalCost.setText("");
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

    @Override
    public void onOkPressed(Ingredient ingredient) {
        int i = 1;
    }
}