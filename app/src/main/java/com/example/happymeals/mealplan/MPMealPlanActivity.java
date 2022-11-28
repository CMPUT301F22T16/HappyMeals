package com.example.happymeals.mealplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.happymeals.DBHandler;
import com.example.happymeals.LoadingDialog;
import com.example.happymeals.databinding.ActivityMpmealPlanBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MPMealPlanActivity extends AppCompatActivity {

    ActivityMpmealPlanBinding activityMpmealPlanBinding;

    RecyclerView.Adapter mpAdapter;
    ArrayList<MealPlan> mealPlans;
    FloatingActionButton new_mp_button;
    RecyclerView meal_plan_list;
    Intent intent_mpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMpmealPlanBinding = ActivityMpmealPlanBinding.inflate(getLayoutInflater());

        setContentView(activityMpmealPlanBinding.getRoot());

        meal_plan_list = activityMpmealPlanBinding.mpRecyclerview;
        new_mp_button = activityMpmealPlanBinding.myMealsAddButton;

        // Add back button to action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Weekly Meal Plans");

        // get userId
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DBHandler db = new DBHandler(user.getUid());

        mealPlans = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        mpAdapter = new MPListAdapter(this, mealPlans, user.getUid());
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