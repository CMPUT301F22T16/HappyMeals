package com.example.happymeals.storage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.happymeals.DBHandler;
import com.example.happymeals.IngredientActivity;
import com.example.happymeals.R;

import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity {

    private ListView storageGrid;
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
        adapter = new StorageGridAdapter(this, storages, db);

        db.getStorages(adapter);


        storageGrid = findViewById(R.id.storage_grid);

        storageGrid.setAdapter(adapter);

        storageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("STORAGE", storages.get(i));
                Intent intent = new Intent(StorageActivity.this, IngredientActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    public void addStorageAction(View view) {
        CreateStorageFragment.newInstance(adapter, db).show(getSupportFragmentManager(), "ADD STORAGE");
    }
}