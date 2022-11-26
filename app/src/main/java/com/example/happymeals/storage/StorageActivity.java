package com.example.happymeals.storage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
    private StorageAdapter adapter;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        Bundle bundle = getIntent().getExtras();
        String username = (String) bundle.getSerializable("USER");
        db = new DBHandler(username);

        // Add back button to action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Storages");

        storages = new ArrayList<>();
        adapter = new StorageAdapter(this, storages, db);

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

    // https://stackoverflow.com/questions/14545139/android-back-button-in-the-title-bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addStorageAction(View view) {
        CreateStorageFragment.newInstance(adapter, db).show(getSupportFragmentManager(), "ADD STORAGE");
    }
}