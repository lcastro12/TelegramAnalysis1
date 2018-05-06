package org.telegram.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.TL_auth_sendCode;
import org.telegram.TL.TLRPC.TL_auth_sentCode;
import org.telegram.TL.TLRPC.TL_error;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.RPCRequest;
import org.telegram.messenger.RPCRequest.RPCRequestDelegate;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Views.SlideView;

public class LoginActivityPhoneView extends SlideView implements OnItemSelectedListener {
    private EditText codeField;
    private HashMap<String, String> codesMap = new HashMap();
    private ArrayList<String> countriesArray = new ArrayList();
    private HashMap<String, String> countriesMap = new HashMap();
    private TextView countryButton;
    private boolean ignoreOnPhoneChange = false;
    private boolean ignoreOnTextChange = false;
    private boolean ignoreSelection = false;
    private HashMap<String, String> languageMap = new HashMap();
    private EditText phoneField;

    class C05291 implements OnClickListener {
        C05291() {
        }

        public void onClick(View view) {
            ActionBarActivity activity = LoginActivityPhoneView.this.delegate;
            activity.startActivityForResult(new Intent(activity, CountrySelectActivity.class), 1);
        }
    }

    class C05302 implements TextWatcher {
        C05302() {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void afterTextChanged(Editable editable) {
            if (LoginActivityPhoneView.this.ignoreOnTextChange) {
                LoginActivityPhoneView.this.ignoreOnTextChange = false;
                return;
            }
            LoginActivityPhoneView.this.ignoreOnTextChange = true;
            String text = PhoneFormat.stripExceptNumbers(LoginActivityPhoneView.this.codeField.getText().toString());
            LoginActivityPhoneView.this.codeField.setText(text);
            String country = (String) LoginActivityPhoneView.this.codesMap.get(text);
            if (country != null) {
                int index = LoginActivityPhoneView.this.countriesArray.indexOf(country);
                if (index != -1) {
                    LoginActivityPhoneView.this.ignoreSelection = true;
                    LoginActivityPhoneView.this.countryButton.setText((CharSequence) LoginActivityPhoneView.this.countriesArray.get(index));
                    LoginActivityPhoneView.this.updatePhoneField();
                }
            }
            LoginActivityPhoneView.this.codeField.setSelection(LoginActivityPhoneView.this.codeField.getText().length());
        }
    }

