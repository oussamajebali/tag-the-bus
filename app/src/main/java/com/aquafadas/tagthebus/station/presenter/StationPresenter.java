package com.aquafadas.tagthebus.station.presenter;

import android.util.Log;

import com.aquafadas.tagthebus.station.model.Station;
import com.aquafadas.tagthebus.station.view.StationView.RemoteView;
import com.aquafadas.tagthebus.station.view.StationView.StationAction;
import com.aquafadas.tagthebus.webservices.ApiClient;
import com.aquafadas.tagthebus.webservices.ApiInterface;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aquafadas.tagthebus.utils.AppStaticValues.BUSES;
import static com.aquafadas.tagthebus.utils.AppStaticValues.CITY;
import static com.aquafadas.tagthebus.utils.AppStaticValues.DISTANCE;
import static com.aquafadas.tagthebus.utils.AppStaticValues.FURNITURE;
import static com.aquafadas.tagthebus.utils.AppStaticValues.ID;
import static com.aquafadas.tagthebus.utils.AppStaticValues.LAT;
import static com.aquafadas.tagthebus.utils.AppStaticValues.LON;
import static com.aquafadas.tagthebus.utils.AppStaticValues.NEARSTATIONS;
import static com.aquafadas.tagthebus.utils.AppStaticValues.STREET_NAME;
import static com.aquafadas.tagthebus.utils.AppStaticValues.UTM_X;
import static com.aquafadas.tagthebus.utils.AppStaticValues.UTM_Y;

/**
 * Created by Oussama on 20/05/2017.
 */

public class StationPresenter implements StationAction {

    private RemoteView remoteView;
    private ApiInterface apiService;
    private ArrayList<Station> stations = new ArrayList<>();

    public StationPresenter(RemoteView remoteView) {
        this.remoteView = remoteView;
        apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    @Override
    public void getStationsList() {
        remoteView.showProgress();
        apiService.getAllStations().enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonObject jsonObject = response.body().getAsJsonObject().getAsJsonObject("data");
                for (int i = 0; i < jsonObject.get(NEARSTATIONS).getAsJsonArray().size(); i++) {
                    Station station = new Station();
                    station.setId(jsonObject.get(NEARSTATIONS).getAsJsonArray().get(i).getAsJsonObject().get(ID).getAsInt());
                    station.setStreet_name(jsonObject.get(NEARSTATIONS).getAsJsonArray().get(i).getAsJsonObject().get(STREET_NAME).getAsString());
                    station.setCity(jsonObject.get(NEARSTATIONS).getAsJsonArray().get(i).getAsJsonObject().get(CITY).getAsString());
                    station.setUtm_x(jsonObject.get(NEARSTATIONS).getAsJsonArray().get(i).getAsJsonObject().get(UTM_X).getAsString());
                    station.setUtm_y(jsonObject.get(NEARSTATIONS).getAsJsonArray().get(i).getAsJsonObject().get(UTM_Y).getAsString());
                    station.setLat(jsonObject.get(NEARSTATIONS).getAsJsonArray().get(i).getAsJsonObject().get(LAT).getAsDouble());
                    station.setLon(jsonObject.get(NEARSTATIONS).getAsJsonArray().get(i).getAsJsonObject().get(LON).getAsDouble());
                    station.setFurniture(jsonObject.get(NEARSTATIONS).getAsJsonArray().get(i).getAsJsonObject().get(FURNITURE).getAsString());
                    station.setBuses(jsonObject.get(NEARSTATIONS).getAsJsonArray().get(i).getAsJsonObject().get(BUSES).getAsString());
                    station.setDistance(jsonObject.get(NEARSTATIONS).getAsJsonArray().get(i).getAsJsonObject().get(DISTANCE).getAsString());

                    stations.add(station);
                }

                remoteView.getStationSuccess(stations);
                remoteView.hideProgress();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable throwable) {
                Log.e(">>>>>>", throwable.toString());
                remoteView.hideProgress();
            }
        });
    }
}
