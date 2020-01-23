package com.example.talkcar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditCarDialog extends DialogFragment {

    private OnInputListener onInputListener;
    private Database databaseRef;
    private Button finishEditBtn;
    private NewCarForm newCarForm;
    private LinearLayout formContainer;
    private FieldsChecker checker;
    private CarView carView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Log.d("BUBA", "here: ");
            carView = (CarView)getArguments().getSerializable("carview");
            newCarForm = (NewCarForm)getArguments().getSerializable("newcarform");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_car,container, false);

        setIds(view);
        newCarForm.changeContainer(formContainer);
        setClickListeners();
        databaseRef = new Database(new MD5());
        checker = new FieldsChecker();

        return view;
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
                onInputListener.sendInputToEdit(car,carView, newCarForm);
                getDialog().dismiss();
            }
        });
    }

    private Car createNewCar(){

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
