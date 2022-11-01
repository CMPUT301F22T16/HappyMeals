package com.example.happymeals.recipe;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import com.example.happymeals.Ingredient;
import com.example.happymeals.R;

public class NewRecipe extends AppCompatActivity implements RecyclerViewInterface {

    Button recipe_img_picker_btn;
    RecyclerView recipe_ingredient_list;
    RecipeIngredientAdapter ingredient_adapter;
    ArrayList<Ingredient> ingredient_data_list;
    Button pick_new_ingredient_btn;
    Uri selected_img;

    ActivityResultLauncher<Intent> add_ingredient_for_result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            handleAddIngredientForResultLauncher(result);
        }
    });

    ActivityResultLauncher<Intent> add_img_for_result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            handleAddImgForResultLauncher(result);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        recipe_img_picker_btn = findViewById(R.id.recipe_img_picker_btn);

        recipe_ingredient_list = findViewById(R.id.recipe_ingredient_recyclerview);

        ingredient_data_list = new ArrayList<>();
        ingredient_data_list.add(new Ingredient("Vegetable","Carrot", 1, 1, null, null));
        ingredient_data_list.add(new Ingredient("Vegetable", "Broccoli", 1, 1, null, null));
        ingredient_data_list.add(new Ingredient("Meat", "Chicken", 1, 1, null, null));
        ingredient_data_list.add(new Ingredient("Dairy", "Milk", 1, 1, null, null));
        ingredient_data_list.add(new Ingredient("Meat", "Eggs", 1, 1, null, null));

        ingredient_adapter = new RecipeIngredientAdapter(this, ingredient_data_list, this);
        recipe_ingredient_list.setLayoutManager(new LinearLayoutManager(this));
        recipe_ingredient_list.setAdapter(ingredient_adapter);

        pick_new_ingredient_btn = findViewById(R.id.recipe_pick_new_ingredient_button);
        pick_new_ingredient_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewRecipe.this, RecipeAddIngredient.class);
                add_ingredient_for_result.launch(intent);
            }
        });

        recipe_img_picker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                add_img_for_result.launch(intent);
            }
        });
    }

    /**
     * This method handles the return value from the RecipeAddIngredient class
     * @param result the returned values from the RecipeAddIngredient class.
     *               The return values consists of the ingredient description and category
     */
    public void handleAddIngredientForResultLauncher(ActivityResult result) {
        if (result != null && result.getResultCode() == RESULT_OK) {
            if (result.getData() == null) return;
            String descExtra = result.getData().getStringExtra("desc");
            String categoryExtra = result.getData().getStringExtra("category");
            ingredient_data_list.add(new Ingredient(categoryExtra, descExtra, 1, 1, null, null));
            recipe_ingredient_list.setAdapter(ingredient_adapter);
        } else {
            Toast.makeText(NewRecipe.this, "Failed to add ingredient", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method handles the return value after the user selects an image from the gallery
     * @param result the Uri object after the user picks an image from the gallery
     */
    public void handleAddImgForResultLauncher(ActivityResult result) {
        if (result != null && result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                selected_img = result.getData().getData();
            } else {
                ;
            }
        } else {
            ;
        }
    }

    /**
     * This method removes the ingredient from the recipe after the user presses the delete button
     * @param position the index position of the ingredient in ingredient_data_list
     * @param op a string denoting the operation to perform. Here the operation is either delete or edit.
     */
    @Override
    public void onItemClick(int position, String op) {
        if (Objects.equals(op, "delete")) {
            ingredient_data_list.remove(position);
            recipe_ingredient_list.setAdapter(ingredient_adapter);
        }
    }
}