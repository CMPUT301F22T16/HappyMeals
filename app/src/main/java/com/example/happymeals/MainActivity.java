package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.happymeals.databinding.ActivityMainBinding;
import com.example.happymeals.storage.StorageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
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
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        setContentView(activityMainBinding.getRoot());

        displayName = user.getDisplayName();
        displayName = displayName.substring(0, displayName.indexOf(' '));
        userWelcome = findViewById(R.id.userWelcome);
        userWelcome.setText("Welcome, " + displayName + "!");

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

        mealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MPMyMealsActivity.class);
                Bundle bundle = new Bundle();
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
