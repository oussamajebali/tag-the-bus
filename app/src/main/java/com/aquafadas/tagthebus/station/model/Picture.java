package com.aquafadas.tagthebus.station.model;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Oussama on 21/05/2017.
 */

public class Picture extends RealmObject implements Serializable {

    private String path;
    private String title;
    private String dateTime;

    public Picture() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
