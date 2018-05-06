package net.hockeyapp.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.plus.PlusShare;
import net.hockeyapp.android.listeners.DownloadFileListener;
import net.hockeyapp.android.objects.ErrorObject;
import net.hockeyapp.android.tasks.DownloadFileTask;
import net.hockeyapp.android.utils.VersionHelper;
import net.hockeyapp.android.views.UpdateView;
import org.telegram.messenger.BuildConfig;

public class UpdateActivity extends Activity implements UpdateActivityInterface, UpdateInfoListener, OnClickListener {
    private final int DIALOG_ERROR_ID = 0;
    private Context context;
    protected DownloadFileTask downloadTask;
    private ErrorObject error;
    protected VersionHelper versionHelper;

    class C02792 implements Runnable {
        C02792() {
        }

        public void run() {
            UpdateActivity.this.showDialog(0);
        }
    }

    class C02803 implements Runnable {
        C02803() {
        }

        public void run() {
            UpdateActivity.this.showDialog(0);
        }
    }

    class C02814 implements DialogInterface.OnClickListener {
        C02814() {
        }

        public void onClick(DialogInterface dialog, int id) {
            UpdateActivity.this.error = null;
            dialog.cancel();
        }
    }

    class C09481 extends DownloadFileListener {
        C09481() {
        }

        public void downloadSuccessful(DownloadFileTask task) {
            UpdateActivity.this.enableUpdateButton();
        }

        public void downloadFailed(DownloadFileTask task, Boolean userWantsRetry) {
            if (userWantsRetry.booleanValue()) {
                UpdateActivity.this.startDownloadTask();
            } else {
                UpdateActivity.this.enableUpdateButton();
            }
        }

        public String getStringForResource(int resourceID) {
            UpdateManagerListener listener = UpdateManager.getLastListener();
            if (listener != null) {
                return listener.getStringForResource(resourceID);
            }
            return null;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("App Update");
        setContentView(getLayoutView());
        this.context = this;
        this.versionHelper = new VersionHelper(getIntent().getStringExtra("json"), this);
        configureView();
        this.downloadTask = (DownloadFileTask) getLastNonConfigurationInstance();
        if (this.downloadTask != null) {
            this.downloadTask.attach(this);
        }
    }

    protected void configureView() {
        ((TextView) findViewById(UpdateView.NAME_LABEL_ID)).setText(getAppName());
        ((TextView) findViewById(4099)).setText("Version " + this.versionHelper.getVersionString() + "\n" + this.versionHelper.getFileInfoString());
        ((Button) findViewById(UpdateView.UPDATE_BUTTON_ID)).setOnClickListener(this);
        WebView webView = (WebView) findViewById(UpdateView.WEB_VIEW_ID);
        webView.clearCache(true);
        webView.destroyDrawingCache();
        webView.loadDataWithBaseURL(Constants.BASE_URL, getReleaseNotes(), "text/html", "utf-8", null);
    }

    protected String getReleaseNotes() {
        return this.versionHelper.getReleaseNotes(false);
    }

    public Object onRetainNonConfigurationInstance() {
        if (this.downloadTask != null) {
            this.downloadTask.detach();
        }
        return this.downloadTask;
    }

    protected void startDownloadTask() {
        startDownloadTask(getIntent().getStringExtra(PlusShare.KEY_CALL_TO_ACTION_URL));
    }

    protected void startDownloadTask(String url) {
        createDownloadTask(url, new C09481());
        this.downloadTask.execute(new String[0]);
    }

    protected void createDownloadTask(String url, DownloadFileListener listener) {
        this.downloadTask = new DownloadFileTask(this, url, listener);
    }

    public void enableUpdateButton() {
        findViewById(UpdateView.UPDATE_BUTTON_ID).setEnabled(true);
    }

    public int getCurrentVersionCode() {
        int currentVersionCode = -1;
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 128).versionCode;
        } catch (NameNotFoundException e) {
            return currentVersionCode;
        }
    }

    public ViewGroup getLayoutView() {
        return new UpdateView(this);
    }

    public String getAppName() {
        try {
            PackageManager pm = getPackageManager();
            return pm.getApplicationLabel(pm.getApplicationInfo(getPackageName(), 0)).toString();
        } catch (NameNotFoundException e) {
            return BuildConfig.FLAVOR;
        }
    }

    private boolean isWriteExternalStorageSet(Context context) {
        return context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    private boolean isUnknownSourcesChecked() {
        Cursor query;
        String[] projection = new String[]{"value"};
        String selection = "name = ? AND value = ?";
        if (VERSION.SDK_INT >= 17) {
            query = getContentResolver().query(Global.CONTENT_URI, projection, selection, new String[]{"install_non_market_apps", String.valueOf(1)}, null);
        } else {
            query = getContentResolver().query(Secure.CONTENT_URI, projection, selection, new String[]{"install_non_market_apps", String.valueOf(1)}, null);
        }
        if (query.getCount() == 1) {
            return true;
        }
        return false;
    }

    public void onClick(View v) {
        if (!isWriteExternalStorageSet(this.context)) {
            this.error = new ErrorObject();
            this.error.setMessage("The permission to access the external storage permission is not set. Please contact the developer.");
            runOnUiThread(new C02792());
        } else if (isUnknownSourcesChecked()) {
            startDownloadTask();
            v.setEnabled(false);
        } else {
            this.error = new ErrorObject();
            this.error.setMessage("The installation from unknown sources is not enabled. Please check the device settings.");
            runOnUiThread(new C02803());
        }
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new Builder(this).setMessage("An error has occured").setCancelable(false).setTitle("Error").setIcon(17301543).setPositiveButton("OK", new C02814()).create();
            default:
                return null;
        }
    }

    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case 0:
                AlertDialog messageDialogError = (AlertDialog) dialog;
                if (this.error != null) {
                    messageDialogError.setMessage(this.error.getMessage());
                    return;
                } else {
                    messageDialogError.setMessage("An unknown error has occured.");
                    return;
                }
            default:
                return;
        }
    }
}
