package com.example.yosk7.drivertaxiapp.model.backend;

import com.example.yosk7.drivertaxiapp.model.datasource.Firebase_DBManager;


public class BackendFactory {

    static Backend DB = null;


    public static Backend getDB() {

//        return new List_DBManager();
        if (DB == null)
            DB= new Firebase_DBManager();
          //DB = new List_DBManager();


        return DB;
    }

    public static int t() {
        return 1;
    }
}

