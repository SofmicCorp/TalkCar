package com.example.talkcar.Emojis;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.talkcar.Cars.CarForm;
import com.example.talkcar.Helpers.DynamicallyXML;
import com.example.talkcar.Interfaces.OnInputListener;
import com.example.talkcar.R;

public class EmojiStorageDialog extends DialogFragment {

    private OnInputListener onInputListener;
    private LinearLayout emojiContainer;
    private DynamicallyXML dynamicallyXML;
    private CarForm carForm;

    private ImageView[] allEmojis;
    private final int SIZE = 15;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_emoji_storage,container, false);

        if (getArguments() != null) {
            carForm = (CarForm)getArguments().getSerializable("newcarform");
        }

        dynamicallyXML = new DynamicallyXML();
        allEmojis = new ImageView[SIZE];

        emojiContainer = view.findViewById(R.id.emoji_container);

        setIds(view);
        setRowsInStorage();
        setEmojiTags();
        setClickListeners();

        return view;
    }

    private void setRowsInStorage(){

        for(int i = 0; i < SIZE; i+=3){
            LinearLayout row = new LinearLayout(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 250);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(lp);
            dynamicallyXML.addAllViewsLayout(row,allEmojis[i],allEmojis[i+1], allEmojis[i+2]);
            dynamicallyXML.addAllViewsLayout(emojiContainer,row);
        }
    }

    private void setIds(View view) {

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

    }

    private void setEmojiTags(){
        for(int i = 0; i < SIZE; i++){
            allEmojis[i].setTag(i);
        }
    }

    private void setClickListeners() {

       for(int i = 0; i < SIZE; i++){
           allEmojis[i].setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   carForm.unmarkAllEmojis();
                   carForm.setEmojiID(v.getTag().toString());
                   carForm.setEmojiContainer();
                   getDialog().dismiss();
               }
           });
       }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onInputListener = (OnInputListener)getActivity();
        }catch(ClassCastException e){

        }
    }
}
