package com.pax.simpleclock;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by zhanzc on 2018/3/1.
 */

public class ClockApplication extends Application {
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("clock.realm")            //realm file name, file is in /data/data/package-name/files/
                .encryptionKey(new byte[64])    //base64 secret key
                .schemaVersion(1)               //realm version
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        mContext = this;
    }

    public Context getAppliationContext() {
        return mContext;
    }
}
