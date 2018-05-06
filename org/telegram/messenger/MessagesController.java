package org.telegram.messenger;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Vibrator;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.Settings.System;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.SparseArray;
import com.google.android.gms.location.LocationStatusCodes;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.TL.TLClassStore;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.Chat;
import org.telegram.TL.TLRPC.ChatParticipants;
import org.telegram.TL.TLRPC.DecryptedMessage;
import org.telegram.TL.TLRPC.Document;
import org.telegram.TL.TLRPC.EncryptedChat;
import org.telegram.TL.TLRPC.EncryptedFile;
import org.telegram.TL.TLRPC.EncryptedMessage;
import org.telegram.TL.TLRPC.FileLocation;
import org.telegram.TL.TLRPC.InputEncryptedFile;
import org.telegram.TL.TLRPC.InputFile;
import org.telegram.TL.TLRPC.InputPeer;
import org.telegram.TL.TLRPC.InputUser;
import org.telegram.TL.TLRPC.Message;
import org.telegram.TL.TLRPC.MessageAction;
import org.telegram.TL.TLRPC.PhotoSize;
import org.telegram.TL.TLRPC.TL_account_registerDevice;
import org.telegram.TL.TLRPC.TL_account_unregisterDevice;
import org.telegram.TL.TLRPC.TL_account_updateStatus;
import org.telegram.TL.TLRPC.TL_auth_logOut;
import org.telegram.TL.TLRPC.TL_chatParticipant;
import org.telegram.TL.TLRPC.TL_contact;
import org.telegram.TL.TLRPC.TL_contacts_contactsNotModified;
import org.telegram.TL.TLRPC.TL_contacts_getContacts;
import org.telegram.TL.TLRPC.TL_contacts_importContacts;
import org.telegram.TL.TLRPC.TL_contacts_importedContacts;
import org.telegram.TL.TLRPC.TL_decryptedMessage;
import org.telegram.TL.TLRPC.TL_decryptedMessageActionSetMessageTTL;
import org.telegram.TL.TLRPC.TL_decryptedMessageMediaContact;
import org.telegram.TL.TLRPC.TL_decryptedMessageMediaDocument;
import org.telegram.TL.TLRPC.TL_decryptedMessageMediaEmpty;
import org.telegram.TL.TLRPC.TL_decryptedMessageMediaGeoPoint;
import org.telegram.TL.TLRPC.TL_decryptedMessageMediaPhoto;
import org.telegram.TL.TLRPC.TL_decryptedMessageMediaVideo;
import org.telegram.TL.TLRPC.TL_decryptedMessageService;
import org.telegram.TL.TLRPC.TL_dialog;
import org.telegram.TL.TLRPC.TL_document;
import org.telegram.TL.TLRPC.TL_documentEncrypted;
import org.telegram.TL.TLRPC.TL_encryptedChat;
import org.telegram.TL.TLRPC.TL_encryptedChatDiscarded;
import org.telegram.TL.TLRPC.TL_encryptedChatRequested;
import org.telegram.TL.TLRPC.TL_encryptedChatWaiting;
import org.telegram.TL.TLRPC.TL_encryptedFile;
import org.telegram.TL.TLRPC.TL_error;
import org.telegram.TL.TLRPC.TL_fileEncryptedLocation;
import org.telegram.TL.TLRPC.TL_fileLocationUnavailable;
import org.telegram.TL.TLRPC.TL_geoPoint;
import org.telegram.TL.TLRPC.TL_geoPointEmpty;
import org.telegram.TL.TLRPC.TL_importedContact;
import org.telegram.TL.TLRPC.TL_inputChatPhotoEmpty;
import org.telegram.TL.TLRPC.TL_inputChatUploadedPhoto;
import org.telegram.TL.TLRPC.TL_inputEncryptedChat;
import org.telegram.TL.TLRPC.TL_inputGeoPoint;
import org.telegram.TL.TLRPC.TL_inputGeoPointEmpty;
import org.telegram.TL.TLRPC.TL_inputMediaContact;
import org.telegram.TL.TLRPC.TL_inputMediaGeoPoint;
import org.telegram.TL.TLRPC.TL_inputMediaPhoto;
import org.telegram.TL.TLRPC.TL_inputMediaUploadedDocument;
import org.telegram.TL.TLRPC.TL_inputMediaUploadedPhoto;
import org.telegram.TL.TLRPC.TL_inputMediaUploadedThumbVideo;
import org.telegram.TL.TLRPC.TL_inputMessagesFilterPhotoVideo;
import org.telegram.TL.TLRPC.TL_inputPeerChat;
import org.telegram.TL.TLRPC.TL_inputPeerContact;
import org.telegram.TL.TLRPC.TL_inputPeerForeign;
import org.telegram.TL.TLRPC.TL_inputPhoneContact;
import org.telegram.TL.TLRPC.TL_inputPhoto;
import org.telegram.TL.TLRPC.TL_inputPhotoCropAuto;
import org.telegram.TL.TLRPC.TL_inputUserContact;
import org.telegram.TL.TLRPC.TL_inputUserForeign;
import org.telegram.TL.TLRPC.TL_inputUserSelf;
import org.telegram.TL.TLRPC.TL_message;
import org.telegram.TL.TLRPC.TL_messageActionChatAddUser;
import org.telegram.TL.TLRPC.TL_messageActionChatDeletePhoto;
import org.telegram.TL.TLRPC.TL_messageActionChatDeleteUser;
import org.telegram.TL.TLRPC.TL_messageActionChatEditPhoto;
import org.telegram.TL.TLRPC.TL_messageActionChatEditTitle;
import org.telegram.TL.TLRPC.TL_messageActionLoginUnknownLocation;
import org.telegram.TL.TLRPC.TL_messageActionTTLChange;
import org.telegram.TL.TLRPC.TL_messageActionUserJoined;
import org.telegram.TL.TLRPC.TL_messageActionUserUpdatedPhoto;
import org.telegram.TL.TLRPC.TL_messageForwarded;
import org.telegram.TL.TLRPC.TL_messageMediaAudio;
import org.telegram.TL.TLRPC.TL_messageMediaContact;
import org.telegram.TL.TLRPC.TL_messageMediaDocument;
import org.telegram.TL.TLRPC.TL_messageMediaEmpty;
import org.telegram.TL.TLRPC.TL_messageMediaGeo;
import org.telegram.TL.TLRPC.TL_messageMediaPhoto;
import org.telegram.TL.TLRPC.TL_messageMediaVideo;
import org.telegram.TL.TLRPC.TL_messageService;
import org.telegram.TL.TLRPC.TL_messages_acceptEncryption;
import org.telegram.TL.TLRPC.TL_messages_addChatUser;
import org.telegram.TL.TLRPC.TL_messages_affectedHistory;
import org.telegram.TL.TLRPC.TL_messages_chatFull;
import org.telegram.TL.TLRPC.TL_messages_createChat;
import org.telegram.TL.TLRPC.TL_messages_deleteChatUser;
import org.telegram.TL.TLRPC.TL_messages_deleteHistory;
import org.telegram.TL.TLRPC.TL_messages_deleteMessages;
import org.telegram.TL.TLRPC.TL_messages_dhConfig;
import org.telegram.TL.TLRPC.TL_messages_dialogsSlice;
import org.telegram.TL.TLRPC.TL_messages_discardEncryption;
import org.telegram.TL.TLRPC.TL_messages_editChatPhoto;
import org.telegram.TL.TLRPC.TL_messages_editChatTitle;
import org.telegram.TL.TLRPC.TL_messages_forwardMessage;
import org.telegram.TL.TLRPC.TL_messages_forwardMessages;
import org.telegram.TL.TLRPC.TL_messages_getDhConfig;
import org.telegram.TL.TLRPC.TL_messages_getDialogs;
import org.telegram.TL.TLRPC.TL_messages_getFullChat;
import org.telegram.TL.TLRPC.TL_messages_getHistory;
import org.telegram.TL.TLRPC.TL_messages_messagesSlice;
import org.telegram.TL.TLRPC.TL_messages_readEncryptedHistory;
import org.telegram.TL.TLRPC.TL_messages_readHistory;
import org.telegram.TL.TLRPC.TL_messages_receivedMessages;
import org.telegram.TL.TLRPC.TL_messages_receivedQueue;
import org.telegram.TL.TLRPC.TL_messages_requestEncryption;
import org.telegram.TL.TLRPC.TL_messages_search;
import org.telegram.TL.TLRPC.TL_messages_sendEncrypted;
import org.telegram.TL.TLRPC.TL_messages_sendEncryptedFile;
import org.telegram.TL.TLRPC.TL_messages_sendMedia;
import org.telegram.TL.TLRPC.TL_messages_sendMessage;
import org.telegram.TL.TLRPC.TL_messages_sentMessage;
import org.telegram.TL.TLRPC.TL_messages_setEncryptedTyping;
import org.telegram.TL.TLRPC.TL_messages_setTyping;
import org.telegram.TL.TLRPC.TL_peerChat;
import org.telegram.TL.TLRPC.TL_peerUser;
import org.telegram.TL.TLRPC.TL_photo;
import org.telegram.TL.TLRPC.TL_photoCachedSize;
import org.telegram.TL.TLRPC.TL_photoSize;
import org.telegram.TL.TLRPC.TL_photoSizeEmpty;
import org.telegram.TL.TLRPC.TL_photos_getUserPhotos;
import org.telegram.TL.TLRPC.TL_photos_photo;
import org.telegram.TL.TLRPC.TL_photos_uploadProfilePhoto;
import org.telegram.TL.TLRPC.TL_updateActivation;
import org.telegram.TL.TLRPC.TL_updateChatParticipantAdd;
import org.telegram.TL.TLRPC.TL_updateChatParticipantDelete;
import org.telegram.TL.TLRPC.TL_updateChatParticipants;
import org.telegram.TL.TLRPC.TL_updateChatUserTyping;
import org.telegram.TL.TLRPC.TL_updateContactLink;
import org.telegram.TL.TLRPC.TL_updateContactRegistered;
import org.telegram.TL.TLRPC.TL_updateDcOptions;
import org.telegram.TL.TLRPC.TL_updateDeleteMessages;
import org.telegram.TL.TLRPC.TL_updateEncryptedChatTyping;
import org.telegram.TL.TLRPC.TL_updateEncryptedMessagesRead;
import org.telegram.TL.TLRPC.TL_updateEncryption;
import org.telegram.TL.TLRPC.TL_updateMessageID;
import org.telegram.TL.TLRPC.TL_updateNewAuthorization;
import org.telegram.TL.TLRPC.TL_updateNewEncryptedMessage;
import org.telegram.TL.TLRPC.TL_updateNewGeoChatMessage;
import org.telegram.TL.TLRPC.TL_updateNewMessage;
import org.telegram.TL.TLRPC.TL_updateReadMessages;
import org.telegram.TL.TLRPC.TL_updateRestoreMessages;
import org.telegram.TL.TLRPC.TL_updateShort;
import org.telegram.TL.TLRPC.TL_updateShortChatMessage;
import org.telegram.TL.TLRPC.TL_updateShortMessage;
import org.telegram.TL.TLRPC.TL_updateUserName;
import org.telegram.TL.TLRPC.TL_updateUserPhoto;
import org.telegram.TL.TLRPC.TL_updateUserStatus;
import org.telegram.TL.TLRPC.TL_updateUserTyping;
import org.telegram.TL.TLRPC.TL_updates;
import org.telegram.TL.TLRPC.TL_updatesCombined;
import org.telegram.TL.TLRPC.TL_updatesTooLong;
import org.telegram.TL.TLRPC.TL_updates_difference;
import org.telegram.TL.TLRPC.TL_updates_differenceEmpty;
import org.telegram.TL.TLRPC.TL_updates_differenceSlice;
import org.telegram.TL.TLRPC.TL_updates_getDifference;
import org.telegram.TL.TLRPC.TL_updates_getState;
import org.telegram.TL.TLRPC.TL_updates_state;
import org.telegram.TL.TLRPC.TL_userForeign;
import org.telegram.TL.TLRPC.TL_userProfilePhoto;
import org.telegram.TL.TLRPC.TL_userProfilePhotoEmpty;
import org.telegram.TL.TLRPC.TL_userRequest;
import org.telegram.TL.TLRPC.TL_userSelf;
import org.telegram.TL.TLRPC.TL_userStatusEmpty;
import org.telegram.TL.TLRPC.TL_userStatusOffline;
import org.telegram.TL.TLRPC.TL_userStatusOnline;
import org.telegram.TL.TLRPC.TL_video;
import org.telegram.TL.TLRPC.TL_videoEncrypted;
import org.telegram.TL.TLRPC.Update;
import org.telegram.TL.TLRPC.Updates;
import org.telegram.TL.TLRPC.User;
import org.telegram.TL.TLRPC.Video;
import org.telegram.TL.TLRPC.contacts_Contacts;
import org.telegram.TL.TLRPC.messages_DhConfig;
import org.telegram.TL.TLRPC.messages_Dialogs;
import org.telegram.TL.TLRPC.messages_Messages;
import org.telegram.TL.TLRPC.messages_SentEncryptedMessage;
import org.telegram.TL.TLRPC.messages_StatedMessage;
import org.telegram.TL.TLRPC.messages_StatedMessages;
import org.telegram.TL.TLRPC.photos_Photos;
import org.telegram.TL.TLRPC.updates_Difference;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.RPCRequest.RPCQuickAckDelegate;
import org.telegram.messenger.RPCRequest.RPCRequestDelegate;
import org.telegram.objects.MessageObject;
import org.telegram.objects.PhotoObject;
import org.telegram.ui.ApplicationLoader;
import org.telegram.ui.LaunchActivity;

public class MessagesController implements NotificationCenterDelegate {
    public static MessagesController Instance = new MessagesController();
    public static final int MESSAGE_SEND_STATE_SENDING = 1;
    public static final int MESSAGE_SEND_STATE_SEND_ERROR = 2;
    public static final int MESSAGE_SEND_STATE_SENT = 0;
    public static final int UPDATE_MASK_ALL = 127;
    public static final int UPDATE_MASK_AVATAR = 2;
    public static final int UPDATE_MASK_CHAT_AVATAR = 8;
    public static final int UPDATE_MASK_CHAT_MEMBERS = 32;
    public static final int UPDATE_MASK_CHAT_NAME = 16;
    public static final int UPDATE_MASK_NAME = 1;
    public static final int UPDATE_MASK_STATUS = 4;
    public static final int UPDATE_MASK_USER_PRINT = 64;
    public static final int chatDidCreated = 15;
    public static final int chatDidFailCreate = 16;
    public static final int chatInfoDidLoaded = 17;
    public static final int closeChats = 5;
    public static final int contactsBookDidLoaded = 14;
    public static final int contactsDidLoaded = 13;
    public static final int dialogsNeedReload = 4;
    public static final int didReceivedNewMessages = 1;
    public static final int encryptedChatCreated = 23;
    public static final int encryptedChatUpdated = 21;
    public static final int mediaCountDidLoaded = 20;
    public static final int mediaDidLoaded = 18;
    public static final int messageReceivedByAck = 9;
    public static final int messageReceivedByServer = 10;
    public static final int messageSendError = 11;
    public static final int messagesDeleted = 6;
    public static final int messagesDidLoaded = 8;
    public static final int messagesReaded = 7;
    public static final int messagesReadedEncrypted = 22;
    public static SecureRandom random = new SecureRandom();
    public static final int reloadSearchResults = 12;
    public static final int updateInterfaces = 3;
    public static final int userPhotosLoaded = 24;
    private SparseArray<EncryptedChat> acceptingChats = new SparseArray();
    public ConcurrentHashMap<Integer, Chat> chats = new ConcurrentHashMap(100, 1.0f, 1);
    public ArrayList<TL_contact> contacts = new ArrayList();
    public HashMap<Integer, Contact> contactsBook = new HashMap();
    public HashMap<String, TL_contact> contactsByPhones = new HashMap();
    public SparseArray<TL_contact> contactsDict = new SparseArray();
    public HashMap<String, ArrayList<Contact>> contactsSectionsDict = new HashMap();
    private Account currentAccount;
    private Long currentDeletingTask = null;
    private ArrayList<Integer> currentDeletingTaskMids = null;
    private int currentDeletingTaskTime = 0;
    private HashMap<String, DelayedMessage> delayedMessages = new HashMap();
    public SparseArray<MessageObject> dialogMessage = new SparseArray();
    public ArrayList<TL_dialog> dialogs = new ArrayList();
    public boolean dialogsEndReached = false;
    public ArrayList<TL_dialog> dialogsServerOnly = new ArrayList();
    public ConcurrentHashMap<Long, TL_dialog> dialogs_dict = new ConcurrentHashMap(100, 1.0f, 1);
    public ConcurrentHashMap<Integer, EncryptedChat> encryptedChats = new ConcurrentHashMap(10, 1.0f, 1);
    public boolean firstGettingTask = false;
    public boolean gettingDifference = false;
    public boolean gettingDifferenceAgain = false;
    private boolean gettingNewDeleteTask = false;
    public SparseArray<User> hidenAddToContacts = new SparseArray();
    private long lastSoundPlay = 0;
    private long lastStatusUpdateTime = 0;
    public boolean loadingContacts = true;
    public boolean loadingDialogs = false;
    private boolean offlineSended = false;
    public long openned_dialog_id;
    public HashMap<Long, CharSequence> printingStrings = new HashMap();
    public ConcurrentHashMap<Long, ArrayList<PrintingUser>> printingUsers = new ConcurrentHashMap(100, 1.0f, 2);
    public boolean registeringForPush = false;
    public SparseArray<MessageObject> sendingMessages = new SparseArray();
    public ArrayList<String> sortedContactsSectionsArray = new ArrayList();
    public ArrayList<String> sortedUsersSectionsArray = new ArrayList();
    private int sound;
    private SoundPool soundPool;
    public int totalDialogsCount = 0;
    private ArrayList<Updates> updatesQueue = new ArrayList();
    private long updatesStartWaitTime = 0;
    public boolean updatingState = false;
    private String uploadingAvatar = null;
    public ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap(100, 1.0f, 1);
    public HashMap<String, ArrayList<TL_contact>> usersSectionsDict = new HashMap();

    class C03773 implements Runnable {

        class C03661 implements Runnable {
            C03661() {
            }

            public void run() {
                MessagesController.this.getNewDeleteTask(MessagesController.this.currentDeletingTask);
                MessagesController.this.currentDeletingTaskTime = 0;
                MessagesController.this.currentDeletingTask = null;
            }
        }

        C03773() {
        }

        public void run() {
            MessagesController.this.deleteMessages(MessagesController.this.currentDeletingTaskMids);
            Utilities.stageQueue.postRunnable(new C03661());
        }
    }

    public static class Contact {
        public String first_name;
        public int id;
        public String last_name;
        public ArrayList<String> phoneTypes = new ArrayList();
        public ArrayList<String> phones = new ArrayList();
    }

    private class DelayedMessage {
        public TL_document documentLocation;
        public EncryptedChat encryptedChat;
        public FileLocation location;
        public MessageObject obj;
        public TL_decryptedMessage sendEncryptedRequest;
        public TL_messages_sendMedia sendRequest;
        public int type;
        public TL_video videoLocation;

        private DelayedMessage() {
        }
    }

    public static class PrintingUser {
        public long lastTime;
        public int userId;
    }

    private class UserActionUpdates extends Updates {
        private UserActionUpdates() {
        }
    }

