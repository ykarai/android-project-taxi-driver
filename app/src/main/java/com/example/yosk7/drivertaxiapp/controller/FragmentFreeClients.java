package com.example.yosk7.drivertaxiapp.controller;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yosk7.drivertaxiapp.R;
import com.example.yosk7.drivertaxiapp.model.backend.BackendFactory;
import com.example.yosk7.drivertaxiapp.model.entities.Client;
import com.example.yosk7.drivertaxiapp.model.entities.ClientRequestStatus;
import com.example.yosk7.drivertaxiapp.model.entities.Driver;

import java.util.ArrayList;
import java.util.List;

public class FragmentFreeClients extends Fragment {


    ListView listView;
    View viewA;
    TextView textView;
    Button callButton;
    Button startDriveButton;
    Button endtDriveButton;
    Button smsButton;
    Button mailButton;


    List<Client> clientsList;
    ClientArrayAdapter clientArrayAdapter;

    int PositionclickLst;
    Client client;

    public static List<Client> clientList0;

    final String TAG = "myFragment";
    //ActivityMainNevigationDrawer a= (ActivityMainNevigationDrawer) getActivity();

    public String firebaseDriverKey;
    public Long firebaseDriverId;

     SeekBar seekBar;
     TextView TextViewseekBarProgress;
     int seekBarProg;
    ImageButton wazeButton;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {


        viewA = inflater.inflate(R.layout.fragment_free_clients, container, false);
        clientsList = BackendFactory.getDB().getClientList();
        listView = (ListView) viewA.findViewById(R.id.item_list);
        //get info from "mother" acticvity
        firebaseDriverKey=((ActivityMainNevigationDrawer)this.getActivity()).firebaseDriverKey; //this refers to your fragment
        firebaseDriverId=((ActivityMainNevigationDrawer)this.getActivity()).firebaseDriverId; //this refers to your fragment
        // Create Adapter
        clientArrayAdapter = new ClientArrayAdapter(getActivity(), R.layout.list_item, clientsList);
        listView.setAdapter(clientArrayAdapter);

        //LinearLayout for Client function
        callButton = (Button) viewA.findViewById(R.id.callButtom);
        startDriveButton = (Button) viewA.findViewById(R.id.startDriveButton);
        endtDriveButton= (Button) viewA.findViewById(R.id.endDriveButton);
        smsButton = (Button) viewA.findViewById(R.id.smsButton);
        mailButton=(Button) viewA.findViewById(R.id.mailButton);

        // Set up Call click for intent to Mail
        mailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",clientsList.get(PositionclickLst).geteMail(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        // Set up Call click for intent to sms
        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //option 1
                // send sms intent
                String phoneNumber = clientsList.get(PositionclickLst).getPhone().toString();
                Uri uri = Uri.parse("smsto:"+"+972"+phoneNumber);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "Hi I am your driver, I come to pick you up");
                startActivity(intent);


            }
        });





        // Set up Call click for intent to call
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, " callButton");
                String phoneNumber = clientsList.get(PositionclickLst).getPhone().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+972" + phoneNumber, null));
                startActivity(intent);

            }
        });

//      change client status
        startDriveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, " DriveButton");

                BackendFactory.getDB().markClientRequestStatusONWORK(PositionclickLst,firebaseDriverKey,firebaseDriverId);



            }
        });

        //      change client status
        endtDriveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, " DriveButton");

                BackendFactory.getDB().markClientRequestStatusFINISH(PositionclickLst,firebaseDriverKey,firebaseDriverId);



            }
        });


        // Set up list Row click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //listView.invalidateViews();
                client = (Client) ((ListView) parent).getAdapter().getItem(position);
                PositionclickLst = clientsList.indexOf(client);


                //  Log.d(TAG, " listClick" );
                textView = (TextView) viewA.findViewById(R.id.textView3);
              //  textView.setText("item " + client.getId().toString() + " clicked");
                String s=client.getName()+" is now in : "+client.getAddress();
                textView.setText(s);

                //textView.setText("item " + clientsList.get(position).getId().toString() + " clicked");
            }
        });

//       add filterable list
        listView.setTextFilterEnabled(true);
        EditText editTxt = (EditText) viewA.findViewById(R.id.filterEditText);
        editTxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text [" + s + "] - Start [" + start + "] - Before [" + before + "] - Count [" + count + "]");
                if (count < before) {
                    // We're deleting char so we need to reset the adapter data
                    clientArrayAdapter.resetData();
                }

                clientArrayAdapter.getFilter().filter(s.toString());
                clientArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        seekBar = (SeekBar)viewA.findViewById(R.id.seekBar);
        TextViewseekBarProgress =(TextView) viewA.findViewById(R.id.TextViewProgress);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (fromUser) {

                    clientArrayAdapter.resetData();
//                }
                seekBarProg=progress;
                String s=progress+"";
                TextViewseekBarProgress.setText(Integer.toString(progress)+" km");
                clientArrayAdapter.getFilter().filter(s);
                clientArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        //click on waze icon
        ImageButton wazeButton = (ImageButton) viewA.findViewById(R.id.waze_button);
        wazeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pickupAddress=client.getAddress();
                //String s2=BackendFactory.getDB().getDriverList().ge
                String lonlang =client.getStartPoint().getLatitude()+","+client.getStartPoint().getLongitude();
                String uri = "waze://?ll="+lonlang+"&navigate=yes";
                startActivity(new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(uri)));

            }
        });



        registerForContextMenu(listView);
        return viewA;
    }


    //////////////////   context Menu

//    // We want to create a context Menu when the user long click on an item
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//
//        super.onCreateContextMenu(menu, v, menuInfo);
//        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
//
//        // We know that each row in the adapter is a Map
//        Client client = ClientArrayAdapter.getItem0(aInfo.position);
//
//        menu.setHeaderTitle("Options for " + client.getName());
//        menu.add(1, 1, 1, "Details");
//        menu.add(1, 2, 2, "Delete");
//
//    }


//    Runnable run = new Runnable() {
//        public void run() {
//            //reload content
//            clientsList.clear();
//            clientsList.addAll(BackendFactory.getDB().getClientList());
//            clientArrayAdapter.notifyDataSetChanged();
//            listView.invalidateViews();
//            listView.refreshDrawableState();
//        }
//    };


    // This method is called when user selects an Item in the Context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        clientsList.remove(aInfo.position);
        clientArrayAdapter.notifyDataSetChanged();
        return true;
    }

}