package com.example.happymeals;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

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
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, RecipeListActivity.class);
        startActivity(intent);
    }
}
