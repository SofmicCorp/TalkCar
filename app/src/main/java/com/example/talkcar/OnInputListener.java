package com.example.talkcar;

public interface OnInputListener{
    void sendInput(Car car);
    void sendInputToEdit(Car car, CarView carView, NewCarForm newCarForm);

    void sendInput(int index);
}
