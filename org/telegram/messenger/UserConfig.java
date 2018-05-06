package org.telegram.messenger;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import java.io.File;
import org.telegram.TL.TLClassStore;
import org.telegram.TL.TLRPC.TL_userSelf;
import org.telegram.TL.TLRPC.User;
import org.telegram.ui.ApplicationLoader;

public class UserConfig {
    public static boolean clientActivated = false;
    public static int clientUserId = 0;
    public static String contactsHash = BuildConfig.FLAVOR;
    public static User currentUser;
    public static String importHash = BuildConfig.FLAVOR;
    public static int lastLocalId = -210000;
    public static int lastSendMessageId = -210000;
    public static String pushString = BuildConfig.FLAVOR;
    public static boolean registeredForPush = false;
    public static boolean saveIncomingPhotos = false;
    private static final Integer sync = Integer.valueOf(1);

    public static int getNewMessageId() {
        int id;
        synchronized (sync) {
            id = lastSendMessageId;
            lastSendMessageId--;
        }
        return id;
    }

    public static void saveConfig(boolean withFile) {
        saveConfig(withFile, null);
    }

    public static void saveConfig(boolean withFile, File oldFile) {
        synchronized (sync) {
            try {
                Editor editor = ApplicationLoader.applicationContext.getSharedPreferences("userconfing", 0).edit();
                if (currentUser != null) {
                    editor.putBoolean("registeredForPush", registeredForPush);
                    editor.putString("pushString", pushString);
                    editor.putInt("lastSendMessageId", lastSendMessageId);
                    editor.putInt("lastLocalId", lastLocalId);
                    editor.putString("contactsHash", contactsHash);
                    editor.putString("importHash", importHash);
                    editor.putBoolean("saveIncomingPhotos", saveIncomingPhotos);
                    if (withFile) {
                        SerializedData data = new SerializedData();
                        currentUser.serializeToStream(data);
                        clientUserId = currentUser.id;
                        clientActivated = true;
                        editor.putString("user", Base64.encodeToString(data.toByteArray(), 0));
                    }
                } else {
                    editor.putBoolean("registeredForPush", registeredForPush);
                    editor.putString("pushString", pushString);
                    editor.putInt("lastSendMessageId", lastSendMessageId);
                    editor.putInt("lastLocalId", lastLocalId);
                    editor.putString("contactsHash", contactsHash);
                    editor.putString("importHash", importHash);
                    editor.putBoolean("saveIncomingPhotos", saveIncomingPhotos);
                    editor.remove("user");
                }
                editor.commit();
                if (oldFile != null) {
                    oldFile.delete();
                }
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            }
        }
    }

    public static void loadConfig() {
        synchronized (sync) {
            final File configFile = new File(ApplicationLoader.applicationContext.getFilesDir(), "user.dat");
            SerializedData data;
            SharedPreferences preferences;
            if (configFile.exists()) {
                try {
                    data = new SerializedData(configFile);
                    int ver = data.readInt32();
                    if (ver == 1) {
                        currentUser = (TL_userSelf) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                        clientUserId = currentUser.id;
                        clientActivated = true;
                        MessagesStorage.lastDateValue = data.readInt32();
                        MessagesStorage.lastPtsValue = data.readInt32();
                        MessagesStorage.lastSeqValue = data.readInt32();
                        registeredForPush = data.readBool();
                        pushString = data.readString();
                        lastSendMessageId = data.readInt32();
                        lastLocalId = data.readInt32();
                        contactsHash = data.readString();
                        importHash = data.readString();
                        saveIncomingPhotos = data.readBool();
                        if (currentUser.status != null) {
                            if (currentUser.status.expires != 0) {
                                currentUser.status.was_online = currentUser.status.expires;
                            } else {
                                currentUser.status.expires = currentUser.status.was_online;
                            }
                        }
                        MessagesStorage.lastQtsValue = data.readInt32();
                        MessagesStorage.lastSecretVersion = data.readInt32();
                        if (data.readInt32() == 1) {
                            MessagesStorage.secretPBytes = data.readByteArray();
                        }
                        MessagesStorage.secretG = data.readInt32();
                        Utilities.stageQueue.postRunnable(new Runnable() {
                            public void run() {
                                UserConfig.saveConfig(true, configFile);
                            }
                        });
                    } else if (ver == 2) {
                        currentUser = (TL_userSelf) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                        clientUserId = currentUser.id;
                        clientActivated = true;
                        preferences = ApplicationLoader.applicationContext.getSharedPreferences("userconfing", 0);
                        registeredForPush = preferences.getBoolean("registeredForPush", false);
                        pushString = preferences.getString("pushString", BuildConfig.FLAVOR);
                        lastSendMessageId = preferences.getInt("lastSendMessageId", -210000);
                        lastLocalId = preferences.getInt("lastLocalId", -210000);
                        contactsHash = preferences.getString("contactsHash", BuildConfig.FLAVOR);
                        importHash = preferences.getString("importHash", BuildConfig.FLAVOR);
                        saveIncomingPhotos = preferences.getBoolean("saveIncomingPhotos", false);
                    }
                    if (lastLocalId > -210000) {
                        lastLocalId = -210000;
                    }
                    if (lastSendMessageId > -210000) {
                        lastSendMessageId = -210000;
                    }
                    Utilities.stageQueue.postRunnable(new Runnable() {
                        public void run() {
                            UserConfig.saveConfig(true, configFile);
                        }
                    });
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            } else {
                preferences = ApplicationLoader.applicationContext.getSharedPreferences("userconfing", 0);
                registeredForPush = preferences.getBoolean("registeredForPush", false);
                pushString = preferences.getString("pushString", BuildConfig.FLAVOR);
                lastSendMessageId = preferences.getInt("lastSendMessageId", -210000);
                lastLocalId = preferences.getInt("lastLocalId", -210000);
                contactsHash = preferences.getString("contactsHash", BuildConfig.FLAVOR);
                importHash = preferences.getString("importHash", BuildConfig.FLAVOR);
                saveIncomingPhotos = preferences.getBoolean("saveIncomingPhotos", false);
                String user = preferences.getString("user", null);
                if (user != null) {
                    byte[] userBytes = Base64.decode(user, 0);
                    if (userBytes != null) {
                        data = new SerializedData(userBytes);
                        currentUser = (TL_userSelf) TLClassStore.Instance().TLdeserialize(data, data.readInt32());
                        clientUserId = currentUser.id;
                        clientActivated = true;
                    }
                }
                if (currentUser == null) {
                    clientActivated = false;
                    clientUserId = 0;
                }
            }
        }
    }

    public static void clearConfig() {
        clientUserId = 0;
        clientActivated = false;
        currentUser = null;
        registeredForPush = false;
        contactsHash = BuildConfig.FLAVOR;
        lastLocalId = -210000;
        importHash = BuildConfig.FLAVOR;
        lastSendMessageId = -210000;
        saveIncomingPhotos = false;
        saveConfig(true);
        MessagesController.Instance.deleteAllAppAccounts();
    }
}
