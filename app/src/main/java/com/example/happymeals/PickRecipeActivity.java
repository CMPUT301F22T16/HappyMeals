package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class PickRecipeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    ListView recipe_list;
    PickRecipeListAdapter recipe_adapter;
    ArrayList<Recipe> dataList;
    SearchView recipe_search_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_recipe);
        // mocking recipe list
        Recipe r1 = new Recipe("Greedy recipe");
        Recipe r2 = new Recipe("fine recipe");
        Recipe r3 = new Recipe("tasty recipe");
        Recipe r4 = new Recipe("ta");
        dataList = new ArrayList<Recipe>();
        dataList.add(r1);
        dataList.add(r2);
        dataList.add(r3);
        dataList.add(r4);


        recipe_list = findViewById(R.id.mp_recipe_list);
        recipe_adapter = new PickRecipeListAdapter(this,dataList);
//
        recipe_list.setAdapter(recipe_adapter);
        recipe_search_bar = findViewById(R.id.searchview_recipe);
        recipe_search_bar.setOnQueryTextListener(this);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String entered_text = newText;
        recipe_adapter.filter(entered_text);
        return false;
    }
}