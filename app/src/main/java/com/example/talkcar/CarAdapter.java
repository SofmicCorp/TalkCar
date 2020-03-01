package com.example.talkcar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.core.view.LayoutInflaterFactory;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {

    private Context mContext;
    private List<Car> mCars;
    private HashMap<String, Message> chatKeyLastMessageMap;

    public CarAdapter(Context mContext, List<Car> mCars, HashMap<String, Message> chatKeyLastMessageMap){

        this.mContext = mContext;
        this.mCars = mCars;
        this.chatKeyLastMessageMap = chatKeyLastMessageMap;
    }

    @NonNull
    @Override
    public CarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.car_item, parent,false);
            return new CarAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Car car = mCars.get(position);
        holder.carNumber.setText(car.getCarNumber());
        holder.profileImage.setImageResource(MainActivity.emojiMap.get(car.getEmojiId()));


        if(chatKeyLastMessageMap.size() > 0) {
            Object[] keysChats = car.getHashMap().values().toArray();
            for (int i = 0; i < chatKeyLastMessageMap.size(); i++) {
                if (chatKeyLastMessageMap.get(keysChats[i]) != null) {
                    holder.profileImageBackground.setImageResource(R.drawable.unreadwhitecircle);
                }
            }
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ChatActivity.class);
                intent.putExtra("chattedCar",car);
                ApplicationModel.setLastCarNumberSearch(car);
                ApplicationModel.setChattedDriverUid(car.getDriverUid());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView carNumber;
        public ImageView profileImageBackground;
        public ImageView profileImage;


        public ViewHolder(View itemView){
            super(itemView);
            carNumber = itemView.findViewById(R.id.car_number);
            profileImageBackground = itemView.findViewById(R.id.profile_image_background);
            profileImage = itemView.findViewById(R.id.profile_image);

        }
    }

}
