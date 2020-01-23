package com.example.talkcar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddNewCarDialog extends DialogFragment {

    private OnInputListener onInputListener;
    private Database databaseRef;
    private Button addNewCarBtn;
    private CarForm carForm;
    private LinearLayout formContainer;
    private FieldsChecker checker;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_car,container, false);
        setIds(view);
        setClickListeners();
        databaseRef = new Database(new MD5());
        carForm = new CarForm((getContext()),formContainer);
        checker = new FieldsChecker();

        return view;
    }

    private void setIds(View view) {

        addNewCarBtn = view.findViewById(R.id.add_car_btn);
        formContainer = view.findViewById(R.id.form_container);
    }

    private void setClickListeners() {

        addNewCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Car car = createNewCar();
                if(car == null){
                    //if we are here details of car are empty or ilegal
                    return;
                }
                //Adding car to the new form list.
                CarForm.allForms.add(carForm);
                onInputListener.sendInput(car);
                getDialog().dismiss();
            }
        });
    }

    public Car createNewCar(){

       if(!checker.checkCarDetailsFields(carForm.getCarNumberPlaceHolder(), carForm.getNicknamePlaceHolder())){
            return null;
        }

        Car car = new Car(carForm.getCarNumberPlaceHolder().getText().toString(), carForm.getNicknamePlaceHolder().getText().toString(), carForm.getEmojiID());

        return car;
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
