package com.example.happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.happymeals.databinding.ActivityMpmealListBinding;

import java.util.ArrayList;
import java.util.List;

public class MPMealListActivity extends AppCompatActivity {

    ActivityMpmealListBinding activityMpmealListBinding;
    RecyclerView meal_list;
    RecyclerView.Adapter mpMealListAdapter;
    MealPlan mealPlan;
    int dayIndex = 0;
    String Ump_id;
    ArrayList<Meal> meals;
    Button addMealsButton;
    Button nextDayButton;
    Button finishButton;
    Intent intent;
    DBHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMpmealListBinding = ActivityMpmealListBinding.inflate(getLayoutInflater());

        setContentView(activityMpmealListBinding.getRoot());

        db = new DBHandler();

        addMealsButton = activityMpmealListBinding.mealPlanAddButton;
        nextDayButton = activityMpmealListBinding.mpFabNextDay;
        finishButton = activityMpmealListBinding.mpFabFinish;
        meal_list = activityMpmealListBinding.mpMealListRecyclerview;

        Bundle bundle = getIntent().getExtras();
        meals = new ArrayList<>();

        if((boolean) bundle.getSerializable("IsNewMP")) {
            mealPlan = new MealPlan();
            db.addMealPlan(mealPlan, this);
        } else {
            meals.clear();
            mealPlan = (MealPlan) bundle.getSerializable("MEALPLAN");
            meals.add(mealPlan.getBreakfast().get(dayIndex));
            meals.add(mealPlan.getLunch().get(dayIndex));
            meals.add(mealPlan.getDinner().get(dayIndex));
        }


        //Testing
//        Ingredient ind = new Ingredient(3,"carrot");
//        List<String> comments = new ArrayList<>();
//        comments.add("LGTM!");
//        List<Ingredient> ingredients = new ArrayList<>();
//        ingredients.add(ind);
//        Recipe r1 = new Recipe("Greedy recipe",1,1,"vst", comments, ingredients);
//        List<Recipe> recipes = new ArrayList<>();
//        recipes.add(r1);
//        List<Double> scalings = new ArrayList<>();
//        scalings.add(1.11);
//        meals = new ArrayList<>();
//        meals.add(new Meal(recipes,scalings,3.4));

        mpMealListAdapter = new MPMealListAdapter(this, meals);
        meal_list.setLayoutManager(new GridLayoutManager(this, 1));
        meal_list.setAdapter(mpMealListAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final Meal meal = meals.get(viewHolder.getAdapterPosition());
                // delete mealPlan
            }
        });
        itemTouchHelper.attachToRecyclerView(activityMpmealListBinding.mpMealListRecyclerview);

        setOnAddButtonListener();
        setOnFinishButtonListener();

    }

    private void setOnAddButtonListener() {
        addMealsButton.setOnClickListener(v -> {
            // pass user id, mealplan index(or which day it is)
//            intent.putExtra();
            intent = new Intent(this, MPMyMealsActivity.class);
            startActivity(intent);

        });
    }

    private void setOnFinishButtonListener() {
        finishButton.setOnClickListener(v -> {
            if(mealPlan.getBreakfast().isEmpty()) {
                db.removeMealPlan(mealPlan, activityMpmealListBinding.getRoot().getContext());
            }
            finish();
        });
    }
}