package net.hockeyapp.android.tasks;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.plus.PlusShare;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Locale;
import net.hockeyapp.android.Constants;
import net.hockeyapp.android.Strings;
import net.hockeyapp.android.Tracking;
import net.hockeyapp.android.UpdateActivity;
import net.hockeyapp.android.UpdateFragment;
import net.hockeyapp.android.UpdateManager;
import net.hockeyapp.android.UpdateManagerListener;
import net.hockeyapp.android.utils.VersionCache;
import net.hockeyapp.android.utils.VersionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.BuildConfig;

public class CheckUpdateTask extends AsyncTask<String, String, JSONArray> {
    private static final int MAX_NUMBER_OF_VERSIONS = 25;
    private Activity activity;
    protected String appIdentifier;
    private UpdateManagerListener listener;
    private Boolean mandatory;
    protected String urlString;
    private long usageTime;

    class C02821 implements OnClickListener {
        C02821() {
        }

        public void onClick(DialogInterface dialog, int which) {
            CheckUpdateTask.this.cleanUp();
        }
    }

    public CheckUpdateTask(WeakReference<Activity> weakActivity, String urlString) {
        this.urlString = null;
        this.appIdentifier = null;
        this.activity = null;
        this.mandatory = Boolean.valueOf(false);
        this.usageTime = 0;
        this.appIdentifier = null;
        this.urlString = urlString;
        if (weakActivity != null) {
            this.activity = (Activity) weakActivity.get();
        }
        if (this.activity != null) {
            this.usageTime = Tracking.getUsageTime(this.activity);
            Constants.loadFromContext(this.activity);
        }
    }

    public CheckUpdateTask(WeakReference<Activity> weakActivity, String urlString, String appIdentifier) {
        this.urlString = null;
        this.appIdentifier = null;
        this.activity = null;
        this.mandatory = Boolean.valueOf(false);
        this.usageTime = 0;
        this.appIdentifier = appIdentifier;
        this.urlString = urlString;
        if (weakActivity != null) {
            this.activity = (Activity) weakActivity.get();
        }
        if (this.activity != null) {
            this.usageTime = Tracking.getUsageTime(this.activity);
            Constants.loadFromContext(this.activity);
        }
    }

    public CheckUpdateTask(WeakReference<Activity> weakActivity, String urlString, String appIdentifier, UpdateManagerListener listener) {
        this.urlString = null;
        this.appIdentifier = null;
        this.activity = null;
        this.mandatory = Boolean.valueOf(false);
        this.usageTime = 0;
        this.appIdentifier = appIdentifier;
        this.urlString = urlString;
        this.listener = listener;
        if (weakActivity != null) {
            this.activity = (Activity) weakActivity.get();
        }
        if (this.activity != null) {
            this.usageTime = Tracking.getUsageTime(this.activity);
            Constants.loadFromContext(this.activity);
        }
    }

    public void attach(WeakReference<Activity> weakActivity) {
        if (weakActivity != null) {
            this.activity = (Activity) weakActivity.get();
        }
        if (this.activity != null) {
            Constants.loadFromContext(this.activity);
        }
    }

    public void detach() {
        this.activity = null;
    }

    protected int getVersionCode() {
        return Integer.parseInt(Constants.APP_VERSION);
    }

