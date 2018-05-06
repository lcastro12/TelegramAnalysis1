package org.telegram.messenger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.support.v4.view.MotionEventCompat;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.zip.GZIPInputStream;
import javax.crypto.Cipher;
import org.telegram.TL.TLClassStore;
import org.telegram.TL.TLObject;
import org.telegram.ui.ApplicationLoader;

public class Utilities {
    private static final String TAG = "Typefaces";
    public static Handler applicationHandler;
    public static int[] arrColors = new int[]{-1160920, -12474109, -2058750, -15756051, -7390217, -244864, -16735804, -1347582};
    public static int[] arrGroupsAvatars = new int[]{C0419R.drawable.group_green, C0419R.drawable.group_red, C0419R.drawable.group_blue, C0419R.drawable.group_yellow};
    public static int[] arrUsersAvatars = new int[]{C0419R.drawable.user_red, C0419R.drawable.user_green, C0419R.drawable.user_yellow, C0419R.drawable.user_blue, C0419R.drawable.user_violet, C0419R.drawable.user_pink, C0419R.drawable.user_aqua, C0419R.drawable.user_orange};
    private static final Hashtable<String, Typeface> cache = new Hashtable();
    public static DispatchQueue cacheOutQueue = new DispatchQueue("cacheOutQueue");
    public static FastDateFormat chatDate;
    public static FastDateFormat chatFullDate;
    static final Class<?>[] constructorSignature = new Class[]{Context.class, AttributeSet.class};
    public static float density;
    public static int externalCacheNotAvailableState = 0;
    public static DispatchQueue fileUploadQueue = new DispatchQueue("fileUploadQueue");
    public static FastDateFormat formatterDay;
    public static FastDateFormat formatterMonth;
    public static FastDateFormat formatterWeek;
    public static FastDateFormat formatterYear;
    public static FastDateFormat formatterYearMax;
    public static DispatchQueue globalQueue = new DispatchQueue("globalQueue");
    public static ArrayList<String> goodPrimes = new ArrayList();
    protected static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static DispatchQueue imageLoadQueue = new DispatchQueue("imageLoadQueue");
    public static boolean isRTL = false;
    private static final Integer lock = Integer.valueOf(1);
    public static ProgressDialog progressDialog;
    public static DispatchQueue stageQueue = new DispatchQueue("stageQueue");
    public static int statusBarHeight = 0;

    static class C04351 implements Runnable {
        C04351() {
        }

        public void run() {
            try {
                SerializedData data = new SerializedData();
                data.writeInt32(Utilities.goodPrimes.size());
                Iterator i$ = Utilities.goodPrimes.iterator();
                while (i$.hasNext()) {
                    data.writeString((String) i$.next());
                }
                byte[] bytes = data.toByteArray();
                Editor editor = ApplicationLoader.applicationContext.getSharedPreferences("primes", 0).edit();
                editor.putString("primes", Base64.encodeToString(bytes, 0));
                editor.commit();
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            }
        }
    }

    static class C04373 implements Runnable {
        C04373() {
        }

        public void run() {
            if (Utilities.progressDialog != null) {
                Utilities.progressDialog.dismiss();
            }
        }
    }

    public static class TPFactorizedValue {
        public long f49p;
        public long f50q;
    }

    public static native byte[] aesIgeEncryption(byte[] bArr, byte[] bArr2, byte[] bArr3, boolean z, boolean z2);

    public static native long doPQNative(long j);

