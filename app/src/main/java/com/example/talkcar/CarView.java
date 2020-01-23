package com.example.talkcar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CarView {

    private TextView carNumber;
    private TextView nickname;
    private String emojiId;
    private androidx.cardview.widget.CardView card;
    private Context context;
    private LinearLayout container;
    private DynamicallyXML dynamicallyXML;
    private int cardId;
    public static ArrayList<CarView> allCarViews =  new ArrayList<>();


    public CarView(TextView carNumber, TextView nickmame, String emojiId,int cardId, LinearLayout container, Context context){

        dynamicallyXML = new DynamicallyXML();
        this.carNumber = carNumber;
        this.nickname = nickmame;
        this.emojiId = emojiId;
        this.context = context;
        this.card = dynamicallyXML.createCardView(context,LinearLayout.LayoutParams.MATCH_PARENT,200,20,20,20,20,15,Color.rgb(254,210,0));
        card.setBackgroundResource(R.drawable.cardview_shape);
        this.container = container;

        if(LoginActivity.applicationModel.getCurrentDriver() != null){
            this.cardId = cardId;
        }

        createCard();

    }

    private void createCard() {

        final int LARGE_NICKNAME_LENGTH = 6;

        StringBuilder carNumberWithDashes = addDashes(carNumber.getText().toString());

        if(nickname.getText().equals(carNumber.getText())){
            nickname.setText(carNumberWithDashes);
        }

        TextView nicknameTV = dynamicallyXML.createTextView(context,nickname.getText().toString(),40, Color.BLACK, Gravity.CENTER,20,50,10,10);
        nicknameTV.setTypeface(nicknameTV.getTypeface(), Typeface.BOLD);

        ImageView delete = dynamicallyXML.createImageView(context,R.drawable.deleteicon,70,70,Gravity.CENTER,100,100,0,5);
        ImageView edit = dynamicallyXML.createImageView(context,R.drawable.editicon,70,70,Gravity.CENTER,20,100,0,5);

        //Layout params

        //Car number lp
        LinearLayout.LayoutParams carNumberLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        carNumberLp.setMargins(0,20,50,20);


        nicknameTV.setGravity(Gravity.CENTER);
        nicknameTV.setLayoutParams(carNumberLp);


        card.addView(edit);
        if(LoginActivity.applicationModel.getCurrentDriver() == null){
            card.addView(delete);
        }
        card.addView(nicknameTV);
        container.addView(card);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                container.removeView(card); // remove from container - only removal from gui
                allCarViews.remove(cardId); // remove from array of carsViews
                NewCarForm.allForms.remove(cardId); //delete the car from forms
                updateAllCarViewsIds(); //update all car views id after removal
            }
        });


    }

    private void updateAllCarViewsIds(){

        for(int i = 0; i < allCarViews.size(); i++){
            allCarViews.get(i).setCardId(i);
        }
    }

    private StringBuilder addDashes(String carNumber) {

        StringBuilder carNumberWithDashes = new StringBuilder();

        for(int i = 0; i < carNumber.length(); i++){

            if(i ==  2 || i == 5){
                carNumberWithDashes.append('-');
            }
            carNumberWithDashes.append(carNumber.charAt(i));

        }


        return carNumberWithDashes;
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

    public void setCarNumber(TextView carNumber) {
        this.carNumber = carNumber;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
}