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

public class EditCarDialog extends DialogFragment {

    private OnInputListener onInputListener;
    private Database databaseRef;
    private Button finishEditBtn;
    private CarForm carForm;
    private LinearLayout formContainer;
    private FieldsChecker checker;
    private CarView carView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            carView = (CarView)getArguments().getSerializable("carview");
            carForm = (CarForm)getArguments().getSerializable("newcarform");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_car,container, false);

        setIds(view);
        carForm.changeContainer(formContainer);
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
                onInputListener.sendInputToEdit(car,carView, carForm);
                getDialog().dismiss();
            }
        });
    }

    private Car createNewCar(){

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
