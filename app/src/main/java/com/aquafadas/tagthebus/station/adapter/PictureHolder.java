package com.aquafadas.tagthebus.station.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aquafadas.tagthebus.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Oussama on 21/05/2017.
 */

public class PictureHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.picture)
    ImageView picture;

    @BindView(R.id.picture_name)
    TextView pictureName;

    @BindView(R.id.picture_date)
    TextView pictureDate;

    public PictureHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
