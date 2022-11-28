/**
 * Acknowledgements:
 * 1. Stock recipe image taken from : https://pixabay.com/photos/cook-healthy-food-meal-vegetables-2364221/
 */

package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.happymeals.recipe.RecipeListActivity;
import com.example.happymeals.storage.StorageActivity;


import com.example.happymeals.meal.MPMyMealsActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton logoutButton;
    Button ingredientButton;
    Button mealButton;
    Button recipeButton;
    Button mealplanButton;
    Button storageButton;
    TextView userWelcome;
    String displayName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        setContentView(R.layout.activity_main);

        displayName = user.getDisplayName();
//        displayName = displayName.split(" ")[0];
        userWelcome = findViewById(R.id.userWelcome);
        userWelcome.setText("Hello,\n" + displayName);

        DBHandler db = new DBHandler(user.getUid());

        logoutButton = findViewById(R.id.logout_button);
        ingredientButton = findViewById(R.id.ingredient_button);
        mealButton = findViewById(R.id.meal_button);
        mealplanButton = findViewById(R.id.mealplan_button);
        recipeButton = findViewById(R.id.recipe_button);
        storageButton = findViewById(R.id.storage_button);
        ingredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, IngredientActivity.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MPMyMealsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Is-From-MealPlan", false);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mealplanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MPMealPlanActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecipeListActivity.class);
                Bundle bundle = new Bundle();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                bundle.putSerializable("USER", user.getUid());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        storageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StorageActivity.class);
                Bundle bundle = new Bundle();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                bundle.putSerializable("USER", user.getUid());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
