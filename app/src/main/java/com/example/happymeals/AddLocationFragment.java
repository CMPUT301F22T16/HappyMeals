package com.example.happymeals;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The AddCityFragment is used to add a new city, it defines the actions to take once the users
 * decides to add/edit a city after pressing the button. Depending on the mode, this activity will
 * retrieve the input from the text box provided by the user.
 */
public class AddLocationFragment extends DialogFragment {

    private EditText location;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_location_fragment_layout, null);

        Context context = getContext();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DBHandler db = new DBHandler(user.getUid());

        location = view.findViewById(R.id.location);


        ArrayList<String> categories = new ArrayList<>(Arrays.asList("Vegetable", "Fruit", "Meat", "Drink", "Dry food", "Others"));
        ArrayAdapter<String> categoryAdapt = new ArrayAdapter<String>(context, R.layout.ingredient_content, R.id.myTextview, categories);


//        location.setAdapter(categoryAdapt);
        location.setHint("Location name");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


        return builder
                .setView(view)
                .setTitle("Add new storage location")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String locationStr = location.getText().toString();
                        if (locationStr.isEmpty()){
                            Toast.makeText(context, "Invalid location", Toast.LENGTH_SHORT).show();
                        }else{
                            Storage storage= new Storage(locationStr);
//                            db.addStorage(storage);

                            // add to database
                        }
                        //startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                })
                .create();
    }

}
