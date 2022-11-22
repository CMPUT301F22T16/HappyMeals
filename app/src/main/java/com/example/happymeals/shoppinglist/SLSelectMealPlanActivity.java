package com.example.happymeals.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.happymeals.DBHandler;
import com.example.happymeals.R;
import com.example.happymeals.databinding.ActivitySlselectMealPlanBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SLSelectMealPlanActivity extends AppCompatActivity {
    ActivitySlselectMealPlanBinding activitySlselectMealPlanBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySlselectMealPlanBinding = ActivitySlselectMealPlanBinding.inflate(getLayoutInflater());

        setContentView(activitySlselectMealPlanBinding.getRoot());

        // Add back button to action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("SL Select Meal Plan");

        // get userId
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DBHandler db = new DBHandler(user.getUid());
    }
}