package com.example.happymeals.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.happymeals.DBHandler;
import com.example.happymeals.LoadingDialog;
import com.example.happymeals.R;
import com.example.happymeals.databinding.ActivitySlselectMealPlanBinding;
import com.example.happymeals.mealplan.MPListAdapter;
import com.example.happymeals.mealplan.MealPlan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SLSelectMealPlanActivity extends AppCompatActivity {
    ActivitySlselectMealPlanBinding activitySlselectMealPlanBinding;

    RecyclerView.Adapter mpAdapter;
    ArrayList<MealPlan> mealPlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySlselectMealPlanBinding = ActivitySlselectMealPlanBinding.inflate(getLayoutInflater());

        setContentView(activitySlselectMealPlanBinding.getRoot());

        // Add back button to action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Shopping List");

        // get userId
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DBHandler db = new DBHandler(user.getUid());

        mealPlans = new ArrayList<>();

        mpAdapter = new SLMealPlanAdapter(this, mealPlans);
        activitySlselectMealPlanBinding.slSelectMpList.setLayoutManager(new GridLayoutManager(this, 1));
        activitySlselectMealPlanBinding.slSelectMpList.setAdapter(mpAdapter);

        LoadingDialog dialog = new LoadingDialog(this);
        db.getSLMealPlans((SLMealPlanAdapter) mpAdapter, dialog);
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