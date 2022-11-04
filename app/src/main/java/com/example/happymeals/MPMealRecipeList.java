package com.example.happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.happymeals.databinding.ActivityMpmealRecipeListBinding;

import java.util.ArrayList;
import java.util.List;

public class MPMealRecipeList extends AppCompatActivity {

    ActivityMpmealRecipeListBinding activityMpmealRecipeListBinding;
    MPMealRecipeListAdapter mpMealRecipeListAdapter;

    ArrayList<Recipe> recipes;
    Button addRecipButton;
    Button cancelButton;
    Button finishButton;
    Intent intent;
    RecyclerView recyclerView;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMpmealRecipeListBinding = ActivityMpmealRecipeListBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_mpmeal_recipe_list);

        // for testing
        recyclerView = findViewById(R.id.mp_recipe_list_recyclerview);
//        Ingredient ind = new Ingredient(3,"carrot");
//        List<String> comments = new ArrayList<>();
//        comments.add("LGTM!");
//        List<Ingredient> ingredients = new ArrayList<>();
//        ingredients.add(ind);
//        Recipe r1 = new Recipe("Greedy recipe",1,1,"vst", comments, ingredients);
        recipes = new ArrayList<>();
//        recipes.add(r1);
        user = new User();

        addRecipButton = findViewById(R.id.mp_recipe_add_button);
        finishButton = findViewById(R.id.mpmeal_recipe_list_finish);
        cancelButton = findViewById(R.id.mpmeal_recipe_list_cancel);
        intent = new Intent(this,MPPickRecipeActivity.class);

        // get the meal object passed in
        Bundle bundle  = getIntent().getExtras();
        Meal meal = (Meal) bundle.getSerializable("MEAL");
        if (meal == null){
            Meal empty_meal = new Meal();
//            m_id = user.addMeal(empty_meal,this);
        }
        // TODO: change to meal instead of using m_id
        mpMealRecipeListAdapter = new MPMealRecipeListAdapter(this, recipes);
        recyclerView.setAdapter(mpMealRecipeListAdapter);
//        mpMealRecipeListAdapter.setMid(m_id);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        LoadingDialog dialog = new LoadingDialog(this);
        user.getRecipesForMeal((MPMealRecipeListAdapter) mpMealRecipeListAdapter,dialog,this);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                mpMealRecipeListAdapter.delete(viewHolder.getAdapterPosition());
            }
        });
        setOnAddButtonListener();
        setOnCancelButtonListener();
        setOnFinishButtonListener();

        mpMealRecipeListAdapter.notifyDataSetChanged();
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setOnAddButtonListener() {
        addRecipButton.setOnClickListener(v -> {
            intent.putExtra("Meal_ID",mpMealRecipeListAdapter.getMid());
            startActivity(intent);

        });
    }

    private void setOnCancelButtonListener() {
        cancelButton.setOnClickListener(v -> {
            // TODO: maybe a confirmation dialog to confirm cancel action?
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