package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.happymeals.databinding.ActivityMainBinding;
import com.example.happymeals.meal.MPMyMealsActivity;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    Button ingredientButton;
    Button mealButton;
    Button recipeButton;
    Button mealplanButton;
    TextView userWelcome;
    String userId;
    String displayName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(activityMainBinding.getRoot());
        userId = getIntent().getStringExtra("USERID");
        displayName = getIntent().getStringExtra("DISPLAY_NAME");
        userWelcome = findViewById(R.id.userWelcome);
        userWelcome.setText("Welcome, " + displayName + "!");
        DBHandler db = new DBHandler(userId);

        ingredientButton = findViewById(R.id.ingredient_button);
        mealButton = findViewById(R.id.meal_button);
        mealplanButton = findViewById(R.id.mealplan_button);
        recipeButton = findViewById(R.id.recipe_button);
        ingredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, IngredientActivity.class);
                intent.putExtra("USERID", userId);
                startActivity(intent);
            }
        });

        mealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MPMyMealsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("USERID", userId);
                bundle.putSerializable("Is-From-MealPlan",false);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mealplanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MPMealPlanActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("USERID", userId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecipeListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("USERID", userId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
