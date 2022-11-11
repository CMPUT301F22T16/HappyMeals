package com.example.happymeals.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.happymeals.DBHandler;
import com.example.happymeals.UserIngredient;
import com.example.happymeals.IngredientAdaptor;
import com.example.happymeals.R;

import java.util.ArrayList;

/**
 * This class creates an Activity that allows the user to add an ingredient to their recipe
 * @author John Yu
 */
public class RecipeAddIngredient extends AppCompatActivity {

    ArrayList<UserIngredient> userIngredientDataList;
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

        userIngredientDataList = new ArrayList<>();
        adaptor = new IngredientAdaptor(RecipeAddIngredient.this, userIngredientDataList);
        ingredientList = findViewById(R.id.recipe_add_ingredient_listview);
        ingredientList.setAdapter(adaptor);
        db.getIngredients(adaptor);

        submit_btn = findViewById(R.id.recipe_add_ingredient_btn);
        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserIngredient item = (UserIngredient) adapterView.getItemAtPosition(i);
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