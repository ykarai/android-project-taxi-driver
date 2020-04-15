package com.example.yosk7.drivertaxiapp.model.entities;





import android.location.Location;

import java.sql.Time;
import java.sql.Timestamp;

/*
represents client details
 */
public class    Client {
    /*
    client details
     */

    public Client() {

    }
    public Client(String s) {
        this.name=s;
    }
    private ClientRequestStatus status;


    private Long id;
    private String name;
    private String phone;
    private String eMail;

    private Locationf startPoint;//location
    private Locationf destinationPoint;//location

    private Time startTime;
    private Time endTime;

    private Long driverId;

    private Timestampf tstamp;

    private String address;

    private Long time0;


    public String getAddress() {
        return address;
    }

    public void setAddress(String t) {
        this.address = t;
    }



    public Long getTime0() {
        return time0;
    }

    public void setTime0(Long t) {
        this.time0 = t;
    }










    public Timestampf getTstamp() {
        return tstamp;
    }

    public void setTstamp(Timestampf tstamp) {
        this.tstamp = tstamp;
    }


    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }






    /*
    gets and sets
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientRequestStatus getStatus() {
        return status;
    }

    public void setStatus(ClientRequestStatus status) {
        this.status = status;
    }


    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public Location getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Locationf startPoint) {
        this.startPoint = startPoint;
    }

    public Location getDestinationPoint() {
        return destinationPoint;
    }

    public void setDestinationPoint(Locationf destinationPoint) {
        this.destinationPoint = destinationPoint;
    }


}
