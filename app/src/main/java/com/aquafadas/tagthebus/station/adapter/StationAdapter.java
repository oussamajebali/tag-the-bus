package com.aquafadas.tagthebus.station.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aquafadas.tagthebus.R;
import com.aquafadas.tagthebus.station.model.Station;

import java.util.ArrayList;

/**
 * Created by Oussama on 20/05/2017.
 */

public class StationAdapter extends RecyclerView.Adapter<StationHolder> {

    private Context context;
    private ArrayList<Station> stations = new ArrayList<>();


    public StationAdapter(Context context, ArrayList<Station> stations) {
        super();
        this.context = context;
        this.stations = stations;
    }


    @Override
    public StationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.station_item, parent, false);
        StationHolder stationHolder = new StationHolder(view);
        return stationHolder;
    }

    @Override
    public void onBindViewHolder(StationHolder holder, int position) {

        final Station station = stations.get(position);
        holder.streetName.setText(station.getStreet_name());
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
}
