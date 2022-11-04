package com.example.happymeals;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.example.happymeals.databinding.ActivityMpmealListBinding;

import java.util.ArrayList;
import java.util.List;

public class MPMealListActivity extends AppCompatActivity {

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
    String userName;
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
        Bundle bundle = getIntent().getExtras();
        userName = (String) bundle.getSerializable("USER");
        dayIndex = 0;
        mealIndex = -1;
        db = new DBHandler(userName);

        meals = new ArrayList<>();
        mealPlan = (MealPlan) bundle.getSerializable("MEALPLAN");

        if((boolean) bundle.getSerializable("IsNewMP")) {
            mealIndex = -1;
            db.addMealPlan(mealPlan, this);
        } else {
            meals.clear();
            if(!mealPlan.getBreakfast().isEmpty()) {
                meals.add(mealPlan.getBreakfast().get(dayIndex));
//                meals.add(mealPlan.getLunch().get(dayIndex));
//                meals.add(mealPlan.getDinner().get(dayIndex));
                mealIndex = 2;
            }
        }

        mpMealListAdapter = new MPMealListAdapter(this, meals, userName, dayIndex, mealPlan, activityLauncher);
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
//                        db.updateMealPlan(mealPlan, activityMpmealListBinding.getRoot().getContext());
                        meals.clear();
                        meals.add(mealPlan.getBreakfast().get(dayIndex));
//                        meals.add(mealPlan.getLunch().get(dayIndex));
//                        meals.add(mealPlan.getDinner().get(dayIndex));
                        mpMealListAdapter.notifyDataSetChanged();
                    }
                });

    }

    private void setOnAddButtonListener() {
        addMealsButton.setOnClickListener(v -> {
            if(mealIndex>=2) {return;}
            mealIndex++;
            intent = new Intent(this, MPMyMealsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("MEALPLAN", mealPlan);
            bundle.putSerializable("MEAL", mealIndex);
            bundle.putSerializable("DAY", dayIndex);
            bundle.putSerializable("USER", userName);
            intent.putExtras(bundle);
//            startActivity(intent);
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
                meals.add(mealPlan.getBreakfast().get(dayIndex));
//                meals.add(mealPlan.getLunch().get(dayIndex));
//                meals.add(mealPlan.getDinner().get(dayIndex));
                mealIndex=2;
                mpMealListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setOnFinishButtonListener() {
        finishButton.setOnClickListener(v -> {
            mealPlan.setDays(mealPlan.getBreakfast().size());
            db.updateMealPlan(mealPlan, activityMpmealListBinding.getRoot().getContext());
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