    class C05313 implements OnEditorActionListener {
        C05313() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 5) {
                return false;
            }
            LoginActivityPhoneView.this.phoneField.requestFocus();
            return true;
        }
    }

    class C05324 implements TextWatcher {
        C05324() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (!LoginActivityPhoneView.this.ignoreOnPhoneChange && count == 1 && after == 0 && s.length() > 1) {
                String phoneChars = "0123456789";
                String str = s.toString();
                if (!phoneChars.contains(str.substring(start, start + 1))) {
                    LoginActivityPhoneView.this.ignoreOnPhoneChange = true;
                    StringBuilder builder = new StringBuilder(str);
                    int toDelete = 0;
                    int a = start;
                    while (a >= 0 && !phoneChars.contains(str.substring(a, a + 1))) {
                        toDelete++;
                        a--;
                    }
                    builder.delete(Math.max(0, start - toDelete), start + 1);
                    str = builder.toString();
                    if (PhoneFormat.strip(str).length() == 0) {
                        LoginActivityPhoneView.this.phoneField.setText(BuildConfig.FLAVOR);
                    } else {
                        LoginActivityPhoneView.this.phoneField.setText(str);
                        LoginActivityPhoneView.this.updatePhoneField();
                    }
                    LoginActivityPhoneView.this.ignoreOnPhoneChange = false;
                }
            }
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (!LoginActivityPhoneView.this.ignoreOnPhoneChange) {
                LoginActivityPhoneView.this.updatePhoneField();
            }
        }
    }

    class C05335 implements Comparator<String> {
        C05335() {
        }

        public int compare(String lhs, String rhs) {
            return lhs.compareTo(rhs);
        }
    }

    class C05346 implements OnEditorActionListener {
        C05346() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 5) {
                return false;
            }
            LoginActivityPhoneView.this.delegate.onNextAction();
            return true;
        }
    }

    protected static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new C05361();
        public String code;
        public String phone;

        static class C05361 implements Creator<SavedState> {
            C05361() {
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        private SavedState(Parcelable superState, String text1, String text2) {
            super(superState);
            this.phone = text1;
            this.code = text2;
            if (this.phone == null) {
                this.phone = BuildConfig.FLAVOR;
            }
            if (this.code == null) {
                this.code = BuildConfig.FLAVOR;
            }
        }

        private SavedState(Parcel in) {
            super(in);
            this.phone = in.readString();
            this.code = in.readString();
        }

        public void writeToParcel(Parcel destination, int flags) {
            super.writeToParcel(destination, flags);
            destination.writeString(this.phone);
            destination.writeString(this.code);
        }
    }

    public LoginActivityPhoneView(Context context) {
        super(context);
    }

    public LoginActivityPhoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginActivityPhoneView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.countryButton = (TextView) findViewById(C0419R.id.login_coutry_textview);
        this.countryButton.setOnClickListener(new C05291());
        this.codeField = (EditText) findViewById(C0419R.id.login_county_code_field);
        this.codeField.addTextChangedListener(new C05302());
        this.codeField.setOnEditorActionListener(new C05313());
        this.phoneField = (EditText) findViewById(C0419R.id.login_phone_field);
        this.phoneField.addTextChangedListener(new C05324());
        if (!isInEditMode()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().getAssets().open("countries.txt")));
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    String[] args = line.split(";");
                    this.countriesArray.add(0, args[2]);
                    this.countriesMap.put(args[2], args[0]);
                    this.codesMap.put(args[0], args[2]);
                    this.languageMap.put(args[1], args[2]);
                }
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            }
            Collections.sort(this.countriesArray, new C05335());
            if (!false) {
                String country = "RU";
                try {
                    TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
                    if (telephonyManager != null) {
                        country = telephonyManager.getSimCountryIso().toUpperCase();
                    }
                } catch (Exception e2) {
                    FileLog.m799e("tmessages", e2);
                }
                if (country == null || country.length() == 0) {
                    try {
                        country = ApplicationLoader.applicationContext.getResources().getConfiguration().locale.getCountry().toUpperCase();
                    } catch (Exception e22) {
                        FileLog.m799e("tmessages", e22);
                    }
                }
                if (country == null || country.length() == 0) {
                    country = "RU";
                }
                String countryName = (String) this.languageMap.get(country);
                if (countryName == null) {
                    countryName = "Russia";
                }
                if (this.countriesArray.indexOf(countryName) != -1) {
                    this.codeField.setText((CharSequence) this.countriesMap.get(countryName));
                }
            }
            Utilities.showKeyboard(this.phoneField);
            this.phoneField.requestFocus();
            this.phoneField.setOnEditorActionListener(new C05346());
        }
    }

    public void selectCountry(String name) {
        if (this.countriesArray.indexOf(name) != -1) {
            this.codeField.setText((CharSequence) this.countriesMap.get(name));
        }
    }

    private void updatePhoneField() {
        this.ignoreOnPhoneChange = true;
        String codeText = this.codeField.getText().toString();
        String phone = PhoneFormat.Instance.format("+" + codeText + this.phoneField.getText().toString());
        int idx = phone.indexOf(" ");
        if (idx == -1) {
            this.phoneField.setSelection(this.phoneField.length());
        } else if (codeText.equals(PhoneFormat.stripExceptNumbers(phone.substring(0, idx)))) {
            this.phoneField.setText(phone.substring(idx).trim());
            r2 = this.phoneField.length();
            this.phoneField.setSelection(this.phoneField.length());
        } else {
            this.phoneField.setText(PhoneFormat.Instance.format(this.phoneField.getText().toString()).trim());
            r2 = this.phoneField.length();
            this.phoneField.setSelection(this.phoneField.length());
        }
        this.ignoreOnPhoneChange = false;
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (this.ignoreSelection) {
            this.ignoreSelection = false;
            return;
        }
        this.ignoreOnTextChange = true;
        this.codeField.setText((CharSequence) this.countriesMap.get((String) this.countriesArray.get(i)));
        updatePhoneField();
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void onNextPressed() {
        if (this.codeField.length() == 0 || this.phoneField.length() == 0) {
            this.delegate.needShowAlert(ApplicationLoader.applicationContext.getString(C0419R.string.InvalidPhoneNumber));
            return;
        }
        TL_auth_sendCode req = new TL_auth_sendCode();
        String phone = PhoneFormat.stripExceptNumbers(BuildConfig.FLAVOR + this.codeField.getText() + this.phoneField.getText());
        req.api_hash = ConnectionsManager.APP_HASH;
        req.api_id = ConnectionsManager.APP_ID;
        req.sms_type = 0;
        req.phone_number = phone;
        req.lang_code = Locale.getDefault().getCountry();
        if (req.lang_code == null || req.lang_code.length() == 0) {
            req.lang_code = "en";
        }
        final Bundle params = new Bundle();
        params.putString("phone", "+" + this.codeField.getText() + this.phoneField.getText());
        params.putString("phoneFormated", phone);
        this.delegate.needShowProgress();
        ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {

            class C05351 implements Runnable {
                C05351() {
                }

                public void run() {
                    if (LoginActivityPhoneView.this.delegate != null) {
                        LoginActivityPhoneView.this.delegate.setPage(1, true, params, false);
                    }
                }
            }

            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    TL_auth_sentCode res = (TL_auth_sentCode) response;
                    params.putString("phoneHash", res.phone_code_hash);
                    if (res.phone_registered) {
                        params.putString("registered", "true");
                    }
                    Utilities.RunOnUIThread(new C05351());
                } else if (!(LoginActivityPhoneView.this.delegate == null || error.text == null)) {
                    if (error.text.contains("PHONE_NUMBER_INVALID")) {
                        LoginActivityPhoneView.this.delegate.needShowAlert(ApplicationLoader.applicationContext.getString(C0419R.string.InvalidPhoneNumber));
                    } else if (error.text.contains("PHONE_CODE_EMPTY") || error.text.contains("PHONE_CODE_INVALID")) {
                        LoginActivityPhoneView.this.delegate.needShowAlert(ApplicationLoader.applicationContext.getString(C0419R.string.InvalidCode));
                    } else if (error.text.contains("PHONE_CODE_EXPIRED")) {
                        LoginActivityPhoneView.this.delegate.needShowAlert(ApplicationLoader.applicationContext.getString(C0419R.string.CodeExpired));
                    } else if (error.text.contains("FLOOD_WAIT")) {
                        LoginActivityPhoneView.this.delegate.needShowAlert(ApplicationLoader.applicationContext.getString(C0419R.string.FloodWait));
                    } else {
                        LoginActivityPhoneView.this.delegate.needShowAlert(error.text);
                    }
                }
                if (LoginActivityPhoneView.this.delegate != null) {
                    LoginActivityPhoneView.this.delegate.needHideProgress();
                }
            }
        }, null, true, RPCRequest.RPCRequestClassGeneric | RPCRequest.RPCRequestClassFailOnServerErrors);
    }

    public void onShow() {
        super.onShow();
        if (this.phoneField != null) {
            this.phoneField.requestFocus();
            this.phoneField.setSelection(this.phoneField.length());
        }
    }

    public String getHeaderName() {
        return getResources().getString(C0419R.string.YourPhone);
    }

    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), this.phoneField.getText().toString(), this.codeField.getText().toString());
    }

    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.codeField.setText(savedState.code);
        this.phoneField.setText(savedState.phone);
    }
}
