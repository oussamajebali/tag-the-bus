package com.aquafadas.tagthebus;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.aquafadas.tagthebus.station.fragment.StationDetailFragment;
import com.aquafadas.tagthebus.station.fragment.StationDetailFragment.OnStationDetailFragmentInteractionListener;
import com.aquafadas.tagthebus.station.fragment.StationFragment;
import com.aquafadas.tagthebus.station.fragment.StationFragment.OnStationFragmentInteractionListener;
import com.aquafadas.tagthebus.utils.ConnectivityReceiver;
import com.aquafadas.tagthebus.utils.ConnectivityReceiver.ConnectivityReceiverListener;

public class MainActivity extends AppCompatActivity implements OnStationFragmentInteractionListener, OnStationDetailFragmentInteractionListener, ConnectivityReceiverListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!checkLocationPermission()) {
            requestLocationPermission();
        } else {
            replaceFragment(new StationFragment(), R.id.frame_home, false);
        }

    }


    public void replaceFragment(Fragment fragment, int frameLayout, boolean withBackStack) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (withBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(frameLayout, fragment);
        fragmentTransaction.commit();
    }

    public void addFragment(Fragment fragment, int frameLayout, boolean withBackStack) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (withBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.add(frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private boolean checkLocationPermission() {
        int accessCoarseLocation = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int accessFineLocation = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (accessCoarseLocation != PackageManager.PERMISSION_GRANTED || accessFineLocation != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 0X1) {

            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(MainActivity.this, "Permission Granted, Now you can access location data.", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(MainActivity.this, "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();
            }

        }
    }


    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 0X1);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        TagTheBusApplication.getInstance().setConnectivityListener(this);
        if (ConnectivityReceiver.isConnected()) {

        } else {

        }


    }

    @Override
    public void openDetailFragment(Bundle bundle) {
        StationDetailFragment fragment = new StationDetailFragment();
        fragment.setArguments(bundle);
        addFragment(fragment, R.id.frame_home, true);

    }
}