    protected JSONArray doInBackground(String... args) {
        try {
            int versionCode = getVersionCode();
            JSONArray json = new JSONArray(VersionCache.getVersionInfo(this.activity));
            if (getCachingEnabled() && findNewVersion(json, versionCode)) {
                return json;
            }
            URLConnection connection = createConnection(new URL(getURLString("json")));
            connection.connect();
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            String jsonString = convertStreamToString(inputStream);
            inputStream.close();
            json = new JSONArray(jsonString);
            if (findNewVersion(json, versionCode)) {
                return limitResponseSize(json);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected URLConnection createConnection(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.addRequestProperty("User-Agent", "HockeySDK/Android");
        if (VERSION.SDK_INT <= 9) {
            connection.setRequestProperty("connection", "close");
        }
        return connection;
    }

    private boolean findNewVersion(JSONArray json, int versionCode) {
        int index = 0;
        while (index < json.length()) {
            try {
                JSONObject entry = json.getJSONObject(index);
                if (entry.getInt("version") <= versionCode || VersionHelper.compareVersionStrings(entry.getString("minimum_os_version"), VERSION.RELEASE) > 0) {
                    index++;
                } else {
                    if (entry.has("mandatory")) {
                        this.mandatory = Boolean.valueOf(entry.getBoolean("mandatory"));
                    }
                    return true;
                }
            } catch (JSONException e) {
                return false;
            }
        }
        return false;
    }

    private JSONArray limitResponseSize(JSONArray json) {
        JSONArray result = new JSONArray();
        for (int index = 0; index < Math.min(json.length(), MAX_NUMBER_OF_VERSIONS); index++) {
            try {
                result.put(json.get(index));
            } catch (JSONException e) {
            }
        }
        return result;
    }

    protected void onPostExecute(JSONArray updateInfo) {
        if (updateInfo != null) {
            if (this.listener != null) {
                this.listener.onUpdateAvailable();
            }
            showDialog(updateInfo);
        } else if (this.listener != null) {
            this.listener.onNoUpdateAvailable();
        }
    }

    private void cleanUp() {
        this.activity = null;
        this.urlString = null;
        this.appIdentifier = null;
    }

    protected String getURLString(String format) {
        StringBuilder builder = new StringBuilder();
        builder.append(this.urlString);
        builder.append("api/2/apps/");
        builder.append(this.appIdentifier != null ? this.appIdentifier : this.activity.getPackageName());
        builder.append("?format=" + format);
        if (Secure.getString(this.activity.getContentResolver(), "android_id") != null) {
            builder.append("&udid=" + encodeParam(Secure.getString(this.activity.getContentResolver(), "android_id")));
        }
        builder.append("&os=Android");
        builder.append("&os_version=" + encodeParam(Constants.ANDROID_VERSION));
        builder.append("&device=" + encodeParam(Constants.PHONE_MODEL));
        builder.append("&oem=" + encodeParam(Constants.PHONE_MANUFACTURER));
        builder.append("&app_version=" + encodeParam(Constants.APP_VERSION));
        builder.append("&sdk=" + encodeParam(Constants.SDK_NAME));
        builder.append("&sdk_version=" + encodeParam(Constants.SDK_VERSION));
        builder.append("&lang=" + encodeParam(Locale.getDefault().getLanguage()));
        builder.append("&usage_time=" + this.usageTime);
        return builder.toString();
    }

    private String encodeParam(String param) {
        try {
            return URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return BuildConfig.FLAVOR;
        }
    }

    @TargetApi(11)
    private void showDialog(final JSONArray updateInfo) {
        if (getCachingEnabled()) {
            VersionCache.setVersionInfo(this.activity, updateInfo.toString());
        }
        if (this.activity != null && !this.activity.isFinishing()) {
            Builder builder = new Builder(this.activity);
            builder.setTitle(Strings.get(this.listener, 9));
            if (this.mandatory.booleanValue()) {
                Toast.makeText(this.activity, Strings.get(this.listener, 8), 1).show();
                startUpdateIntent(updateInfo, Boolean.valueOf(true));
                return;
            }
            builder.setMessage(Strings.get(this.listener, 10));
            builder.setNegativeButton(Strings.get(this.listener, 11), new C02821());
            builder.setPositiveButton(Strings.get(this.listener, 12), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (CheckUpdateTask.this.getCachingEnabled()) {
                        VersionCache.setVersionInfo(CheckUpdateTask.this.activity, "[]");
                    }
                    WeakReference<Activity> weakActivity = new WeakReference(CheckUpdateTask.this.activity);
                    if (UpdateManager.fragmentsSupported().booleanValue() && UpdateManager.runsOnTablet(weakActivity).booleanValue()) {
                        CheckUpdateTask.this.showUpdateFragment(updateInfo);
                    } else {
                        CheckUpdateTask.this.startUpdateIntent(updateInfo, Boolean.valueOf(false));
                    }
                }
            });
            builder.create().show();
        }
    }

    @TargetApi(11)
    private void startUpdateIntent(JSONArray updateInfo, Boolean finish) {
        Class<?> activityClass = null;
        if (this.listener != null) {
            activityClass = this.listener.getUpdateActivityClass();
        }
        if (activityClass == null) {
            activityClass = UpdateActivity.class;
        }
        if (this.activity != null) {
            Intent intent = new Intent();
            intent.setClass(this.activity, activityClass);
            intent.putExtra("json", updateInfo.toString());
            intent.putExtra(PlusShare.KEY_CALL_TO_ACTION_URL, getURLString("apk"));
            this.activity.startActivity(intent);
            if (finish.booleanValue()) {
                this.activity.finish();
            }
        }
        cleanUp();
    }

    @TargetApi(11)
    private void showUpdateFragment(JSONArray updateInfo) {
        if (this.activity != null) {
            FragmentTransaction fragmentTransaction = this.activity.getFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(4097);
            Fragment existingFragment = this.activity.getFragmentManager().findFragmentByTag("hockey_update_dialog");
            if (existingFragment != null) {
                fragmentTransaction.remove(existingFragment);
            }
            fragmentTransaction.addToBackStack(null);
            Class<? extends UpdateFragment> fragmentClass = UpdateFragment.class;
            if (this.listener != null) {
                fragmentClass = this.listener.getUpdateFragmentClass();
            }
            try {
                ((DialogFragment) fragmentClass.getMethod("newInstance", new Class[]{JSONArray.class, String.class}).invoke(null, new Object[]{updateInfo, getURLString("apk")})).show(fragmentTransaction, "hockey_update_dialog");
            } catch (Exception e) {
                Log.d(Constants.TAG, "An exception happened while showing the update fragment:");
                e.printStackTrace();
                Log.d(Constants.TAG, "Showing update activity instead.");
                startUpdateIntent(updateInfo, Boolean.valueOf(false));
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String convertStreamToString(java.io.InputStream r6) {
        /*
        r2 = new java.io.BufferedReader;
        r4 = new java.io.InputStreamReader;
        r4.<init>(r6);
        r5 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r2.<init>(r4, r5);
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r1 = 0;
    L_0x0012:
        r1 = r2.readLine();	 Catch:{ IOException -> 0x002f }
        if (r1 == 0) goto L_0x003b;
    L_0x0018:
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x002f }
        r4.<init>();	 Catch:{ IOException -> 0x002f }
        r4 = r4.append(r1);	 Catch:{ IOException -> 0x002f }
        r5 = "\n";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x002f }
        r4 = r4.toString();	 Catch:{ IOException -> 0x002f }
        r3.append(r4);	 Catch:{ IOException -> 0x002f }
        goto L_0x0012;
    L_0x002f:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0049 }
        r6.close();	 Catch:{ IOException -> 0x0044 }
    L_0x0036:
        r4 = r3.toString();
        return r4;
    L_0x003b:
        r6.close();	 Catch:{ IOException -> 0x003f }
        goto L_0x0036;
    L_0x003f:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0036;
    L_0x0044:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0036;
    L_0x0049:
        r4 = move-exception;
        r6.close();	 Catch:{ IOException -> 0x004e }
    L_0x004d:
        throw r4;
    L_0x004e:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x004d;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.tasks.CheckUpdateTask.convertStreamToString(java.io.InputStream):java.lang.String");
    }

    protected boolean getCachingEnabled() {
        return true;
    }
}
