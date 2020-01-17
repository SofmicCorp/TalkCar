package com.example.talkcar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddNewCarCarDialog extends DialogFragment {

    private OnInputListener onInputListener;
    private Database databaseRef;
    private Button addNewCarBtn;
    private NewCarForm newCarForm;
    private LinearLayout formContainer;
    private FieldsChecker checker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_car,container, false);

        NewCarForm.removeAllForms();
        setIds(view);
        setClickListeners();
        databaseRef = new Database(new MD5());
        newCarForm = new NewCarForm((getContext()),formContainer);
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

                Car car;
                car = createNewCar();
                if(car == null){
                    return;
                }
                onInputListener.sendInput(car);
                getDialog().dismiss();
            }
        });
    }

    public Car createNewCar(){

       if(!checker.checkCarDetailsFields(newCarForm.getCarNumberPlaceHolder(),newCarForm.getNicknamePlaceHolder())){
            return null;
        }

        Car car = new Car(newCarForm.getCarNumberPlaceHolder().getText().toString(),newCarForm.getNicknamePlaceHolder().getText().toString(),newCarForm.getEmojiID());

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
