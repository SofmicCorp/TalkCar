package com.example.talkcar.Chats;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talkcar.MainActivity;
import com.example.talkcar.R;
import com.google.firebase.auth.FirebaseAuth;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public static final int MESSAGE_TYPE_LEFT = 0;
    public static final int MESSAGE_TYPE_RIGHT = 1;
    private Context mContext;
    private Chat mChat;
    private String emojiId;

    public ChatAdapter(Context mContext,Chat mChat,String emojiId){

        this.mContext = mContext;
        this.mChat = mChat;
        this.emojiId = emojiId;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MESSAGE_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent,false);
            return new ChatAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent,false);
            return new ChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Message message = mChat.getMessages().get(position);

        holder.showMessage.setText(message.getMessage());
        holder.profileImage.setImageResource(MainActivity.emojiMap.get(emojiId));
    }

    @Override
    public int getItemCount() {
        return mChat.getMessages().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView showMessage;
        public ImageView profileImage;

        public ViewHolder(View itemView){
            super(itemView);

            showMessage = itemView.findViewById(R.id.show_message);
            profileImage = itemView.findViewById(R.id.profile_image);

        }
    }

    @Override
    public int getItemViewType(int position) {

        if(mChat.getMessages().get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            return MESSAGE_TYPE_RIGHT;
        } else {
            return MESSAGE_TYPE_LEFT;
        }
    }
}
