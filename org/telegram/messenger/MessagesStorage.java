package org.telegram.messenger;

import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.TL.TLClassStore;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.Chat;
import org.telegram.TL.TLRPC.ChatParticipants;
import org.telegram.TL.TLRPC.EncryptedChat;
import org.telegram.TL.TLRPC.Message;
import org.telegram.TL.TLRPC.Photo;
import org.telegram.TL.TLRPC.TL_chatParticipant;
import org.telegram.TL.TLRPC.TL_contact;
import org.telegram.TL.TLRPC.TL_dialog;
import org.telegram.TL.TLRPC.TL_messageMediaPhoto;
import org.telegram.TL.TLRPC.TL_messageMediaVideo;
import org.telegram.TL.TLRPC.TL_messages_messages;
import org.telegram.TL.TLRPC.TL_photoEmpty;
import org.telegram.TL.TLRPC.User;
import org.telegram.TL.TLRPC.UserStatus;
import org.telegram.TL.TLRPC.WallPaper;
import org.telegram.TL.TLRPC.messages_Dialogs;
import org.telegram.TL.TLRPC.messages_Messages;
import org.telegram.TL.TLRPC.photos_Photos;
import org.telegram.ui.ApplicationLoader;

public class MessagesStorage {
    public static MessagesStorage Instance = new MessagesStorage();
    public static int lastDateValue = 0;
    public static int lastPtsValue = 0;
    public static int lastQtsValue = 0;
    public static int lastSecretVersion = 0;
    public static int lastSeqValue = 0;
    public static int secretG = 0;
    public static byte[] secretPBytes = null;
    public static final int wallpapersDidLoaded = 171;
    private File cacheFile;
    private SQLiteDatabase database;
    public DispatchQueue storageQueue = new DispatchQueue("storageQueue");

    class C04101 implements Runnable {
        C04101() {
        }

        public void run() {
            MessagesStorage.lastDateValue = 0;
            MessagesStorage.lastSeqValue = 0;
            MessagesStorage.lastPtsValue = 0;
            MessagesStorage.lastQtsValue = 0;
            MessagesStorage.lastSecretVersion = 0;
            MessagesStorage.secretPBytes = null;
            MessagesStorage.secretG = 0;
            if (MessagesStorage.this.database != null) {
                MessagesStorage.this.database.close();
                MessagesStorage.this.database = null;
            }
            if (MessagesStorage.this.cacheFile != null) {
                MessagesStorage.this.cacheFile.delete();
                MessagesStorage.this.cacheFile = null;
            }
            try {
                File old = new File(ApplicationLoader.applicationContext.getFilesDir(), "cache.db");
                if (old.exists()) {
                    old.delete();
                }
                old = new File(ApplicationLoader.applicationContext.getFilesDir(), "cache2.db");
                if (old.exists()) {
                    old.delete();
                }
                old = new File(ApplicationLoader.applicationContext.getFilesDir(), "cache3.db");
                if (old.exists()) {
                    old.delete();
                }
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            }
            MessagesStorage.this.openDatabase();
        }
    }

    class C04145 implements Runnable {
        C04145() {
        }

        public void run() {
            try {
                SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized("SELECT data FROM wallpapers WHERE 1", new Object[0]);
                ArrayList<WallPaper> wallPapers = new ArrayList();
                while (cursor.next()) {
                    byte[] bytes = cursor.byteArrayValue(0);
                    if (bytes != null) {
                        SerializedData data = new SerializedData(bytes);
                        wallPapers.add((WallPaper) TLClassStore.Instance().TLdeserialize(data, data.readInt32()));
                    }
                }
                cursor.dispose();
                NotificationCenter.Instance.postNotificationName(MessagesStorage.wallpapersDidLoaded, wallPapers);
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            }
        }
    }

