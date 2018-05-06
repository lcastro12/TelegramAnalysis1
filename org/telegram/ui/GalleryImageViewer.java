package org.telegram.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ExploreByTouchHelper;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.appstate.AppStateClient;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import org.telegram.TL.TLRPC.FileLocation;
import org.telegram.TL.TLRPC.Photo;
import org.telegram.TL.TLRPC.PhotoSize;
import org.telegram.TL.TLRPC.TL_messageActionEmpty;
import org.telegram.TL.TLRPC.TL_messageActionUserUpdatedPhoto;
import org.telegram.TL.TLRPC.TL_messageMediaPhoto;
import org.telegram.TL.TLRPC.TL_messageMediaVideo;
import org.telegram.TL.TLRPC.TL_messageService;
import org.telegram.TL.TLRPC.TL_photoCachedSize;
import org.telegram.TL.TLRPC.TL_photoEmpty;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.objects.MessageObject;
import org.telegram.objects.PhotoObject;
import org.telegram.ui.Views.AbstractGalleryActivity;
import org.telegram.ui.Views.GalleryViewPager;
import org.telegram.ui.Views.PZSImageView;

public class GalleryImageViewer extends AbstractGalleryActivity implements NotificationCenterDelegate {
    public static int needShowAllMedia = AppStateClient.STATUS_WRITE_OUT_OF_DATE_VERSION;
    private View bottomView;
    private boolean cacheEndReached = false;
    private int classGuid;
    private long currentDialog = 0;
    private String currentFileName;
    private TextView fakeTitleView;
    private boolean firstLoad = true;
    private boolean fromAll = false;
    private boolean ignoreSet = false;
    private ArrayList<MessageObject> imagesArrTemp = new ArrayList();
    private HashMap<Integer, MessageObject> imagesByIdsTemp = new HashMap();
    private boolean isVideo = false;
    private boolean loadingMore = false;
    private ProgressBar loadingProgress;
    private LocalPagerAdapter localPagerAdapter;
    private GalleryViewPager mViewPager;
    private TextView nameTextView;
    private boolean needSearchMessage = false;
    private TextView timeTextView;
    private TextView title;
    private int totalCount = 0;
    private int user_id = 0;
    private boolean withoutBottom = false;

    class C04981 implements OnClickListener {
        C04981() {
        }

