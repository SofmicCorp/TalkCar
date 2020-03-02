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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {

    private Context mContext;
    private List<Car> mCars;
    private HashMap<String, Message> chatKeyLastMessageMap;
    private String theLastMessage;
    private CarAdapter.ViewHolder holder;

    public CarAdapter(Context mContext, List<Car> mCars, HashMap<String, Message> chatKeyLastMessageMap){

        this.mContext = mContext;
        this.mCars = mCars;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Car car = mCars.get(position);
        holder.carNumber.setText(car.getCarNumber());
        holder.profileImage.setImageResource(MainActivity.emojiMap.get(car.getEmojiId()));
        theLastMessage = "default";


        Log.d("LUBA", "chatKeyLastMessageMap.size(): " + chatKeyLastMessageMap.size());
        if(chatKeyLastMessageMap.size() > 0) {
            if(car.getHashMap() != null) {
                Object[] keysChats = car.getHashMap().values().toArray();
                for (int i = 0; i < keysChats.length; i++) {
                    if (chatKeyLastMessageMap.get(keysChats[i]) != null) {
                        holder.profileImageBackground.setImageResource(R.drawable.unreadwhitecircle);
                    }
                }
            }
        }
        handleLastMessage(holder,car);

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
        private TextView lastMessage;
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

    private void handleLastMessage(final ViewHolder holder, final Car car){

        Database.findAllLastMessages(new OnGetDataListener() {
            @Override
            public void onSuccess(Object object) {
                if(object != null){
                    ArrayList<Message> allMessages = (ArrayList<Message>) object;
                    for (int i = 0; i < allMessages.size(); i++) {
                        if(allMessages.get(i).getReceiver().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && allMessages.get(i).getSender().equals(car.getDriverUid())
                                || allMessages.get(i).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) &&  allMessages.get(i).getReceiver().equals(car.getDriverUid())){
                            theLastMessage = allMessages.get(i).getMessage();
                        }
                    }
                }

                switch (theLastMessage){
                    case "default":
                        holder.lastMessage.setText("There is no message yet");
                        break;
                    default:
                        if(!(holder.lastMessage.getText().toString().equals(theLastMessage))) {
                            Log.d("LUBA", "holder.lastMessage.getText().toString() " + holder.lastMessage.getText().toString());
                            Log.d("LUBA", "theLastMessage " + theLastMessage);
                            holder.lastMessage.setText(theLastMessage);
                        }
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });
    }

    public ViewHolder getHolder() {
        return holder;
    }

    public void setHolder(ViewHolder holder) {
        this.holder = holder;
    }
}
