package com.example.yosk7.drivertaxiapp.model.backend;

//import com.androidproject.ya.clientapp.model.datasource.Utils;
//import com.androidproject.ya.clientapp.model.entities.Client;
import com.example.yosk7.drivertaxiapp.model.datasource.Utils;
import com.example.yosk7.drivertaxiapp.model.entities.Client;
import com.example.yosk7.drivertaxiapp.model.entities.Driver;


import java.util.List;

public interface Backend {

//    Long addDriver(ContentValues driver, Location a, Location b, Utils.Action<Long> action);

 //   boolean removeDriver(Long id);
 //   boolean updateDriver(Long id, ContentValues values);

    Long addDriver(Driver driver, Utils.Action<Long> action);
    List<Driver> getDriverList();
    List<Client> getClientList();

    void markClientRequestStatusONWORK(int positionclickLst, String driverKey, Long firebaseDriverKey);
    void markClientRequestStatusFINISH(int positionclickLst, String driverKey, Long firebaseDriverKey);



}

