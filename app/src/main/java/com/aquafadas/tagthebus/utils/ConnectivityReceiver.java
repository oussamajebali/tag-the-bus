package com.aquafadas.tagthebus.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.aquafadas.tagthebus.TagTheBusApplication;

/**
 * Created by Oussama on 20/05/2017.
 */

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        Boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }

    }

    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) TagTheBusApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}