    static {
        try {
            FileInputStream sUrandomIn = new FileInputStream(new File("/dev/urandom"));
            byte[] buffer = new byte[1024];
            sUrandomIn.read(buffer);
            sUrandomIn.close();
            random.setSeed(buffer);
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
    }

    public MessagesController() {
        MessagesStorage storage = MessagesStorage.Instance;
        NotificationCenter.Instance.addObserver(this, FileLoader.FileDidUpload);
        NotificationCenter.Instance.addObserver(this, 10001);
        NotificationCenter.Instance.addObserver(this, 10);
        addSupportUser();
        try {
            this.soundPool = new SoundPool(1, 5, 0);
            this.sound = this.soundPool.load(ApplicationLoader.applicationContext, C0419R.raw.sound_a, 1);
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
    }

    public void addSupportUser() {
        TL_userForeign user = new TL_userForeign();
        user.phone = "333";
        user.id = 333000;
        user.first_name = "Telegram";
        user.last_name = BuildConfig.FLAVOR;
        user.status = null;
        user.photo = new TL_userProfilePhotoEmpty();
        this.users.put(Integer.valueOf(user.id), user);
    }

    public void didReceivedNotification(int id, Object... args) {
        if (id == FileLoader.FileDidUpload) {
            fileDidUploaded((String) args[0], (InputFile) args[1], (InputEncryptedFile) args[2]);
        } else if (id == 10001) {
            fileDidFailedUpload((String) args[0]);
        } else if (id == 10) {
            Integer msgId = args[0];
            MessageObject obj = (MessageObject) this.dialogMessage.get(msgId.intValue());
            if (obj != null) {
                long uid;
                Integer newMsgId = args[1];
                this.dialogMessage.remove(msgId.intValue());
                this.dialogMessage.put(newMsgId.intValue(), obj);
                obj.messageOwner.id = newMsgId.intValue();
                obj.messageOwner.send_state = 0;
                if (obj.messageOwner.to_id.chat_id != 0) {
                    uid = (long) (-obj.messageOwner.to_id.chat_id);
                } else {
                    if (obj.messageOwner.to_id.user_id == UserConfig.clientUserId) {
                        obj.messageOwner.to_id.user_id = obj.messageOwner.from_id;
                    }
                    uid = (long) obj.messageOwner.to_id.user_id;
                }
                TL_dialog dialog = (TL_dialog) this.dialogs_dict.get(Long.valueOf(uid));
                if (dialog != null && dialog.top_message == msgId.intValue()) {
                    dialog.top_message = newMsgId.intValue();
                }
                NotificationCenter.Instance.postNotificationName(4, new Object[0]);
            }
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        NotificationCenter.Instance.removeObserver(this, FileLoader.FileDidUpload);
        NotificationCenter.Instance.removeObserver(this, 10001);
        NotificationCenter.Instance.removeObserver(this, 10);
    }

    public void cleanUp() {
        this.dialogs_dict.clear();
        this.dialogs.clear();
        this.dialogsServerOnly.clear();
        this.acceptingChats.clear();
        this.users.clear();
        this.chats.clear();
        this.sendingMessages.clear();
        this.delayedMessages.clear();
        this.dialogMessage.clear();
        this.printingUsers.clear();
        this.printingStrings.clear();
        this.totalDialogsCount = 0;
        this.contactsBook.clear();
        this.contactsSectionsDict.clear();
        this.sortedContactsSectionsArray.clear();
        this.contacts.clear();
        this.contactsDict.clear();
        this.usersSectionsDict.clear();
        this.sortedUsersSectionsArray.clear();
        this.contactsByPhones.clear();
        this.hidenAddToContacts.clear();
        this.updatesQueue.clear();
        this.updatesStartWaitTime = 0;
        this.currentDeletingTaskTime = 0;
        this.currentDeletingTaskMids = null;
        this.gettingNewDeleteTask = false;
        this.currentDeletingTask = null;
        this.loadingContacts = false;
        this.loadingDialogs = false;
        this.dialogsEndReached = false;
        this.gettingDifference = false;
        this.gettingDifferenceAgain = false;
        this.firstGettingTask = false;
        this.updatingState = false;
        this.lastStatusUpdateTime = 0;
        this.offlineSended = false;
        this.registeringForPush = false;
        this.uploadingAvatar = null;
        addSupportUser();
    }

    public void didAddedNewTask(final int minDate) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                if ((MessagesController.this.currentDeletingTask == null && !MessagesController.this.gettingNewDeleteTask) || (MessagesController.this.currentDeletingTaskTime != 0 && minDate < MessagesController.this.currentDeletingTaskTime)) {
                    MessagesController.this.getNewDeleteTask(null);
                }
            }
        });
    }

    public void getNewDeleteTask(final Long oldTask) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                MessagesController.this.gettingNewDeleteTask = true;
                MessagesStorage.Instance.getNewTask(oldTask);
            }
        });
    }

    private void checkDeletingTask() {
        int currentServerTime = ConnectionsManager.Instance.getCurrentTime();
        if (this.currentDeletingTask != null && this.currentDeletingTaskTime != 0 && this.currentDeletingTaskTime <= currentServerTime) {
            this.currentDeletingTaskTime = 0;
            Utilities.RunOnUIThread(new C03773());
        }
    }

    public void processLoadedDeleteTask(final Long taskId, final int taskTime, final ArrayList<Integer> messages) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                MessagesController.this.gettingNewDeleteTask = false;
                if (taskId != null) {
                    MessagesController.this.currentDeletingTaskTime = taskTime;
                    MessagesController.this.currentDeletingTask = taskId;
                    MessagesController.this.currentDeletingTaskMids = messages;
                    MessagesController.this.checkDeletingTask();
                    return;
                }
                MessagesController.this.currentDeletingTaskTime = 0;
                MessagesController.this.currentDeletingTask = null;
                MessagesController.this.currentDeletingTaskMids = null;
            }
        });
    }

    public void checkAppAccount() {
        AccountManager am = AccountManager.get(ApplicationLoader.applicationContext);
        Account[] accounts = am.getAccountsByType("org.telegram.messenger.account");
        boolean recreateAccount = false;
        if (UserConfig.currentUser != null) {
            if (accounts.length == 1) {
                Account acc = accounts[0];
                if (acc.name.equals(UserConfig.currentUser.phone)) {
                    this.currentAccount = acc;
                } else {
                    recreateAccount = true;
                }
            } else {
                recreateAccount = true;
            }
        } else if (accounts.length > 0) {
            recreateAccount = true;
        }
        if (recreateAccount) {
            for (Account c : accounts) {
                am.removeAccount(c, null, null);
            }
            if (UserConfig.currentUser != null) {
                this.currentAccount = new Account(UserConfig.currentUser.phone, "org.telegram.messenger.account");
                am.addAccountExplicitly(this.currentAccount, BuildConfig.FLAVOR, null);
            }
        }
    }

    public void deleteAllAppAccounts() {
        try {
            AccountManager am = AccountManager.get(ApplicationLoader.applicationContext);
            for (Account c : am.getAccountsByType("org.telegram.messenger.account")) {
                am.removeAccount(c, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadUserPhotos(int uid, int offset, int count, long max_id, boolean fromCache, int classGuid) {
        if (fromCache) {
            MessagesStorage.Instance.getUserPhotos(uid, offset, count, max_id, classGuid);
            return;
        }
        User user = (User) this.users.get(Integer.valueOf(uid));
        if (user != null) {
            InputUser inputUser;
            TLObject req = new TL_photos_getUserPhotos();
            req.limit = count;
            req.offset = offset;
            req.max_id = (int) max_id;
            if ((user instanceof TL_userForeign) || (user instanceof TL_userRequest)) {
                inputUser = new TL_inputUserForeign();
                inputUser.user_id = user.id;
                inputUser.access_hash = user.access_hash;
            } else {
                inputUser = new TL_inputUserContact();
                inputUser.user_id = user.id;
            }
            req.user_id = inputUser;
            ConnectionsManager connectionsManager = ConnectionsManager.Instance;
            final int i = uid;
            final int i2 = offset;
            final int i3 = count;
            final long j = max_id;
            final boolean z = fromCache;
            final int i4 = classGuid;
            C08525 c08525 = new RPCRequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    if (error == null) {
                        MessagesController.this.processLoadedUserPhotos((photos_Photos) response, i, i2, i3, j, z, i4);
                    }
                }
            };
            ConnectionsManager.Instance.bindRequestToGuid(Long.valueOf(connectionsManager.performRpc(req, c08525, null, true, RPCRequest.RPCRequestClassGeneric)), classGuid);
        }
    }

    public void processLoadedUserPhotos(photos_Photos res, int uid, int offset, int count, long max_id, boolean fromCache, int classGuid) {
        if (!fromCache) {
            MessagesStorage.Instance.putUserPhotos(uid, res);
        } else if (res == null || res.photos.isEmpty()) {
            loadUserPhotos(uid, offset, count, max_id, false, classGuid);
            return;
        }
        final int i = uid;
        final int i2 = offset;
        final int i3 = count;
        final boolean z = fromCache;
        final int i4 = classGuid;
        final photos_Photos org_telegram_TL_TLRPC_photos_Photos = res;
        Utilities.RunOnUIThread(new Runnable() {
            public void run() {
                NotificationCenter.Instance.postNotificationName(24, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Boolean.valueOf(z), Integer.valueOf(i4), org_telegram_TL_TLRPC_photos_Photos.photos);
            }
        });
    }

    public void processLoadedMedia(messages_Messages res, long uid, int offset, int count, int max_id, boolean fromCache, int classGuid) {
        int lower_part = (int) uid;
        if (fromCache && res.messages.isEmpty() && lower_part != 0) {
            loadMedia(uid, offset, count, max_id, false, classGuid);
            return;
        }
        if (!fromCache) {
            MessagesStorage.Instance.putUsersAndChats(res.users, res.chats, true, true);
            MessagesStorage.Instance.putMedia(uid, res.messages);
        }
        HashMap<Integer, User> usersLocal = new HashMap();
        Iterator i$ = res.users.iterator();
        while (i$.hasNext()) {
            User u = (User) i$.next();
            usersLocal.put(Integer.valueOf(u.id), u);
        }
        final ArrayList<MessageObject> objects = new ArrayList();
        i$ = res.messages.iterator();
        while (i$.hasNext()) {
            objects.add(new MessageObject((Message) i$.next(), usersLocal));
        }
        final messages_Messages org_telegram_TL_TLRPC_messages_Messages = res;
        final boolean z = fromCache;
        final long j = uid;
        final int i = classGuid;
        Utilities.RunOnUIThread(new Runnable() {
            public void run() {
                int totalCount;
                if (org_telegram_TL_TLRPC_messages_Messages instanceof TL_messages_messagesSlice) {
                    totalCount = org_telegram_TL_TLRPC_messages_Messages.count;
                } else {
                    totalCount = org_telegram_TL_TLRPC_messages_Messages.messages.size();
                }
                Iterator i$ = org_telegram_TL_TLRPC_messages_Messages.users.iterator();
                while (i$.hasNext()) {
                    User user = (User) i$.next();
                    if (z) {
                        MessagesController.this.users.putIfAbsent(Integer.valueOf(user.id), user);
                    } else {
                        MessagesController.this.users.put(Integer.valueOf(user.id), user);
                        if (user.id == UserConfig.clientUserId) {
                            UserConfig.currentUser = user;
                        }
                    }
                }
                i$ = org_telegram_TL_TLRPC_messages_Messages.chats.iterator();
                while (i$.hasNext()) {
                    Chat chat = (Chat) i$.next();
                    if (z) {
                        MessagesController.this.chats.putIfAbsent(Integer.valueOf(chat.id), chat);
                    } else {
                        MessagesController.this.chats.put(Integer.valueOf(chat.id), chat);
                    }
                }
                NotificationCenter.Instance.postNotificationName(18, Long.valueOf(j), Integer.valueOf(totalCount), objects, Boolean.valueOf(z), Integer.valueOf(i));
            }
        });
    }

    public void loadMedia(long uid, int offset, int count, int max_id, boolean fromCache, int classGuid) {
        int lower_part = (int) uid;
        if (fromCache || lower_part == 0) {
            MessagesStorage.Instance.loadMedia(uid, offset, count, max_id, classGuid);
            return;
        }
        TLObject req = new TL_messages_search();
        req.offset = offset;
        req.limit = count;
        req.max_id = max_id;
        req.filter = new TL_inputMessagesFilterPhotoVideo();
        req.f63q = BuildConfig.FLAVOR;
        if (uid < 0) {
            req.peer = new TL_inputPeerChat();
            req.peer.chat_id = -lower_part;
        } else {
            User user = (User) this.users.get(Integer.valueOf(lower_part));
            if ((user instanceof TL_userForeign) || (user instanceof TL_userRequest)) {
                req.peer = new TL_inputPeerForeign();
                req.peer.access_hash = user.access_hash;
            } else {
                req.peer = new TL_inputPeerContact();
            }
            req.peer.user_id = lower_part;
        }
        ConnectionsManager connectionsManager = ConnectionsManager.Instance;
        final long j = uid;
        final int i = offset;
        final int i2 = count;
        final int i3 = max_id;
        final int i4 = classGuid;
        C08558 c08558 = new RPCRequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    MessagesController.this.processLoadedMedia((messages_Messages) response, j, i, i2, i3, false, i4);
                }
            }
        };
        ConnectionsManager.Instance.bindRequestToGuid(Long.valueOf(connectionsManager.performRpc(req, c08558, null, true, RPCRequest.RPCRequestClassGeneric)), classGuid);
    }

    public void processLoadedMediaCount(int count, long uid, int classGuid, boolean fromCache) {
        final long j = uid;
        final boolean z = fromCache;
        final int i = count;
        final int i2 = classGuid;
        Utilities.RunOnUIThread(new Runnable() {
            public void run() {
                int lower_part = (int) j;
                if (z && i == -1 && lower_part != 0) {
                    MessagesController.this.getMediaCount(j, i2, false);
                    return;
                }
                if (!z) {
                    MessagesStorage.Instance.putMediaCount(j, i);
                }
                if (z && i == -1) {
                    NotificationCenter.Instance.postNotificationName(20, Long.valueOf(j), Integer.valueOf(0), Boolean.valueOf(z));
                    return;
                }
                NotificationCenter.Instance.postNotificationName(20, Long.valueOf(j), Integer.valueOf(i), Boolean.valueOf(z));
            }
        });
    }

    public void getMediaCount(final long uid, final int classGuid, boolean fromCache) {
        int lower_part = (int) uid;
        if (fromCache || lower_part == 0) {
            MessagesStorage.Instance.getMediaCount(uid, classGuid);
            return;
        }
        TL_messages_search req = new TL_messages_search();
        req.offset = 0;
        req.limit = 1;
        req.max_id = 0;
        req.filter = new TL_inputMessagesFilterPhotoVideo();
        req.f63q = BuildConfig.FLAVOR;
        if (uid < 0) {
            req.peer = new TL_inputPeerChat();
            req.peer.chat_id = -lower_part;
        } else {
            User user = (User) this.users.get(Integer.valueOf(lower_part));
            if ((user instanceof TL_userForeign) || (user instanceof TL_userRequest)) {
                req.peer = new TL_inputPeerForeign();
                req.peer.access_hash = user.access_hash;
            } else {
                req.peer = new TL_inputPeerContact();
            }
            req.peer.user_id = lower_part;
        }
        ConnectionsManager.Instance.bindRequestToGuid(Long.valueOf(ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    messages_Messages res = (messages_Messages) response;
                    if (res instanceof TL_messages_messagesSlice) {
                        MessagesController.this.processLoadedMediaCount(res.count, uid, classGuid, false);
                    } else {
                        MessagesController.this.processLoadedMediaCount(res.messages.size(), uid, classGuid, false);
                    }
                }
            }
        }, null, true, RPCRequest.RPCRequestClassGeneric)), classGuid);
    }

    public void uploadAndApplyUserAvatar(PhotoSize bigPhoto) {
        if (bigPhoto != null) {
            this.uploadingAvatar = Utilities.getCacheDir() + "/" + bigPhoto.location.volume_id + "_" + bigPhoto.location.local_id + ".jpg";
            FileLoader.Instance.uploadFile(this.uploadingAvatar, null, null);
        }
    }

    public void readContacts() {
        if (this.contactsBook.size() == 0) {
            Utilities.globalQueue.postRunnable(new Runnable() {

                class C03511 implements Comparator<Contact> {
                    C03511() {
                    }

                    public int compare(Contact contact, Contact contact2) {
                        String toComapre1 = contact.first_name;
                        if (toComapre1.length() == 0) {
                            toComapre1 = contact.last_name;
                        }
                        String toComapre2 = contact2.first_name;
                        if (toComapre2.length() == 0) {
                            toComapre2 = contact2.last_name;
                        }
                        return toComapre1.compareTo(toComapre2);
                    }
                }

                class C03522 implements Comparator<String> {
                    C03522() {
                    }

                    public int compare(String s, String s2) {
                        char cv1 = s.charAt(0);
                        char cv2 = s2.charAt(0);
                        if (cv1 == '#') {
                            return 1;
                        }
                        if (cv2 == '#') {
                            return -1;
                        }
                        return s.compareTo(s2);
                    }
                }

                public void run() {
                    Contact contact;
                    FileLog.m800e("tmessages", "start read contacts from phone");
                    HashMap<Integer, Contact> contactsMap = new HashMap();
                    HashMap<String, ArrayList<Contact>> sectionsDict = new HashMap();
                    ArrayList<String> sortedSectionsArray = new ArrayList();
                    ContentResolver cr = ApplicationLoader.applicationContext.getContentResolver();
                    String[] projectioPhones = new String[]{"contact_id", "data1", "data2", "data3"};
                    String ids = BuildConfig.FLAVOR;
                    Cursor pCur = cr.query(Phone.CONTENT_URI, projectioPhones, null, null, null);
                    if (pCur != null) {
                        if (pCur.getCount() > 0) {
                            while (pCur.moveToNext()) {
                                String number = pCur.getString(1);
                                if (!(number == null || number.length() == 0)) {
                                    Integer id = Integer.valueOf(pCur.getInt(0));
                                    if (ids.length() != 0) {
                                        ids = ids + ",";
                                    }
                                    ids = ids + id;
                                    int type = pCur.getInt(2);
                                    contact = (Contact) contactsMap.get(id);
                                    if (contact == null) {
                                        contact = new Contact();
                                        contact.first_name = BuildConfig.FLAVOR;
                                        contact.last_name = BuildConfig.FLAVOR;
                                        contactsMap.put(id, contact);
                                        contact.id = id.intValue();
                                    }
                                    contact.phones.add(number);
                                    if (type == 0) {
                                        contact.phoneTypes.add(pCur.getString(3));
                                    } else if (type == 1) {
                                        contact.phoneTypes.add(ApplicationLoader.applicationContext.getString(C0419R.string.PhoneHome));
                                    } else if (type == 2) {
                                        contact.phoneTypes.add(ApplicationLoader.applicationContext.getString(C0419R.string.PhoneMobile));
                                    } else if (type == 3) {
                                        contact.phoneTypes.add(ApplicationLoader.applicationContext.getString(C0419R.string.PhoneWork));
                                    } else if (type == 12) {
                                        contact.phoneTypes.add(ApplicationLoader.applicationContext.getString(C0419R.string.PhoneMain));
                                    } else {
                                        contact.phoneTypes.add(ApplicationLoader.applicationContext.getString(C0419R.string.PhoneOther));
                                    }
                                }
                            }
                        }
                        pCur.close();
                    }
                    ContentResolver contentResolver = cr;
                    pCur = contentResolver.query(Data.CONTENT_URI, new String[]{"contact_id", "data2", "data3", "display_name", "data5"}, "contact_id IN (" + ids + ") AND " + "mimetype" + " = '" + "vnd.android.cursor.item/name" + "'", null, null);
                    if (pCur != null && pCur.getCount() > 0) {
                        while (pCur.moveToNext()) {
                            int id2 = pCur.getInt(0);
                            String fname = pCur.getString(1);
                            String sname = pCur.getString(2);
                            String sname2 = pCur.getString(3);
                            String mname = pCur.getString(4);
                            contact = (Contact) contactsMap.get(Integer.valueOf(id2));
                            if (contact != null) {
                                contact.first_name = fname;
                                contact.last_name = sname;
                                if (contact.first_name == null) {
                                    contact.first_name = BuildConfig.FLAVOR;
                                }
                                if (!(mname == null || mname.length() == 0)) {
                                    if (contact.first_name.length() != 0) {
                                        contact.first_name += " " + mname;
                                    } else {
                                        contact.first_name = mname;
                                    }
                                }
                                if (contact.last_name == null) {
                                    contact.last_name = BuildConfig.FLAVOR;
                                }
                                if (contact.last_name.length() == 0 && contact.first_name.length() == 0 && sname2 != null && sname2.length() != 0) {
                                    contact.first_name = sname2;
                                }
                            }
                        }
                        pCur.close();
                    }
                    ArrayList<TL_inputPhoneContact> toImport = new ArrayList();
                    String contactsImportHash = BuildConfig.FLAVOR;
                    MessageDigest mdEnc = null;
                    try {
                        mdEnc = MessageDigest.getInstance("MD5");
                    } catch (Exception e) {
                        FileLog.m799e("tmessages", e);
                    }
                    for (Entry<Integer, Contact> pair : contactsMap.entrySet()) {
                        Contact value = (Contact) pair.getValue();
                        id2 = ((Integer) pair.getKey()).intValue();
                        for (int a = 0; a < value.phones.size(); a++) {
                            TL_inputPhoneContact imp = new TL_inputPhoneContact();
                            imp.client_id = (long) id2;
                            imp.first_name = value.first_name;
                            imp.last_name = value.last_name;
                            imp.phone = PhoneFormat.stripExceptNumbers((String) value.phones.get(a));
                            toImport.add(imp);
                            String str = imp.client_id + imp.first_name + imp.last_name + imp.phone;
                            if (mdEnc != null) {
                                mdEnc.update(str.getBytes());
                            }
                        }
                        String key = value.first_name;
                        if (key.length() == 0) {
                            key = value.last_name;
                        }
                        if (key.length() == 0) {
                            key = "#";
                            if (value.phones.size() != 0) {
                                value.first_name = "+" + ((String) value.phones.get(0));
                            }
                        } else {
                            key = key.toUpperCase();
                        }
                        if (key.length() > 1) {
                            key = key.substring(0, 1);
                        }
                        ArrayList<Contact> arr = (ArrayList) sectionsDict.get(key);
                        if (arr == null) {
                            arr = new ArrayList();
                            sectionsDict.put(key, arr);
                            sortedSectionsArray.add(key);
                        }
                        arr.add(value);
                    }
                    for (Entry<String, ArrayList<Contact>> entry : sectionsDict.entrySet()) {
                        Collections.sort((List) entry.getValue(), new C03511());
                    }
                    Collections.sort(sortedSectionsArray, new C03522());
                    String importHash = String.format(Locale.US, "%32s", new Object[]{new BigInteger(1, mdEnc.digest()).toString(16)}).replace(' ', '0');
                    if (toImport.isEmpty() || UserConfig.importHash.equals(importHash)) {
                        MessagesController.this.loadContacts(true);
                    } else {
                        UserConfig.importHash = importHash;
                        UserConfig.saveConfig(false);
                        MessagesController.this.importContacts(toImport);
                    }
                    final HashMap<Integer, Contact> hashMap = contactsMap;
                    final HashMap<String, ArrayList<Contact>> hashMap2 = sectionsDict;
                    final ArrayList<String> arrayList = sortedSectionsArray;
                    Utilities.RunOnUIThread(new Runnable() {
                        public void run() {
                            MessagesController.this.contactsBook = hashMap;
                            MessagesController.this.contactsSectionsDict = hashMap2;
                            MessagesController.this.sortedContactsSectionsArray = arrayList;
                            NotificationCenter.Instance.postNotificationName(14, new Object[0]);
                        }
                    });
                }
            });
        }
    }

    private void performSyncContacts() {
        Utilities.globalQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    Uri rawContactUri = RawContacts.CONTENT_URI.buildUpon().appendQueryParameter("account_name", MessagesController.this.currentAccount.name).appendQueryParameter("account_type", MessagesController.this.currentAccount.type).build();
                    Cursor c1 = ApplicationLoader.applicationContext.getContentResolver().query(rawContactUri, new String[]{"_id", "sync2"}, null, null, null);
                    HashMap<Integer, Long> bookContacts = new HashMap();
                    if (c1 != null) {
                        while (c1.moveToNext()) {
                            bookContacts.put(Integer.valueOf(c1.getInt(1)), Long.valueOf(c1.getLong(0)));
                        }
                        c1.close();
                        Iterator i$ = MessagesController.this.contacts.iterator();
                        while (i$.hasNext()) {
                            TL_contact u = (TL_contact) i$.next();
                            if (!bookContacts.containsKey(Integer.valueOf(u.user_id))) {
                                User user = (User) MessagesController.this.users.get(Integer.valueOf(u.user_id));
                                MessagesController.addContact(MessagesController.this.currentAccount, user, user.phone);
                            }
                        }
                    }
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
        });
    }

    public static long addContact(Account account, User user, String phone) {
        long j = -1;
        if (!(account == null || user == null || phone == null)) {
            ArrayList<ContentProviderOperation> query = new ArrayList();
            Builder builder = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI);
            builder.withValue("account_name", account.name);
            builder.withValue("account_type", account.type);
            builder.withValue("sync1", phone);
            builder.withValue("sync2", Integer.valueOf(user.id));
            query.add(builder.build());
            builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
            builder.withValueBackReference("raw_contact_id", 0);
            builder.withValue("mimetype", "vnd.android.cursor.item/name");
            builder.withValue("data2", user.first_name);
            builder.withValue("data3", user.last_name);
            query.add(builder.build());
            builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
            builder.withValueBackReference("raw_contact_id", 0);
            builder.withValue("mimetype", "vnd.android.cursor.item/phone_v2");
            builder.withValue("data1", "+" + phone);
            builder.withValue("data2", Integer.valueOf(2));
            query.add(builder.build());
            builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
            builder.withValueBackReference("raw_contact_id", 0);
            builder.withValue("mimetype", "vnd.android.cursor.item/vnd.org.telegram.messenger.android.profile");
            builder.withValue("data1", "+" + phone);
            builder.withValue("data2", "Telegram Profile");
            builder.withValue("data3", "+" + phone);
            builder.withValue("data4", Integer.valueOf(user.id));
            query.add(builder.build());
            try {
                j = Long.parseLong(ApplicationLoader.applicationContext.getContentResolver().applyBatch("com.android.contacts", query)[0].uri.getLastPathSegment());
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            }
        }
        return j;
    }

    public void deleteMessages(ArrayList<Integer> messages) {
        Iterator i$ = messages.iterator();
        while (i$.hasNext()) {
            MessageObject obj = (MessageObject) this.dialogMessage.get(((Integer) i$.next()).intValue());
            if (obj != null) {
                obj.deleted = true;
            }
        }
        MessagesStorage.Instance.markMessagesAsDeleted(messages, true);
        MessagesStorage.Instance.updateDialogsWithDeletedMessages(messages, true);
        NotificationCenter.Instance.postNotificationName(6, messages);
        ArrayList<Integer> toSend = new ArrayList();
        i$ = messages.iterator();
        while (i$.hasNext()) {
            Integer mid = (Integer) i$.next();
            if (mid.intValue() > 0) {
                toSend.add(mid);
            }
        }
        if (!toSend.isEmpty()) {
            TL_messages_deleteMessages req = new TL_messages_deleteMessages();
            req.id = messages;
            ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
                public void run(TLObject response, TL_error error) {
                }
            }, null, true, RPCRequest.RPCRequestClassGeneric);
        }
    }

    public void deleteDialog(final long did, int offset, final boolean onlyHistory) {
        TL_dialog dialog = (TL_dialog) this.dialogs_dict.get(Long.valueOf(did));
        if (dialog != null) {
            int lower_part = (int) did;
            if (offset == 0) {
                if (!onlyHistory) {
                    this.dialogs.remove(dialog);
                    this.dialogsServerOnly.remove(dialog);
                    this.dialogs_dict.remove(Long.valueOf(did));
                    this.totalDialogsCount--;
                }
                this.dialogMessage.remove(dialog.top_message);
                MessagesStorage.Instance.deleteDialog(did, onlyHistory);
                NotificationCenter.Instance.postNotificationName(4, new Object[0]);
            }
            if (lower_part != 0) {
                TL_messages_deleteHistory req = new TL_messages_deleteHistory();
                req.offset = offset;
                if (did < 0) {
                    req.peer = new TL_inputPeerChat();
                    req.peer.chat_id = -lower_part;
                } else {
                    User user = (User) this.users.get(Integer.valueOf(lower_part));
                    if ((user instanceof TL_userForeign) || (user instanceof TL_userRequest)) {
                        req.peer = new TL_inputPeerForeign();
                        req.peer.access_hash = user.access_hash;
                    } else {
                        req.peer = new TL_inputPeerContact();
                    }
                    req.peer.user_id = lower_part;
                }
                ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            TL_messages_affectedHistory res = (TL_messages_affectedHistory) response;
                            if (res.offset > 0) {
                                MessagesController.this.deleteDialog(did, res.offset, onlyHistory);
                            }
                            if (MessagesStorage.lastSeqValue + 1 == res.seq) {
                                MessagesStorage.lastSeqValue = res.seq;
                                MessagesStorage.lastPtsValue = res.pts;
                                MessagesStorage.Instance.saveDiffParams(MessagesStorage.lastSeqValue, MessagesStorage.lastPtsValue, MessagesStorage.lastDateValue, MessagesStorage.lastQtsValue);
                            } else if (MessagesStorage.lastSeqValue != res.seq) {
                                FileLog.m800e("tmessages", "need get diff TL_messages_deleteHistory, seq: " + MessagesStorage.lastSeqValue + " " + res.seq);
                                if (MessagesController.this.gettingDifference || MessagesController.this.updatesStartWaitTime == 0 || (MessagesController.this.updatesStartWaitTime != 0 && MessagesController.this.updatesStartWaitTime + 1500 > System.currentTimeMillis())) {
                                    if (MessagesController.this.updatesStartWaitTime == 0) {
                                        MessagesController.this.updatesStartWaitTime = System.currentTimeMillis();
                                    }
                                    FileLog.m800e("tmessages", "add TL_messages_deleteHistory to queue");
                                    UserActionUpdates updates = new UserActionUpdates();
                                    updates.seq = res.seq;
                                    MessagesController.this.updatesQueue.add(updates);
                                    return;
                                }
                                MessagesController.this.getDifference();
                            }
                        }
                    }
                }, null, true, RPCRequest.RPCRequestClassGeneric);
                return;
            }
            declineSecretChat((int) (did >> 32));
        }
    }

    public void loadChatInfo(int chat_id) {
        MessagesStorage.Instance.loadChatInfo(chat_id);
    }

    public void processChatInfo(final int chat_id, ChatParticipants info, ArrayList<User> usersArr, boolean fromCache) {
        if (info == null && fromCache) {
            TL_messages_getFullChat req = new TL_messages_getFullChat();
            req.chat_id = chat_id;
            ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    if (error == null) {
                        final TL_messages_chatFull res = (TL_messages_chatFull) response;
                        MessagesStorage.Instance.putUsersAndChats(res.users, res.chats, true, true);
                        MessagesStorage.Instance.updateChatInfo(chat_id, res.full_chat.participants, false);
                        Utilities.RunOnUIThread(new Runnable() {
                            public void run() {
                                Iterator i$ = res.users.iterator();
                                while (i$.hasNext()) {
                                    User user = (User) i$.next();
                                    MessagesController.this.users.put(Integer.valueOf(user.id), user);
                                    if (user.id == UserConfig.clientUserId) {
                                        UserConfig.currentUser = user;
                                    }
                                }
                                i$ = res.chats.iterator();
                                while (i$.hasNext()) {
                                    Chat chat = (Chat) i$.next();
                                    MessagesController.this.chats.put(Integer.valueOf(chat.id), chat);
                                }
                                NotificationCenter.Instance.postNotificationName(17, Integer.valueOf(chat_id), res.full_chat.participants);
                            }
                        });
                    }
                }
            }, null, true, RPCRequest.RPCRequestClassGeneric);
            return;
        }
        final ArrayList<User> arrayList = usersArr;
        final boolean z = fromCache;
        final int i = chat_id;
        final ChatParticipants chatParticipants = info;
        Utilities.RunOnUIThread(new Runnable() {
            public void run() {
                Iterator i$ = arrayList.iterator();
                while (i$.hasNext()) {
                    User user = (User) i$.next();
                    if (z) {
                        MessagesController.this.users.putIfAbsent(Integer.valueOf(user.id), user);
                    } else {
                        MessagesController.this.users.put(Integer.valueOf(user.id), user);
                        if (user.id == UserConfig.clientUserId) {
                            UserConfig.currentUser = user;
                        }
                    }
                }
                NotificationCenter.Instance.postNotificationName(17, Integer.valueOf(i), chatParticipants);
            }
        });
    }

    public void updateTimerProc() {
        long currentTime = System.currentTimeMillis();
        checkDeletingTask();
        if (UserConfig.clientUserId != 0) {
            TL_account_updateStatus req;
            if (ApplicationLoader.lastPauseTime == 0) {
                if (this.lastStatusUpdateTime != -1 && (this.lastStatusUpdateTime == 0 || this.lastStatusUpdateTime <= System.currentTimeMillis() - 55000 || this.offlineSended)) {
                    this.lastStatusUpdateTime = -1;
                    req = new TL_account_updateStatus();
                    req.offline = false;
                    ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
                        public void run(TLObject response, TL_error error) {
                            MessagesController.this.lastStatusUpdateTime = System.currentTimeMillis();
                        }
                    }, null, true, RPCRequest.RPCRequestClassGeneric);
                    this.offlineSended = false;
                }
            } else if (!this.offlineSended && ApplicationLoader.lastPauseTime <= System.currentTimeMillis() - 2000) {
                req = new TL_account_updateStatus();
                req.offline = true;
                ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                    }
                }, null, true, RPCRequest.RPCRequestClassGeneric);
                this.offlineSended = true;
            }
            if (this.updatesStartWaitTime != 0 && this.updatesStartWaitTime + 1500 < currentTime) {
                FileLog.m800e("tmessages", "UPDATES WAIT TIMEOUT - CHECK QUEUE");
                processUpdatesQueue(false);
            }
        }
        if (!this.printingUsers.isEmpty()) {
            boolean updated = false;
            ArrayList<Long> keys = new ArrayList(this.printingUsers.keySet());
            int b = 0;
            while (b < keys.size()) {
                Long key = (Long) keys.get(b);
                ArrayList<PrintingUser> arr = (ArrayList) this.printingUsers.get(key);
                int a = 0;
                while (a < arr.size()) {
                    PrintingUser user = (PrintingUser) arr.get(a);
                    if (user.lastTime + 5900 < currentTime) {
                        updated = true;
                        arr.remove(user);
                        a--;
                    }
                    a++;
                }
                if (arr.isEmpty()) {
                    this.printingUsers.remove(key);
                    keys.remove(b);
                    b--;
                }
                b++;
            }
            updatePrintingStrings();
            if (updated) {
                Utilities.RunOnUIThread(new Runnable() {
                    public void run() {
                        NotificationCenter.Instance.postNotificationName(3, Integer.valueOf(64));
                    }
                });
            }
        }
    }

    public void updatePrintingStrings() {
        final HashMap<Long, CharSequence> newPrintingStrings = new HashMap();
        Iterator it = new ArrayList(this.printingUsers.keySet()).iterator();
        while (it.hasNext()) {
            Long key = (Long) it.next();
            if (key.longValue() > 0) {
                newPrintingStrings.put(key, ApplicationLoader.applicationContext.getString(C0419R.string.Typing));
            } else {
                ArrayList<PrintingUser> arr = (ArrayList) this.printingUsers.get(key);
                int count = 0;
                String label = BuildConfig.FLAVOR;
                Iterator i$ = arr.iterator();
                while (i$.hasNext()) {
                    User user = (User) this.users.get(Integer.valueOf(((PrintingUser) i$.next()).userId));
                    if (user != null) {
                        if (label.length() != 0) {
                            label = label + ", ";
                        }
                        label = label + Utilities.formatName(user.first_name, user.last_name);
                        count++;
                    }
                    if (count == 2) {
                        break;
                    }
                }
                if (label.length() != 0) {
                    if (count <= 1) {
                        newPrintingStrings.put(key, Html.fromHtml(String.format("%s %s", new Object[]{label, ApplicationLoader.applicationContext.getString(C0419R.string.IsTyping)})));
                    } else if (arr.size() > 2) {
                        Object[] objArr = new Object[3];
                        objArr[0] = label;
                        objArr[1] = String.format(ApplicationLoader.applicationContext.getString(C0419R.string.AndMoreTyping), new Object[]{Integer.valueOf(arr.size() - 2)});
                        objArr[2] = ApplicationLoader.applicationContext.getString(C0419R.string.AreTyping);
                        newPrintingStrings.put(key, Html.fromHtml(String.format("%s %s %s", objArr)));
                    } else {
                        newPrintingStrings.put(key, Html.fromHtml(String.format("%s %s", new Object[]{label, ApplicationLoader.applicationContext.getString(C0419R.string.AreTyping)})));
                    }
                }
            }
        }
        Utilities.RunOnUIThread(new Runnable() {
            public void run() {
                MessagesController.this.printingStrings = newPrintingStrings;
            }
        });
    }

    public void processLoadedContacts(final ArrayList<TL_contact> contactsArr, final ArrayList<User> usersArr, final int from) {
        Utilities.stageQueue.postRunnable(new Runnable() {

            class C03561 implements Comparator<TL_contact> {
                C03561() {
                }

                public int compare(TL_contact tl_contact, TL_contact tl_contact2) {
                    if (tl_contact.user_id > tl_contact2.user_id) {
                        return 1;
                    }
                    if (tl_contact.user_id < tl_contact2.user_id) {
                        return -1;
                    }
                    return 0;
                }
            }

            class C03594 implements Comparator<String> {
                C03594() {
                }

                public int compare(String s, String s2) {
                    char cv1 = s.charAt(0);
                    char cv2 = s2.charAt(0);
                    if (cv1 == '#') {
                        return 1;
                    }
                    if (cv2 == '#') {
                        return -1;
                    }
                    return s.compareTo(s2);
                }
            }

            public void run() {
                FileLog.m800e("tmessages", "done loading contacts");
                if (from == 1 && contactsArr.isEmpty()) {
                    MessagesController.this.loadContacts(false);
                    return;
                }
                Iterator i$;
                if (from == 0 || from == 2 || from == 3) {
                    MessagesStorage.Instance.putUsersAndChats(usersArr, null, true, true);
                    MessagesStorage.Instance.putContacts(contactsArr, true);
                    Collections.sort(MessagesController.this.contacts, new C03561());
                    String ids = BuildConfig.FLAVOR;
                    i$ = contactsArr.iterator();
                    while (i$.hasNext()) {
                        TL_contact aContactsArr = (TL_contact) i$.next();
                        if (ids.length() != 0) {
                            ids = ids + ",";
                        }
                        ids = ids + aContactsArr.user_id;
                    }
                    UserConfig.contactsHash = Utilities.MD5(ids);
                    UserConfig.saveConfig(false);
                    if (from == 2) {
                        MessagesController.this.loadContacts(false);
                    }
                }
                HashMap<Integer, User> usersDict = new HashMap();
                i$ = usersArr.iterator();
                while (i$.hasNext()) {
                    User user = (User) i$.next();
                    usersDict.put(Integer.valueOf(user.id), user);
                }
                final HashMap<Integer, User> hashMap = usersDict;
                Collections.sort(contactsArr, new Comparator<TL_contact>() {
                    public int compare(TL_contact tl_contact, TL_contact tl_contact2) {
                        User user1 = (User) hashMap.get(Integer.valueOf(tl_contact.user_id));
                        User user2 = (User) hashMap.get(Integer.valueOf(tl_contact2.user_id));
                        String name1 = user1.first_name;
                        if (name1 == null || name1.length() == 0) {
                            name1 = user1.last_name;
                        }
                        String name2 = user2.first_name;
                        if (name2 == null || name2.length() == 0) {
                            name2 = user2.last_name;
                        }
                        return name1.compareTo(name2);
                    }
                });
                final HashMap<String, ArrayList<TL_contact>> sectionsDict = new HashMap();
                final SparseArray<TL_contact> contactsDictionery = new SparseArray();
                final ArrayList<String> sortedSectionsArray = new ArrayList();
                final HashMap<String, TL_contact> contactsPhones = new HashMap();
                i$ = contactsArr.iterator();
                while (i$.hasNext()) {
                    TL_contact value = (TL_contact) i$.next();
                    user = (User) usersDict.get(Integer.valueOf(value.user_id));
                    if (user != null) {
                        contactsDictionery.put(value.user_id, value);
                        contactsPhones.put(user.phone, value);
                        String key = user.first_name;
                        if (key == null || key.length() == 0) {
                            key = user.last_name;
                        }
                        if (key.length() == 0) {
                            key = "#";
                        } else {
                            key = key.toUpperCase();
                        }
                        if (key.length() > 1) {
                            key = key.substring(0, 1);
                        }
                        ArrayList<TL_contact> arr = (ArrayList) sectionsDict.get(key);
                        if (arr == null) {
                            arr = new ArrayList();
                            sectionsDict.put(key, arr);
                            sortedSectionsArray.add(key);
                        }
                        arr.add(value);
                    }
                }
                for (Entry<String, ArrayList<TL_contact>> entry : sectionsDict.entrySet()) {
                    hashMap = usersDict;
                    Collections.sort((List) entry.getValue(), new Comparator<TL_contact>() {
                        public int compare(TL_contact contact, TL_contact contact2) {
                            User user1 = (User) hashMap.get(Integer.valueOf(contact.user_id));
                            User user2 = (User) hashMap.get(Integer.valueOf(contact2.user_id));
                            String toComapre1 = user1.first_name;
                            if (toComapre1 == null || toComapre1.length() == 0) {
                                toComapre1 = user1.last_name;
                            }
                            String toComapre2 = user2.first_name;
                            if (toComapre2 == null || toComapre2.length() == 0) {
                                toComapre2 = user2.last_name;
                            }
                            return toComapre1.compareTo(toComapre2);
                        }
                    });
                }
                Collections.sort(sortedSectionsArray, new C03594());
                Utilities.RunOnUIThread(new Runnable() {
                    public void run() {
                        Iterator i$ = usersArr.iterator();
                        while (i$.hasNext()) {
                            User user = (User) i$.next();
                            if (from == 1) {
                                MessagesController.this.users.putIfAbsent(Integer.valueOf(user.id), user);
                            } else {
                                MessagesController.this.users.put(Integer.valueOf(user.id), user);
                                if (user.id == UserConfig.clientUserId) {
                                    UserConfig.currentUser = user;
                                }
                            }
                        }
                        MessagesController.this.contacts = contactsArr;
                        MessagesController.this.contactsByPhones = contactsPhones;
                        MessagesController.this.contactsDict = contactsDictionery;
                        MessagesController.this.usersSectionsDict = sectionsDict;
                        MessagesController.this.sortedUsersSectionsArray = sortedSectionsArray;
                        if (from == 0) {
                            MessagesController.this.loadingContacts = false;
                        }
                        MessagesController.this.performSyncContacts();
                        NotificationCenter.Instance.postNotificationName(13, new Object[0]);
                    }
                });
            }
        });
    }

    public void sendTyping(long dialog_id, int classGuid) {
        if (dialog_id != 0) {
            int lower_part = (int) dialog_id;
            if (lower_part != 0) {
                TL_messages_setTyping req = new TL_messages_setTyping();
                if (lower_part < 0) {
                    req.peer = new TL_inputPeerChat();
                    req.peer.chat_id = -lower_part;
                } else {
                    User user = (User) this.users.get(Integer.valueOf(lower_part));
                    if (user == null) {
                        return;
                    }
                    if ((user instanceof TL_userForeign) || (user instanceof TL_userRequest)) {
                        req.peer = new TL_inputPeerForeign();
                        req.peer.user_id = user.id;
                        req.peer.access_hash = user.access_hash;
                    } else {
                        req.peer = new TL_inputPeerContact();
                        req.peer.user_id = user.id;
                    }
                }
                req.typing = true;
                ConnectionsManager.Instance.bindRequestToGuid(Long.valueOf(ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                    }
                }, null, true, RPCRequest.RPCRequestClassGeneric)), classGuid);
                return;
            }
            EncryptedChat chat = (EncryptedChat) this.encryptedChats.get(Integer.valueOf((int) (dialog_id >> 32)));
            if (chat.auth_key != null && chat.auth_key.length > 1 && (chat instanceof TL_encryptedChat)) {
                TL_messages_setEncryptedTyping req2 = new TL_messages_setEncryptedTyping();
                req2.peer = new TL_inputEncryptedChat();
                req2.peer.chat_id = chat.id;
                req2.peer.access_hash = chat.access_hash;
                req2.typing = true;
                ConnectionsManager.Instance.bindRequestToGuid(Long.valueOf(ConnectionsManager.Instance.performRpc(req2, new RPCRequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                    }
                }, null, true, RPCRequest.RPCRequestClassGeneric)), classGuid);
            }
        }
    }

    public void loadContacts(boolean fromCache) {
        this.loadingContacts = true;
        if (fromCache) {
            FileLog.m800e("tmessages", "load contacts from cache");
            MessagesStorage.Instance.getContacts();
            return;
        }
        FileLog.m800e("tmessages", "load contacts from server");
        TL_contacts_getContacts req = new TL_contacts_getContacts();
        req.hash = UserConfig.contactsHash;
        ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    contacts_Contacts res = (contacts_Contacts) response;
                    if (!(res instanceof TL_contacts_contactsNotModified)) {
                        MessagesController.this.processLoadedContacts(res.contacts, res.users, 0);
                    }
                }
            }
        }, null, true, RPCRequest.RPCRequestClassGeneric);
    }

    public void importContacts(ArrayList<TL_inputPhoneContact> contactsArr) {
        TL_contacts_importContacts req = new TL_contacts_importContacts();
        req.contacts = contactsArr;
        req.replace = false;
        FileLog.m800e("tmessages", "start import contacts");
        ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    FileLog.m800e("tmessages", "contacts imported");
                    TL_contacts_importedContacts res = (TL_contacts_importedContacts) response;
                    MessagesStorage.Instance.putUsersAndChats(res.users, null, true, true);
                    ArrayList<TL_contact> cArr = new ArrayList();
                    Iterator i$ = res.imported.iterator();
                    while (i$.hasNext()) {
                        TL_importedContact c = (TL_importedContact) i$.next();
                        TL_contact contact = new TL_contact();
                        contact.user_id = c.user_id;
                        cArr.add(contact);
                    }
                    MessagesController.this.processLoadedContacts(cArr, res.users, 2);
                    return;
                }
                FileLog.m800e("tmessages", "import contacts error " + error.text);
            }
        }, null, true, RPCRequest.RPCRequestClassGeneric | RPCRequest.RPCRequestClassFailOnServerErrors);
    }

    public void loadMessages(long dialog_id, int offset, int count, int max_id, boolean fromCache, int midDate, int classGuid, boolean from_unread, boolean forward) {
        int lower_part = (int) dialog_id;
        if (fromCache || lower_part == 0) {
            MessagesStorage.Instance.getMessages(dialog_id, offset, count, max_id, midDate, classGuid, from_unread, forward);
            return;
        }
        TLObject req = new TL_messages_getHistory();
        if (lower_part < 0) {
            req.peer = new TL_inputPeerChat();
            req.peer.chat_id = -lower_part;
        } else {
            User user = (User) this.users.get(Integer.valueOf(lower_part));
            if ((user instanceof TL_userForeign) || (user instanceof TL_userRequest)) {
                req.peer = new TL_inputPeerForeign();
                req.peer.user_id = user.id;
                req.peer.access_hash = user.access_hash;
            } else {
                req.peer = new TL_inputPeerContact();
                req.peer.user_id = user.id;
            }
        }
        req.offset = offset;
        req.limit = count;
        req.max_id = max_id;
        ConnectionsManager connectionsManager = ConnectionsManager.Instance;
        final long j = dialog_id;
        final int i = offset;
        final int i2 = count;
        final int i3 = max_id;
        final int i4 = classGuid;
        AnonymousClass26 anonymousClass26 = new RPCRequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    MessagesController.this.processLoadedMessages((messages_Messages) response, j, i, i2, i3, false, i4, 0, 0, 0, 0, false);
                }
            }
        };
        ConnectionsManager.Instance.bindRequestToGuid(Long.valueOf(connectionsManager.performRpc(req, anonymousClass26, null, true, RPCRequest.RPCRequestClassGeneric)), classGuid);
    }

    public void processLoadedMessages(messages_Messages messagesRes, long dialog_id, int offset, int count, int max_id, boolean isCache, int classGuid, int first_unread, int last_unread, int unread_count, int last_date, boolean isForward) {
        final long j = dialog_id;
        final boolean z = isCache;
        final messages_Messages org_telegram_TL_TLRPC_messages_Messages = messagesRes;
        final boolean z2 = isForward;
        final int i = offset;
        final int i2 = count;
        final int i3 = max_id;
        final int i4 = classGuid;
        final int i5 = first_unread;
        final int i6 = last_unread;
        final int i7 = unread_count;
        final int i8 = last_date;
        Utilities.stageQueue.postRunnable(new Runnable() {

            class C03611 implements Runnable {
                C03611() {
                }

                public void run() {
                    MessagesController.this.loadMessages(j, i, i2, i3, false, 0, i4, false, false);
                }
            }

            public void run() {
                int lower_id = (int) j;
                if (!z) {
                    MessagesStorage.Instance.putMessages(org_telegram_TL_TLRPC_messages_Messages, j);
                }
                if (lower_id == 0 || !z || org_telegram_TL_TLRPC_messages_Messages.messages.size() != 0 || z2) {
                    HashMap<Integer, User> usersLocal = new HashMap();
                    Iterator i$ = org_telegram_TL_TLRPC_messages_Messages.users.iterator();
                    while (i$.hasNext()) {
                        User u = (User) i$.next();
                        usersLocal.put(Integer.valueOf(u.id), u);
                    }
                    final ArrayList<MessageObject> objects = new ArrayList();
                    i$ = org_telegram_TL_TLRPC_messages_Messages.messages.iterator();
                    while (i$.hasNext()) {
                        Message message = (Message) i$.next();
                        message.dialog_id = j;
                        objects.add(new MessageObject(message, usersLocal));
                    }
                    Utilities.RunOnUIThread(new Runnable() {
                        public void run() {
                            Iterator i$ = org_telegram_TL_TLRPC_messages_Messages.users.iterator();
                            while (i$.hasNext()) {
                                User u = (User) i$.next();
                                if (!z) {
                                    MessagesController.this.users.put(Integer.valueOf(u.id), u);
                                    if (u.id == UserConfig.clientUserId) {
                                        UserConfig.currentUser = u;
                                    }
                                } else if (u.id == UserConfig.clientUserId || u.id == 333000) {
                                    MessagesController.this.users.put(Integer.valueOf(u.id), u);
                                } else {
                                    MessagesController.this.users.putIfAbsent(Integer.valueOf(u.id), u);
                                }
                            }
                            i$ = org_telegram_TL_TLRPC_messages_Messages.chats.iterator();
                            while (i$.hasNext()) {
                                Chat c = (Chat) i$.next();
                                if (z) {
                                    MessagesController.this.chats.putIfAbsent(Integer.valueOf(c.id), c);
                                } else {
                                    MessagesController.this.chats.put(Integer.valueOf(c.id), c);
                                }
                            }
                            NotificationCenter.Instance.postNotificationName(8, Long.valueOf(j), Integer.valueOf(i), Integer.valueOf(i2), objects, Boolean.valueOf(z), Integer.valueOf(i5), Integer.valueOf(i6), Integer.valueOf(i7), Integer.valueOf(i8), Boolean.valueOf(z2));
                        }
                    });
                    return;
                }
                Utilities.RunOnUIThread(new C03611());
            }
        });
    }

    public void loadDialogs(final int offset, final int serverOffset, final int count, boolean fromCache) {
        if (!this.loadingDialogs) {
            this.loadingDialogs = true;
            if (fromCache) {
                MessagesStorage.Instance.getDialogs(offset, serverOffset, count);
                return;
            }
            TL_messages_getDialogs req = new TL_messages_getDialogs();
            req.offset = serverOffset;
            req.limit = count;
            ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    if (error == null) {
                        MessagesController.this.processLoadedDialogs((messages_Dialogs) response, null, offset, serverOffset, count, false, false);
                    }
                }
            }, null, true, RPCRequest.RPCRequestClassGeneric);
        }
    }

    public void processDialogsUpdate(final messages_Dialogs dialogsRes, ArrayList<EncryptedChat> arrayList) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                final HashMap<Long, TL_dialog> new_dialogs_dict = new HashMap();
                final HashMap<Integer, MessageObject> new_dialogMessage = new HashMap();
                HashMap<Integer, User> usersLocal = new HashMap();
                Iterator i$ = dialogsRes.users.iterator();
                while (i$.hasNext()) {
                    User u = (User) i$.next();
                    usersLocal.put(Integer.valueOf(u.id), u);
                }
                i$ = dialogsRes.messages.iterator();
                while (i$.hasNext()) {
                    Message m = (Message) i$.next();
                    new_dialogMessage.put(Integer.valueOf(m.id), new MessageObject(m, usersLocal));
                }
                i$ = dialogsRes.dialogs.iterator();
                while (i$.hasNext()) {
                    TL_dialog d = (TL_dialog) i$.next();
                    if (d.last_message_date == 0) {
                        MessageObject mess = (MessageObject) new_dialogMessage.get(Integer.valueOf(d.top_message));
                        if (mess != null) {
                            d.last_message_date = mess.messageOwner.date;
                        }
                    }
                    if (d.id == 0) {
                        if (d.peer instanceof TL_peerUser) {
                            d.id = (long) d.peer.user_id;
                        } else if (d.peer instanceof TL_peerChat) {
                            d.id = (long) (-d.peer.chat_id);
                        }
                    }
                    new_dialogs_dict.put(Long.valueOf(d.id), d);
                }
                Utilities.RunOnUIThread(new Runnable() {

                    class C03631 implements Comparator<TL_dialog> {
                        C03631() {
                        }

                        public int compare(TL_dialog tl_dialog, TL_dialog tl_dialog2) {
                            if (tl_dialog.last_message_date == tl_dialog2.last_message_date) {
                                return 0;
                            }
                            if (tl_dialog.last_message_date < tl_dialog2.last_message_date) {
                                return 1;
                            }
                            return -1;
                        }
                    }

                    public void run() {
                        Iterator i$ = dialogsRes.users.iterator();
                        while (i$.hasNext()) {
                            User u = (User) i$.next();
                            MessagesController.this.users.putIfAbsent(Integer.valueOf(u.id), u);
                        }
                        i$ = dialogsRes.chats.iterator();
                        while (i$.hasNext()) {
                            Chat c = (Chat) i$.next();
                            MessagesController.this.chats.putIfAbsent(Integer.valueOf(c.id), c);
                        }
                        for (Entry<Long, TL_dialog> pair : new_dialogs_dict.entrySet()) {
                            long key = ((Long) pair.getKey()).longValue();
                            TL_dialog value = (TL_dialog) pair.getValue();
                            TL_dialog currentDialog = (TL_dialog) MessagesController.this.dialogs_dict.get(Long.valueOf(key));
                            if (currentDialog == null) {
                                MessagesController.this.dialogs_dict.put(Long.valueOf(key), value);
                                MessagesController.this.dialogMessage.put(value.top_message, new_dialogMessage.get(Integer.valueOf(value.top_message)));
                            } else {
                                currentDialog.unread_count = value.unread_count;
                                MessageObject oldMsg = (MessageObject) MessagesController.this.dialogMessage.get(currentDialog.top_message);
                                if (oldMsg != null && currentDialog.top_message <= 0) {
                                    MessageObject newMsg = (MessageObject) new_dialogMessage.get(Integer.valueOf(value.top_message));
                                    if (oldMsg.deleted || newMsg == null || newMsg.messageOwner.date > oldMsg.messageOwner.date) {
                                        MessagesController.this.dialogs_dict.put(Long.valueOf(key), value);
                                        MessagesController.this.dialogMessage.remove(oldMsg.messageOwner.id);
                                        MessagesController.this.dialogMessage.put(value.top_message, new_dialogMessage.get(Integer.valueOf(value.top_message)));
                                    }
                                } else if ((oldMsg != null && oldMsg.deleted) || value.top_message > currentDialog.top_message) {
                                    MessagesController.this.dialogs_dict.put(Long.valueOf(key), value);
                                    if (oldMsg != null) {
                                        MessagesController.this.dialogMessage.remove(oldMsg.messageOwner.id);
                                    }
                                    MessagesController.this.dialogMessage.put(value.top_message, new_dialogMessage.get(Integer.valueOf(value.top_message)));
                                }
                            }
                        }
                        MessagesController.this.dialogs.clear();
                        MessagesController.this.dialogsServerOnly.clear();
                        MessagesController.this.dialogs.addAll(MessagesController.this.dialogs_dict.values());
                        Collections.sort(MessagesController.this.dialogs, new C03631());
                        i$ = MessagesController.this.dialogs.iterator();
                        while (i$.hasNext()) {
                            TL_dialog d = (TL_dialog) i$.next();
                            if (((int) d.id) != 0) {
                                MessagesController.this.dialogsServerOnly.add(d);
                            }
                        }
                        NotificationCenter.Instance.postNotificationName(4, new Object[0]);
                    }
                });
            }
        });
    }

    public void processLoadedDialogs(messages_Dialogs dialogsRes, ArrayList<EncryptedChat> encChats, int offset, int serverOffset, int count, boolean isCache, boolean resetEnd) {
        final boolean z = isCache;
        final messages_Dialogs org_telegram_TL_TLRPC_messages_Dialogs = dialogsRes;
        final boolean z2 = resetEnd;
        final int i = offset;
        final int i2 = serverOffset;
        final int i3 = count;
        final ArrayList<EncryptedChat> arrayList = encChats;
        Utilities.stageQueue.postRunnable(new Runnable() {

            class C03671 implements Runnable {
                C03671() {
                }

                public void run() {
                    Iterator i$ = org_telegram_TL_TLRPC_messages_Dialogs.users.iterator();
                    while (i$.hasNext()) {
                        User u = (User) i$.next();
                        if (!z) {
                            MessagesController.this.users.put(Integer.valueOf(u.id), u);
                            if (u.id == UserConfig.clientUserId) {
                                UserConfig.currentUser = u;
                            }
                        } else if (u.id == UserConfig.clientUserId || u.id == 333000) {
                            MessagesController.this.users.put(Integer.valueOf(u.id), u);
                        } else {
                            MessagesController.this.users.putIfAbsent(Integer.valueOf(u.id), u);
                        }
                    }
                    MessagesController.this.loadingDialogs = false;
                    if (z2) {
                        MessagesController.this.dialogsEndReached = false;
                        NotificationCenter.Instance.postNotificationName(4, new Object[0]);
                    }
                    MessagesController.this.loadDialogs(i, i2, i3, false);
                }
            }

            public void run() {
                if (z && org_telegram_TL_TLRPC_messages_Dialogs.dialogs.size() == 0) {
                    Utilities.RunOnUIThread(new C03671());
                    return;
                }
                int new_totalDialogsCount;
                final HashMap<Long, TL_dialog> new_dialogs_dict = new HashMap();
                final HashMap<Integer, MessageObject> new_dialogMessage = new HashMap();
                HashMap<Integer, User> usersLocal = new HashMap();
                if (!z) {
                    MessagesStorage.Instance.putDialogs(org_telegram_TL_TLRPC_messages_Dialogs);
                }
                if (org_telegram_TL_TLRPC_messages_Dialogs instanceof TL_messages_dialogsSlice) {
                    new_totalDialogsCount = org_telegram_TL_TLRPC_messages_Dialogs.count;
                } else {
                    new_totalDialogsCount = org_telegram_TL_TLRPC_messages_Dialogs.dialogs.size();
                }
                Iterator i$ = org_telegram_TL_TLRPC_messages_Dialogs.users.iterator();
                while (i$.hasNext()) {
                    User u = (User) i$.next();
                    usersLocal.put(Integer.valueOf(u.id), u);
                }
                i$ = org_telegram_TL_TLRPC_messages_Dialogs.messages.iterator();
                while (i$.hasNext()) {
                    Message m = (Message) i$.next();
                    new_dialogMessage.put(Integer.valueOf(m.id), new MessageObject(m, usersLocal));
                }
                i$ = org_telegram_TL_TLRPC_messages_Dialogs.dialogs.iterator();
                while (i$.hasNext()) {
                    TL_dialog d = (TL_dialog) i$.next();
                    if (d.last_message_date == 0) {
                        MessageObject mess = (MessageObject) new_dialogMessage.get(Integer.valueOf(d.top_message));
                        if (mess != null) {
                            d.last_message_date = mess.messageOwner.date;
                        }
                    }
                    if (d.id == 0) {
                        if (d.peer instanceof TL_peerUser) {
                            d.id = (long) d.peer.user_id;
                        } else if (d.peer instanceof TL_peerChat) {
                            d.id = (long) (-d.peer.chat_id);
                        }
                    }
                    new_dialogs_dict.put(Long.valueOf(d.id), d);
                }
                final int arg1 = new_totalDialogsCount;
                Utilities.RunOnUIThread(new Runnable() {

                    class C03681 implements Comparator<TL_dialog> {
                        C03681() {
                        }

                        public int compare(TL_dialog tl_dialog, TL_dialog tl_dialog2) {
                            if (tl_dialog.last_message_date == tl_dialog2.last_message_date) {
                                return 0;
                            }
                            if (tl_dialog.last_message_date < tl_dialog2.last_message_date) {
                                return 1;
                            }
                            return -1;
                        }
                    }

                    public void run() {
                        Iterator i$ = org_telegram_TL_TLRPC_messages_Dialogs.users.iterator();
                        while (i$.hasNext()) {
                            User u = (User) i$.next();
                            if (!z) {
                                MessagesController.this.users.put(Integer.valueOf(u.id), u);
                                if (u.id == UserConfig.clientUserId) {
                                    UserConfig.currentUser = u;
                                }
                            } else if (u.id == UserConfig.clientUserId || u.id == 333000) {
                                MessagesController.this.users.put(Integer.valueOf(u.id), u);
                            } else {
                                MessagesController.this.users.putIfAbsent(Integer.valueOf(u.id), u);
                            }
                        }
                        i$ = org_telegram_TL_TLRPC_messages_Dialogs.chats.iterator();
                        while (i$.hasNext()) {
                            Chat c = (Chat) i$.next();
                            if (z) {
                                MessagesController.this.chats.putIfAbsent(Integer.valueOf(c.id), c);
                            } else {
                                MessagesController.this.chats.put(Integer.valueOf(c.id), c);
                            }
                        }
                        if (arrayList != null) {
                            i$ = arrayList.iterator();
                            while (i$.hasNext()) {
                                EncryptedChat encryptedChat = (EncryptedChat) i$.next();
                                MessagesController.this.encryptedChats.put(Integer.valueOf(encryptedChat.id), encryptedChat);
                            }
                        }
                        MessagesController.this.loadingDialogs = false;
                        MessagesController.this.totalDialogsCount = arg1;
                        for (Entry<Long, TL_dialog> pair : new_dialogs_dict.entrySet()) {
                            long key = ((Long) pair.getKey()).longValue();
                            TL_dialog value = (TL_dialog) pair.getValue();
                            TL_dialog currentDialog = (TL_dialog) MessagesController.this.dialogs_dict.get(Long.valueOf(key));
                            if (currentDialog == null) {
                                MessagesController.this.dialogs_dict.put(Long.valueOf(key), value);
                                MessagesController.this.dialogMessage.put(value.top_message, new_dialogMessage.get(Integer.valueOf(value.top_message)));
                            } else {
                                MessageObject oldMsg = (MessageObject) MessagesController.this.dialogMessage.get(value.top_message);
                                if (oldMsg != null && currentDialog.top_message <= 0) {
                                    MessageObject newMsg = (MessageObject) new_dialogMessage.get(Integer.valueOf(value.top_message));
                                    if (oldMsg.deleted || newMsg == null || newMsg.messageOwner.date > oldMsg.messageOwner.date) {
                                        MessagesController.this.dialogMessage.remove(oldMsg.messageOwner.id);
                                        MessagesController.this.dialogs_dict.put(Long.valueOf(key), value);
                                        MessagesController.this.dialogMessage.put(value.top_message, new_dialogMessage.get(Integer.valueOf(value.top_message)));
                                    }
                                } else if ((oldMsg != null && oldMsg.deleted) || value.top_message > currentDialog.top_message) {
                                    if (oldMsg != null) {
                                        MessagesController.this.dialogMessage.remove(oldMsg.messageOwner.id);
                                    }
                                    MessagesController.this.dialogs_dict.put(Long.valueOf(key), value);
                                    MessagesController.this.dialogMessage.put(value.top_message, new_dialogMessage.get(Integer.valueOf(value.top_message)));
                                }
                            }
                        }
                        MessagesController.this.dialogs.clear();
                        MessagesController.this.dialogsServerOnly.clear();
                        MessagesController.this.dialogs.addAll(MessagesController.this.dialogs_dict.values());
                        Collections.sort(MessagesController.this.dialogs, new C03681());
                        i$ = MessagesController.this.dialogs.iterator();
                        while (i$.hasNext()) {
                            TL_dialog d = (TL_dialog) i$.next();
                            if (((int) d.id) != 0) {
                                MessagesController.this.dialogsServerOnly.add(d);
                            }
                        }
                        MessagesController messagesController = MessagesController.this;
                        boolean z = (org_telegram_TL_TLRPC_messages_Dialogs.dialogs.size() == 0 || org_telegram_TL_TLRPC_messages_Dialogs.dialogs.size() != i3) && !z;
                        messagesController.dialogsEndReached = z;
                        NotificationCenter.Instance.postNotificationName(4, new Object[0]);
                    }
                });
            }
        });
    }

    public TL_photo generatePhotoSizes(String path) {
        long time = System.currentTimeMillis();
        Bitmap bitmap = FileLoader.loadBitmap(path, 800.0f, 800.0f);
        ArrayList<PhotoSize> sizes = new ArrayList();
        PhotoSize size = FileLoader.scaleAndSaveImage(bitmap, 90.0f, 90.0f, 55, true);
        if (size != null) {
            size.type = "s";
            sizes.add(size);
        }
        size = FileLoader.scaleAndSaveImage(bitmap, 320.0f, 320.0f, 87, false);
        if (size != null) {
            size.type = "m";
            sizes.add(size);
        }
        size = FileLoader.scaleAndSaveImage(bitmap, 800.0f, 800.0f, 87, false);
        if (size != null) {
            size.type = "x";
            sizes.add(size);
        }
        if (VERSION.SDK_INT < 11 && bitmap != null) {
            bitmap.recycle();
        }
        if (sizes.isEmpty()) {
            return null;
        }
        UserConfig.saveConfig(false);
        TL_photo photo = new TL_photo();
        photo.user_id = UserConfig.clientUserId;
        photo.date = ConnectionsManager.Instance.getCurrentTime();
        photo.sizes = sizes;
        photo.caption = BuildConfig.FLAVOR;
        photo.geo = new TL_geoPointEmpty();
        return photo;
    }

    public void markDialogAsRead(long dialog_id, int max_id, int max_positive_id, int offset, int max_date, boolean was) {
        int lower_part = (int) dialog_id;
        if (lower_part != 0) {
            if (max_id != 0 || offset != 0) {
                TLObject req = new TL_messages_readHistory();
                if (lower_part < 0) {
                    req.peer = new TL_inputPeerChat();
                    req.peer.chat_id = -lower_part;
                } else {
                    User user = (User) this.users.get(Integer.valueOf(lower_part));
                    if ((user instanceof TL_userForeign) || (user instanceof TL_userRequest)) {
                        req.peer = new TL_inputPeerForeign();
                        req.peer.user_id = user.id;
                        req.peer.access_hash = user.access_hash;
                    } else {
                        req.peer = new TL_inputPeerContact();
                        req.peer.user_id = user.id;
                    }
                }
                req.max_id = max_positive_id;
                req.offset = offset;
                if (offset == 0) {
                    MessagesStorage.Instance.processPendingRead(dialog_id, max_positive_id, max_date, false);
                }
                if (req.max_id != Integer.MAX_VALUE) {
                    ConnectionsManager connectionsManager = ConnectionsManager.Instance;
                    final long j = dialog_id;
                    final int i = max_positive_id;
                    final int i2 = max_date;
                    final boolean z = was;
                    AnonymousClass31 anonymousClass31 = new RPCRequestDelegate() {
                        public void run(TLObject response, TL_error error) {
                            if (error == null) {
                                MessagesStorage.Instance.processPendingRead(j, i, i2, true);
                                TL_messages_affectedHistory res = (TL_messages_affectedHistory) response;
                                if (res.offset > 0) {
                                    MessagesController.this.markDialogAsRead(j, 0, i, res.offset, i2, z);
                                }
                                if (MessagesStorage.lastSeqValue + 1 == res.seq) {
                                    MessagesStorage.lastSeqValue = res.seq;
                                    MessagesStorage.lastPtsValue = res.pts;
                                    MessagesStorage.Instance.saveDiffParams(MessagesStorage.lastSeqValue, MessagesStorage.lastPtsValue, MessagesStorage.lastDateValue, MessagesStorage.lastQtsValue);
                                } else if (MessagesStorage.lastSeqValue != res.seq) {
                                    FileLog.m800e("tmessages", "need get diff TL_messages_readHistory, seq: " + MessagesStorage.lastSeqValue + " " + res.seq);
                                    if (MessagesController.this.gettingDifference || MessagesController.this.updatesStartWaitTime == 0 || (MessagesController.this.updatesStartWaitTime != 0 && MessagesController.this.updatesStartWaitTime + 1500 > System.currentTimeMillis())) {
                                        if (MessagesController.this.updatesStartWaitTime == 0) {
                                            MessagesController.this.updatesStartWaitTime = System.currentTimeMillis();
                                        }
                                        FileLog.m800e("tmessages", "add TL_messages_readHistory to queue");
                                        UserActionUpdates updates = new UserActionUpdates();
                                        updates.seq = res.seq;
                                        MessagesController.this.updatesQueue.add(updates);
                                        return;
                                    }
                                    MessagesController.this.getDifference();
                                }
                            }
                        }
                    };
                    connectionsManager.performRpc(req, anonymousClass31, null, true, RPCRequest.RPCRequestClassGeneric);
                }
                final int i3 = offset;
                final long j2 = dialog_id;
                MessagesStorage.Instance.storageQueue.postRunnable(new Runnable() {

                    class C03701 implements Runnable {
                        C03701() {
                        }

                        public void run() {
                            if (i3 == 0) {
                                TL_dialog dialog = (TL_dialog) MessagesController.this.dialogs_dict.get(Long.valueOf(j2));
                                if (dialog != null) {
                                    dialog.unread_count = 0;
                                    NotificationCenter.Instance.postNotificationName(4, new Object[0]);
                                }
                            }
                        }
                    }

                    public void run() {
                        Utilities.RunOnUIThread(new C03701());
                    }
                });
                if (offset == 0) {
                    TL_messages_receivedMessages req2 = new TL_messages_receivedMessages();
                    req2.max_id = max_positive_id;
                    ConnectionsManager.Instance.performRpc(req2, new RPCRequestDelegate() {
                        public void run(TLObject response, TL_error error) {
                        }
                    }, null, true, RPCRequest.RPCRequestClassGeneric | RPCRequest.RPCRequestClassFailOnServerErrors);
                }
            }
        } else if (max_date != 0) {
            EncryptedChat chat = (EncryptedChat) this.encryptedChats.get(Integer.valueOf((int) (dialog_id >> 32)));
            if (chat.auth_key != null && chat.auth_key.length > 1 && (chat instanceof TL_encryptedChat)) {
                TL_messages_readEncryptedHistory req3 = new TL_messages_readEncryptedHistory();
                req3.peer = new TL_inputEncryptedChat();
                req3.peer.chat_id = chat.id;
                req3.peer.access_hash = chat.access_hash;
                req3.max_date = max_date;
                ConnectionsManager.Instance.performRpc(req3, new RPCRequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                    }
                }, null, true, RPCRequest.RPCRequestClassGeneric);
            }
            MessagesStorage.Instance.processPendingRead(dialog_id, max_id, max_date, false);
            final long j3 = dialog_id;
            MessagesStorage.Instance.storageQueue.postRunnable(new Runnable() {

                class C03711 implements Runnable {
                    C03711() {
                    }

                    public void run() {
                        TL_dialog dialog = (TL_dialog) MessagesController.this.dialogs_dict.get(Long.valueOf(j3));
                        if (dialog != null) {
                            dialog.unread_count = 0;
                            NotificationCenter.Instance.postNotificationName(4, new Object[0]);
                        }
                    }
                }

                public void run() {
                    Utilities.RunOnUIThread(new C03711());
                }
            });
            if (chat.ttl > 0 && was) {
                int serverTime = Math.max(ConnectionsManager.Instance.getCurrentTime(), max_date);
                MessagesStorage.Instance.createTaskForDate(chat.id, serverTime, serverTime, 0);
            }
        }
    }

    public void cancelSendingMessage(MessageObject object) {
        String keyToRemvoe = null;
        for (Entry<String, DelayedMessage> entry : this.delayedMessages.entrySet()) {
            if (((DelayedMessage) entry.getValue()).obj.messageOwner.id == object.messageOwner.id) {
                keyToRemvoe = (String) entry.getKey();
                break;
            }
        }
        if (keyToRemvoe != null) {
            ArrayList<Integer> messages = new ArrayList();
            messages.add(Integer.valueOf(object.messageOwner.id));
            FileLoader.Instance.cancelUploadFile(keyToRemvoe);
            deleteMessages(messages);
        }
    }

    private long getNextRandomId() {
        long val = 0;
        while (val == 0) {
            val = random.nextLong();
        }
        return val;
    }

    public void sendMessage(User user, long peer) {
        sendMessage(null, 0.0d, 0.0d, null, null, null, null, user, null, peer);
    }

    public void sendMessage(MessageObject message, long peer) {
        sendMessage(null, 0.0d, 0.0d, null, null, message, null, null, null, peer);
    }

    public void sendMessage(TL_document document, long peer) {
        sendMessage(null, 0.0d, 0.0d, null, null, null, null, null, document, peer);
    }

    public void sendMessage(String message, long peer) {
        sendMessage(message, 0.0d, 0.0d, null, null, null, null, null, null, peer);
    }

    public void sendMessage(FileLocation location, long peer) {
        sendMessage(null, 0.0d, 0.0d, null, null, null, location, null, null, peer);
    }

    public void sendMessage(double lat, double lon, long peer) {
        sendMessage(null, lat, lon, null, null, null, null, null, null, peer);
    }

    public void sendMessage(TL_photo photo, long peer) {
        sendMessage(null, 0.0d, 0.0d, photo, null, null, null, null, null, peer);
    }

    public void sendMessage(TL_video video, long peer) {
        sendMessage(null, 0.0d, 0.0d, null, video, null, null, null, null, peer);
    }

    public void sendTTLMessage(EncryptedChat encryptedChat) {
        TL_messageService newMsg = new TL_messageService();
        newMsg.action = new TL_messageActionTTLChange();
        newMsg.action.ttl = encryptedChat.ttl;
        int newMessageId = UserConfig.getNewMessageId();
        newMsg.id = newMessageId;
        newMsg.local_id = newMessageId;
        newMsg.from_id = UserConfig.clientUserId;
        newMsg.unread = true;
        newMsg.dialog_id = ((long) encryptedChat.id) << 32;
        newMsg.to_id = new TL_peerUser();
        if (encryptedChat.participant_id == UserConfig.clientUserId) {
            newMsg.to_id.user_id = encryptedChat.admin_id;
        } else {
            newMsg.to_id.user_id = encryptedChat.participant_id;
        }
        newMsg.out = true;
        newMsg.date = ConnectionsManager.Instance.getCurrentTime();
        newMsg.random_id = getNextRandomId();
        UserConfig.saveConfig(false);
        MessageObject newMsgObj = new MessageObject(newMsg, this.users);
        newMsgObj.messageOwner.send_state = 1;
        ArrayList<MessageObject> objArr = new ArrayList();
        objArr.add(newMsgObj);
        ArrayList<Message> arr = new ArrayList();
        arr.add(newMsg);
        MessagesStorage.Instance.putMessages(arr, false, true);
        updateInterfaceWithMessages(newMsg.dialog_id, objArr);
        NotificationCenter.Instance.postNotificationName(4, new Object[0]);
        this.sendingMessages.put(newMsg.id, newMsgObj);
        TL_decryptedMessageService reqSend = new TL_decryptedMessageService();
        reqSend.random_id = newMsg.random_id;
        reqSend.random_bytes = new byte[Math.max(1, (int) Math.ceil(random.nextDouble() * 16.0d))];
        random.nextBytes(reqSend.random_bytes);
        reqSend.action = new TL_decryptedMessageActionSetMessageTTL();
        reqSend.action.ttl_seconds = encryptedChat.ttl;
        performSendEncryptedRequest(reqSend, newMsgObj, encryptedChat, null);
    }

    private void sendMessage(String message, double lat, double lon, TL_photo photo, TL_video video, MessageObject msgObj, FileLocation location, User user, TL_document document, long peer) {
        Message newMsg = null;
        int type = -1;
        if (message != null) {
            newMsg = new TL_message();
            newMsg.media = new TL_messageMediaEmpty();
            type = 0;
            newMsg.message = message;
        } else if (lat != 0.0d && lon != 0.0d) {
            newMsg = new TL_message();
            newMsg.media = new TL_messageMediaGeo();
            newMsg.media.geo = new TL_geoPoint();
            newMsg.media.geo.lat = lat;
            newMsg.media.geo._long = lon;
            newMsg.message = BuildConfig.FLAVOR;
            type = 1;
        } else if (photo != null) {
            newMsg = new TL_message();
            newMsg.media = new TL_messageMediaPhoto();
            newMsg.media.photo = photo;
            type = 2;
            newMsg.message = "-1";
            FileLocation location1 = ((PhotoSize) photo.sizes.get(photo.sizes.size() - 1)).location;
            newMsg.attachPath = Utilities.getCacheDir() + "/" + location1.volume_id + "_" + location1.local_id + ".jpg";
        } else if (video != null) {
            newMsg = new TL_message();
            newMsg.media = new TL_messageMediaVideo();
            newMsg.media.video = video;
            type = 3;
            newMsg.message = "-1";
            newMsg.attachPath = video.path;
        } else if (msgObj != null) {
            newMsg = new TL_messageForwarded();
            if (msgObj.messageOwner instanceof TL_messageForwarded) {
                newMsg.fwd_from_id = msgObj.messageOwner.fwd_from_id;
                newMsg.fwd_date = msgObj.messageOwner.fwd_date;
                newMsg.media = msgObj.messageOwner.media;
                newMsg.message = msgObj.messageOwner.message;
                newMsg.fwd_msg_id = msgObj.messageOwner.id;
                type = 4;
            } else if (msgObj.type == 11) {
                newMsg.fwd_from_id = msgObj.messageOwner.from_id;
                newMsg.fwd_date = msgObj.messageOwner.date;
                newMsg.media = new TL_messageMediaPhoto();
                newMsg.media.photo = msgObj.messageOwner.action.photo;
                newMsg.message = BuildConfig.FLAVOR;
                newMsg.fwd_msg_id = msgObj.messageOwner.id;
                type = 5;
            } else {
                newMsg.fwd_from_id = msgObj.messageOwner.from_id;
                newMsg.fwd_date = msgObj.messageOwner.date;
                newMsg.media = msgObj.messageOwner.media;
                newMsg.message = msgObj.messageOwner.message;
                newMsg.fwd_msg_id = msgObj.messageOwner.id;
                type = 4;
            }
        } else if (location == null) {
            if (user != null) {
                newMsg = new TL_message();
                newMsg.media = new TL_messageMediaContact();
                newMsg.media.phone_number = user.phone;
                newMsg.media.first_name = user.first_name;
                newMsg.media.last_name = user.last_name;
                newMsg.media.user_id = user.id;
                newMsg.message = BuildConfig.FLAVOR;
                type = 6;
            } else if (document != null) {
                newMsg = new TL_message();
                newMsg.media = new TL_messageMediaDocument();
                newMsg.media.document = document;
                type = 7;
                newMsg.message = "-1";
                newMsg.attachPath = document.path;
            }
        }
        if (newMsg != null) {
            int newMessageId = UserConfig.getNewMessageId();
            newMsg.id = newMessageId;
            newMsg.local_id = newMessageId;
            newMsg.from_id = UserConfig.clientUserId;
            newMsg.unread = true;
            newMsg.dialog_id = peer;
            int lower_id = (int) peer;
            EncryptedChat encryptedChat = null;
            InputPeer sendToPeer = null;
            if (lower_id == 0) {
                encryptedChat = (EncryptedChat) this.encryptedChats.get(Integer.valueOf((int) (peer >> 32)));
                newMsg.to_id = new TL_peerUser();
                if (encryptedChat.participant_id == UserConfig.clientUserId) {
                    newMsg.to_id.user_id = encryptedChat.admin_id;
                } else {
                    newMsg.to_id.user_id = encryptedChat.participant_id;
                }
                newMsg.ttl = encryptedChat.ttl;
            } else if (lower_id < 0) {
                newMsg.to_id = new TL_peerChat();
                newMsg.to_id.chat_id = -lower_id;
                sendToPeer = new TL_inputPeerChat();
                sendToPeer.chat_id = -lower_id;
            } else {
                newMsg.to_id = new TL_peerUser();
                newMsg.to_id.user_id = lower_id;
                User sendToUser = (User) this.users.get(Integer.valueOf(lower_id));
                if (sendToUser == null) {
                    return;
                }
                if ((sendToUser instanceof TL_userForeign) || (sendToUser instanceof TL_userRequest)) {
                    sendToPeer = new TL_inputPeerForeign();
                    sendToPeer.user_id = sendToUser.id;
                    sendToPeer.access_hash = sendToUser.access_hash;
                } else {
                    sendToPeer = new TL_inputPeerContact();
                    sendToPeer.user_id = sendToUser.id;
                }
            }
            newMsg.out = true;
            newMsg.date = ConnectionsManager.Instance.getCurrentTime();
            newMsg.random_id = getNextRandomId();
            UserConfig.saveConfig(false);
            MessageObject newMsgObj = new MessageObject(newMsg, null);
            newMsgObj.messageOwner.send_state = 1;
            ArrayList<MessageObject> objArr = new ArrayList();
            objArr.add(newMsgObj);
            ArrayList<Message> arr = new ArrayList();
            arr.add(newMsg);
            MessagesStorage.Instance.putMessages(arr, false, true);
            updateInterfaceWithMessages(peer, objArr);
            NotificationCenter.Instance.postNotificationName(4, new Object[0]);
            this.sendingMessages.put(newMsg.id, newMsgObj);
            TL_decryptedMessage reqSend;
            if (type == 0) {
                if (encryptedChat == null) {
                    TL_messages_sendMessage reqSend2 = new TL_messages_sendMessage();
                    reqSend2.message = message;
                    reqSend2.peer = sendToPeer;
                    reqSend2.random_id = newMsg.random_id;
                    performSendMessageRequest(reqSend2, newMsgObj);
                    return;
                }
                reqSend = new TL_decryptedMessage();
                reqSend.random_id = newMsg.random_id;
                reqSend.random_bytes = new byte[Math.max(1, (int) Math.ceil(random.nextDouble() * 16.0d))];
                random.nextBytes(reqSend.random_bytes);
                reqSend.message = message;
                reqSend.media = new TL_decryptedMessageMediaEmpty();
                performSendEncryptedRequest(reqSend, newMsgObj, encryptedChat, null);
            } else if (type == 1 || type == 2 || type == 3 || type == 5 || type == 6 || type == 7) {
                DelayedMessage delayedMessage;
                if (encryptedChat == null) {
                    TL_messages_sendMedia reqSend3 = new TL_messages_sendMedia();
                    reqSend3.peer = sendToPeer;
                    reqSend3.random_id = newMsg.random_id;
                    if (type == 1) {
                        reqSend3.media = new TL_inputMediaGeoPoint();
                        reqSend3.media.geo_point = new TL_inputGeoPoint();
                        reqSend3.media.geo_point.lat = lat;
                        reqSend3.media.geo_point._long = lon;
                        performSendMessageRequest(reqSend3, newMsgObj);
                        return;
                    } else if (type == 2) {
                        reqSend3.media = new TL_inputMediaUploadedPhoto();
                        delayedMessage = new DelayedMessage();
                        delayedMessage.sendRequest = reqSend3;
                        delayedMessage.type = 0;
                        delayedMessage.obj = newMsgObj;
                        delayedMessage.location = ((PhotoSize) photo.sizes.get(photo.sizes.size() - 1)).location;
                        performSendDelayedMessage(delayedMessage);
                        return;
                    } else if (type == 3) {
                        reqSend3.media = new TL_inputMediaUploadedThumbVideo();
                        reqSend3.media.duration = video.duration;
                        reqSend3.media.f58w = video.w;
                        reqSend3.media.f57h = video.h;
                        delayedMessage = new DelayedMessage();
                        delayedMessage.sendRequest = reqSend3;
                        delayedMessage.type = 1;
                        delayedMessage.obj = newMsgObj;
                        delayedMessage.location = video.thumb.location;
                        delayedMessage.videoLocation = video;
                        performSendDelayedMessage(delayedMessage);
                        return;
                    } else if (type == 5) {
                        reqSend3.media = new TL_inputMediaPhoto();
                        TL_inputPhoto ph = new TL_inputPhoto();
                        ph.id = msgObj.messageOwner.action.photo.id;
                        ph.access_hash = msgObj.messageOwner.action.photo.access_hash;
                        ((TL_inputMediaPhoto) reqSend3.media).id = ph;
                        performSendMessageRequest(reqSend3, newMsgObj);
                        return;
                    } else if (type == 6) {
                        reqSend3.media = new TL_inputMediaContact();
                        reqSend3.media.phone_number = user.phone;
                        reqSend3.media.first_name = user.first_name;
                        reqSend3.media.last_name = user.last_name;
                        performSendMessageRequest(reqSend3, newMsgObj);
                        return;
                    } else if (type == 7) {
                        reqSend3.media = new TL_inputMediaUploadedDocument();
                        reqSend3.media.mime_type = document.mime_type;
                        reqSend3.media.file_name = document.file_name;
                        delayedMessage = new DelayedMessage();
                        delayedMessage.sendRequest = reqSend3;
                        delayedMessage.type = 2;
                        delayedMessage.obj = newMsgObj;
                        delayedMessage.documentLocation = document;
                        performSendDelayedMessage(delayedMessage);
                        return;
                    } else {
                        return;
                    }
                }
                reqSend = new TL_decryptedMessage();
                reqSend.random_id = newMsg.random_id;
                reqSend.random_bytes = new byte[Math.max(1, (int) Math.ceil(random.nextDouble() * 16.0d))];
                random.nextBytes(reqSend.random_bytes);
                reqSend.message = BuildConfig.FLAVOR;
                if (type == 1) {
                    reqSend.media = new TL_decryptedMessageMediaGeoPoint();
                    reqSend.media.lat = lat;
                    reqSend.media._long = lon;
                    performSendEncryptedRequest(reqSend, newMsgObj, encryptedChat, null);
                } else if (type == 2) {
                    reqSend.media = new TL_decryptedMessageMediaPhoto();
                    reqSend.media.iv = new byte[32];
                    reqSend.media.key = new byte[32];
                    random.nextBytes(reqSend.media.iv);
                    random.nextBytes(reqSend.media.key);
                    PhotoSize small = (PhotoSize) photo.sizes.get(0);
                    PhotoSize big = (PhotoSize) photo.sizes.get(photo.sizes.size() - 1);
                    reqSend.media.thumb = small.bytes;
                    reqSend.media.thumb_h = small.f59h;
                    reqSend.media.thumb_w = small.f60w;
                    reqSend.media.f56w = big.f60w;
                    reqSend.media.f55h = big.f59h;
                    reqSend.media.size = big.size;
                    delayedMessage = new DelayedMessage();
                    delayedMessage.sendEncryptedRequest = reqSend;
                    delayedMessage.type = 0;
                    delayedMessage.obj = newMsgObj;
                    delayedMessage.encryptedChat = encryptedChat;
                    delayedMessage.location = ((PhotoSize) photo.sizes.get(photo.sizes.size() - 1)).location;
                    performSendDelayedMessage(delayedMessage);
                } else if (type == 3) {
                    reqSend.media = new TL_decryptedMessageMediaVideo();
                    reqSend.media.iv = new byte[32];
                    reqSend.media.key = new byte[32];
                    random.nextBytes(reqSend.media.iv);
                    random.nextBytes(reqSend.media.key);
                    reqSend.media.duration = video.duration;
                    reqSend.media.size = video.size;
                    reqSend.media.f56w = video.w;
                    reqSend.media.f55h = video.h;
                    reqSend.media.thumb = video.thumb.bytes;
                    reqSend.media.thumb_h = video.thumb.f59h;
                    reqSend.media.thumb_w = video.thumb.f60w;
                    delayedMessage = new DelayedMessage();
                    delayedMessage.sendEncryptedRequest = reqSend;
                    delayedMessage.type = 1;
                    delayedMessage.obj = newMsgObj;
                    delayedMessage.encryptedChat = encryptedChat;
                    delayedMessage.videoLocation = video;
                    performSendDelayedMessage(delayedMessage);
                } else if (type == 5) {
                } else {
                    if (type == 6) {
                        reqSend.media = new TL_decryptedMessageMediaContact();
                        reqSend.media.phone_number = user.phone;
                        reqSend.media.first_name = user.first_name;
                        reqSend.media.last_name = user.last_name;
                        reqSend.media.user_id = user.id;
                        performSendEncryptedRequest(reqSend, newMsgObj, encryptedChat, null);
                    } else if (type == 7) {
                        reqSend.media = new TL_decryptedMessageMediaDocument();
                        reqSend.media.iv = new byte[32];
                        reqSend.media.key = new byte[32];
                        random.nextBytes(reqSend.media.iv);
                        random.nextBytes(reqSend.media.key);
                        reqSend.media.size = document.size;
                        reqSend.media.thumb = new byte[0];
                        reqSend.media.thumb_h = 0;
                        reqSend.media.thumb_w = 0;
                        reqSend.media.file_name = document.file_name;
                        reqSend.media.mime_type = document.mime_type;
                        delayedMessage = new DelayedMessage();
                        delayedMessage.sendEncryptedRequest = reqSend;
                        delayedMessage.type = 2;
                        delayedMessage.obj = newMsgObj;
                        delayedMessage.encryptedChat = encryptedChat;
                        delayedMessage.documentLocation = document;
                        performSendDelayedMessage(delayedMessage);
                    }
                }
            } else if (type == 4) {
                TL_messages_forwardMessage reqSend4 = new TL_messages_forwardMessage();
                reqSend4.peer = sendToPeer;
                reqSend4.random_id = newMsg.random_id;
                if (msgObj.messageOwner.id >= 0) {
                    reqSend4.id = msgObj.messageOwner.id;
                } else {
                    reqSend4.id = msgObj.messageOwner.fwd_msg_id;
                }
                performSendMessageRequest(reqSend4, newMsgObj);
            }
        }
    }

    private void processSendedMessage(Message newMsg, Message sendedMessage, EncryptedFile file, DecryptedMessage decryptedMessage) {
        PhotoSize size;
        String fileName;
        String fileName2;
        boolean result;
        if (sendedMessage != null) {
            PhotoSize size2;
            if ((sendedMessage.media instanceof TL_messageMediaPhoto) && sendedMessage.media.photo != null && (newMsg.media instanceof TL_messageMediaPhoto) && newMsg.media.photo != null) {
                Iterator it = sendedMessage.media.photo.sizes.iterator();
                while (it.hasNext()) {
                    size = (PhotoSize) it.next();
                    if (!(size instanceof TL_photoSizeEmpty)) {
                        Iterator i$ = newMsg.media.photo.sizes.iterator();
                        while (i$.hasNext()) {
                            size2 = (PhotoSize) i$.next();
                            if (size.type.equals(size2.type)) {
                                fileName = size2.location.volume_id + "_" + size2.location.local_id;
                                fileName2 = size.location.volume_id + "_" + size.location.local_id;
                                if (!fileName.equals(fileName2)) {
                                    new File(Utilities.getCacheDir(), fileName + ".jpg").renameTo(new File(Utilities.getCacheDir(), fileName2 + ".jpg"));
                                    FileLoader.Instance.replaceImageInCache(fileName, fileName2);
                                    size2.location = size.location;
                                }
                            }
                        }
                    }
                }
                sendedMessage.message = newMsg.message;
                sendedMessage.attachPath = newMsg.attachPath;
            } else if ((sendedMessage.media instanceof TL_messageMediaVideo) && sendedMessage.media.video != null && (newMsg.media instanceof TL_messageMediaVideo) && newMsg.media.video != null) {
                size2 = newMsg.media.video.thumb;
                size = sendedMessage.media.video.thumb;
                if (size2.location != null && size.location != null && !(size instanceof TL_photoSizeEmpty) && !(size2 instanceof TL_photoSizeEmpty)) {
                    fileName = size2.location.volume_id + "_" + size2.location.local_id;
                    fileName2 = size.location.volume_id + "_" + size.location.local_id;
                    if (!fileName.equals(fileName2)) {
                        result = new File(Utilities.getCacheDir(), fileName + ".jpg").renameTo(new File(Utilities.getCacheDir(), fileName2 + ".jpg"));
                        FileLoader.Instance.replaceImageInCache(fileName, fileName2);
                        size2.location = size.location;
                        sendedMessage.message = newMsg.message;
                        sendedMessage.attachPath = newMsg.attachPath;
                    }
                }
            } else if ((sendedMessage.media instanceof TL_messageMediaDocument) && sendedMessage.media.document != null && (newMsg.media instanceof TL_messageMediaDocument) && newMsg.media.document != null) {
                sendedMessage.message = newMsg.message;
                sendedMessage.attachPath = newMsg.attachPath;
            }
        } else if (file == null) {
        } else {
            ArrayList<Message> arr;
            if ((newMsg.media instanceof TL_messageMediaPhoto) && newMsg.media.photo != null) {
                size = (PhotoSize) newMsg.media.photo.sizes.get(newMsg.media.photo.sizes.size() - 1);
                fileName = size.location.volume_id + "_" + size.location.local_id;
                size.location = new TL_fileEncryptedLocation();
                size.location.key = decryptedMessage.media.key;
                size.location.iv = decryptedMessage.media.iv;
                size.location.dc_id = file.dc_id;
                size.location.volume_id = file.id;
                size.location.secret = file.access_hash;
                size.location.local_id = file.key_fingerprint;
                fileName2 = size.location.volume_id + "_" + size.location.local_id;
                result = new File(Utilities.getCacheDir(), fileName + ".jpg").renameTo(new File(Utilities.getCacheDir(), fileName2 + ".jpg"));
                FileLoader.Instance.replaceImageInCache(fileName, fileName2);
                arr = new ArrayList();
                arr.add(newMsg);
                MessagesStorage.Instance.putMessages(arr, false, true);
            } else if ((newMsg.media instanceof TL_messageMediaVideo) && newMsg.media.video != null) {
                Video video = newMsg.media.video;
                newMsg.media.video = new TL_videoEncrypted();
                newMsg.media.video.duration = video.duration;
                newMsg.media.video.thumb = video.thumb;
                newMsg.media.video.id = video.id;
                newMsg.media.video.dc_id = file.dc_id;
                newMsg.media.video.f70w = video.f70w;
                newMsg.media.video.f69h = video.f69h;
                newMsg.media.video.date = video.date;
                newMsg.media.video.caption = BuildConfig.FLAVOR;
                newMsg.media.video.user_id = video.user_id;
                newMsg.media.video.size = file.size;
                newMsg.media.video.id = file.id;
                newMsg.media.video.access_hash = file.access_hash;
                newMsg.media.video.key = decryptedMessage.media.key;
                newMsg.media.video.iv = decryptedMessage.media.iv;
                newMsg.media.video.path = video.path;
                arr = new ArrayList();
                arr.add(newMsg);
                MessagesStorage.Instance.putMessages(arr, false, true);
            } else if ((newMsg.media instanceof TL_messageMediaDocument) && newMsg.media.document != null) {
                Document document = newMsg.media.document;
                newMsg.media.document = new TL_documentEncrypted();
                newMsg.media.document.id = file.id;
                newMsg.media.document.access_hash = file.access_hash;
                newMsg.media.document.user_id = document.user_id;
                newMsg.media.document.date = document.date;
                newMsg.media.document.file_name = document.file_name;
                newMsg.media.document.mime_type = document.mime_type;
                newMsg.media.document.size = file.size;
                newMsg.media.document.key = decryptedMessage.media.key;
                newMsg.media.document.iv = decryptedMessage.media.iv;
                newMsg.media.document.path = document.path;
                newMsg.media.document.thumb = document.thumb;
                newMsg.media.document.dc_id = file.dc_id;
                arr = new ArrayList();
                arr.add(newMsg);
                MessagesStorage.Instance.putMessages(arr, false, true);
            }
        }
    }

    private void performSendEncryptedRequest(DecryptedMessage req, MessageObject newMsgObj, EncryptedChat chat, InputEncryptedFile encryptedFile) {
        if (req != null) {
            TLObject reqToSend;
            SerializedData data = new SerializedData();
            req.serializeToStream(data);
            SerializedData toEncrypt = new SerializedData();
            toEncrypt.writeInt32(data.length());
            toEncrypt.writeRaw(data.toByteArray());
            byte[] innerData = toEncrypt.toByteArray();
            Object messageKeyFull = Utilities.computeSHA1(innerData);
            byte[] messageKey = new byte[16];
            System.arraycopy(messageKeyFull, messageKeyFull.length - 16, messageKey, 0, 16);
            MessageKeyData keyData = Utilities.generateMessageKeyData(chat.auth_key, messageKey, false);
            SerializedData dataForEncryption = new SerializedData();
            dataForEncryption.writeRaw(innerData);
            byte[] b = new byte[1];
            while (dataForEncryption.length() % 16 != 0) {
                random.nextBytes(b);
                dataForEncryption.writeByte(b[0]);
            }
            byte[] encryptedData = Utilities.aesIgeEncryption(dataForEncryption.toByteArray(), keyData.aesKey, keyData.aesIv, true, false);
            data = new SerializedData();
            data.writeInt64(chat.key_fingerprint);
            data.writeRaw(messageKey);
            data.writeRaw(encryptedData);
            TLObject req2;
            if (encryptedFile == null) {
                req2 = new TL_messages_sendEncrypted();
                req2.data = data.toByteArray();
                req2.random_id = req.random_id;
                req2.peer = new TL_inputEncryptedChat();
                req2.peer.chat_id = chat.id;
                req2.peer.access_hash = chat.access_hash;
                reqToSend = req2;
            } else {
                req2 = new TL_messages_sendEncryptedFile();
                req2.data = data.toByteArray();
                req2.random_id = req.random_id;
                req2.peer = new TL_inputEncryptedChat();
                req2.peer.chat_id = chat.id;
                req2.peer.access_hash = chat.access_hash;
                req2.file = encryptedFile;
                reqToSend = req2;
            }
            final MessageObject messageObject = newMsgObj;
            final DecryptedMessage decryptedMessage = req;
            ConnectionsManager.Instance.performRpc(reqToSend, new RPCRequestDelegate() {

                class C03721 implements Runnable {
                    C03721() {
                    }

                    public void run() {
                        messageObject.messageOwner.send_state = 0;
                        NotificationCenter.Instance.postNotificationName(10, Integer.valueOf(messageObject.messageOwner.id), Integer.valueOf(messageObject.messageOwner.id));
                        MessagesController.this.sendingMessages.remove(messageObject.messageOwner.id);
                    }
                }

                class C03732 implements Runnable {
                    C03732() {
                    }

                    public void run() {
                        MessagesController.this.sendingMessages.remove(messageObject.messageOwner.id);
                        messageObject.messageOwner.send_state = 2;
                        NotificationCenter.Instance.postNotificationName(11, Integer.valueOf(messageObject.messageOwner.id));
                    }
                }

                public void run(TLObject response, TL_error error) {
                    if (error == null) {
                        messages_SentEncryptedMessage res = (messages_SentEncryptedMessage) response;
                        messageObject.messageOwner.date = res.date;
                        if (res.file instanceof TL_encryptedFile) {
                            MessagesController.this.processSendedMessage(messageObject.messageOwner, null, res.file, decryptedMessage);
                        }
                        MessagesStorage.Instance.updateMessageStateAndId(messageObject.messageOwner.random_id, Integer.valueOf(messageObject.messageOwner.id), messageObject.messageOwner.id, res.date, true);
                        Utilities.RunOnUIThread(new C03721());
                        return;
                    }
                    Utilities.RunOnUIThread(new C03732());
                }
            }, null, true, RPCRequest.RPCRequestClassGeneric);
        }
    }

    private void performSendMessageRequest(TLObject req, final MessageObject newMsgObj) {
        RPCQuickAckDelegate rPCQuickAckDelegate;
        ConnectionsManager connectionsManager = ConnectionsManager.Instance;
        RPCRequestDelegate anonymousClass37 = new RPCRequestDelegate() {

            class C03752 implements Runnable {
                C03752() {
                }

                public void run() {
                    MessagesController.this.sendingMessages.remove(newMsgObj.messageOwner.id);
                    newMsgObj.messageOwner.send_state = 2;
                    NotificationCenter.Instance.postNotificationName(11, Integer.valueOf(newMsgObj.messageOwner.id));
                }
            }

            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    final int oldId = newMsgObj.messageOwner.id;
                    ArrayList<Message> sendedMessages = new ArrayList();
                    UserActionUpdates updates;
                    if (response instanceof TL_messages_sentMessage) {
                        TL_messages_sentMessage res = (TL_messages_sentMessage) response;
                        newMsgObj.messageOwner.id = res.id;
                        if (MessagesStorage.lastSeqValue + 1 == res.seq) {
                            MessagesStorage.lastSeqValue = res.seq;
                            MessagesStorage.lastDateValue = res.date;
                            MessagesStorage.lastPtsValue = res.pts;
                            MessagesStorage.Instance.saveDiffParams(MessagesStorage.lastSeqValue, MessagesStorage.lastPtsValue, MessagesStorage.lastDateValue, MessagesStorage.lastQtsValue);
                        } else if (MessagesStorage.lastSeqValue != res.seq) {
                            FileLog.m800e("tmessages", "need get diff TL_messages_sentMessage, seq: " + MessagesStorage.lastSeqValue + " " + res.seq);
                            if (MessagesController.this.gettingDifference || MessagesController.this.updatesStartWaitTime == 0 || (MessagesController.this.updatesStartWaitTime != 0 && MessagesController.this.updatesStartWaitTime + 1500 > System.currentTimeMillis())) {
                                if (MessagesController.this.updatesStartWaitTime == 0) {
                                    MessagesController.this.updatesStartWaitTime = System.currentTimeMillis();
                                }
                                FileLog.m800e("tmessages", "add TL_messages_sentMessage to queue");
                                updates = new UserActionUpdates();
                                updates.seq = res.seq;
                                MessagesController.this.updatesQueue.add(updates);
                            } else {
                                MessagesController.this.getDifference();
                            }
                        }
                    } else if (response instanceof messages_StatedMessage) {
                        messages_StatedMessage res2 = (messages_StatedMessage) response;
                        sendedMessages.add(res2.message);
                        newMsgObj.messageOwner.id = res2.message.id;
                        MessagesController.this.processSendedMessage(newMsgObj.messageOwner, res2.message, null, null);
                        if (MessagesStorage.lastSeqValue + 1 == res2.seq) {
                            MessagesStorage.lastSeqValue = res2.seq;
                            MessagesStorage.lastPtsValue = res2.pts;
                            MessagesStorage.lastDateValue = res2.message.date;
                            MessagesStorage.Instance.saveDiffParams(MessagesStorage.lastSeqValue, MessagesStorage.lastPtsValue, MessagesStorage.lastDateValue, MessagesStorage.lastQtsValue);
                        } else if (MessagesStorage.lastSeqValue != res2.seq) {
                            FileLog.m800e("tmessages", "need get diff messages_StatedMessage, seq: " + MessagesStorage.lastSeqValue + " " + res2.seq);
                            if (MessagesController.this.gettingDifference || MessagesController.this.updatesStartWaitTime == 0 || (MessagesController.this.updatesStartWaitTime != 0 && MessagesController.this.updatesStartWaitTime + 1500 > System.currentTimeMillis())) {
                                if (MessagesController.this.updatesStartWaitTime == 0) {
                                    MessagesController.this.updatesStartWaitTime = System.currentTimeMillis();
                                }
                                FileLog.m800e("tmessages", "add messages_StatedMessage to queue");
                                updates = new UserActionUpdates();
                                updates.seq = res2.seq;
                                MessagesController.this.updatesQueue.add(updates);
                            } else {
                                MessagesController.this.getDifference();
                            }
                        }
                    } else if (response instanceof messages_StatedMessages) {
                        messages_StatedMessages res3 = (messages_StatedMessages) response;
                        if (!res3.messages.isEmpty()) {
                            Message message = (Message) res3.messages.get(0);
                            newMsgObj.messageOwner.id = message.id;
                            sendedMessages.add(message);
                            MessagesController.this.processSendedMessage(newMsgObj.messageOwner, message, null, null);
                        }
                        if (MessagesStorage.lastSeqValue + 1 == res3.seq) {
                            MessagesStorage.lastSeqValue = res3.seq;
                            MessagesStorage.lastPtsValue = res3.pts;
                            MessagesStorage.Instance.saveDiffParams(MessagesStorage.lastSeqValue, MessagesStorage.lastPtsValue, MessagesStorage.lastDateValue, MessagesStorage.lastQtsValue);
                        } else if (MessagesStorage.lastSeqValue != res3.seq) {
                            FileLog.m800e("tmessages", "need get diff messages_StatedMessages, seq: " + MessagesStorage.lastSeqValue + " " + res3.seq);
                            if (MessagesController.this.gettingDifference || MessagesController.this.updatesStartWaitTime == 0 || (MessagesController.this.updatesStartWaitTime != 0 && MessagesController.this.updatesStartWaitTime + 1500 > System.currentTimeMillis())) {
                                if (MessagesController.this.updatesStartWaitTime == 0) {
                                    MessagesController.this.updatesStartWaitTime = System.currentTimeMillis();
                                }
                                FileLog.m800e("tmessages", "add messages_StatedMessages to queue");
                                updates = new UserActionUpdates();
                                updates.seq = res3.seq;
                                MessagesController.this.updatesQueue.add(updates);
                            } else {
                                MessagesController.this.getDifference();
                            }
                        }
                    }
                    MessagesStorage.Instance.updateMessageStateAndId(newMsgObj.messageOwner.random_id, Integer.valueOf(oldId), newMsgObj.messageOwner.id, 0, true);
                    if (!sendedMessages.isEmpty()) {
                        MessagesStorage.Instance.putMessages(sendedMessages, true, true);
                    }
                    Utilities.RunOnUIThread(new Runnable() {
                        public void run() {
                            newMsgObj.messageOwner.send_state = 0;
                            NotificationCenter.Instance.postNotificationName(10, Integer.valueOf(oldId), Integer.valueOf(newMsgObj.messageOwner.id));
                            MessagesController.this.sendingMessages.remove(oldId);
                        }
                    });
                    return;
                }
                Utilities.RunOnUIThread(new C03752());
            }
        };
        if (req instanceof TL_messages_forwardMessages) {
            rPCQuickAckDelegate = null;
        } else {
            rPCQuickAckDelegate = new RPCQuickAckDelegate() {
                public void quickAck() {
                    final int msg_id = newMsgObj.messageOwner.id;
                    Utilities.RunOnUIThread(new Runnable() {
                        public void run() {
                            newMsgObj.messageOwner.send_state = 0;
                            NotificationCenter.Instance.postNotificationName(9, Integer.valueOf(msg_id));
                        }
                    });
                }
            };
        }
        connectionsManager.performRpc(req, anonymousClass37, null, rPCQuickAckDelegate, true, RPCRequest.RPCRequestClassFailOnServerErrors | RPCRequest.RPCRequestClassGeneric, Integer.MAX_VALUE);
    }

    private void performSendDelayedMessage(final DelayedMessage message) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                String location;
                if (message.type == 0) {
                    location = Utilities.getCacheDir() + "/" + message.location.volume_id + "_" + message.location.local_id + ".jpg";
                    MessagesController.this.delayedMessages.put(location, message);
                    if (message.sendRequest != null) {
                        FileLoader.Instance.uploadFile(location, null, null);
                    } else {
                        FileLoader.Instance.uploadFile(location, message.sendEncryptedRequest.media.key, message.sendEncryptedRequest.media.iv);
                    }
                } else if (message.type == 1) {
                    if (message.sendRequest == null) {
                        location = message.videoLocation.path;
                        if (location == null) {
                            location = Utilities.getCacheDir() + "/" + message.videoLocation.id + ".mp4";
                        }
                        MessagesController.this.delayedMessages.put(location, message);
                        FileLoader.Instance.uploadFile(location, message.sendEncryptedRequest.media.key, message.sendEncryptedRequest.media.iv);
                    } else if (message.sendRequest.media.thumb == null) {
                        location = Utilities.getCacheDir() + "/" + message.location.volume_id + "_" + message.location.local_id + ".jpg";
                        MessagesController.this.delayedMessages.put(location, message);
                        FileLoader.Instance.uploadFile(location, null, null);
                    } else {
                        location = message.videoLocation.path;
                        if (location == null) {
                            location = Utilities.getCacheDir() + "/" + message.videoLocation.id + ".mp4";
                        }
                        MessagesController.this.delayedMessages.put(location, message);
                        FileLoader.Instance.uploadFile(location, null, null);
                    }
                } else if (message.type == 2) {
                    location = message.documentLocation.path;
                    MessagesController.this.delayedMessages.put(location, message);
                    if (message.sendRequest != null) {
                        FileLoader.Instance.uploadFile(location, null, null);
                    } else {
                        FileLoader.Instance.uploadFile(location, message.sendEncryptedRequest.media.key, message.sendEncryptedRequest.media.iv);
                    }
                }
            }
        });
    }

    public void fileDidFailedUpload(final String location) {
        if (this.uploadingAvatar == null || !this.uploadingAvatar.equals(location)) {
            Utilities.RunOnUIThread(new Runnable() {
                public void run() {
                    DelayedMessage obj = (DelayedMessage) MessagesController.this.delayedMessages.get(location);
                    if (obj != null) {
                        obj.obj.messageOwner.send_state = 2;
                        MessagesController.this.sendingMessages.remove(obj.obj.messageOwner.id);
                        NotificationCenter.Instance.postNotificationName(11, Integer.valueOf(obj.obj.messageOwner.id));
                        MessagesController.this.delayedMessages.remove(location);
                    }
                }
            });
        } else {
            this.uploadingAvatar = null;
        }
    }

    public void rebuildContactsWithNewUser(final User user, final long bookId) {
        Utilities.stageQueue.postRunnable(new Runnable() {

            class C03781 implements Comparator<Contact> {
                C03781() {
                }

                public int compare(Contact contact, Contact contact2) {
                    String toComapre1 = contact.first_name;
                    if (toComapre1.length() == 0) {
                        toComapre1 = contact.last_name;
                    }
                    String toComapre2 = contact2.first_name;
                    if (toComapre2.length() == 0) {
                        toComapre2 = contact2.last_name;
                    }
                    return toComapre1.compareTo(toComapre2);
                }
            }

            class C03792 implements Comparator<String> {
                C03792() {
                }

                public int compare(String s, String s2) {
                    char cv1 = s.charAt(0);
                    char cv2 = s2.charAt(0);
                    if (cv1 == '#') {
                        return 1;
                    }
                    if (cv2 == '#') {
                        return -1;
                    }
                    return s.compareTo(s2);
                }
            }

            class C03803 implements Comparator<TL_contact> {
                C03803() {
                }

                public int compare(TL_contact tl_contact, TL_contact tl_contact2) {
                    User user1 = (User) MessagesController.this.users.get(Integer.valueOf(tl_contact.user_id));
                    User user2 = (User) MessagesController.this.users.get(Integer.valueOf(tl_contact2.user_id));
                    String name1 = user1.first_name;
                    if (name1 == null || name1.length() == 0) {
                        name1 = user1.last_name;
                    }
                    String name2 = user2.first_name;
                    if (name2 == null || name2.length() == 0) {
                        name2 = user2.last_name;
                    }
                    return name1.compareTo(name2);
                }
            }

            class C03814 implements Comparator<TL_contact> {
                C03814() {
                }

                public int compare(TL_contact contact, TL_contact contact2) {
                    User user1 = (User) MessagesController.this.users.get(Integer.valueOf(contact.user_id));
                    User user2 = (User) MessagesController.this.users.get(Integer.valueOf(contact2.user_id));
                    String toComapre1 = user1.first_name;
                    if (toComapre1 == null || toComapre1.length() == 0) {
                        toComapre1 = user1.last_name;
                    }
                    String toComapre2 = user2.first_name;
                    if (toComapre2 == null || toComapre2.length() == 0) {
                        toComapre2 = user2.last_name;
                    }
                    return toComapre1.compareTo(toComapre2);
                }
            }

            class C03825 implements Comparator<String> {
                C03825() {
                }

                public int compare(String s, String s2) {
                    char cv1 = s.charAt(0);
                    char cv2 = s2.charAt(0);
                    if (cv1 == '#') {
                        return 1;
                    }
                    if (cv2 == '#') {
                        return -1;
                    }
                    return s.compareTo(s2);
                }
            }

            class C03836 implements Comparator<TL_contact> {
                C03836() {
                }

                public int compare(TL_contact lhs, TL_contact rhs) {
                    return Integer.valueOf(lhs.user_id).compareTo(Integer.valueOf(rhs.user_id));
                }
            }

            public void run() {
                final HashMap<Integer, Contact> contactsMapBook = new HashMap(MessagesController.this.contactsBook);
                final HashMap<String, ArrayList<Contact>> sectionsDictBook = new HashMap();
                final ArrayList<String> sortedSectionsArrayBook = new ArrayList();
                Contact newContactBook = new Contact();
                newContactBook.first_name = user.first_name;
                newContactBook.last_name = user.last_name;
                newContactBook.id = (int) bookId;
                newContactBook.phones = new ArrayList();
                newContactBook.phones.add(user.phone);
                newContactBook.phoneTypes = new ArrayList();
                newContactBook.phoneTypes.add(ApplicationLoader.applicationContext.getString(C0419R.string.PhoneMobile));
                contactsMapBook.put(Integer.valueOf((int) bookId), newContactBook);
                String contactsImportHash = BuildConfig.FLAVOR;
                MessageDigest mdEnc = null;
                try {
                    mdEnc = MessageDigest.getInstance("MD5");
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
                for (Entry<Integer, Contact> pair : contactsMapBook.entrySet()) {
                    Contact value = (Contact) pair.getValue();
                    int id = ((Integer) pair.getKey()).intValue();
                    if (mdEnc != null) {
                        for (int a = 0; a < value.phones.size(); a++) {
                            mdEnc.update((id + value.first_name + value.last_name + ((String) value.phones.get(a))).getBytes());
                        }
                    }
                    String key = value.last_name;
                    if (key.length() == 0) {
                        key = value.first_name;
                    }
                    if (key.length() == 0) {
                        key = "#";
                        if (value.phones.size() != 0) {
                            value.first_name = "+" + ((String) value.phones.get(0));
                        }
                    } else {
                        key = key.toUpperCase();
                    }
                    if (key.length() > 1) {
                        key = key.substring(0, 1);
                    }
                    ArrayList<Contact> arr = (ArrayList) sectionsDictBook.get(key);
                    if (arr == null) {
                        arr = new ArrayList();
                        sectionsDictBook.put(key, arr);
                        sortedSectionsArrayBook.add(key);
                    }
                    arr.add(value);
                }
                for (Entry<String, ArrayList<Contact>> entry : sectionsDictBook.entrySet()) {
                    Collections.sort((List) entry.getValue(), new C03781());
                }
                Collections.sort(sortedSectionsArrayBook, new C03792());
                String importHash = String.format(Locale.US, "%32s", new Object[]{new BigInteger(1, mdEnc.digest()).toString(16)}).replace(' ', '0');
                final ArrayList<TL_contact> contactsArr = new ArrayList(MessagesController.this.contacts);
                TL_contact newContact = new TL_contact();
                newContact.user_id = user.id;
                contactsArr.add(newContact);
                Collections.sort(contactsArr, new C03803());
                final HashMap<String, ArrayList<TL_contact>> sectionsDict = new HashMap();
                final SparseArray<TL_contact> contactsDictionery = new SparseArray();
                final ArrayList<String> sortedSectionsArray = new ArrayList();
                final HashMap<String, TL_contact> contactsPhones = new HashMap();
                Iterator i$ = contactsArr.iterator();
                while (i$.hasNext()) {
                    TL_contact value2 = (TL_contact) i$.next();
                    User user = (User) MessagesController.this.users.get(Integer.valueOf(value2.user_id));
                    contactsDictionery.put(value2.user_id, value2);
                    contactsPhones.put(user.phone, value2);
                    key = user.last_name;
                    if (key == null || key.length() == 0) {
                        key = user.first_name;
                    }
                    if (key.length() == 0) {
                        key = "#";
                    } else {
                        key = key.toUpperCase();
                    }
                    if (key.length() > 1) {
                        key = key.substring(0, 1);
                    }
                    ArrayList<TL_contact> arr2 = (ArrayList) sectionsDict.get(key);
                    if (arr2 == null) {
                        arr2 = new ArrayList();
                        sectionsDict.put(key, arr2);
                        sortedSectionsArray.add(key);
                    }
                    arr2.add(value2);
                }
                for (Entry<String, ArrayList<TL_contact>> entry2 : sectionsDict.entrySet()) {
                    Collections.sort((List) entry2.getValue(), new C03814());
                }
                Collections.sort(sortedSectionsArray, new C03825());
                ArrayList<TL_contact> arrayList = new ArrayList(contactsArr);
                Collections.sort(arrayList, new C03836());
                String ids = BuildConfig.FLAVOR;
                i$ = arrayList.iterator();
                while (i$.hasNext()) {
                    TL_contact aContactsArr = (TL_contact) i$.next();
                    if (ids.length() != 0) {
                        ids = ids + ",";
                    }
                    ids = ids + aContactsArr.user_id;
                }
                UserConfig.contactsHash = Utilities.MD5(ids);
                UserConfig.importHash = importHash;
                UserConfig.saveConfig(false);
                MessagesStorage.Instance.putContacts(contactsArr, true);
                ArrayList<User> users = new ArrayList();
                users.add(user);
                MessagesStorage.Instance.putUsersAndChats(users, null, true, true);
                Utilities.RunOnUIThread(new Runnable() {
                    public void run() {
                        MessagesController.this.contactsBook = contactsMapBook;
                        MessagesController.this.contactsSectionsDict = sectionsDictBook;
                        MessagesController.this.sortedContactsSectionsArray = sortedSectionsArrayBook;
                        NotificationCenter.Instance.postNotificationName(14, new Object[0]);
                        MessagesController.this.contacts = contactsArr;
                        MessagesController.this.contactsByPhones = contactsPhones;
                        MessagesController.this.contactsDict = contactsDictionery;
                        MessagesController.this.usersSectionsDict = sectionsDict;
                        MessagesController.this.sortedUsersSectionsArray = sortedSectionsArray;
                        NotificationCenter.Instance.postNotificationName(13, new Object[0]);
                        NotificationCenter.Instance.postNotificationName(3, Integer.valueOf(1));
                    }
                });
            }
        });
    }

    public void addContact(User user) {
        if (user != null) {
            long num = addContact(this.currentAccount, user, user.phone);
            rebuildContactsWithNewUser(user, num);
            TL_contacts_importContacts req = new TL_contacts_importContacts();
            ArrayList<TL_inputPhoneContact> contacts = new ArrayList();
            TL_inputPhoneContact c = new TL_inputPhoneContact();
            c.phone = user.phone;
            c.first_name = user.first_name;
            c.last_name = user.last_name;
            c.client_id = num;
            contacts.add(c);
            req.contacts = contacts;
            req.replace = false;
            ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
                public void run(TLObject response, TL_error error) {
                }
            }, null, true, RPCRequest.RPCRequestClassGeneric | RPCRequest.RPCRequestClassFailOnServerErrors);
        }
    }

    public void fileDidUploaded(final String location, final InputFile file, final InputEncryptedFile encryptedFile) {
        if (this.uploadingAvatar == null || !this.uploadingAvatar.equals(location)) {
            Utilities.RunOnUIThread(new Runnable() {
                public void run() {
                    DelayedMessage message = (DelayedMessage) MessagesController.this.delayedMessages.get(location);
                    if (message != null) {
                        if (file != null) {
                            if (message.type == 0) {
                                message.sendRequest.media.file = file;
                                MessagesController.this.performSendMessageRequest(message.sendRequest, message.obj);
                            } else if (message.type == 1) {
                                if (message.sendRequest.media.thumb == null) {
                                    message.sendRequest.media.thumb = file;
                                    MessagesController.this.performSendDelayedMessage(message);
                                } else {
                                    message.sendRequest.media.file = file;
                                    MessagesController.this.performSendMessageRequest(message.sendRequest, message.obj);
                                }
                            } else if (message.type == 2) {
                                message.sendRequest.media.file = file;
                                MessagesController.this.performSendMessageRequest(message.sendRequest, message.obj);
                            }
                        } else if (encryptedFile != null) {
                            if (message.type == 0) {
                                MessagesController.this.performSendEncryptedRequest(message.sendEncryptedRequest, message.obj, message.encryptedChat, encryptedFile);
                            } else if (message.type == 1) {
                                MessagesController.this.performSendEncryptedRequest(message.sendEncryptedRequest, message.obj, message.encryptedChat, encryptedFile);
                            } else if (message.type == 2) {
                                MessagesController.this.performSendEncryptedRequest(message.sendEncryptedRequest, message.obj, message.encryptedChat, encryptedFile);
                            }
                        }
                        MessagesController.this.delayedMessages.remove(location);
                    }
                }
            });
            return;
        }
        TL_photos_uploadProfilePhoto req = new TL_photos_uploadProfilePhoto();
        req.caption = BuildConfig.FLAVOR;
        req.crop = new TL_inputPhotoCropAuto();
        req.file = file;
        req.geo_point = new TL_inputGeoPointEmpty();
        ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {

            class C03851 implements Runnable {
                C03851() {
                }

                public void run() {
                    NotificationCenter.Instance.postNotificationName(3, Integer.valueOf(2));
                    UserConfig.saveConfig(true);
                }
            }

            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    User user = (User) MessagesController.this.users.get(Integer.valueOf(UserConfig.clientUserId));
                    if (user == null) {
                        user = UserConfig.currentUser;
                        MessagesController.this.users.put(Integer.valueOf(user.id), user);
                    } else {
                        UserConfig.currentUser = user;
                    }
                    if (user != null) {
                        TL_photos_photo photo = (TL_photos_photo) response;
                        ArrayList<PhotoSize> sizes = photo.photo.sizes;
                        PhotoSize smallSize = PhotoObject.getClosestPhotoSizeWithSize(sizes, 100, 100);
                        PhotoSize bigSize = PhotoObject.getClosestPhotoSizeWithSize(sizes, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE);
                        user.photo = new TL_userProfilePhoto();
                        user.photo.photo_id = photo.photo.id;
                        if (smallSize != null) {
                            user.photo.photo_small = smallSize.location;
                        }
                        if (bigSize != null) {
                            user.photo.photo_big = bigSize.location;
                        } else if (smallSize != null) {
                            user.photo.photo_small = smallSize.location;
                        }
                        MessagesStorage.Instance.clearUserPhotos(user.id);
                        ArrayList<User> users = new ArrayList();
                        users.add(user);
                        MessagesStorage.Instance.putUsersAndChats(users, null, false, true);
                        Utilities.RunOnUIThread(new C03851());
                    }
                }
            }
        }, null, true, RPCRequest.RPCRequestClassGeneric);
    }

    public void createChat(String title, ArrayList<Integer> selectedContacts, final InputFile uploadedAvatar) {
        TL_messages_createChat req = new TL_messages_createChat();
        req.title = title;
        Iterator i$ = selectedContacts.iterator();
        while (i$.hasNext()) {
            InputUser inputUser;
            User user = (User) this.users.get((Integer) i$.next());
            if ((user instanceof TL_userForeign) || (user instanceof TL_userRequest)) {
                inputUser = new TL_inputUserForeign();
                inputUser.user_id = user.id;
                inputUser.access_hash = user.access_hash;
            } else {
                inputUser = new TL_inputUserContact();
                inputUser.user_id = user.id;
            }
            req.users.add(inputUser);
        }
        ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {

            class C03861 implements Runnable {
                C03861() {
                }

                public void run() {
                    NotificationCenter.Instance.postNotificationName(16, new Object[0]);
                }
            }

            public void run(TLObject response, TL_error error) {
                if (error != null) {
                    Utilities.RunOnUIThread(new C03861());
                    return;
                }
                final messages_StatedMessage res = (messages_StatedMessage) response;
                MessagesStorage.Instance.putUsersAndChats(res.users, res.chats, true, true);
                Utilities.RunOnUIThread(new Runnable() {
                    public void run() {
                        Chat chat;
                        Iterator i$ = res.users.iterator();
                        while (i$.hasNext()) {
                            User user = (User) i$.next();
                            MessagesController.this.users.put(Integer.valueOf(user.id), user);
                            if (user.id == UserConfig.clientUserId) {
                                UserConfig.currentUser = user;
                            }
                        }
                        i$ = res.chats.iterator();
                        while (i$.hasNext()) {
                            chat = (Chat) i$.next();
                            MessagesController.this.chats.put(Integer.valueOf(chat.id), chat);
                        }
                        ArrayList<MessageObject> messagesObj = new ArrayList();
                        messagesObj.add(new MessageObject(res.message, MessagesController.this.users));
                        chat = (Chat) res.chats.get(0);
                        MessagesController.this.updateInterfaceWithMessages((long) (-chat.id), messagesObj);
                        NotificationCenter.Instance.postNotificationName(15, Integer.valueOf(chat.id));
                        NotificationCenter.Instance.postNotificationName(4, new Object[0]);
                        if (uploadedAvatar != null) {
                            MessagesController.this.changeChatAvatar(chat.id, uploadedAvatar);
                        }
                    }
                });
                ArrayList<Message> messages = new ArrayList();
                messages.add(res.message);
                MessagesStorage.Instance.putMessages(messages, true, true);
                if (MessagesStorage.lastSeqValue + 1 == res.seq) {
                    MessagesStorage.lastSeqValue = res.seq;
                    MessagesStorage.lastPtsValue = res.pts;
                    MessagesStorage.Instance.saveDiffParams(MessagesStorage.lastSeqValue, MessagesStorage.lastPtsValue, MessagesStorage.lastDateValue, MessagesStorage.lastQtsValue);
                } else if (MessagesStorage.lastSeqValue != res.seq) {
                    FileLog.m800e("tmessages", "need get diff TL_messages_createChat, seq: " + MessagesStorage.lastSeqValue + " " + res.seq);
                    if (MessagesController.this.gettingDifference || MessagesController.this.updatesStartWaitTime == 0 || (MessagesController.this.updatesStartWaitTime != 0 && MessagesController.this.updatesStartWaitTime + 1500 > System.currentTimeMillis())) {
                        if (MessagesController.this.updatesStartWaitTime == 0) {
                            MessagesController.this.updatesStartWaitTime = System.currentTimeMillis();
                        }
                        FileLog.m800e("tmessages", "add TL_messages_createChat to queue");
                        UserActionUpdates updates = new UserActionUpdates();
                        updates.seq = res.seq;
                        MessagesController.this.updatesQueue.add(updates);
                        return;
                    }
                    MessagesController.this.getDifference();
                }
            }
        }, null, true, RPCRequest.RPCRequestClassGeneric);
    }

    public void addUserToChat(int chat_id, final int user_id, final ChatParticipants info) {
        TL_messages_addChatUser req = new TL_messages_addChatUser();
        req.chat_id = chat_id;
        req.fwd_limit = 50;
        User user = (User) this.users.get(Integer.valueOf(user_id));
        if ((user instanceof TL_userForeign) || (user instanceof TL_userRequest)) {
            req.user_id = new TL_inputUserForeign();
            req.user_id.user_id = user.id;
            req.user_id.access_hash = user.access_hash;
        } else {
            req.user_id = new TL_inputUserContact();
            req.user_id.user_id = user.id;
        }
        ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    final messages_StatedMessage res = (messages_StatedMessage) response;
                    MessagesStorage.Instance.putUsersAndChats(res.users, res.chats, true, true);
                    Utilities.RunOnUIThread(new Runnable() {
                        public void run() {
                            Chat chat;
                            Iterator i$ = res.users.iterator();
                            while (i$.hasNext()) {
                                User user = (User) i$.next();
                                MessagesController.this.users.put(Integer.valueOf(user.id), user);
                                if (user.id == UserConfig.clientUserId) {
                                    UserConfig.currentUser = user;
                                }
                            }
                            i$ = res.chats.iterator();
                            while (i$.hasNext()) {
                                chat = (Chat) i$.next();
                                MessagesController.this.chats.put(Integer.valueOf(chat.id), chat);
                            }
                            ArrayList<MessageObject> messagesObj = new ArrayList();
                            messagesObj.add(new MessageObject(res.message, MessagesController.this.users));
                            chat = (Chat) res.chats.get(0);
                            MessagesController.this.chats.put(Integer.valueOf(chat.id), chat);
                            MessagesController.this.updateInterfaceWithMessages((long) (-chat.id), messagesObj);
                            NotificationCenter.Instance.postNotificationName(3, Integer.valueOf(32));
                            NotificationCenter.Instance.postNotificationName(4, new Object[0]);
                            if (info != null) {
                                i$ = info.participants.iterator();
                                while (i$.hasNext()) {
                                    if (((TL_chatParticipant) i$.next()).user_id == user_id) {
                                        return;
                                    }
                                }
                                TL_chatParticipant newPart = new TL_chatParticipant();
                                newPart.user_id = user_id;
                                newPart.inviter_id = UserConfig.clientUserId;
                                newPart.date = ConnectionsManager.Instance.getCurrentTime();
                                info.participants.add(0, newPart);
                                MessagesStorage.Instance.updateChatInfo(info.chat_id, info, true);
                                NotificationCenter.Instance.postNotificationName(17, Integer.valueOf(info.chat_id), info);
                            }
                        }
                    });
                    ArrayList<Message> messages = new ArrayList();
                    messages.add(res.message);
                    MessagesStorage.Instance.putMessages(messages, true, true);
                    if (MessagesStorage.lastSeqValue + 1 == res.seq) {
                        MessagesStorage.lastSeqValue = res.seq;
                        MessagesStorage.lastPtsValue = res.pts;
                        MessagesStorage.Instance.saveDiffParams(MessagesStorage.lastSeqValue, MessagesStorage.lastPtsValue, MessagesStorage.lastDateValue, MessagesStorage.lastQtsValue);
                    } else if (MessagesStorage.lastSeqValue != res.seq) {
                        FileLog.m800e("tmessages", "need get diff TL_messages_addChatUser, seq: " + MessagesStorage.lastSeqValue + " " + res.seq);
                        if (MessagesController.this.gettingDifference || MessagesController.this.updatesStartWaitTime == 0 || (MessagesController.this.updatesStartWaitTime != 0 && MessagesController.this.updatesStartWaitTime + 1500 > System.currentTimeMillis())) {
                            if (MessagesController.this.updatesStartWaitTime == 0) {
                                MessagesController.this.updatesStartWaitTime = System.currentTimeMillis();
                            }
                            FileLog.m800e("tmessages", "add TL_messages_addChatUser to queue");
                            UserActionUpdates updates = new UserActionUpdates();
                            updates.seq = res.seq;
                            MessagesController.this.updatesQueue.add(updates);
                            return;
                        }
                        MessagesController.this.getDifference();
                    }
                }
            }
        }, null, true, RPCRequest.RPCRequestClassGeneric);
    }

    public void deleteUserFromChat(int chat_id, final int user_id, final ChatParticipants info) {
        TL_messages_deleteChatUser req = new TL_messages_deleteChatUser();
        req.chat_id = chat_id;
        User user = (User) this.users.get(Integer.valueOf(user_id));
        if ((user instanceof TL_userForeign) || (user instanceof TL_userRequest)) {
            req.user_id = new TL_inputUserForeign();
            req.user_id.user_id = user.id;
            req.user_id.access_hash = user.access_hash;
        } else if (user instanceof TL_userSelf) {
            req.user_id = new TL_inputUserSelf();
        } else {
            req.user_id = new TL_inputUserContact();
            req.user_id.user_id = user.id;
        }
        ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    final messages_StatedMessage res = (messages_StatedMessage) response;
                    MessagesStorage.Instance.putUsersAndChats(res.users, res.chats, true, true);
                    Utilities.RunOnUIThread(new Runnable() {
                        public void run() {
                            Iterator i$ = res.users.iterator();
                            while (i$.hasNext()) {
                                User user = (User) i$.next();
                                MessagesController.this.users.put(Integer.valueOf(user.id), user);
                                if (user.id == UserConfig.clientUserId) {
                                    UserConfig.currentUser = user;
                                }
                            }
                            i$ = res.chats.iterator();
                            while (i$.hasNext()) {
                                Chat chat = (Chat) i$.next();
                                MessagesController.this.chats.put(Integer.valueOf(chat.id), chat);
                            }
                            if (user_id != UserConfig.clientUserId) {
                                ArrayList<MessageObject> messagesObj = new ArrayList();
                                messagesObj.add(new MessageObject(res.message, MessagesController.this.users));
                                chat = (Chat) res.chats.get(0);
                                MessagesController.this.chats.put(Integer.valueOf(chat.id), chat);
                                MessagesController.this.updateInterfaceWithMessages((long) (-chat.id), messagesObj);
                                NotificationCenter.Instance.postNotificationName(3, Integer.valueOf(32));
                                NotificationCenter.Instance.postNotificationName(4, new Object[0]);
                            }
                            boolean changed = false;
                            if (info != null) {
                                for (int a = 0; a < info.participants.size(); a++) {
                                    if (((TL_chatParticipant) info.participants.get(a)).user_id == user_id) {
                                        info.participants.remove(a);
                                        changed = true;
                                        break;
                                    }
                                }
                                if (changed) {
                                    MessagesStorage.Instance.updateChatInfo(info.chat_id, info, true);
                                    NotificationCenter.Instance.postNotificationName(17, Integer.valueOf(info.chat_id), info);
                                }
                            }
                        }
                    });
                    if (user_id != UserConfig.clientUserId) {
                        ArrayList<Message> messages = new ArrayList();
                        messages.add(res.message);
                        MessagesStorage.Instance.putMessages(messages, true, true);
                    }
                    if (MessagesStorage.lastSeqValue + 1 == res.seq) {
                        MessagesStorage.lastSeqValue = res.seq;
                        MessagesStorage.lastPtsValue = res.pts;
                        MessagesStorage.Instance.saveDiffParams(MessagesStorage.lastSeqValue, MessagesStorage.lastPtsValue, MessagesStorage.lastDateValue, MessagesStorage.lastQtsValue);
                    } else if (MessagesStorage.lastSeqValue != res.seq) {
                        FileLog.m800e("tmessages", "need get diff TL_messages_deleteChatUser, seq: " + MessagesStorage.lastSeqValue + " " + res.seq);
                        if (MessagesController.this.gettingDifference || MessagesController.this.updatesStartWaitTime == 0 || (MessagesController.this.updatesStartWaitTime != 0 && MessagesController.this.updatesStartWaitTime + 1500 > System.currentTimeMillis())) {
                            if (MessagesController.this.updatesStartWaitTime == 0) {
                                MessagesController.this.updatesStartWaitTime = System.currentTimeMillis();
                            }
                            FileLog.m800e("tmessages", "add TL_messages_deleteChatUser to queue");
                            UserActionUpdates updates = new UserActionUpdates();
                            updates.seq = res.seq;
                            MessagesController.this.updatesQueue.add(updates);
                            return;
                        }
                        MessagesController.this.getDifference();
                    }
                }
            }
        }, null, true, RPCRequest.RPCRequestClassGeneric);
    }

    public void changeChatTitle(int chat_id, String title) {
        TL_messages_editChatTitle req = new TL_messages_editChatTitle();
        req.chat_id = chat_id;
        req.title = title;
        ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    final messages_StatedMessage res = (messages_StatedMessage) response;
                    MessagesStorage.Instance.putUsersAndChats(res.users, res.chats, true, true);
                    Utilities.RunOnUIThread(new Runnable() {
                        public void run() {
                            Chat chat;
                            Iterator i$ = res.users.iterator();
                            while (i$.hasNext()) {
                                User user = (User) i$.next();
                                MessagesController.this.users.put(Integer.valueOf(user.id), user);
                                if (user.id == UserConfig.clientUserId) {
                                    UserConfig.currentUser = user;
                                }
                            }
                            i$ = res.chats.iterator();
                            while (i$.hasNext()) {
                                chat = (Chat) i$.next();
                                MessagesController.this.chats.put(Integer.valueOf(chat.id), chat);
                            }
                            ArrayList<MessageObject> messagesObj = new ArrayList();
                            messagesObj.add(new MessageObject(res.message, MessagesController.this.users));
                            chat = (Chat) res.chats.get(0);
                            MessagesController.this.chats.put(Integer.valueOf(chat.id), chat);
                            MessagesController.this.updateInterfaceWithMessages((long) (-chat.id), messagesObj);
                            NotificationCenter.Instance.postNotificationName(4, new Object[0]);
                            NotificationCenter.Instance.postNotificationName(3, Integer.valueOf(16));
                        }
                    });
                    ArrayList<Message> messages = new ArrayList();
                    messages.add(res.message);
                    MessagesStorage.Instance.putMessages(messages, true, true);
                    if (MessagesStorage.lastSeqValue + 1 == res.seq) {
                        MessagesStorage.lastSeqValue = res.seq;
                        MessagesStorage.lastPtsValue = res.pts;
                        MessagesStorage.Instance.saveDiffParams(MessagesStorage.lastSeqValue, MessagesStorage.lastPtsValue, MessagesStorage.lastDateValue, MessagesStorage.lastQtsValue);
                    } else if (MessagesStorage.lastSeqValue != res.seq) {
                        FileLog.m800e("tmessages", "need get diff TL_messages_editChatTitle, seq: " + MessagesStorage.lastSeqValue + " " + res.seq);
                        if (MessagesController.this.gettingDifference || MessagesController.this.updatesStartWaitTime == 0 || (MessagesController.this.updatesStartWaitTime != 0 && MessagesController.this.updatesStartWaitTime + 1500 > System.currentTimeMillis())) {
                            if (MessagesController.this.updatesStartWaitTime == 0) {
                                MessagesController.this.updatesStartWaitTime = System.currentTimeMillis();
                            }
                            FileLog.m800e("tmessages", "add TL_messages_editChatTitle to queue");
                            UserActionUpdates updates = new UserActionUpdates();
                            updates.seq = res.seq;
                            MessagesController.this.updatesQueue.add(updates);
                            return;
                        }
                        MessagesController.this.getDifference();
                    }
                }
            }
        }, null, true, RPCRequest.RPCRequestClassGeneric);
    }

    public void changeChatAvatar(int chat_id, InputFile uploadedAvatar) {
        TL_messages_editChatPhoto req2 = new TL_messages_editChatPhoto();
        req2.chat_id = chat_id;
        if (uploadedAvatar != null) {
            req2.photo = new TL_inputChatUploadedPhoto();
            req2.photo.file = uploadedAvatar;
            req2.photo.crop = new TL_inputPhotoCropAuto();
        } else {
            req2.photo = new TL_inputChatPhotoEmpty();
        }
        ConnectionsManager.Instance.performRpc(req2, new RPCRequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    final messages_StatedMessage res = (messages_StatedMessage) response;
                    MessagesStorage.Instance.putUsersAndChats(res.users, res.chats, true, true);
                    Utilities.RunOnUIThread(new Runnable() {
                        public void run() {
                            Chat chat;
                            Iterator i$ = res.users.iterator();
                            while (i$.hasNext()) {
                                User user = (User) i$.next();
                                MessagesController.this.users.put(Integer.valueOf(user.id), user);
                                if (user.id == UserConfig.clientUserId) {
                                    UserConfig.currentUser = user;
                                }
                            }
                            i$ = res.chats.iterator();
                            while (i$.hasNext()) {
                                chat = (Chat) i$.next();
                                MessagesController.this.chats.put(Integer.valueOf(chat.id), chat);
                            }
                            ArrayList<MessageObject> messagesObj = new ArrayList();
                            messagesObj.add(new MessageObject(res.message, MessagesController.this.users));
                            chat = (Chat) res.chats.get(0);
                            MessagesController.this.chats.put(Integer.valueOf(chat.id), chat);
                            MessagesController.this.updateInterfaceWithMessages((long) (-chat.id), messagesObj);
                            NotificationCenter.Instance.postNotificationName(4, new Object[0]);
                            NotificationCenter.Instance.postNotificationName(3, Integer.valueOf(8));
                        }
                    });
                    ArrayList<Message> messages = new ArrayList();
                    messages.add(res.message);
                    MessagesStorage.Instance.putMessages(messages, true, true);
                    if (MessagesStorage.lastSeqValue + 1 == res.seq) {
                        MessagesStorage.lastSeqValue = res.seq;
                        MessagesStorage.lastPtsValue = res.pts;
                        MessagesStorage.Instance.saveDiffParams(MessagesStorage.lastSeqValue, MessagesStorage.lastPtsValue, MessagesStorage.lastDateValue, MessagesStorage.lastQtsValue);
                    } else if (MessagesStorage.lastSeqValue != res.seq) {
                        FileLog.m800e("tmessages", "need get diff TL_messages_editChatPhoto, seq: " + MessagesStorage.lastSeqValue + " " + res.seq);
                        if (MessagesController.this.gettingDifference || MessagesController.this.updatesStartWaitTime == 0 || (MessagesController.this.updatesStartWaitTime != 0 && MessagesController.this.updatesStartWaitTime + 1500 > System.currentTimeMillis())) {
                            if (MessagesController.this.updatesStartWaitTime == 0) {
                                MessagesController.this.updatesStartWaitTime = System.currentTimeMillis();
                            }
                            FileLog.m800e("tmessages", "add TL_messages_editChatPhoto to queue");
                            UserActionUpdates updates = new UserActionUpdates();
                            updates.seq = res.seq;
                            MessagesController.this.updatesQueue.add(updates);
                            return;
                        }
                        MessagesController.this.getDifference();
                    }
                }
            }
        }, null, true, RPCRequest.RPCRequestClassGeneric);
    }

    public void unregistedPush() {
        ConnectionsManager.Instance.performRpc(new TL_auth_logOut(), new RPCRequestDelegate() {
            public void run(TLObject response, TL_error error) {
            }
        }, null, true, RPCRequest.RPCRequestClassGeneric);
        if (UserConfig.registeredForPush && UserConfig.pushString.length() != 0) {
            TL_account_unregisterDevice req = new TL_account_unregisterDevice();
            req.token = UserConfig.pushString;
            req.token_type = 2;
            ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
                public void run(TLObject response, TL_error error) {
                }
            }, null, true, RPCRequest.RPCRequestClassGeneric);
        }
    }

    public void registerForPush(final String regid) {
        if (regid != null && regid.length() != 0 && !this.registeringForPush && UserConfig.clientUserId != 0) {
            if (!UserConfig.registeredForPush || !regid.equals(UserConfig.pushString)) {
                this.registeringForPush = true;
                TL_account_registerDevice req = new TL_account_registerDevice();
                req.token_type = 2;
                req.token = regid;
                req.app_sandbox = false;
                try {
                    req.lang_code = Locale.getDefault().getCountry();
                    req.device_model = Build.MANUFACTURER + Build.MODEL;
                    if (req.device_model == null) {
                        req.device_model = "Android unknown";
                    }
                    req.system_version = "SDK " + VERSION.SDK_INT;
                    req.app_version = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0).versionName;
                    if (req.app_version == null) {
                        req.app_version = "App version unknown";
                    }
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                    req.lang_code = "en";
                    req.device_model = "Android unknown";
                    req.system_version = "SDK " + VERSION.SDK_INT;
                    req.app_version = "App version unknown";
                }
                if (req.lang_code == null || req.lang_code.length() == 0) {
                    req.lang_code = "en";
                }
                if (req.device_model == null || req.device_model.length() == 0) {
                    req.device_model = "Android unknown";
                }
                if (req.app_version == null || req.app_version.length() == 0) {
                    req.app_version = "App version unknown";
                }
                if (req.system_version == null || req.system_version.length() == 0) {
                    req.system_version = "SDK Unknown";
                }
                if (req.app_version != null) {
                    ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {

                        class C03931 implements Runnable {
                            C03931() {
                            }

                            public void run() {
                                MessagesController.this.registeringForPush = false;
                            }
                        }

                        public void run(TLObject response, TL_error error) {
                            if (error == null) {
                                FileLog.m800e("tmessages", "registered for push");
                                UserConfig.registeredForPush = true;
                                UserConfig.pushString = regid;
                                UserConfig.saveConfig(false);
                            }
                            Utilities.RunOnUIThread(new C03931());
                        }
                    }, null, true, RPCRequest.RPCRequestClassGeneric);
                }
            }
        }
    }

    public void loadCurrentState() {
        if (!this.updatingState) {
            this.updatingState = true;
            ConnectionsManager.Instance.performRpc(new TL_updates_getState(), new RPCRequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    MessagesController.this.updatingState = false;
                    if (error == null) {
                        TL_updates_state res = (TL_updates_state) response;
                        MessagesStorage.lastDateValue = res.date;
                        MessagesStorage.lastPtsValue = res.pts;
                        MessagesStorage.lastSeqValue = res.seq;
                        MessagesStorage.lastQtsValue = res.qts;
                        MessagesStorage.Instance.saveDiffParams(MessagesStorage.lastSeqValue, MessagesStorage.lastPtsValue, MessagesStorage.lastDateValue, MessagesStorage.lastQtsValue);
                    } else if (error.code != 401) {
                        MessagesController.this.loadCurrentState();
                    }
                }
            }, null, true, RPCRequest.RPCRequestClassGeneric);
        }
    }

    private int getUpdateSeq(Updates updates) {
        if (updates instanceof TL_updatesCombined) {
            return updates.seq_start;
        }
        return updates.seq;
    }

    private void processUpdatesQueue(boolean getDifference) {
        final int stateCopy;
        if (!this.updatesQueue.isEmpty()) {
            Collections.sort(this.updatesQueue, new Comparator<Updates>() {
                public int compare(Updates updates, Updates updates2) {
                    int seq1 = MessagesController.this.getUpdateSeq(updates);
                    int seq2 = MessagesController.this.getUpdateSeq(updates2);
                    if (seq1 == seq2) {
                        return 0;
                    }
                    if (seq1 > seq2) {
                        return 1;
                    }
                    return -1;
                }
            });
            boolean anyProceed = false;
            int a = 0;
            while (this.updatesQueue.size() > 0) {
                Updates updates = (Updates) this.updatesQueue.get(a);
                int seq = getUpdateSeq(updates);
                if (MessagesStorage.lastSeqValue + 1 == seq || MessagesStorage.lastSeqValue == seq) {
                    processUpdates(updates, true);
                    anyProceed = true;
                    this.updatesQueue.remove(a);
                    a--;
                } else if (MessagesStorage.lastSeqValue >= seq) {
                    this.updatesQueue.remove(a);
                    a--;
                } else if (this.updatesStartWaitTime == 0 || (!anyProceed && this.updatesStartWaitTime + 1500 <= System.currentTimeMillis())) {
                    FileLog.m800e("tmessages", "HOLE IN UPDATES QUEUE - getDifference");
                    this.updatesStartWaitTime = 0;
                    this.updatesQueue.clear();
                    getDifference();
                    return;
                } else {
                    FileLog.m800e("tmessages", "HOLE IN UPDATES QUEUE - will wait more time");
                    if (anyProceed) {
                        this.updatesStartWaitTime = System.currentTimeMillis();
                        return;
                    }
                    return;
                }
                a++;
            }
            this.updatesQueue.clear();
            FileLog.m800e("tmessages", "UPDATES QUEUE PROCEED - OK");
            this.updatesStartWaitTime = 0;
            if (getDifference) {
                stateCopy = ConnectionsManager.Instance.connectionState;
                Utilities.RunOnUIThread(new Runnable() {
                    public void run() {
                        NotificationCenter.Instance.postNotificationName(703, Integer.valueOf(stateCopy));
                    }
                });
            }
        } else if (getDifference) {
            stateCopy = ConnectionsManager.Instance.connectionState;
            Utilities.RunOnUIThread(new Runnable() {
                public void run() {
                    NotificationCenter.Instance.postNotificationName(703, Integer.valueOf(stateCopy));
                }
            });
        } else {
            this.updatesStartWaitTime = 0;
        }
    }

    public void getDifference() {
        registerForPush(UserConfig.pushString);
        if (MessagesStorage.lastDateValue == 0) {
            loadCurrentState();
        } else if (!this.gettingDifference) {
            if (!this.firstGettingTask) {
                getNewDeleteTask(null);
                this.firstGettingTask = true;
            }
            this.gettingDifference = true;
            TL_updates_getDifference req = new TL_updates_getDifference();
            req.pts = MessagesStorage.lastPtsValue;
            req.date = MessagesStorage.lastDateValue;
            req.qts = MessagesStorage.lastQtsValue;
            FileLog.m800e("tmessages", "start getDifference with date = " + MessagesStorage.lastDateValue + " pts = " + MessagesStorage.lastPtsValue + " seq = " + MessagesStorage.lastSeqValue);
            if (ConnectionsManager.Instance.connectionState == 0) {
                ConnectionsManager.Instance.connectionState = 3;
                final int stateCopy = ConnectionsManager.Instance.connectionState;
                Utilities.RunOnUIThread(new Runnable() {
                    public void run() {
                        NotificationCenter.Instance.postNotificationName(703, Integer.valueOf(stateCopy));
                    }
                });
            }
            ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    MessagesController.this.gettingDifferenceAgain = false;
                    if (error == null) {
                        final updates_Difference res = (updates_Difference) response;
                        MessagesController.this.gettingDifferenceAgain = res instanceof TL_updates_differenceSlice;
                        final HashMap<Integer, User> usersDict = new HashMap();
                        Iterator i$ = res.users.iterator();
                        while (i$.hasNext()) {
                            User user = (User) i$.next();
                            usersDict.put(Integer.valueOf(user.id), user);
                        }
                        final ArrayList<TL_updateMessageID> msgUpdates = new ArrayList();
                        if (!res.other_updates.isEmpty()) {
                            int a = 0;
                            while (a < res.other_updates.size()) {
                                Update upd = (Update) res.other_updates.get(a);
                                if (upd instanceof TL_updateMessageID) {
                                    msgUpdates.add((TL_updateMessageID) upd);
                                    res.other_updates.remove(a);
                                    a--;
                                }
                                a++;
                            }
                        }
                        MessagesStorage.Instance.storageQueue.postRunnable(new Runnable() {

                            class C03972 implements Runnable {

                                class C03962 implements Runnable {
                                    C03962() {
                                    }

                                    public void run() {
                                        MessagesStorage.Instance.startTransaction(false);
                                        MessagesStorage.Instance.putMessages(res.new_messages, false, false);
                                        MessagesStorage.Instance.putUsersAndChats(res.users, res.chats, false, false);
                                        MessagesStorage.Instance.commitTransaction(false);
                                    }
                                }

                                C03972() {
                                }

                                public void run() {
                                    if (!(res.new_messages.isEmpty() && res.new_encrypted_messages.isEmpty())) {
                                        Message message;
                                        final HashMap<Long, ArrayList<MessageObject>> messages = new HashMap();
                                        Iterator i$ = res.new_encrypted_messages.iterator();
                                        while (i$.hasNext()) {
                                            message = MessagesController.this.decryptMessage((EncryptedMessage) i$.next());
                                            if (message != null) {
                                                res.new_messages.add(message);
                                            }
                                        }
                                        MessageObject lastMessage = null;
                                        i$ = res.new_messages.iterator();
                                        while (i$.hasNext()) {
                                            long uid;
                                            message = (Message) i$.next();
                                            MessageObject obj = new MessageObject(message, usersDict);
                                            long dialog_id = obj.messageOwner.dialog_id;
                                            if (dialog_id == 0) {
                                                if (obj.messageOwner.to_id.chat_id != 0) {
                                                    dialog_id = (long) (-obj.messageOwner.to_id.chat_id);
                                                } else {
                                                    dialog_id = (long) obj.messageOwner.to_id.user_id;
                                                }
                                            }
                                            if (!(res instanceof TL_updates_differenceSlice) && (!(dialog_id == MessagesController.this.openned_dialog_id && ApplicationLoader.lastPauseTime == 0) && !obj.messageOwner.out && obj.messageOwner.unread && (lastMessage == null || lastMessage.messageOwner.date < obj.messageOwner.date))) {
                                                lastMessage = obj;
                                            }
                                            if (message.dialog_id != 0) {
                                                uid = message.dialog_id;
                                            } else if (message.to_id.chat_id != 0) {
                                                uid = (long) (-message.to_id.chat_id);
                                            } else {
                                                if (message.to_id.user_id == UserConfig.clientUserId) {
                                                    message.to_id.user_id = message.from_id;
                                                }
                                                uid = (long) message.to_id.user_id;
                                            }
                                            ArrayList<MessageObject> arr = (ArrayList) messages.get(Long.valueOf(uid));
                                            if (arr == null) {
                                                arr = new ArrayList();
                                                messages.put(Long.valueOf(uid), arr);
                                            }
                                            arr.add(obj);
                                        }
                                        final MessageObject object = lastMessage;
                                        Utilities.RunOnUIThread(new Runnable() {
                                            public void run() {
                                                Iterator i$ = res.users.iterator();
                                                while (i$.hasNext()) {
                                                    User user = (User) i$.next();
                                                    MessagesController.this.users.put(Integer.valueOf(user.id), user);
                                                    if (user.id == UserConfig.clientUserId) {
                                                        UserConfig.currentUser = user;
                                                    }
                                                }
                                                i$ = res.chats.iterator();
                                                while (i$.hasNext()) {
                                                    Chat chat = (Chat) i$.next();
                                                    MessagesController.this.chats.put(Integer.valueOf(chat.id), chat);
                                                }
                                                for (Entry<Long, ArrayList<MessageObject>> pair : messages.entrySet()) {
                                                    ArrayList<MessageObject> value = (ArrayList) pair.getValue();
                                                    MessagesController.this.updateInterfaceWithMessages(((Long) pair.getKey()).longValue(), value);
                                                }
                                                NotificationCenter.Instance.postNotificationName(4, new Object[0]);
                                                if (object != null) {
                                                    MessagesController.this.showInAppNotification(object);
                                                }
                                            }
                                        });
                                        MessagesStorage.Instance.storageQueue.postRunnable(new C03962());
                                    }
                                    if (!(res == null || res.other_updates.isEmpty())) {
                                        MessagesController.this.processUpdateArray(res.other_updates, res.users, res.chats);
                                    }
                                    MessagesController.this.gettingDifference = false;
                                    if (res instanceof TL_updates_difference) {
                                        MessagesStorage.lastSeqValue = res.state.seq;
                                        MessagesStorage.lastDateValue = res.state.date;
                                        MessagesStorage.lastPtsValue = res.state.pts;
                                        MessagesStorage.lastQtsValue = res.state.qts;
                                        ConnectionsManager.Instance.connectionState = 0;
                                        MessagesController.this.processUpdatesQueue(true);
                                    } else if (res instanceof TL_updates_differenceSlice) {
                                        MessagesStorage.lastSeqValue = res.intermediate_state.seq;
                                        MessagesStorage.lastDateValue = res.intermediate_state.date;
                                        MessagesStorage.lastPtsValue = res.intermediate_state.pts;
                                        MessagesStorage.lastQtsValue = res.intermediate_state.qts;
                                        MessagesController.this.gettingDifferenceAgain = true;
                                        MessagesController.this.getDifference();
                                    } else if (res instanceof TL_updates_differenceEmpty) {
                                        MessagesStorage.lastSeqValue = res.seq;
                                        MessagesStorage.lastDateValue = res.date;
                                        ConnectionsManager.Instance.connectionState = 0;
                                        MessagesController.this.processUpdatesQueue(true);
                                    }
                                    MessagesStorage.Instance.saveDiffParams(MessagesStorage.lastSeqValue, MessagesStorage.lastPtsValue, MessagesStorage.lastDateValue, MessagesStorage.lastQtsValue);
                                    FileLog.m800e("tmessages", "received difference with date = " + MessagesStorage.lastDateValue + " pts = " + MessagesStorage.lastPtsValue + " seq = " + MessagesStorage.lastSeqValue);
                                    FileLog.m800e("tmessages", "messages = " + res.new_messages.size() + " users = " + res.users.size() + " chats = " + res.chats.size() + " other updates = " + res.other_updates.size());
                                }
                            }

                            public void run() {
                                if (!msgUpdates.isEmpty()) {
                                    final HashMap<Integer, Integer> corrected = new HashMap();
                                    Iterator i$ = msgUpdates.iterator();
                                    while (i$.hasNext()) {
                                        TL_updateMessageID update = (TL_updateMessageID) i$.next();
                                        Integer oldId = MessagesStorage.Instance.updateMessageStateAndId(update.random_id, null, update.id, 0, false);
                                        if (oldId != null) {
                                            corrected.put(oldId, Integer.valueOf(update.id));
                                        }
                                    }
                                    if (!corrected.isEmpty()) {
                                        Utilities.RunOnUIThread(new Runnable() {
                                            public void run() {
                                                for (Entry<Integer, Integer> entry : corrected.entrySet()) {
                                                    MessagesController.this.sendingMessages.remove(((Integer) entry.getKey()).intValue());
                                                    Integer newId = (Integer) entry.getValue();
                                                    NotificationCenter.Instance.postNotificationName(10, oldId, newId);
                                                }
                                            }
                                        });
                                    }
                                }
                                Utilities.stageQueue.postRunnable(new C03972());
                            }
                        });
                        return;
                    }
                    MessagesController.this.gettingDifference = false;
                    MessagesController.this.loadCurrentState();
                    FileLog.m800e("tmessages", "get difference error, don't know what to do :(");
                }
            }, null, true, RPCRequest.RPCRequestClassGeneric);
        }
    }

    public void processUpdates(Updates updates, boolean fromQueue) {
        boolean needGetDiff = false;
        boolean needReceivedQueue = false;
        if (updates instanceof TL_updateShort) {
            ArrayList<Update> arr = new ArrayList();
            arr.add(updates.update);
            processUpdateArray(arr, null, null);
        } else if (updates instanceof TL_updateShortChatMessage) {
            missingData = this.chats.get(Integer.valueOf(updates.chat_id)) == null || this.users.get(Integer.valueOf(updates.from_id)) == null;
            if (MessagesStorage.lastSeqValue + 1 == updates.seq && !missingData) {
                message = new TL_message();
                message.from_id = updates.from_id;
                message.id = updates.id;
                message.to_id = new TL_peerChat();
                message.to_id.chat_id = updates.chat_id;
                message.message = updates.message;
                message.date = updates.date;
                message.unread = true;
                message.media = new TL_messageMediaEmpty();
                MessagesStorage.lastSeqValue = updates.seq;
                MessagesStorage.lastPtsValue = updates.pts;
                obj = new MessageObject(message, null);
                objArr = new ArrayList();
                objArr.add(obj);
                arr = new ArrayList();
                arr.add(message);
                printUpdate = updatePrintingUsersWithNewMessages((long) (-updates.chat_id), objArr);
                if (printUpdate) {
                    updatePrintingStrings();
                }
                r5 = updates;
                Utilities.RunOnUIThread(new Runnable() {
                    public void run() {
                        if (printUpdate) {
                            NotificationCenter.Instance.postNotificationName(3, Integer.valueOf(64));
                        }
                        if (obj.messageOwner.from_id != UserConfig.clientUserId) {
                            long dialog_id;
                            if (obj.messageOwner.to_id.chat_id != 0) {
                                dialog_id = (long) (-obj.messageOwner.to_id.chat_id);
                            } else {
                                dialog_id = (long) obj.messageOwner.to_id.user_id;
                            }
                            if (!(dialog_id == MessagesController.this.openned_dialog_id && ApplicationLoader.lastPauseTime == 0)) {
                                MessagesController.this.showInAppNotification(obj);
                            }
                        }
                        MessagesController.this.updateInterfaceWithMessages((long) (-r5.chat_id), objArr);
                        NotificationCenter.Instance.postNotificationName(4, new Object[0]);
                    }
                });
                MessagesStorage.Instance.putMessages(arr, false, true);
            } else if (!(missingData || MessagesStorage.lastSeqValue == updates.seq)) {
                FileLog.m800e("tmessages", "need get diff TL_updateShortChatMessage, seq: " + MessagesStorage.lastSeqValue + " " + updates.seq);
                if (this.gettingDifference || this.updatesStartWaitTime == 0 || (this.updatesStartWaitTime != 0 && this.updatesStartWaitTime + 1500 > System.currentTimeMillis())) {
                    if (this.updatesStartWaitTime == 0) {
                        this.updatesStartWaitTime = System.currentTimeMillis();
                    }
                    FileLog.m800e("tmessages", "add TL_updateShortChatMessage to queue");
                    this.updatesQueue.add(updates);
                } else {
                    needGetDiff = true;
                }
            }
        } else if (updates instanceof TL_updateShortMessage) {
            missingData = this.users.get(Integer.valueOf(updates.from_id)) == null;
            if (MessagesStorage.lastSeqValue + 1 == updates.seq && !missingData) {
                message = new TL_message();
                message.from_id = updates.from_id;
                message.id = updates.id;
                message.to_id = new TL_peerUser();
                message.to_id.user_id = updates.from_id;
                message.message = updates.message;
                message.date = updates.date;
                message.unread = true;
                message.media = new TL_messageMediaEmpty();
                MessagesStorage.lastSeqValue = updates.seq;
                MessagesStorage.lastPtsValue = updates.pts;
                MessagesStorage.lastDateValue = updates.date;
                obj = new MessageObject(message, null);
                objArr = new ArrayList();
                objArr.add(obj);
                arr = new ArrayList();
                arr.add(message);
                printUpdate = updatePrintingUsersWithNewMessages((long) updates.from_id, objArr);
                if (printUpdate) {
                    updatePrintingStrings();
                }
                r5 = updates;
                Utilities.RunOnUIThread(new Runnable() {
                    public void run() {
                        if (printUpdate) {
                            NotificationCenter.Instance.postNotificationName(3, Integer.valueOf(64));
                        }
                        if (obj.messageOwner.from_id != UserConfig.clientUserId) {
                            long dialog_id;
                            if (obj.messageOwner.to_id.chat_id != 0) {
                                dialog_id = (long) (-obj.messageOwner.to_id.chat_id);
                            } else {
                                dialog_id = (long) obj.messageOwner.to_id.user_id;
                            }
                            if (!(dialog_id == MessagesController.this.openned_dialog_id && ApplicationLoader.lastPauseTime == 0)) {
                                MessagesController.this.showInAppNotification(obj);
                            }
                        }
                        MessagesController.this.updateInterfaceWithMessages((long) r5.from_id, objArr);
                        NotificationCenter.Instance.postNotificationName(4, new Object[0]);
                    }
                });
                MessagesStorage.Instance.putMessages(arr, false, true);
            } else if (!(missingData || MessagesStorage.lastSeqValue == updates.seq)) {
                FileLog.m800e("tmessages", "need get diff TL_updateShortMessage, seq: " + MessagesStorage.lastSeqValue + " " + updates.seq);
                if (this.gettingDifference || this.updatesStartWaitTime == 0 || (this.updatesStartWaitTime != 0 && this.updatesStartWaitTime + 1500 > System.currentTimeMillis())) {
                    if (this.updatesStartWaitTime == 0) {
                        this.updatesStartWaitTime = System.currentTimeMillis();
                    }
                    FileLog.m800e("tmessages", "add TL_updateShortMessage to queue");
                    this.updatesQueue.add(updates);
                } else {
                    needGetDiff = true;
                }
            }
        } else if (updates instanceof TL_updatesCombined) {
            if (MessagesStorage.lastSeqValue + 1 == updates.seq_start || MessagesStorage.lastSeqValue == updates.seq_start) {
                MessagesStorage.Instance.putUsersAndChats(updates.users, updates.chats, true, true);
                lastPtsValue = MessagesStorage.lastPtsValue;
                lastQtsValue = MessagesStorage.lastQtsValue;
                if (processUpdateArray(updates.updates, updates.users, updates.chats)) {
                    MessagesStorage.lastDateValue = updates.date;
                    MessagesStorage.lastSeqValue = updates.seq;
                    if (MessagesStorage.lastQtsValue != lastQtsValue) {
                        needReceivedQueue = true;
                    }
                } else {
                    MessagesStorage.lastPtsValue = lastPtsValue;
                    MessagesStorage.lastQtsValue = lastQtsValue;
                    FileLog.m800e("tmessages", "need get diff inner TL_updatesCombined, seq: " + MessagesStorage.lastSeqValue + " " + updates.seq);
                    needGetDiff = true;
                }
            } else {
                FileLog.m800e("tmessages", "need get diff TL_updatesCombined, seq: " + MessagesStorage.lastSeqValue + " " + updates.seq_start);
                if (this.gettingDifference || this.updatesStartWaitTime == 0 || (this.updatesStartWaitTime != 0 && this.updatesStartWaitTime + 1500 > System.currentTimeMillis())) {
                    if (this.updatesStartWaitTime == 0) {
                        this.updatesStartWaitTime = System.currentTimeMillis();
                    }
                    FileLog.m800e("tmessages", "add TL_updatesCombined to queue");
                    this.updatesQueue.add(updates);
                } else {
                    needGetDiff = true;
                }
            }
        } else if (updates instanceof TL_updates) {
            if (MessagesStorage.lastSeqValue + 1 == updates.seq || updates.seq == 0 || updates.seq == MessagesStorage.lastSeqValue) {
                MessagesStorage.Instance.putUsersAndChats(updates.users, updates.chats, true, true);
                lastPtsValue = MessagesStorage.lastPtsValue;
                lastQtsValue = MessagesStorage.lastQtsValue;
                if (processUpdateArray(updates.updates, updates.users, updates.chats)) {
                    MessagesStorage.lastDateValue = updates.date;
                    if (updates.seq != 0) {
                        MessagesStorage.lastSeqValue = updates.seq;
                    }
                    if (MessagesStorage.lastQtsValue != lastQtsValue) {
                        needReceivedQueue = true;
                    }
                } else {
                    needGetDiff = true;
                    MessagesStorage.lastPtsValue = lastPtsValue;
                    MessagesStorage.lastQtsValue = lastQtsValue;
                    FileLog.m800e("tmessages", "need get diff inner TL_updates, seq: " + MessagesStorage.lastSeqValue + " " + updates.seq);
                }
            } else {
                FileLog.m800e("tmessages", "need get diff TL_updates, seq: " + MessagesStorage.lastSeqValue + " " + updates.seq);
                if (this.gettingDifference || this.updatesStartWaitTime == 0 || (this.updatesStartWaitTime != 0 && this.updatesStartWaitTime + 1500 > System.currentTimeMillis())) {
                    if (this.updatesStartWaitTime == 0) {
                        this.updatesStartWaitTime = System.currentTimeMillis();
                    }
                    FileLog.m800e("tmessages", "add TL_updates to queue");
                    this.updatesQueue.add(updates);
                } else {
                    needGetDiff = true;
                }
            }
        } else if (updates instanceof TL_updatesTooLong) {
            FileLog.m800e("tmessages", "need get diff TL_updatesTooLong");
            needGetDiff = true;
        } else if (updates instanceof UserActionUpdates) {
            MessagesStorage.lastSeqValue = updates.seq;
        }
        if (needGetDiff && !fromQueue) {
            getDifference();
        } else if (!(fromQueue || this.updatesQueue.isEmpty())) {
            processUpdatesQueue(false);
        }
        if (needReceivedQueue) {
            TL_messages_receivedQueue req = new TL_messages_receivedQueue();
            req.max_qts = MessagesStorage.lastQtsValue;
            ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
                public void run(TLObject response, TL_error error) {
                }
            }, null, true, RPCRequest.RPCRequestClassGeneric);
        }
        MessagesStorage.Instance.saveDiffParams(MessagesStorage.lastSeqValue, MessagesStorage.lastPtsValue, MessagesStorage.lastDateValue, MessagesStorage.lastQtsValue);
    }

    public boolean processUpdateArray(ArrayList<Update> updates, ArrayList<User> usersArr, ArrayList<Chat> chatsArr) {
        if (updates.isEmpty()) {
            return true;
        }
        AbstractMap usersDict;
        Iterator i$;
        ConcurrentHashMap<Integer, Chat> chatsDict;
        long currentTime = System.currentTimeMillis();
        final HashMap<Long, ArrayList<MessageObject>> messages = new HashMap();
        ArrayList<Message> messagesArr = new ArrayList();
        final ArrayList<Integer> markAsReadMessages = new ArrayList();
        final HashMap<Integer, Integer> markAsReadEncrypted = new HashMap();
        final ArrayList<Integer> deletedMessages = new ArrayList();
        final ArrayList<Long> printChanges = new ArrayList();
        final ArrayList<ChatParticipants> chatInfoToUpdate = new ArrayList();
        final ArrayList<Update> updatesOnMainThread = new ArrayList();
        ArrayList<TL_updateEncryptedMessagesRead> tasks = new ArrayList();
        MessageObject lastMessage = null;
        boolean usersAdded = false;
        boolean checkForUsers = true;
        if (usersArr != null) {
            usersDict = new ConcurrentHashMap();
            i$ = usersArr.iterator();
            while (i$.hasNext()) {
                User user = (User) i$.next();
                usersDict.put(Integer.valueOf(user.id), user);
            }
        } else {
            checkForUsers = false;
            usersDict = this.users;
        }
        if (chatsArr != null) {
            chatsDict = new ConcurrentHashMap();
            i$ = chatsArr.iterator();
            while (i$.hasNext()) {
                Chat chat = (Chat) i$.next();
                chatsDict.put(Integer.valueOf(chat.id), chat);
            }
        } else {
            checkForUsers = false;
            chatsDict = this.chats;
        }
        boolean reloadContacts = false;
        int interfaceUpdateMask = 0;
        i$ = updates.iterator();
        while (i$.hasNext()) {
            Update update = (Update) i$.next();
            MessageObject messageObject;
            long uid;
            ArrayList<MessageObject> arr;
            if (update instanceof TL_updateNewMessage) {
                TL_updateNewMessage upd = (TL_updateNewMessage) update;
                if (checkForUsers) {
                    if (usersDict.get(Integer.valueOf(upd.message.from_id)) != null || this.users.get(Integer.valueOf(upd.message.from_id)) != null) {
                        if (upd.message.to_id.chat_id != 0) {
                            if (chatsDict.get(Integer.valueOf(upd.message.to_id.chat_id)) == null && this.chats.get(Integer.valueOf(upd.message.to_id.chat_id)) == null) {
                            }
                        }
                    }
                    return false;
                }
                messagesArr.add(upd.message);
                messageObject = new MessageObject(upd.message, usersDict);
                if (messageObject.type == 11) {
                    interfaceUpdateMask |= 8;
                } else if (messageObject.type == 10) {
                    interfaceUpdateMask |= 16;
                }
                if (upd.message.to_id.chat_id != 0) {
                    uid = (long) (-upd.message.to_id.chat_id);
                } else {
                    if (upd.message.to_id.user_id == UserConfig.clientUserId) {
                        upd.message.to_id.user_id = upd.message.from_id;
                    }
                    uid = (long) upd.message.to_id.user_id;
                }
                arr = (ArrayList) messages.get(Long.valueOf(uid));
                if (arr == null) {
                    arr = new ArrayList();
                    messages.put(Long.valueOf(uid), arr);
                }
                arr.add(messageObject);
                MessagesStorage.lastPtsValue = update.pts;
                if (!(upd.message.from_id == UserConfig.clientUserId || upd.message.to_id == null)) {
                    if (uid != this.openned_dialog_id || ApplicationLoader.lastPauseTime != 0) {
                        lastMessage = messageObject;
                    }
                }
            } else if (!(update instanceof TL_updateMessageID)) {
                if (update instanceof TL_updateReadMessages) {
                    markAsReadMessages.addAll(update.messages);
                    MessagesStorage.lastPtsValue = update.pts;
                } else if (update instanceof TL_updateDeleteMessages) {
                    deletedMessages.addAll(update.messages);
                    MessagesStorage.lastPtsValue = update.pts;
                } else if (update instanceof TL_updateRestoreMessages) {
                    MessagesStorage.lastPtsValue = update.pts;
                } else if ((update instanceof TL_updateUserTyping) || (update instanceof TL_updateChatUserTyping)) {
                    if (update.user_id != UserConfig.clientUserId) {
                        uid = (long) (-update.chat_id);
                        if (uid == 0) {
                            uid = (long) update.user_id;
                        }
                        arr = (ArrayList) this.printingUsers.get(Long.valueOf(uid));
                        if (arr == null) {
                            arr = new ArrayList();
                            this.printingUsers.put(Long.valueOf(uid), arr);
                        }
                        exist = false;
                        i$ = arr.iterator();
                        while (i$.hasNext()) {
                            u = (PrintingUser) i$.next();
                            if (u.userId == update.user_id) {
                                exist = true;
                                u.lastTime = currentTime;
                                break;
                            }
                        }
                        if (!exist) {
                            newUser = new PrintingUser();
                            newUser.userId = update.user_id;
                            newUser.lastTime = currentTime;
                            arr.add(newUser);
                            if (!printChanges.contains(Long.valueOf(uid))) {
                                printChanges.add(Long.valueOf(uid));
                            }
                        }
                    }
                } else if (update instanceof TL_updateChatParticipants) {
                    interfaceUpdateMask |= 32;
                    chatInfoToUpdate.add(update.participants);
                } else if (update instanceof TL_updateUserStatus) {
                    interfaceUpdateMask |= 4;
                    updatesOnMainThread.add(update);
                } else if (update instanceof TL_updateUserName) {
                    interfaceUpdateMask |= 1;
                    updatesOnMainThread.add(update);
                } else if (update instanceof TL_updateUserPhoto) {
                    interfaceUpdateMask |= 2;
                    MessagesStorage.Instance.clearUserPhotos(update.user_id);
                    updatesOnMainThread.add(update);
                } else if (update instanceof TL_updateContactRegistered) {
                    if (usersDict.containsKey(Integer.valueOf(update.user_id))) {
                        newMessage = new TL_messageService();
                        newMessage.action = new TL_messageActionUserJoined();
                        r3 = UserConfig.getNewMessageId();
                        newMessage.id = r3;
                        newMessage.local_id = r3;
                        UserConfig.saveConfig(false);
                        newMessage.unread = true;
                        newMessage.date = update.date;
                        newMessage.from_id = update.user_id;
                        newMessage.to_id = new TL_peerUser();
                        newMessage.to_id.user_id = UserConfig.clientUserId;
                        newMessage.out = false;
                        newMessage.dialog_id = (long) update.user_id;
                        messagesArr.add(newMessage);
                        messageObject = new MessageObject(newMessage, usersDict);
                        arr = (ArrayList) messages.get(Long.valueOf(newMessage.dialog_id));
                        if (arr == null) {
                            arr = new ArrayList();
                            messages.put(Long.valueOf(newMessage.dialog_id), arr);
                        }
                        arr.add(messageObject);
                        if (!(newMessage.from_id == UserConfig.clientUserId || newMessage.to_id == null || (newMessage.dialog_id == this.openned_dialog_id && ApplicationLoader.lastPauseTime == 0))) {
                            lastMessage = messageObject;
                        }
                    }
                    reloadContacts = true;
                } else if (update instanceof TL_updateContactLink) {
                    reloadContacts = true;
                } else if (!(update instanceof TL_updateActivation)) {
                    if (update instanceof TL_updateNewAuthorization) {
                        newMessage = new TL_messageService();
                        newMessage.action = new TL_messageActionLoginUnknownLocation();
                        newMessage.action.title = update.device;
                        newMessage.action.address = update.location;
                        r3 = UserConfig.getNewMessageId();
                        newMessage.id = r3;
                        newMessage.local_id = r3;
                        UserConfig.saveConfig(false);
                        newMessage.unread = true;
                        newMessage.date = update.date;
                        newMessage.from_id = 333000;
                        newMessage.to_id = new TL_peerUser();
                        newMessage.to_id.user_id = UserConfig.clientUserId;
                        newMessage.out = false;
                        newMessage.dialog_id = 333000;
                        messagesArr.add(newMessage);
                        messageObject = new MessageObject(newMessage, usersDict);
                        arr = (ArrayList) messages.get(Long.valueOf(newMessage.dialog_id));
                        if (arr == null) {
                            arr = new ArrayList();
                            messages.put(Long.valueOf(newMessage.dialog_id), arr);
                        }
                        arr.add(messageObject);
                        if (!(newMessage.from_id == UserConfig.clientUserId || newMessage.to_id == null)) {
                            if (newMessage.dialog_id != this.openned_dialog_id || ApplicationLoader.lastPauseTime != 0) {
                                lastMessage = messageObject;
                            }
                        }
                    } else if (!(update instanceof TL_updateNewGeoChatMessage)) {
                        if (update instanceof TL_updateNewEncryptedMessage) {
                            MessagesStorage.lastQtsValue = update.qts;
                            Message message = decryptMessage(((TL_updateNewEncryptedMessage) update).message);
                            if (message != null) {
                                int cid = ((TL_updateNewEncryptedMessage) update).message.chat_id;
                                messagesArr.add(message);
                                messageObject = new MessageObject(message, usersDict);
                                uid = ((long) cid) << 32;
                                arr = (ArrayList) messages.get(Long.valueOf(uid));
                                if (arr == null) {
                                    arr = new ArrayList();
                                    messages.put(Long.valueOf(uid), arr);
                                }
                                arr.add(messageObject);
                                if (!(message.from_id == UserConfig.clientUserId || message.to_id == null)) {
                                    if (uid != this.openned_dialog_id || ApplicationLoader.lastPauseTime != 0) {
                                        lastMessage = messageObject;
                                    }
                                }
                            }
                        } else if (update instanceof TL_updateEncryptedChatTyping) {
                            uid = ((long) update.chat_id) << 32;
                            arr = (ArrayList) this.printingUsers.get(Long.valueOf(uid));
                            if (arr == null) {
                                arr = new ArrayList();
                                this.printingUsers.put(Long.valueOf(uid), arr);
                            }
                            exist = false;
                            i$ = arr.iterator();
                            while (i$.hasNext()) {
                                u = (PrintingUser) i$.next();
                                if (u.userId == update.user_id) {
                                    exist = true;
                                    u.lastTime = currentTime;
                                    break;
                                }
                            }
                            if (!exist) {
                                newUser = new PrintingUser();
                                newUser.userId = update.user_id;
                                newUser.lastTime = currentTime;
                                arr.add(newUser);
                                if (!printChanges.contains(Long.valueOf(uid))) {
                                    printChanges.add(Long.valueOf(uid));
                                }
                            }
                        } else if (update instanceof TL_updateEncryptedMessagesRead) {
                            markAsReadEncrypted.put(Integer.valueOf(update.chat_id), Integer.valueOf(Math.max(update.max_date, update.date)));
                            tasks.add((TL_updateEncryptedMessagesRead) update);
                        } else if (update instanceof TL_updateChatParticipantAdd) {
                            MessagesStorage.Instance.updateChatInfo(update.chat_id, update.user_id, false, update.inviter_id, update.version);
                        } else if (update instanceof TL_updateChatParticipantDelete) {
                            MessagesStorage.Instance.updateChatInfo(update.chat_id, update.user_id, true, 0, update.version);
                        } else if (update instanceof TL_updateDcOptions) {
                            ConnectionsManager.Instance.updateDcSettings();
                        } else if (update instanceof TL_updateEncryption) {
                            final EncryptedChat newChat = update.chat;
                            long dialog_id = ((long) newChat.id) << 32;
                            EncryptedChat existingChat = (EncryptedChat) this.encryptedChats.get(Integer.valueOf(newChat.id));
                            if (existingChat == null) {
                                Semaphore semaphore = new Semaphore(0);
                                ArrayList<TLObject> result = new ArrayList();
                                MessagesStorage.Instance.getEncryptedChat(newChat.id, semaphore, result);
                                try {
                                    semaphore.acquire();
                                } catch (Exception e) {
                                    FileLog.m799e("tmessages", e);
                                }
                                if (result.size() == 2) {
                                    existingChat = (EncryptedChat) result.get(0);
                                    user = (User) result.get(1);
                                    this.users.putIfAbsent(Integer.valueOf(user.id), user);
                                }
                            }
                            if ((newChat instanceof TL_encryptedChatRequested) && existingChat == null) {
                                int user_id = newChat.participant_id;
                                if (user_id == UserConfig.clientUserId) {
                                    user_id = newChat.admin_id;
                                }
                                user = (User) this.users.get(Integer.valueOf(user_id));
                                if (user == null) {
                                    user = (User) usersDict.get(Integer.valueOf(user_id));
                                }
                                newChat.user_id = user_id;
                                final TL_dialog dialog = new TL_dialog();
                                dialog.id = dialog_id;
                                dialog.unread_count = 0;
                                dialog.top_message = 0;
                                dialog.last_message_date = update.date;
                                usersAdded = true;
                                final ArrayList<User> arrayList = usersArr;
                                final ArrayList<Chat> arrayList2 = chatsArr;
                                Utilities.RunOnUIThread(new Runnable() {

                                    class C03991 implements Comparator<TL_dialog> {
                                        C03991() {
                                        }

                                        public int compare(TL_dialog tl_dialog, TL_dialog tl_dialog2) {
                                            if (tl_dialog.last_message_date == tl_dialog2.last_message_date) {
                                                return 0;
                                            }
                                            if (tl_dialog.last_message_date < tl_dialog2.last_message_date) {
                                                return 1;
                                            }
                                            return -1;
                                        }
                                    }

                                    public void run() {
                                        Iterator i$;
                                        if (arrayList != null) {
                                            i$ = arrayList.iterator();
                                            while (i$.hasNext()) {
                                                User user = (User) i$.next();
                                                MessagesController.this.users.put(Integer.valueOf(user.id), user);
                                                if (user.id == UserConfig.clientUserId) {
                                                    UserConfig.currentUser = user;
                                                }
                                            }
                                        }
                                        if (arrayList2 != null) {
                                            i$ = arrayList2.iterator();
                                            while (i$.hasNext()) {
                                                Chat chat = (Chat) i$.next();
                                                MessagesController.this.chats.put(Integer.valueOf(chat.id), chat);
                                            }
                                        }
                                        MessagesController.this.dialogs_dict.put(Long.valueOf(dialog.id), dialog);
                                        MessagesController.this.dialogs.add(dialog);
                                        MessagesController.this.dialogsServerOnly.clear();
                                        MessagesController.this.encryptedChats.put(Integer.valueOf(newChat.id), newChat);
                                        Collections.sort(MessagesController.this.dialogs, new C03991());
                                        i$ = MessagesController.this.dialogs.iterator();
                                        while (i$.hasNext()) {
                                            TL_dialog d = (TL_dialog) i$.next();
                                            if (((int) d.id) != 0) {
                                                MessagesController.this.dialogsServerOnly.add(d);
                                            }
                                        }
                                        NotificationCenter.Instance.postNotificationName(4, new Object[0]);
                                    }
                                });
                                MessagesStorage.Instance.putEncryptedChat(newChat, user, dialog);
                                acceptSecretChat(newChat);
                            } else if (!(newChat instanceof TL_encryptedChat)) {
                                final EncryptedChat encryptedChat = existingChat;
                                Utilities.RunOnUIThread(new Runnable() {
                                    public void run() {
                                        if (encryptedChat != null) {
                                            newChat.user_id = encryptedChat.user_id;
                                            newChat.auth_key = encryptedChat.auth_key;
                                            MessagesController.this.encryptedChats.put(Integer.valueOf(newChat.id), newChat);
                                        }
                                        MessagesStorage.Instance.updateEncryptedChat(newChat);
                                        NotificationCenter.Instance.postNotificationName(21, newChat);
                                    }
                                });
                            } else if (existingChat != null && (existingChat instanceof TL_encryptedChatWaiting)) {
                                if (existingChat.auth_key == null || existingChat.auth_key.length == 1) {
                                    newChat.a_or_b = existingChat.a_or_b;
                                    newChat.user_id = existingChat.user_id;
                                    processAcceptedSecretChat(newChat);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!messages.isEmpty()) {
            for (Entry<Long, ArrayList<MessageObject>> pair : messages.entrySet()) {
                Long key = (Long) pair.getKey();
                if (updatePrintingUsersWithNewMessages(key.longValue(), (ArrayList) pair.getValue()) && !printChanges.contains(key)) {
                    printChanges.add(key);
                }
            }
        }
        if (reloadContacts) {
            loadContacts(false);
        }
        if (!printChanges.isEmpty()) {
            updatePrintingStrings();
        }
        final MessageObject lastMessageArg = lastMessage;
        final int interfaceUpdateMaskFinal = interfaceUpdateMask;
        final boolean usersAddedConst = usersAdded;
        if (!(messages.isEmpty() && markAsReadMessages.isEmpty() && deletedMessages.isEmpty() && printChanges.isEmpty() && chatInfoToUpdate.isEmpty() && updatesOnMainThread.isEmpty() && markAsReadEncrypted.isEmpty())) {
            final ArrayList<User> arrayList3 = usersArr;
            final ArrayList<Chat> arrayList4 = chatsArr;
            Utilities.RunOnUIThread(new Runnable() {
                public void run() {
                    Iterator i$;
                    MessageObject obj;
                    int updateMask = interfaceUpdateMaskFinal;
                    if (!usersAddedConst) {
                        if (arrayList3 != null) {
                            i$ = arrayList3.iterator();
                            while (i$.hasNext()) {
                                User user = (User) i$.next();
                                MessagesController.this.users.put(Integer.valueOf(user.id), user);
                                if (user.id == UserConfig.clientUserId) {
                                    UserConfig.currentUser = user;
                                }
                            }
                        }
                        if (arrayList4 != null) {
                            i$ = arrayList4.iterator();
                            while (i$.hasNext()) {
                                Chat chat = (Chat) i$.next();
                                MessagesController.this.chats.put(Integer.valueOf(chat.id), chat);
                            }
                        }
                    }
                    if (!updatesOnMainThread.isEmpty()) {
                        ArrayList<User> dbUsers = new ArrayList();
                        ArrayList<User> dbUsersStatus = new ArrayList();
                        i$ = updatesOnMainThread.iterator();
                        while (i$.hasNext()) {
                            Update update = (Update) i$.next();
                            User toDbUser = new User();
                            toDbUser.id = update.user_id;
                            User currentUser = (User) MessagesController.this.users.get(Integer.valueOf(update.user_id));
                            if (update instanceof TL_updateUserStatus) {
                                if (!(update.status instanceof TL_userStatusEmpty)) {
                                    if (currentUser != null) {
                                        currentUser.id = update.user_id;
                                        currentUser.status = update.status;
                                        if (update.status instanceof TL_userStatusOnline) {
                                            currentUser.status.was_online = update.status.expires;
                                        } else if (update.status instanceof TL_userStatusOffline) {
                                            currentUser.status.expires = update.status.was_online;
                                        } else {
                                            currentUser.status.was_online = 0;
                                            currentUser.status.expires = 0;
                                        }
                                    }
                                    toDbUser.status = update.status;
                                    dbUsersStatus.add(toDbUser);
                                }
                            } else if (update instanceof TL_updateUserName) {
                                if (currentUser != null) {
                                    currentUser.first_name = update.first_name;
                                    currentUser.last_name = update.last_name;
                                }
                                toDbUser.first_name = update.first_name;
                                toDbUser.last_name = update.last_name;
                                dbUsers.add(toDbUser);
                            } else if (update instanceof TL_updateUserPhoto) {
                                if (currentUser != null) {
                                    currentUser.photo = update.photo;
                                }
                                toDbUser.photo = update.photo;
                                dbUsers.add(toDbUser);
                            }
                        }
                        MessagesStorage.Instance.updateUsers(dbUsersStatus, true, true, true);
                        MessagesStorage.Instance.updateUsers(dbUsers, false, true, true);
                    }
                    if (!messages.isEmpty()) {
                        for (Entry<Long, ArrayList<MessageObject>> entry : messages.entrySet()) {
                            Long key = (Long) entry.getKey();
                            ArrayList<MessageObject> value = (ArrayList) entry.getValue();
                            MessagesController.this.updateInterfaceWithMessages(key.longValue(), value);
                        }
                        NotificationCenter.Instance.postNotificationName(4, new Object[0]);
                    }
                    if (!markAsReadMessages.isEmpty()) {
                        i$ = markAsReadMessages.iterator();
                        while (i$.hasNext()) {
                            obj = (MessageObject) MessagesController.this.dialogMessage.get(((Integer) i$.next()).intValue());
                            if (obj != null) {
                                obj.messageOwner.unread = false;
                            }
                        }
                        NotificationCenter.Instance.postNotificationName(7, markAsReadMessages);
                    }
                    if (!markAsReadEncrypted.isEmpty()) {
                        for (Entry<Integer, Integer> entry2 : markAsReadEncrypted.entrySet()) {
                            NotificationCenter.Instance.postNotificationName(22, entry2.getKey(), entry2.getValue());
                            TL_dialog dialog = (TL_dialog) MessagesController.this.dialogs_dict.get(Long.valueOf(((long) ((Integer) entry2.getKey()).intValue()) << 32));
                            if (dialog != null) {
                                MessageObject message = (MessageObject) MessagesController.this.dialogMessage.get(dialog.top_message);
                                if (message != null && message.messageOwner.date <= ((Integer) entry2.getValue()).intValue()) {
                                    message.messageOwner.unread = false;
                                }
                            }
                        }
                    }
                    if (!deletedMessages.isEmpty()) {
                        NotificationCenter.Instance.postNotificationName(6, deletedMessages);
                        i$ = deletedMessages.iterator();
                        while (i$.hasNext()) {
                            obj = (MessageObject) MessagesController.this.dialogMessage.get(((Integer) i$.next()).intValue());
                            if (obj != null) {
                                obj.deleted = true;
                            }
                        }
                    }
                    if (!printChanges.isEmpty()) {
                        updateMask |= 64;
                    }
                    if (!chatInfoToUpdate.isEmpty()) {
                        i$ = chatInfoToUpdate.iterator();
                        while (i$.hasNext()) {
                            ChatParticipants info = (ChatParticipants) i$.next();
                            MessagesStorage.Instance.updateChatInfo(info.chat_id, info, true);
                            NotificationCenter.Instance.postNotificationName(17, Integer.valueOf(info.chat_id), info);
                        }
                    }
                    if (updateMask != 0) {
                        NotificationCenter.Instance.postNotificationName(3, Integer.valueOf(updateMask));
                    }
                    if (lastMessageArg != null) {
                        MessagesController.this.showInAppNotification(lastMessageArg);
                    }
                }
            });
        }
        if (!messagesArr.isEmpty()) {
            MessagesStorage.Instance.putMessages(messagesArr, true, true);
        }
        if (!(markAsReadMessages.isEmpty() && markAsReadEncrypted.isEmpty())) {
            MessagesStorage.Instance.markMessagesAsRead(markAsReadMessages, markAsReadEncrypted, true);
        }
        if (!deletedMessages.isEmpty()) {
            MessagesStorage.Instance.markMessagesAsDeleted(deletedMessages, true);
        }
        if (!deletedMessages.isEmpty()) {
            MessagesStorage.Instance.updateDialogsWithDeletedMessages(deletedMessages, true);
        }
        if (!markAsReadMessages.isEmpty()) {
            MessagesStorage.Instance.updateDialogsWithReadedMessages(markAsReadMessages, true);
        }
        if (!tasks.isEmpty()) {
            i$ = tasks.iterator();
            while (i$.hasNext()) {
                TL_updateEncryptedMessagesRead update2 = (TL_updateEncryptedMessagesRead) i$.next();
                MessagesStorage.Instance.createTaskForDate(update2.chat_id, update2.max_date, update2.date, 1);
            }
        }
        return true;
    }

    private boolean updatePrintingUsersWithNewMessages(long uid, ArrayList<MessageObject> messages) {
        if (uid > 0) {
            if (((ArrayList) this.printingUsers.get(Long.valueOf(uid))) != null) {
                this.printingUsers.remove(Long.valueOf(uid));
                return true;
            }
        } else if (uid < 0) {
            ArrayList<Integer> messagesUsers = new ArrayList();
            Iterator i$ = messages.iterator();
            while (i$.hasNext()) {
                MessageObject message = (MessageObject) i$.next();
                if (!messagesUsers.contains(Integer.valueOf(message.messageOwner.from_id))) {
                    messagesUsers.add(Integer.valueOf(message.messageOwner.from_id));
                }
            }
            ArrayList<PrintingUser> arr = (ArrayList) this.printingUsers.get(Long.valueOf(uid));
            boolean changed = false;
            if (arr != null) {
                int a = 0;
                while (a < arr.size()) {
                    if (messagesUsers.contains(Integer.valueOf(((PrintingUser) arr.get(a)).userId))) {
                        arr.remove(a);
                        a--;
                        if (arr.isEmpty()) {
                            this.printingUsers.remove(Long.valueOf(uid));
                        }
                        changed = true;
                    }
                    a++;
                }
            }
            if (changed) {
                return true;
            }
        }
        return false;
    }

    private void playNotificationSound() {
        if (this.lastSoundPlay <= System.currentTimeMillis() - 1800) {
            try {
                this.lastSoundPlay = System.currentTimeMillis();
                this.soundPool.play(this.sound, 1.0f, 1.0f, 1, 0, 1.0f);
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            }
        }
    }

    private void showInAppNotification(MessageObject messageObject) {
        if (UserConfig.clientActivated) {
            if (ApplicationLoader.lastPauseTime != 0) {
                ApplicationLoader.lastPauseTime = System.currentTimeMillis();
                FileLog.m800e("tmessages", "reset sleep timeout by recieved message");
            }
            if (messageObject != null) {
                SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
                boolean globalEnabled = preferences.getBoolean("EnableAll", true);
                if (!globalEnabled) {
                    return;
                }
                long dialog_id;
                int user_id;
                int chat_id;
                if (ApplicationLoader.lastPauseTime == 0) {
                    boolean inAppSounds = preferences.getBoolean("EnableInAppSounds", true);
                    boolean inAppVibrate = preferences.getBoolean("EnableInAppVibrate", true);
                    boolean inAppPreview = preferences.getBoolean("EnableInAppPreview", true);
                    if (inAppSounds || inAppVibrate || inAppPreview) {
                        dialog_id = messageObject.messageOwner.dialog_id;
                        user_id = messageObject.messageOwner.from_id;
                        chat_id = 0;
                        if (dialog_id == 0) {
                            if (messageObject.messageOwner.to_id.chat_id != 0) {
                                dialog_id = (long) (-messageObject.messageOwner.to_id.chat_id);
                                chat_id = messageObject.messageOwner.to_id.chat_id;
                            } else if (messageObject.messageOwner.to_id.user_id != 0) {
                                dialog_id = messageObject.messageOwner.to_id.user_id == UserConfig.clientUserId ? (long) messageObject.messageOwner.from_id : (long) messageObject.messageOwner.to_id.user_id;
                            }
                        } else if (((EncryptedChat) this.encryptedChats.get(Integer.valueOf((int) (dialog_id >> 32)))) == null) {
                            return;
                        }
                        if (dialog_id != 0 && ((User) this.users.get(Integer.valueOf(user_id))) != null) {
                            if ((chat_id == 0 || ((Chat) this.chats.get(Integer.valueOf(chat_id))) != null) && preferences.getBoolean("notify_" + dialog_id, true)) {
                                if (inAppPreview) {
                                    NotificationCenter.Instance.postNotificationName(701, messageObject);
                                }
                                if (inAppVibrate) {
                                    ((Vibrator) ApplicationLoader.applicationContext.getSystemService("vibrator")).vibrate(100);
                                }
                                if (inAppSounds) {
                                    playNotificationSound();
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                dialog_id = messageObject.messageOwner.dialog_id;
                chat_id = messageObject.messageOwner.to_id.chat_id;
                user_id = messageObject.messageOwner.to_id.user_id;
                if (user_id != 0 && user_id == UserConfig.clientUserId) {
                    user_id = messageObject.messageOwner.from_id;
                }
                if (dialog_id == 0) {
                    if (chat_id != 0) {
                        dialog_id = (long) (-chat_id);
                    } else if (user_id != 0) {
                        dialog_id = (long) user_id;
                    }
                }
                if (dialog_id == 0 || preferences.getBoolean("notify_" + dialog_id, true)) {
                    boolean groupEnabled = preferences.getBoolean("EnableGroup", true);
                    if (chat_id == 0 || globalEnabled) {
                        FileLocation photoPath = null;
                        boolean globalVibrate = preferences.getBoolean("EnableVibrateAll", true);
                        boolean groupVibrate = preferences.getBoolean("EnableVibrateGroup", true);
                        boolean groupPreview = preferences.getBoolean("EnablePreviewGroup", true);
                        boolean userPreview = preferences.getBoolean("EnablePreviewAll", true);
                        String defaultPath = null;
                        Uri defaultUri = System.DEFAULT_NOTIFICATION_URI;
                        if (defaultUri != null) {
                            defaultPath = defaultUri.getPath();
                        }
                        String globalSound = preferences.getString("GlobalSoundPath", defaultPath);
                        String chatSound = preferences.getString("GroupSoundPath", defaultPath);
                        String userSoundPath = null;
                        String chatSoundPath = null;
                        NotificationManager mNotificationManager = (NotificationManager) ApplicationLoader.applicationContext.getSystemService("notification");
                        Intent intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        String msg = null;
                        if (((int) dialog_id) != 0) {
                            if (chat_id != 0) {
                                intent.putExtra("chatId", chat_id);
                            }
                            if (user_id != 0) {
                                intent.putExtra("userId", user_id);
                            }
                            User u;
                            if (chat_id == 0 && user_id != 0) {
                                u = (User) this.users.get(Integer.valueOf(user_id));
                                if (u != null) {
                                    if (!(u.photo == null || u.photo.photo_small == null || u.photo.photo_small.volume_id == 0 || u.photo.photo_small.local_id == 0)) {
                                        photoPath = u.photo.photo_small;
                                    }
                                    if (!userPreview) {
                                        msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessageNoText, new Object[]{Utilities.formatName(u.first_name, u.last_name)});
                                    } else if (messageObject.messageOwner instanceof TL_messageService) {
                                        if (messageObject.messageOwner.action instanceof TL_messageActionUserJoined) {
                                            msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationContactJoined, new Object[]{Utilities.formatName(u.first_name, u.last_name)});
                                        } else if (messageObject.messageOwner.action instanceof TL_messageActionUserUpdatedPhoto) {
                                            msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationContactNewPhoto, new Object[]{Utilities.formatName(u.first_name, u.last_name)});
                                        } else if (messageObject.messageOwner.action instanceof TL_messageActionLoginUnknownLocation) {
                                            msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationUnrecognizedDevice, new Object[]{messageObject.messageOwner.action.title, messageObject.messageOwner.action.address});
                                        }
                                    } else if (messageObject.messageOwner.media instanceof TL_messageMediaEmpty) {
                                        msg = (messageObject.messageOwner.message == null || messageObject.messageOwner.message.length() == 0) ? ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessageNoText, new Object[]{Utilities.formatName(u.first_name, u.last_name)}) : ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessageText, new Object[]{Utilities.formatName(u.first_name, u.last_name), messageObject.messageOwner.message});
                                    } else if (messageObject.messageOwner.media instanceof TL_messageMediaPhoto) {
                                        msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessagePhoto, new Object[]{Utilities.formatName(u.first_name, u.last_name)});
                                    } else if (messageObject.messageOwner.media instanceof TL_messageMediaVideo) {
                                        msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessageVideo, new Object[]{Utilities.formatName(u.first_name, u.last_name)});
                                    } else if (messageObject.messageOwner.media instanceof TL_messageMediaContact) {
                                        msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessageContact, new Object[]{Utilities.formatName(u.first_name, u.last_name)});
                                    } else if (messageObject.messageOwner.media instanceof TL_messageMediaGeo) {
                                        msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessageMap, new Object[]{Utilities.formatName(u.first_name, u.last_name)});
                                    } else if (messageObject.messageOwner.media instanceof TL_messageMediaDocument) {
                                        msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessageDocument, new Object[]{Utilities.formatName(u.first_name, u.last_name)});
                                    } else if (messageObject.messageOwner.media instanceof TL_messageMediaAudio) {
                                        msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessageAudio, new Object[]{Utilities.formatName(u.first_name, u.last_name)});
                                    }
                                } else {
                                    return;
                                }
                            } else if (chat_id != 0 && user_id == 0) {
                                if (((Chat) this.chats.get(Integer.valueOf(chat_id))) != null) {
                                    u = (User) this.users.get(Integer.valueOf(messageObject.messageOwner.from_id));
                                    if (u != null) {
                                        if (!(u.photo == null || u.photo.photo_small == null || u.photo.photo_small.volume_id == 0 || u.photo.photo_small.local_id == 0)) {
                                            photoPath = u.photo.photo_small;
                                        }
                                        if (!groupPreview) {
                                            msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessageGroupNoText, new Object[]{Utilities.formatName(u.first_name, u.last_name), chat.title});
                                        } else if (messageObject.messageOwner instanceof TL_messageService) {
                                            if (messageObject.messageOwner.action instanceof TL_messageActionChatAddUser) {
                                                if (messageObject.messageOwner.action.user_id == UserConfig.clientUserId) {
                                                    msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationInvitedToGroup, new Object[]{Utilities.formatName(u.first_name, u.last_name), chat.title});
                                                } else {
                                                    if (((User) this.users.get(Integer.valueOf(messageObject.messageOwner.action.user_id))) != null) {
                                                        msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationGroupAddMember, new Object[]{Utilities.formatName(u.first_name, u.last_name), chat.title, Utilities.formatName(((User) this.users.get(Integer.valueOf(messageObject.messageOwner.action.user_id))).first_name, ((User) this.users.get(Integer.valueOf(messageObject.messageOwner.action.user_id))).last_name)});
                                                    } else {
                                                        return;
                                                    }
                                                }
                                            } else if (messageObject.messageOwner.action instanceof TL_messageActionChatEditTitle) {
                                                msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationEditedGroupName, new Object[]{Utilities.formatName(u.first_name, u.last_name), messageObject.messageOwner.action.title});
                                            } else if ((messageObject.messageOwner.action instanceof TL_messageActionChatEditPhoto) || (messageObject.messageOwner.action instanceof TL_messageActionChatDeletePhoto)) {
                                                msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationEditedGroupPhoto, new Object[]{Utilities.formatName(u.first_name, u.last_name), chat.title});
                                            } else if (messageObject.messageOwner.action instanceof TL_messageActionChatDeleteUser) {
                                                if (messageObject.messageOwner.action.user_id == UserConfig.clientUserId) {
                                                    msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationGroupKickYou, new Object[]{Utilities.formatName(u.first_name, u.last_name), chat.title});
                                                } else if (messageObject.messageOwner.action.user_id == u.id) {
                                                    msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationGroupLeftMember, new Object[]{Utilities.formatName(u.first_name, u.last_name), chat.title});
                                                } else {
                                                    if (((User) this.users.get(Integer.valueOf(messageObject.messageOwner.action.user_id))) != null) {
                                                        msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationGroupKickMember, new Object[]{Utilities.formatName(u.first_name, u.last_name), chat.title, Utilities.formatName(((User) this.users.get(Integer.valueOf(messageObject.messageOwner.action.user_id))).first_name, ((User) this.users.get(Integer.valueOf(messageObject.messageOwner.action.user_id))).last_name)});
                                                    } else {
                                                        return;
                                                    }
                                                }
                                            }
                                        } else if (messageObject.messageOwner.media instanceof TL_messageMediaEmpty) {
                                            msg = (messageObject.messageOwner.message == null || messageObject.messageOwner.message.length() == 0) ? ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessageGroupNoText, new Object[]{Utilities.formatName(u.first_name, u.last_name), chat.title}) : ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessageGroupText, new Object[]{Utilities.formatName(u.first_name, u.last_name), chat.title, messageObject.messageOwner.message});
                                        } else if (messageObject.messageOwner.media instanceof TL_messageMediaPhoto) {
                                            msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessageGroupPhoto, new Object[]{Utilities.formatName(u.first_name, u.last_name), chat.title});
                                        } else if (messageObject.messageOwner.media instanceof TL_messageMediaVideo) {
                                            msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessageGroupVideo, new Object[]{Utilities.formatName(u.first_name, u.last_name), chat.title});
                                        } else if (messageObject.messageOwner.media instanceof TL_messageMediaContact) {
                                            msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessageGroupContact, new Object[]{Utilities.formatName(u.first_name, u.last_name), chat.title});
                                        } else if (messageObject.messageOwner.media instanceof TL_messageMediaGeo) {
                                            msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessageGroupMap, new Object[]{Utilities.formatName(u.first_name, u.last_name), chat.title});
                                        } else if (messageObject.messageOwner.media instanceof TL_messageMediaDocument) {
                                            msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessageGroupDocument, new Object[]{Utilities.formatName(u.first_name, u.last_name), chat.title});
                                        } else if (messageObject.messageOwner.media instanceof TL_messageMediaAudio) {
                                            msg = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationMessageGroupAudio, new Object[]{Utilities.formatName(u.first_name, u.last_name), chat.title});
                                        }
                                    } else {
                                        return;
                                    }
                                }
                                return;
                            }
                        }
                        msg = ApplicationLoader.applicationContext.getString(C0419R.string.YouHaveNewMessage);
                        intent.putExtra("encId", (int) (dialog_id >> 32));
                        if (msg != null) {
                            boolean needVibrate = false;
                            if (user_id != 0) {
                                userSoundPath = preferences.getString("sound_path_" + user_id, null);
                                needVibrate = globalVibrate;
                            }
                            if (chat_id != 0) {
                                chatSoundPath = preferences.getString("sound_chat_path_" + chat_id, null);
                                needVibrate = groupVibrate;
                            }
                            String choosenSoundPath = null;
                            if (user_id != 0) {
                                if (userSoundPath != null) {
                                    choosenSoundPath = userSoundPath;
                                } else if (globalSound != null) {
                                    choosenSoundPath = globalSound;
                                }
                            } else if (chat_id == 0) {
                                choosenSoundPath = globalSound;
                            } else if (chatSoundPath != null) {
                                choosenSoundPath = chatSoundPath;
                            } else if (chatSound != null) {
                                choosenSoundPath = chatSound;
                            }
                            intent.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                            intent.setFlags(32768);
                            PendingIntent contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1073741824);
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(ApplicationLoader.applicationContext.getString(C0419R.string.AppName)).setSmallIcon(C0419R.drawable.notification).setStyle(new BigTextStyle().bigText(msg)).setContentText(msg).setAutoCancel(true).setTicker(msg);
                            if (photoPath != null) {
                                Bitmap img = FileLoader.Instance.getImageFromMemory(photoPath, null, null, "50_50", false);
                                if (img != null) {
                                    mBuilder.setLargeIcon(img);
                                }
                            }
                            if (needVibrate) {
                                mBuilder.setVibrate(new long[]{0, 100, 0, 100});
                            }
                            if (!(choosenSoundPath == null || choosenSoundPath.equals("NoSound"))) {
                                if (choosenSoundPath.equals(defaultPath)) {
                                    mBuilder.setSound(defaultUri);
                                } else {
                                    mBuilder.setSound(Uri.parse(choosenSoundPath));
                                }
                            }
                            mBuilder.setContentIntent(contentIntent);
                            mNotificationManager.cancel(1);
                            Notification notification = mBuilder.build();
                            notification.ledARGB = -16711936;
                            notification.ledOnMS = LocationStatusCodes.GEOFENCE_NOT_AVAILABLE;
                            notification.ledOffMS = LocationStatusCodes.GEOFENCE_NOT_AVAILABLE;
                            notification.flags |= 1;
                            try {
                                mNotificationManager.notify(1, notification);
                            } catch (Exception e) {
                                FileLog.m799e("tmessages", e);
                            }
                        }
                    }
                }
            }
        }
    }

    public void dialogsUnreadCountIncr(final HashMap<Long, Integer> values) {
        Utilities.RunOnUIThread(new Runnable() {
            public void run() {
                for (Entry<Long, Integer> entry : values.entrySet()) {
                    TL_dialog dialog = (TL_dialog) MessagesController.this.dialogs_dict.get(entry.getKey());
                    if (dialog != null) {
                        dialog.unread_count = ((Integer) entry.getValue()).intValue() + dialog.unread_count;
                    }
                }
                NotificationCenter.Instance.postNotificationName(4, new Object[0]);
            }
        });
    }

    private void updateInterfaceWithMessages(long uid, ArrayList<MessageObject> messages) {
        MessageObject lastMessage = null;
        int lastDate = 0;
        TL_dialog dialog = (TL_dialog) this.dialogs_dict.get(Long.valueOf(uid));
        NotificationCenter.Instance.postNotificationName(1, Long.valueOf(uid), messages);
        Iterator i$ = messages.iterator();
        while (i$.hasNext()) {
            MessageObject message = (MessageObject) i$.next();
            if (lastMessage == null || message.messageOwner.date > lastDate) {
                lastMessage = message;
                lastDate = message.messageOwner.date;
            }
        }
        if (dialog == null) {
            dialog = new TL_dialog();
            dialog.id = uid;
            dialog.unread_count = 0;
            dialog.top_message = lastMessage.messageOwner.id;
            dialog.last_message_date = lastMessage.messageOwner.date;
            this.dialogs_dict.put(Long.valueOf(uid), dialog);
            this.dialogs.add(dialog);
            this.dialogMessage.put(lastMessage.messageOwner.id, lastMessage);
        } else {
            this.dialogMessage.remove(dialog.top_message);
            dialog.top_message = lastMessage.messageOwner.id;
            dialog.last_message_date = lastMessage.messageOwner.date;
            this.dialogMessage.put(lastMessage.messageOwner.id, lastMessage);
        }
        this.dialogsServerOnly.clear();
        Collections.sort(this.dialogs, new Comparator<TL_dialog>() {
            public int compare(TL_dialog tl_dialog, TL_dialog tl_dialog2) {
                if (tl_dialog.last_message_date == tl_dialog2.last_message_date) {
                    return 0;
                }
                if (tl_dialog.last_message_date < tl_dialog2.last_message_date) {
                    return 1;
                }
                return -1;
            }
        });
        i$ = this.dialogs.iterator();
        while (i$.hasNext()) {
            TL_dialog d = (TL_dialog) i$.next();
            if (((int) d.id) != 0) {
                this.dialogsServerOnly.add(d);
            }
        }
    }

    public Message decryptMessage(EncryptedMessage message) {
        EncryptedChat chat = (EncryptedChat) this.encryptedChats.get(Integer.valueOf(message.chat_id));
        if (chat == null) {
            Semaphore semaphore = new Semaphore(0);
            ArrayList<TLObject> result = new ArrayList();
            MessagesStorage.Instance.getEncryptedChat(message.chat_id, semaphore, result);
            try {
                semaphore.acquire();
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            }
            if (result.size() == 2) {
                chat = (EncryptedChat) result.get(0);
                User user = (User) result.get(1);
                this.encryptedChats.put(Integer.valueOf(chat.id), chat);
                this.users.putIfAbsent(Integer.valueOf(user.id), user);
            }
        }
        if (chat == null) {
            return null;
        }
        SerializedData is = new SerializedData(message.bytes);
        if (chat.key_fingerprint == is.readInt64()) {
            MessageKeyData keyData = Utilities.generateMessageKeyData(chat.auth_key, is.readData(16), false);
            is = new SerializedData(Utilities.aesIgeEncryption(is.readData(message.bytes.length - 24), keyData.aesKey, keyData.aesIv, false, false));
            int len = is.readInt32();
            TLObject object = TLClassStore.Instance().TLdeserialize(is, is.readInt32());
            if (object != null) {
                int from_id = chat.admin_id;
                if (from_id == UserConfig.clientUserId) {
                    from_id = chat.participant_id;
                }
                Message newMessage;
                int newMessageId;
                if (object instanceof TL_decryptedMessage) {
                    TL_decryptedMessage decryptedMessage = (TL_decryptedMessage) object;
                    newMessage = new TL_message();
                    newMessage.message = decryptedMessage.message;
                    newMessage.date = message.date;
                    newMessageId = UserConfig.getNewMessageId();
                    newMessage.id = newMessageId;
                    newMessage.local_id = newMessageId;
                    UserConfig.saveConfig(false);
                    newMessage.from_id = from_id;
                    newMessage.to_id = new TL_peerUser();
                    newMessage.to_id.user_id = UserConfig.clientUserId;
                    newMessage.out = false;
                    newMessage.unread = true;
                    newMessage.dialog_id = ((long) chat.id) << 32;
                    newMessage.ttl = chat.ttl;
                    if (decryptedMessage.media instanceof TL_decryptedMessageMediaEmpty) {
                        newMessage.media = new TL_messageMediaEmpty();
                        return newMessage;
                    } else if (decryptedMessage.media instanceof TL_decryptedMessageMediaContact) {
                        newMessage.media = new TL_messageMediaContact();
                        newMessage.media.last_name = decryptedMessage.media.last_name;
                        newMessage.media.first_name = decryptedMessage.media.first_name;
                        newMessage.media.phone_number = decryptedMessage.media.phone_number;
                        newMessage.media.user_id = decryptedMessage.media.user_id;
                        return newMessage;
                    } else if (decryptedMessage.media instanceof TL_decryptedMessageMediaGeoPoint) {
                        newMessage.media = new TL_messageMediaGeo();
                        newMessage.media.geo = new TL_geoPoint();
                        newMessage.media.geo.lat = decryptedMessage.media.lat;
                        newMessage.media.geo._long = decryptedMessage.media._long;
                        return newMessage;
                    } else if (decryptedMessage.media instanceof TL_decryptedMessageMediaPhoto) {
                        if (decryptedMessage.media.key.length != 32 || decryptedMessage.media.iv.length != 32) {
                            return null;
                        }
                        newMessage.media = new TL_messageMediaPhoto();
                        newMessage.media.photo = new TL_photo();
                        newMessage.media.photo.user_id = newMessage.from_id;
                        newMessage.media.photo.date = newMessage.date;
                        newMessage.media.photo.caption = BuildConfig.FLAVOR;
                        newMessage.media.photo.geo = new TL_geoPointEmpty();
                        if (decryptedMessage.media.thumb.length != 0 && decryptedMessage.media.thumb.length <= 5000 && decryptedMessage.media.thumb_w < 100 && decryptedMessage.media.thumb_h < 100) {
                            TL_photoCachedSize small = new TL_photoCachedSize();
                            small.w = decryptedMessage.media.thumb_w;
                            small.h = decryptedMessage.media.thumb_h;
                            small.bytes = decryptedMessage.media.thumb;
                            small.type = "s";
                            small.location = new TL_fileLocationUnavailable();
                            newMessage.media.photo.sizes.add(small);
                        }
                        TL_photoSize big = new TL_photoSize();
                        big.w = decryptedMessage.media.f56w;
                        big.h = decryptedMessage.media.f55h;
                        big.type = "x";
                        big.size = message.file.size;
                        big.location = new TL_fileEncryptedLocation();
                        big.location.key = decryptedMessage.media.key;
                        big.location.iv = decryptedMessage.media.iv;
                        big.location.dc_id = message.file.dc_id;
                        big.location.volume_id = message.file.id;
                        big.location.secret = message.file.access_hash;
                        big.location.local_id = message.file.key_fingerprint;
                        newMessage.media.photo.sizes.add(big);
                        return newMessage;
                    } else if (decryptedMessage.media instanceof TL_decryptedMessageMediaVideo) {
                        if (decryptedMessage.media.key.length != 32 || decryptedMessage.media.iv.length != 32) {
                            return null;
                        }
                        newMessage.media = new TL_messageMediaVideo();
                        newMessage.media.video = new TL_videoEncrypted();
                        if (decryptedMessage.media.thumb.length == 0 || decryptedMessage.media.thumb.length > 5000 || decryptedMessage.media.thumb_w >= 100 || decryptedMessage.media.thumb_h >= 100) {
                            newMessage.media.video.thumb = new TL_photoSizeEmpty();
                            newMessage.media.video.thumb.type = "s";
                        } else {
                            newMessage.media.video.thumb = new TL_photoCachedSize();
                            newMessage.media.video.thumb.bytes = decryptedMessage.media.thumb;
                            newMessage.media.video.thumb.f60w = decryptedMessage.media.thumb_w;
                            newMessage.media.video.thumb.f59h = decryptedMessage.media.thumb_h;
                            newMessage.media.video.thumb.type = "s";
                            newMessage.media.video.thumb.location = new TL_fileLocationUnavailable();
                        }
                        newMessage.media.video.duration = decryptedMessage.media.duration;
                        newMessage.media.video.dc_id = message.file.dc_id;
                        newMessage.media.video.f70w = decryptedMessage.media.f56w;
                        newMessage.media.video.f69h = decryptedMessage.media.f55h;
                        newMessage.media.video.date = message.date;
                        newMessage.media.video.caption = BuildConfig.FLAVOR;
                        newMessage.media.video.user_id = from_id;
                        newMessage.media.video.size = message.file.size;
                        newMessage.media.video.id = message.file.id;
                        newMessage.media.video.access_hash = message.file.access_hash;
                        newMessage.media.video.key = decryptedMessage.media.key;
                        newMessage.media.video.iv = decryptedMessage.media.iv;
                        return newMessage;
                    } else if (!(decryptedMessage.media instanceof TL_decryptedMessageMediaDocument)) {
                        return null;
                    } else {
                        if (decryptedMessage.media.key.length != 32 || decryptedMessage.media.iv.length != 32) {
                            return null;
                        }
                        newMessage.media = new TL_messageMediaDocument();
                        newMessage.media.document = new TL_documentEncrypted();
                        newMessage.media.document.id = message.file.id;
                        newMessage.media.document.access_hash = message.file.access_hash;
                        newMessage.media.document.user_id = decryptedMessage.media.user_id;
                        newMessage.media.document.date = message.date;
                        newMessage.media.document.file_name = decryptedMessage.media.file_name;
                        newMessage.media.document.mime_type = decryptedMessage.media.mime_type;
                        newMessage.media.document.size = message.file.size;
                        newMessage.media.document.key = decryptedMessage.media.key;
                        newMessage.media.document.iv = decryptedMessage.media.iv;
                        if (decryptedMessage.media.thumb.length == 0 || decryptedMessage.media.thumb.length > 5000 || decryptedMessage.media.thumb_w >= 100 || decryptedMessage.media.thumb_h >= 100) {
                            newMessage.media.document.thumb = new TL_photoSizeEmpty();
                            newMessage.media.document.thumb.type = "s";
                        } else {
                            newMessage.media.document.thumb = new TL_photoCachedSize();
                            newMessage.media.document.thumb.bytes = decryptedMessage.media.thumb;
                            newMessage.media.document.thumb.f60w = decryptedMessage.media.thumb_w;
                            newMessage.media.document.thumb.f59h = decryptedMessage.media.thumb_h;
                            newMessage.media.document.thumb.type = "s";
                            newMessage.media.document.thumb.location = new TL_fileLocationUnavailable();
                        }
                        newMessage.media.document.dc_id = message.file.dc_id;
                        return newMessage;
                    }
                } else if (object instanceof TL_decryptedMessageService) {
                    TL_decryptedMessageService serviceMessage = (TL_decryptedMessageService) object;
                    if (serviceMessage.action instanceof TL_decryptedMessageActionSetMessageTTL) {
                        newMessage = new TL_messageService();
                        newMessage.action = new TL_messageActionTTLChange();
                        MessageAction messageAction = newMessage.action;
                        int i = serviceMessage.action.ttl_seconds;
                        chat.ttl = i;
                        messageAction.ttl = i;
                        newMessageId = UserConfig.getNewMessageId();
                        newMessage.id = newMessageId;
                        newMessage.local_id = newMessageId;
                        UserConfig.saveConfig(false);
                        newMessage.unread = true;
                        newMessage.date = message.date;
                        newMessage.from_id = from_id;
                        newMessage.to_id = new TL_peerUser();
                        newMessage.to_id.user_id = UserConfig.clientUserId;
                        newMessage.out = false;
                        newMessage.dialog_id = ((long) chat.id) << 32;
                        MessagesStorage.Instance.updateEncryptedChatTTL(chat);
                        return newMessage;
                    }
                }
            }
        }
        return null;
    }

    public void processAcceptedSecretChat(final EncryptedChat encryptedChat) {
        BigInteger p = new BigInteger(1, MessagesStorage.secretPBytes);
        BigInteger i_authKey = new BigInteger(1, encryptedChat.g_a_or_b);
        if (Utilities.isGoodGaAndGb(i_authKey, p)) {
            byte[] authKey = i_authKey.modPow(new BigInteger(1, encryptedChat.a_or_b), p).toByteArray();
            byte[] correctedAuth;
            if (authKey.length > 256) {
                correctedAuth = new byte[256];
                System.arraycopy(authKey, authKey.length - 256, correctedAuth, 0, 256);
                authKey = correctedAuth;
            } else if (authKey.length < 256) {
                correctedAuth = new byte[256];
                System.arraycopy(authKey, 0, correctedAuth, 256 - authKey.length, authKey.length);
                for (int a = 0; a < 256 - authKey.length; a++) {
                    authKey[a] = (byte) 0;
                }
                authKey = correctedAuth;
            }
            byte[] authKeyHash = Utilities.computeSHA1(authKey);
            byte[] authKeyId = new byte[8];
            System.arraycopy(authKeyHash, authKeyHash.length - 8, authKeyId, 0, 8);
            if (encryptedChat.key_fingerprint == Utilities.bytesToLong(authKeyId)) {
                encryptedChat.auth_key = authKey;
                MessagesStorage.Instance.updateEncryptedChat(encryptedChat);
                Utilities.RunOnUIThread(new Runnable() {
                    public void run() {
                        MessagesController.this.encryptedChats.put(Integer.valueOf(encryptedChat.id), encryptedChat);
                        NotificationCenter.Instance.postNotificationName(21, encryptedChat);
                    }
                });
                return;
            }
            final TL_encryptedChatDiscarded newChat = new TL_encryptedChatDiscarded();
            newChat.id = encryptedChat.id;
            newChat.user_id = encryptedChat.user_id;
            newChat.auth_key = encryptedChat.auth_key;
            MessagesStorage.Instance.updateEncryptedChat(newChat);
            Utilities.RunOnUIThread(new Runnable() {
                public void run() {
                    MessagesController.this.encryptedChats.put(Integer.valueOf(newChat.id), newChat);
                    NotificationCenter.Instance.postNotificationName(21, newChat);
                }
            });
            declineSecretChat(encryptedChat.id);
            return;
        }
        declineSecretChat(encryptedChat.id);
    }

    public void declineSecretChat(int chat_id) {
        TL_messages_discardEncryption req = new TL_messages_discardEncryption();
        req.chat_id = chat_id;
        ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
            public void run(TLObject response, TL_error error) {
            }
        }, null, true, RPCRequest.RPCRequestClassGeneric);
    }

    public void acceptSecretChat(final EncryptedChat encryptedChat) {
        if (this.acceptingChats.get(encryptedChat.id) == null) {
            this.acceptingChats.put(encryptedChat.id, encryptedChat);
            TL_messages_getDhConfig req = new TL_messages_getDhConfig();
            req.random_length = 256;
            req.version = MessagesStorage.lastSecretVersion;
            ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {

                class C08531 implements RPCRequestDelegate {
                    C08531() {
                    }

                    public void run(TLObject response, TL_error error) {
                        MessagesController.this.acceptingChats.remove(encryptedChat.id);
                        if (error == null) {
                            final EncryptedChat newChat = (EncryptedChat) response;
                            newChat.auth_key = encryptedChat.auth_key;
                            newChat.user_id = encryptedChat.user_id;
                            MessagesStorage.Instance.updateEncryptedChat(newChat);
                            Utilities.RunOnUIThread(new Runnable() {
                                public void run() {
                                    MessagesController.this.encryptedChats.put(Integer.valueOf(newChat.id), newChat);
                                    NotificationCenter.Instance.postNotificationName(21, newChat);
                                }
                            });
                        }
                    }
                }

                public void run(TLObject response, TL_error error) {
                    if (error == null) {
                        int a;
                        messages_DhConfig res = (messages_DhConfig) response;
                        if (response instanceof TL_messages_dhConfig) {
                            if (Utilities.isGoodPrime(res.f72p, res.f71g)) {
                                MessagesStorage.secretPBytes = res.f72p;
                                MessagesStorage.secretG = res.f71g;
                                MessagesStorage.lastSecretVersion = res.version;
                                MessagesStorage.Instance.saveSecretParams(MessagesStorage.lastSecretVersion, MessagesStorage.secretG, MessagesStorage.secretPBytes);
                            } else {
                                MessagesController.this.acceptingChats.remove(encryptedChat.id);
                                MessagesController.this.declineSecretChat(encryptedChat.id);
                                return;
                            }
                        }
                        byte[] salt = new byte[256];
                        for (a = 0; a < 256; a++) {
                            salt[a] = (byte) (((byte) ((int) (MessagesController.random.nextDouble() * 256.0d))) ^ res.random[a]);
                        }
                        encryptedChat.a_or_b = salt;
                        BigInteger p = new BigInteger(1, MessagesStorage.secretPBytes);
                        BigInteger g_b = BigInteger.valueOf((long) MessagesStorage.secretG).modPow(new BigInteger(1, salt), p);
                        BigInteger g_a = new BigInteger(1, encryptedChat.g_a);
                        if (Utilities.isGoodGaAndGb(g_a, p)) {
                            byte[] correctedAuth;
                            byte[] g_b_bytes = g_b.toByteArray();
                            if (g_b_bytes.length > 256) {
                                correctedAuth = new byte[256];
                                System.arraycopy(g_b_bytes, 1, correctedAuth, 0, 256);
                                g_b_bytes = correctedAuth;
                            }
                            byte[] authKey = g_a.modPow(new BigInteger(1, salt), p).toByteArray();
                            if (authKey.length > 256) {
                                correctedAuth = new byte[256];
                                System.arraycopy(authKey, authKey.length - 256, correctedAuth, 0, 256);
                                authKey = correctedAuth;
                            } else if (authKey.length < 256) {
                                correctedAuth = new byte[256];
                                System.arraycopy(authKey, 0, correctedAuth, 256 - authKey.length, authKey.length);
                                for (a = 0; a < 256 - authKey.length; a++) {
                                    authKey[a] = (byte) 0;
                                }
                                authKey = correctedAuth;
                            }
                            byte[] authKeyHash = Utilities.computeSHA1(authKey);
                            byte[] authKeyId = new byte[8];
                            System.arraycopy(authKeyHash, authKeyHash.length - 8, authKeyId, 0, 8);
                            encryptedChat.auth_key = authKey;
                            TL_messages_acceptEncryption req2 = new TL_messages_acceptEncryption();
                            req2.g_b = g_b_bytes;
                            req2.peer = new TL_inputEncryptedChat();
                            req2.peer.chat_id = encryptedChat.id;
                            req2.peer.access_hash = encryptedChat.access_hash;
                            req2.key_fingerprint = Utilities.bytesToLong(authKeyId);
                            ConnectionsManager.Instance.performRpc(req2, new C08531(), null, true, RPCRequest.RPCRequestClassGeneric);
                            return;
                        }
                        MessagesController.this.acceptingChats.remove(encryptedChat.id);
                        MessagesController.this.declineSecretChat(encryptedChat.id);
                        return;
                    }
                    MessagesController.this.acceptingChats.remove(encryptedChat.id);
                }
            }, null, true, RPCRequest.RPCRequestClassGeneric);
        }
    }

    public void startSecretChat(final Context context, final int user_id) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(C0419R.string.Loading));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        TL_messages_getDhConfig req = new TL_messages_getDhConfig();
        req.random_length = 256;
        req.version = MessagesStorage.lastSecretVersion;
        ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {

            class C04021 implements Runnable {
                C04021() {
                }

                public void run() {
                    if (!((ActionBarActivity) context).isFinishing()) {
                        progressDialog.dismiss();
                    }
                }
            }

            class C04063 implements Runnable {
                C04063() {
                }

                public void run() {
                    if (!((ActionBarActivity) context).isFinishing()) {
                        progressDialog.dismiss();
                    }
                }
            }

            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    InputUser inputUser;
                    messages_DhConfig res = (messages_DhConfig) response;
                    if (response instanceof TL_messages_dhConfig) {
                        if (Utilities.isGoodPrime(res.f72p, res.f71g)) {
                            MessagesStorage.secretPBytes = res.f72p;
                            MessagesStorage.secretG = res.f71g;
                            MessagesStorage.lastSecretVersion = res.version;
                            MessagesStorage.Instance.saveSecretParams(MessagesStorage.lastSecretVersion, MessagesStorage.secretG, MessagesStorage.secretPBytes);
                        } else {
                            Utilities.RunOnUIThread(new C04021());
                            return;
                        }
                    }
                    final byte[] salt = new byte[256];
                    for (int a = 0; a < 256; a++) {
                        salt[a] = (byte) (((byte) ((int) (MessagesController.random.nextDouble() * 256.0d))) ^ res.random[a]);
                    }
                    byte[] g_a = BigInteger.valueOf((long) MessagesStorage.secretG).modPow(new BigInteger(1, salt), new BigInteger(1, MessagesStorage.secretPBytes)).toByteArray();
                    if (g_a.length > 256) {
                        byte[] correctedAuth = new byte[256];
                        System.arraycopy(g_a, 1, correctedAuth, 0, 256);
                        g_a = correctedAuth;
                    }
                    final User user = (User) MessagesController.this.users.get(Integer.valueOf(user_id));
                    if ((user instanceof TL_userForeign) || (user instanceof TL_userRequest)) {
                        inputUser = new TL_inputUserForeign();
                        inputUser.user_id = user.id;
                        inputUser.access_hash = user.access_hash;
                    } else {
                        inputUser = new TL_inputUserContact();
                        inputUser.user_id = user.id;
                    }
                    TL_messages_requestEncryption req2 = new TL_messages_requestEncryption();
                    req2.g_a = g_a;
                    req2.user_id = inputUser;
                    req2.random_id = (int) (MessagesController.random.nextDouble() * 2.147483647E9d);
                    ConnectionsManager.Instance.performRpc(req2, new RPCRequestDelegate() {

                        class C04052 implements Runnable {
                            C04052() {
                            }

                            public void run() {
                                if (!((ActionBarActivity) context).isFinishing()) {
                                    progressDialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle(context.getString(C0419R.string.AppName));
                                    builder.setMessage(String.format(context.getString(C0419R.string.CreateEncryptedChatOutdatedError), new Object[]{user.first_name, user.first_name}));
                                    builder.setPositiveButton(ApplicationLoader.applicationContext.getString(C0419R.string.OK), null);
                                    builder.show().setCanceledOnTouchOutside(true);
                                }
                            }
                        }

                        public void run(final TLObject response, TL_error error) {
                            if (error == null) {
                                Utilities.RunOnUIThread(new Runnable() {

                                    class C04031 implements Comparator<TL_dialog> {
                                        C04031() {
                                        }

                                        public int compare(TL_dialog tl_dialog, TL_dialog tl_dialog2) {
                                            if (tl_dialog.last_message_date == tl_dialog2.last_message_date) {
                                                return 0;
                                            }
                                            if (tl_dialog.last_message_date < tl_dialog2.last_message_date) {
                                                return 1;
                                            }
                                            return -1;
                                        }
                                    }

                                    public void run() {
                                        if (!((ActionBarActivity) context).isFinishing()) {
                                            progressDialog.dismiss();
                                        }
                                        EncryptedChat chat = response;
                                        chat.user_id = chat.participant_id;
                                        MessagesController.this.encryptedChats.put(Integer.valueOf(chat.id), chat);
                                        chat.a_or_b = salt;
                                        TL_dialog dialog = new TL_dialog();
                                        dialog.id = ((long) chat.id) << 32;
                                        dialog.unread_count = 0;
                                        dialog.top_message = 0;
                                        dialog.last_message_date = ConnectionsManager.Instance.getCurrentTime();
                                        MessagesController.this.dialogs_dict.put(Long.valueOf(dialog.id), dialog);
                                        MessagesController.this.dialogs.add(dialog);
                                        MessagesController.this.dialogsServerOnly.clear();
                                        Collections.sort(MessagesController.this.dialogs, new C04031());
                                        Iterator i$ = MessagesController.this.dialogs.iterator();
                                        while (i$.hasNext()) {
                                            TL_dialog d = (TL_dialog) i$.next();
                                            if (((int) d.id) != 0) {
                                                MessagesController.this.dialogsServerOnly.add(d);
                                            }
                                        }
                                        NotificationCenter.Instance.postNotificationName(4, new Object[0]);
                                        MessagesStorage.Instance.putEncryptedChat(chat, user, dialog);
                                        NotificationCenter.Instance.postNotificationName(23, chat);
                                    }
                                });
                            } else {
                                Utilities.RunOnUIThread(new C04052());
                            }
                        }
                    }, null, true, RPCRequest.RPCRequestClassGeneric | RPCRequest.RPCRequestClassFailOnServerErrors);
                    return;
                }
                Utilities.RunOnUIThread(new C04063());
            }
        }, null, true, RPCRequest.RPCRequestClassGeneric | RPCRequest.RPCRequestClassFailOnServerErrors);
    }
}
