package com.example.happymeals.meal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.DBHandler;
import com.example.happymeals.LoadingDialog;
import com.example.happymeals.R;
import com.example.happymeals.Recipe;
import com.example.happymeals.RecipeIngredient;
import com.example.happymeals.databinding.ActivityMpmealRecipeListBinding;
import com.example.happymeals.recipe.EditRecipe;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This activity allows user to view all the recipes from the meal,
 * and also allows user to add new recipes
 */
public class MPMealRecipeList extends AppCompatActivity {

    ActivityMpmealRecipeListBinding activityMpmealRecipeListBinding;
    MPMealRecipeListAdapter mpMealRecipeListAdapter;

    Button addRecipButton;
    Button cancelButton;
    Button finishButton;
    Intent intent;
    RecyclerView recyclerView;
    DBHandler dbHandler;
    Meal meal;
    Context context;
    boolean is_new_meal;
    boolean is_modified;

    ActivityResultLauncher<Intent> add_recipe_for_result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            handleAddRecipeForResultLauncher(result);
        }
    });

    ActivityResultLauncher<Intent> modify_meal_for_result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            handleModifyMealForResultLauncher(result);
        }
    });


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

        is_modified = false;

        // set up users
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        dbHandler = new DBHandler(user.getUid());


        // get the meal object passed in
        Bundle bundle = getIntent().getExtras();
        is_new_meal = (boolean) bundle.getSerializable("IsNewMeal");

        if (is_new_meal) {
            meal = new Meal();
        } else {
            meal = (Meal) bundle.getSerializable("MEAL");
        }

        // set up adapter
        mpMealRecipeListAdapter = new MPMealRecipeListAdapter(this, meal);
        recyclerView.setAdapter(mpMealRecipeListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int item_index = viewHolder.getAdapterPosition();
                mpMealRecipeListAdapter.delete(item_index);
                is_modified = true;

            }
        });

        setOnAddButtonListener();
        setOnCancelButtonListener();
        setOnFinishButtonListener();

        mpMealRecipeListAdapter.notifyDataSetChanged();
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    private void handleModifyMealForResultLauncher(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            Bundle bundle = data.getExtras();
            ArrayList<Recipe> recipes = (ArrayList<Recipe>) bundle.getSerializable("Updated-Meal-Recipes");
            mpMealRecipeListAdapter.updateRecipesList((ArrayList<Recipe>) recipes);
            mpMealRecipeListAdapter.notifyDataSetChanged();
            is_modified = true;
        }
    }

    private void handleAddRecipeForResultLauncher(ActivityResult result) {
        if (result != null && result.getResultCode() == RESULT_OK) {
            if (result.getData() == null) return;
            String title = result.getData().getStringExtra("title");
            int prepTime = result.getData().getIntExtra("prep_time", 0);
            int numServ = result.getData().getIntExtra("num_serv", 0);
            String category = result.getData().getStringExtra("category");
            List<String> comments = (ArrayList<String>) result.getData().getSerializableExtra("comments");
            List<RecipeIngredient> ing = (ArrayList<RecipeIngredient>) result.getData().getSerializableExtra("ingredients");
            String filetype = result.getData().getStringExtra("filetype");

            String uriStr = result.getData().getStringExtra("photo");
            Uri uri = null;
            if (!Objects.equals(uriStr, "")) {
                uri = Uri.parse(uriStr);
            }
            Recipe newRecipe = new Recipe(title, prepTime, numServ, category, comments, ing);
            newRecipe.setDownloadUri(uriStr);
            dbHandler.addRecipe(newRecipe);
            mpMealRecipeListAdapter.updateRecipesList((ArrayList<Recipe>) meal.getRecipes());
            mpMealRecipeListAdapter.notifyDataSetChanged();

//            ContentResolver cR = this.getContentResolver();
//            String type = cR.getType(uri);
            dbHandler.uploadImage(uri, newRecipe, filetype);
            is_modified = true;
        } else {
            Toast.makeText(context, "Failed to add recipe", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * on add, a bottom sheet dialog is displayed
     * for user to decide how they want to add
     * a new recipe to the meal list
     */
    private void setOnAddButtonListener() {
        addRecipButton.setOnClickListener(v -> {
            showBottomSheetDialogOnAdd();
        });
    }

    /**
     * On cancel, we discard the changes we made
     */
    private void setOnCancelButtonListener() {
        cancelButton.setOnClickListener(v -> {
            if (is_modified) {
                showAlertOnCancel();
            } else {
                finish();
            }
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
                        mpMealRecipeListAdapter.notifyDataSetChanged();
                        finish();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Creates and show a bottom sheet dialog.
     * the bottom sheet provides user with two choices,
     * whether to add a recipe from user's recipe list
     * or create a new recipe and add to the recipe list
     * for the meal.
     */
    private void showBottomSheetDialogOnAdd() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.meal_recipe_list_bottom_sheet);

        TextView add_from_recipe = bottomSheetDialog.findViewById(R.id.bottom_sheet_textview1);
        TextView create_recipe = bottomSheetDialog.findViewById(R.id.bottom_sheet_textview2);
        TextView cancel = bottomSheetDialog.findViewById(R.id.bottom_sheet_cancel);
        add_from_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                intent = new Intent(context, MPPickRecipeActivity.class);
                Bundle bundle = new Bundle();
                meal = mpMealRecipeListAdapter.getMeal();
                bundle.putSerializable("MEAL", meal);
                intent.putExtras(bundle);
                modify_meal_for_result.launch(intent);
                bottomSheetDialog.dismiss();
            }
        });

        create_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditRecipe.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("USER", dbHandler.getUsername());
                intent.putExtras(bundle);
                // The operation extra tells the EditRecipe Activity whether it is adding or editing a recipe
                intent.putExtra("operation", "add");
                add_recipe_for_result.launch(intent);
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
            meal = mpMealRecipeListAdapter.getMeal();
            if (is_new_meal) {
                dbHandler.addMeal(meal);
            }
            dbHandler.modifyMeal(meal);
            finish();
        });
    }
}