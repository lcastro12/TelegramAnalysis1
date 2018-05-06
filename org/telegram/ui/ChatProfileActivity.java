package org.telegram.ui;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.System;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import org.telegram.TL.TLRPC.Chat;
import org.telegram.TL.TLRPC.ChatParticipants;
import org.telegram.TL.TLRPC.FileLocation;
import org.telegram.TL.TLRPC.InputFile;
import org.telegram.TL.TLRPC.PhotoSize;
import org.telegram.TL.TLRPC.TL_chatParticipant;
import org.telegram.TL.TLRPC.TL_chatParticipantsForbidden;
import org.telegram.TL.TLRPC.TL_chatPhotoEmpty;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Cells.ChatOrUserCell;
import org.telegram.ui.ContactsActivity.ContactsActivityDelegate;
import org.telegram.ui.Views.AvatarUpdater;
import org.telegram.ui.Views.AvatarUpdater.AvatarUpdaterDelegate;
import org.telegram.ui.Views.BackupImageView;
import org.telegram.ui.Views.BaseFragment;
import org.telegram.ui.Views.OnSwipeTouchListener;

public class ChatProfileActivity extends BaseFragment implements NotificationCenterDelegate, ContactsActivityDelegate {
    private AvatarUpdater avatarUpdater = new AvatarUpdater();
    private int chat_id;
    private ChatParticipants info;
    private ListView listView;
    private ListAdapter listViewAdapter;
    private int onlineCount = -1;
    private String selectedPhone;
    private TL_chatParticipant selectedUser;
    private ArrayList<Integer> sortedUsers = new ArrayList();
    private int totalMediaCount = -1;

    class C04642 implements OnItemLongClickListener {

        class C04631 implements OnClickListener {
            C04631() {
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    ChatProfileActivity.this.kickUser(ChatProfileActivity.this.selectedUser);
                }
            }
        }

