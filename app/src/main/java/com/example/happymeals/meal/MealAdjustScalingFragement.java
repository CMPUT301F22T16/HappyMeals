package com.example.happymeals.meal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.happymeals.DBHandler;
import com.example.happymeals.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealAdjustScalingFragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealAdjustScalingFragement extends DialogFragment {

    private EditText scaling;
    private TextView recipe_title;
    private Button confirm_button;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle saedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_meal_adjust_scaling,null);
        Context context = getContext();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DBHandler db = new DBHandler(user.getUid());

        scaling = view.findViewById(R.id.textViewEditScaling);
        recipe_title = view.findViewById(R.id.textViewRecipeTitle);
        confirm_button = view.findViewById(R.id.editScalingConfirmButton);
        return new AlertDialog.Builder(getContext()).create();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MealAdjustScalingFragement.
     */
    public static MealAdjustScalingFragement newInstance(String param1, String param2) {
        MealAdjustScalingFragement fragment = new MealAdjustScalingFragement();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

}