package com.example.yosk7.drivertaxiapp.controller;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import android.app.Notification;

import com.example.yosk7.drivertaxiapp.R;
import com.example.yosk7.drivertaxiapp.model.backend.BackendFactory;
import com.example.yosk7.drivertaxiapp.model.entities.Client;
import com.example.yosk7.drivertaxiapp.model.entities.Driver;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class MyBroadcastReceiver extends BroadcastReceiver {
    //    @Override
//    public void onReceive(Context context, Intent intent) {
//
//    }

    public MyBroadcastReceiver() {


    }

    final String TAG = "myBroadcastReceiver";
//    private int MY_NOTFICATION_ID;
//    final String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
//    @SuppressLint("WrongConstant")
//    NotificationChannel notificationChannel;
//    NotificationManager notificationManager;

    private int MY_NOTFICATION_ID;
    final String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    //   final String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    @SuppressLint("WrongConstant")
    NotificationChannel notificationChannel;
    NotificationManager notificationManager;


    /**
     * Called when the activity is first created.
     */
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, " onBroadcastReceiverCreate ...");
        final PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, ActivityMainNevigationDrawer.class), FLAG_UPDATE_CURRENT);


//        Intent notificationIntent = new Intent(context,Normalizer.Form.class).putExtra("layout", "99");
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,FLAG_UPDATE_CURRENT);

//       Resume or restart the app (same as the launcher click)
//        Intent resultIntent = Intent(context, ActivityMainNevigationDrawer);

//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);



//        Intent i = new Intent(context, ActivityMainNevigationDrawer.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);


        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.CYAN);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        final NotificationCompat.Builder b = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_menu_send)
                .setContentTitle("new request")
                .setContentText("you have a new relevant request")
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");

        notificationManager.notify(1, b.build());


        // TODO Auto-generated method stub
//                    Toast.makeText(context, intent.getAction(), Toast.LENGTH_LONG).show();
//                    // Extract data included in the Intent
//                    CharSequence intentData = intent.getCharSequenceExtra("message");
//                    //Toast.makeText(context, "broadcasrecevier.example.com.broadcastrecivierbasicexample received the Intent's message: " + intentData, Toast.LENGTH_LONG).show();
//                    if (intent.getAction().matches("android.intent.action.TIME_TICK"))
//                        Toast.makeText(context, "TIME_TICK", Toast.LENGTH_LONG).show();
//                    else if (intent.getAction().matches("android.intent.action.BOOT_COMPLETED"))
//                        Toast.makeText(context, "BOOT_COMPLETED", Toast.LENGTH_LONG).show();
//                    else if (intent.getAction().matches("android.intent.action.TIME_SET"))
//                        Toast.makeText(context, "TIME_SET", Toast.LENGTH_LONG).show();
//                    else if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
//                        Toast.makeText(context, "SMS_RECEIVED", Toast.LENGTH_LONG).show();
        // if (intent.getAction().matches("broadcasrecevier.example.com.broadcastrecivierbasicexample.A_CUSTOM_INTENT"))
        // Toast.makeText(context, "A_CUSTOM_INTENT aviad", Toast.LENGTH_LONG).show();
    }
}

