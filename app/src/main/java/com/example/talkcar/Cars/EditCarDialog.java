package com.example.talkcar.Cars;

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

import com.example.talkcar.Helpers.FieldsChecker;
import com.example.talkcar.MainActivity;
import com.example.talkcar.Interfaces.OnInputListener;
import com.example.talkcar.R;

public class EditCarDialog extends DialogFragment {

    private OnInputListener onInputListener;
    private Button finishEditBtn;
    private CarForm carForm;
    private LinearLayout formContainer;
    private FieldsChecker checker;
    private LicencePlateView licencePlateView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            licencePlateView = (LicencePlateView)getArguments().getSerializable("carview");
            carForm = (CarForm)getArguments().getSerializable("newcarform");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_edit_car,container, false);

        //If there is no nickname the nickname of the car will be the car number.
        //when user press edit the nickname edit text should be empty and not show the car number
        if(carForm.getCarNumberPlaceHolder().getText().toString().equals(
                carForm.getNicknamePlaceHolder().getText().toString())){
            carForm.getNicknamePlaceHolder().setText("");
        }

        setIds(view);
        carForm.changeContainer(formContainer);
        setClickListeners();
        checker = new FieldsChecker();
        locateButtons();

        return view;
    }

    private void locateButtons() {

        if(getContext() instanceof MainActivity){
            //If we are in edit mode in main activity
            carForm.getCarNumberPlaceHolder().setEnabled(false);
        }
    }


    private void setIds(View view) {

        finishEditBtn = view.findViewById(R.id.finish_edit_btn);
        formContainer= view.findViewById(R.id.form_container);
    }

    private void setClickListeners() {

        finishEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Car car = createNewCar();
                if(car == null){
                    //if we are here details of car are empty or ilegal
                    return;
                }
                onInputListener.sendInputToEdit(car, licencePlateView, carForm);
                getDialog().dismiss();

            }
        });
    }

    private Car createNewCar(){

        if(!checker.checkCarDetailsFields(carForm.getCarNumberPlaceHolder(), carForm.getNicknamePlaceHolder())){
            return null;
        }

        Car car = new Car(carForm.getCarNumberPlaceHolder().getText().toString(), carForm.getNicknamePlaceHolder().getText().toString(), carForm.getEmojiID(), null);

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
