package com.aquafadas.tagthebus.station.view;

import com.aquafadas.tagthebus.station.model.Station;

import java.util.ArrayList;

/**
 * Created by Oussama on 20/05/2017.
 */

public class StationView {

    public interface StationAction {

        void getStationsList();
    }

    public interface RemoteView {

        void hideProgress();

        void showProgress();

        void getStationSuccess(ArrayList<Station> stations);

        void getStationError();

    }
}
