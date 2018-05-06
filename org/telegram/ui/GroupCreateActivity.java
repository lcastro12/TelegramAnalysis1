package org.telegram.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.TL.TLRPC.FileLocation;
import org.telegram.TL.TLRPC.TL_contact;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.Emoji.XImageSpan;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ContactsActivity.ContactListRowHolder;
import org.telegram.ui.Views.BaseFragment;
import org.telegram.ui.Views.PinnedHeaderListView;
import org.telegram.ui.Views.SectionedBaseAdapter;

public class GroupCreateActivity extends BaseFragment implements NotificationCenterDelegate {
    private ArrayList<XImageSpan> allSpans;
    private int beforeChangeIndex;
    private CharSequence changeString;
    private TextView countTextView;
    private TextView doneTextView;
    private TextView epmtyTextView;
    private boolean ignoreChange;
    private PinnedHeaderListView listView;
    private SectionedBaseAdapter listViewAdapter;
    private Timer searchDialogsTimer;
    public ArrayList<User> searchResult;
    public ArrayList<CharSequence> searchResultNames;
    private boolean searchWas;
    private boolean searching;
    private HashMap<Integer, XImageSpan> selectedContacts;
    private EditText userSelectEditText;

    class C05071 implements TextWatcher {
        C05071() {
        }

        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            if (!GroupCreateActivity.this.ignoreChange) {
                GroupCreateActivity.this.beforeChangeIndex = GroupCreateActivity.this.userSelectEditText.getSelectionStart();
                GroupCreateActivity.this.changeString = new SpannableString(charSequence);
            }
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void afterTextChanged(Editable editable) {
            if (!GroupCreateActivity.this.ignoreChange) {
                boolean search = false;
                int afterChangeIndex = GroupCreateActivity.this.userSelectEditText.getSelectionEnd();
                if (editable.toString().length() < GroupCreateActivity.this.changeString.toString().length()) {
                    String deletedString = BuildConfig.FLAVOR;
                    try {
                        deletedString = GroupCreateActivity.this.changeString.toString().substring(afterChangeIndex, GroupCreateActivity.this.beforeChangeIndex);
                    } catch (Exception e) {
                        FileLog.m799e("tmessages", e);
                    }
                    if (deletedString.length() > 0) {
                        if (GroupCreateActivity.this.searching && GroupCreateActivity.this.searchWas) {
                            search = true;
                        }
                        Spannable span = GroupCreateActivity.this.userSelectEditText.getText();
                        for (int a = 0; a < GroupCreateActivity.this.allSpans.size(); a++) {
                            XImageSpan sp = (XImageSpan) GroupCreateActivity.this.allSpans.get(a);
                            if (span.getSpanStart(sp) == -1) {
                                GroupCreateActivity.this.allSpans.remove(sp);
                                GroupCreateActivity.this.selectedContacts.remove(Integer.valueOf(sp.uid));
                            }
                        }
                        if (GroupCreateActivity.this.selectedContacts.isEmpty()) {
                            GroupCreateActivity.this.doneTextView.setText(GroupCreateActivity.this.getStringEntry(C0419R.string.Done));
                        } else {
                            GroupCreateActivity.this.doneTextView.setText(GroupCreateActivity.this.getStringEntry(C0419R.string.Done) + " (" + GroupCreateActivity.this.selectedContacts.size() + ")");
                        }
                        GroupCreateActivity.this.countTextView.setText(GroupCreateActivity.this.selectedContacts.size() + "/200");
                        GroupCreateActivity.this.listView.invalidateViews();
                    } else {
                        search = true;
                    }
                } else {
                    search = true;
                }
                if (search) {
                    String text = GroupCreateActivity.this.userSelectEditText.getText().toString().replace("<", BuildConfig.FLAVOR);
                    if (text.length() != 0) {
                        GroupCreateActivity.this.searchDialogs(text);
                        GroupCreateActivity.this.searching = true;
                        GroupCreateActivity.this.searchWas = true;
                        GroupCreateActivity.this.epmtyTextView.setText(GroupCreateActivity.this.getStringEntry(C0419R.string.NoResult));
                        GroupCreateActivity.this.listViewAdapter.notifyDataSetChanged();
                        return;
                    }
                    GroupCreateActivity.this.searchResult = null;
                    GroupCreateActivity.this.searchResultNames = null;
                    GroupCreateActivity.this.searching = false;
                    GroupCreateActivity.this.searchWas = false;
                    GroupCreateActivity.this.epmtyTextView.setText(GroupCreateActivity.this.getStringEntry(C0419R.string.NoContacts));
                    GroupCreateActivity.this.listViewAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    class C05082 implements OnItemClickListener {
        C05082() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            User user;
            int section = GroupCreateActivity.this.listViewAdapter.getSectionForPosition(i);
            int row = GroupCreateActivity.this.listViewAdapter.getPositionInSectionForPosition(i);
            if (GroupCreateActivity.this.searching && GroupCreateActivity.this.searchWas) {
                user = (User) GroupCreateActivity.this.searchResult.get(row);
            } else {
                user = (User) MessagesController.Instance.users.get(Integer.valueOf(((TL_contact) ((ArrayList) MessagesController.Instance.usersSectionsDict.get(MessagesController.Instance.sortedUsersSectionsArray.get(section))).get(row)).user_id));
                GroupCreateActivity.this.listView.invalidateViews();
            }
            if (GroupCreateActivity.this.selectedContacts.containsKey(Integer.valueOf(user.id))) {
                XImageSpan span = (XImageSpan) GroupCreateActivity.this.selectedContacts.get(Integer.valueOf(user.id));
                GroupCreateActivity.this.selectedContacts.remove(Integer.valueOf(user.id));
                SpannableStringBuilder text = new SpannableStringBuilder(GroupCreateActivity.this.userSelectEditText.getText());
                text.delete(text.getSpanStart(span), text.getSpanEnd(span));
                GroupCreateActivity.this.allSpans.remove(span);
                GroupCreateActivity.this.ignoreChange = true;
                GroupCreateActivity.this.userSelectEditText.setText(text);
                GroupCreateActivity.this.userSelectEditText.setSelection(text.length());
                GroupCreateActivity.this.ignoreChange = false;
            } else if (GroupCreateActivity.this.selectedContacts.size() != 200) {
                GroupCreateActivity.this.ignoreChange = true;
                GroupCreateActivity.this.createAndPutChipForUser(user).uid = user.id;
                GroupCreateActivity.this.ignoreChange = false;
            } else {
                return;
            }
            if (GroupCreateActivity.this.selectedContacts.isEmpty()) {
                GroupCreateActivity.this.doneTextView.setText(GroupCreateActivity.this.getStringEntry(C0419R.string.Done));
            } else {
                GroupCreateActivity.this.doneTextView.setText(GroupCreateActivity.this.getStringEntry(C0419R.string.Done) + " (" + GroupCreateActivity.this.selectedContacts.size() + ")");
            }
            GroupCreateActivity.this.countTextView.setText(GroupCreateActivity.this.selectedContacts.size() + "/200");
            if (GroupCreateActivity.this.searching || GroupCreateActivity.this.searchWas) {
                GroupCreateActivity.this.searching = false;
                GroupCreateActivity.this.searchWas = false;
                GroupCreateActivity.this.epmtyTextView.setText(GroupCreateActivity.this.getStringEntry(C0419R.string.NoContacts));
                GroupCreateActivity.this.ignoreChange = true;
                SpannableStringBuilder ssb = new SpannableStringBuilder(BuildConfig.FLAVOR);
                Iterator i$ = GroupCreateActivity.this.allSpans.iterator();
                while (i$.hasNext()) {
                    XImageSpan sp = (XImageSpan) i$.next();
                    ssb.append("<<");
                    ssb.setSpan(sp, ssb.length() - 2, ssb.length(), 33);
                }
                GroupCreateActivity.this.userSelectEditText.setText(ssb);
                GroupCreateActivity.this.userSelectEditText.setSelection(ssb.length());
                GroupCreateActivity.this.ignoreChange = false;
                GroupCreateActivity.this.listViewAdapter.notifyDataSetChanged();
                return;
            }
            GroupCreateActivity.this.listView.invalidateViews();
        }
    }

    class C05126 implements OnClickListener {
        C05126() {
        }

        public void onClick(View view) {
            if (!GroupCreateActivity.this.selectedContacts.isEmpty()) {
                Object result = new ArrayList();
                result.addAll(GroupCreateActivity.this.selectedContacts.keySet());
                NotificationCenter.Instance.addToMemCache(2, result);
                ((ApplicationActivity) GroupCreateActivity.this.parentActivity).presentFragment(new GroupCreateFinalActivity(), "group_craate_final", false);
            }
        }
    }

    class C05137 implements Runnable {
        C05137() {
        }

        public void run() {
            GroupCreateActivity.this.removeSelfFromStack();
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
            if (GroupCreateActivity.this.searching && GroupCreateActivity.this.searchWas) {
                return (GroupCreateActivity.this.searchResult == null || GroupCreateActivity.this.searchResult.isEmpty()) ? 0 : 1;
            } else {
                return MessagesController.Instance.sortedUsersSectionsArray.size();
            }
        }

        public int getCountForSection(int section) {
            if (GroupCreateActivity.this.searching && GroupCreateActivity.this.searchWas) {
                return GroupCreateActivity.this.searchResult == null ? 0 : GroupCreateActivity.this.searchResult.size();
            } else {
                return ((ArrayList) MessagesController.Instance.usersSectionsDict.get(MessagesController.Instance.sortedUsersSectionsArray.get(section))).size();
            }
        }

        public View getItemView(int section, int position, View convertView, ViewGroup parent) {
            User user;
            int size;
            if (GroupCreateActivity.this.searchWas && GroupCreateActivity.this.searching) {
                user = (User) MessagesController.Instance.users.get(Integer.valueOf(((User) GroupCreateActivity.this.searchResult.get(position)).id));
                size = GroupCreateActivity.this.searchResult.size();
            } else {
                ArrayList<TL_contact> arr = (ArrayList) MessagesController.Instance.usersSectionsDict.get(MessagesController.Instance.sortedUsersSectionsArray.get(section));
                user = (User) MessagesController.Instance.users.get(Integer.valueOf(((TL_contact) arr.get(position)).user_id));
                size = arr.size();
            }
            if (convertView == null) {
                convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.group_create_row_layout, parent, false);
            }
            ContactListRowHolder holder = (ContactListRowHolder) convertView.getTag();
            if (holder == null) {
                holder = new ContactListRowHolder(convertView);
                convertView.setTag(holder);
            }
            ImageView checkButton = (ImageView) convertView.findViewById(C0419R.id.settings_row_check_button);
            if (GroupCreateActivity.this.selectedContacts.containsKey(Integer.valueOf(user.id))) {
                checkButton.setImageResource(C0419R.drawable.btn_check_on_old);
            } else {
                checkButton.setImageResource(C0419R.drawable.btn_check_off_old);
            }
            View divider = convertView.findViewById(C0419R.id.settings_row_divider);
            if (position == size - 1) {
                divider.setVisibility(4);
            } else {
                divider.setVisibility(0);
            }
            if (GroupCreateActivity.this.searchWas && GroupCreateActivity.this.searching) {
                holder.nameTextView.setText((CharSequence) GroupCreateActivity.this.searchResultNames.get(position));
            } else if (user.first_name.length() != 0 && user.last_name.length() != 0) {
                holder.nameTextView.setText(Html.fromHtml(user.first_name + " <b>" + user.last_name + "</b>"));
            } else if (user.first_name.length() != 0) {
                holder.nameTextView.setText(Html.fromHtml("<b>" + user.first_name + "</b>"));
            } else {
                holder.nameTextView.setText(Html.fromHtml("<b>" + user.last_name + "</b>"));
            }
            FileLocation photo = null;
            if (user.photo != null) {
                photo = user.photo.photo_small;
            }
            holder.avatarImage.setImage(photo, "50_50", Utilities.getUserAvatarForId(user.id));
            if (user.status == null) {
                holder.messageTextView.setText(GroupCreateActivity.this.getStringEntry(C0419R.string.Offline));
                holder.messageTextView.setTextColor(-8355712);
            } else {
                int currentTime = ConnectionsManager.Instance.getCurrentTime();
                if (user.status.expires > currentTime || user.status.was_online > currentTime) {
                    holder.messageTextView.setTextColor(-13272408);
                    holder.messageTextView.setText(GroupCreateActivity.this.getStringEntry(C0419R.string.Online));
                } else {
                    if (user.status.was_online > FileLoader.FileDidUpload || user.status.expires > FileLoader.FileDidUpload) {
                        int value = user.status.was_online;
                        if (value == 0) {
                            value = user.status.expires;
                        }
                        holder.messageTextView.setText(GroupCreateActivity.this.getStringEntry(C0419R.string.LastSeen) + " " + Utilities.formatDateOnline((long) value));
                    } else {
                        holder.messageTextView.setText(GroupCreateActivity.this.getStringEntry(C0419R.string.Invisible));
                    }
                    holder.messageTextView.setTextColor(-8355712);
                }
            }
            return convertView;
        }

        public int getItemViewType(int section, int position) {
            return 0;
        }

        public int getItemViewTypeCount() {
            return 1;
        }

        public int getSectionHeaderViewType(int section) {
            return 0;
        }

        public int getSectionHeaderViewTypeCount() {
            return 1;
        }

        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_section_layout, parent, false);
                convertView.setBackgroundColor(-1);
            }
            TextView textView = (TextView) convertView.findViewById(C0419R.id.settings_section_text);
            if (GroupCreateActivity.this.searching && GroupCreateActivity.this.searchWas) {
                textView.setText(GroupCreateActivity.this.getStringEntry(C0419R.string.AllContacts));
            } else {
                textView.setText((CharSequence) MessagesController.Instance.sortedUsersSectionsArray.get(section));
            }
            return convertView;
        }
    }

    public GroupCreateActivity() {
        this.ignoreChange = false;
        this.selectedContacts = new HashMap();
        this.allSpans = new ArrayList();
        this.animationType = 1;
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.Instance.addObserver(this, 13);
        NotificationCenter.Instance.addObserver(this, 3);
        NotificationCenter.Instance.addObserver(this, 15);
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.Instance.removeObserver(this, 13);
        NotificationCenter.Instance.removeObserver(this, 3);
        NotificationCenter.Instance.removeObserver(this, 15);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.fragmentView == null) {
            this.fragmentView = inflater.inflate(C0419R.layout.group_create_layout, container, false);
            this.epmtyTextView = (TextView) this.fragmentView.findViewById(C0419R.id.searchEmptyView);
            this.userSelectEditText = (EditText) this.fragmentView.findViewById(C0419R.id.bubble_input_text);
            this.countTextView = (TextView) this.fragmentView.findViewById(C0419R.id.bubble_counter_text);
            if (VERSION.SDK_INT >= 11) {
                this.userSelectEditText.setTextIsSelectable(false);
            }
            this.userSelectEditText.addTextChangedListener(new C05071());
            this.listView = (PinnedHeaderListView) this.fragmentView.findViewById(C0419R.id.listView);
            this.listView.setEmptyView(this.epmtyTextView);
            this.listView.setVerticalScrollBarEnabled(false);
            PinnedHeaderListView pinnedHeaderListView = this.listView;
            android.widget.ListAdapter listAdapter = new ListAdapter(this.parentActivity);
            this.listViewAdapter = listAdapter;
            pinnedHeaderListView.setAdapter(listAdapter);
            this.listView.setOnItemClickListener(new C05082());
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
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setCustomView(null);
            actionBar.setTitle(getStringEntry(C0419R.string.NewGroup));
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
        if (getActivity() != null) {
            ((ApplicationActivity) this.parentActivity).showActionBar();
            ((ApplicationActivity) this.parentActivity).updateActionBar();
        }
    }

    public XImageSpan createAndPutChipForUser(User user) {
        View textView = ((LayoutInflater) this.parentActivity.getSystemService("layout_inflater")).inflate(C0419R.layout.group_create_bubble, null);
        ((TextView) textView.findViewById(C0419R.id.bubble_text_view)).setText(Utilities.formatName(user.first_name, user.last_name));
        int spec = MeasureSpec.makeMeasureSpec(0, 0);
        textView.measure(spec, spec);
        textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(textView.getWidth(), textView.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        canvas.translate((float) (-textView.getScrollX()), (float) (-textView.getScrollY()));
        textView.draw(canvas);
        textView.setDrawingCacheEnabled(true);
        Bitmap viewBmp = textView.getDrawingCache().copy(Config.ARGB_8888, true);
        textView.destroyDrawingCache();
        BitmapDrawable bmpDrawable = new BitmapDrawable(b);
        bmpDrawable.setBounds(0, 0, b.getWidth(), b.getHeight());
        SpannableStringBuilder ssb = new SpannableStringBuilder(BuildConfig.FLAVOR);
        XImageSpan span = new XImageSpan(bmpDrawable, 1);
        this.allSpans.add(span);
        this.selectedContacts.put(Integer.valueOf(user.id), span);
        Iterator i$ = this.allSpans.iterator();
        while (i$.hasNext()) {
            XImageSpan sp = (XImageSpan) i$.next();
            ssb.append("<<");
            ssb.setSpan(sp, ssb.length() - 2, ssb.length(), 33);
        }
        this.userSelectEditText.setText(ssb);
        this.userSelectEditText.setSelection(ssb.length());
        return span;
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
                    GroupCreateActivity.this.searchDialogsTimer.cancel();
                    GroupCreateActivity.this.searchDialogsTimer = null;
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
                GroupCreateActivity.this.processSearch(query);
            }
        }, 100, 300);
    }

    private void processSearch(final String query) {
        Utilities.globalQueue.postRunnable(new Runnable() {
            public void run() {
                if (query.length() == 0) {
                    GroupCreateActivity.this.updateSearchResults(new ArrayList(), new ArrayList());
                    return;
                }
                long time = System.currentTimeMillis();
                ArrayList<User> resultArray = new ArrayList();
                ArrayList<CharSequence> resultArrayNames = new ArrayList();
                String q = query.toLowerCase();
                Iterator i$ = MessagesController.Instance.contacts.iterator();
                while (i$.hasNext()) {
                    User user = (User) MessagesController.Instance.users.get(Integer.valueOf(((TL_contact) i$.next()).user_id));
                    if ((user.first_name.toLowerCase().startsWith(q) || user.last_name.toLowerCase().startsWith(q)) && user.id != UserConfig.clientUserId) {
                        resultArrayNames.add(Utilities.generateSearchName(user.first_name, user.last_name, q));
                        resultArray.add(user);
                    }
                }
                GroupCreateActivity.this.updateSearchResults(resultArray, resultArrayNames);
            }
        });
    }

    private void updateSearchResults(final ArrayList<User> users, final ArrayList<CharSequence> names) {
        Utilities.RunOnUIThread(new Runnable() {
            public void run() {
                GroupCreateActivity.this.searchResult = users;
                GroupCreateActivity.this.searchResultNames = names;
                GroupCreateActivity.this.listViewAdapter.notifyDataSetChanged();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finishFragment();
                break;
        }
        return true;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(C0419R.menu.group_create_menu, menu);
        this.doneTextView = (TextView) ((SupportMenuItem) menu.findItem(C0419R.id.done_menu_item)).getActionView().findViewById(C0419R.id.done_button);
        this.doneTextView.setOnClickListener(new C05126());
    }

    public void didReceivedNotification(int id, Object... args) {
        if (id == 13) {
            if (this.listViewAdapter != null) {
                this.listViewAdapter.notifyDataSetChanged();
            }
        } else if (id == 3) {
            int mask = ((Integer) args[0]).intValue();
            if (((mask & 2) != 0 || (mask & 1) != 0 || (mask & 4) != 0) && this.listView != null) {
                this.listView.invalidateViews();
            }
        } else if (id == 15) {
            Utilities.RunOnUIThread(new C05137());
        }
    }
}
