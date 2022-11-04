package com.example.happymeals;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.happymeals.databinding.ActivityMpmealRecipeListBinding;

import java.util.ArrayList;
import java.util.List;

public class MPMealRecipeList extends AppCompatActivity {

    ActivityMpmealRecipeListBinding activityMpmealRecipeListBinding;
    MPMealRecipeListAdapter mpMealRecipeListAdapter;

    List<Recipe> recipes;
    Button addRecipButton;
    Button cancelButton;
    Button finishButton;
    Intent intent;
    RecyclerView recyclerView;
    DBHandler dbHandler;
    Meal meal;
    Context context;
    ActivityResultLauncher<Intent> activityLauncher;


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

        // set up users
        dbHandler = new DBHandler();



        // get the meal object passed in
        Bundle bundle  = getIntent().getExtras();
        boolean is_new_meal = (boolean) bundle.getSerializable("IsNewMeal");

        if (is_new_meal){
            meal = new Meal();
            dbHandler.addMeal(meal,this);
        } else {
            meal = (Meal) bundle.getSerializable("MEAL");
            recipes = meal.getRecipes();
        }

        // set up adapter
        mpMealRecipeListAdapter = new MPMealRecipeListAdapter(this, (ArrayList<Recipe>) recipes,dbHandler);
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
                meal.removeRecipe(item_index); // Be catious of this when implement sort feature
                dbHandler.modifyMeal(meal,context);
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
            intent = new Intent(this,MPPickRecipeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("MEAL", meal);
            intent.putExtras(bundle);
            activityLauncher.launch(intent);

        });
    }

    private void setOnCancelButtonListener() {
        cancelButton.setOnClickListener(v -> {
            // TODO: do we need this?
//            finish();
            mpMealRecipeListAdapter.notifyDataSetChanged();
        });
    }

    private void setOnFinishButtonListener() {
        finishButton.setOnClickListener(v -> {
            // TODO: add all the recipes to the meal,(modify meal), or insert the newly created meal
            finish();

        });
    }
}