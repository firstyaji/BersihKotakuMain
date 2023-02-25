package com.bersih.kotaku;

import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;
import static timber.log.Timber.DebugTree;

@HiltAndroidApp
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new DebugTree());
    }
}
