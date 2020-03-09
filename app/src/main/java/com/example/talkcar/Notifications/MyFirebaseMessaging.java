package com.example.talkcar.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.talkcar.AllChatsActivity;
import com.example.talkcar.ApplicationModel;
import com.example.talkcar.Chat;
import com.example.talkcar.ChatActivity;
import com.example.talkcar.Database;
import com.example.talkcar.LoginActivity;
import com.example.talkcar.MainActivity;
import com.example.talkcar.R;
import com.example.talkcar.SettingsActivity;
import com.example.talkcar.SignupActivity;
import com.example.talkcar.WaitingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    final String CHANNEL_ID = "talkcar";
    final String CHANNEL_NAME = "TalkCar";
    final String CHANNEL_DESC = "TalkCar Notification";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("LIBI", "ApplicationModel: " + ApplicationModel.getCurrentDriver());
        Log.d("LIBI", "onMessageReceived: " + remoteMessage.getData());
        String sented = remoteMessage.getData().get("sented");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Log.d("LIBI", "firebase user : " + firebaseUser);
        Log.d("LIBI", "sented: " + sented);
        Log.d("LIBI", "firebaseUser.getUid()): " + firebaseUser.getUid());

        String keyChat = remoteMessage.getData().get("keyChat");
        Log.d("LIBI", "key chat : " + keyChat);
        Log.d("LIBI", "ApplicationModel.currentChatKey : " + ApplicationModel.currentChatKey);


        if(firebaseUser!= null) {

            if (!(ChatActivity.isActive || MainActivity.isActive || LoginActivity.isActive || SignupActivity.isActive ||
                    WaitingActivity.isActive || SettingsActivity.isActive || AllChatsActivity.isActive)) {
                displayNotification(remoteMessage);
            }

            if(MainActivity.isActive){

            }

            if (keyChat.equals(ApplicationModel.currentChatKey)) {
                ChatActivity.readChat(keyChat);
            }
        }
    }

    private void displayNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        //When clicking the notification the user will be pass to WaitingActivity
        Intent intent = new Intent(this, WaitingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent, PendingIntent.FLAG_ONE_SHOT);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder bullder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if(j > 0){
            i = j;
        }
        notificationManager.notify(i, bullder.build());

    }
}
