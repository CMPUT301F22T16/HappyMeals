package com.example.happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.happymeals.databinding.ActivityMpmyMealsBinding;

import java.util.ArrayList;
import java.util.List;

public class MPMyMealsActivity extends AppCompatActivity {

    ActivityMpmyMealsBinding activityMpmyMealsBinding;
    RecyclerView.Adapter myMealsAdapter;
    ArrayList<Meal> meals;
    Button cancel_button;
    Button finish_button;
    Button add_button;
    Intent intent;
    RecyclerView recyclerView;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMpmyMealsBinding = ActivityMpmyMealsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_mpmy_meals);
        recyclerView = findViewById(R.id.my_meals_recyclerview);

        //Testing
        Ingredient ind = new Ingredient(3,"carrot");
        List<String> comments = new ArrayList<>();
        comments.add("LGTM!");
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ind);
        Recipe r1 = new Recipe("Greedy recipe",1,1,"vst", comments, ingredients);
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(r1);
        List<Double> scalings = new ArrayList<>();
        scalings.add(1.11);
        meals = new ArrayList<>();
        meals.add(new Meal(recipes,scalings,3.4));

        cancel_button = findViewById(R.id.my_meals_cancel);
        finish_button = findViewById(R.id.my_meals_finish);
        add_button = findViewById(R.id.my_meals_add_button);
        intent = new Intent(this,MPMealRecipeList.class);

        myMealsAdapter = new MPMyMealsAdapter(this, meals);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(myMealsAdapter);

        // testing with firebase
        user = new User();
        LoadingDialog dialog = new LoadingDialog(this);
        user.getUserMeals((MPMyMealsAdapter) myMealsAdapter,dialog,this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final Meal meal = meals.get(viewHolder.getAdapterPosition());
                // TODO: delete mealPlan
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        setOnCancelButtonListener();
        setOnFinishButtonListener();
        setOnAddButtonListener();
    }
    private void setOnCancelButtonListener() {
        cancel_button.setOnClickListener(v -> {
            // maybe pop up that all changes will not be saved. sure to exist? pop up alert
            finish();
        });
    }
    private void setOnAddButtonListener() {
        add_button.setOnClickListener(v -> {
            // TODO: pass user id, indexed meal
            startActivity(intent);

        });
    }
    private void setOnFinishButtonListener() {
        finish_button.setOnClickListener(v -> {
            // TODO:update the meal plan at the passed in index day
            finish();

        });
    }
}