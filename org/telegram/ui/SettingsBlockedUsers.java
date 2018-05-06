package org.telegram.ui;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.TL_contactBlocked;
import org.telegram.TL.TLRPC.TL_contacts_block;
import org.telegram.TL.TLRPC.TL_contacts_getBlocked;
import org.telegram.TL.TLRPC.TL_contacts_unblock;
import org.telegram.TL.TLRPC.TL_error;
import org.telegram.TL.TLRPC.TL_inputUserContact;
import org.telegram.TL.TLRPC.TL_inputUserForeign;
import org.telegram.TL.TLRPC.TL_userForeign;
import org.telegram.TL.TLRPC.TL_userRequest;
import org.telegram.TL.TLRPC.User;
import org.telegram.TL.TLRPC.contacts_Blocked;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.RPCRequest;
import org.telegram.messenger.RPCRequest.RPCRequestDelegate;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Cells.ChatOrUserCell;
import org.telegram.ui.ContactsActivity.ContactsActivityDelegate;
import org.telegram.ui.Views.BaseFragment;
import org.telegram.ui.Views.OnSwipeTouchListener;

public class SettingsBlockedUsers extends BaseFragment implements NotificationCenterDelegate, ContactsActivityDelegate {
    private ArrayList<TL_contactBlocked> blockedContacts = new ArrayList();
    private HashMap<Integer, TL_contactBlocked> blockedContactsDict = new HashMap();
    private View emptyView;
    private ListView listView;
    private ListAdapter listViewAdapter;
    private boolean loading;
    private View progressView;
    private int selectedUserId;

