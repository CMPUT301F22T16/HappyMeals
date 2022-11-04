package com.example.happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.example.happymeals.databinding.ActivityMpmealPlanBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class MPMealPlanActivity extends AppCompatActivity {

    ActivityMpmealPlanBinding activityMpmealPlanBinding;

    RecyclerView.Adapter mpAdapter;
    ArrayList<MealPlan> mealPlans;
    FloatingActionButton new_mp_button;
    RecyclerView meal_plan_list;
    Intent intent_mpl;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMpmealPlanBinding = ActivityMpmealPlanBinding.inflate(getLayoutInflater());

        setContentView(activityMpmealPlanBinding.getRoot());

        meal_plan_list = activityMpmealPlanBinding.mpRecyclerview;
        new_mp_button = activityMpmealPlanBinding.myMealsAddButton;

        // Add back button to action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Meal Plans");

        // get user name
        Bundle bundle = getIntent().getExtras();
        userName = (String) bundle.getSerializable("USER");
        DBHandler db = new DBHandler(userName);

        mealPlans = new ArrayList<>();

//        //Testing
//        Ingredient ind = new Ingredient(3,"carrot");
//        List<String> comments = new ArrayList<>();
//        comments.add("LGTM!");
//        List<Ingredient> ingredients = new ArrayList<>();
//        ingredients.add(ind);
//        Recipe r1 = new Recipe("Greedy recipe",1,1,"vst", comments, ingredients);
//        List<Recipe> recipes = new ArrayList<>();
//        recipes.add(r1);
//        List<Double> scalings = new ArrayList<>();
//        scalings.add(1.11);
//        ArrayList<Meal> mealsb = new ArrayList<>();
//        ArrayList<Meal> mealsl = new ArrayList<>();
//        ArrayList<Meal> mealsn = new ArrayList<>();
//        mealsb.add(new Meal(recipes,scalings,3.4));
//        mealsl.add(new Meal(recipes,scalings,3.4));
//        mealsn.add(new Meal(recipes,scalings,3.4));
//        MealPlan mealPlan = new MealPlan(mealsb,mealsl,mealsn,1);
//        MealPlan mealPlan2 = new MealPlan(mealsb,mealsl,mealsn,1);
//        mealPlans.add(mealPlan);
//        mealPlans.add(mealPlan2);
//
//        user.addMealPlan(mealPlan, this);
//        user.addMealPlan(mealPlan2, this);

        mpAdapter = new MPListAdapter(this, mealPlans, userName);
        meal_plan_list.setLayoutManager(new GridLayoutManager(this, 1));
        meal_plan_list.setAdapter(mpAdapter);

        LoadingDialog dialog = new LoadingDialog(this);
        db.getUserMealPlans((MPListAdapter) mpAdapter, dialog);

        setOnAddButtonListener();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final MealPlan mealPlan = mealPlans.get(viewHolder.getAdapterPosition());
                // delete mealPlan
                db.removeMealPlan(mealPlan);
            }
        });
        itemTouchHelper.attachToRecyclerView(meal_plan_list);
        setOnAddButtonListener();
    }

    private void setOnAddButtonListener() {
        new_mp_button.setOnClickListener(v -> {
            intent_mpl = new Intent(this, MPMealListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("MEALPLAN", new MealPlan());
            bundle.putSerializable("USER", userName);
            bundle.putSerializable("IsNewMP", true);
            intent_mpl.putExtras(bundle);
            startActivity(intent_mpl);
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