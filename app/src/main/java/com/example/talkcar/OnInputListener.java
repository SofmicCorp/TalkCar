package com.example.talkcar;

public interface OnInputListener{
    void sendInput(Car car);
    void sendInputToEdit(Car car, LicencePlateView licencePlateView, CarForm carForm);
    void sendInput(int index);

}
