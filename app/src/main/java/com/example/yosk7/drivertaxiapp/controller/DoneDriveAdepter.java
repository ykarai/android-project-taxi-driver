package com.example.yosk7.drivertaxiapp.controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import com.example.yosk7.drivertaxiapp.R;
import com.example.yosk7.drivertaxiapp.model.entities.Client;

import java.util.ArrayList;
import java.util.List;

import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;
import android.widget.Toast;

public class DoneDriveAdepter extends ArrayAdapter<Client> {
    final String TAG = "AdepterDoneDriverFragme";
    public Context context;
    private int listItemLayout;
    private List<Client> clientList;
    private List<Client> origClientList;
    private Filter planetFilter;

    public DoneDriveAdepter(Context context, int layoutId, ArrayList<Client> clientList) {
        super(context, layoutId, clientList);
        listItemLayout = layoutId;
        context = context;
        this.clientList = clientList;
        this.origClientList = clientList;


    }

    public int getCount() {
        return clientList.size();
    }

    public Client getItem(int position) {
        return clientList.get(position);
    }

//    public static Client getItem0(int position) {
//        return clienttList.get(position);
//    }

    @Override
    public long getItemId(int position) {
        return clientList.get(position).hashCode();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Client client = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(listItemLayout, parent, false);

            viewHolder.item1 = (TextView) convertView.findViewById(R.id.row_item1B);
            viewHolder.item2 = (TextView) convertView.findViewById(R.id.row_item_2B);
            viewHolder.btn_countact = (Button) convertView.findViewById(R.id.countactButton);

            convertView.setTag(viewHolder); // view lookup cache stored in tag
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.item1.setText(client.getId().toString());
        viewHolder.item2.setText(client.getName());

        viewHolder.btn_countact.setTag(R.integer.btn_conta_view, convertView);
        viewHolder.btn_countact.setTag(R.integer.btn_conta_pos, position);

        viewHolder.btn_countact.setOnClickListener(new View.OnClickListener() {
            @Override
            // The logic on press on the (Button) contacts
            public void onClick(View v) {


                Log.d(TAG, "press: " + client.getId());
//               addContact(getContext(),client.getName(),client.getPhone());


                final AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                // adb.setView(alertDialogView);
                adb.setTitle("You're sure you want to add this traveler to your contacts?");
                adb.setMessage("Name: " + client.getName() + "\nPhone: " + client.getPhone());
                adb.setIcon(android.R.mipmap.sym_def_app_icon);
                adb.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        addContact(getContext(), client.getName(), client.getPhone());
                        Toast.makeText(getContext(), " + added to Contact", Toast.LENGTH_SHORT).show();

                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                adb.show();

            }
        });


        // Return the completed view to render on screen
        return convertView;
    }


    private static class ViewHolder {
        TextView item1, item2;
        Button btn_countact;

    }

    private void addContact(Context ctx, String displayName, String phoneNumber) {

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                .withValue(RawContacts.ACCOUNT_TYPE, null)
                .withValue(RawContacts.ACCOUNT_NAME, null).build());

        //Phone Number
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                .withValue(Phone.NUMBER, phoneNumber)
                .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                .withValue(Phone.TYPE, "1").build());

        //Display name/Contact name
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.DISPLAY_NAME, displayName)
                .build());


// Asking the Contact provider to create a new contact

        // Executing all the insert operations as a single database transaction
        // getContext().getApplicationContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
// Asking the Contact provider to create a new contact
        try {
            ContentProviderResult[] res = getContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Log.d(TAG, "added contact");

        } catch (RemoteException e) {
            Log.d(TAG, "no added contact");

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public void resetData() {
        clientList = origClientList;
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
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origClientList;
                results.count = origClientList.size();
            } else {
                // We perform filtering operation
                List<Client> nClienttList = new ArrayList<Client>();
                for (Client p : clientList) {
                    //  p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase())
                    if (p.getDriverId().toString().equals(constraint.toString())) {
                        nClienttList.add(p);

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
            //if (results.count == 0)
                //notifyDataSetInvalidated();

                clientList = (List<Client>) results.values;
                notifyDataSetChanged();



        }


    }


}
