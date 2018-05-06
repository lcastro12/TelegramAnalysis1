package net.hockeyapp.android.utils;

public class FeedbackParser {

    private static class FeedbackParserHolder {
        public static final FeedbackParser INSTANCE = new FeedbackParser();

        private FeedbackParserHolder() {
        }
    }

    private FeedbackParser() {
    }

    public static FeedbackParser getInstance() {
        return FeedbackParserHolder.INSTANCE;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public net.hockeyapp.android.objects.FeedbackResponse parseFeedbackResponse(java.lang.String r28) {
        /*
        r27 = this;
        r9 = 0;
        r5 = 0;
        if (r28 == 0) goto L_0x0198;
    L_0x0004:
        r13 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x01c1 }
        r0 = r28;
        r13.<init>(r0);	 Catch:{ JSONException -> 0x01c1 }
        r25 = "feedback";
        r0 = r25;
        r8 = r13.getJSONObject(r0);	 Catch:{ JSONException -> 0x01c1 }
        r6 = new net.hockeyapp.android.objects.Feedback;	 Catch:{ JSONException -> 0x01c1 }
        r6.<init>();	 Catch:{ JSONException -> 0x01c1 }
        r25 = "messages";
        r0 = r25;
        r15 = r8.getJSONArray(r0);	 Catch:{ JSONException -> 0x019e }
        r14 = 0;
        r7 = 0;
        r25 = r15.length();	 Catch:{ JSONException -> 0x019e }
        if (r25 <= 0) goto L_0x0129;
    L_0x0028:
        r14 = new java.util.ArrayList;	 Catch:{ JSONException -> 0x019e }
        r14.<init>();	 Catch:{ JSONException -> 0x019e }
        r11 = 0;
    L_0x002e:
        r25 = r15.length();	 Catch:{ JSONException -> 0x019e }
        r0 = r25;
        if (r11 >= r0) goto L_0x0129;
    L_0x0036:
        r25 = r15.getJSONObject(r11);	 Catch:{ JSONException -> 0x019e }
        r26 = "subject";
        r25 = r25.getString(r26);	 Catch:{ JSONException -> 0x019e }
        r20 = r25.toString();	 Catch:{ JSONException -> 0x019e }
        r25 = r15.getJSONObject(r11);	 Catch:{ JSONException -> 0x019e }
        r26 = "text";
        r25 = r25.getString(r26);	 Catch:{ JSONException -> 0x019e }
        r21 = r25.toString();	 Catch:{ JSONException -> 0x019e }
        r25 = r15.getJSONObject(r11);	 Catch:{ JSONException -> 0x019e }
        r26 = "oem";
        r25 = r25.getString(r26);	 Catch:{ JSONException -> 0x019e }
        r18 = r25.toString();	 Catch:{ JSONException -> 0x019e }
        r25 = r15.getJSONObject(r11);	 Catch:{ JSONException -> 0x019e }
        r26 = "model";
        r25 = r25.getString(r26);	 Catch:{ JSONException -> 0x019e }
        r16 = r25.toString();	 Catch:{ JSONException -> 0x019e }
        r25 = r15.getJSONObject(r11);	 Catch:{ JSONException -> 0x019e }
        r26 = "os_version";
        r25 = r25.getString(r26);	 Catch:{ JSONException -> 0x019e }
        r19 = r25.toString();	 Catch:{ JSONException -> 0x019e }
        r25 = r15.getJSONObject(r11);	 Catch:{ JSONException -> 0x019e }
        r26 = "created_at";
        r25 = r25.getString(r26);	 Catch:{ JSONException -> 0x019e }
        r3 = r25.toString();	 Catch:{ JSONException -> 0x019e }
        r25 = r15.getJSONObject(r11);	 Catch:{ JSONException -> 0x019e }
        r26 = "id";
        r12 = r25.getInt(r26);	 Catch:{ JSONException -> 0x019e }
        r25 = r15.getJSONObject(r11);	 Catch:{ JSONException -> 0x019e }
        r26 = "token";
        r25 = r25.getString(r26);	 Catch:{ JSONException -> 0x019e }
        r22 = r25.toString();	 Catch:{ JSONException -> 0x019e }
        r25 = r15.getJSONObject(r11);	 Catch:{ JSONException -> 0x019e }
        r26 = "via";
        r24 = r25.getInt(r26);	 Catch:{ JSONException -> 0x019e }
        r25 = r15.getJSONObject(r11);	 Catch:{ JSONException -> 0x019e }
        r26 = "user_string";
        r25 = r25.getString(r26);	 Catch:{ JSONException -> 0x019e }
        r23 = r25.toString();	 Catch:{ JSONException -> 0x019e }
        r25 = r15.getJSONObject(r11);	 Catch:{ JSONException -> 0x019e }
        r26 = "clean_text";
        r25 = r25.getString(r26);	 Catch:{ JSONException -> 0x019e }
        r2 = r25.toString();	 Catch:{ JSONException -> 0x019e }
        r25 = r15.getJSONObject(r11);	 Catch:{ JSONException -> 0x019e }
        r26 = "name";
        r25 = r25.getString(r26);	 Catch:{ JSONException -> 0x019e }
        r17 = r25.toString();	 Catch:{ JSONException -> 0x019e }
        r25 = r15.getJSONObject(r11);	 Catch:{ JSONException -> 0x019e }
        r26 = "app_id";
        r25 = r25.getString(r26);	 Catch:{ JSONException -> 0x019e }
        r1 = r25.toString();	 Catch:{ JSONException -> 0x019e }
        r7 = new net.hockeyapp.android.objects.FeedbackMessage;	 Catch:{ JSONException -> 0x019e }
        r7.<init>();	 Catch:{ JSONException -> 0x019e }
        r7.setAppId(r1);	 Catch:{ JSONException -> 0x019e }
        r7.setCleanText(r2);	 Catch:{ JSONException -> 0x019e }
        r7.setCreatedAt(r3);	 Catch:{ JSONException -> 0x019e }
        r7.setId(r12);	 Catch:{ JSONException -> 0x019e }
        r0 = r16;
        r7.setModel(r0);	 Catch:{ JSONException -> 0x019e }
        r0 = r17;
        r7.setName(r0);	 Catch:{ JSONException -> 0x019e }
        r0 = r18;
        r7.setOem(r0);	 Catch:{ JSONException -> 0x019e }
        r0 = r19;
        r7.setOsVersion(r0);	 Catch:{ JSONException -> 0x019e }
        r0 = r20;
        r7.setSubjec(r0);	 Catch:{ JSONException -> 0x019e }
        r0 = r21;
        r7.setText(r0);	 Catch:{ JSONException -> 0x019e }
        r0 = r22;
        r7.setToken(r0);	 Catch:{ JSONException -> 0x019e }
        r0 = r23;
        r7.setUserString(r0);	 Catch:{ JSONException -> 0x019e }
        r0 = r24;
        r7.setVia(r0);	 Catch:{ JSONException -> 0x019e }
        r14.add(r7);	 Catch:{ JSONException -> 0x019e }
        r11 = r11 + 1;
        goto L_0x002e;
    L_0x0129:
        r6.setMessages(r14);	 Catch:{ JSONException -> 0x019e }
        r25 = "name";
        r0 = r25;
        r25 = r8.getString(r0);	 Catch:{ JSONException -> 0x0199 }
        r25 = r25.toString();	 Catch:{ JSONException -> 0x0199 }
        r0 = r25;
        r6.setName(r0);	 Catch:{ JSONException -> 0x0199 }
    L_0x013d:
        r25 = "email";
        r0 = r25;
        r25 = r8.getString(r0);	 Catch:{ JSONException -> 0x01a4 }
        r25 = r25.toString();	 Catch:{ JSONException -> 0x01a4 }
        r0 = r25;
        r6.setEmail(r0);	 Catch:{ JSONException -> 0x01a4 }
    L_0x014e:
        r25 = "id";
        r0 = r25;
        r25 = r8.getInt(r0);	 Catch:{ JSONException -> 0x01a9 }
        r0 = r25;
        r6.setId(r0);	 Catch:{ JSONException -> 0x01a9 }
    L_0x015b:
        r25 = "created_at";
        r0 = r25;
        r25 = r8.getString(r0);	 Catch:{ JSONException -> 0x01ae }
        r25 = r25.toString();	 Catch:{ JSONException -> 0x01ae }
        r0 = r25;
        r6.setCreatedAt(r0);	 Catch:{ JSONException -> 0x01ae }
    L_0x016c:
        r10 = new net.hockeyapp.android.objects.FeedbackResponse;	 Catch:{ JSONException -> 0x019e }
        r10.<init>();	 Catch:{ JSONException -> 0x019e }
        r10.setFeedback(r6);	 Catch:{ JSONException -> 0x01b8 }
        r25 = "status";
        r0 = r25;
        r25 = r13.getString(r0);	 Catch:{ JSONException -> 0x01b3 }
        r25 = r25.toString();	 Catch:{ JSONException -> 0x01b3 }
        r0 = r25;
        r10.setStatus(r0);	 Catch:{ JSONException -> 0x01b3 }
    L_0x0185:
        r25 = "token";
        r0 = r25;
        r25 = r13.getString(r0);	 Catch:{ JSONException -> 0x01bc }
        r25 = r25.toString();	 Catch:{ JSONException -> 0x01bc }
        r0 = r25;
        r10.setToken(r0);	 Catch:{ JSONException -> 0x01bc }
    L_0x0196:
        r5 = r6;
        r9 = r10;
    L_0x0198:
        return r9;
    L_0x0199:
        r4 = move-exception;
        r4.printStackTrace();	 Catch:{ JSONException -> 0x019e }
        goto L_0x013d;
    L_0x019e:
        r4 = move-exception;
        r5 = r6;
    L_0x01a0:
        r4.printStackTrace();
        goto L_0x0198;
    L_0x01a4:
        r4 = move-exception;
        r4.printStackTrace();	 Catch:{ JSONException -> 0x019e }
        goto L_0x014e;
    L_0x01a9:
        r4 = move-exception;
        r4.printStackTrace();	 Catch:{ JSONException -> 0x019e }
        goto L_0x015b;
    L_0x01ae:
        r4 = move-exception;
        r4.printStackTrace();	 Catch:{ JSONException -> 0x019e }
        goto L_0x016c;
    L_0x01b3:
        r4 = move-exception;
        r4.printStackTrace();	 Catch:{ JSONException -> 0x01b8 }
        goto L_0x0185;
    L_0x01b8:
        r4 = move-exception;
        r5 = r6;
        r9 = r10;
        goto L_0x01a0;
    L_0x01bc:
        r4 = move-exception;
        r4.printStackTrace();	 Catch:{ JSONException -> 0x01b8 }
        goto L_0x0196;
    L_0x01c1:
        r4 = move-exception;
        goto L_0x01a0;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.utils.FeedbackParser.parseFeedbackResponse(java.lang.String):net.hockeyapp.android.objects.FeedbackResponse");
    }
}
