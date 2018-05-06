package org.telegram.ui.Views;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import java.io.File;
import org.telegram.TL.TLRPC.InputFile;
import org.telegram.TL.TLRPC.PhotoSize;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;

public class AvatarUpdater implements NotificationCenterDelegate {
    private PhotoSize bigPhoto;
    private boolean clearAfterUpdate = false;
    public String currentPicturePath;
    public AvatarUpdaterDelegate delegate;
    public Activity parentActivity = null;
    public Fragment parentFragment = null;
    File picturePath = null;
    public boolean returnOnly = false;
    private PhotoSize smallPhoto;
    public String uploadingAvatar = null;

    class C06012 implements Runnable {
        C06012() {
        }

        public void run() {
            NotificationCenter.Instance.removeObserver(AvatarUpdater.this, FileLoader.FileDidUpload);
            NotificationCenter.Instance.removeObserver(AvatarUpdater.this, 10001);
            AvatarUpdater.this.uploadingAvatar = null;
            if (AvatarUpdater.this.clearAfterUpdate) {
                AvatarUpdater.this.parentFragment = null;
                AvatarUpdater.this.parentActivity = null;
                AvatarUpdater.this.delegate = null;
            }
        }
    }

    public interface AvatarUpdaterDelegate {
        void didUploadedPhoto(InputFile inputFile, PhotoSize photoSize, PhotoSize photoSize2);
    }

    public void clear() {
        if (this.uploadingAvatar != null) {
            this.clearAfterUpdate = true;
            return;
        }
        this.parentFragment = null;
        this.parentActivity = null;
        this.delegate = null;
    }

    public void openCamera() {
        try {
            Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            File image = Utilities.generatePicturePath();
            if (image != null) {
                takePictureIntent.putExtra("output", Uri.fromFile(image));
                this.currentPicturePath = image.getAbsolutePath();
            }
            if (this.parentFragment != null) {
                this.parentFragment.startActivityForResult(takePictureIntent, 0);
            } else if (this.parentActivity != null) {
                this.parentActivity.startActivityForResult(takePictureIntent, 0);
            }
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
    }

    public void openGallery() {
        try {
            Intent photoPickerIntent = new Intent("android.intent.action.PICK");
            photoPickerIntent.setType("image/*");
            if (this.parentFragment != null) {
                this.parentFragment.startActivityForResult(photoPickerIntent, 1);
            } else if (this.parentActivity != null) {
                this.parentActivity.startActivityForResult(photoPickerIntent, 1);
            }
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
    }

    private void startCrop(String path) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 800);
            cropIntent.putExtra("outputY", 800);
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra("return-data", false);
            this.picturePath = Utilities.generatePicturePath();
            cropIntent.putExtra("output", Uri.fromFile(this.picturePath));
            cropIntent.putExtra("output", Uri.fromFile(this.picturePath));
            if (this.parentFragment != null) {
                this.parentFragment.startActivityForResult(cropIntent, 2);
            } else if (this.parentActivity != null) {
                this.parentActivity.startActivityForResult(cropIntent, 2);
            }
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
            processBitmap(FileLoader.loadBitmap(path, 800.0f, 800.0f));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != -1) {
            return;
        }
        if (requestCode == 0) {
            Utilities.addMediaToGallery(this.currentPicturePath);
            startCrop(this.currentPicturePath);
            this.currentPicturePath = null;
        } else if (requestCode == 1) {
            if (data != null) {
                Uri imageUri = data.getData();
                Cursor cursor = null;
                try {
                    if (this.parentFragment != null) {
                        cursor = this.parentFragment.getActivity().getContentResolver().query(imageUri, new String[]{"_data"}, null, null, null);
                    } else if (this.parentActivity != null) {
                        cursor = this.parentActivity.getContentResolver().query(imageUri, new String[]{"_data"}, null, null, null);
                    }
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            startCrop(cursor.getString(0));
                        }
                        cursor.close();
                    }
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        } else if (requestCode == 2) {
            processBitmap(FileLoader.loadBitmap(this.picturePath.getAbsolutePath(), 800.0f, 800.0f));
        }
    }

    private void processBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            this.smallPhoto = FileLoader.scaleAndSaveImage(bitmap, 100.0f, 100.0f, 87, false);
            this.bigPhoto = FileLoader.scaleAndSaveImage(bitmap, 800.0f, 800.0f, 87, false);
            if (this.bigPhoto != null && this.smallPhoto != null) {
                if (!this.returnOnly) {
                    UserConfig.saveConfig(false);
                    this.uploadingAvatar = Utilities.getCacheDir() + "/" + this.bigPhoto.location.volume_id + "_" + this.bigPhoto.location.local_id + ".jpg";
                    NotificationCenter.Instance.addObserver(this, FileLoader.FileDidUpload);
                    NotificationCenter.Instance.addObserver(this, 10001);
                    FileLoader.Instance.uploadFile(this.uploadingAvatar, null, null);
                } else if (this.delegate != null) {
                    this.delegate.didUploadedPhoto(null, this.smallPhoto, this.bigPhoto);
                }
            }
        }
    }

    public void didReceivedNotification(int id, final Object... args) {
        String location;
        if (id == FileLoader.FileDidUpload) {
            location = args[0];
            if (this.uploadingAvatar != null && location.equals(this.uploadingAvatar)) {
                Utilities.RunOnUIThread(new Runnable() {
                    public void run() {
                        NotificationCenter.Instance.removeObserver(AvatarUpdater.this, FileLoader.FileDidUpload);
                        NotificationCenter.Instance.removeObserver(AvatarUpdater.this, 10001);
                        if (AvatarUpdater.this.delegate != null) {
                            AvatarUpdater.this.delegate.didUploadedPhoto((InputFile) args[1], AvatarUpdater.this.smallPhoto, AvatarUpdater.this.bigPhoto);
                        }
                        AvatarUpdater.this.uploadingAvatar = null;
                        if (AvatarUpdater.this.clearAfterUpdate) {
                            AvatarUpdater.this.parentFragment = null;
                            AvatarUpdater.this.parentActivity = null;
                            AvatarUpdater.this.delegate = null;
                        }
                    }
                });
            }
        } else if (id == 10001) {
            location = (String) args[0];
            if (this.uploadingAvatar != null && location.equals(this.uploadingAvatar)) {
                Utilities.RunOnUIThread(new C06012());
            }
        }
    }
}
