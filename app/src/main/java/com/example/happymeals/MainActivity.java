package com.example.happymeals;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button addUButton;
    Button addStorButton;
    //ListView view;
    Context context = this;
    List<Storage> nice = new ArrayList<>();
    //ArrayAdapter<Storage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        User user = new User();
        nice = user.getStorages();
        Log.d("Storagesd", nice.toString());

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
                Log.d("Storagesd", nice.toString());
            }
        });

        //view = findViewById(R.id.city_list);
        //adapter = new ArrayAdapter<>(this, R.layout.content, user.getStorages());
    }
}
