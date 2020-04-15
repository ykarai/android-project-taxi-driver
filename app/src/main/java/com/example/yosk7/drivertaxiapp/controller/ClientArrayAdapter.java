package com.example.yosk7.drivertaxiapp.controller;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.yosk7.drivertaxiapp.R;
import com.example.yosk7.drivertaxiapp.model.backend.BackendFactory;
import com.example.yosk7.drivertaxiapp.model.datasource.Firebase_DBManager;
import com.example.yosk7.drivertaxiapp.model.entities.Client;

import java.util.ArrayList;
import java.util.List;

import android.widget.Filter;

public class ClientArrayAdapter extends ArrayAdapter<Client> implements Filterable {

    private int listItemLayout;
    private Context contextt;
    private List<Client> clienttList;
    //  List<Client> nClienttList2 = new ArrayList<Client>();

    private List<Client> origClientList;

    private Filter planetFilter;

    public ClientArrayAdapter(Context context, int layoutId, List<Client> clientList) {
        super(context, layoutId, clientList);
        listItemLayout = layoutId;
        this.clienttList = clientList;
        this.origClientList = clientList;
        this.contextt = context;

    }


    public int getCount() {
        return clienttList.size();
    }

    public Client getItem(int position) {
        return clienttList.get(position);
    }

//    public static Client getItem0(int position) {
//        return clienttList.get(position);
//    }

    @Override
    public long getItemId(int position) {
        return clienttList.get(position).hashCode();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Client client = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
//             LayoutInflater inflater = LayoutInflater.from(getContext());
            //           LayoutInflater inflater = LayoutInflater.from(contextt);
            LayoutInflater inflater = (LayoutInflater) contextt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(listItemLayout, parent, false);

            viewHolder.item1 = (TextView) convertView.findViewById(R.id.row_item1);
            viewHolder.item2 = (TextView) convertView.findViewById(R.id.row_item2);
            viewHolder.item3 = (TextView) convertView.findViewById(R.id.row_item3);
            viewHolder.item3.setBackgroundColor(Color.WHITE);
            viewHolder.item3.setTextColor(Color.BLACK);

            convertView.setTag(viewHolder); // view lookup cache stored in tag
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        //viewHolder.item1.setText(client.getId().toString());
        viewHolder.item2.setText(client.getName());
        // viewHolder.item3.setText(client.getDestinationPoint().toString());
        viewHolder.item3.setText(showDistance(client));

// check if client added to the Firebase after the time- StarTime
// (the startTime: is when we start to listen to Firebase with listener )
        if (client.getTime0() > Firebase_DBManager.getStarTime()) {
            viewHolder.item2.setTextColor(Color.BLUE);
//            viewHolder.item1.setTextColor(Color.YELLOW);
//            viewHolder.item1.setBackgroundColor(Color.RED);
        } else {
            viewHolder.item2.setTextColor(Color.GRAY);
//            viewHolder.item1.setBackgroundColor(Color.WHITE);
//            viewHolder.item1.setTextColor(Color.BLACK);

        }
        // Return the completed view to render on screen
        return convertView;
    }

    private static class ViewHolder {
        TextView item1;
        TextView item2;
        TextView item3;
    }

    public void resetData() {
        clienttList = origClientList;
    }

    /*
     * We create our filter
     */

    @Override
    public Filter getFilter() {
        if (planetFilter == null) //Filter claSS
            planetFilter = new PlanetFilter();

        return planetFilter;
    }


    private class PlanetFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            Boolean filter2 = false;
            int filter2distance=0;
            // We implement here the filter logic
            if (android.text.TextUtils.isDigitsOnly(constraint)) {
                final String result = constraint.toString().replaceAll("[^0-9]+", " ");
                filter2distance = Integer.parseInt(result);
                filter2 = true;
                int i=filter2distance;
            }

            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origClientList;
                results.count = origClientList.size();
            } else {
                // We perform filtering operation
                List<Client> nClienttList = new ArrayList<Client>();

                if (filter2)
                    for (Client p : clienttList) {
                    int t =(showDistance2(p));
                        if ( t< filter2distance && t!=0) {
                            nClienttList.add(p);
                        }
                    }
                else {
                    for (Client p : clienttList) {
                        if (p.getStatus().toString().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                            nClienttList.add(p);
                        }
                    }
                }

                results.values = nClienttList;
                results.count = nClienttList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                clienttList = (List<Client>) results.values;
                notifyDataSetChanged();
            }

        }

    }


    public void updateData(List data) {

        this.clienttList = data;
        notifyDataSetChanged();
    }

    public List<Client> getData() {
        return BackendFactory.getDB().getClientList();
    }


    private String showDistance(Client client) {

        float[] results = new float[1];
        Location.distanceBetween(client.getStartPoint().getLatitude(), client.getStartPoint().getLongitude(),
                client.getDestinationPoint().getLatitude(), client.getDestinationPoint().getLongitude(), results);

        float distance = (client.getStartPoint().distanceTo(client.getDestinationPoint()));
        if (distance > 1000) {
            return String.format("%.1f", distance / 1000) + "  km";
        } else {
            return String.format("%.0f", distance) + "    meter";
        }


    }
    private int showDistance2(Client client) {

        float[] results = new float[1];
        Location.distanceBetween(client.getStartPoint().getLatitude(), client.getStartPoint().getLongitude(),
                client.getDestinationPoint().getLatitude(), client.getDestinationPoint().getLongitude(), results);

        float distance = (client.getStartPoint().distanceTo(client.getDestinationPoint()));
        if (distance > 1000) {
            return (int) (distance / 1000);
        } else {
            return (int) distance;
        }


    }

}



