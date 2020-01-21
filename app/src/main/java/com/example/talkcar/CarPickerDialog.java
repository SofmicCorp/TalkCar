package com.example.talkcar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        driver = LoginActivity.applicationModel.getCurrentDriver();
        allCars = new ArrayList<>();
        dynamicallyXML = new DynamicallyXML();
        context = getContext();
        addAllDriverCars();

        return view;
    }

    private void addAllDriverCars() {

        //Set the array of car views
        for(int i = 0; i < driver.getCars().size(); i++){

            Car car = driver.getCars().get(i);
            TextView carNumber = dynamicallyXML.createTextView(context,car.getCarNumber(),10, Color.BLACK, Gravity.CENTER,0,0,0,0);
            TextView nickname = dynamicallyXML.createTextView(context,car.getNickname(),10,Color.BLACK,Gravity.CENTER,0,0,0,0);
            final CarView carView = new CarView(carNumber,nickname,car.getEmojiId(),i,carsContainer,context);
            carView.getCard().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onInputListener.sendInput(carView.getCardId());
                    getDialog().dismiss();
                }
            });
            allCars.add(carView);
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
