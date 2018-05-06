package org.telegram.ui;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.LocationStatusCodes;
import java.io.File;
import java.util.ArrayList;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.FileLocation;
import org.telegram.TL.TLRPC.InputFile;
import org.telegram.TL.TLRPC.PhotoSize;
import org.telegram.TL.TLRPC.TL_auth_resetAuthorizations;
import org.telegram.TL.TLRPC.TL_boolTrue;
import org.telegram.TL.TLRPC.TL_error;
import org.telegram.TL.TLRPC.TL_inputGeoPointEmpty;
import org.telegram.TL.TLRPC.TL_inputPhotoCropAuto;
import org.telegram.TL.TLRPC.TL_inputPhotoEmpty;
import org.telegram.TL.TLRPC.TL_photos_photo;
import org.telegram.TL.TLRPC.TL_photos_updateProfilePhoto;
import org.telegram.TL.TLRPC.TL_photos_uploadProfilePhoto;
import org.telegram.TL.TLRPC.TL_userProfilePhoto;
import org.telegram.TL.TLRPC.TL_userProfilePhotoEmpty;
import org.telegram.TL.TLRPC.User;
import org.telegram.TL.TLRPC.UserProfilePhoto;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.RPCRequest;
import org.telegram.messenger.RPCRequest.RPCRequestDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.objects.PhotoObject;
import org.telegram.ui.Views.AvatarUpdater;
import org.telegram.ui.Views.AvatarUpdater.AvatarUpdaterDelegate;
import org.telegram.ui.Views.BackupImageView;
import org.telegram.ui.Views.BaseFragment;
import org.telegram.ui.Views.OnSwipeTouchListener;

public class SettingsActivity extends BaseFragment implements NotificationCenterDelegate {
    int askQuestionRow;
    private AvatarUpdater avatarUpdater = new AvatarUpdater();
    int backgroundRow;
    int blockedRow;
    int enableAnimationsRow;
    private ListAdapter listAdapter;
    private ListView listView;
    int logoutRow;
    int messagesSectionRow;
    int notificationRow;
    int numberRow;
    int numberSectionRow;
    int profileRow;
    int rowCount;
    int sendByEnterRow;
    int sendLogsRow;
    int settingsSectionRow;
    int supportSectionRow;
    int terminateSessionsRow;
    int textSizeRow;

    class C05652 implements OnItemClickListener {

        class C05631 implements OnClickListener {
            C05631() {
            }

            public void onClick(DialogInterface dialog, int which) {
                Editor editor = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit();
                editor.putInt("fons_size", which + 12);
                editor.commit();
                if (SettingsActivity.this.listView != null) {
                    SettingsActivity.this.listView.invalidateViews();
                }
            }
        }

        class C05642 implements OnClickListener {

            class C08801 implements RPCRequestDelegate {
                C08801() {
                }

                public void run(TLObject response, TL_error error) {
                    ActionBarActivity inflaterActivity = SettingsActivity.this.parentActivity;
                    if (inflaterActivity == null) {
                        inflaterActivity = (ActionBarActivity) SettingsActivity.this.getActivity();
                    }
                    if (inflaterActivity != null) {
                        if (error == null && (response instanceof TL_boolTrue)) {
                            Toast.makeText(inflaterActivity, C0419R.string.TerminateAllSessions, 0).show();
                        } else {
                            Toast.makeText(inflaterActivity, C0419R.string.UnknownError, 0).show();
                        }
                        UserConfig.registeredForPush = false;
                        MessagesController.Instance.registerForPush(UserConfig.pushString);
                    }
                }
            }

            C05642() {
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                ConnectionsManager.Instance.performRpc(new TL_auth_resetAuthorizations(), new C08801(), null, true, RPCRequest.RPCRequestClassGeneric);
            }
        }

