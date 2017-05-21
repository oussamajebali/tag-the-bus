package com.aquafadas.tagthebus.station.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aquafadas.tagthebus.R;
import com.aquafadas.tagthebus.station.adapter.PictureAdapter;
import com.aquafadas.tagthebus.station.adapter.StationAdapter;
import com.aquafadas.tagthebus.station.manager.RealmManager;
import com.aquafadas.tagthebus.station.model.Picture;
import com.aquafadas.tagthebus.station.model.Station;
import com.aquafadas.tagthebus.utils.ImageUtility;
import com.aquafadas.tagthebus.utils.RecyclerItemClickListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static android.app.Activity.RESULT_CANCELED;
import static com.aquafadas.tagthebus.utils.AppStaticValues.CAMERA_PICTURE;
import static com.aquafadas.tagthebus.utils.AppStaticValues.GALLERY_PICTURE;
import static com.aquafadas.tagthebus.utils.AppStaticValues.PATH;
import static com.aquafadas.tagthebus.utils.AppStaticValues.PICTURE;
import static com.aquafadas.tagthebus.utils.AppStaticValues.STATION;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StationDetailFragment.OnStationDetailFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class StationDetailFragment extends Fragment {

    @BindView(R.id.station_title)
    TextView stationTitle;

    @BindView(R.id.back_to_list)
    ImageView backToList;

    @BindView(R.id.add_picture)
    ImageView addPicture;

    @BindView(R.id.picture_recycler)
    RecyclerView pictureRecycler;

    private PictureAdapter pictureAdapter;
    private ArrayList<Picture> pictures = new ArrayList<>();
    private OnStationDetailFragmentInteractionListener mListener;
    private Station station;
    private Realm realm;
    private int stationId = -1;

    public StationDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            stationId = getArguments().getInt(STATION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_station_detail, container, false);
        ButterKnife.bind(this, view);
        realm = RealmManager.with(this).getRealm();
        station = RealmManager.with(this).getStation(stationId);
        if (station != null)
            stationTitle.setText(station.getStreet_name());
        stationTitle.setSelected(true);

        pictures = RealmManager.getInstance().getPictures(stationId);
        initPictureRecycler();
        return view;
    }


    private void initPictureRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        pictureRecycler.setHasFixedSize(true);
        pictureRecycler.setLayoutManager(linearLayoutManager);
        pictureAdapter = new PictureAdapter(getActivity(), pictures);
        pictureRecycler.setAdapter(pictureAdapter);
        pictureAdapter.notifyDataSetChanged();


        pictureRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(PICTURE, pictures.get(position));
                        bundle.putSerializable(STATION, station);
                        if (mListener != null && new File(pictures.get(position).getPath()).exists())
                            mListener.openShareFragment(bundle);
                    }
                }));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStationDetailFragmentInteractionListener) {
            mListener = (OnStationDetailFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStationDetailFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.back_to_list)
    public void backToList() {
        getActivity().onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick(R.id.add_picture)
    public void takePicture() {
        if (!checkCameraPermission()) {
            requestCameraPermission();
        } else {
            getImage();
        }
    }


    private void getImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Take a picture")
                .setCancelable(true)
                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_PICTURE);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
                        galleryIntent.setType("image/*");
                        galleryIntent.putExtra("return-data", true);
                        startActivityForResult(galleryIntent, GALLERY_PICTURE);
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 0X1) {

            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getActivity(), "Permission Granted, Now you can access Camera.", Toast.LENGTH_LONG).show();

            } else {
                requestCameraPermission();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkCameraPermission() {
        int writeStorage = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readStorage = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (writeStorage != PackageManager.PERMISSION_GRANTED || readStorage != PackageManager.PERMISSION_GRANTED || camera != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0X1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {

            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri photoUri = null;

            try {
                photoUri = ImageUtility.savePicture(getActivity(), ImageUtility.modifyOrientation(bitmap, ImageUtility.getRealPathFromURI(getActivity(), selectedImage)));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoUri != null) {
                Bundle bundle = new Bundle();
                bundle.putInt(STATION, stationId);
                bundle.putString(PATH, photoUri.getPath());
                if (mListener != null)
                    mListener.openPictureFragment(bundle);
            }

        }


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStationDetailFragmentInteractionListener {
        void openPictureFragment(Bundle bundle);

        void openShareFragment(Bundle bundle);
    }
}
