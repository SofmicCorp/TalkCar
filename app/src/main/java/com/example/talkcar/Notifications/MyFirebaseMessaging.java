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
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.talkcar.ApplicationModel;
import com.example.talkcar.ChatActivity;
import com.example.talkcar.Database;
import com.example.talkcar.LoginActivity;
import com.example.talkcar.R;
import com.example.talkcar.WaitingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessaging extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("LIBI", "onMessageReceived: " + remoteMessage.getData());

        String sented = remoteMessage.getData().get("sented");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Log.d("LIBI", "firebase user : " + firebaseUser);
        Log.d("LIBI", "sented: " + sented);
        Log.d("LIBI", "firebaseUser.getUid()): " + firebaseUser.getUid());

        if(firebaseUser!= null /*&& sented.equals(firebaseUser.getUid())*/){
            Log.d("LIBI", "im in if: ");

            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        Log.d("LIBI", "user: " + user);
        Log.d("LIBI", "icon: " + icon);
        Log.d("LIBI", "title: " + title);
        Log.d("LIBI", "body: " + body);


        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, WaitingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent, PendingIntent.FLAG_ONE_SHOT);

//        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder bullder = new NotificationCompat.Builder(this)
//                .setSmallIcon(Integer.parseInt(icon))
//                .setContentTitle(title)
//                .setContentText(body)
//                .setAutoCancel(true)
//                .setSound(defaultSound)
//                .setContentIntent(pendingIntent);
//        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

//        int i = 0;
//        if(j > 0){
//            i = j;
//        }
//
//        noti.notify(i, bullder.build());

        final String CHANNEL_ID = "mor";
        final String CHANNEL_NAME = "Mor";
        final String CHANNEL_DESC = "Mor Notification";


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
//                .setSmallIcon(Integer.parseInt(icon))
//                .setContentTitle(title)
//                .setContentText(body)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder bullder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if(j > 0){
            i = j;
        }

        noti.notify(i, bullder.build());

//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//        notificationManagerCompat.notify(1,bullder.build());


    }

}
