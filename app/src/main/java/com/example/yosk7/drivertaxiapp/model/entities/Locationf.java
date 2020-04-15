package com.example.yosk7.drivertaxiapp.model.entities;

import android.location.Location;

public class Locationf extends Location {

    public static Location l = new Location("A");//= new Location(from);

    public Locationf() {
        super(l);

    }


    public Locationf(Location ll) {
        super(ll);
        //this.set(ll);
    }

    ;

}
