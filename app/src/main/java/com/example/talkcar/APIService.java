package com.example.talkcar;

import com.example.talkcar.Notifications.MyResponse;
import com.example.talkcar.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAAoQVZ_8:APA91bF0JD6XTjc31nCqUV4CRqpzdfyjZ0G0M0sjYA8DWqrsQ42K6vXsc7i-9Mobtlzzo3UYQWNrM-v4oF35f_YM7m96G_bIs2J2gwXz-94iBnAx2A_lZ3UL_W5JGFoWyqGp9GY7DOpl"
             }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
