package com.example.happymeals;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

import android.widget.SearchView;
import android.widget.TextView;

import com.example.happymeals.storage.Storage;
import com.example.happymeals.storage.StorageAdapter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;

/**
 * The MainActivity class defines the actions to takes at the home screen as well as initiating
 * new activities when pressing certain buttons.
 */
public class IngredientActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ArrayList<UserIngredient> userIngredientList;
    IngredientAdaptor ingredientAdaptor;
    ListView ingredientListView;
    TextView totalCost;
    ExtendedFloatingActionButton floatingAdd;
    FloatingActionButton sortIngredients;

    // This integer is used to store the index of the Ingredient object in the list.
    int ingredientPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        getSupportActionBar().setTitle("Ingredients");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Ingredient list selected from storages
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Storage storage = null;
        try {
            storage = (Storage) bundle.getSerializable("STORAGE");
        } catch (Exception e) {

        }



        userIngredientList = new ArrayList<UserIngredient>();

        ingredientListView = (ListView) findViewById(R.id.ingredient_list);
        floatingAdd =  findViewById(R.id.floatingAdd);
        sortIngredients = (FloatingActionButton) findViewById(R.id.sort_ingredients);

        ingredientAdaptor = new IngredientAdaptor(this, userIngredientList);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DBHandler db = new DBHandler(user.getUid());


        if (storage != null ) { // Ingredient Activity is launched by Storage Activity
            // Disable UI that we don't want
            setContentView(R.layout.storage_ingredient);
            ingredientListView = findViewById(R.id.storage_ingredient_list);
            db.getIngredientsForStorage(ingredientAdaptor, storage);
            ingredientListView.setAdapter(ingredientAdaptor);

            // SearchView
            SearchView search = findViewById(R.id.ingredient_search);
            search.setOnQueryTextListener(this);


            return;
        } else {

            db.getIngredients(ingredientAdaptor);
        }

        ingredientListView.setAdapter(ingredientAdaptor);

        ingredientPosition = -1;


        sortIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IngredientSortFragment.newInstance(userIngredientList, ingredientAdaptor).show(getSupportFragmentManager(), "SORTING INGREDIENTS");
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
                            double count = addIngredient.getDoubleExtra("count", -1);
                            double unitCost = addIngredient.getDoubleExtra("unit cost", -1);
                            int year = addIngredient.getIntExtra("year", -1);
                            int month = addIngredient.getIntExtra("month", -1);
                            int day = addIngredient.getIntExtra("day", -1);
                            String location = addIngredient.getStringExtra("location");
                            String unit = addIngredient.getStringExtra("unit");
                            String mode = addIngredient.getStringExtra("mode");

                            if (mode.equals("Edit") && ingredientPosition != -1) {
                                UserIngredient oldUserIngredient = userIngredientList.get(ingredientPosition);

                                oldUserIngredient.setCategory(category);
                                oldUserIngredient.setDescription(description);
                                oldUserIngredient.setAmount(count);
                                oldUserIngredient.setCost(unitCost);
                                oldUserIngredient.setDate(year, month, day);
                                oldUserIngredient.setLoc(location);

                                db.updateIngredient(oldUserIngredient);
                                ingredientPosition = -1;
                            } else if (mode.equals("Add")) {
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, month);
                                cal.set(Calendar.DAY_OF_MONTH, day);
                                Date date = cal.getTime();
                                UserIngredient newUserIngredient = new UserIngredient(category, description, count, unitCost, date, location, unit);
                                //ingredientList.add(newIngredient);
                                db.newIngredient(newUserIngredient);
                                ingredientPosition = -1;
                            }
                            ingredientListView.setAdapter(ingredientAdaptor);

                        }
                    }
                });

        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                UserIngredient userIngredient = (UserIngredient) parent.getItemAtPosition(position);
                ingredientPosition = position;


                ViewIngredientFragment.newInstance(userIngredient).show(getSupportFragmentManager(), "VIEW_INGREDIENT");

            }
        });

        // https://stackoverflow.com/questions/4186021/how-to-start-new-activity-on-button-click
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IngredientActivity.this, AddNewIngredient.class);
                intent.putExtra("mode", "Add");
                addIngredientActivityResultLauncher.launch(intent);
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

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String entered_text = s;
        ingredientAdaptor.filter(entered_text);
        return false;
    }
}