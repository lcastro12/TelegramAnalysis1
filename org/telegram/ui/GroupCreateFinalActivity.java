package org.telegram.ui;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.TL.TLRPC.FileLocation;
import org.telegram.TL.TLRPC.InputFile;
import org.telegram.TL.TLRPC.PhotoSize;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Cells.ChatOrUserCell;
import org.telegram.ui.Views.AvatarUpdater;
import org.telegram.ui.Views.AvatarUpdater.AvatarUpdaterDelegate;
import org.telegram.ui.Views.BackupImageView;
import org.telegram.ui.Views.BaseFragment;
import org.telegram.ui.Views.PinnedHeaderListView;
import org.telegram.ui.Views.SectionedBaseAdapter;

public class GroupCreateFinalActivity extends BaseFragment implements NotificationCenterDelegate, AvatarUpdaterDelegate {
    private FileLocation avatar;
    private BackupImageView avatarImage;
    private AvatarUpdater avatarUpdater = new AvatarUpdater();
    private boolean createAfterUpload;
    private boolean donePressed;
    private PinnedHeaderListView listView;
    private TextView nameTextView;
    private ArrayList<Integer> selectedContacts;
    private InputFile uploadedAvatar;

    class C05151 implements OnClickListener {

        class C05141 implements DialogInterface.OnClickListener {
            C05141() {
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    GroupCreateFinalActivity.this.avatarUpdater.openCamera();
                } else if (i == 1) {
                    GroupCreateFinalActivity.this.avatarUpdater.openGallery();
                } else if (i == 2) {
                    GroupCreateFinalActivity.this.avatar = null;
                    GroupCreateFinalActivity.this.uploadedAvatar = null;
                    GroupCreateFinalActivity.this.avatarImage.setImage(GroupCreateFinalActivity.this.avatar, "50_50", (int) C0419R.drawable.group_blue);
                }
            }
        }

        C05151() {
        }

