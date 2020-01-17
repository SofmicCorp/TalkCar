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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_car,container, false);

        NewCarForm.removeAllForms();
        setIds(view);
        setClickListeners();
        databaseRef = new Database(new MD5());
        newCarForm = new NewCarForm((getContext()),formContainer);

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

                do{
                    car = createNewCar();
                }while(car == null);


                onInputListener.sendInput(car);
                getDialog().dismiss();
                getDialog().dismiss();
            }
        });
    }

    public Car createNewCar(){

        String carNumber = newCarForm.getCarNumberPlaceHolder().getText().toString();
        String nickName = newCarForm.getNicknamePlaceHolder().getText().toString();
        String emojiId = newCarForm.getEmojiID();

        if(carNumber.isEmpty()){
            newCarForm.getCarNumberPlaceHolder().setError("Please enter car number");
            return null;
        }

        if(nickName.isEmpty()){
            nickName = carNumber;
        }

        if(isCarNumberAlreadyExists()){


        }

        Car car = new Car(carNumber,nickName,emojiId);

        return car;
    }

    private boolean isCarNumberAlreadyExists() {

        //need to be completed. need to check is car number is not exists in data base!
        return false;

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
