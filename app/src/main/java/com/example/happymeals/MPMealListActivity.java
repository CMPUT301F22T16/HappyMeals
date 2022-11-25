package com.example.happymeals;

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
import android.view.MenuItem;
import android.widget.Button;

import com.example.happymeals.databinding.ActivityMpmealListBinding;
import com.example.happymeals.meal.MPMyMealsActivity;
import com.example.happymeals.meal.Meal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MPMealListActivity extends AppCompatActivity {
    // TODO this class needs to be fixed
    ActivityMpmealListBinding activityMpmealListBinding;
    RecyclerView meal_list;
    RecyclerView.Adapter mpMealListAdapter;
    MealPlan mealPlan;
    int dayIndex;
    int mealIndex;
    ArrayList<Meal> meals;
    Button addMealsButton;
    Button nextDayButton;
    Button finishButton;
    Intent intent;
    DBHandler db;
    ActivityResultLauncher<Intent> activityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMpmealListBinding = ActivityMpmealListBinding.inflate(getLayoutInflater());

        setContentView(activityMpmealListBinding.getRoot());
        setUpActivityLauncher();

        addMealsButton = activityMpmealListBinding.mealPlanAddButton;
        nextDayButton = activityMpmealListBinding.mpFabNextDay;
        finishButton = activityMpmealListBinding.mpFabFinish;
        meal_list = activityMpmealListBinding.mpMealListRecyclerview;

        // Add back button to action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("One Day Meals");

        // get user name
        dayIndex = 0;
        mealIndex = -1;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = new DBHandler(user.getUid());

        Bundle bundle = getIntent().getExtras();
        meals = new ArrayList<>();
        mealPlan = (MealPlan) bundle.getSerializable("MEALPLAN");

        if((boolean) bundle.getSerializable("IsNewMP")) {
            mealIndex = -1;
            db.addMealPlan(mealPlan);
        } else {
            meals.clear();
            if(!mealPlan.getMeals().isEmpty()) {
                for(Meal meal : mealPlan.getMeals().get(dayIndex)) {
                    meals.add(meal);
            }
            }
//            if(!mealPlan.getBreakfast().isEmpty()) {
//                meals.add(mealPlan.getBreakfast().get(dayIndex));
////                meals.add(mealPlan.getLunch().get(dayIndex));
////                meals.add(mealPlan.getDinner().get(dayIndex));
//                mealIndex = 2;
//            }
        }

        mpMealListAdapter = new MPMealListAdapter(this, meals, user.getUid(), dayIndex, mealPlan, activityLauncher);
        meal_list.setLayoutManager(new GridLayoutManager(this, 1));
        meal_list.setAdapter(mpMealListAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final Meal meal = meals.get(viewHolder.getAdapterPosition());
                // TODO: implement delete when the data structure is fixed to contain multiple meals for a day
                // delete meal
            }
        });
        itemTouchHelper.attachToRecyclerView(activityMpmealListBinding.mpMealListRecyclerview);

        setOnAddButtonListener();
        setOnNextButtonListener();
        setOnFinishButtonListener();
    }

    public void setUpActivityLauncher() {
        // this activity launcher was modified from Misha Akopov's answer(May 3, 2020) to this question:
        // https://stackoverflow.com/questions/61455381/how-to-replace-startactivityforresult-with-activity-result-apis
        activityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Bundle bundle = data.getExtras();
                        mealPlan = (MealPlan) bundle.getSerializable("M-MEALPLAN");
                        meals.clear();
                        meals.addAll(mealPlan.getMeals().get(dayIndex));
                        mpMealListAdapter.notifyDataSetChanged();
                    }
                });

    }

    private void setOnAddButtonListener() {
        addMealsButton.setOnClickListener(v -> {
            if(mealPlan.getNum_days() > dayIndex) {
                mealIndex= mealPlan.getMeals().get(dayIndex).size();
            } else {
                mealIndex = 0;
            }
            intent = new Intent(this, MPMyMealsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Is-From-MealPlan",true);
            bundle.putSerializable("MEALPLAN", mealPlan);
            bundle.putSerializable("MEAL", mealIndex);
            bundle.putSerializable("DAY", dayIndex);
            intent.putExtras(bundle);
            activityLauncher.launch(intent);
        });
    }

    private void setOnNextButtonListener() {
        nextDayButton.setOnClickListener(v -> {
            dayIndex++;
            if(mealPlan.getNum_days() <= dayIndex) {
                meals.clear();
                mealIndex=-1;
                mpMealListAdapter.notifyDataSetChanged();
            } else {
                meals.clear();
                meals.addAll(mealPlan.getMeals().get(dayIndex));
                mpMealListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setOnFinishButtonListener() {
        finishButton.setOnClickListener(v -> {
            mealPlan.setDays();
            db.updateMealPlan(mealPlan);
            finish();
        });
    }

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