        public void onClick(View view) {
            Builder builder = new Builder(GroupCreateFinalActivity.this.parentActivity);
            builder.setItems(GroupCreateFinalActivity.this.avatar != null ? new CharSequence[]{GroupCreateFinalActivity.this.getStringEntry(C0419R.string.FromCamera), GroupCreateFinalActivity.this.getStringEntry(C0419R.string.FromGalley), GroupCreateFinalActivity.this.getStringEntry(C0419R.string.DeletePhoto)} : new CharSequence[]{GroupCreateFinalActivity.this.getStringEntry(C0419R.string.FromCamera), GroupCreateFinalActivity.this.getStringEntry(C0419R.string.FromGalley)}, new C05141());
            builder.show().setCanceledOnTouchOutside(true);
        }
    }

    class C05173 implements OnClickListener {
        C05173() {
        }

        public void onClick(View view) {
            if (!GroupCreateFinalActivity.this.donePressed && GroupCreateFinalActivity.this.nameTextView.getText().length() != 0) {
                GroupCreateFinalActivity.this.donePressed = true;
                Utilities.ShowProgressDialog(GroupCreateFinalActivity.this.parentActivity, GroupCreateFinalActivity.this.getStringEntry(C0419R.string.Loading));
                if (GroupCreateFinalActivity.this.avatarUpdater.uploadingAvatar != null) {
                    GroupCreateFinalActivity.this.createAfterUpload = true;
                } else {
                    MessagesController.Instance.createChat(GroupCreateFinalActivity.this.nameTextView.getText().toString(), GroupCreateFinalActivity.this.selectedContacts, GroupCreateFinalActivity.this.uploadedAvatar);
                }
            }
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

        public boolean areAllItemsEnabled() {
            return false;
        }

        public boolean isEnabled(int position) {
            return false;
        }

        public long getItemId(int section, int position) {
            return 0;
        }

        public int getSectionCount() {
            return 1;
        }

        public int getCountForSection(int section) {
            if (GroupCreateFinalActivity.this.selectedContacts == null) {
                return 0;
            }
            return GroupCreateFinalActivity.this.selectedContacts.size();
        }

        public View getItemView(int section, int position, View convertView, ViewGroup parent) {
            boolean z;
            User user = (User) MessagesController.Instance.users.get(GroupCreateFinalActivity.this.selectedContacts.get(position));
            if (convertView == null) {
                convertView = new ChatOrUserCell(this.mContext);
                ((ChatOrUserCell) convertView).useBoldFont = true;
                ((ChatOrUserCell) convertView).usePadding = false;
            }
            ((ChatOrUserCell) convertView).setData(user, null, null, null, null);
            ChatOrUserCell chatOrUserCell = (ChatOrUserCell) convertView;
            if (position != GroupCreateFinalActivity.this.selectedContacts.size() - 1) {
                z = true;
            } else {
                z = false;
            }
            chatOrUserCell.useSeparator = z;
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
            if (GroupCreateFinalActivity.this.selectedContacts.size() == 1) {
                textView.setText(GroupCreateFinalActivity.this.selectedContacts.size() + " " + GroupCreateFinalActivity.this.getStringEntry(C0419R.string.MEMBER));
            } else {
                textView.setText(GroupCreateFinalActivity.this.selectedContacts.size() + " " + GroupCreateFinalActivity.this.getStringEntry(C0419R.string.MEMBERS));
            }
            return convertView;
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.Instance.addObserver(this, 3);
        NotificationCenter.Instance.addObserver(this, 15);
        NotificationCenter.Instance.addObserver(this, 16);
        this.avatarUpdater.parentFragment = this;
        this.avatarUpdater.delegate = this;
        this.selectedContacts = (ArrayList) NotificationCenter.Instance.getFromMemCache(2);
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.Instance.removeObserver(this, 3);
        NotificationCenter.Instance.removeObserver(this, 15);
        NotificationCenter.Instance.removeObserver(this, 16);
        this.avatarUpdater.clear();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.fragmentView == null) {
            this.fragmentView = inflater.inflate(C0419R.layout.group_create_final_layout, container, false);
            ((ImageButton) this.fragmentView.findViewById(C0419R.id.settings_change_avatar_button)).setOnClickListener(new C05151());
            this.avatarImage = (BackupImageView) this.fragmentView.findViewById(C0419R.id.settings_avatar_image);
            this.nameTextView = (EditText) this.fragmentView.findViewById(C0419R.id.bubble_input_text);
            this.listView = (PinnedHeaderListView) this.fragmentView.findViewById(C0419R.id.listView);
            this.listView.setAdapter(new ListAdapter(this.parentActivity));
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

    public void didUploadedPhoto(final InputFile file, final PhotoSize small, PhotoSize big) {
        Utilities.RunOnUIThread(new Runnable() {
            public void run() {
                GroupCreateFinalActivity.this.uploadedAvatar = file;
                GroupCreateFinalActivity.this.avatar = small.location;
                GroupCreateFinalActivity.this.avatarImage.setImage(GroupCreateFinalActivity.this.avatar, "50_50", (int) C0419R.drawable.group_blue);
                if (GroupCreateFinalActivity.this.createAfterUpload) {
                    FileLog.m800e("tmessages", "avatar did uploaded");
                    MessagesController.Instance.createChat(GroupCreateFinalActivity.this.nameTextView.getText().toString(), GroupCreateFinalActivity.this.selectedContacts, GroupCreateFinalActivity.this.uploadedAvatar);
                }
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.avatarUpdater.onActivityResult(requestCode, resultCode, data);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(C0419R.menu.group_create_menu, menu);
        ((TextView) ((SupportMenuItem) menu.findItem(C0419R.id.done_menu_item)).getActionView().findViewById(C0419R.id.done_button)).setOnClickListener(new C05173());
    }

    public void didReceivedNotification(int id, final Object... args) {
        if (id == 3) {
            int mask = ((Integer) args[0]).intValue();
            if ((mask & 2) != 0 || (mask & 1) != 0 || (mask & 4) != 0) {
                updateVisibleRows(mask);
            }
        } else if (id == 16) {
            Utilities.HideProgressDialog(this.parentActivity);
            this.donePressed = false;
            FileLog.m800e("tmessages", "did fail create chat");
        } else if (id == 15) {
            Utilities.RunOnUIThread(new Runnable() {
                public void run() {
                    Utilities.HideProgressDialog(GroupCreateFinalActivity.this.parentActivity);
                    ChatActivity fragment = new ChatActivity();
                    Bundle bundle = new Bundle();
                    bundle.putInt("chat_id", ((Integer) args[0]).intValue());
                    fragment.setArguments(bundle);
                    ((ApplicationActivity) GroupCreateFinalActivity.this.parentActivity).presentFragment(fragment, "chat" + Math.random(), true, false);
                }
            });
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