        public void onClick(View view) {
            try {
                FileLocation file = GalleryImageViewer.this.getCurrentFile();
                File f = new File(Utilities.getCacheDir(), file.volume_id + "_" + file.local_id + ".jpg");
                if (f.exists()) {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("image/jpeg");
                    intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(f));
                    GalleryImageViewer.this.startActivity(intent);
                }
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            }
        }
    }

    class C04992 implements OnClickListener {
        C04992() {
        }

        public void onClick(View view) {
            MessageObject obj = (MessageObject) GalleryImageViewer.this.localPagerAdapter.imagesArr.get(GalleryImageViewer.this.mViewPager.getCurrentItem());
            if (obj.messageOwner.send_state == 0) {
                ArrayList<Integer> arr = new ArrayList();
                arr.add(Integer.valueOf(obj.messageOwner.id));
                MessagesController.Instance.deleteMessages(arr);
                GalleryImageViewer.this.finish();
            }
        }
    }

    class C05014 implements OnPreDrawListener {
        C05014() {
        }

        public boolean onPreDraw() {
            GalleryImageViewer.this.mViewPager.beginFakeDrag();
            if (GalleryImageViewer.this.mViewPager.isFakeDragging()) {
                GalleryImageViewer.this.mViewPager.fakeDragBy(1.0f);
                GalleryImageViewer.this.mViewPager.endFakeDrag();
            }
            GalleryImageViewer.this.mViewPager.getViewTreeObserver().removeOnPreDrawListener(this);
            return false;
        }
    }

    public class LocalPagerAdapter extends PagerAdapter {
        public ArrayList<MessageObject> imagesArr;
        private ArrayList<FileLocation> imagesArrLocations;
        public HashMap<Integer, MessageObject> imagesByIds;

        public LocalPagerAdapter(ArrayList<MessageObject> _imagesArr, HashMap<Integer, MessageObject> _imagesByIds) {
            this.imagesArr = _imagesArr;
            this.imagesByIds = _imagesByIds;
        }

        public LocalPagerAdapter(ArrayList<FileLocation> locations) {
            this.imagesArrLocations = locations;
        }

        public void setPrimaryItem(ViewGroup container, final int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (container != null && object != null && !GalleryImageViewer.this.ignoreSet) {
                ((GalleryViewPager) container).mCurrentView = (PZSImageView) ((View) object).findViewById(C0419R.id.page_image);
                if (this.imagesArr != null) {
                    GalleryImageViewer.this.didShowMessageObject((MessageObject) this.imagesArr.get(position));
                    if (GalleryImageViewer.this.totalCount != 0 && !GalleryImageViewer.this.needSearchMessage) {
                        if (this.imagesArr.size() < GalleryImageViewer.this.totalCount && !GalleryImageViewer.this.loadingMore && position < 5) {
                            boolean z;
                            MessageObject lastMessage = (MessageObject) this.imagesArr.get(0);
                            MessagesController messagesController = MessagesController.Instance;
                            long access$1000 = GalleryImageViewer.this.currentDialog;
                            int i = lastMessage.messageOwner.id;
                            if (GalleryImageViewer.this.cacheEndReached) {
                                z = false;
                            } else {
                                z = true;
                            }
                            messagesController.loadMedia(access$1000, 0, 100, i, z, GalleryImageViewer.this.classGuid);
                            GalleryImageViewer.this.loadingMore = true;
                        }
                        Utilities.RunOnUIThread(new Runnable() {
                            public void run() {
                                GalleryImageViewer.this.getSupportActionBar().setTitle(String.format("%d %s %d", new Object[]{Integer.valueOf(((GalleryImageViewer.this.totalCount - LocalPagerAdapter.this.imagesArr.size()) + position) + 1), GalleryImageViewer.this.getString(C0419R.string.Of), Integer.valueOf(GalleryImageViewer.this.totalCount)}));
                                if (GalleryImageViewer.this.title != null) {
                                    GalleryImageViewer.this.fakeTitleView.setText(String.format("%d %s %d", new Object[]{Integer.valueOf(((GalleryImageViewer.this.totalCount - LocalPagerAdapter.this.imagesArr.size()) + position) + 1), GalleryImageViewer.this.getString(C0419R.string.Of), Integer.valueOf(GalleryImageViewer.this.totalCount)}));
                                    GalleryImageViewer.this.fakeTitleView.measure(MeasureSpec.makeMeasureSpec(400, ExploreByTouchHelper.INVALID_ID), MeasureSpec.makeMeasureSpec(100, ExploreByTouchHelper.INVALID_ID));
                                    GalleryImageViewer.this.title.setWidth(GalleryImageViewer.this.fakeTitleView.getMeasuredWidth() + Utilities.dp(8));
                                    GalleryImageViewer.this.title.setMaxWidth(GalleryImageViewer.this.fakeTitleView.getMeasuredWidth() + Utilities.dp(8));
                                }
                            }
                        });
                    }
                } else if (this.imagesArrLocations != null) {
                    FileLocation file = (FileLocation) this.imagesArrLocations.get(position);
                    GalleryImageViewer.this.currentFileName = file.volume_id + "_" + file.local_id + ".jpg";
                    GalleryImageViewer.this.checkCurrentFile();
                    if (this.imagesArrLocations.size() > 1) {
                        Utilities.RunOnUIThread(new Runnable() {
                            public void run() {
                                GalleryImageViewer.this.getSupportActionBar().setTitle(String.format("%d %s %d", new Object[]{Integer.valueOf(position + 1), GalleryImageViewer.this.getString(C0419R.string.Of), Integer.valueOf(LocalPagerAdapter.this.imagesArrLocations.size())}));
                                if (GalleryImageViewer.this.title != null) {
                                    GalleryImageViewer.this.fakeTitleView.setText(String.format("%d %s %d", new Object[]{Integer.valueOf(position + 1), GalleryImageViewer.this.getString(C0419R.string.Of), Integer.valueOf(LocalPagerAdapter.this.imagesArrLocations.size())}));
                                    GalleryImageViewer.this.fakeTitleView.measure(MeasureSpec.makeMeasureSpec(400, ExploreByTouchHelper.INVALID_ID), MeasureSpec.makeMeasureSpec(100, ExploreByTouchHelper.INVALID_ID));
                                    GalleryImageViewer.this.title.setWidth(GalleryImageViewer.this.fakeTitleView.getMeasuredWidth() + Utilities.dp(8));
                                    GalleryImageViewer.this.title.setMaxWidth(GalleryImageViewer.this.fakeTitleView.getMeasuredWidth() + Utilities.dp(8));
                                }
                            }
                        });
                    } else {
                        GalleryImageViewer.this.getSupportActionBar().setTitle(GalleryImageViewer.this.getString(C0419R.string.Gallery));
                    }
                }
            }
        }

        public void updateViews() {
            int count = GalleryImageViewer.this.mViewPager.getChildCount();
            for (int a = 0; a < count; a++) {
                TextView playButton = (TextView) GalleryImageViewer.this.mViewPager.getChildAt(a).findViewById(C0419R.id.action_button);
                MessageObject message = (MessageObject) playButton.getTag();
                if (message != null) {
                    processViews(playButton, message);
                }
            }
        }

        public void processViews(TextView playButton, MessageObject message) {
            if (message.messageOwner.send_state != 1 && message.messageOwner.send_state != 2) {
                playButton.setVisibility(0);
                String fileName = message.messageOwner.media.video.dc_id + "_" + message.messageOwner.media.video.id + ".mp4";
                boolean load = false;
                if (message.messageOwner.attachPath == null || message.messageOwner.attachPath.length() == 0) {
                    if (new File(Utilities.getCacheDir(), fileName).exists()) {
                        playButton.setText(GalleryImageViewer.this.getString(C0419R.string.ViewVideo));
                    } else {
                        load = true;
                    }
                } else if (new File(message.messageOwner.attachPath).exists()) {
                    playButton.setText(GalleryImageViewer.this.getString(C0419R.string.ViewVideo));
                } else {
                    load = true;
                }
                if (load) {
                    Float progress = (Float) FileLoader.Instance.fileProgresses.get(fileName);
                    if (FileLoader.Instance.isLoadingFile(fileName)) {
                        playButton.setText(C0419R.string.CancelDownload);
                        return;
                    }
                    playButton.setText(String.format("%s %.1f MB", new Object[]{GalleryImageViewer.this.getString(C0419R.string.DOWNLOAD), Float.valueOf((((float) message.messageOwner.media.video.size) / 1024.0f) / 1024.0f)}));
                }
            }
        }

        public int getItemPosition(Object object) {
            return -2;
        }

        public Object instantiateItem(View collection, int position) {
            View view = View.inflate(collection.getContext(), C0419R.layout.gallery_page_layout, null);
            ((ViewPager) collection).addView(view, 0);
            PZSImageView iv = (PZSImageView) view.findViewById(C0419R.id.page_image);
            final TextView playButton = (TextView) view.findViewById(C0419R.id.action_button);
            if (this.imagesArr != null) {
                final MessageObject message = (MessageObject) this.imagesArr.get(position);
                view.setTag(Integer.valueOf(message.messageOwner.id));
                ArrayList<PhotoSize> sizes;
                PhotoSize sizeFull;
                if (message.messageOwner instanceof TL_messageService) {
                    if (message.messageOwner.action instanceof TL_messageActionUserUpdatedPhoto) {
                        iv.isVideo = false;
                        iv.setImage(message.messageOwner.action.newUserPhoto.photo_big, null, 0, -1);
                    } else {
                        sizes = message.messageOwner.action.photo.sizes;
                        iv.isVideo = false;
                        if (sizes.size() > 0) {
                            sizeFull = PhotoObject.getClosestPhotoSizeWithSize(sizes, 800, 800);
                            if (message.imagePreview != null) {
                                iv.setImage(sizeFull.location, null, message.imagePreview, sizeFull.size);
                            } else {
                                iv.setImage(sizeFull.location, null, 0, sizeFull.size);
                            }
                        }
                    }
                } else if (message.messageOwner.media instanceof TL_messageMediaPhoto) {
                    sizes = message.messageOwner.media.photo.sizes;
                    iv.isVideo = false;
                    if (sizes.size() > 0) {
                        sizeFull = PhotoObject.getClosestPhotoSizeWithSize(sizes, 800, 800);
                        if (message.imagePreview != null) {
                            iv.setImage(sizeFull.location, null, message.imagePreview, sizeFull.size);
                        } else {
                            iv.setImage(sizeFull.location, null, 0, sizeFull.size);
                        }
                    }
                } else if (message.messageOwner.media instanceof TL_messageMediaVideo) {
                    processViews(playButton, message);
                    playButton.setTag(message);
                    playButton.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            boolean loadFile = false;
                            String fileName = message.messageOwner.media.video.dc_id + "_" + message.messageOwner.media.video.id + ".mp4";
                            Intent intent;
                            if (message.messageOwner.attachPath == null || message.messageOwner.attachPath.length() == 0) {
                                File cacheFile = new File(Utilities.getCacheDir(), fileName);
                                if (cacheFile.exists()) {
                                    intent = new Intent("android.intent.action.VIEW");
                                    intent.setDataAndType(Uri.fromFile(cacheFile), "video/mp4");
                                    GalleryImageViewer.this.startActivity(intent);
                                } else {
                                    loadFile = true;
                                }
                            } else {
                                File f = new File(message.messageOwner.attachPath);
                                if (f.exists()) {
                                    intent = new Intent("android.intent.action.VIEW");
                                    intent.setDataAndType(Uri.fromFile(f), "video/mp4");
                                    GalleryImageViewer.this.startActivity(intent);
                                } else {
                                    loadFile = true;
                                }
                            }
                            if (loadFile) {
                                if (FileLoader.Instance.isLoadingFile(fileName)) {
                                    FileLoader.Instance.cancelLoadFile(message.messageOwner.media.video, null, null);
                                } else {
                                    FileLoader.Instance.loadFile(message.messageOwner.media.video, null, null);
                                }
                                GalleryImageViewer.this.checkCurrentFile();
                                LocalPagerAdapter.this.processViews(playButton, message);
                            }
                        }
                    });
                    iv.isVideo = true;
                    if (message.messageOwner.media.video.thumb instanceof TL_photoCachedSize) {
                        iv.setImageBitmap(message.imagePreview);
                    } else if (message.messageOwner.media.video.thumb != null) {
                        iv.setImage(message.messageOwner.media.video.thumb.location, null, 0, message.messageOwner.media.video.thumb.size);
                    }
                }
            } else {
                iv.isVideo = false;
                iv.setImage((FileLocation) this.imagesArrLocations.get(position), null, 0, -1);
            }
            return view;
        }

        public void destroyItem(View collection, int position, Object view) {
            ((ViewPager) collection).removeView((View) view);
            PZSImageView iv = (PZSImageView) ((View) view).findViewById(C0419R.id.page_image);
            FileLoader.Instance.cancelLoadingForImageView(iv);
            iv.clearImage();
        }

        public int getCount() {
            if (this.imagesArr != null) {
                return this.imagesArr.size();
            }
            if (this.imagesArrLocations != null) {
                return this.imagesArrLocations.size();
            }
            return 0;
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public Parcelable saveState() {
            return null;
        }

        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        public void finishUpdate(View container) {
        }

        public void startUpdate(View container) {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.classGuid = ConnectionsManager.Instance.generateClassGuid();
        setContentView((int) C0419R.layout.gallery_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setTitle(getString(C0419R.string.Gallery));
        actionBar.show();
        this.mViewPager = (GalleryViewPager) findViewById(C0419R.id.gallery_view_pager);
        ImageView shareButton = (ImageView) findViewById(C0419R.id.gallery_view_share_button);
        ImageView deleteButton = (ImageView) findViewById(C0419R.id.gallery_view_delete_button);
        this.nameTextView = (TextView) findViewById(C0419R.id.gallery_view_name_text);
        this.timeTextView = (TextView) findViewById(C0419R.id.gallery_view_time_text);
        this.bottomView = findViewById(C0419R.id.gallery_view_bottom_view);
        this.fakeTitleView = (TextView) findViewById(C0419R.id.fake_title_view);
        this.loadingProgress = (ProgressBar) findViewById(C0419R.id.action_progress);
        this.title = (TextView) findViewById(C0419R.id.action_bar_title);
        if (this.title == null) {
            this.title = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", "android"));
        }
        NotificationCenter.Instance.addObserver(this, 10005);
        NotificationCenter.Instance.addObserver(this, 10004);
        NotificationCenter.Instance.addObserver(this, 10003);
        NotificationCenter.Instance.addObserver(this, 20);
        NotificationCenter.Instance.addObserver(this, 18);
        NotificationCenter.Instance.addObserver(this, 24);
        Integer index = null;
        if (this.localPagerAdapter == null) {
            MessageObject file = (MessageObject) NotificationCenter.Instance.getFromMemCache(51);
            FileLocation fileLocation = (FileLocation) NotificationCenter.Instance.getFromMemCache(53);
            ArrayList<MessageObject> messagesArr = (ArrayList) NotificationCenter.Instance.getFromMemCache(54);
            index = (Integer) NotificationCenter.Instance.getFromMemCache(55);
            Integer uid = (Integer) NotificationCenter.Instance.getFromMemCache(56);
            if (uid != null) {
                this.user_id = uid.intValue();
            }
            ArrayList<MessageObject> imagesArr;
            HashMap<Integer, MessageObject> imagesByIds;
            if (file != null) {
                imagesArr = new ArrayList();
                imagesByIds = new HashMap();
                imagesArr.add(file);
                if (file.messageOwner.action == null || (file.messageOwner.action instanceof TL_messageActionEmpty)) {
                    this.needSearchMessage = true;
                    imagesByIds.put(Integer.valueOf(file.messageOwner.id), file);
                    if (file.messageOwner.dialog_id != 0) {
                        this.currentDialog = file.messageOwner.dialog_id;
                    } else if (file.messageOwner.to_id.chat_id != 0) {
                        this.currentDialog = (long) (-file.messageOwner.to_id.chat_id);
                    } else if (file.messageOwner.to_id.user_id == UserConfig.clientUserId) {
                        this.currentDialog = (long) file.messageOwner.from_id;
                    } else {
                        this.currentDialog = (long) file.messageOwner.to_id.user_id;
                    }
                }
                this.localPagerAdapter = new LocalPagerAdapter(imagesArr, imagesByIds);
            } else if (fileLocation != null) {
                ArrayList<FileLocation> arr = new ArrayList();
                arr.add(fileLocation);
                this.withoutBottom = true;
                deleteButton.setVisibility(4);
                this.nameTextView.setVisibility(4);
                this.timeTextView.setVisibility(4);
                this.localPagerAdapter = new LocalPagerAdapter(arr);
            } else if (messagesArr != null) {
                imagesArr = new ArrayList();
                imagesByIds = new HashMap();
                imagesArr.addAll(messagesArr);
                Collections.reverse(imagesArr);
                Iterator i$ = imagesArr.iterator();
                while (i$.hasNext()) {
                    MessageObject message = (MessageObject) i$.next();
                    imagesByIds.put(Integer.valueOf(message.messageOwner.id), message);
                }
                index = Integer.valueOf((imagesArr.size() - index.intValue()) - 1);
                MessageObject object = (MessageObject) imagesArr.get(0);
                if (object.messageOwner.dialog_id != 0) {
                    this.currentDialog = object.messageOwner.dialog_id;
                } else if (object.messageOwner.to_id.chat_id != 0) {
                    this.currentDialog = (long) (-object.messageOwner.to_id.chat_id);
                } else if (object.messageOwner.to_id.user_id == UserConfig.clientUserId) {
                    this.currentDialog = (long) object.messageOwner.from_id;
                } else {
                    this.currentDialog = (long) object.messageOwner.to_id.user_id;
                }
                this.localPagerAdapter = new LocalPagerAdapter(imagesArr, imagesByIds);
            }
        }
        this.mViewPager.setPageMargin(Utilities.dp(20));
        this.mViewPager.setOffscreenPageLimit(1);
        this.mViewPager.setAdapter(this.localPagerAdapter);
        if (index != null) {
            this.fromAll = true;
            this.mViewPager.setCurrentItem(index.intValue());
        }
        shareButton.setOnClickListener(new C04981());
        deleteButton.setOnClickListener(new C04992());
        if (this.currentDialog != 0 && this.totalCount == 0) {
            MessagesController.Instance.getMediaCount(this.currentDialog, this.classGuid, true);
        }
        if (this.user_id != 0) {
            MessagesController.Instance.loadUserPhotos(this.user_id, 0, 30, 0, true, this.classGuid);
        }
        checkCurrentFile();
    }

    protected void onDestroy() {
        super.onDestroy();
        NotificationCenter.Instance.removeObserver(this, 10005);
        NotificationCenter.Instance.removeObserver(this, 10004);
        NotificationCenter.Instance.removeObserver(this, 10003);
        NotificationCenter.Instance.removeObserver(this, 20);
        NotificationCenter.Instance.removeObserver(this, 18);
        NotificationCenter.Instance.removeObserver(this, 24);
        ConnectionsManager.Instance.cancelRpcsForClassGuid(this.classGuid);
    }

    public void didReceivedNotification(int id, Object... args) {
        String location;
        if (id == 10005) {
            location = args[0];
            if (this.currentFileName != null && this.currentFileName.equals(location)) {
                if (this.loadingProgress != null) {
                    this.loadingProgress.setVisibility(8);
                }
                if (this.localPagerAdapter != null) {
                    this.localPagerAdapter.updateViews();
                }
            }
        } else if (id == 10004) {
            location = (String) args[0];
            if (this.currentFileName != null && this.currentFileName.equals(location)) {
                if (this.loadingProgress != null) {
                    this.loadingProgress.setVisibility(8);
                }
                if (this.localPagerAdapter != null) {
                    this.localPagerAdapter.updateViews();
                }
            }
        } else if (id == 10003) {
            location = (String) args[0];
            if (this.currentFileName != null && this.currentFileName.equals(location)) {
                Float progress = args[1];
                if (this.loadingProgress != null) {
                    this.loadingProgress.setVisibility(0);
                    this.loadingProgress.setProgress((int) (progress.floatValue() * 100.0f));
                }
                if (this.localPagerAdapter != null) {
                    this.localPagerAdapter.updateViews();
                }
            }
        } else if (id == 24) {
            guid = ((Integer) args[4]).intValue();
            if (this.user_id == ((Integer) args[0]).intValue() && this.classGuid == guid) {
                fromCache = ((Boolean) args[3]).booleanValue();
                FileLocation currentLocation = null;
                int setToImage = -1;
                if (!(this.localPagerAdapter == null || this.mViewPager == null)) {
                    int idx = this.mViewPager.getCurrentItem();
                    if (this.localPagerAdapter.imagesArrLocations.size() > idx) {
                        currentLocation = (FileLocation) this.localPagerAdapter.imagesArrLocations.get(idx);
                    }
                }
                ArrayList<Photo> photos = args[5];
                if (!photos.isEmpty()) {
                    ArrayList<FileLocation> arr = new ArrayList();
                    i$ = photos.iterator();
                    while (i$.hasNext()) {
                        Photo photo = (Photo) i$.next();
                        if (!(photo instanceof TL_photoEmpty)) {
                            PhotoSize sizeFull = PhotoObject.getClosestPhotoSizeWithSize(photo.sizes, 800, 800);
                            if (sizeFull != null) {
                                if (currentLocation != null && sizeFull.location.local_id == currentLocation.local_id && sizeFull.location.volume_id == currentLocation.volume_id) {
                                    setToImage = arr.size();
                                }
                                arr.add(sizeFull.location);
                            }
                        }
                    }
                    this.mViewPager.setAdapter(null);
                    count = this.mViewPager.getChildCount();
                    for (a = 0; a < count; a++) {
                        this.mViewPager.removeView(this.mViewPager.getChildAt(0));
                    }
                    this.mViewPager.mCurrentView = null;
                    this.needSearchMessage = false;
                    this.ignoreSet = true;
                    r2 = this.mViewPager;
                    r3 = new LocalPagerAdapter(arr);
                    this.localPagerAdapter = r3;
                    r2.setAdapter(r3);
                    this.mViewPager.invalidate();
                    this.ignoreSet = false;
                    if (setToImage != -1) {
                        this.mViewPager.setCurrentItem(setToImage);
                    } else {
                        this.mViewPager.setCurrentItem(0);
                    }
                    if (fromCache) {
                        MessagesController.Instance.loadUserPhotos(this.user_id, 0, 30, 0, false, this.classGuid);
                    }
                }
            }
        } else if (id == 20) {
            if (((Long) args[0]).longValue() == this.currentDialog) {
                if (((int) this.currentDialog) != 0 && ((Boolean) args[2]).booleanValue()) {
                    MessagesController.Instance.getMediaCount(this.currentDialog, this.classGuid, false);
                }
                this.totalCount = ((Integer) args[1]).intValue();
                if (this.needSearchMessage && this.firstLoad) {
                    this.firstLoad = false;
                    MessagesController.Instance.loadMedia(this.currentDialog, 0, 100, 0, true, this.classGuid);
                    this.loadingMore = true;
                } else if (this.mViewPager != null && this.localPagerAdapter != null && this.localPagerAdapter.imagesArr != null) {
                    final int size = ((this.totalCount - this.localPagerAdapter.imagesArr.size()) + this.mViewPager.getCurrentItem()) + 1;
                    Utilities.RunOnUIThread(new Runnable() {
                        public void run() {
                            GalleryImageViewer.this.getSupportActionBar().setTitle(String.format("%d %s %d", new Object[]{Integer.valueOf(size), GalleryImageViewer.this.getString(C0419R.string.Of), Integer.valueOf(GalleryImageViewer.this.totalCount)}));
                            if (GalleryImageViewer.this.title != null) {
                                GalleryImageViewer.this.fakeTitleView.setText(String.format("%d %s %d", new Object[]{Integer.valueOf(size), GalleryImageViewer.this.getString(C0419R.string.Of), Integer.valueOf(GalleryImageViewer.this.totalCount)}));
                                GalleryImageViewer.this.fakeTitleView.measure(MeasureSpec.makeMeasureSpec(400, ExploreByTouchHelper.INVALID_ID), MeasureSpec.makeMeasureSpec(100, ExploreByTouchHelper.INVALID_ID));
                                GalleryImageViewer.this.title.setWidth(GalleryImageViewer.this.fakeTitleView.getMeasuredWidth() + Utilities.dp(8));
                                GalleryImageViewer.this.title.setMaxWidth(GalleryImageViewer.this.fakeTitleView.getMeasuredWidth() + Utilities.dp(8));
                            }
                        }
                    });
                }
            }
        } else if (id == 18) {
            long uid = ((Long) args[0]).longValue();
            guid = ((Integer) args[4]).intValue();
            if (uid == this.currentDialog && guid == this.classGuid && this.localPagerAdapter != null && this.localPagerAdapter.imagesArr != null) {
                this.loadingMore = false;
                ArrayList<MessageObject> arr2 = args[2];
                fromCache = ((Boolean) args[3]).booleanValue();
                this.cacheEndReached = !fromCache;
                int added;
                MessageObject message;
                if (!this.needSearchMessage) {
                    added = 0;
                    i$ = arr2.iterator();
                    while (i$.hasNext()) {
                        message = (MessageObject) i$.next();
                        if (!this.localPagerAdapter.imagesByIds.containsKey(Integer.valueOf(message.messageOwner.id))) {
                            added++;
                            this.localPagerAdapter.imagesArr.add(0, message);
                            this.localPagerAdapter.imagesByIds.put(Integer.valueOf(message.messageOwner.id), message);
                        }
                    }
                    if (arr2.isEmpty() && !fromCache) {
                        this.totalCount = arr2.size();
                    }
                    if (added != 0) {
                        int current = this.mViewPager.getCurrentItem();
                        this.ignoreSet = true;
                        this.imagesArrTemp = new ArrayList(this.localPagerAdapter.imagesArr);
                        this.imagesByIdsTemp = new HashMap(this.localPagerAdapter.imagesByIds);
                        r2 = this.mViewPager;
                        r3 = new LocalPagerAdapter(this.imagesArrTemp, this.imagesByIdsTemp);
                        this.localPagerAdapter = r3;
                        r2.setAdapter(r3);
                        this.mViewPager.invalidate();
                        this.ignoreSet = false;
                        this.imagesArrTemp = null;
                        this.imagesByIdsTemp = null;
                        this.mViewPager.setCurrentItem(current + added);
                        return;
                    }
                    this.totalCount = this.localPagerAdapter.imagesArr.size();
                } else if (arr2.isEmpty()) {
                    this.needSearchMessage = false;
                } else {
                    int foundIndex = -1;
                    MessageObject currentMessage = (MessageObject) this.localPagerAdapter.imagesArr.get(this.mViewPager.getCurrentItem());
                    added = 0;
                    i$ = arr2.iterator();
                    while (i$.hasNext()) {
                        message = (MessageObject) i$.next();
                        if (!this.imagesByIdsTemp.containsKey(Integer.valueOf(message.messageOwner.id))) {
                            added++;
                            this.imagesArrTemp.add(0, message);
                            this.imagesByIdsTemp.put(Integer.valueOf(message.messageOwner.id), message);
                            if (message.messageOwner.id == currentMessage.messageOwner.id) {
                                foundIndex = arr2.size() - added;
                            }
                        }
                    }
                    if (added == 0) {
                        this.totalCount = this.imagesArrTemp.size();
                    }
                    if (foundIndex != -1) {
                        this.mViewPager.setAdapter(null);
                        count = this.mViewPager.getChildCount();
                        for (a = 0; a < count; a++) {
                            this.mViewPager.removeView(this.mViewPager.getChildAt(0));
                        }
                        this.mViewPager.mCurrentView = null;
                        this.needSearchMessage = false;
                        this.ignoreSet = true;
                        r2 = this.mViewPager;
                        r3 = new LocalPagerAdapter(this.imagesArrTemp, this.imagesByIdsTemp);
                        this.localPagerAdapter = r3;
                        r2.setAdapter(r3);
                        this.mViewPager.invalidate();
                        this.ignoreSet = false;
                        this.mViewPager.setCurrentItem(foundIndex);
                        this.imagesArrTemp = null;
                        this.imagesByIdsTemp = null;
                    } else if (!this.cacheEndReached || !arr2.isEmpty()) {
                        MessageObject lastMessage = (MessageObject) this.imagesArrTemp.get(0);
                        this.loadingMore = true;
                        MessagesController.Instance.loadMedia(this.currentDialog, 0, 100, lastMessage.messageOwner.id, true, this.classGuid);
                    }
                }
            }
        }
    }

    private FileLocation getCurrentFile() {
        int item = this.mViewPager.getCurrentItem();
        if (this.withoutBottom) {
            return (FileLocation) this.localPagerAdapter.imagesArrLocations.get(item);
        }
        MessageObject message = (MessageObject) this.localPagerAdapter.imagesArr.get(item);
        PhotoSize sizeFull;
        if (message.messageOwner instanceof TL_messageService) {
            if (message.messageOwner.action instanceof TL_messageActionUserUpdatedPhoto) {
                return message.messageOwner.action.newUserPhoto.photo_big;
            }
            sizeFull = PhotoObject.getClosestPhotoSizeWithSize(message.messageOwner.action.photo.sizes, 800, 800);
            if (sizeFull != null) {
                return sizeFull.location;
            }
        } else if (message.messageOwner.media instanceof TL_messageMediaPhoto) {
            sizeFull = PhotoObject.getClosestPhotoSizeWithSize(message.messageOwner.media.photo.sizes, 800, 800);
            if (sizeFull != null) {
                return sizeFull.location;
            }
        } else if (message.messageOwner.media instanceof TL_messageMediaVideo) {
            return message.messageOwner.media.video.thumb.location;
        }
        return null;
    }

    public void topBtn() {
        if (getSupportActionBar().isShowing()) {
            getSupportActionBar().hide();
            startViewAnimation(this.bottomView, false);
            return;
        }
        this.bottomView.setVisibility(0);
        getSupportActionBar().show();
        startViewAnimation(this.bottomView, true);
    }

    public void didShowMessageObject(MessageObject obj) {
        User user = (User) MessagesController.Instance.users.get(Integer.valueOf(obj.messageOwner.from_id));
        if (user != null) {
            this.nameTextView.setText(Utilities.formatName(user.first_name, user.last_name));
            this.timeTextView.setText(Utilities.formatterYearMax.format(((long) obj.messageOwner.date) * 1000));
        } else {
            this.nameTextView.setText(BuildConfig.FLAVOR);
        }
        boolean z = obj.messageOwner.media != null && (obj.messageOwner.media instanceof TL_messageMediaVideo);
        this.isVideo = z;
        FileLocation file;
        if (obj.messageOwner instanceof TL_messageService) {
            if (obj.messageOwner.action instanceof TL_messageActionUserUpdatedPhoto) {
                file = obj.messageOwner.action.newUserPhoto.photo_big;
                this.currentFileName = file.volume_id + "_" + file.local_id + ".jpg";
            } else {
                ArrayList<PhotoSize> sizes = obj.messageOwner.action.photo.sizes;
                if (sizes.size() > 0) {
                    PhotoSize sizeFull = PhotoObject.getClosestPhotoSizeWithSize(sizes, 800, 800);
                    if (sizeFull != null) {
                        this.currentFileName = sizeFull.location.volume_id + "_" + sizeFull.location.local_id + ".jpg";
                    }
                }
            }
        } else if (obj.messageOwner.media == null) {
            this.currentFileName = null;
        } else if (obj.messageOwner.media instanceof TL_messageMediaVideo) {
            this.currentFileName = obj.messageOwner.media.video.dc_id + "_" + obj.messageOwner.media.video.id + ".mp4";
        } else if (obj.messageOwner.media instanceof TL_messageMediaPhoto) {
            file = getCurrentFile();
            if (file != null) {
                this.currentFileName = file.volume_id + "_" + file.local_id + ".jpg";
            } else {
                this.currentFileName = null;
            }
        }
        checkCurrentFile();
        supportInvalidateOptionsMenu();
    }

    private void checkCurrentFile() {
        if (this.currentFileName == null) {
            this.loadingProgress.setVisibility(8);
        } else if (new File(Utilities.getCacheDir(), this.currentFileName).exists()) {
            this.loadingProgress.setVisibility(8);
        } else {
            this.loadingProgress.setVisibility(0);
            Float progress = (Float) FileLoader.Instance.fileProgresses.get(this.currentFileName);
            if (progress != null) {
                this.loadingProgress.setProgress((int) (progress.floatValue() * 100.0f));
            } else {
                this.loadingProgress.setProgress(0);
            }
        }
        if (!this.isVideo) {
            return;
        }
        if (FileLoader.Instance.isLoadingFile(this.currentFileName)) {
            this.loadingProgress.setVisibility(0);
        } else {
            this.loadingProgress.setVisibility(8);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (this.withoutBottom) {
            inflater.inflate(C0419R.menu.gallery_save_only_menu, menu);
        } else if (this.isVideo) {
            inflater.inflate(C0419R.menu.gallery_video_menu, menu);
        } else {
            inflater.inflate(C0419R.menu.gallery_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void openOptionsMenu() {
        FileLocation file = getCurrentFile();
        if (file != null && new File(Utilities.getCacheDir(), file.volume_id + "_" + file.local_id + ".jpg").exists()) {
            super.openOptionsMenu();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        processSelectedMenu(item.getItemId());
        return true;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        fixLayout();
    }

    private void fixLayout() {
        if (this.mViewPager != null) {
            this.mViewPager.getViewTreeObserver().addOnPreDrawListener(new C05014());
        }
    }

    private void processSelectedMenu(int itemId) {
        switch (itemId) {
            case 16908332:
                this.mViewPager.setAdapter(null);
                this.localPagerAdapter = null;
                finish();
                System.gc();
                return;
            case C0419R.id.gallery_menu_save:
                FileLocation file = getCurrentFile();
                File f = new File(Utilities.getCacheDir(), file.volume_id + "_" + file.local_id + ".jpg");
                File dstFile = Utilities.generatePicturePath();
                try {
                    Utilities.copyFile(f, dstFile);
                    Utilities.addMediaToGallery(Uri.fromFile(dstFile));
                    return;
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                    return;
                }
            case C0419R.id.gallery_menu_showall:
                if (this.fromAll) {
                    finish();
                    return;
                } else if (!this.localPagerAdapter.imagesArr.isEmpty() && this.currentDialog != 0) {
                    finish();
                    NotificationCenter.Instance.postNotificationName(needShowAllMedia, Long.valueOf(this.currentDialog));
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 10) {
            int chatId = data.getIntExtra("chatId", 0);
            int userId = data.getIntExtra("userId", 0);
            int dialog_id = 0;
            if (chatId != 0) {
                dialog_id = -chatId;
            } else if (userId != 0) {
                dialog_id = userId;
            }
            FileLocation location = getCurrentFile();
            if (dialog_id != 0 && location != null) {
                Intent intent = new Intent(this, ChatActivity.class);
                if (chatId != 0) {
                    intent.putExtra("chatId", chatId);
                } else {
                    intent.putExtra("userId", userId);
                }
                startActivity(intent);
                NotificationCenter.Instance.postNotificationName(5, new Object[0]);
                finish();
                if (this.withoutBottom) {
                    MessagesController.Instance.sendMessage(location, (long) dialog_id);
                    return;
                }
                MessagesController.Instance.sendMessage((MessageObject) this.localPagerAdapter.imagesArr.get(this.mViewPager.getCurrentItem()), (long) dialog_id);
            }
        }
    }

    private void startViewAnimation(final View panel, boolean up) {
        Animation animation;
        if (up) {
            animation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
            animation.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    panel.setVisibility(0);
                }

                public void onAnimationEnd(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }
            });
            animation.setDuration(100);
        } else {
            animation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
            animation.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    panel.setVisibility(4);
                }

                public void onAnimationRepeat(Animation animation) {
                }
            });
            animation.setDuration(400);
        }
        panel.startAnimation(animation);
    }
}
