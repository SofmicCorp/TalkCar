package com.example.talkcar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    private ImageView profieImage;
    private TextView chattedCarNicknameTV;
    private TextView chattedCarNumberTV;
    private Car chattedCar;
    private Database database;
    private MediaPlayer sendSound;

    private DatabaseReference reference;

    private ImageButton btnSend;
    private EditText textSend;

    private ChatAdapter chatAdapter;

    private RecyclerView recyclerView;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        database = new Database(new MD5());
        setIds();
        getIntentDetails();
        setClickListeners();
        loadOldChat(chattedCar);
        setSounds(ChatActivity.this);

    }

    public void setSounds(Context context){
        sendSound = MediaPlayer.create(context, R.raw.send_message_sound);
    }

    private void loadOldChat(Car chattedCar) {

        final String chatKey = ApplicationModel.getCurrentCar().getHashMap().get(chattedCar.getCarNumber());

        database.searchChatByKey(chatKey, new OnGetDataListener() {
            @Override
            public void onSuccess(Object object) {

                if(object != null) {
                    readChat(chatKey);
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

                Log.d("BUBA", "onFailure: i failed bitch ");
            }
        });
    }

    public void makeSendSound(){
        sendSound.start();
    }

    private void setClickListeners() {

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = textSend.getText().toString();
                if(!msg.equals("")){
                    Log.d("BUBA", "msg is  " + msg);
                    Message newMessage = new Message(ApplicationModel.currentCar.getCarNumber(),chattedCar.getCarNumber(),msg);
                    sendMessage(newMessage);
                    makeSendSound();
                }
                textSend.setText("");
            }
        });
    }

    private void getIntentDetails() {

        intent = getIntent();
        chattedCar = (Car)intent.getSerializableExtra("chattedCar");
        String imageId = chattedCar.getEmojiId();
        profieImage.setImageResource(MainActivity.emojiMap.get(imageId));
        chattedCarNicknameTV.setText(chattedCar.getNickname());
        chattedCarNumberTV.setText(chattedCar.getCarNumber());
        if(chattedCar.getNickname().equals(chattedCar.getCarNumber())){
            chattedCarNicknameTV.setText("");
        }

    }

    private void setIds() {

        btnSend = (ImageButton)findViewById(R.id.btn_send);
        textSend = (EditText) findViewById(R.id.text_send);
        profieImage = (ImageView)findViewById(R.id.profile_image);
        chattedCarNicknameTV = (TextView)findViewById(R.id.nickname);
        chattedCarNumberTV = (TextView)findViewById(R.id.car_number);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void sendMessage(final Message newMessage){

        Log.d("BUBA", "message = : " + newMessage);
        final String chatKey = ApplicationModel.getCurrentCar().getHashMap().get(newMessage.getReceiver());

        database.searchChatByKey(chatKey, new OnGetDataListener() {
            @Override
            public void onSuccess(Object object) {

                Chat chat;

                if(object != null){
                    Log.d("BUBA", "chat was  found in database: ");

                    chat = (Chat)object;
                   chat.addMessage(newMessage);
                    database.saveChat(chat);
                } else {
                    //if there is no chat between those cars.
                    Log.d("BUBA", "chat was not found in database: ");
                    chat = new Chat(chatKey);
                    chat.addMessage(newMessage);
                    Log.d("BUBA", "chat: " + chat.getMessages().get(0).getMessage());
                    database.saveChat(chat);
                }

                readChat(chatKey);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

                Log.d("BUBA", "onFailure: i failed bitch ");
            }
        });
    }

    private void readChat(String chatKey){

        database.searchChatByKey(chatKey, new OnGetDataListener() {
            @Override
            public void onSuccess(Object object) {

                Chat chat = (Chat)object;

                Log.d("BUBA", "chat is : " + chat);

                chatAdapter = new ChatAdapter(ChatActivity.this,chat,chattedCar.getEmojiId());
                recyclerView.setAdapter(chatAdapter);

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });

    }
}