    private java.lang.Integer updateMessageStateAndIdInternal(long r11, java.lang.Integer r13, int r14, int r15) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r10 = this;
        if (r13 == 0) goto L_0x003b;
    L_0x0002:
        r4 = r13.intValue();
        if (r4 != r14) goto L_0x003b;
    L_0x0008:
        if (r15 == 0) goto L_0x003b;
    L_0x000a:
        r3 = 0;
        r4 = r10.database;	 Catch:{ Exception -> 0x0028, all -> 0x0034 }
        r5 = "UPDATE messages SET send_state = 0, date = ? WHERE mid = ?";	 Catch:{ Exception -> 0x0028, all -> 0x0034 }
        r3 = r4.executeFast(r5);	 Catch:{ Exception -> 0x0028, all -> 0x0034 }
        r4 = 1;	 Catch:{ Exception -> 0x0028, all -> 0x0034 }
        r3.bindInteger(r4, r15);	 Catch:{ Exception -> 0x0028, all -> 0x0034 }
        r4 = 2;	 Catch:{ Exception -> 0x0028, all -> 0x0034 }
        r3.bindInteger(r4, r14);	 Catch:{ Exception -> 0x0028, all -> 0x0034 }
        r3.step();	 Catch:{ Exception -> 0x0028, all -> 0x0034 }
        if (r3 == 0) goto L_0x0023;
    L_0x0020:
        r3.dispose();
    L_0x0023:
        r2 = java.lang.Integer.valueOf(r14);
    L_0x0027:
        return r2;
    L_0x0028:
        r1 = move-exception;
        r4 = "tmessages";	 Catch:{ Exception -> 0x0028, all -> 0x0034 }
        org.telegram.messenger.FileLog.m799e(r4, r1);	 Catch:{ Exception -> 0x0028, all -> 0x0034 }
        if (r3 == 0) goto L_0x0023;
    L_0x0030:
        r3.dispose();
        goto L_0x0023;
    L_0x0034:
        r4 = move-exception;
        if (r3 == 0) goto L_0x003a;
    L_0x0037:
        r3.dispose();
    L_0x003a:
        throw r4;
    L_0x003b:
        r2 = r13;
        if (r2 != 0) goto L_0x006e;
    L_0x003e:
        r0 = 0;
        r4 = r10.database;	 Catch:{ Exception -> 0x0072, all -> 0x007e }
        r5 = java.util.Locale.US;	 Catch:{ Exception -> 0x0072, all -> 0x007e }
        r6 = "SELECT mid FROM randoms WHERE random_id = %d LIMIT 1";	 Catch:{ Exception -> 0x0072, all -> 0x007e }
        r7 = 1;	 Catch:{ Exception -> 0x0072, all -> 0x007e }
        r7 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x0072, all -> 0x007e }
        r8 = 0;	 Catch:{ Exception -> 0x0072, all -> 0x007e }
        r9 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x0072, all -> 0x007e }
        r7[r8] = r9;	 Catch:{ Exception -> 0x0072, all -> 0x007e }
        r5 = java.lang.String.format(r5, r6, r7);	 Catch:{ Exception -> 0x0072, all -> 0x007e }
        r6 = 0;	 Catch:{ Exception -> 0x0072, all -> 0x007e }
        r6 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x0072, all -> 0x007e }
        r0 = r4.queryFinalized(r5, r6);	 Catch:{ Exception -> 0x0072, all -> 0x007e }
        r4 = r0.next();	 Catch:{ Exception -> 0x0072, all -> 0x007e }
        if (r4 == 0) goto L_0x0069;	 Catch:{ Exception -> 0x0072, all -> 0x007e }
    L_0x0060:
        r4 = 0;	 Catch:{ Exception -> 0x0072, all -> 0x007e }
        r4 = r0.intValue(r4);	 Catch:{ Exception -> 0x0072, all -> 0x007e }
        r2 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x0072, all -> 0x007e }
    L_0x0069:
        if (r0 == 0) goto L_0x006e;
    L_0x006b:
        r0.dispose();
    L_0x006e:
        if (r2 != 0) goto L_0x0085;
    L_0x0070:
        r2 = 0;
        goto L_0x0027;
    L_0x0072:
        r1 = move-exception;
        r4 = "tmessages";	 Catch:{ Exception -> 0x0072, all -> 0x007e }
        org.telegram.messenger.FileLog.m799e(r4, r1);	 Catch:{ Exception -> 0x0072, all -> 0x007e }
        if (r0 == 0) goto L_0x006e;
    L_0x007a:
        r0.dispose();
        goto L_0x006e;
    L_0x007e:
        r4 = move-exception;
        if (r0 == 0) goto L_0x0084;
    L_0x0081:
        r0.dispose();
    L_0x0084:
        throw r4;
    L_0x0085:
        r3 = 0;
        r4 = r10.database;	 Catch:{ Exception -> 0x00dd, all -> 0x00e9 }
        r5 = "UPDATE messages SET mid = ?, send_state = 0 WHERE mid = ?";	 Catch:{ Exception -> 0x00dd, all -> 0x00e9 }
        r3 = r4.executeFast(r5);	 Catch:{ Exception -> 0x00dd, all -> 0x00e9 }
        r4 = 1;	 Catch:{ Exception -> 0x00dd, all -> 0x00e9 }
        r3.bindInteger(r4, r14);	 Catch:{ Exception -> 0x00dd, all -> 0x00e9 }
        r4 = 2;	 Catch:{ Exception -> 0x00dd, all -> 0x00e9 }
        r5 = r2.intValue();	 Catch:{ Exception -> 0x00dd, all -> 0x00e9 }
        r3.bindInteger(r4, r5);	 Catch:{ Exception -> 0x00dd, all -> 0x00e9 }
        r3.step();	 Catch:{ Exception -> 0x00dd, all -> 0x00e9 }
        if (r3 == 0) goto L_0x00a2;
    L_0x009f:
        r3.dispose();
    L_0x00a2:
        r4 = r10.database;	 Catch:{ Exception -> 0x00f0, all -> 0x00fc }
        r5 = "UPDATE media SET mid = ? WHERE mid = ?";	 Catch:{ Exception -> 0x00f0, all -> 0x00fc }
        r3 = r4.executeFast(r5);	 Catch:{ Exception -> 0x00f0, all -> 0x00fc }
        r4 = 1;	 Catch:{ Exception -> 0x00f0, all -> 0x00fc }
        r3.bindInteger(r4, r14);	 Catch:{ Exception -> 0x00f0, all -> 0x00fc }
        r4 = 2;	 Catch:{ Exception -> 0x00f0, all -> 0x00fc }
        r5 = r2.intValue();	 Catch:{ Exception -> 0x00f0, all -> 0x00fc }
        r3.bindInteger(r4, r5);	 Catch:{ Exception -> 0x00f0, all -> 0x00fc }
        r3.step();	 Catch:{ Exception -> 0x00f0, all -> 0x00fc }
        if (r3 == 0) goto L_0x00be;
    L_0x00bb:
        r3.dispose();
    L_0x00be:
        r4 = r10.database;	 Catch:{ Exception -> 0x0103, all -> 0x0110 }
        r5 = "UPDATE dialogs SET last_mid = ? WHERE last_mid = ?";	 Catch:{ Exception -> 0x0103, all -> 0x0110 }
        r3 = r4.executeFast(r5);	 Catch:{ Exception -> 0x0103, all -> 0x0110 }
        r4 = 1;	 Catch:{ Exception -> 0x0103, all -> 0x0110 }
        r3.bindInteger(r4, r14);	 Catch:{ Exception -> 0x0103, all -> 0x0110 }
        r4 = 2;	 Catch:{ Exception -> 0x0103, all -> 0x0110 }
        r5 = r2.intValue();	 Catch:{ Exception -> 0x0103, all -> 0x0110 }
        r5 = (long) r5;	 Catch:{ Exception -> 0x0103, all -> 0x0110 }
        r3.bindLong(r4, r5);	 Catch:{ Exception -> 0x0103, all -> 0x0110 }
        r3.step();	 Catch:{ Exception -> 0x0103, all -> 0x0110 }
        if (r3 == 0) goto L_0x0027;
    L_0x00d8:
        r3.dispose();
        goto L_0x0027;
    L_0x00dd:
        r1 = move-exception;
        r4 = "tmessages";	 Catch:{ Exception -> 0x00dd, all -> 0x00e9 }
        org.telegram.messenger.FileLog.m799e(r4, r1);	 Catch:{ Exception -> 0x00dd, all -> 0x00e9 }
        if (r3 == 0) goto L_0x00a2;
    L_0x00e5:
        r3.dispose();
        goto L_0x00a2;
    L_0x00e9:
        r4 = move-exception;
        if (r3 == 0) goto L_0x00ef;
    L_0x00ec:
        r3.dispose();
    L_0x00ef:
        throw r4;
    L_0x00f0:
        r1 = move-exception;
        r4 = "tmessages";	 Catch:{ Exception -> 0x00f0, all -> 0x00fc }
        org.telegram.messenger.FileLog.m799e(r4, r1);	 Catch:{ Exception -> 0x00f0, all -> 0x00fc }
        if (r3 == 0) goto L_0x00be;
    L_0x00f8:
        r3.dispose();
        goto L_0x00be;
    L_0x00fc:
        r4 = move-exception;
        if (r3 == 0) goto L_0x0102;
    L_0x00ff:
        r3.dispose();
    L_0x0102:
        throw r4;
    L_0x0103:
        r1 = move-exception;
        r4 = "tmessages";	 Catch:{ Exception -> 0x0103, all -> 0x0110 }
        org.telegram.messenger.FileLog.m799e(r4, r1);	 Catch:{ Exception -> 0x0103, all -> 0x0110 }
        if (r3 == 0) goto L_0x0027;
    L_0x010b:
        r3.dispose();
        goto L_0x0027;
    L_0x0110:
        r4 = move-exception;
        if (r3 == 0) goto L_0x0116;
    L_0x0113:
        r3.dispose();
    L_0x0116:
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.updateMessageStateAndIdInternal(long, java.lang.Integer, int, int):java.lang.Integer");
    }

    public MessagesStorage() {
        this.storageQueue.setPriority(10);
        openDatabase();
    }

    public void openDatabase() {
        this.cacheFile = new File(ApplicationLoader.applicationContext.getFilesDir(), "cache4.db");
        boolean createTable = false;
        if (!this.cacheFile.exists()) {
            createTable = true;
        }
        try {
            this.database = new SQLiteDatabase(this.cacheFile.getPath());
            if (createTable) {
                this.database.executeFast("CREATE TABLE users(uid INTEGER PRIMARY KEY, name TEXT, status INTEGER, data BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE messages(mid INTEGER PRIMARY KEY, uid INTEGER, read_state INTEGER, send_state INTEGER, date INTEGER, data BLOB, out INTEGER, ttl INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE chats(uid INTEGER PRIMARY KEY, name TEXT, data BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE enc_chats(uid INTEGER PRIMARY KEY, user INTEGER, name TEXT, data BLOB, g BLOB, authkey BLOB, ttl INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE dialogs(did INTEGER PRIMARY KEY, date INTEGER, unread_count INTEGER, last_mid INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE chat_settings(uid INTEGER PRIMARY KEY, participants BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE contacts(uid INTEGER PRIMARY KEY, mutual INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE pending_read(uid INTEGER PRIMARY KEY, max_id INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE media(mid INTEGER PRIMARY KEY, uid INTEGER, date INTEGER, data BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE media_counts(uid INTEGER PRIMARY KEY, count INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE wallpapers(uid INTEGER PRIMARY KEY, data BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE randoms(random_id INTEGER PRIMARY KEY, mid INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE enc_tasks(date INTEGER, data BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE params(id INTEGER PRIMARY KEY, seq INTEGER, pts INTEGER, date INTEGER, qts INTEGER, lsv INTEGER, sg INTEGER, pbytes BLOB)").stepThis().dispose();
                this.database.executeFast("INSERT INTO params VALUES(1, 0, 0, 0, 0, 0, 0, NULL)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE user_photos(uid INTEGER, id INTEGER, data BLOB, PRIMARY KEY (uid, id))").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS date_idx_dialogs ON dialogs(date);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS date_idx_enc_tasks ON enc_tasks(date);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS last_mid_idx_dialogs ON dialogs(last_mid);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_mid_idx_media ON media(uid, mid);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS mid_idx_media ON media(mid);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_date_mid_idx_media ON media(uid, date, mid);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_mid_idx_messages ON messages(uid, mid);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_date_mid_idx_messages ON messages(uid, date, mid);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS mid_out_idx_messages ON messages(mid, out);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS task_idx_messages ON messages(uid, out, read_state, ttl, date, send_state);").stepThis().dispose();
                return;
            }
            SQLiteCursor cursor = this.database.queryFinalized("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='params'", new Object[0]);
            boolean create = false;
            if (!cursor.next()) {
                create = true;
            } else if (cursor.intValue(0) == 0) {
                create = true;
            }
            cursor.dispose();
            if (create) {
                this.database.executeFast("CREATE TABLE params(id INTEGER PRIMARY KEY, seq INTEGER, pts INTEGER, date INTEGER, qts INTEGER, lsv INTEGER, sg INTEGER, pbytes BLOB)").stepThis().dispose();
                this.database.executeFast("INSERT INTO params VALUES(1, 0, 0, 0, 0, 0, 0, NULL)").stepThis().dispose();
            } else {
                cursor = this.database.queryFinalized("SELECT seq, pts, date, qts, lsv, sg, pbytes FROM params WHERE id = 1", new Object[0]);
                if (cursor.next()) {
                    lastSeqValue = cursor.intValue(0);
                    lastPtsValue = cursor.intValue(1);
                    lastDateValue = cursor.intValue(2);
                    lastQtsValue = cursor.intValue(3);
                    lastSecretVersion = cursor.intValue(4);
                    secretG = cursor.intValue(5);
                    if (cursor.isNull(6)) {
                        secretPBytes = null;
                    } else {
                        secretPBytes = cursor.byteArrayValue(6);
                        if (secretPBytes != null && secretPBytes.length == 1) {
                            secretPBytes = null;
                        }
                    }
                }
                cursor.dispose();
            }
            this.database.executeFast("CREATE TABLE IF NOT EXISTS user_photos(uid INTEGER, id INTEGER, data BLOB, PRIMARY KEY (uid, id))").stepThis().dispose();
            this.database.executeFast("CREATE INDEX IF NOT EXISTS mid_idx_media ON media(mid);").stepThis().dispose();
            this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_date_mid_idx_media ON media(uid, date, mid);").stepThis().dispose();
            this.database.executeFast("DROP INDEX IF EXISTS read_state_out_idx_messages;").stepThis().dispose();
            this.database.executeFast("DROP INDEX IF EXISTS ttl_idx_messages;").stepThis().dispose();
            this.database.executeFast("DROP INDEX IF EXISTS date_idx_messages;").stepThis().dispose();
            this.database.executeFast("CREATE INDEX IF NOT EXISTS mid_out_idx_messages ON messages(mid, out);").stepThis().dispose();
            this.database.executeFast("CREATE INDEX IF NOT EXISTS task_idx_messages ON messages(uid, out, read_state, ttl, date, send_state);").stepThis().dispose();
            this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_date_mid_idx_messages ON messages(uid, date, mid);").stepThis().dispose();
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
    }

    public void cleanUp() {
        this.storageQueue.postRunnable(new C04101());
    }

    public void saveSecretParams(final int lsv, final int sg, final byte[] pbytes) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("UPDATE params SET lsv = ?, sg = ?, pbytes = ? WHERE id = 1");
                    state.bindInteger(1, lsv);
                    state.bindInteger(2, sg);
                    if (pbytes != null) {
                        state.bindByteArray(3, pbytes);
                    } else {
                        state.bindByteArray(3, new byte[1]);
                    }
                    state.step();
                    state.dispose();
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        });
    }

    public void saveDiffParams(int seq, int pts, int date, int qts) {
        final int i = seq;
        final int i2 = pts;
        final int i3 = date;
        final int i4 = qts;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("UPDATE params SET seq = ?, pts = ?, date = ?, qts = ? WHERE id = 1");
                    state.bindInteger(1, i);
                    state.bindInteger(2, i2);
                    state.bindInteger(3, i3);
                    state.bindInteger(4, i4);
                    state.step();
                    state.dispose();
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        });
    }

    public void putWallpapers(final ArrayList<WallPaper> wallPapers) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    MessagesStorage.this.database.executeFast("DELETE FROM wallpapers WHERE 1").stepThis().dispose();
                    MessagesStorage.this.database.beginTransaction();
                    SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO wallpapers VALUES(?, ?)");
                    int num = 0;
                    Iterator i$ = wallPapers.iterator();
                    while (i$.hasNext()) {
                        WallPaper wallPaper = (WallPaper) i$.next();
                        state.requery();
                        SerializedData data = new SerializedData();
                        wallPaper.serializeToStream(data);
                        state.bindInteger(1, num);
                        state.bindByteArray(2, data.toByteArray());
                        state.step();
                        num++;
                    }
                    state.dispose();
                    MessagesStorage.this.database.commitTransaction();
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        });
    }

    public void getWallpapers() {
        this.storageQueue.postRunnable(new C04145());
    }

    public void deleteDialog(final long did, final boolean messagesOnly) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    if (!messagesOnly) {
                        MessagesStorage.this.database.executeFast("DELETE FROM dialogs WHERE did = " + did).stepThis().dispose();
                        MessagesStorage.this.database.executeFast("DELETE FROM chat_settings WHERE uid = " + did).stepThis().dispose();
                    }
                    MessagesStorage.this.database.executeFast("DELETE FROM media_counts WHERE uid = " + did).stepThis().dispose();
                    MessagesStorage.this.database.executeFast("DELETE FROM messages WHERE uid = " + did).stepThis().dispose();
                    MessagesStorage.this.database.executeFast("DELETE FROM media WHERE uid = " + did).stepThis().dispose();
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        });
    }

    public void getUserPhotos(int uid, int offset, int count, long max_id, int classGuid) {
        SQLiteCursor cursor;
        if (max_id != 0) {
            try {
                cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM user_photos WHERE uid = %d AND id < %d ORDER BY id DESC LIMIT %d", new Object[]{Integer.valueOf(uid), Long.valueOf(max_id), Integer.valueOf(count)}), new Object[0]);
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
                return;
            }
        }
        cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM user_photos WHERE uid = %d ORDER BY id DESC LIMIT %d,%d", new Object[]{Integer.valueOf(uid), Integer.valueOf(offset), Integer.valueOf(count)}), new Object[0]);
        final photos_Photos res = new photos_Photos();
        while (cursor.next()) {
            byte[] messageData = cursor.byteArrayValue(0);
            if (messageData != null) {
                SerializedData data = new SerializedData(messageData);
                res.photos.add((Photo) TLClassStore.Instance().TLdeserialize(data, data.readInt32()));
            }
        }
        cursor.dispose();
        final int i = uid;
        final int i2 = offset;
        final int i3 = count;
        final long j = max_id;
        final int i4 = classGuid;
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                MessagesController.Instance.processLoadedUserPhotos(res, i, i2, i3, j, true, i4);
            }
        });
    }

    public void clearUserPhotos(final int uid) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    MessagesStorage.this.database.executeFast("DELETE FROM user_photos WHERE uid = " + uid).stepThis().dispose();
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        });
    }

    public void putUserPhotos(final int uid, final photos_Photos photos) {
        if (photos != null && !photos.photos.isEmpty()) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    try {
                        SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO user_photos VALUES(?, ?, ?)");
                        Iterator i$ = photos.photos.iterator();
                        while (i$.hasNext()) {
                            Photo photo = (Photo) i$.next();
                            if (!(photo instanceof TL_photoEmpty)) {
                                state.requery();
                                SerializedData data = new SerializedData();
                                photo.serializeToStream(data);
                                state.bindInteger(1, uid);
                                state.bindLong(2, photo.id);
                                state.bindByteArray(3, data.toByteArray());
                                state.step();
                            }
                        }
                        state.dispose();
                    } catch (Exception e) {
                        FileLog.m799e("tmessages", e);
                    }
                }
            });
        }
    }

    public void getNewTask(final Long oldTask) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    if (oldTask != null) {
                        MessagesStorage.this.database.executeFast("DELETE FROM enc_tasks WHERE rowid = " + oldTask).stepThis().dispose();
                    }
                    Long taskId = null;
                    int date = 0;
                    ArrayList<Integer> arr = null;
                    SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized("SELECT rowid, date, data FROM enc_tasks ORDER BY date ASC LIMIT 1", new Object[0]);
                    if (cursor.next()) {
                        taskId = Long.valueOf(cursor.longValue(0));
                        date = cursor.intValue(1);
                        byte[] data = cursor.byteArrayValue(2);
                        SerializedData serializedData = new SerializedData(data);
                        arr = new ArrayList();
                        int count = data.length / 4;
                        for (int a = 0; a < count; a++) {
                            arr.add(Integer.valueOf(serializedData.readInt32()));
                        }
                    }
                    cursor.dispose();
                    MessagesController.Instance.processLoadedDeleteTask(taskId, date, arr);
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        });
    }

    public void createTaskForDate(int chat_id, int time, int readTime, int isOut) {
        final int i = chat_id;
        final int i2 = isOut;
        final int i3 = time;
        final int i4 = readTime;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                int minDate = Integer.MAX_VALUE;
                try {
                    ArrayList<Integer> arr;
                    SparseArray<ArrayList<Integer>> messages = new SparseArray();
                    String mids = BuildConfig.FLAVOR;
                    SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT mid, ttl FROM messages WHERE uid = %d AND out = %d AND read_state = 1 AND ttl > 0 AND date <= %d AND send_state = 0", new Object[]{Long.valueOf(((long) i) << 32), Integer.valueOf(i2), Integer.valueOf(i3)}), new Object[0]);
                    while (cursor.next()) {
                        int mid = cursor.intValue(0);
                        int date = i4 + cursor.intValue(1);
                        minDate = Math.min(minDate, date);
                        arr = (ArrayList) messages.get(date);
                        if (arr == null) {
                            arr = new ArrayList();
                            messages.put(date, arr);
                        }
                        if (mids.length() != 0) {
                            mids = mids + ",";
                        }
                        mids = mids + BuildConfig.FLAVOR + mid;
                        arr.add(Integer.valueOf(mid));
                    }
                    cursor.dispose();
                    if (messages.size() != 0) {
                        MessagesStorage.this.database.beginTransaction();
                        SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("INSERT INTO enc_tasks VALUES(?, ?)");
                        for (int a = 0; a < messages.size(); a++) {
                            int key = messages.keyAt(a);
                            arr = (ArrayList) messages.get(key);
                            SerializedData data = new SerializedData();
                            int b = 0;
                            while (b < arr.size()) {
                                data.writeInt32(((Integer) arr.get(b)).intValue());
                                if (b == arr.size() - 1 || (b != 0 && b % 100 == 0)) {
                                    state.requery();
                                    byte[] toDb = data.toByteArray();
                                    state.bindInteger(1, key);
                                    state.bindByteArray(2, toDb);
                                    state.step();
                                    if (b != arr.size() - 1) {
                                        data = new SerializedData();
                                    }
                                }
                                b++;
                            }
                        }
                        state.dispose();
                        MessagesStorage.this.database.commitTransaction();
                        MessagesStorage.this.database.executeFast(String.format(Locale.US, "UPDATE messages SET ttl = 0 WHERE mid IN (%s)", new Object[]{mids})).stepThis().dispose();
                        MessagesController.Instance.didAddedNewTask(minDate);
                    }
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        });
    }

    private void updateDialogsWithReadedMessagesInternal(ArrayList<Integer> messages) {
        if (Thread.currentThread().getId() != this.storageQueue.getId()) {
            throw new RuntimeException("wrong db thread");
        }
        try {
            Iterator i$;
            int uid;
            SQLiteCursor cursor;
            SerializedData data;
            String toLoad;
            byte[] chatData;
            HashMap<Long, Integer> dialogsToUpdate = new HashMap();
            String dialogsToReload = BuildConfig.FLAVOR;
            if (!(messages == null || messages.isEmpty())) {
                String ids = BuildConfig.FLAVOR;
                i$ = messages.iterator();
                while (i$.hasNext()) {
                    uid = ((Integer) i$.next()).intValue();
                    if (ids.length() != 0) {
                        ids = ids + ",";
                    }
                    ids = ids + uid;
                }
                cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT uid FROM messages WHERE mid IN(%s) AND out = 0", new Object[]{ids}), new Object[0]);
                while (cursor.next()) {
                    long uid2 = cursor.longValue(0);
                    Integer currentCount = (Integer) dialogsToUpdate.get(Long.valueOf(uid2));
                    if (currentCount == null) {
                        dialogsToUpdate.put(Long.valueOf(uid2), Integer.valueOf(1));
                    } else {
                        dialogsToUpdate.put(Long.valueOf(uid2), Integer.valueOf(currentCount.intValue() + 1));
                    }
                }
                cursor.dispose();
                this.database.beginTransaction();
                SQLitePreparedStatement state = this.database.executeFast("UPDATE dialogs SET unread_count = max(0, (SELECT unread_count FROM dialogs WHERE did = ?) - ?) WHERE did = ?");
                for (Entry<Long, Integer> entry : dialogsToUpdate.entrySet()) {
                    state.requery();
                    state.bindLong(1, ((Long) entry.getKey()).longValue());
                    state.bindInteger(2, ((Integer) entry.getValue()).intValue());
                    state.bindLong(3, ((Long) entry.getKey()).longValue());
                    state.step();
                    if (dialogsToReload.length() != 0) {
                        dialogsToReload = dialogsToReload + ",";
                    }
                    dialogsToReload = dialogsToReload + ((Long) entry.getKey());
                }
                state.dispose();
                this.database.commitTransaction();
            }
            messages_Dialogs dialogs = new messages_Dialogs();
            ArrayList<EncryptedChat> encryptedChats = new ArrayList();
            ArrayList<Integer> usersToLoad = new ArrayList();
            ArrayList<Integer> chatsToLoad = new ArrayList();
            ArrayList<Integer> encryptedToLoad = new ArrayList();
            cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT d.did, d.last_mid, d.unread_count, d.date, m.data, m.read_state, m.mid, m.send_state FROM dialogs as d LEFT JOIN messages as m ON d.last_mid = m.mid WHERE d.did IN(%s)", new Object[]{dialogsToReload}), new Object[0]);
            while (cursor.next()) {
                TL_dialog dialog = new TL_dialog();
                dialog.id = cursor.longValue(0);
                dialog.top_message = cursor.intValue(1);
                dialog.unread_count = cursor.intValue(2);
                dialog.last_message_date = cursor.intValue(3);
                dialogs.dialogs.add(dialog);
                byte[] messageData = cursor.byteArrayValue(4);
                if (messageData != null) {
                    data = new SerializedData(messageData);
                    Message message = (Message) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                    message.unread = cursor.intValue(5) != 1;
                    message.id = cursor.intValue(6);
                    message.send_state = cursor.intValue(7);
                    dialogs.messages.add(message);
                    if (!usersToLoad.contains(Integer.valueOf(message.from_id))) {
                        usersToLoad.add(Integer.valueOf(message.from_id));
                    }
                    if (!(message.action == null || message.action.user_id == 0 || usersToLoad.contains(Integer.valueOf(message.action.user_id)))) {
                        usersToLoad.add(Integer.valueOf(message.action.user_id));
                    }
                    if (!(message.fwd_from_id == 0 || usersToLoad.contains(Integer.valueOf(message.fwd_from_id)))) {
                        usersToLoad.add(Integer.valueOf(message.fwd_from_id));
                    }
                }
                int lower_id = (int) dialog.id;
                if (lower_id == 0) {
                    int encryptedId = (int) (dialog.id >> 32);
                    if (!encryptedToLoad.contains(Integer.valueOf(encryptedId))) {
                        encryptedToLoad.add(Integer.valueOf(encryptedId));
                    }
                } else if (lower_id > 0) {
                    if (!usersToLoad.contains(Integer.valueOf(lower_id))) {
                        usersToLoad.add(Integer.valueOf(lower_id));
                    }
                } else if (!chatsToLoad.contains(Integer.valueOf(-lower_id))) {
                    chatsToLoad.add(Integer.valueOf(-lower_id));
                }
            }
            cursor.dispose();
            if (!encryptedToLoad.isEmpty()) {
                toLoad = BuildConfig.FLAVOR;
                i$ = encryptedToLoad.iterator();
                while (i$.hasNext()) {
                    uid = ((Integer) i$.next()).intValue();
                    if (toLoad.length() != 0) {
                        toLoad = toLoad + ",";
                    }
                    toLoad = toLoad + uid;
                }
                cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT data, user, g, authkey, ttl FROM enc_chats WHERE uid IN(%s)", new Object[]{toLoad}), new Object[0]);
                while (cursor.next()) {
                    chatData = cursor.byteArrayValue(0);
                    if (chatData != null) {
                        data = new SerializedData(chatData);
                        EncryptedChat chat = (EncryptedChat) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                        encryptedChats.add(chat);
                        chat.user_id = cursor.intValue(1);
                        if (!usersToLoad.contains(Integer.valueOf(chat.user_id))) {
                            usersToLoad.add(Integer.valueOf(chat.user_id));
                        }
                        chat.a_or_b = cursor.byteArrayValue(2);
                        chat.auth_key = cursor.byteArrayValue(3);
                        chat.ttl = cursor.intValue(4);
                    }
                }
                cursor.dispose();
            }
            if (!chatsToLoad.isEmpty()) {
                toLoad = BuildConfig.FLAVOR;
                i$ = chatsToLoad.iterator();
                while (i$.hasNext()) {
                    uid = ((Integer) i$.next()).intValue();
                    if (toLoad.length() != 0) {
                        toLoad = toLoad + ",";
                    }
                    toLoad = toLoad + uid;
                }
                cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM chats WHERE uid IN(%s)", new Object[]{toLoad}), new Object[0]);
                while (cursor.next()) {
                    chatData = cursor.byteArrayValue(0);
                    if (chatData != null) {
                        data = new SerializedData(chatData);
                        dialogs.chats.add((Chat) TLClassStore.Instance().TLdeserialize(data, data.readInt32()));
                    }
                }
                cursor.dispose();
            }
            if (!usersToLoad.isEmpty()) {
                toLoad = BuildConfig.FLAVOR;
                i$ = usersToLoad.iterator();
                while (i$.hasNext()) {
                    uid = ((Integer) i$.next()).intValue();
                    if (toLoad.length() != 0) {
                        toLoad = toLoad + ",";
                    }
                    toLoad = toLoad + uid;
                }
                cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT data, status FROM users WHERE uid IN(%s)", new Object[]{toLoad}), new Object[0]);
                while (cursor.next()) {
                    byte[] userData = cursor.byteArrayValue(0);
                    if (userData != null) {
                        data = new SerializedData(userData);
                        User user = (User) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                        if (user.status != null) {
                            UserStatus userStatus = user.status;
                            UserStatus userStatus2 = user.status;
                            int intValue = cursor.intValue(1);
                            userStatus2.expires = intValue;
                            userStatus.was_online = intValue;
                        }
                        dialogs.users.add(user);
                    }
                }
                cursor.dispose();
            }
            if (!dialogs.dialogs.isEmpty() || !encryptedChats.isEmpty()) {
                MessagesController.Instance.processDialogsUpdate(dialogs, encryptedChats);
            }
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
    }

    public void updateDialogsWithReadedMessages(final ArrayList<Integer> messages, boolean useQueue) {
        if (!messages.isEmpty()) {
            if (useQueue) {
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        MessagesStorage.this.updateDialogsWithReadedMessagesInternal(messages);
                    }
                });
            } else {
                updateDialogsWithReadedMessagesInternal(messages);
            }
        }
    }

    public void updateChatInfo(final int chat_id, final ChatParticipants info, final boolean ifExist) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    if (ifExist) {
                        boolean dontExist = true;
                        SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized("SELECT uid FROM chat_settings WHERE uid = " + chat_id, new Object[0]);
                        if (cursor.next()) {
                            dontExist = false;
                        }
                        cursor.dispose();
                        if (dontExist) {
                            return;
                        }
                    }
                    SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO chat_settings VALUES(?, ?)");
                    SerializedData data = new SerializedData();
                    info.serializeToStream(data);
                    state.bindInteger(1, chat_id);
                    state.bindByteArray(2, data.toByteArray());
                    state.step();
                    state.dispose();
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        });
    }

    public void updateChatInfo(int chat_id, int user_id, boolean deleted, int invited_id, int version) {
        final int i = chat_id;
        final boolean z = deleted;
        final int i2 = user_id;
        final int i3 = invited_id;
        final int i4 = version;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SerializedData data;
                    SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized("SELECT participants FROM chat_settings WHERE uid = " + i, new Object[0]);
                    ChatParticipants info = null;
                    ArrayList<User> loadedUsers = new ArrayList();
                    if (cursor.next()) {
                        byte[] userData = cursor.byteArrayValue(0);
                        if (userData != null) {
                            data = new SerializedData(userData);
                            info = (ChatParticipants) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                        }
                    }
                    cursor.dispose();
                    if (info != null) {
                        if (z) {
                            for (int a = 0; a < info.participants.size(); a++) {
                                if (((TL_chatParticipant) info.participants.get(a)).user_id == i2) {
                                    info.participants.remove(a);
                                    break;
                                }
                            }
                        } else {
                            TL_chatParticipant participant = new TL_chatParticipant();
                            participant.user_id = i2;
                            participant.inviter_id = i3;
                            participant.date = ConnectionsManager.Instance.getCurrentTime();
                            info.participants.add(participant);
                        }
                        info.version = i4;
                        final ChatParticipants finalInfo = info;
                        Utilities.RunOnUIThread(new Runnable() {
                            public void run() {
                                NotificationCenter.Instance.postNotificationName(17, Integer.valueOf(finalInfo.chat_id), finalInfo);
                            }
                        });
                        SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO chat_settings VALUES(?, ?)");
                        data = new SerializedData();
                        info.serializeToStream(data);
                        state.bindInteger(1, i);
                        state.bindByteArray(2, data.toByteArray());
                        state.step();
                        state.dispose();
                    }
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        });
    }

    public void loadChatInfo(final int chat_id) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    byte[] userData;
                    SerializedData data;
                    SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized("SELECT participants FROM chat_settings WHERE uid = " + chat_id, new Object[0]);
                    ChatParticipants info = null;
                    ArrayList<User> loadedUsers = new ArrayList();
                    if (cursor.next()) {
                        userData = cursor.byteArrayValue(0);
                        if (userData != null) {
                            data = new SerializedData(userData);
                            info = (ChatParticipants) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                        }
                    }
                    cursor.dispose();
                    if (info != null) {
                        String usersToLoad = BuildConfig.FLAVOR;
                        Iterator i$ = info.participants.iterator();
                        while (i$.hasNext()) {
                            TL_chatParticipant c = (TL_chatParticipant) i$.next();
                            if (usersToLoad.length() != 0) {
                                usersToLoad = usersToLoad + ",";
                            }
                            usersToLoad = usersToLoad + c.user_id;
                        }
                        if (usersToLoad.length() != 0) {
                            cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT data, status FROM users WHERE uid IN(%s)", new Object[]{usersToLoad}), new Object[0]);
                            while (cursor.next()) {
                                userData = cursor.byteArrayValue(0);
                                if (userData != null) {
                                    data = new SerializedData(userData);
                                    User user = (User) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                                    loadedUsers.add(user);
                                    if (user.status != null) {
                                        UserStatus userStatus = user.status;
                                        UserStatus userStatus2 = user.status;
                                        int intValue = cursor.intValue(1);
                                        userStatus2.expires = intValue;
                                        userStatus.was_online = intValue;
                                    }
                                }
                            }
                            cursor.dispose();
                        }
                    }
                    MessagesController.Instance.processChatInfo(chat_id, info, loadedUsers, true);
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        });
    }

    public void processPendingRead(long dialog_id, int max_id, int max_date, boolean delete) {
        final boolean z = delete;
        final long j = dialog_id;
        final int i = max_id;
        final int i2 = max_date;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    if (!z) {
                        SQLitePreparedStatement state;
                        MessagesStorage.this.database.beginTransaction();
                        if (((int) j) != 0) {
                            state = MessagesStorage.this.database.executeFast("UPDATE messages SET read_state = 1 WHERE uid = ? AND mid <= ? AND read_state = 0 AND out = 0");
                            state.requery();
                            state.bindLong(1, j);
                            state.bindInteger(2, i);
                            state.step();
                            state.dispose();
                        } else {
                            state = MessagesStorage.this.database.executeFast("UPDATE messages SET read_state = 1 WHERE uid = ? AND date <= ? AND read_state = 0 AND out = 0");
                            state.requery();
                            state.bindLong(1, j);
                            state.bindInteger(2, i2);
                            state.step();
                            state.dispose();
                        }
                        state = MessagesStorage.this.database.executeFast("UPDATE dialogs SET unread_count = 0 WHERE did = ?");
                        state.requery();
                        state.bindLong(1, j);
                        state.step();
                        state.dispose();
                        MessagesStorage.this.database.commitTransaction();
                    }
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        });
    }

    public void searchDialogs(final Integer token, final String query, final boolean needEncrypted) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    ArrayList<User> encUsers = new ArrayList();
                    String q = query.trim().toLowerCase();
                    if (q.length() == 0) {
                        NotificationCenter.Instance.postNotificationName(12, token, new ArrayList(), new ArrayList(), new ArrayList());
                        return;
                    }
                    int i$;
                    int len$;
                    byte[] userData;
                    SerializedData data;
                    User user;
                    UserStatus userStatus;
                    UserStatus userStatus2;
                    int intValue;
                    String[] arr$;
                    byte[] chatData;
                    ArrayList<TLObject> resultArray = new ArrayList();
                    ArrayList<CharSequence> resultArrayNames = new ArrayList();
                    SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized("SELECT u.data, u.status, u.name FROM users as u INNER JOIN contacts as c ON u.uid = c.uid", new Object[0]);
                    while (cursor.next()) {
                        for (String str : cursor.stringValue(2).split(" ")) {
                            if (str.startsWith(q)) {
                                userData = cursor.byteArrayValue(0);
                                if (userData == null) {
                                    break;
                                }
                                data = new SerializedData(userData);
                                user = (User) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                                if (user.id != UserConfig.clientUserId) {
                                    if (user.status != null) {
                                        userStatus = user.status;
                                        userStatus2 = user.status;
                                        intValue = cursor.intValue(1);
                                        userStatus2.expires = intValue;
                                        userStatus.was_online = intValue;
                                    }
                                    resultArrayNames.add(Utilities.generateSearchName(user.first_name, user.last_name, q));
                                    resultArray.add(user);
                                }
                            }
                        }
                    }
                    cursor.dispose();
                    if (needEncrypted) {
                        cursor = MessagesStorage.this.database.queryFinalized("SELECT q.data, q.name, q.user, q.g, q.authkey, q.ttl, u.data, u.status FROM enc_chats as q INNER JOIN dialogs as d ON (q.uid << 32) = d.did INNER JOIN users as u ON q.user = u.uid", new Object[0]);
                        while (cursor.next()) {
                            arr$ = cursor.stringValue(1).split(" ");
                            len$ = arr$.length;
                            i$ = 0;
                            while (i$ < len$) {
                                if (arr$[i$].startsWith(q)) {
                                    chatData = cursor.byteArrayValue(0);
                                    userData = cursor.byteArrayValue(6);
                                    if (!(chatData == null || userData == null)) {
                                        data = new SerializedData(chatData);
                                        EncryptedChat chat = (EncryptedChat) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                                        chat.user_id = cursor.intValue(2);
                                        chat.a_or_b = cursor.byteArrayValue(3);
                                        chat.auth_key = cursor.byteArrayValue(4);
                                        chat.ttl = cursor.intValue(5);
                                        SerializedData data2 = new SerializedData(userData);
                                        user = (User) TLClassStore.Instance().TLdeserialize(data2, data2.readInt32());
                                        if (user.status != null) {
                                            userStatus = user.status;
                                            userStatus2 = user.status;
                                            intValue = cursor.intValue(7);
                                            userStatus2.expires = intValue;
                                            userStatus.was_online = intValue;
                                        }
                                        resultArrayNames.add(Html.fromHtml("<font color=\"#00a60e\">" + Utilities.formatName(user.first_name, user.last_name) + "</font>"));
                                        resultArray.add(chat);
                                        encUsers.add(user);
                                    }
                                } else {
                                    i$++;
                                }
                            }
                        }
                        cursor.dispose();
                    }
                    cursor = MessagesStorage.this.database.queryFinalized("SELECT c.data, c.name FROM chats as c INNER JOIN dialogs as d ON c.uid = -d.did", new Object[0]);
                    while (cursor.next()) {
                        arr$ = cursor.stringValue(1).split(" ");
                        len$ = arr$.length;
                        i$ = 0;
                        while (i$ < len$) {
                            if (arr$[i$].startsWith(q)) {
                                chatData = cursor.byteArrayValue(0);
                                if (chatData != null) {
                                    data = new SerializedData(chatData);
                                    Chat chat2 = (Chat) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                                    resultArrayNames.add(Utilities.generateSearchName(chat2.title, null, q));
                                    resultArray.add(chat2);
                                }
                            } else {
                                i$++;
                            }
                        }
                    }
                    cursor.dispose();
                    NotificationCenter.Instance.postNotificationName(12, token, resultArray, resultArrayNames, encUsers);
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        });
    }

    public void putContacts(final ArrayList<TL_contact> contacts, final boolean deleteAll) {
        if (!contacts.isEmpty()) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    try {
                        if (deleteAll) {
                            MessagesStorage.this.database.executeFast("DELETE FROM contacts WHERE 1").stepThis().dispose();
                        }
                        MessagesStorage.this.database.beginTransaction();
                        SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO contacts VALUES(?, ?)");
                        Iterator i$ = contacts.iterator();
                        while (i$.hasNext()) {
                            TL_contact contact = (TL_contact) i$.next();
                            state.requery();
                            state.bindInteger(1, contact.user_id);
                            state.bindInteger(2, contact.mutual ? 1 : 0);
                            state.step();
                        }
                        state.dispose();
                        MessagesStorage.this.database.commitTransaction();
                    } catch (Exception e) {
                        FileLog.m799e("tmessages", e);
                    }
                }
            });
        }
    }

    public void getContacts() {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    ArrayList<TL_contact> contacts = new ArrayList();
                    ArrayList<User> users = new ArrayList();
                    SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized("SELECT * FROM contacts WHERE 1", new Object[0]);
                    String uids = BuildConfig.FLAVOR;
                    while (cursor.next()) {
                        int user_id = cursor.intValue(0);
                        if (user_id != UserConfig.clientUserId) {
                            TL_contact contact = new TL_contact();
                            contact.user_id = user_id;
                            contact.mutual = cursor.intValue(1) == 1;
                            if (uids.length() != 0) {
                                uids = uids + ",";
                            }
                            contacts.add(contact);
                            uids = uids + contact.user_id;
                        }
                    }
                    cursor.dispose();
                    if (uids.length() != 0) {
                        cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT data, status FROM users WHERE uid IN(%s)", new Object[]{uids}), new Object[0]);
                        while (cursor.next()) {
                            byte[] userData = cursor.byteArrayValue(0);
                            if (userData != null) {
                                SerializedData data = new SerializedData(userData);
                                User user = (User) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                                users.add(user);
                                if (user.status != null) {
                                    UserStatus userStatus = user.status;
                                    UserStatus userStatus2 = user.status;
                                    int intValue = cursor.intValue(1);
                                    userStatus2.expires = intValue;
                                    userStatus.was_online = intValue;
                                }
                            }
                        }
                        cursor.dispose();
                    }
                    MessagesController.Instance.processLoadedContacts(contacts, users, 1);
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        });
    }

    public void putMediaCount(final long uid, final int count) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLitePreparedStatement state2 = MessagesStorage.this.database.executeFast("REPLACE INTO media_counts VALUES(?, ?)");
                    state2.requery();
                    state2.bindLong(1, uid);
                    state2.bindInteger(2, count);
                    state2.step();
                    state2.dispose();
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        });
    }

    public void getMediaCount(final long uid, final int classGuid) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                int count = -1;
                try {
                    SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT count FROM media_counts WHERE uid = %d LIMIT 1", new Object[]{Long.valueOf(uid)}), new Object[0]);
                    if (cursor.next()) {
                        count = cursor.intValue(0);
                    }
                    cursor.dispose();
                    int lower_part = (int) uid;
                    if (count == -1 && lower_part == 0) {
                        cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT COUNT(mid) FROM media WHERE uid = %d LIMIT 1", new Object[]{Long.valueOf(uid)}), new Object[0]);
                        if (cursor.next()) {
                            count = cursor.intValue(0);
                        }
                        cursor.dispose();
                        if (count != -1) {
                            MessagesStorage.this.putMediaCount(uid, count);
                        }
                    }
                    MessagesController.Instance.processLoadedMediaCount(count, uid, classGuid, true);
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        });
    }

    public void loadMedia(long uid, int offset, int count, int max_id, int classGuid) {
        final long j = uid;
        final int i = max_id;
        final int i2 = count;
        final int i3 = offset;
        final int i4 = classGuid;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                TL_messages_messages res = new TL_messages_messages();
                try {
                    SQLiteCursor cursor;
                    SerializedData data;
                    ArrayList<Integer> loadedUsers = new ArrayList();
                    ArrayList<Integer> fromUser = new ArrayList();
                    if (((int) j) != 0) {
                        if (i != 0) {
                            cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT data, mid FROM media WHERE uid = %d AND mid < %d ORDER BY date DESC, mid DESC LIMIT %d", new Object[]{Long.valueOf(j), Integer.valueOf(i), Integer.valueOf(i2)}), new Object[0]);
                        } else {
                            cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT data, mid FROM media WHERE uid = %d ORDER BY date DESC, mid DESC LIMIT %d,%d", new Object[]{Long.valueOf(j), Integer.valueOf(i3), Integer.valueOf(i2)}), new Object[0]);
                        }
                    } else if (i != 0) {
                        cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT data, mid FROM media WHERE uid = %d AND mid < %d ORDER BY date DESC LIMIT %d", new Object[]{Long.valueOf(j), Integer.valueOf(i), Integer.valueOf(i2)}), new Object[0]);
                    } else {
                        cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT data, mid FROM media WHERE uid = %d ORDER BY date DESC LIMIT %d,%d", new Object[]{Long.valueOf(j), Integer.valueOf(i3), Integer.valueOf(i2)}), new Object[0]);
                    }
                    while (cursor.next()) {
                        byte[] messageData = cursor.byteArrayValue(0);
                        if (messageData != null) {
                            data = new SerializedData(messageData);
                            Message message = (Message) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                            message.id = cursor.intValue(1);
                            message.dialog_id = j;
                            res.messages.add(message);
                            fromUser.add(Integer.valueOf(message.from_id));
                        }
                    }
                    cursor.dispose();
                    String usersToLoad = BuildConfig.FLAVOR;
                    Iterator i$ = fromUser.iterator();
                    while (i$.hasNext()) {
                        int uid = ((Integer) i$.next()).intValue();
                        if (!loadedUsers.contains(Integer.valueOf(uid))) {
                            if (usersToLoad.length() != 0) {
                                usersToLoad = usersToLoad + ",";
                            }
                            usersToLoad = usersToLoad + uid;
                            loadedUsers.add(Integer.valueOf(uid));
                        }
                    }
                    if (usersToLoad.length() != 0) {
                        cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT data, status FROM users WHERE uid IN(%s)", new Object[]{usersToLoad}), new Object[0]);
                        while (cursor.next()) {
                            byte[] userData = cursor.byteArrayValue(0);
                            if (userData != null) {
                                data = new SerializedData(userData);
                                User user = (User) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                                loadedUsers.add(Integer.valueOf(user.id));
                                if (user.status != null) {
                                    UserStatus userStatus = user.status;
                                    UserStatus userStatus2 = user.status;
                                    int intValue = cursor.intValue(1);
                                    userStatus2.expires = intValue;
                                    userStatus.was_online = intValue;
                                }
                                res.users.add(user);
                            }
                        }
                        cursor.dispose();
                    }
                    MessagesController.Instance.processLoadedMedia(res, j, i3, i2, i, true, i4);
                } catch (Exception e) {
                    res.messages.clear();
                    res.chats.clear();
                    res.users.clear();
                    FileLog.m799e("tmessages", e);
                    MessagesController.Instance.processLoadedMedia(res, j, i3, i2, i, true, i4);
                } catch (Throwable th) {
                    Throwable th2 = th;
                    MessagesController.Instance.processLoadedMedia(res, j, i3, i2, i, true, i4);
                }
            }
        });
    }

    public void putMedia(final long uid, final ArrayList<Message> messages) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    MessagesStorage.this.database.beginTransaction();
                    SQLitePreparedStatement state2 = MessagesStorage.this.database.executeFast("REPLACE INTO media VALUES(?, ?, ?, ?)");
                    Iterator i$ = messages.iterator();
                    while (i$.hasNext()) {
                        Message message = (Message) i$.next();
                        if ((message.media instanceof TL_messageMediaVideo) || (message.media instanceof TL_messageMediaPhoto)) {
                            state2.requery();
                            SerializedData data = new SerializedData();
                            message.serializeToStream(data);
                            state2.bindInteger(1, message.id);
                            state2.bindLong(2, uid);
                            state2.bindInteger(3, message.date);
                            state2.bindByteArray(4, data.toByteArray());
                            state2.step();
                        } else {
                            Log.e("tmessages", "test");
                        }
                    }
                    state2.dispose();
                    MessagesStorage.this.database.commitTransaction();
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        });
    }

    public void getMessages(long dialog_id, int offset, int count, int max_id, int minDate, int classGuid, boolean from_unread, boolean forward) {
        final int i = count;
        final int i2 = offset;
        final long j = dialog_id;
        final boolean z = forward;
        final int i3 = minDate;
        final int i4 = max_id;
        final boolean z2 = from_unread;
        final int i5 = classGuid;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                TL_messages_messages res = new TL_messages_messages();
                int count_unread = 0;
                int count_query = i;
                int offset_query = i2;
                int min_unread_id = 0;
                int max_unread_id = 0;
                int max_unread_date = 0;
                try {
                    SQLiteCursor cursor;
                    SerializedData serializedData;
                    ArrayList<Integer> loadedUsers = new ArrayList();
                    ArrayList<Integer> fromUser = new ArrayList();
                    if (((int) j) != 0) {
                        if (z) {
                            cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT read_state, data, send_state, mid, date FROM messages WHERE uid = %d AND date >= %d AND mid > %d ORDER BY date ASC, mid ASC LIMIT %d", new Object[]{Long.valueOf(j), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(count_query)}), new Object[0]);
                        } else if (i3 == 0) {
                            if (z2) {
                                cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT min(mid), max(mid), max(date) FROM messages WHERE uid = %d AND out = 0 AND read_state = 0 AND mid > 0", new Object[]{Long.valueOf(j)}), new Object[0]);
                                if (cursor.next()) {
                                    min_unread_id = cursor.intValue(0);
                                    max_unread_id = cursor.intValue(1);
                                    max_unread_date = cursor.intValue(2);
                                }
                                cursor.dispose();
                                if (min_unread_id != 0) {
                                    cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT COUNT(*) FROM messages WHERE uid = %d AND mid >= %d AND out = 0 AND read_state = 0", new Object[]{Long.valueOf(j), Integer.valueOf(min_unread_id)}), new Object[0]);
                                    if (cursor.next()) {
                                        count_unread = cursor.intValue(0);
                                    }
                                    cursor.dispose();
                                }
                            }
                            if (count_query > count_unread || count_unread < 4) {
                                count_query = Math.max(count_query, count_unread + 10);
                                if (count_unread < 4) {
                                    count_unread = 0;
                                    min_unread_id = 0;
                                    max_unread_id = 0;
                                }
                            } else {
                                offset_query = count_unread - count_query;
                                count_query += 10;
                            }
                            cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT read_state, data, send_state, mid, date FROM messages WHERE uid = %d ORDER BY date DESC, mid DESC LIMIT %d,%d", new Object[]{Long.valueOf(j), Integer.valueOf(offset_query), Integer.valueOf(count_query)}), new Object[0]);
                        } else if (i4 != 0) {
                            cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT read_state, data, send_state, mid, date FROM messages WHERE uid = %d AND date < %d AND mid < %d ORDER BY date DESC, mid DESC LIMIT %d", new Object[]{Long.valueOf(j), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(count_query)}), new Object[0]);
                        } else {
                            cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT read_state, data, send_state, mid, date FROM messages WHERE uid = %d AND date < %d ORDER BY date DESC, mid DESC LIMIT %d,%d", new Object[]{Long.valueOf(j), Integer.valueOf(i3), Integer.valueOf(offset_query), Integer.valueOf(count_query)}), new Object[0]);
                        }
                    } else if (z) {
                        cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT read_state, data, send_state, mid, date FROM messages WHERE uid = %d AND mid < %d ORDER BY mid DESC LIMIT %d", new Object[]{Long.valueOf(j), Integer.valueOf(i4), Integer.valueOf(count_query)}), new Object[0]);
                    } else if (i3 != 0) {
                        cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT read_state, data, send_state, mid, date FROM messages WHERE uid = %d AND date < %d ORDER BY mid ASC LIMIT %d,%d", new Object[]{Long.valueOf(j), Integer.valueOf(i3), Integer.valueOf(offset_query), Integer.valueOf(count_query)}), new Object[0]);
                    } else {
                        if (z2) {
                            cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT max(mid), min(mid), max(date) FROM messages WHERE uid = %d AND out = 0 AND read_state = 0 AND mid < 0", new Object[]{Long.valueOf(j)}), new Object[0]);
                            if (cursor.next()) {
                                min_unread_id = cursor.intValue(0);
                                max_unread_id = cursor.intValue(1);
                                max_unread_date = cursor.intValue(2);
                            }
                            cursor.dispose();
                            if (min_unread_id != 0) {
                                cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT COUNT(*) FROM messages WHERE uid = %d AND mid <= %d AND out = 0 AND read_state = 0", new Object[]{Long.valueOf(j), Integer.valueOf(min_unread_id)}), new Object[0]);
                                if (cursor.next()) {
                                    count_unread = cursor.intValue(0);
                                }
                                cursor.dispose();
                            }
                        }
                        if (count_query > count_unread || count_unread < 4) {
                            count_query = Math.max(count_query, count_unread + 10);
                            if (count_unread < 4) {
                                count_unread = 0;
                                min_unread_id = 0;
                                max_unread_id = 0;
                            }
                        } else {
                            offset_query = count_unread - count_query;
                            count_query += 10;
                        }
                        cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT read_state, data, send_state, mid, date FROM messages WHERE uid = %d ORDER BY mid ASC LIMIT %d,%d", new Object[]{Long.valueOf(j), Integer.valueOf(offset_query), Integer.valueOf(count_query)}), new Object[0]);
                    }
                    while (cursor.next()) {
                        byte[] messageData = cursor.byteArrayValue(1);
                        if (messageData != null) {
                            serializedData = new SerializedData(messageData);
                            Message message = (Message) TLClassStore.Instance().TLdeserialize(serializedData, serializedData.readInt32());
                            int read_state = cursor.intValue(0);
                            message.unread = cursor.intValue(0) != 1;
                            message.id = cursor.intValue(3);
                            message.date = cursor.intValue(4);
                            message.dialog_id = j;
                            res.messages.add(message);
                            fromUser.add(Integer.valueOf(message.from_id));
                            if (!(message.action == null || message.action.user_id == 0)) {
                                fromUser.add(Integer.valueOf(message.action.user_id));
                            }
                            if (!(message.media == null || message.media.user_id == 0)) {
                                fromUser.add(Integer.valueOf(message.media.user_id));
                            }
                            if (message.fwd_from_id != 0) {
                                fromUser.add(Integer.valueOf(message.fwd_from_id));
                            }
                            message.send_state = cursor.intValue(2);
                            if (!message.unread || message.id > 0) {
                                message.send_state = 0;
                            }
                        }
                    }
                    cursor.dispose();
                    String usersToLoad = BuildConfig.FLAVOR;
                    Iterator i$ = fromUser.iterator();
                    while (i$.hasNext()) {
                        int uid = ((Integer) i$.next()).intValue();
                        if (!loadedUsers.contains(Integer.valueOf(uid))) {
                            if (usersToLoad.length() != 0) {
                                usersToLoad = usersToLoad + ",";
                            }
                            usersToLoad = usersToLoad + uid;
                            loadedUsers.add(Integer.valueOf(uid));
                        }
                    }
                    if (usersToLoad.length() != 0) {
                        cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT data, status FROM users WHERE uid IN(%s)", new Object[]{usersToLoad}), new Object[0]);
                        while (cursor.next()) {
                            byte[] userData = cursor.byteArrayValue(0);
                            if (userData != null) {
                                serializedData = new SerializedData(userData);
                                User user = (User) TLClassStore.Instance().TLdeserialize(serializedData, serializedData.readInt32());
                                loadedUsers.add(Integer.valueOf(user.id));
                                if (user.status != null) {
                                    UserStatus userStatus = user.status;
                                    UserStatus userStatus2 = user.status;
                                    int intValue = cursor.intValue(1);
                                    userStatus2.expires = intValue;
                                    userStatus.was_online = intValue;
                                }
                                res.users.add(user);
                            }
                        }
                        cursor.dispose();
                    }
                    MessagesController.Instance.processLoadedMessages(res, j, i2, count_query, i4, true, i5, min_unread_id, max_unread_id, count_unread, max_unread_date, z);
                } catch (Exception e) {
                    res.messages.clear();
                    res.chats.clear();
                    res.users.clear();
                    FileLog.m799e("tmessages", e);
                    MessagesController.Instance.processLoadedMessages(res, j, i2, count_query, i4, true, i5, min_unread_id, max_unread_id, count_unread, max_unread_date, z);
                } catch (Throwable th) {
                    Throwable th2 = th;
                    MessagesController.Instance.processLoadedMessages(res, j, i2, count_query, i4, true, i5, min_unread_id, max_unread_id, count_unread, max_unread_date, z);
                }
            }
        });
    }

    public void startTransaction(boolean useQueue) {
        if (useQueue) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    try {
                        MessagesStorage.this.database.beginTransaction();
                    } catch (Exception e) {
                        FileLog.m799e("tmessages", e);
                    }
                }
            });
            return;
        }
        try {
            this.database.beginTransaction();
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
    }

    public void commitTransaction(boolean useQueue) {
        if (useQueue) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    MessagesStorage.this.database.commitTransaction();
                }
            });
        } else {
            this.database.commitTransaction();
        }
    }

    public void updateEncryptedChatTTL(final EncryptedChat chat) {
        if (chat != null) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r4 = this;
                    r1 = 0;
                    r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0026, all -> 0x0032 }
                    r2 = r2.database;	 Catch:{ Exception -> 0x0026, all -> 0x0032 }
                    r3 = "UPDATE enc_chats SET ttl = ? WHERE uid = ?";	 Catch:{ Exception -> 0x0026, all -> 0x0032 }
                    r1 = r2.executeFast(r3);	 Catch:{ Exception -> 0x0026, all -> 0x0032 }
                    r2 = 1;	 Catch:{ Exception -> 0x0026, all -> 0x0032 }
                    r3 = r3;	 Catch:{ Exception -> 0x0026, all -> 0x0032 }
                    r3 = r3.ttl;	 Catch:{ Exception -> 0x0026, all -> 0x0032 }
                    r1.bindInteger(r2, r3);	 Catch:{ Exception -> 0x0026, all -> 0x0032 }
                    r2 = 2;	 Catch:{ Exception -> 0x0026, all -> 0x0032 }
                    r3 = r3;	 Catch:{ Exception -> 0x0026, all -> 0x0032 }
                    r3 = r3.id;	 Catch:{ Exception -> 0x0026, all -> 0x0032 }
                    r1.bindInteger(r2, r3);	 Catch:{ Exception -> 0x0026, all -> 0x0032 }
                    r1.step();	 Catch:{ Exception -> 0x0026, all -> 0x0032 }
                    if (r1 == 0) goto L_0x0025;
                L_0x0022:
                    r1.dispose();
                L_0x0025:
                    return;
                L_0x0026:
                    r0 = move-exception;
                    r2 = "tmessages";	 Catch:{ Exception -> 0x0026, all -> 0x0032 }
                    org.telegram.messenger.FileLog.m799e(r2, r0);	 Catch:{ Exception -> 0x0026, all -> 0x0032 }
                    if (r1 == 0) goto L_0x0025;
                L_0x002e:
                    r1.dispose();
                    goto L_0x0025;
                L_0x0032:
                    r2 = move-exception;
                    if (r1 == 0) goto L_0x0038;
                L_0x0035:
                    r1.dispose();
                L_0x0038:
                    throw r2;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.27.run():void");
                }
            });
        }
    }

    public void updateEncryptedChat(final EncryptedChat chat) {
        if (chat != null) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r5 = this;
                    r2 = 0;
                    r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r3 = r3.database;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r4 = "UPDATE enc_chats SET data = ?, g = ?, authkey = ?, ttl = ? WHERE uid = ?";	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r2 = r3.executeFast(r4);	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r0 = new org.telegram.messenger.SerializedData;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r0.<init>();	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r3 = r3;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r3.serializeToStream(r0);	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r3 = 1;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r4 = r0.toByteArray();	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r2.bindByteArray(r3, r4);	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r3 = r3;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r3 = r3.a_or_b;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    if (r3 == 0) goto L_0x0054;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                L_0x0025:
                    r3 = 2;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r4 = r3;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r4 = r4.a_or_b;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r2.bindByteArray(r3, r4);	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                L_0x002d:
                    r3 = r3;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r3 = r3.auth_key;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    if (r3 == 0) goto L_0x0068;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                L_0x0033:
                    r3 = 3;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r4 = r3;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r4 = r4.auth_key;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r2.bindByteArray(r3, r4);	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                L_0x003b:
                    r3 = 4;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r4 = r3;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r4 = r4.ttl;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r2.bindInteger(r3, r4);	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r3 = 5;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r4 = r3;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r4 = r4.id;	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r2.bindInteger(r3, r4);	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r2.step();	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    if (r2 == 0) goto L_0x0053;
                L_0x0050:
                    r2.dispose();
                L_0x0053:
                    return;
                L_0x0054:
                    r3 = 2;
                    r4 = 1;
                    r4 = new byte[r4];	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r2.bindByteArray(r3, r4);	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    goto L_0x002d;
                L_0x005c:
                    r1 = move-exception;
                    r3 = "tmessages";	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    org.telegram.messenger.FileLog.m799e(r3, r1);	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    if (r2 == 0) goto L_0x0053;
                L_0x0064:
                    r2.dispose();
                    goto L_0x0053;
                L_0x0068:
                    r3 = 3;
                    r4 = 1;
                    r4 = new byte[r4];	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    r2.bindByteArray(r3, r4);	 Catch:{ Exception -> 0x005c, all -> 0x0070 }
                    goto L_0x003b;
                L_0x0070:
                    r3 = move-exception;
                    if (r2 == 0) goto L_0x0076;
                L_0x0073:
                    r2.dispose();
                L_0x0076:
                    throw r3;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.28.run():void");
                }
            });
        }
    }

    public void getEncryptedChat(final int chat_id, final Semaphore semaphore, final ArrayList<TLObject> result) {
        if (semaphore != null && result != null) {
            this.storageQueue.postRunnable(new Runnable() {
                /* JADX WARNING: inconsistent code. */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void run() {
                    /*
                    r15 = this;
                    r14 = 2;
                    r7 = 0;
                    r8 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x00d6 }
                    r8 = r8.database;	 Catch:{ Exception -> 0x00d6 }
                    r9 = java.util.Locale.US;	 Catch:{ Exception -> 0x00d6 }
                    r10 = "SELECT data, user, g, authkey, ttl FROM enc_chats WHERE uid = %d";
                    r11 = 1;
                    r11 = new java.lang.Object[r11];	 Catch:{ Exception -> 0x00d6 }
                    r12 = 0;
                    r13 = r3;	 Catch:{ Exception -> 0x00d6 }
                    r13 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x00d6 }
                    r11[r12] = r13;	 Catch:{ Exception -> 0x00d6 }
                    r9 = java.lang.String.format(r9, r10, r11);	 Catch:{ Exception -> 0x00d6 }
                    r10 = 0;
                    r10 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x00d6 }
                    r2 = r8.queryFinalized(r9, r10);	 Catch:{ Exception -> 0x00d6 }
                    r8 = r2.next();	 Catch:{ Exception -> 0x00d6 }
                    if (r8 == 0) goto L_0x0066;
                L_0x0029:
                    r8 = 0;
                    r1 = r2.byteArrayValue(r8);	 Catch:{ Exception -> 0x00d6 }
                    if (r1 == 0) goto L_0x0066;
                L_0x0030:
                    r3 = new org.telegram.messenger.SerializedData;	 Catch:{ Exception -> 0x00d6 }
                    r3.<init>(r1);	 Catch:{ Exception -> 0x00d6 }
                    r8 = org.telegram.TL.TLClassStore.Instance();	 Catch:{ Exception -> 0x00d6 }
                    r9 = r3.readInt32();	 Catch:{ Exception -> 0x00d6 }
                    r0 = r8.TLdeserialize(r3, r9);	 Catch:{ Exception -> 0x00d6 }
                    r0 = (org.telegram.TL.TLRPC.EncryptedChat) r0;	 Catch:{ Exception -> 0x00d6 }
                    r8 = r5;	 Catch:{ Exception -> 0x00d6 }
                    r8.add(r0);	 Catch:{ Exception -> 0x00d6 }
                    r8 = 1;
                    r8 = r2.intValue(r8);	 Catch:{ Exception -> 0x00d6 }
                    r0.user_id = r8;	 Catch:{ Exception -> 0x00d6 }
                    r7 = r0.user_id;	 Catch:{ Exception -> 0x00d6 }
                    r8 = 2;
                    r8 = r2.byteArrayValue(r8);	 Catch:{ Exception -> 0x00d6 }
                    r0.a_or_b = r8;	 Catch:{ Exception -> 0x00d6 }
                    r8 = 3;
                    r8 = r2.byteArrayValue(r8);	 Catch:{ Exception -> 0x00d6 }
                    r0.auth_key = r8;	 Catch:{ Exception -> 0x00d6 }
                    r8 = 4;
                    r8 = r2.intValue(r8);	 Catch:{ Exception -> 0x00d6 }
                    r0.ttl = r8;	 Catch:{ Exception -> 0x00d6 }
                L_0x0066:
                    r2.dispose();	 Catch:{ Exception -> 0x00d6 }
                    if (r7 == 0) goto L_0x00d0;
                L_0x006b:
                    r8 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x00d6 }
                    r8 = r8.database;	 Catch:{ Exception -> 0x00d6 }
                    r9 = java.util.Locale.US;	 Catch:{ Exception -> 0x00d6 }
                    r10 = "SELECT data, status FROM users WHERE uid = %d";
                    r11 = 1;
                    r11 = new java.lang.Object[r11];	 Catch:{ Exception -> 0x00d6 }
                    r12 = 0;
                    r13 = java.lang.Integer.valueOf(r7);	 Catch:{ Exception -> 0x00d6 }
                    r11[r12] = r13;	 Catch:{ Exception -> 0x00d6 }
                    r9 = java.lang.String.format(r9, r10, r11);	 Catch:{ Exception -> 0x00d6 }
                    r10 = 0;
                    r10 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x00d6 }
                    r2 = r8.queryFinalized(r9, r10);	 Catch:{ Exception -> 0x00d6 }
                    r8 = r2.next();	 Catch:{ Exception -> 0x00d6 }
                    if (r8 == 0) goto L_0x00c0;
                L_0x0090:
                    r8 = 0;
                    r6 = r2.byteArrayValue(r8);	 Catch:{ Exception -> 0x00d6 }
                    if (r6 == 0) goto L_0x00c0;
                L_0x0097:
                    r3 = new org.telegram.messenger.SerializedData;	 Catch:{ Exception -> 0x00d6 }
                    r3.<init>(r6);	 Catch:{ Exception -> 0x00d6 }
                    r8 = org.telegram.TL.TLClassStore.Instance();	 Catch:{ Exception -> 0x00d6 }
                    r9 = r3.readInt32();	 Catch:{ Exception -> 0x00d6 }
                    r5 = r8.TLdeserialize(r3, r9);	 Catch:{ Exception -> 0x00d6 }
                    r5 = (org.telegram.TL.TLRPC.User) r5;	 Catch:{ Exception -> 0x00d6 }
                    r8 = r5.status;	 Catch:{ Exception -> 0x00d6 }
                    if (r8 == 0) goto L_0x00bb;
                L_0x00ae:
                    r8 = r5.status;	 Catch:{ Exception -> 0x00d6 }
                    r9 = r5.status;	 Catch:{ Exception -> 0x00d6 }
                    r10 = 1;
                    r10 = r2.intValue(r10);	 Catch:{ Exception -> 0x00d6 }
                    r9.expires = r10;	 Catch:{ Exception -> 0x00d6 }
                    r8.was_online = r10;	 Catch:{ Exception -> 0x00d6 }
                L_0x00bb:
                    r8 = r5;	 Catch:{ Exception -> 0x00d6 }
                    r8.add(r5);	 Catch:{ Exception -> 0x00d6 }
                L_0x00c0:
                    r2.dispose();	 Catch:{ Exception -> 0x00d6 }
                    r8 = r5;	 Catch:{ Exception -> 0x00d6 }
                    r8 = r8.size();	 Catch:{ Exception -> 0x00d6 }
                    if (r8 == r14) goto L_0x00d0;
                L_0x00cb:
                    r8 = r5;	 Catch:{ Exception -> 0x00d6 }
                    r8.clear();	 Catch:{ Exception -> 0x00d6 }
                L_0x00d0:
                    r8 = r4;
                    r8.release();
                L_0x00d5:
                    return;
                L_0x00d6:
                    r4 = move-exception;
                    r8 = "tmessages";
                    org.telegram.messenger.FileLog.m799e(r8, r4);	 Catch:{ all -> 0x00e2 }
                    r8 = r4;
                    r8.release();
                    goto L_0x00d5;
                L_0x00e2:
                    r8 = move-exception;
                    r9 = r4;
                    r9.release();
                    throw r8;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.29.run():void");
                }
            });
        }
    }

    public void putEncryptedChat(final EncryptedChat chat, final User user, final TL_dialog dialog) {
        if (chat != null) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    try {
                        SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO enc_chats VALUES(?, ?, ?, ?, ?, ?, ?)");
                        SerializedData data = new SerializedData();
                        chat.serializeToStream(data);
                        state.bindInteger(1, chat.id);
                        state.bindInteger(2, user.id);
                        if (user.first_name == null || user.last_name == null) {
                            state.bindString(3, BuildConfig.FLAVOR);
                        } else {
                            state.bindString(3, (user.first_name + " " + user.last_name).toLowerCase());
                        }
                        state.bindByteArray(4, data.toByteArray());
                        if (chat.a_or_b != null) {
                            state.bindByteArray(5, chat.a_or_b);
                        } else {
                            state.bindByteArray(5, new byte[1]);
                        }
                        if (chat.auth_key != null) {
                            state.bindByteArray(6, chat.auth_key);
                        } else {
                            state.bindByteArray(6, new byte[1]);
                        }
                        state.bindInteger(7, chat.ttl);
                        state.step();
                        state.dispose();
                        if (dialog != null) {
                            state = MessagesStorage.this.database.executeFast("REPLACE INTO dialogs VALUES(?, ?, ?, ?)");
                            state.bindLong(1, dialog.id);
                            state.bindInteger(2, dialog.last_message_date);
                            state.bindInteger(3, dialog.unread_count);
                            state.bindInteger(4, dialog.top_message);
                            state.step();
                            state.dispose();
                        }
                    } catch (Exception e) {
                        FileLog.m799e("tmessages", e);
                    }
                }
            });
        }
    }

    private void putUsersAndChatsInternal(ArrayList<User> users, ArrayList<Chat> chats, boolean withTransaction) {
        if (Thread.currentThread().getId() != this.storageQueue.getId()) {
            throw new RuntimeException("wrong db thread");
        }
        SQLitePreparedStatement state;
        Iterator i$;
        if (withTransaction) {
            try {
                this.database.beginTransaction();
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
                return;
            }
        }
        if (!(users == null || users.isEmpty())) {
            state = this.database.executeFast("REPLACE INTO users VALUES(?, ?, ?, ?)");
            i$ = users.iterator();
            while (i$.hasNext()) {
                User user = (User) i$.next();
                state.requery();
                SerializedData data = new SerializedData();
                user.serializeToStream(data);
                state.bindInteger(1, user.id);
                if (user.first_name == null || user.last_name == null) {
                    state.bindString(2, BuildConfig.FLAVOR);
                } else {
                    state.bindString(2, (user.first_name + " " + user.last_name).toLowerCase());
                }
                if (user.status != null) {
                    state.bindInteger(3, user.status.was_online != 0 ? user.status.was_online : user.status.expires);
                } else {
                    state.bindInteger(3, 0);
                }
                state.bindByteArray(4, data.toByteArray());
                state.step();
            }
            state.dispose();
        }
        if (!(chats == null || chats.isEmpty())) {
            state = this.database.executeFast("REPLACE INTO chats VALUES(?, ?, ?)");
            i$ = chats.iterator();
            while (i$.hasNext()) {
                Chat chat = (Chat) i$.next();
                state.requery();
                data = new SerializedData();
                chat.serializeToStream(data);
                state.bindInteger(1, chat.id);
                if (chat.title != null) {
                    state.bindString(2, chat.title.toLowerCase());
                } else {
                    state.bindString(2, BuildConfig.FLAVOR);
                }
                state.bindByteArray(3, data.toByteArray());
                state.step();
            }
            state.dispose();
        }
        if (withTransaction) {
            this.database.commitTransaction();
        }
    }

    public void putUsersAndChats(final ArrayList<User> users, final ArrayList<Chat> chats, final boolean withTransaction, boolean useQueue) {
        if (users != null && users.isEmpty() && chats != null && chats.isEmpty()) {
            return;
        }
        if (useQueue) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    MessagesStorage.this.putUsersAndChatsInternal(users, chats, withTransaction);
                }
            });
        } else {
            putUsersAndChatsInternal(users, chats, withTransaction);
        }
    }

    private void putMessagesInternal(ArrayList<Message> messages, boolean withTransaction) {
        if (Thread.currentThread().getId() != this.storageQueue.getId()) {
            throw new RuntimeException("wrong db thread");
        }
        SQLiteCursor cursor;
        Integer count;
        if (withTransaction) {
            try {
                this.database.beginTransaction();
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
                return;
            }
        }
        HashMap<Long, Message> messagesMap = new HashMap();
        HashMap<Long, Integer> messagesCounts = new HashMap();
        HashMap<Long, Integer> mediaCounts = new HashMap();
        HashMap<Integer, Long> messagesIdsMap = new HashMap();
        HashMap<Integer, Long> messagesMediaIdsMap = new HashMap();
        String messageIds = BuildConfig.FLAVOR;
        String messageMediaIds = BuildConfig.FLAVOR;
        SQLitePreparedStatement state = this.database.executeFast("REPLACE INTO messages VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
        SQLitePreparedStatement state2 = this.database.executeFast("REPLACE INTO media VALUES(?, ?, ?, ?)");
        SQLitePreparedStatement state3 = this.database.executeFast("REPLACE INTO randoms VALUES(?, ?)");
        Iterator i$ = messages.iterator();
        while (i$.hasNext()) {
            Message message = (Message) i$.next();
            long dialog_id = 0;
            if (message.unread && !message.out) {
                if (messageIds.length() > 0) {
                    messageIds = messageIds + ",";
                }
                messageIds = messageIds + message.id;
                dialog_id = message.dialog_id;
                if (dialog_id == 0) {
                    if (message.to_id.chat_id != 0) {
                        dialog_id = (long) (-message.to_id.chat_id);
                    } else if (message.to_id.user_id != 0) {
                        dialog_id = (long) message.to_id.user_id;
                    }
                }
                messagesIdsMap.put(Integer.valueOf(message.id), Long.valueOf(dialog_id));
            }
            if ((message.media instanceof TL_messageMediaVideo) || (message.media instanceof TL_messageMediaPhoto)) {
                if (dialog_id == 0) {
                    dialog_id = message.dialog_id;
                    if (dialog_id == 0) {
                        if (message.to_id.chat_id != 0) {
                            dialog_id = (long) (-message.to_id.chat_id);
                        } else if (message.to_id.user_id != 0) {
                            dialog_id = (long) message.to_id.user_id;
                        }
                    }
                }
                if (messageMediaIds.length() > 0) {
                    messageMediaIds = messageMediaIds + ",";
                }
                messageMediaIds = messageMediaIds + message.id;
                messagesMediaIdsMap.put(Integer.valueOf(message.id), Long.valueOf(dialog_id));
            }
        }
        if (messageIds.length() > 0) {
            cursor = this.database.queryFinalized("SELECT mid FROM messages WHERE mid IN(" + messageIds + ")", new Object[0]);
            while (cursor.next()) {
                messagesIdsMap.remove(Integer.valueOf(cursor.intValue(0)));
            }
            cursor.dispose();
            for (Long dialog_id2 : messagesIdsMap.values()) {
                count = (Integer) messagesCounts.get(dialog_id2);
                if (count == null) {
                    count = Integer.valueOf(0);
                }
                messagesCounts.put(dialog_id2, Integer.valueOf(count.intValue() + 1));
            }
        }
        if (messageMediaIds.length() > 0) {
            cursor = this.database.queryFinalized("SELECT mid FROM media WHERE mid IN(" + messageMediaIds + ")", new Object[0]);
            while (cursor.next()) {
                messagesMediaIdsMap.remove(Integer.valueOf(cursor.intValue(0)));
            }
            cursor.dispose();
            for (Long dialog_id22 : messagesMediaIdsMap.values()) {
                count = (Integer) mediaCounts.get(dialog_id22);
                if (count == null) {
                    count = Integer.valueOf(0);
                }
                mediaCounts.put(dialog_id22, Integer.valueOf(count.intValue() + 1));
            }
        }
        i$ = messages.iterator();
        while (i$.hasNext()) {
            message = (Message) i$.next();
            dialog_id = message.dialog_id;
            if (dialog_id == 0) {
                if (message.to_id.chat_id != 0) {
                    dialog_id = (long) (-message.to_id.chat_id);
                } else if (message.to_id.user_id != 0) {
                    dialog_id = (long) message.to_id.user_id;
                }
            }
            state.requery();
            int messageId = message.id;
            if (message.local_id != 0) {
                messageId = message.local_id;
            }
            SerializedData data = new SerializedData();
            message.serializeToStream(data);
            Message lastMessage = (Message) messagesMap.get(Long.valueOf(dialog_id));
            if (lastMessage == null || message.date > lastMessage.date) {
                messagesMap.put(Long.valueOf(dialog_id), message);
            }
            state.bindInteger(1, messageId);
            state.bindLong(2, dialog_id);
            state.bindInteger(3, message.unread ? 0 : 1);
            state.bindInteger(4, message.send_state);
            state.bindInteger(5, message.date);
            byte[] bytes = data.toByteArray();
            state.bindByteArray(6, bytes);
            state.bindInteger(7, message.out ? 1 : 0);
            state.bindInteger(8, message.ttl);
            state.step();
            if (message.random_id != 0) {
                state3.requery();
                state3.bindLong(1, message.random_id);
                state3.bindInteger(2, messageId);
                state3.step();
            }
            if ((message.media instanceof TL_messageMediaVideo) || (message.media instanceof TL_messageMediaPhoto)) {
                state2.requery();
                state2.bindInteger(1, messageId);
                state2.bindLong(2, dialog_id);
                state2.bindInteger(3, message.date);
                state2.bindByteArray(4, bytes);
                state2.step();
            }
        }
        state.dispose();
        state2.dispose();
        state3.dispose();
        state = this.database.executeFast("REPLACE INTO dialogs VALUES(?, ?, ifnull((SELECT unread_count FROM dialogs WHERE did = ?), 0) + ?, ?)");
        for (Entry<Long, Message> pair : messagesMap.entrySet()) {
            state.requery();
            Long key = (Long) pair.getKey();
            Message value = (Message) pair.getValue();
            Integer unread_count = (Integer) messagesCounts.get(key);
            if (unread_count == null) {
                unread_count = Integer.valueOf(0);
            }
            messageId = value.id;
            if (value.local_id != 0) {
                messageId = value.local_id;
            }
            state.bindLong(1, key.longValue());
            state.bindInteger(2, value.date);
            state.bindLong(3, key.longValue());
            state.bindInteger(4, unread_count.intValue());
            state.bindInteger(5, messageId);
            state.step();
        }
        state.dispose();
        if (withTransaction) {
            this.database.commitTransaction();
        }
        MessagesController.Instance.dialogsUnreadCountIncr(messagesCounts);
        if (!mediaCounts.isEmpty()) {
            state = this.database.executeFast("REPLACE INTO media_counts VALUES(?, ?)");
            for (Entry<Long, Integer> pair2 : mediaCounts.entrySet()) {
                long uid = ((Long) pair2.getKey()).longValue();
                int lower_part = (int) uid;
                int count2 = -1;
                cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT count FROM media_counts WHERE uid = %d LIMIT 1", new Object[]{Long.valueOf(uid)}), new Object[0]);
                if (cursor.next()) {
                    count2 = cursor.intValue(0);
                }
                if (count2 != -1) {
                    state.requery();
                    count2 += ((Integer) pair2.getValue()).intValue();
                    state.bindLong(1, uid);
                    state.bindInteger(2, count2);
                    state.step();
                }
            }
            state.dispose();
        }
    }

    public void putMessages(final ArrayList<Message> messages, final boolean withTransaction, boolean useQueue) {
        if (messages.size() != 0) {
            if (useQueue) {
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        MessagesStorage.this.putMessagesInternal(messages, withTransaction);
                    }
                });
            } else {
                putMessagesInternal(messages, withTransaction);
            }
        }
    }

    public Integer updateMessageStateAndId(long random_id, Integer _oldId, int newId, int date, boolean useQueue) {
        if (!useQueue) {
            return updateMessageStateAndIdInternal(random_id, _oldId, newId, date);
        }
        final long j = random_id;
        final Integer num = _oldId;
        final int i = newId;
        final int i2 = date;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                MessagesStorage.this.updateMessageStateAndIdInternal(j, num, i, i2);
            }
        });
        return null;
    }

    private void updateUsersInternal(ArrayList<User> users, boolean onlyStatus, boolean withTransaction) {
        if (Thread.currentThread().getId() != this.storageQueue.getId()) {
            throw new RuntimeException("wrong db thread");
        } else if (onlyStatus) {
            if (withTransaction) {
                try {
                    this.database.beginTransaction();
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                    return;
                }
            }
            state = this.database.executeFast("UPDATE users SET status = ? WHERE uid = ?");
            i$ = users.iterator();
            while (i$.hasNext()) {
                user = (User) i$.next();
                state.requery();
                if (user.status != null) {
                    state.bindInteger(1, user.status.was_online != 0 ? user.status.was_online : user.status.expires);
                } else {
                    state.bindInteger(1, 0);
                }
                state.bindInteger(2, user.id);
                state.step();
            }
            state.dispose();
            if (withTransaction) {
                this.database.commitTransaction();
            }
        } else {
            SerializedData data;
            String ids = BuildConfig.FLAVOR;
            HashMap<Integer, User> usersDict = new HashMap();
            i$ = users.iterator();
            while (i$.hasNext()) {
                user = (User) i$.next();
                if (ids.length() != 0) {
                    ids = ids + ",";
                }
                ids = ids + user.id;
                usersDict.put(Integer.valueOf(user.id), user);
            }
            ArrayList<User> loadedUsers = new ArrayList();
            SQLiteCursor cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT data, status FROM users WHERE uid IN(%s)", new Object[]{ids}), new Object[0]);
            while (cursor.next()) {
                byte[] userData = cursor.byteArrayValue(0);
                if (userData != null) {
                    data = new SerializedData(userData);
                    user = (User) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                    loadedUsers.add(user);
                    if (user.status != null) {
                        UserStatus userStatus = user.status;
                        UserStatus userStatus2 = user.status;
                        int intValue = cursor.intValue(1);
                        userStatus2.expires = intValue;
                        userStatus.was_online = intValue;
                    }
                    User updateUser = (User) usersDict.get(Integer.valueOf(user.id));
                    if (updateUser.first_name != null && updateUser.last_name != null) {
                        user.first_name = updateUser.first_name;
                        user.last_name = updateUser.last_name;
                    } else if (updateUser.photo != null) {
                        user.photo = updateUser.photo;
                    }
                }
            }
            cursor.dispose();
            if (!loadedUsers.isEmpty()) {
                if (withTransaction) {
                    this.database.beginTransaction();
                }
                state = this.database.executeFast("REPLACE INTO users VALUES(?, ?, ?, ?)");
                i$ = loadedUsers.iterator();
                while (i$.hasNext()) {
                    user = (User) i$.next();
                    state.requery();
                    data = new SerializedData();
                    user.serializeToStream(data);
                    state.bindInteger(1, user.id);
                    if (user.first_name == null || user.last_name == null) {
                        state.bindString(2, BuildConfig.FLAVOR);
                    } else {
                        state.bindString(2, (user.first_name + " " + user.last_name).toLowerCase());
                    }
                    if (user.status != null) {
                        state.bindInteger(3, user.status.was_online != 0 ? user.status.was_online : user.status.expires);
                    } else {
                        state.bindInteger(3, 0);
                    }
                    state.bindByteArray(4, data.toByteArray());
                    state.step();
                }
                state.dispose();
                if (withTransaction) {
                    this.database.commitTransaction();
                }
            }
        }
    }

    public void updateUsers(final ArrayList<User> users, final boolean onlyStatus, final boolean withTransaction, boolean useQueue) {
        if (!users.isEmpty()) {
            if (useQueue) {
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        MessagesStorage.this.updateUsersInternal(users, onlyStatus, withTransaction);
                    }
                });
            } else {
                updateUsersInternal(users, onlyStatus, withTransaction);
            }
        }
    }

    private void markMessagesAsReadInternal(ArrayList<Integer> messages, HashMap<Integer, Integer> encryptedMessages) {
        if (Thread.currentThread().getId() != this.storageQueue.getId()) {
            throw new RuntimeException("wrong db thread");
        }
        Iterator i$;
        if (messages != null) {
            try {
                if (!messages.isEmpty()) {
                    String ids = BuildConfig.FLAVOR;
                    i$ = messages.iterator();
                    while (i$.hasNext()) {
                        int uid = ((Integer) i$.next()).intValue();
                        if (ids.length() != 0) {
                            ids = ids + ",";
                        }
                        ids = ids + uid;
                    }
                    this.database.executeFast(String.format(Locale.US, "UPDATE messages SET read_state = 1 WHERE mid IN (%s)", new Object[]{ids})).stepThis().dispose();
                }
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
                return;
            }
        }
        if (encryptedMessages != null && !encryptedMessages.isEmpty()) {
            for (Entry<Integer, Integer> entry : encryptedMessages.entrySet()) {
                long dialog_id = ((long) ((Integer) entry.getKey()).intValue()) << 32;
                int max_date = ((Integer) entry.getValue()).intValue();
                SQLitePreparedStatement state = this.database.executeFast("UPDATE messages SET read_state = 1 WHERE uid = ? AND date <= ? AND read_state = 0 AND out = 1");
                state.requery();
                state.bindLong(1, dialog_id);
                state.bindInteger(2, max_date);
                state.step();
                state.dispose();
            }
        }
    }

    public void markMessagesAsRead(final ArrayList<Integer> messages, final HashMap<Integer, Integer> encryptedMessages, boolean useQueue) {
        if (useQueue) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    MessagesStorage.this.markMessagesAsReadInternal(messages, encryptedMessages);
                }
            });
        } else {
            markMessagesAsReadInternal(messages, encryptedMessages);
        }
    }

    private void markMessagesAsDeletedInternal(ArrayList<Integer> messages) {
        if (Thread.currentThread().getId() != this.storageQueue.getId()) {
            throw new RuntimeException("wrong db thread");
        }
        try {
            String ids = BuildConfig.FLAVOR;
            Iterator i$ = messages.iterator();
            while (i$.hasNext()) {
                int uid = ((Integer) i$.next()).intValue();
                if (ids.length() != 0) {
                    ids = ids + ",";
                }
                ids = ids + uid;
            }
            this.database.executeFast(String.format(Locale.US, "DELETE FROM messages WHERE mid IN (%s)", new Object[]{ids})).stepThis().dispose();
            this.database.executeFast(String.format(Locale.US, "DELETE FROM media WHERE mid IN (%s)", new Object[]{ids})).stepThis().dispose();
            this.database.executeFast("DELETE FROM media_counts WHERE 1").stepThis().dispose();
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
    }

    private void updateDialogsWithDeletedMessagesInternal(ArrayList<Integer> messages) {
        if (Thread.currentThread().getId() != this.storageQueue.getId()) {
            throw new RuntimeException("wrong db thread");
        }
        try {
            int uid;
            SerializedData data;
            String toLoad;
            byte[] chatData;
            String ids = BuildConfig.FLAVOR;
            Iterator i$ = messages.iterator();
            while (i$.hasNext()) {
                uid = ((Integer) i$.next()).intValue();
                if (ids.length() != 0) {
                    ids = ids + ",";
                }
                ids = ids + uid;
            }
            SQLiteCursor cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT did FROM dialogs WHERE last_mid IN(%s)", new Object[]{ids}), new Object[0]);
            ArrayList<Long> dialogsToUpdate = new ArrayList();
            while (cursor.next()) {
                dialogsToUpdate.add(Long.valueOf(cursor.longValue(0)));
            }
            cursor.dispose();
            this.database.beginTransaction();
            SQLitePreparedStatement state = this.database.executeFast("UPDATE dialogs SET last_mid = (SELECT mid FROM messages WHERE uid = ? AND date = (SELECT MAX(date) FROM messages WHERE uid = ? )) WHERE did = ?");
            i$ = dialogsToUpdate.iterator();
            while (i$.hasNext()) {
                long did = ((Long) i$.next()).longValue();
                state.requery();
                state.bindLong(1, did);
                state.bindLong(2, did);
                state.bindLong(3, did);
                state.step();
            }
            state.dispose();
            this.database.commitTransaction();
            ids = BuildConfig.FLAVOR;
            i$ = dialogsToUpdate.iterator();
            while (i$.hasNext()) {
                long uid2 = ((Long) i$.next()).longValue();
                if (ids.length() != 0) {
                    ids = ids + ",";
                }
                ids = ids + uid2;
            }
            messages_Dialogs dialogs = new messages_Dialogs();
            ArrayList<EncryptedChat> encryptedChats = new ArrayList();
            ArrayList<Integer> usersToLoad = new ArrayList();
            ArrayList<Integer> chatsToLoad = new ArrayList();
            ArrayList<Integer> encryptedToLoad = new ArrayList();
            cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT d.did, d.last_mid, d.unread_count, d.date, m.data, m.read_state, m.mid, m.send_state FROM dialogs as d LEFT JOIN messages as m ON d.last_mid = m.mid WHERE d.did IN(%s)", new Object[]{ids}), new Object[0]);
            while (cursor.next()) {
                TL_dialog dialog = new TL_dialog();
                dialog.id = cursor.longValue(0);
                dialog.top_message = cursor.intValue(1);
                dialog.unread_count = cursor.intValue(2);
                dialog.last_message_date = cursor.intValue(3);
                dialogs.dialogs.add(dialog);
                byte[] messageData = cursor.byteArrayValue(4);
                if (messageData != null) {
                    data = new SerializedData(messageData);
                    Message message = (Message) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                    message.unread = cursor.intValue(5) != 1;
                    message.id = cursor.intValue(6);
                    message.send_state = cursor.intValue(7);
                    dialogs.messages.add(message);
                    if (!usersToLoad.contains(Integer.valueOf(message.from_id))) {
                        usersToLoad.add(Integer.valueOf(message.from_id));
                    }
                    if (!(message.action == null || message.action.user_id == 0 || usersToLoad.contains(Integer.valueOf(message.action.user_id)))) {
                        usersToLoad.add(Integer.valueOf(message.action.user_id));
                    }
                    if (!(message.fwd_from_id == 0 || usersToLoad.contains(Integer.valueOf(message.fwd_from_id)))) {
                        usersToLoad.add(Integer.valueOf(message.fwd_from_id));
                    }
                }
                int lower_id = (int) dialog.id;
                if (lower_id == 0) {
                    int encryptedId = (int) (dialog.id >> 32);
                    if (!encryptedToLoad.contains(Integer.valueOf(encryptedId))) {
                        encryptedToLoad.add(Integer.valueOf(encryptedId));
                    }
                } else if (lower_id > 0) {
                    if (!usersToLoad.contains(Integer.valueOf(lower_id))) {
                        usersToLoad.add(Integer.valueOf(lower_id));
                    }
                } else if (!chatsToLoad.contains(Integer.valueOf(-lower_id))) {
                    chatsToLoad.add(Integer.valueOf(-lower_id));
                }
            }
            cursor.dispose();
            if (!encryptedToLoad.isEmpty()) {
                toLoad = BuildConfig.FLAVOR;
                i$ = encryptedToLoad.iterator();
                while (i$.hasNext()) {
                    uid = ((Integer) i$.next()).intValue();
                    if (toLoad.length() != 0) {
                        toLoad = toLoad + ",";
                    }
                    toLoad = toLoad + uid;
                }
                cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT data, user, g, authkey, ttl FROM enc_chats WHERE uid IN(%s)", new Object[]{toLoad}), new Object[0]);
                while (cursor.next()) {
                    chatData = cursor.byteArrayValue(0);
                    if (chatData != null) {
                        data = new SerializedData(chatData);
                        EncryptedChat chat = (EncryptedChat) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                        encryptedChats.add(chat);
                        chat.user_id = cursor.intValue(1);
                        if (!usersToLoad.contains(Integer.valueOf(chat.user_id))) {
                            usersToLoad.add(Integer.valueOf(chat.user_id));
                        }
                        chat.a_or_b = cursor.byteArrayValue(2);
                        chat.auth_key = cursor.byteArrayValue(3);
                        chat.ttl = cursor.intValue(4);
                    }
                }
                cursor.dispose();
            }
            if (!chatsToLoad.isEmpty()) {
                toLoad = BuildConfig.FLAVOR;
                i$ = chatsToLoad.iterator();
                while (i$.hasNext()) {
                    uid = ((Integer) i$.next()).intValue();
                    if (toLoad.length() != 0) {
                        toLoad = toLoad + ",";
                    }
                    toLoad = toLoad + uid;
                }
                cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM chats WHERE uid IN(%s)", new Object[]{toLoad}), new Object[0]);
                while (cursor.next()) {
                    chatData = cursor.byteArrayValue(0);
                    if (chatData != null) {
                        data = new SerializedData(chatData);
                        dialogs.chats.add((Chat) TLClassStore.Instance().TLdeserialize(data, data.readInt32()));
                    }
                }
                cursor.dispose();
            }
            if (!usersToLoad.isEmpty()) {
                toLoad = BuildConfig.FLAVOR;
                i$ = usersToLoad.iterator();
                while (i$.hasNext()) {
                    uid = ((Integer) i$.next()).intValue();
                    if (toLoad.length() != 0) {
                        toLoad = toLoad + ",";
                    }
                    toLoad = toLoad + uid;
                }
                cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT data, status FROM users WHERE uid IN(%s)", new Object[]{toLoad}), new Object[0]);
                while (cursor.next()) {
                    byte[] userData = cursor.byteArrayValue(0);
                    if (userData != null) {
                        data = new SerializedData(userData);
                        User user = (User) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                        if (user.status != null) {
                            UserStatus userStatus = user.status;
                            UserStatus userStatus2 = user.status;
                            int intValue = cursor.intValue(1);
                            userStatus2.expires = intValue;
                            userStatus.was_online = intValue;
                        }
                        dialogs.users.add(user);
                    }
                }
                cursor.dispose();
            }
            if (!dialogs.dialogs.isEmpty() || !encryptedChats.isEmpty()) {
                MessagesController.Instance.processDialogsUpdate(dialogs, encryptedChats);
            }
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
    }

    public void updateDialogsWithDeletedMessages(final ArrayList<Integer> messages, boolean useQueue) {
        if (!messages.isEmpty()) {
            if (useQueue) {
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        MessagesStorage.this.updateDialogsWithDeletedMessagesInternal(messages);
                    }
                });
            } else {
                updateDialogsWithDeletedMessagesInternal(messages);
            }
        }
    }

    public void markMessagesAsDeleted(final ArrayList<Integer> messages, boolean useQueue) {
        if (!messages.isEmpty()) {
            if (useQueue) {
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        MessagesStorage.this.markMessagesAsDeletedInternal(messages);
                    }
                });
            } else {
                markMessagesAsDeletedInternal(messages);
            }
        }
    }

    public void putMessages(final messages_Messages messages, final long dialog_id) {
        if (!messages.messages.isEmpty()) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    try {
                        SQLitePreparedStatement state;
                        Iterator i$;
                        SerializedData data;
                        MessagesStorage.this.database.beginTransaction();
                        if (!messages.messages.isEmpty()) {
                            state = MessagesStorage.this.database.executeFast("REPLACE INTO messages VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
                            SQLitePreparedStatement state2 = MessagesStorage.this.database.executeFast("REPLACE INTO media VALUES(?, ?, ?, ?)");
                            i$ = messages.messages.iterator();
                            while (i$.hasNext()) {
                                int i;
                                Message message = (Message) i$.next();
                                state.requery();
                                data = new SerializedData();
                                message.serializeToStream(data);
                                state.bindInteger(1, message.id);
                                state.bindLong(2, dialog_id);
                                if (message.unread) {
                                    i = 0;
                                } else {
                                    i = 1;
                                }
                                state.bindInteger(3, i);
                                state.bindInteger(4, message.send_state);
                                state.bindInteger(5, message.date);
                                byte[] bytes = data.toByteArray();
                                state.bindByteArray(6, bytes);
                                if (message.out) {
                                    i = 1;
                                } else {
                                    i = 0;
                                }
                                state.bindInteger(7, i);
                                state.bindInteger(8, 0);
                                state.step();
                                if ((message.media instanceof TL_messageMediaVideo) || (message.media instanceof TL_messageMediaPhoto)) {
                                    state2.requery();
                                    state2.bindInteger(1, message.id);
                                    state2.bindLong(2, dialog_id);
                                    state2.bindInteger(3, message.date);
                                    state2.bindByteArray(4, bytes);
                                    state2.step();
                                }
                            }
                            state.dispose();
                            state2.dispose();
                        }
                        if (!messages.users.isEmpty()) {
                            state = MessagesStorage.this.database.executeFast("REPLACE INTO users VALUES(?, ?, ?, ?)");
                            i$ = messages.users.iterator();
                            while (i$.hasNext()) {
                                User user = (User) i$.next();
                                state.requery();
                                data = new SerializedData();
                                user.serializeToStream(data);
                                state.bindInteger(1, user.id);
                                if (user.first_name == null || user.last_name == null) {
                                    state.bindString(2, BuildConfig.FLAVOR);
                                } else {
                                    state.bindString(2, (user.first_name + " " + user.last_name).toLowerCase());
                                }
                                if (user.status != null) {
                                    state.bindInteger(3, user.status.was_online != 0 ? user.status.was_online : user.status.expires);
                                } else {
                                    state.bindInteger(3, 0);
                                }
                                state.bindByteArray(4, data.toByteArray());
                                state.step();
                            }
                            state.dispose();
                        }
                        if (!messages.chats.isEmpty()) {
                            state = MessagesStorage.this.database.executeFast("REPLACE INTO chats VALUES(?, ?, ?)");
                            i$ = messages.chats.iterator();
                            while (i$.hasNext()) {
                                Chat chat = (Chat) i$.next();
                                state.requery();
                                data = new SerializedData();
                                chat.serializeToStream(data);
                                state.bindInteger(1, chat.id);
                                if (chat.title != null) {
                                    state.bindString(2, chat.title.toLowerCase());
                                } else {
                                    state.bindString(2, BuildConfig.FLAVOR);
                                }
                                state.bindByteArray(3, data.toByteArray());
                                state.step();
                            }
                            state.dispose();
                        }
                        MessagesStorage.this.database.commitTransaction();
                    } catch (Exception e) {
                        FileLog.m799e("tmessages", e);
                    }
                }
            });
        }
    }

    public void getDialogs(final int offset, final int serverOffset, final int count) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                String toLoad;
                Iterator i$;
                byte[] chatData;
                messages_Dialogs dialogs = new messages_Dialogs();
                ArrayList<EncryptedChat> encryptedChats = new ArrayList();
                ArrayList<Integer> usersToLoad = new ArrayList();
                usersToLoad.add(Integer.valueOf(UserConfig.clientUserId));
                ArrayList<Integer> chatsToLoad = new ArrayList();
                ArrayList<Integer> encryptedToLoad = new ArrayList();
                SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT d.did, d.last_mid, d.unread_count, d.date, m.data, m.read_state, m.mid, m.send_state FROM dialogs as d LEFT JOIN messages as m ON d.last_mid = m.mid ORDER BY d.date DESC LIMIT %d,%d", new Object[]{Integer.valueOf(offset), Integer.valueOf(count)}), new Object[0]);
                while (cursor.next()) {
                    TL_dialog dialog = new TL_dialog();
                    dialog.id = cursor.longValue(0);
                    dialog.top_message = cursor.intValue(1);
                    dialog.unread_count = cursor.intValue(2);
                    dialog.last_message_date = cursor.intValue(3);
                    dialogs.dialogs.add(dialog);
                    byte[] messageData = cursor.byteArrayValue(4);
                    if (messageData != null) {
                        SerializedData data = new SerializedData(messageData);
                        Message message = (Message) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                        if (message != null) {
                            message.unread = cursor.intValue(5) != 1;
                            message.id = cursor.intValue(6);
                            message.send_state = cursor.intValue(7);
                            dialogs.messages.add(message);
                            if (!usersToLoad.contains(Integer.valueOf(message.from_id))) {
                                usersToLoad.add(Integer.valueOf(message.from_id));
                            }
                            if (!(message.action == null || message.action.user_id == 0)) {
                                if (!usersToLoad.contains(Integer.valueOf(message.action.user_id))) {
                                    usersToLoad.add(Integer.valueOf(message.action.user_id));
                                }
                            }
                            if (message.fwd_from_id != 0) {
                                if (!usersToLoad.contains(Integer.valueOf(message.fwd_from_id))) {
                                    usersToLoad.add(Integer.valueOf(message.fwd_from_id));
                                }
                            }
                        }
                    }
                    int lower_id = (int) dialog.id;
                    if (lower_id == 0) {
                        int encryptedId = (int) (dialog.id >> 32);
                        if (!encryptedToLoad.contains(Integer.valueOf(encryptedId))) {
                            encryptedToLoad.add(Integer.valueOf(encryptedId));
                        }
                    } else if (lower_id > 0) {
                        if (!usersToLoad.contains(Integer.valueOf(lower_id))) {
                            usersToLoad.add(Integer.valueOf(lower_id));
                        }
                    } else {
                        try {
                            if (!chatsToLoad.contains(Integer.valueOf(-lower_id))) {
                                chatsToLoad.add(Integer.valueOf(-lower_id));
                            }
                        } catch (Exception e) {
                            dialogs.dialogs.clear();
                            dialogs.users.clear();
                            dialogs.chats.clear();
                            encryptedChats.clear();
                            FileLog.m799e("tmessages", e);
                            MessagesController.Instance.processLoadedDialogs(dialogs, encryptedChats, 0, 0, 100, true, true);
                            return;
                        }
                    }
                }
                cursor.dispose();
                if (!encryptedToLoad.isEmpty()) {
                    toLoad = BuildConfig.FLAVOR;
                    i$ = encryptedToLoad.iterator();
                    while (i$.hasNext()) {
                        int uid = ((Integer) i$.next()).intValue();
                        if (toLoad.length() != 0) {
                            toLoad = toLoad + ",";
                        }
                        toLoad = toLoad + uid;
                    }
                    cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT data, user, g, authkey, ttl FROM enc_chats WHERE uid IN(%s)", new Object[]{toLoad}), new Object[0]);
                    while (cursor.next()) {
                        try {
                            chatData = cursor.byteArrayValue(0);
                            if (chatData != null) {
                                data = new SerializedData(chatData);
                                EncryptedChat chat = (EncryptedChat) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                                if (chat != null) {
                                    encryptedChats.add(chat);
                                    chat.user_id = cursor.intValue(1);
                                    if (!usersToLoad.contains(Integer.valueOf(chat.user_id))) {
                                        usersToLoad.add(Integer.valueOf(chat.user_id));
                                    }
                                    chat.a_or_b = cursor.byteArrayValue(2);
                                    chat.auth_key = cursor.byteArrayValue(3);
                                    chat.ttl = cursor.intValue(4);
                                }
                            }
                        } catch (Exception e2) {
                            FileLog.m799e("tmessages", e2);
                        }
                    }
                    cursor.dispose();
                }
                if (!chatsToLoad.isEmpty()) {
                    toLoad = BuildConfig.FLAVOR;
                    i$ = chatsToLoad.iterator();
                    while (i$.hasNext()) {
                        uid = ((Integer) i$.next()).intValue();
                        if (toLoad.length() != 0) {
                            toLoad = toLoad + ",";
                        }
                        toLoad = toLoad + uid;
                    }
                    cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM chats WHERE uid IN(%s)", new Object[]{toLoad}), new Object[0]);
                    while (cursor.next()) {
                        try {
                            chatData = cursor.byteArrayValue(0);
                            if (chatData != null) {
                                data = new SerializedData(chatData);
                                Chat chat2 = (Chat) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                                if (chat2 != null) {
                                    dialogs.chats.add(chat2);
                                }
                            }
                        } catch (Exception e22) {
                            FileLog.m799e("tmessages", e22);
                        }
                    }
                    cursor.dispose();
                }
                if (!usersToLoad.isEmpty()) {
                    toLoad = BuildConfig.FLAVOR;
                    i$ = usersToLoad.iterator();
                    while (i$.hasNext()) {
                        uid = ((Integer) i$.next()).intValue();
                        if (toLoad.length() != 0) {
                            toLoad = toLoad + ",";
                        }
                        toLoad = toLoad + uid;
                    }
                    cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT data, status FROM users WHERE uid IN(%s)", new Object[]{toLoad}), new Object[0]);
                    while (cursor.next()) {
                        try {
                            byte[] userData = cursor.byteArrayValue(0);
                            if (userData != null) {
                                data = new SerializedData(userData);
                                User user = (User) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                                if (user != null) {
                                    if (user.status != null) {
                                        UserStatus userStatus = user.status;
                                        UserStatus userStatus2 = user.status;
                                        int intValue = cursor.intValue(1);
                                        userStatus2.expires = intValue;
                                        userStatus.was_online = intValue;
                                    }
                                    dialogs.users.add(user);
                                }
                            }
                        } catch (Exception e222) {
                            FileLog.m799e("tmessages", e222);
                        }
                    }
                    cursor.dispose();
                }
                MessagesController.Instance.processLoadedDialogs(dialogs, encryptedChats, offset, serverOffset, count, true, false);
            }
        });
    }

    public void putDialogs(final messages_Dialogs dialogs) {
        if (!dialogs.dialogs.isEmpty()) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    try {
                        Message message;
                        SQLitePreparedStatement state;
                        SerializedData data;
                        MessagesStorage.this.database.beginTransaction();
                        HashMap<Integer, Message> new_dialogMessage = new HashMap();
                        Iterator i$ = dialogs.messages.iterator();
                        while (i$.hasNext()) {
                            message = (Message) i$.next();
                            new_dialogMessage.put(Integer.valueOf(message.id), message);
                        }
                        if (!dialogs.dialogs.isEmpty()) {
                            state = MessagesStorage.this.database.executeFast("REPLACE INTO messages VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
                            SQLitePreparedStatement state2 = MessagesStorage.this.database.executeFast("REPLACE INTO dialogs VALUES(?, ?, ?, ?)");
                            SQLitePreparedStatement state3 = MessagesStorage.this.database.executeFast("REPLACE INTO media VALUES(?, ?, ?, ?)");
                            i$ = dialogs.dialogs.iterator();
                            while (i$.hasNext()) {
                                TL_dialog dialog = (TL_dialog) i$.next();
                                state.requery();
                                state2.requery();
                                int uid = dialog.peer.user_id;
                                if (uid == 0) {
                                    uid = -dialog.peer.chat_id;
                                }
                                message = (Message) new_dialogMessage.get(Integer.valueOf(dialog.top_message));
                                data = new SerializedData();
                                message.serializeToStream(data);
                                state.bindInteger(1, message.id);
                                state.bindInteger(2, uid);
                                state.bindInteger(3, message.unread ? 0 : 1);
                                state.bindInteger(4, message.send_state);
                                state.bindInteger(5, message.date);
                                byte[] bytes = data.toByteArray();
                                state.bindByteArray(6, bytes);
                                state.bindInteger(7, message.out ? 1 : 0);
                                state.bindInteger(8, 0);
                                state.step();
                                state2.bindLong(1, (long) uid);
                                state2.bindInteger(2, message.date);
                                state2.bindInteger(3, dialog.unread_count);
                                state2.bindInteger(4, dialog.top_message);
                                state2.step();
                                if ((message.media instanceof TL_messageMediaVideo) || (message.media instanceof TL_messageMediaPhoto)) {
                                    state3.requery();
                                    state3.bindLong(1, (long) message.id);
                                    state3.bindInteger(2, uid);
                                    state3.bindInteger(3, message.date);
                                    state3.bindByteArray(4, bytes);
                                    state3.step();
                                }
                            }
                            state.dispose();
                            state2.dispose();
                            state3.dispose();
                        }
                        if (!dialogs.users.isEmpty()) {
                            state = MessagesStorage.this.database.executeFast("REPLACE INTO users VALUES(?, ?, ?, ?)");
                            i$ = dialogs.users.iterator();
                            while (i$.hasNext()) {
                                User user = (User) i$.next();
                                state.requery();
                                data = new SerializedData();
                                user.serializeToStream(data);
                                state.bindInteger(1, user.id);
                                if (user.first_name == null || user.last_name == null) {
                                    state.bindString(2, BuildConfig.FLAVOR);
                                } else {
                                    state.bindString(2, (user.first_name + " " + user.last_name).toLowerCase());
                                }
                                if (user.status != null) {
                                    state.bindInteger(3, user.status.was_online != 0 ? user.status.was_online : user.status.expires);
                                } else {
                                    state.bindInteger(3, 0);
                                }
                                state.bindByteArray(4, data.toByteArray());
                                state.step();
                            }
                            state.dispose();
                        }
                        if (!dialogs.chats.isEmpty()) {
                            state = MessagesStorage.this.database.executeFast("REPLACE INTO chats VALUES(?, ?, ?)");
                            i$ = dialogs.chats.iterator();
                            while (i$.hasNext()) {
                                Chat chat = (Chat) i$.next();
                                state.requery();
                                data = new SerializedData();
                                chat.serializeToStream(data);
                                state.bindInteger(1, chat.id);
                                if (chat.title != null) {
                                    state.bindString(2, chat.title.toLowerCase());
                                } else {
                                    state.bindString(2, BuildConfig.FLAVOR);
                                }
                                state.bindByteArray(3, data.toByteArray());
                                state.step();
                            }
                            state.dispose();
                        }
                        MessagesStorage.this.database.commitTransaction();
                    } catch (Exception e) {
                        FileLog.m799e("tmessages", e);
                    }
                }
            });
        }
    }
}
