package com.example.happymeals.meal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.happymeals.DBHandler;
import com.example.happymeals.LoadingDialog;
import com.example.happymeals.R;
import com.example.happymeals.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * This Activity allows user to pick recipes to add to the meal
 */
public class MPPickRecipeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    ListView recipe_list;
    MPPickRecipeListAdapter recipe_adapter;
    List<Recipe> dataList; // contains all the existing recipes in the meal
    SearchView recipe_search_bar;
    Button confirm_button;
    DBHandler dbHandler;
    Meal meal;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mppick_recipe);
        dataList = new ArrayList<Recipe>();

        // back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set up ui components
        confirm_button = findViewById(R.id.confirm_recipe_selection_button);
        recipe_list = findViewById(R.id.mp_recipe_list);
        recipe_search_bar = findViewById(R.id.searchview_recipe);


        // Set up user here
        dbHandler = new DBHandler();

        // set up adapter
        intent =  getIntent();
        Bundle bundle  = intent.getExtras();
        meal = (Meal) bundle.getSerializable("MEAL");
        dataList = meal.getRecipes();
        recipe_adapter = new MPPickRecipeListAdapter(this, (ArrayList<Recipe>) dataList);

        // set up search bar
        recipe_list.setAdapter(recipe_adapter);
        recipe_search_bar.setOnQueryTextListener(this);

        // get user's recipes
        LoadingDialog dialog = new LoadingDialog(this);
        dbHandler.getUserRecipesForMeals(recipe_adapter,dialog);

        // set up all the button listeners
        setOnConfirmButtonListener();
        setOnListViewItemListener();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String entered_text = newText;
        recipe_adapter.filter(entered_text);
        return false;
    }

    private void setOnConfirmButtonListener() {
        confirm_button.setOnClickListener(v -> {
            // update recipes for the meal
            if (recipe_adapter.getRecipesSelected().size()<1){
                createPopup("You haven't select anything yet");
            } else {

                ArrayList<Recipe> meal_recipes = recipe_adapter.getAllRecipes();
//                meal.setRecipes(r);
//                dbHandler.modifyMeal(meal);

                Intent return_intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Updated-Meal-Recipes", meal_recipes);
                return_intent.putExtras(bundle);
                setResult(Activity.RESULT_OK,return_intent);
                this.finish();// get back to caller activity which is meal recipe list
            }

        });
    }

    /**
     * Set an listener on the items of the list
     * When clicked, either add or remove from the buffer
     */
    private void setOnListViewItemListener() {
        recipe_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox checkBox = view.findViewById(R.id.checkBox);
                checkBox.toggle();
                if(checkBox.isChecked()){
                    recipe_adapter.addToBuffer(i);
                } else {
                    recipe_adapter.removeFromBuffer(i);
                }

            }
        });
    }

    /**
     * This method creates a popup
     * @param message set the message we want to display
     * Knowledge from Sumit Saxena's anwser(Apr 22, 2016) to
     * https://stackoverflow.com/questions/22655599/alertdialog-builder-with-custom-layout-and-edittext-cannot-access-view
     */
    public void createPopup(String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.popup, null);
        dialogBuilder.setView((popupView));
        TextView alertMessage = popupView.findViewById(R.id.textViewAlertMessage);
        alertMessage.setText(message);
        dialogBuilder.create();
        dialogBuilder.show();
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
}