package org.telegram.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.Html;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.google.android.gms.location.LocationStatusCodes;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.TL_auth_authorization;
import org.telegram.TL.TLRPC.TL_auth_sendCall;
import org.telegram.TL.TLRPC.TL_auth_signIn;
import org.telegram.TL.TLRPC.TL_error;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.RPCRequest;
import org.telegram.messenger.RPCRequest.RPCRequestDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Views.SlideView;

public class LoginActivitySmsView extends SlideView implements NotificationCenterDelegate {
    private EditText codeField;
    private TextView confirmTextView;
    private Bundle currentParams;
    private double lastCurrentTime;
    private String phoneHash;
    private String registered;
    private String requestPhone;
    private int time = 60000;
    private TextView timeText;
    private Timer timeTimer;
    private final Integer timerSync = Integer.valueOf(1);
    private boolean waitingForSms = false;

    class C05411 implements OnClickListener {
        C05411() {
        }

        public void onClick(View view) {
            LoginActivitySmsView.this.onBackPressed();
            LoginActivitySmsView.this.delegate.setPage(0, true, null, true);
        }
    }

    class C05422 implements OnEditorActionListener {
        C05422() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 5) {
                return false;
            }
            if (LoginActivitySmsView.this.delegate != null) {
                LoginActivitySmsView.this.delegate.onNextAction();
            }
            return true;
        }
    }

    class C05443 extends TimerTask {

        class C05431 implements Runnable {

            class C08711 implements RPCRequestDelegate {
                C08711() {
                }

                public void run(TLObject response, TL_error error) {
                }
            }

            C05431() {
            }

            public void run() {
                if (LoginActivitySmsView.this.time >= LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) {
                    int seconds = (LoginActivitySmsView.this.time / LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) - (((LoginActivitySmsView.this.time / LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) / 60) * 60);
                    LoginActivitySmsView.this.timeText.setText(String.format("%s %d:%02d", new Object[]{ApplicationLoader.applicationContext.getResources().getString(C0419R.string.CallText), Integer.valueOf(minutes), Integer.valueOf(seconds)}));
                    return;
                }
                LoginActivitySmsView.this.timeText.setText(ApplicationLoader.applicationContext.getResources().getString(C0419R.string.Calling));
                synchronized (LoginActivitySmsView.this.timerSync) {
                    if (LoginActivitySmsView.this.timeTimer != null) {
                        LoginActivitySmsView.this.timeTimer.cancel();
                        LoginActivitySmsView.this.timeTimer = null;
                    }
                }
                TL_auth_sendCall req = new TL_auth_sendCall();
                req.phone_number = LoginActivitySmsView.this.requestPhone;
                req.phone_code_hash = LoginActivitySmsView.this.phoneHash;
                ConnectionsManager.Instance.performRpc(req, new C08711(), null, true, RPCRequest.RPCRequestClassGeneric | RPCRequest.RPCRequestClassFailOnServerErrors);
            }
        }

        C05443() {
        }

        public void run() {
            double currentTime = (double) System.currentTimeMillis();
            LoginActivitySmsView.access$126(LoginActivitySmsView.this, currentTime - LoginActivitySmsView.this.lastCurrentTime);
            LoginActivitySmsView.this.lastCurrentTime = currentTime;
            Utilities.RunOnUIThread(new C05431());
        }
    }

    protected static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new C05501();
        public Bundle params;

        static class C05501 implements Creator<SavedState> {
            C05501() {
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        private SavedState(Parcelable superState, Bundle p1) {
            super(superState);
            this.params = p1;
        }

        private SavedState(Parcel in) {
            super(in);
            this.params = in.readBundle();
        }

        public void writeToParcel(Parcel destination, int flags) {
            super.writeToParcel(destination, flags);
            destination.writeBundle(this.params);
        }
    }

    static /* synthetic */ int access$126(LoginActivitySmsView x0, double x1) {
        int i = (int) (((double) x0.time) - x1);
        x0.time = i;
        return i;
    }

    public LoginActivitySmsView(Context context) {
        super(context);
    }

    public LoginActivitySmsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginActivitySmsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.confirmTextView = (TextView) findViewById(C0419R.id.login_sms_confirm_text);
        this.codeField = (EditText) findViewById(C0419R.id.login_sms_code_field);
        this.timeText = (TextView) findViewById(C0419R.id.login_time_text);
        ((TextView) findViewById(C0419R.id.wrong_number)).setOnClickListener(new C05411());
        this.codeField.setOnEditorActionListener(new C05422());
    }

    public String getHeaderName() {
        return getResources().getString(C0419R.string.YourCode);
    }

    public void setParams(Bundle params) {
        this.codeField.setText(BuildConfig.FLAVOR);
        NotificationCenter.Instance.addObserver(this, 998);
        this.currentParams = params;
        this.waitingForSms = true;
        String phone = params.getString("phone");
        this.requestPhone = params.getString("phoneFormated");
        this.phoneHash = params.getString("phoneHash");
        this.registered = params.getString("registered");
        String number = PhoneFormat.Instance.format(phone);
        this.confirmTextView.setText(Html.fromHtml(String.format(ApplicationLoader.applicationContext.getResources().getString(C0419R.string.SentSmsCode) + " <b>%s</b>", new Object[]{number})));
        Utilities.showKeyboard(this.codeField);
        this.codeField.requestFocus();
        this.time = 60000;
        try {
            synchronized (this.timerSync) {
                if (this.timeTimer != null) {
                    this.timeTimer.cancel();
                    this.timeTimer = null;
                }
            }
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
        this.timeText.setText(String.format("%s 1:00", new Object[]{ApplicationLoader.applicationContext.getResources().getString(C0419R.string.CallText)}));
        this.lastCurrentTime = (double) System.currentTimeMillis();
        this.timeTimer = new Timer();
        this.timeTimer.schedule(new C05443(), 0, 1000);
    }

    public void onNextPressed() {
        this.waitingForSms = false;
        NotificationCenter.Instance.removeObserver(this, 998);
        final TL_auth_signIn req = new TL_auth_signIn();
        req.phone_number = this.requestPhone;
        req.phone_code = this.codeField.getText().toString();
        req.phone_code_hash = this.phoneHash;
        try {
            synchronized (this.timerSync) {
                if (this.timeTimer != null) {
                    this.timeTimer.cancel();
                    this.timeTimer = null;
                }
            }
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
        this.delegate.needShowProgress();
        ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {

            class C05462 implements Runnable {
                C05462() {
                }

                public void run() {
                    Bundle params = new Bundle();
                    params.putString("phoneFormated", LoginActivitySmsView.this.requestPhone);
                    params.putString("phoneHash", LoginActivitySmsView.this.phoneHash);
                    params.putString("code", req.phone_code);
                    LoginActivitySmsView.this.delegate.setPage(2, true, params, false);
                    try {
                        synchronized (LoginActivitySmsView.this.timerSync) {
                            if (LoginActivitySmsView.this.timeTimer != null) {
                                LoginActivitySmsView.this.timeTimer.cancel();
                                LoginActivitySmsView.this.timeTimer = null;
                            }
                        }
                    } catch (Exception e) {
                        FileLog.m799e("tmessages", e);
                    }
                }
            }

            class C05483 extends TimerTask {

                class C05471 implements Runnable {

                    class C08721 implements RPCRequestDelegate {
                        C08721() {
                        }

                        public void run(TLObject response, TL_error error) {
                        }
                    }

                    C05471() {
                    }

                    public void run() {
                        if (LoginActivitySmsView.this.time >= LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) {
                            int seconds = (LoginActivitySmsView.this.time / LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) - (((LoginActivitySmsView.this.time / LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) / 60) * 60);
                            LoginActivitySmsView.this.timeText.setText(String.format("%s %d:%02d", new Object[]{ApplicationLoader.applicationContext.getResources().getString(C0419R.string.CallText), Integer.valueOf(minutes), Integer.valueOf(seconds)}));
                            return;
                        }
                        LoginActivitySmsView.this.timeText.setText(ApplicationLoader.applicationContext.getResources().getString(C0419R.string.Calling));
                        synchronized (LoginActivitySmsView.this.timerSync) {
                            if (LoginActivitySmsView.this.timeTimer != null) {
                                LoginActivitySmsView.this.timeTimer.cancel();
                                LoginActivitySmsView.this.timeTimer = null;
                            }
                        }
                        TL_auth_sendCall req = new TL_auth_sendCall();
                        req.phone_number = LoginActivitySmsView.this.requestPhone;
                        req.phone_code_hash = LoginActivitySmsView.this.phoneHash;
                        ConnectionsManager.Instance.performRpc(req, new C08721(), null, true, RPCRequest.RPCRequestClassGeneric | RPCRequest.RPCRequestClassFailOnServerErrors);
                    }
                }

                C05483() {
                }

                public void run() {
                    double currentTime = (double) System.currentTimeMillis();
                    LoginActivitySmsView.access$126(LoginActivitySmsView.this, currentTime - LoginActivitySmsView.this.lastCurrentTime);
                    LoginActivitySmsView.this.lastCurrentTime = currentTime;
                    Utilities.RunOnUIThread(new C05471());
                }
            }

            public void run(TLObject response, TL_error error) {
                if (LoginActivitySmsView.this.delegate != null) {
                    LoginActivitySmsView.this.delegate.needHideProgress();
                }
                if (error == null) {
                    final TL_auth_authorization res = (TL_auth_authorization) response;
                    Utilities.RunOnUIThread(new Runnable() {
                        public void run() {
                            if (LoginActivitySmsView.this.delegate != null) {
                                try {
                                    synchronized (LoginActivitySmsView.this.timerSync) {
                                        if (LoginActivitySmsView.this.timeTimer != null) {
                                            LoginActivitySmsView.this.timeTimer.cancel();
                                            LoginActivitySmsView.this.timeTimer = null;
                                        }
                                    }
                                } catch (Exception e) {
                                    FileLog.m799e("tmessages", e);
                                }
                                UserConfig.clearConfig();
                                MessagesStorage.Instance.cleanUp();
                                MessagesController.Instance.cleanUp();
                                ConnectionsManager.Instance.cleanUp();
                                UserConfig.currentUser = res.user;
                                UserConfig.clientActivated = true;
                                UserConfig.clientUserId = res.user.id;
                                UserConfig.saveConfig(true);
                                ArrayList<User> users = new ArrayList();
                                users.add(UserConfig.currentUser);
                                MessagesStorage.Instance.putUsersAndChats(users, null, true, true);
                                MessagesController.Instance.users.put(Integer.valueOf(res.user.id), res.user);
                                MessagesController.Instance.checkAppAccount();
                                LoginActivitySmsView.this.delegate.needFinishActivity();
                            }
                        }
                    });
                } else if (error.text.contains("PHONE_NUMBER_UNOCCUPIED") && LoginActivitySmsView.this.registered == null) {
                    Utilities.RunOnUIThread(new C05462());
                } else {
                    if (LoginActivitySmsView.this.timeTimer == null) {
                        LoginActivitySmsView.this.timeTimer = new Timer();
                        LoginActivitySmsView.this.timeTimer.schedule(new C05483(), 0, 1000);
                    }
                    if (error.text.contains("PHONE_NUMBER_INVALID")) {
                        LoginActivitySmsView.this.delegate.needShowAlert(ApplicationLoader.applicationContext.getString(C0419R.string.InvalidPhoneNumber));
                    } else if (error.text.contains("PHONE_CODE_EMPTY") || error.text.contains("PHONE_CODE_INVALID")) {
                        LoginActivitySmsView.this.delegate.needShowAlert(ApplicationLoader.applicationContext.getString(C0419R.string.InvalidCode));
                    } else if (error.text.contains("PHONE_CODE_EXPIRED")) {
                        LoginActivitySmsView.this.delegate.needShowAlert(ApplicationLoader.applicationContext.getString(C0419R.string.CodeExpired));
                    } else {
                        LoginActivitySmsView.this.delegate.needShowAlert(error.text);
                    }
                }
            }
        }, null, true, RPCRequest.RPCRequestClassGeneric | RPCRequest.RPCRequestClassFailOnServerErrors);
    }

    public void onBackPressed() {
        try {
            synchronized (this.timerSync) {
                if (this.timeTimer != null) {
                    this.timeTimer.cancel();
                    this.timeTimer = null;
                }
            }
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
        this.currentParams = null;
        NotificationCenter.Instance.removeObserver(this, 998);
        this.waitingForSms = false;
    }

    public void onDestroyActivity() {
        super.onDestroyActivity();
        NotificationCenter.Instance.removeObserver(this, 998);
        try {
            synchronized (this.timerSync) {
                if (this.timeTimer != null) {
                    this.timeTimer.cancel();
                    this.timeTimer = null;
                }
            }
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
        this.waitingForSms = false;
    }

    public void onShow() {
        super.onShow();
        if (this.codeField != null) {
            this.codeField.requestFocus();
            this.codeField.setSelection(this.codeField.length());
        }
    }

    public void didReceivedNotification(int id, final Object... args) {
        if (id == 998) {
            Utilities.RunOnUIThread(new Runnable() {
                public void run() {
                    if (LoginActivitySmsView.this.waitingForSms && LoginActivitySmsView.this.codeField != null) {
                        LoginActivitySmsView.this.codeField.setText(BuildConfig.FLAVOR + args[0]);
                        LoginActivitySmsView.this.onNextPressed();
                    }
                }
            });
        }
    }

    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), this.currentParams);
    }

    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.currentParams = savedState.params;
        if (this.currentParams != null) {
            setParams(this.currentParams);
        }
    }
}
