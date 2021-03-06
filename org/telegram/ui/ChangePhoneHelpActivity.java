package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C0488R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.LayoutHelper;

public class ChangePhoneHelpActivity extends BaseFragment {
    private ImageView imageView;
    private TextView textView1;
    private TextView textView2;

    class C11361 extends ActionBarMenuOnItemClick {
        C11361() {
        }

        public void onItemClick(int id) {
            if (id == -1) {
                ChangePhoneHelpActivity.this.finishFragment();
            }
        }
    }

    class C11372 implements OnTouchListener {
        C11372() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }

    class C11393 implements OnClickListener {

        class C11381 implements DialogInterface.OnClickListener {
            C11381() {
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                ChangePhoneHelpActivity.this.presentFragment(new ChangePhoneActivity(), true);
            }
        }

        C11393() {
        }

        public void onClick(View v) {
            if (ChangePhoneHelpActivity.this.getParentActivity() != null) {
                Builder builder = new Builder(ChangePhoneHelpActivity.this.getParentActivity());
                builder.setTitle(LocaleController.getString("AppName", C0488R.string.AppName));
                builder.setMessage(LocaleController.getString("PhoneNumberAlert", C0488R.string.PhoneNumberAlert));
                builder.setPositiveButton(LocaleController.getString("OK", C0488R.string.OK), new C11381());
                builder.setNegativeButton(LocaleController.getString("Cancel", C0488R.string.Cancel), null);
                ChangePhoneHelpActivity.this.showDialog(builder.create());
            }
        }
    }

    public View createView(Context context) {
        String value;
        this.actionBar.setBackButtonImage(C0488R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        User user = UserConfig.getInstance(this.currentAccount).getCurrentUser();
        if (user == null || user.phone == null || user.phone.length() == 0) {
            value = LocaleController.getString("NumberUnknown", C0488R.string.NumberUnknown);
        } else {
            value = PhoneFormat.getInstance().format("+" + user.phone);
        }
        this.actionBar.setTitle(value);
        this.actionBar.setActionBarMenuOnItemClick(new C11361());
        this.fragmentView = new RelativeLayout(context);
        this.fragmentView.setOnTouchListener(new C11372());
        RelativeLayout relativeLayout = this.fragmentView;
        ScrollView scrollView = new ScrollView(context);
        relativeLayout.addView(scrollView);
        LayoutParams layoutParams3 = (LayoutParams) scrollView.getLayoutParams();
        layoutParams3.width = -1;
        layoutParams3.height = -2;
        layoutParams3.addRule(15, -1);
        scrollView.setLayoutParams(layoutParams3);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(0, AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f));
        scrollView.addView(linearLayout);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -2;
        linearLayout.setLayoutParams(layoutParams);
        this.imageView = new ImageView(context);
        this.imageView.setImageResource(C0488R.drawable.phone_change);
        this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_changephoneinfo_image), Mode.MULTIPLY));
        linearLayout.addView(this.imageView, LayoutHelper.createLinear(-2, -2, 1));
        this.textView1 = new TextView(context);
        this.textView1.setTextSize(1, 16.0f);
        this.textView1.setGravity(1);
        this.textView1.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        try {
            this.textView1.setText(AndroidUtilities.replaceTags(LocaleController.getString("PhoneNumberHelp", C0488R.string.PhoneNumberHelp)));
        } catch (Throwable e) {
            FileLog.m3e(e);
            this.textView1.setText(LocaleController.getString("PhoneNumberHelp", C0488R.string.PhoneNumberHelp));
        }
        linearLayout.addView(this.textView1, LayoutHelper.createLinear(-2, -2, 1, 20, 56, 20, 0));
        this.textView2 = new TextView(context);
        this.textView2.setTextSize(1, 18.0f);
        this.textView2.setGravity(1);
        this.textView2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4));
        this.textView2.setText(LocaleController.getString("PhoneNumberChange", C0488R.string.PhoneNumberChange));
        this.textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textView2.setPadding(0, AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f));
        linearLayout.addView(this.textView2, LayoutHelper.createLinear(-2, -2, 1, 20, 46, 20, 0));
        this.textView2.setOnClickListener(new C11393());
        return this.fragmentView;
    }

    public ThemeDescription[] getThemeDescriptions() {
        r8 = new ThemeDescription[8];
        r8[0] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite);
        r8[1] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        r8[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        r8[3] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        r8[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        r8[5] = new ThemeDescription(this.textView1, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r8[6] = new ThemeDescription(this.textView2, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlueText4);
        r8[7] = new ThemeDescription(this.imageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_changephoneinfo_image);
        return r8;
    }
}
