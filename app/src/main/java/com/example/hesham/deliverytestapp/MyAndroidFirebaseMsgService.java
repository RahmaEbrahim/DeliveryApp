package com.example.hesham.deliverytestapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model.Session;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //Log data to Log Cat
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //create notification
        Map<String, String> map = remoteMessage.getData();
        Session session  =  Session.getIntsance();
        String msgType = map.get("messageType");
        switch (msgType) {
            case "AdminOrderToDriver":
                if (session.getAdmin() == false) {
                    createNotification(map.get("customerNumber"), map.get("timeBalance"),remoteMessage.getNotification().getBody() , map.get("messageType"));
                }
                break;
            case "DriverAcceptedOrder":
                if (session.getAdmin() == true) {
                    createNotification(map.get(("customerNumber")), map.get("timeBalance"),remoteMessage.getNotification().getBody(), map.get("messageType"));
                }
                break;
            case "DriverRecievedOrder":
                if (session.getAdmin() == true) {
                    createNotification(map.get(("customerNumber")), map.get("timeBalance"), remoteMessage.getNotification().getBody(), map.get("messageType"));
                }
                break;
            case "DriverDeliveredOrder":
                if (session.getAdmin() == true) {
                    createNotification(map.get(("customerNumber")), map.get("timeBalance"), remoteMessage.getNotification().getBody(), map.get("messageType"));
                }
                break;
                default:
                    break;
        }
    }
    private void createNotification( String customrNumber,String timeBalance,String messageBody,String messageType) {
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageType)
                .setContentText(messageBody)
                .setAutoCancel( true )
                .setSound(notificationSoundURI);
                //.setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mNotificationBuilder.build());
    }
}