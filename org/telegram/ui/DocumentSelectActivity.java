package org.telegram.ui;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Views.BackupImageView;
import org.telegram.ui.Views.BaseFragment;
import org.telegram.ui.Views.OnSwipeTouchListener;

public class DocumentSelectActivity extends BaseFragment {
    private File currentDir;
    public DocumentSelectActivityDelegate delegate;
    private TextView emptyView;
    private ArrayList<HistoryEntry> history = new ArrayList();
    private ArrayList<ListItem> items = new ArrayList();
    private ListAdapter listAdapter;
    private ListView listView;
    private BroadcastReceiver receiver = new C04951();
    private boolean receiverRegistered = false;
    private long sizeLimit = 1073741824;

    class C04951 extends BroadcastReceiver {

        class C04941 implements Runnable {
            C04941() {
            }

            public void run() {
                if (DocumentSelectActivity.this.currentDir == null) {
                    DocumentSelectActivity.this.listRoots();
                } else {
                    DocumentSelectActivity.this.listFiles(DocumentSelectActivity.this.currentDir);
                }
            }
        }

        C04951() {
        }

        public void onReceive(Context arg0, Intent intent) {
            Runnable r = new C04941();
            if ("android.intent.action.MEDIA_UNMOUNTED".equals(intent.getAction())) {
                DocumentSelectActivity.this.listView.postDelayed(r, 1000);
            } else {
                r.run();
            }
        }
    }

