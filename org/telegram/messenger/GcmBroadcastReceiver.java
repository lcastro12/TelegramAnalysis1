package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import org.telegram.ui.ApplicationLoader;

public class GcmBroadcastReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION_ID = 1;

    public void onReceive(final Context context, final Intent intent) {
        FileLog.m798d("tmessages", "GCM received intent: " + intent);
        setResultCode(-1);
        if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
            final WakeLock wl = ((PowerManager) context.getSystemService("power")).newWakeLock(1, "lock");
            wl.acquire();
            if (context.getSharedPreferences("Notifications", 0).getBoolean("EnableAll", true)) {
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        String messageType = GoogleCloudMessaging.getInstance(context).getMessageType(intent);
                        ConnectionsManager.Instance.resumeNetworkMaybe();
                        wl.release();
                    }
                });
                thread.setPriority(10);
                thread.start();
                return;
            }
            FileLog.m798d("tmessages", "GCM disabled");
        } else if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
            String registration = intent.getStringExtra(ApplicationLoader.PROPERTY_REG_ID);
            if (intent.getStringExtra("error") != null) {
                FileLog.m800e("tmessages", "Registration failed, should try again later.");
            } else if (intent.getStringExtra("unregistered") != null) {
                FileLog.m800e("tmessages", "unregistration done, new messages from the authorized sender will be rejected");
            } else if (registration != null) {
                FileLog.m800e("tmessages", "registration id = " + registration);
            }
        }
    }
}
