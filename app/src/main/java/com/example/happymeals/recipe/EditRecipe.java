package com.example.happymeals.recipe;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import com.example.happymeals.DBHandler;
import com.example.happymeals.Recipe;
import com.example.happymeals.RecipeIngredient;
import com.example.happymeals.UserIngredient;
import com.example.happymeals.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This class creates the EditRecipe Activity for the user to edit a recipe
 * @author John Yu
 */
public class EditRecipe extends AppCompatActivity implements RecipeViewCommentsFragment.OnFragmentInteractionListener, RecipeViewIngredientsFragment.OnFragmentInteractionListener {

    /**
     * This variable stores the button to pick a new image for the recipe
     */
    Button recipe_img_picker_btn;

    /**
     * This variable stores the data list of comments
     */
    ArrayList<String> recipe_comments_data_list;

    /**
     * This variable stores the data list of ingredients
     */
    ArrayList<RecipeIngredient> recipeIngredient_data_list;

    /**
     * This is a button for the user to pick a new ingredient
     */
    ExtendedFloatingActionButton pick_new_ingredient_btn;

    /**
     * This is a button for the user to add a new comment
     */
    ExtendedFloatingActionButton recipe_new_comment_btn;

    /**
     * This is a button for the user to view comments.
     */
    ExtendedFloatingActionButton recipe_view_comments_btn;

    /**
     * This is a button for the user to view ingredients.
     */
    ExtendedFloatingActionButton recipe_view_ingredients_btn;

    /**
     * This is a button to submit the changes back to the parent activity which is {@link com.example.happymeals.RecipeListActivity}
     */
    Button recipe_submit_btn;