    class C05721 implements OnItemClickListener {
        C05721() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (i < SettingsBlockedUsers.this.blockedContacts.size()) {
                UserProfileActivity fragment = new UserProfileActivity();
                Bundle args = new Bundle();
                args.putInt("user_id", ((TL_contactBlocked) SettingsBlockedUsers.this.blockedContacts.get(i)).user_id);
                fragment.setArguments(args);
                ((ApplicationActivity) SettingsBlockedUsers.this.parentActivity).presentFragment(fragment, "user_" + ((TL_contactBlocked) SettingsBlockedUsers.this.blockedContacts.get(i)).user_id, false);
            }
        }
    }

    class C05742 implements OnItemLongClickListener {

        class C05731 implements OnClickListener {

            class C08831 implements RPCRequestDelegate {
                C08831() {
                }

                public void run(TLObject response, TL_error error) {
                }
            }

            C05731() {
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    TL_contacts_unblock req = new TL_contacts_unblock();
                    User user = (User) MessagesController.Instance.users.get(Integer.valueOf(SettingsBlockedUsers.this.selectedUserId));
                    if ((user instanceof TL_userForeign) || (user instanceof TL_userRequest)) {
                        req.id = new TL_inputUserForeign();
                        req.id.user_id = SettingsBlockedUsers.this.selectedUserId;
                        req.id.access_hash = user.access_hash;
                    } else {
                        req.id = new TL_inputUserContact();
                        req.id.user_id = SettingsBlockedUsers.this.selectedUserId;
                    }
                    TL_contactBlocked blocked = (TL_contactBlocked) SettingsBlockedUsers.this.blockedContactsDict.get(Integer.valueOf(SettingsBlockedUsers.this.selectedUserId));
                    SettingsBlockedUsers.this.blockedContactsDict.remove(Integer.valueOf(SettingsBlockedUsers.this.selectedUserId));
                    SettingsBlockedUsers.this.blockedContacts.remove(blocked);
                    SettingsBlockedUsers.this.listViewAdapter.notifyDataSetChanged();
                    ConnectionsManager.Instance.performRpc(req, new C08831(), null, true, RPCRequest.RPCRequestClassGeneric);
                }
            }
        }

        C05742() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (i < SettingsBlockedUsers.this.blockedContacts.size()) {
                SettingsBlockedUsers.this.selectedUserId = ((TL_contactBlocked) SettingsBlockedUsers.this.blockedContacts.get(i)).user_id;
                Builder builder = new Builder(SettingsBlockedUsers.this.parentActivity);
                builder.setItems(new CharSequence[]{SettingsBlockedUsers.this.getStringEntry(C0419R.string.Unblock)}, new C05731());
                builder.show().setCanceledOnTouchOutside(true);
            }
            return true;
        }
    }

    private class ListAdapter extends BaseAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean areAllItemsEnabled() {
            return false;
        }

        public boolean isEnabled(int i) {
            return i != SettingsBlockedUsers.this.blockedContacts.size();
        }

        public int getCount() {
            if (SettingsBlockedUsers.this.blockedContacts.isEmpty()) {
                return 0;
            }
            return SettingsBlockedUsers.this.blockedContacts.size() + 1;
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
            if (type == 0) {
                if (view == null) {
                    view = new ChatOrUserCell(this.mContext);
                    ((ChatOrUserCell) view).useBoldFont = true;
                    ((ChatOrUserCell) view).usePadding = false;
                    ((ChatOrUserCell) view).useSeparator = true;
                }
                User user = (User) MessagesController.Instance.users.get(Integer.valueOf(((TL_contactBlocked) SettingsBlockedUsers.this.blockedContacts.get(i)).user_id));
                ChatOrUserCell chatOrUserCell = (ChatOrUserCell) view;
                String format = (user.phone == null || user.phone.length() == 0) ? "Unknown" : PhoneFormat.Instance.format("+" + user.phone);
                chatOrUserCell.setData(user, null, null, null, format);
                return view;
            } else if (type != 1 || view != null) {
                return view;
            } else {
                view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_unblock_info_row_layout, viewGroup, false);
                SettingsBlockedUsers.this.registerForContextMenu(view);
                return view;
            }
        }

        public int getItemViewType(int i) {
            if (i == SettingsBlockedUsers.this.blockedContacts.size()) {
                return 1;
            }
            return 0;
        }

        public int getViewTypeCount() {
            return 2;
        }

        public boolean isEmpty() {
            return SettingsBlockedUsers.this.blockedContacts.isEmpty();
        }
    }

    class C08843 extends OnSwipeTouchListener {
        C08843() {
        }

        public void onSwipeRight() {
            SettingsBlockedUsers.this.finishFragment(true);
        }
    }

    class C08854 extends OnSwipeTouchListener {
        C08854() {
        }

        public void onSwipeRight() {
            SettingsBlockedUsers.this.finishFragment(true);
        }
    }

    class C08865 implements RPCRequestDelegate {

        class C05751 implements Runnable {
            C05751() {
            }

            public void run() {
                SettingsBlockedUsers.this.loading = false;
                if (SettingsBlockedUsers.this.progressView != null) {
                    SettingsBlockedUsers.this.progressView.setVisibility(8);
                }
                if (SettingsBlockedUsers.this.listView != null && SettingsBlockedUsers.this.listView.getEmptyView() == null) {
                    SettingsBlockedUsers.this.listView.setEmptyView(SettingsBlockedUsers.this.emptyView);
                }
                if (SettingsBlockedUsers.this.listViewAdapter != null) {
                    SettingsBlockedUsers.this.listViewAdapter.notifyDataSetChanged();
                }
            }
        }

        C08865() {
        }

        public void run(TLObject response, TL_error error) {
            if (error != null) {
                Utilities.RunOnUIThread(new C05751());
            }
            final contacts_Blocked res = (contacts_Blocked) response;
            Utilities.RunOnUIThread(new Runnable() {
                public void run() {
                    SettingsBlockedUsers.this.loading = false;
                    Iterator i$ = res.users.iterator();
                    while (i$.hasNext()) {
                        User user = (User) i$.next();
                        MessagesController.Instance.users.put(Integer.valueOf(user.id), user);
                    }
                    i$ = res.blocked.iterator();
                    while (i$.hasNext()) {
                        TL_contactBlocked blocked = (TL_contactBlocked) i$.next();
                        if (!SettingsBlockedUsers.this.blockedContactsDict.containsKey(Integer.valueOf(blocked.user_id))) {
                            SettingsBlockedUsers.this.blockedContacts.add(blocked);
                            SettingsBlockedUsers.this.blockedContactsDict.put(Integer.valueOf(blocked.user_id), blocked);
                        }
                    }
                    if (SettingsBlockedUsers.this.progressView != null) {
                        SettingsBlockedUsers.this.progressView.setVisibility(8);
                    }
                    if (SettingsBlockedUsers.this.listView != null && SettingsBlockedUsers.this.listView.getEmptyView() == null) {
                        SettingsBlockedUsers.this.listView.setEmptyView(SettingsBlockedUsers.this.emptyView);
                    }
                    if (SettingsBlockedUsers.this.listViewAdapter != null) {
                        SettingsBlockedUsers.this.listViewAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    class C08876 implements RPCRequestDelegate {
        C08876() {
        }

        public void run(TLObject response, TL_error error) {
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.Instance.addObserver(this, 3);
        loadBlockedContacts(0, 200);
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.Instance.removeObserver(this, 3);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.fragmentView == null) {
            this.fragmentView = inflater.inflate(C0419R.layout.settings_blocked_users_layout, container, false);
            this.listViewAdapter = new ListAdapter(this.parentActivity);
            this.listView = (ListView) this.fragmentView.findViewById(C0419R.id.listView);
            this.progressView = this.fragmentView.findViewById(C0419R.id.progressLayout);
            this.emptyView = this.fragmentView.findViewById(C0419R.id.searchEmptyView);
            if (this.loading) {
                this.progressView.setVisibility(0);
                this.emptyView.setVisibility(8);
                this.listView.setEmptyView(null);
            } else {
                this.progressView.setVisibility(8);
                this.listView.setEmptyView(this.emptyView);
            }
            this.listView.setAdapter(this.listViewAdapter);
            this.listView.setOnItemClickListener(new C05721());
            this.listView.setOnItemLongClickListener(new C05742());
            this.listView.setOnTouchListener(new C08843());
            this.emptyView.setOnTouchListener(new C08854());
        } else {
            ViewGroup parent = (ViewGroup) this.fragmentView.getParent();
            if (parent != null) {
                parent.removeView(this.fragmentView);
            }
        }
        return this.fragmentView;
    }

    private void loadBlockedContacts(int offset, int count) {
        if (!this.loading) {
            this.loading = true;
            TL_contacts_getBlocked req = new TL_contacts_getBlocked();
            req.offset = offset;
            req.limit = count;
            ConnectionsManager.Instance.bindRequestToGuid(Long.valueOf(ConnectionsManager.Instance.performRpc(req, new C08865(), null, true, RPCRequest.RPCRequestClassGeneric)), this.classGuid);
        }
    }

    public void didReceivedNotification(int id, Object... args) {
        if (id == 3) {
            int mask = ((Integer) args[0]).intValue();
            if ((mask & 2) != 0 || (mask & 1) != 0) {
                updateVisibleRows(mask);
            }
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
            actionBar.setTitle(getStringEntry(C0419R.string.BlockedUsers));
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
        if (!this.isFinish && getActivity() != null) {
            if (!(this.firstStart || this.listViewAdapter == null)) {
                this.listViewAdapter.notifyDataSetChanged();
            }
            this.firstStart = false;
            ((ApplicationActivity) this.parentActivity).showActionBar();
            ((ApplicationActivity) this.parentActivity).updateActionBar();
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(C0419R.menu.settings_block_users_bar_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finishFragment();
                break;
            case C0419R.id.block_user:
                ContactsActivity fragment = new ContactsActivity();
                fragment.animationType = 1;
                Bundle bundle = new Bundle();
                bundle.putBoolean("onlyUsers", true);
                bundle.putBoolean("destroyAfterSelect", true);
                bundle.putBoolean("usersAsSections", true);
                bundle.putBoolean("returnAsResult", true);
                fragment.delegate = this;
                fragment.setArguments(bundle);
                ((ApplicationActivity) this.parentActivity).presentFragment(fragment, "contacts_block", false);
                break;
        }
        return true;
    }

    public void didSelectContact(int user_id) {
        if (!this.blockedContactsDict.containsKey(Integer.valueOf(user_id))) {
            TL_contacts_block req = new TL_contacts_block();
            User user = (User) MessagesController.Instance.users.get(Integer.valueOf(this.selectedUserId));
            if ((user instanceof TL_userForeign) || (user instanceof TL_userRequest)) {
                req.id = new TL_inputUserForeign();
                req.id.access_hash = user.access_hash;
                req.id.user_id = user_id;
            } else {
                req.id = new TL_inputUserContact();
                req.id.user_id = user_id;
            }
            TL_contactBlocked blocked = new TL_contactBlocked();
            blocked.user_id = user_id;
            blocked.date = (int) (System.currentTimeMillis() / 1000);
            this.blockedContactsDict.put(Integer.valueOf(blocked.user_id), blocked);
            this.blockedContacts.add(blocked);
            this.listViewAdapter.notifyDataSetChanged();
            ConnectionsManager.Instance.performRpc(req, new C08876(), null, true, RPCRequest.RPCRequestClassGeneric);
        }
    }
}
