package com.example.happymeals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * The AddCityFragment is used to add a new city, it defines the actions to take once the users
 * decides to add/edit a city after pressing the button. Depending on the mode, this activity will
 * retrieve the input from the text box provided by the user.
 */
public class ViewIngredientFragment extends DialogFragment {

    private EditText cityName;
    private EditText provinceName;
    private OnFragmentInteractionListener listener;
    Ingredient newIngredient;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Ingredient ingredient);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;

        }else{
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_ingredient_fragment_layout, null);
//        cityName = view.findViewById(R.id.city_name_editText);
//        provinceName = view.findViewById(R.id.province_editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        final Bundle cityToChange = this.getArguments();

//        if (cityToChange != null){
//            newCity = (City) cityToChange.getSerializable("city");
//            cityName.setText(newCity.getCity());
//            provinceName.setText(newCity.getProvince());
//        }

        return builder
                .setView(view)
                .setTitle("Add/Edit City")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String city = cityName.getText().toString();
                        String province = provinceName.getText().toString();

                        // If the bundle is empty, then we are editing a city.
                        if (cityToChange == null){
                            // listener.onOkPressed(new City(province, city));
                            //listener.onOkPressed();
                        } else{
//                            newCity.setCity(city);
//                            newCity.setProvince(province);
                        }

                    }
                }).create();
    }

    // This is used to serialize the city object and so it can be passed between activities.
    static ViewIngredientFragment newInstance(Ingredient ingredient) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient", ingredient);

        ViewIngredientFragment fragment = new ViewIngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
