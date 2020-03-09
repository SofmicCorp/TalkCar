package com.example.talkcar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.talkcar.Notifications.Data;

import java.io.Serializable;
import java.util.ArrayList;

public class LicencePlateView implements Serializable {

    private TextView nickname;
    private androidx.cardview.widget.CardView card;
    private Context context;
    private LinearLayout container;
    private DynamicallyXML dynamicallyXML;
    private int cardId;
    private Activity activity;
    public static ArrayList<LicencePlateView> allLicencePlateViews =  new ArrayList<>();


    public LicencePlateView(TextView nickname, int cardId, LinearLayout container, Context context, Activity activity, String carNumber){

        dynamicallyXML = new DynamicallyXML();
        this.nickname = nickname;
        this.context = context;
        this.activity = activity;
        this.card = dynamicallyXML.createCardView(context,LinearLayout.LayoutParams.MATCH_PARENT,200,20,20,20,20,15,Color.rgb(254,210,0));
        card.setBackgroundResource(R.drawable.cardview_shape);
        this.container = container;
        this.cardId = cardId;

        createCard(carNumber);

    }

    private void createCard(String carNumber) {

        nickname.setTypeface(nickname.getTypeface(), Typeface.BOLD);

        ImageView delete = dynamicallyXML.createImageView(context,R.drawable.deleteicon,70,70,Gravity.LEFT,100,100,0,5);
        ImageView edit = dynamicallyXML.createImageView(context,R.drawable.editicon,70,70,Gravity.LEFT,20,100,0,5);

        //Layout params

        //Car number lp
        LinearLayout.LayoutParams carNumberLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        carNumberLp.setMargins(30,0,0,0);

        if(carNumber.equals(nickname.getText().toString())){
            if(carNumber.length() == 7){
                nickname.setText(FieldsChecker.addDashesSevenDigit(carNumber));
            } else {
                nickname.setText(FieldsChecker.addDashesEightDigit(carNumber));
            }

        }

        nickname.setGravity(Gravity.CENTER);
        nickname.setLayoutParams(carNumberLp);

        card.addView(edit);
        card.addView(delete);
        card.addView(nickname);
        container.addView(card);

        setClickListeners(delete,edit);
    }

    void setClickListeners(ImageView delete,ImageView edit){


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SignupActivity.isActive)
                    return;
                if(ApplicationModel.getCurrentDriver().getCars().size() == 1){
                    Toast.makeText(context, "You need to have at least 1 car", Toast.LENGTH_SHORT).show();
                    return;
                }
                Car carToDelete = findMyCarByCarNumber(CarForm.allForms.get(cardId).getCarNumberPlaceHolder().getText().toString());
                if(SignupActivity.isActive) {
                   removeLicencePlateFromUI(carToDelete);
                } else {
                    if(carToDelete != null) {
                        AllChatsActivity.deleteAllChatOfCar(carToDelete);
                        deleteChattedCarsHash(carToDelete);
                        ApplicationModel.getCurrentDriver().getCars().remove(carToDelete);
                        Database.saveDriver(ApplicationModel.getCurrentDriver(),ApplicationModel.getCurrentDriver().getuId());
                        removeLicencePlateFromUI(carToDelete);


                    }
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditCarDialog dialog = new EditCarDialog();

                Bundle bundle = new Bundle();
                bundle.putSerializable("carview", allLicencePlateViews.get(cardId));
                bundle.putSerializable("newcarform", CarForm.allForms.get(cardId));
                dialog.setArguments(bundle);
                FragmentManager ft = ((FragmentActivity)activity).getSupportFragmentManager();
                dialog.show(ft,"EditCarDialog");
            }
        });
    }

    private void deleteChattedCarsHash(final Car carToDelete) {

        if(carToDelete.getHashMap() == null)
            return;

        for(String carNumber : carToDelete.getHashMap().keySet()){
            Database.searchCarByCarNumber(carNumber, new OnGetDataListener() {
                @Override
                public void onSuccess(Object object) {

                    if(object != null){
                        Driver driver = (Driver)object;
                        ApplicationModel.getLastCarNumberSearch().getHashMap().remove(carToDelete.getCarNumber());
                        ApplicationModel.chattedCarsMap.remove(ApplicationModel.getLastCarNumberSearch());
                        Database.saveDriver(driver,driver.getuId());


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
    }

    private void removeLicencePlateFromUI(Car car) {
        container.removeView(card); // remove from container - only removal from gui
        allLicencePlateViews.remove(cardId); // remove from array of carsViews
        CarForm.allForms.remove(cardId); //delete the car from forms
        updateAllCarViewsIds(); //update all car views id after removal
    }

    private Car findMyCarByCarNumber(String carNumberToFind) {

     if(MainActivity.isActive) {
         for (Car car : ApplicationModel.getCurrentDriver().getCars()) {
             if (car.getCarNumber().equals(carNumberToFind)) {
                 return car;
             }
         }
     }
        return null;
    }

    public void changeContainer(LinearLayout formContainer){

        container.removeView(card);
        dynamicallyXML.addAllViewsLayout(formContainer,card);
        container= formContainer;
    }

    private void updateAllCarViewsIds(){

        for(int i = 0; i < allLicencePlateViews.size(); i++){
            allLicencePlateViews.get(i).setCardId(i);
        }
    }

    public static void removeAllCarViews(){

        allLicencePlateViews.removeAll(allLicencePlateViews);
    }

    public CardView getCard() {
        return card;
    }

    public int getCardId() {
        return cardId;
    }

    public TextView getNickname() {
        return nickname;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
}
