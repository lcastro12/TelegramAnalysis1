package org.telegram.ui;

import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings.System;
import android.support.v7.app.ActionBar;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.plus.PlusShare;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.EncryptedChat;
import org.telegram.TL.TLRPC.FileLocation;
import org.telegram.TL.TLRPC.TL_contactBlocked;
import org.telegram.TL.TLRPC.TL_contacts_block;
import org.telegram.TL.TLRPC.TL_encryptedChat;
import org.telegram.TL.TLRPC.TL_error;
import org.telegram.TL.TLRPC.TL_inputUserContact;
import org.telegram.TL.TLRPC.TL_inputUserForeign;
import org.telegram.TL.TLRPC.TL_userEmpty;
import org.telegram.TL.TLRPC.TL_userForeign;
import org.telegram.TL.TLRPC.TL_userRequest;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.RPCRequest;
import org.telegram.messenger.RPCRequest.RPCRequestDelegate;
import org.telegram.messenger.Utilities;
import org.telegram.ui.MessagesActivity.MessagesActivityDelegate;
import org.telegram.ui.Views.BackupImageView;
import org.telegram.ui.Views.BaseFragment;
import org.telegram.ui.Views.IdenticonView;
import org.telegram.ui.Views.OnSwipeTouchListener;

public class UserProfileActivity extends BaseFragment implements NotificationCenterDelegate, MessagesActivityDelegate {
    private boolean creatingChat = false;
    private EncryptedChat currentEncryptedChat;
    private long dialog_id;
    private ListAdapter listAdapter;
    private ListView listView;
    private String selectedPhone;
    private int totalMediaCount = -1;
    private int user_id;

    class C05921 implements OnClickListener {
        C05921() {
        }

        public void onClick(View view) {
            UserProfileActivity.this.creatingChat = true;
            MessagesController.Instance.startSecretChat(UserProfileActivity.this.parentActivity, UserProfileActivity.this.user_id);
        }
    }

    class C05942 implements OnItemClickListener {

        class C05931 implements DialogInterface.OnClickListener {
            C05931() {
            }

            public void onClick(DialogInterface dialog, int which) {
                int oldValue = UserProfileActivity.this.currentEncryptedChat.ttl;
                if (which == 0) {
                    UserProfileActivity.this.currentEncryptedChat.ttl = 0;
                } else if (which == 1) {
                    UserProfileActivity.this.currentEncryptedChat.ttl = 2;
                } else if (which == 2) {
                    UserProfileActivity.this.currentEncryptedChat.ttl = 5;
                } else if (which == 3) {
                    UserProfileActivity.this.currentEncryptedChat.ttl = 60;
                } else if (which == 4) {
                    UserProfileActivity.this.currentEncryptedChat.ttl = ConnectionsManager.DC_UPDATE_TIME;
                } else if (which == 5) {
                    UserProfileActivity.this.currentEncryptedChat.ttl = 86400;
                } else if (which == 6) {
                    UserProfileActivity.this.currentEncryptedChat.ttl = 604800;
                }
                if (oldValue != UserProfileActivity.this.currentEncryptedChat.ttl) {
                    if (UserProfileActivity.this.listView != null) {
                        UserProfileActivity.this.listView.invalidateViews();
                    }
                    MessagesController.Instance.sendTTLMessage(UserProfileActivity.this.currentEncryptedChat);
                    MessagesStorage.Instance.updateEncryptedChat(UserProfileActivity.this.currentEncryptedChat);
                }
            }
        }

