package org.telegram.ui;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.TL.TLRPC.EncryptedChat;
import org.telegram.TL.TLRPC.TL_contact;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesController.Contact;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Cells.ChatOrUserCell;
import org.telegram.ui.Views.BackupImageView;
import org.telegram.ui.Views.BaseFragment;
import org.telegram.ui.Views.OnSwipeTouchListener;
import org.telegram.ui.Views.PinnedHeaderListView;
import org.telegram.ui.Views.SectionedBaseAdapter;

public class ContactsActivity extends BaseFragment implements NotificationCenterDelegate {
    private boolean createSecretChat;
    private boolean creatingChat = false;
    public ContactsActivityDelegate delegate;
    private boolean destroyAfterSelect;
    private TextView epmtyTextView;
    private HashMap<Integer, User> ignoreUsers;
    private PinnedHeaderListView listView;
    private SectionedBaseAdapter listViewAdapter;
    private boolean onlyUsers;
    private boolean returnAsResult;
    private Timer searchDialogsTimer;
    private SupportMenuItem searchItem;
    private BaseAdapter searchListViewAdapter;
    public ArrayList<User> searchResult;
    public ArrayList<CharSequence> searchResultNames;
    private SearchView searchView;
    private boolean searchWas;
    private boolean searching;
    public int selectAlertString = 0;
    private boolean usersAsSections;

