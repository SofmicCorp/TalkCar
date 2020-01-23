package com.example.talkcar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class CarPickerDialog extends DialogFragment {

    private OnInputListener onInputListener;
    private ArrayList<CarView> allCars;
    private LinearLayout carsContainer;
    private Driver driver;
    private Context context;
    private DynamicallyXML dynamicallyXML;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_car_picker,container, false);

        setIds(view);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        driver = LoginActivity.applicationModel.getCurrentDriver();
        allCars = new ArrayList<>();
        changeContainerToAllCarViews();
        dynamicallyXML = new DynamicallyXML();
        context = getContext();
        addAllDriverCars();
        
        return view;
    }

    private void addAllDriverCars() {

        StringBuilder carNickname;

        //Set the array of car views
        for(int i = 0; i < driver.getCars().size(); i++){

            final CarView carView = CarView.allCarViews.get(i);
            carView.getCard().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onInputListener.sendInput(carView.getCardId());
                    getDialog().dismiss();
                }
            });
            allCars.add(CarView.allCarViews.get(i));
        }
    }

    private void changeContainerToAllCarViews(){

        for(int i = 0; i < CarView.allCarViews.size(); i++){
            CarView.allCarViews.get(i).changeContainer(carsContainer);
        }

    }

    private void setIds(View view) {

        carsContainer = (LinearLayout)view.findViewById(R.id.cars_container);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onInputListener = (OnInputListener)getActivity();
        }catch(ClassCastException e){

        }
    }
}
