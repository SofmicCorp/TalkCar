package com.example.talkcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.talkcar.Cache.ApplicationModel;
import com.example.talkcar.Cars.Car;
import com.example.talkcar.Chats.Chat;
import com.example.talkcar.Chats.ChatAdapter;
import com.example.talkcar.Chats.Message;
import com.example.talkcar.Database.Database;
import com.example.talkcar.Driver.Driver;
import com.example.talkcar.Interfaces.APIService;
import com.example.talkcar.Interfaces.OnGetDataListener;
import com.example.talkcar.Notifications.Client;
import com.example.talkcar.Notifications.Data;
import com.example.talkcar.Notifications.MyResponse;
import com.example.talkcar.Notifications.Sender;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private ImageView profieImage;
    private TextView chattedCarNicknameTV;
    private TextView chattedCarNumberTV;
    private static Car chattedCar;
    public static Database database;
    private MediaPlayer sendSound;
    private static MediaPlayer receiveSound;
    public static boolean isActive = false;
    private int keyIndex;
    public static Activity activity;
    public static Context context;
    private String chatKey;

    private DatabaseReference reference;

    private ImageButton btnSend;
    private EditText textSend;

    private static ChatAdapter chatAdapter;

    private static RecyclerView recyclerView;

    private Intent intent;

    APIService apiService;

    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setIds();
        activity = this;
        getIntentDetails();
        setClickListeners();
        chatKey = ApplicationModel.chattedCarsMap.getKeyChats().get(keyIndex);
        loadOldChat(chattedCar);
        setSounds();
    }

    @Override
    public void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isActive = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ApplicationModel.currentChatKey = null;
    }

    public void setSounds(){
        sendSound = MediaPlayer.create(ChatActivity.this, R.raw.send_message_sound);
        receiveSound = MediaPlayer.create(ChatActivity.this,R.raw.recieve_message_sound);
    }

    private void loadOldChat(Car chattedCar) {

            ApplicationModel.currentChatKey = chatKey;
            readChat(chatKey);
    }

    public void makeSendSound(){
        sendSound.start();
    }

    public static void makeReceiveSound(){
        receiveSound.start();
    }

    private void setClickListeners() {

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = textSend.getText().toString();
                if(!msg.equals("")){
                    Message newMessage = new Message(FirebaseAuth.getInstance().getCurrentUser().getUid(),ApplicationModel.getChattedDriverUid(),msg);
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
        keyIndex = intent.getIntExtra("index",-1);
        profieImage.setImageResource(MainActivity.emojiMap.get(chattedCar.getEmojiId()));
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

        context = this;

    }

    private void sendMessage(final Message newMessage){

        database.searchChatByKey(chatKey, new OnGetDataListener() {
            @Override
            public void onSuccess(Object object) {

                Chat chat;
                if(object != null){
                    chat = (Chat)object;
                } else {
                    //if there is no chat between those cars.
                    chat = new Chat(chatKey);
                }
                chat.addMessage(newMessage);
                chat.setSomeMessageWereNotRead(true);
                database.saveChat(chat);
                readChat(chatKey);

                //----------------------------------------------------------------------------------

                final String msg = newMessage.getMessage();

                reference = FirebaseDatabase.getInstance().getReference("Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Driver driver = dataSnapshot.getValue(Driver.class);

                        if(notify){
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

            }
        });
    }


    private void sendNotification(final String receiver, final String sender, final String msg) {

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String token = snapshot.getValue(String.class);
                    Data data = new Data(FirebaseAuth.getInstance().getCurrentUser().getUid(),chatKey,R.mipmap.talkcar_launcher_round,sender + " :" + msg,"BIP BIP! Someone just sent you a message",receiver);

                    Sender sender = new Sender(data,token);

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        if(response.body().success != 1){
                                            Log.d("ERROR", "Error has been accord: ");
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

    public static void readChat(String chatKey){

        database.searchChatByKey(chatKey, new OnGetDataListener() {
            @Override
            public void onSuccess(Object object) {
                if(object == null)
                    return;
                Chat chat = (Chat)object;
                if(chat.getMessages().get(chat.getMessages().size() -1).getReceiver().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    if(chat.isSomeMessageWereNotRead()){
                        makeReceiveSound();
                    }
                    chat.setSomeMessageWereNotRead(false);
                    MainActivity.someMessageWereNotRead = false;
                    Database.saveChat(chat);
                }
                chatAdapter = new ChatAdapter(context,chat,chattedCar.getEmojiId());
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
