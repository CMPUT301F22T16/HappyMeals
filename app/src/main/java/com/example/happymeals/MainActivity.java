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
    Button testButton;
    Context context = this;
    List<Storage> nice = new ArrayList<>();
    List<Meal> databasemeals = new ArrayList<>();
    //ArrayAdapter<Storage> adapter;
    LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);
    List<Storage> storages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FOR TESTING PURPOSES
        User user = new User();
        Storage storage = new Storage("Basket");
        user.newStorage(storage, context);

        testButton = findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