    class C04962 implements OnItemClickListener {
        C04962() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ListItem item = (ListItem) DocumentSelectActivity.this.items.get(i);
            File file = item.file;
            if (file.isDirectory()) {
                HistoryEntry he = new HistoryEntry();
                he.scrollItem = DocumentSelectActivity.this.listView.getFirstVisiblePosition();
                he.scrollOffset = DocumentSelectActivity.this.listView.getChildAt(0).getTop();
                he.dir = DocumentSelectActivity.this.currentDir;
                ActionBar actionBar = DocumentSelectActivity.this.parentActivity.getSupportActionBar();
                he.title = actionBar.getTitle().toString();
                if (DocumentSelectActivity.this.listFiles(file)) {
                    DocumentSelectActivity.this.history.add(he);
                    actionBar.setTitle(item.title);
                    DocumentSelectActivity.this.listView.setSelection(0);
                }
            } else if (!file.canRead()) {
                DocumentSelectActivity.this.showErrorBox(DocumentSelectActivity.this.getString(C0419R.string.AccessError));
            } else if (DocumentSelectActivity.this.sizeLimit != 0 && file.length() > DocumentSelectActivity.this.sizeLimit) {
                DocumentSelectActivity.this.showErrorBox(DocumentSelectActivity.this.getString(C0419R.string.FileUploadLimit, Utilities.formatFileSize(DocumentSelectActivity.this.sizeLimit)));
            } else if (file.length() != 0 && DocumentSelectActivity.this.delegate != null) {
                DocumentSelectActivity.this.delegate.didSelectFile(DocumentSelectActivity.this, file.getAbsolutePath(), item.title, item.ext, file.length());
            }
        }
    }

    class C04974 implements Comparator<File> {
        C04974() {
        }

        public int compare(File lhs, File rhs) {
            if (lhs.isDirectory() != rhs.isDirectory()) {
                return lhs.isDirectory() ? -1 : 1;
            } else {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        }
    }

    public interface DocumentSelectActivityDelegate {
        void didSelectFile(DocumentSelectActivity documentSelectActivity, String str, String str2, String str3, long j);
    }

    private class HistoryEntry {
        File dir;
        int scrollItem;
        int scrollOffset;
        String title;

        private HistoryEntry() {
        }
    }

    private class ListAdapter extends BaseAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public int getCount() {
            return DocumentSelectActivity.this.items.size();
        }

        public Object getItem(int position) {
            return DocumentSelectActivity.this.items.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public int getViewTypeCount() {
            return 2;
        }

        public int getItemViewType(int pos) {
            return ((ListItem) DocumentSelectActivity.this.items.get(pos)).subtitle.length() > 0 ? 0 : 1;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ListItem item = (ListItem) DocumentSelectActivity.this.items.get(position);
            if (v == null) {
                v = View.inflate(this.mContext, C0419R.layout.document_item, null);
                if (item.subtitle.length() == 0) {
                    v.findViewById(C0419R.id.docs_item_info).setVisibility(8);
                }
            }
            TextView typeTextView = (TextView) v.findViewById(C0419R.id.docs_item_type);
            ((TextView) v.findViewById(C0419R.id.docs_item_title)).setText(item.title);
            ((TextView) v.findViewById(C0419R.id.docs_item_info)).setText(item.subtitle);
            BackupImageView imageView = (BackupImageView) v.findViewById(C0419R.id.docs_item_thumb);
            if (item.thumb != null) {
                imageView.setImageBitmap(null);
                typeTextView.setText(item.ext.toUpperCase().substring(0, Math.min(item.ext.length(), 4)));
                imageView.setImage(item.thumb, "55_42", 0);
                imageView.setScaleType(ScaleType.CENTER_CROP);
                imageView.setVisibility(0);
                typeTextView.setVisibility(0);
            } else if (item.icon != 0) {
                imageView.setImageResource(item.icon);
                imageView.setScaleType(ScaleType.CENTER);
                imageView.setVisibility(0);
                typeTextView.setVisibility(8);
            } else {
                typeTextView.setText(item.ext.toUpperCase().substring(0, Math.min(item.ext.length(), 4)));
                imageView.setVisibility(8);
                typeTextView.setVisibility(0);
            }
            return v;
        }
    }

    private class ListItem {
        String ext;
        File file;
        int icon;
        String subtitle;
        String thumb;
        String title;

        private ListItem() {
            this.subtitle = BuildConfig.FLAVOR;
            this.ext = BuildConfig.FLAVOR;
        }
    }

    class C08653 extends OnSwipeTouchListener {
        C08653() {
        }

        public void onSwipeRight() {
            DocumentSelectActivity.this.finishFragment(true);
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        return true;
    }

    public void onFragmentDestroy() {
        if (this.receiverRegistered) {
            this.parentActivity.unregisterReceiver(this.receiver);
        }
        super.onFragmentDestroy();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!this.receiverRegistered) {
            this.receiverRegistered = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
            filter.addAction("android.intent.action.MEDIA_CHECKING");
            filter.addAction("android.intent.action.MEDIA_EJECT");
            filter.addAction("android.intent.action.MEDIA_MOUNTED");
            filter.addAction("android.intent.action.MEDIA_NOFS");
            filter.addAction("android.intent.action.MEDIA_REMOVED");
            filter.addAction("android.intent.action.MEDIA_SHARED");
            filter.addAction("android.intent.action.MEDIA_UNMOUNTABLE");
            filter.addAction("android.intent.action.MEDIA_UNMOUNTED");
            filter.addDataScheme("file");
            this.parentActivity.registerReceiver(this.receiver, filter);
        }
        if (this.fragmentView == null) {
            this.fragmentView = inflater.inflate(C0419R.layout.document_select_layout, container, false);
            this.listAdapter = new ListAdapter(this.parentActivity);
            this.emptyView = (TextView) this.fragmentView.findViewById(C0419R.id.searchEmptyView);
            this.listView = (ListView) this.fragmentView.findViewById(C0419R.id.listView);
            this.listView.setEmptyView(this.emptyView);
            this.listView.setAdapter(this.listAdapter);
            this.listView.setOnItemClickListener(new C04962());
            this.listView.setOnTouchListener(new C08653());
            listRoots();
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
            actionBar.setSubtitle(null);
            actionBar.setCustomView(null);
            actionBar.setTitle(getStringEntry(C0419R.string.SelectFile));
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

    public boolean onBackPressed() {
        if (this.history.size() <= 0) {
            return super.onBackPressed();
        }
        HistoryEntry he = (HistoryEntry) this.history.remove(this.history.size() - 1);
        this.parentActivity.getSupportActionBar().setTitle(he.title);
        if (he.dir != null) {
            listFiles(he.dir);
        } else {
            listRoots();
        }
        this.listView.setSelectionFromTop(he.scrollItem, he.scrollOffset);
        return false;
    }

    private boolean listFiles(File dir) {
        if (dir.canRead()) {
            this.emptyView.setText(C0419R.string.NoFiles);
            try {
                File[] files = dir.listFiles();
                if (files == null) {
                    showErrorBox(getString(C0419R.string.UnknownError));
                    return false;
                }
                this.currentDir = dir;
                this.items.clear();
                Arrays.sort(files, new C04974());
                for (File file : files) {
                    if (!file.getName().startsWith(".")) {
                        ListItem item = new ListItem();
                        item.title = file.getName();
                        item.file = file;
                        if (file.isDirectory()) {
                            item.icon = C0419R.drawable.ic_directory;
                        } else {
                            String fname = file.getName();
                            String[] sp = fname.split("\\.");
                            item.ext = sp.length > 1 ? sp[sp.length - 1] : "?";
                            item.subtitle = Utilities.formatFileSize(file.length());
                            fname = fname.toLowerCase();
                            if (fname.endsWith(".jpg") || fname.endsWith(".png") || fname.endsWith(".gif") || fname.endsWith(".jpeg")) {
                                item.thumb = file.getAbsolutePath();
                            }
                        }
                        this.items.add(item);
                    }
                }
                this.listAdapter.notifyDataSetChanged();
                return true;
            } catch (Exception e) {
                showErrorBox(e.getLocalizedMessage());
                return false;
            }
        } else if ((!dir.getAbsolutePath().startsWith(Environment.getExternalStorageDirectory().toString()) && !dir.getAbsolutePath().startsWith("/sdcard") && !dir.getAbsolutePath().startsWith("/mnt/sdcard")) || Environment.getExternalStorageState().equals("mounted") || Environment.getExternalStorageState().equals("mounted_ro")) {
            showErrorBox(getString(C0419R.string.AccessError));
            return false;
        } else {
            this.currentDir = dir;
            this.items.clear();
            if ("shared".equals(Environment.getExternalStorageState())) {
                this.emptyView.setText(C0419R.string.UsbActive);
            } else {
                this.emptyView.setText(C0419R.string.NotMounted);
            }
            this.listAdapter.notifyDataSetChanged();
            return true;
        }
    }

    private void showErrorBox(String error) {
        new Builder(this.parentActivity).setTitle(C0419R.string.AppName).setMessage(error).setPositiveButton(C0419R.string.OK, null).show();
    }

    private void listRoots() {
        this.currentDir = null;
        this.items.clear();
        String extStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        DocumentSelectActivity documentSelectActivity = this;
        ListItem ext = new ListItem();
        int i = (VERSION.SDK_INT < 9 || Environment.isExternalStorageRemovable()) ? C0419R.string.SdCard : C0419R.string.InternalStorage;
        ext.title = getString(i);
        i = (VERSION.SDK_INT < 9 || Environment.isExternalStorageRemovable()) ? C0419R.drawable.ic_external_storage : C0419R.drawable.ic_storage;
        ext.icon = i;
        ext.subtitle = getRootSubtitle(extStorage);
        ext.file = Environment.getExternalStorageDirectory();
        this.items.add(ext);
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/mounts"));
            HashMap<String, ArrayList<String>> aliases = new HashMap();
            ArrayList<String> result = new ArrayList();
            String extDevice = null;
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                } else if (!((!line.contains("/mnt") && !line.contains("/storage") && !line.contains("/sdcard")) || line.contains("asec") || line.contains("tmpfs") || line.contains("none"))) {
                    String[] info = line.split(" ");
                    if (!aliases.containsKey(info[0])) {
                        aliases.put(info[0], new ArrayList());
                    }
                    ((ArrayList) aliases.get(info[0])).add(info[1]);
                    if (info[1].equals(extStorage)) {
                        extDevice = info[0];
                    }
                    result.add(info[1]);
                }
            }
            reader.close();
            if (extDevice != null) {
                result.removeAll((Collection) aliases.get(extDevice));
                Iterator i$ = result.iterator();
                while (i$.hasNext()) {
                    String path = (String) i$.next();
                    try {
                        boolean isSd = path.toLowerCase().contains("sd");
                        documentSelectActivity = this;
                        ListItem item = new ListItem();
                        item.title = getString(isSd ? C0419R.string.SdCard : C0419R.string.ExternalStorage);
                        item.icon = C0419R.drawable.ic_external_storage;
                        item.subtitle = getRootSubtitle(path);
                        item.file = new File(path);
                        this.items.add(item);
                    } catch (Exception e) {
                        FileLog.m799e("tmessages", e);
                    }
                }
            }
        } catch (Exception e2) {
            FileLog.m799e("tmessages", e2);
        }
        documentSelectActivity = this;
        ListItem fs = new ListItem();
        fs.title = "/";
        fs.subtitle = getString(C0419R.string.SystemRoot);
        fs.icon = C0419R.drawable.ic_directory;
        fs.file = new File("/");
        this.items.add(fs);
        this.listAdapter.notifyDataSetChanged();
    }

    private String getRootSubtitle(String path) {
        StatFs stat = new StatFs(path);
        long free = ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize());
        if (((long) stat.getBlockCount()) * ((long) stat.getBlockSize()) == 0) {
            return BuildConfig.FLAVOR;
        }
        return getString(C0419R.string.FreeOfTotal, Utilities.formatFileSize(free), Utilities.formatFileSize(((long) stat.getBlockCount()) * ((long) stat.getBlockSize())));
    }
}
