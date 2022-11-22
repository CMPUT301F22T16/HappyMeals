package com.example.happymeals.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.happymeals.databinding.ActivitySlshoppingListBinding;

public class SLShoppingListActivity extends AppCompatActivity {
    ActivitySlshoppingListBinding activity_slshopping_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity_slshopping_list = ActivitySlshoppingListBinding.inflate(getLayoutInflater());

        setContentView(activity_slshopping_list.getRoot());

        // Add back button to action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Shopping List");
    }
}