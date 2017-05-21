package com.aquafadas.tagthebus.station.manager;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.aquafadas.tagthebus.station.model.Picture;
import com.aquafadas.tagthebus.station.model.Station;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Oussama on 21/05/2017.
 */

public class RealmManager {

    private static RealmManager instance;
    private final Realm realm;

    public RealmManager(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmManager with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmManager(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmManager with(Activity activity) {

        if (instance == null) {
            instance = new RealmManager(activity.getApplication());
        }
        return instance;
    }

    public static RealmManager with(Application application) {

        if (instance == null) {
            instance = new RealmManager(application);
        }
        return instance;
    }

    public static RealmManager getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Station.class
    public void clearAll() {

        realm.beginTransaction();
        realm.clear(Station.class);
        realm.commitTransaction();
    }

    //find all objects in the Station.class
    public ArrayList<Station> getStations() {

        return new ArrayList<>(realm.where(Station.class).findAll());
    }


    public ArrayList<Picture> getPictures(int id) {
        return new ArrayList<>(realm.where(Station.class).equalTo("id", id).findFirst().getPictures());
    }

    //query a single Station with the given id
    public Station getStation(int id) {
        return realm.where(Station.class).equalTo("id", id).findFirst();
    }
}
