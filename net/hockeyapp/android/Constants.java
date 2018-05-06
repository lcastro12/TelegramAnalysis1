package net.hockeyapp.android;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import java.io.File;
import org.telegram.messenger.BuildConfig;

public class Constants {
    public static String ANDROID_VERSION = null;
    public static String APP_PACKAGE = null;
    public static String APP_VERSION = null;
    public static String APP_VERSION_NAME = null;
    public static final String BASE_URL = "https://sdk.hockeyapp.net/";
    public static String FILES_PATH = null;
    public static String PHONE_MANUFACTURER = null;
    public static String PHONE_MODEL = null;
    public static final String SDK_NAME = "HockeySDK";
    public static final String SDK_VERSION = "3.0.1";
    public static final String TAG = "HockeyApp";

    public static void loadFromContext(Context context) {
        ANDROID_VERSION = VERSION.RELEASE;
        PHONE_MODEL = Build.MODEL;
        PHONE_MANUFACTURER = Build.MANUFACTURER;
        loadFilesPath(context);
        loadPackageData(context);
    }

    private static void loadFilesPath(Context context) {
        if (context != null) {
            try {
                File file = context.getFilesDir();
                if (file != null) {
                    FILES_PATH = file.getAbsolutePath();
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception thrown when accessing the files dir:");
                e.printStackTrace();
            }
        }
    }

    private static void loadPackageData(Context context) {
        if (context != null) {
            try {
                PackageManager packageManager = context.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                APP_PACKAGE = packageInfo.packageName;
                APP_VERSION = BuildConfig.FLAVOR + packageInfo.versionCode;
                APP_VERSION_NAME = packageInfo.versionName;
                int buildNumber = loadBuildNumber(context, packageManager);
                if (buildNumber != 0 && buildNumber > packageInfo.versionCode) {
                    APP_VERSION = BuildConfig.FLAVOR + buildNumber;
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception thrown when accessing the package info:");
                e.printStackTrace();
            }
        }
    }

    private static int loadBuildNumber(Context context, PackageManager packageManager) {
        int i = 0;
        try {
            Bundle metaData = packageManager.getApplicationInfo(context.getPackageName(), 128).metaData;
            if (metaData != null) {
                i = metaData.getInt("buildNumber", 0);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception thrown when accessing the application info:");
            e.printStackTrace();
        }
        return i;
    }
}
