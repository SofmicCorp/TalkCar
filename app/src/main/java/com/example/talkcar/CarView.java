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

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.io.Serializable;
import java.util.ArrayList;

public class CarView implements Serializable {

    private TextView nickname;
    private androidx.cardview.widget.CardView card;
    private Context context;
    private LinearLayout container;
    private DynamicallyXML dynamicallyXML;
    private int cardId;
    private Activity activity;
    public static ArrayList<CarView> allCarViews =  new ArrayList<>();


    public CarView(TextView nickname,int cardId, LinearLayout container, Context context,Activity activity, String carNumber){

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

        ImageView delete = dynamicallyXML.createImageView(context,R.drawable.deleteicon,70,70,Gravity.CENTER,100,100,0,5);
        ImageView edit = dynamicallyXML.createImageView(context,R.drawable.editicon,70,70,Gravity.CENTER,20,100,0,5);

        //Layout params

        //Car number lp
        LinearLayout.LayoutParams carNumberLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        carNumberLp.setMargins(0,20,50,20);

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
        if(ApplicationModel.getCurrentDriver() == null){
            card.addView(delete);
        }
        card.addView(nickname);
        container.addView(card);

        setClickListeners(delete,edit);
    }

    void setClickListeners(ImageView delete,ImageView edit){

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                container.removeView(card); // remove from container - only removal from gui
                allCarViews.remove(cardId); // remove from array of carsViews
                CarForm.allForms.remove(cardId); //delete the car from forms
                updateAllCarViewsIds(); //update all car views id after removal
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditCarDialog dialog = new EditCarDialog();

                Bundle bundle = new Bundle();
                bundle.putSerializable("carview",allCarViews.get(cardId));
                bundle.putSerializable("newcarform", CarForm.allForms.get(cardId));
                dialog.setArguments(bundle);
                FragmentManager ft = ((FragmentActivity)activity).getSupportFragmentManager();
                dialog.show(ft,"EditCarDialog");
            }
        });
    }

    public void changeContainer(LinearLayout formContainer){

        container.removeView(card);
        dynamicallyXML.addAllViewsLayout(formContainer,card);
        container= formContainer;
    }

    private void updateAllCarViewsIds(){

        for(int i = 0; i < allCarViews.size(); i++){
            allCarViews.get(i).setCardId(i);
        }
    }

    public static void removeAllCarViews(){

        allCarViews.removeAll(allCarViews);
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
