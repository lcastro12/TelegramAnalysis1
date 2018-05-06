package net.hockeyapp.android;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.plus.PlusShare;
import net.hockeyapp.android.listeners.DownloadFileListener;
import net.hockeyapp.android.tasks.DownloadFileTask;
import net.hockeyapp.android.utils.VersionHelper;
import net.hockeyapp.android.views.UpdateView;
import org.json.JSONArray;
import org.json.JSONException;
import org.telegram.messenger.BuildConfig;

public class UpdateFragment extends DialogFragment implements OnClickListener, UpdateInfoListener {
    private DownloadFileTask downloadTask;
    private String urlString;
    private VersionHelper versionHelper;
    private JSONArray versionInfo;

    public static UpdateFragment newInstance(JSONArray versionInfo, String urlString) {
        Bundle state = new Bundle();
        state.putString(PlusShare.KEY_CALL_TO_ACTION_URL, urlString);
        state.putString("versionInfo", versionInfo.toString());
        UpdateFragment fragment = new UpdateFragment();
        fragment.setArguments(state);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.urlString = getArguments().getString(PlusShare.KEY_CALL_TO_ACTION_URL);
            this.versionInfo = new JSONArray(getArguments().getString("versionInfo"));
            setStyle(1, 16973939);
        } catch (JSONException e) {
            dismiss();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getLayoutView();
        this.versionHelper = new VersionHelper(this.versionInfo.toString(), this);
        ((TextView) view.findViewById(UpdateView.NAME_LABEL_ID)).setText(getAppName());
        ((TextView) view.findViewById(4099)).setText("Version " + this.versionHelper.getVersionString() + "\n" + this.versionHelper.getFileInfoString());
        ((Button) view.findViewById(UpdateView.UPDATE_BUTTON_ID)).setOnClickListener(this);
        WebView webView = (WebView) view.findViewById(UpdateView.WEB_VIEW_ID);
        webView.clearCache(true);
        webView.destroyDrawingCache();
        webView.loadDataWithBaseURL(Constants.BASE_URL, this.versionHelper.getReleaseNotes(false), "text/html", "utf-8", null);
        return view;
    }

    public void onClick(View view) {
        startDownloadTask(getActivity());
        dismiss();
    }

    private void startDownloadTask(final Activity activity) {
        this.downloadTask = new DownloadFileTask(activity, this.urlString, new DownloadFileListener() {
            public void downloadFailed(DownloadFileTask task, Boolean userWantsRetry) {
                if (userWantsRetry.booleanValue()) {
                    UpdateFragment.this.startDownloadTask(activity);
                }
            }

            public void downloadSuccessful(DownloadFileTask task) {
            }

            public String getStringForResource(int resourceID) {
                UpdateManagerListener listener = UpdateManager.getLastListener();
                if (listener != null) {
                    return listener.getStringForResource(resourceID);
                }
                return null;
            }
        });
        this.downloadTask.execute(new String[0]);
    }

    public int getCurrentVersionCode() {
        int currentVersionCode = -1;
        try {
            return getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 128).versionCode;
        } catch (NameNotFoundException e) {
            return currentVersionCode;
        } catch (NullPointerException e2) {
            return currentVersionCode;
        }
    }

    public String getAppName() {
        Activity activity = getActivity();
        try {
            PackageManager pm = activity.getPackageManager();
            return pm.getApplicationLabel(pm.getApplicationInfo(activity.getPackageName(), 0)).toString();
        } catch (NameNotFoundException e) {
            return BuildConfig.FLAVOR;
        }
    }

    public View getLayoutView() {
        return new UpdateView(getActivity(), false, true);
    }
}
