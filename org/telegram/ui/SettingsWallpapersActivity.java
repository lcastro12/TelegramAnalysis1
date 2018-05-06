package org.telegram.ui;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.PhotoSize;
import org.telegram.TL.TLRPC.TL_account_getWallPapers;
import org.telegram.TL.TLRPC.TL_error;
import org.telegram.TL.TLRPC.TL_wallPaper;
import org.telegram.TL.TLRPC.TL_wallPaperSolid;
import org.telegram.TL.TLRPC.Vector;
import org.telegram.TL.TLRPC.WallPaper;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.RPCRequest;
import org.telegram.messenger.RPCRequest.RPCRequestDelegate;
import org.telegram.messenger.Utilities;
import org.telegram.objects.PhotoObject;
import org.telegram.ui.Views.BackupImageView;
import org.telegram.ui.Views.BaseFragment;
import org.telegram.ui.Views.HorizontalListView;

public class SettingsWallpapersActivity extends BaseFragment implements NotificationCenterDelegate {
    private ImageView backgroundImage;
    private String currentPicturePath;
    private View doneButton;
    private ListAdapter listAdapter;
    private HorizontalListView listView;
    private String loadingFile = null;
    private File loadingFileObject = null;
    private PhotoSize loadingSize = null;
    private ProgressBar progressBar;
    private int selectedBackground;
    private int selectedColor;
    private ArrayList<WallPaper> wallPapers = new ArrayList();
    private HashMap<Integer, WallPaper> wallpappersByIds = new HashMap();

    class C05851 implements OnItemClickListener {

        class C05841 implements OnClickListener {
            C05841() {
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                    File image = Utilities.generatePicturePath();
                    if (image != null) {
                        takePictureIntent.putExtra("output", Uri.fromFile(image));
                        SettingsWallpapersActivity.this.currentPicturePath = image.getAbsolutePath();
                    }
                    SettingsWallpapersActivity.this.startActivityForResult(takePictureIntent, 0);
                } else if (i == 1) {
                    Intent photoPickerIntent = new Intent("android.intent.action.PICK");
                    photoPickerIntent.setType("image/*");
                    SettingsWallpapersActivity.this.startActivityForResult(photoPickerIntent, 1);
                }
            }
        }

