package com.twsela.driver;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.twsela.driver.utils.Utils;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Shamyyoun on 4/23/16.
 */
public class TwselaApp extends Application {
    private String language;

    @Override
    public void onCreate() {
        super.onCreate();

        // init Crashlytics disabled for debugging mode
        Crashlytics crashlytics = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();
        Fabric.with(this, crashlytics, new Crashlytics());

        // change app locale to english to make sure that layouts directions are ltr
        language = "en";
        Utils.changeAppLocale(this, language);
    }

    /**
     * method used to return current application instance
     */
    public static TwselaApp getInstance(Context context) {
        return (TwselaApp) context.getApplicationContext();
    }

    public static String getLanguage(Context context) {
        return getInstance(context).language;
    }
}
