package net.hockeyapp.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import net.hockeyapp.android.tasks.LoginTask;
import net.hockeyapp.android.utils.AsyncTaskUtils;
import net.hockeyapp.android.utils.HockeyLog;
import net.hockeyapp.android.utils.Util;

public class LoginActivity extends Activity {
    private Handler mLoginHandler;
    private LoginTask mLoginTask;
    private int mMode;
    private String mSecret;
    private String mUrl;

    class C00471 implements OnClickListener {
        C00471() {
        }

        public void onClick(View v) {
            LoginActivity.this.performAuthentication();
        }
    }

    private static class LoginHandler extends Handler {
        private final WeakReference<Activity> mWeakActivity;

        LoginHandler(Activity activity) {
            this.mWeakActivity = new WeakReference(activity);
        }

        public void handleMessage(Message msg) {
            Activity activity = (Activity) this.mWeakActivity.get();
            if (activity != null) {
                if (msg.getData().getBoolean("success")) {
                    activity.finish();
                    if (LoginManager.listener != null) {
                        LoginManager.listener.onSuccess();
                        return;
                    }
                    return;
                }
                Toast.makeText(activity, "Login failed. Check your credentials.", 1).show();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0051R.layout.hockeyapp_activity_login);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mUrl = extras.getString(UpdateFragment.FRAGMENT_URL);
            this.mSecret = extras.getString("secret");
            this.mMode = extras.getInt("mode");
        }
        configureView();
        initLoginHandler();
        Object object = getLastNonConfigurationInstance();
        if (object != null) {
            this.mLoginTask = (LoginTask) object;
            this.mLoginTask.attach(this, this.mLoginHandler);
        }
    }

    public Object onRetainNonConfigurationInstance() {
        if (this.mLoginTask != null) {
            this.mLoginTask.detach();
        }
        return this.mLoginTask;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        if (LoginManager.listener != null) {
            LoginManager.listener.onBack();
        }
        return true;
    }

    private void configureView() {
        if (this.mMode == 1) {
            ((EditText) findViewById(C0051R.id.input_password)).setVisibility(4);
        }
        ((TextView) findViewById(C0051R.id.text_headline)).setText(this.mMode == 1 ? C0051R.string.hockeyapp_login_headline_text_email_only : C0051R.string.hockeyapp_login_headline_text);
        ((Button) findViewById(C0051R.id.button_login)).setOnClickListener(new C00471());
    }

    private void initLoginHandler() {
        this.mLoginHandler = new LoginHandler(this);
    }

    private void performAuthentication() {
        if (Util.isConnectedToNetwork(this)) {
            String email = ((EditText) findViewById(C0051R.id.input_email)).getText().toString();
            String password = ((EditText) findViewById(C0051R.id.input_password)).getText().toString();
            boolean ready = false;
            Map<String, String> params = new HashMap();
            if (this.mMode == 1) {
                ready = !TextUtils.isEmpty(email);
                params.put("email", email);
                params.put("authcode", md5(this.mSecret + email));
            } else if (this.mMode == 2) {
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    ready = false;
                } else {
                    ready = true;
                }
                params.put("email", email);
                params.put("password", password);
            }
            if (ready) {
                this.mLoginTask = new LoginTask(this, this.mLoginHandler, this.mUrl, this.mMode, params);
                AsyncTaskUtils.execute(this.mLoginTask);
                return;
            }
            Toast.makeText(this, getString(C0051R.string.hockeyapp_login_missing_credentials_toast), 1).show();
            return;
        }
        Toast.makeText(this, C0051R.string.hockeyapp_error_no_network_message, 1).show();
    }

    public String md5(String s) {
        try {
            return Util.bytesToHex(Util.hash(s.getBytes(), "MD5"));
        } catch (Throwable e) {
            HockeyLog.error("Failed to create MD5 hash", e);
            return TtmlNode.ANONYMOUS_REGION_ID;
        }
    }
}
