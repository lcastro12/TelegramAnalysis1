package org.telegram.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.telegram.TL.TLRPC.EncryptedChat;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Views.BaseFragment;
import org.telegram.ui.Views.IdenticonView;

public class IdenticonActivity extends BaseFragment {
    private int chat_id;

    public boolean onFragmentCreate() {
        this.chat_id = getArguments().getInt("chat_id");
        return super.onFragmentCreate();
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.fragmentView == null) {
            this.fragmentView = inflater.inflate(C0419R.layout.identicon_layout, container, false);
            IdenticonView identiconView = (IdenticonView) this.fragmentView.findViewById(C0419R.id.identicon_view);
            TextView textView = (TextView) this.fragmentView.findViewById(C0419R.id.identicon_text);
            EncryptedChat encryptedChat = (EncryptedChat) MessagesController.Instance.encryptedChats.get(Integer.valueOf(this.chat_id));
            if (encryptedChat != null) {
                identiconView.setBytes(encryptedChat.auth_key);
                User user = (User) MessagesController.Instance.users.get(Integer.valueOf(encryptedChat.user_id));
                textView.setText(Html.fromHtml(String.format(getStringEntry(C0419R.string.EncryptionKeyDescription), new Object[]{user.first_name, user.first_name})));
            }
        } else {
            ViewGroup parent = (ViewGroup) this.fragmentView.getParent();
            if (parent != null) {
                parent.removeView(this.fragmentView);
            }
        }
        return this.fragmentView;
    }

    public void applySelfActionBar() {
        if (this.parentActivity != null) {
            ActionBar actionBar = this.parentActivity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setSubtitle(null);
            actionBar.setCustomView(null);
            actionBar.setTitle(getStringEntry(C0419R.string.EncryptionKey));
            TextView title = (TextView) this.parentActivity.findViewById(C0419R.id.action_bar_title);
            if (title == null) {
                title = (TextView) this.parentActivity.findViewById(this.parentActivity.getResources().getIdentifier("action_bar_title", "id", "android"));
            }
            if (title != null) {
                title.setCompoundDrawablesWithIntrinsicBounds(C0419R.drawable.ic_lock_white, 0, 0, 0);
                title.setCompoundDrawablePadding(Utilities.dp(4));
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        fixLayout();
    }

    public void onResume() {
        super.onResume();
        if (!this.isFinish && getActivity() != null) {
            ((ApplicationActivity) this.parentActivity).showActionBar();
            ((ApplicationActivity) this.parentActivity).updateActionBar();
            fixLayout();
        }
    }

    private void fixLayout() {
        final View v = getView();
        if (v != null) {
            v.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                public boolean onPreDraw() {
                    LinearLayout layout = IdenticonActivity.this.fragmentView;
                    int rotation = ((WindowManager) IdenticonActivity.this.parentActivity.getSystemService("window")).getDefaultDisplay().getRotation();
                    if (rotation == 3 || rotation == 1) {
                        layout.setOrientation(0);
                    } else {
                        layout.setOrientation(1);
                    }
                    v.setPadding(v.getPaddingLeft(), 0, v.getPaddingRight(), v.getPaddingBottom());
                    v.getViewTreeObserver().removeOnPreDrawListener(this);
                    TextView title = (TextView) IdenticonActivity.this.parentActivity.findViewById(C0419R.id.action_bar_title);
                    if (title == null) {
                        title = (TextView) IdenticonActivity.this.parentActivity.findViewById(ApplicationLoader.applicationContext.getResources().getIdentifier("action_bar_title", "id", "android"));
                    }
                    if (title != null) {
                        title.setCompoundDrawablesWithIntrinsicBounds(C0419R.drawable.ic_lock_white, 0, 0, 0);
                        title.setCompoundDrawablePadding(Utilities.dp(4));
                    }
                    return false;
                }
            });
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finishFragment();
                break;
        }
        return true;
    }
}
