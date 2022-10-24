package com.example.happymeals;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button addUButton;
    Button addStorButton;
    Context context = this;
    List<Storage> nice = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        User user = new User();

        addUButton = findViewById(R.id.add_user_button);
        addUButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("gStor", String.valueOf(nice.size()));
                }
            });

        addStorButton = findViewById(R.id.add_stor_button);
        addStorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                //Storage storage = new Storage("Cabinet");
                //Ingredient ing = new Ingredient(2, 2, date, storage);
                //user.newIngredient(context, ing);
                //user.getIngredients(context);
            }
        });
    }
}
