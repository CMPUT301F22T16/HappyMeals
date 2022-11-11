package com.example.happymeals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.databinding.ActivityMpmealRecipeListBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import java.util.ArrayList;
import java.util.List;

/**
 * This activity allows user to view all the recipes from the meal,
 * and also allows user to add new recipes
 */
public class MPMealRecipeList extends AppCompatActivity {

    ActivityMpmealRecipeListBinding activityMpmealRecipeListBinding;
    MPMealRecipeListAdapter mpMealRecipeListAdapter;

    List<Recipe> recipes;
    List<Recipe> recipes_old;
    Button addRecipButton;
    Button cancelButton;
    Button finishButton;
    Intent intent;
    RecyclerView recyclerView;
    DBHandler dbHandler;
    Meal meal;
    Context context;
    ActivityResultLauncher<Intent> activityLauncher;
    boolean is_new_meal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMpmealRecipeListBinding = ActivityMpmealRecipeListBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_mpmeal_recipe_list);
        context = this;

        recyclerView = findViewById(R.id.mp_recipe_list_recyclerview);
        addRecipButton = findViewById(R.id.mp_recipe_add_button);
        finishButton = findViewById(R.id.mpmeal_recipe_list_finish);
        cancelButton = findViewById(R.id.mpmeal_recipe_list_cancel);

        recipes = new ArrayList<>();
        recipes_old = new ArrayList<>();

        // set up users
        dbHandler = new DBHandler();


        // get the meal object passed in
        Bundle bundle = getIntent().getExtras();
        is_new_meal = (boolean) bundle.getSerializable("IsNewMeal");

        if (is_new_meal) {
            meal = new Meal();
            dbHandler.addMeal(meal);
        } else {
            meal = (Meal) bundle.getSerializable("MEAL");
            recipes = meal.getRecipes();
        }
        // keeps a copy of the recipes
        for (Recipe r : recipes){
            recipes_old.add(r);
        }

        // set up adapter
        mpMealRecipeListAdapter = new MPMealRecipeListAdapter(this, (ArrayList<Recipe>) recipes, dbHandler);
        recyclerView.setAdapter(mpMealRecipeListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        LoadingDialog dialog = new LoadingDialog(this);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int item_index = viewHolder.getAdapterPosition();
                mpMealRecipeListAdapter.delete(item_index);
                dbHandler.modifyMeal(meal);
            }
        });

        setOnAddButtonListener();
        setOnCancelButtonListener();
        setOnFinishButtonListener();
        setUpActivityLauncher();

        mpMealRecipeListAdapter.notifyDataSetChanged();
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void setUpActivityLauncher() {
        // this activity launcher was modified from Misha Akopov's answer(May 3, 2020) to this question:
        // https://stackoverflow.com/questions/61455381/how-to-replace-startactivityforresult-with-activity-result-apis
        activityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Bundle bundle = data.getExtras();
                        meal = (Meal) bundle.getSerializable("Modified-Meal");
                        recipes = meal.getRecipes();
                        mpMealRecipeListAdapter.setRecipesList((ArrayList<Recipe>) recipes);
                        mpMealRecipeListAdapter.notifyDataSetChanged();
                    }
                });

    }

    private void setOnAddButtonListener() {
        addRecipButton.setOnClickListener(v -> {
            showBottomSheetDialog();
        });
    }

    /**
     * On cancel, we discard the changes we made
     */
    private void setOnCancelButtonListener() {
        cancelButton.setOnClickListener(v -> {
            if(recipes.isEmpty() && is_new_meal) {
                dbHandler.removeMeal(meal);
                finish();
            }
            showAlertOnCancel();
        });
    }

    /**
     * build an alert dialog on cancel
     * code taken from David Hedlund's answer from here:
     * https://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android
     */
    public void showAlertOnCancel(){
        new AlertDialog.Builder(context)
                .setTitle("Discard changes")
                .setMessage("Changes will not be saved. Are you sure to proceed?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        if (is_new_meal){
                            dbHandler.removeMeal(meal);
                        } else {
                            meal.setRecipes(recipes_old);
                            dbHandler.modifyMeal(meal);
                        }
                        mpMealRecipeListAdapter.notifyDataSetChanged();
                        finish();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.meal_recipe_list_bottom_sheet);

        TextView add_from_recipe = bottomSheetDialog.findViewById(R.id.add_from_recipe_textview);
        TextView add_from_ingredient = bottomSheetDialog.findViewById(R.id.add_from_ingredient_textview);
        TextView cancel = bottomSheetDialog.findViewById(R.id.bottom_sheet_cancel);
        add_from_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                intent = new Intent(context, MPPickRecipeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("MEAL", meal);
                intent.putExtras(bundle);
                activityLauncher.launch(intent);
            }
        });

        add_from_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Share is Clicked", Toast.LENGTH_LONG).show();
                bottomSheetDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    private void setOnFinishButtonListener() {
        finishButton.setOnClickListener(v -> {
            finish();

        });
    }
}