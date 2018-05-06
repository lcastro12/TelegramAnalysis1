package org.telegram.ui;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.ExploreByTouchHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.Iterator;
import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.MessagesActivity.MessagesActivityDelegate;
import org.telegram.ui.Views.BaseFragment;
import org.telegram.ui.Views.NotificationView;

public class ApplicationActivity extends ActionBarActivity implements NotificationCenterDelegate, MessagesActivityDelegate {
    private View backStatusButton;
    private View containerView;
    private int currentConnectionState;
    private boolean finished = false;
    private NotificationView notificationView;
    private String photoPath = null;
    private String sendingText = null;
    private View statusBackground;
    private TextView statusText;
    private View statusView;
    private String videoPath = null;

    class C04381 implements OnClickListener {
        C04381() {
        }

        public void onClick(View view) {
            if (ApplicationLoader.fragmentsStack.size() > 1) {
                ApplicationActivity.this.onBackPressed();
            }
        }
    }

    class C04392 implements OnGlobalLayoutListener {
        C04392() {
        }

        public void onGlobalLayout() {
            int height;
            boolean z = true;
            int rotation = ((WindowManager) ApplicationActivity.this.getSystemService("window")).getDefaultDisplay().getRotation();
            int currentActionBarHeight = ApplicationActivity.this.getSupportActionBar().getHeight();
            if (currentActionBarHeight == Utilities.dp(48) || currentActionBarHeight == Utilities.dp(40)) {
                height = Utilities.dp(48);
                if (rotation == 3 || rotation == 1) {
                    height = Utilities.dp(40);
                }
            } else {
                height = currentActionBarHeight;
            }
            if (ApplicationActivity.this.notificationView != null) {
                NotificationView access$000 = ApplicationActivity.this.notificationView;
                if (!(rotation == 3 || rotation == 1)) {
                    z = false;
                }
                access$000.applyOrientationPaddings(z, height);
            }
            if (VERSION.SDK_INT < 16) {
                ApplicationActivity.this.containerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            } else {
                ApplicationActivity.this.containerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            Utilities.statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        NotificationCenter.Instance.postNotificationName(702, this);
        this.currentConnectionState = ConnectionsManager.Instance.connectionState;
        Iterator i$ = ApplicationLoader.fragmentsStack.iterator();
        while (i$.hasNext()) {
            BaseFragment fragment = (BaseFragment) i$.next();
            if (fragment.fragmentView != null) {
                ViewGroup parent = (ViewGroup) fragment.fragmentView.getParent();
                if (parent != null) {
                    parent.removeView(fragment.fragmentView);
                }
                fragment.fragmentView = null;
            }
            fragment.parentActivity = this;
        }
        setContentView((int) C0419R.layout.application_layout);
        NotificationCenter.Instance.addObserver(this, 1234);
        NotificationCenter.Instance.addObserver(this, 658);
        NotificationCenter.Instance.addObserver(this, 701);
        NotificationCenter.Instance.addObserver(this, 702);
        NotificationCenter.Instance.addObserver(this, 703);
        NotificationCenter.Instance.addObserver(this, GalleryImageViewer.needShowAllMedia);
        getSupportActionBar().setLogo((int) C0419R.drawable.ab_icon_fixed2);
        this.statusView = getLayoutInflater().inflate(C0419R.layout.updating_state_layout, null);
        this.statusBackground = this.statusView.findViewById(C0419R.id.back_button_background);
        this.backStatusButton = this.statusView.findViewById(C0419R.id.back_button);
        this.containerView = findViewById(C0419R.id.container);
        this.statusText = (TextView) this.statusView.findViewById(C0419R.id.status_text);
        this.statusBackground.setOnClickListener(new C04381());
        if (ApplicationLoader.fragmentsStack.isEmpty()) {
            MessagesActivity fragment2 = new MessagesActivity();
            fragment2.onFragmentCreate();
            ApplicationLoader.fragmentsStack.add(fragment2);
        }
        boolean pushOpened = false;
        Integer push_user_id = (Integer) NotificationCenter.Instance.getFromMemCache("push_user_id", Integer.valueOf(0));
        Integer push_chat_id = (Integer) NotificationCenter.Instance.getFromMemCache("push_chat_id", Integer.valueOf(0));
        Integer push_enc_id = (Integer) NotificationCenter.Instance.getFromMemCache("push_enc_id", Integer.valueOf(0));
        Integer open_settings = (Integer) NotificationCenter.Instance.getFromMemCache("open_settings", Integer.valueOf(0));
        this.photoPath = (String) NotificationCenter.Instance.getFromMemCache(533);
        this.videoPath = (String) NotificationCenter.Instance.getFromMemCache(534);
        this.sendingText = (String) NotificationCenter.Instance.getFromMemCache(535);
        ChatActivity fragment3;
        Bundle bundle;
        if (push_user_id.intValue() != 0) {
            if (push_user_id.intValue() == UserConfig.clientUserId) {
                open_settings = Integer.valueOf(1);
            } else {
                fragment3 = new ChatActivity();
                bundle = new Bundle();
                bundle.putInt("user_id", push_user_id.intValue());
                fragment3.setArguments(bundle);
                if (fragment3.onFragmentCreate()) {
                    pushOpened = true;
                    ApplicationLoader.fragmentsStack.add(fragment3);
                    getSupportFragmentManager().beginTransaction().replace(C0419R.id.container, fragment3, "chat" + Math.random()).commitAllowingStateLoss();
                }
            }
        } else if (push_chat_id.intValue() != 0) {
            fragment3 = new ChatActivity();
            bundle = new Bundle();
            bundle.putInt("chat_id", push_chat_id.intValue());
            fragment3.setArguments(bundle);
            if (fragment3.onFragmentCreate()) {
                pushOpened = true;
                ApplicationLoader.fragmentsStack.add(fragment3);
                getSupportFragmentManager().beginTransaction().replace(C0419R.id.container, fragment3, "chat" + Math.random()).commitAllowingStateLoss();
            }
        } else if (push_enc_id.intValue() != 0) {
            fragment3 = new ChatActivity();
            bundle = new Bundle();
            bundle.putInt("enc_id", push_enc_id.intValue());
            fragment3.setArguments(bundle);
            if (fragment3.onFragmentCreate()) {
                pushOpened = true;
                ApplicationLoader.fragmentsStack.add(fragment3);
                getSupportFragmentManager().beginTransaction().replace(C0419R.id.container, fragment3, "chat" + Math.random()).commitAllowingStateLoss();
            }
        }
        if (!(this.videoPath == null && this.photoPath == null && this.sendingText == null)) {
            fragment2 = new MessagesActivity();
            fragment2.selectAlertString = C0419R.string.ForwardMessagesTo;
            fragment2.animationType = 1;
            Bundle args = new Bundle();
            args.putBoolean("onlySelect", true);
            fragment2.setArguments(args);
            fragment2.delegate = this;
            ApplicationLoader.fragmentsStack.add(fragment2);
            fragment2.onFragmentCreate();
            getSupportFragmentManager().beginTransaction().replace(C0419R.id.container, fragment2, fragment2.getTag()).commitAllowingStateLoss();
            pushOpened = true;
        }
        if (open_settings.intValue() != 0) {
            SettingsActivity fragment4 = new SettingsActivity();
            ApplicationLoader.fragmentsStack.add(fragment4);
            fragment4.onFragmentCreate();
            getSupportFragmentManager().beginTransaction().replace(C0419R.id.container, fragment4, "settings").commitAllowingStateLoss();
            pushOpened = true;
        }
        if (!pushOpened) {
            fragment = (BaseFragment) ApplicationLoader.fragmentsStack.get(ApplicationLoader.fragmentsStack.size() - 1);
            getSupportFragmentManager().beginTransaction().replace(C0419R.id.container, fragment, fragment.getTag()).commitAllowingStateLoss();
        }
        getWindow().setBackgroundDrawableResource(C0419R.drawable.transparent);
        getWindow().setFormat(4);
    }

    private void prepareForHideShowActionBar() {
        try {
            Class firstClass = getSupportActionBar().getClass();
            Class aClass = firstClass.getSuperclass();
            if (aClass == ActionBar.class) {
                firstClass.getDeclaredMethod("setShowHideAnimationEnabled", new Class[]{Boolean.TYPE}).invoke(getSupportActionBar(), new Object[]{Boolean.valueOf(false)});
                return;
            }
            Field field = aClass.getDeclaredField("mActionBar");
            field.setAccessible(true);
            field.get(getSupportActionBar()).getClass().getDeclaredMethod("setShowHideAnimationEnabled", new Class[]{Boolean.TYPE}).invoke(field.get(getSupportActionBar()), new Object[]{Boolean.valueOf(false)});
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
    }

    public void showActionBar() {
        prepareForHideShowActionBar();
        getSupportActionBar().show();
    }

    public void hideActionBar() {
        prepareForHideShowActionBar();
        getSupportActionBar().hide();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.photoPath = (String) NotificationCenter.Instance.getFromMemCache(533);
        this.videoPath = (String) NotificationCenter.Instance.getFromMemCache(534);
        this.sendingText = (String) NotificationCenter.Instance.getFromMemCache(535);
        if (!(this.videoPath == null && this.photoPath == null && this.sendingText == null)) {
            MessagesActivity fragment = new MessagesActivity();
            fragment.selectAlertString = C0419R.string.ForwardMessagesTo;
            fragment.animationType = 1;
            Bundle args = new Bundle();
            args.putBoolean("onlySelect", true);
            fragment.setArguments(args);
            fragment.delegate = this;
            ApplicationLoader.fragmentsStack.add(fragment);
            fragment.onFragmentCreate();
            getSupportFragmentManager().beginTransaction().replace(C0419R.id.container, fragment, fragment.getTag()).commitAllowingStateLoss();
        }
        Integer push_user_id = (Integer) NotificationCenter.Instance.getFromMemCache("push_user_id", Integer.valueOf(0));
        Integer push_chat_id = (Integer) NotificationCenter.Instance.getFromMemCache("push_chat_id", Integer.valueOf(0));
        Integer push_enc_id = (Integer) NotificationCenter.Instance.getFromMemCache("push_enc_id", Integer.valueOf(0));
        Integer open_settings = (Integer) NotificationCenter.Instance.getFromMemCache("open_settings", Integer.valueOf(0));
        ChatActivity fragment2;
        Bundle bundle;
        if (push_user_id.intValue() != 0) {
            if (push_user_id.intValue() == UserConfig.clientUserId) {
                open_settings = Integer.valueOf(1);
            } else {
                fragment2 = new ChatActivity();
                bundle = new Bundle();
                bundle.putInt("user_id", push_user_id.intValue());
                fragment2.setArguments(bundle);
                if (fragment2.onFragmentCreate()) {
                    ApplicationLoader.fragmentsStack.add(fragment2);
                    getSupportFragmentManager().beginTransaction().replace(C0419R.id.container, fragment2, "chat" + Math.random()).commitAllowingStateLoss();
                }
            }
        } else if (push_chat_id.intValue() != 0) {
            fragment2 = new ChatActivity();
            bundle = new Bundle();
            bundle.putInt("chat_id", push_chat_id.intValue());
            fragment2.setArguments(bundle);
            if (fragment2.onFragmentCreate()) {
                ApplicationLoader.fragmentsStack.add(fragment2);
                getSupportFragmentManager().beginTransaction().replace(C0419R.id.container, fragment2, "chat" + Math.random()).commitAllowingStateLoss();
            }
        } else if (push_enc_id.intValue() != 0) {
            fragment2 = new ChatActivity();
            bundle = new Bundle();
            bundle.putInt("enc_id", push_enc_id.intValue());
            fragment2.setArguments(bundle);
            if (fragment2.onFragmentCreate()) {
                ApplicationLoader.fragmentsStack.add(fragment2);
                getSupportFragmentManager().beginTransaction().replace(C0419R.id.container, fragment2, "chat" + Math.random()).commitAllowingStateLoss();
            }
        }
        if (open_settings.intValue() != 0) {
            SettingsActivity fragment3 = new SettingsActivity();
            ApplicationLoader.fragmentsStack.add(fragment3);
            fragment3.onFragmentCreate();
            getSupportFragmentManager().beginTransaction().replace(C0419R.id.container, fragment3, "settings").commitAllowingStateLoss();
        }
    }

    public void didSelectDialog(MessagesActivity messageFragment, long dialog_id) {
        if (dialog_id != 0) {
            int lower_part = (int) dialog_id;
            ChatActivity fragment = new ChatActivity();
            Bundle bundle = new Bundle();
            if (lower_part == 0) {
                NotificationCenter.Instance.postNotificationName(5, new Object[0]);
                bundle.putInt("enc_id", (int) (dialog_id >> 32));
                fragment.setArguments(bundle);
                fragment.scrollToTopOnResume = true;
                presentFragment(fragment, "chat" + Math.random(), true, false);
            } else if (lower_part > 0) {
                NotificationCenter.Instance.postNotificationName(5, new Object[0]);
                bundle.putInt("user_id", lower_part);
                fragment.setArguments(bundle);
                fragment.scrollToTopOnResume = true;
                presentFragment(fragment, "chat" + Math.random(), true, false);
            } else if (lower_part < 0) {
                NotificationCenter.Instance.postNotificationName(5, new Object[0]);
                bundle.putInt("chat_id", -lower_part);
                fragment.setArguments(bundle);
                fragment.scrollToTopOnResume = true;
                presentFragment(fragment, "chat" + Math.random(), true, false);
            }
            if (this.photoPath != null) {
                fragment.processSendingPhoto(this.photoPath);
            } else if (this.videoPath != null) {
                fragment.processSendingVideo(this.videoPath);
            } else if (this.sendingText != null) {
                fragment.processSendingText(this.sendingText);
            }
            this.photoPath = null;
            this.videoPath = null;
            this.sendingText = null;
        }
    }

    private void checkForCrashes() {
        CrashManager.register(this, ConnectionsManager.HOCKEY_APP_HASH);
    }

    private void checkForUpdates() {
        if (ConnectionsManager.DEBUG_VERSION) {
            UpdateManager.register(this, ConnectionsManager.HOCKEY_APP_HASH);
        }
    }

    protected void onPause() {
        super.onPause();
        ApplicationLoader.lastPauseTime = System.currentTimeMillis();
        if (this.notificationView != null) {
            this.notificationView.hide(false);
        }
        View focusView = getCurrentFocus();
        if (focusView instanceof EditText) {
            focusView.clearFocus();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        processOnFinish();
    }

    protected void onResume() {
        super.onResume();
        if (this.notificationView == null && getLayoutInflater() != null) {
            this.notificationView = (NotificationView) getLayoutInflater().inflate(C0419R.layout.notification_layout, null);
        }
        fixLayout();
        checkForCrashes();
        checkForUpdates();
        ApplicationLoader.resetLastPauseTime();
        supportInvalidateOptionsMenu();
        updateActionBar();
        try {
            ((NotificationManager) getSystemService("notification")).cancel(1);
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
    }

    private void processOnFinish() {
        if (!this.finished) {
            this.finished = true;
            NotificationCenter.Instance.removeObserver(this, 1234);
            NotificationCenter.Instance.removeObserver(this, 658);
            NotificationCenter.Instance.removeObserver(this, 701);
            NotificationCenter.Instance.removeObserver(this, 702);
            NotificationCenter.Instance.removeObserver(this, 703);
            NotificationCenter.Instance.removeObserver(this, GalleryImageViewer.needShowAllMedia);
            if (this.notificationView != null) {
                this.notificationView.hide(false);
                this.notificationView.destroy();
                this.notificationView = null;
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        fixLayout();
    }

    private void fixLayout() {
        if (this.containerView != null) {
            this.containerView.getViewTreeObserver().addOnGlobalLayoutListener(new C04392());
        }
    }

    public void didReceivedNotification(int id, Object... args) {
        if (id == 1234) {
            Iterator i$ = ApplicationLoader.fragmentsStack.iterator();
            while (i$.hasNext()) {
                ((BaseFragment) i$.next()).onFragmentDestroy();
            }
            ApplicationLoader.fragmentsStack.clear();
            startActivity(new Intent(this, LaunchActivity.class));
            processOnFinish();
            finish();
        } else if (id == GalleryImageViewer.needShowAllMedia) {
            long dialog_id = ((Long) args[0]).longValue();
            MediaActivity fragment = new MediaActivity();
            bundle = new Bundle();
            if (dialog_id != 0) {
                bundle.putLong("dialog_id", dialog_id);
                fragment.setArguments(bundle);
                presentFragment(fragment, "media_" + dialog_id, false);
            }
        } else if (id == 658) {
            Integer push_user_id = (Integer) NotificationCenter.Instance.getFromMemCache("push_user_id", Integer.valueOf(0));
            Integer push_chat_id = (Integer) NotificationCenter.Instance.getFromMemCache("push_chat_id", Integer.valueOf(0));
            Integer push_enc_id = (Integer) NotificationCenter.Instance.getFromMemCache("push_enc_id", Integer.valueOf(0));
            ChatActivity fragment2;
            if (push_user_id.intValue() != 0) {
                NotificationCenter.Instance.postNotificationName(5, new Object[0]);
                fragment2 = new ChatActivity();
                bundle = new Bundle();
                bundle.putInt("user_id", push_user_id.intValue());
                fragment2.setArguments(bundle);
                if (fragment2.onFragmentCreate()) {
                    if (ApplicationLoader.fragmentsStack.size() > 0) {
                        ((BaseFragment) ApplicationLoader.fragmentsStack.get(ApplicationLoader.fragmentsStack.size() - 1)).willBeHidden();
                    }
                    ApplicationLoader.fragmentsStack.add(fragment2);
                    getSupportFragmentManager().beginTransaction().replace(C0419R.id.container, fragment2, "chat" + Math.random()).commitAllowingStateLoss();
                }
            } else if (push_chat_id.intValue() != 0) {
                NotificationCenter.Instance.postNotificationName(5, new Object[0]);
                fragment2 = new ChatActivity();
                bundle = new Bundle();
                bundle.putInt("chat_id", push_chat_id.intValue());
                fragment2.setArguments(bundle);
                if (fragment2.onFragmentCreate()) {
                    if (ApplicationLoader.fragmentsStack.size() > 0) {
                        ((BaseFragment) ApplicationLoader.fragmentsStack.get(ApplicationLoader.fragmentsStack.size() - 1)).willBeHidden();
                    }
                    ApplicationLoader.fragmentsStack.add(fragment2);
                    getSupportFragmentManager().beginTransaction().replace(C0419R.id.container, fragment2, "chat" + Math.random()).commitAllowingStateLoss();
                }
            } else if (push_enc_id.intValue() != 0) {
                NotificationCenter.Instance.postNotificationName(5, new Object[0]);
                fragment2 = new ChatActivity();
                bundle = new Bundle();
                bundle.putInt("enc_id", push_enc_id.intValue());
                fragment2.setArguments(bundle);
                if (fragment2.onFragmentCreate()) {
                    if (ApplicationLoader.fragmentsStack.size() > 0) {
                        ((BaseFragment) ApplicationLoader.fragmentsStack.get(ApplicationLoader.fragmentsStack.size() - 1)).willBeHidden();
                    }
                    ApplicationLoader.fragmentsStack.add(fragment2);
                    getSupportFragmentManager().beginTransaction().replace(C0419R.id.container, fragment2, "chat" + Math.random()).commitAllowingStateLoss();
                }
            }
        } else if (id == 701) {
            if (this.notificationView != null) {
                this.notificationView.show(args[0]);
            }
        } else if (id == 702) {
            if (args[0] != this) {
                processOnFinish();
            }
        } else if (id == 703) {
            int state = ((Integer) args[0]).intValue();
            if (this.currentConnectionState != state) {
                FileLog.m800e("tmessages", "switch to state " + state);
                this.currentConnectionState = state;
                updateActionBar();
            }
        }
    }

    public void fixBackButton() {
        if (VERSION.SDK_INT == 19) {
            try {
                Class aClass = getSupportActionBar().getClass().getSuperclass();
                if (aClass != ActionBar.class) {
                    Field field = aClass.getDeclaredField("mActionBar");
                    field.setAccessible(true);
                    android.app.ActionBar bar = (android.app.ActionBar) field.get(getSupportActionBar());
                    field = bar.getClass().getDeclaredField("mActionView");
                    field.setAccessible(true);
                    View v = (View) field.get(bar);
                    field = v.getClass().getDeclaredField("mHomeLayout");
                    field.setAccessible(true);
                    ((View) field.get(v)).setVisibility(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            BaseFragment currentFragment = null;
            if (!ApplicationLoader.fragmentsStack.isEmpty()) {
                currentFragment = (BaseFragment) ApplicationLoader.fragmentsStack.get(ApplicationLoader.fragmentsStack.size() - 1);
            }
            boolean canApplyLoading = true;
            if (currentFragment != null && (this.currentConnectionState == 0 || !currentFragment.canApplyUpdateStatus() || this.statusView == null)) {
                currentFragment.applySelfActionBar();
                canApplyLoading = false;
            }
            if (canApplyLoading && this.statusView != null) {
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayShowHomeEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setSubtitle(null);
                if (ApplicationLoader.fragmentsStack.size() > 1) {
                    this.backStatusButton.setVisibility(0);
                    this.statusBackground.setEnabled(true);
                } else {
                    this.backStatusButton.setVisibility(8);
                    this.statusBackground.setEnabled(false);
                }
                if (this.currentConnectionState == 1) {
                    this.statusText.setText(getString(C0419R.string.WaitingForNetwork));
                } else if (this.currentConnectionState == 2) {
                    this.statusText.setText(getString(C0419R.string.Connecting));
                } else if (this.currentConnectionState == 3) {
                    this.statusText.setText(getString(C0419R.string.Updating));
                }
                if (actionBar.getCustomView() != this.statusView) {
                    actionBar.setCustomView(this.statusView);
                }
                try {
                    if (this.statusView.getLayoutParams() instanceof LayoutParams) {
                        LayoutParams statusParams = (LayoutParams) this.statusView.getLayoutParams();
                        this.statusText.measure(MeasureSpec.makeMeasureSpec(800, ExploreByTouchHelper.INVALID_ID), MeasureSpec.makeMeasureSpec(100, ExploreByTouchHelper.INVALID_ID));
                        statusParams.width = this.statusText.getMeasuredWidth() + Utilities.dp(54);
                        this.statusView.setLayoutParams(statusParams);
                    } else if (this.statusView.getLayoutParams() instanceof android.app.ActionBar.LayoutParams) {
                        android.app.ActionBar.LayoutParams statusParams2 = (android.app.ActionBar.LayoutParams) this.statusView.getLayoutParams();
                        this.statusText.measure(MeasureSpec.makeMeasureSpec(800, ExploreByTouchHelper.INVALID_ID), MeasureSpec.makeMeasureSpec(100, ExploreByTouchHelper.INVALID_ID));
                        statusParams2.width = this.statusText.getMeasuredWidth() + Utilities.dp(54);
                        this.statusView.setLayoutParams(statusParams2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void presentFragment(BaseFragment fragment, String tag, boolean bySwipe) {
        presentFragment(fragment, tag, false, bySwipe);
    }

    public void presentFragment(BaseFragment fragment, String tag, boolean removeLast, boolean bySwipe) {
        if (getCurrentFocus() != null) {
            Utilities.hideKeyboard(getCurrentFocus());
        }
        if (fragment.onFragmentCreate()) {
            BaseFragment current = null;
            if (!ApplicationLoader.fragmentsStack.isEmpty()) {
                current = (BaseFragment) ApplicationLoader.fragmentsStack.get(ApplicationLoader.fragmentsStack.size() - 1);
            }
            if (current != null) {
                current.willBeHidden();
            }
            FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
            if (removeLast && current != null) {
                ApplicationLoader.fragmentsStack.remove(ApplicationLoader.fragmentsStack.size() - 1);
                current.onFragmentDestroy();
            }
            if (ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).getBoolean("view_animations", true)) {
                if (bySwipe) {
                    fTrans.setCustomAnimations(C0419R.anim.slide_left, C0419R.anim.no_anim);
                } else {
                    fTrans.setCustomAnimations(C0419R.anim.scale_in, C0419R.anim.no_anim);
                }
            }
            fTrans.replace(C0419R.id.container, fragment, tag);
            fTrans.commitAllowingStateLoss();
            ApplicationLoader.fragmentsStack.add(fragment);
        }
    }

    public void removeFromStack(BaseFragment fragment) {
        ApplicationLoader.fragmentsStack.remove(fragment);
        fragment.onFragmentDestroy();
    }

    public void finishFragment(boolean bySwipe) {
        if (getCurrentFocus() != null) {
            Utilities.hideKeyboard(getCurrentFocus());
        }
        if (ApplicationLoader.fragmentsStack.size() < 2) {
            Iterator i$ = ApplicationLoader.fragmentsStack.iterator();
            while (i$.hasNext()) {
                ((BaseFragment) i$.next()).onFragmentDestroy();
            }
            ApplicationLoader.fragmentsStack.clear();
            MessagesActivity fragment = new MessagesActivity();
            fragment.onFragmentCreate();
            ApplicationLoader.fragmentsStack.add(fragment);
            getSupportFragmentManager().beginTransaction().replace(C0419R.id.container, fragment, "chats").commitAllowingStateLoss();
            return;
        }
        ((BaseFragment) ApplicationLoader.fragmentsStack.get(ApplicationLoader.fragmentsStack.size() - 1)).onFragmentDestroy();
        BaseFragment prev = (BaseFragment) ApplicationLoader.fragmentsStack.get(ApplicationLoader.fragmentsStack.size() - 2);
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        if (ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).getBoolean("view_animations", true)) {
            if (bySwipe) {
                fTrans.setCustomAnimations(C0419R.anim.no_anim_show, C0419R.anim.slide_right_away);
            } else {
                fTrans.setCustomAnimations(C0419R.anim.no_anim_show, C0419R.anim.scale_out);
            }
        }
        fTrans.replace(C0419R.id.container, prev, prev.getTag());
        fTrans.commitAllowingStateLoss();
        ApplicationLoader.fragmentsStack.remove(ApplicationLoader.fragmentsStack.size() - 1);
    }

    public void onBackPressed() {
        if (ApplicationLoader.fragmentsStack.size() == 1) {
            ((BaseFragment) ApplicationLoader.fragmentsStack.get(0)).onFragmentDestroy();
            ApplicationLoader.fragmentsStack.clear();
            processOnFinish();
            finish();
        } else if (!ApplicationLoader.fragmentsStack.isEmpty() && ((BaseFragment) ApplicationLoader.fragmentsStack.get(ApplicationLoader.fragmentsStack.size() - 1)).onBackPressed()) {
            finishFragment(false);
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        try {
            super.onSaveInstanceState(outState);
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
    }
}
