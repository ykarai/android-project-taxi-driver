package com.example.yosk7.drivertaxiapp.controller;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yosk7.drivertaxiapp.R;
import com.example.yosk7.drivertaxiapp.model.backend.BackendFactory;
import com.example.yosk7.drivertaxiapp.model.entities.Client;

import java.util.ArrayList;
import java.util.List;

public class FragmentDoneDrive extends Fragment {


    ListView listView;
    View viewA;
   // public static List<Client> clientList0;

    final String TAG = "myFragment2";
    public String firebaseDriverKey;
    public Long firebaseDriverId;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        final List<Client> clientsList = BackendFactory.getDB().getClientList();
        // Create Adapter
        DoneDriveAdepter doneDriveAdepter = new DoneDriveAdepter(getActivity(), R.layout.list_item_button, (ArrayList<Client>) clientsList);
        //doneDriveAdepter.notifyDataSetChanged();
        viewA = inflater.inflate(R.layout.fragment_contacts, container, false);
        listView = (ListView) viewA.findViewById(R.id.list_item);
        listView.setAdapter(doneDriveAdepter);

//get info from "father" acticvity
        firebaseDriverKey=((ActivityMainNevigationDrawer)this.getActivity()).firebaseDriverKey; //this refers to your fragment
        firebaseDriverId=((ActivityMainNevigationDrawer)this.getActivity()).firebaseDriverId; //this refers to your fragment
        //start filtering the ListView
        doneDriveAdepter.getFilter().filter(firebaseDriverId.toString());
        doneDriveAdepter.notifyDataSetChanged();


        return viewA;
    }




}
