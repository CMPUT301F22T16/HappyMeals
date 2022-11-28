package com.example.happymeals.meal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.happymeals.DBHandler;
import com.example.happymeals.LoadingDialog;
import com.example.happymeals.mealplan.MealPlan;
import com.example.happymeals.R;
import com.example.happymeals.databinding.ActivityMpmyMealsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
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
    FloatingActionButton edit_addtomp_button;
    FloatingActionButton add_button;
    Intent intent;
    RecyclerView recyclerView;
    DBHandler dbHandler;
    int dayIndex;
    int mealIndex;
    AtomicBoolean isEdit;
    Boolean is_from_meal_plan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMpmyMealsBinding = ActivityMpmyMealsBinding.inflate(getLayoutInflater());
        setContentView(activityMpmyMealsBinding.getRoot());
        is_from_meal_plan = false;

        // back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Meals");

        // User
        Bundle bundle = getIntent().getExtras();
        is_from_meal_plan = (Boolean) bundle.getSerializable("Is-From-MealPlan");
        if (is_from_meal_plan) {
            mealPlan = (MealPlan) bundle.getSerializable("MEALPLAN");
            dayIndex = (int) bundle.getSerializable("DAY");
            mealIndex = (int) bundle.getSerializable("MEAL");
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        dbHandler = new DBHandler(user.getUid());
        isEdit = new AtomicBoolean(false);

        meals = new ArrayList<>();
        recyclerView = activityMpmyMealsBinding.myMealsRecyclerview;
        finish_button = activityMpmyMealsBinding.myMealsFinish;
        add_button = activityMpmyMealsBinding.myMealsAddButton;
        edit_addtomp_button = activityMpmyMealsBinding.myMealsEditAddtompButton;
        intent = new Intent(this, MPMealRecipeList.class);

        if (is_from_meal_plan){
            myMealsAdapter = new MPMyMealsAdapter(this, meals, dbHandler, isEdit, dayIndex, mealIndex, mealPlan);
        } else{
            myMealsAdapter = new MPMyMealsAdapter(this, meals, dbHandler,isEdit);
        }
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(myMealsAdapter);

        LoadingDialog dialog = new LoadingDialog(this);
        dbHandler.getUserMeals((MPMyMealsAdapter) myMealsAdapter,dialog);

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
        } else {
            finish_button.setVisibility(View.INVISIBLE);
            add_button.setVisibility(View.INVISIBLE);
        }
        setOnEditAddToMPButtonListener();
        setOnFinishButtonListener();
        setOnAddButtonListener();
        myMealsAdapter.notifyDataSetChanged();
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
                finish_button.setVisibility(View.VISIBLE);
                add_button.setVisibility(View.VISIBLE);
                edit_addtomp_button.setVisibility(View.INVISIBLE);
                isEdit.set(true);
        });
    }

    private void setOnFinishButtonListener() {
        finish_button.setOnClickListener(v -> {
            if (is_from_meal_plan) {
                finish_button.setVisibility(View.INVISIBLE);
                add_button.setVisibility(View.INVISIBLE);
                edit_addtomp_button.setVisibility(View.VISIBLE);
                isEdit.set(false);
            } else {
                finish();
            }
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