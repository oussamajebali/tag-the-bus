package com.aquafadas.tagthebus.station.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aquafadas.tagthebus.R;
import com.aquafadas.tagthebus.station.model.Picture;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Oussama on 21/05/2017.
 */

public class PictureAdapter extends RecyclerView.Adapter<PictureHolder> {

    private Context context;
    private ArrayList<Picture> pictures = new ArrayList<>();


    public PictureAdapter(Context context, ArrayList<Picture> pictures) {
        super();
        this.context = context;
        this.pictures = pictures;
    }


    @Override
    public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_item, parent, false);
        return new PictureHolder(view);
    }

    @Override
    public void onBindViewHolder(PictureHolder holder, int position) {
        final Picture picture = pictures.get(position);
        holder.pictureName.setText(picture.getTitle());
        holder.pictureDate.setText(picture.getDateTime());
        if (picture.getPath() != null) {
            File file = new File(picture.getPath());
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                holder.picture.setImageBitmap(bitmap);
            }
        } else {
            holder.picture.setImageResource(R.drawable.background);
        }
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
}
