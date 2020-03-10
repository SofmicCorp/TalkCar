package com.example.talkcar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.io.Serializable;
import java.util.ArrayList;

public class CarForm extends RelativeLayout implements Serializable {

    private DynamicallyXML dynamicallyXML;
    private int formNumber;
    private EditText carNumberPlaceHolder;
    private EditText nicknamePlaceHolder;
    private String emojiID;
    private Context context;
    private ImageView[] allEmojis;
    private LinearLayout pickEmojiHeader;
    private LinearLayout inputUserContainer;
    private LinearLayout emojiContainer;
    private LinearLayout seeMoreContainer;
    private LinearLayout currentContainer;
    private TextView seeMore;
    private TextView pickYourEmojiText;
    public static ArrayList<CarForm> allForms = new ArrayList<>();

    public CarForm(Context context, LinearLayout formContainer) {
        super(context);
        this.currentContainer = formContainer; //Saving current container
        dynamicallyXML = new DynamicallyXML();
        this.formNumber = allForms.size();
        this.context = context;
        setEmojiID("1"); //In every form the default emoji will be the emoji in place 1 in the array.
        createAllEmojies();

        //Create Form layouts

         inputUserContainer = new LinearLayout(context);
        pickEmojiHeader = new LinearLayout(context);
         emojiContainer = new LinearLayout(context);
         seeMoreContainer = new LinearLayout(context);

         //Create Layout Params
        LinearLayout.LayoutParams lpCenterForm = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,4);
        LinearLayout.LayoutParams lpInputUser  = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpCenter = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams lpEmoji = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);


        lpEmoji.gravity = Gravity.CENTER_HORIZONTAL;
        lpCenterForm.gravity = Gravity.CENTER_HORIZONTAL;
        lpCenter.gravity = Gravity.CENTER_HORIZONTAL;

        //Set LayoutParams
        inputUserContainer.setLayoutParams(lpInputUser);
        emojiContainer.setLayoutParams(lpEmoji);
        seeMoreContainer.setLayoutParams(lpCenter);
        pickEmojiHeader.setLayoutParams(lpCenter);
        formContainer.setLayoutParams(lpCenterForm);


        //Set Layouts Orientation
        inputUserContainer.setOrientation(LinearLayout.VERTICAL);
        pickEmojiHeader.setOrientation(LinearLayout.HORIZONTAL);
        emojiContainer.setOrientation(LinearLayout.HORIZONTAL);
        seeMoreContainer.setOrientation(LinearLayout.HORIZONTAL);

        carNumberPlaceHolder =  dynamicallyXML.createEditText(context,"Car Number", InputType.TYPE_CLASS_PHONE);
        nicknamePlaceHolder = dynamicallyXML.createEditText(context,"Nickname (optional)",InputType.TYPE_CLASS_TEXT);
        pickYourEmojiText = dynamicallyXML.createTextView(context,"Pick Your Car's Emoji!","sans-serif-condensed",13,Color.BLACK,Gravity.CENTER_HORIZONTAL,0,30,0,0);
        seeMore = dynamicallyXML.createTextView(context,"See More","sans-serif-condensed",13,Color.BLACK,Gravity.CENTER_HORIZONTAL,0,50,0,0);
        seeMore.setTypeface(seeMore.getTypeface(), Typeface.BOLD);

        pickYourEmojiText.setGravity(Gravity.CENTER);
        seeMore.setGravity(Gravity.CENTER);

        //Set click listener to all emojis.
        setEmojiClickListeners(allEmojis);

        //add To Emoji Container
        dynamicallyXML.addAllViewsLayout(inputUserContainer,carNumberPlaceHolder,nicknamePlaceHolder);
        pickEmojiHeader.addView(pickYourEmojiText);
        addEmojiToContainer(emojiContainer);
        seeMoreContainer.addView(seeMore);

        setClickListeners();

        dynamicallyXML.addAllViewsLayout(formContainer,inputUserContainer,pickEmojiHeader,emojiContainer,seeMoreContainer);

    }

    private void setClickListeners() {

        seeMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmojiStorageDialog();
            }
        });
    }

    private void openEmojiStorageDialog() {

        EmojiStorageDialog dialog = new EmojiStorageDialog();

        Bundle bundle = new Bundle();
        bundle.putSerializable("newcarform", this);
        dialog.setArguments(bundle);
        FragmentManager ft = ((FragmentActivity)getContext()).getSupportFragmentManager();
        dialog.show(ft,"EmojiStorageDialog");
    }

    private void createAllEmojies() {

        final int SIZE = 15;

        allEmojis = new ImageView[SIZE];

        //Adding all Emoji to the array
        allEmojis[0] = dynamicallyXML.createImageView(getContext(),R.drawable.driver1,150,150, Gravity.CENTER,100,10,0,0);
        allEmojis[1] = dynamicallyXML.createImageView(getContext(),R.drawable.driver2,150,150, Gravity.CENTER,100,10,0,0);
        allEmojis[2] = dynamicallyXML.createImageView(getContext(),R.drawable.driver3,150,150, Gravity.CENTER,100,10,0,0);
        allEmojis[3] = dynamicallyXML.createImageView(getContext(),R.drawable.twoboys,150,150, Gravity.CENTER,100,10,0,0);
        allEmojis[4] = dynamicallyXML.createImageView(getContext(),R.drawable.twogirls,150,150, Gravity.CENTER,100,10,0,0);
        allEmojis[5] = dynamicallyXML.createImageView(getContext(),R.drawable.batmobile,150,150, Gravity.CENTER,100,10,0,0);
        allEmojis[6] = dynamicallyXML.createImageView(getContext(),R.drawable.backtothefuture,150,150, Gravity.CENTER,100,10,0,0);
        allEmojis[7] = dynamicallyXML.createImageView(getContext(),R.drawable.blondegirl,150,150, Gravity.CENTER,100,10,0,0);
        allEmojis[8] = dynamicallyXML.createImageView(getContext(),R.drawable.oldman,150,150, Gravity.CENTER,100,10,0,0);
        allEmojis[9] = dynamicallyXML.createImageView(getContext(),R.drawable.taxidriver,150,150, Gravity.CENTER,100,10,0,0);
        allEmojis[10] = dynamicallyXML.createImageView(getContext(),R.drawable.taxidriver2,150,150, Gravity.CENTER,100,10,0,0);
        allEmojis[11] = dynamicallyXML.createImageView(getContext(),R.drawable.simpsons,150,150, Gravity.CENTER,100,10,0,0);
        allEmojis[12] = dynamicallyXML.createImageView(getContext(),R.drawable.blackcar,150,150, Gravity.CENTER,100,10,0,0);
        allEmojis[13] = dynamicallyXML.createImageView(getContext(),R.drawable.girlwithbluecar,150,150, Gravity.CENTER,100,10,0,0);
        allEmojis[14] = dynamicallyXML.createImageView(getContext(),R.drawable.youngadult,150,150, Gravity.CENTER,100,10,0,0);

        setEmojiTags(SIZE);

    }
    private void setEmojiTags(int size){
        for(int i = 0; i < size; i++){
            allEmojis[i].setTag(i);
        }
    }

    public void changeContainer(LinearLayout formContainer){

        //Create Layout Params
        LinearLayout.LayoutParams lpCenterForm = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lpCenterForm.gravity = Gravity.CENTER_VERTICAL;

        currentContainer.removeView(pickEmojiHeader);
        currentContainer.removeView(inputUserContainer);
        currentContainer.removeView(emojiContainer);
        currentContainer.removeView(seeMoreContainer);

        setEmojiContainer();
        dynamicallyXML.addAllViewsLayout(formContainer,inputUserContainer,pickEmojiHeader,emojiContainer,seeMoreContainer);
        currentContainer= formContainer;
    }

    private void addEmojiToContainer(LinearLayout emojiContainer) {

//        emojiID =  allEmojies[1].getTag().toString();

        int middleIndex = Integer.parseInt(emojiID);
        int firstIndex = middleIndex - 1;
        int lastIndex = middleIndex + 1;

        if(firstIndex < 0){
            firstIndex = allEmojis.length - 1;
        }
        if(lastIndex > allEmojis.length - 1){
            lastIndex = 0;
        }


        dynamicallyXML.addAllViewsLayout(emojiContainer,allEmojis[firstIndex], allEmojis[middleIndex], allEmojis[lastIndex]);

        //Default value if driver doesnt pick an emoji
        markEmoji(allEmojis[Integer.parseInt(emojiID)],Color.rgb(44,167,239));
    }

    public void setEmojiContainer(){

        emojiContainer.removeAllViews();
        addEmojiToContainer(emojiContainer);

    }

    private void setEmojiClickListeners(final ImageView[] allEmojies) {

        for(int i = 0; i < allEmojies.length; i++){
            allEmojies[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    markEmoji(allEmojies[Integer.parseInt(v.getTag().toString())],Color.rgb(44,167,239));
                    for(int j = 0; j < allEmojies.length; j++){
                        if(j != Integer.parseInt(v.getTag().toString())){
                            markEmoji(allEmojies[j], Color.WHITE);
                        }
                    }
                    setEmojiID(allEmojies[Integer.parseInt(v.getTag().toString())].getTag().toString());
                }
            });
        }
    }

    public void markEmoji(ImageView emoji,int color) {

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(100);
        gd.setStroke(4, color);
        emoji.setBackgroundDrawable(gd);
    }

    public void unmarkAllEmojis(){
        for(int i = 0; i < allEmojis.length; i++){
            markEmoji(allEmojis[i],Color.WHITE);
        }
    }

    public static void removeAllForms(){

        allForms.removeAll(allForms);
    }

    public static void createFormsFromCars(ArrayList<Car> cars,Context context){

        LinearLayout container = new LinearLayout(context); //Fake container

        for(int i = 0; i < cars.size(); i++){
            CarForm form = new CarForm(context, container);
            allForms.add(form);
        }
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

    public int getFormNumber() {
        return formNumber;
    }
}
