package com.example.talkcar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import org.w3c.dom.Text;

public class CarView {

    private TextView carNumber;
    private TextView nickname;
    private String emojiId;
    private androidx.cardview.widget.CardView card;
    private Context context;
    private LinearLayout container;
    private DynamicallyXML dynamicallyXML;
    private int cardId;


    public CarView(TextView carNumber, TextView nickmame, String emojiId,int cardId, LinearLayout container, Context context){

        dynamicallyXML = new DynamicallyXML();
        this.carNumber = carNumber;
        this.nickname = nickmame;
        this.emojiId = emojiId;
        this.context = context;
        this.card = dynamicallyXML.createCardView(context,LinearLayout.LayoutParams.MATCH_PARENT,400,20,20,20,20,15,Color.rgb(254,210,0));
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
            nickname.setText("no nickmame");
        }

        ImageView emoji;
        TextView carNumbertv = dynamicallyXML.createTextView(context,carNumberWithDashes.toString(),40, Color.BLACK, Gravity.CENTER,20,50,10,10);
        TextView nicknametv = dynamicallyXML.createTextView(context,nickname.getText().toString(),20, Color.BLACK, Gravity.CENTER,20,50,10,10);
        carNumbertv.setTypeface(carNumbertv.getTypeface(), Typeface.BOLD);

        if(nicknametv.getText().length() > LARGE_NICKNAME_LENGTH){
            nicknametv.setTextSize(10);
        }

        if(emojiId.equals("1")) {
            emoji = dynamicallyXML.createImageView(context, R.drawable.driver1, 200, 200, Gravity.CENTER, 800, 100, 5, 5);
        } else if(emojiId.equals("2")){
            emoji = dynamicallyXML.createImageView(context, R.drawable.driver2, 200, 200, Gravity.CENTER, 800, 100, 5, 5);
        } else {
            emoji = dynamicallyXML.createImageView(context, R.drawable.driver3, 200, 200, Gravity.CENTER, 800, 100, 5, 5);
        }
        ImageView delete = dynamicallyXML.createImageView(context,R.drawable.deleteicon,70,70,Gravity.CENTER,100,300,0,5);
        ImageView edit = dynamicallyXML.createImageView(context,R.drawable.editicon,70,70,Gravity.CENTER,20,300,0,5);

        //Layout params
        //Car number lp
        LinearLayout.LayoutParams carNumberLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        carNumberLp.setMargins(0,20,50,20);

        //nickname lp
        LinearLayout.LayoutParams nicknameLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        nicknameLp.setMargins(790,300,5,5);

        carNumbertv.setGravity(Gravity.CENTER);
        carNumbertv.setLayoutParams(carNumberLp);
        nicknametv.setLayoutParams(nicknameLp);

        card.addView(emoji);
        card.addView(nicknametv);
        card.addView(edit);
        if(LoginActivity.applicationModel.getCurrentDriver() == null){
            card.addView(delete);
        }
        card.addView(carNumbertv);
        container.addView(card);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeView(card);
                //Checks if we are from mainActivity which means there is a current driver and
                //we need to delete his
                if(LoginActivity.applicationModel.getCurrentDriver() != null){
                    LoginActivity.applicationModel.getCurrentDriver().getCars().remove(cardId);
                }
            }
        });


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

    public CardView getCard() {
        return card;
    }

    public int getCardId() {
        return cardId;
    }
}
