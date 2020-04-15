package com.example.yosk7.drivertaxiapp.controller;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.yosk7.drivertaxiapp.model.datasource.Firebase_DBManager;
import com.example.yosk7.drivertaxiapp.model.datasource.Utils;
import com.example.yosk7.drivertaxiapp.model.entities.Client;
import com.example.yosk7.drivertaxiapp.model.entities.Driver;

import java.util.List;

import static com.example.yosk7.drivertaxiapp.model.datasource.Firebase_DBManager.notifyToclientsList;
import static com.example.yosk7.drivertaxiapp.model.datasource.Firebase_DBManager.notifyToclientsList2;

public class MyService extends Service {


    static int count = 1;
    int id = 0, startId = -1;
    boolean isRun = false;
    final String TAG = "myService";

    public MyService() {
        id = count++;
    }

    String serviceInfo() {
        return "service [" + id + "] startId = " + startId;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        id++;
        Log.d(TAG, serviceInfo() + " onServiceCreate ...");


//      start listening for new clients only
        notifyToclientsList2(new Utils.NotifyDataChange<List<Client>>() {

            @Override
            public void OnDataChanged(List<Client> obj) {

            //    Toast.makeText(getBaseContext(), "client list cheanged ", Toast.LENGTH_LONG).show();


            //  Toast.makeText(this, "A_CUSTOM_INTENT", Toast.LENGTH_LONG).show();
                Intent intent = new Intent("MyCustomIntent");
               // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // add data to the Intent
                intent.putExtra("message", "clients list Changed");
                intent.setAction("broadcasrecevier.com.broadcastrecivierbasicexample.A_CUSTOM_INTENT");
                sendBroadcast(intent);





            }

            @Override
            public void onFailure(Exception exception) {

            }
        },0);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, serviceInfo() + " onServiceDestroy ...");
        isRun = false;
        Firebase_DBManager.stopNotifyToclientList2();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startId = startId;
        isRun = true;
        Log.d(TAG, serviceInfo() + " onServiceStartCommand start ...");

        //for Debug
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, serviceInfo() + " print ...");
                }
            }
        };
        thread.start();
        //Log.d(TAG, serviceInfo() + " onServiceStartCommand end ...");

        return Service.START_STICKY;
    }


//    //    @Override
////    public IBinder onBind(Intent intent) {
////        // TODO: Return the communication channel to the service.
////        throw new UnsupportedOperationException("Not yet implemented");
////    }

}