package com.example.happymeals.shoppinglist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.happymeals.RecipeIngredient;

public class SLShoppingListAdapter extends ArrayAdapter<RecipeIngredient> {

    public SLShoppingListAdapter(@NonNull Context context, int resource, @NonNull RecipeIngredient[] objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
