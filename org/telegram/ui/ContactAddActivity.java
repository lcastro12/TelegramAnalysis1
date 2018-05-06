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
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.TL.TLRPC.FileLocation;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Views.BackupImageView;
import org.telegram.ui.Views.BaseFragment;

public class ContactAddActivity extends BaseFragment implements NotificationCenterDelegate {
    private BackupImageView avatarImage;
    private View doneButton;
    private EditText firstNameField;
    private EditText lastNameField;
    private TextView onlineText;
    private String phone = null;
    private TextView phoneText;
    private int user_id;

    class C04771 implements OnEditorActionListener {
        C04771() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 5) {
                return false;
            }
            ContactAddActivity.this.lastNameField.requestFocus();
            ContactAddActivity.this.lastNameField.setSelection(ContactAddActivity.this.lastNameField.length());
            return true;
        }
    }

    class C04782 implements OnEditorActionListener {
        C04782() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 6) {
                return false;
            }
            ContactAddActivity.this.doneButton.performClick();
            return true;
        }
    }

    class C04793 implements OnClickListener {
        C04793() {
        }

        public void onClick(View view) {
            ContactAddActivity.this.finishFragment();
        }
    }

    class C04804 implements OnClickListener {
        C04804() {
        }

        public void onClick(View view) {
            if (ContactAddActivity.this.firstNameField.getText().length() != 0) {
                User user = (User) MessagesController.Instance.users.get(Integer.valueOf(ContactAddActivity.this.user_id));
                user.first_name = ContactAddActivity.this.firstNameField.getText().toString();
                user.last_name = ContactAddActivity.this.lastNameField.getText().toString();
                MessagesController.Instance.addContact(user);
                ContactAddActivity.this.finishFragment();
            }
        }
    }

    class C04815 implements AnimationListener {
        C04815() {
        }

        public void onAnimationStart(Animation animation) {
            ContactAddActivity.this.onAnimationStart();
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            ContactAddActivity.this.onAnimationEnd();
            ContactAddActivity.this.firstNameField.requestFocus();
            Utilities.showKeyboard(ContactAddActivity.this.firstNameField);
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.Instance.addObserver(this, 3);
        this.user_id = getArguments().getInt("user_id", 0);
        this.phone = getArguments().getString("phone");
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.Instance.removeObserver(this, 3);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.fragmentView == null) {
            this.fragmentView = inflater.inflate(C0419R.layout.contact_add_layout, container, false);
            User user = (User) MessagesController.Instance.users.get(Integer.valueOf(this.user_id));
            if (this.phone != null) {
                user.phone = PhoneFormat.stripExceptNumbers(this.phone);
            }
            this.onlineText = (TextView) this.fragmentView.findViewById(C0419R.id.settings_online);
            this.avatarImage = (BackupImageView) this.fragmentView.findViewById(C0419R.id.settings_avatar_image);
            this.phoneText = (TextView) this.fragmentView.findViewById(C0419R.id.settings_name);
            this.phoneText.setTypeface(Utilities.getTypeface("fonts/rmedium.ttf"));
            this.firstNameField = (EditText) this.fragmentView.findViewById(C0419R.id.first_name_field);
            this.firstNameField.setOnEditorActionListener(new C04771());
            this.lastNameField = (EditText) this.fragmentView.findViewById(C0419R.id.last_name_field);
            this.lastNameField.setOnEditorActionListener(new C04782());
            if (user != null) {
                this.firstNameField.setText(user.first_name);
                this.firstNameField.setSelection(this.firstNameField.length());
                this.lastNameField.setText(user.last_name);
            }
            updateAvatarLayout();
        } else {
            ViewGroup parent = (ViewGroup) this.fragmentView.getParent();
            if (parent != null) {
                parent.removeView(this.fragmentView);
            }
        }
        return this.fragmentView;
    }

    private void updateAvatarLayout() {
        if (this.phoneText != null) {
            User user = (User) MessagesController.Instance.users.get(Integer.valueOf(this.user_id));
            if (user != null) {
                this.phoneText.setText(PhoneFormat.Instance.format("+" + user.phone));
                if (user.status == null) {
                    this.onlineText.setText(getStringEntry(C0419R.string.Offline));
                } else {
                    int currentTime = ConnectionsManager.Instance.getCurrentTime();
                    if (user.status.expires > currentTime || user.status.was_online > currentTime) {
                        this.onlineText.setText(getStringEntry(C0419R.string.Online));
                    } else if (user.status.was_online > FileLoader.FileDidUpload || user.status.expires > FileLoader.FileDidUpload) {
                        int value = user.status.was_online;
                        if (value == 0) {
                            value = user.status.expires;
                        }
                        this.onlineText.setText(String.format("%s %s", new Object[]{getStringEntry(C0419R.string.LastSeen), Utilities.formatDateOnline((long) value)}));
                    } else {
                        this.onlineText.setText(getStringEntry(C0419R.string.Invisible));
                    }
                }
                FileLocation photo = null;
                if (user.photo != null) {
                    photo = user.photo.photo_small;
                }
                this.avatarImage.setImage(photo, "50_50", Utilities.getUserAvatarForId(user.id));
            }
        }
    }

    public void didReceivedNotification(int id, Object... args) {
        if (id == 3 && (((Integer) args[0]).intValue() & 2) != 0) {
            updateAvatarLayout();
        }
    }

    public boolean canApplyUpdateStatus() {
        return false;
    }

    public void applySelfActionBar() {
        if (this.parentActivity != null) {
            ActionBar actionBar = this.parentActivity.getSupportActionBar();
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            TextView title = (TextView) this.parentActivity.findViewById(C0419R.id.action_bar_title);
            if (title == null) {
                title = (TextView) this.parentActivity.findViewById(this.parentActivity.getResources().getIdentifier("action_bar_title", "id", "android"));
            }
            if (title != null) {
                title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                title.setCompoundDrawablePadding(0);
            }
            actionBar.setCustomView((int) C0419R.layout.settings_do_action_layout);
            actionBar.getCustomView().findViewById(C0419R.id.cancel_button).setOnClickListener(new C04793());
            this.doneButton = actionBar.getCustomView().findViewById(C0419R.id.done_button);
            this.doneButton.setOnClickListener(new C04804());
        }
    }

    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ((ApplicationActivity) this.parentActivity).updateActionBar();
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
        anim.setAnimationListener(new C04815());
        return anim;
    }
}
