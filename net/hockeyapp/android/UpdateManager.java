package net.hockeyapp.android;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask.Status;
import android.os.Build.VERSION;
import java.lang.ref.WeakReference;
import java.util.Date;
import net.hockeyapp.android.tasks.CheckUpdateTask;

public class UpdateManager {
    private static UpdateManagerListener lastListener = null;
    private static CheckUpdateTask updateTask = null;

    public static void register(Activity activity, String appIdentifier) {
        register(activity, appIdentifier, null);
    }

    public static void register(Activity activity, String appIdentifier, UpdateManagerListener listener) {
        register(activity, Constants.BASE_URL, appIdentifier, listener);
    }

    public static void register(Activity activity, String urlString, String appIdentifier, UpdateManagerListener listener) {
        lastListener = listener;
        WeakReference<Activity> weakActivity = new WeakReference(activity);
        if ((!fragmentsSupported().booleanValue() || !dialogShown(weakActivity)) && !checkExpiryDate(weakActivity, listener) && !installedFromMarket(weakActivity)) {
            startUpdateTask(weakActivity, urlString, appIdentifier, listener);
        }
    }

    public static void unregister() {
        if (updateTask != null) {
            updateTask.cancel(true);
            updateTask.detach();
            updateTask = null;
        }
        lastListener = null;
    }

    private static boolean checkExpiryDate(WeakReference<Activity> weakActivity, UpdateManagerListener listener) {
        boolean result = false;
        boolean handle = false;
        if (listener != null) {
            Date expiryDate = listener.getExpiryDate();
            result = expiryDate != null && new Date().compareTo(expiryDate) > 0;
            if (result) {
                handle = listener.onBuildExpired();
            }
        }
        if (result && handle) {
            startExpiryInfoIntent(weakActivity);
        }
        return result;
    }

    private static boolean installedFromMarket(WeakReference<Activity> weakActivity) {
        Activity activity = (Activity) weakActivity.get();
        if (activity == null) {
            return false;
        }
        try {
            String installer = activity.getPackageManager().getInstallerPackageName(activity.getPackageName());
            if (VERSION.SDK_INT < 9) {
                boolean result = installer != null && installer.length() > 0;
                return result;
            } else if (installer == null || installer.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } catch (Throwable th) {
            return false;
        }
    }

    private static void startExpiryInfoIntent(WeakReference<Activity> weakActivity) {
        if (weakActivity != null) {
            Activity activity = (Activity) weakActivity.get();
            if (activity != null) {
                activity.finish();
                Intent intent = new Intent(activity, ExpiryInfoActivity.class);
                intent.addFlags(335544320);
                activity.startActivity(intent);
            }
        }
    }

    private static void startUpdateTask(WeakReference<Activity> weakActivity, String urlString, String appIdentifier, UpdateManagerListener listener) {
        if (updateTask == null || updateTask.getStatus() == Status.FINISHED) {
            updateTask = new CheckUpdateTask(weakActivity, urlString, appIdentifier, listener);
            updateTask.execute(new String[0]);
            return;
        }
        updateTask.attach(weakActivity);
    }

    @TargetApi(11)
    private static boolean dialogShown(WeakReference<Activity> weakActivity) {
        if (weakActivity == null) {
            return false;
        }
        Activity activity = (Activity) weakActivity.get();
        if (activity == null || activity.getFragmentManager().findFragmentByTag("hockey_update_dialog") == null) {
            return false;
        }
        return true;
    }

    @SuppressLint({"NewApi"})
    public static Boolean fragmentsSupported() {
        try {
            boolean z;
            if (VERSION.SDK_INT < 11 || Fragment.class == null) {
                z = false;
            } else {
                z = true;
            }
            return Boolean.valueOf(z);
        } catch (NoClassDefFoundError e) {
            return Boolean.valueOf(false);
        }
    }

    public static Boolean runsOnTablet(WeakReference<Activity> weakActivity) {
        boolean z = false;
        if (weakActivity != null) {
            Activity activity = (Activity) weakActivity.get();
            if (activity != null) {
                Configuration configuration = activity.getResources().getConfiguration();
                if ((configuration.screenLayout & 15) == 3 || (configuration.screenLayout & 15) == 4) {
                    z = true;
                }
                return Boolean.valueOf(z);
            }
        }
        return Boolean.valueOf(false);
    }

    public static UpdateManagerListener getLastListener() {
        return lastListener;
    }
}
