package com.example.yosk7.drivertaxiapp.model.backend;

import android.content.ContentValues;

import com.example.yosk7.drivertaxiapp.model.entities.Driver;

public class Const {


    public static class ClientConst {
        public static final String NAME = "name";
        public static final String ID = "id";
        public static final String PHONE = "phone";
        public static final String EMAIL = "eMail";
    }


    public static ContentValues CourseToContentValues(Driver driver) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(ClientConst.NAME, driver.getName());
        contentValues.put(ClientConst.ID, driver.getId());
        contentValues.put(ClientConst.PHONE, driver.getPhone());
        contentValues.put(ClientConst.EMAIL, driver.geteMail());


        return contentValues;
    }


    public static Driver ContentValuesToCourse(ContentValues contentValues) {

        Driver driver = new Driver();

        driver.setName(contentValues.getAsString(ClientConst.NAME));
        driver.setId(contentValues.getAsLong(ClientConst.ID));
        driver.setPhone(contentValues.getAsString(ClientConst.PHONE));
        driver.seteMail(contentValues.getAsString(ClientConst.EMAIL));

        return driver;

    }

}
