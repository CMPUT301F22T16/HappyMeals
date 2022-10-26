package com.example.happymeals.recipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.example.happymeals.R;

public class NewRecipe extends AppCompatActivity {

//    ListView recipe_ingredient_list;
    RecyclerView recipe_ingredient_list;
    IngredientAdapter ingredient_adapter;
    ArrayList<Ingredient> ingredient_data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        recipe_ingredient_list = findViewById(R.id.recipe_ingredient_recyclerview);

        ingredient_data_list = new ArrayList<>();
        ingredient_data_list.add(new Ingredient("Carrot"));
        ingredient_data_list.add(new Ingredient("Broccoli"));
        ingredient_data_list.add(new Ingredient("Chicken"));
        ingredient_data_list.add(new Ingredient("Milk"));
        ingredient_data_list.add(new Ingredient("Eggs"));

//        ingredient_adapter = new IngredientAdapter(this, ingredient_data_list);
//
//        recipe_ingredient_list.setAdapter(ingredient_adapter);

        ingredient_adapter = new IngredientAdapter(this, ingredient_data_list);
        recipe_ingredient_list.setAdapter(ingredient_adapter);
        recipe_ingredient_list.setLayoutManager(new LinearLayoutManager(this));
    }
}