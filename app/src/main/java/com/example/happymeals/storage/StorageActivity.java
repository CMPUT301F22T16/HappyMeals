package com.example.happymeals.storage;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.example.happymeals.DBHandler;
import com.example.happymeals.R;
import com.example.happymeals.Storage;
import com.example.happymeals.StorageGridAdapter;

import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity {

    private GridView storageGrid;
    private List<Storage> storages;
    private StorageGridAdapter adapter;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        Bundle bundle = getIntent().getExtras();
        String username = (String) bundle.getSerializable("USER");
        db = new DBHandler(username);

        storages = new ArrayList<>();
        storages.add(new Storage("Fridge"));
        storages.add(new Storage("Freezer"));
        storages.add(new Storage("Pantry"));
        storages.add(new Storage("Pantry"));
        storages.add(new Storage("Pantry"));
        storages.add(new Storage("Pantry"));
        storages.add(new Storage("Pantry"));
        storages.add(new Storage("Pantry"));

        adapter = new StorageGridAdapter(this, storages, db);

        storageGrid = findViewById(R.id.storage_grid);

        storageGrid.setAdapter(adapter);

    }
}