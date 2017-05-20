package com.aquafadas.tagthebus.station.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aquafadas.tagthebus.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Oussama on 20/05/2017.
 */

public class StationHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.street_name)
    TextView streetName;


    public StationHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