    class C04831 implements OnItemClickListener {
        C04831() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (ContactsActivity.this.searching && ContactsActivity.this.searchWas) {
                int user_id = ((User) ContactsActivity.this.searchResult.get(i)).id;
                if (user_id != UserConfig.clientUserId) {
                    if (ContactsActivity.this.returnAsResult) {
                        if (ContactsActivity.this.ignoreUsers == null || !ContactsActivity.this.ignoreUsers.containsKey(Integer.valueOf(user_id))) {
                            ContactsActivity.this.didSelectResult(user_id, true);
                            return;
                        }
                        return;
                    } else if (ContactsActivity.this.createSecretChat) {
                        ContactsActivity.this.creatingChat = true;
                        MessagesController.Instance.startSecretChat(ContactsActivity.this.parentActivity, user_id);
                        return;
                    } else {
                        ChatActivity fragment = new ChatActivity();
                        Bundle bundle = new Bundle();
                        bundle.putInt("user_id", user_id);
                        fragment.setArguments(bundle);
                        ((ApplicationActivity) ContactsActivity.this.parentActivity).presentFragment(fragment, "chat" + Math.random(), ContactsActivity.this.destroyAfterSelect, false);
                        return;
                    }
                }
                return;
            }
            int section = ContactsActivity.this.listViewAdapter.getSectionForPosition(i);
            int row = ContactsActivity.this.listViewAdapter.getPositionInSectionForPosition(i);
            int uid = 0;
            if (ContactsActivity.this.usersAsSections) {
                if (section < MessagesController.Instance.sortedUsersSectionsArray.size()) {
                    uid = ((TL_contact) ((ArrayList) MessagesController.Instance.usersSectionsDict.get(MessagesController.Instance.sortedUsersSectionsArray.get(section))).get(row)).user_id;
                }
            } else if (section == 0) {
                if (row == 0) {
                    try {
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("text/plain");
                        intent.putExtra("android.intent.extra.TEXT", ContactsActivity.this.getStringEntry(C0419R.string.InviteText));
                        ContactsActivity.this.startActivity(intent);
                        return;
                    } catch (Exception e) {
                        FileLog.m799e("tmessages", e);
                        return;
                    }
                }
                if (row - 1 < MessagesController.Instance.contacts.size()) {
                    uid = ((TL_contact) MessagesController.Instance.contacts.get(row - 1)).user_id;
                } else {
                    return;
                }
            }
            if (uid == 0) {
                String usePhone = null;
                Iterator i$ = ((Contact) ((ArrayList) MessagesController.Instance.contactsSectionsDict.get(MessagesController.Instance.sortedContactsSectionsArray.get(section - 1))).get(row)).phones.iterator();
                while (i$.hasNext()) {
                    String phone = (String) i$.next();
                    if (usePhone == null) {
                        usePhone = phone;
                    }
                    TL_contact cLocal = (TL_contact) MessagesController.Instance.contactsByPhones.get(PhoneFormat.stripExceptNumbers(usePhone));
                    if (cLocal != null) {
                        if (cLocal.user_id == UserConfig.clientUserId) {
                            return;
                        }
                        if (ContactsActivity.this.createSecretChat) {
                            ContactsActivity.this.creatingChat = true;
                            MessagesController.Instance.startSecretChat(ContactsActivity.this.parentActivity, cLocal.user_id);
                            return;
                        }
                        fragment = new ChatActivity();
                        bundle = new Bundle();
                        bundle.putInt("user_id", cLocal.user_id);
                        fragment.setArguments(bundle);
                        ((ApplicationActivity) ContactsActivity.this.parentActivity).presentFragment(fragment, "chat" + Math.random(), ContactsActivity.this.destroyAfterSelect, false);
                        return;
                    }
                }
                Builder builder = new Builder(ContactsActivity.this.parentActivity);
                builder.setMessage(ContactsActivity.this.getStringEntry(C0419R.string.InviteUser));
                builder.setTitle(ContactsActivity.this.getStringEntry(C0419R.string.AppName));
                final String arg1 = usePhone;
                builder.setPositiveButton(ContactsActivity.this.getStringEntry(C0419R.string.OK), new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Intent intent = new Intent("android.intent.action.VIEW", Uri.fromParts("sms", arg1, null));
                            intent.putExtra("sms_body", ContactsActivity.this.getStringEntry(C0419R.string.InviteText));
                            ContactsActivity.this.startActivity(intent);
                        } catch (Exception e) {
                            FileLog.m799e("tmessages", e);
                        }
                    }
                });
                builder.setNegativeButton(ContactsActivity.this.getStringEntry(C0419R.string.Cancel), null);
                builder.show().setCanceledOnTouchOutside(true);
            } else if (uid == UserConfig.clientUserId) {
            } else {
                if (ContactsActivity.this.returnAsResult) {
                    if (ContactsActivity.this.ignoreUsers == null || !ContactsActivity.this.ignoreUsers.containsKey(Integer.valueOf(uid))) {
                        ContactsActivity.this.didSelectResult(uid, true);
                    }
                } else if (ContactsActivity.this.createSecretChat) {
                    ContactsActivity.this.creatingChat = true;
                    MessagesController.Instance.startSecretChat(ContactsActivity.this.parentActivity, uid);
                } else {
                    fragment = new ChatActivity();
                    bundle = new Bundle();
                    bundle.putInt("user_id", uid);
                    fragment.setArguments(bundle);
                    ((ApplicationActivity) ContactsActivity.this.parentActivity).presentFragment(fragment, "chat" + Math.random(), ContactsActivity.this.destroyAfterSelect, false);
                }
            }
        }
    }

    public static class ContactListRowHolder {
        public BackupImageView avatarImage;
        public TextView messageTextView;
        public TextView nameTextView;

        public ContactListRowHolder(View view) {
            this.messageTextView = (TextView) view.findViewById(C0419R.id.messages_list_row_message);
            this.nameTextView = (TextView) view.findViewById(C0419R.id.messages_list_row_name);
            this.avatarImage = (BackupImageView) view.findViewById(C0419R.id.messages_list_row_avatar);
        }
    }

    public interface ContactsActivityDelegate {
        void didSelectContact(int i);
    }

    private class SearchAdapter extends BaseAdapter {
        private Context mContext;

        public SearchAdapter(Context context) {
            this.mContext = context;
        }

        public boolean areAllItemsEnabled() {
            return true;
        }

        public boolean isEnabled(int i) {
            return true;
        }

        public int getCount() {
            if (ContactsActivity.this.searchResult == null) {
                return 0;
            }
            return ContactsActivity.this.searchResult.size();
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
            boolean z = false;
            if (view == null) {
                view = new ChatOrUserCell(this.mContext);
                ((ChatOrUserCell) view).usePadding = false;
            }
            ChatOrUserCell chatOrUserCell = (ChatOrUserCell) view;
            if (i != ContactsActivity.this.searchResult.size() - 1) {
                z = true;
            }
            chatOrUserCell.useSeparator = z;
            User user = (User) MessagesController.Instance.users.get(Integer.valueOf(((User) ContactsActivity.this.searchResult.get(i)).id));
            if (user != null) {
                ((ChatOrUserCell) view).setData(user, null, null, (CharSequence) ContactsActivity.this.searchResultNames.get(i), null);
                if (ContactsActivity.this.ignoreUsers != null) {
                    if (ContactsActivity.this.ignoreUsers.containsKey(Integer.valueOf(user.id))) {
                        ((ChatOrUserCell) view).drawAlpha = 0.5f;
                    } else {
                        ((ChatOrUserCell) view).drawAlpha = 1.0f;
                    }
                }
            }
            return view;
        }

        public int getItemViewType(int i) {
            return 0;
        }

        public int getViewTypeCount() {
            return 1;
        }

        public boolean isEmpty() {
            return ContactsActivity.this.searchResult == null || ContactsActivity.this.searchResult.size() == 0;
        }
    }

    class C08592 extends OnSwipeTouchListener {
        C08592() {
        }

        public void onSwipeRight() {
            ContactsActivity.this.finishFragment(true);
            if (ContactsActivity.this.searchItem != null && ContactsActivity.this.searchItem.isActionViewExpanded()) {
                ContactsActivity.this.searchItem.collapseActionView();
            }
        }
    }

    class C08603 extends OnSwipeTouchListener {
        C08603() {
        }

        public void onSwipeRight() {
            ContactsActivity.this.finishFragment(true);
            if (ContactsActivity.this.searchItem != null && ContactsActivity.this.searchItem.isActionViewExpanded()) {
                ContactsActivity.this.searchItem.collapseActionView();
            }
        }
    }

    class C08618 implements OnQueryTextListener {
        C08618() {
        }

        public boolean onQueryTextSubmit(String s) {
            return true;
        }

        public boolean onQueryTextChange(String s) {
            ContactsActivity.this.searchDialogs(s);
            if (s.length() != 0) {
                ContactsActivity.this.searchWas = true;
                if (ContactsActivity.this.listView != null) {
                    ContactsActivity.this.listView.setPadding(Utilities.dp(16), ContactsActivity.this.listView.getPaddingTop(), Utilities.dp(16), ContactsActivity.this.listView.getPaddingBottom());
                    ContactsActivity.this.listView.setAdapter(ContactsActivity.this.searchListViewAdapter);
                    if (VERSION.SDK_INT >= 11) {
                        ContactsActivity.this.listView.setFastScrollAlwaysVisible(false);
                    }
                    ContactsActivity.this.listView.setFastScrollEnabled(false);
                    ContactsActivity.this.listView.setVerticalScrollBarEnabled(true);
                }
                if (ContactsActivity.this.epmtyTextView != null) {
                    ContactsActivity.this.epmtyTextView.setText(ContactsActivity.this.getStringEntry(C0419R.string.NoResult));
                }
            }
            return true;
        }
    }

    class C08629 implements OnActionExpandListener {
        C08629() {
        }

        public boolean onMenuItemActionExpand(MenuItem menuItem) {
            if (ContactsActivity.this.parentActivity != null) {
                ContactsActivity.this.parentActivity.getSupportActionBar().setIcon((int) C0419R.drawable.ic_ab_search);
            }
            ContactsActivity.this.searching = true;
            return true;
        }

        public boolean onMenuItemActionCollapse(MenuItem menuItem) {
            ContactsActivity.this.searchView.setQuery(BuildConfig.FLAVOR, false);
            ContactsActivity.this.searchDialogs(null);
            ContactsActivity.this.searching = false;
            ContactsActivity.this.searchWas = false;
            ViewGroup group = (ViewGroup) ContactsActivity.this.listView.getParent();
            ContactsActivity.this.listView.setAdapter(ContactsActivity.this.listViewAdapter);
            if (Utilities.isRTL) {
                ContactsActivity.this.listView.setPadding(Utilities.dp(30), ContactsActivity.this.listView.getPaddingTop(), Utilities.dp(16), ContactsActivity.this.listView.getPaddingBottom());
            } else {
                ContactsActivity.this.listView.setPadding(Utilities.dp(16), ContactsActivity.this.listView.getPaddingTop(), Utilities.dp(30), ContactsActivity.this.listView.getPaddingBottom());
            }
            if (VERSION.SDK_INT >= 11) {
                ContactsActivity.this.listView.setFastScrollAlwaysVisible(true);
            }
            ContactsActivity.this.listView.setFastScrollEnabled(true);
            ContactsActivity.this.listView.setVerticalScrollBarEnabled(false);
            ((ApplicationActivity) ContactsActivity.this.parentActivity).updateActionBar();
            ContactsActivity.this.epmtyTextView.setText(ContactsActivity.this.getStringEntry(C0419R.string.NoContacts));
            return true;
        }
    }

    private class ListAdapter extends SectionedBaseAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public Object getItem(int section, int position) {
            return null;
        }

        public long getItemId(int section, int position) {
            return 0;
        }

        public int getSectionCount() {
            int count;
            if (ContactsActivity.this.usersAsSections) {
                count = 0 + MessagesController.Instance.sortedUsersSectionsArray.size();
            } else {
                count = 0 + 1;
            }
            if (ContactsActivity.this.onlyUsers) {
                return count;
            }
            return count + MessagesController.Instance.sortedContactsSectionsArray.size();
        }

        public int getCountForSection(int section) {
            if (ContactsActivity.this.usersAsSections) {
                if (section < MessagesController.Instance.sortedUsersSectionsArray.size()) {
                    return ((ArrayList) MessagesController.Instance.usersSectionsDict.get(MessagesController.Instance.sortedUsersSectionsArray.get(section))).size();
                }
            } else if (section == 0) {
                return MessagesController.Instance.contacts.size() + 1;
            }
            return ((ArrayList) MessagesController.Instance.contactsSectionsDict.get(MessagesController.Instance.sortedContactsSectionsArray.get(section - 1))).size();
        }

        public View getItemView(int section, int position, View convertView, ViewGroup parent) {
            View divider;
            User user = null;
            int count = 0;
            if (ContactsActivity.this.usersAsSections) {
                if (section < MessagesController.Instance.sortedUsersSectionsArray.size()) {
                    ArrayList<TL_contact> arr = (ArrayList) MessagesController.Instance.usersSectionsDict.get(MessagesController.Instance.sortedUsersSectionsArray.get(section));
                    user = (User) MessagesController.Instance.users.get(Integer.valueOf(((TL_contact) arr.get(position)).user_id));
                    count = arr.size();
                }
            } else if (section == 0) {
                if (position == 0) {
                    if (convertView == null) {
                        convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.contacts_invite_row_layout, parent, false);
                    }
                    divider = convertView.findViewById(C0419R.id.settings_row_divider);
                    if (MessagesController.Instance.contacts.isEmpty()) {
                        divider.setVisibility(4);
                    } else {
                        divider.setVisibility(0);
                    }
                    return convertView;
                }
                user = (User) MessagesController.Instance.users.get(Integer.valueOf(((TL_contact) MessagesController.Instance.contacts.get(position - 1)).user_id));
                count = MessagesController.Instance.contacts.size();
            }
            if (user != null) {
                boolean z;
                if (convertView == null) {
                    View chatOrUserCell = new ChatOrUserCell(this.mContext);
                    ((ChatOrUserCell) chatOrUserCell).useBoldFont = true;
                    ((ChatOrUserCell) chatOrUserCell).usePadding = false;
                }
                ((ChatOrUserCell) convertView).setData(user, null, null, null, null);
                if (ContactsActivity.this.ignoreUsers != null) {
                    if (ContactsActivity.this.ignoreUsers.containsKey(Integer.valueOf(user.id))) {
                        ((ChatOrUserCell) convertView).drawAlpha = 0.5f;
                    } else {
                        ((ChatOrUserCell) convertView).drawAlpha = 1.0f;
                    }
                }
                ChatOrUserCell chatOrUserCell2 = (ChatOrUserCell) convertView;
                if (position != count - 1) {
                    z = true;
                } else {
                    z = false;
                }
                chatOrUserCell2.useSeparator = z;
                return convertView;
            }
            TextView textView;
            if (convertView == null) {
                convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_row_button_layout, parent, false);
                textView = (TextView) convertView.findViewById(C0419R.id.settings_row_text);
            } else {
                textView = (TextView) convertView.findViewById(C0419R.id.settings_row_text);
            }
            divider = convertView.findViewById(C0419R.id.settings_row_divider);
            ArrayList<Contact> arr2 = (ArrayList) MessagesController.Instance.contactsSectionsDict.get(MessagesController.Instance.sortedContactsSectionsArray.get(section - 1));
            Contact contact = (Contact) arr2.get(position);
            if (position == arr2.size() - 1) {
                divider.setVisibility(4);
            } else {
                divider.setVisibility(0);
            }
            if (contact.first_name != null && contact.last_name != null) {
                textView.setText(Html.fromHtml(contact.first_name + " <b>" + contact.last_name + "</b>"));
            } else if (contact.first_name == null || contact.last_name != null) {
                textView.setText(Html.fromHtml("<b>" + contact.last_name + "</b>"));
            } else {
                textView.setText(Html.fromHtml("<b>" + contact.first_name + "</b>"));
            }
            return convertView;
        }

        public int getItemViewType(int section, int position) {
            if (ContactsActivity.this.usersAsSections) {
                if (section < MessagesController.Instance.sortedUsersSectionsArray.size()) {
                    return 0;
                }
            } else if (section == 0) {
                if (position == 0) {
                    return 2;
                }
                return 0;
            }
            return 1;
        }

        public int getItemViewTypeCount() {
            return 3;
        }

        public int getSectionHeaderViewType(int section) {
            if (ContactsActivity.this.usersAsSections) {
                return section < MessagesController.Instance.sortedUsersSectionsArray.size() ? 1 : 1;
            } else {
                if (section == 0) {
                    return 0;
                }
                return 1;
            }
        }

        public int getSectionHeaderViewTypeCount() {
            return 2;
        }

        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
            if (ContactsActivity.this.usersAsSections) {
                if (section < MessagesController.Instance.sortedUsersSectionsArray.size()) {
                    if (convertView == null) {
                        convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_section_layout, parent, false);
                        convertView.setBackgroundColor(-1);
                    }
                    ((TextView) convertView.findViewById(C0419R.id.settings_section_text)).setText((CharSequence) MessagesController.Instance.sortedUsersSectionsArray.get(section));
                    return convertView;
                }
            } else if (section == 0) {
                if (convertView == null) {
                    convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.empty_layout, parent, false);
                }
                return convertView;
            }
            if (convertView == null) {
                convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_section_layout, parent, false);
                convertView.setBackgroundColor(-1);
            }
            ((TextView) convertView.findViewById(C0419R.id.settings_section_text)).setText((CharSequence) MessagesController.Instance.sortedContactsSectionsArray.get(section - 1));
            return convertView;
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.Instance.addObserver(this, 14);
        NotificationCenter.Instance.addObserver(this, 13);
        NotificationCenter.Instance.addObserver(this, 3);
        NotificationCenter.Instance.addObserver(this, 23);
        if (getArguments() != null) {
            this.onlyUsers = getArguments().getBoolean("onlyUsers", false);
            this.destroyAfterSelect = getArguments().getBoolean("destroyAfterSelect", false);
            this.usersAsSections = getArguments().getBoolean("usersAsSections", false);
            this.returnAsResult = getArguments().getBoolean("returnAsResult", false);
            this.createSecretChat = getArguments().getBoolean("createSecretChat", false);
            if (this.destroyAfterSelect) {
                this.ignoreUsers = (HashMap) NotificationCenter.Instance.getFromMemCache(7);
            }
        }
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.Instance.removeObserver(this, 14);
        NotificationCenter.Instance.removeObserver(this, 13);
        NotificationCenter.Instance.removeObserver(this, 3);
        NotificationCenter.Instance.removeObserver(this, 23);
        this.delegate = null;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void willBeHidden() {
        if (this.searchItem != null && this.searchItem.isActionViewExpanded()) {
            this.searchItem.collapseActionView();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.fragmentView == null) {
            this.fragmentView = inflater.inflate(C0419R.layout.contacts_layout, container, false);
            this.epmtyTextView = (TextView) this.fragmentView.findViewById(C0419R.id.searchEmptyView);
            this.searchListViewAdapter = new SearchAdapter(this.parentActivity);
            this.listView = (PinnedHeaderListView) this.fragmentView.findViewById(C0419R.id.listView);
            this.listView.setEmptyView(this.epmtyTextView);
            this.listView.setVerticalScrollBarEnabled(false);
            PinnedHeaderListView pinnedHeaderListView = this.listView;
            android.widget.ListAdapter listAdapter = new ListAdapter(this.parentActivity);
            this.listViewAdapter = listAdapter;
            pinnedHeaderListView.setAdapter(listAdapter);
            this.listView.setOnItemClickListener(new C04831());
            this.listView.setOnTouchListener(new C08592());
            this.epmtyTextView.setOnTouchListener(new C08603());
        } else {
            ViewGroup parent = (ViewGroup) this.fragmentView.getParent();
            if (parent != null) {
                parent.removeView(this.fragmentView);
            }
        }
        return this.fragmentView;
    }

    private void didSelectResult(final int user_id, boolean useAlert) {
        if (!useAlert || this.selectAlertString == 0) {
            if (this.delegate != null) {
                this.delegate.didSelectContact(user_id);
                this.delegate = null;
            }
            finishFragment();
            if (this.searchItem != null && this.searchItem.isActionViewExpanded()) {
                this.searchItem.collapseActionView();
                return;
            }
            return;
        }
        Builder builder = new Builder(this.parentActivity);
        builder.setTitle(C0419R.string.AppName);
        User user = (User) MessagesController.Instance.users.get(Integer.valueOf(user_id));
        builder.setMessage(String.format(getStringEntry(this.selectAlertString), new Object[]{Utilities.formatName(user.first_name, user.last_name)}));
        builder.setPositiveButton(C0419R.string.OK, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ContactsActivity.this.didSelectResult(user_id, false);
            }
        });
        builder.setNegativeButton(C0419R.string.Cancel, null);
        builder.show().setCanceledOnTouchOutside(true);
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
            actionBar.setSubtitle(null);
            TextView title = (TextView) this.parentActivity.findViewById(C0419R.id.action_bar_title);
            if (title == null) {
                title = (TextView) this.parentActivity.findViewById(this.parentActivity.getResources().getIdentifier("action_bar_title", "id", "android"));
            }
            if (title != null) {
                title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                title.setCompoundDrawablePadding(0);
            }
            if (this.destroyAfterSelect) {
                actionBar.setTitle(getStringEntry(C0419R.string.SelectContact));
            } else {
                actionBar.setTitle(getStringEntry(C0419R.string.Contacts));
            }
            ((ApplicationActivity) this.parentActivity).fixBackButton();
        }
    }

    public void onResume() {
        super.onResume();
        if (!this.isFinish && getActivity() != null) {
            if (!(this.firstStart || this.listViewAdapter == null)) {
                this.listViewAdapter.notifyDataSetChanged();
            }
            this.firstStart = false;
            ((ApplicationActivity) this.parentActivity).showActionBar();
            ((ApplicationActivity) this.parentActivity).updateActionBar();
        }
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
                    ContactsActivity.this.searchDialogsTimer.cancel();
                    ContactsActivity.this.searchDialogsTimer = null;
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
                ContactsActivity.this.processSearch(query);
            }
        }, 100, 300);
    }

    private void processSearch(final String query) {
        Utilities.globalQueue.postRunnable(new Runnable() {
            public void run() {
                String q = query.trim().toLowerCase();
                if (q.length() == 0) {
                    ContactsActivity.this.updateSearchResults(new ArrayList(), new ArrayList());
                    return;
                }
                long time = System.currentTimeMillis();
                ArrayList<User> resultArray = new ArrayList();
                ArrayList<CharSequence> resultArrayNames = new ArrayList();
                Iterator i$ = MessagesController.Instance.contacts.iterator();
                while (i$.hasNext()) {
                    User user = (User) MessagesController.Instance.users.get(Integer.valueOf(((TL_contact) i$.next()).user_id));
                    if (((user.first_name != null && user.first_name.toLowerCase().startsWith(q)) || (user.last_name != null && user.last_name.toLowerCase().startsWith(q))) && user.id != UserConfig.clientUserId) {
                        resultArrayNames.add(Utilities.generateSearchName(user.first_name, user.last_name, q));
                        resultArray.add(user);
                    }
                }
                ContactsActivity.this.updateSearchResults(resultArray, resultArrayNames);
            }
        });
    }

    private void updateSearchResults(final ArrayList<User> users, final ArrayList<CharSequence> names) {
        Utilities.RunOnUIThread(new Runnable() {
            public void run() {
                ContactsActivity.this.searchResult = users;
                ContactsActivity.this.searchResultNames = names;
                ContactsActivity.this.searchListViewAdapter.notifyDataSetChanged();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                if (this.searchItem != null && this.searchItem.isActionViewExpanded()) {
                    this.searchItem.collapseActionView();
                }
                finishFragment();
                break;
        }
        return true;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(C0419R.menu.contacts_menu, menu);
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
        this.searchView.setOnQueryTextListener(new C08618());
        this.searchItem.setSupportOnActionExpandListener(new C08629());
    }

    public void didReceivedNotification(int id, Object... args) {
        if (id == 13 || id == 14) {
            if (this.listViewAdapter != null) {
                this.listViewAdapter.notifyDataSetChanged();
            }
        } else if (id == 3) {
            int mask = ((Integer) args[0]).intValue();
            if ((mask & 2) != 0 || (mask & 1) != 0 || (mask & 4) != 0) {
                updateVisibleRows(mask);
            }
        } else if (id == 23 && this.createSecretChat && this.creatingChat) {
            EncryptedChat encryptedChat = args[0];
            ChatActivity fragment = new ChatActivity();
            Bundle bundle = new Bundle();
            bundle.putInt("enc_id", encryptedChat.id);
            fragment.setArguments(bundle);
            ((ApplicationActivity) this.parentActivity).presentFragment(fragment, "chat" + Math.random(), true, false);
        }
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
}
