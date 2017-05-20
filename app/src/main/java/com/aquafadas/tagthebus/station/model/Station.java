package com.aquafadas.tagthebus.station.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Oussama on 20/05/2017.
 */

public class Station implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("street_name")
    @Expose
    private String street_name;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("utm_x")
    @Expose
    private String utm_x;

    @SerializedName("utm_y")
    @Expose
    private String utm_y;

    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("lon")
    @Expose
    private double lon;

    @SerializedName("furniture")
    @Expose
    private String furniture;

    @SerializedName("buses")
    @Expose
    private String buses;

    @SerializedName("distance")
    @Expose
    private String distance;


    public Station() {
    }

    public Station(int id, String street_name, String city, String utm_x, String utm_y, double lat, double lon, String furniture, String buses, String distance) {
        this.id = id;
        this.street_name = street_name;
        this.city = city;
        this.utm_x = utm_x;
        this.utm_y = utm_y;
        this.lat = lat;
        this.lon = lon;
        this.furniture = furniture;
        this.buses = buses;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUtm_x() {
        return utm_x;
    }

    public void setUtm_x(String utm_x) {
        this.utm_x = utm_x;
    }

    public String getUtm_y() {
        return utm_y;
    }

    public void setUtm_y(String utm_y) {
        this.utm_y = utm_y;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getFurniture() {
        return furniture;
    }

    public void setFurniture(String furniture) {
        this.furniture = furniture;
    }

    public String getBuses() {
        return buses;
    }

    public void setBuses(String buses) {
        this.buses = buses;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id='" + id + '\'' +
                ", street_name='" + street_name + '\'' +
                ", city='" + city + '\'' +
                ", utm_x='" + utm_x + '\'' +
                ", utm_y='" + utm_y + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", furniture='" + furniture + '\'' +
                ", buses='" + buses + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }
}
