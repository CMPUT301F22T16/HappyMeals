package com.example.happymeals.storage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.happymeals.DBHandler;
import com.example.happymeals.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateStorageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateStorageFragment extends DialogFragment {

    StorageAdapter adapter;
    DBHandler db;

    public static CreateStorageFragment newInstance(StorageAdapter adapter, DBHandler db) {
        Bundle args = new Bundle();

        args.putSerializable("ADAPTER", adapter);
        args.putSerializable("DBHandler", db);

        CreateStorageFragment fragment = new CreateStorageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_create_storage, null);

        Bundle bundle = this.getArguments();
        adapter = (StorageAdapter) bundle.getSerializable("ADAPTER");
        db = (DBHandler) bundle.getSerializable("DBHandler");

        EditText input = view.findViewById(R.id.storage_name_fragment);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        return builder
                .setView(view)
                .setTitle("New Storage")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = input.getText().toString();

                        Storage storage = new Storage(name);
                        db.addStorage(storage);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .create();


    }
}