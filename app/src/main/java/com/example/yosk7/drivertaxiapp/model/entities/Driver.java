package com.example.yosk7.drivertaxiapp.model.entities;

public class Driver

{
    public Driver() {

    }
    private Long id;
    private String name;
    private String phone;
    private String eMail;
    private String password;



    private String pushFireBaseKey;


    private Locationf startPoint;//
    private Locationf destinationPoint;//location
    private Timestampf tstamp;
    private Long time0;


    public String getPushFireBaseKey() {
        return pushFireBaseKey;
    }

    public void setPushFireBaseKey(String pushFireBaseKey) {
        this.pushFireBaseKey = pushFireBaseKey;
    }

    public Locationf getDestinationPoint() {
        return destinationPoint;
    }

    public void setDestinationPoint(Locationf destinationPoint) {
        this.destinationPoint = destinationPoint;
    }







    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Locationf getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Locationf startPoint) {
        this.startPoint = startPoint;
    }

    public Timestampf getTstamp() {
        return tstamp;
    }

    public void setTstamp(Timestampf tstamp) {
        this.tstamp = tstamp;
    }

    public Long getTime0() {
        return time0;
    }

    public void setTime0(Long time0) {
        this.time0 = time0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getePassword() {
        return password;
    }

    public void setePassword(String password) {
        this.password = password;
    }


}