        C05942() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            SharedPreferences preferences;
            if ((i == 4 && UserProfileActivity.this.dialog_id == 0) || (UserProfileActivity.this.dialog_id != 0 && ((i == 6 && (UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat)) || (i == 4 && !(UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat))))) {
                String key;
                preferences = UserProfileActivity.this.parentActivity.getSharedPreferences("Notifications", 0);
                if (UserProfileActivity.this.dialog_id == 0) {
                    key = "notify_" + UserProfileActivity.this.user_id;
                } else {
                    key = "notify_" + UserProfileActivity.this.dialog_id;
                }
                boolean value = preferences.getBoolean(key, true);
                Editor editor = preferences.edit();
                editor.putBoolean(key, !value);
                editor.commit();
                UserProfileActivity.this.listView.invalidateViews();
            } else if ((i == 5 && UserProfileActivity.this.dialog_id == 0) || (UserProfileActivity.this.dialog_id != 0 && ((i == 7 && (UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat)) || (i == 5 && !(UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat))))) {
                try {
                    Intent tmpIntent = new Intent("android.intent.action.RINGTONE_PICKER");
                    tmpIntent.putExtra("android.intent.extra.ringtone.TYPE", 2);
                    tmpIntent.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", false);
                    preferences = UserProfileActivity.this.parentActivity.getSharedPreferences("Notifications", 0);
                    Uri currentSound = null;
                    String defaultPath = null;
                    Uri defaultUri = System.DEFAULT_NOTIFICATION_URI;
                    if (defaultUri != null) {
                        defaultPath = defaultUri.getPath();
                    }
                    String path = preferences.getString("sound_path_" + UserProfileActivity.this.user_id, defaultPath);
                    if (!(path == null || path.equals("NoSound"))) {
                        currentSound = path.equals(defaultPath) ? defaultUri : Uri.parse(path);
                    }
                    tmpIntent.putExtra("android.intent.extra.ringtone.EXISTING_URI", currentSound);
                    UserProfileActivity.this.startActivityForResult(tmpIntent, 0);
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            } else if ((i == 7 && UserProfileActivity.this.dialog_id == 0) || (UserProfileActivity.this.dialog_id != 0 && ((i == 9 && (UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat)) || (i == 7 && !(UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat))))) {
                MediaActivity fragment = new MediaActivity();
                bundle = new Bundle();
                if (UserProfileActivity.this.dialog_id != 0) {
                    bundle.putLong("dialog_id", UserProfileActivity.this.dialog_id);
                } else {
                    bundle.putLong("dialog_id", (long) UserProfileActivity.this.user_id);
                }
                fragment.setArguments(bundle);
                ((ApplicationActivity) UserProfileActivity.this.parentActivity).presentFragment(fragment, "media_user_" + UserProfileActivity.this.user_id, false);
            } else if (i == 5 && UserProfileActivity.this.dialog_id != 0 && (UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat)) {
                IdenticonActivity fragment2 = new IdenticonActivity();
                bundle = new Bundle();
                bundle.putInt("chat_id", (int) (UserProfileActivity.this.dialog_id >> 32));
                fragment2.setArguments(bundle);
                ((ApplicationActivity) UserProfileActivity.this.parentActivity).presentFragment(fragment2, "key_" + UserProfileActivity.this.dialog_id, false);
            } else if (i == 4 && UserProfileActivity.this.dialog_id != 0 && (UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat)) {
                Builder builder = new Builder(UserProfileActivity.this.parentActivity);
                builder.setTitle(UserProfileActivity.this.getStringEntry(C0419R.string.MessageLifetime));
                builder.setItems(new CharSequence[]{UserProfileActivity.this.getStringEntry(C0419R.string.ShortMessageLifetimeForever), UserProfileActivity.this.getStringEntry(C0419R.string.ShortMessageLifetime2s), UserProfileActivity.this.getStringEntry(C0419R.string.ShortMessageLifetime5s), UserProfileActivity.this.getStringEntry(C0419R.string.ShortMessageLifetime1m), UserProfileActivity.this.getStringEntry(C0419R.string.ShortMessageLifetime1h), UserProfileActivity.this.getStringEntry(C0419R.string.ShortMessageLifetime1d), UserProfileActivity.this.getStringEntry(C0419R.string.ShortMessageLifetime1w)}, new C05931());
                builder.setNegativeButton(UserProfileActivity.this.getStringEntry(C0419R.string.Cancel), null);
                builder.show().setCanceledOnTouchOutside(true);
            }
        }
    }

    class C05954 implements OnPreDrawListener {
        C05954() {
        }

