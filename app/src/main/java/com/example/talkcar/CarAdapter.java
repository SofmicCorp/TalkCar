package com.example.talkcar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.core.view.LayoutInflaterFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talkcar.Notifications.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {

    private Context mContext;
    private ChattedCarsMap mCarsMap;
    private HashMap<String, Message> chatKeyLastMessageMap;
    private CarAdapter.ViewHolder holder;


    public CarAdapter(Context mContext, ChattedCarsMap mCarsMap, HashMap<String, Message> chatKeyLastMessageMap){

        this.mContext = mContext;
        this.mCarsMap = mCarsMap;
        this.chatKeyLastMessageMap = chatKeyLastMessageMap;

    }

    @NonNull
    @Override
    public CarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.car_item, parent,false);
            holder =  new CarAdapter.ViewHolder(view);
        Log.d("KUBA", "holder from car adapter class: " + holder);
            return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Car car = mCarsMap.getChattedCars().get(position);
        final String keyChat = mCarsMap.getKeyChats().get(position);
        holder.carNumber.setText(car.getCarNumber());
        holder.profileImage.setImageResource(MainActivity.emojiMap.get(car.getEmojiId()));



        Log.d("LUBA", "chatKeyLastMessageMap.size(): " + chatKeyLastMessageMap.size());
        if(chatKeyLastMessageMap.size() > 0) {
            if(car.getHashMap() != null) {

                    if (chatKeyLastMessageMap.get(keyChat) != null) {
                        Log.d("YULIA", "here: ");
                        holder.profileImageBackground.setImageResource(R.drawable.unreadwhitecircle);
                    }
            }
        }

        handleLastMessage(holder,car,keyChat);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ChatActivity.class);
                intent.putExtra("chattedCar",car);
                intent.putExtra("index",position);
                ApplicationModel.setLastCarNumberSearch(car);
                ApplicationModel.setChattedDriverUid(car.getDriverUid());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCarsMap.getChattedCars().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView carNumber;
        public ImageView profileImageBackground;
        public ImageView profileImage;
        public TextView lastMessage;
        public View horizontalLine;


        public ViewHolder(View itemView){
            super(itemView);
            carNumber = itemView.findViewById(R.id.car_number);
            profileImageBackground = itemView.findViewById(R.id.profile_image_background);
            profileImage = itemView.findViewById(R.id.profile_image);
            lastMessage = itemView.findViewById(R.id.last_message);
            horizontalLine = itemView.findViewById(R.id.horizontal_line);
        }
    }

    private void handleLastMessage(final ViewHolder holder, final Car car,String keyChat){

        Database.findChatByKey(keyChat, new OnGetDataListener() {
            @Override
            public void onSuccess(Object object) {

                if(object != null) {
                    Chat chat = (Chat) object;
                    holder.lastMessage.setText(chat.getMessages().get(chat.getMessages().size() - 1).getMessage());
                } else {
                    holder.lastMessage.setText("There is no message to show yet.");
                }

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });

//        Database.findAllLastMessagesToSpecificDriver(FirebaseAuth.getInstance().getCurrentUser().getUid(),new OnGetDataListener() {
//            @Override
//            public void onSuccess(Object object) {
//                if(object != null){
//                    if(allLastMessages.size() == 0){
//                        allLastMessages = (ArrayList<Message>) object;
//                    }
//
//                    for (int i = 0; i < allLastMessages.size(); i++) {
//                            if(car.getDriverUid().equals(allLastMessages.get(i).getSender()) ||car.getDriverUid().equals( allLastMessages.get(i).getReceiver())) {
//                                theLastMessage = allLastMessages.get(i).getMessage();
//                                allLastMessages.remove(i);
//                                if (!(holder.lastMessage.getText().toString().equals(theLastMessage))) {
//                                    holder.lastMessage.setText(theLastMessage);
//                                    break;
//                                }
//                            }
//                    }
//                }
//
//                if(theLastMessage.equals("default")){
//                    holder.lastMessage.setText("There is no message yet");
//                }

//                switch (theLastMessage){
//                    case "default":
//                        holder.lastMessage.setText("There is no message yet");
//                        break;
//                    default:
//                        if(!(holder.lastMessage.getText().toString().equals(theLastMessage))) {
//                            Log.d("LUBA", "holder.lastMessage.getText().toString() " + holder.lastMessage.getText().toString());
//                            Log.d("LUBA", "theLastMessage " + theLastMessage);
//                            holder.lastMessage.setText(theLastMessage);
//                        }
//                }
//            }

//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onFailure() {
//
//            }
//        });
    }

    public ViewHolder getHolder() {
        return holder;
    }

    public void setHolder(ViewHolder holder) {
        this.holder = holder;
    }
}
