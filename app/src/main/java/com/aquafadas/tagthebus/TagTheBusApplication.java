package com.aquafadas.tagthebus;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.aquafadas.tagthebus.utils.ConnectivityReceiver;
import com.aquafadas.tagthebus.utils.ConnectivityReceiver.ConnectivityReceiverListener;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Oussama on 20/05/2017.
 */

public class TagTheBusApplication extends MultiDexApplication {

    private static TagTheBusApplication mInstance;
    private static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        context = this.getApplicationContext();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static synchronized TagTheBusApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return context;
    }

    public void setConnectivityListener(ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
