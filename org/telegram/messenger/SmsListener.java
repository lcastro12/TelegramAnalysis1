package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsListener extends BroadcastReceiver {
    private SharedPreferences preferences;

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    SmsMessage[] msgs = new SmsMessage[pdus.length];
                    String wholeString = BuildConfig.FLAVOR;
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        wholeString = wholeString + msgs[i].getMessageBody();
                    }
                    try {
                        Matcher matcher = Pattern.compile("[0-9]+").matcher(wholeString);
                        if (matcher.find() && matcher.group(0).length() >= 3) {
                            NotificationCenter.Instance.postNotificationName(998, matcher.group(0));
                        }
                    } catch (Exception e) {
                        FileLog.m799e("tmessages", e);
                    }
                } catch (Exception e2) {
                    FileLog.m799e("tmessages", e2);
                }
            }
        }
    }
}
