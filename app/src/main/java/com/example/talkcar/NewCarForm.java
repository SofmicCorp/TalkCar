package com.example.talkcar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class NewCarForm extends RelativeLayout {

    private DynamicallyXML dynamicallyXML;
    private int formNumber;
    private EditText carNumberPlaceHolder;
    private EditText nicknamePlaceHolder;
    private String emojiID;
    private Context context;
    public static ArrayList<NewCarForm> allForms = new ArrayList<>();

    public NewCarForm(Context context,LinearLayout allFormContainer) {
        super(context);

        dynamicallyXML = new DynamicallyXML();
        this.formNumber = allForms.size();
        this.context = context;

        //Create Form layouts
        LinearLayout formHeaderAndDeleteContainer = new LinearLayout(context);
        LinearLayout inputUserContainer = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 250);
        LinearLayout emojiContainer = new LinearLayout(context);
        emojiContainer.setLayoutParams(lp);

        //Set Layouts Orientation
        inputUserContainer.setOrientation(LinearLayout.VERTICAL);
        formHeaderAndDeleteContainer.setOrientation(LinearLayout.HORIZONTAL);
        emojiContainer.setOrientation(LinearLayout.HORIZONTAL);

        carNumberPlaceHolder =  dynamicallyXML.createEditText(context,"Car Number", InputType.TYPE_CLASS_PHONE);
        nicknamePlaceHolder = dynamicallyXML.createEditText(context,"Nickname",InputType.TYPE_CLASS_TEXT);
        TextView pickYourEmojiText = dynamicallyXML.createTextView(context,"Pick your emoji's car!",13,Color.BLACK,Gravity.CENTER,220,50,0,0);
        dynamicallyXML.addAllViewsLayout(inputUserContainer,carNumberPlaceHolder,nicknamePlaceHolder,pickYourEmojiText);

        //add To Emoji Container
        addEmojiToContainer(emojiContainer);
        dynamicallyXML.addAllViewsLayout(allFormContainer,formHeaderAndDeleteContainer,inputUserContainer,emojiContainer);
        allForms.add(this);
    }


    private void addEmojiToContainer(LinearLayout emojiContainer) {

        ImageView driverOne = dynamicallyXML.createImageView(context,R.drawable.driver1,150,150,Gravity.CENTER,100,10,0,0);
        ImageView driverTwo = dynamicallyXML.createImageView(context,R.drawable.driver2,150,150, Gravity.CENTER,100,10,0,0);
        ImageView driverThree = dynamicallyXML.createImageView(context,R.drawable.driver3,150,150,Gravity.CENTER,100,10,0,0);

        driverOne.setTag(1);
        driverTwo.setTag(2);
        driverThree.setTag(3);

        //Default value if driver doesnt pick an emoji
        markEmoji(driverTwo,Color.rgb(44,167,239));
        emojiID =  driverTwo.getTag().toString();

        setEmojiClickListeners(driverOne,driverTwo,driverThree);
        dynamicallyXML.addAllViewsLayout(emojiContainer,driverOne, driverTwo, driverThree);
    }

    private void setEmojiClickListeners(final ImageView driverOne, final ImageView driverTwo, final ImageView driverThree) {

        driverOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                markEmoji(driverOne,Color.rgb(44,167,239));
                markEmoji(driverTwo, Color.WHITE);
                markEmoji(driverThree, Color.WHITE);


                    emojiID =  driverOne.getTag().toString();
            }
        });

        driverTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markEmoji(driverOne, Color.WHITE);
                markEmoji(driverTwo,Color.rgb(44,167,239));
                markEmoji(driverThree, Color.WHITE);

                emojiID =  driverTwo.getTag().toString();
            }
        });

        driverThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                markEmoji(driverOne, Color.WHITE);
                markEmoji(driverTwo, Color.WHITE);
                markEmoji(driverThree,Color.rgb(44,167,239));


                emojiID =  driverThree.getTag().toString();
            }
        });
    }

    private void markEmoji(ImageView emoji,int color) {

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(100);
        gd.setStroke(4, color);
        emoji.setBackgroundDrawable(gd);
    }

    public static void removeAllForms(){

        allForms.removeAll(allForms);
    }

    public EditText getCarNumberPlaceHolder() {
        return carNumberPlaceHolder;
    }

    public EditText getNicknamePlaceHolder() {
        return nicknamePlaceHolder;
    }

    public String getEmojiID() {
        return emojiID;
    }

    public void setCarNumberPlaceHolder(EditText carNumberPlaceHolder) {
        this.carNumberPlaceHolder = carNumberPlaceHolder;
    }

    public void setNicknamePlaceHolder(EditText nicknamePlaceHolder) {
        this.nicknamePlaceHolder = nicknamePlaceHolder;
    }

    public void setEmojiID(String emojiID) {
        this.emojiID = emojiID;
    }


}