        C04642() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            int size = 0;
            if (ChatProfileActivity.this.info != null) {
                size = 0 + ChatProfileActivity.this.info.participants.size();
            }
            if (i <= 6 || i >= size + 7) {
                return false;
            }
            TL_chatParticipant user = (TL_chatParticipant) ChatProfileActivity.this.info.participants.get(((Integer) ChatProfileActivity.this.sortedUsers.get(i - 7)).intValue());
            if (user.user_id == UserConfig.clientUserId) {
                return false;
            }
            if (ChatProfileActivity.this.info.admin_id != UserConfig.clientUserId && user.inviter_id != UserConfig.clientUserId) {
                return false;
            }
            ChatProfileActivity.this.selectedUser = user;
            Builder builder = new Builder(ChatProfileActivity.this.parentActivity);
            builder.setItems(new CharSequence[]{ChatProfileActivity.this.getStringEntry(C0419R.string.KickFromGroup)}, new C04631());
            builder.show().setCanceledOnTouchOutside(true);
            return true;
        }
    }

    class C04653 implements OnItemClickListener {
        C04653() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            SharedPreferences preferences;
            if (i == 2) {
                preferences = ChatProfileActivity.this.parentActivity.getSharedPreferences("Notifications", 0);
                String key = "notify_" + (-ChatProfileActivity.this.chat_id);
                boolean value = preferences.getBoolean(key, true);
                Editor editor = preferences.edit();
                editor.putBoolean(key, !value);
                editor.commit();
                ChatProfileActivity.this.listView.invalidateViews();
            } else if (i == 3) {
                try {
                    Intent tmpIntent = new Intent("android.intent.action.RINGTONE_PICKER");
                    tmpIntent.putExtra("android.intent.extra.ringtone.TYPE", 2);
                    tmpIntent.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", false);
                    preferences = ChatProfileActivity.this.parentActivity.getSharedPreferences("Notifications", 0);
                    Uri currentSound = null;
                    String defaultPath = null;
                    Uri defaultUri = System.DEFAULT_NOTIFICATION_URI;
                    if (defaultUri != null) {
                        defaultPath = defaultUri.getPath();
                    }
                    String path = preferences.getString("sound_chat_path_" + ChatProfileActivity.this.chat_id, defaultPath);
                    if (!(path == null || path.equals("NoSound"))) {
                        currentSound = path.equals(defaultPath) ? defaultUri : Uri.parse(path);
                    }
                    tmpIntent.putExtra("android.intent.extra.ringtone.EXISTING_URI", currentSound);
                    ChatProfileActivity.this.startActivityForResult(tmpIntent, 15);
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            } else if (i == 5) {
                MediaActivity fragment = new MediaActivity();
                Bundle bundle = new Bundle();
                bundle.putLong("dialog_id", (long) (-ChatProfileActivity.this.chat_id));
                fragment.setArguments(bundle);
                ((ApplicationActivity) ChatProfileActivity.this.parentActivity).presentFragment(fragment, "media_chat_" + ChatProfileActivity.this.chat_id, false);
            } else {
                int size = 0;
                if (ChatProfileActivity.this.info != null) {
                    size = 0 + ChatProfileActivity.this.info.participants.size();
                }
                if (i > 6 && i < size + 7) {
                    int user_id = ((TL_chatParticipant) ChatProfileActivity.this.info.participants.get(((Integer) ChatProfileActivity.this.sortedUsers.get(i - 7)).intValue())).user_id;
                    if (user_id != UserConfig.clientUserId) {
                        UserProfileActivity fragment2 = new UserProfileActivity();
                        Bundle args = new Bundle();
                        args.putInt("user_id", user_id);
                        fragment2.setArguments(args);
                        ((ApplicationActivity) ChatProfileActivity.this.parentActivity).presentFragment(fragment2, "user_" + user_id, false);
                    }
                } else if (size + 7 == i) {
                    if (ChatProfileActivity.this.info.participants.size() < 200) {
                        ChatProfileActivity.this.openAddMenu();
                    } else {
                        ChatProfileActivity.this.kickUser(null);
                    }
                } else if (size + 7 == i + 1) {
                    ChatProfileActivity.this.kickUser(null);
                }
            }
        }
    }

    class C04665 implements Comparator<Integer> {
        C04665() {
        }

        public int compare(Integer lhs, Integer rhs) {
            User user1 = (User) MessagesController.Instance.users.get(Integer.valueOf(((TL_chatParticipant) ChatProfileActivity.this.info.participants.get(rhs.intValue())).user_id));
            User user2 = (User) MessagesController.Instance.users.get(Integer.valueOf(((TL_chatParticipant) ChatProfileActivity.this.info.participants.get(lhs.intValue())).user_id));
            Integer status1 = Integer.valueOf(0);
            Integer status2 = Integer.valueOf(0);
            if (!(user1 == null || user1.status == null)) {
                if (user1.id == UserConfig.clientUserId) {
                    status1 = Integer.valueOf(ConnectionsManager.Instance.getCurrentTime() + 50000);
                } else {
                    status1 = Integer.valueOf(user1.status.expires);
                    if (status1.intValue() == 0) {
                        status1 = Integer.valueOf(user1.status.was_online);
                    }
                }
            }
            if (!(user2 == null || user2.status == null)) {
                if (user2.id == UserConfig.clientUserId) {
                    status2 = Integer.valueOf(ConnectionsManager.Instance.getCurrentTime() + 50000);
                } else {
                    status2 = Integer.valueOf(user2.status.expires);
                    if (status2.intValue() == 0) {
                        status2 = Integer.valueOf(user2.status.was_online);
                    }
                }
            }
            return status1.compareTo(status2);
        }
    }

    class C04676 implements View.OnClickListener {
        C04676() {
        }

        public void onClick(View view) {
            ChatProfileActivity.this.openAddMenu();
        }
    }

    private class ListAdapter extends BaseAdapter {
        private Context mContext;

        class C04681 implements View.OnClickListener {
            C04681() {
            }

            public void onClick(View view) {
                ChatProfileChangeNameActivity fragment = new ChatProfileChangeNameActivity();
                Bundle bundle = new Bundle();
                bundle.putInt("chat_id", ChatProfileActivity.this.chat_id);
                fragment.setArguments(bundle);
                ((ApplicationActivity) ChatProfileActivity.this.parentActivity).presentFragment(fragment, "chat_name_" + ChatProfileActivity.this.chat_id, false);
            }
        }

        class C04702 implements View.OnClickListener {
            C04702() {
            }

            public void onClick(View view) {
                CharSequence[] items;
                int type;
                Builder builder = new Builder(ChatProfileActivity.this.parentActivity);
                Chat chat = (Chat) MessagesController.Instance.chats.get(Integer.valueOf(ChatProfileActivity.this.chat_id));
                if (chat.photo == null || chat.photo.photo_big == null || (chat.photo instanceof TL_chatPhotoEmpty)) {
                    items = new CharSequence[]{ChatProfileActivity.this.getStringEntry(C0419R.string.FromCamera), ChatProfileActivity.this.getStringEntry(C0419R.string.FromGalley)};
                    type = 0;
                } else {
                    items = new CharSequence[]{ChatProfileActivity.this.getStringEntry(C0419R.string.OpenPhoto), ChatProfileActivity.this.getStringEntry(C0419R.string.FromCamera), ChatProfileActivity.this.getStringEntry(C0419R.string.FromGalley), ChatProfileActivity.this.getStringEntry(C0419R.string.DeletePhoto)};
                    type = 1;
                }
                final int arg0 = type;
                builder.setItems(items, new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int action = 0;
                        if (arg0 == 1) {
                            if (i == 0) {
                                action = 0;
                            } else if (i == 1) {
                                action = 1;
                            } else if (i == 2) {
                                action = 2;
                            } else if (i == 3) {
                                action = 3;
                            }
                        } else if (arg0 == 0) {
                            if (i == 0) {
                                action = 1;
                            } else if (i == 1) {
                                action = 2;
                            }
                        }
                        ChatProfileActivity.this.processPhotoMenu(action);
                    }
                });
                builder.show().setCanceledOnTouchOutside(true);
            }
        }

        class C04723 implements View.OnClickListener {

            class C04711 implements OnClickListener {
                C04711() {
                }

                public void onClick(DialogInterface dialogInterface, int i) {
                    ChatProfileActivity.this.kickUser(null);
                }
            }

            C04723() {
            }

            public void onClick(View view) {
                Builder builder = new Builder(ChatProfileActivity.this.parentActivity);
                builder.setMessage(ChatProfileActivity.this.getStringEntry(C0419R.string.AreYouSure));
                builder.setTitle(ChatProfileActivity.this.getStringEntry(C0419R.string.AppName));
                builder.setPositiveButton(ChatProfileActivity.this.getStringEntry(C0419R.string.OK), new C04711());
                builder.setNegativeButton(ChatProfileActivity.this.getStringEntry(C0419R.string.Cancel), null);
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
            return (i == 2 || i == 3 || i == 5 || i > 6) && i != getCount() - 1;
        }

        public int getCount() {
            if (ChatProfileActivity.this.info == null || (ChatProfileActivity.this.info instanceof TL_chatParticipantsForbidden)) {
                return 6;
            }
            int count = 6 + (ChatProfileActivity.this.info.participants.size() + 2);
            if (ChatProfileActivity.this.info.participants.size() < 200) {
                return count + 1;
            }
            return count;
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
            if (type == 0) {
                TextView onlineText;
                Chat chat = (Chat) MessagesController.Instance.chats.get(Integer.valueOf(ChatProfileActivity.this.chat_id));
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.chat_profile_avatar_layout, viewGroup, false);
                    onlineText = (TextView) view.findViewById(C0419R.id.settings_online);
                    ((ImageButton) view.findViewById(C0419R.id.settings_edit_name)).setOnClickListener(new C04681());
                    ((ImageButton) view.findViewById(C0419R.id.settings_change_avatar_button)).setOnClickListener(new C04702());
                } else {
                    onlineText = (TextView) view.findViewById(C0419R.id.settings_online);
                }
                BackupImageView avatarImage = (BackupImageView) view.findViewById(C0419R.id.settings_avatar_image);
                TextView textView2 = (TextView) view.findViewById(C0419R.id.settings_name);
                textView2.setTypeface(Utilities.getTypeface("fonts/rmedium.ttf"));
                textView2.setText(chat.title);
                if (chat.participants_count == 0 || ChatProfileActivity.this.onlineCount <= 0) {
                    textView = onlineText;
                    textView.setText(String.format("%d %s", new Object[]{Integer.valueOf(chat.participants_count), ChatProfileActivity.this.getStringEntry(C0419R.string.Members)}));
                } else {
                    textView = onlineText;
                    textView.setText(Html.fromHtml(String.format("%d %s, <font color='#357aa8'>%d %s</font>", new Object[]{Integer.valueOf(chat.participants_count), ChatProfileActivity.this.getStringEntry(C0419R.string.Members), Integer.valueOf(ChatProfileActivity.this.onlineCount), ChatProfileActivity.this.getStringEntry(C0419R.string.Online)})));
                }
                FileLocation photo = null;
                if (chat.photo != null) {
                    photo = chat.photo.photo_small;
                }
                avatarImage.setImage(photo, "50_50", Utilities.getGroupAvatarForId(chat.id));
                return view;
            }
            if (type == 1) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_section_layout, viewGroup, false);
                }
                textView2 = (TextView) view.findViewById(C0419R.id.settings_section_text);
                if (i == 1) {
                    textView2.setText(ChatProfileActivity.this.getStringEntry(C0419R.string.SETTINGS));
                } else if (i == 4) {
                    textView2.setText(ChatProfileActivity.this.getStringEntry(C0419R.string.SHAREDMEDIA));
                } else if (i == 6) {
                    textView = textView2;
                    textView.setText(String.format("%d %s", new Object[]{Integer.valueOf(((Chat) MessagesController.Instance.chats.get(Integer.valueOf(ChatProfileActivity.this.chat_id))).participants_count), ChatProfileActivity.this.getStringEntry(C0419R.string.MEMBERS)}));
                }
            } else if (type == 2) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_row_check_layout, viewGroup, false);
                }
                textView2 = (TextView) view.findViewById(C0419R.id.settings_row_text);
                divider = view.findViewById(C0419R.id.settings_row_divider);
                if (i == 2) {
                    ImageView checkButton = (ImageView) view.findViewById(C0419R.id.settings_row_check_button);
                    if (this.mContext.getSharedPreferences("Notifications", 0).getBoolean("notify_" + (-ChatProfileActivity.this.chat_id), true)) {
                        checkButton.setImageResource(C0419R.drawable.btn_check_on);
                    } else {
                        checkButton.setImageResource(C0419R.drawable.btn_check_off);
                    }
                    textView2.setText(ChatProfileActivity.this.getStringEntry(C0419R.string.Notifications));
                    divider.setVisibility(0);
                }
            } else if (type == 3) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.user_profile_leftright_row_layout, viewGroup, false);
                }
                textView2 = (TextView) view.findViewById(C0419R.id.settings_row_text);
                TextView detailTextView = (TextView) view.findViewById(C0419R.id.settings_row_text_detail);
                divider = view.findViewById(C0419R.id.settings_row_divider);
                if (i == 3) {
                    String name = this.mContext.getSharedPreferences("Notifications", 0).getString("sound_chat_" + ChatProfileActivity.this.chat_id, ChatProfileActivity.this.getStringEntry(C0419R.string.Default));
                    if (name.equals("NoSound")) {
                        detailTextView.setText(ChatProfileActivity.this.getStringEntry(C0419R.string.NoSound));
                    } else {
                        detailTextView.setText(name);
                    }
                    textView2.setText(C0419R.string.Sound);
                    divider.setVisibility(4);
                } else if (i == 5) {
                    textView2.setText(C0419R.string.SharedMedia);
                    if (ChatProfileActivity.this.totalMediaCount == -1) {
                        detailTextView.setText(ChatProfileActivity.this.getStringEntry(C0419R.string.Loading));
                    } else {
                        detailTextView.setText(String.format("%d", new Object[]{Integer.valueOf(ChatProfileActivity.this.totalMediaCount)}));
                    }
                    divider.setVisibility(4);
                }
            } else if (type == 4) {
                User user = (User) MessagesController.Instance.users.get(Integer.valueOf(((TL_chatParticipant) ChatProfileActivity.this.info.participants.get(((Integer) ChatProfileActivity.this.sortedUsers.get(i - 7)).intValue())).user_id));
                if (view == null) {
                    View chatOrUserCell = new ChatOrUserCell(this.mContext);
                    ((ChatOrUserCell) chatOrUserCell).useBoldFont = true;
                    ((ChatOrUserCell) chatOrUserCell).usePadding = false;
                    ((ChatOrUserCell) chatOrUserCell).useSeparator = true;
                }
                ((ChatOrUserCell) view).setData(user, null, null, null, null);
            } else if (type == 5) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.chat_profile_add_row, viewGroup, false);
                }
            } else if (type == 6 && view == null && view == null) {
                view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_logout_button, viewGroup, false);
                textView2 = (TextView) view.findViewById(C0419R.id.settings_row_text);
                textView2.setText(ChatProfileActivity.this.getStringEntry(C0419R.string.DeleteAndExit));
                textView2.setOnClickListener(new C04723());
            }
            return view;
        }

        public int getItemViewType(int i) {
            if (i == 0) {
                return 0;
            }
            if (i == 1 || i == 4 || i == 6) {
                return 1;
            }
            if (i == 2) {
                return 2;
            }
            if (i == 3 || i == 5) {
                return 3;
            }
            if (i <= 6) {
                return 0;
            }
            int size = 0;
            if (ChatProfileActivity.this.info != null) {
                size = 0 + ChatProfileActivity.this.info.participants.size();
            }
            if (i > 6 && i < size + 7) {
                return 4;
            }
            if (size + 7 == i) {
                return (ChatProfileActivity.this.info == null || ChatProfileActivity.this.info.participants.size() >= 200) ? 6 : 5;
            } else {
                if (size + 8 == i) {
                    return 6;
                }
                return 0;
            }
        }

        public int getViewTypeCount() {
            return 7;
        }

        public boolean isEmpty() {
            return false;
        }
    }

    class C08571 implements AvatarUpdaterDelegate {
        C08571() {
        }

        public void didUploadedPhoto(InputFile file, PhotoSize small, PhotoSize big) {
            if (ChatProfileActivity.this.chat_id != 0) {
                MessagesController.Instance.changeChatAvatar(ChatProfileActivity.this.chat_id, file);
            }
        }
    }

    class C08584 extends OnSwipeTouchListener {
        C08584() {
        }

        public void onSwipeRight() {
            ChatProfileActivity.this.finishFragment(true);
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.Instance.addObserver(this, 3);
        NotificationCenter.Instance.addObserver(this, 17);
        NotificationCenter.Instance.addObserver(this, 20);
        NotificationCenter.Instance.addObserver(this, 5);
        this.chat_id = getArguments().getInt("chat_id", 0);
        this.info = (ChatParticipants) NotificationCenter.Instance.getFromMemCache(5);
        updateOnlineCount();
        MessagesController.Instance.getMediaCount((long) (-this.chat_id), this.classGuid, true);
        this.avatarUpdater.delegate = new C08571();
        this.avatarUpdater.parentFragment = this;
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.Instance.removeObserver(this, 3);
        NotificationCenter.Instance.removeObserver(this, 17);
        NotificationCenter.Instance.removeObserver(this, 20);
        NotificationCenter.Instance.removeObserver(this, 5);
        this.avatarUpdater.clear();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.fragmentView == null) {
            this.fragmentView = inflater.inflate(C0419R.layout.chat_profile_layout, container, false);
            this.listView = (ListView) this.fragmentView.findViewById(C0419R.id.listView);
            ListView listView = this.listView;
            android.widget.ListAdapter listAdapter = new ListAdapter(this.parentActivity);
            this.listViewAdapter = listAdapter;
            listView.setAdapter(listAdapter);
            this.listView.setOnItemLongClickListener(new C04642());
            this.listView.setOnItemClickListener(new C04653());
            this.listView.setOnTouchListener(new C08584());
        } else {
            ViewGroup parent = (ViewGroup) this.fragmentView.getParent();
            if (parent != null) {
                parent.removeView(this.fragmentView);
            }
        }
        return this.fragmentView;
    }

    public void didSelectContact(int user_id) {
        MessagesController.Instance.addUserToChat(this.chat_id, user_id, this.info);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.avatarUpdater.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 15) {
            Uri ringtone = (Uri) data.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            String name = null;
            if (!(ringtone == null || this.parentActivity == null)) {
                Ringtone rng = RingtoneManager.getRingtone(this.parentActivity, ringtone);
                if (rng != null) {
                    name = rng.getTitle(this.parentActivity);
                    rng.stop();
                }
            }
            Editor editor = this.parentActivity.getSharedPreferences("Notifications", 0).edit();
            if (name == null || ringtone == null) {
                editor.putString("sound_chat_" + this.chat_id, "NoSound");
                editor.putString("sound_chat_path_" + this.chat_id, "NoSound");
            } else {
                editor.putString("sound_chat_" + this.chat_id, name);
                editor.putString("sound_chat_path_" + this.chat_id, ringtone.toString());
            }
            editor.commit();
            this.listView.invalidateViews();
        }
    }

    public void didReceivedNotification(int id, Object... args) {
        if (id == 3) {
            int mask = ((Integer) args[0]).intValue();
            if (!((mask & 8) == 0 && (mask & 16) == 0 && (mask & 32) == 0)) {
                updateOnlineCount();
            }
            if ((mask & 2) != 0 || (mask & 1) != 0 || (mask & 4) != 0) {
                updateVisibleRows(mask);
            }
        } else if (id == 17) {
            if (((Integer) args[0]).intValue() == this.chat_id) {
                this.info = (ChatParticipants) args[1];
                updateOnlineCount();
                if (this.listViewAdapter != null) {
                    this.listViewAdapter.notifyDataSetChanged();
                }
            }
        } else if (id == 20) {
            int lower_part = (int) ((Long) args[0]).longValue();
            if (lower_part < 0 && this.chat_id == (-lower_part)) {
                this.totalMediaCount = ((Integer) args[1]).intValue();
                if (this.listView != null) {
                    this.listView.invalidateViews();
                }
            }
        } else if (id == 5) {
            removeSelfFromStack();
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
            actionBar.setCustomView(null);
            actionBar.setTitle(getStringEntry(C0419R.string.GroupInfo));
            actionBar.setSubtitle(null);
            TextView title = (TextView) this.parentActivity.findViewById(C0419R.id.action_bar_title);
            if (title == null) {
                title = (TextView) this.parentActivity.findViewById(this.parentActivity.getResources().getIdentifier("action_bar_title", "id", "android"));
            }
            if (title != null) {
                title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                title.setCompoundDrawablePadding(0);
            }
            ((ApplicationActivity) this.parentActivity).fixBackButton();
        }
    }

    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            if (this.listViewAdapter != null) {
                this.listViewAdapter.notifyDataSetChanged();
            }
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

    private void updateVisibleRows(int mask) {
        if (this.listView != null) {
            int count = this.listView.getChildCount();
            for (int a = 0; a < count; a++) {
                View child = this.listView.getChildAt(a);
                if (child instanceof ChatOrUserCell) {
                    ((ChatOrUserCell) child).update(mask);
                }
            }
        }
    }

    private void updateOnlineCount() {
        if (this.info != null) {
            this.onlineCount = 0;
            int currentTime = ConnectionsManager.Instance.getCurrentTime();
            this.sortedUsers.clear();
            int i = 0;
            Iterator i$ = this.info.participants.iterator();
            while (i$.hasNext()) {
                User user = (User) MessagesController.Instance.users.get(Integer.valueOf(((TL_chatParticipant) i$.next()).user_id));
                if (!(user == null || user.status == null || ((user.status.expires <= currentTime && user.status.was_online <= currentTime && user.id != UserConfig.clientUserId) || (user.status.expires <= FileLoader.FileDidUpload && user.status.was_online <= FileLoader.FileDidUpload)))) {
                    this.onlineCount++;
                }
                this.sortedUsers.add(Integer.valueOf(i));
                i++;
            }
            Collections.sort(this.sortedUsers, new C04665());
            if (this.listView != null) {
                this.listView.invalidateViews();
            }
        }
    }

    private void processPhotoMenu(int action) {
        if (action == 0) {
            if (this.parentActivity != null) {
                Chat chat = (Chat) MessagesController.Instance.chats.get(Integer.valueOf(this.chat_id));
                if (chat.photo != null && chat.photo.photo_big != null) {
                    NotificationCenter.Instance.addToMemCache(53, chat.photo.photo_big);
                    startActivity(new Intent(this.parentActivity, GalleryImageViewer.class));
                }
            }
        } else if (action == 1) {
            this.avatarUpdater.openCamera();
        } else if (action == 2) {
            this.avatarUpdater.openGallery();
        } else if (action == 3) {
            MessagesController.Instance.changeChatAvatar(this.chat_id, null);
        }
    }

    private void openAddMenu() {
        ContactsActivity fragment = new ContactsActivity();
        fragment.animationType = 1;
        Bundle bundle = new Bundle();
        bundle.putBoolean("onlyUsers", true);
        bundle.putBoolean("destroyAfterSelect", true);
        bundle.putBoolean("usersAsSections", true);
        bundle.putBoolean("returnAsResult", true);
        fragment.selectAlertString = C0419R.string.AddToTheGroup;
        fragment.delegate = this;
        if (this.info != null) {
            Object users = new HashMap();
            Iterator i$ = this.info.participants.iterator();
            while (i$.hasNext()) {
                users.put(Integer.valueOf(((TL_chatParticipant) i$.next()).user_id), null);
            }
            NotificationCenter.Instance.addToMemCache(7, users);
        }
        fragment.setArguments(bundle);
        ((ApplicationActivity) this.parentActivity).presentFragment(fragment, "contacts_block", false);
    }

    private void kickUser(TL_chatParticipant user) {
        if (user != null) {
            MessagesController.Instance.deleteUserFromChat(this.chat_id, user.user_id, this.info);
            return;
        }
        NotificationCenter.Instance.removeObserver(this, 5);
        NotificationCenter.Instance.postNotificationName(5, new Object[0]);
        MessagesController.Instance.deleteUserFromChat(this.chat_id, UserConfig.clientUserId, this.info);
        MessagesController.Instance.deleteDialog((long) (-this.chat_id), 0, false);
        finishFragment();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(C0419R.menu.group_profile_menu, menu);
        ((TextView) ((SupportMenuItem) menu.findItem(C0419R.id.block_user)).getActionView().findViewById(C0419R.id.done_button)).setOnClickListener(new C04676());
    }
}
