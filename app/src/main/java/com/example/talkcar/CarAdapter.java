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
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

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
        holder.chattedCarNumber.setText(car.getCarNumber());
        holder.chattedProfileImage.setImageResource(MainActivity.emojiMap.get(car.getEmojiId()));

        holder.myCarNumber.setText(mCarsMap.getMyCars().get(position).getCarNumber());
        holder.myCarProfileImage.setImageResource(MainActivity.emojiMap.get(mCarsMap.getMyCars().get(position).getEmojiId()));




        Log.d("LUBA", "chatKeyLastMessageMap.size(): " + chatKeyLastMessageMap.size());
        if(chatKeyLastMessageMap.size() > 0) {
            if(car.getHashMap() != null) {

                    if (chatKeyLastMessageMap.get(keyChat) != null) {
                        Log.d("YULIA", "here: ");
                        holder.chattedProfileImageBackground.setImageResource(R.drawable.unreadwhitecircle);
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

        public TextView chattedCarNumber;
        public ImageView chattedProfileImageBackground;
        public ImageView chattedProfileImage;

        public TextView myCarNumber;
        public ImageView myCarProfileImageBackground;
        public ImageView myCarProfileImage;

        public TextView lastMessage;
        public View horizontalLine;


        public ViewHolder(View itemView){
            super(itemView);
            chattedCarNumber = itemView.findViewById(R.id.car_number);
            chattedProfileImageBackground = itemView.findViewById(R.id.profile_image_background);
            chattedProfileImage = itemView.findViewById(R.id.profile_image);
            lastMessage = itemView.findViewById(R.id.last_message);
            horizontalLine = itemView.findViewById(R.id.horizontal_line);
            myCarNumber = itemView.findViewById(R.id.my_car_number);
            myCarProfileImageBackground = itemView.findViewById(R.id.my_car_profile_image_background);
            myCarProfileImage = itemView.findViewById(R.id.my_car_profile_image);
            
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
