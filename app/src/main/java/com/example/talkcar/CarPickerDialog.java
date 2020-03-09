package com.example.talkcar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class CarPickerDialog extends DialogFragment {

    private OnInputListener onInputListener;
    private ArrayList<LicencePlateView> allCars;
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
        driver = ApplicationModel.getCurrentDriver();
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

            final LicencePlateView licencePlateView = LicencePlateView.allLicencePlateViews.get(i);
            licencePlateView.getCard().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onInputListener.sendInput(licencePlateView.getCardId());
                    getDialog().dismiss();
                }
            });
            allCars.add(LicencePlateView.allLicencePlateViews.get(i));
        }
    }

    private void changeContainerToAllCarViews(){

        for(int i = 0; i < LicencePlateView.allLicencePlateViews.size(); i++){
            LicencePlateView.allLicencePlateViews.get(i).changeContainer(carsContainer);
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