        public boolean onPreDraw() {
            UserProfileActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
            if (UserProfileActivity.this.dialog_id != 0) {
                TextView title = (TextView) UserProfileActivity.this.parentActivity.findViewById(C0419R.id.action_bar_title);
                if (title == null) {
                    title = (TextView) UserProfileActivity.this.parentActivity.findViewById(ApplicationLoader.applicationContext.getResources().getIdentifier("action_bar_title", "id", "android"));
                }
                if (title != null) {
                    title.setCompoundDrawablesWithIntrinsicBounds(C0419R.drawable.ic_lock_white, 0, 0, 0);
                    title.setCompoundDrawablePadding(Utilities.dp(4));
                }
            }
            return false;
        }
    }

    private class ListAdapter extends BaseAdapter {
        private Context mContext;

        class C05961 implements OnClickListener {
            C05961() {
            }

            public void onClick(View view) {
                User user = (User) MessagesController.Instance.users.get(Integer.valueOf(UserProfileActivity.this.user_id));
                if (user.photo != null && user.photo.photo_big != null) {
                    NotificationCenter.Instance.addToMemCache(56, Integer.valueOf(UserProfileActivity.this.user_id));
                    NotificationCenter.Instance.addToMemCache(53, user.photo.photo_big);
                    UserProfileActivity.this.startActivity(new Intent(UserProfileActivity.this.parentActivity, GalleryImageViewer.class));
                }
            }
        }

        class C05993 implements OnClickListener {
            C05993() {
            }

            public void onClick(View view) {
                User user = (User) MessagesController.Instance.users.get(Integer.valueOf(UserProfileActivity.this.user_id));
                if (user != null && !(user instanceof TL_userEmpty)) {
                    NotificationCenter.Instance.postNotificationName(5, new Object[0]);
                    ChatActivity fragment = new ChatActivity();
                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id", UserProfileActivity.this.user_id);
                    fragment.setArguments(bundle);
                    ((ApplicationActivity) UserProfileActivity.this.parentActivity).presentFragment(fragment, "chat" + Math.random(), true, false);
                }
            }
        }

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean areAllItemsEnabled() {
            return false;
        }

        public boolean isEnabled(int i) {
            if (UserProfileActivity.this.dialog_id == 0) {
                if (i == 2 || i == 4 || i == 5 || i == 7) {
                    return true;
                }
                return false;
            } else if (UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat) {
                if (i == 2 || i == 4 || i == 5 || i == 6 || i == 7 || i == 9) {
                    return true;
                }
                return false;
            } else if (i == 2 || i == 4 || i == 5 || i == 9) {
                return true;
            } else {
                return false;
            }
        }

