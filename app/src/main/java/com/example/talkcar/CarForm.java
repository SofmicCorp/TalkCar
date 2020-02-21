package com.example.talkcar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
    private ImageView[] allEmojies;
    private LinearLayout formHeader;
    private LinearLayout inputUserContainer;
    private LinearLayout emojiContainer;
    private LinearLayout seeMoreContainer;
    private LinearLayout currentContainer;
    private TextView seeMore;
    private int emojiPosition = 0;
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
         formHeader = new LinearLayout(context);
         inputUserContainer = new LinearLayout(context);
         emojiContainer = new LinearLayout(context);
         seeMoreContainer = new LinearLayout(context);

         LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 250);

        emojiContainer.setLayoutParams(lp);

        //Set Layouts Orientation
        inputUserContainer.setOrientation(LinearLayout.VERTICAL);
        formHeader.setOrientation(LinearLayout.HORIZONTAL);
        emojiContainer.setOrientation(LinearLayout.HORIZONTAL);
        seeMoreContainer.setOrientation(LinearLayout.VERTICAL);

        carNumberPlaceHolder =  dynamicallyXML.createEditText(context,"Car Number", InputType.TYPE_CLASS_PHONE);
        nicknamePlaceHolder = dynamicallyXML.createEditText(context,"Nickname (optional)",InputType.TYPE_CLASS_TEXT);
        TextView pickYourEmojiText = dynamicallyXML.createTextView(context,"pick your emoji's car!",13,Color.BLACK,Gravity.CENTER,220,50,0,0);
        dynamicallyXML.addAllViewsLayout(inputUserContainer,carNumberPlaceHolder,nicknamePlaceHolder,pickYourEmojiText);

        //Set click listener to all emojis.
        setEmojiClickListeners(allEmojies);
        //add To Emoji Container
        addEmojiToContainer(emojiContainer);
        seeMore = dynamicallyXML.createTextView(context,"see more",13,Color.BLACK,Gravity.CENTER,370,50,0,0);
        setSeeMoreClickListener();
        dynamicallyXML.addAllViewsLayout(seeMoreContainer,seeMore);

        dynamicallyXML.addAllViewsLayout(formContainer,formHeader,inputUserContainer,emojiContainer,seeMoreContainer);

    }

    private void setSeeMoreClickListener() {

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

        final int SIZE = 9;

        allEmojies = new ImageView[SIZE];

        //Adding all Emoji to the array
        allEmojies[0] = dynamicallyXML.createImageView(getContext(),R.drawable.driver1,150,150, Gravity.CENTER,100,10,0,0);
        allEmojies[1] = dynamicallyXML.createImageView(getContext(),R.drawable.driver2,150,150, Gravity.CENTER,100,10,0,0);
        allEmojies[2] = dynamicallyXML.createImageView(getContext(),R.drawable.driver3,150,150, Gravity.CENTER,100,10,0,0);
        allEmojies[3] = dynamicallyXML.createImageView(getContext(),R.drawable.twoboys,150,150, Gravity.CENTER,100,10,0,0);
        allEmojies[4] = dynamicallyXML.createImageView(getContext(),R.drawable.twogirls,150,150, Gravity.CENTER,100,10,0,0);
        allEmojies[5] = dynamicallyXML.createImageView(getContext(),R.drawable.batmobile,150,150, Gravity.CENTER,100,10,0,0);
        allEmojies[6] = dynamicallyXML.createImageView(getContext(),R.drawable.backtothefuture,150,150, Gravity.CENTER,100,10,0,0);
        allEmojies[7] = dynamicallyXML.createImageView(getContext(),R.drawable.blondegirl,150,150, Gravity.CENTER,100,10,0,0);
        allEmojies[8] = dynamicallyXML.createImageView(getContext(),R.drawable.oldman,150,150, Gravity.CENTER,100,10,0,0);

        setEmojiTags(SIZE);

    }
    private void setEmojiTags(int size){
        for(int i = 0; i < size; i++){
            allEmojies[i].setTag(i);
        }
    }

    public void changeContainer(LinearLayout formContainer){

        currentContainer.removeView(formHeader);
        currentContainer.removeView(inputUserContainer);
        currentContainer.removeView(emojiContainer);
        currentContainer.removeView(seeMoreContainer);

        setEmojiContainer();
        dynamicallyXML.addAllViewsLayout(formContainer,formHeader,inputUserContainer,emojiContainer,seeMoreContainer);
        currentContainer= formContainer;
    }

    private void addEmojiToContainer(LinearLayout emojiContainer) {

//        emojiID =  allEmojies[1].getTag().toString();

        int middleIndex = Integer.parseInt(emojiID);
        int firstIndex = middleIndex - 1;
        int lastIndex = middleIndex + 1;

        if(firstIndex < 0){
            firstIndex = allEmojies.length - 1;
        }
        if(lastIndex > allEmojies.length - 1){
            lastIndex = 0;
        }


        dynamicallyXML.addAllViewsLayout(emojiContainer,allEmojies[firstIndex], allEmojies[middleIndex], allEmojies[lastIndex]);

        //Default value if driver doesnt pick an emoji
        markEmoji(allEmojies[Integer.parseInt(emojiID)],Color.rgb(44,167,239));
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
        for(int i = 0; i < allEmojies.length; i++){
            markEmoji(allEmojies[i],Color.WHITE);
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

}
