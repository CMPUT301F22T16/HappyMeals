package com.example.happymeals;

import static java.sql.Types.NULL;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.MutableBoolean;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.happymeals.databinding.ActivityMpmyMealsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This activity can be started either from the Main activity
 * or MPMealList activity.
 * If it is started from main activity, it will display
 * all user's meals
 * If otherwise, it will dispaly all the meals in the meal plan
 */
public class MPMyMealsActivity extends AppCompatActivity {

    ActivityMpmyMealsBinding activityMpmyMealsBinding;
    MPMyMealsAdapter myMealsAdapter;
    ArrayList<Meal> meals;
    MealPlan mealPlan;
    Button cancel_button;
    Button finish_button;
    Button edit_addtomp_button;
    Button add_button;
    Intent intent;
    RecyclerView recyclerView;
    DBHandler dbHandler;
    String userName;
    int dayIndex;
    int mealIndex;
    AtomicBoolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMpmyMealsBinding = ActivityMpmyMealsBinding.inflate(getLayoutInflater());
        setContentView(activityMpmyMealsBinding.getRoot());

        // back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Meals");

        // User
        Bundle bundle = getIntent().getExtras();
        userName = (String) bundle.getSerializable("USER");
        Boolean is_from_meal_plan = (Boolean) bundle.getSerializable("Is-From-MealPan");
        if (is_from_meal_plan) {
            mealPlan = (MealPlan) bundle.getSerializable("MEALPLAN");
            dayIndex = (int) bundle.getSerializable("DAY");
            mealIndex = (int) bundle.getSerializable("MEAL");
        }

        dbHandler = new DBHandler(userName);
        isEdit = new AtomicBoolean(false);

        meals = new ArrayList<>();
        recyclerView = activityMpmyMealsBinding.myMealsRecyclerview;
        finish_button = activityMpmyMealsBinding.myMealsFinish;
        add_button = activityMpmyMealsBinding.myMealsAddButton;
        edit_addtomp_button = activityMpmyMealsBinding.myMealsEditAddtompButton;
        intent = new Intent(this,MPMealRecipeList.class);

        if (is_from_meal_plan){
            myMealsAdapter = new MPMyMealsAdapter(this, meals, dbHandler, isEdit, dayIndex, mealIndex, mealPlan);
        } else{
            myMealsAdapter = new MPMyMealsAdapter(this, meals, dbHandler,isEdit);
        }
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(myMealsAdapter);

        LoadingDialog dialog = new LoadingDialog(this);
        dbHandler.getUserMeals((MPMyMealsAdapter) myMealsAdapter,dialog,this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                myMealsAdapter.remove(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        if (!is_from_meal_plan){edit_addtomp_button.setVisibility(View.INVISIBLE);
        }
        setOnEditAddToMPButtonListener();
        setOnFinishButtonListener();
        setOnAddButtonListener();
    }

    private void setOnAddButtonListener() {
        add_button.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("IsNewMeal", true);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setOnEditAddToMPButtonListener() {
        edit_addtomp_button.setOnClickListener(v -> {
            if(isEdit.get()) {
                isEdit.set(false);
                edit_addtomp_button.setText(getResources().getString(R.string.meal_plan_edit));
            } else {
                isEdit.set(true);
                edit_addtomp_button.setText(getResources().getString(R.string.meal_plan_addtomp));
            }
        });
    }

    private void setOnFinishButtonListener() {
        finish_button.setOnClickListener(v -> {
            finish();
        });
    }

    // https://stackoverflow.com/questions/14545139/android-back-button-in-the-title-bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}