package com.example.happymeals.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.happymeals.R;

import java.util.ArrayList;

public class RecipeAddIngredient extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;

    RecyclerView ingredient_list;
    IngredientAdapter adapter;
    ArrayList<Ingredient> data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add_ingredient);

        spinner = findViewById(R.id.recipe_add_ingredient_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.sort_category, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        ingredient_list = findViewById(R.id.recipe_add_ingredient_recyclerview);

        data_list = new ArrayList<>();
        data_list.add(new Ingredient("Carrot", "Vegetable"));
        data_list.add(new Ingredient("Broccoli", "Vegetable"));
        data_list.add(new Ingredient("Chicken", "Meat"));
        data_list.add(new Ingredient("Milk", "Dairy"));
        data_list.add(new Ingredient("Eggs", "Meat"));

        adapter = new IngredientAdapter(this, data_list);

        ingredient_list.setAdapter(adapter);
        ingredient_list.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        /*
            Write the sort function here
         */
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}