    static {
        density = 1.0f;
        density = ApplicationLoader.applicationContext.getResources().getDisplayMetrics().density;
        String primes = ApplicationLoader.applicationContext.getSharedPreferences("primes", 0).getString("primes", null);
        if (primes == null) {
            goodPrimes.add("C71CAEB9C6B1C9048E6C522F70F13F73980D40238E3E21C14934D037563D930F48198A0AA7C14058229493D22530F4DBFA336F6E0AC925139543AED44CCE7C3720FD51F69458705AC68CD4FE6B6B13ABDC9746512969328454F18FAF8C595F642477FE96BB2A941D5BCD1D4AC8CC49880708FA9B378E3C4F3A9060BEE67CF9A4A4A695811051907E162753B56B0F6B410DBA74D8A84B2A14B3144E0EF1284754FD17ED950D5965B4B9DD46582DB1178D169C6BC465B0D6FF9CA3928FEF5B9AE4E418FC15E83EBEA0F87FA9FF5EED70050DED2849F47BF959D956850CE929851F0D8115F635B105EE2E4E15D04B2454BF6F4FADF034B10403119CD8E3B92FCC5B");
        } else {
            try {
                byte[] bytes = Base64.decode(primes, 0);
                if (bytes != null) {
                    SerializedData data = new SerializedData(bytes);
                    int count = data.readInt32();
                    for (int a = 0; a < count; a++) {
                        goodPrimes.add(data.readString());
                    }
                }
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
                goodPrimes.clear();
                goodPrimes.add("C71CAEB9C6B1C9048E6C522F70F13F73980D40238E3E21C14934D037563D930F48198A0AA7C14058229493D22530F4DBFA336F6E0AC925139543AED44CCE7C3720FD51F69458705AC68CD4FE6B6B13ABDC9746512969328454F18FAF8C595F642477FE96BB2A941D5BCD1D4AC8CC49880708FA9B378E3C4F3A9060BEE67CF9A4A4A695811051907E162753B56B0F6B410DBA74D8A84B2A14B3144E0EF1284754FD17ED950D5965B4B9DD46582DB1178D169C6BC465B0D6FF9CA3928FEF5B9AE4E418FC15E83EBEA0F87FA9FF5EED70050DED2849F47BF959D956850CE929851F0D8115F635B105EE2E4E15D04B2454BF6F4FADF034B10403119CD8E3B92FCC5B");
            }
        }
        System.loadLibrary("tmessages");
        recreateFormatters();
    }

