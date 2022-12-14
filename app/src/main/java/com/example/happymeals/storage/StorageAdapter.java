package com.example.happymeals.storage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.happymeals.DBHandler;
import com.example.happymeals.R;

import java.io.Serializable;
import java.util.List;

public class StorageAdapter extends ArrayAdapter<Storage> implements Serializable {

    private Context context;
    private List<Storage> storages;
    private DBHandler db;

    public StorageAdapter(@NonNull Context context, List<Storage> storages, DBHandler user) {
        super(context, 0, storages);

        this.context = context;
        this.storages = storages;
        this.db = user;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.storage_content, parent, false);
        }

        Storage storage = storages.get(position);

        TextView storageNameView = convertView.findViewById(R.id.storage_card_title);
        storageNameView.setText(storage.getStoreName());

        return convertView;
    }
}
