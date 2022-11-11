package com.example.happymeals.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.happymeals.DBHandler;
import com.example.happymeals.Ingredient;
import com.example.happymeals.IngredientAdaptor;
import com.example.happymeals.LoadingDialog;
import com.example.happymeals.R;
import com.example.happymeals.RecipeListAdapter;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class creates an Activity that allows the user to add an ingredient to their recipe
 * @author John Yu
 */
public class RecipeAddIngredient extends AppCompatActivity {

    ArrayList<Ingredient> ingredientDataList;
    ListView ingredientList;
    IngredientAdaptor adaptor;

    /**
     * This button lets the user submit their new ingredient
     */
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add_ingredient);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        String username = (String) bundle.getSerializable("USER");
        DBHandler db = new DBHandler(username);

        ingredientDataList = new ArrayList<>();
        adaptor = new IngredientAdaptor(RecipeAddIngredient.this, ingredientDataList);
        ingredientList = findViewById(R.id.recipe_add_ingredient_listview);
        ingredientList.setAdapter(adaptor);
        db.getIngredients(adaptor);

        submit_btn = findViewById(R.id.recipe_add_ingredient_btn);
        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ingredient item = (Ingredient) adapterView.getItemAtPosition(i);
                submit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.putExtra("ingredient", item);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        });
    }
}