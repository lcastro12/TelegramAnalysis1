package org.telegram.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C0488R;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.InputPeer;
import org.telegram.tgnet.TLRPC.TL_account_reportPeer;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputReportReasonOther;
import org.telegram.tgnet.TLRPC.TL_messages_report;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;

public class ReportOtherActivity extends BaseFragment {
    private static final int done_button = 1;
    private long dialog_id = getArguments().getLong("dialog_id", 0);
    private View doneButton;
    private EditTextBoldCursor firstNameField;
    private View headerLabelView;
    private int message_id = getArguments().getInt("message_id", 0);

    class C22051 extends ActionBarMenuOnItemClick {

        class C22041 implements RequestDelegate {
            C22041() {
            }

            public void run(TLObject response, TL_error error) {
            }
        }

        C22051() {
        }

        public void onItemClick(int id) {
            if (id == -1) {
                ReportOtherActivity.this.finishFragment();
            } else if (id == 1 && ReportOtherActivity.this.firstNameField.getText().length() != 0) {
                TLObject req;
                InputPeer peer = MessagesController.getInstance(UserConfig.selectedAccount).getInputPeer((int) ReportOtherActivity.this.dialog_id);
                TLObject request;
                if (ReportOtherActivity.this.message_id != 0) {
                    request = new TL_messages_report();
                    request.peer = peer;
                    request.id.add(Integer.valueOf(ReportOtherActivity.this.message_id));
                    request.reason = new TL_inputReportReasonOther();
                    request.reason.text = ReportOtherActivity.this.firstNameField.getText().toString();
                    req = request;
                } else {
                    request = new TL_account_reportPeer();
                    request.peer = MessagesController.getInstance(ReportOtherActivity.this.currentAccount).getInputPeer((int) ReportOtherActivity.this.dialog_id);
                    request.reason = new TL_inputReportReasonOther();
                    request.reason.text = ReportOtherActivity.this.firstNameField.getText().toString();
                    req = request;
                }
                ConnectionsManager.getInstance(ReportOtherActivity.this.currentAccount).sendRequest(req, new C22041());
                if (ReportOtherActivity.this.getParentActivity() != null) {
                    Toast.makeText(ReportOtherActivity.this.getParentActivity(), LocaleController.getString("ReportChatSent", C0488R.string.ReportChatSent), 0).show();
                }
                ReportOtherActivity.this.finishFragment();
            }
        }
    }

    class C22062 implements OnTouchListener {
        C22062() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }

    class C22073 implements OnEditorActionListener {
        C22073() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 6 || ReportOtherActivity.this.doneButton == null) {
                return false;
            }
            ReportOtherActivity.this.doneButton.performClick();
            return true;
        }
    }

    class C22084 implements Runnable {
        C22084() {
        }

        public void run() {
            if (ReportOtherActivity.this.firstNameField != null) {
                ReportOtherActivity.this.firstNameField.requestFocus();
                AndroidUtilities.showKeyboard(ReportOtherActivity.this.firstNameField);
            }
        }
    }

    public ReportOtherActivity(Bundle args) {
        super(args);
    }

    public View createView(Context context) {
        int i = 3;
        this.actionBar.setBackButtonImage(C0488R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("ReportChat", C0488R.string.ReportChat));
        this.actionBar.setActionBarMenuOnItemClick(new C22051());
        this.doneButton = this.actionBar.createMenu().addItemWithWidth(1, C0488R.drawable.ic_done, AndroidUtilities.dp(56.0f));
        LinearLayout linearLayout = new LinearLayout(context);
        this.fragmentView = linearLayout;
        this.fragmentView.setLayoutParams(new LayoutParams(-1, -1));
        ((LinearLayout) this.fragmentView).setOrientation(1);
        this.fragmentView.setOnTouchListener(new C22062());
        this.firstNameField = new EditTextBoldCursor(context);
        this.firstNameField.setTextSize(1, 18.0f);
        this.firstNameField.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
        this.firstNameField.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
        this.firstNameField.setMaxLines(3);
        this.firstNameField.setPadding(0, 0, 0, 0);
        this.firstNameField.setGravity(LocaleController.isRTL ? 5 : 3);
        this.firstNameField.setInputType(180224);
        this.firstNameField.setImeOptions(6);
        EditTextBoldCursor editTextBoldCursor = this.firstNameField;
        if (LocaleController.isRTL) {
            i = 5;
        }
        editTextBoldCursor.setGravity(i);
        this.firstNameField.setCursorColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.firstNameField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.firstNameField.setCursorWidth(1.5f);
        this.firstNameField.setOnEditorActionListener(new C22073());
        linearLayout.addView(this.firstNameField, LayoutHelper.createLinear(-1, 36, 24.0f, 24.0f, 24.0f, 0.0f));
        this.firstNameField.setHint(LocaleController.getString("ReportChatDescription", C0488R.string.ReportChatDescription));
        this.firstNameField.setSelection(this.firstNameField.length());
        return this.fragmentView;
    }

    public void onResume() {
        super.onResume();
        if (!MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
            this.firstNameField.requestFocus();
            AndroidUtilities.showKeyboard(this.firstNameField);
        }
    }

    public void onTransitionAnimationEnd(boolean isOpen, boolean backward) {
        if (isOpen) {
            AndroidUtilities.runOnUIThread(new C22084(), 100);
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescription[] themeDescriptionArr = new ThemeDescription[9];
        themeDescriptionArr[0] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[1] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[3] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        themeDescriptionArr[5] = new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[6] = new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText);
        themeDescriptionArr[7] = new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField);
        themeDescriptionArr[8] = new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated);
        return themeDescriptionArr;
    }
}