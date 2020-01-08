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
    private TextView carNumbernth;
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
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250);
        LinearLayout emojiContainer = new LinearLayout(context);
        emojiContainer.setLayoutParams(lp);

        //Set Layouts Orientation
        inputUserContainer.setOrientation(LinearLayout.VERTICAL);
        formHeaderAndDeleteContainer.setOrientation(LinearLayout.HORIZONTAL);
        emojiContainer.setOrientation(LinearLayout.HORIZONTAL);

        carNumbernth = dynamicallyXML.createTextView(context,"Car number #" + (formNumber + 1),18, Color.rgb(44,167,239), Gravity.CENTER,100,50,0,0);
        ImageView deleteFormBtn = dynamicallyXML.createImageView(context,R.drawable.minussign,70,70,Gravity.CENTER,-200,25,0,0);
        dynamicallyXML.addAllViewsLayout(formHeaderAndDeleteContainer,carNumbernth,deleteFormBtn);

        carNumberPlaceHolder =  dynamicallyXML.createEditText(context,"Car Number", InputType.TYPE_CLASS_PHONE);
        nicknamePlaceHolder = dynamicallyXML.createEditText(context,"Nickname",InputType.TYPE_CLASS_TEXT);
        TextView pickYourEmojiText = dynamicallyXML.createTextView(context,"Pick your emoji's car!",13,Color.BLACK,Gravity.CENTER,220,50,0,0);
        dynamicallyXML.addAllViewsLayout(inputUserContainer,carNumberPlaceHolder,nicknamePlaceHolder,pickYourEmojiText);


        //add To Emoji Container
        addEmojiToContainer(emojiContainer);
        dynamicallyXML.addAllViewsLayout(allFormContainer,formHeaderAndDeleteContainer,inputUserContainer,emojiContainer);

        setFormListeners(deleteFormBtn,formHeaderAndDeleteContainer,inputUserContainer,emojiContainer);

        allForms.add(this);

    }

    private void setFormListeners(final ImageView deleteFormBtn, final LinearLayout formHeaderAndDeleteContainer, final LinearLayout inputUserContainer, final LinearLayout emojiContainer ) {

        deleteFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFormContext(formHeaderAndDeleteContainer, inputUserContainer,emojiContainer);
            }
        });
    }

    private void deleteFormContext(LinearLayout formHeaderAndDeleteContainer, LinearLayout inputUserContainer, LinearLayout emojiContainer) {

        formHeaderAndDeleteContainer.removeAllViews();
        inputUserContainer.removeAllViews();
        emojiContainer.removeAllViews();

        formHeaderAndDeleteContainer = null;
        inputUserContainer = null;
        emojiContainer = null;

        allForms.remove(this);

        updateAllForms();

    }

    private void updateAllForms() {

        for(int i = 0; i < allForms.size(); i++){
            allForms.get(i).getCarNumbernth().setText("Car number #" + (i + 1));
        }
    }

    private void addEmojiToContainer(LinearLayout emojiContainer) {

        ImageView driverOne = dynamicallyXML.createImageView(context,R.drawable.driver1,200,200,Gravity.CENTER,100,20,0,0);
        ImageView driverTwo = dynamicallyXML.createImageView(context,R.drawable.driver2,200,200, Gravity.CENTER,100,20,0,0);
        ImageView driverThree = dynamicallyXML.createImageView(context,R.drawable.driver3,200,200,Gravity.CENTER,100,20,0,0);


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

                    driverOne.setTag(1);
                    emojiID =  driverOne.getTag().toString();
            }
        });

        driverTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markEmoji(driverOne, Color.WHITE);
                markEmoji(driverTwo,Color.rgb(44,167,239));
                markEmoji(driverThree, Color.WHITE);

                driverOne.setTag(2);
                emojiID =  driverOne.getTag().toString();
            }
        });

        driverThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                markEmoji(driverOne, Color.WHITE);
                markEmoji(driverTwo, Color.WHITE);
                markEmoji(driverThree,Color.rgb(44,167,239));


                driverOne.setTag(3);
                emojiID =  driverOne.getTag().toString();
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

    public void setCarNumbernth(TextView carNumbernth) {
        this.carNumbernth = carNumbernth;
    }

    public TextView getCarNumbernth() {
        return carNumbernth;
    }
}
