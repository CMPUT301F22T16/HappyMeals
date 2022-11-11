package com.example.happymeals.recipe;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import com.example.happymeals.DBHandler;
import com.example.happymeals.Ingredient;
import com.example.happymeals.LoadingDialog;
import com.example.happymeals.R;

/**
 * This class creates the EditRecipe Activity for the user to edit a recipe
 * @author John Yu
 */
public class EditRecipe extends AppCompatActivity implements RecyclerViewInterface {

    /**
     * This variable stores the button to pick a new image for the recipe
     */
    Button recipe_img_picker_btn;

    /**
     * This variable stores the recyclerview that displays the ingredients contained in the recipe
     */
    RecyclerView recipe_ingredient_list;

    /**
     * This variable stores the RecyclerView Adapter to displays the ingredients in the RecyclerView
     */
    RecipeIngredientAdapter ingredient_adapter;

    /**
     * This variable stores the data list of ingredients
     */
    ArrayList<Ingredient> ingredient_data_list;

    /**
     * This is a button for the user to pick a new ingredient
     */
    Button pick_new_ingredient_btn;

    /**
     * This is a button to submit the changes back to the parent activity which is {@link com.example.happymeals.RecipeListActivity}
     */
    Button recipe_submit_btn;

    /**
     * This variable stores the photograph of the recipe
     */
    Uri selected_img;

    /**
     * This variable keeps track of the ingredient index position that the user has selected
     */
    int selection = -1;

    /**
     * This is an EditText where the user can edit the title of their recipe
     */
    EditText recipeTitleEditText;

    /**
     * This is an EditText where the user can edit the preparation time of their recipe
     */
    EditText recipePrepTimeEditText;

    /**
     * This is an EditText where the user can edit the number of servings of their recipe
     */
    EditText recipeNumServEditText;

    /**
     * This is an EditText where the user can edit the category of their recipe
     */
    EditText recipeCategoryEditText;

    /**
     * This creates an ActivityResultLauncher where the user can send and receive data to the {@link RecipeAddIngredient} class.
     */
    ActivityResultLauncher<Intent> add_ingredient_for_result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            handleAddIngredientForResultLauncher(result);
        }
    });

    /**
     * This creates an ActivityResultLauncher where the user can send and receive data to the {@link RecipeEditIngredient} class
     */
    ActivityResultLauncher<Intent> edit_ingredient_for_result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            handleEditIngredientForResultLauncher(result);
        }
    });

    /**
     * This creates an ActivityResultLauncher when launched will open a gallery for the user to select their image.
     */
    ActivityResultLauncher<Intent> add_img_for_result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            handleAddImgForResultLauncher(result);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        Intent intent = getIntent();

        Bundle bundle = getIntent().getExtras();
        String username = (String) bundle.getSerializable("USER");
        DBHandler db = new DBHandler(username);

        recipe_img_picker_btn = findViewById(R.id.recipe_img_picker_btn);

        recipe_ingredient_list = findViewById(R.id.recipe_ingredient_recyclerview);

        recipeTitleEditText = findViewById(R.id.recipe_title_edit_text);
        recipePrepTimeEditText = findViewById(R.id.recipe_prep_time_edit_text);
        recipeNumServEditText = findViewById(R.id.recipe_num_serv_edit_text);
        recipeCategoryEditText = findViewById(R.id.recipe_category_edit_text);

        recipeTitleEditText.setText(intent.getStringExtra("title"));
        recipePrepTimeEditText.setText(getString(R.string.integer_to_string, intent.getIntExtra("preparation_time", 0)));
        recipeNumServEditText.setText(getString(R.string.integer_to_string, intent.getIntExtra("num_servings", 0)));
        recipeCategoryEditText.setText(intent.getStringExtra("category"));



        ingredient_data_list = (ArrayList<Ingredient>) intent.getSerializableExtra("ingredients");

        ingredient_adapter = new RecipeIngredientAdapter(this, ingredient_data_list, this);
        recipe_ingredient_list.setLayoutManager(new LinearLayoutManager(this));
        recipe_ingredient_list.setAdapter(ingredient_adapter);

        pick_new_ingredient_btn = findViewById(R.id.recipe_pick_new_ingredient_button);
        pick_new_ingredient_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditRecipe.this, RecipeAddIngredient.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("USER", db.getUsername());
                intent.putExtras(bundle);
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

        recipe_submit_btn = findViewById(R.id.recipe_submit_button);
        recipe_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("title", recipeTitleEditText.getText().toString());
                intent.putExtra("prep_time", Integer.parseInt(recipePrepTimeEditText.getText().toString()));
                intent.putExtra("num_serv", Integer.parseInt(recipeNumServEditText.getText().toString()));
                intent.putExtra("category", recipeCategoryEditText.getText().toString());
                intent.putExtra("ingredients", ingredient_data_list);
                setResult(RESULT_OK, intent);
                finish();
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
//            String descriptionExtra = result.getData().getStringExtra("description");
//            Integer amountExtra = Integer.parseInt(result.getData().getStringExtra("amount"));
//            ingredient_data_list.add(new Ingredient(amountExtra, descriptionExtra));
//            recipe_ingredient_list.setAdapter(ingredient_adapter);
            Ingredient item = (Ingredient) result.getData().getSerializableExtra("ingredient");
            ingredient_data_list.add(item);
            recipe_ingredient_list.setAdapter(ingredient_adapter);
        } else {
            Toast.makeText(EditRecipe.this, "Failed to add ingredient", Toast.LENGTH_SHORT).show();
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
     * This method handles the return value after the user edits an ingredient
     * @param result the returned value from the RecipeEditIngredient Activity
     */
    public void handleEditIngredientForResultLauncher(ActivityResult result) {
        if (result != null && result.getResultCode() == RESULT_OK) {
            if (result.getData() == null) return;
            String desc = result.getData().getStringExtra("desc");
            String loc = result.getData().getStringExtra("loc");
            String category = result.getData().getStringExtra("category");
            int amount = result.getData().getIntExtra("amount", 0);
            double cost = result.getData().getDoubleExtra("cost", 0.00);
            Ingredient item = ingredient_data_list.get(selection);
            item.setDescription(desc);
            item.setLoc(loc);
            item.setCategory(category);
            item.setAmount(amount);
            item.setCost(cost);
            recipe_ingredient_list.setAdapter(ingredient_adapter);
        } else {
            Toast.makeText(EditRecipe.this, "Failed to edit ingredient", Toast.LENGTH_SHORT).show();
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
        } else {
            selection = position;  // This variable stores the index position of the ingredient being edited
            Intent intent = new Intent(EditRecipe.this, RecipeEditIngredient.class);
            Ingredient item = ingredient_data_list.get(position);
            intent.putExtra("desc", item.getDescription());
            intent.putExtra("loc", item.getLoc());
            intent.putExtra("date", item.getDate().getTime());
            intent.putExtra("category", item.getCategory());
            intent.putExtra("amount", item.getAmount());
            intent.putExtra("cost", item.getCost());
            edit_ingredient_for_result.launch(intent);
        }
    }
}