        C05851() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (i == 0) {
                Builder builder = new Builder(SettingsWallpapersActivity.this.parentActivity);
                builder.setItems(new CharSequence[]{SettingsWallpapersActivity.this.getStringEntry(C0419R.string.FromCamera), SettingsWallpapersActivity.this.getStringEntry(C0419R.string.FromGalley), SettingsWallpapersActivity.this.getStringEntry(C0419R.string.Cancel)}, new C05841());
                builder.show().setCanceledOnTouchOutside(true);
                return;
            }
            SettingsWallpapersActivity.this.selectedBackground = ((WallPaper) SettingsWallpapersActivity.this.wallPapers.get(i - 1)).id;
            SettingsWallpapersActivity.this.listAdapter.notifyDataSetChanged();
            SettingsWallpapersActivity.this.processSelectedBackground();
        }
    }

    class C05862 implements View.OnClickListener {
        C05862() {
        }

        public void onClick(View view) {
            SettingsWallpapersActivity.this.finishFragment();
        }
    }

    class C05873 implements View.OnClickListener {
        C05873() {
        }

        public void onClick(View view) {
            boolean done;
            WallPaper wallPaper = (WallPaper) SettingsWallpapersActivity.this.wallpappersByIds.get(Integer.valueOf(SettingsWallpapersActivity.this.selectedBackground));
            if (wallPaper == null || wallPaper.id == 1000001 || !(wallPaper instanceof TL_wallPaper)) {
                done = true;
            } else {
                PhotoSize size = PhotoObject.getClosestPhotoSizeWithSize(wallPaper.sizes, Utilities.dp(320), Utilities.dp(480));
                try {
                    done = Utilities.copyFile(new File(Utilities.getCacheDir(), size.location.volume_id + "_" + size.location.local_id + ".jpg"), new File(ApplicationLoader.applicationContext.getFilesDir(), "wallpaper.jpg"));
                } catch (Exception e) {
                    done = false;
                    FileLog.m799e("tmessages", e);
                }
            }
            if (done) {
                Editor editor = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit();
                editor.putInt("selectedBackground", SettingsWallpapersActivity.this.selectedBackground);
                editor.putInt("selectedColor", SettingsWallpapersActivity.this.selectedColor);
                editor.commit();
                ApplicationLoader.cachedWallpaper = null;
            }
            SettingsWallpapersActivity.this.finishFragment();
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
            return true;
        }

        public int getCount() {
            return SettingsWallpapersActivity.this.wallPapers.size() + 1;
        }

        public Object getItem(int i) {
            return null;
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public boolean hasStableIds() {
            return true;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            int type = getItemViewType(i);
            View selection;
            WallPaper wallPaper;
            if (type == 0) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_wallpapers_my_row, viewGroup, false);
                }
                View parentView = view.findViewById(C0419R.id.parent);
                ImageView imageView = (ImageView) view.findViewById(C0419R.id.image);
                selection = view.findViewById(C0419R.id.selection);
                if (i == 0) {
                    if (SettingsWallpapersActivity.this.selectedBackground == -1 || SettingsWallpapersActivity.this.selectedColor != 0 || SettingsWallpapersActivity.this.selectedBackground == 1000001) {
                        imageView.setBackgroundColor(1514625126);
                    } else {
                        imageView.setBackgroundColor(1509949440);
                    }
                    imageView.setImageResource(C0419R.drawable.ic_gallery_background);
                    if (SettingsWallpapersActivity.this.selectedBackground == -1) {
                        selection.setVisibility(0);
                    } else {
                        selection.setVisibility(4);
                    }
                } else {
                    imageView.setImageBitmap(null);
                    wallPaper = (WallPaper) SettingsWallpapersActivity.this.wallPapers.get(i - 1);
                    imageView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK | wallPaper.bg_color);
                    if (wallPaper.id == SettingsWallpapersActivity.this.selectedBackground) {
                        selection.setVisibility(0);
                    } else {
                        selection.setVisibility(4);
                    }
                }
            } else if (type == 1) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_wallpapers_other_row, viewGroup, false);
                }
                BackupImageView image = (BackupImageView) view.findViewById(C0419R.id.image);
                selection = view.findViewById(C0419R.id.selection);
                wallPaper = (WallPaper) SettingsWallpapersActivity.this.wallPapers.get(i - 1);
                image.setImage(PhotoObject.getClosestPhotoSizeWithSize(wallPaper.sizes, Utilities.dp(100), Utilities.dp(100)).location, "100_100", 0);
                if (wallPaper.id == SettingsWallpapersActivity.this.selectedBackground) {
                    selection.setVisibility(0);
                } else {
                    selection.setVisibility(4);
                }
            }
            return view;
        }

        public int getItemViewType(int i) {
            if (i == 0 || (((WallPaper) SettingsWallpapersActivity.this.wallPapers.get(i - 1)) instanceof TL_wallPaperSolid)) {
                return 0;
            }
            return 1;
        }

        public int getViewTypeCount() {
            return 2;
        }

        public boolean isEmpty() {
            return false;
        }
    }

    class C08915 implements RPCRequestDelegate {
        C08915() {
        }

        public void run(final TLObject response, TL_error error) {
            Utilities.RunOnUIThread(new Runnable() {
                public void run() {
                    SettingsWallpapersActivity.this.wallPapers.clear();
                    Vector res = response;
                    SettingsWallpapersActivity.this.wallpappersByIds.clear();
                    Iterator i$ = res.objects.iterator();
                    while (i$.hasNext()) {
                        Object obj = i$.next();
                        SettingsWallpapersActivity.this.wallPapers.add((WallPaper) obj);
                        SettingsWallpapersActivity.this.wallpappersByIds.put(Integer.valueOf(((WallPaper) obj).id), (WallPaper) obj);
                    }
                    SettingsWallpapersActivity.this.listAdapter.notifyDataSetChanged();
                    if (SettingsWallpapersActivity.this.backgroundImage != null) {
                        SettingsWallpapersActivity.this.processSelectedBackground();
                    }
                    MessagesStorage.Instance.putWallpapers(SettingsWallpapersActivity.this.wallPapers);
                }
            });
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.Instance.addObserver(this, 10005);
        NotificationCenter.Instance.addObserver(this, 10004);
        NotificationCenter.Instance.addObserver(this, 10003);
        NotificationCenter.Instance.addObserver(this, MessagesStorage.wallpapersDidLoaded);
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
        this.selectedBackground = preferences.getInt("selectedBackground", 1000001);
        this.selectedColor = preferences.getInt("selectedColor", 0);
        MessagesStorage.Instance.getWallpapers();
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.Instance.removeObserver(this, 10005);
        NotificationCenter.Instance.removeObserver(this, 10004);
        NotificationCenter.Instance.removeObserver(this, 10003);
        NotificationCenter.Instance.removeObserver(this, MessagesStorage.wallpapersDidLoaded);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.fragmentView == null) {
            this.fragmentView = inflater.inflate(C0419R.layout.settings_wallpapers_layout, container, false);
            this.listAdapter = new ListAdapter(this.parentActivity);
            this.progressBar = (ProgressBar) this.fragmentView.findViewById(C0419R.id.action_progress);
            this.backgroundImage = (ImageView) this.fragmentView.findViewById(C0419R.id.background_image);
            this.listView = (HorizontalListView) this.fragmentView.findViewById(C0419R.id.listView);
            this.listView.setAdapter(this.listAdapter);
            this.listView.setOnItemClickListener(new C05851());
            this.fragmentView.findViewById(C0419R.id.cancel_button).setOnClickListener(new C05862());
            this.doneButton = this.fragmentView.findViewById(C0419R.id.done_button);
            this.doneButton.setOnClickListener(new C05873());
            processSelectedBackground();
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
        if (resultCode != -1) {
            return;
        }
        Bitmap bitmap;
        if (requestCode == 0) {
            Utilities.addMediaToGallery(this.currentPicturePath);
            try {
                bitmap = FileLoader.loadBitmap(this.currentPicturePath, (float) Utilities.dp(320), (float) Utilities.dp(480));
                bitmap.compress(CompressFormat.JPEG, 87, new FileOutputStream(new File(ApplicationLoader.applicationContext.getFilesDir(), "wallpaper.jpg")));
                this.selectedBackground = -1;
                this.selectedColor = 0;
                this.backgroundImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            }
            this.currentPicturePath = null;
        } else if (requestCode == 1) {
            Uri imageUri = data.getData();
            Cursor cursor = this.parentActivity.getContentResolver().query(imageUri, new String[]{"_data"}, null, null, null);
            if (cursor != null) {
                String imageFilePath = null;
                try {
                    if (cursor.moveToFirst()) {
                        imageFilePath = cursor.getString(0);
                    }
                    cursor.close();
                    bitmap = FileLoader.loadBitmap(imageFilePath, (float) Utilities.dp(320), (float) Utilities.dp(480));
                    bitmap.compress(CompressFormat.JPEG, 87, new FileOutputStream(new File(ApplicationLoader.applicationContext.getFilesDir(), "wallpaper.jpg")));
                    this.selectedBackground = -1;
                    this.selectedColor = 0;
                    this.backgroundImage.setImageBitmap(bitmap);
                } catch (Exception e2) {
                    FileLog.m799e("tmessages", e2);
                }
            }
        }
    }

    private void processSelectedBackground() {
        WallPaper wallPaper = (WallPaper) this.wallpappersByIds.get(Integer.valueOf(this.selectedBackground));
        if (this.selectedBackground == -1 || this.selectedBackground == 1000001 || wallPaper == null || !(wallPaper instanceof TL_wallPaper)) {
            if (this.loadingFile != null) {
                FileLoader.Instance.cancelLoadFile(null, this.loadingSize, null);
            }
            if (this.selectedBackground == 1000001) {
                this.backgroundImage.setImageResource(C0419R.drawable.background_hd);
                this.backgroundImage.setBackgroundColor(0);
                this.selectedColor = 0;
            } else if (this.selectedBackground == -1) {
                File toFile = new File(ApplicationLoader.applicationContext.getFilesDir(), "wallpaper.jpg");
                if (toFile.exists()) {
                    this.backgroundImage.setImageURI(Uri.fromFile(toFile));
                } else {
                    this.selectedBackground = 1000001;
                    processSelectedBackground();
                }
            } else if (wallPaper == null) {
                return;
            } else {
                if (wallPaper instanceof TL_wallPaperSolid) {
                    this.backgroundImage.setImageBitmap(null);
                    this.selectedColor = ViewCompat.MEASURED_STATE_MASK | wallPaper.bg_color;
                    this.backgroundImage.setBackgroundColor(this.selectedColor);
                }
            }
            this.loadingFileObject = null;
            this.loadingFile = null;
            this.loadingSize = null;
            this.doneButton.setEnabled(true);
            this.progressBar.setVisibility(8);
            return;
        }
        PhotoSize size = PhotoObject.getClosestPhotoSizeWithSize(wallPaper.sizes, Utilities.dp(320), Utilities.dp(480));
        String fileName = size.location.volume_id + "_" + size.location.local_id + ".jpg";
        File f = new File(Utilities.getCacheDir(), fileName);
        if (f.exists()) {
            if (this.loadingFile != null) {
                FileLoader.Instance.cancelLoadFile(null, this.loadingSize, null);
            }
            this.loadingFileObject = null;
            this.loadingFile = null;
            this.loadingSize = null;
            this.backgroundImage.setImageURI(Uri.fromFile(f));
            this.backgroundImage.setBackgroundColor(0);
            this.selectedColor = 0;
            this.doneButton.setEnabled(true);
            this.progressBar.setVisibility(8);
            return;
        }
        this.progressBar.setProgress(0);
        this.loadingFile = fileName;
        this.loadingFileObject = f;
        this.doneButton.setEnabled(false);
        this.progressBar.setVisibility(0);
        this.loadingSize = size;
        this.selectedColor = 0;
        FileLoader.Instance.loadFile(null, size, null);
        this.backgroundImage.setBackgroundColor(0);
    }

    public void didReceivedNotification(int id, final Object... args) {
        String location;
        if (id == 10005) {
            location = args[0];
            if (this.loadingFile != null && this.loadingFile.equals(location)) {
                this.loadingFileObject = null;
                this.loadingFile = null;
                this.loadingSize = null;
                this.progressBar.setVisibility(8);
                this.doneButton.setEnabled(false);
            }
        } else if (id == 10004) {
            location = (String) args[0];
            if (this.loadingFile != null && this.loadingFile.equals(location)) {
                this.backgroundImage.setImageURI(Uri.fromFile(this.loadingFileObject));
                this.progressBar.setVisibility(8);
                this.backgroundImage.setBackgroundColor(0);
                this.doneButton.setEnabled(true);
                this.loadingFileObject = null;
                this.loadingFile = null;
                this.loadingSize = null;
            }
        } else if (id == 10003) {
            location = (String) args[0];
            if (this.loadingFile != null && this.loadingFile.equals(location)) {
                this.progressBar.setProgress((int) (args[1].floatValue() * 100.0f));
            }
        } else if (id == MessagesStorage.wallpapersDidLoaded) {
            Utilities.RunOnUIThread(new Runnable() {
                public void run() {
                    SettingsWallpapersActivity.this.wallPapers = (ArrayList) args[0];
                    SettingsWallpapersActivity.this.wallpappersByIds.clear();
                    Iterator i$ = SettingsWallpapersActivity.this.wallPapers.iterator();
                    while (i$.hasNext()) {
                        WallPaper wallPaper = (WallPaper) i$.next();
                        SettingsWallpapersActivity.this.wallpappersByIds.put(Integer.valueOf(wallPaper.id), wallPaper);
                    }
                    if (SettingsWallpapersActivity.this.listAdapter != null) {
                        SettingsWallpapersActivity.this.listAdapter.notifyDataSetChanged();
                    }
                    if (!(SettingsWallpapersActivity.this.wallPapers.isEmpty() || SettingsWallpapersActivity.this.backgroundImage == null)) {
                        SettingsWallpapersActivity.this.processSelectedBackground();
                    }
                    SettingsWallpapersActivity.this.loadWallpapers();
                }
            });
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        fixLayout();
    }

    private void loadWallpapers() {
        ConnectionsManager.Instance.bindRequestToGuid(Long.valueOf(ConnectionsManager.Instance.performRpc(new TL_account_getWallPapers(), new C08915(), null, true, RPCRequest.RPCRequestClassGeneric)), this.classGuid);
    }

    private void fixLayout() {
        final View view = getView();
        if (view != null) {
            view.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {

                class C05901 implements Runnable {
                    C05901() {
                    }

                    public void run() {
                        SettingsWallpapersActivity.this.listView.scrollTo(0);
                    }
                }

                public boolean onPreDraw() {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (SettingsWallpapersActivity.this.listAdapter != null) {
                        SettingsWallpapersActivity.this.listAdapter.notifyDataSetChanged();
                    }
                    if (SettingsWallpapersActivity.this.listView != null) {
                        SettingsWallpapersActivity.this.listView.post(new C05901());
                    }
                    return false;
                }
            });
        }
    }

    public boolean canApplyUpdateStatus() {
        return false;
    }

    public void onResume() {
        super.onResume();
        if (!this.isFinish && getActivity() != null) {
            if (!(this.firstStart || this.listAdapter == null)) {
                this.listAdapter.notifyDataSetChanged();
            }
            ((ApplicationActivity) this.parentActivity).hideActionBar();
            processSelectedBackground();
            fixLayout();
        }
    }
}
