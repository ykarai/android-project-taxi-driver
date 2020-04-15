package com.example.yosk7.drivertaxiapp.model.datasource;

import android.support.annotation.NonNull;
import android.util.Log;


import com.example.yosk7.drivertaxiapp.model.entities.ClientRequestStatus;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.yosk7.drivertaxiapp.model.backend.Backend;
import com.example.yosk7.drivertaxiapp.model.entities.Client;
import com.example.yosk7.drivertaxiapp.model.entities.Driver;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * implementaion of our app Backend with Firebase
 */
public class Firebase_DBManager implements Backend {


    public final static String TAG = "myFirebase_DBManager";
    static FirebaseDatabase database;
    static DatabaseReference driversRef;
    static DatabaseReference clientsRef;

    static List<Driver> driversList;
    static List<Client> clientsList;

    static ChildEventListener driversRefChildEventListener;
    static ChildEventListener driversRefChildEventListener2;

    static ChildEventListener clientsRefChildEventListener;
    static ChildEventListener clientsRefChildEventListener2;

    static long starTime;

    static {
        database = FirebaseDatabase.getInstance();
        driversRef = database.getReference("drivers");
        clientsRef = database.getReference("clients");


        driversList = new ArrayList<>();
        clientsList = new ArrayList<>();
    }

    /*
       add driver to database
     */
    @Override
    public Long addDriver(Driver driver, Utils.Action<Long> action) {

        Task<Void> upload = driversRef.push().setValue(driver);
        return driver.getId();

    }

