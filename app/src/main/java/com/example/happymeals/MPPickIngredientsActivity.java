package com.example.happymeals;

        import androidx.appcompat.app.AppCompatActivity;

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

        import java.util.ArrayList;
        import java.util.List;

/**
 * This Activity allows user to pick recipes to add to the meal
 */
public class MPPickIngredientsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    ListView ingredient_list;
    MPPickIngredientsAdapter ingredient_adapter;
    List<Recipe> meal_recipe_list; // contains all the existing recipes in the meal
    List<Ingredient> existing_ingredients;
    SearchView ingredient_search_bar;
    Button confirmButton;
    DBHandler dbHandler;
    Meal meal;
    Intent intent;
    /*
    TODO:
        1.fetch all the recipes in the meal
            to check if any recipes is consist of
            single ingredient. if there is such
            a recipes, fetch the ingredient, and
            add that ingredient(s) to existing_ingredients
            list.
            1.2. fetch all user ingredients-- should be done by modified the method in DBhandler
        2. After user have selected all the ingredients he
            wants to add, we check each ingredient
            1. fetch all recipes of the user
            2. check if there exist a recipes that
            has single ingredients, and the id match
            that ingredient in the buffer user selected
            3. if the ingredient was an existing recipes,
            add that do the recipe buffer. if not, make
            one recipe for the ingredient, and add to the buffer
        3. combine the recipe buffer with the existing recipes we
            fetched from first step
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mppick_ingredients);
        meal_recipe_list = new ArrayList<Recipe>();
        existing_ingredients = new ArrayList<Ingredient>();

        // back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set up ui components
        confirmButton = findViewById(R.id.confirm_ingredient_selection_button);
        ingredient_list = findViewById(R.id.mp_ingredient_list);
        ingredient_search_bar = findViewById(R.id.searchview_ingredient);

        // Set up user here
        dbHandler = new DBHandler();

        // get meal
        intent =  getIntent();
        Bundle bundle  = intent.getExtras();
        meal = (Meal) bundle.getSerializable("MEAL");

        // get all the existing recipes for the meal first
        meal_recipe_list = meal.getRecipes();

        for (Recipe recipe : meal_recipe_list){

            int ingredients_count = recipe.getIngredients().size();
            if ( ingredients_count == 1){
                existing_ingredients.add(recipe.getIngredients().get(0));
            }
        }
        ingredient_adapter = new MPPickIngredientsAdapter(this, (ArrayList<Ingredient>) existing_ingredients,(ArrayList<Recipe>) meal_recipe_list);

        // set up search bar
        ingredient_list.setAdapter(ingredient_adapter);
        ingredient_search_bar.setOnQueryTextListener(this);

        // get user's recipes
        LoadingDialog dialog = new LoadingDialog(this);
        // TODO: fetch all ingredients
//        dbHandler
        // TODO: deal with fetch all user's recipe here.
        dbHandler.getUserRecipesForMeals(ingredient_adapter,dialog);

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
        ingredient_adapter.filter(entered_text);
        return false;
    }

    private void setOnConfirmButtonListener() {
        confirmButton.setOnClickListener(v -> {
            // update recipes for the meal
            if (ingredient_adapter.getIngredientsSelected().size()<1){
                createPopup("You haven't select anything yet");
            } else {

                List<Ingredient> r = ingredient_adapter.getAllIngredients();
                // TODO: update meal here
//                meal.setRecipes(r);
//                dbHandler.modifyMeal(meal);
//
//                Intent return_intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("Modified-Meal", meal);
//                return_intent.putExtras(bundle);
//                setResult(Activity.RESULT_OK,return_intent);
                this.finish();// get back to caller activity which is meal recipe list
            }

        });
    }

    /**
     * Set an listener on the items of the list
     * When clicked, either add or remove from the buffer
     */
    private void setOnListViewItemListener() {
        ingredient_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox checkBox = view.findViewById(R.id.checkBox);
                checkBox.toggle();
                if(checkBox.isChecked()){
                    ingredient_adapter.addToBuffer(i);
                } else {
                    ingredient_adapter.removeFromBuffer(i);
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