package org.telegram.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.telegram.TL.TLRPC.PhotoSize;
import org.telegram.TL.TLRPC.TL_messageMediaPhoto;
import org.telegram.TL.TLRPC.TL_messageMediaVideo;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.Utilities;
import org.telegram.objects.MessageObject;
import org.telegram.ui.Views.BackupImageView;
import org.telegram.ui.Views.BaseFragment;
import org.telegram.ui.Views.OnSwipeTouchListener;

public class MediaActivity extends BaseFragment implements NotificationCenterDelegate {
    private boolean cacheEndReached = false;
    private long dialog_id;
    private View emptyView;
    private boolean endReached = false;
    private int itemWidth = 100;
    private ListAdapter listAdapter;
    private GridView listView;
    private boolean loading = false;
    private int max_id;
    private ArrayList<MessageObject> messages = new ArrayList();
    private HashMap<Integer, MessageObject> messagesDict = new HashMap();
    private int orientation = 0;
    private View progressView;
    private int totalCount = 0;

    class C05511 implements OnItemClickListener {
        C05511() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            NotificationCenter.Instance.addToMemCache(54, MediaActivity.this.messages);
            NotificationCenter.Instance.addToMemCache(55, Integer.valueOf(i));
            MediaActivity.this.startActivity(new Intent(MediaActivity.this.parentActivity, GalleryImageViewer.class));
        }
    }

    class C05522 implements OnScrollListener {
        C05522() {
        }

        public void onScrollStateChanged(AbsListView absListView, int i) {
        }

        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            boolean z = true;
            if (visibleItemCount != 0 && firstVisibleItem + visibleItemCount > totalItemCount - 2 && !MediaActivity.this.loading && !MediaActivity.this.endReached) {
                MediaActivity.this.loading = true;
                MessagesController messagesController = MessagesController.Instance;
                long access$300 = MediaActivity.this.dialog_id;
                int access$400 = MediaActivity.this.max_id;
                if (MediaActivity.this.cacheEndReached) {
                    z = false;
                }
                messagesController.loadMedia(access$300, 0, 50, access$400, z, MediaActivity.this.classGuid);
            }
        }
    }

    class C05535 implements OnPreDrawListener {
        C05535() {
        }

        public boolean onPreDraw() {
            int rotation = ((WindowManager) MediaActivity.this.parentActivity.getSystemService("window")).getDefaultDisplay().getRotation();
            if (rotation == 3 || rotation == 1) {
                MediaActivity.this.orientation = 1;
                MediaActivity.this.listView.setNumColumns(6);
                MediaActivity.this.itemWidth = (MediaActivity.this.getResources().getDisplayMetrics().widthPixels / 6) - (Utilities.dp(2) * 5);
                MediaActivity.this.listView.setColumnWidth(MediaActivity.this.itemWidth);
            } else {
                MediaActivity.this.orientation = 0;
                MediaActivity.this.listView.setNumColumns(4);
                MediaActivity.this.itemWidth = (MediaActivity.this.getResources().getDisplayMetrics().widthPixels / 4) - (Utilities.dp(2) * 3);
                MediaActivity.this.listView.setColumnWidth(MediaActivity.this.itemWidth);
            }
            MediaActivity.this.listView.setPadding(MediaActivity.this.listView.getPaddingLeft(), Utilities.dp(4), MediaActivity.this.listView.getPaddingRight(), MediaActivity.this.listView.getPaddingBottom());
            MediaActivity.this.listAdapter.notifyDataSetChanged();
            MediaActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
            return false;
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
            return i != MediaActivity.this.messages.size();
        }

        public int getCount() {
            int size = MediaActivity.this.messages.size();
            int i = (MediaActivity.this.messages.isEmpty() || MediaActivity.this.endReached) ? 0 : 1;
            return i + size;
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
            MessageObject message;
            LayoutParams params;
            BackupImageView imageView;
            if (type == 0) {
                message = (MessageObject) MediaActivity.this.messages.get(i);
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.media_photo_layout, viewGroup, false);
                }
                params = view.getLayoutParams();
                params.width = MediaActivity.this.itemWidth;
                params.height = MediaActivity.this.itemWidth;
                view.setLayoutParams(params);
                imageView = (BackupImageView) view.findViewById(C0419R.id.media_photo_image);
                if (message.messageOwner.media == null || message.messageOwner.media.photo == null || message.messageOwner.media.photo.sizes.isEmpty()) {
                    imageView.setImageResource(C0419R.drawable.photo_placeholder_in);
                } else {
                    ArrayList<PhotoSize> sizes = message.messageOwner.media.photo.sizes;
                    if (!false) {
                        if (message.imagePreview != null) {
                            imageView.setImageBitmap(message.imagePreview);
                        } else {
                            imageView.setImage(((PhotoSize) message.messageOwner.media.photo.sizes.get(0)).location, null, (int) C0419R.drawable.photo_placeholder_in);
                        }
                    }
                }
            } else if (type == 1) {
                message = (MessageObject) MediaActivity.this.messages.get(i);
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.media_video_layout, viewGroup, false);
                }
                params = view.getLayoutParams();
                params.width = MediaActivity.this.itemWidth;
                params.height = MediaActivity.this.itemWidth;
                view.setLayoutParams(params);
                TextView textView = (TextView) view.findViewById(C0419R.id.chat_video_time);
                imageView = (BackupImageView) view.findViewById(C0419R.id.media_photo_image);
                if (message.messageOwner.media.video == null || message.messageOwner.media.video.thumb == null) {
                    textView.setVisibility(8);
                    imageView.setImageResource(C0419R.drawable.photo_placeholder_in);
                } else {
                    int duration = message.messageOwner.media.video.duration;
                    int seconds = duration - ((duration / 60) * 60);
                    textView.setText(String.format("%d:%02d", new Object[]{Integer.valueOf(duration / 60), Integer.valueOf(seconds)}));
                    if (message.imagePreview != null) {
                        imageView.setImageBitmap(message.imagePreview);
                    } else {
                        imageView.setImage(message.messageOwner.media.video.thumb.location, null, (int) C0419R.drawable.photo_placeholder_in);
                    }
                    textView.setVisibility(0);
                }
            } else if (type == 2) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.media_loading_layout, viewGroup, false);
                }
                params = view.getLayoutParams();
                params.width = MediaActivity.this.itemWidth;
                params.height = MediaActivity.this.itemWidth;
                view.setLayoutParams(params);
            }
            return view;
        }

        public int getItemViewType(int i) {
            if (i == MediaActivity.this.messages.size()) {
                return 2;
            }
            if (((MessageObject) MediaActivity.this.messages.get(i)).messageOwner.media instanceof TL_messageMediaVideo) {
                return 1;
            }
            return 0;
        }

        public int getViewTypeCount() {
            return 3;
        }

        public boolean isEmpty() {
            return MediaActivity.this.messages.isEmpty();
        }
    }

    class C08743 extends OnSwipeTouchListener {
        C08743() {
        }

        public void onSwipeRight() {
            MediaActivity.this.finishFragment(true);
        }
    }

    class C08754 extends OnSwipeTouchListener {
        C08754() {
        }

        public void onSwipeRight() {
            MediaActivity.this.finishFragment(true);
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.Instance.addObserver(this, 18);
        NotificationCenter.Instance.addObserver(this, 6);
        NotificationCenter.Instance.addObserver(this, 1);
        NotificationCenter.Instance.addObserver(this, 10);
        this.dialog_id = getArguments().getLong("dialog_id", 0);
        this.loading = true;
        MessagesController.Instance.loadMedia(this.dialog_id, 0, 50, 0, true, this.classGuid);
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.Instance.removeObserver(this, 18);
        NotificationCenter.Instance.removeObserver(this, 1);
        NotificationCenter.Instance.removeObserver(this, 6);
        NotificationCenter.Instance.removeObserver(this, 10);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.fragmentView == null) {
            this.fragmentView = inflater.inflate(C0419R.layout.media_layout, container, false);
            this.emptyView = this.fragmentView.findViewById(C0419R.id.searchEmptyView);
            this.listView = (GridView) this.fragmentView.findViewById(C0419R.id.media_grid);
            this.progressView = this.fragmentView.findViewById(C0419R.id.progressLayout);
            GridView gridView = this.listView;
            android.widget.ListAdapter listAdapter = new ListAdapter(this.parentActivity);
            this.listAdapter = listAdapter;
            gridView.setAdapter(listAdapter);
            this.listView.setOnItemClickListener(new C05511());
            if (this.loading && this.messages.isEmpty()) {
                this.progressView.setVisibility(0);
                this.listView.setEmptyView(null);
            } else {
                this.progressView.setVisibility(8);
                this.listView.setEmptyView(this.emptyView);
            }
            this.listView.setOnScrollListener(new C05522());
            this.listView.setOnTouchListener(new C08743());
            this.emptyView.setOnTouchListener(new C08754());
        } else {
            ViewGroup parent = (ViewGroup) this.fragmentView.getParent();
            if (parent != null) {
                parent.removeView(this.fragmentView);
            }
        }
        return this.fragmentView;
    }

    public void didReceivedNotification(int id, Object... args) {
        Iterator i$;
        if (id == 18) {
            long uid = ((Long) args[0]).longValue();
            int guid = ((Integer) args[4]).intValue();
            if (uid == this.dialog_id && guid == this.classGuid) {
                this.loading = false;
                this.totalCount = ((Integer) args[1]).intValue();
                boolean added = false;
                i$ = args[2].iterator();
                while (i$.hasNext()) {
                    MessageObject message = (MessageObject) i$.next();
                    if (!this.messagesDict.containsKey(Integer.valueOf(message.messageOwner.id))) {
                        if (this.max_id == 0 || message.messageOwner.id < this.max_id) {
                            this.max_id = message.messageOwner.id;
                        }
                        this.messagesDict.put(Integer.valueOf(message.messageOwner.id), message);
                        this.messages.add(message);
                        added = true;
                    }
                }
                if (!added) {
                    this.endReached = true;
                }
                this.cacheEndReached = !((Boolean) args[3]).booleanValue();
                if (this.progressView != null) {
                    this.progressView.setVisibility(8);
                }
                if (this.listView != null && this.listView.getEmptyView() == null) {
                    this.listView.setEmptyView(this.emptyView);
                }
                if (this.listAdapter != null) {
                    this.listAdapter.notifyDataSetChanged();
                }
            }
        } else if (id == 6) {
            boolean updated = false;
            i$ = args[0].iterator();
            while (i$.hasNext()) {
                Integer ids = (Integer) i$.next();
                obj = (MessageObject) this.messagesDict.get(ids);
                if (obj != null) {
                    this.messages.remove(obj);
                    this.messagesDict.remove(ids);
                    this.totalCount--;
                    updated = true;
                }
            }
            if (updated && this.listAdapter != null) {
                this.listAdapter.notifyDataSetChanged();
            }
        } else if (id == 1) {
            if (((Long) args[0]).longValue() == this.dialog_id) {
                i$ = ((ArrayList) args[1]).iterator();
                while (i$.hasNext()) {
                    obj = (MessageObject) i$.next();
                    if (obj.messageOwner.media != null && (((obj.messageOwner.media instanceof TL_messageMediaPhoto) || (obj.messageOwner.media instanceof TL_messageMediaVideo)) && !this.messagesDict.containsKey(Integer.valueOf(obj.messageOwner.id)))) {
                        if ((this.max_id == 0 || obj.messageOwner.id < this.max_id) && obj.messageOwner.id > 0) {
                            this.max_id = obj.messageOwner.id;
                        }
                        this.messagesDict.put(Integer.valueOf(obj.messageOwner.id), obj);
                        this.messages.add(0, obj);
                    }
                }
                if (this.listAdapter != null) {
                    this.listAdapter.notifyDataSetChanged();
                }
            }
        } else if (id == 10) {
            Integer msgId = args[0];
            obj = (MessageObject) this.messagesDict.get(msgId);
            if (obj != null) {
                Integer newMsgId = args[1];
                this.messagesDict.remove(msgId);
                this.messagesDict.put(newMsgId, obj);
                obj.messageOwner.id = newMsgId.intValue();
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
            actionBar.setCustomView(null);
            actionBar.setTitle(getStringEntry(C0419R.string.SharedMedia));
            actionBar.setSubtitle(null);
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
            this.listView.getViewTreeObserver().addOnPreDrawListener(new C05535());
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                if (VERSION.SDK_INT < 11) {
                    this.listView.setAdapter(null);
                    this.listView = null;
                    this.listAdapter = null;
                }
                finishFragment();
                break;
        }
        return true;
    }
}
