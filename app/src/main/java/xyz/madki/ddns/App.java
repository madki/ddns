package xyz.madki.ddns;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by madki on 23/12/15.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