    public static File getCacheDir() {
        if (externalCacheNotAvailableState == 1 || (externalCacheNotAvailableState == 0 && Environment.getExternalStorageState().startsWith("mounted"))) {
            externalCacheNotAvailableState = 1;
            return ApplicationLoader.applicationContext.getExternalCacheDir();
        }
        externalCacheNotAvailableState = 2;
        return ApplicationLoader.applicationContext.getCacheDir();
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[(bytes.length * 2)];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & MotionEventCompat.ACTION_MASK;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 15];
        }
        return new String(hexChars);
    }

    public static int dp(int value) {
        return (int) (density * ((float) value));
    }

    public static boolean isGoodPrime(byte[] prime, int g) {
        if (g < 2 || g > 7) {
            return false;
        }
        if (prime.length != 256 || prime[0] >= (byte) 0) {
            return false;
        }
        String hex = bytesToHex(prime);
        Iterator i$ = goodPrimes.iterator();
        while (i$.hasNext()) {
            if (((String) i$.next()).equals(hex)) {
                return true;
            }
        }
        BigInteger dhBI = new BigInteger(1, prime);
        if (g == 2) {
            if (dhBI.mod(BigInteger.valueOf(8)).intValue() != 7) {
                return false;
            }
        } else if (g == 3) {
            if (dhBI.mod(BigInteger.valueOf(3)).intValue() != 2) {
                return false;
            }
        } else if (g == 5) {
            val = dhBI.mod(BigInteger.valueOf(5)).intValue();
            if (!(val == 1 || val == 4)) {
                return false;
            }
        } else if (g == 6) {
            val = dhBI.mod(BigInteger.valueOf(24)).intValue();
            if (!(val == 19 || val == 23)) {
                return false;
            }
        } else if (g == 7) {
            val = dhBI.mod(BigInteger.valueOf(7)).intValue();
            if (!(val == 3 || val == 5 || val == 6)) {
                return false;
            }
        }
        BigInteger dhBI2 = dhBI.subtract(BigInteger.valueOf(1)).divide(BigInteger.valueOf(2));
        if (!dhBI.isProbablePrime(30) || !dhBI2.isProbablePrime(30)) {
            return false;
        }
        goodPrimes.add(hex);
        globalQueue.postRunnable(new C04351());
        return true;
    }

    public static boolean isGoodGaAndGb(BigInteger g_a, BigInteger p) {
        return g_a.compareTo(BigInteger.valueOf(1)) == 1 && g_a.compareTo(p.subtract(BigInteger.valueOf(1))) == -1;
    }

    public static TPFactorizedValue getFactorizedValue(long what) {
        long g = doPQNative(what);
        if (g <= 1 || g >= what) {
            FileLog.m800e("tmessages", String.format("**** Factorization failed for %d", new Object[]{Long.valueOf(what)}));
            TPFactorizedValue result = new TPFactorizedValue();
            result.f49p = 0;
            result.f50q = 0;
            return result;
        }
        long p1 = g;
        long p2 = what / g;
        if (p1 > p2) {
            long tmp = p1;
            p1 = p2;
            p2 = tmp;
        }
        result = new TPFactorizedValue();
        result.f49p = p1;
        result.f50q = p2;
        return result;
    }

    public static byte[] computeSHA1(byte[] convertme, int offset, int len) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(convertme, offset, len);
            return md.digest();
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
            return null;
        }
    }

    public static byte[] computeSHA1(byte[] convertme) {
        try {
            return MessageDigest.getInstance("SHA-1").digest(convertme);
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
            return null;
        }
    }

    public static byte[] encryptWithRSA(BigInteger[] key, byte[] data) {
        try {
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(key[0], key[1]));
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(1, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
            return null;
        }
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    public static int bytesToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    public static MessageKeyData generateMessageKeyData(byte[] authKey, byte[] messageKey, boolean incoming) {
        MessageKeyData keyData = new MessageKeyData();
        if (authKey == null || authKey.length == 0) {
            keyData.aesIv = null;
            keyData.aesKey = null;
        } else {
            int x = incoming ? 8 : 0;
            SerializedData data = new SerializedData();
            data.writeRaw(messageKey);
            data.writeRaw(authKey, x, 32);
            byte[] sha1_a = computeSHA1(data.toByteArray());
            data = new SerializedData();
            data.writeRaw(authKey, x + 32, 16);
            data.writeRaw(messageKey);
            data.writeRaw(authKey, x + 48, 16);
            byte[] sha1_b = computeSHA1(data.toByteArray());
            data = new SerializedData();
            data.writeRaw(authKey, x + 64, 32);
            data.writeRaw(messageKey);
            byte[] sha1_c = computeSHA1(data.toByteArray());
            data = new SerializedData();
            data.writeRaw(messageKey);
            data.writeRaw(authKey, x + 96, 32);
            byte[] sha1_d = computeSHA1(data.toByteArray());
            SerializedData aesKey = new SerializedData();
            aesKey.writeRaw(sha1_a, 0, 8);
            aesKey.writeRaw(sha1_b, 8, 12);
            aesKey.writeRaw(sha1_c, 4, 12);
            keyData.aesKey = aesKey.toByteArray();
            SerializedData aesIv = new SerializedData();
            aesIv.writeRaw(sha1_a, 8, 12);
            aesIv.writeRaw(sha1_b, 0, 8);
            aesIv.writeRaw(sha1_c, 16, 4);
            aesIv.writeRaw(sha1_d, 0, 8);
            keyData.aesIv = aesIv.toByteArray();
        }
        return keyData;
    }

    public static TLObject decompress(byte[] data, TLObject parentObject) {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        try {
            GZIPInputStream gis = new GZIPInputStream(is, 512);
            ByteArrayOutputStream bytesOutput = new ByteArrayOutputStream();
            data = new byte[512];
            while (true) {
                int bytesRead = gis.read(data);
                if (bytesRead != -1) {
                    bytesOutput.write(data, 0, bytesRead);
                } else {
                    gis.close();
                    is.close();
                    SerializedData stream = new SerializedData(bytesOutput.toByteArray());
                    return TLClassStore.Instance().TLdeserialize(stream, stream.readInt32(), parentObject);
                }
            }
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
            return null;
        }
    }

    public static Typeface getTypeface(String assetPath) {
        Typeface typeface;
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    cache.put(assetPath, Typeface.createFromAsset(ApplicationLoader.applicationContext.getAssets(), assetPath));
                } catch (Exception e) {
                    FileLog.m800e(TAG, "Could not get typeface '" + assetPath + "' because " + e.getMessage());
                    typeface = null;
                }
            }
            typeface = (Typeface) cache.get(assetPath);
        }
        return typeface;
    }

    public static void showKeyboard(View view) {
        if (view != null) {
            ((InputMethodManager) view.getContext().getSystemService("input_method")).showSoftInput(view, 1);
            ((InputMethodManager) view.getContext().getSystemService("input_method")).showSoftInput(view, 0);
        }
    }

    public static boolean isKeyboardShowed(View view) {
        if (view == null) {
            return false;
        }
        return ((InputMethodManager) view.getContext().getSystemService("input_method")).isActive(view);
    }

    public static void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService("input_method");
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void ShowProgressDialog(final Activity activity, final String message) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (!activity.isFinishing()) {
                    Utilities.progressDialog = new ProgressDialog(activity);
                    if (message != null) {
                        Utilities.progressDialog.setMessage(message);
                    }
                    Utilities.progressDialog.setCanceledOnTouchOutside(false);
                    Utilities.progressDialog.setCancelable(false);
                    Utilities.progressDialog.show();
                }
            }
        });
    }

    public static void recreateFormatters() {
        Locale locale = Locale.getDefault();
        String lang = locale.getLanguage();
        boolean z = lang != null && lang.toLowerCase().equals("ar");
        isRTL = z;
        if (lang.equals("en")) {
            formatterMonth = FastDateFormat.getInstance("MMM dd", locale);
            formatterYear = FastDateFormat.getInstance("dd.MM.yy", locale);
            formatterYearMax = FastDateFormat.getInstance("dd.MM.yyyy", locale);
            chatDate = FastDateFormat.getInstance("MMMM d", locale);
            chatFullDate = FastDateFormat.getInstance("MMMM d, yyyy", locale);
        } else {
            formatterMonth = FastDateFormat.getInstance("dd MMM", locale);
            formatterYear = FastDateFormat.getInstance("dd.MM.yy", locale);
            formatterYearMax = FastDateFormat.getInstance("dd.MM.yyyy", locale);
            chatDate = FastDateFormat.getInstance("d MMMM", locale);
            chatFullDate = FastDateFormat.getInstance("d MMMM yyyy", locale);
        }
        formatterWeek = FastDateFormat.getInstance("EEE", locale);
        if (lang == null) {
            formatterDay = FastDateFormat.getInstance("h:mm a", Locale.US);
        } else if (DateFormat.is24HourFormat(ApplicationLoader.applicationContext)) {
            formatterDay = FastDateFormat.getInstance("HH:mm", locale);
        } else if (lang.toLowerCase().equals("ar")) {
            formatterDay = FastDateFormat.getInstance("h:mm a", locale);
        } else {
            formatterDay = FastDateFormat.getInstance("h:mm a", Locale.US);
        }
    }

    public static String formatDateChat(long date) {
        Calendar rightNow = Calendar.getInstance();
        int year = rightNow.get(1);
        rightNow.setTimeInMillis(date * 1000);
        if (year == rightNow.get(1)) {
            return chatDate.format(date * 1000);
        }
        return chatFullDate.format(date * 1000);
    }

    public static String formatDate(long date) {
        Calendar rightNow = Calendar.getInstance();
        int day = rightNow.get(6);
        int year = rightNow.get(1);
        rightNow.setTimeInMillis(date * 1000);
        int dateDay = rightNow.get(6);
        int dateYear = rightNow.get(1);
        if (dateDay == day && year == dateYear) {
            return formatterDay.format(new Date(1000 * date));
        }
        if (dateDay + 1 == day && year == dateYear) {
            return ApplicationLoader.applicationContext.getResources().getString(C0419R.string.Yesterday);
        }
        if (year == dateYear) {
            return formatterMonth.format(new Date(1000 * date));
        }
        return formatterYear.format(new Date(1000 * date));
    }

    public static String formatDateOnline(long date) {
        Calendar rightNow = Calendar.getInstance();
        int day = rightNow.get(6);
        int year = rightNow.get(1);
        rightNow.setTimeInMillis(1000 * date);
        int dateDay = rightNow.get(6);
        int dateYear = rightNow.get(1);
        if (dateDay == day && year == dateYear) {
            return String.format("%s %s", new Object[]{ApplicationLoader.applicationContext.getResources().getString(C0419R.string.TodayAt), formatterDay.format(new Date(1000 * date))});
        } else if (dateDay + 1 == day && year == dateYear) {
            return String.format("%s %s", new Object[]{ApplicationLoader.applicationContext.getResources().getString(C0419R.string.YesterdayAt), formatterDay.format(new Date(1000 * date))});
        } else if (year == dateYear) {
            return String.format("%s %s %s", new Object[]{formatterMonth.format(new Date(1000 * date)), ApplicationLoader.applicationContext.getResources().getString(C0419R.string.OtherAt), formatterDay.format(new Date(1000 * date))});
        } else {
            return String.format("%s %s %s", new Object[]{formatterYear.format(new Date(1000 * date)), ApplicationLoader.applicationContext.getResources().getString(C0419R.string.OtherAt), formatterDay.format(new Date(1000 * date))});
        }
    }

    public static void HideProgressDialog(Activity activity) {
        activity.runOnUiThread(new C04373());
    }

    public static boolean copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        boolean result = true;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
            result = false;
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        } catch (Throwable th) {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
        return result;
    }

    public static void RunOnUIThread(Runnable runnable) {
        synchronized (lock) {
            if (applicationHandler == null) {
                applicationHandler = new Handler(ApplicationLoader.applicationContext.getMainLooper());
            }
            applicationHandler.post(runnable);
        }
    }

    public static int getColorIndex(int id) {
        int[] arr;
        String str;
        if (id >= 0) {
            arr = arrUsersAvatars;
        } else {
            arr = arrGroupsAvatars;
        }
        if (id >= 0) {
            try {
                str = String.format(Locale.US, "%d%d", new Object[]{Integer.valueOf(id), Integer.valueOf(UserConfig.clientUserId)});
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
                return id % arr.length;
            }
        }
        str = String.format(Locale.US, "%d", new Object[]{Integer.valueOf(id)});
        if (str.length() > 15) {
            str = str.substring(0, 15);
        }
        int b = MessageDigest.getInstance("MD5").digest(str.getBytes())[Math.abs(id % 16)];
        if (b < 0) {
            b += 256;
        }
        return Math.abs(b) % arr.length;
    }

    public static int getColorForId(int id) {
        if (id == 333000) {
            return -15756051;
        }
        return arrColors[getColorIndex(id)];
    }

    public static int getUserAvatarForId(int id) {
        if (id == 333000) {
            return C0419R.drawable.telegram_avatar;
        }
        return arrUsersAvatars[getColorIndex(id)];
    }

    public static int getGroupAvatarForId(int id) {
        return arrGroupsAvatars[getColorIndex(-id)];
    }

    public static String MD5(String md5) {
        try {
            byte[] array = MessageDigest.getInstance("MD5").digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & MotionEventCompat.ACTION_MASK) | 256).substring(1, 3));
            }
            return sb.toString();
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
            return null;
        }
    }

    public static void addMediaToGallery(String fromPath) {
        if (fromPath != null) {
            Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            mediaScanIntent.setData(Uri.fromFile(new File(fromPath)));
            ApplicationLoader.applicationContext.sendBroadcast(mediaScanIntent);
        }
    }

    public static void addMediaToGallery(Uri uri) {
        if (uri != null) {
            Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            mediaScanIntent.setData(uri);
            ApplicationLoader.applicationContext.sendBroadcast(mediaScanIntent);
        }
    }

    private static File getAlbumDir() {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ApplicationLoader.applicationContext.getResources().getString(C0419R.string.AppName));
            if (storageDir == null || storageDir.mkdirs() || storageDir.exists()) {
                return storageDir;
            }
            FileLog.m798d("tmessages", "failed to create directory");
            return null;
        }
        FileLog.m798d("tmessages", "External storage is not mounted READ/WRITE.");
        return null;
    }

    public static String getPath(Context context, Uri uri) {
        boolean isKitKat;
        if (VERSION.SDK_INT >= 19) {
            isKitKat = true;
        } else {
            isKitKat = false;
        }
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            String[] split;
            if (isExternalStorageDocument(uri)) {
                split = DocumentsContract.getDocumentId(uri).split(":");
                if ("primary".equalsIgnoreCase(split[0])) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                return null;
            } else if (isDownloadsDocument(uri)) {
                return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
            } else if (!isMediaDocument(uri)) {
                return null;
            } else {
                String type = DocumentsContract.getDocumentId(uri).split(":")[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = "_id=?";
                return getDataColumn(context, contentUri, "_id=?", new String[]{split[1]});
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        } else {
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
            return null;
        }
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, selection, selectionArgs, null);
            if (cursor == null || !cursor.moveToFirst()) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            String string = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
            return string;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static File generatePicturePath() {
        try {
            return File.createTempFile("IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_", ".jpg", getAlbumDir());
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
            return null;
        }
    }

    public static CharSequence generateSearchName(String name, String name2, String q) {
        if (name == null && name2 == null) {
            return BuildConfig.FLAVOR;
        }
        CharSequence builder = new SpannableStringBuilder();
        String wholeString = name;
        if (wholeString == null || wholeString.length() == 0) {
            wholeString = name2;
        } else if (!(name2 == null || name2.length() == 0)) {
            wholeString = wholeString + " " + name2;
        }
        for (String str : wholeString.trim().split(" ")) {
            if (str != null) {
                if (str.toLowerCase().startsWith(q)) {
                    if (builder.length() != 0) {
                        builder.append(" ");
                    }
                    builder.append(Html.fromHtml("<font color=\"#357aa8\">" + str.substring(0, q.length()) + "</font>"));
                    builder.append(str.substring(q.length()));
                } else {
                    if (builder.length() != 0) {
                        builder.append(" ");
                    }
                    builder.append(str);
                }
            }
        }
        return builder;
    }

    public static File generateVideoPath() {
        try {
            return File.createTempFile("VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_", ".mp4", getAlbumDir());
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
            return null;
        }
    }

    public static String formatName(String firstName, String lastName) {
        String result = firstName;
        if (result == null || result.length() == 0) {
            return lastName;
        }
        if (result.length() == 0 || lastName.length() == 0) {
            return result;
        }
        return result + " " + lastName;
    }

    public static String formatFileSize(long size) {
        if (size < 1024) {
            return String.format("%d B", new Object[]{Long.valueOf(size)});
        } else if (size < 1048576) {
            return String.format("%.1f KB", new Object[]{Float.valueOf(((float) size) / 1024.0f)});
        } else if (size < 1073741824) {
            return String.format("%.1f MB", new Object[]{Float.valueOf((((float) size) / 1024.0f) / 1024.0f)});
        } else {
            return String.format("%.1f GB", new Object[]{Float.valueOf(((((float) size) / 1024.0f) / 1024.0f) / 1024.0f)});
        }
    }

    public static String stringForMessageListDate(long date) {
        Calendar rightNow = Calendar.getInstance();
        int day = rightNow.get(6);
        int year = rightNow.get(1);
        rightNow.setTimeInMillis(date * 1000);
        int dateDay = rightNow.get(6);
        if (year != rightNow.get(1)) {
            return formatterYear.format(new Date(date * 1000));
        }
        int dayDiff = dateDay - day;
        if (dayDiff == 0 || (dayDiff == -1 && ((long) ((int) (System.currentTimeMillis() / 1000))) - date < 28800)) {
            return formatterDay.format(new Date(date * 1000));
        }
        if (dayDiff <= -7 || dayDiff > -1) {
            return formatterMonth.format(new Date(date * 1000));
        }
        return formatterWeek.format(new Date(date * 1000));
    }
}
