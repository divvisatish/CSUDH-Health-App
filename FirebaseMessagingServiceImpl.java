package com.csudh.healthapp.csudhhealthapp;

/**
 * Created by divvi on 11/26/2017.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by divvi on 11/21/2017.
 */

public class FirebaseMessagingServiceImpl extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size() > 0) {

            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            String clickAction = remoteMessage.getNotification().getClickAction();
            String requestType = remoteMessage.getData().get("request_type");
            String bloodType = remoteMessage.getData().get("blood_type");
            String comment = remoteMessage.getData().get("comment");
            String flag = remoteMessage.getData().get("flag");

            //showNotifications(payload);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
            builder.setSmallIcon(R.drawable.csudhhealthlogo)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(new NotificationCompat.BigTextStyle().bigText("Request type: "+requestType+"\nRequired blood type: "+bloodType+"\nComments: "+comment));

            Intent resultIntent = new Intent(clickAction);

            Bundle bundle = new Bundle();
            bundle.putString("requestType", requestType);
            bundle.putString("bloodType",bloodType);
            bundle.putString("comment",comment);
            bundle.putString("flag",flag);

            resultIntent.putExtras(bundle);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            int notificationId = (int) System.currentTimeMillis();
            notificationManager.notify(notificationId,builder.build());

        }
    }

    /*private void showNotifications(Map<String, String> payload) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(payload.get("username"));
        builder.setContentText(payload.get("email"));
        Intent resultIntent = new Intent(getApplicationContext(), HomepageActivity.class);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getApplicationContext());

        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());
    }*/
}
