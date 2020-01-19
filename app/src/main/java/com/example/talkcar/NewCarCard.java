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

public class NewCarCard {

    private TextView carNumber;
    private TextView nickname;
    private String emojiId;
    private ImageView delete;
    private ImageView edit;
    private CardView card;
    private Context context;
    private LinearLayout allFormContainer;
    private DynamicallyXML dynamicallyXML;


    public NewCarCard(TextView carNumber, TextView nickmame, String emojiId, ImageView delete /*ImageView edit*/, LinearLayout allFormContainer, Context context){

        dynamicallyXML = new DynamicallyXML();
        this.carNumber = carNumber;
        this.nickname = nickmame;
        this.emojiId = emojiId;
        this.delete = delete;
        //this.edit = edit;
        this.card = dynamicallyXML.createCardView(context,LinearLayout.LayoutParams.MATCH_PARENT,400,20,20,20,20,15,Color.rgb(254,210,0));
        this.allFormContainer = allFormContainer;
        this.context = context;

        createCard();
    }

    private void createCard() {

        ImageView emoji;
        TextView carNumbertv = dynamicallyXML.createTextView(context,carNumber.getText().toString(),40, Color.BLACK, Gravity.CENTER,20,50,10,10);
        TextView nicknametv = dynamicallyXML.createTextView(context,nickname.getText().toString(),20, Color.BLACK, Gravity.CENTER,20,50,10,10);
        carNumbertv.setTypeface(carNumbertv.getTypeface(), Typeface.BOLD);


        if(emojiId.equals("1")) {
            emoji = dynamicallyXML.createImageView(context, R.drawable.driver1, 200, 200, Gravity.CENTER, 800, 100, 5, 5);
        } else if(emojiId.equals("2")){
            emoji = dynamicallyXML.createImageView(context, R.drawable.driver2, 200, 200, Gravity.CENTER, 800, 100, 5, 5);
        } else {
            emoji = dynamicallyXML.createImageView(context, R.drawable.driver3, 200, 200, Gravity.CENTER, 800, 100, 5, 5);
        }
        ImageView delete = dynamicallyXML.createImageView(context,R.drawable.deleteicon,70,70,Gravity.CENTER,20,300,0,5);
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
        card.addView(delete);
        card.addView(carNumbertv);

        allFormContainer.addView(card);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allFormContainer.removeView(card);
            }
        });
    }


}
