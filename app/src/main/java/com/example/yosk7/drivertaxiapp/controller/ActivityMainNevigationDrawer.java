package com.example.yosk7.drivertaxiapp.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.yosk7.drivertaxiapp.R;
import com.example.yosk7.drivertaxiapp.model.backend.BackendFactory;
import com.example.yosk7.drivertaxiapp.model.datasource.Firebase_DBManager;
import com.example.yosk7.drivertaxiapp.model.datasource.Utils;
import com.example.yosk7.drivertaxiapp.model.entities.Client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.yosk7.drivertaxiapp.model.datasource.Firebase_DBManager.notifyToclientsList;
import static com.example.yosk7.drivertaxiapp.model.datasource.Firebase_DBManager.stopNotifyToclientList;
import static com.example.yosk7.drivertaxiapp.model.datasource.Firebase_DBManager.stopNotifyToclientList2;


public class ActivityMainNevigationDrawer extends AppCompatActivity {

    final String TAG = "myMainActivityNavigationDrawer";
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    static Boolean finishLodingDataFlag = false;
    static int fragment = 0;
    FragmentFreeClients fragment1 = new FragmentFreeClients();
    FragmentDoneDrive fragment2 = new FragmentDoneDrive();
    public String firebaseDriverKey;
    public Long firebaseDriverId;
    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        //      start loading clients with "finishLodingDataFlag"
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Long doInBackground(Void... params) {

                notifyToclientsList(new Utils.NotifyDataChange<List<Client>>() {
                    @Override
                    public void OnDataChanged(List<Client> obj) {
                    }

                    @Override
                    public void onFailure(Exception exception) {
                    }
                }, new Utils.NotifyEndLoadinData<Boolean>() {
                    @Override
                    public void onEnd(Boolean obj) {
                        if (obj == true) { // succeed loading all data from fireBase
                            finishLodingDataFlag = true;
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            //stop first listener (listen for first charging of clients)
                            stopNotifyToclientList();
                        }
                    }
                });
                return Long.valueOf(0);
            }

            @Override
            protected void onPostExecute(Long aLong) {
            }
        }.execute();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nevigation_drawer);
        setTitle("  Welcome To Taxi App  !");

//      get from the intent activated the activity info
        Bundle resultIntent = getIntent().getExtras();
        if (resultIntent != null) {
            firebaseDriverKey = resultIntent.getString("firebaseDriverKey");
            firebaseDriverId = resultIntent.getLong("firebaseDriverId");
        }


        // Log.d(TAG, " myMainActivityN");

        // register receiver
        registerReceiver(
                new MyBroadcastReceiver(),
                new IntentFilter("broadcasrecevier.com.broadcastrecivierbasicexample.A_CUSTOM_INTENT"));

//       navigation drawer
        dl = (DrawerLayout) findViewById(R.id.activity_main_nevigation_drawer);
        t = new ActionBarDrawerToggle(this, dl, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle("Open          Welcome To Taxi App !");
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("Close          Welcome To Taxi App !");
                supportInvalidateOptionsMenu();
            }
        };
        dl.addDrawerListener(t);
        t.syncState();
        nv = (NavigationView) findViewById(R.id.nv);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (finishLodingDataFlag)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);////////////setDisplayHomeAsUpEnabled///////////
        else
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_Rides:
                        loadFragment(fragment1);
                        fragment = 1;
                        return true;
                    case R.id.nav_Contacts:
                        loadFragment(fragment2);
                        fragment = 2;
                        permissions();
                        return true;
                    case R.id.nav_exit:
                        finish();
                        System.exit(0);
                    default:
                        return true;
                }
            }


        });


        //start service listening for new clients only

        startService(new Intent(getBaseContext(), MyService.class));
        //showContacts();
    }

//end on create


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //last fregment was in using (for reopen)
        if (fragment == 1)
            loadFragment(fragment1);
            //   loadFragment(fragmentnotifyToDriversList1);
        else if (fragment == 2)
            loadFragment(fragment2);

    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//      stop service listening for new clients only
        stopService(new Intent(getBaseContext(), MyService.class));

//      stop  listener2 listen for - new- clients only
        stopNotifyToclientList2();

        // when exist and stop activity   ->   Reset Clock
        Firebase_DBManager.setStarTime(0);  // when exist and stop activity   ->   Reset Clock
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);

    }

    //  load fragment function
    private void loadFragment(Fragment fragment) {
        dl.closeDrawer(nv);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes


    }


    private void permissions() {
        //     Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, 5);
        }


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                //stopUpdateButton.setEnabled(true);
                //getLocationButton.setEnabled(false);
                // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            } else {
                Toast.makeText(this, "Until you grant the permission, we canot continue", Toast.LENGTH_SHORT).show();
            }
        }

    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);












//    /**
//     * Show the contacts in the ListView.
//     */
//    private void showContacts() {
//        // Check the SDK version and whether the permission is already granted or not.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
//            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
//        } else {
//            // Android version is lesser than 6.0 or the permission is already granted.
//            //List<String> contacts = getContactNames();
//           // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
////            lstNames.setAdapter(adapter);
//        }
//    }
//
////    @Override
////    public void onRequestPermissionsResult(int requestCode, String[] permissions,
////                                           int[] grantResults) {
////        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
////            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                // Permission is granted
////                showContacts();
////            } else {
////                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
////            }
////        }
////    }
////
////    /**
////     * Read the name of all the contacts.
////     *
////     * @return a list of names.
////     */
////    private List<String> getContactNames() {
////        List<String> contacts = new ArrayList<>();
////        // Get the ContentResolver
////        ContentResolver cr = getContentResolver();
////        // Get the Cursor of all the contacts
////        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
////
////        // Move the cursor to first. Also check whether the cursor is empty or not.
////        if (cursor.moveToFirst()) {
////            // Iterate through the cursor
////            do {
////                // Get the contacts name
////                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
////                contacts.add(name);
////            } while (cursor.moveToNext());
////        }
////        // Close the curosor
////        cursor.close();
////
////        return contacts;
////    }
//
//
}