    /**
     * This variable stores the photograph of the recipe
     */
    String selected_img = "";

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
     * This is the database handler that handles all things related to the firebase database.
     */
    DBHandler db;

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
     * This creates an ActivityResultLauncher when launched will open a gallery for the user to select their image.
     */
    ActivityResultLauncher<Intent> add_img_for_result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            handleAddImgForResultLauncher(result);
            Toast.makeText(EditRecipe.this, "Successfully added a new image.", Toast.LENGTH_SHORT).show();
        }
    });

    /**
     * This creates an ActivityResultLauncher where the user can send and receive data to the {@link RecipeAddComment} class
     */
    ActivityResultLauncher<Intent> add_comment_for_result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            handleAddCommentForResultLauncher(result);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        // back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Recipe");

        Intent intent = getIntent();

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = new DBHandler("Guest");

        // Initialize widgets
        recipe_img_picker_btn = findViewById(R.id.recipe_img_picker_btn);

        recipeTitleEditText = findViewById(R.id.recipe_title_edit_text);
        recipePrepTimeEditText = findViewById(R.id.recipe_prep_time_edit_text);
        recipeNumServEditText = findViewById(R.id.recipe_num_serv_edit_text);
        recipeCategoryEditText = findViewById(R.id.recipe_category_edit_text);

        // Set data from RecipeListActivity
        String operation = intent.getStringExtra("operation");
        if (operation.equals("add")) {
            recipeTitleEditText.setText("");
            recipePrepTimeEditText.setText("");
            recipeNumServEditText.setText("");
            recipeCategoryEditText.setText("");
            recipe_comments_data_list = new ArrayList<>();
            recipeIngredient_data_list = new ArrayList<>();
        } else if (operation.equals("edit")) {
            recipeTitleEditText.setText(intent.getStringExtra("title"));
            recipePrepTimeEditText.setText(getString(R.string.integer_to_string, intent.getIntExtra("preparation_time", 0)));
            recipeNumServEditText.setText(getString(R.string.integer_to_string, intent.getIntExtra("num_servings", 0)));
            recipeCategoryEditText.setText(intent.getStringExtra("category"));
            recipe_comments_data_list = (ArrayList<String>) intent.getSerializableExtra("comments");
            recipeIngredient_data_list = (ArrayList<RecipeIngredient>) intent.getSerializableExtra("ingredients");
            selected_img = intent.getStringExtra("photo");
        }


        // Create the button click listener to view comments.
        recipe_view_comments_btn = findViewById(R.id.recipe_view_comments_button);
        recipe_view_comments_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RecipeViewCommentsFragment(EditRecipe.this, recipe_comments_data_list).show(getSupportFragmentManager(), "View Comments");
            }
        });

        // Create the button click listener to view ingredients.
        recipe_view_ingredients_btn = findViewById(R.id.recipe_view_ingredients_button);
        recipe_view_ingredients_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RecipeViewIngredientsFragment(EditRecipe.this, recipeIngredient_data_list).show(getSupportFragmentManager(), "View Ingredients");
            }
        });

        // Handle add new ingredient button
        pick_new_ingredient_btn = findViewById(R.id.recipe_pick_new_ingredient_button);
        pick_new_ingredient_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditRecipe.this, RecipeAddIngredient.class);
                add_ingredient_for_result.launch(intent);
            }
        });

        // Handle add new image button
        recipe_img_picker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                add_img_for_result.launch(intent);
            }
        });

        // Handle add new comment button
        recipe_new_comment_btn = findViewById(R.id.recipe_add_new_comment_button);
        recipe_new_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditRecipe.this, RecipeAddComment.class);
                add_comment_for_result.launch(intent);
            }
        });

        // Handle submit button
        recipe_submit_btn = findViewById(R.id.recipe_submit_button);
        recipe_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("title", recipeTitleEditText.getText().toString());
                intent.putExtra("prep_time", Integer.parseInt(recipePrepTimeEditText.getText().toString()));
                intent.putExtra("num_serv", Integer.parseInt(recipeNumServEditText.getText().toString()));
                intent.putExtra("category", recipeCategoryEditText.getText().toString());
                intent.putExtra("comments", recipe_comments_data_list);
                intent.putExtra("ingredients", recipeIngredient_data_list);
                intent.putExtra("photo", selected_img);


                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * This method handles the return value from the {@link RecipeAddIngredient} Activity.
     * @param result the returned values from the {@link RecipeAddIngredient} Activity..
     *               The return values consists of the ingredient description and category
     */
    public void handleAddIngredientForResultLauncher(ActivityResult result) {
        if (result != null && result.getResultCode() == RESULT_OK) {
            if (result.getData() == null) return;
            String descriptionExtra = result.getData().getStringExtra("description");
            String categoryExtra = result.getData().getStringExtra("category");
            Double amountExtra = result.getData().getDoubleExtra("amount", 0.00);
            String amountUnitExtra = result.getData().getStringExtra("amount_unit");
            recipeIngredient_data_list.add(new RecipeIngredient(descriptionExtra, categoryExtra, amountExtra));
//            recipe_ingredient_list.setAdapter(ingredient_adapter);
        } else {
            Toast.makeText(EditRecipe.this, "Failed to add ingredient", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method handles the return value after the user adds a comment.
     * @param result the returned value from the {@link RecipeAddComment} Activity
     */
    public void handleAddCommentForResultLauncher(ActivityResult result) {
        if (result != null && result.getResultCode() == RESULT_OK) {
            if (result.getData() == null) return;
            String commentExtra = result.getData().getStringExtra("comment");
            recipe_comments_data_list.add(commentExtra);
//            recipe_comments_list.setAdapter(comments_adapter);
        } else {
            Toast.makeText(EditRecipe.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method handles the return value after the user selects an image from the gallery
     * @param result the Uri object after the user picks an image from the gallery
     */
    public void handleAddImgForResultLauncher(ActivityResult result) {
        if (result != null && result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                selected_img = result.getData().getData().toString();
            } else {
                ;
            }
        } else {
            ;
        }
    }

    /**
     * handles on back button clicked,
     * returns to home
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOkPressed_Comment(ArrayList<String> data_list) {
        recipe_comments_data_list = data_list;
    }

    @Override
    public void onOkPressed_Ingredient(ArrayList<RecipeIngredient> data_list) {
        recipeIngredient_data_list = data_list;
    }
}