package org.telegram.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager.TaskDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.StatFs;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.C0488R;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ContactsController.Contact;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocaleController.LocaleInfo;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SendMessagesHelper.SendingMediaInfo;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.camera.CameraController;
import org.telegram.messenger.exoplayer2.trackselection.AdaptiveTrackSelection;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatInvite;
import org.telegram.tgnet.TLRPC.LangPackString;
import org.telegram.tgnet.TLRPC.MessageMedia;
import org.telegram.tgnet.TLRPC.TL_contacts_resolveUsername;
import org.telegram.tgnet.TLRPC.TL_contacts_resolvedPeer;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputGameShortName;
import org.telegram.tgnet.TLRPC.TL_inputMediaGame;
import org.telegram.tgnet.TLRPC.TL_inputStickerSetShortName;
import org.telegram.tgnet.TLRPC.TL_langpack_getStrings;
import org.telegram.tgnet.TLRPC.TL_messages_checkChatInvite;
import org.telegram.tgnet.TLRPC.TL_messages_importChatInvite;
import org.telegram.tgnet.TLRPC.TL_webPage;
import org.telegram.tgnet.TLRPC.Updates;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.Vector;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.Theme.ThemeInfo;
import org.telegram.ui.Adapters.DrawerLayoutAdapter;
import org.telegram.ui.Cells.DrawerAddCell;
import org.telegram.ui.Cells.DrawerUserCell;
import org.telegram.ui.Cells.LanguageCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.EmbedBottomSheet;
import org.telegram.ui.Components.JoinGroupAlert;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PasscodeView;
import org.telegram.ui.Components.PasscodeView.PasscodeViewDelegate;
import org.telegram.ui.Components.PipRoundVideoView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.ThemeEditorView;
import org.telegram.ui.DialogsActivity.DialogsActivityDelegate;
import org.telegram.ui.LocationActivity.LocationActivityDelegate;

public class LaunchActivity extends Activity implements NotificationCenterDelegate, ActionBarLayoutDelegate, DialogsActivityDelegate {
    private static ArrayList<BaseFragment> layerFragmentsStack = new ArrayList();
    private static ArrayList<BaseFragment> mainFragmentsStack = new ArrayList();
    private static ArrayList<BaseFragment> rightFragmentsStack = new ArrayList();
    private ActionBarLayout actionBarLayout;
    private View backgroundTablet;
    private ArrayList<User> contactsToSend;
    private int currentAccount;
    private int currentConnectionState;
    private String documentsMimeType;
    private ArrayList<String> documentsOriginalPathsArray;
    private ArrayList<String> documentsPathsArray;
    private ArrayList<Uri> documentsUrisArray;
    private DrawerLayoutAdapter drawerLayoutAdapter;
    protected DrawerLayoutContainer drawerLayoutContainer;
    private HashMap<String, String> englishLocaleStrings;
    private boolean finished;
    private ActionBarLayout layersActionBarLayout;
    private boolean loadingLocaleDialog;
    private AlertDialog localeDialog;
    private Runnable lockRunnable;
    private OnGlobalLayoutListener onGlobalLayoutListener;
    private Intent passcodeSaveIntent;
    private boolean passcodeSaveIntentIsNew;
    private boolean passcodeSaveIntentIsRestore;
    private PasscodeView passcodeView;
    private ArrayList<SendingMediaInfo> photoPathsArray;
    private ActionBarLayout rightActionBarLayout;
    private String sendingText;
    private FrameLayout shadowTablet;
    private FrameLayout shadowTabletSide;
    private RecyclerListView sideMenu;
    private HashMap<String, String> systemLocaleStrings;
    private boolean tabletFullSize;
    private String videoPath;
    private AlertDialog visibleDialog;

