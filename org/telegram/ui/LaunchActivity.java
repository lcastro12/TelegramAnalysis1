package org.telegram.ui;

import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import org.telegram.TL.TLRPC.Chat;
import org.telegram.TL.TLRPC.EncryptedChat;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Views.PausableActivity;

public class LaunchActivity extends PausableActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getWindow().setBackgroundDrawableResource(C0419R.drawable.transparent);
            getSupportActionBar().hide();
            if (UserConfig.clientActivated) {
                Intent intent = getIntent();
                if (!(intent == null || intent.getAction() == null)) {
                    Cursor cursor;
                    if ("android.intent.action.SEND".equals(intent.getAction())) {
                        if (intent.getType() != null) {
                            Parcelable parcelable;
                            Object path;
                            if (intent.getType().startsWith("image/")) {
                                parcelable = intent.getParcelableExtra("android.intent.extra.STREAM");
                                if (parcelable instanceof Uri) {
                                    path = Utilities.getPath(this, (Uri) parcelable);
                                } else {
                                    path = intent.getParcelableExtra("android.intent.extra.STREAM").toString();
                                    if (path.startsWith("content:")) {
                                        cursor = getContentResolver().query(Uri.parse(path), new String[]{"_data"}, null, null, null);
                                        if (cursor != null) {
                                            cursor.moveToFirst();
                                            path = cursor.getString(0);
                                            cursor.close();
                                        }
                                    }
                                }
                                if (path != null) {
                                    if (path.startsWith("file:")) {
                                        path = path.replace("file://", BuildConfig.FLAVOR);
                                    }
                                    NotificationCenter.Instance.addToMemCache(533, path);
                                }
                            } else if (intent.getType().startsWith("video/")) {
                                parcelable = intent.getParcelableExtra("android.intent.extra.STREAM");
                                if (parcelable instanceof Uri) {
                                    path = Utilities.getPath(this, (Uri) parcelable);
                                } else {
                                    path = parcelable.toString();
                                    if (path.startsWith("content:")) {
                                        cursor = getContentResolver().query(Uri.parse(path), new String[]{"_data"}, null, null, null);
                                        if (cursor != null) {
                                            cursor.moveToFirst();
                                            path = cursor.getString(0);
                                            cursor.close();
                                        }
                                    }
                                }
                                if (path != null) {
                                    if (path.startsWith("file:")) {
                                        path = path.replace("file://", BuildConfig.FLAVOR);
                                    }
                                    NotificationCenter.Instance.addToMemCache(534, path);
                                }
                            } else if (intent.getType().equals("text/plain")) {
                                Object text = intent.getStringExtra("android.intent.extra.TEXT");
                                if (text.length() != 0) {
                                    NotificationCenter.Instance.addToMemCache(535, text);
                                }
                            }
                        }
                    } else if ("android.intent.action.VIEW".equals(intent.getAction())) {
                        cursor = getContentResolver().query(intent.getData(), null, null, null, null);
                        if (cursor.moveToFirst()) {
                            int userId = cursor.getInt(cursor.getColumnIndex("DATA4"));
                            NotificationCenter.Instance.postNotificationName(5, new Object[0]);
                            NotificationCenter.Instance.addToMemCache("push_user_id", Integer.valueOf(userId));
                        }
                    } else if (intent.getAction().equals("org.telegram.messenger.OPEN_ACCOUNT")) {
                        NotificationCenter.Instance.addToMemCache("open_settings", Integer.valueOf(1));
                    }
                }
                openNotificationChat();
                startActivity(new Intent(this, ApplicationActivity.class));
                finish();
            } else if ("android.intent.action.SEND".equals(getIntent().getAction())) {
                finish();
                return;
            } else {
                startActivity(new Intent(this, IntroActivity.class));
                finish();
            }
            getIntent().setAction(null);
            try {
                ((NotificationManager) getSystemService("notification")).cancel(1);
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            }
        }
    }

    private void openNotificationChat() {
        if ((getIntent().getFlags() & AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START) == 0) {
            int chatId = getIntent().getIntExtra("chatId", 0);
            int userId = getIntent().getIntExtra("userId", 0);
            int encId = getIntent().getIntExtra("encId", 0);
            if (chatId != 0) {
                if (((Chat) MessagesController.Instance.chats.get(Integer.valueOf(chatId))) != null) {
                    NotificationCenter.Instance.postNotificationName(5, new Object[0]);
                    NotificationCenter.Instance.addToMemCache("push_chat_id", Integer.valueOf(chatId));
                }
            } else if (userId != 0) {
                if (((User) MessagesController.Instance.users.get(Integer.valueOf(userId))) != null) {
                    NotificationCenter.Instance.postNotificationName(5, new Object[0]);
                    NotificationCenter.Instance.addToMemCache("push_user_id", Integer.valueOf(userId));
                }
            } else if (encId != 0 && ((EncryptedChat) MessagesController.Instance.encryptedChats.get(Integer.valueOf(encId))) != null) {
                NotificationCenter.Instance.postNotificationName(5, new Object[0]);
                NotificationCenter.Instance.addToMemCache("push_enc_id", Integer.valueOf(encId));
            }
        }
    }
}
