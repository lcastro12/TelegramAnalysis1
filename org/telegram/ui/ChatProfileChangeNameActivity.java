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
import org.telegram.TL.TLRPC.Chat;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Views.BaseFragment;

public class ChatProfileChangeNameActivity extends BaseFragment {
    private int chat_id;
    private View doneButton;
    private EditText firstNameField;
    private View headerLabelView;

    class C04731 implements OnEditorActionListener {
        C04731() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 6) {
                return false;
            }
            ChatProfileChangeNameActivity.this.doneButton.performClick();
            return true;
        }
    }

    class C04742 implements OnClickListener {
        C04742() {
        }

        public void onClick(View view) {
            ChatProfileChangeNameActivity.this.finishFragment();
        }
    }

    class C04753 implements OnClickListener {
        C04753() {
        }

        public void onClick(View view) {
            if (ChatProfileChangeNameActivity.this.firstNameField.getText().length() != 0) {
                ChatProfileChangeNameActivity.this.saveName();
                ChatProfileChangeNameActivity.this.finishFragment();
            }
        }
    }

    class C04764 implements AnimationListener {
        C04764() {
        }

        public void onAnimationStart(Animation animation) {
            ChatProfileChangeNameActivity.this.onAnimationStart();
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            ChatProfileChangeNameActivity.this.onAnimationEnd();
            ChatProfileChangeNameActivity.this.firstNameField.requestFocus();
            Utilities.showKeyboard(ChatProfileChangeNameActivity.this.firstNameField);
        }
    }

    public ChatProfileChangeNameActivity() {
        this.animationType = 1;
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.chat_id = getArguments().getInt("chat_id", 0);
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.fragmentView == null) {
            this.fragmentView = inflater.inflate(C0419R.layout.chat_profile_change_name_layout, container, false);
            Chat currentChat = (Chat) MessagesController.Instance.chats.get(Integer.valueOf(this.chat_id));
            this.firstNameField = (EditText) this.fragmentView.findViewById(C0419R.id.first_name_field);
            this.firstNameField.setOnEditorActionListener(new C04731());
            this.firstNameField.setText(currentChat.title);
            this.firstNameField.setSelection(this.firstNameField.length());
            ((TextView) this.fragmentView.findViewById(C0419R.id.settings_section_text)).setText(getStringEntry(C0419R.string.EnterGroupNameTitle));
        } else {
            ViewGroup parent = (ViewGroup) this.fragmentView.getParent();
            if (parent != null) {
                parent.removeView(this.fragmentView);
            }
        }
        return this.fragmentView;
    }

    public boolean canApplyUpdateStatus() {
        return false;
    }

    public void applySelfActionBar() {
        if (this.parentActivity != null) {
            ActionBar actionBar = this.parentActivity.getSupportActionBar();
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setCustomView((int) C0419R.layout.settings_do_action_layout);
            actionBar.getCustomView().findViewById(C0419R.id.cancel_button).setOnClickListener(new C04742());
            this.doneButton = actionBar.getCustomView().findViewById(C0419R.id.done_button);
            this.doneButton.setOnClickListener(new C04753());
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
        anim.setAnimationListener(new C04764());
        return anim;
    }

    private void saveName() {
        MessagesController.Instance.changeChatTitle(this.chat_id, this.firstNameField.getText().toString());
    }
}
