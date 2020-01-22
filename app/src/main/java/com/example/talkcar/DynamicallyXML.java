package com.example.talkcar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class DynamicallyXML {


    public ImageView createImageView(Context context,int image, int width, int height, int gravity, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        lp.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);
        lp.gravity = gravity;
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(image);
        imageView.setLayoutParams(lp);

        return imageView;
    }

    public TextView createTextView(Context context,String text, int size, int color, int gravity, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(910, 130);
        lp.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);
        lp.gravity = gravity;
        TextView textView = new TextView(context);
        textView.setTextSize(size);
        textView.setText(text);
        textView.setTypeface(Typeface.create("sans-serif-smallcaps", Typeface.NORMAL));
        textView.setTextColor(color);
        textView.setLayoutParams(lp);

        return textView;
    }

    public EditText createEditText(Context context,String hint, int type) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(700, 100);
        lp.setMargins(0,30,20,20);
        lp.gravity = Gravity.CENTER;
        EditText editText = new EditText(context);
        editText.setHint(hint);
        editText.setTextSize(13);
        editText.setPadding(40,0,0,0);
        editText.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
        editText.setHintTextColor(Color.BLACK);
        editText.setTextColor(Color.BLACK);
        editText.setBackgroundResource(R.drawable.edit_text_shape);
        editText.setInputType(type);
        editText.setLayoutParams(lp);

        return editText;

    }

    public CardView createCardView(Context context,int width, int height,int leftMargin,int topMargin, int rightMargin,int bottomMargin, int radius,int color){
        CardView cardView = new CardView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);
        cardView.setLayoutParams(layoutParams);
        cardView.setRadius(radius);
        cardView.setPadding(25, 25, 25, 25);
        cardView.setCardBackgroundColor(color);
        cardView.setMaxCardElevation(30);
        cardView.setMaxCardElevation(6);

        return cardView;
    }

    //To be complete
    public Button createButton(Context context,String text, int color){
        Button button = new Button(context);
        button.setText(text);
        button.setBackgroundColor(color);

        return button;
    }



    public void addAllViewsLayout(LinearLayout layout, View... view) {

        for(View v: view){
            layout.addView(v);
        }
    }
}
