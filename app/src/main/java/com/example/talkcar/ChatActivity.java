package com.example.talkcar;

import androidx.annotation.NonNull;
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

import com.example.talkcar.Notifications.Client;
import com.example.talkcar.Notifications.Data;
import com.example.talkcar.Notifications.MyResponse;
import com.example.talkcar.Notifications.Sender;
import com.example.talkcar.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    APIService apiService;

    boolean notify = false;

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
                notify = true;
                String msg = textSend.getText().toString();
                if(!msg.equals("")){
                    Message newMessage = new Message(ApplicationModel.getCurrentUser().getUid(),ApplicationModel.getChattedDriverUid(),msg);
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
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

    }

    private void sendMessage(final Message newMessage){

        Log.d("LIBI", "newMessage " + newMessage);
        Log.d("LIBI", "newMessage.getSender " + newMessage.getSender());
        Log.d("LIBI", "newMessage.getRec " + newMessage.getReceiver());
        Log.d("LIBI", "newMessage.getmsg " + newMessage.getMessage());



        final String chatKey = ApplicationModel.getCurrentCar().getHashMap().get(ApplicationModel.getLastCarNumberSearch().getCarNumber());

        database.searchChatByKey(chatKey, new OnGetDataListener() {
            @Override
            public void onSuccess(Object object) {

                Chat chat;

                if(object != null){

                    chat = (Chat)object;
                    chat.addMessage(newMessage);
                    database.saveChat(chat);
                } else {
                    //if there is no chat between those cars.
                    chat = new Chat(chatKey);
                    chat.addMessage(newMessage);
                    database.saveChat(chat);
                }
                readChat(chatKey);

                //----------------------------------------------------------------------------------

                final String msg = newMessage.getMessage();

                reference = FirebaseDatabase.getInstance().getReference("Drivers").child(ApplicationModel.getCurrentUser().getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Driver driver = dataSnapshot.getValue(Driver.class);
                        Log.d("LIBI", "onDataChange: " + driver.getName());

                        if(notify){
                            //Maybe we should change the sender/reciver to somthing else
                            sendNotification(newMessage.getReceiver(),ApplicationModel.getCurrentCar().getCarNumber(),msg);
                        }
                        notify = false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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



    private void sendNotification(final String receiver, final String sender, final String msg) {

        Log.d("LIBI", "Receiver: " + receiver);

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        DatabaseReference ref = tokens.child(receiver);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(ApplicationModel.getCurrentUser().getUid(),ApplicationModel.getCurrentCar().getCarNumber(),R.mipmap.talkcar_launcher_round,sender + " :" + msg,"New Message",receiver);

                    Sender sender = new Sender(data,token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        Log.d("SUSA", "here on code 200: ");
                                        if(response.body().success != 1){
                                            Log.d("SUSA", "ERRORRRRR: ");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query query = tokens.orderByKey().equalTo(receiver);

        Log.d("LIBI", "sendNotification: query: " + query);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String token = snapshot.getValue(String.class);
                    Data data = new Data(ApplicationModel.getCurrentUser().getUid(),ApplicationModel.getCurrentCar().getCarNumber(),R.mipmap.talkcar_launcher_round,sender + " :" + msg,"New Message",receiver);

                    Sender sender = new Sender(data,token);

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        Log.d("SUSA", "here on code 200: ");
                                        if(response.body().success != 1){
                                            Log.d("SUSA", "ERRORRRRR: ");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
