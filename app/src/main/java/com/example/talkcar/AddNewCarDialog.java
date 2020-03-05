package com.example.talkcar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;

public class AddNewCarDialog extends DialogFragment {

    private OnInputListener onInputListener;
    private Database databaseRef;
    private Button addNewCarBtn;
    private CarForm carForm;
    private LinearLayout formContainer;
    private FieldsChecker checker;
    private DynamicallyXML dynamicallyXML;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_car,container, false);
        dynamicallyXML = new DynamicallyXML();
        setIds(view);
        setClickListeners();
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

                checkCarNumberExistens(car);
            }
        });
    }


    private void checkCarNumberExistens(final Car car) {

        for (int i = 0; i <CarForm.allForms.size() ; i++) {
            if(CarForm.allForms.get(i).getCarNumberPlaceHolder().getText().toString().equals(car.getCarNumber())){
                carForm.getCarNumberPlaceHolder().setError("You already used this car number...");
                return;
            }
        }

        final ProgressBar progressBar = dynamicallyXML.createProgressBar(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100);
        params.gravity = Gravity.CENTER;
        formContainer.addView(progressBar,params);

        Database.searchCarByCarNumber(car.getCarNumber(), new OnGetDataListener() {
            @Override
            public void onSuccess(Object findedCar) {
                if(findedCar == null) {
                    //There is no car with that car number
                    // Adding car to the new form list.
                    CarForm.allForms.add(carForm);
                    onInputListener.sendInput(car);
                    getDialog().dismiss();
                } else {
                    formContainer.removeView(progressBar);
                    carForm.getNicknamePlaceHolder().setText("");
                    carForm.getCarNumberPlaceHolder().setError("Car number is already exists...");
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });

    }

    public Car createNewCar(){

       if(!checker.checkCarDetailsFields(carForm.getCarNumberPlaceHolder(), carForm.getNicknamePlaceHolder())){
            return null;
        }

       //create a new car - two options - if you add car AFTER registration or BEFORE
       Car car;
       if(FirebaseAuth.getInstance().getCurrentUser() != null) {
           //AFTER (and that why FirebaseAuth.getInstance().getCurrentUser() IS NOT null
            car = new Car(carForm.getCarNumberPlaceHolder().getText().toString(), carForm.getNicknamePlaceHolder().getText().toString(), carForm.getEmojiID(), FirebaseAuth.getInstance().getCurrentUser().getUid());
       } else {
           //BEFORE (and that why FirebaseAuth.getInstance().getCurrentUser() is null
            car = new Car(carForm.getCarNumberPlaceHolder().getText().toString(), carForm.getNicknamePlaceHolder().getText().toString(), carForm.getEmojiID(), null);
       }
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
