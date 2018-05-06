package org.telegram.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.TL_account_updateProfile;
import org.telegram.TL.TLRPC.TL_error;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.RPCRequest;
import org.telegram.messenger.RPCRequest.RPCRequestDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Views.BaseFragment;

public class SettingsChangeNameActivity extends BaseFragment {
    private View doneButton;
    private EditText firstNameField;
    private View headerLabelView;
    private EditText lastNameField;

    class C05771 implements OnClickListener {
        C05771() {
        }

        public void onClick(View view) {
            SettingsChangeNameActivity.this.finishFragment();
        }
    }

    class C05782 implements OnClickListener {
        C05782() {
        }

        public void onClick(View view) {
            if (SettingsChangeNameActivity.this.firstNameField.getText().length() != 0) {
                SettingsChangeNameActivity.this.saveName();
                SettingsChangeNameActivity.this.finishFragment();
            }
        }
    }

    class C05793 implements AnimationListener {
        C05793() {
        }

        public void onAnimationStart(Animation animation) {
            SettingsChangeNameActivity.this.onAnimationStart();
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            SettingsChangeNameActivity.this.onAnimationEnd();
            SettingsChangeNameActivity.this.firstNameField.requestFocus();
            Utilities.showKeyboard(SettingsChangeNameActivity.this.firstNameField);
        }
    }

    class C05804 implements OnEditorActionListener {
        C05804() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 5) {
                return false;
            }
            SettingsChangeNameActivity.this.lastNameField.requestFocus();
            SettingsChangeNameActivity.this.lastNameField.setSelection(SettingsChangeNameActivity.this.lastNameField.length());
            return true;
        }
    }

    class C05815 implements OnEditorActionListener {
        C05815() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 6) {
                return false;
            }
            SettingsChangeNameActivity.this.doneButton.performClick();
            return true;
        }
    }

    class C08886 implements RPCRequestDelegate {
        C08886() {
        }

        public void run(TLObject response, TL_error error) {
        }
    }

    public SettingsChangeNameActivity() {
        this.animationType = 1;
    }

    public boolean canApplyUpdateStatus() {
        return false;
    }

    public void onResume() {
        super.onResume();
        if (!this.isFinish) {
            ActionBar actionBar = this.parentActivity.getSupportActionBar();
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setSubtitle(null);
            actionBar.setCustomView((int) C0419R.layout.settings_do_action_layout);
            actionBar.getCustomView().findViewById(C0419R.id.cancel_button).setOnClickListener(new C05771());
            this.doneButton = actionBar.getCustomView().findViewById(C0419R.id.done_button);
            this.doneButton.setOnClickListener(new C05782());
            if (!ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).getBoolean("view_animations", true)) {
                this.firstNameField.requestFocus();
                Utilities.showKeyboard(this.firstNameField);
            }
        }
    }

    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (nextAnim == 0) {
            return super.onCreateAnimation(transit, enter, nextAnim);
        }
        Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        anim.setAnimationListener(new C05793());
        return anim;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.fragmentView == null) {
            this.fragmentView = inflater.inflate(C0419R.layout.settings_change_name_layout, container, false);
            User user = (User) MessagesController.Instance.users.get(Integer.valueOf(UserConfig.clientUserId));
            if (user == null) {
                user = UserConfig.currentUser;
            }
            this.firstNameField = (EditText) this.fragmentView.findViewById(C0419R.id.first_name_field);
            this.firstNameField.setOnEditorActionListener(new C05804());
            this.lastNameField = (EditText) this.fragmentView.findViewById(C0419R.id.last_name_field);
            this.lastNameField.setOnEditorActionListener(new C05815());
            if (user != null) {
                this.firstNameField.setText(user.first_name);
                this.firstNameField.setSelection(this.firstNameField.length());
                this.lastNameField.setText(user.last_name);
            }
            ((TextView) this.fragmentView.findViewById(C0419R.id.settings_section_text)).setText(getStringEntry(C0419R.string.YourFirstNameAndLastName));
        } else {
            ViewGroup parent = (ViewGroup) this.fragmentView.getParent();
            if (parent != null) {
                parent.removeView(this.fragmentView);
            }
        }
        return this.fragmentView;
    }

    private void saveName() {
        TL_account_updateProfile req = new TL_account_updateProfile();
        User user = UserConfig.currentUser;
        String obj = this.firstNameField.getText().toString();
        req.first_name = obj;
        user.first_name = obj;
        user = UserConfig.currentUser;
        obj = this.lastNameField.getText().toString();
        req.last_name = obj;
        user.last_name = obj;
        User user2 = (User) MessagesController.Instance.users.get(Integer.valueOf(UserConfig.clientUserId));
        if (user2 != null) {
            user2.first_name = req.first_name;
            user2.last_name = req.last_name;
        }
        UserConfig.saveConfig(true);
        NotificationCenter.Instance.postNotificationName(3, Integer.valueOf(1));
        ConnectionsManager.Instance.performRpc(req, new C08886(), null, true, RPCRequest.RPCRequestClassGeneric);
    }
}
