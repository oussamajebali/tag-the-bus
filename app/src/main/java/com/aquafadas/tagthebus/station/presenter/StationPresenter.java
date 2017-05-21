package com.aquafadas.tagthebus.station.presenter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.aquafadas.tagthebus.station.fragment.StationFragment;
import com.aquafadas.tagthebus.station.manager.RealmManager;
import com.aquafadas.tagthebus.station.manager.SharedManager;
import com.aquafadas.tagthebus.station.model.Station;
import com.aquafadas.tagthebus.station.view.StationView.RemoteView;
import com.aquafadas.tagthebus.station.view.StationView.StationAction;
import com.aquafadas.tagthebus.webservices.ApiClient;
import com.aquafadas.tagthebus.webservices.ApiInterface;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import io.realm.Realm;
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
    private Realm realm;
    private Context context;

    public StationPresenter(Fragment fragment, RemoteView remoteView) {
        this.remoteView = remoteView;
        apiService = ApiClient.getClient().create(ApiInterface.class);
        realm = RealmManager.with(fragment).getRealm();
        context = fragment.getActivity().getApplicationContext();
    }

    @Override
    public void getStationsList() {

        if (!SharedManager.with(context).getPreLoad()) {
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

                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(station);
                        realm.commitTransaction();
                    }

                    stations = RealmManager.getInstance().getStations();
                    remoteView.getStationSuccess(stations);
                    remoteView.hideProgress();
                    SharedManager.with(context).setPreLoad(true);
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable throwable) {
                    remoteView.hideProgress();
                    remoteView.getStationError();
                }
            });
        } else {
            stations = RealmManager.getInstance().getStations();
            remoteView.getStationSuccess(stations);
        }

    }
}
