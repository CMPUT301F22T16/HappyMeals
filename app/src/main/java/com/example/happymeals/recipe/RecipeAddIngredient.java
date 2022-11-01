package com.example.happymeals.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.happymeals.Ingredient;
import com.example.happymeals.R;

import java.util.ArrayList;
import java.util.Date;

public class RecipeAddIngredient extends AppCompatActivity implements AdapterView.OnItemSelectedListener, RecyclerViewInterface {

    Spinner spinner;

    RecyclerView ingredient_list;
    IngredientAdapter adapter;
    ArrayList<Ingredient> data_list;

    Button submit_btn;

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
        data_list.add(new Ingredient("Vegetable", "Carrot", 1, 1, new Date()));
        data_list.add(new Ingredient("Vegetable", "Broccoli", 1, 1, new Date()));
        data_list.add(new Ingredient("Meat", "Chicken", 1, 1, new Date()));
        data_list.add(new Ingredient("Dairy", "Milk", 1, 1, new Date()));
        data_list.add(new Ingredient("Meat", "Eggs", 1, 1, new Date()));

        adapter = new IngredientAdapter(this, data_list, this);

        ingredient_list.setLayoutManager(new LinearLayoutManager(this));
        ingredient_list.setAdapter(adapter);

        submit_btn = findViewById(R.id.recipe_add_ingredient_btn);
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

    @Override
    public void onItemClick(int position, String op) {
        // ----- This code changes the item color upon selection ----- //
//        for (Ingredient i : data_list) {
//            i.setSelected(false);
//        }
//        data_list.get(position).setSelected(true);
        ingredient_list.setAdapter(adapter);

        // ----- This code handles the submit button press ----- //
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Ingredient item = data_list.get(position);
                intent.putExtra("desc", item.getDescription());
                intent.putExtra("category", item.getCategory());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}