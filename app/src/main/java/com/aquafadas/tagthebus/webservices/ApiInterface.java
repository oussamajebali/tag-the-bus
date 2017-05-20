package com.aquafadas.tagthebus.webservices;

import com.aquafadas.tagthebus.station.model.Station;
import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Oussama on 20/05/2017.
 */

public interface ApiInterface {

    @GET("1.json")
    Call<JsonElement> getAllStations();
}
