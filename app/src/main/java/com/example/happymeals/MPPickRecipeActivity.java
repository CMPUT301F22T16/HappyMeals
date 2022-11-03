package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MPPickRecipeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    ListView recipe_list;
    MPPickRecipeListAdapter recipe_adapter;
    ArrayList<Recipe> dataList;
    SearchView recipe_search_bar;
    Button confirmButton;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mppick_recipe);
        Intent i = getIntent();
        String m_id = i.getStringExtra("Meal_ID");

        confirmButton = findViewById(R.id.confirm_recipe_selection_button);

        dataList = new ArrayList<Recipe>();



        user = new User();

        recipe_list = findViewById(R.id.mp_recipe_list);
        recipe_adapter = new MPPickRecipeListAdapter(this,dataList);

        recipe_list.setAdapter(recipe_adapter);
        recipe_search_bar = findViewById(R.id.searchview_recipe);
        recipe_search_bar.setOnQueryTextListener(this);
        LoadingDialog dialog = new LoadingDialog(this);
        user.getUserRecipesForMeals(recipe_adapter,dialog,this);
        setOnConfirmButtonListener();
        setOnListViewItemListener();
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

    private void setOnConfirmButtonListener() {
        confirmButton.setOnClickListener(v -> {
            // TODO:should add all selection to meal recipe list
            // update firebase
            finish(); // get back to caller activity which is meal recipe list

        });
    }

    private void setOnListViewItemListener() {
        recipe_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: sync check box:https://stackoverflow.com/questions/5417339/android-listview-with-checkbox-and-all-clickable
                // TODO: add selected recipes to a buffer list
                CheckBox checkBox = view.findViewById(R.id.checkBox);
                checkBox.toggle();
                if(checkBox.isChecked()){
                    recipe_adapter.addToBuffer(i);
                } else {
                    recipe_adapter.removeFromBuffer(i);
                }

            }
        });
    }
}