        C05652() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Builder builder;
            if (i == SettingsActivity.this.textSizeRow) {
                builder = new Builder(SettingsActivity.this.parentActivity);
                builder.setTitle(SettingsActivity.this.getStringEntry(C0419R.string.TextSize));
                r8 = new CharSequence[9];
                r8[0] = String.format("%d", new Object[]{Integer.valueOf(12)});
                r8[1] = String.format("%d", new Object[]{Integer.valueOf(13)});
                r8[2] = String.format("%d", new Object[]{Integer.valueOf(14)});
                r8[3] = String.format("%d", new Object[]{Integer.valueOf(15)});
                r8[4] = String.format("%d", new Object[]{Integer.valueOf(16)});
                r8[5] = String.format("%d", new Object[]{Integer.valueOf(17)});
                r8[6] = String.format("%d", new Object[]{Integer.valueOf(18)});
                r8[7] = String.format("%d", new Object[]{Integer.valueOf(19)});
                r8[8] = String.format("%d", new Object[]{Integer.valueOf(20)});
                builder.setItems(r8, new C05631());
                builder.setNegativeButton(SettingsActivity.this.getStringEntry(C0419R.string.Cancel), null);
                builder.show().setCanceledOnTouchOutside(true);
            } else if (i == SettingsActivity.this.enableAnimationsRow) {
                preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                boolean animations = preferences.getBoolean("view_animations", true);
                editor = preferences.edit();
                editor.putBoolean("view_animations", !animations);
                editor.commit();
                if (SettingsActivity.this.listView != null) {
                    SettingsActivity.this.listView.invalidateViews();
                }
            } else if (i == SettingsActivity.this.notificationRow) {
                ((ApplicationActivity) SettingsActivity.this.parentActivity).presentFragment(new SettingsNotificationsActivity(), "settings_notifications", false);
            } else if (i == SettingsActivity.this.blockedRow) {
                ((ApplicationActivity) SettingsActivity.this.parentActivity).presentFragment(new SettingsBlockedUsers(), "settings_blocked", false);
            } else if (i == SettingsActivity.this.backgroundRow) {
                ((ApplicationActivity) SettingsActivity.this.parentActivity).presentFragment(new SettingsWallpapersActivity(), "settings_wallpapers", false);
            } else if (i == SettingsActivity.this.askQuestionRow) {
                ChatActivity fragment = new ChatActivity();
                Bundle bundle = new Bundle();
                bundle.putInt("user_id", 333000);
                fragment.setArguments(bundle);
                ((ApplicationActivity) SettingsActivity.this.parentActivity).presentFragment(fragment, "chat" + Math.random(), false);
            } else if (i == SettingsActivity.this.sendLogsRow) {
                SettingsActivity.this.sendLogs();
            } else if (i == SettingsActivity.this.sendByEnterRow) {
                preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                boolean send = preferences.getBoolean("send_by_enter", false);
                editor = preferences.edit();
                editor.putBoolean("send_by_enter", !send);
                editor.commit();
                if (SettingsActivity.this.listView != null) {
                    SettingsActivity.this.listView.invalidateViews();
                }
            } else if (i == SettingsActivity.this.terminateSessionsRow) {
                builder = new Builder(SettingsActivity.this.parentActivity);
                builder.setMessage(SettingsActivity.this.getStringEntry(C0419R.string.AreYouSure));
                builder.setTitle(SettingsActivity.this.getStringEntry(C0419R.string.AppName));
                builder.setPositiveButton(SettingsActivity.this.getStringEntry(C0419R.string.OK), new C05642());
                builder.setNegativeButton(SettingsActivity.this.getStringEntry(C0419R.string.Cancel), null);
                builder.show().setCanceledOnTouchOutside(true);
            }
        }
    }

    private class ListAdapter extends BaseAdapter {
        private Context mContext;

        class C05661 implements View.OnClickListener {
            C05661() {
            }

            public void onClick(View view) {
                ((ApplicationActivity) SettingsActivity.this.parentActivity).presentFragment(new SettingsChangeNameActivity(), "change_name", false);
            }
        }

        class C05692 implements View.OnClickListener {
            C05692() {
            }

            public void onClick(View view) {
                Builder builder = new Builder(SettingsActivity.this.parentActivity);
                User user = (User) MessagesController.Instance.users.get(Integer.valueOf(UserConfig.clientUserId));
                if (user == null) {
                    user = UserConfig.currentUser;
                }
                if (user != null) {
                    CharSequence[] items;
                    boolean fullMenu = false;
                    if (user.photo == null || user.photo.photo_big == null || (user.photo instanceof TL_userProfilePhotoEmpty)) {
                        items = new CharSequence[]{SettingsActivity.this.getStringEntry(C0419R.string.FromCamera), SettingsActivity.this.getStringEntry(C0419R.string.FromGalley)};
                    } else {
                        items = new CharSequence[]{SettingsActivity.this.getStringEntry(C0419R.string.OpenPhoto), SettingsActivity.this.getStringEntry(C0419R.string.FromCamera), SettingsActivity.this.getStringEntry(C0419R.string.FromGalley), SettingsActivity.this.getStringEntry(C0419R.string.DeletePhoto)};
                        fullMenu = true;
                    }
                    final boolean full = fullMenu;
                    builder.setItems(items, new OnClickListener() {

                        class C08821 implements RPCRequestDelegate {

                            class C05671 implements Runnable {
                                C05671() {
                                }

                                public void run() {
                                    NotificationCenter.Instance.postNotificationName(3, Integer.valueOf(127));
                                    UserConfig.saveConfig(true);
                                }
                            }

                            C08821() {
                            }

                            public void run(TLObject response, TL_error error) {
                                if (error == null) {
                                    User user = (User) MessagesController.Instance.users.get(Integer.valueOf(UserConfig.clientUserId));
                                    if (user == null) {
                                        user = UserConfig.currentUser;
                                        MessagesController.Instance.users.put(Integer.valueOf(user.id), user);
                                    } else {
                                        UserConfig.currentUser = user;
                                    }
                                    if (user != null) {
                                        MessagesStorage.Instance.clearUserPhotos(user.id);
                                        ArrayList<User> users = new ArrayList();
                                        users.add(user);
                                        MessagesStorage.Instance.putUsersAndChats(users, null, false, true);
                                        user.photo = (UserProfilePhoto) response;
                                        Utilities.RunOnUIThread(new C05671());
                                    }
                                }
                            }
                        }

                        public void onClick(DialogInterface dialogInterface, int i) {
                            User user;
                            if (i == 0 && full) {
                                user = (User) MessagesController.Instance.users.get(Integer.valueOf(UserConfig.clientUserId));
                                if (user != null && user.photo != null && user.photo.photo_big != null) {
                                    NotificationCenter.Instance.addToMemCache(56, Integer.valueOf(user.id));
                                    NotificationCenter.Instance.addToMemCache(53, user.photo.photo_big);
                                    SettingsActivity.this.startActivity(new Intent(SettingsActivity.this.parentActivity, GalleryImageViewer.class));
                                }
                            } else if ((i == 0 && !full) || (i == 1 && full)) {
                                SettingsActivity.this.avatarUpdater.openCamera();
                            } else if ((i == 1 && !full) || (i == 2 && full)) {
                                SettingsActivity.this.avatarUpdater.openGallery();
                            } else if (i == 3) {
                                TL_photos_updateProfilePhoto req = new TL_photos_updateProfilePhoto();
                                req.id = new TL_inputPhotoEmpty();
                                req.crop = new TL_inputPhotoCropAuto();
                                UserConfig.currentUser.photo = new TL_userProfilePhotoEmpty();
                                user = (User) MessagesController.Instance.users.get(Integer.valueOf(UserConfig.clientUserId));
                                if (user == null) {
                                    user = UserConfig.currentUser;
                                }
                                if (user != null) {
                                    if (user != null) {
                                        user.photo = UserConfig.currentUser.photo;
                                    }
                                    NotificationCenter.Instance.postNotificationName(3, Integer.valueOf(127));
                                    ConnectionsManager.Instance.performRpc(req, new C08821(), null, true, RPCRequest.RPCRequestClassGeneric);
                                }
                            }
                        }
                    });
                    builder.show().setCanceledOnTouchOutside(true);
                }
            }
        }

        class C05713 implements View.OnClickListener {

            class C05701 implements OnClickListener {
                C05701() {
                }

                public void onClick(DialogInterface dialogInterface, int i) {
                    NotificationCenter.Instance.postNotificationName(1234, new Object[0]);
                    MessagesController.Instance.unregistedPush();
                    SettingsActivity.this.listView.setAdapter(null);
                    UserConfig.clearConfig();
                }
            }

            C05713() {
            }

            public void onClick(View view) {
                Builder builder = new Builder(SettingsActivity.this.parentActivity);
                builder.setMessage(SettingsActivity.this.getStringEntry(C0419R.string.AreYouSure));
                builder.setTitle(SettingsActivity.this.getStringEntry(C0419R.string.AppName));
                builder.setPositiveButton(SettingsActivity.this.getStringEntry(C0419R.string.OK), new C05701());
                builder.setNegativeButton(SettingsActivity.this.getStringEntry(C0419R.string.Cancel), null);
                builder.show().setCanceledOnTouchOutside(true);
            }
        }

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean areAllItemsEnabled() {
            return false;
        }

        public boolean isEnabled(int i) {
            return i == SettingsActivity.this.textSizeRow || i == SettingsActivity.this.enableAnimationsRow || i == SettingsActivity.this.blockedRow || i == SettingsActivity.this.notificationRow || i == SettingsActivity.this.backgroundRow || i == SettingsActivity.this.askQuestionRow || i == SettingsActivity.this.sendLogsRow || i == SettingsActivity.this.sendByEnterRow || i == SettingsActivity.this.terminateSessionsRow;
        }

        public int getCount() {
            return SettingsActivity.this.rowCount;
        }

        public Object getItem(int i) {
            return null;
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public boolean hasStableIds() {
            return false;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            int type = getItemViewType(i);
            TextView textView;
            User user;
            if (type == 0) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_name_layout, viewGroup, false);
                    ((ImageButton) view.findViewById(C0419R.id.settings_edit_name)).setOnClickListener(new C05661());
                    ((ImageButton) view.findViewById(C0419R.id.settings_change_avatar_button)).setOnClickListener(new C05692());
                }
                textView = (TextView) view.findViewById(C0419R.id.settings_name);
                textView.setTypeface(Utilities.getTypeface("fonts/rmedium.ttf"));
                user = (User) MessagesController.Instance.users.get(Integer.valueOf(UserConfig.clientUserId));
                if (user == null) {
                    user = UserConfig.currentUser;
                }
                if (user != null) {
                    textView.setText(Utilities.formatName(user.first_name, user.last_name));
                    BackupImageView avatarImage = (BackupImageView) view.findViewById(C0419R.id.settings_avatar_image);
                    FileLocation photo = null;
                    if (user.photo != null) {
                        photo = user.photo.photo_small;
                    }
                    avatarImage.setImage(photo, null, Utilities.getUserAvatarForId(user.id));
                }
                return view;
            }
            if (type == 1) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_section_layout, viewGroup, false);
                }
                textView = (TextView) view.findViewById(C0419R.id.settings_section_text);
                if (i == SettingsActivity.this.numberSectionRow) {
                    textView.setText(SettingsActivity.this.getStringEntry(C0419R.string.YourPhoneNumber));
                } else {
                    if (i == SettingsActivity.this.settingsSectionRow) {
                        textView.setText(SettingsActivity.this.getStringEntry(C0419R.string.SETTINGS));
                    } else {
                        if (i == SettingsActivity.this.supportSectionRow) {
                            textView.setText(SettingsActivity.this.getStringEntry(C0419R.string.Support));
                        } else {
                            if (i == SettingsActivity.this.messagesSectionRow) {
                                textView.setText(SettingsActivity.this.getStringEntry(C0419R.string.MessagesSettings));
                            }
                        }
                    }
                }
            } else if (type == 2) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_row_button_layout, viewGroup, false);
                }
                textView = (TextView) view.findViewById(C0419R.id.settings_row_text);
                divider = view.findViewById(C0419R.id.settings_row_divider);
                if (i == SettingsActivity.this.numberRow) {
                    user = UserConfig.currentUser;
                    if (user == null || user.phone == null || user.phone.length() == 0) {
                        textView.setText("Unknown");
                    } else {
                        textView.setText(PhoneFormat.Instance.format("+" + user.phone));
                    }
                    divider.setVisibility(4);
                } else {
                    if (i == SettingsActivity.this.notificationRow) {
                        textView.setText(SettingsActivity.this.getStringEntry(C0419R.string.NotificationsAndSounds));
                        divider.setVisibility(0);
                    } else {
                        if (i == SettingsActivity.this.blockedRow) {
                            textView.setText(SettingsActivity.this.getStringEntry(C0419R.string.BlockedUsers));
                            divider.setVisibility(SettingsActivity.this.backgroundRow != 0 ? 0 : 4);
                        } else {
                            if (i == SettingsActivity.this.backgroundRow) {
                                textView.setText(SettingsActivity.this.getStringEntry(C0419R.string.ChatBackground));
                                divider.setVisibility(0);
                            } else {
                                if (i == SettingsActivity.this.sendLogsRow) {
                                    textView.setText("Send Logs");
                                    divider.setVisibility(0);
                                } else {
                                    if (i == SettingsActivity.this.askQuestionRow) {
                                        textView.setText(SettingsActivity.this.getStringEntry(C0419R.string.AskAQuestion));
                                        divider.setVisibility(4);
                                    } else {
                                        if (i == SettingsActivity.this.terminateSessionsRow) {
                                            textView.setText(SettingsActivity.this.getStringEntry(C0419R.string.TerminateAllSessions));
                                            divider.setVisibility(4);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (type == 3) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_row_check_layout, viewGroup, false);
                }
                textView = (TextView) view.findViewById(C0419R.id.settings_row_text);
                divider = view.findViewById(C0419R.id.settings_row_divider);
                ImageView checkButton = (ImageView) view.findViewById(C0419R.id.settings_row_check_button);
                SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                if (i == SettingsActivity.this.enableAnimationsRow) {
                    textView.setText(SettingsActivity.this.getStringEntry(C0419R.string.EnableAnimations));
                    divider.setVisibility(0);
                    if (preferences.getBoolean("view_animations", true)) {
                        checkButton.setImageResource(C0419R.drawable.btn_check_on);
                    } else {
                        checkButton.setImageResource(C0419R.drawable.btn_check_off);
                    }
                } else {
                    if (i == SettingsActivity.this.sendByEnterRow) {
                        textView.setText(SettingsActivity.this.getStringEntry(C0419R.string.SendByEnter));
                        divider.setVisibility(4);
                        if (preferences.getBoolean("send_by_enter", false)) {
                            checkButton.setImageResource(C0419R.drawable.btn_check_on);
                        } else {
                            checkButton.setImageResource(C0419R.drawable.btn_check_off);
                        }
                    }
                }
            } else if (type == 4) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_logout_button, viewGroup, false);
                    ((TextView) view.findViewById(C0419R.id.settings_row_text)).setOnClickListener(new C05713());
                }
            } else if (type == 5) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.user_profile_leftright_row_layout, viewGroup, false);
                }
                textView = (TextView) view.findViewById(C0419R.id.settings_row_text);
                TextView detailTextView = (TextView) view.findViewById(C0419R.id.settings_row_text_detail);
                divider = view.findViewById(C0419R.id.settings_row_divider);
                if (i == SettingsActivity.this.textSizeRow) {
                    int size = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).getInt("fons_size", 16);
                    detailTextView.setText(String.format("%d", new Object[]{Integer.valueOf(size)}));
                    textView.setText(ApplicationLoader.applicationContext.getString(C0419R.string.TextSize));
                    divider.setVisibility(0);
                }
            }
            return view;
        }

        public int getItemViewType(int i) {
            if (i == SettingsActivity.this.profileRow) {
                return 0;
            }
            if (i == SettingsActivity.this.numberSectionRow || i == SettingsActivity.this.settingsSectionRow || i == SettingsActivity.this.supportSectionRow || i == SettingsActivity.this.messagesSectionRow) {
                return 1;
            }
            if (i == SettingsActivity.this.textSizeRow) {
                return 5;
            }
            if (i == SettingsActivity.this.enableAnimationsRow || i == SettingsActivity.this.sendByEnterRow) {
                return 3;
            }
            if (i == SettingsActivity.this.numberRow || i == SettingsActivity.this.notificationRow || i == SettingsActivity.this.blockedRow || i == SettingsActivity.this.backgroundRow || i == SettingsActivity.this.askQuestionRow || i == SettingsActivity.this.sendLogsRow || i == SettingsActivity.this.terminateSessionsRow || i != SettingsActivity.this.logoutRow) {
                return 2;
            }
            return 4;
        }

        public int getViewTypeCount() {
            return 6;
        }

        public boolean isEmpty() {
            return false;
        }
    }

    class C08791 implements AvatarUpdaterDelegate {

        class C08781 implements RPCRequestDelegate {

            class C05621 implements Runnable {
                C05621() {
                }

                public void run() {
                    NotificationCenter.Instance.postNotificationName(3, Integer.valueOf(127));
                    UserConfig.saveConfig(true);
                }
            }

            C08781() {
            }

            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    User user = (User) MessagesController.Instance.users.get(Integer.valueOf(UserConfig.clientUserId));
                    if (user == null) {
                        user = UserConfig.currentUser;
                        if (user != null) {
                            MessagesController.Instance.users.put(Integer.valueOf(user.id), user);
                        } else {
                            return;
                        }
                    }
                    UserConfig.currentUser = user;
                    if (user != null) {
                        TL_photos_photo photo = (TL_photos_photo) response;
                        ArrayList<PhotoSize> sizes = photo.photo.sizes;
                        PhotoSize smallSize = PhotoObject.getClosestPhotoSizeWithSize(sizes, 100, 100);
                        PhotoSize bigSize = PhotoObject.getClosestPhotoSizeWithSize(sizes, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE);
                        user.photo = new TL_userProfilePhoto();
                        user.photo.photo_id = photo.photo.id;
                        if (smallSize != null) {
                            user.photo.photo_small = smallSize.location;
                        }
                        if (bigSize != null) {
                            user.photo.photo_big = bigSize.location;
                        } else if (smallSize != null) {
                            user.photo.photo_small = smallSize.location;
                        }
                        MessagesStorage.Instance.clearUserPhotos(user.id);
                        ArrayList<User> users = new ArrayList();
                        users.add(user);
                        MessagesStorage.Instance.putUsersAndChats(users, null, false, true);
                        Utilities.RunOnUIThread(new C05621());
                    }
                }
            }
        }

        C08791() {
        }

        public void didUploadedPhoto(InputFile file, PhotoSize small, PhotoSize big) {
            TL_photos_uploadProfilePhoto req = new TL_photos_uploadProfilePhoto();
            req.caption = BuildConfig.FLAVOR;
            req.crop = new TL_inputPhotoCropAuto();
            req.file = file;
            req.geo_point = new TL_inputGeoPointEmpty();
            ConnectionsManager.Instance.performRpc(req, new C08781(), null, true, RPCRequest.RPCRequestClassGeneric);
        }
    }

    class C08813 extends OnSwipeTouchListener {
        C08813() {
        }

        public void onSwipeRight() {
            SettingsActivity.this.finishFragment(true);
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.avatarUpdater.parentFragment = this;
        this.avatarUpdater.delegate = new C08791();
        NotificationCenter.Instance.addObserver(this, 3);
        this.rowCount = 0;
        int i = this.rowCount;
        this.rowCount = i + 1;
        this.profileRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.numberSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.numberRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.settingsSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.enableAnimationsRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.notificationRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.blockedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.backgroundRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.terminateSessionsRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.messagesSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.textSizeRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.sendByEnterRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.supportSectionRow = i;
        if (ConnectionsManager.DEBUG_VERSION) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.sendLogsRow = i;
        }
        i = this.rowCount;
        this.rowCount = i + 1;
        this.askQuestionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.logoutRow = i;
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.Instance.removeObserver(this, 3);
        this.avatarUpdater.clear();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.fragmentView == null) {
            this.fragmentView = inflater.inflate(C0419R.layout.settings_layout, container, false);
            this.listAdapter = new ListAdapter(this.parentActivity);
            this.listView = (ListView) this.fragmentView.findViewById(C0419R.id.listView);
            this.listView.setAdapter(this.listAdapter);
            this.listView.setOnItemClickListener(new C05652());
            this.listView.setOnTouchListener(new C08813());
        } else {
            ViewGroup parent = (ViewGroup) this.fragmentView.getParent();
            if (parent != null) {
                parent.removeView(this.fragmentView);
            }
        }
        return this.fragmentView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 232) {
            FileLog.cleanupLogs();
        } else {
            this.avatarUpdater.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void didReceivedNotification(int id, Object... args) {
        if (id == 3) {
            int mask = ((Integer) args[0]).intValue();
            if (((mask & 2) != 0 || (mask & 1) != 0) && this.listView != null) {
                this.listView.invalidateViews();
            }
        }
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
            actionBar.setTitle(getStringEntry(C0419R.string.Settings));
            TextView title = (TextView) this.parentActivity.findViewById(C0419R.id.action_bar_title);
            if (title == null) {
                title = (TextView) this.parentActivity.findViewById(this.parentActivity.getResources().getIdentifier("action_bar_title", "id", "android"));
            }
            if (title != null) {
                title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                title.setCompoundDrawablePadding(0);
            }
        }
    }

    private void sendLogs() {
        try {
            ArrayList<Uri> uris = new ArrayList();
            for (File file : new File(ApplicationLoader.applicationContext.getExternalFilesDir(null).getAbsolutePath() + "/logs").listFiles()) {
                uris.add(Uri.fromFile(file));
            }
            if (!uris.isEmpty()) {
                Intent i = new Intent("android.intent.action.SEND_MULTIPLE");
                i.setType("message/rfc822");
                i.putExtra("android.intent.extra.EMAIL", new String[]{"drklo.2kb@gmail.com"});
                i.putExtra("android.intent.extra.SUBJECT", "last logs");
                i.putParcelableArrayListExtra("android.intent.extra.STREAM", uris);
                startActivityForResult(Intent.createChooser(i, "Select email application."), 232);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        super.onResume();
        if (!this.isFinish && getActivity() != null) {
            if (!(this.firstStart || this.listAdapter == null)) {
                this.listAdapter.notifyDataSetChanged();
            }
            this.firstStart = false;
            ((ApplicationActivity) this.parentActivity).showActionBar();
            ((ApplicationActivity) this.parentActivity).updateActionBar();
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
