package com.aquafadas.tagthebus.station.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aquafadas.tagthebus.R;
import com.aquafadas.tagthebus.station.adapter.StationAdapter;
import com.aquafadas.tagthebus.station.manager.SharedManager;
import com.aquafadas.tagthebus.station.model.Station;
import com.aquafadas.tagthebus.station.presenter.StationPresenter;
import com.aquafadas.tagthebus.station.view.StationView.RemoteView;
import com.aquafadas.tagthebus.station.view.StationView.StationAction;
import com.aquafadas.tagthebus.utils.RecyclerItemClickListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aquafadas.tagthebus.utils.AppStaticValues.REQUEST_CHECK_SETTINGS;
import static com.aquafadas.tagthebus.utils.AppStaticValues.STATION;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StationFragment.OnStationFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class StationFragment extends Fragment implements RemoteView, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult>, OnMapReadyCallback {

    @BindView(R.id.station_recycler)
    RecyclerView stationRecycler;

    @BindView(R.id.list_button)
    Button listButton;

    @BindView(R.id.map_button)
    Button mapButton;

    @BindView(R.id.map_layout)
    RelativeLayout mapLayout;

    @BindView(R.id.refresh_station)
    ImageView refreshStation;


    private OnStationFragmentInteractionListener mListener;
    private StationAction stationAction;

    private StationAdapter stationAdapter;
    private ProgressDialog progressDialog;

    GoogleMap googleMap;
    LatLngBounds.Builder builder;
    LatLngBounds bounds;
    private List<MarkerOptions> markerOptionsList = new ArrayList<>();
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    private ArrayList<Station> stations = new ArrayList<>();

    public StationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stationAction = new StationPresenter(this, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station, container, false);
        ButterKnife.bind(this, view);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
        stationAction.getStationsList();

        return view;
    }

    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }

    /*
     * building the GoogleApiClient
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    /*
     * creating a Location Request (interval, priority)
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(50000);
        mLocationRequest.setMaxWaitTime(15000);
        mLocationRequest.setFastestInterval(50000 / 2);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /*
     * location settings dialog builder
     */
    protected void buildLocationSettingsRequest() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        mLocationSettingsRequest = builder.build();
    }

    private void populateMap(ArrayList<Station> stations) {


        googleMap.clear();
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });


        for (Station station : stations) {

            MarkerOptions markerOption = new MarkerOptions()
                    .title(station.getStreet_name())
                    .snippet(station.getBuses())
                    .position(new LatLng(station.getLat(), station.getLon()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_marker));

            markerOptionsList.add(markerOption);
            googleMap.addMarker(markerOption);


        }


        if (!markerOptionsList.isEmpty()) {
            applyCameraUpdate(markerOptionsList);
        }


    }

    private void applyCameraUpdate(List<MarkerOptions> markerOptionsList) {

        builder = new LatLngBounds.Builder();
        LatLng latLng;
        for (int i = 0; i < markerOptionsList.size(); i++) {
            latLng = new LatLng(markerOptionsList.get(i).getPosition().latitude, markerOptionsList.get(i).getPosition().longitude);
            builder.include(latLng);
        }
        bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStationFragmentInteractionListener) {
            mListener = (OnStationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStationFragmentInteractionListener");
        }
    }


    @OnClick(R.id.list_button)
    public void showList() {
        stationRecycler.setVisibility(View.VISIBLE);
        mapLayout.setVisibility(View.GONE);
        listButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        listButton.setTextColor(getResources().getColor(R.color.white));
        mapButton.setBackgroundColor(getResources().getColor(R.color.white));
        mapButton.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @OnClick(R.id.map_button)
    public void showMap() {
        stationRecycler.setVisibility(View.GONE);
        mapLayout.setVisibility(View.VISIBLE);
        mapButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mapButton.setTextColor(getResources().getColor(R.color.white));
        listButton.setBackgroundColor(getResources().getColor(R.color.white));
        listButton.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @OnClick(R.id.refresh_station)
    public void refreshStationList() {
        SharedManager.with(getActivity()).setPreLoad(false);
        stationAction.getStationsList();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void initStationRecycler(ArrayList<Station> stations) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stationRecycler.setHasFixedSize(true);
        stationRecycler.setLayoutManager(linearLayoutManager);
        stationAdapter = new StationAdapter(getActivity(), stations);
        stationRecycler.setAdapter(stationAdapter);
        stationAdapter.notifyDataSetChanged();

    }

    private void goToDetailFromList(final ArrayList<Station> stations) {
        stationRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(STATION, stations.get(position).getId());
                        if (mListener != null)
                            mListener.openDetailFragment(bundle);
                    }
                }));
    }

    private void goToDetailFromMap(final ArrayList<Station> stations) {
        if (googleMap != null) {
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    for (Station station : stations) {
                        if (String.valueOf(station.getStreet_name()).equals(marker.getTitle())) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(STATION, station.getId());
                            if (mListener != null)
                                mListener.openDetailFragment(bundle);
                        }
                    }


                }
            });
        }
    }

    @Override
    public void getStationSuccess(ArrayList<Station> stations) {
        if (!stations.isEmpty()) {
            this.stations = stations;
            initStationRecycler(stations);
            goToDetailFromList(stations);
        } else
            Toast.makeText(getActivity(), "Temporary server error. Please try again later", Toast.LENGTH_LONG).show();

    }

    @Override
    public void getStationError() {
        Toast.makeText(getActivity(), "Temporary server error. Please try again later", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i("GPS", "All location settings are satisfied.");
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i("GPS", "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");
                //Utils.showGpsStatusAlert(MapActivity.this);
                try {

                    status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i("MapActivity", "PendingIntent unable to execute request.");
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i("GPS", "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                //Utils.showGpsStatusAlert(MapActivity.this);
                try {

                    status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i("MapActivity", "PendingIntent unable to execute request.");
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        checkLocationSettings();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        //googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if (!stations.isEmpty()) {
            populateMap(stations);
            goToDetailFromMap(stations);
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDestroyView() {
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.remove(mapFragment);
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLocationSettings();
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
    public interface OnStationFragmentInteractionListener {
        void openDetailFragment(Bundle bundle);
    }
}
