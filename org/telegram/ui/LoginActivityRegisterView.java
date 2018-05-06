package org.telegram.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import java.util.ArrayList;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.TL_auth_authorization;
import org.telegram.TL.TLRPC.TL_auth_signUp;
import org.telegram.TL.TLRPC.TL_error;
import org.telegram.TL.TLRPC.TL_userSelf;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.RPCRequest;
import org.telegram.messenger.RPCRequest.RPCRequestDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Views.SlideView;

public class LoginActivityRegisterView extends SlideView {
    private Bundle currentParams;
    private EditText firstNameField;
    private EditText lastNameField;
    private String phoneCode;
    private String phoneHash;
    private String requestPhone;

    class C05371 implements OnClickListener {
        C05371() {
        }

        public void onClick(View view) {
            LoginActivityRegisterView.this.onBackPressed();
            LoginActivityRegisterView.this.delegate.setPage(0, true, null, true);
        }
    }

    class C05382 implements OnEditorActionListener {
        C05382() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 5) {
                return false;
            }
            LoginActivityRegisterView.this.lastNameField.requestFocus();
            return true;
        }
    }

    protected static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new C05401();
        public String firstName;
        public String lastName;
        public Bundle params;

        static class C05401 implements Creator<SavedState> {
            C05401() {
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        private SavedState(Parcelable superState, String text1, String text2, Bundle p1) {
            super(superState);
            this.firstName = text1;
            this.lastName = text2;
            if (this.firstName == null) {
                this.firstName = BuildConfig.FLAVOR;
            }
            if (this.lastName == null) {
                this.lastName = BuildConfig.FLAVOR;
            }
            this.params = p1;
        }

        private SavedState(Parcel in) {
            super(in);
            this.firstName = in.readString();
            this.lastName = in.readString();
            this.params = in.readBundle();
        }

        public void writeToParcel(Parcel destination, int flags) {
            super.writeToParcel(destination, flags);
            destination.writeString(this.firstName);
            destination.writeString(this.lastName);
            destination.writeBundle(this.params);
        }
    }

    class C08703 implements RPCRequestDelegate {
        C08703() {
        }

        public void run(TLObject response, TL_error error) {
            if (LoginActivityRegisterView.this.delegate != null) {
                LoginActivityRegisterView.this.delegate.needHideProgress();
            }
            if (error == null) {
                final TL_auth_authorization res = (TL_auth_authorization) response;
                Utilities.RunOnUIThread(new Runnable() {
                    public void run() {
                        TL_userSelf user = res.user;
                        UserConfig.clearConfig();
                        MessagesStorage.Instance.cleanUp();
                        MessagesController.Instance.cleanUp();
                        ConnectionsManager.Instance.cleanUp();
                        UserConfig.currentUser = user;
                        UserConfig.clientActivated = true;
                        UserConfig.clientUserId = user.id;
                        UserConfig.saveConfig(true);
                        ArrayList<User> users = new ArrayList();
                        users.add(user);
                        MessagesStorage.Instance.putUsersAndChats(users, null, true, true);
                        MessagesController.Instance.users.put(Integer.valueOf(res.user.id), res.user);
                        MessagesController.Instance.checkAppAccount();
                        if (LoginActivityRegisterView.this.delegate != null) {
                            LoginActivityRegisterView.this.delegate.needFinishActivity();
                        }
                    }
                });
            } else if (LoginActivityRegisterView.this.delegate == null) {
            } else {
                if (error.text.contains("PHONE_NUMBER_INVALID")) {
                    LoginActivityRegisterView.this.delegate.needShowAlert(ApplicationLoader.applicationContext.getString(C0419R.string.InvalidPhoneNumber));
                } else if (error.text.contains("PHONE_CODE_EMPTY") || error.text.contains("PHONE_CODE_INVALID")) {
                    LoginActivityRegisterView.this.delegate.needShowAlert(ApplicationLoader.applicationContext.getString(C0419R.string.InvalidCode));
                } else if (error.text.contains("PHONE_CODE_EXPIRED")) {
                    LoginActivityRegisterView.this.delegate.needShowAlert(ApplicationLoader.applicationContext.getString(C0419R.string.CodeExpired));
                } else if (error.text.contains("FIRSTNAME_INVALID")) {
                    LoginActivityRegisterView.this.delegate.needShowAlert(ApplicationLoader.applicationContext.getString(C0419R.string.InvalidFirstName));
                } else if (error.text.contains("LASTNAME_INVALID")) {
                    LoginActivityRegisterView.this.delegate.needShowAlert(ApplicationLoader.applicationContext.getString(C0419R.string.InvalidLastName));
                } else {
                    LoginActivityRegisterView.this.delegate.needShowAlert(error.text);
                }
            }
        }
    }

    public LoginActivityRegisterView(Context context) {
        super(context);
    }

    public LoginActivityRegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginActivityRegisterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.firstNameField = (EditText) findViewById(C0419R.id.login_first_name_field);
        this.lastNameField = (EditText) findViewById(C0419R.id.login_last_name_field);
        ((TextView) findViewById(C0419R.id.changed_mind)).setOnClickListener(new C05371());
        this.firstNameField.setOnEditorActionListener(new C05382());
    }

    public void resetAvatar() {
    }

    public void onDestroyActivity() {
        super.onDestroyActivity();
    }

    public void onBackPressed() {
        this.currentParams = null;
    }

    public String getHeaderName() {
        return getResources().getString(C0419R.string.YourName);
    }

    public void onShow() {
        super.onShow();
        if (this.firstNameField != null) {
            this.firstNameField.requestFocus();
            this.firstNameField.setSelection(this.firstNameField.length());
        }
    }

    public void setParams(Bundle params) {
        if (params != null) {
            this.firstNameField.setText(BuildConfig.FLAVOR);
            this.lastNameField.setText(BuildConfig.FLAVOR);
            this.requestPhone = params.getString("phoneFormated");
            this.phoneHash = params.getString("phoneHash");
            this.phoneCode = params.getString("code");
            this.currentParams = params;
            resetAvatar();
        }
    }

    public void onNextPressed() {
        TL_auth_signUp req = new TL_auth_signUp();
        req.phone_code = this.phoneCode;
        req.phone_code_hash = this.phoneHash;
        req.phone_number = this.requestPhone;
        req.first_name = this.firstNameField.getText().toString();
        req.last_name = this.lastNameField.getText().toString();
        this.delegate.needShowProgress();
        ConnectionsManager.Instance.performRpc(req, new C08703(), null, true, RPCRequest.RPCRequestClassGeneric);
    }

    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), this.firstNameField.getText().toString(), this.lastNameField.getText().toString(), this.currentParams);
    }

    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.currentParams = savedState.params;
        if (this.currentParams != null) {
            setParams(this.currentParams);
        }
        this.firstNameField.setText(savedState.firstName);
        this.lastNameField.setText(savedState.lastName);
    }
}