    /**
     * add drivers from firebase to local driver list ,ValueEvent listener: return true when finish loading all drivers from Firebase
     * @param notifyDataChange Helps to alert about data changes on driver's in Firebase
     * @param notifyEndLoadinData  Helps to alert when finish loading (first loading) *all* the driver's data from Firebase, implement by ValueEventListener listener
     */
    public static void notifyToDriversList(final Utils.NotifyDataChange<List<Driver>> notifyDataChange,
                                           final Utils.NotifyEndLoadinData<Boolean> notifyEndLoadinData)
    {
        if (notifyDataChange != null) {
            if (driversRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify driver list"));
                return;
            }
            driversList.clear();
            driversRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    String id = dataSnapshot.getKey();
                    driver.setPushFireBaseKey(id);
                    // add to the local listDriver
                    driversList.add(driver);
                    notifyDataChange.OnDataChanged(driversList);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Driver student = dataSnapshot.getValue(Driver.class);
                    String id = dataSnapshot.getKey();
                    student.setPushFireBaseKey(id);
                    // change at the local listDriver
                    for (int i = 0; i < driversList.size(); i++) {
                        if (driversList.get(i).getId().equals(id)) {
                            driversList.set(i, student);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(driversList);
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    String id = dataSnapshot.getKey();
                    driver.setPushFireBaseKey(id);
                    for (int i = 0; i < driversList.size(); i++) {
                        if (driversList.get(i).getPushFireBaseKey() == id) {
                            driversList.remove(i);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(driversList);
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };

            driversRef.addChildEventListener(driversRefChildEventListener);

            driversRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("We're done loading the initial data " + dataSnapshot.getChildrenCount() + " items");
                    notifyEndLoadinData.onEnd(true);
//                    stopNotifyToclientList();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });


        }
    }

    /**
     * stop the listener for driverList
     */
    public static void stopNotifyToDriverList() {
        if (driversRefChildEventListener != null) {
            driversRef.removeEventListener(driversRefChildEventListener);
            driversRefChildEventListener = null;
        }

    }

    /**
     * add drivers from firebase to local driver list
     * @param notifyDataChange Helps to alert about data changes on driver's in Firebase
     */
    public static void notifyToDriversList2(final Utils.NotifyDataChange<List<Driver>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (driversRefChildEventListener2 != null) {
                notifyDataChange.onFailure(new Exception("first unNotify driver list"));
                return;
            }
            driversList.clear();
            driversRefChildEventListener2 = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    String id = dataSnapshot.getKey();
                    driver.setId(Long.parseLong(id));
                    driversList.add(driver);
                    notifyDataChange.OnDataChanged(driversList);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Driver student = dataSnapshot.getValue(Driver.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    student.setId(id);
                    for (int i = 0; i < driversList.size(); i++) {
                        if (driversList.get(i).getId().equals(id)) {
                            driversList.set(i, student);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(driversList);
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    driver.setId(id);
                    for (int i = 0; i < driversList.size(); i++) {
                        if (driversList.get(i).getId() == id) {
                            driversList.remove(i);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(driversList);
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };

            driversRef.addChildEventListener(driversRefChildEventListener2);
        }
    }
    /**
     * stop the listener for driverList
     */
    public static void stopNotifyToDriverList2() {
        if (driversRefChildEventListener2 != null) {
            driversRef.removeEventListener(driversRefChildEventListener2);
            driversRefChildEventListener2 = null;
        }
    }



    /**
     * add client's from firebase to local client's list ,ValueEvent listener: return true when finish loading all client's from Firebase
     * @param notifyDataChange Helps to alert about data changes on client's in Firebase
     * @param notifyEndLoadinData  Helps to alert when finish loading (first loading) *all* the client's data from Firebase, implement by ValueEventListener listener
     */
    public static void notifyToclientsList(final Utils.NotifyDataChange<List<Client>> notifyDataChange,
                                           final Utils.NotifyEndLoadinData<Boolean> notifyEndLoadinData) {
        if (notifyDataChange != null) {
            if (clientsRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify client list"));
                return;
            }
            clientsList.clear();
            clientsRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    // Log.d(TAG, " onNotifyToclientsList ...");
                    Client client = dataSnapshot.getValue(Client.class);
                    String id = dataSnapshot.getKey();
                    client.setId(Long.parseLong(id));
                    clientsList.add(client);
                    Log.d(TAG, " List Size :===" + clientsList.size() + "my name" + client.getId());
                    notifyDataChange.OnDataChanged(clientsList);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Client client = dataSnapshot.getValue(Client.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    client.setId(id);
                    for (int i = 0; i < clientsList.size(); i++) {
                        if (clientsList.get(i).getId().equals(id)) {
                            clientsList.set(i, client);
                            break;
                        }
                    }
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Client client = dataSnapshot.getValue(Client.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    client.setId(id);
                    for (int i = 0; i < clientsList.size(); i++) {
                        if (clientsList.get(i).getId() == id) {
                            clientsList.remove(i);
                            break;
                        }
                    }
//                    notifyDataChange.OnDataChanged(clientsList);
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };

            // add to the firebase referance event listener (add, remove..)
            clientsRef.addChildEventListener(clientsRefChildEventListener);

            // this event listener activated after loadding *all* children from FireBase
            clientsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("We're done loading the initial " + dataSnapshot.getChildrenCount() + " items");
                    notifyEndLoadinData.onEnd(true);
                    stopNotifyToclientList();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }
    }
    /**
     * stop the listener for client list
     */
    public static void stopNotifyToclientList() {
        if (clientsRefChildEventListener != null) {
            clientsRef.removeEventListener(clientsRefChildEventListener);
            clientsRefChildEventListener = null;
        }

    }
    /**
     * add client's from firebase to local client's list ,ValueEvent listener: return true when finish loading all client's from Firebase
     * @param notifyDataChange Helps to alert about data changes on client's in Firebase
     */
    public static void notifyToclientsList2(final Utils.NotifyDataChange<List<Client>> notifyDataChange, int a) {
        if (notifyDataChange != null) {
            if (clientsRefChildEventListener2 != null) {
                notifyDataChange.onFailure(new Exception("first unNotify client list"));
                return;
            }
            // clientsList.clear();
            clientsRefChildEventListener2 = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    // Log.d(TAG, " onNotifyToclientsList ...");
                    Client client = dataSnapshot.getValue(Client.class);
                    String id = dataSnapshot.getKey();
                    client.setId(Long.parseLong(id));
                    clientsList.add(0, client);
                    Log.d(TAG, " List Size :===" + clientsList.size() + "my name" + client.getId());
                    notifyDataChange.OnDataChanged(clientsList);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Client client = dataSnapshot.getValue(Client.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    client.setId(id);
                    for (int i = 0; i < clientsList.size(); i++) {
                        if (clientsList.get(i).getId().equals(id)) {
                            clientsList.set(i, client);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(clientsList);
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Client client = dataSnapshot.getValue(Client.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    client.setId(id);
                    for (int i = 0; i < clientsList.size(); i++) {
                        if (clientsList.get(i).getId() == id) {
                            clientsList.remove(i);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(clientsList);
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };

//           startAt- listener to the FireBace that checked if add new child to the clinet(in the fire base)
            starTime = Calendar.getInstance().getTime().getTime();
            clientsRef.orderByChild("time0").startAt(starTime).addChildEventListener(clientsRefChildEventListener2);

        }
    }
    /**
     /**
     * stop the listener for client list
     */
    public static void stopNotifyToclientList2() {
        if (clientsRefChildEventListener2 != null) {
            clientsRef.removeEventListener(clientsRefChildEventListener2);
            clientsRefChildEventListener2 = null;


        }

    }

    /**
     *  get driver list
     * @return driver list
     */
    @Override
    public List<Driver> getDriverList() {

        return driversList;
    }
    /**
     *  get client list
     * @return client list
     */
    @Override
    public List<Client> getClientList() {

        return clientsList;
    }

    /**
     * set client equest status to ONWORK
     * @param positiOnClickList position of client in clientlist
     * @param driverKey driver firebase key
     * @param driverid driver id
     */
    @Override
    public void markClientRequestStatusONWORK(int positiOnClickList, String driverKey, Long driverid) {


        Client client = clientsList.get(positiOnClickList);
        client.setStatus(ClientRequestStatus.ONWORK);
        client.setDriverId(driverid);
        // client.setDriverId(driverKey);
        String key = client.getId().toString();
        Task<Void> upload = clientsRef.child(key).setValue(client);
        //clientsRef.child(key).setValue(locationA);
        upload.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // action.onProgress("upload student data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // action.onProgress("error upload student data", 100);
            }
        });

    }

    /**
     * set client equest status to FINISH
     * @param positionclickLst position of client in clientlist
     * @param driverKey driver firebase key
     * @param firebaseDriverKey driver id
     */
    @Override
    public void markClientRequestStatusFINISH(int positionclickLst, String driverKey, Long firebaseDriverKey) {

        Client client = clientsList.get(positionclickLst);
        client.setStatus(ClientRequestStatus.FINISH);

//       client.setDriverId(0);
        String key = client.getId().toString();
        Task<Void> upload = clientsRef.child(key).setValue(client);
        //clientsRef.child(key).setValue(locationA);

        upload.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // action.onProgress("upload student data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // action.onProgress("error upload student data", 100);
            }
        });

    }

    /**
     * return star Time
     * @return start time
     */
    public static long getStarTime() {
        return starTime;
    }

    /**
     * set star Time
     * @param starTime start time
     */
    public static void setStarTime(long starTime) {
        Firebase_DBManager.starTime = starTime;
    }


}


