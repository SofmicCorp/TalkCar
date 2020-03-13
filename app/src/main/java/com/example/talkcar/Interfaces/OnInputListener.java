package com.example.talkcar.Interfaces;

import com.example.talkcar.Cars.Car;
import com.example.talkcar.Cars.CarForm;
import com.example.talkcar.Cars.LicencePlateView;

public interface OnInputListener{
    void sendInput(Car car);
    void sendInputToEdit(Car car, LicencePlateView licencePlateView, CarForm carForm);
    void sendInput(int index);

}