    class C18852 implements OnTouchListener {
        C18852() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (LaunchActivity.this.actionBarLayout.fragmentsStack.isEmpty() || event.getAction() != 1) {
                return false;
            }
            float x = event.getX();
            float y = event.getY();
            int[] location = new int[2];
            LaunchActivity.this.layersActionBarLayout.getLocationOnScreen(location);
            int viewX = location[0];
            int viewY = location[1];
            if (LaunchActivity.this.layersActionBarLayout.checkTransitionAnimation() || (x > ((float) viewX) && x < ((float) (LaunchActivity.this.layersActionBarLayout.getWidth() + viewX)) && y > ((float) viewY) && y < ((float) (LaunchActivity.this.layersActionBarLayout.getHeight() + viewY)))) {
                return false;
            }
            if (!LaunchActivity.this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                int a = 0;
                while (LaunchActivity.this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                    LaunchActivity.this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) LaunchActivity.this.layersActionBarLayout.fragmentsStack.get(0));
                    a = (a - 1) + 1;
                }
                LaunchActivity.this.layersActionBarLayout.closeLastFragment(true);
            }
            return true;
        }
    }

    class C18873 implements OnClickListener {
        C18873() {
        }

        public void onClick(View v) {
        }
    }

    class C18884 implements OnItemClickListener {
        C18884() {
        }

        public void onItemClick(View view, int position) {
            boolean z = false;
            if (position == 0) {
                DrawerLayoutAdapter access$700 = LaunchActivity.this.drawerLayoutAdapter;
                if (!LaunchActivity.this.drawerLayoutAdapter.isAccountsShowed()) {
                    z = true;
                }
                access$700.setAccountsShowed(z, true);
            } else if (view instanceof DrawerUserCell) {
                LaunchActivity.this.switchToAccount(((DrawerUserCell) view).getAccountNumber(), true);
                LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
            } else if (view instanceof DrawerAddCell) {
                int freeAccount = -1;
                for (int a = 0; a < 3; a++) {
                    if (!UserConfig.getInstance(a).isClientActivated()) {
                        freeAccount = a;
                        break;
                    }
                }
                if (freeAccount >= 0) {
                    LaunchActivity.this.presentFragment(new LoginActivity(freeAccount));
                }
                LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
            } else {
                int id = LaunchActivity.this.drawerLayoutAdapter.getId(position);
                if (id == 2) {
                    LaunchActivity.this.presentFragment(new GroupCreateActivity());
                    LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                } else if (id == 3) {
                    args = new Bundle();
                    args.putBoolean("onlyUsers", true);
                    args.putBoolean("destroyAfterSelect", true);
                    args.putBoolean("createSecretChat", true);
                    args.putBoolean("allowBots", false);
                    LaunchActivity.this.presentFragment(new ContactsActivity(args));
                    LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                } else if (id == 4) {
                    SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                    if (BuildVars.DEBUG_VERSION || !preferences.getBoolean("channel_intro", false)) {
                        LaunchActivity.this.presentFragment(new ChannelIntroActivity());
                        preferences.edit().putBoolean("channel_intro", true).commit();
                    } else {
                        args = new Bundle();
                        args.putInt("step", 0);
                        LaunchActivity.this.presentFragment(new ChannelCreateActivity(args));
                    }
                    LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                } else if (id == 6) {
                    LaunchActivity.this.presentFragment(new ContactsActivity(null));
                    LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                } else if (id == 7) {
                    LaunchActivity.this.presentFragment(new InviteContactsActivity());
                    LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                } else if (id == 8) {
                    LaunchActivity.this.presentFragment(new SettingsActivity());
                    LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                } else if (id == 9) {
                    Browser.openUrl(LaunchActivity.this, LocaleController.getString("TelegramFaqUrl", C0488R.string.TelegramFaqUrl));
                    LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                } else if (id == 10) {
                    LaunchActivity.this.presentFragment(new CallLogActivity());
                    LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                } else if (id == 11) {
                    args = new Bundle();
                    args.putInt("user_id", UserConfig.getInstance(LaunchActivity.this.currentAccount).getClientUserId());
                    LaunchActivity.this.presentFragment(new ChatActivity(args));
                    LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                }
            }
        }
    }

    class C18906 implements PasscodeViewDelegate {
        C18906() {
        }

        public void didAcceptedPassword() {
            SharedConfig.isWaitingForPasscodeEnter = false;
            if (LaunchActivity.this.passcodeSaveIntent != null) {
                LaunchActivity.this.handleIntent(LaunchActivity.this.passcodeSaveIntent, LaunchActivity.this.passcodeSaveIntentIsNew, LaunchActivity.this.passcodeSaveIntentIsRestore, true);
                LaunchActivity.this.passcodeSaveIntent = null;
            }
            LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            LaunchActivity.this.actionBarLayout.showLastFragment();
            if (AndroidUtilities.isTablet()) {
                LaunchActivity.this.layersActionBarLayout.showLastFragment();
                LaunchActivity.this.rightActionBarLayout.showLastFragment();
            }
        }
    }

    private class VcardData {
        String name;
        ArrayList<String> phones;

        private VcardData() {
            this.phones = new ArrayList();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onCreate(android.os.Bundle r38) {
        /*
        r37 = this;
        org.telegram.messenger.ApplicationLoader.postInitApplication();
        r32 = r37.getResources();
        r32 = r32.getConfiguration();
        r0 = r37;
        r1 = r32;
        org.telegram.messenger.AndroidUtilities.checkDisplaySize(r0, r1);
        r32 = org.telegram.messenger.UserConfig.selectedAccount;
        r0 = r32;
        r1 = r37;
        r1.currentAccount = r0;
        r0 = r37;
        r0 = r0.currentAccount;
        r32 = r0;
        r32 = org.telegram.messenger.UserConfig.getInstance(r32);
        r32 = r32.isClientActivated();
        if (r32 != 0) goto L_0x00dc;
    L_0x002a:
        r19 = r37.getIntent();
        if (r19 == 0) goto L_0x0057;
    L_0x0030:
        r32 = r19.getAction();
        if (r32 == 0) goto L_0x0057;
    L_0x0036:
        r32 = "android.intent.action.SEND";
        r33 = r19.getAction();
        r32 = r32.equals(r33);
        if (r32 != 0) goto L_0x0050;
    L_0x0043:
        r32 = r19.getAction();
        r33 = "android.intent.action.SEND_MULTIPLE";
        r32 = r32.equals(r33);
        if (r32 == 0) goto L_0x0057;
    L_0x0050:
        super.onCreate(r38);
        r37.finish();
    L_0x0056:
        return;
    L_0x0057:
        r25 = org.telegram.messenger.MessagesController.getGlobalMainSettings();
        r32 = "intro_crashed_time";
        r34 = 0;
        r0 = r25;
        r1 = r32;
        r2 = r34;
        r10 = r0.getLong(r1, r2);
        r32 = "fromIntro";
        r33 = 0;
        r0 = r19;
        r1 = r32;
        r2 = r33;
        r17 = r0.getBooleanExtra(r1, r2);
        if (r17 == 0) goto L_0x008b;
    L_0x007b:
        r32 = r25.edit();
        r33 = "intro_crashed_time";
        r34 = 0;
        r32 = r32.putLong(r33, r34);
        r32.commit();
    L_0x008b:
        r32 = java.lang.System.currentTimeMillis();
        r32 = r10 - r32;
        r32 = java.lang.Math.abs(r32);
        r34 = 120000; // 0x1d4c0 float:1.68156E-40 double:5.9288E-319;
        r32 = (r32 > r34 ? 1 : (r32 == r34 ? 0 : -1));
        if (r32 < 0) goto L_0x00dc;
    L_0x009c:
        if (r19 == 0) goto L_0x00dc;
    L_0x009e:
        if (r17 != 0) goto L_0x00dc;
    L_0x00a0:
        r32 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r33 = "logininfo2";
        r34 = 0;
        r25 = r32.getSharedPreferences(r33, r34);
        r30 = r25.getAll();
        r32 = r30.isEmpty();
        if (r32 == 0) goto L_0x00dc;
    L_0x00b5:
        r20 = new android.content.Intent;
        r32 = org.telegram.ui.IntroActivity.class;
        r0 = r20;
        r1 = r37;
        r2 = r32;
        r0.<init>(r1, r2);
        r32 = r19.getData();
        r0 = r20;
        r1 = r32;
        r0.setData(r1);
        r0 = r37;
        r1 = r20;
        r0.startActivity(r1);
        super.onCreate(r38);
        r37.finish();
        goto L_0x0056;
    L_0x00dc:
        r32 = 1;
        r0 = r37;
        r1 = r32;
        r0.requestWindowFeature(r1);
        r32 = 2131558412; // 0x7f0d000c float:1.874214E38 double:1.0531297835E-314;
        r0 = r37;
        r1 = r32;
        r0.setTheme(r1);
        r32 = android.os.Build.VERSION.SDK_INT;
        r33 = 21;
        r0 = r32;
        r1 = r33;
        if (r0 < r1) goto L_0x0114;
    L_0x00f9:
        r32 = new android.app.ActivityManager$TaskDescription;	 Catch:{ Exception -> 0x08de }
        r33 = 0;
        r34 = 0;
        r35 = "actionBarDefault";
        r35 = org.telegram.ui.ActionBar.Theme.getColor(r35);	 Catch:{ Exception -> 0x08de }
        r36 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r35 = r35 | r36;
        r32.<init>(r33, r34, r35);	 Catch:{ Exception -> 0x08de }
        r0 = r37;
        r1 = r32;
        r0.setTaskDescription(r1);	 Catch:{ Exception -> 0x08de }
    L_0x0114:
        r32 = r37.getWindow();
        r33 = 2131165683; // 0x7f0701f3 float:1.794559E38 double:1.0529357496E-314;
        r32.setBackgroundDrawableResource(r33);
        r32 = org.telegram.messenger.SharedConfig.passcodeHash;
        r32 = r32.length();
        if (r32 <= 0) goto L_0x0135;
    L_0x0126:
        r32 = org.telegram.messenger.SharedConfig.allowScreenCapture;
        if (r32 != 0) goto L_0x0135;
    L_0x012a:
        r32 = r37.getWindow();	 Catch:{ Exception -> 0x0633 }
        r33 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r34 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r32.setFlags(r33, r34);	 Catch:{ Exception -> 0x0633 }
    L_0x0135:
        super.onCreate(r38);
        r32 = android.os.Build.VERSION.SDK_INT;
        r33 = 24;
        r0 = r32;
        r1 = r33;
        if (r0 < r1) goto L_0x0148;
    L_0x0142:
        r32 = r37.isInMultiWindowMode();
        org.telegram.messenger.AndroidUtilities.isInMultiwindow = r32;
    L_0x0148:
        r32 = 0;
        r0 = r37;
        r1 = r32;
        org.telegram.ui.ActionBar.Theme.createChatResources(r0, r1);
        r32 = org.telegram.messenger.SharedConfig.passcodeHash;
        r32 = r32.length();
        if (r32 == 0) goto L_0x016d;
    L_0x0159:
        r32 = org.telegram.messenger.SharedConfig.appLocked;
        if (r32 == 0) goto L_0x016d;
    L_0x015d:
        r0 = r37;
        r0 = r0.currentAccount;
        r32 = r0;
        r32 = org.telegram.tgnet.ConnectionsManager.getInstance(r32);
        r32 = r32.getCurrentTime();
        org.telegram.messenger.SharedConfig.lastPauseTime = r32;
    L_0x016d:
        r32 = r37.getResources();
        r33 = "status_bar_height";
        r34 = "dimen";
        r35 = "android";
        r27 = r32.getIdentifier(r33, r34, r35);
        if (r27 <= 0) goto L_0x018e;
    L_0x0180:
        r32 = r37.getResources();
        r0 = r32;
        r1 = r27;
        r32 = r0.getDimensionPixelSize(r1);
        org.telegram.messenger.AndroidUtilities.statusBarHeight = r32;
    L_0x018e:
        r32 = new org.telegram.ui.ActionBar.ActionBarLayout;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.actionBarLayout = r0;
        r32 = new org.telegram.ui.ActionBar.DrawerLayoutContainer;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.drawerLayoutContainer = r0;
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r33 = new android.view.ViewGroup$LayoutParams;
        r34 = -1;
        r35 = -1;
        r33.<init>(r34, r35);
        r0 = r37;
        r1 = r32;
        r2 = r33;
        r0.setContentView(r1, r2);
        r32 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r32 == 0) goto L_0x0641;
    L_0x01ca:
        r32 = r37.getWindow();
        r33 = 16;
        r32.setSoftInputMode(r33);
        r21 = new org.telegram.ui.LaunchActivity$1;
        r0 = r21;
        r1 = r37;
        r2 = r37;
        r0.<init>(r2);
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r33 = -1;
        r34 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r33 = org.telegram.ui.Components.LayoutHelper.createFrame(r33, r34);
        r0 = r32;
        r1 = r21;
        r2 = r33;
        r0.addView(r1, r2);
        r32 = new android.view.View;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.backgroundTablet = r0;
        r32 = r37.getResources();
        r33 = 2131165266; // 0x7f070052 float:1.7944744E38 double:1.0529355435E-314;
        r13 = r32.getDrawable(r33);
        r13 = (android.graphics.drawable.BitmapDrawable) r13;
        r32 = android.graphics.Shader.TileMode.REPEAT;
        r33 = android.graphics.Shader.TileMode.REPEAT;
        r0 = r32;
        r1 = r33;
        r13.setTileModeXY(r0, r1);
        r0 = r37;
        r0 = r0.backgroundTablet;
        r32 = r0;
        r0 = r32;
        r0.setBackgroundDrawable(r13);
        r0 = r37;
        r0 = r0.backgroundTablet;
        r32 = r0;
        r33 = -1;
        r34 = -1;
        r33 = org.telegram.ui.Components.LayoutHelper.createRelative(r33, r34);
        r0 = r21;
        r1 = r32;
        r2 = r33;
        r0.addView(r1, r2);
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r21;
        r1 = r32;
        r0.addView(r1);
        r32 = new org.telegram.ui.ActionBar.ActionBarLayout;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.rightActionBarLayout = r0;
        r0 = r37;
        r0 = r0.rightActionBarLayout;
        r32 = r0;
        r33 = rightFragmentsStack;
        r32.init(r33);
        r0 = r37;
        r0 = r0.rightActionBarLayout;
        r32 = r0;
        r0 = r32;
        r1 = r37;
        r0.setDelegate(r1);
        r0 = r37;
        r0 = r0.rightActionBarLayout;
        r32 = r0;
        r0 = r21;
        r1 = r32;
        r0.addView(r1);
        r32 = new android.widget.FrameLayout;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.shadowTabletSide = r0;
        r0 = r37;
        r0 = r0.shadowTabletSide;
        r32 = r0;
        r33 = 1076449908; // 0x40295274 float:2.6456575 double:5.31836919E-315;
        r32.setBackgroundColor(r33);
        r0 = r37;
        r0 = r0.shadowTabletSide;
        r32 = r0;
        r0 = r21;
        r1 = r32;
        r0.addView(r1);
        r32 = new android.widget.FrameLayout;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.shadowTablet = r0;
        r0 = r37;
        r0 = r0.shadowTablet;
        r33 = r0;
        r32 = layerFragmentsStack;
        r32 = r32.isEmpty();
        if (r32 == 0) goto L_0x0639;
    L_0x02c4:
        r32 = 8;
    L_0x02c6:
        r0 = r33;
        r1 = r32;
        r0.setVisibility(r1);
        r0 = r37;
        r0 = r0.shadowTablet;
        r32 = r0;
        r33 = 2130706432; // 0x7f000000 float:1.7014118E38 double:1.0527088494E-314;
        r32.setBackgroundColor(r33);
        r0 = r37;
        r0 = r0.shadowTablet;
        r32 = r0;
        r0 = r21;
        r1 = r32;
        r0.addView(r1);
        r0 = r37;
        r0 = r0.shadowTablet;
        r32 = r0;
        r33 = new org.telegram.ui.LaunchActivity$2;
        r0 = r33;
        r1 = r37;
        r0.<init>();
        r32.setOnTouchListener(r33);
        r0 = r37;
        r0 = r0.shadowTablet;
        r32 = r0;
        r33 = new org.telegram.ui.LaunchActivity$3;
        r0 = r33;
        r1 = r37;
        r0.<init>();
        r32.setOnClickListener(r33);
        r32 = new org.telegram.ui.ActionBar.ActionBarLayout;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.layersActionBarLayout = r0;
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r33 = 1;
        r32.setRemoveActionBarExtraHeight(r33);
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r0 = r37;
        r0 = r0.shadowTablet;
        r33 = r0;
        r32.setBackgroundView(r33);
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r33 = 1;
        r32.setUseAlphaAnimations(r33);
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r33 = 2131165251; // 0x7f070043 float:1.7944714E38 double:1.052935536E-314;
        r32.setBackgroundResource(r33);
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r33 = layerFragmentsStack;
        r32.init(r33);
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r0 = r32;
        r1 = r37;
        r0.setDelegate(r1);
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r33 = r0;
        r32.setDrawerLayoutContainer(r33);
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r33 = r0;
        r32 = layerFragmentsStack;
        r32 = r32.isEmpty();
        if (r32 == 0) goto L_0x063d;
    L_0x037e:
        r32 = 8;
    L_0x0380:
        r0 = r33;
        r1 = r32;
        r0.setVisibility(r1);
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r0 = r21;
        r1 = r32;
        r0.addView(r1);
    L_0x0394:
        r32 = new org.telegram.ui.Components.RecyclerListView;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.sideMenu = r0;
        r0 = r37;
        r0 = r0.sideMenu;
        r32 = r0;
        r32 = r32.getItemAnimator();
        r32 = (org.telegram.messenger.support.widget.DefaultItemAnimator) r32;
        r33 = 0;
        r32.setDelayAnimations(r33);
        r0 = r37;
        r0 = r0.sideMenu;
        r32 = r0;
        r33 = "chats_menuBackground";
        r33 = org.telegram.ui.ActionBar.Theme.getColor(r33);
        r32.setBackgroundColor(r33);
        r0 = r37;
        r0 = r0.sideMenu;
        r32 = r0;
        r33 = new org.telegram.messenger.support.widget.LinearLayoutManager;
        r34 = 1;
        r35 = 0;
        r0 = r33;
        r1 = r37;
        r2 = r34;
        r3 = r35;
        r0.<init>(r1, r2, r3);
        r32.setLayoutManager(r33);
        r0 = r37;
        r0 = r0.sideMenu;
        r32 = r0;
        r33 = new org.telegram.ui.Adapters.DrawerLayoutAdapter;
        r0 = r33;
        r1 = r37;
        r0.<init>(r1);
        r0 = r33;
        r1 = r37;
        r1.drawerLayoutAdapter = r0;
        r32.setAdapter(r33);
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r0 = r37;
        r0 = r0.sideMenu;
        r33 = r0;
        r32.setDrawerLayout(r33);
        r0 = r37;
        r0 = r0.sideMenu;
        r32 = r0;
        r22 = r32.getLayoutParams();
        r22 = (android.widget.FrameLayout.LayoutParams) r22;
        r28 = org.telegram.messenger.AndroidUtilities.getRealScreenSize();
        r32 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r32 == 0) goto L_0x065b;
    L_0x041b:
        r32 = 1134559232; // 0x43a00000 float:320.0 double:5.605467397E-315;
        r32 = org.telegram.messenger.AndroidUtilities.dp(r32);
    L_0x0421:
        r0 = r32;
        r1 = r22;
        r1.width = r0;
        r32 = -1;
        r0 = r32;
        r1 = r22;
        r1.height = r0;
        r0 = r37;
        r0 = r0.sideMenu;
        r32 = r0;
        r0 = r32;
        r1 = r22;
        r0.setLayoutParams(r1);
        r0 = r37;
        r0 = r0.sideMenu;
        r32 = r0;
        r33 = new org.telegram.ui.LaunchActivity$4;
        r0 = r33;
        r1 = r37;
        r0.<init>();
        r32.setOnItemClickListener(r33);
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r0 = r37;
        r0 = r0.actionBarLayout;
        r33 = r0;
        r32.setParentActionBarLayout(r33);
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r33 = r0;
        r32.setDrawerLayoutContainer(r33);
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r33 = mainFragmentsStack;
        r32.init(r33);
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r32;
        r1 = r37;
        r0.setDelegate(r1);
        org.telegram.ui.ActionBar.Theme.loadWallpaper();
        r32 = new org.telegram.ui.Components.PasscodeView;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.passcodeView = r0;
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r0 = r37;
        r0 = r0.passcodeView;
        r33 = r0;
        r34 = -1;
        r35 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r34 = org.telegram.ui.Components.LayoutHelper.createFrame(r34, r35);
        r32.addView(r33, r34);
        r37.checkCurrentAccount();
        r32 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r33 = org.telegram.messenger.NotificationCenter.closeOtherAppActivities;
        r34 = 1;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r34[r35] = r37;
        r32.postNotificationName(r33, r34);
        r0 = r37;
        r0 = r0.currentAccount;
        r32 = r0;
        r32 = org.telegram.tgnet.ConnectionsManager.getInstance(r32);
        r32 = r32.getConnectionState();
        r0 = r32;
        r1 = r37;
        r1.currentConnectionState = r0;
        r32 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r33 = org.telegram.messenger.NotificationCenter.reloadInterface;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r33 = org.telegram.messenger.NotificationCenter.suggestedLangpack;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r33 = org.telegram.messenger.NotificationCenter.didSetNewTheme;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r33 = org.telegram.messenger.NotificationCenter.needSetDayNightTheme;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r33 = org.telegram.messenger.NotificationCenter.closeOtherAppActivities;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r33 = org.telegram.messenger.NotificationCenter.didSetPasscode;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r33 = org.telegram.messenger.NotificationCenter.didSetNewWallpapper;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r33 = org.telegram.messenger.NotificationCenter.notificationsCountUpdated;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r32;
        r0 = r0.fragmentsStack;
        r32 = r0;
        r32 = r32.isEmpty();
        if (r32 == 0) goto L_0x07f8;
    L_0x0563:
        r0 = r37;
        r0 = r0.currentAccount;
        r32 = r0;
        r32 = org.telegram.messenger.UserConfig.getInstance(r32);
        r32 = r32.isClientActivated();
        if (r32 != 0) goto L_0x067f;
    L_0x0573:
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r33 = new org.telegram.ui.LoginActivity;
        r33.<init>();
        r32.addFragmentToStack(r33);
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r33 = 0;
        r34 = 0;
        r32.setAllowOpenDrawer(r33, r34);
    L_0x058e:
        if (r38 == 0) goto L_0x05b4;
    L_0x0590:
        r32 = "fragment";
        r0 = r38;
        r1 = r32;
        r16 = r0.getString(r1);	 Catch:{ Exception -> 0x0740 }
        if (r16 == 0) goto L_0x05b4;
    L_0x059d:
        r32 = "args";
        r0 = r38;
        r1 = r32;
        r7 = r0.getBundle(r1);	 Catch:{ Exception -> 0x0740 }
        r32 = -1;
        r33 = r16.hashCode();	 Catch:{ Exception -> 0x0740 }
        switch(r33) {
            case -1529105743: goto L_0x0713;
            case -1349522494: goto L_0x0702;
            case 3052376: goto L_0x06ad;
            case 3108362: goto L_0x06f1;
            case 98629247: goto L_0x06cf;
            case 738950403: goto L_0x06e0;
            case 1434631203: goto L_0x06be;
            default: goto L_0x05b1;
        };
    L_0x05b1:
        switch(r32) {
            case 0: goto L_0x0724;
            case 1: goto L_0x0746;
            case 2: goto L_0x0761;
            case 3: goto L_0x0783;
            case 4: goto L_0x079f;
            case 5: goto L_0x07bb;
            case 6: goto L_0x07dd;
            default: goto L_0x05b4;
        };
    L_0x05b4:
        r37.checkLayout();
        r33 = r37.getIntent();
        r34 = 0;
        if (r38 == 0) goto L_0x08ca;
    L_0x05bf:
        r32 = 1;
    L_0x05c1:
        r35 = 0;
        r0 = r37;
        r1 = r33;
        r2 = r34;
        r3 = r32;
        r4 = r35;
        r0.handleIntent(r1, r2, r3, r4);
        r23 = android.os.Build.DISPLAY;	 Catch:{ Exception -> 0x08d8 }
        r24 = android.os.Build.USER;	 Catch:{ Exception -> 0x08d8 }
        if (r23 == 0) goto L_0x08ce;
    L_0x05d6:
        r23 = r23.toLowerCase();	 Catch:{ Exception -> 0x08d8 }
    L_0x05da:
        if (r24 == 0) goto L_0x08d3;
    L_0x05dc:
        r24 = r23.toLowerCase();	 Catch:{ Exception -> 0x08d8 }
    L_0x05e0:
        r32 = "flyme";
        r0 = r23;
        r1 = r32;
        r32 = r0.contains(r1);	 Catch:{ Exception -> 0x08d8 }
        if (r32 != 0) goto L_0x05fa;
    L_0x05ed:
        r32 = "flyme";
        r0 = r24;
        r1 = r32;
        r32 = r0.contains(r1);	 Catch:{ Exception -> 0x08d8 }
        if (r32 == 0) goto L_0x0622;
    L_0x05fa:
        r32 = 1;
        org.telegram.messenger.AndroidUtilities.incorrectDisplaySizeFix = r32;	 Catch:{ Exception -> 0x08d8 }
        r32 = r37.getWindow();	 Catch:{ Exception -> 0x08d8 }
        r32 = r32.getDecorView();	 Catch:{ Exception -> 0x08d8 }
        r31 = r32.getRootView();	 Catch:{ Exception -> 0x08d8 }
        r32 = r31.getViewTreeObserver();	 Catch:{ Exception -> 0x08d8 }
        r33 = new org.telegram.ui.LaunchActivity$5;	 Catch:{ Exception -> 0x08d8 }
        r0 = r33;
        r1 = r37;
        r2 = r31;
        r0.<init>(r2);	 Catch:{ Exception -> 0x08d8 }
        r0 = r33;
        r1 = r37;
        r1.onGlobalLayoutListener = r0;	 Catch:{ Exception -> 0x08d8 }
        r32.addOnGlobalLayoutListener(r33);	 Catch:{ Exception -> 0x08d8 }
    L_0x0622:
        r32 = org.telegram.messenger.MediaController.getInstance();
        r33 = 1;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.setBaseActivity(r1, r2);
        goto L_0x0056;
    L_0x0633:
        r14 = move-exception;
        org.telegram.messenger.FileLog.m3e(r14);
        goto L_0x0135;
    L_0x0639:
        r32 = 0;
        goto L_0x02c6;
    L_0x063d:
        r32 = 0;
        goto L_0x0380;
    L_0x0641:
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r0 = r37;
        r0 = r0.actionBarLayout;
        r33 = r0;
        r34 = new android.view.ViewGroup$LayoutParams;
        r35 = -1;
        r36 = -1;
        r34.<init>(r35, r36);
        r32.addView(r33, r34);
        goto L_0x0394;
    L_0x065b:
        r32 = 1134559232; // 0x43a00000 float:320.0 double:5.605467397E-315;
        r32 = org.telegram.messenger.AndroidUtilities.dp(r32);
        r0 = r28;
        r0 = r0.x;
        r33 = r0;
        r0 = r28;
        r0 = r0.y;
        r34 = r0;
        r33 = java.lang.Math.min(r33, r34);
        r34 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r34 = org.telegram.messenger.AndroidUtilities.dp(r34);
        r33 = r33 - r34;
        r32 = java.lang.Math.min(r32, r33);
        goto L_0x0421;
    L_0x067f:
        r12 = new org.telegram.ui.DialogsActivity;
        r32 = 0;
        r0 = r32;
        r12.<init>(r0);
        r0 = r37;
        r0 = r0.sideMenu;
        r32 = r0;
        r0 = r32;
        r12.setSideMenu(r0);
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r32;
        r0.addFragmentToStack(r12);
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r33 = 1;
        r34 = 0;
        r32.setAllowOpenDrawer(r33, r34);
        goto L_0x058e;
    L_0x06ad:
        r33 = "chat";
        r0 = r16;
        r1 = r33;
        r33 = r0.equals(r1);	 Catch:{ Exception -> 0x0740 }
        if (r33 == 0) goto L_0x05b1;
    L_0x06ba:
        r32 = 0;
        goto L_0x05b1;
    L_0x06be:
        r33 = "settings";
        r0 = r16;
        r1 = r33;
        r33 = r0.equals(r1);	 Catch:{ Exception -> 0x0740 }
        if (r33 == 0) goto L_0x05b1;
    L_0x06cb:
        r32 = 1;
        goto L_0x05b1;
    L_0x06cf:
        r33 = "group";
        r0 = r16;
        r1 = r33;
        r33 = r0.equals(r1);	 Catch:{ Exception -> 0x0740 }
        if (r33 == 0) goto L_0x05b1;
    L_0x06dc:
        r32 = 2;
        goto L_0x05b1;
    L_0x06e0:
        r33 = "channel";
        r0 = r16;
        r1 = r33;
        r33 = r0.equals(r1);	 Catch:{ Exception -> 0x0740 }
        if (r33 == 0) goto L_0x05b1;
    L_0x06ed:
        r32 = 3;
        goto L_0x05b1;
    L_0x06f1:
        r33 = "edit";
        r0 = r16;
        r1 = r33;
        r33 = r0.equals(r1);	 Catch:{ Exception -> 0x0740 }
        if (r33 == 0) goto L_0x05b1;
    L_0x06fe:
        r32 = 4;
        goto L_0x05b1;
    L_0x0702:
        r33 = "chat_profile";
        r0 = r16;
        r1 = r33;
        r33 = r0.equals(r1);	 Catch:{ Exception -> 0x0740 }
        if (r33 == 0) goto L_0x05b1;
    L_0x070f:
        r32 = 5;
        goto L_0x05b1;
    L_0x0713:
        r33 = "wallpapers";
        r0 = r16;
        r1 = r33;
        r33 = r0.equals(r1);	 Catch:{ Exception -> 0x0740 }
        if (r33 == 0) goto L_0x05b1;
    L_0x0720:
        r32 = 6;
        goto L_0x05b1;
    L_0x0724:
        if (r7 == 0) goto L_0x05b4;
    L_0x0726:
        r9 = new org.telegram.ui.ChatActivity;	 Catch:{ Exception -> 0x0740 }
        r9.<init>(r7);	 Catch:{ Exception -> 0x0740 }
        r0 = r37;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0740 }
        r32 = r0;
        r0 = r32;
        r32 = r0.addFragmentToStack(r9);	 Catch:{ Exception -> 0x0740 }
        if (r32 == 0) goto L_0x05b4;
    L_0x0739:
        r0 = r38;
        r9.restoreSelfArgs(r0);	 Catch:{ Exception -> 0x0740 }
        goto L_0x05b4;
    L_0x0740:
        r14 = move-exception;
        org.telegram.messenger.FileLog.m3e(r14);
        goto L_0x05b4;
    L_0x0746:
        r29 = new org.telegram.ui.SettingsActivity;	 Catch:{ Exception -> 0x0740 }
        r29.<init>();	 Catch:{ Exception -> 0x0740 }
        r0 = r37;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0740 }
        r32 = r0;
        r0 = r32;
        r1 = r29;
        r0.addFragmentToStack(r1);	 Catch:{ Exception -> 0x0740 }
        r0 = r29;
        r1 = r38;
        r0.restoreSelfArgs(r1);	 Catch:{ Exception -> 0x0740 }
        goto L_0x05b4;
    L_0x0761:
        if (r7 == 0) goto L_0x05b4;
    L_0x0763:
        r18 = new org.telegram.ui.GroupCreateFinalActivity;	 Catch:{ Exception -> 0x0740 }
        r0 = r18;
        r0.<init>(r7);	 Catch:{ Exception -> 0x0740 }
        r0 = r37;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0740 }
        r32 = r0;
        r0 = r32;
        r1 = r18;
        r32 = r0.addFragmentToStack(r1);	 Catch:{ Exception -> 0x0740 }
        if (r32 == 0) goto L_0x05b4;
    L_0x077a:
        r0 = r18;
        r1 = r38;
        r0.restoreSelfArgs(r1);	 Catch:{ Exception -> 0x0740 }
        goto L_0x05b4;
    L_0x0783:
        if (r7 == 0) goto L_0x05b4;
    L_0x0785:
        r8 = new org.telegram.ui.ChannelCreateActivity;	 Catch:{ Exception -> 0x0740 }
        r8.<init>(r7);	 Catch:{ Exception -> 0x0740 }
        r0 = r37;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0740 }
        r32 = r0;
        r0 = r32;
        r32 = r0.addFragmentToStack(r8);	 Catch:{ Exception -> 0x0740 }
        if (r32 == 0) goto L_0x05b4;
    L_0x0798:
        r0 = r38;
        r8.restoreSelfArgs(r0);	 Catch:{ Exception -> 0x0740 }
        goto L_0x05b4;
    L_0x079f:
        if (r7 == 0) goto L_0x05b4;
    L_0x07a1:
        r8 = new org.telegram.ui.ChannelEditActivity;	 Catch:{ Exception -> 0x0740 }
        r8.<init>(r7);	 Catch:{ Exception -> 0x0740 }
        r0 = r37;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0740 }
        r32 = r0;
        r0 = r32;
        r32 = r0.addFragmentToStack(r8);	 Catch:{ Exception -> 0x0740 }
        if (r32 == 0) goto L_0x05b4;
    L_0x07b4:
        r0 = r38;
        r8.restoreSelfArgs(r0);	 Catch:{ Exception -> 0x0740 }
        goto L_0x05b4;
    L_0x07bb:
        if (r7 == 0) goto L_0x05b4;
    L_0x07bd:
        r26 = new org.telegram.ui.ProfileActivity;	 Catch:{ Exception -> 0x0740 }
        r0 = r26;
        r0.<init>(r7);	 Catch:{ Exception -> 0x0740 }
        r0 = r37;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0740 }
        r32 = r0;
        r0 = r32;
        r1 = r26;
        r32 = r0.addFragmentToStack(r1);	 Catch:{ Exception -> 0x0740 }
        if (r32 == 0) goto L_0x05b4;
    L_0x07d4:
        r0 = r26;
        r1 = r38;
        r0.restoreSelfArgs(r1);	 Catch:{ Exception -> 0x0740 }
        goto L_0x05b4;
    L_0x07dd:
        r29 = new org.telegram.ui.WallpapersActivity;	 Catch:{ Exception -> 0x0740 }
        r29.<init>();	 Catch:{ Exception -> 0x0740 }
        r0 = r37;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0740 }
        r32 = r0;
        r0 = r32;
        r1 = r29;
        r0.addFragmentToStack(r1);	 Catch:{ Exception -> 0x0740 }
        r0 = r29;
        r1 = r38;
        r0.restoreSelfArgs(r1);	 Catch:{ Exception -> 0x0740 }
        goto L_0x05b4;
    L_0x07f8:
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r32;
        r0 = r0.fragmentsStack;
        r32 = r0;
        r33 = 0;
        r15 = r32.get(r33);
        r15 = (org.telegram.ui.ActionBar.BaseFragment) r15;
        r0 = r15 instanceof org.telegram.ui.DialogsActivity;
        r32 = r0;
        if (r32 == 0) goto L_0x081f;
    L_0x0812:
        r15 = (org.telegram.ui.DialogsActivity) r15;
        r0 = r37;
        r0 = r0.sideMenu;
        r32 = r0;
        r0 = r32;
        r15.setSideMenu(r0);
    L_0x081f:
        r6 = 1;
        r32 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r32 == 0) goto L_0x0884;
    L_0x0826:
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r32;
        r0 = r0.fragmentsStack;
        r32 = r0;
        r32 = r32.size();
        r33 = 1;
        r0 = r32;
        r1 = r33;
        if (r0 > r1) goto L_0x08c8;
    L_0x083e:
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r0 = r32;
        r0 = r0.fragmentsStack;
        r32 = r0;
        r32 = r32.isEmpty();
        if (r32 == 0) goto L_0x08c8;
    L_0x0850:
        r6 = 1;
    L_0x0851:
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r0 = r32;
        r0 = r0.fragmentsStack;
        r32 = r0;
        r32 = r32.size();
        r33 = 1;
        r0 = r32;
        r1 = r33;
        if (r0 != r1) goto L_0x0884;
    L_0x0869:
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r0 = r32;
        r0 = r0.fragmentsStack;
        r32 = r0;
        r33 = 0;
        r32 = r32.get(r33);
        r0 = r32;
        r0 = r0 instanceof org.telegram.ui.LoginActivity;
        r32 = r0;
        if (r32 == 0) goto L_0x0884;
    L_0x0883:
        r6 = 0;
    L_0x0884:
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r32;
        r0 = r0.fragmentsStack;
        r32 = r0;
        r32 = r32.size();
        r33 = 1;
        r0 = r32;
        r1 = r33;
        if (r0 != r1) goto L_0x08b7;
    L_0x089c:
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r32;
        r0 = r0.fragmentsStack;
        r32 = r0;
        r33 = 0;
        r32 = r32.get(r33);
        r0 = r32;
        r0 = r0 instanceof org.telegram.ui.LoginActivity;
        r32 = r0;
        if (r32 == 0) goto L_0x08b7;
    L_0x08b6:
        r6 = 0;
    L_0x08b7:
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r33 = 0;
        r0 = r32;
        r1 = r33;
        r0.setAllowOpenDrawer(r6, r1);
        goto L_0x05b4;
    L_0x08c8:
        r6 = 0;
        goto L_0x0851;
    L_0x08ca:
        r32 = 0;
        goto L_0x05c1;
    L_0x08ce:
        r23 = "";
        goto L_0x05da;
    L_0x08d3:
        r24 = "";
        goto L_0x05e0;
    L_0x08d8:
        r14 = move-exception;
        org.telegram.messenger.FileLog.m3e(r14);
        goto L_0x0622;
    L_0x08de:
        r32 = move-exception;
        goto L_0x0114;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.LaunchActivity.onCreate(android.os.Bundle):void");
    }

    public void switchToAccount(int account, boolean removeAll) {
        if (account != UserConfig.selectedAccount) {
            ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
            UserConfig.selectedAccount = account;
            UserConfig.getInstance(0).saveConfig(false);
            checkCurrentAccount();
            if (AndroidUtilities.isTablet()) {
                this.layersActionBarLayout.removeAllFragments();
                this.rightActionBarLayout.removeAllFragments();
                if (!this.tabletFullSize) {
                    this.shadowTabletSide.setVisibility(0);
                    if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                        this.backgroundTablet.setVisibility(0);
                    }
                }
            }
            if (removeAll) {
                this.actionBarLayout.removeAllFragments();
            } else {
                this.actionBarLayout.removeFragmentFromStack(0);
            }
            DialogsActivity dialogsActivity = new DialogsActivity(null);
            dialogsActivity.setSideMenu(this.sideMenu);
            this.actionBarLayout.addFragmentToStack(dialogsActivity, 0);
            this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            this.actionBarLayout.showLastFragment();
            if (AndroidUtilities.isTablet()) {
                this.layersActionBarLayout.showLastFragment();
                this.rightActionBarLayout.showLastFragment();
            }
            if (!ApplicationLoader.mainInterfacePaused) {
                ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
            }
        }
    }

    private void switchToAvailableAccountOrLogout() {
        int account = -1;
        for (int a = 0; a < 3; a++) {
            if (UserConfig.getInstance(a).isClientActivated()) {
                account = a;
                break;
            }
        }
        if (account != -1) {
            switchToAccount(account, true);
            return;
        }
        if (this.drawerLayoutAdapter != null) {
            this.drawerLayoutAdapter.notifyDataSetChanged();
        }
        Iterator it = this.actionBarLayout.fragmentsStack.iterator();
        while (it.hasNext()) {
            ((BaseFragment) it.next()).onFragmentDestroy();
        }
        this.actionBarLayout.fragmentsStack.clear();
        if (AndroidUtilities.isTablet()) {
            it = this.layersActionBarLayout.fragmentsStack.iterator();
            while (it.hasNext()) {
                ((BaseFragment) it.next()).onFragmentDestroy();
            }
            this.layersActionBarLayout.fragmentsStack.clear();
            it = this.rightActionBarLayout.fragmentsStack.iterator();
            while (it.hasNext()) {
                ((BaseFragment) it.next()).onFragmentDestroy();
            }
            this.rightActionBarLayout.fragmentsStack.clear();
        }
        startActivity(new Intent(this, IntroActivity.class));
        onFinish();
        finish();
    }

    public int getMainFragmentsCount() {
        return mainFragmentsStack.size();
    }

    private void checkCurrentAccount() {
        if (this.currentAccount != UserConfig.selectedAccount) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.appDidLogout);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.mainUserInfoChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdatedConnectionState);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.needShowAlert);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.openArticle);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.hasNewContactsToImport);
        }
        this.currentAccount = UserConfig.selectedAccount;
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.appDidLogout);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.mainUserInfoChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdatedConnectionState);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.needShowAlert);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.openArticle);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.hasNewContactsToImport);
        updateCurrentConnectionState(this.currentAccount);
    }

    private void checkLayout() {
        int i = 0;
        int i2 = 8;
        if (AndroidUtilities.isTablet() && this.rightActionBarLayout != null) {
            int a;
            BaseFragment chatFragment;
            if (AndroidUtilities.isInMultiwindow || (AndroidUtilities.isSmallTablet() && getResources().getConfiguration().orientation != 2)) {
                this.tabletFullSize = true;
                if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    a = 0;
                    while (this.rightActionBarLayout.fragmentsStack.size() > 0) {
                        chatFragment = (BaseFragment) this.rightActionBarLayout.fragmentsStack.get(a);
                        if (chatFragment instanceof ChatActivity) {
                            ((ChatActivity) chatFragment).setIgnoreAttachOnPause(true);
                        }
                        chatFragment.onPause();
                        this.rightActionBarLayout.fragmentsStack.remove(a);
                        this.actionBarLayout.fragmentsStack.add(chatFragment);
                        a = (a - 1) + 1;
                    }
                    if (this.passcodeView.getVisibility() != 0) {
                        this.actionBarLayout.showLastFragment();
                    }
                }
                this.shadowTabletSide.setVisibility(8);
                this.rightActionBarLayout.setVisibility(8);
                View view = this.backgroundTablet;
                if (this.actionBarLayout.fragmentsStack.isEmpty()) {
                    i2 = 0;
                }
                view.setVisibility(i2);
                return;
            }
            int i3;
            this.tabletFullSize = false;
            if (this.actionBarLayout.fragmentsStack.size() >= 2) {
                for (a = 1; a < this.actionBarLayout.fragmentsStack.size(); a = (a - 1) + 1) {
                    chatFragment = (BaseFragment) this.actionBarLayout.fragmentsStack.get(a);
                    if (chatFragment instanceof ChatActivity) {
                        ((ChatActivity) chatFragment).setIgnoreAttachOnPause(true);
                    }
                    chatFragment.onPause();
                    this.actionBarLayout.fragmentsStack.remove(a);
                    this.rightActionBarLayout.fragmentsStack.add(chatFragment);
                }
                if (this.passcodeView.getVisibility() != 0) {
                    this.actionBarLayout.showLastFragment();
                    this.rightActionBarLayout.showLastFragment();
                }
            }
            ActionBarLayout actionBarLayout = this.rightActionBarLayout;
            if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                i3 = 8;
            } else {
                i3 = 0;
            }
            actionBarLayout.setVisibility(i3);
            View view2 = this.backgroundTablet;
            if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                i3 = 0;
            } else {
                i3 = 8;
            }
            view2.setVisibility(i3);
            FrameLayout frameLayout = this.shadowTabletSide;
            if (this.actionBarLayout.fragmentsStack.isEmpty()) {
                i = 8;
            }
            frameLayout.setVisibility(i);
        }
    }

    private void showPasscodeActivity() {
        if (this.passcodeView != null) {
            SharedConfig.appLocked = true;
            if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
                SecretMediaViewer.getInstance().closePhoto(false, false);
            } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                PhotoViewer.getInstance().closePhoto(false, true);
            } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                ArticleViewer.getInstance().close(false, true);
            }
            this.passcodeView.onShow();
            SharedConfig.isWaitingForPasscodeEnter = true;
            this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
            this.passcodeView.setDelegate(new C18906());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean handleIntent(android.content.Intent r86, boolean r87, boolean r88, boolean r89) {
        /*
        r85 = this;
        r4 = org.telegram.messenger.AndroidUtilities.handleProxyIntent(r85, r86);
        if (r4 == 0) goto L_0x0009;
    L_0x0006:
        r61 = 1;
    L_0x0008:
        return r61;
    L_0x0009:
        r4 = org.telegram.ui.PhotoViewer.hasInstance();
        if (r4 == 0) goto L_0x0034;
    L_0x000f:
        r4 = org.telegram.ui.PhotoViewer.getInstance();
        r4 = r4.isVisible();
        if (r4 == 0) goto L_0x0034;
    L_0x0019:
        if (r86 == 0) goto L_0x0028;
    L_0x001b:
        r4 = "android.intent.action.MAIN";
        r5 = r86.getAction();
        r4 = r4.equals(r5);
        if (r4 != 0) goto L_0x0034;
    L_0x0028:
        r4 = org.telegram.ui.PhotoViewer.getInstance();
        r5 = 0;
        r16 = 1;
        r0 = r16;
        r4.closePhoto(r5, r0);
    L_0x0034:
        r43 = r86.getFlags();
        r4 = 1;
        r0 = new int[r4];
        r47 = r0;
        r4 = 0;
        r5 = "currentAccount";
        r16 = org.telegram.messenger.UserConfig.selectedAccount;
        r0 = r86;
        r1 = r16;
        r5 = r0.getIntExtra(r5, r1);
        r47[r4] = r5;
        r4 = 0;
        r4 = r47[r4];
        r5 = 1;
        r0 = r85;
        r0.switchToAccount(r4, r5);
        if (r89 != 0) goto L_0x0087;
    L_0x0058:
        r4 = 1;
        r4 = org.telegram.messenger.AndroidUtilities.needShowPasscode(r4);
        if (r4 != 0) goto L_0x0063;
    L_0x005f:
        r4 = org.telegram.messenger.SharedConfig.isWaitingForPasscodeEnter;
        if (r4 == 0) goto L_0x0087;
    L_0x0063:
        r85.showPasscodeActivity();
        r0 = r86;
        r1 = r85;
        r1.passcodeSaveIntent = r0;
        r0 = r87;
        r1 = r85;
        r1.passcodeSaveIntentIsNew = r0;
        r0 = r88;
        r1 = r85;
        r1.passcodeSaveIntentIsRestore = r0;
        r0 = r85;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r5 = 0;
        r4.saveConfig(r5);
        r61 = 0;
        goto L_0x0008;
    L_0x0087:
        r61 = 0;
        r4 = 0;
        r65 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r62 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r63 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r64 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r53 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r52 = java.lang.Integer.valueOf(r4);
        r36 = 0;
        r4 = org.telegram.messenger.SharedConfig.directShare;
        if (r4 == 0) goto L_0x00c4;
    L_0x00ad:
        if (r86 == 0) goto L_0x01fd;
    L_0x00af:
        r4 = r86.getExtras();
        if (r4 == 0) goto L_0x01fd;
    L_0x00b5:
        r4 = r86.getExtras();
        r5 = "dialogId";
        r16 = 0;
        r0 = r16;
        r36 = r4.getLong(r5, r0);
    L_0x00c4:
        r69 = 0;
        r71 = 0;
        r70 = 0;
        r4 = 0;
        r0 = r85;
        r0.photoPathsArray = r4;
        r4 = 0;
        r0 = r85;
        r0.videoPath = r4;
        r4 = 0;
        r0 = r85;
        r0.sendingText = r4;
        r4 = 0;
        r0 = r85;
        r0.documentsPathsArray = r4;
        r4 = 0;
        r0 = r85;
        r0.documentsOriginalPathsArray = r4;
        r4 = 0;
        r0 = r85;
        r0.documentsMimeType = r4;
        r4 = 0;
        r0 = r85;
        r0.documentsUrisArray = r4;
        r4 = 0;
        r0 = r85;
        r0.contactsToSend = r4;
        r0 = r85;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r4 = r4.isClientActivated();
        if (r4 == 0) goto L_0x02c1;
    L_0x0100:
        r4 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r4 = r4 & r43;
        if (r4 != 0) goto L_0x02c1;
    L_0x0106:
        if (r86 == 0) goto L_0x02c1;
    L_0x0108:
        r4 = r86.getAction();
        if (r4 == 0) goto L_0x02c1;
    L_0x010e:
        if (r88 != 0) goto L_0x02c1;
    L_0x0110:
        r4 = "android.intent.action.SEND";
        r5 = r86.getAction();
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x05d0;
    L_0x011d:
        r42 = 0;
        r76 = r86.getType();
        if (r76 == 0) goto L_0x045e;
    L_0x0125:
        r4 = "text/x-vcard";
        r0 = r76;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x045e;
    L_0x0130:
        r4 = r86.getExtras();	 Catch:{ Exception -> 0x02ac }
        r5 = "android.intent.extra.STREAM";
        r77 = r4.get(r5);	 Catch:{ Exception -> 0x02ac }
        r77 = (android.net.Uri) r77;	 Catch:{ Exception -> 0x02ac }
        if (r77 == 0) goto L_0x045a;
    L_0x013f:
        r31 = r85.getContentResolver();	 Catch:{ Exception -> 0x02ac }
        r0 = r31;
        r1 = r77;
        r72 = r0.openInputStream(r1);	 Catch:{ Exception -> 0x02ac }
        r84 = new java.util.ArrayList;	 Catch:{ Exception -> 0x02ac }
        r84.<init>();	 Catch:{ Exception -> 0x02ac }
        r32 = 0;
        r27 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x02ac }
        r4 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x02ac }
        r5 = "UTF-8";
        r0 = r72;
        r4.<init>(r0, r5);	 Catch:{ Exception -> 0x02ac }
        r0 = r27;
        r0.<init>(r4);	 Catch:{ Exception -> 0x02ac }
    L_0x0163:
        r48 = r27.readLine();	 Catch:{ Exception -> 0x02ac }
        if (r48 == 0) goto L_0x03d4;
    L_0x0169:
        r4 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x02ac }
        if (r4 == 0) goto L_0x0170;
    L_0x016d:
        org.telegram.messenger.FileLog.m0d(r48);	 Catch:{ Exception -> 0x02ac }
    L_0x0170:
        r4 = ":";
        r0 = r48;
        r24 = r0.split(r4);	 Catch:{ Exception -> 0x02ac }
        r0 = r24;
        r4 = r0.length;	 Catch:{ Exception -> 0x02ac }
        r5 = 2;
        if (r4 != r5) goto L_0x0163;
    L_0x017f:
        r4 = 0;
        r4 = r24[r4];	 Catch:{ Exception -> 0x02ac }
        r5 = "BEGIN";
        r4 = r4.equals(r5);	 Catch:{ Exception -> 0x02ac }
        if (r4 == 0) goto L_0x0201;
    L_0x018b:
        r4 = 1;
        r4 = r24[r4];	 Catch:{ Exception -> 0x02ac }
        r5 = "VCARD";
        r4 = r4.equals(r5);	 Catch:{ Exception -> 0x02ac }
        if (r4 == 0) goto L_0x0201;
    L_0x0197:
        r32 = new org.telegram.ui.LaunchActivity$VcardData;	 Catch:{ Exception -> 0x02ac }
        r4 = 0;
        r0 = r32;
        r1 = r85;
        r0.<init>();	 Catch:{ Exception -> 0x02ac }
        r0 = r84;
        r1 = r32;
        r0.add(r1);	 Catch:{ Exception -> 0x02ac }
    L_0x01a8:
        if (r32 == 0) goto L_0x0163;
    L_0x01aa:
        r4 = 0;
        r4 = r24[r4];	 Catch:{ Exception -> 0x02ac }
        r5 = "FN";
        r4 = r4.startsWith(r5);	 Catch:{ Exception -> 0x02ac }
        if (r4 != 0) goto L_0x01cc;
    L_0x01b6:
        r4 = 0;
        r4 = r24[r4];	 Catch:{ Exception -> 0x02ac }
        r5 = "ORG";
        r4 = r4.startsWith(r5);	 Catch:{ Exception -> 0x02ac }
        if (r4 == 0) goto L_0x03af;
    L_0x01c2:
        r0 = r32;
        r4 = r0.name;	 Catch:{ Exception -> 0x02ac }
        r4 = android.text.TextUtils.isEmpty(r4);	 Catch:{ Exception -> 0x02ac }
        if (r4 == 0) goto L_0x03af;
    L_0x01cc:
        r51 = 0;
        r50 = 0;
        r4 = 0;
        r4 = r24[r4];	 Catch:{ Exception -> 0x02ac }
        r5 = ";";
        r56 = r4.split(r5);	 Catch:{ Exception -> 0x02ac }
        r0 = r56;
        r5 = r0.length;	 Catch:{ Exception -> 0x02ac }
        r4 = 0;
    L_0x01de:
        if (r4 >= r5) goto L_0x0240;
    L_0x01e0:
        r55 = r56[r4];	 Catch:{ Exception -> 0x02ac }
        r16 = "=";
        r0 = r55;
        r1 = r16;
        r25 = r0.split(r1);	 Catch:{ Exception -> 0x02ac }
        r0 = r25;
        r0 = r0.length;	 Catch:{ Exception -> 0x02ac }
        r16 = r0;
        r17 = 2;
        r0 = r16;
        r1 = r17;
        if (r0 == r1) goto L_0x021c;
    L_0x01fa:
        r4 = r4 + 1;
        goto L_0x01de;
    L_0x01fd:
        r36 = 0;
        goto L_0x00c4;
    L_0x0201:
        r4 = 0;
        r4 = r24[r4];	 Catch:{ Exception -> 0x02ac }
        r5 = "END";
        r4 = r4.equals(r5);	 Catch:{ Exception -> 0x02ac }
        if (r4 == 0) goto L_0x01a8;
    L_0x020d:
        r4 = 1;
        r4 = r24[r4];	 Catch:{ Exception -> 0x02ac }
        r5 = "VCARD";
        r4 = r4.equals(r5);	 Catch:{ Exception -> 0x02ac }
        if (r4 == 0) goto L_0x01a8;
    L_0x0219:
        r32 = 0;
        goto L_0x01a8;
    L_0x021c:
        r16 = 0;
        r16 = r25[r16];	 Catch:{ Exception -> 0x02ac }
        r17 = "CHARSET";
        r16 = r16.equals(r17);	 Catch:{ Exception -> 0x02ac }
        if (r16 == 0) goto L_0x022e;
    L_0x0229:
        r16 = 1;
        r50 = r25[r16];	 Catch:{ Exception -> 0x02ac }
        goto L_0x01fa;
    L_0x022e:
        r16 = 0;
        r16 = r25[r16];	 Catch:{ Exception -> 0x02ac }
        r17 = "ENCODING";
        r16 = r16.equals(r17);	 Catch:{ Exception -> 0x02ac }
        if (r16 == 0) goto L_0x01fa;
    L_0x023b:
        r16 = 1;
        r51 = r25[r16];	 Catch:{ Exception -> 0x02ac }
        goto L_0x01fa;
    L_0x0240:
        r4 = 1;
        r4 = r24[r4];	 Catch:{ Exception -> 0x02ac }
        r0 = r32;
        r0.name = r4;	 Catch:{ Exception -> 0x02ac }
        if (r51 == 0) goto L_0x0163;
    L_0x0249:
        r4 = "QUOTED-PRINTABLE";
        r0 = r51;
        r4 = r0.equalsIgnoreCase(r4);	 Catch:{ Exception -> 0x02ac }
        if (r4 == 0) goto L_0x0163;
    L_0x0254:
        r0 = r32;
        r4 = r0.name;	 Catch:{ Exception -> 0x02ac }
        r5 = "=";
        r4 = r4.endsWith(r5);	 Catch:{ Exception -> 0x02ac }
        if (r4 == 0) goto L_0x0284;
    L_0x0261:
        if (r51 == 0) goto L_0x0284;
    L_0x0263:
        r0 = r32;
        r4 = r0.name;	 Catch:{ Exception -> 0x02ac }
        r5 = 0;
        r0 = r32;
        r0 = r0.name;	 Catch:{ Exception -> 0x02ac }
        r16 = r0;
        r16 = r16.length();	 Catch:{ Exception -> 0x02ac }
        r16 = r16 + -1;
        r0 = r16;
        r4 = r4.substring(r5, r0);	 Catch:{ Exception -> 0x02ac }
        r0 = r32;
        r0.name = r4;	 Catch:{ Exception -> 0x02ac }
        r48 = r27.readLine();	 Catch:{ Exception -> 0x02ac }
        if (r48 != 0) goto L_0x0392;
    L_0x0284:
        r0 = r32;
        r4 = r0.name;	 Catch:{ Exception -> 0x02ac }
        r4 = r4.getBytes();	 Catch:{ Exception -> 0x02ac }
        r28 = org.telegram.messenger.AndroidUtilities.decodeQuotedPrintable(r4);	 Catch:{ Exception -> 0x02ac }
        if (r28 == 0) goto L_0x0163;
    L_0x0292:
        r0 = r28;
        r4 = r0.length;	 Catch:{ Exception -> 0x02ac }
        if (r4 == 0) goto L_0x0163;
    L_0x0297:
        r35 = new java.lang.String;	 Catch:{ Exception -> 0x02ac }
        r0 = r35;
        r1 = r28;
        r2 = r50;
        r0.<init>(r1, r2);	 Catch:{ Exception -> 0x02ac }
        if (r35 == 0) goto L_0x0163;
    L_0x02a4:
        r0 = r35;
        r1 = r32;
        r1.name = r0;	 Catch:{ Exception -> 0x02ac }
        goto L_0x0163;
    L_0x02ac:
        r40 = move-exception;
        org.telegram.messenger.FileLog.m3e(r40);
        r42 = 1;
    L_0x02b2:
        if (r42 == 0) goto L_0x02c1;
    L_0x02b4:
        r4 = "Unsupported content";
        r5 = 0;
        r0 = r85;
        r4 = android.widget.Toast.makeText(r0, r4, r5);
        r4.show();
    L_0x02c1:
        r4 = r65.intValue();
        if (r4 == 0) goto L_0x0d60;
    L_0x02c7:
        r24 = new android.os.Bundle;
        r24.<init>();
        r4 = "user_id";
        r5 = r65.intValue();
        r0 = r24;
        r0.putInt(r4, r5);
        r4 = r64.intValue();
        if (r4 == 0) goto L_0x02ea;
    L_0x02de:
        r4 = "message_id";
        r5 = r64.intValue();
        r0 = r24;
        r0.putInt(r4, r5);
    L_0x02ea:
        r4 = mainFragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0313;
    L_0x02f2:
        r4 = 0;
        r4 = r47[r4];
        r5 = org.telegram.messenger.MessagesController.getInstance(r4);
        r4 = mainFragmentsStack;
        r16 = mainFragmentsStack;
        r16 = r16.size();
        r16 = r16 + -1;
        r0 = r16;
        r4 = r4.get(r0);
        r4 = (org.telegram.ui.ActionBar.BaseFragment) r4;
        r0 = r24;
        r4 = r5.checkCanOpenChat(r0, r4);
        if (r4 == 0) goto L_0x0333;
    L_0x0313:
        r44 = new org.telegram.ui.ChatActivity;
        r0 = r44;
        r1 = r24;
        r0.<init>(r1);
        r0 = r85;
        r4 = r0.actionBarLayout;
        r5 = 0;
        r16 = 1;
        r17 = 1;
        r0 = r44;
        r1 = r16;
        r2 = r17;
        r4 = r4.presentFragment(r0, r5, r1, r2);
        if (r4 == 0) goto L_0x0333;
    L_0x0331:
        r61 = 1;
    L_0x0333:
        if (r61 != 0) goto L_0x038a;
    L_0x0335:
        if (r87 != 0) goto L_0x038a;
    L_0x0337:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x1188;
    L_0x033d:
        r0 = r85;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r4 = r4.isClientActivated();
        if (r4 != 0) goto L_0x1154;
    L_0x034b:
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x036f;
    L_0x0357:
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r5 = new org.telegram.ui.LoginActivity;
        r5.<init>();
        r4.addFragmentToStack(r5);
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 0;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
    L_0x036f:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4.showLastFragment();
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x038a;
    L_0x037c:
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r4.showLastFragment();
        r0 = r85;
        r4 = r0.rightActionBarLayout;
        r4.showLastFragment();
    L_0x038a:
        r4 = 0;
        r0 = r86;
        r0.setAction(r4);
        goto L_0x0008;
    L_0x0392:
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02ac }
        r4.<init>();	 Catch:{ Exception -> 0x02ac }
        r0 = r32;
        r5 = r0.name;	 Catch:{ Exception -> 0x02ac }
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x02ac }
        r0 = r48;
        r4 = r4.append(r0);	 Catch:{ Exception -> 0x02ac }
        r4 = r4.toString();	 Catch:{ Exception -> 0x02ac }
        r0 = r32;
        r0.name = r4;	 Catch:{ Exception -> 0x02ac }
        goto L_0x0254;
    L_0x03af:
        r4 = 0;
        r4 = r24[r4];	 Catch:{ Exception -> 0x02ac }
        r5 = "TEL";
        r4 = r4.startsWith(r5);	 Catch:{ Exception -> 0x02ac }
        if (r4 == 0) goto L_0x0163;
    L_0x03bb:
        r4 = 1;
        r4 = r24[r4];	 Catch:{ Exception -> 0x02ac }
        r5 = 1;
        r59 = org.telegram.PhoneFormat.PhoneFormat.stripExceptNumbers(r4, r5);	 Catch:{ Exception -> 0x02ac }
        r4 = r59.length();	 Catch:{ Exception -> 0x02ac }
        if (r4 <= 0) goto L_0x0163;
    L_0x03c9:
        r0 = r32;
        r4 = r0.phones;	 Catch:{ Exception -> 0x02ac }
        r0 = r59;
        r4.add(r0);	 Catch:{ Exception -> 0x02ac }
        goto L_0x0163;
    L_0x03d4:
        r27.close();	 Catch:{ Exception -> 0x0452 }
        r72.close();	 Catch:{ Exception -> 0x0452 }
    L_0x03da:
        r22 = 0;
    L_0x03dc:
        r4 = r84.size();	 Catch:{ Exception -> 0x02ac }
        r0 = r22;
        if (r0 >= r4) goto L_0x02b2;
    L_0x03e4:
        r0 = r84;
        r1 = r22;
        r83 = r0.get(r1);	 Catch:{ Exception -> 0x02ac }
        r83 = (org.telegram.ui.LaunchActivity.VcardData) r83;	 Catch:{ Exception -> 0x02ac }
        r0 = r83;
        r4 = r0.name;	 Catch:{ Exception -> 0x02ac }
        if (r4 == 0) goto L_0x0457;
    L_0x03f4:
        r0 = r83;
        r4 = r0.phones;	 Catch:{ Exception -> 0x02ac }
        r4 = r4.isEmpty();	 Catch:{ Exception -> 0x02ac }
        if (r4 != 0) goto L_0x0457;
    L_0x03fe:
        r0 = r85;
        r4 = r0.contactsToSend;	 Catch:{ Exception -> 0x02ac }
        if (r4 != 0) goto L_0x040d;
    L_0x0404:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x02ac }
        r4.<init>();	 Catch:{ Exception -> 0x02ac }
        r0 = r85;
        r0.contactsToSend = r4;	 Catch:{ Exception -> 0x02ac }
    L_0x040d:
        r26 = 0;
    L_0x040f:
        r0 = r83;
        r4 = r0.phones;	 Catch:{ Exception -> 0x02ac }
        r4 = r4.size();	 Catch:{ Exception -> 0x02ac }
        r0 = r26;
        if (r0 >= r4) goto L_0x0457;
    L_0x041b:
        r0 = r83;
        r4 = r0.phones;	 Catch:{ Exception -> 0x02ac }
        r0 = r26;
        r59 = r4.get(r0);	 Catch:{ Exception -> 0x02ac }
        r59 = (java.lang.String) r59;	 Catch:{ Exception -> 0x02ac }
        r80 = new org.telegram.tgnet.TLRPC$TL_userContact_old2;	 Catch:{ Exception -> 0x02ac }
        r80.<init>();	 Catch:{ Exception -> 0x02ac }
        r0 = r59;
        r1 = r80;
        r1.phone = r0;	 Catch:{ Exception -> 0x02ac }
        r0 = r83;
        r4 = r0.name;	 Catch:{ Exception -> 0x02ac }
        r0 = r80;
        r0.first_name = r4;	 Catch:{ Exception -> 0x02ac }
        r4 = "";
        r0 = r80;
        r0.last_name = r4;	 Catch:{ Exception -> 0x02ac }
        r4 = 0;
        r0 = r80;
        r0.id = r4;	 Catch:{ Exception -> 0x02ac }
        r0 = r85;
        r4 = r0.contactsToSend;	 Catch:{ Exception -> 0x02ac }
        r0 = r80;
        r4.add(r0);	 Catch:{ Exception -> 0x02ac }
        r26 = r26 + 1;
        goto L_0x040f;
    L_0x0452:
        r40 = move-exception;
        org.telegram.messenger.FileLog.m3e(r40);	 Catch:{ Exception -> 0x02ac }
        goto L_0x03da;
    L_0x0457:
        r22 = r22 + 1;
        goto L_0x03dc;
    L_0x045a:
        r42 = 1;
        goto L_0x02b2;
    L_0x045e:
        r4 = "android.intent.extra.TEXT";
        r0 = r86;
        r74 = r0.getStringExtra(r4);
        if (r74 != 0) goto L_0x0478;
    L_0x0469:
        r4 = "android.intent.extra.TEXT";
        r0 = r86;
        r75 = r0.getCharSequenceExtra(r4);
        if (r75 == 0) goto L_0x0478;
    L_0x0474:
        r74 = r75.toString();
    L_0x0478:
        r4 = "android.intent.extra.SUBJECT";
        r0 = r86;
        r73 = r0.getStringExtra(r4);
        if (r74 == 0) goto L_0x0537;
    L_0x0483:
        r4 = r74.length();
        if (r4 == 0) goto L_0x0537;
    L_0x0489:
        r4 = "http://";
        r0 = r74;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x049f;
    L_0x0494:
        r4 = "https://";
        r0 = r74;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x04c3;
    L_0x049f:
        if (r73 == 0) goto L_0x04c3;
    L_0x04a1:
        r4 = r73.length();
        if (r4 == 0) goto L_0x04c3;
    L_0x04a7:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r73;
        r4 = r4.append(r0);
        r5 = "\n";
        r4 = r4.append(r5);
        r0 = r74;
        r4 = r4.append(r0);
        r74 = r4.toString();
    L_0x04c3:
        r0 = r74;
        r1 = r85;
        r1.sendingText = r0;
    L_0x04c9:
        r4 = "android.intent.extra.STREAM";
        r0 = r86;
        r57 = r0.getParcelableExtra(r4);
        if (r57 == 0) goto L_0x05c6;
    L_0x04d4:
        r0 = r57;
        r4 = r0 instanceof android.net.Uri;
        if (r4 != 0) goto L_0x04e2;
    L_0x04da:
        r4 = r57.toString();
        r57 = android.net.Uri.parse(r4);
    L_0x04e2:
        r77 = r57;
        r77 = (android.net.Uri) r77;
        if (r77 == 0) goto L_0x04f0;
    L_0x04e8:
        r4 = org.telegram.messenger.AndroidUtilities.isInternalUri(r77);
        if (r4 == 0) goto L_0x04f0;
    L_0x04ee:
        r42 = 1;
    L_0x04f0:
        if (r42 != 0) goto L_0x02b2;
    L_0x04f2:
        if (r77 == 0) goto L_0x0546;
    L_0x04f4:
        if (r76 == 0) goto L_0x0501;
    L_0x04f6:
        r4 = "image/";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0512;
    L_0x0501:
        r4 = r77.toString();
        r4 = r4.toLowerCase();
        r5 = ".jpg";
        r4 = r4.endsWith(r5);
        if (r4 == 0) goto L_0x0546;
    L_0x0512:
        r0 = r85;
        r4 = r0.photoPathsArray;
        if (r4 != 0) goto L_0x0521;
    L_0x0518:
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r85;
        r0.photoPathsArray = r4;
    L_0x0521:
        r46 = new org.telegram.messenger.SendMessagesHelper$SendingMediaInfo;
        r46.<init>();
        r0 = r77;
        r1 = r46;
        r1.uri = r0;
        r0 = r85;
        r4 = r0.photoPathsArray;
        r0 = r46;
        r4.add(r0);
        goto L_0x02b2;
    L_0x0537:
        if (r73 == 0) goto L_0x04c9;
    L_0x0539:
        r4 = r73.length();
        if (r4 <= 0) goto L_0x04c9;
    L_0x053f:
        r0 = r73;
        r1 = r85;
        r1.sendingText = r0;
        goto L_0x04c9;
    L_0x0546:
        r58 = org.telegram.messenger.AndroidUtilities.getPath(r77);
        if (r58 == 0) goto L_0x05a6;
    L_0x054c:
        r4 = "file:";
        r0 = r58;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0563;
    L_0x0557:
        r4 = "file://";
        r5 = "";
        r0 = r58;
        r58 = r0.replace(r4, r5);
    L_0x0563:
        if (r76 == 0) goto L_0x0578;
    L_0x0565:
        r4 = "video/";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0578;
    L_0x0570:
        r0 = r58;
        r1 = r85;
        r1.videoPath = r0;
        goto L_0x02b2;
    L_0x0578:
        r0 = r85;
        r4 = r0.documentsPathsArray;
        if (r4 != 0) goto L_0x0590;
    L_0x057e:
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r85;
        r0.documentsPathsArray = r4;
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r85;
        r0.documentsOriginalPathsArray = r4;
    L_0x0590:
        r0 = r85;
        r4 = r0.documentsPathsArray;
        r0 = r58;
        r4.add(r0);
        r0 = r85;
        r4 = r0.documentsOriginalPathsArray;
        r5 = r77.toString();
        r4.add(r5);
        goto L_0x02b2;
    L_0x05a6:
        r0 = r85;
        r4 = r0.documentsUrisArray;
        if (r4 != 0) goto L_0x05b5;
    L_0x05ac:
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r85;
        r0.documentsUrisArray = r4;
    L_0x05b5:
        r0 = r85;
        r4 = r0.documentsUrisArray;
        r0 = r77;
        r4.add(r0);
        r0 = r76;
        r1 = r85;
        r1.documentsMimeType = r0;
        goto L_0x02b2;
    L_0x05c6:
        r0 = r85;
        r4 = r0.sendingText;
        if (r4 != 0) goto L_0x02b2;
    L_0x05cc:
        r42 = 1;
        goto L_0x02b2;
    L_0x05d0:
        r4 = r86.getAction();
        r5 = "android.intent.action.SEND_MULTIPLE";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0742;
    L_0x05dd:
        r42 = 0;
        r4 = "android.intent.extra.STREAM";
        r0 = r86;
        r78 = r0.getParcelableArrayListExtra(r4);	 Catch:{ Exception -> 0x0728 }
        r76 = r86.getType();	 Catch:{ Exception -> 0x0728 }
        if (r78 == 0) goto L_0x0632;
    L_0x05ee:
        r22 = 0;
    L_0x05f0:
        r4 = r78.size();	 Catch:{ Exception -> 0x0728 }
        r0 = r22;
        if (r0 >= r4) goto L_0x062a;
    L_0x05f8:
        r0 = r78;
        r1 = r22;
        r57 = r0.get(r1);	 Catch:{ Exception -> 0x0728 }
        r57 = (android.os.Parcelable) r57;	 Catch:{ Exception -> 0x0728 }
        r0 = r57;
        r4 = r0 instanceof android.net.Uri;	 Catch:{ Exception -> 0x0728 }
        if (r4 != 0) goto L_0x0610;
    L_0x0608:
        r4 = r57.toString();	 Catch:{ Exception -> 0x0728 }
        r57 = android.net.Uri.parse(r4);	 Catch:{ Exception -> 0x0728 }
    L_0x0610:
        r0 = r57;
        r0 = (android.net.Uri) r0;	 Catch:{ Exception -> 0x0728 }
        r77 = r0;
        if (r77 == 0) goto L_0x0627;
    L_0x0618:
        r4 = org.telegram.messenger.AndroidUtilities.isInternalUri(r77);	 Catch:{ Exception -> 0x0728 }
        if (r4 == 0) goto L_0x0627;
    L_0x061e:
        r0 = r78;
        r1 = r22;
        r0.remove(r1);	 Catch:{ Exception -> 0x0728 }
        r22 = r22 + -1;
    L_0x0627:
        r22 = r22 + 1;
        goto L_0x05f0;
    L_0x062a:
        r4 = r78.isEmpty();	 Catch:{ Exception -> 0x0728 }
        if (r4 == 0) goto L_0x0632;
    L_0x0630:
        r78 = 0;
    L_0x0632:
        if (r78 == 0) goto L_0x073f;
    L_0x0634:
        if (r76 == 0) goto L_0x068f;
    L_0x0636:
        r4 = "image/";
        r0 = r76;
        r4 = r0.startsWith(r4);	 Catch:{ Exception -> 0x0728 }
        if (r4 == 0) goto L_0x068f;
    L_0x0641:
        r22 = 0;
    L_0x0643:
        r4 = r78.size();	 Catch:{ Exception -> 0x0728 }
        r0 = r22;
        if (r0 >= r4) goto L_0x072e;
    L_0x064b:
        r0 = r78;
        r1 = r22;
        r57 = r0.get(r1);	 Catch:{ Exception -> 0x0728 }
        r57 = (android.os.Parcelable) r57;	 Catch:{ Exception -> 0x0728 }
        r0 = r57;
        r4 = r0 instanceof android.net.Uri;	 Catch:{ Exception -> 0x0728 }
        if (r4 != 0) goto L_0x0663;
    L_0x065b:
        r4 = r57.toString();	 Catch:{ Exception -> 0x0728 }
        r57 = android.net.Uri.parse(r4);	 Catch:{ Exception -> 0x0728 }
    L_0x0663:
        r0 = r57;
        r0 = (android.net.Uri) r0;	 Catch:{ Exception -> 0x0728 }
        r77 = r0;
        r0 = r85;
        r4 = r0.photoPathsArray;	 Catch:{ Exception -> 0x0728 }
        if (r4 != 0) goto L_0x0678;
    L_0x066f:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0728 }
        r4.<init>();	 Catch:{ Exception -> 0x0728 }
        r0 = r85;
        r0.photoPathsArray = r4;	 Catch:{ Exception -> 0x0728 }
    L_0x0678:
        r46 = new org.telegram.messenger.SendMessagesHelper$SendingMediaInfo;	 Catch:{ Exception -> 0x0728 }
        r46.<init>();	 Catch:{ Exception -> 0x0728 }
        r0 = r77;
        r1 = r46;
        r1.uri = r0;	 Catch:{ Exception -> 0x0728 }
        r0 = r85;
        r4 = r0.photoPathsArray;	 Catch:{ Exception -> 0x0728 }
        r0 = r46;
        r4.add(r0);	 Catch:{ Exception -> 0x0728 }
        r22 = r22 + 1;
        goto L_0x0643;
    L_0x068f:
        r22 = 0;
    L_0x0691:
        r4 = r78.size();	 Catch:{ Exception -> 0x0728 }
        r0 = r22;
        if (r0 >= r4) goto L_0x072e;
    L_0x0699:
        r0 = r78;
        r1 = r22;
        r57 = r0.get(r1);	 Catch:{ Exception -> 0x0728 }
        r57 = (android.os.Parcelable) r57;	 Catch:{ Exception -> 0x0728 }
        r0 = r57;
        r4 = r0 instanceof android.net.Uri;	 Catch:{ Exception -> 0x0728 }
        if (r4 != 0) goto L_0x06b1;
    L_0x06a9:
        r4 = r57.toString();	 Catch:{ Exception -> 0x0728 }
        r57 = android.net.Uri.parse(r4);	 Catch:{ Exception -> 0x0728 }
    L_0x06b1:
        r0 = r57;
        r0 = (android.net.Uri) r0;	 Catch:{ Exception -> 0x0728 }
        r77 = r0;
        r58 = org.telegram.messenger.AndroidUtilities.getPath(r77);	 Catch:{ Exception -> 0x0728 }
        r54 = r57.toString();	 Catch:{ Exception -> 0x0728 }
        if (r54 != 0) goto L_0x06c3;
    L_0x06c1:
        r54 = r58;
    L_0x06c3:
        if (r58 == 0) goto L_0x0709;
    L_0x06c5:
        r4 = "file:";
        r0 = r58;
        r4 = r0.startsWith(r4);	 Catch:{ Exception -> 0x0728 }
        if (r4 == 0) goto L_0x06dc;
    L_0x06d0:
        r4 = "file://";
        r5 = "";
        r0 = r58;
        r58 = r0.replace(r4, r5);	 Catch:{ Exception -> 0x0728 }
    L_0x06dc:
        r0 = r85;
        r4 = r0.documentsPathsArray;	 Catch:{ Exception -> 0x0728 }
        if (r4 != 0) goto L_0x06f4;
    L_0x06e2:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0728 }
        r4.<init>();	 Catch:{ Exception -> 0x0728 }
        r0 = r85;
        r0.documentsPathsArray = r4;	 Catch:{ Exception -> 0x0728 }
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0728 }
        r4.<init>();	 Catch:{ Exception -> 0x0728 }
        r0 = r85;
        r0.documentsOriginalPathsArray = r4;	 Catch:{ Exception -> 0x0728 }
    L_0x06f4:
        r0 = r85;
        r4 = r0.documentsPathsArray;	 Catch:{ Exception -> 0x0728 }
        r0 = r58;
        r4.add(r0);	 Catch:{ Exception -> 0x0728 }
        r0 = r85;
        r4 = r0.documentsOriginalPathsArray;	 Catch:{ Exception -> 0x0728 }
        r0 = r54;
        r4.add(r0);	 Catch:{ Exception -> 0x0728 }
    L_0x0706:
        r22 = r22 + 1;
        goto L_0x0691;
    L_0x0709:
        r0 = r85;
        r4 = r0.documentsUrisArray;	 Catch:{ Exception -> 0x0728 }
        if (r4 != 0) goto L_0x0718;
    L_0x070f:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0728 }
        r4.<init>();	 Catch:{ Exception -> 0x0728 }
        r0 = r85;
        r0.documentsUrisArray = r4;	 Catch:{ Exception -> 0x0728 }
    L_0x0718:
        r0 = r85;
        r4 = r0.documentsUrisArray;	 Catch:{ Exception -> 0x0728 }
        r0 = r77;
        r4.add(r0);	 Catch:{ Exception -> 0x0728 }
        r0 = r76;
        r1 = r85;
        r1.documentsMimeType = r0;	 Catch:{ Exception -> 0x0728 }
        goto L_0x0706;
    L_0x0728:
        r40 = move-exception;
        org.telegram.messenger.FileLog.m3e(r40);
        r42 = 1;
    L_0x072e:
        if (r42 == 0) goto L_0x02c1;
    L_0x0730:
        r4 = "Unsupported content";
        r5 = 0;
        r0 = r85;
        r4 = android.widget.Toast.makeText(r0, r4, r5);
        r4.show();
        goto L_0x02c1;
    L_0x073f:
        r42 = 1;
        goto L_0x072e;
    L_0x0742:
        r4 = "android.intent.action.VIEW";
        r5 = r86.getAction();
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0c8d;
    L_0x074f:
        r34 = r86.getData();
        if (r34 == 0) goto L_0x02c1;
    L_0x0755:
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r15 = 0;
        r9 = 0;
        r10 = 0;
        r11 = 0;
        r59 = 0;
        r14 = 0;
        r60 = 0;
        r13 = 0;
        r12 = 0;
        r67 = r34.getScheme();
        if (r67 == 0) goto L_0x07de;
    L_0x0769:
        r4 = "http";
        r0 = r67;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x077f;
    L_0x0774:
        r4 = "https";
        r0 = r67;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x0971;
    L_0x077f:
        r4 = r34.getHost();
        r45 = r4.toLowerCase();
        r4 = "telegram.me";
        r0 = r45;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x07b3;
    L_0x0792:
        r4 = "t.me";
        r0 = r45;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x07b3;
    L_0x079d:
        r4 = "telegram.dog";
        r0 = r45;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x07b3;
    L_0x07a8:
        r4 = "telesco.pe";
        r0 = r45;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x07de;
    L_0x07b3:
        r58 = r34.getPath();
        if (r58 == 0) goto L_0x07de;
    L_0x07b9:
        r4 = r58.length();
        r5 = 1;
        if (r4 <= r5) goto L_0x07de;
    L_0x07c0:
        r4 = 1;
        r0 = r58;
        r58 = r0.substring(r4);
        r4 = "joinchat/";
        r0 = r58;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0828;
    L_0x07d2:
        r4 = "joinchat/";
        r5 = "";
        r0 = r58;
        r7 = r0.replace(r4, r5);
    L_0x07de:
        if (r11 == 0) goto L_0x07fd;
    L_0x07e0:
        r4 = "@";
        r4 = r11.startsWith(r4);
        if (r4 == 0) goto L_0x07fd;
    L_0x07e9:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = " ";
        r4 = r4.append(r5);
        r4 = r4.append(r11);
        r11 = r4.toString();
    L_0x07fd:
        if (r59 != 0) goto L_0x0801;
    L_0x07ff:
        if (r60 == 0) goto L_0x0bec;
    L_0x0801:
        r24 = new android.os.Bundle;
        r24.<init>();
        r4 = "phone";
        r0 = r24;
        r1 = r59;
        r0.putString(r4, r1);
        r4 = "hash";
        r0 = r24;
        r1 = r60;
        r0.putString(r4, r1);
        r4 = new org.telegram.ui.LaunchActivity$7;
        r0 = r85;
        r1 = r24;
        r4.<init>(r1);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r4);
        goto L_0x02c1;
    L_0x0828:
        r4 = "addstickers/";
        r0 = r58;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0840;
    L_0x0833:
        r4 = "addstickers/";
        r5 = "";
        r0 = r58;
        r8 = r0.replace(r4, r5);
        goto L_0x07de;
    L_0x0840:
        r4 = "iv/";
        r0 = r58;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0878;
    L_0x084b:
        r4 = 0;
        r5 = "url";
        r0 = r34;
        r5 = r0.getQueryParameter(r5);
        r15[r4] = r5;
        r4 = 1;
        r5 = "rhash";
        r0 = r34;
        r5 = r0.getQueryParameter(r5);
        r15[r4] = r5;
        r4 = 0;
        r4 = r15[r4];
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 != 0) goto L_0x0875;
    L_0x086c:
        r4 = 1;
        r4 = r15[r4];
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 == 0) goto L_0x07de;
    L_0x0875:
        r15 = 0;
        goto L_0x07de;
    L_0x0878:
        r4 = "msg/";
        r0 = r58;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x088e;
    L_0x0883:
        r4 = "share/";
        r0 = r58;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0900;
    L_0x088e:
        r4 = "url";
        r0 = r34;
        r11 = r0.getQueryParameter(r4);
        if (r11 != 0) goto L_0x089c;
    L_0x0899:
        r11 = "";
    L_0x089c:
        r4 = "text";
        r0 = r34;
        r4 = r0.getQueryParameter(r4);
        if (r4 == 0) goto L_0x08dc;
    L_0x08a7:
        r4 = r11.length();
        if (r4 <= 0) goto L_0x08c2;
    L_0x08ad:
        r12 = 1;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4 = r4.append(r11);
        r5 = "\n";
        r4 = r4.append(r5);
        r11 = r4.toString();
    L_0x08c2:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4 = r4.append(r11);
        r5 = "text";
        r0 = r34;
        r5 = r0.getQueryParameter(r5);
        r4 = r4.append(r5);
        r11 = r4.toString();
    L_0x08dc:
        r4 = r11.length();
        r5 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        if (r4 <= r5) goto L_0x08eb;
    L_0x08e4:
        r4 = 0;
        r5 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        r11 = r11.substring(r4, r5);
    L_0x08eb:
        r4 = "\n";
        r4 = r11.endsWith(r4);
        if (r4 == 0) goto L_0x07de;
    L_0x08f4:
        r4 = 0;
        r5 = r11.length();
        r5 = r5 + -1;
        r11 = r11.substring(r4, r5);
        goto L_0x08eb;
    L_0x0900:
        r4 = "confirmphone";
        r0 = r58;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x091f;
    L_0x090b:
        r4 = "phone";
        r0 = r34;
        r59 = r0.getQueryParameter(r4);
        r4 = "hash";
        r0 = r34;
        r60 = r0.getQueryParameter(r4);
        goto L_0x07de;
    L_0x091f:
        r4 = r58.length();
        r5 = 1;
        if (r4 < r5) goto L_0x07de;
    L_0x0926:
        r68 = r34.getPathSegments();
        r4 = r68.size();
        if (r4 <= 0) goto L_0x0954;
    L_0x0930:
        r4 = 0;
        r0 = r68;
        r6 = r0.get(r4);
        r6 = (java.lang.String) r6;
        r4 = r68.size();
        r5 = 1;
        if (r4 <= r5) goto L_0x0954;
    L_0x0940:
        r4 = 1;
        r0 = r68;
        r4 = r0.get(r4);
        r4 = (java.lang.String) r4;
        r13 = org.telegram.messenger.Utilities.parseInt(r4);
        r4 = r13.intValue();
        if (r4 != 0) goto L_0x0954;
    L_0x0953:
        r13 = 0;
    L_0x0954:
        r4 = "start";
        r0 = r34;
        r9 = r0.getQueryParameter(r4);
        r4 = "startgroup";
        r0 = r34;
        r10 = r0.getQueryParameter(r4);
        r4 = "game";
        r0 = r34;
        r14 = r0.getQueryParameter(r4);
        goto L_0x07de;
    L_0x0971:
        r4 = "tg";
        r0 = r67;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x07de;
    L_0x097c:
        r79 = r34.toString();
        r4 = "tg:resolve";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0996;
    L_0x098b:
        r4 = "tg://resolve";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x09ec;
    L_0x0996:
        r4 = "tg:resolve";
        r5 = "tg://telegram.org";
        r0 = r79;
        r4 = r0.replace(r4, r5);
        r5 = "tg://resolve";
        r16 = "tg://telegram.org";
        r0 = r16;
        r79 = r4.replace(r5, r0);
        r34 = android.net.Uri.parse(r79);
        r4 = "domain";
        r0 = r34;
        r6 = r0.getQueryParameter(r4);
        r4 = "start";
        r0 = r34;
        r9 = r0.getQueryParameter(r4);
        r4 = "startgroup";
        r0 = r34;
        r10 = r0.getQueryParameter(r4);
        r4 = "game";
        r0 = r34;
        r14 = r0.getQueryParameter(r4);
        r4 = "post";
        r0 = r34;
        r4 = r0.getQueryParameter(r4);
        r13 = org.telegram.messenger.Utilities.parseInt(r4);
        r4 = r13.intValue();
        if (r4 != 0) goto L_0x07de;
    L_0x09e9:
        r13 = 0;
        goto L_0x07de;
    L_0x09ec:
        r4 = "tg:join";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0a02;
    L_0x09f7:
        r4 = "tg://join";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0a29;
    L_0x0a02:
        r4 = "tg:join";
        r5 = "tg://telegram.org";
        r0 = r79;
        r4 = r0.replace(r4, r5);
        r5 = "tg://join";
        r16 = "tg://telegram.org";
        r0 = r16;
        r79 = r4.replace(r5, r0);
        r34 = android.net.Uri.parse(r79);
        r4 = "invite";
        r0 = r34;
        r7 = r0.getQueryParameter(r4);
        goto L_0x07de;
    L_0x0a29:
        r4 = "tg:addstickers";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0a3f;
    L_0x0a34:
        r4 = "tg://addstickers";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0a66;
    L_0x0a3f:
        r4 = "tg:addstickers";
        r5 = "tg://telegram.org";
        r0 = r79;
        r4 = r0.replace(r4, r5);
        r5 = "tg://addstickers";
        r16 = "tg://telegram.org";
        r0 = r16;
        r79 = r4.replace(r5, r0);
        r34 = android.net.Uri.parse(r79);
        r4 = "set";
        r0 = r34;
        r8 = r0.getQueryParameter(r4);
        goto L_0x07de;
    L_0x0a66:
        r4 = "tg:msg";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0a92;
    L_0x0a71:
        r4 = "tg://msg";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0a92;
    L_0x0a7c:
        r4 = "tg://share";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0a92;
    L_0x0a87:
        r4 = "tg:share";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0b38;
    L_0x0a92:
        r4 = "tg:msg";
        r5 = "tg://telegram.org";
        r0 = r79;
        r4 = r0.replace(r4, r5);
        r5 = "tg://msg";
        r16 = "tg://telegram.org";
        r0 = r16;
        r4 = r4.replace(r5, r0);
        r5 = "tg://share";
        r16 = "tg://telegram.org";
        r0 = r16;
        r4 = r4.replace(r5, r0);
        r5 = "tg:share";
        r16 = "tg://telegram.org";
        r0 = r16;
        r79 = r4.replace(r5, r0);
        r34 = android.net.Uri.parse(r79);
        r4 = "url";
        r0 = r34;
        r11 = r0.getQueryParameter(r4);
        if (r11 != 0) goto L_0x0ad4;
    L_0x0ad1:
        r11 = "";
    L_0x0ad4:
        r4 = "text";
        r0 = r34;
        r4 = r0.getQueryParameter(r4);
        if (r4 == 0) goto L_0x0b14;
    L_0x0adf:
        r4 = r11.length();
        if (r4 <= 0) goto L_0x0afa;
    L_0x0ae5:
        r12 = 1;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4 = r4.append(r11);
        r5 = "\n";
        r4 = r4.append(r5);
        r11 = r4.toString();
    L_0x0afa:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4 = r4.append(r11);
        r5 = "text";
        r0 = r34;
        r5 = r0.getQueryParameter(r5);
        r4 = r4.append(r5);
        r11 = r4.toString();
    L_0x0b14:
        r4 = r11.length();
        r5 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        if (r4 <= r5) goto L_0x0b23;
    L_0x0b1c:
        r4 = 0;
        r5 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        r11 = r11.substring(r4, r5);
    L_0x0b23:
        r4 = "\n";
        r4 = r11.endsWith(r4);
        if (r4 == 0) goto L_0x07de;
    L_0x0b2c:
        r4 = 0;
        r5 = r11.length();
        r5 = r5 + -1;
        r11 = r11.substring(r4, r5);
        goto L_0x0b23;
    L_0x0b38:
        r4 = "tg:confirmphone";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0b4e;
    L_0x0b43:
        r4 = "tg://confirmphone";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0b7e;
    L_0x0b4e:
        r4 = "tg:confirmphone";
        r5 = "tg://telegram.org";
        r0 = r79;
        r4 = r0.replace(r4, r5);
        r5 = "tg://confirmphone";
        r16 = "tg://telegram.org";
        r0 = r16;
        r79 = r4.replace(r5, r0);
        r34 = android.net.Uri.parse(r79);
        r4 = "phone";
        r0 = r34;
        r59 = r0.getQueryParameter(r4);
        r4 = "hash";
        r0 = r34;
        r60 = r0.getQueryParameter(r4);
        goto L_0x07de;
    L_0x0b7e:
        r4 = "tg:openmessage";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0b94;
    L_0x0b89:
        r4 = "tg://openmessage";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x07de;
    L_0x0b94:
        r4 = "tg:openmessage";
        r5 = "tg://telegram.org";
        r0 = r79;
        r4 = r0.replace(r4, r5);
        r5 = "tg://openmessage";
        r16 = "tg://telegram.org";
        r0 = r16;
        r79 = r4.replace(r5, r0);
        r34 = android.net.Uri.parse(r79);
        r4 = "user_id";
        r0 = r34;
        r81 = r0.getQueryParameter(r4);
        r4 = "chat_id";
        r0 = r34;
        r29 = r0.getQueryParameter(r4);
        r4 = "message_id";
        r0 = r34;
        r49 = r0.getQueryParameter(r4);
        if (r81 == 0) goto L_0x0be1;
    L_0x0bcd:
        r4 = java.lang.Integer.parseInt(r81);	 Catch:{ NumberFormatException -> 0x11ea }
        r65 = java.lang.Integer.valueOf(r4);	 Catch:{ NumberFormatException -> 0x11ea }
    L_0x0bd5:
        if (r49 == 0) goto L_0x07de;
    L_0x0bd7:
        r4 = java.lang.Integer.parseInt(r49);	 Catch:{ NumberFormatException -> 0x11e4 }
        r64 = java.lang.Integer.valueOf(r4);	 Catch:{ NumberFormatException -> 0x11e4 }
        goto L_0x07de;
    L_0x0be1:
        if (r29 == 0) goto L_0x0bd5;
    L_0x0be3:
        r4 = java.lang.Integer.parseInt(r29);	 Catch:{ NumberFormatException -> 0x11e7 }
        r62 = java.lang.Integer.valueOf(r4);	 Catch:{ NumberFormatException -> 0x11e7 }
        goto L_0x0bd5;
    L_0x0bec:
        if (r6 != 0) goto L_0x0bf8;
    L_0x0bee:
        if (r7 != 0) goto L_0x0bf8;
    L_0x0bf0:
        if (r8 != 0) goto L_0x0bf8;
    L_0x0bf2:
        if (r11 != 0) goto L_0x0bf8;
    L_0x0bf4:
        if (r14 != 0) goto L_0x0bf8;
    L_0x0bf6:
        if (r15 == 0) goto L_0x0c04;
    L_0x0bf8:
        r4 = 0;
        r5 = r47[r4];
        r16 = 0;
        r4 = r85;
        r4.runLinkRequest(r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16);
        goto L_0x02c1;
    L_0x0c04:
        r16 = r85.getContentResolver();	 Catch:{ Exception -> 0x0c84 }
        r17 = r86.getData();	 Catch:{ Exception -> 0x0c84 }
        r18 = 0;
        r19 = 0;
        r20 = 0;
        r21 = 0;
        r33 = r16.query(r17, r18, r19, r20, r21);	 Catch:{ Exception -> 0x0c84 }
        if (r33 == 0) goto L_0x02c1;
    L_0x0c1a:
        r4 = r33.moveToFirst();	 Catch:{ Exception -> 0x0c84 }
        if (r4 == 0) goto L_0x0c7f;
    L_0x0c20:
        r4 = "account_name";
        r0 = r33;
        r4 = r0.getColumnIndex(r4);	 Catch:{ Exception -> 0x0c84 }
        r0 = r33;
        r4 = r0.getString(r4);	 Catch:{ Exception -> 0x0c84 }
        r4 = org.telegram.messenger.Utilities.parseInt(r4);	 Catch:{ Exception -> 0x0c84 }
        r23 = r4.intValue();	 Catch:{ Exception -> 0x0c84 }
        r22 = 0;
    L_0x0c39:
        r4 = 3;
        r0 = r22;
        if (r0 >= r4) goto L_0x0c56;
    L_0x0c3e:
        r4 = org.telegram.messenger.UserConfig.getInstance(r22);	 Catch:{ Exception -> 0x0c84 }
        r4 = r4.getClientUserId();	 Catch:{ Exception -> 0x0c84 }
        r0 = r23;
        if (r4 != r0) goto L_0x0c8a;
    L_0x0c4a:
        r4 = 0;
        r47[r4] = r22;	 Catch:{ Exception -> 0x0c84 }
        r4 = 0;
        r4 = r47[r4];	 Catch:{ Exception -> 0x0c84 }
        r5 = 1;
        r0 = r85;
        r0.switchToAccount(r4, r5);	 Catch:{ Exception -> 0x0c84 }
    L_0x0c56:
        r4 = "DATA4";
        r0 = r33;
        r4 = r0.getColumnIndex(r4);	 Catch:{ Exception -> 0x0c84 }
        r0 = r33;
        r82 = r0.getInt(r4);	 Catch:{ Exception -> 0x0c84 }
        r4 = 0;
        r4 = r47[r4];	 Catch:{ Exception -> 0x0c84 }
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);	 Catch:{ Exception -> 0x0c84 }
        r5 = org.telegram.messenger.NotificationCenter.closeChats;	 Catch:{ Exception -> 0x0c84 }
        r16 = 0;
        r0 = r16;
        r0 = new java.lang.Object[r0];	 Catch:{ Exception -> 0x0c84 }
        r16 = r0;
        r0 = r16;
        r4.postNotificationName(r5, r0);	 Catch:{ Exception -> 0x0c84 }
        r65 = java.lang.Integer.valueOf(r82);	 Catch:{ Exception -> 0x0c84 }
    L_0x0c7f:
        r33.close();	 Catch:{ Exception -> 0x0c84 }
        goto L_0x02c1;
    L_0x0c84:
        r40 = move-exception;
        org.telegram.messenger.FileLog.m3e(r40);
        goto L_0x02c1;
    L_0x0c8a:
        r22 = r22 + 1;
        goto L_0x0c39;
    L_0x0c8d:
        r4 = r86.getAction();
        r5 = "org.telegram.messenger.OPEN_ACCOUNT";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0ca1;
    L_0x0c9a:
        r4 = 1;
        r53 = java.lang.Integer.valueOf(r4);
        goto L_0x02c1;
    L_0x0ca1:
        r4 = r86.getAction();
        r5 = "new_dialog";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0cb5;
    L_0x0cae:
        r4 = 1;
        r52 = java.lang.Integer.valueOf(r4);
        goto L_0x02c1;
    L_0x0cb5:
        r4 = r86.getAction();
        r5 = "com.tmessages.openchat";
        r4 = r4.startsWith(r5);
        if (r4 == 0) goto L_0x0d3e;
    L_0x0cc2:
        r4 = "chatId";
        r5 = 0;
        r0 = r86;
        r30 = r0.getIntExtra(r4, r5);
        r4 = "userId";
        r5 = 0;
        r0 = r86;
        r82 = r0.getIntExtra(r4, r5);
        r4 = "encId";
        r5 = 0;
        r0 = r86;
        r41 = r0.getIntExtra(r4, r5);
        if (r30 == 0) goto L_0x0cfe;
    L_0x0ce2:
        r4 = 0;
        r4 = r47[r4];
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
        r5 = org.telegram.messenger.NotificationCenter.closeChats;
        r16 = 0;
        r0 = r16;
        r0 = new java.lang.Object[r0];
        r16 = r0;
        r0 = r16;
        r4.postNotificationName(r5, r0);
        r62 = java.lang.Integer.valueOf(r30);
        goto L_0x02c1;
    L_0x0cfe:
        if (r82 == 0) goto L_0x0d1c;
    L_0x0d00:
        r4 = 0;
        r4 = r47[r4];
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
        r5 = org.telegram.messenger.NotificationCenter.closeChats;
        r16 = 0;
        r0 = r16;
        r0 = new java.lang.Object[r0];
        r16 = r0;
        r0 = r16;
        r4.postNotificationName(r5, r0);
        r65 = java.lang.Integer.valueOf(r82);
        goto L_0x02c1;
    L_0x0d1c:
        if (r41 == 0) goto L_0x0d3a;
    L_0x0d1e:
        r4 = 0;
        r4 = r47[r4];
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
        r5 = org.telegram.messenger.NotificationCenter.closeChats;
        r16 = 0;
        r0 = r16;
        r0 = new java.lang.Object[r0];
        r16 = r0;
        r0 = r16;
        r4.postNotificationName(r5, r0);
        r63 = java.lang.Integer.valueOf(r41);
        goto L_0x02c1;
    L_0x0d3a:
        r69 = 1;
        goto L_0x02c1;
    L_0x0d3e:
        r4 = r86.getAction();
        r5 = "com.tmessages.openplayer";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0d4f;
    L_0x0d4b:
        r71 = 1;
        goto L_0x02c1;
    L_0x0d4f:
        r4 = r86.getAction();
        r5 = "org.tmessages.openlocations";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x02c1;
    L_0x0d5c:
        r70 = 1;
        goto L_0x02c1;
    L_0x0d60:
        r4 = r62.intValue();
        if (r4 == 0) goto L_0x0dd4;
    L_0x0d66:
        r24 = new android.os.Bundle;
        r24.<init>();
        r4 = "chat_id";
        r5 = r62.intValue();
        r0 = r24;
        r0.putInt(r4, r5);
        r4 = r64.intValue();
        if (r4 == 0) goto L_0x0d89;
    L_0x0d7d:
        r4 = "message_id";
        r5 = r64.intValue();
        r0 = r24;
        r0.putInt(r4, r5);
    L_0x0d89:
        r4 = mainFragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0db2;
    L_0x0d91:
        r4 = 0;
        r4 = r47[r4];
        r5 = org.telegram.messenger.MessagesController.getInstance(r4);
        r4 = mainFragmentsStack;
        r16 = mainFragmentsStack;
        r16 = r16.size();
        r16 = r16 + -1;
        r0 = r16;
        r4 = r4.get(r0);
        r4 = (org.telegram.ui.ActionBar.BaseFragment) r4;
        r0 = r24;
        r4 = r5.checkCanOpenChat(r0, r4);
        if (r4 == 0) goto L_0x0333;
    L_0x0db2:
        r44 = new org.telegram.ui.ChatActivity;
        r0 = r44;
        r1 = r24;
        r0.<init>(r1);
        r0 = r85;
        r4 = r0.actionBarLayout;
        r5 = 0;
        r16 = 1;
        r17 = 1;
        r0 = r44;
        r1 = r16;
        r2 = r17;
        r4 = r4.presentFragment(r0, r5, r1, r2);
        if (r4 == 0) goto L_0x0333;
    L_0x0dd0:
        r61 = 1;
        goto L_0x0333;
    L_0x0dd4:
        r4 = r63.intValue();
        if (r4 == 0) goto L_0x0e0d;
    L_0x0dda:
        r24 = new android.os.Bundle;
        r24.<init>();
        r4 = "enc_id";
        r5 = r63.intValue();
        r0 = r24;
        r0.putInt(r4, r5);
        r44 = new org.telegram.ui.ChatActivity;
        r0 = r44;
        r1 = r24;
        r0.<init>(r1);
        r0 = r85;
        r4 = r0.actionBarLayout;
        r5 = 0;
        r16 = 1;
        r17 = 1;
        r0 = r44;
        r1 = r16;
        r2 = r17;
        r4 = r4.presentFragment(r0, r5, r1, r2);
        if (r4 == 0) goto L_0x0333;
    L_0x0e09:
        r61 = 1;
        goto L_0x0333;
    L_0x0e0d:
        if (r69 == 0) goto L_0x0e63;
    L_0x0e0f:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 != 0) goto L_0x0e22;
    L_0x0e15:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4.removeAllFragments();
    L_0x0e1c:
        r61 = 0;
        r87 = 0;
        goto L_0x0333;
    L_0x0e22:
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0e1c;
    L_0x0e2e:
        r22 = 0;
    L_0x0e30:
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.size();
        r4 = r4 + -1;
        if (r4 <= 0) goto L_0x0e5a;
    L_0x0e3e:
        r0 = r85;
        r5 = r0.layersActionBarLayout;
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r16 = 0;
        r0 = r16;
        r4 = r4.get(r0);
        r4 = (org.telegram.ui.ActionBar.BaseFragment) r4;
        r5.removeFragmentFromStack(r4);
        r22 = r22 + -1;
        r22 = r22 + 1;
        goto L_0x0e30;
    L_0x0e5a:
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r5 = 0;
        r4.closeLastFragment(r5);
        goto L_0x0e1c;
    L_0x0e63:
        if (r71 == 0) goto L_0x0e8e;
    L_0x0e65:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0e8a;
    L_0x0e71:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r5 = 0;
        r44 = r4.get(r5);
        r44 = (org.telegram.ui.ActionBar.BaseFragment) r44;
        r4 = new org.telegram.ui.Components.AudioPlayerAlert;
        r0 = r85;
        r4.<init>(r0);
        r0 = r44;
        r0.showDialog(r4);
    L_0x0e8a:
        r61 = 0;
        goto L_0x0333;
    L_0x0e8e:
        if (r70 == 0) goto L_0x0ec2;
    L_0x0e90:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0ebe;
    L_0x0e9c:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r5 = 0;
        r44 = r4.get(r5);
        r44 = (org.telegram.ui.ActionBar.BaseFragment) r44;
        r4 = new org.telegram.ui.Components.SharingLocationsAlert;
        r5 = new org.telegram.ui.LaunchActivity$8;
        r0 = r85;
        r1 = r47;
        r5.<init>(r1);
        r0 = r85;
        r4.<init>(r0, r5);
        r0 = r44;
        r0.showDialog(r4);
    L_0x0ebe:
        r61 = 0;
        goto L_0x0333;
    L_0x0ec2:
        r0 = r85;
        r4 = r0.videoPath;
        if (r4 != 0) goto L_0x0ee6;
    L_0x0ec8:
        r0 = r85;
        r4 = r0.photoPathsArray;
        if (r4 != 0) goto L_0x0ee6;
    L_0x0ece:
        r0 = r85;
        r4 = r0.sendingText;
        if (r4 != 0) goto L_0x0ee6;
    L_0x0ed4:
        r0 = r85;
        r4 = r0.documentsPathsArray;
        if (r4 != 0) goto L_0x0ee6;
    L_0x0eda:
        r0 = r85;
        r4 = r0.contactsToSend;
        if (r4 != 0) goto L_0x0ee6;
    L_0x0ee0:
        r0 = r85;
        r4 = r0.documentsUrisArray;
        if (r4 == 0) goto L_0x10a6;
    L_0x0ee6:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 != 0) goto L_0x0f02;
    L_0x0eec:
        r4 = 0;
        r4 = r47[r4];
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
        r5 = org.telegram.messenger.NotificationCenter.closeChats;
        r16 = 0;
        r0 = r16;
        r0 = new java.lang.Object[r0];
        r16 = r0;
        r0 = r16;
        r4.postNotificationName(r5, r0);
    L_0x0f02:
        r4 = 0;
        r4 = (r36 > r4 ? 1 : (r36 == r4 ? 0 : -1));
        if (r4 != 0) goto L_0x1089;
    L_0x0f08:
        r24 = new android.os.Bundle;
        r24.<init>();
        r4 = "onlySelect";
        r5 = 1;
        r0 = r24;
        r0.putBoolean(r4, r5);
        r4 = "dialogsType";
        r5 = 3;
        r0 = r24;
        r0.putInt(r4, r5);
        r4 = "allowSwitchAccount";
        r5 = 1;
        r0 = r24;
        r0.putBoolean(r4, r5);
        r0 = r85;
        r4 = r0.contactsToSend;
        if (r4 == 0) goto L_0x0fe4;
    L_0x0f2e:
        r4 = "selectAlertString";
        r5 = "SendContactTo";
        r16 = 2131494349; // 0x7f0c05cd float:1.8612204E38 double:1.053098132E-314;
        r0 = r16;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r0);
        r0 = r24;
        r0.putString(r4, r5);
        r4 = "selectAlertStringGroup";
        r5 = "SendContactToGroup";
        r16 = 2131494336; // 0x7f0c05c0 float:1.8612178E38 double:1.0530981257E-314;
        r0 = r16;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r0);
        r0 = r24;
        r0.putString(r4, r5);
    L_0x0f56:
        r44 = new org.telegram.ui.DialogsActivity;
        r0 = r44;
        r1 = r24;
        r0.<init>(r1);
        r0 = r44;
        r1 = r85;
        r0.setDelegate(r1);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x1011;
    L_0x0f6c:
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.size();
        if (r4 <= 0) goto L_0x100e;
    L_0x0f78:
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r0 = r85;
        r5 = r0.layersActionBarLayout;
        r5 = r5.fragmentsStack;
        r5 = r5.size();
        r5 = r5 + -1;
        r4 = r4.get(r5);
        r4 = r4 instanceof org.telegram.ui.DialogsActivity;
        if (r4 == 0) goto L_0x100e;
    L_0x0f92:
        r66 = 1;
    L_0x0f94:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r5 = 1;
        r16 = 1;
        r0 = r44;
        r1 = r66;
        r2 = r16;
        r4.presentFragment(r0, r1, r5, r2);
        r61 = 1;
        r4 = org.telegram.ui.SecretMediaViewer.hasInstance();
        if (r4 == 0) goto L_0x103f;
    L_0x0fac:
        r4 = org.telegram.ui.SecretMediaViewer.getInstance();
        r4 = r4.isVisible();
        if (r4 == 0) goto L_0x103f;
    L_0x0fb6:
        r4 = org.telegram.ui.SecretMediaViewer.getInstance();
        r5 = 0;
        r16 = 0;
        r0 = r16;
        r4.closePhoto(r5, r0);
    L_0x0fc2:
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 0;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x107b;
    L_0x0fd4:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4.showLastFragment();
        r0 = r85;
        r4 = r0.rightActionBarLayout;
        r4.showLastFragment();
        goto L_0x0333;
    L_0x0fe4:
        r4 = "selectAlertString";
        r5 = "SendMessagesTo";
        r16 = 2131494349; // 0x7f0c05cd float:1.8612204E38 double:1.053098132E-314;
        r0 = r16;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r0);
        r0 = r24;
        r0.putString(r4, r5);
        r4 = "selectAlertStringGroup";
        r5 = "SendMessagesToGroup";
        r16 = 2131494350; // 0x7f0c05ce float:1.8612206E38 double:1.0530981326E-314;
        r0 = r16;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r0);
        r0 = r24;
        r0.putString(r4, r5);
        goto L_0x0f56;
    L_0x100e:
        r66 = 0;
        goto L_0x0f94;
    L_0x1011:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.size();
        r5 = 1;
        if (r4 <= r5) goto L_0x103c;
    L_0x101e:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r0 = r85;
        r5 = r0.actionBarLayout;
        r5 = r5.fragmentsStack;
        r5 = r5.size();
        r5 = r5 + -1;
        r4 = r4.get(r5);
        r4 = r4 instanceof org.telegram.ui.DialogsActivity;
        if (r4 == 0) goto L_0x103c;
    L_0x1038:
        r66 = 1;
    L_0x103a:
        goto L_0x0f94;
    L_0x103c:
        r66 = 0;
        goto L_0x103a;
    L_0x103f:
        r4 = org.telegram.ui.PhotoViewer.hasInstance();
        if (r4 == 0) goto L_0x105d;
    L_0x1045:
        r4 = org.telegram.ui.PhotoViewer.getInstance();
        r4 = r4.isVisible();
        if (r4 == 0) goto L_0x105d;
    L_0x104f:
        r4 = org.telegram.ui.PhotoViewer.getInstance();
        r5 = 0;
        r16 = 1;
        r0 = r16;
        r4.closePhoto(r5, r0);
        goto L_0x0fc2;
    L_0x105d:
        r4 = org.telegram.ui.ArticleViewer.hasInstance();
        if (r4 == 0) goto L_0x0fc2;
    L_0x1063:
        r4 = org.telegram.ui.ArticleViewer.getInstance();
        r4 = r4.isVisible();
        if (r4 == 0) goto L_0x0fc2;
    L_0x106d:
        r4 = org.telegram.ui.ArticleViewer.getInstance();
        r5 = 0;
        r16 = 1;
        r0 = r16;
        r4.close(r5, r0);
        goto L_0x0fc2;
    L_0x107b:
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 1;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x0333;
    L_0x1089:
        r39 = new java.util.ArrayList;
        r39.<init>();
        r4 = java.lang.Long.valueOf(r36);
        r0 = r39;
        r0.add(r4);
        r4 = 0;
        r5 = 0;
        r16 = 0;
        r0 = r85;
        r1 = r39;
        r2 = r16;
        r0.didSelectDialogs(r4, r1, r5, r2);
        goto L_0x0333;
    L_0x10a6:
        r4 = r53.intValue();
        if (r4 == 0) goto L_0x10f5;
    L_0x10ac:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r5 = new org.telegram.ui.SettingsActivity;
        r5.<init>();
        r16 = 0;
        r17 = 1;
        r18 = 1;
        r0 = r16;
        r1 = r17;
        r2 = r18;
        r4.presentFragment(r5, r0, r1, r2);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x10e8;
    L_0x10ca:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4.showLastFragment();
        r0 = r85;
        r4 = r0.rightActionBarLayout;
        r4.showLastFragment();
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 0;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
    L_0x10e4:
        r61 = 1;
        goto L_0x0333;
    L_0x10e8:
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 1;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x10e4;
    L_0x10f5:
        r4 = r52.intValue();
        if (r4 == 0) goto L_0x0333;
    L_0x10fb:
        r24 = new android.os.Bundle;
        r24.<init>();
        r4 = "destroyAfterSelect";
        r5 = 1;
        r0 = r24;
        r0.putBoolean(r4, r5);
        r0 = r85;
        r4 = r0.actionBarLayout;
        r5 = new org.telegram.ui.ContactsActivity;
        r0 = r24;
        r5.<init>(r0);
        r16 = 0;
        r17 = 1;
        r18 = 1;
        r0 = r16;
        r1 = r17;
        r2 = r18;
        r4.presentFragment(r5, r0, r1, r2);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x1147;
    L_0x1129:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4.showLastFragment();
        r0 = r85;
        r4 = r0.rightActionBarLayout;
        r4.showLastFragment();
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 0;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
    L_0x1143:
        r61 = 1;
        goto L_0x0333;
    L_0x1147:
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 1;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x1143;
    L_0x1154:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x036f;
    L_0x1160:
        r38 = new org.telegram.ui.DialogsActivity;
        r4 = 0;
        r0 = r38;
        r0.<init>(r4);
        r0 = r85;
        r4 = r0.sideMenu;
        r0 = r38;
        r0.setSideMenu(r4);
        r0 = r85;
        r4 = r0.actionBarLayout;
        r0 = r38;
        r4.addFragmentToStack(r0);
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 1;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x036f;
    L_0x1188:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x036f;
    L_0x1194:
        r0 = r85;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r4 = r4.isClientActivated();
        if (r4 != 0) goto L_0x11bc;
    L_0x11a2:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r5 = new org.telegram.ui.LoginActivity;
        r5.<init>();
        r4.addFragmentToStack(r5);
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 0;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x036f;
    L_0x11bc:
        r38 = new org.telegram.ui.DialogsActivity;
        r4 = 0;
        r0 = r38;
        r0.<init>(r4);
        r0 = r85;
        r4 = r0.sideMenu;
        r0 = r38;
        r0.setSideMenu(r4);
        r0 = r85;
        r4 = r0.actionBarLayout;
        r0 = r38;
        r4.addFragmentToStack(r0);
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 1;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x036f;
    L_0x11e4:
        r4 = move-exception;
        goto L_0x07de;
    L_0x11e7:
        r4 = move-exception;
        goto L_0x0bd5;
    L_0x11ea:
        r4 = move-exception;
        goto L_0x0bd5;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.LaunchActivity.handleIntent(android.content.Intent, boolean, boolean, boolean):boolean");
    }

    private void runLinkRequest(int intentAccount, String username, String group, String sticker, String botUser, String botChat, String message, boolean hasUrl, Integer messageId, String game, String[] instantView, int state) {
        final AlertDialog progressDialog = new AlertDialog(this, 1);
        progressDialog.setMessage(LocaleController.getString("Loading", C0488R.string.Loading));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        int requestId = 0;
        TLObject req;
        final String str;
        final String str2;
        if (username != null) {
            req = new TL_contacts_resolveUsername();
            req.username = username;
            final String str3 = game;
            final int i = intentAccount;
            str = botChat;
            str2 = botUser;
            final Integer num = messageId;
            requestId = ConnectionsManager.getInstance(intentAccount).sendRequest(req, new RequestDelegate() {
                public void run(final TLObject response, final TL_error error) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (!LaunchActivity.this.isFinishing()) {
                                try {
                                    progressDialog.dismiss();
                                } catch (Throwable e) {
                                    FileLog.m3e(e);
                                }
                                final TL_contacts_resolvedPeer res = response;
                                if (error != null || LaunchActivity.this.actionBarLayout == null || (str3 != null && (str3 == null || res.users.isEmpty()))) {
                                    try {
                                        Toast.makeText(LaunchActivity.this, LocaleController.getString("NoUsernameFound", C0488R.string.NoUsernameFound), 0).show();
                                        return;
                                    } catch (Throwable e2) {
                                        FileLog.m3e(e2);
                                        return;
                                    }
                                }
                                MessagesController.getInstance(i).putUsers(res.users, false);
                                MessagesController.getInstance(i).putChats(res.chats, false);
                                MessagesStorage.getInstance(i).putUsersAndChats(res.users, res.chats, false, true);
                                Bundle args;
                                DialogsActivity fragment;
                                if (str3 != null) {
                                    args = new Bundle();
                                    args.putBoolean("onlySelect", true);
                                    args.putBoolean("cantSendToChannels", true);
                                    args.putInt("dialogsType", 1);
                                    args.putString("selectAlertString", LocaleController.getString("SendGameTo", C0488R.string.SendGameTo));
                                    args.putString("selectAlertStringGroup", LocaleController.getString("SendGameToGroup", C0488R.string.SendGameToGroup));
                                    fragment = new DialogsActivity(args);
                                    fragment.setDelegate(new DialogsActivityDelegate() {
                                        public void didSelectDialogs(DialogsActivity fragment, ArrayList<Long> dids, CharSequence message, boolean param) {
                                            long did = ((Long) dids.get(0)).longValue();
                                            TL_inputMediaGame inputMediaGame = new TL_inputMediaGame();
                                            inputMediaGame.id = new TL_inputGameShortName();
                                            inputMediaGame.id.short_name = str3;
                                            inputMediaGame.id.bot_id = MessagesController.getInstance(i).getInputUser((User) res.users.get(0));
                                            SendMessagesHelper.getInstance(i).sendGame(MessagesController.getInstance(i).getInputPeer((int) did), inputMediaGame, 0, 0);
                                            Bundle args = new Bundle();
                                            args.putBoolean("scrollToTopOnResume", true);
                                            int lower_part = (int) did;
                                            int high_id = (int) (did >> 32);
                                            if (lower_part == 0) {
                                                args.putInt("enc_id", high_id);
                                            } else if (high_id == 1) {
                                                args.putInt("chat_id", lower_part);
                                            } else if (lower_part > 0) {
                                                args.putInt("user_id", lower_part);
                                            } else if (lower_part < 0) {
                                                args.putInt("chat_id", -lower_part);
                                            }
                                            if (MessagesController.getInstance(i).checkCanOpenChat(args, fragment)) {
                                                NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                LaunchActivity.this.actionBarLayout.presentFragment(new ChatActivity(args), true, false, true);
                                            }
                                        }
                                    });
                                    boolean removeLast = AndroidUtilities.isTablet() ? LaunchActivity.this.layersActionBarLayout.fragmentsStack.size() > 0 && (LaunchActivity.this.layersActionBarLayout.fragmentsStack.get(LaunchActivity.this.layersActionBarLayout.fragmentsStack.size() - 1) instanceof DialogsActivity) : LaunchActivity.this.actionBarLayout.fragmentsStack.size() > 1 && (LaunchActivity.this.actionBarLayout.fragmentsStack.get(LaunchActivity.this.actionBarLayout.fragmentsStack.size() - 1) instanceof DialogsActivity);
                                    LaunchActivity.this.actionBarLayout.presentFragment(fragment, removeLast, true, true);
                                    if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
                                        SecretMediaViewer.getInstance().closePhoto(false, false);
                                    } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                                        PhotoViewer.getInstance().closePhoto(false, true);
                                    } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                                        ArticleViewer.getInstance().close(false, true);
                                    }
                                    LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                    if (AndroidUtilities.isTablet()) {
                                        LaunchActivity.this.actionBarLayout.showLastFragment();
                                        LaunchActivity.this.rightActionBarLayout.showLastFragment();
                                        return;
                                    }
                                    LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                } else if (str != null) {
                                    final User user = !res.users.isEmpty() ? (User) res.users.get(0) : null;
                                    if (user == null || (user.bot && user.bot_nochats)) {
                                        try {
                                            Toast.makeText(LaunchActivity.this, LocaleController.getString("BotCantJoinGroups", C0488R.string.BotCantJoinGroups), 0).show();
                                            return;
                                        } catch (Throwable e22) {
                                            FileLog.m3e(e22);
                                            return;
                                        }
                                    }
                                    args = new Bundle();
                                    args.putBoolean("onlySelect", true);
                                    args.putInt("dialogsType", 2);
                                    args.putString("addToGroupAlertString", LocaleController.formatString("AddToTheGroupTitle", C0488R.string.AddToTheGroupTitle, UserObject.getUserName(user), "%1$s"));
                                    fragment = new DialogsActivity(args);
                                    fragment.setDelegate(new DialogsActivityDelegate() {
                                        public void didSelectDialogs(DialogsActivity fragment, ArrayList<Long> dids, CharSequence message, boolean param) {
                                            long did = ((Long) dids.get(0)).longValue();
                                            Bundle args = new Bundle();
                                            args.putBoolean("scrollToTopOnResume", true);
                                            args.putInt("chat_id", -((int) did));
                                            if (LaunchActivity.mainFragmentsStack.isEmpty() || MessagesController.getInstance(i).checkCanOpenChat(args, (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                                                NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                MessagesController.getInstance(i).addUserToChat(-((int) did), user, null, 0, str, null);
                                                LaunchActivity.this.actionBarLayout.presentFragment(new ChatActivity(args), true, false, true);
                                            }
                                        }
                                    });
                                    LaunchActivity.this.presentFragment(fragment);
                                } else {
                                    boolean isBot = false;
                                    args = new Bundle();
                                    long dialog_id;
                                    if (res.chats.isEmpty()) {
                                        args.putInt("user_id", ((User) res.users.get(0)).id);
                                        dialog_id = (long) ((User) res.users.get(0)).id;
                                    } else {
                                        args.putInt("chat_id", ((Chat) res.chats.get(0)).id);
                                        dialog_id = (long) (-((Chat) res.chats.get(0)).id);
                                    }
                                    if (str2 != null && res.users.size() > 0 && ((User) res.users.get(0)).bot) {
                                        args.putString("botUser", str2);
                                        isBot = true;
                                    }
                                    if (num != null) {
                                        args.putInt("message_id", num.intValue());
                                    }
                                    BaseFragment lastFragment = !LaunchActivity.mainFragmentsStack.isEmpty() ? (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1) : null;
                                    if (lastFragment != null && !MessagesController.getInstance(i).checkCanOpenChat(args, lastFragment)) {
                                        return;
                                    }
                                    if (isBot && lastFragment != null && (lastFragment instanceof ChatActivity) && ((ChatActivity) lastFragment).getDialogId() == dialog_id) {
                                        ((ChatActivity) lastFragment).setBotUser(str2);
                                        return;
                                    }
                                    ChatActivity fragment2 = new ChatActivity(args);
                                    NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                    LaunchActivity.this.actionBarLayout.presentFragment(fragment2, false, true, true);
                                }
                            }
                        }
                    });
                }
            });
        } else if (group != null) {
            if (state == 0) {
                req = new TL_messages_checkChatInvite();
                req.hash = group;
                final int i2 = intentAccount;
                final String str4 = group;
                str = username;
                str2 = sticker;
                final String str5 = botUser;
                final String str6 = botChat;
                final String str7 = message;
                final boolean z = hasUrl;
                final Integer num2 = messageId;
                final String str8 = game;
                final String[] strArr = instantView;
                requestId = ConnectionsManager.getInstance(intentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(final TLObject response, final TL_error error) {
                        AndroidUtilities.runOnUIThread(new Runnable() {

                            class C18771 implements DialogInterface.OnClickListener {
                                C18771() {
                                }

                                public void onClick(DialogInterface dialogInterface, int i) {
                                    LaunchActivity.this.runLinkRequest(i2, str, str4, str2, str5, str6, str7, z, num2, str8, strArr, 1);
                                }
                            }

                            public void run() {
                                if (!LaunchActivity.this.isFinishing()) {
                                    try {
                                        progressDialog.dismiss();
                                    } catch (Throwable e) {
                                        FileLog.m3e(e);
                                    }
                                    Builder builder;
                                    if (error != null || LaunchActivity.this.actionBarLayout == null) {
                                        builder = new Builder(LaunchActivity.this);
                                        builder.setTitle(LocaleController.getString("AppName", C0488R.string.AppName));
                                        if (error.text.startsWith("FLOOD_WAIT")) {
                                            builder.setMessage(LocaleController.getString("FloodWait", C0488R.string.FloodWait));
                                        } else {
                                            builder.setMessage(LocaleController.getString("JoinToGroupErrorNotExist", C0488R.string.JoinToGroupErrorNotExist));
                                        }
                                        builder.setPositiveButton(LocaleController.getString("OK", C0488R.string.OK), null);
                                        LaunchActivity.this.showAlertDialog(builder);
                                        return;
                                    }
                                    ChatInvite invite = response;
                                    if (invite.chat != null && !ChatObject.isLeftFromChat(invite.chat)) {
                                        MessagesController.getInstance(i2).putChat(invite.chat, false);
                                        ArrayList<Chat> chats = new ArrayList();
                                        chats.add(invite.chat);
                                        MessagesStorage.getInstance(i2).putUsersAndChats(null, chats, false, true);
                                        Bundle args = new Bundle();
                                        args.putInt("chat_id", invite.chat.id);
                                        if (LaunchActivity.mainFragmentsStack.isEmpty() || MessagesController.getInstance(i2).checkCanOpenChat(args, (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                                            ChatActivity fragment = new ChatActivity(args);
                                            NotificationCenter.getInstance(i2).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                            LaunchActivity.this.actionBarLayout.presentFragment(fragment, false, true, true);
                                        }
                                    } else if (((invite.chat != null || (invite.channel && !invite.megagroup)) && (invite.chat == null || (ChatObject.isChannel(invite.chat) && !invite.chat.megagroup))) || LaunchActivity.mainFragmentsStack.isEmpty()) {
                                        builder = new Builder(LaunchActivity.this);
                                        builder.setTitle(LocaleController.getString("AppName", C0488R.string.AppName));
                                        String str = "ChannelJoinTo";
                                        Object[] objArr = new Object[1];
                                        objArr[0] = invite.chat != null ? invite.chat.title : invite.title;
                                        builder.setMessage(LocaleController.formatString(str, C0488R.string.ChannelJoinTo, objArr));
                                        builder.setPositiveButton(LocaleController.getString("OK", C0488R.string.OK), new C18771());
                                        builder.setNegativeButton(LocaleController.getString("Cancel", C0488R.string.Cancel), null);
                                        LaunchActivity.this.showAlertDialog(builder);
                                    } else {
                                        BaseFragment fragment2 = (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1);
                                        fragment2.showDialog(new JoinGroupAlert(LaunchActivity.this, invite, str4, fragment2));
                                    }
                                }
                            }
                        });
                    }
                }, 2);
            } else if (state == 1) {
                req = new TL_messages_importChatInvite();
                req.hash = group;
                final int i3 = intentAccount;
                ConnectionsManager.getInstance(intentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(final TLObject response, final TL_error error) {
                        if (error == null) {
                            MessagesController.getInstance(i3).processUpdates((Updates) response, false);
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                if (!LaunchActivity.this.isFinishing()) {
                                    try {
                                        progressDialog.dismiss();
                                    } catch (Throwable e) {
                                        FileLog.m3e(e);
                                    }
                                    if (error != null) {
                                        Builder builder = new Builder(LaunchActivity.this);
                                        builder.setTitle(LocaleController.getString("AppName", C0488R.string.AppName));
                                        if (error.text.startsWith("FLOOD_WAIT")) {
                                            builder.setMessage(LocaleController.getString("FloodWait", C0488R.string.FloodWait));
                                        } else if (error.text.equals("USERS_TOO_MUCH")) {
                                            builder.setMessage(LocaleController.getString("JoinToGroupErrorFull", C0488R.string.JoinToGroupErrorFull));
                                        } else {
                                            builder.setMessage(LocaleController.getString("JoinToGroupErrorNotExist", C0488R.string.JoinToGroupErrorNotExist));
                                        }
                                        builder.setPositiveButton(LocaleController.getString("OK", C0488R.string.OK), null);
                                        LaunchActivity.this.showAlertDialog(builder);
                                    } else if (LaunchActivity.this.actionBarLayout != null) {
                                        Updates updates = response;
                                        if (!updates.chats.isEmpty()) {
                                            Chat chat = (Chat) updates.chats.get(0);
                                            chat.left = false;
                                            chat.kicked = false;
                                            MessagesController.getInstance(i3).putUsers(updates.users, false);
                                            MessagesController.getInstance(i3).putChats(updates.chats, false);
                                            Bundle args = new Bundle();
                                            args.putInt("chat_id", chat.id);
                                            if (LaunchActivity.mainFragmentsStack.isEmpty() || MessagesController.getInstance(i3).checkCanOpenChat(args, (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                                                ChatActivity fragment = new ChatActivity(args);
                                                NotificationCenter.getInstance(i3).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                LaunchActivity.this.actionBarLayout.presentFragment(fragment, false, true, true);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                }, 2);
            }
        } else if (sticker != null) {
            if (!mainFragmentsStack.isEmpty()) {
                TL_inputStickerSetShortName stickerset = new TL_inputStickerSetShortName();
                stickerset.short_name = sticker;
                BaseFragment fragment = (BaseFragment) mainFragmentsStack.get(mainFragmentsStack.size() - 1);
                fragment.showDialog(new StickersAlert(this, fragment, stickerset, null, null));
                return;
            }
            return;
        } else if (message != null) {
            Bundle args = new Bundle();
            args.putBoolean("onlySelect", true);
            DialogsActivity fragment2 = new DialogsActivity(args);
            final boolean z2 = hasUrl;
            final int i4 = intentAccount;
            final String str9 = message;
            fragment2.setDelegate(new DialogsActivityDelegate() {
                public void didSelectDialogs(DialogsActivity fragment, ArrayList<Long> dids, CharSequence m, boolean param) {
                    long did = ((Long) dids.get(0)).longValue();
                    Bundle args = new Bundle();
                    args.putBoolean("scrollToTopOnResume", true);
                    args.putBoolean("hasUrl", z2);
                    int lower_part = (int) did;
                    int high_id = (int) (did >> 32);
                    if (lower_part == 0) {
                        args.putInt("enc_id", high_id);
                    } else if (high_id == 1) {
                        args.putInt("chat_id", lower_part);
                    } else if (lower_part > 0) {
                        args.putInt("user_id", lower_part);
                    } else if (lower_part < 0) {
                        args.putInt("chat_id", -lower_part);
                    }
                    if (MessagesController.getInstance(i4).checkCanOpenChat(args, fragment)) {
                        NotificationCenter.getInstance(i4).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                        DataQuery.getInstance(i4).saveDraft(did, str9, null, null, false);
                        LaunchActivity.this.actionBarLayout.presentFragment(new ChatActivity(args), true, false, true);
                    }
                }
            });
            presentFragment(fragment2, false, true);
        } else if (instantView != null) {
        }
        if (requestId != 0) {
            i3 = intentAccount;
            i4 = requestId;
            progressDialog.setButton(-2, LocaleController.getString("Cancel", C0488R.string.Cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ConnectionsManager.getInstance(i3).cancelRequest(i4, true);
                    try {
                        dialog.dismiss();
                    } catch (Throwable e) {
                        FileLog.m3e(e);
                    }
                }
            });
            try {
                progressDialog.show();
            } catch (Exception e) {
            }
        }
    }

    public AlertDialog showAlertDialog(Builder builder) {
        AlertDialog alertDialog = null;
        try {
            if (this.visibleDialog != null) {
                this.visibleDialog.dismiss();
                this.visibleDialog = null;
            }
        } catch (Throwable e) {
            FileLog.m3e(e);
        }
        try {
            this.visibleDialog = builder.show();
            this.visibleDialog.setCanceledOnTouchOutside(true);
            this.visibleDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    if (LaunchActivity.this.visibleDialog != null && LaunchActivity.this.visibleDialog == LaunchActivity.this.localeDialog) {
                        try {
                            Toast.makeText(LaunchActivity.this, LaunchActivity.this.getStringForLanguageAlert(LocaleController.getInstance().getCurrentLocaleInfo().shortName.equals("en") ? LaunchActivity.this.englishLocaleStrings : LaunchActivity.this.systemLocaleStrings, "ChangeLanguageLater", C0488R.string.ChangeLanguageLater), 1).show();
                        } catch (Throwable e) {
                            FileLog.m3e(e);
                        }
                        LaunchActivity.this.localeDialog = null;
                    }
                    LaunchActivity.this.visibleDialog = null;
                }
            });
            return this.visibleDialog;
        } catch (Throwable e2) {
            FileLog.m3e(e2);
            return alertDialog;
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent, true, false, false);
    }

    public void didSelectDialogs(DialogsActivity dialogsFragment, ArrayList<Long> dids, CharSequence message, boolean param) {
        long did = ((Long) dids.get(0)).longValue();
        int lower_part = (int) did;
        int high_id = (int) (did >> 32);
        Bundle args = new Bundle();
        int account = dialogsFragment != null ? dialogsFragment.getCurrentAccount() : this.currentAccount;
        args.putBoolean("scrollToTopOnResume", true);
        if (!AndroidUtilities.isTablet()) {
            NotificationCenter.getInstance(account).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        }
        if (lower_part == 0) {
            args.putInt("enc_id", high_id);
        } else if (high_id == 1) {
            args.putInt("chat_id", lower_part);
        } else if (lower_part > 0) {
            args.putInt("user_id", lower_part);
        } else if (lower_part < 0) {
            args.putInt("chat_id", -lower_part);
        }
        if (MessagesController.getInstance(account).checkCanOpenChat(args, dialogsFragment)) {
            boolean z;
            boolean z2;
            BaseFragment chatActivity = new ChatActivity(args);
            ActionBarLayout actionBarLayout = this.actionBarLayout;
            if (dialogsFragment != null) {
                z = true;
            } else {
                z = false;
            }
            if (dialogsFragment == null) {
                z2 = true;
            } else {
                z2 = false;
            }
            actionBarLayout.presentFragment(chatActivity, z, z2, true);
            if (this.videoPath != null) {
                chatActivity.openVideoEditor(this.videoPath, this.sendingText);
                this.sendingText = null;
            }
            if (this.photoPathsArray != null) {
                if (this.sendingText != null && this.sendingText.length() <= Callback.DEFAULT_DRAG_ANIMATION_DURATION && this.photoPathsArray.size() == 1) {
                    ((SendingMediaInfo) this.photoPathsArray.get(0)).caption = this.sendingText;
                    this.sendingText = null;
                }
                SendMessagesHelper.prepareSendingMedia(this.photoPathsArray, did, null, null, false, false);
            }
            if (this.sendingText != null) {
                SendMessagesHelper.prepareSendingText(this.sendingText, did);
            }
            if (!(this.documentsPathsArray == null && this.documentsUrisArray == null)) {
                SendMessagesHelper.prepareSendingDocuments(this.documentsPathsArray, this.documentsOriginalPathsArray, this.documentsUrisArray, this.documentsMimeType, did, null, null);
            }
            if (!(this.contactsToSend == null || this.contactsToSend.isEmpty())) {
                Iterator it = this.contactsToSend.iterator();
                while (it.hasNext()) {
                    SendMessagesHelper.getInstance(account).sendMessage((User) it.next(), did, null, null, null);
                }
            }
            this.photoPathsArray = null;
            this.videoPath = null;
            this.sendingText = null;
            this.documentsPathsArray = null;
            this.documentsOriginalPathsArray = null;
            this.contactsToSend = null;
        }
    }

    private void onFinish() {
        if (!this.finished) {
            this.finished = true;
            if (this.lockRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(this.lockRunnable);
                this.lockRunnable = null;
            }
            if (this.currentAccount != -1) {
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.appDidLogout);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.mainUserInfoChanged);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdatedConnectionState);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.needShowAlert);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.openArticle);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.hasNewContactsToImport);
            }
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.suggestedLangpack);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.reloadInterface);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewTheme);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needSetDayNightTheme);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.closeOtherAppActivities);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetPasscode);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.notificationsCountUpdated);
        }
    }

    public void presentFragment(BaseFragment fragment) {
        this.actionBarLayout.presentFragment(fragment);
    }

    public boolean presentFragment(BaseFragment fragment, boolean removeLast, boolean forceWithoutAnimation) {
        return this.actionBarLayout.presentFragment(fragment, removeLast, forceWithoutAnimation, true);
    }

    public ActionBarLayout getActionBarLayout() {
        return this.actionBarLayout;
    }

    public ActionBarLayout getLayersActionBarLayout() {
        return this.layersActionBarLayout;
    }

    public ActionBarLayout getRightActionBarLayout() {
        return this.rightActionBarLayout;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!(SharedConfig.passcodeHash.length() == 0 || SharedConfig.lastPauseTime == 0)) {
            SharedConfig.lastPauseTime = 0;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
        }
        super.onActivityResult(requestCode, resultCode, data);
        ThemeEditorView editorView = ThemeEditorView.getInstance();
        if (editorView != null) {
            editorView.onActivityResult(requestCode, resultCode, data);
        }
        if (this.actionBarLayout.fragmentsStack.size() != 0) {
            ((BaseFragment) this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1)).onActivityResultFragment(requestCode, resultCode, data);
        }
        if (AndroidUtilities.isTablet()) {
            if (this.rightActionBarLayout.fragmentsStack.size() != 0) {
                ((BaseFragment) this.rightActionBarLayout.fragmentsStack.get(this.rightActionBarLayout.fragmentsStack.size() - 1)).onActivityResultFragment(requestCode, resultCode, data);
            }
            if (this.layersActionBarLayout.fragmentsStack.size() != 0) {
                ((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(this.layersActionBarLayout.fragmentsStack.size() - 1)).onActivityResultFragment(requestCode, resultCode, data);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 3 || requestCode == 4 || requestCode == 5 || requestCode == 19 || requestCode == 20) {
            boolean showAlert = true;
            if (grantResults.length > 0 && grantResults[0] == 0) {
                if (requestCode == 4) {
                    ImageLoader.getInstance().checkMediaPaths();
                    return;
                } else if (requestCode == 5) {
                    ContactsController.getInstance(this.currentAccount).forceImportContacts();
                    return;
                } else if (requestCode == 3) {
                    if (SharedConfig.inappCamera) {
                        CameraController.getInstance().initCamera();
                        return;
                    }
                    return;
                } else if (requestCode == 19 || requestCode == 20) {
                    showAlert = false;
                }
            }
            if (showAlert) {
                Builder builder = new Builder((Context) this);
                builder.setTitle(LocaleController.getString("AppName", C0488R.string.AppName));
                if (requestCode == 3) {
                    builder.setMessage(LocaleController.getString("PermissionNoAudio", C0488R.string.PermissionNoAudio));
                } else if (requestCode == 4) {
                    builder.setMessage(LocaleController.getString("PermissionStorage", C0488R.string.PermissionStorage));
                } else if (requestCode == 5) {
                    builder.setMessage(LocaleController.getString("PermissionContacts", C0488R.string.PermissionContacts));
                } else if (requestCode == 19 || requestCode == 20) {
                    builder.setMessage(LocaleController.getString("PermissionNoCamera", C0488R.string.PermissionNoCamera));
                }
                builder.setNegativeButton(LocaleController.getString("PermissionOpenSettings", C0488R.string.PermissionOpenSettings), new DialogInterface.OnClickListener() {
                    @TargetApi(9)
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.getPackageName()));
                            LaunchActivity.this.startActivity(intent);
                        } catch (Throwable e) {
                            FileLog.m3e(e);
                        }
                    }
                });
                builder.setPositiveButton(LocaleController.getString("OK", C0488R.string.OK), null);
                builder.show();
                return;
            }
        } else if (requestCode == 2 && grantResults.length > 0 && grantResults[0] == 0) {
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.locationPermissionGranted, new Object[0]);
        }
        if (this.actionBarLayout.fragmentsStack.size() != 0) {
            ((BaseFragment) this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1)).onRequestPermissionsResultFragment(requestCode, permissions, grantResults);
        }
        if (AndroidUtilities.isTablet()) {
            if (this.rightActionBarLayout.fragmentsStack.size() != 0) {
                ((BaseFragment) this.rightActionBarLayout.fragmentsStack.get(this.rightActionBarLayout.fragmentsStack.size() - 1)).onRequestPermissionsResultFragment(requestCode, permissions, grantResults);
            }
            if (this.layersActionBarLayout.fragmentsStack.size() != 0) {
                ((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(this.layersActionBarLayout.fragmentsStack.size() - 1)).onRequestPermissionsResultFragment(requestCode, permissions, grantResults);
            }
        }
    }

    protected void onPause() {
        super.onPause();
        SharedConfig.lastAppPauseTime = System.currentTimeMillis();
        ApplicationLoader.mainInterfacePaused = true;
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                ApplicationLoader.mainInterfacePausedStageQueue = true;
                ApplicationLoader.mainInterfacePausedStageQueueTime = 0;
            }
        });
        onPasscodePause();
        this.actionBarLayout.onPause();
        if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onPause();
            this.layersActionBarLayout.onPause();
        }
        if (this.passcodeView != null) {
            this.passcodeView.onPause();
        }
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
        AndroidUtilities.unregisterUpdates();
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().onPause();
        }
    }

    protected void onStart() {
        super.onStart();
        Browser.bindCustomTabsService(this);
    }

    protected void onStop() {
        super.onStop();
        Browser.unbindCustomTabsService(this);
    }

    protected void onDestroy() {
        if (PhotoViewer.getPipInstance() != null) {
            PhotoViewer.getPipInstance().destroyPhotoViewer();
        }
        if (PhotoViewer.hasInstance()) {
            PhotoViewer.getInstance().destroyPhotoViewer();
        }
        if (SecretMediaViewer.hasInstance()) {
            SecretMediaViewer.getInstance().destroyPhotoViewer();
        }
        if (ArticleViewer.hasInstance()) {
            ArticleViewer.getInstance().destroyArticleViewer();
        }
        if (StickerPreviewViewer.hasInstance()) {
            StickerPreviewViewer.getInstance().destroy();
        }
        PipRoundVideoView pipRoundVideoView = PipRoundVideoView.getInstance();
        MediaController.getInstance().setBaseActivity(this, false);
        MediaController.getInstance().setFeedbackView(this.actionBarLayout, false);
        if (pipRoundVideoView != null) {
            pipRoundVideoView.close(false);
        }
        Theme.destroyResources();
        EmbedBottomSheet embedBottomSheet = EmbedBottomSheet.getInstance();
        if (embedBottomSheet != null) {
            embedBottomSheet.destroy();
        }
        ThemeEditorView editorView = ThemeEditorView.getInstance();
        if (editorView != null) {
            editorView.destroy();
        }
        try {
            if (this.visibleDialog != null) {
                this.visibleDialog.dismiss();
                this.visibleDialog = null;
            }
        } catch (Throwable e) {
            FileLog.m3e(e);
        }
        try {
            if (this.onGlobalLayoutListener != null) {
                getWindow().getDecorView().getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(this.onGlobalLayoutListener);
            }
        } catch (Throwable e2) {
            FileLog.m3e(e2);
        }
        super.onDestroy();
        onFinish();
    }

    protected void onResume() {
        super.onResume();
        MediaController.getInstance().setFeedbackView(this.actionBarLayout, true);
        showLanguageAlert(false);
        ApplicationLoader.mainInterfacePaused = false;
        NotificationsController.lastNoDataNotificationTime = 0;
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                ApplicationLoader.mainInterfacePausedStageQueue = false;
                ApplicationLoader.mainInterfacePausedStageQueueTime = System.currentTimeMillis();
            }
        });
        checkFreeDiscSpace();
        MediaController.checkGallery();
        onPasscodeResume();
        if (this.passcodeView.getVisibility() != 0) {
            this.actionBarLayout.onResume();
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.onResume();
                this.layersActionBarLayout.onResume();
            }
        } else {
            this.actionBarLayout.dismissDialogs();
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.dismissDialogs();
                this.layersActionBarLayout.dismissDialogs();
            }
            this.passcodeView.onResume();
        }
        AndroidUtilities.checkForCrashes(this);
        AndroidUtilities.checkForUpdates(this);
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
        updateCurrentConnectionState(this.currentAccount);
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().onResume();
        }
        if (PipRoundVideoView.getInstance() != null && MediaController.getInstance().isMessagePaused()) {
            MessageObject messageObject = MediaController.getInstance().getPlayingMessageObject();
            if (messageObject != null) {
                MediaController.getInstance().seekToProgress(messageObject, messageObject.audioProgress);
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        AndroidUtilities.checkDisplaySize(this, newConfig);
        super.onConfigurationChanged(newConfig);
        checkLayout();
        PipRoundVideoView pipRoundVideoView = PipRoundVideoView.getInstance();
        if (pipRoundVideoView != null) {
            pipRoundVideoView.onConfigurationChanged();
        }
        EmbedBottomSheet embedBottomSheet = EmbedBottomSheet.getInstance();
        if (embedBottomSheet != null) {
            embedBottomSheet.onConfigurationChanged(newConfig);
        }
        PhotoViewer photoViewer = PhotoViewer.getPipInstance();
        if (photoViewer != null) {
            photoViewer.onConfigurationChanged(newConfig);
        }
        ThemeEditorView editorView = ThemeEditorView.getInstance();
        if (editorView != null) {
            editorView.onConfigurationChanged();
        }
    }

    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        AndroidUtilities.isInMultiwindow = isInMultiWindowMode;
        checkLayout();
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.appDidLogout) {
            switchToAvailableAccountOrLogout();
        } else if (id == NotificationCenter.closeOtherAppActivities) {
            if (args[0] != this) {
                onFinish();
                finish();
            }
        } else if (id == NotificationCenter.didUpdatedConnectionState) {
            int state = ConnectionsManager.getInstance(account).getConnectionState();
            if (this.currentConnectionState != state) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.m0d("switch to state " + state);
                }
                this.currentConnectionState = state;
                updateCurrentConnectionState(account);
            }
        } else if (id == NotificationCenter.mainUserInfoChanged) {
            this.drawerLayoutAdapter.notifyDataSetChanged();
        } else if (id == NotificationCenter.needShowAlert) {
            Integer reason = args[0];
            builder = new Builder((Context) this);
            builder.setTitle(LocaleController.getString("AppName", C0488R.string.AppName));
            builder.setPositiveButton(LocaleController.getString("OK", C0488R.string.OK), null);
            if (reason.intValue() != 2) {
                final int i = account;
                builder.setNegativeButton(LocaleController.getString("MoreInfo", C0488R.string.MoreInfo), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!LaunchActivity.mainFragmentsStack.isEmpty()) {
                            MessagesController.getInstance(i).openByUserName("spambot", (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1), 1);
                        }
                    }
                });
            }
            if (reason.intValue() == 0) {
                builder.setMessage(LocaleController.getString("NobodyLikesSpam1", C0488R.string.NobodyLikesSpam1));
            } else if (reason.intValue() == 1) {
                builder.setMessage(LocaleController.getString("NobodyLikesSpam2", C0488R.string.NobodyLikesSpam2));
            } else if (reason.intValue() == 2) {
                builder.setMessage((String) args[1]);
            }
            if (!mainFragmentsStack.isEmpty()) {
                ((BaseFragment) mainFragmentsStack.get(mainFragmentsStack.size() - 1)).showDialog(builder.create());
            }
        } else if (id == NotificationCenter.wasUnableToFindCurrentLocation) {
            HashMap<String, MessageObject> waitingForLocation = args[0];
            builder = new Builder((Context) this);
            builder.setTitle(LocaleController.getString("AppName", C0488R.string.AppName));
            builder.setPositiveButton(LocaleController.getString("OK", C0488R.string.OK), null);
            final HashMap<String, MessageObject> hashMap = waitingForLocation;
            final int i2 = account;
            builder.setNegativeButton(LocaleController.getString("ShareYouLocationUnableManually", C0488R.string.ShareYouLocationUnableManually), new DialogInterface.OnClickListener() {

                class C18801 implements LocationActivityDelegate {
                    C18801() {
                    }

                    public void didSelectLocation(MessageMedia location, int live) {
                        for (Entry<String, MessageObject> entry : hashMap.entrySet()) {
                            MessageObject messageObject = (MessageObject) entry.getValue();
                            SendMessagesHelper.getInstance(i2).sendMessage(location, messageObject.getDialogId(), messageObject, null, null);
                        }
                    }
                }

                public void onClick(DialogInterface dialogInterface, int i) {
                    if (!LaunchActivity.mainFragmentsStack.isEmpty() && AndroidUtilities.isGoogleMapsInstalled((BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                        LocationActivity fragment = new LocationActivity(0);
                        fragment.setDelegate(new C18801());
                        LaunchActivity.this.presentFragment(fragment);
                    }
                }
            });
            builder.setMessage(LocaleController.getString("ShareYouLocationUnable", C0488R.string.ShareYouLocationUnable));
            if (!mainFragmentsStack.isEmpty()) {
                ((BaseFragment) mainFragmentsStack.get(mainFragmentsStack.size() - 1)).showDialog(builder.create());
            }
        } else if (id == NotificationCenter.didSetNewWallpapper) {
            if (this.sideMenu != null) {
                child = this.sideMenu.getChildAt(0);
                if (child != null) {
                    child.invalidate();
                }
            }
        } else if (id == NotificationCenter.didSetPasscode) {
            if (SharedConfig.passcodeHash.length() <= 0 || SharedConfig.allowScreenCapture) {
                try {
                    getWindow().clearFlags(MessagesController.UPDATE_MASK_CHANNEL);
                    return;
                } catch (Throwable e) {
                    FileLog.m3e(e);
                    return;
                }
            }
            try {
                getWindow().setFlags(MessagesController.UPDATE_MASK_CHANNEL, MessagesController.UPDATE_MASK_CHANNEL);
            } catch (Throwable e2) {
                FileLog.m3e(e2);
            }
        } else if (id == NotificationCenter.reloadInterface) {
            rebuildAllFragments(false);
        } else if (id == NotificationCenter.suggestedLangpack) {
            showLanguageAlert(false);
        } else if (id == NotificationCenter.openArticle) {
            if (!mainFragmentsStack.isEmpty()) {
                ArticleViewer.getInstance().setParentActivity(this, (BaseFragment) mainFragmentsStack.get(mainFragmentsStack.size() - 1));
                ArticleViewer.getInstance().open((TL_webPage) args[0], (String) args[1]);
            }
        } else if (id == NotificationCenter.hasNewContactsToImport) {
            if (this.actionBarLayout != null && !this.actionBarLayout.fragmentsStack.isEmpty()) {
                int type = ((Integer) args[0]).intValue();
                final HashMap<String, Contact> contactHashMap = args[1];
                final boolean first = ((Boolean) args[2]).booleanValue();
                final boolean schedule = ((Boolean) args[3]).booleanValue();
                BaseFragment fragment = (BaseFragment) this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1);
                builder = new Builder((Context) this);
                builder.setTitle(LocaleController.getString("UpdateContactsTitle", C0488R.string.UpdateContactsTitle));
                builder.setMessage(LocaleController.getString("UpdateContactsMessage", C0488R.string.UpdateContactsMessage));
                final int i3 = account;
                builder.setPositiveButton(LocaleController.getString("OK", C0488R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContactsController.getInstance(i3).syncPhoneBookByAlert(contactHashMap, first, schedule, false);
                    }
                });
                i3 = account;
                builder.setNegativeButton(LocaleController.getString("Cancel", C0488R.string.Cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ContactsController.getInstance(i3).syncPhoneBookByAlert(contactHashMap, first, schedule, true);
                    }
                });
                i3 = account;
                builder.setOnBackButtonListener(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContactsController.getInstance(i3).syncPhoneBookByAlert(contactHashMap, first, schedule, true);
                    }
                });
                AlertDialog dialog = builder.create();
                fragment.showDialog(dialog);
                dialog.setCanceledOnTouchOutside(false);
            }
        } else if (id == NotificationCenter.didSetNewTheme) {
            if (!args[0].booleanValue()) {
                if (this.sideMenu != null) {
                    this.sideMenu.setBackgroundColor(Theme.getColor(Theme.key_chats_menuBackground));
                    this.sideMenu.setGlowColor(Theme.getColor(Theme.key_chats_menuBackground));
                    this.sideMenu.getAdapter().notifyDataSetChanged();
                }
                if (VERSION.SDK_INT >= 21) {
                    try {
                        setTaskDescription(new TaskDescription(null, null, Theme.getColor(Theme.key_actionBarDefault) | Theme.ACTION_BAR_VIDEO_EDIT_COLOR));
                    } catch (Exception e3) {
                    }
                }
            }
        } else if (id == NotificationCenter.needSetDayNightTheme) {
            ThemeInfo theme = args[0];
            this.actionBarLayout.animateThemedValues(theme);
            if (AndroidUtilities.isTablet()) {
                this.layersActionBarLayout.animateThemedValues(theme);
                this.rightActionBarLayout.animateThemedValues(theme);
            }
        } else if (id == NotificationCenter.notificationsCountUpdated && this.sideMenu != null) {
            Integer accountNum = args[0];
            int count = this.sideMenu.getChildCount();
            for (int a = 0; a < count; a++) {
                child = this.sideMenu.getChildAt(a);
                if ((child instanceof DrawerUserCell) && ((DrawerUserCell) child).getAccountNumber() == accountNum.intValue()) {
                    child.invalidate();
                    return;
                }
            }
        }
    }

    private String getStringForLanguageAlert(HashMap<String, String> map, String key, int intKey) {
        String value = (String) map.get(key);
        if (value == null) {
            return LocaleController.getString(key, intKey);
        }
        return value;
    }

    private void checkFreeDiscSpace() {
        if (VERSION.SDK_INT < 26) {
            Utilities.globalQueue.postRunnable(new Runnable() {

                class C18821 implements Runnable {
                    C18821() {
                    }

                    public void run() {
                        try {
                            AlertsCreator.createFreeSpaceDialog(LaunchActivity.this).show();
                        } catch (Throwable th) {
                        }
                    }
                }

                public void run() {
                    if (UserConfig.getInstance(LaunchActivity.this.currentAccount).isClientActivated()) {
                        try {
                            SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                            if (Math.abs(preferences.getLong("last_space_check", 0) - System.currentTimeMillis()) >= 259200000) {
                                File path = FileLoader.getDirectory(4);
                                if (path != null) {
                                    long freeSpace;
                                    StatFs statFs = new StatFs(path.getAbsolutePath());
                                    if (VERSION.SDK_INT < 18) {
                                        freeSpace = (long) Math.abs(statFs.getAvailableBlocks() * statFs.getBlockSize());
                                    } else {
                                        freeSpace = statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
                                    }
                                    preferences.edit().putLong("last_space_check", System.currentTimeMillis()).commit();
                                    if (freeSpace < 104857600) {
                                        AndroidUtilities.runOnUIThread(new C18821());
                                    }
                                }
                            }
                        } catch (Throwable th) {
                        }
                    }
                }
            }, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
        }
    }

    private void showLanguageAlertInternal(LocaleInfo systemInfo, LocaleInfo englishInfo, String systemLang) {
        try {
            LocaleInfo localeInfo;
            this.loadingLocaleDialog = false;
            boolean firstSystem = systemInfo.builtIn || LocaleController.getInstance().isCurrentLocalLocale();
            Builder builder = new Builder((Context) this);
            builder.setTitle(getStringForLanguageAlert(this.systemLocaleStrings, "ChooseYourLanguage", C0488R.string.ChooseYourLanguage));
            builder.setSubtitle(getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguage", C0488R.string.ChooseYourLanguage));
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(1);
            final LanguageCell[] cells = new LanguageCell[2];
            final LocaleInfo[] selectedLanguage = new LocaleInfo[1];
            LocaleInfo[] locales = new LocaleInfo[2];
            String englishName = getStringForLanguageAlert(this.systemLocaleStrings, "English", C0488R.string.English);
            if (firstSystem) {
                localeInfo = systemInfo;
            } else {
                localeInfo = englishInfo;
            }
            locales[0] = localeInfo;
            if (firstSystem) {
                localeInfo = englishInfo;
            } else {
                localeInfo = systemInfo;
            }
            locales[1] = localeInfo;
            if (!firstSystem) {
                systemInfo = englishInfo;
            }
            selectedLanguage[0] = systemInfo;
            int a = 0;
            while (a < 2) {
                cells[a] = new LanguageCell(this, true);
                cells[a].setLanguage(locales[a], locales[a] == englishInfo ? englishName : null, true);
                cells[a].setTag(Integer.valueOf(a));
                cells[a].setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 2));
                cells[a].setLanguageSelected(a == 0);
                linearLayout.addView(cells[a], LayoutHelper.createLinear(-1, 48));
                cells[a].setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        Integer tag = (Integer) v.getTag();
                        selectedLanguage[0] = ((LanguageCell) v).getCurrentLocale();
                        for (int a = 0; a < cells.length; a++) {
                            boolean z;
                            LanguageCell languageCell = cells[a];
                            if (a == tag.intValue()) {
                                z = true;
                            } else {
                                z = false;
                            }
                            languageCell.setLanguageSelected(z);
                        }
                    }
                });
                a++;
            }
            LanguageCell cell = new LanguageCell(this, true);
            cell.setValue(getStringForLanguageAlert(this.systemLocaleStrings, "ChooseYourLanguageOther", C0488R.string.ChooseYourLanguageOther), getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguageOther", C0488R.string.ChooseYourLanguageOther));
            cell.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    LaunchActivity.this.localeDialog = null;
                    LaunchActivity.this.drawerLayoutContainer.closeDrawer(true);
                    LaunchActivity.this.presentFragment(new LanguageSelectActivity());
                    if (LaunchActivity.this.visibleDialog != null) {
                        LaunchActivity.this.visibleDialog.dismiss();
                        LaunchActivity.this.visibleDialog = null;
                    }
                }
            });
            linearLayout.addView(cell, LayoutHelper.createLinear(-1, 48));
            builder.setView(linearLayout);
            builder.setNegativeButton(LocaleController.getString("OK", C0488R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    LocaleController.getInstance().applyLanguage(selectedLanguage[0], true, false, LaunchActivity.this.currentAccount);
                    LaunchActivity.this.rebuildAllFragments(true);
                }
            });
            this.localeDialog = showAlertDialog(builder);
            MessagesController.getGlobalMainSettings().edit().putString("language_showed2", systemLang).commit();
        } catch (Throwable e) {
            FileLog.m3e(e);
        }
    }

    private void showLanguageAlert(boolean force) {
        try {
            if (!this.loadingLocaleDialog) {
                String showedLang = MessagesController.getGlobalMainSettings().getString("language_showed2", TtmlNode.ANONYMOUS_REGION_ID);
                final String systemLang = LocaleController.getSystemLocaleStringIso639().toLowerCase();
                if (force || !showedLang.equals(systemLang)) {
                    String arg;
                    final LocaleInfo[] infos = new LocaleInfo[2];
                    if (systemLang.contains("-")) {
                        arg = systemLang.split("-")[0];
                    } else {
                        arg = systemLang;
                    }
                    String alias;
                    if ("in".equals(arg)) {
                        alias = TtmlNode.ATTR_ID;
                    } else if ("iw".equals(arg)) {
                        alias = "he";
                    } else if ("jw".equals(arg)) {
                        alias = "jv";
                    } else {
                        alias = null;
                    }
                    for (int a = 0; a < LocaleController.getInstance().languages.size(); a++) {
                        LocaleInfo info = (LocaleInfo) LocaleController.getInstance().languages.get(a);
                        if (info.shortName.equals("en")) {
                            infos[0] = info;
                        }
                        if (info.shortName.replace("_", "-").equals(systemLang) || info.shortName.equals(arg) || (alias != null && info.shortName.equals(alias))) {
                            infos[1] = info;
                        }
                        if (infos[0] != null && infos[1] != null) {
                            break;
                        }
                    }
                    if (infos[0] != null && infos[1] != null && infos[0] != infos[1]) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.m0d("show lang alert for " + infos[0].getKey() + " and " + infos[1].getKey());
                        }
                        this.systemLocaleStrings = null;
                        this.englishLocaleStrings = null;
                        this.loadingLocaleDialog = true;
                        TL_langpack_getStrings req = new TL_langpack_getStrings();
                        req.lang_code = infos[1].shortName.replace("_", "-");
                        req.keys.add("English");
                        req.keys.add("ChooseYourLanguage");
                        req.keys.add("ChooseYourLanguageOther");
                        req.keys.add("ChangeLanguageLater");
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                            public void run(TLObject response, TL_error error) {
                                final HashMap<String, String> keys = new HashMap();
                                if (response != null) {
                                    Vector vector = (Vector) response;
                                    for (int a = 0; a < vector.objects.size(); a++) {
                                        LangPackString string = (LangPackString) vector.objects.get(a);
                                        keys.put(string.key, string.value);
                                    }
                                }
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        LaunchActivity.this.systemLocaleStrings = keys;
                                        if (LaunchActivity.this.englishLocaleStrings != null && LaunchActivity.this.systemLocaleStrings != null) {
                                            LaunchActivity.this.showLanguageAlertInternal(infos[1], infos[0], systemLang);
                                        }
                                    }
                                });
                            }
                        }, 8);
                        req = new TL_langpack_getStrings();
                        req.lang_code = infos[0].shortName.replace("_", "-");
                        req.keys.add("English");
                        req.keys.add("ChooseYourLanguage");
                        req.keys.add("ChooseYourLanguageOther");
                        req.keys.add("ChangeLanguageLater");
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                            public void run(TLObject response, TL_error error) {
                                final HashMap<String, String> keys = new HashMap();
                                if (response != null) {
                                    Vector vector = (Vector) response;
                                    for (int a = 0; a < vector.objects.size(); a++) {
                                        LangPackString string = (LangPackString) vector.objects.get(a);
                                        keys.put(string.key, string.value);
                                    }
                                }
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        LaunchActivity.this.englishLocaleStrings = keys;
                                        if (LaunchActivity.this.englishLocaleStrings != null && LaunchActivity.this.systemLocaleStrings != null) {
                                            LaunchActivity.this.showLanguageAlertInternal(infos[1], infos[0], systemLang);
                                        }
                                    }
                                });
                            }
                        }, 8);
                    }
                } else if (BuildVars.LOGS_ENABLED) {
                    FileLog.m0d("alert already showed for " + showedLang);
                }
            }
        } catch (Throwable e) {
            FileLog.m3e(e);
        }
    }

    private void onPasscodePause() {
        if (this.lockRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(this.lockRunnable);
            this.lockRunnable = null;
        }
        if (SharedConfig.passcodeHash.length() != 0) {
            SharedConfig.lastPauseTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            this.lockRunnable = new Runnable() {
                public void run() {
                    if (LaunchActivity.this.lockRunnable == this) {
                        if (AndroidUtilities.needShowPasscode(true)) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.m0d("lock app");
                            }
                            LaunchActivity.this.showPasscodeActivity();
                        } else if (BuildVars.LOGS_ENABLED) {
                            FileLog.m0d("didn't pass lock check");
                        }
                        LaunchActivity.this.lockRunnable = null;
                    }
                }
            };
            if (SharedConfig.appLocked) {
                AndroidUtilities.runOnUIThread(this.lockRunnable, 1000);
            } else if (SharedConfig.autoLockIn != 0) {
                AndroidUtilities.runOnUIThread(this.lockRunnable, (((long) SharedConfig.autoLockIn) * 1000) + 1000);
            }
        } else {
            SharedConfig.lastPauseTime = 0;
        }
        SharedConfig.saveConfig();
    }

    private void onPasscodeResume() {
        if (this.lockRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(this.lockRunnable);
            this.lockRunnable = null;
        }
        if (AndroidUtilities.needShowPasscode(true)) {
            showPasscodeActivity();
        }
        if (SharedConfig.lastPauseTime != 0) {
            SharedConfig.lastPauseTime = 0;
            SharedConfig.saveConfig();
        }
    }

    private void updateCurrentConnectionState(int account) {
        if (this.actionBarLayout != null) {
            String title = null;
            String subtitle = null;
            Runnable action = null;
            if (this.currentConnectionState == 2) {
                title = LocaleController.getString("WaitingForNetwork", C0488R.string.WaitingForNetwork);
            } else if (this.currentConnectionState == 1) {
                title = LocaleController.getString("Connecting", C0488R.string.Connecting);
                action = new Runnable() {
                    public void run() {
                        if (AndroidUtilities.isTablet()) {
                            if (!LaunchActivity.layerFragmentsStack.isEmpty() && (LaunchActivity.layerFragmentsStack.get(LaunchActivity.layerFragmentsStack.size() - 1) instanceof ProxySettingsActivity)) {
                                return;
                            }
                        } else if (!LaunchActivity.mainFragmentsStack.isEmpty() && (LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1) instanceof ProxySettingsActivity)) {
                            return;
                        }
                        LaunchActivity.this.presentFragment(new ProxySettingsActivity());
                    }
                };
            } else if (this.currentConnectionState == 5) {
                title = LocaleController.getString("Updating", C0488R.string.Updating);
            } else if (this.currentConnectionState == 4) {
                title = LocaleController.getString("ConnectingToProxy", C0488R.string.ConnectingToProxy);
                subtitle = LocaleController.getString("ConnectingToProxyTapToDisable", C0488R.string.ConnectingToProxyTapToDisable);
                action = new Runnable() {

                    class C18861 implements DialogInterface.OnClickListener {
                        C18861() {
                        }

                        public void onClick(DialogInterface dialogInterface, int i) {
                            Editor editor = MessagesController.getGlobalMainSettings().edit();
                            editor.putBoolean("proxy_enabled", false);
                            editor.commit();
                            for (int a = 0; a < 3; a++) {
                                ConnectionsManager.native_setProxySettings(a, TtmlNode.ANONYMOUS_REGION_ID, 0, TtmlNode.ANONYMOUS_REGION_ID, TtmlNode.ANONYMOUS_REGION_ID);
                            }
                            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.proxySettingsChanged, new Object[0]);
                        }
                    }

                    public void run() {
                        if (LaunchActivity.this.actionBarLayout != null && !LaunchActivity.this.actionBarLayout.fragmentsStack.isEmpty()) {
                            SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                            BaseFragment fragment = (BaseFragment) LaunchActivity.this.actionBarLayout.fragmentsStack.get(LaunchActivity.this.actionBarLayout.fragmentsStack.size() - 1);
                            Builder builder = new Builder(LaunchActivity.this);
                            builder.setTitle(LocaleController.getString("Proxy", C0488R.string.Proxy));
                            builder.setMessage(LocaleController.formatString("ConnectingToProxyDisableAlert", C0488R.string.ConnectingToProxyDisableAlert, preferences.getString("proxy_ip", TtmlNode.ANONYMOUS_REGION_ID)));
                            builder.setPositiveButton(LocaleController.getString("ConnectingToProxyDisable", C0488R.string.ConnectingToProxyDisable), new C18861());
                            builder.setNegativeButton(LocaleController.getString("Cancel", C0488R.string.Cancel), null);
                            fragment.showDialog(builder.create());
                        }
                    }
                };
            }
            this.actionBarLayout.setTitleOverlayText(title, subtitle, action);
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        try {
            super.onSaveInstanceState(outState);
            BaseFragment lastFragment = null;
            if (AndroidUtilities.isTablet()) {
                if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    lastFragment = (BaseFragment) this.layersActionBarLayout.fragmentsStack.get(this.layersActionBarLayout.fragmentsStack.size() - 1);
                } else if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    lastFragment = (BaseFragment) this.rightActionBarLayout.fragmentsStack.get(this.rightActionBarLayout.fragmentsStack.size() - 1);
                } else if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                    lastFragment = (BaseFragment) this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1);
                }
            } else if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                lastFragment = (BaseFragment) this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1);
            }
            if (lastFragment != null) {
                Bundle args = lastFragment.getArguments();
                if ((lastFragment instanceof ChatActivity) && args != null) {
                    outState.putBundle("args", args);
                    outState.putString("fragment", "chat");
                } else if (lastFragment instanceof SettingsActivity) {
                    outState.putString("fragment", "settings");
                } else if ((lastFragment instanceof GroupCreateFinalActivity) && args != null) {
                    outState.putBundle("args", args);
                    outState.putString("fragment", "group");
                } else if (lastFragment instanceof WallpapersActivity) {
                    outState.putString("fragment", "wallpapers");
                } else if ((lastFragment instanceof ProfileActivity) && ((ProfileActivity) lastFragment).isChat() && args != null) {
                    outState.putBundle("args", args);
                    outState.putString("fragment", "chat_profile");
                } else if ((lastFragment instanceof ChannelCreateActivity) && args != null && args.getInt("step") == 0) {
                    outState.putBundle("args", args);
                    outState.putString("fragment", "channel");
                } else if ((lastFragment instanceof ChannelEditActivity) && args != null) {
                    outState.putBundle("args", args);
                    outState.putString("fragment", "edit");
                }
                lastFragment.saveSelfArgs(outState);
            }
        } catch (Throwable e) {
            FileLog.m3e(e);
        }
    }

    public void onBackPressed() {
        if (this.passcodeView.getVisibility() == 0) {
            finish();
        } else if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(true, false);
        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
        } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(true, false);
        } else if (this.drawerLayoutContainer.isDrawerOpened()) {
            this.drawerLayoutContainer.closeDrawer(false);
        } else if (!AndroidUtilities.isTablet()) {
            this.actionBarLayout.onBackPressed();
        } else if (this.layersActionBarLayout.getVisibility() == 0) {
            this.layersActionBarLayout.onBackPressed();
        } else {
            boolean cancel = false;
            if (this.rightActionBarLayout.getVisibility() == 0 && !this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                cancel = !((BaseFragment) this.rightActionBarLayout.fragmentsStack.get(this.rightActionBarLayout.fragmentsStack.size() + -1)).onBackPressed();
            }
            if (!cancel) {
                this.actionBarLayout.onBackPressed();
            }
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        this.actionBarLayout.onLowMemory();
        if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onLowMemory();
            this.layersActionBarLayout.onLowMemory();
        }
    }

    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
        try {
            Menu menu = mode.getMenu();
            if (!(menu == null || this.actionBarLayout.extendActionMode(menu) || !AndroidUtilities.isTablet() || this.rightActionBarLayout.extendActionMode(menu))) {
                this.layersActionBarLayout.extendActionMode(menu);
            }
        } catch (Throwable e) {
            FileLog.m3e(e);
        }
        if (VERSION.SDK_INT < 23 || mode.getType() != 1) {
            this.actionBarLayout.onActionModeStarted(mode);
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.onActionModeStarted(mode);
                this.layersActionBarLayout.onActionModeStarted(mode);
            }
        }
    }

    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        if (VERSION.SDK_INT < 23 || mode.getType() != 1) {
            this.actionBarLayout.onActionModeFinished(mode);
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.onActionModeFinished(mode);
                this.layersActionBarLayout.onActionModeFinished(mode);
            }
        }
    }

    public boolean onPreIme() {
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(true, false);
            return true;
        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
            return true;
        } else if (!ArticleViewer.hasInstance() || !ArticleViewer.getInstance().isVisible()) {
            return false;
        } else {
            ArticleViewer.getInstance().close(true, false);
            return true;
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 82 && !SharedConfig.isWaitingForPasscodeEnter) {
            if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                return super.onKeyUp(keyCode, event);
            }
            if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                return super.onKeyUp(keyCode, event);
            }
            if (AndroidUtilities.isTablet()) {
                if (this.layersActionBarLayout.getVisibility() == 0 && !this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    this.layersActionBarLayout.onKeyUp(keyCode, event);
                } else if (this.rightActionBarLayout.getVisibility() != 0 || this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    this.actionBarLayout.onKeyUp(keyCode, event);
                } else {
                    this.rightActionBarLayout.onKeyUp(keyCode, event);
                }
            } else if (this.actionBarLayout.fragmentsStack.size() != 1) {
                this.actionBarLayout.onKeyUp(keyCode, event);
            } else if (this.drawerLayoutContainer.isDrawerOpened()) {
                this.drawerLayoutContainer.closeDrawer(false);
            } else {
                if (getCurrentFocus() != null) {
                    AndroidUtilities.hideKeyboard(getCurrentFocus());
                }
                this.drawerLayoutContainer.openDrawer(false);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean needPresentFragment(BaseFragment fragment, boolean removeLast, boolean forceWithoutAnimation, ActionBarLayout layout) {
        boolean z = true;
        if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        if (AndroidUtilities.isTablet()) {
            DrawerLayoutContainer drawerLayoutContainer = this.drawerLayoutContainer;
            boolean z2 = ((fragment instanceof LoginActivity) || (fragment instanceof CountrySelectActivity) || this.layersActionBarLayout.getVisibility() == 0) ? false : true;
            drawerLayoutContainer.setAllowOpenDrawer(z2, true);
            if ((fragment instanceof DialogsActivity) && ((DialogsActivity) fragment).isMainDialogList() && layout != this.actionBarLayout) {
                this.actionBarLayout.removeAllFragments();
                this.actionBarLayout.presentFragment(fragment, removeLast, forceWithoutAnimation, false);
                this.layersActionBarLayout.removeAllFragments();
                this.layersActionBarLayout.setVisibility(8);
                this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                if (this.tabletFullSize) {
                    return false;
                }
                this.shadowTabletSide.setVisibility(0);
                if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    return false;
                }
                this.backgroundTablet.setVisibility(0);
                return false;
            } else if (fragment instanceof ChatActivity) {
                int a;
                ActionBarLayout actionBarLayout;
                if ((!this.tabletFullSize && layout == this.rightActionBarLayout) || (this.tabletFullSize && layout == this.actionBarLayout)) {
                    boolean result;
                    if (this.tabletFullSize && layout == this.actionBarLayout && this.actionBarLayout.fragmentsStack.size() == 1) {
                        result = false;
                    } else {
                        result = true;
                    }
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        a = 0;
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                            a = (a - 1) + 1;
                        }
                        actionBarLayout = this.layersActionBarLayout;
                        if (forceWithoutAnimation) {
                            z = false;
                        }
                        actionBarLayout.closeLastFragment(z);
                    }
                    if (!result) {
                        this.actionBarLayout.presentFragment(fragment, false, forceWithoutAnimation, false);
                    }
                    return result;
                } else if (!this.tabletFullSize && layout != this.rightActionBarLayout) {
                    this.rightActionBarLayout.setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.presentFragment(fragment, removeLast, true, false);
                    if (this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        return false;
                    }
                    a = 0;
                    while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                        this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                        a = (a - 1) + 1;
                    }
                    actionBarLayout = this.layersActionBarLayout;
                    if (forceWithoutAnimation) {
                        z = false;
                    }
                    actionBarLayout.closeLastFragment(z);
                    return false;
                } else if (!this.tabletFullSize || layout == this.actionBarLayout) {
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        a = 0;
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                            a = (a - 1) + 1;
                        }
                        r7 = this.layersActionBarLayout;
                        if (forceWithoutAnimation) {
                            z2 = false;
                        } else {
                            z2 = true;
                        }
                        r7.closeLastFragment(z2);
                    }
                    actionBarLayout = this.actionBarLayout;
                    if (this.actionBarLayout.fragmentsStack.size() <= 1) {
                        z = false;
                    }
                    actionBarLayout.presentFragment(fragment, z, forceWithoutAnimation, false);
                    return false;
                } else {
                    r7 = this.actionBarLayout;
                    if (this.actionBarLayout.fragmentsStack.size() > 1) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    r7.presentFragment(fragment, z2, forceWithoutAnimation, false);
                    if (this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        return false;
                    }
                    a = 0;
                    while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                        this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                        a = (a - 1) + 1;
                    }
                    actionBarLayout = this.layersActionBarLayout;
                    if (forceWithoutAnimation) {
                        z = false;
                    }
                    actionBarLayout.closeLastFragment(z);
                    return false;
                }
            } else if (layout == this.layersActionBarLayout) {
                return true;
            } else {
                this.layersActionBarLayout.setVisibility(0);
                this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
                if (fragment instanceof LoginActivity) {
                    this.backgroundTablet.setVisibility(0);
                    this.shadowTabletSide.setVisibility(8);
                    this.shadowTablet.setBackgroundColor(0);
                } else {
                    this.shadowTablet.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
                }
                this.layersActionBarLayout.presentFragment(fragment, removeLast, forceWithoutAnimation, false);
                return false;
            }
        }
        boolean allow = true;
        if (fragment instanceof LoginActivity) {
            if (mainFragmentsStack.size() == 0) {
                allow = false;
            }
        } else if ((fragment instanceof CountrySelectActivity) && mainFragmentsStack.size() == 1) {
            allow = false;
        }
        this.drawerLayoutContainer.setAllowOpenDrawer(allow, false);
        return true;
    }

    public boolean needAddFragmentToStack(BaseFragment fragment, ActionBarLayout layout) {
        if (AndroidUtilities.isTablet()) {
            DrawerLayoutContainer drawerLayoutContainer = this.drawerLayoutContainer;
            boolean z = ((fragment instanceof LoginActivity) || (fragment instanceof CountrySelectActivity) || this.layersActionBarLayout.getVisibility() == 0) ? false : true;
            drawerLayoutContainer.setAllowOpenDrawer(z, true);
            if (fragment instanceof DialogsActivity) {
                if (((DialogsActivity) fragment).isMainDialogList() && layout != this.actionBarLayout) {
                    this.actionBarLayout.removeAllFragments();
                    this.actionBarLayout.addFragmentToStack(fragment);
                    this.layersActionBarLayout.removeAllFragments();
                    this.layersActionBarLayout.setVisibility(8);
                    this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    if (this.tabletFullSize) {
                        return false;
                    }
                    this.shadowTabletSide.setVisibility(0);
                    if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                        return false;
                    }
                    this.backgroundTablet.setVisibility(0);
                    return false;
                }
            } else if (fragment instanceof ChatActivity) {
                int a;
                if (!this.tabletFullSize && layout != this.rightActionBarLayout) {
                    this.rightActionBarLayout.setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.addFragmentToStack(fragment);
                    if (this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        return false;
                    }
                    a = 0;
                    while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                        this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                        a = (a - 1) + 1;
                    }
                    this.layersActionBarLayout.closeLastFragment(true);
                    return false;
                } else if (this.tabletFullSize && layout != this.actionBarLayout) {
                    this.actionBarLayout.addFragmentToStack(fragment);
                    if (this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        return false;
                    }
                    a = 0;
                    while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                        this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                        a = (a - 1) + 1;
                    }
                    this.layersActionBarLayout.closeLastFragment(true);
                    return false;
                }
            } else if (layout != this.layersActionBarLayout) {
                this.layersActionBarLayout.setVisibility(0);
                this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
                if (fragment instanceof LoginActivity) {
                    this.backgroundTablet.setVisibility(0);
                    this.shadowTabletSide.setVisibility(8);
                    this.shadowTablet.setBackgroundColor(0);
                } else {
                    this.shadowTablet.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
                }
                this.layersActionBarLayout.addFragmentToStack(fragment);
                return false;
            }
            return true;
        }
        boolean allow = true;
        if (fragment instanceof LoginActivity) {
            if (mainFragmentsStack.size() == 0) {
                allow = false;
            }
        } else if ((fragment instanceof CountrySelectActivity) && mainFragmentsStack.size() == 1) {
            allow = false;
        }
        this.drawerLayoutContainer.setAllowOpenDrawer(allow, false);
        return true;
    }

    public boolean needCloseLastFragment(ActionBarLayout layout) {
        if (AndroidUtilities.isTablet()) {
            if (layout == this.actionBarLayout && layout.fragmentsStack.size() <= 1) {
                onFinish();
                finish();
                return false;
            } else if (layout == this.rightActionBarLayout) {
                if (!this.tabletFullSize) {
                    this.backgroundTablet.setVisibility(0);
                }
            } else if (layout == this.layersActionBarLayout && this.actionBarLayout.fragmentsStack.isEmpty() && this.layersActionBarLayout.fragmentsStack.size() == 1) {
                onFinish();
                finish();
                return false;
            }
        } else if (layout.fragmentsStack.size() <= 1) {
            onFinish();
            finish();
            return false;
        } else if (layout.fragmentsStack.size() >= 2 && !(layout.fragmentsStack.get(0) instanceof LoginActivity)) {
            this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        }
        return true;
    }

    public void rebuildAllFragments(boolean last) {
        if (this.layersActionBarLayout != null) {
            this.layersActionBarLayout.rebuildAllFragmentViews(last, last);
        } else {
            this.actionBarLayout.rebuildAllFragmentViews(last, last);
        }
    }

    public void onRebuildAllFragments(ActionBarLayout layout, boolean last) {
        if (AndroidUtilities.isTablet() && layout == this.layersActionBarLayout) {
            this.rightActionBarLayout.rebuildAllFragmentViews(last, last);
            this.actionBarLayout.rebuildAllFragmentViews(last, last);
        }
        this.drawerLayoutAdapter.notifyDataSetChanged();
    }
}