        public int getCount() {
            if (UserProfileActivity.this.dialog_id != 0 && (UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat)) {
                return 10;
            }
            return 8;
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
            User user;
            if (type == 0) {
                TextView onlineText;
                BackupImageView avatarImage;
                user = (User) MessagesController.Instance.users.get(Integer.valueOf(UserProfileActivity.this.user_id));
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.user_profile_avatar_layout, viewGroup, false);
                    onlineText = (TextView) view.findViewById(C0419R.id.settings_online);
                    avatarImage = (BackupImageView) view.findViewById(C0419R.id.settings_avatar_image);
                    avatarImage.setOnClickListener(new C05961());
                } else {
                    avatarImage = (BackupImageView) view.findViewById(C0419R.id.settings_avatar_image);
                    onlineText = (TextView) view.findViewById(C0419R.id.settings_online);
                }
                TextView textView = (TextView) view.findViewById(C0419R.id.settings_name);
                textView.setTypeface(Utilities.getTypeface("fonts/rmedium.ttf"));
                textView.setText(Utilities.formatName(user.first_name, user.last_name));
                if (user.status == null) {
                    onlineText.setText(UserProfileActivity.this.getStringEntry(C0419R.string.Offline));
                } else {
                    int currentTime = ConnectionsManager.Instance.getCurrentTime();
                    if (user.status.expires > currentTime || user.status.was_online > currentTime) {
                        onlineText.setText(UserProfileActivity.this.getStringEntry(C0419R.string.Online));
                    } else if (user.status.was_online > 10000 || user.status.expires > 10000) {
                        int value = user.status.was_online;
                        if (value == 0) {
                            value = user.status.expires;
                        }
                        onlineText.setText(String.format("%s %s", new Object[]{UserProfileActivity.this.getStringEntry(C0419R.string.LastSeen), Utilities.formatDateOnline((long) value)}));
                    } else {
                        onlineText.setText(UserProfileActivity.this.getStringEntry(C0419R.string.Invisible));
                    }
                }
                FileLocation photo = null;
                if (user.photo != null) {
                    photo = user.photo.photo_small;
                }
                avatarImage.setImage(photo, "50_50", Utilities.getUserAvatarForId(user.id));
                return view;
            }
            if (type == 1) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_section_layout, viewGroup, false);
                }
                textView = (TextView) view.findViewById(C0419R.id.settings_section_text);
                if (i == 1) {
                    textView.setText(UserProfileActivity.this.getStringEntry(C0419R.string.PHONE));
                } else if (i == 3) {
                    textView.setText(UserProfileActivity.this.getStringEntry(C0419R.string.SETTINGS));
                } else if ((i == 6 && UserProfileActivity.this.dialog_id == 0) || (UserProfileActivity.this.dialog_id != 0 && ((i == 8 && (UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat)) || (i == 6 && !(UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat))))) {
                    textView.setText(UserProfileActivity.this.getStringEntry(C0419R.string.SHAREDMEDIA));
                }
            } else if (type == 2) {
                user = (User) MessagesController.Instance.users.get(Integer.valueOf(UserProfileActivity.this.user_id));
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.user_profile_phone_layout, viewGroup, false);
                    final User user2 = user;
                    view.setOnClickListener(new OnClickListener() {

                        class C05971 implements DialogInterface.OnClickListener {
                            C05971() {
                            }

                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 1) {
                                    try {
                                        Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:+" + UserProfileActivity.this.selectedPhone));
                                        intent.addFlags(268435456);
                                        UserProfileActivity.this.startActivity(intent);
                                    } catch (Exception e) {
                                        FileLog.m799e("tmessages", e);
                                    }
                                } else if (i != 0) {
                                } else {
                                    if (VERSION.SDK_INT < 11) {
                                        ((ClipboardManager) UserProfileActivity.this.parentActivity.getSystemService("clipboard")).setText(UserProfileActivity.this.selectedPhone);
                                    } else {
                                        ((android.content.ClipboardManager) UserProfileActivity.this.parentActivity.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(PlusShare.KEY_CALL_TO_ACTION_LABEL, UserProfileActivity.this.selectedPhone));
                                    }
                                }
                            }
                        }

                        public void onClick(View view) {
                            if (user2.phone != null && user2.phone.length() != 0) {
                                UserProfileActivity.this.selectedPhone = user2.phone;
                                Builder builder = new Builder(UserProfileActivity.this.parentActivity);
                                builder.setItems(new CharSequence[]{UserProfileActivity.this.getStringEntry(C0419R.string.Copy), UserProfileActivity.this.getStringEntry(C0419R.string.Call)}, new C05971());
                                builder.show().setCanceledOnTouchOutside(true);
                            }
                        }
                    });
                }
                ((ImageButton) view.findViewById(C0419R.id.settings_edit_name)).setOnClickListener(new C05993());
                textView = (TextView) view.findViewById(C0419R.id.settings_row_text);
                detailTextView = (TextView) view.findViewById(C0419R.id.settings_row_text_detail);
                divider = view.findViewById(C0419R.id.settings_row_divider);
                if (i == 2) {
                    if (user.phone == null || user.phone.length() == 0) {
                        textView.setText("Unknown");
                    } else {
                        textView.setText(PhoneFormat.Instance.format("+" + user.phone));
                    }
                    divider.setVisibility(4);
                    detailTextView.setText(UserProfileActivity.this.getStringEntry(C0419R.string.PhoneMobile));
                }
            } else if (type == 3) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_row_check_layout, viewGroup, false);
                }
                textView = (TextView) view.findViewById(C0419R.id.settings_row_text);
                divider = view.findViewById(C0419R.id.settings_row_divider);
                if ((i == 4 && UserProfileActivity.this.dialog_id == 0) || (UserProfileActivity.this.dialog_id != 0 && ((i == 6 && (UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat)) || (i == 4 && !(UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat))))) {
                    String key;
                    SharedPreferences preferences = this.mContext.getSharedPreferences("Notifications", 0);
                    if (UserProfileActivity.this.dialog_id == 0) {
                        key = "notify_" + UserProfileActivity.this.user_id;
                    } else {
                        key = "notify_" + UserProfileActivity.this.dialog_id;
                    }
                    ImageView checkButton = (ImageView) view.findViewById(C0419R.id.settings_row_check_button);
                    if (preferences.getBoolean(key, true)) {
                        checkButton.setImageResource(C0419R.drawable.btn_check_on);
                    } else {
                        checkButton.setImageResource(C0419R.drawable.btn_check_off);
                    }
                    textView.setText(UserProfileActivity.this.getStringEntry(C0419R.string.Notifications));
                    divider.setVisibility(0);
                }
            } else if (type == 4) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.user_profile_leftright_row_layout, viewGroup, false);
                }
                textView = (TextView) view.findViewById(C0419R.id.settings_row_text);
                detailTextView = (TextView) view.findViewById(C0419R.id.settings_row_text_detail);
                divider = view.findViewById(C0419R.id.settings_row_divider);
                if ((i == 5 && UserProfileActivity.this.dialog_id == 0) || (UserProfileActivity.this.dialog_id != 0 && ((i == 7 && (UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat)) || (i == 5 && !(UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat))))) {
                    String name = this.mContext.getSharedPreferences("Notifications", 0).getString("sound_" + UserProfileActivity.this.user_id, UserProfileActivity.this.getStringEntry(C0419R.string.Default));
                    if (name.equals("NoSound")) {
                        detailTextView.setText(UserProfileActivity.this.getStringEntry(C0419R.string.NoSound));
                    } else {
                        detailTextView.setText(name);
                    }
                    textView.setText(C0419R.string.Sound);
                    divider.setVisibility(4);
                } else if ((i == 7 && UserProfileActivity.this.dialog_id == 0) || (UserProfileActivity.this.dialog_id != 0 && ((i == 9 && (UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat)) || (i == 7 && !(UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat))))) {
                    textView.setText(C0419R.string.SharedMedia);
                    if (UserProfileActivity.this.totalMediaCount == -1) {
                        detailTextView.setText(UserProfileActivity.this.getStringEntry(C0419R.string.Loading));
                    } else {
                        detailTextView.setText(String.format("%d", new Object[]{Integer.valueOf(UserProfileActivity.this.totalMediaCount)}));
                    }
                    divider.setVisibility(4);
                } else if (i == 4 && UserProfileActivity.this.dialog_id != 0) {
                    EncryptedChat encryptedChat = (EncryptedChat) MessagesController.Instance.encryptedChats.get(Integer.valueOf((int) (UserProfileActivity.this.dialog_id >> 32)));
                    textView.setText(C0419R.string.MessageLifetime);
                    divider.setVisibility(0);
                    if (encryptedChat.ttl == 0) {
                        detailTextView.setText(UserProfileActivity.this.getStringEntry(C0419R.string.ShortMessageLifetimeForever));
                    } else if (encryptedChat.ttl == 2) {
                        detailTextView.setText(UserProfileActivity.this.getStringEntry(C0419R.string.ShortMessageLifetime2s));
                    } else if (encryptedChat.ttl == 5) {
                        detailTextView.setText(UserProfileActivity.this.getStringEntry(C0419R.string.ShortMessageLifetime5s));
                    } else if (encryptedChat.ttl == 60) {
                        detailTextView.setText(UserProfileActivity.this.getStringEntry(C0419R.string.ShortMessageLifetime1m));
                    } else if (encryptedChat.ttl == 3600) {
                        detailTextView.setText(UserProfileActivity.this.getStringEntry(C0419R.string.ShortMessageLifetime1h));
                    } else if (encryptedChat.ttl == 86400) {
                        detailTextView.setText(UserProfileActivity.this.getStringEntry(C0419R.string.ShortMessageLifetime1d));
                    } else if (encryptedChat.ttl == 604800) {
                        detailTextView.setText(UserProfileActivity.this.getStringEntry(C0419R.string.ShortMessageLifetime1w));
                    } else {
                        detailTextView.setText(String.format("%d", new Object[]{Integer.valueOf(encryptedChat.ttl)}));
                    }
                }
            } else if (type == 5) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.user_profile_identicon_layout, viewGroup, false);
                }
                textView = (TextView) view.findViewById(C0419R.id.settings_row_text);
                view.findViewById(C0419R.id.settings_row_divider).setVisibility(0);
                ((IdenticonView) view.findViewById(C0419R.id.identicon_view)).setBytes(((EncryptedChat) MessagesController.Instance.encryptedChats.get(Integer.valueOf((int) (UserProfileActivity.this.dialog_id >> 32)))).auth_key);
                textView.setText(UserProfileActivity.this.getStringEntry(C0419R.string.EncryptionKey));
            }
            return view;
        }

        public int getItemViewType(int i) {
            if (UserProfileActivity.this.dialog_id != 0) {
                if (UserProfileActivity.this.currentEncryptedChat instanceof TL_encryptedChat) {
                    if (i == 0) {
                        return 0;
                    }
                    if (i == 1 || i == 3 || i == 8) {
                        return 1;
                    }
                    if (i == 2) {
                        return 2;
                    }
                    if (i == 6) {
                        return 3;
                    }
                    if (i == 7 || i == 9 || i == 4) {
                        return 4;
                    }
                    if (i == 5) {
                        return 5;
                    }
                    return 0;
                } else if (i == 0) {
                    return 0;
                } else {
                    if (i == 1 || i == 3 || i == 6) {
                        return 1;
                    }
                    if (i == 2) {
                        return 2;
                    }
                    if (i == 4) {
                        return 3;
                    }
                    if (i == 5 || i == 7) {
                        return 4;
                    }
                    return 0;
                }
            } else if (i == 0) {
                return 0;
            } else {
                if (i == 1 || i == 3 || i == 6) {
                    return 1;
                }
                if (i == 2) {
                    return 2;
                }
                if (i == 4) {
                    return 3;
                }
                if (i == 5 || i == 7) {
                    return 4;
                }
                return 0;
            }
        }

        public int getViewTypeCount() {
            return 6;
        }

        public boolean isEmpty() {
            return false;
        }
    }

    class C08923 extends OnSwipeTouchListener {
        C08923() {
        }

        public void onSwipeRight() {
            UserProfileActivity.this.finishFragment(true);
        }
    }

    class C08935 implements RPCRequestDelegate {
        C08935() {
        }

        public void run(TLObject response, TL_error error) {
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.Instance.addObserver(this, 3);
        NotificationCenter.Instance.addObserver(this, 13);
        NotificationCenter.Instance.addObserver(this, 20);
        NotificationCenter.Instance.addObserver(this, 23);
        NotificationCenter.Instance.addObserver(this, 21);
        this.user_id = getArguments().getInt("user_id", 0);
        this.dialog_id = getArguments().getLong("dialog_id", 0);
        if (this.dialog_id != 0) {
            this.currentEncryptedChat = (EncryptedChat) MessagesController.Instance.encryptedChats.get(Integer.valueOf((int) (this.dialog_id >> 32)));
        }
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.Instance.removeObserver(this, 3);
        NotificationCenter.Instance.removeObserver(this, 13);
        NotificationCenter.Instance.removeObserver(this, 20);
        NotificationCenter.Instance.removeObserver(this, 23);
        NotificationCenter.Instance.removeObserver(this, 21);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.fragmentView == null) {
            this.fragmentView = inflater.inflate(C0419R.layout.user_profile_layout, container, false);
            this.listAdapter = new ListAdapter(this.parentActivity);
            View startSecretButton = this.fragmentView.findViewById(C0419R.id.start_secret_button);
            startSecretButton.setOnClickListener(new C05921());
            if (this.dialog_id == 0) {
                startSecretButton.setVisibility(0);
            } else {
                startSecretButton.setVisibility(8);
            }
            this.listView = (ListView) this.fragmentView.findViewById(C0419R.id.listView);
            this.listView.setAdapter(this.listAdapter);
            this.listView.setOnItemClickListener(new C05942());
            if (this.dialog_id != 0) {
                MessagesController.Instance.getMediaCount(this.dialog_id, this.classGuid, true);
            } else {
                MessagesController.Instance.getMediaCount((long) this.user_id, this.classGuid, true);
            }
            this.listView.setOnTouchListener(new C08923());
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
        if (resultCode == -1 && data != null) {
            Uri ringtone = (Uri) data.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            String name = null;
            if (ringtone != null) {
                Ringtone rng = RingtoneManager.getRingtone(ApplicationLoader.applicationContext, ringtone);
                if (rng != null) {
                    name = rng.getTitle(ApplicationLoader.applicationContext);
                    rng.stop();
                }
            }
            Editor editor = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0).edit();
            if (requestCode == 0) {
                if (name == null || ringtone == null) {
                    editor.putString("sound_" + this.user_id, "NoSound");
                    editor.putString("sound_path_" + this.user_id, "NoSound");
                } else {
                    editor.putString("sound_" + this.user_id, name);
                    editor.putString("sound_path_" + this.user_id, ringtone.toString());
                }
            }
            editor.commit();
            this.listView.invalidateViews();
        }
    }

    public void didReceivedNotification(int id, Object... args) {
        if (id == 3) {
            int mask = ((Integer) args[0]).intValue();
            if (((mask & 2) != 0 || (mask & 1) != 0) && this.listView != null) {
                this.listView.invalidateViews();
            }
        } else if (id == 13) {
            if (this.parentActivity != null) {
                this.parentActivity.supportInvalidateOptionsMenu();
            }
        } else if (id == 20) {
            long uid = ((Long) args[0]).longValue();
            if ((uid > 0 && ((long) this.user_id) == uid && this.dialog_id == 0) || (this.dialog_id != 0 && this.dialog_id == uid)) {
                this.totalMediaCount = ((Integer) args[1]).intValue();
                if (this.listView != null) {
                    this.listView.invalidateViews();
                }
            }
        } else if (id == 23) {
            if (this.creatingChat) {
                NotificationCenter.Instance.postNotificationName(5, new Object[0]);
                EncryptedChat encryptedChat = args[0];
                ChatActivity fragment = new ChatActivity();
                Bundle bundle = new Bundle();
                bundle.putInt("enc_id", encryptedChat.id);
                fragment.setArguments(bundle);
                ((ApplicationActivity) this.parentActivity).presentFragment(fragment, "chat" + Math.random(), true, false);
            }
        } else if (id == 21) {
            EncryptedChat chat = args[0];
            if (this.currentEncryptedChat != null && chat.id == this.currentEncryptedChat.id) {
                this.currentEncryptedChat = chat;
                if (this.listAdapter != null) {
                    this.listAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void applySelfActionBar() {
        if (this.parentActivity != null) {
            ActionBar actionBar = this.parentActivity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setSubtitle(null);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setCustomView(null);
            if (this.dialog_id != 0) {
                actionBar.setTitle(getStringEntry(C0419R.string.SecretTitle));
            } else {
                actionBar.setTitle(getStringEntry(C0419R.string.ContactInfo));
            }
            TextView title = (TextView) this.parentActivity.findViewById(C0419R.id.action_bar_title);
            if (title == null) {
                title = (TextView) this.parentActivity.findViewById(this.parentActivity.getResources().getIdentifier("action_bar_title", "id", "android"));
            }
            if (title == null) {
                return;
            }
            if (this.dialog_id != 0) {
                title.setCompoundDrawablesWithIntrinsicBounds(C0419R.drawable.ic_lock_white, 0, 0, 0);
                title.setCompoundDrawablePadding(Utilities.dp(4));
                return;
            }
            title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            title.setCompoundDrawablePadding(0);
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
            fixLayout();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        fixLayout();
    }

    private void fixLayout() {
        if (this.listView != null) {
            this.listView.getViewTreeObserver().addOnPreDrawListener(new C05954());
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        User user;
        Bundle args;
        switch (item.getItemId()) {
            case 16908332:
                finishFragment();
                break;
            case C0419R.id.block_contact:
                TL_contacts_block req = new TL_contacts_block();
                user = (User) MessagesController.Instance.users.get(Integer.valueOf(this.user_id));
                if ((user instanceof TL_userForeign) || (user instanceof TL_userRequest)) {
                    req.id = new TL_inputUserForeign();
                    req.id.access_hash = user.access_hash;
                    req.id.user_id = this.user_id;
                } else {
                    req.id = new TL_inputUserContact();
                    req.id.user_id = this.user_id;
                }
                TL_contactBlocked blocked = new TL_contactBlocked();
                blocked.user_id = this.user_id;
                blocked.date = (int) (System.currentTimeMillis() / 1000);
                ConnectionsManager.Instance.performRpc(req, new C08935(), null, true, RPCRequest.RPCRequestClassGeneric);
                break;
            case C0419R.id.share_contact:
                MessagesActivity fragment = new MessagesActivity();
                args = new Bundle();
                args.putBoolean("onlySelect", true);
                args.putBoolean("serverOnly", true);
                fragment.setArguments(args);
                fragment.delegate = this;
                ((ApplicationActivity) this.parentActivity).presentFragment(fragment, "chat_select", false);
                break;
            case C0419R.id.add_contact:
                user = (User) MessagesController.Instance.users.get(Integer.valueOf(this.user_id));
                ContactAddActivity fragment2 = new ContactAddActivity();
                args = new Bundle();
                args.putInt("user_id", user.id);
                fragment2.setArguments(args);
                ((ApplicationActivity) this.parentActivity).presentFragment(fragment2, "add_contact_" + user.id, false);
                break;
        }
        return true;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (MessagesController.Instance.contactsDict.get(this.user_id) == null) {
            User user = (User) MessagesController.Instance.users.get(Integer.valueOf(this.user_id));
            if (user.phone == null || user.phone.length() == 0) {
                inflater.inflate(C0419R.menu.user_profile_block_menu, menu);
                return;
            } else {
                inflater.inflate(C0419R.menu.user_profile_menu, menu);
                return;
            }
        }
        inflater.inflate(C0419R.menu.user_profile_contact_menu, menu);
    }

    public void didSelectDialog(MessagesActivity messageFragment, long dialog_id) {
        if (dialog_id != 0) {
            ChatActivity fragment = new ChatActivity();
            Bundle bundle = new Bundle();
            int lower_part = (int) dialog_id;
            if (lower_part == 0) {
                NotificationCenter.Instance.postNotificationName(5, new Object[0]);
                bundle.putInt("enc_id", (int) (dialog_id >> 32));
                fragment.setArguments(bundle);
                fragment.scrollToTopOnResume = true;
                ((ApplicationActivity) this.parentActivity).presentFragment(fragment, "chat" + Math.random(), false);
                messageFragment.removeSelfFromStack();
                removeSelfFromStack();
            } else if (lower_part > 0) {
                NotificationCenter.Instance.postNotificationName(5, new Object[0]);
                bundle.putInt("user_id", lower_part);
                fragment.setArguments(bundle);
                fragment.scrollToTopOnResume = true;
                ((ApplicationActivity) this.parentActivity).presentFragment(fragment, "chat" + Math.random(), true, false);
                removeSelfFromStack();
                messageFragment.removeSelfFromStack();
            } else if (lower_part < 0) {
                NotificationCenter.Instance.postNotificationName(5, new Object[0]);
                bundle.putInt("chat_id", -lower_part);
                fragment.setArguments(bundle);
                fragment.scrollToTopOnResume = true;
                ((ApplicationActivity) this.parentActivity).presentFragment(fragment, "chat" + Math.random(), true, false);
                messageFragment.removeSelfFromStack();
                removeSelfFromStack();
            }
            MessagesController.Instance.sendMessage((User) MessagesController.Instance.users.get(Integer.valueOf(this.user_id)), dialog_id);
        }
    }
}
