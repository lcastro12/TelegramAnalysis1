package org.telegram.ui;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.widget.ExploreByTouchHelper;
import android.view.ViewConfiguration;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationStatusCodes;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Views.BaseFragment;

public class ApplicationLoader extends Application {
    public static final String EXTRA_MESSAGE = "message";
    public static ApplicationLoader Instance = null;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static Context applicationContext;
    public static Bitmap cachedWallpaper = null;
    public static ArrayList<BaseFragment> fragmentsStack = new ArrayList();
    public static long lastPauseTime;
    private String SENDER_ID = "760348033671";
    private Locale currentLocale;
    private GoogleCloudMessaging gcm;
    private AtomicInteger msgId = new AtomicInteger();
    private String regid;

    class C04401 extends AsyncTask<String, String, Boolean> {
        C04401() {
        }

        protected Boolean doInBackground(String... objects) {
            if (ApplicationLoader.this.gcm == null) {
                ApplicationLoader.this.gcm = GoogleCloudMessaging.getInstance(ApplicationLoader.applicationContext);
            }
            int count = 0;
            while (count < LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) {
                count++;
                try {
                    ApplicationLoader.this.regid = ApplicationLoader.this.gcm.register(ApplicationLoader.this.SENDER_ID);
                    ApplicationLoader.this.sendRegistrationIdToBackend(true);
                    ApplicationLoader.this.storeRegistrationId(ApplicationLoader.applicationContext, ApplicationLoader.this.regid);
                    return Boolean.valueOf(true);
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                    try {
                        if (count % 20 == 0) {
                            Thread.sleep(1800000);
                        } else {
                            Thread.sleep(5000);
                        }
                    } catch (Exception e2) {
                        FileLog.m799e("tmessages", e2);
                    }
                }
            }
            return Boolean.valueOf(false);
        }
    }

    public void onCreate() {
        super.onCreate();
        this.currentLocale = Locale.getDefault();
        Instance = this;
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.net.preferIPv6Addresses", "false");
        applicationContext = getApplicationContext();
        Utilities.getTypeface("fonts/rmedium.ttf");
        UserConfig.loadConfig();
        if (UserConfig.currentUser != null) {
            SharedPreferences preferences = getSharedPreferences("Notifications", 0);
            if (preferences.getInt("v", 0) != 1) {
                Editor editor = applicationContext.getSharedPreferences("mainconfig", 0).edit();
                if (preferences.contains("view_animations")) {
                    editor.putBoolean("view_animations", preferences.getBoolean("view_animations", false));
                }
                if (preferences.contains("selectedBackground")) {
                    editor.putInt("selectedBackground", preferences.getInt("selectedBackground", 1000001));
                }
                if (preferences.contains("selectedColor")) {
                    editor.putInt("selectedColor", preferences.getInt("selectedColor", 0));
                }
                if (preferences.contains("fons_size")) {
                    editor.putInt("fons_size", preferences.getInt("fons_size", 16));
                }
                editor.commit();
                editor = preferences.edit();
                editor.putInt("v", 1);
                editor.remove("view_animations");
                editor.remove("selectedBackground");
                editor.remove("selectedColor");
                editor.remove("fons_size");
                editor.commit();
            }
            MessagesStorage init = MessagesStorage.Instance;
            MessagesController.Instance.users.put(Integer.valueOf(UserConfig.clientUserId), UserConfig.currentUser);
        }
        MessagesController.Instance.checkAppAccount();
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
        if (checkPlayServices()) {
            this.gcm = GoogleCloudMessaging.getInstance(this);
            this.regid = getRegistrationId(applicationContext);
            if (this.regid.length() == 0) {
                registerInBackground();
            } else {
                sendRegistrationIdToBackend(false);
            }
        } else {
            FileLog.m798d("tmessages", "No valid Google Play Services APK found.");
        }
        PhoneFormat format = PhoneFormat.Instance;
        lastPauseTime = System.currentTimeMillis();
        FileLog.m800e("tmessages", "start application with time " + lastPauseTime);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Locale newLocale = newConfig.locale;
        if (newLocale != null) {
            String d1 = newLocale.getDisplayName();
            String d2 = this.currentLocale.getDisplayName();
            if (!(d1 == null || d2 == null || d1.equals(d2))) {
                Utilities.recreateFormatters();
            }
            this.currentLocale = newLocale;
        }
    }

    public static void resetLastPauseTime() {
        lastPauseTime = 0;
        ConnectionsManager.Instance.applicationMovedToForeground();
    }

    private boolean checkPlayServices() {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == 0;
    }

    private String getRegistrationId(Context context) {
        SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, BuildConfig.FLAVOR);
        if (registrationId.length() == 0) {
            FileLog.m798d("tmessages", "Registration not found.");
            return BuildConfig.FLAVOR;
        } else if (prefs.getInt(PROPERTY_APP_VERSION, ExploreByTouchHelper.INVALID_ID) == getAppVersion()) {
            return registrationId;
        } else {
            FileLog.m798d("tmessages", "App version changed.");
            return BuildConfig.FLAVOR;
        }
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(ApplicationLoader.class.getSimpleName(), 0);
    }

    public static int getAppVersion() {
        try {
            return applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground() {
        AsyncTask<String, String, Boolean> task = new C04401().execute(new String[]{null, null, null});
    }

    private void sendRegistrationIdToBackend(final boolean isNew) {
        Utilities.stageQueue.postRunnable(new Runnable() {

            class C04411 implements Runnable {
                C04411() {
                }

                public void run() {
                    MessagesController.Instance.registerForPush(ApplicationLoader.this.regid);
                }
            }

            public void run() {
                UserConfig.pushString = ApplicationLoader.this.regid;
                UserConfig.registeredForPush = !isNew;
                UserConfig.saveConfig(false);
                Utilities.RunOnUIThread(new C04411());
            }
        });
    }

    private void storeRegistrationId(Context context, String regId) {
        SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion();
        FileLog.m800e("tmessages", "Saving regId on app version " + appVersion);
        Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
}
