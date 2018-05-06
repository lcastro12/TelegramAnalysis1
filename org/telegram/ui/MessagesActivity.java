package org.telegram.ui;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.Chat;
import org.telegram.TL.TLRPC.EncryptedChat;
import org.telegram.TL.TLRPC.TL_dialog;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Cells.ChatOrUserCell;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Views.BaseFragment;

public class MessagesActivity extends BaseFragment implements NotificationCenterDelegate {
    private static boolean dialogsLoaded = false;
    private int activityToken = ((int) (MessagesController.random.nextDouble() * 2.147483647E9d));
    public MessagesActivityDelegate delegate;
    private View empryView;
    private ListView messagesListView;
    private MessagesAdapter messagesListViewAdapter;
    private boolean onlySelect = false;
    private View progressView;
    private Timer searchDialogsTimer;
    private TextView searchEmptyView;
    private SupportMenuItem searchItem;
    public ArrayList<TLObject> searchResult;
    public ArrayList<CharSequence> searchResultNames;
    private SearchView searchView;
    private boolean searchWas = false;
    private boolean searching = false;
    public int selectAlertString = 0;
    private long selectedDialog;
    private boolean serverOnly = false;

    class C05541 implements OnItemClickListener {
        C05541() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            long dialog_id = 0;
            if (MessagesActivity.this.searching && MessagesActivity.this.searchWas) {
                if (i < MessagesActivity.this.searchResult.size()) {
                    TLObject obj = (TLObject) MessagesActivity.this.searchResult.get(i);
                    if (obj instanceof User) {
                        dialog_id = (long) ((User) obj).id;
                    } else if (obj instanceof Chat) {
                        dialog_id = (long) (-((Chat) obj).id);
                    } else if (obj instanceof EncryptedChat) {
                        dialog_id = ((long) ((EncryptedChat) obj).id) << 32;
                    }
                } else {
                    return;
                }
            } else if (MessagesActivity.this.serverOnly) {
                if (i < MessagesController.Instance.dialogsServerOnly.size()) {
                    dialog_id = ((TL_dialog) MessagesController.Instance.dialogsServerOnly.get(i)).id;
                } else {
                    return;
                }
            } else if (i < MessagesController.Instance.dialogs.size()) {
                dialog_id = ((TL_dialog) MessagesController.Instance.dialogs.get(i)).id;
            } else {
                return;
            }
            if (MessagesActivity.this.onlySelect) {
                MessagesActivity.this.didSelectResult(dialog_id, true);
                return;
            }
            ChatActivity fragment = new ChatActivity();
            Bundle bundle = new Bundle();
            int lower_part = (int) dialog_id;
            if (lower_part == 0) {
                bundle.putInt("enc_id", (int) (dialog_id >> 32));
                fragment.setArguments(bundle);
                ((ApplicationActivity) MessagesActivity.this.parentActivity).presentFragment(fragment, "chat" + Math.random(), false);
            } else if (lower_part > 0) {
                bundle.putInt("user_id", lower_part);
                fragment.setArguments(bundle);
                ((ApplicationActivity) MessagesActivity.this.parentActivity).presentFragment(fragment, "chat" + Math.random(), false);
            } else if (lower_part < 0) {
                bundle.putInt("chat_id", -lower_part);
                fragment.setArguments(bundle);
                ((ApplicationActivity) MessagesActivity.this.parentActivity).presentFragment(fragment, "chat" + Math.random(), false);
            }
        }
    }

    class C05572 implements OnItemLongClickListener {

        class C05551 implements OnClickListener {
            C05551() {
            }

            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    MessagesController.Instance.deleteDialog(MessagesActivity.this.selectedDialog, 0, true);
                } else if (which == 1) {
                    MessagesController.Instance.deleteUserFromChat((int) (-MessagesActivity.this.selectedDialog), UserConfig.clientUserId, null);
                    MessagesController.Instance.deleteDialog(MessagesActivity.this.selectedDialog, 0, false);
                }
            }
        }

        class C05562 implements OnClickListener {
            C05562() {
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                MessagesController.Instance.deleteDialog(MessagesActivity.this.selectedDialog, 0, false);
            }
        }

        C05572() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (MessagesActivity.this.onlySelect) {
                return false;
            }
            TL_dialog dialog;
            if (MessagesActivity.this.serverOnly) {
                if (i >= MessagesController.Instance.dialogsServerOnly.size()) {
                    return false;
                }
                dialog = (TL_dialog) MessagesController.Instance.dialogsServerOnly.get(i);
            } else if (i >= MessagesController.Instance.dialogs.size()) {
                return false;
            } else {
                dialog = (TL_dialog) MessagesController.Instance.dialogs.get(i);
            }
            MessagesActivity.this.selectedDialog = dialog.id;
            Builder builder = new Builder(MessagesActivity.this.parentActivity);
            builder.setTitle(MessagesActivity.this.getStringEntry(C0419R.string.AppName));
            if (((int) MessagesActivity.this.selectedDialog) < 0) {
                builder.setItems(new CharSequence[]{MessagesActivity.this.getStringEntry(C0419R.string.ClearHistory), MessagesActivity.this.getStringEntry(C0419R.string.DeleteChat)}, new C05551());
            } else {
                builder.setMessage(MessagesActivity.this.getStringEntry(C0419R.string.DeleteChatQuestion));
                builder.setPositiveButton(MessagesActivity.this.getStringEntry(C0419R.string.Delete), new C05562());
            }
            builder.setNegativeButton(MessagesActivity.this.getStringEntry(C0419R.string.Cancel), null);
            builder.show().setCanceledOnTouchOutside(true);
            return true;
        }
    }

    class C05583 implements OnScrollListener {
        C05583() {
        }

        public void onScrollStateChanged(AbsListView absListView, int i) {
        }

        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if ((MessagesActivity.this.searching && MessagesActivity.this.searchWas) || visibleItemCount <= 0) {
                return;
            }
            if ((absListView.getLastVisiblePosition() == MessagesController.Instance.dialogs.size() && !MessagesActivity.this.serverOnly) || (absListView.getLastVisiblePosition() == MessagesController.Instance.dialogsServerOnly.size() && MessagesActivity.this.serverOnly)) {
                MessagesController.Instance.loadDialogs(MessagesController.Instance.dialogs.size(), MessagesController.Instance.dialogsServerOnly.size(), 100, true);
            }
        }
    }

    public interface MessagesActivityDelegate {
        void didSelectDialog(MessagesActivity messagesActivity, long j);
    }

    private class MessagesAdapter extends BaseAdapter {
        private Context mContext;

        public MessagesAdapter(Context context) {
            this.mContext = context;
        }

        public boolean areAllItemsEnabled() {
            return true;
        }

        public boolean isEnabled(int i) {
            return true;
        }

        public int getCount() {
            if (!MessagesActivity.this.searching || !MessagesActivity.this.searchWas) {
                int count;
                if (MessagesActivity.this.serverOnly) {
                    count = MessagesController.Instance.dialogsServerOnly.size();
                } else {
                    count = MessagesController.Instance.dialogs.size();
                }
                if (count == 0 && MessagesController.Instance.loadingDialogs) {
                    return 0;
                }
                if (MessagesController.Instance.dialogsEndReached) {
                    return count;
                }
                return count + 1;
            } else if (MessagesActivity.this.searchResult == null) {
                return 0;
            } else {
                return MessagesActivity.this.searchResult.size();
            }
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
            if (MessagesActivity.this.searching && MessagesActivity.this.searchWas) {
                if (view == null) {
                    view = new ChatOrUserCell(this.mContext);
                }
                User user = null;
                Chat chat = null;
                EncryptedChat encryptedChat = null;
                TLObject obj = (TLObject) MessagesActivity.this.searchResult.get(i);
                if (obj instanceof User) {
                    user = (User) MessagesController.Instance.users.get(Integer.valueOf(((User) obj).id));
                } else if (obj instanceof Chat) {
                    chat = (Chat) MessagesController.Instance.chats.get(Integer.valueOf(((Chat) obj).id));
                } else if (obj instanceof EncryptedChat) {
                    encryptedChat = (EncryptedChat) MessagesController.Instance.encryptedChats.get(Integer.valueOf(((EncryptedChat) obj).id));
                    user = (User) MessagesController.Instance.users.get(Integer.valueOf(encryptedChat.user_id));
                }
                ((ChatOrUserCell) view).setData(user, chat, encryptedChat, (CharSequence) MessagesActivity.this.searchResultNames.get(i), null);
                return view;
            } else if (getItemViewType(i) == 1) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.loading_more_layout, viewGroup, false);
                }
                return view;
            } else {
                if (view == null) {
                    view = new DialogCell(this.mContext);
                }
                if (MessagesActivity.this.serverOnly) {
                    ((DialogCell) view).setDialog((TL_dialog) MessagesController.Instance.dialogsServerOnly.get(i));
                } else {
                    ((DialogCell) view).setDialog((TL_dialog) MessagesController.Instance.dialogs.get(i));
                }
                return view;
            }
        }

        public int getItemViewType(int i) {
            if (MessagesActivity.this.searching && MessagesActivity.this.searchWas) {
                TLObject obj = (TLObject) MessagesActivity.this.searchResult.get(i);
                if ((obj instanceof User) || (obj instanceof EncryptedChat)) {
                    return 2;
                }
                return 3;
            } else if ((!MessagesActivity.this.serverOnly || i != MessagesController.Instance.dialogsServerOnly.size()) && (MessagesActivity.this.serverOnly || i != MessagesController.Instance.dialogs.size())) {
                return 0;
            } else {
                return 1;
            }
        }

        public int getViewTypeCount() {
            return 4;
        }

        public boolean isEmpty() {
            boolean z = true;
            if (MessagesActivity.this.searching && MessagesActivity.this.searchWas) {
                if (MessagesActivity.this.searchResult == null || MessagesActivity.this.searchResult.size() == 0) {
                    return true;
                }
                return false;
            } else if (MessagesController.Instance.loadingDialogs && MessagesController.Instance.dialogs.isEmpty()) {
                return false;
            } else {
                int count;
                if (MessagesActivity.this.serverOnly) {
                    count = MessagesController.Instance.dialogsServerOnly.size();
                } else {
                    count = MessagesController.Instance.dialogs.size();
                }
                if (count == 0 && MessagesController.Instance.loadingDialogs) {
                    return true;
                }
                if (!MessagesController.Instance.dialogsEndReached) {
                    count++;
                }
                if (count != 0) {
                    z = false;
                }
                return z;
            }
        }
    }

    class C08767 implements OnQueryTextListener {
        C08767() {
        }

        public boolean onQueryTextSubmit(String s) {
            return true;
        }

        public boolean onQueryTextChange(String s) {
            MessagesActivity.this.searchDialogs(s);
            if (s.length() != 0) {
                MessagesActivity.this.searchWas = true;
                if (MessagesActivity.this.messagesListViewAdapter != null) {
                    MessagesActivity.this.messagesListViewAdapter.notifyDataSetChanged();
                }
                if (MessagesActivity.this.searchEmptyView != null) {
                    MessagesActivity.this.messagesListView.setEmptyView(MessagesActivity.this.searchEmptyView);
                    MessagesActivity.this.empryView.setVisibility(8);
                }
            }
            return true;
        }
    }

    class C08778 implements OnActionExpandListener {
        C08778() {
        }

        public boolean onMenuItemActionExpand(MenuItem menuItem) {
            if (MessagesActivity.this.parentActivity != null) {
                MessagesActivity.this.parentActivity.getSupportActionBar().setIcon((int) C0419R.drawable.ic_ab_logo);
            }
            MessagesActivity.this.searching = true;
            if (MessagesActivity.this.messagesListView != null) {
                MessagesActivity.this.messagesListView.setEmptyView(MessagesActivity.this.searchEmptyView);
            }
            if (MessagesActivity.this.empryView != null) {
                MessagesActivity.this.empryView.setVisibility(8);
            }
            return true;
        }

        public boolean onMenuItemActionCollapse(MenuItem menuItem) {
            MessagesActivity.this.searchView.setQuery(BuildConfig.FLAVOR, false);
            MessagesActivity.this.searchDialogs(null);
            MessagesActivity.this.searching = false;
            MessagesActivity.this.searchWas = false;
            if (MessagesActivity.this.messagesListView != null) {
                MessagesActivity.this.messagesListView.setEmptyView(MessagesActivity.this.empryView);
                MessagesActivity.this.searchEmptyView.setVisibility(8);
            }
            if (MessagesActivity.this.messagesListViewAdapter != null) {
                MessagesActivity.this.messagesListViewAdapter.notifyDataSetChanged();
            }
            if (MessagesActivity.this.onlySelect) {
                ((ApplicationActivity) MessagesActivity.this.parentActivity).fixBackButton();
            }
            return true;
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.Instance.addObserver(this, 4);
        NotificationCenter.Instance.addObserver(this, 999);
        NotificationCenter.Instance.addObserver(this, 3);
        NotificationCenter.Instance.addObserver(this, 12);
        NotificationCenter.Instance.addObserver(this, 21);
        NotificationCenter.Instance.addObserver(this, 13);
        NotificationCenter.Instance.addObserver(this, 1234);
        if (getArguments() != null) {
            this.onlySelect = getArguments().getBoolean("onlySelect", false);
            this.serverOnly = getArguments().getBoolean("serverOnly", false);
        }
        if (!dialogsLoaded) {
            MessagesController.Instance.readContacts();
            MessagesController.Instance.loadDialogs(0, 0, 100, true);
            dialogsLoaded = true;
        }
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.Instance.removeObserver(this, 4);
        NotificationCenter.Instance.removeObserver(this, 999);
        NotificationCenter.Instance.removeObserver(this, 3);
        NotificationCenter.Instance.removeObserver(this, 12);
        NotificationCenter.Instance.removeObserver(this, 21);
        NotificationCenter.Instance.removeObserver(this, 13);
        NotificationCenter.Instance.removeObserver(this, 1234);
        this.delegate = null;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.fragmentView == null) {
            this.fragmentView = inflater.inflate(C0419R.layout.messages_list, container, false);
            this.messagesListViewAdapter = new MessagesAdapter(this.parentActivity);
            this.messagesListView = (ListView) this.fragmentView.findViewById(C0419R.id.messages_list_view);
            this.messagesListView.setAdapter(this.messagesListViewAdapter);
            this.progressView = this.fragmentView.findViewById(C0419R.id.progressLayout);
            this.messagesListViewAdapter.notifyDataSetChanged();
            this.searchEmptyView = (TextView) this.fragmentView.findViewById(C0419R.id.searchEmptyView);
            this.empryView = this.fragmentView.findViewById(C0419R.id.list_empty_view);
            if (MessagesController.Instance.loadingDialogs && MessagesController.Instance.dialogs.isEmpty()) {
                this.messagesListView.setEmptyView(null);
                this.searchEmptyView.setVisibility(8);
                this.empryView.setVisibility(8);
                this.progressView.setVisibility(0);
            } else {
                if (this.searching && this.searchWas) {
                    this.messagesListView.setEmptyView(this.searchEmptyView);
                    this.empryView.setVisibility(8);
                } else {
                    this.messagesListView.setEmptyView(this.empryView);
                    this.searchEmptyView.setVisibility(8);
                }
                this.progressView.setVisibility(8);
            }
            this.messagesListView.setOnItemClickListener(new C05541());
            this.messagesListView.setOnItemLongClickListener(new C05572());
            this.messagesListView.setOnScrollListener(new C05583());
            if (MessagesController.Instance.loadingDialogs) {
                this.progressView.setVisibility(0);
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
            if (this.onlySelect) {
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayShowHomeEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setDisplayShowCustomEnabled(false);
                actionBar.setSubtitle(null);
                actionBar.setCustomView(null);
                actionBar.setTitle(getStringEntry(C0419R.string.SelectChat));
                ((ApplicationActivity) this.parentActivity).fixBackButton();
            } else {
                ImageView view = (ImageView) this.parentActivity.findViewById(16908332);
                if (view == null) {
                    view = (ImageView) this.parentActivity.findViewById(C0419R.id.home);
                }
                if (view != null) {
                    view.setPadding(Utilities.dp(6), 0, Utilities.dp(6), 0);
                }
                actionBar.setHomeButtonEnabled(false);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayUseLogoEnabled(true);
                actionBar.setDisplayShowCustomEnabled(false);
                actionBar.setCustomView(null);
                actionBar.setSubtitle(null);
                actionBar.setTitle(getStringEntry(C0419R.string.AppName));
            }
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

    public void onResume() {
        super.onResume();
        if (!this.isFinish && getActivity() != null) {
            if (this.messagesListViewAdapter != null) {
                this.messagesListViewAdapter.notifyDataSetChanged();
            }
            ((ApplicationActivity) this.parentActivity).showActionBar();
            ((ApplicationActivity) this.parentActivity).updateActionBar();
        }
    }

    public void didReceivedNotification(int id, Object... args) {
        if (id == 4) {
            if (this.messagesListViewAdapter != null) {
                this.messagesListViewAdapter.notifyDataSetChanged();
            }
            if (this.messagesListView == null) {
                return;
            }
            if (MessagesController.Instance.loadingDialogs && MessagesController.Instance.dialogs.isEmpty()) {
                if (this.messagesListView.getEmptyView() != null) {
                    this.messagesListView.setEmptyView(null);
                }
                this.searchEmptyView.setVisibility(8);
                this.empryView.setVisibility(8);
                this.progressView.setVisibility(0);
                return;
            }
            if (this.messagesListView.getEmptyView() == null) {
                if (this.searching && this.searchWas) {
                    this.messagesListView.setEmptyView(this.searchEmptyView);
                    this.empryView.setVisibility(8);
                } else {
                    this.messagesListView.setEmptyView(this.empryView);
                    this.searchEmptyView.setVisibility(8);
                }
            }
            this.progressView.setVisibility(8);
        } else if (id == 999) {
            if (this.messagesListView != null) {
                updateVisibleRows(0);
            }
        } else if (id == 3) {
            updateVisibleRows(((Integer) args[0]).intValue());
        } else if (id == 12) {
            if (((Integer) args[0]).intValue() == this.activityToken) {
                updateSearchResults((ArrayList) args[1], (ArrayList) args[2], (ArrayList) args[3]);
            }
        } else if (id == 1234) {
            dialogsLoaded = false;
        } else if (id == 21) {
            updateVisibleRows(0);
        } else if (id == 13) {
            updateVisibleRows(0);
        }
    }

    private void updateVisibleRows(int mask) {
        if (this.messagesListView != null) {
            int count = this.messagesListView.getChildCount();
            for (int a = 0; a < count; a++) {
                View child = this.messagesListView.getChildAt(a);
                if (child instanceof DialogCell) {
                    ((DialogCell) child).update(mask);
                } else if (child instanceof ChatOrUserCell) {
                    ((ChatOrUserCell) child).update(mask);
                }
            }
        }
    }

    public void willBeHidden() {
        if (this.searchItem != null && this.searchItem.isActionViewExpanded()) {
            this.searchItem.collapseActionView();
        }
    }

    private void didSelectResult(final long dialog_id, boolean useAlert) {
        if (useAlert && this.selectAlertString != 0) {
            Builder builder = new Builder(this.parentActivity);
            builder.setTitle(C0419R.string.AppName);
            int lower_part = (int) dialog_id;
            User user;
            if (lower_part == 0) {
                user = (User) MessagesController.Instance.users.get(Integer.valueOf(((EncryptedChat) MessagesController.Instance.encryptedChats.get(Integer.valueOf((int) (dialog_id >> 32)))).user_id));
                builder.setMessage(String.format(getStringEntry(this.selectAlertString), new Object[]{Utilities.formatName(user.first_name, user.last_name)}));
            } else if (lower_part > 0) {
                user = (User) MessagesController.Instance.users.get(Integer.valueOf(lower_part));
                builder.setMessage(String.format(getStringEntry(this.selectAlertString), new Object[]{Utilities.formatName(user.first_name, user.last_name)}));
            } else if (lower_part < 0) {
                Chat chat = (Chat) MessagesController.Instance.chats.get(Integer.valueOf(-lower_part));
                builder.setMessage(String.format(getStringEntry(this.selectAlertString), new Object[]{chat.title}));
            }
            builder.setPositiveButton(C0419R.string.OK, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MessagesActivity.this.didSelectResult(dialog_id, false);
                }
            });
            builder.setNegativeButton(C0419R.string.Cancel, null);
            builder.show().setCanceledOnTouchOutside(true);
        } else if (this.delegate != null) {
            this.delegate.didSelectDialog(this, dialog_id);
            this.delegate = null;
        } else {
            finishFragment();
        }
    }

    public void updateSearchResults(final ArrayList<TLObject> result, final ArrayList<CharSequence> names, final ArrayList<User> encUsers) {
        Utilities.RunOnUIThread(new Runnable() {
            public void run() {
                Iterator i$ = result.iterator();
                while (i$.hasNext()) {
                    TLObject obj = (TLObject) i$.next();
                    if (obj instanceof User) {
                        User user = (User) obj;
                        MessagesController.Instance.users.putIfAbsent(Integer.valueOf(user.id), user);
                    } else if (obj instanceof Chat) {
                        Chat chat = (Chat) obj;
                        MessagesController.Instance.chats.putIfAbsent(Integer.valueOf(chat.id), chat);
                    } else if (obj instanceof EncryptedChat) {
                        EncryptedChat chat2 = (EncryptedChat) obj;
                        MessagesController.Instance.encryptedChats.putIfAbsent(Integer.valueOf(chat2.id), chat2);
                    }
                }
                i$ = encUsers.iterator();
                while (i$.hasNext()) {
                    user = (User) i$.next();
                    MessagesController.Instance.users.putIfAbsent(Integer.valueOf(user.id), user);
                }
                MessagesActivity.this.searchResult = result;
                MessagesActivity.this.searchResultNames = names;
                if (MessagesActivity.this.searching) {
                    MessagesActivity.this.messagesListViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void searchDialogs(final String query) {
        if (query == null) {
            this.searchResult = null;
            this.searchResultNames = null;
            return;
        }
        try {
            if (this.searchDialogsTimer != null) {
                this.searchDialogsTimer.cancel();
            }
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
        this.searchDialogsTimer = new Timer();
        this.searchDialogsTimer.schedule(new TimerTask() {
            public void run() {
                try {
                    MessagesActivity.this.searchDialogsTimer.cancel();
                    MessagesActivity.this.searchDialogsTimer = null;
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
                MessagesStorage.Instance.searchDialogs(Integer.valueOf(MessagesActivity.this.activityToken), query, !MessagesActivity.this.serverOnly);
            }
        }, 100, 300);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (this.onlySelect) {
            inflater.inflate(C0419R.menu.messages_list_select_menu, menu);
        } else {
            inflater.inflate(C0419R.menu.messages_list_menu, menu);
        }
        this.searchItem = (SupportMenuItem) menu.findItem(C0419R.id.messages_list_menu_search);
        this.searchView = (SearchView) this.searchItem.getActionView();
        TextView textView = (TextView) this.searchView.findViewById(C0419R.id.search_src_text);
        if (textView != null) {
            textView.setTextColor(-1);
            try {
                Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
                mCursorDrawableRes.setAccessible(true);
                mCursorDrawableRes.set(textView, Integer.valueOf(C0419R.drawable.search_carret));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ImageView img = (ImageView) this.searchView.findViewById(C0419R.id.search_close_btn);
        if (img != null) {
            img.setImageResource(C0419R.drawable.ic_msg_btn_cross_custom);
        }
        this.searchView.setOnQueryTextListener(new C08767());
        this.searchItem.setSupportOnActionExpandListener(new C08778());
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        FragmentActivity inflaterActivity = this.parentActivity;
        if (inflaterActivity == null) {
            inflaterActivity = getActivity();
        }
        if (inflaterActivity != null) {
            BaseFragment fragment;
            Bundle bundle;
            switch (itemId) {
                case 16908332:
                    if (this.onlySelect) {
                        finishFragment();
                        break;
                    }
                    break;
                case C0419R.id.messages_list_menu_new_messages:
                    fragment = new ContactsActivity();
                    bundle = new Bundle();
                    bundle.putBoolean("onlyUsers", true);
                    bundle.putBoolean("destroyAfterSelect", true);
                    bundle.putBoolean("usersAsSections", true);
                    fragment.animationType = 1;
                    fragment.setArguments(bundle);
                    ((ApplicationActivity) inflaterActivity).presentFragment(fragment, "contacts_chat", false);
                    break;
                case C0419R.id.messages_list_menu_new_chat:
                    ((ApplicationActivity) inflaterActivity).presentFragment(new GroupCreateActivity(), "group_create", false);
                    break;
                case C0419R.id.messages_list_menu_new_secret_chat:
                    fragment = new ContactsActivity();
                    bundle = new Bundle();
                    bundle.putBoolean("onlyUsers", true);
                    bundle.putBoolean("destroyAfterSelect", true);
                    bundle.putBoolean("usersAsSections", true);
                    bundle.putBoolean("createSecretChat", true);
                    fragment.animationType = 1;
                    fragment.setArguments(bundle);
                    ((ApplicationActivity) inflaterActivity).presentFragment(fragment, "contacts_chat", false);
                    break;
                case C0419R.id.messages_list_menu_contacts:
                    ((ApplicationActivity) inflaterActivity).presentFragment(new ContactsActivity(), "contacts", false);
                    break;
                case C0419R.id.messages_list_menu_settings:
                    ((ApplicationActivity) inflaterActivity).presentFragment(new SettingsActivity(), "settings", false);
                    break;
                default:
                    break;
            }
        }
        return true;
    }
}
