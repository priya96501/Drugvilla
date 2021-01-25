package com.drugvilla.in.fcm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.util.Config;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.drugvilla.in.R;
import com.drugvilla.in.ui.others.ContactUs;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MyMessagingService extends FirebaseMessagingService {

    private Uri defaultUri;
    private PendingIntent pendingIntent;
    private Intent intent;
    String image_url;
    Bitmap bitmap;
    private static final String TAG = "MyFirebaseMessagingServ";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // if app is open then  data is displayed and if app is not running or closed then notification is displayed

        //if app is not running or closed then notification is displayed
        if (remoteMessage.getNotification() != null) {
            Log.d("msg", "Message From : " + remoteMessage.getFrom()); //sender ID
            Log.d("msg", "Notification Title : " + remoteMessage.getNotification().getTitle()); //notification title
            Log.d("msg", "Notification Body  : " + remoteMessage.getNotification().getBody()); //notification body

        }
        // if app is open then  data is displayed
        Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("message"));
        Log.d("msgdf", "data : " + remoteMessage.getData().toString());
        Log.d("msgdf", "data : " + remoteMessage.getData().get("body"));
        Log.d("msgdf", "data : " + remoteMessage.getData().get("title"));
        image_url = remoteMessage.getData().get("image");
        Log.d("msgdf", "data : " + image_url);

        if (remoteMessage.getData() != null) {
            //To get a Bitmap image from the URL received
            bitmap = getBitmapfromUrl(image_url);
            sendNotification(remoteMessage, bitmap);
        }
    }


    /*
     *To get a Bitmap image from the URL received
     * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    private void sendNotification(RemoteMessage remotemessage, Bitmap image) {
        Map<String, String> data = remotemessage.getData();

        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        //style.bigPicture(bitmap);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(getApplicationContext(), ContactUs.class);// set the activity on clicking any notification
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

            //Configure Notification Channel
            notificationChannel.setDescription("Game Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(data.get("title"))
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(data.get("body"))
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image))/*Notification with Image*/
                .setLargeIcon(image)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);


        notificationManager.notify(1, notificationBuilder.build());


    }

   /* private void getImage(final RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();

        com.drugvilla.in.fcm.Config.content = data.get("content");
        com.drugvilla.in.fcm.Config.imageUrl = data.get("imageUrl");
        com.drugvilla.in.fcm.Config.gameUrl = data.get("gameUrl");
        com.drugvilla.in.fcm.Config.title = data.get("title");
        //Create thread to fetch image from notification
        if (remoteMessage.getData() != null) {

          *//*  uiHandler.post(new Runnable() {
                @Override
                public void run() {*//*
            // Get image from data Notification
         //   Picasso.with(getApplicationContext()).load(com.drugvilla.in.fcm.Config.imageUrl).into(target);
               *//* }
            }) ;*//*
        }
    }
*/

    private void setNotification(RemoteMessage remotemessage) {

        Map<String, String> data = remotemessage.getData();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = "Default";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Default channel for app");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(data.get("title"))
                .setContentText(data.get("message"))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        manager.notify(1, builder.build());

    }

    private void setUpPendingIntent1(String click_action) {
        intent = new Intent(click_action);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
