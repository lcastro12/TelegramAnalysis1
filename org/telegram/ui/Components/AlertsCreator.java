package org.telegram.ui.Components;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C0488R;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.MessagesStorage.IntCallback;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.SecretChatHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.InputPeer;
import org.telegram.tgnet.TLRPC.TL_account_reportPeer;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputReportReasonPornography;
import org.telegram.tgnet.TLRPC.TL_inputReportReasonSpam;
import org.telegram.tgnet.TLRPC.TL_inputReportReasonViolence;
import org.telegram.tgnet.TLRPC.TL_messages_report;
import org.telegram.tgnet.TLRPC.TL_peerNotifySettings;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.CacheControlActivity;
import org.telegram.ui.Cells.RadioColorCell;
import org.telegram.ui.Cells.TextColorCell;
import org.telegram.ui.Components.NumberPicker.Formatter;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.ReportOtherActivity;

public class AlertsCreator {

    public interface PaymentAlertDelegate {
        void didPressedNewCard();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.app.Dialog processError(int r7, org.telegram.tgnet.TLRPC.TL_error r8, org.telegram.ui.ActionBar.BaseFragment r9, org.telegram.tgnet.TLObject r10, java.lang.Object... r11) {
        /*
        r6 = 2131493268; // 0x7f0c0194 float:1.8610011E38 double:1.053097598E-314;
        r5 = 2131493453; // 0x7f0c024d float:1.8610387E38 double:1.0530976895E-314;
        r2 = 1;
        r4 = 2131493543; // 0x7f0c02a7 float:1.861057E38 double:1.053097734E-314;
        r1 = 0;
        r0 = r8.code;
        r3 = 406; // 0x196 float:5.69E-43 double:2.006E-321;
        if (r0 == r3) goto L_0x0015;
    L_0x0011:
        r0 = r8.text;
        if (r0 != 0) goto L_0x0017;
    L_0x0015:
        r0 = 0;
    L_0x0016:
        return r0;
    L_0x0017:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_channels_joinChannel;
        if (r0 != 0) goto L_0x002f;
    L_0x001b:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_channels_editAdmin;
        if (r0 != 0) goto L_0x002f;
    L_0x001f:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_channels_inviteToChannel;
        if (r0 != 0) goto L_0x002f;
    L_0x0023:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_addChatUser;
        if (r0 != 0) goto L_0x002f;
    L_0x0027:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_startBot;
        if (r0 != 0) goto L_0x002f;
    L_0x002b:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_channels_editBanned;
        if (r0 == 0) goto L_0x005d;
    L_0x002f:
        if (r9 == 0) goto L_0x0040;
    L_0x0031:
        r2 = r8.text;
        r0 = r11[r1];
        r0 = (java.lang.Boolean) r0;
        r0 = r0.booleanValue();
        showAddUserAlert(r2, r9, r0);
    L_0x003e:
        r0 = 0;
        goto L_0x0016;
    L_0x0040:
        r0 = r8.text;
        r3 = "PEER_FLOOD";
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x003e;
    L_0x004b:
        r0 = org.telegram.messenger.NotificationCenter.getInstance(r7);
        r3 = org.telegram.messenger.NotificationCenter.needShowAlert;
        r4 = new java.lang.Object[r2];
        r2 = java.lang.Integer.valueOf(r2);
        r4[r1] = r2;
        r0.postNotificationName(r3, r4);
        goto L_0x003e;
    L_0x005d:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_createChat;
        if (r0 == 0) goto L_0x0078;
    L_0x0061:
        r0 = r8.text;
        r2 = "FLOOD_WAIT";
        r0 = r0.startsWith(r2);
        if (r0 == 0) goto L_0x0072;
    L_0x006c:
        r0 = r8.text;
        showFloodWaitAlert(r0, r9);
        goto L_0x003e;
    L_0x0072:
        r0 = r8.text;
        showAddUserAlert(r0, r9, r1);
        goto L_0x003e;
    L_0x0078:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_channels_createChannel;
        if (r0 == 0) goto L_0x0093;
    L_0x007c:
        r0 = r8.text;
        r2 = "FLOOD_WAIT";
        r0 = r0.startsWith(r2);
        if (r0 == 0) goto L_0x008d;
    L_0x0087:
        r0 = r8.text;
        showFloodWaitAlert(r0, r9);
        goto L_0x003e;
    L_0x008d:
        r0 = r8.text;
        showAddUserAlert(r0, r9, r1);
        goto L_0x003e;
    L_0x0093:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_editMessage;
        if (r0 == 0) goto L_0x00b0;
    L_0x0097:
        r0 = r8.text;
        r1 = "MESSAGE_NOT_MODIFIED";
        r0 = r0.equals(r1);
        if (r0 != 0) goto L_0x003e;
    L_0x00a2:
        r0 = "EditMessageError";
        r1 = 2131493415; // 0x7f0c0227 float:1.861031E38 double:1.0530976707E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x00b0:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_sendMessage;
        if (r0 != 0) goto L_0x00c4;
    L_0x00b4:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_sendMedia;
        if (r0 != 0) goto L_0x00c4;
    L_0x00b8:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_sendBroadcast;
        if (r0 != 0) goto L_0x00c4;
    L_0x00bc:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_sendInlineBotResult;
        if (r0 != 0) goto L_0x00c4;
    L_0x00c0:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_forwardMessages;
        if (r0 == 0) goto L_0x00e2;
    L_0x00c4:
        r0 = r8.text;
        r3 = "PEER_FLOOD";
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x003e;
    L_0x00cf:
        r0 = org.telegram.messenger.NotificationCenter.getInstance(r7);
        r3 = org.telegram.messenger.NotificationCenter.needShowAlert;
        r2 = new java.lang.Object[r2];
        r4 = java.lang.Integer.valueOf(r1);
        r2[r1] = r4;
        r0.postNotificationName(r3, r2);
        goto L_0x003e;
    L_0x00e2:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_importChatInvite;
        if (r0 == 0) goto L_0x0126;
    L_0x00e6:
        r0 = r8.text;
        r1 = "FLOOD_WAIT";
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x00fd;
    L_0x00f1:
        r0 = "FloodWait";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r4);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x00fd:
        r0 = r8.text;
        r1 = "USERS_TOO_MUCH";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0117;
    L_0x0108:
        r0 = "JoinToGroupErrorFull";
        r1 = 2131493711; // 0x7f0c034f float:1.861091E38 double:1.053097817E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x0117:
        r0 = "JoinToGroupErrorNotExist";
        r1 = 2131493712; // 0x7f0c0350 float:1.8610912E38 double:1.0530978174E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x0126:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_getAttachedStickers;
        if (r0 == 0) goto L_0x0160;
    L_0x012a:
        if (r9 == 0) goto L_0x003e;
    L_0x012c:
        r0 = r9.getParentActivity();
        if (r0 == 0) goto L_0x003e;
    L_0x0132:
        r0 = r9.getParentActivity();
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "ErrorOccurred";
        r3 = org.telegram.messenger.LocaleController.getString(r3, r5);
        r2 = r2.append(r3);
        r3 = "\n";
        r2 = r2.append(r3);
        r3 = r8.text;
        r2 = r2.append(r3);
        r2 = r2.toString();
        r0 = android.widget.Toast.makeText(r0, r2, r1);
        r0.show();
        goto L_0x003e;
    L_0x0160:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_account_confirmPhone;
        if (r0 == 0) goto L_0x01be;
    L_0x0164:
        r0 = r8.text;
        r1 = "PHONE_CODE_EMPTY";
        r0 = r0.contains(r1);
        if (r0 != 0) goto L_0x017a;
    L_0x016f:
        r0 = r8.text;
        r1 = "PHONE_CODE_INVALID";
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x0189;
    L_0x017a:
        r0 = "InvalidCode";
        r1 = 2131493679; // 0x7f0c032f float:1.8610845E38 double:1.053097801E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x0189:
        r0 = r8.text;
        r1 = "PHONE_CODE_EXPIRED";
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x01a0;
    L_0x0194:
        r0 = "CodeExpired";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r6);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x01a0:
        r0 = r8.text;
        r1 = "FLOOD_WAIT";
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x01b7;
    L_0x01ab:
        r0 = "FloodWait";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r4);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x01b7:
        r0 = r8.text;
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x01be:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_auth_resendCode;
        if (r0 == 0) goto L_0x025b;
    L_0x01c2:
        r0 = r8.text;
        r1 = "PHONE_NUMBER_INVALID";
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x01dc;
    L_0x01cd:
        r0 = "InvalidPhoneNumber";
        r1 = 2131493682; // 0x7f0c0332 float:1.8610851E38 double:1.0530978026E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x01dc:
        r0 = r8.text;
        r1 = "PHONE_CODE_EMPTY";
        r0 = r0.contains(r1);
        if (r0 != 0) goto L_0x01f2;
    L_0x01e7:
        r0 = r8.text;
        r1 = "PHONE_CODE_INVALID";
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x0201;
    L_0x01f2:
        r0 = "InvalidCode";
        r1 = 2131493679; // 0x7f0c032f float:1.8610845E38 double:1.053097801E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x0201:
        r0 = r8.text;
        r1 = "PHONE_CODE_EXPIRED";
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x0218;
    L_0x020c:
        r0 = "CodeExpired";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r6);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x0218:
        r0 = r8.text;
        r1 = "FLOOD_WAIT";
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x022f;
    L_0x0223:
        r0 = "FloodWait";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r4);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x022f:
        r0 = r8.code;
        r1 = -1000; // 0xfffffffffffffc18 float:NaN double:NaN;
        if (r0 == r1) goto L_0x003e;
    L_0x0235:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "ErrorOccurred";
        r1 = org.telegram.messenger.LocaleController.getString(r1, r5);
        r0 = r0.append(r1);
        r1 = "\n";
        r0 = r0.append(r1);
        r1 = r8.text;
        r0 = r0.append(r1);
        r0 = r0.toString();
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x025b:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_account_sendConfirmPhoneCode;
        if (r0 == 0) goto L_0x029e;
    L_0x025f:
        r0 = r8.code;
        r1 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        if (r0 != r1) goto L_0x0275;
    L_0x0265:
        r0 = "CancelLinkExpired";
        r1 = 2131493130; // 0x7f0c010a float:1.8609731E38 double:1.05309753E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r0 = showSimpleAlert(r9, r0);
        goto L_0x0016;
    L_0x0275:
        r0 = r8.text;
        if (r0 == 0) goto L_0x003e;
    L_0x0279:
        r0 = r8.text;
        r1 = "FLOOD_WAIT";
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x0291;
    L_0x0284:
        r0 = "FloodWait";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r4);
        r0 = showSimpleAlert(r9, r0);
        goto L_0x0016;
    L_0x0291:
        r0 = "ErrorOccurred";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r5);
        r0 = showSimpleAlert(r9, r0);
        goto L_0x0016;
    L_0x029e:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_account_changePhone;
        if (r0 == 0) goto L_0x0316;
    L_0x02a2:
        r0 = r8.text;
        r1 = "PHONE_NUMBER_INVALID";
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x02bc;
    L_0x02ad:
        r0 = "InvalidPhoneNumber";
        r1 = 2131493682; // 0x7f0c0332 float:1.8610851E38 double:1.0530978026E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x02bc:
        r0 = r8.text;
        r1 = "PHONE_CODE_EMPTY";
        r0 = r0.contains(r1);
        if (r0 != 0) goto L_0x02d2;
    L_0x02c7:
        r0 = r8.text;
        r1 = "PHONE_CODE_INVALID";
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x02e1;
    L_0x02d2:
        r0 = "InvalidCode";
        r1 = 2131493679; // 0x7f0c032f float:1.8610845E38 double:1.053097801E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x02e1:
        r0 = r8.text;
        r1 = "PHONE_CODE_EXPIRED";
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x02f8;
    L_0x02ec:
        r0 = "CodeExpired";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r6);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x02f8:
        r0 = r8.text;
        r1 = "FLOOD_WAIT";
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x030f;
    L_0x0303:
        r0 = "FloodWait";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r4);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x030f:
        r0 = r8.text;
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x0316:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_account_sendChangePhoneCode;
        if (r0 == 0) goto L_0x03b5;
    L_0x031a:
        r0 = r8.text;
        r3 = "PHONE_NUMBER_INVALID";
        r0 = r0.contains(r3);
        if (r0 == 0) goto L_0x0334;
    L_0x0325:
        r0 = "InvalidPhoneNumber";
        r1 = 2131493682; // 0x7f0c0332 float:1.8610851E38 double:1.0530978026E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x0334:
        r0 = r8.text;
        r3 = "PHONE_CODE_EMPTY";
        r0 = r0.contains(r3);
        if (r0 != 0) goto L_0x034a;
    L_0x033f:
        r0 = r8.text;
        r3 = "PHONE_CODE_INVALID";
        r0 = r0.contains(r3);
        if (r0 == 0) goto L_0x0359;
    L_0x034a:
        r0 = "InvalidCode";
        r1 = 2131493679; // 0x7f0c032f float:1.8610845E38 double:1.053097801E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x0359:
        r0 = r8.text;
        r3 = "PHONE_CODE_EXPIRED";
        r0 = r0.contains(r3);
        if (r0 == 0) goto L_0x0370;
    L_0x0364:
        r0 = "CodeExpired";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r6);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x0370:
        r0 = r8.text;
        r3 = "FLOOD_WAIT";
        r0 = r0.startsWith(r3);
        if (r0 == 0) goto L_0x0387;
    L_0x037b:
        r0 = "FloodWait";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r4);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x0387:
        r0 = r8.text;
        r3 = "PHONE_NUMBER_OCCUPIED";
        r0 = r0.startsWith(r3);
        if (r0 == 0) goto L_0x03a9;
    L_0x0392:
        r3 = "ChangePhoneNumberOccupied";
        r4 = 2131493141; // 0x7f0c0115 float:1.8609754E38 double:1.0530975353E-314;
        r2 = new java.lang.Object[r2];
        r0 = r11[r1];
        r0 = (java.lang.String) r0;
        r2[r1] = r0;
        r0 = org.telegram.messenger.LocaleController.formatString(r3, r4, r2);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x03a9:
        r0 = "ErrorOccurred";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r5);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x03b5:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_updateUserName;
        if (r0 == 0) goto L_0x0406;
    L_0x03b9:
        r3 = r8.text;
        r0 = -1;
        r4 = r3.hashCode();
        switch(r4) {
            case 288843630: goto L_0x03d3;
            case 533175271: goto L_0x03dd;
            default: goto L_0x03c3;
        };
    L_0x03c3:
        r1 = r0;
    L_0x03c4:
        switch(r1) {
            case 0: goto L_0x03e8;
            case 1: goto L_0x03f7;
            default: goto L_0x03c7;
        };
    L_0x03c7:
        r0 = "ErrorOccurred";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r5);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x03d3:
        r2 = "USERNAME_INVALID";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x03c3;
    L_0x03dc:
        goto L_0x03c4;
    L_0x03dd:
        r1 = "USERNAME_OCCUPIED";
        r1 = r3.equals(r1);
        if (r1 == 0) goto L_0x03c3;
    L_0x03e6:
        r1 = r2;
        goto L_0x03c4;
    L_0x03e8:
        r0 = "UsernameInvalid";
        r1 = 2131494563; // 0x7f0c06a3 float:1.8612638E38 double:1.053098238E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x03f7:
        r0 = "UsernameInUse";
        r1 = 2131494562; // 0x7f0c06a2 float:1.8612636E38 double:1.0530982374E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x0406:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_contacts_importContacts;
        if (r0 == 0) goto L_0x0449;
    L_0x040a:
        if (r8 == 0) goto L_0x0417;
    L_0x040c:
        r0 = r8.text;
        r1 = "FLOOD_WAIT";
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x0423;
    L_0x0417:
        r0 = "FloodWait";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r4);
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x0423:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "ErrorOccurred";
        r1 = org.telegram.messenger.LocaleController.getString(r1, r5);
        r0 = r0.append(r1);
        r1 = "\n";
        r0 = r0.append(r1);
        r1 = r8.text;
        r0 = r0.append(r1);
        r0 = r0.toString();
        showSimpleAlert(r9, r0);
        goto L_0x003e;
    L_0x0449:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_account_getPassword;
        if (r0 != 0) goto L_0x0451;
    L_0x044d:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_account_getTmpPassword;
        if (r0 == 0) goto L_0x046e;
    L_0x0451:
        r0 = r8.text;
        r1 = "FLOOD_WAIT";
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x0467;
    L_0x045c:
        r0 = r8.text;
        r0 = getFloodWaitString(r0);
        showSimpleToast(r9, r0);
        goto L_0x003e;
    L_0x0467:
        r0 = r8.text;
        showSimpleToast(r9, r0);
        goto L_0x003e;
    L_0x046e:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_payments_sendPaymentForm;
        if (r0 == 0) goto L_0x04ba;
    L_0x0472:
        r3 = r8.text;
        r0 = -1;
        r4 = r3.hashCode();
        switch(r4) {
            case -1144062453: goto L_0x0487;
            case -784238410: goto L_0x0491;
            default: goto L_0x047c;
        };
    L_0x047c:
        r1 = r0;
    L_0x047d:
        switch(r1) {
            case 0: goto L_0x049c;
            case 1: goto L_0x04ab;
            default: goto L_0x0480;
        };
    L_0x0480:
        r0 = r8.text;
        showSimpleToast(r9, r0);
        goto L_0x003e;
    L_0x0487:
        r2 = "BOT_PRECHECKOUT_FAILED";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x047c;
    L_0x0490:
        goto L_0x047d;
    L_0x0491:
        r1 = "PAYMENT_FAILED";
        r1 = r3.equals(r1);
        if (r1 == 0) goto L_0x047c;
    L_0x049a:
        r1 = r2;
        goto L_0x047d;
    L_0x049c:
        r0 = "PaymentPrecheckoutFailed";
        r1 = 2131494114; // 0x7f0c04e2 float:1.8611727E38 double:1.053098016E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleToast(r9, r0);
        goto L_0x003e;
    L_0x04ab:
        r0 = "PaymentFailed";
        r1 = 2131494101; // 0x7f0c04d5 float:1.86117E38 double:1.0530980096E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleToast(r9, r0);
        goto L_0x003e;
    L_0x04ba:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_payments_validateRequestedInfo;
        if (r0 == 0) goto L_0x003e;
    L_0x04be:
        r2 = r8.text;
        r0 = -1;
        r3 = r2.hashCode();
        switch(r3) {
            case 1758025548: goto L_0x04d2;
            default: goto L_0x04c8;
        };
    L_0x04c8:
        switch(r0) {
            case 0: goto L_0x04dd;
            default: goto L_0x04cb;
        };
    L_0x04cb:
        r0 = r8.text;
        showSimpleToast(r9, r0);
        goto L_0x003e;
    L_0x04d2:
        r3 = "SHIPPING_NOT_AVAILABLE";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x04c8;
    L_0x04db:
        r0 = r1;
        goto L_0x04c8;
    L_0x04dd:
        r0 = "PaymentNoShippingMethod";
        r1 = 2131494103; // 0x7f0c04d7 float:1.8611705E38 double:1.0530980106E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleToast(r9, r0);
        goto L_0x003e;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.AlertsCreator.processError(int, org.telegram.tgnet.TLRPC$TL_error, org.telegram.ui.ActionBar.BaseFragment, org.telegram.tgnet.TLObject, java.lang.Object[]):android.app.Dialog");
    }

    public static Toast showSimpleToast(BaseFragment baseFragment, String text) {
        if (text == null || baseFragment == null || baseFragment.getParentActivity() == null) {
            return null;
        }
        Toast toast = Toast.makeText(baseFragment.getParentActivity(), text, 1);
        toast.show();
        return toast;
    }

    public static Dialog showSimpleAlert(BaseFragment baseFragment, String text) {
        if (text == null || baseFragment == null || baseFragment.getParentActivity() == null) {
            return null;
        }
        Builder builder = new Builder(baseFragment.getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", C0488R.string.AppName));
        builder.setMessage(text);
        builder.setPositiveButton(LocaleController.getString("OK", C0488R.string.OK), null);
        Dialog dialog = builder.create();
        baseFragment.showDialog(dialog);
        return dialog;
    }

    public static Dialog createMuteAlert(Context context, final long dialog_id) {
        if (context == null) {
            return null;
        }
        BottomSheet.Builder builder = new BottomSheet.Builder(context);
        builder.setTitle(LocaleController.getString("Notifications", C0488R.string.Notifications));
        CharSequence[] items = new CharSequence[4];
        items[0] = LocaleController.formatString("MuteFor", C0488R.string.MuteFor, LocaleController.formatPluralString("Hours", 1));
        items[1] = LocaleController.formatString("MuteFor", C0488R.string.MuteFor, LocaleController.formatPluralString("Hours", 8));
        items[2] = LocaleController.formatString("MuteFor", C0488R.string.MuteFor, LocaleController.formatPluralString("Days", 2));
        items[3] = LocaleController.getString("MuteDisable", C0488R.string.MuteDisable);
        builder.setItems(items, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                long flags;
                int untilTime = ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime();
                if (i == 0) {
                    untilTime += 3600;
                } else if (i == 1) {
                    untilTime += 28800;
                } else if (i == 2) {
                    untilTime += 172800;
                } else if (i == 3) {
                    untilTime = ConnectionsManager.DEFAULT_DATACENTER_ID;
                }
                Editor editor = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
                if (i == 3) {
                    editor.putInt("notify2_" + dialog_id, 2);
                    flags = 1;
                } else {
                    editor.putInt("notify2_" + dialog_id, 3);
                    editor.putInt("notifyuntil_" + dialog_id, untilTime);
                    flags = (((long) untilTime) << 32) | 1;
                }
                NotificationsController.getInstance(UserConfig.selectedAccount).removeNotificationsForDialog(dialog_id);
                MessagesStorage.getInstance(UserConfig.selectedAccount).setDialogFlags(dialog_id, flags);
                editor.commit();
                TL_dialog dialog = (TL_dialog) MessagesController.getInstance(UserConfig.selectedAccount).dialogs_dict.get(dialog_id);
                if (dialog != null) {
                    dialog.notify_settings = new TL_peerNotifySettings();
                    dialog.notify_settings.mute_until = untilTime;
                }
                NotificationsController.getInstance(UserConfig.selectedAccount).updateServerNotificationsSettings(dialog_id);
            }
        });
        return builder.create();
    }

    public static Dialog createReportAlert(Context context, long dialog_id, int messageId, BaseFragment parentFragment) {
        if (context == null || parentFragment == null) {
            return null;
        }
        BottomSheet.Builder builder = new BottomSheet.Builder(context);
        builder.setTitle(LocaleController.getString("ReportChat", C0488R.string.ReportChat));
        final long j = dialog_id;
        final int i = messageId;
        final BaseFragment baseFragment = parentFragment;
        final Context context2 = context;
        builder.setItems(new CharSequence[]{LocaleController.getString("ReportChatSpam", C0488R.string.ReportChatSpam), LocaleController.getString("ReportChatViolence", C0488R.string.ReportChatViolence), LocaleController.getString("ReportChatPornography", C0488R.string.ReportChatPornography), LocaleController.getString("ReportChatOther", C0488R.string.ReportChatOther)}, new OnClickListener() {

            class C13561 implements RequestDelegate {
                C13561() {
                }

                public void run(TLObject response, TL_error error) {
                }
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 3) {
                    Bundle args = new Bundle();
                    args.putLong("dialog_id", j);
                    args.putLong("message_id", (long) i);
                    baseFragment.presentFragment(new ReportOtherActivity(args));
                    return;
                }
                TLObject req;
                InputPeer peer = MessagesController.getInstance(UserConfig.selectedAccount).getInputPeer((int) j);
                TLObject request;
                if (i != 0) {
                    request = new TL_messages_report();
                    request.peer = peer;
                    request.id.add(Integer.valueOf(i));
                    if (i == 0) {
                        request.reason = new TL_inputReportReasonSpam();
                    } else if (i == 1) {
                        request.reason = new TL_inputReportReasonViolence();
                    } else if (i == 2) {
                        request.reason = new TL_inputReportReasonPornography();
                    }
                    req = request;
                } else {
                    request = new TL_account_reportPeer();
                    request.peer = peer;
                    if (i == 0) {
                        request.reason = new TL_inputReportReasonSpam();
                    } else if (i == 1) {
                        request.reason = new TL_inputReportReasonViolence();
                    } else if (i == 2) {
                        request.reason = new TL_inputReportReasonPornography();
                    }
                    req = request;
                }
                ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(req, new C13561());
                Toast.makeText(context2, LocaleController.getString("ReportChatSent", C0488R.string.ReportChatSent), 0).show();
            }
        });
        return builder.create();
    }

    private static String getFloodWaitString(String error) {
        String timeString;
        int time = Utilities.parseInt(error).intValue();
        if (time < 60) {
            timeString = LocaleController.formatPluralString("Seconds", time);
        } else {
            timeString = LocaleController.formatPluralString("Minutes", time / 60);
        }
        return LocaleController.formatString("FloodWaitTime", C0488R.string.FloodWaitTime, timeString);
    }

    public static void showFloodWaitAlert(String error, BaseFragment fragment) {
        if (error != null && error.startsWith("FLOOD_WAIT") && fragment != null && fragment.getParentActivity() != null) {
            String timeString;
            int time = Utilities.parseInt(error).intValue();
            if (time < 60) {
                timeString = LocaleController.formatPluralString("Seconds", time);
            } else {
                timeString = LocaleController.formatPluralString("Minutes", time / 60);
            }
            Builder builder = new Builder(fragment.getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", C0488R.string.AppName));
            builder.setMessage(LocaleController.formatString("FloodWaitTime", C0488R.string.FloodWaitTime, timeString));
            builder.setPositiveButton(LocaleController.getString("OK", C0488R.string.OK), null);
            fragment.showDialog(builder.create(), true, null);
        }
    }

    public static void showSendMediaAlert(int result, BaseFragment fragment) {
        if (result != 0) {
            Builder builder = new Builder(fragment.getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", C0488R.string.AppName));
            if (result == 1) {
                builder.setMessage(LocaleController.getString("ErrorSendRestrictedStickers", C0488R.string.ErrorSendRestrictedStickers));
            } else if (result == 2) {
                builder.setMessage(LocaleController.getString("ErrorSendRestrictedMedia", C0488R.string.ErrorSendRestrictedMedia));
            }
            builder.setPositiveButton(LocaleController.getString("OK", C0488R.string.OK), null);
            fragment.showDialog(builder.create(), true, null);
        }
    }

    public static void showAddUserAlert(String error, final BaseFragment fragment, boolean isChannel) {
        if (error != null && fragment != null && fragment.getParentActivity() != null) {
            Builder builder = new Builder(fragment.getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", C0488R.string.AppName));
            boolean z = true;
            switch (error.hashCode()) {
                case -1763467626:
                    if (error.equals("USERS_TOO_FEW")) {
                        z = true;
                        break;
                    }
                    break;
                case -538116776:
                    if (error.equals("USER_BLOCKED")) {
                        z = true;
                        break;
                    }
                    break;
                case -512775857:
                    if (error.equals("USER_RESTRICTED")) {
                        z = true;
                        break;
                    }
                    break;
                case -454039871:
                    if (error.equals("PEER_FLOOD")) {
                        z = false;
                        break;
                    }
                    break;
                case -420079733:
                    if (error.equals("BOTS_TOO_MUCH")) {
                        z = true;
                        break;
                    }
                    break;
                case 98635865:
                    if (error.equals("USER_KICKED")) {
                        z = true;
                        break;
                    }
                    break;
                case 517420851:
                    if (error.equals("USER_BOT")) {
                        z = true;
                        break;
                    }
                    break;
                case 845559454:
                    if (error.equals("YOU_BLOCKED_USER")) {
                        z = true;
                        break;
                    }
                    break;
                case 916342611:
                    if (error.equals("USER_ADMIN_INVALID")) {
                        z = true;
                        break;
                    }
                    break;
                case 1047173446:
                    if (error.equals("CHAT_ADMIN_BAN_REQUIRED")) {
                        z = true;
                        break;
                    }
                    break;
                case 1167301807:
                    if (error.equals("USERS_TOO_MUCH")) {
                        z = true;
                        break;
                    }
                    break;
                case 1227003815:
                    if (error.equals("USER_ID_INVALID")) {
                        z = true;
                        break;
                    }
                    break;
                case 1253103379:
                    if (error.equals("ADMINS_TOO_MUCH")) {
                        z = true;
                        break;
                    }
                    break;
                case 1623167701:
                    if (error.equals("USER_NOT_MUTUAL_CONTACT")) {
                        z = true;
                        break;
                    }
                    break;
                case 1754587486:
                    if (error.equals("CHAT_ADMIN_INVITE_REQUIRED")) {
                        z = true;
                        break;
                    }
                    break;
                case 1916725894:
                    if (error.equals("USER_PRIVACY_RESTRICTED")) {
                        z = true;
                        break;
                    }
                    break;
            }
            switch (z) {
                case false:
                    builder.setMessage(LocaleController.getString("NobodyLikesSpam2", C0488R.string.NobodyLikesSpam2));
                    builder.setNegativeButton(LocaleController.getString("MoreInfo", C0488R.string.MoreInfo), new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MessagesController.getInstance(fragment.getCurrentAccount()).openByUserName("spambot", fragment, 1);
                        }
                    });
                    break;
                case true:
                case true:
                case true:
                    if (!isChannel) {
                        builder.setMessage(LocaleController.getString("GroupUserCantAdd", C0488R.string.GroupUserCantAdd));
                        break;
                    } else {
                        builder.setMessage(LocaleController.getString("ChannelUserCantAdd", C0488R.string.ChannelUserCantAdd));
                        break;
                    }
                case true:
                    if (!isChannel) {
                        builder.setMessage(LocaleController.getString("GroupUserAddLimit", C0488R.string.GroupUserAddLimit));
                        break;
                    } else {
                        builder.setMessage(LocaleController.getString("ChannelUserAddLimit", C0488R.string.ChannelUserAddLimit));
                        break;
                    }
                case true:
                    if (!isChannel) {
                        builder.setMessage(LocaleController.getString("GroupUserLeftError", C0488R.string.GroupUserLeftError));
                        break;
                    } else {
                        builder.setMessage(LocaleController.getString("ChannelUserLeftError", C0488R.string.ChannelUserLeftError));
                        break;
                    }
                case true:
                    if (!isChannel) {
                        builder.setMessage(LocaleController.getString("GroupUserCantAdmin", C0488R.string.GroupUserCantAdmin));
                        break;
                    } else {
                        builder.setMessage(LocaleController.getString("ChannelUserCantAdmin", C0488R.string.ChannelUserCantAdmin));
                        break;
                    }
                case true:
                    if (!isChannel) {
                        builder.setMessage(LocaleController.getString("GroupUserCantBot", C0488R.string.GroupUserCantBot));
                        break;
                    } else {
                        builder.setMessage(LocaleController.getString("ChannelUserCantBot", C0488R.string.ChannelUserCantBot));
                        break;
                    }
                case true:
                    if (!isChannel) {
                        builder.setMessage(LocaleController.getString("InviteToGroupError", C0488R.string.InviteToGroupError));
                        break;
                    } else {
                        builder.setMessage(LocaleController.getString("InviteToChannelError", C0488R.string.InviteToChannelError));
                        break;
                    }
                case true:
                    builder.setMessage(LocaleController.getString("CreateGroupError", C0488R.string.CreateGroupError));
                    break;
                case true:
                    builder.setMessage(LocaleController.getString("UserRestricted", C0488R.string.UserRestricted));
                    break;
                case true:
                    builder.setMessage(LocaleController.getString("YouBlockedUser", C0488R.string.YouBlockedUser));
                    break;
                case true:
                case true:
                    builder.setMessage(LocaleController.getString("AddAdminErrorBlacklisted", C0488R.string.AddAdminErrorBlacklisted));
                    break;
                case true:
                    builder.setMessage(LocaleController.getString("AddAdminErrorNotAMember", C0488R.string.AddAdminErrorNotAMember));
                    break;
                case true:
                    builder.setMessage(LocaleController.getString("AddBannedErrorAdmin", C0488R.string.AddBannedErrorAdmin));
                    break;
                default:
                    builder.setMessage(LocaleController.getString("ErrorOccurred", C0488R.string.ErrorOccurred) + "\n" + error);
                    break;
            }
            builder.setPositiveButton(LocaleController.getString("OK", C0488R.string.OK), null);
            fragment.showDialog(builder.create(), true, null);
        }
    }

    public static Dialog createColorSelectDialog(Activity parentActivity, long dialog_id, boolean globalGroup, boolean globalAll, Runnable onSelect) {
        int currentColor;
        SharedPreferences preferences = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        if (globalGroup) {
            currentColor = preferences.getInt("GroupLed", -16776961);
        } else if (globalAll) {
            currentColor = preferences.getInt("MessagesLed", -16776961);
        } else {
            if (preferences.contains("color_" + dialog_id)) {
                currentColor = preferences.getInt("color_" + dialog_id, -16776961);
            } else if (((int) dialog_id) < 0) {
                currentColor = preferences.getInt("GroupLed", -16776961);
            } else {
                currentColor = preferences.getInt("MessagesLed", -16776961);
            }
        }
        View linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        String[] descriptions = new String[]{LocaleController.getString("ColorRed", C0488R.string.ColorRed), LocaleController.getString("ColorOrange", C0488R.string.ColorOrange), LocaleController.getString("ColorYellow", C0488R.string.ColorYellow), LocaleController.getString("ColorGreen", C0488R.string.ColorGreen), LocaleController.getString("ColorCyan", C0488R.string.ColorCyan), LocaleController.getString("ColorBlue", C0488R.string.ColorBlue), LocaleController.getString("ColorViolet", C0488R.string.ColorViolet), LocaleController.getString("ColorPink", C0488R.string.ColorPink), LocaleController.getString("ColorWhite", C0488R.string.ColorWhite)};
        final int[] selectedColor = new int[]{currentColor};
        for (int a = 0; a < 9; a++) {
            RadioColorCell cell = new RadioColorCell(parentActivity);
            cell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            cell.setTag(Integer.valueOf(a));
            cell.setCheckColor(TextColorCell.colors[a], TextColorCell.colors[a]);
            cell.setTextAndValue(descriptions[a], currentColor == TextColorCell.colorsToSave[a]);
            linearLayout.addView(cell);
            linearLayout = linearLayout;
            cell.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int count = linearLayout.getChildCount();
                    for (int a = 0; a < count; a++) {
                        boolean z;
                        View cell = (RadioColorCell) linearLayout.getChildAt(a);
                        if (cell == v) {
                            z = true;
                        } else {
                            z = false;
                        }
                        cell.setChecked(z, true);
                    }
                    selectedColor[0] = TextColorCell.colorsToSave[((Integer) v.getTag()).intValue()];
                }
            });
        }
        Builder builder = new Builder((Context) parentActivity);
        builder.setTitle(LocaleController.getString("LedColor", C0488R.string.LedColor));
        builder.setView(linearLayout);
        final boolean z = globalAll;
        final boolean z2 = globalGroup;
        final long j = dialog_id;
        final Runnable runnable = onSelect;
        builder.setPositiveButton(LocaleController.getString("Set", C0488R.string.Set), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int which) {
                Editor editor = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
                if (z) {
                    editor.putInt("MessagesLed", selectedColor[0]);
                } else if (z2) {
                    editor.putInt("GroupLed", selectedColor[0]);
                } else {
                    editor.putInt("color_" + j, selectedColor[0]);
                }
                editor.commit();
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        final boolean z3 = globalAll;
        final boolean z4 = globalGroup;
        final long j2 = dialog_id;
        final Runnable runnable2 = onSelect;
        builder.setNeutralButton(LocaleController.getString("LedDisabled", C0488R.string.LedDisabled), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Editor editor = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
                if (z3) {
                    editor.putInt("MessagesLed", 0);
                } else if (z4) {
                    editor.putInt("GroupLed", 0);
                } else {
                    editor.putInt("color_" + j2, 0);
                }
                editor.commit();
                if (runnable2 != null) {
                    runnable2.run();
                }
            }
        });
        if (!(globalAll || globalGroup)) {
            final long j3 = dialog_id;
            final Runnable runnable3 = onSelect;
            builder.setNegativeButton(LocaleController.getString("Default", C0488R.string.Default), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Editor editor = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
                    editor.remove("color_" + j3);
                    editor.commit();
                    if (runnable3 != null) {
                        runnable3.run();
                    }
                }
            });
        }
        return builder.create();
    }

    public static Dialog createVibrationSelectDialog(Activity parentActivity, BaseFragment parentFragment, long dialog_id, boolean globalGroup, boolean globalAll, Runnable onSelect) {
        String prefix;
        if (dialog_id != 0) {
            prefix = "vibrate_";
        } else {
            prefix = globalGroup ? "vibrate_group" : "vibrate_messages";
        }
        return createVibrationSelectDialog(parentActivity, parentFragment, dialog_id, prefix, onSelect);
    }

    public static Dialog createVibrationSelectDialog(Activity parentActivity, BaseFragment parentFragment, long dialog_id, String prefKeyPrefix, Runnable onSelect) {
        String[] descriptions;
        SharedPreferences preferences = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        final int[] selected = new int[1];
        if (dialog_id != 0) {
            selected[0] = preferences.getInt(prefKeyPrefix + dialog_id, 0);
            if (selected[0] == 3) {
                selected[0] = 2;
            } else if (selected[0] == 2) {
                selected[0] = 3;
            }
            descriptions = new String[]{LocaleController.getString("VibrationDefault", C0488R.string.VibrationDefault), LocaleController.getString("Short", C0488R.string.Short), LocaleController.getString("Long", C0488R.string.Long), LocaleController.getString("VibrationDisabled", C0488R.string.VibrationDisabled)};
        } else {
            selected[0] = preferences.getInt(prefKeyPrefix, 0);
            if (selected[0] == 0) {
                selected[0] = 1;
            } else if (selected[0] == 1) {
                selected[0] = 2;
            } else if (selected[0] == 2) {
                selected[0] = 0;
            }
            descriptions = new String[]{LocaleController.getString("VibrationDisabled", C0488R.string.VibrationDisabled), LocaleController.getString("VibrationDefault", C0488R.string.VibrationDefault), LocaleController.getString("Short", C0488R.string.Short), LocaleController.getString("Long", C0488R.string.Long), LocaleController.getString("OnlyIfSilent", C0488R.string.OnlyIfSilent)};
        }
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        int a = 0;
        while (a < descriptions.length) {
            RadioColorCell cell = new RadioColorCell(parentActivity);
            cell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            cell.setTag(Integer.valueOf(a));
            cell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
            cell.setTextAndValue(descriptions[a], selected[0] == a);
            linearLayout.addView(cell);
            final long j = dialog_id;
            final String str = prefKeyPrefix;
            final BaseFragment baseFragment = parentFragment;
            final Runnable runnable = onSelect;
            cell.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    selected[0] = ((Integer) v.getTag()).intValue();
                    Editor editor = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
                    if (j != 0) {
                        if (selected[0] == 0) {
                            editor.putInt(str + j, 0);
                        } else if (selected[0] == 1) {
                            editor.putInt(str + j, 1);
                        } else if (selected[0] == 2) {
                            editor.putInt(str + j, 3);
                        } else if (selected[0] == 3) {
                            editor.putInt(str + j, 2);
                        }
                    } else if (selected[0] == 0) {
                        editor.putInt(str, 2);
                    } else if (selected[0] == 1) {
                        editor.putInt(str, 0);
                    } else if (selected[0] == 2) {
                        editor.putInt(str, 1);
                    } else if (selected[0] == 3) {
                        editor.putInt(str, 3);
                    } else if (selected[0] == 4) {
                        editor.putInt(str, 4);
                    }
                    editor.commit();
                    if (baseFragment != null) {
                        baseFragment.dismissCurrentDialig();
                    }
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
            a++;
        }
        Builder builder = new Builder((Context) parentActivity);
        builder.setTitle(LocaleController.getString("Vibrate", C0488R.string.Vibrate));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("Cancel", C0488R.string.Cancel), null);
        return builder.create();
    }

    public static Dialog createLocationUpdateDialog(Activity parentActivity, User user, IntCallback callback) {
        final int[] selected = new int[1];
        String[] descriptions = new String[]{LocaleController.getString("SendLiveLocationFor15m", C0488R.string.SendLiveLocationFor15m), LocaleController.getString("SendLiveLocationFor1h", C0488R.string.SendLiveLocationFor1h), LocaleController.getString("SendLiveLocationFor8h", C0488R.string.SendLiveLocationFor8h)};
        final LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        TextView titleTextView = new TextView(parentActivity);
        if (user != null) {
            titleTextView.setText(LocaleController.formatString("LiveLocationAlertPrivate", C0488R.string.LiveLocationAlertPrivate, UserObject.getFirstName(user)));
        } else {
            titleTextView.setText(LocaleController.getString("LiveLocationAlertGroup", C0488R.string.LiveLocationAlertGroup));
        }
        titleTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        titleTextView.setTextSize(1, 16.0f);
        titleTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        linearLayout.addView(titleTextView, LayoutHelper.createLinear(-2, -2, (LocaleController.isRTL ? 5 : 3) | 48, 24, 0, 24, 8));
        int a = 0;
        while (a < descriptions.length) {
            RadioColorCell cell = new RadioColorCell(parentActivity);
            cell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            cell.setTag(Integer.valueOf(a));
            cell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
            cell.setTextAndValue(descriptions[a], selected[0] == a);
            linearLayout.addView(cell);
            cell.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    selected[0] = ((Integer) v.getTag()).intValue();
                    int count = linearLayout.getChildCount();
                    for (int a = 0; a < count; a++) {
                        View child = linearLayout.getChildAt(a);
                        if (child instanceof RadioColorCell) {
                            boolean z;
                            RadioColorCell radioColorCell = (RadioColorCell) child;
                            if (child == v) {
                                z = true;
                            } else {
                                z = false;
                            }
                            radioColorCell.setChecked(z, true);
                        }
                    }
                }
            });
            a++;
        }
        Builder builder = new Builder((Context) parentActivity);
        builder.setTopImage(new ShareLocationDrawable(parentActivity, false), Theme.getColor(Theme.key_dialogTopBackground));
        builder.setView(linearLayout);
        final IntCallback intCallback = callback;
        builder.setPositiveButton(LocaleController.getString("ShareFile", C0488R.string.ShareFile), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int time;
                if (selected[0] == 0) {
                    time = 900;
                } else if (selected[0] == 1) {
                    time = 3600;
                } else {
                    time = 28800;
                }
                intCallback.run(time);
            }
        });
        builder.setNeutralButton(LocaleController.getString("Cancel", C0488R.string.Cancel), null);
        return builder.create();
    }

    public static Dialog createFreeSpaceDialog(LaunchActivity parentActivity) {
        final int[] selected = new int[1];
        int keepMedia = MessagesController.getGlobalMainSettings().getInt("keep_media", 2);
        if (keepMedia == 2) {
            selected[0] = 3;
        } else if (keepMedia == 0) {
            selected[0] = 1;
        } else if (keepMedia == 1) {
            selected[0] = 2;
        } else if (keepMedia == 3) {
            selected[0] = 0;
        }
        String[] descriptions = new String[]{LocaleController.formatPluralString("Days", 3), LocaleController.formatPluralString("Weeks", 1), LocaleController.formatPluralString("Months", 1), LocaleController.getString("LowDiskSpaceNeverRemove", C0488R.string.LowDiskSpaceNeverRemove)};
        final LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        View titleTextView = new TextView(parentActivity);
        titleTextView.setText(LocaleController.getString("LowDiskSpaceTitle2", C0488R.string.LowDiskSpaceTitle2));
        titleTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        titleTextView.setTextSize(1, 16.0f);
        titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        titleTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        linearLayout.addView(titleTextView, LayoutHelper.createLinear(-2, -2, (LocaleController.isRTL ? 5 : 3) | 48, 24, 0, 24, 8));
        int a = 0;
        while (a < descriptions.length) {
            RadioColorCell cell = new RadioColorCell(parentActivity);
            cell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            cell.setTag(Integer.valueOf(a));
            cell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
            cell.setTextAndValue(descriptions[a], selected[0] == a);
            linearLayout.addView(cell);
            cell.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int num = ((Integer) v.getTag()).intValue();
                    if (num == 0) {
                        selected[0] = 3;
                    } else if (num == 1) {
                        selected[0] = 0;
                    } else if (num == 2) {
                        selected[0] = 1;
                    } else if (num == 3) {
                        selected[0] = 2;
                    }
                    int count = linearLayout.getChildCount();
                    for (int a = 0; a < count; a++) {
                        View child = linearLayout.getChildAt(a);
                        if (child instanceof RadioColorCell) {
                            boolean z;
                            RadioColorCell radioColorCell = (RadioColorCell) child;
                            if (child == v) {
                                z = true;
                            } else {
                                z = false;
                            }
                            radioColorCell.setChecked(z, true);
                        }
                    }
                }
            });
            a++;
        }
        Builder builder = new Builder((Context) parentActivity);
        builder.setTitle(LocaleController.getString("LowDiskSpaceTitle", C0488R.string.LowDiskSpaceTitle));
        builder.setMessage(LocaleController.getString("LowDiskSpaceMessage", C0488R.string.LowDiskSpaceMessage));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("OK", C0488R.string.OK), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MessagesController.getGlobalMainSettings().edit().putInt("keep_media", selected[0]).commit();
            }
        });
        final LaunchActivity launchActivity = parentActivity;
        builder.setNeutralButton(LocaleController.getString("ClearMediaCache", C0488R.string.ClearMediaCache), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                launchActivity.presentFragment(new CacheControlActivity());
            }
        });
        return builder.create();
    }

    public static Dialog createPrioritySelectDialog(Activity parentActivity, BaseFragment parentFragment, long dialog_id, boolean globalGroup, boolean globalAll, Runnable onSelect) {
        String[] descriptions;
        SharedPreferences preferences = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        final int[] selected = new int[1];
        if (dialog_id != 0) {
            selected[0] = preferences.getInt("priority_" + dialog_id, 3);
            if (selected[0] == 3) {
                selected[0] = 0;
            } else if (selected[0] == 4) {
                selected[0] = 1;
            } else if (selected[0] == 5) {
                selected[0] = 2;
            } else if (selected[0] == 0) {
                selected[0] = 3;
            } else {
                selected[0] = 4;
            }
            descriptions = new String[]{LocaleController.getString("NotificationsPrioritySettings", C0488R.string.NotificationsPrioritySettings), LocaleController.getString("NotificationsPriorityLow", C0488R.string.NotificationsPriorityLow), LocaleController.getString("NotificationsPriorityMedium", C0488R.string.NotificationsPriorityMedium), LocaleController.getString("NotificationsPriorityHigh", C0488R.string.NotificationsPriorityHigh), LocaleController.getString("NotificationsPriorityUrgent", C0488R.string.NotificationsPriorityUrgent)};
        } else {
            if (globalAll) {
                selected[0] = preferences.getInt("priority_messages", 1);
            } else if (globalGroup) {
                selected[0] = preferences.getInt("priority_group", 1);
            }
            if (selected[0] == 4) {
                selected[0] = 0;
            } else if (selected[0] == 5) {
                selected[0] = 1;
            } else if (selected[0] == 0) {
                selected[0] = 2;
            } else {
                selected[0] = 3;
            }
            descriptions = new String[]{LocaleController.getString("NotificationsPriorityLow", C0488R.string.NotificationsPriorityLow), LocaleController.getString("NotificationsPriorityMedium", C0488R.string.NotificationsPriorityMedium), LocaleController.getString("NotificationsPriorityHigh", C0488R.string.NotificationsPriorityHigh), LocaleController.getString("NotificationsPriorityUrgent", C0488R.string.NotificationsPriorityUrgent)};
        }
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        int a = 0;
        while (a < descriptions.length) {
            RadioColorCell cell = new RadioColorCell(parentActivity);
            cell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            cell.setTag(Integer.valueOf(a));
            cell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
            cell.setTextAndValue(descriptions[a], selected[0] == a);
            linearLayout.addView(cell);
            final long j = dialog_id;
            final boolean z = globalGroup;
            final BaseFragment baseFragment = parentFragment;
            final Runnable runnable = onSelect;
            cell.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    selected[0] = ((Integer) v.getTag()).intValue();
                    Editor editor = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
                    int option;
                    if (j != 0) {
                        if (selected[0] == 0) {
                            option = 3;
                        } else if (selected[0] == 1) {
                            option = 4;
                        } else if (selected[0] == 2) {
                            option = 5;
                        } else if (selected[0] == 3) {
                            option = 0;
                        } else {
                            option = 1;
                        }
                        editor.putInt("priority_" + j, option);
                    } else {
                        String str;
                        if (selected[0] == 0) {
                            option = 4;
                        } else if (selected[0] == 1) {
                            option = 5;
                        } else if (selected[0] == 2) {
                            option = 0;
                        } else {
                            option = 1;
                        }
                        if (z) {
                            str = "priority_group";
                        } else {
                            str = "priority_messages";
                        }
                        editor.putInt(str, option);
                    }
                    editor.commit();
                    if (baseFragment != null) {
                        baseFragment.dismissCurrentDialig();
                    }
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
            a++;
        }
        Builder builder = new Builder((Context) parentActivity);
        builder.setTitle(LocaleController.getString("NotificationsImportance", C0488R.string.NotificationsImportance));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("Cancel", C0488R.string.Cancel), null);
        return builder.create();
    }

    public static Dialog createPopupSelectDialog(Activity parentActivity, final BaseFragment parentFragment, final boolean globalGroup, boolean globalAll, final Runnable onSelect) {
        SharedPreferences preferences = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        final int[] selected = new int[1];
        if (globalAll) {
            selected[0] = preferences.getInt("popupAll", 0);
        } else if (globalGroup) {
            selected[0] = preferences.getInt("popupGroup", 0);
        }
        String[] descriptions = new String[]{LocaleController.getString("NoPopup", C0488R.string.NoPopup), LocaleController.getString("OnlyWhenScreenOn", C0488R.string.OnlyWhenScreenOn), LocaleController.getString("OnlyWhenScreenOff", C0488R.string.OnlyWhenScreenOff), LocaleController.getString("AlwaysShowPopup", C0488R.string.AlwaysShowPopup)};
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        int a = 0;
        while (a < descriptions.length) {
            RadioColorCell cell = new RadioColorCell(parentActivity);
            cell.setTag(Integer.valueOf(a));
            cell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            cell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
            cell.setTextAndValue(descriptions[a], selected[0] == a);
            linearLayout.addView(cell);
            cell.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    selected[0] = ((Integer) v.getTag()).intValue();
                    Editor editor = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
                    editor.putInt(globalGroup ? "popupGroup" : "popupAll", selected[0]);
                    editor.commit();
                    if (parentFragment != null) {
                        parentFragment.dismissCurrentDialig();
                    }
                    if (onSelect != null) {
                        onSelect.run();
                    }
                }
            });
            a++;
        }
        Builder builder = new Builder((Context) parentActivity);
        builder.setTitle(LocaleController.getString("PopupNotification", C0488R.string.PopupNotification));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("Cancel", C0488R.string.Cancel), null);
        return builder.create();
    }

    public static Dialog createSingleChoiceDialog(Activity parentActivity, final BaseFragment parentFragment, String[] options, String title, int selected, final OnClickListener listener) {
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        for (int a = 0; a < options.length; a++) {
            boolean z;
            RadioColorCell cell = new RadioColorCell(parentActivity);
            cell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            cell.setTag(Integer.valueOf(a));
            cell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
            String str = options[a];
            if (selected == a) {
                z = true;
            } else {
                z = false;
            }
            cell.setTextAndValue(str, z);
            linearLayout.addView(cell);
            cell.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int sel = ((Integer) v.getTag()).intValue();
                    if (parentFragment != null) {
                        parentFragment.dismissCurrentDialig();
                    }
                    listener.onClick(null, sel);
                }
            });
        }
        Builder builder = new Builder((Context) parentActivity);
        builder.setTitle(title);
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("Cancel", C0488R.string.Cancel), null);
        return builder.create();
    }

    public static Builder createTTLAlert(Context context, final EncryptedChat encryptedChat) {
        Builder builder = new Builder(context);
        builder.setTitle(LocaleController.getString("MessageLifetime", C0488R.string.MessageLifetime));
        final NumberPicker numberPicker = new NumberPicker(context);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(20);
        if (encryptedChat.ttl > 0 && encryptedChat.ttl < 16) {
            numberPicker.setValue(encryptedChat.ttl);
        } else if (encryptedChat.ttl == 30) {
            numberPicker.setValue(16);
        } else if (encryptedChat.ttl == 60) {
            numberPicker.setValue(17);
        } else if (encryptedChat.ttl == 3600) {
            numberPicker.setValue(18);
        } else if (encryptedChat.ttl == 86400) {
            numberPicker.setValue(19);
        } else if (encryptedChat.ttl == 604800) {
            numberPicker.setValue(20);
        } else if (encryptedChat.ttl == 0) {
            numberPicker.setValue(0);
        }
        numberPicker.setFormatter(new Formatter() {
            public String format(int value) {
                if (value == 0) {
                    return LocaleController.getString("ShortMessageLifetimeForever", C0488R.string.ShortMessageLifetimeForever);
                }
                if (value >= 1 && value < 16) {
                    return LocaleController.formatTTLString(value);
                }
                if (value == 16) {
                    return LocaleController.formatTTLString(30);
                }
                if (value == 17) {
                    return LocaleController.formatTTLString(60);
                }
                if (value == 18) {
                    return LocaleController.formatTTLString(3600);
                }
                if (value == 19) {
                    return LocaleController.formatTTLString(86400);
                }
                if (value == 20) {
                    return LocaleController.formatTTLString(604800);
                }
                return TtmlNode.ANONYMOUS_REGION_ID;
            }
        });
        builder.setView(numberPicker);
        builder.setNegativeButton(LocaleController.getString("Done", C0488R.string.Done), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int oldValue = encryptedChat.ttl;
                which = numberPicker.getValue();
                if (which >= 0 && which < 16) {
                    encryptedChat.ttl = which;
                } else if (which == 16) {
                    encryptedChat.ttl = 30;
                } else if (which == 17) {
                    encryptedChat.ttl = 60;
                } else if (which == 18) {
                    encryptedChat.ttl = 3600;
                } else if (which == 19) {
                    encryptedChat.ttl = 86400;
                } else if (which == 20) {
                    encryptedChat.ttl = 604800;
                }
                if (oldValue != encryptedChat.ttl) {
                    SecretChatHelper.getInstance(UserConfig.selectedAccount).sendTTLMessage(encryptedChat, null);
                    MessagesStorage.getInstance(UserConfig.selectedAccount).updateEncryptedChatTTL(encryptedChat);
                }
            }
        });
        return builder;
    }
}
