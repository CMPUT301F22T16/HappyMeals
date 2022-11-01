package com.example.happymeals.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.happymeals.R;

import java.util.Date;

public class RecipeEditIngredient extends AppCompatActivity {

    EditText desc_edit_text;
    EditText loc_edit_text;
    EditText date_edit_text;
    EditText category_edit_text;
    EditText amount_edit_text;
    EditText cost_edit_text;
    Button clear_btn;
    Button save_btn;

    String desc = null;
    String loc = null;
    Date date = new Date();
    String category = null;
    Integer amount = null;
    Integer cost = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_edit_ingredient);

        desc_edit_text = findViewById(R.id.recipe_edit_ingredient_description);
        loc_edit_text = findViewById(R.id.recipe_edit_ingredient_location);
        date_edit_text = findViewById(R.id.recipe_edit_ingredient_date);
        category_edit_text = findViewById(R.id.recipe_edit_ingredient_category);
        amount_edit_text = findViewById(R.id.recipe_edit_ingredient_amount);
        cost_edit_text = findViewById(R.id.recipe_edit_ingredient_cost);
        clear_btn = findViewById(R.id.recipe_edit_ingredient_clear_btn);
        save_btn = findViewById(R.id.recipe_edit_ingredient_save_btn);

        Intent intent = getIntent();
        desc = intent.getStringExtra("desc");
        loc = intent.getStringExtra("loc");
        long date_long = intent.getLongExtra("date", new Date().getTime());
        Log.d("date_long", Long.toString(date_long));
        date.setTime(date_long);
        category = intent.getStringExtra("category");
        amount = intent.getIntExtra("amount", 0);
        cost = intent.getIntExtra("cost", 0);

        desc_edit_text.setText(desc);
        loc_edit_text.setText(loc);
        date_edit_text.setText(date.toString());
        category_edit_text.setText(category);
        amount_edit_text.setText(getString(R.string.integer_to_string, amount));
        cost_edit_text.setText(getString(R.string.integer_to_string, cost));

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}